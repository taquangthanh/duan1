package views;

import javax.swing.*;

import dao.Log;
import dao.serviceNhanVien;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.SQLException;
import java.util.logging.Level;

public class formDoiMatKhau extends JFrame {
    private String user;
    private int role;
    private JPasswordField txtPassOld;
    private JPasswordField txtPassNew;
    private JPasswordField txtConfirm;
    private JButton btnDoiPass;
    private JButton btnThoat;
    private JPanel mainPanel;
    private JLabel lblTitle;
    serviceNhanVien serviceNhanVienn = new serviceNhanVien();


    public formDoiMatKhau() throws IOException {
        Log log = new Log("hieupro.txt");
        this.setTitle("Đổi Mật Khẩu");
        this.setContentPane(mainPanel);
        this.setDefaultCloseOperation(2);
        this.setSize(400, 300);
        this.setVisible(true);
        this.setLocationRelativeTo(null);
        this.setResizable(false); // chống chỉnh sửa size frame

        run();  // chữ chạy

        // khi mở form sẽ mã nhân viên và vai trò của nhân viên đó
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowOpened(WindowEvent e) {
                luuText();
            }
        });


        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                formChinh formChinh = null;
                try {
                    formChinh = new formChinh();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
                formChinh.setUser(user);
                formChinh.setRole(role);
                dispose();
            }
        });

        // nút thoát chương trình
        btnThoat.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                formChinh formChinh = null;
                try {
                    formChinh = new formChinh();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
                formChinh.setUser(user);
                formChinh.setRole(role);
                dispose();
            }
        });

        // nút đổi mật khẩu
        btnDoiPass.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (loi()) {
                    try {
                        JOptionPane.showMessageDialog(null, serviceNhanVienn.doiMatKhau(user, String.valueOf(txtPassNew.getPassword()), String.valueOf(txtPassOld.getPassword())));
                    } catch (SQLException ex) {
                        JOptionPane.showMessageDialog(null, "gặp lỗi rồi! Quay lại để gửi lỗi cho admin nha");
                        log.logger.setLevel(Level.WARNING);
                        log.logger.info(ex.getMessage());
                        log.logger.warning("Lỗi ở form đổi mật khẩu");
                        StringWriter sw = new StringWriter();
                        PrintWriter pw = new PrintWriter(sw);
                        ex.printStackTrace(pw);
                        String sStackTrace = sw.toString(); // stack trace as a string
                        log.logger.severe(sStackTrace);
                    }
                }
            }
        });
    }


    // nút check lỗi form
    private boolean loi() {
        if (String.valueOf(txtPassNew.getPassword()).isBlank() || String.valueOf(txtPassNew.getPassword()).isEmpty()) {
            JOptionPane.showMessageDialog(null, "không được để trống mật khẩu mới");
            return false;
        }

        if (String.valueOf(txtPassOld.getPassword()).isBlank() || String.valueOf(txtPassOld.getPassword()).isEmpty()) {
            JOptionPane.showMessageDialog(null, "không được để trống mật khẩu cũ");
            return false;
        }
        if (String.valueOf(txtConfirm.getPassword()).isBlank() || String.valueOf(txtConfirm.getPassword()).isEmpty()) {
            JOptionPane.showMessageDialog(null, "vui lòng xác nhận mã");
            return false;
        }
        if (!String.valueOf(txtConfirm.getPassword()).equals(String.valueOf(txtPassNew.getPassword()))) {
            JOptionPane.showMessageDialog(null, "xác nhận lại mật khẩu");
            txtConfirm.setText("");
            return false;
        }
        if (!String.valueOf(txtPassNew.getPassword()).matches("[0-9a-zA-Z]{1,}")) {
            JOptionPane.showMessageDialog(null, "mật khẩu mới vui lòng là chữ la tinh hoặc số", "Lỗi", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        return true;
    }


    // thread chữ chạy
    private void run() {
        Thread thread = new Thread() {
            @Override
            public void run() {

                while (true) {
                    try {
                        sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    lblTitle.setText(lblTitle.getText().charAt(lblTitle.getText().length() - 1) + lblTitle.getText().substring(0, lblTitle.getText().length() - 1));
                }
            }
        };
        thread.start();
    }

    // Phương thức set giá trị cho 3 biến phân quyền
    public void setUser(String user) {
        this.user = user;
    }

    public void setRole(int role) {
        this.role = role;
    }

    // đọc dữ liệu phân quyền lên form
    private void luuText() {
        System.out.println(user + " bên form đổi mk");
    }

    public static void main(String[] args) throws IOException {
        new formDoiMatKhau();
    }

}
