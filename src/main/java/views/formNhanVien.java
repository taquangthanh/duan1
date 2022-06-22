package views;


import dao.Log;
import dao.RendererHighlighted;
import model.NhanVien;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import java.awt.event.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.SQLException;
import java.util.logging.Level;

public class formNhanVien extends JFrame {
    private String user;
    private int role;
    private JPanel mainPanel;
    private JTabbedPane tabbedPane1;
    private JTable tblNhanVien;
    private JTextField txtHoTen;
    private JTextField txtDiaChi;
    private JTextField txtSDT;
    private JTextField txtEmail;
    private JPasswordField txtMk;
    private JRadioButton rdbNV;
    private JRadioButton rdbChu;
    private JButton btnThem;
    private JButton btnSua;
    private JButton btnXoa;
    private JButton btnMoi;
    private JTextField txtMaNV;
    private JTextField txtTimKiem;
    private JButton btnTBLXoa;
    private JPanel pnlQuanLi;
    private JPanel pnlDanhSach;
    DefaultTableModel _dtm;
    serviceNhanVien _list = new serviceNhanVien();
    boolean check = false;

    public formNhanVien() throws SQLException, IOException {
        tabbedPane1.setSelectedIndex(1);
        this.setTitle("Quản lí nhân viên");
        this.setContentPane(mainPanel);
        this.setSize(700, 500);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(2);
        this.setResizable(false); // chống chỉnh sửa size frame
        this.setVisible(true);
        _dtm = (DefaultTableModel) tblNhanVien.getModel();
        _dtm.setColumnIdentifiers(new String[]{
                "Mã Nhân Viên", "Họ Tên", "Địa Chỉ", "Số Điện Thoại", "Email", "Mật Khẩu", "Chức Vụ"
        });
        tblNhanVien.setModel(_dtm);
        loadtbl();
        rdbChu.setSelected(true);
        RendererHighlighted renderer = new RendererHighlighted(txtTimKiem);
        tblNhanVien.setDefaultEditor(Object.class, null);
        TableRowSorter<TableModel> rowSorter
                = new TableRowSorter<>(tblNhanVien.getModel());

        tblNhanVien.setRowSorter(rowSorter);
        tblNhanVien.setDefaultRenderer(Object.class, renderer);


        // mở chương trình và lưu giá trị
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowOpened(WindowEvent e) {
                luuText();
            }
        });

        //tắt chương trình quay lại form chính
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                formChinh formChinh = null;
                try {
                    formChinh = new formChinh();
                } catch (IOException ex) {
                    try {
                        baoLoi(ex);
                    } catch (IOException exc) {
                        exc.printStackTrace();
                    }
                }
                formChinh.setUser(user);
                formChinh.setRole(role);
                dispose();
            }
        });

        // nút thêm nhân viên
        btnThem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (txtMaNV.getText().isBlank() || txtMaNV.getText().isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Mã Nhân Viên Không Được Để Trống");
                    xoaForm();
                    return;
                }

                if (check) {
                    try {
                        JOptionPane.showMessageDialog(null, _list.themLai(txtMaNV.getText()));
                        loadtblXoa();
                        xoaForm();
                    } catch (SQLException ex) {
                        try {
                            baoLoi(ex);
                        } catch (IOException exc) {
                            exc.printStackTrace();
                        }
                    }
                    return;
                }
                if (loi()) {
                    try {
                        JOptionPane.showMessageDialog(null, _list.addNV(nv()));
                        loadtbl();
                        xoaForm();
                    } catch (SQLException ex) {
                        try {
                            baoLoi(ex);
                        } catch (IOException exc) {
                            exc.printStackTrace();
                        }
                    }
                }
            }
        });

        // mouse click nhân viên từ tbl lên form
        tblNhanVien.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int i = tblNhanVien.getSelectedRow();
                try {
                    NhanVien nv = _list.getlist().get(i);
                    txtMk.setText(nv.getMatkhau());
                } catch (SQLException throwables) {
                    try {
                        baoLoi(throwables);
                    } catch (IOException exc) {
                        exc.printStackTrace();
                    }
                }
                txtMaNV.setText(String.valueOf(tblNhanVien.getValueAt(i, 0)));
                txtHoTen.setText(String.valueOf(tblNhanVien.getValueAt(i, 1)));
                txtDiaChi.setText(String.valueOf(tblNhanVien.getValueAt(i, 2)));
                txtSDT.setText(String.valueOf(tblNhanVien.getValueAt(i, 3)));
                txtEmail.setText(String.valueOf(tblNhanVien.getValueAt(i, 4)));
                txtMk.setText(String.valueOf(tblNhanVien.getValueAt(i, 5)));
                if (String.valueOf(tblNhanVien.getValueAt(i, 6)).equalsIgnoreCase("Chủ")) {
                    rdbChu.setSelected(true);
                } else {
                    rdbNV.setSelected(true);
                }


            }
        });

        // nút xóa form
        btnMoi.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                xoaForm();
            }
        });

        // nút sửa nhân viên
        btnSua.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (loi()) {
                    try {
                        JOptionPane.showMessageDialog(null, _list.updateNV(nv()));
                        xoaForm();
                        loadtbl();
                    } catch (SQLException ex) {
                        try {
                            baoLoi(ex);
                        } catch (IOException exc) {
                            exc.printStackTrace();
                        }
                    }
                }
            }
        });


        // nút xóa nhân viên
        btnXoa.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                String input = JOptionPane.showInputDialog("Mời bạn nhập mã nhân viên cần xóa");
                if(check){
                    try {
                        if(_list.boNhanVien(input)){
                            JOptionPane.showMessageDialog(null, "Xóa nhân viên thành công");
                            loadtblXoa();
                            return;
                        }else {
                            JOptionPane.showMessageDialog(null, "Nhân viên này có liên quan tới các hóa đơn nên không thể xóa");
                            return;
                        }
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                    }
                }
                try {
                    JOptionPane.showMessageDialog(null, _list.deleteNV(input));
                    xoaForm();
                    loadtbl();
                } catch (SQLException ex) {
                    try {
                        baoLoi(ex);
                    } catch (IOException exc) {
                        exc.printStackTrace();
                    }
                }

            }
        });

        // nút hiện tbl xóa
        btnTBLXoa.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!check) {
                    try {
                        loadtblXoa();
                    } catch (SQLException ex) {
                        try {
                            baoLoi(ex);
                        } catch (IOException exc) {
                            exc.printStackTrace();
                        }
                    }
                    check = true;
                    JOptionPane.showMessageDialog(null, "Đã hiện thị những nhân viên bị xóa");
                    btnTBLXoa.setText("Hiện Thị Lại Nhân Viên Đang Làm");
                    btnThem.setText("Thêm Lại");
                    btnSua.setEnabled(false);
                    txtHoTen.setEnabled(false);
                    txtEmail.setEnabled(false);
                    txtMk.setEnabled(false);
                    txtDiaChi.setEnabled(false);
                    txtSDT.setEnabled(false);
                    return;
                }


                if (check) {
                    try {
                        loadtbl();
                    } catch (SQLException ex) {
                        try {
                            baoLoi(ex);
                        } catch (IOException exc) {
                            exc.printStackTrace();
                        }
                    }
                    JOptionPane.showMessageDialog(null, "Đã hiện thị những nhân viên đang làm");
                    btnTBLXoa.setText("Hiện Thị Những Nhân Viên Đã Xóa");
                    check = false;
                    btnThem.setText("Thêm");
                    btnSua.setEnabled(true);
                    txtHoTen.setEnabled(true);
                    txtEmail.setEnabled(true);
                    txtMk.setEnabled(true);
                    txtDiaChi.setEnabled(true);
                    txtSDT.setEnabled(true);
                }
            }
        });


        // nút tìm kiếm
        txtTimKiem.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                String text = txtTimKiem.getText();
                if (text.trim().length() == 0) {
                    rowSorter.setRowFilter(null);
                } else {
                    rowSorter.setRowFilter(RowFilter.regexFilter("(?i)" + text));
                }
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                String text = txtTimKiem.getText();
                if (text.trim().length() == 0) {
                    rowSorter.setRowFilter(null);
                } else {
                    rowSorter.setRowFilter(RowFilter.regexFilter("(?i)" + text));
                }
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                throw new UnsupportedOperationException("Not supported yet.");
            }
        });

    }


    // Load dữ liệu lên table
    private void loadtbl() throws SQLException {
        _dtm = (DefaultTableModel) tblNhanVien.getModel();
        while (_dtm.getRowCount() > 0) {
            _dtm.setRowCount(0);
        }
        for (NhanVien nv : _list.getlist()
        ) {
            _dtm.addRow(new Object[]{
                    nv.getManv(), nv.getHoten(), nv.getDaichi(), nv.getSdt(), nv.getEmail(), nv.getMatkhau(), nv.getRole() == 1 ? "Chủ" : "Nhân Viên"});
        }
    }


    // Load table những nhân viên đã xóa
    private void loadtblXoa() throws SQLException {
        _dtm = (DefaultTableModel) tblNhanVien.getModel();
        while (_dtm.getRowCount() > 0) {
            _dtm.setRowCount(0);
        }
        for (NhanVien nv : _list.getlistXoa()
        ) {
            _dtm.addRow(new Object[]{
                    nv.getManv(), nv.getHoten(), nv.getDaichi(), nv.getSdt(), nv.getEmail(), nv.getMatkhau(), nv.getRole() == 1 ? "Chủ" : "Nhân Viên"});
        }
    }

    //Phương thức đọc form lấy ra nhân viên
    private NhanVien nv() {
        return new NhanVien(txtMaNV.getText(), txtHoTen.getText(), txtDiaChi.getText(), txtSDT.getText(), txtEmail.getText(), String.valueOf(txtMk.getPassword()), (rdbChu.isSelected() ? 1 : 0));
    }

    // Xóa Form
    private void xoaForm() {
        txtEmail.setText("");
        txtMaNV.setText("");
        txtHoTen.setText("");
        txtDiaChi.setText("");
        txtSDT.setText("");
        txtMk.setText("");
        rdbChu.setSelected(true);
    }


    // Phương thức check lỗi trên form
    private boolean loi() {
        if(txtMaNV.getText().isEmpty() || txtMaNV.getText().isBlank()){
            JOptionPane.showMessageDialog(null, "Mã nhân viên không được để trống", "lỗi", 2);
            return false;
        }


        if (!txtMaNV.getText().matches("[0-9a-zA-Z]{1,}")) {
            JOptionPane.showMessageDialog(null, "Mã nhân viên vui lòng là chữ la tinh hoặc số", "Lỗi", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        if (txtHoTen.getText().isEmpty() || txtHoTen.getText().isBlank()) {
            JOptionPane.showMessageDialog(null, "Tên không được để trống", "Cảnh Báo", 2);
            txtHoTen.requestFocus();
            return false;
        }

        if (!txtHoTen.getText().matches("[^0-9!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>\\/?]{1,}")) {
            JOptionPane.showMessageDialog(null, "Tên vui lòng chữ cái", "Lỗi", JOptionPane.WARNING_MESSAGE);
            return false;
        }

        if (txtDiaChi.getText().isEmpty() || txtDiaChi.getText().isBlank()) {
            JOptionPane.showMessageDialog(null, "Địa chỉ không được để trống", "Cảnh Báo", 2);
            txtDiaChi.requestFocus();
            return false;
        }

        if (!txtDiaChi.getText().matches("[^0-9!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>\\/?]{1,}")) {
            JOptionPane.showMessageDialog(null, "Địa chỉ vui lòng chữ cái", "Lỗi", JOptionPane.WARNING_MESSAGE);
            return false;
        }


        if (txtSDT.getText().isEmpty() || txtSDT.getText().isBlank()) {
            JOptionPane.showMessageDialog(null, "Sđt không được để trống", "Cảnh Báo", 2);
            txtSDT.requestFocus();
            return false;
        }

        if (!txtSDT.getText().matches("0[0-9]{9}")) {
            JOptionPane.showMessageDialog(null, "Bạn đã nhập sai sđt", "Lỗi", JOptionPane.WARNING_MESSAGE);
            return false;
        }

        if (txtEmail.getText().isEmpty() || txtEmail.getText().isBlank()) {
            JOptionPane.showMessageDialog(null, "Email không được để trống", "Cảnh Báo", 2);
            txtEmail.requestFocus();
            return false;
        }
        if (!txtEmail.getText().matches("^[a-zA-Z][\\w-]+@([\\w]+\\.[\\w]+|[\\w]+\\.[\\w]{2,}\\.[\\w]{2,})$")) {
            JOptionPane.showMessageDialog(null, "Email sai định dạng", "Cảnh Báo", 2);
            txtEmail.requestFocus();
            return false;
        }

        if(String.valueOf(txtMk.getPassword()).isEmpty() || String.valueOf(txtMk.getPassword()).isBlank()){
            JOptionPane.showMessageDialog(null, "mật khẩu nhân viên không được để trống", "lỗi", 2);
            return false;
        }


        if (!String.valueOf(txtMk.getPassword()).matches("[0-9a-zA-Z]{1,}")) {
            JOptionPane.showMessageDialog(null, "mật khẩu nhân viên vui lòng là chữ la tinh hoặc số", "Lỗi", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        return true;
    }

    // Phương thức set giá trị cho 2 biến phân quyền
    public void setUser(String user) {
        this.user = user;
    }

    public void setRole(int role) {
        this.role = role;
    }

    private void baoLoi(Exception ex) throws IOException {
        Log log = new Log("hieupro.txt");
        log.logger.setLevel(Level.WARNING);
        JOptionPane.showMessageDialog(null, "gặp lỗi rồi! Quay lại để gửi lỗi cho admin nha");
        log.logger.info(ex.getMessage());
        log.logger.warning("lỗi bên form nhân viên");

        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        ex.printStackTrace(pw);
        String sStackTrace = sw.toString(); // stack trace as a string
        log.logger.severe(sStackTrace);
    }

    // đọc dữ liệu phân quyền lên form
    private void luuText() {

        System.out.println(user + " bên form nhân viên");
    }


    public static void main(String[] args) throws SQLException, IOException {
        new formNhanVien();
    }

}
