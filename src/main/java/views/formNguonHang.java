package views;

import dao.Log;
import dao.RendererHighlighted;
import dao.serviceNguonHang;
import model.NguonHang;

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

public class formNguonHang extends JFrame {
    private String user;
    private int role;
    private JPanel mainPanel;
    private JTabbedPane tabbedPane1;
    private JTextField txtHoTen;
    private JTextField txtDiaChi;
    private JTextField txtSDT;
    private JTable tblNguonHang;
    private JTextField txtTimKiem;
    private JButton btnThem;
    private JButton btnSua;
    private JButton btnXoa;
    private JButton btnMoi;
    private JButton btnTblXoa;
    DefaultTableModel _dtm;
    serviceNguonHang _list = new serviceNguonHang();
    boolean check = false;

    public formNguonHang() throws SQLException, IOException {
        tabbedPane1.setSelectedIndex(1);
        this.setTitle("Nguồn Hàng");
        this.setContentPane(mainPanel);
        this.setSize(500, 300);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(2);
        this.setResizable(false); // chống chỉnh sửa size frame
        this.setVisible(true);
        _dtm = (DefaultTableModel) tblNguonHang.getModel();
        _dtm.setColumnIdentifiers(new String[]{
                "Tên Nguồn Hàng", "Địa Chỉ", "Số Điện Thoại"
        });
        tblNguonHang.setModel(_dtm);
        loadtbl();

        RendererHighlighted renderer = new RendererHighlighted(txtTimKiem);
        tblNguonHang.setDefaultEditor(Object.class, null);
        TableRowSorter<TableModel> rowSorter
                = new TableRowSorter<>(tblNguonHang.getModel());
        tblNguonHang.setRowSorter(rowSorter);
        tblNguonHang.setDefaultRenderer(Object.class, renderer);

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


        // thêm nguồn hàng
        btnThem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (check) {
                    int i = tblNguonHang.getSelectedRow();
                    try {
                        NguonHang nguonHang = _list.getlistXoa().get(i);
                        JOptionPane.showMessageDialog(null, _list.themLai(nguonHang.getId()));
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
                        JOptionPane.showMessageDialog(null, _list.themNguonHang(nguonHang()));
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

        // nút cho nguồn hàng vào thùng rác
        btnXoa.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (loi()){
                    int i = tblNguonHang.getSelectedRow();
                    try {
                        NguonHang nguonHang = _list.getlist().get(i);
                        JOptionPane.showMessageDialog(null, _list.deleteNH(nguonHang.getId()));
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


        // mouse click đẩy từ tbl lên form
        tblNguonHang.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int i = tblNguonHang.getSelectedRow();
                System.out.println(i);
                txtHoTen.setText(String.valueOf(tblNguonHang.getValueAt(i, 0)));
                txtDiaChi.setText(String.valueOf(tblNguonHang.getValueAt(i, 1)));
                txtSDT.setText(String.valueOf(tblNguonHang.getValueAt(i, 2)));
            }
        });

        // nút xóa form
        btnMoi.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                xoaForm();
            }
        });
        btnSua.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int i = tblNguonHang.getSelectedRow();
                try {
                    NguonHang nguonHang = _list.getlist().get(i);
                    JOptionPane.showMessageDialog(null, _list.updateNH(nguonHang(), nguonHang.getId()));
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
        });



    }

    // khởi tạo 1 giá trị nguồn hàng
    private NguonHang nguonHang() {
        return new NguonHang(1, txtHoTen.getText(), txtDiaChi.getText(), txtSDT.getText());
    }


    // xóa form
    private void xoaForm() {
        txtHoTen.setText("");
        txtDiaChi.setText("");
        txtSDT.setText("");
    }

    // Load dữ liệu lên table
    private void loadtbl() throws SQLException {
        _dtm = (DefaultTableModel) tblNguonHang.getModel();
        while (_dtm.getRowCount() > 0) {
            _dtm.setRowCount(0);
        }
        for (NguonHang nv : _list.getlist()
        ) {
            _dtm.addRow(new Object[]{
                    nv.getTenNguonHang(), nv.getDiaChi(), nv.getSdt()});
        }
    }


    // Load dữ liệu lên table xóa
    private void loadtblXoa() throws SQLException {
        _dtm = (DefaultTableModel) tblNguonHang.getModel();
        while (_dtm.getRowCount() > 0) {
            _dtm.setRowCount(0);
        }
        for (NguonHang nv : _list.getlistXoa()
        ) {
            _dtm.addRow(new Object[]{
                    nv.getTenNguonHang(), nv.getDiaChi(), nv.getSdt()});
        }
    }

    private void baoLoi(Exception ex) throws IOException {
        Log log = new Log("hieupro.txt");
        JOptionPane.showMessageDialog(null, "gặp lỗi rồi! Quay lại để gửi lỗi cho admin nha");
        log.logger.setLevel(Level.WARNING);
        log.logger.info(ex.getMessage());
        log.logger.warning("Lỗi ở form nguồn hàng");
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        ex.printStackTrace(pw);
        String sStackTrace = sw.toString(); // stack trace as a string
        log.logger.severe(sStackTrace);
    }

    // Phương thức set giá trị cho 2 biến phân quyền
    public void setUser(String user) {
        this.user = user;
    }

    public void setRole(int role) {
        this.role = role;
    }


    // đọc dữ liệu phân quyền lên form
    private void luuText() {
        System.out.println(user + " bên form nguồn hàng");
    }


    // phương thức check lỗi

    private boolean loi() {
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

        if (!txtDiaChi.getText().matches("[^!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>\\/?]{1,}")) {
            JOptionPane.showMessageDialog(null, "Địa chỉ vui lòng chữ cái", "Lỗi", JOptionPane.WARNING_MESSAGE);
            return false;
        }


        if (txtSDT.getText().isEmpty() || txtSDT.getText().isBlank()) {
            JOptionPane.showMessageDialog(null, "Sđt không được để trống", "Cảnh Báo", 2);
            txtSDT.requestFocus();
            return false;
        }

        if (!txtSDT.getText().matches("0[0-9]{1,}")) {
            JOptionPane.showMessageDialog(null, "Bạn đã nhập sai sđt", "Lỗi", JOptionPane.WARNING_MESSAGE);
            return false;
        }


        return true;
    }

    public static void main(String[] args) throws SQLException, IOException {
        new formNguonHang();
    }

}
