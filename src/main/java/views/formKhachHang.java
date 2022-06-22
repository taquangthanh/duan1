package views;

import dao.Log;
import model.KhachHang;
import dao.serviceKhachHang;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.SQLException;
import java.util.logging.Level;

public class formKhachHang extends JFrame {
    private String user;
    private int role;
    private JPanel mainPanel;
    private JTabbedPane tabbedPane1;
    private JTextField txtSdt;
    private JTextField txtName;
    private JTable tblKhachHang;
    private JButton btnSua;
    private JButton btnThem;
    private JButton btnMoi;
    DefaultTableModel _dtm;
    serviceKhachHang serviceKhachHang = new serviceKhachHang();

    public formKhachHang() throws SQLException, IOException {

        this.setTitle("Thượng đế của cửa hàng");
        this.setContentPane(mainPanel);
        this.setSize(500, 300);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(2);
        this.setResizable(false); // chống chỉnh sửa size frame
        this.setVisible(true);
        _dtm = (DefaultTableModel) tblKhachHang.getModel();
        _dtm.setColumnIdentifiers(new String[]{
                "Họ tên", "Số Điện Thoại", "Điểm tích lũy"
        });
        tblKhachHang.setModel(_dtm);
        loadtbl();

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

        // nút click khách hàng đẩy từ tbl lên form
        tblKhachHang.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int i = tblKhachHang.getSelectedRow();

                txtName.setText(String.valueOf(tblKhachHang.getValueAt(i, 0)));
                txtSdt.setText(String.valueOf(tblKhachHang.getValueAt(i, 1)));

            }
        });

        // nút thêm khách hàng
        btnThem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                KhachHang khachHang = new KhachHang(1, txtName.getText(), txtSdt.getText(), 0);
                if (loi()) {
                    try {
                        JOptionPane.showMessageDialog(null, serviceKhachHang.themKhachHang(khachHang));
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

        // nút xóa form
        btnMoi.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                xoaForm();
            }
        });

        // nút sửa khách hàng
        btnSua.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int i = tblKhachHang.getSelectedRow();
                try {
                    KhachHang khachHang = serviceKhachHang.getlist().get(i);
                    KhachHang khachHang1 = new KhachHang(khachHang.getId(), txtName.getText(), txtSdt.getText(), khachHang.getDiem());
                    JOptionPane.showMessageDialog(null, serviceKhachHang.suaKhachHang(khachHang1));
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

    // Load dữ liệu lên table
    private void loadtbl() throws SQLException {
        _dtm = (DefaultTableModel) tblKhachHang.getModel();
        while (_dtm.getRowCount() > 0) {
            _dtm.setRowCount(0);
        }
        for (KhachHang nv : serviceKhachHang.getlist()
        ) {
            _dtm.addRow(new Object[]{
                    nv.getTen(), nv.getSdt(), nv.getDiem()});
        }
    }

    // phương thức check lỗi
    private boolean loi() {
//        if (txtName.getText().isEmpty() || txtName.getText().isBlank()) {
//            JOptionPane.showMessageDialog(null, "Tên không được để trống", "Cảnh Báo", 2);
//            txtName.requestFocus();
//            return false;
//        }
//
//        if (!txtName.getText().matches("[^0-9!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>\\/?]{1,}")) {
//            JOptionPane.showMessageDialog(null, "Tên vui lòng chữ cái", "Lỗi", JOptionPane.WARNING_MESSAGE);
//            return false;
//        }
//
//        if (txtDiaChi.getText().isEmpty() || txtDiaChi.getText().isBlank()) {
//            JOptionPane.showMessageDialog(null, "Địa chỉ không được để trống", "Cảnh Báo", 2);
//            txtDiaChi.requestFocus();
//            return false;
//        }
//
//        if (!txtDiaChi.getText().matches("[^0-9!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>\\/?]{1,}")) {
//            JOptionPane.showMessageDialog(null, "Địa chỉ vui lòng chữ cái", "Lỗi", JOptionPane.WARNING_MESSAGE);
//            return false;
//        }
//        if (txtSdt.getText().isEmpty() || txtSdt.getText().isBlank()) {
//            JOptionPane.showMessageDialog(null, "Sđt không được để trống", "Cảnh Báo", 2);
//            txtSdt.requestFocus();
//            return false;
//        }
//
//        if (!txtSdt.getText().matches("0[0-9]{10}")) {
//            JOptionPane.showMessageDialog(null, "Bạn đã nhập sai sđt", "Lỗi", JOptionPane.WARNING_MESSAGE);
//            return false;
//        }

        return true;
    }

    private void xoaForm() {
        txtName.setText("");
        txtSdt.setText("");
    }


    // set quyền + báo lỗi form
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
        log.logger.warning("lỗi bên form khách hàng");

        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        ex.printStackTrace(pw);
        String sStackTrace = sw.toString(); // stack trace as a string
        log.logger.severe(sStackTrace);
    }

    // đọc dữ liệu phân quyền lên form
    private void luuText() {
        System.out.println(user + " bên form khách hàng");
    }


    public static void main(String[] args) throws SQLException, IOException {
        new formKhachHang();
    }

}
