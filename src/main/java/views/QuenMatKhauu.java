package views;

import dao.Log;
import dao.serviceNhanVien;
import io.github.cdimascio.dotenv.Dotenv;
import io.github.cdimascio.dotenv.DotenvEntry;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Properties;
import java.util.Random;
import java.util.logging.Level;

public class QuenMatKhauu extends JFrame {
    private JTextField txtEmail;
    private JButton btnLayCode;
    private JTextField txtCode;
    private JTextField txtTaiKhoan;
    private JPasswordField txtPassNew;
    private JPasswordField txtComfimPass;
    private JButton btnDoiPass;
    private JButton btnThoat;
    private JLabel lblTitle;
    private JLabel lblTime;
    private JPanel mainPanle;
    int code;
    dao.serviceNhanVien serviceNhanVien = new serviceNhanVien();

    public QuenMatKhauu() throws IOException {
        Log log = new Log("hieupro.txt");
        Dotenv dotenv = Dotenv.configure().load();
        this.setTitle("Quên Mật Khẩu");
        this.setContentPane(mainPanle);
        this.setVisible(true);
        this.setSize(400, 300);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(2);
        if (lblTime.getText().equals("0")) {
            btnLayCode.setEnabled(true);
        }


        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                try {
                    loginFormm loginForm = new loginFormm();
                    dispose();
                } catch (SQLException ex) {
                    try {
                        baoLoi(ex);
                    } catch (IOException exc) {
                        exc.printStackTrace();
                    }
                }
            }
        });

        // lây code để gửi email
        btnLayCode.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (lblTime.getText().equals("0") || lblTime.getText().length() == 0) {
                    Random random = new Random();
                    code = random.nextInt(9999);
                    String user = dotenv.get("MY_ENV_VAR1");
                    String pass = dotenv.get("MY_EVV_VAR2");
                    String to = txtEmail.getText();
                    String subject = "Mã để đổi mật khẩu";
                    String message = "Đây là mã của bạn " + code;
                    Properties props = System.getProperties();
                    props.put("mail.smtp.user", "username");
                    props.put("mail.smtp.host", "smtp.gmail.com");
                    props.put("mail.smtp.port", "587");
                    props.put("mail.debug", "true");
                    props.put("mail.smtp.auth", "true");
                    props.put("mail.smtp.starttls.enable", "true");
                    props.put("mail.smtp.EnableSSL.enable", "true");

                    props.setProperty("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
                    props.setProperty("mail.smtp.socketFactory.fallback", "false");
                    props.setProperty("mail.smtp.port", "465");
                    props.setProperty("mail.smtp.socketFactory.port", "465");

                    Session sessiona = Session.getInstance(props,
                            new Authenticator() {
                                protected PasswordAuthentication getPasswordAuthentication() {
                                    return new PasswordAuthentication(user, pass);
                                }
                            });
                    try {
                        Message messagea = new MimeMessage(sessiona);
                        messagea.setFrom(new InternetAddress(user));
                        messagea.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
                        messagea.setSubject(subject);
                        messagea.setText(message);
                        Transport.send(messagea);
                        lblTitle.setText("Chờ mã trong :");
                        JOptionPane.showMessageDialog(null, "Gửi mã thành công");
                        run();
                    } catch (Exception ex) {
                        try {
                            baoLoi(ex);
                        } catch (IOException exc) {
                            exc.printStackTrace();
                        }
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Đợi chút rồi gửi lại email nha!");
                }
            }
        });

        // nút thoát
        btnThoat.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    loginFormm loginFormm = new loginFormm();
                    dispose();
                } catch (SQLException ex) {
                    try {
                        baoLoi(ex);
                    } catch (IOException exc) {
                        exc.printStackTrace();
                    }
                }

            }
        });

        // nút đổi pass
        btnDoiPass.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (loi()) {
                    try {
                        JOptionPane.showMessageDialog(null, serviceNhanVien.updatePassNVQuen(txtTaiKhoan.getText(), String.valueOf(txtPassNew.getPassword()), txtEmail.getText()));
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
    }

    // phương thức check lỗi ở form
    private boolean loi() {
        if (Integer.parseInt(txtCode.getText()) != code) {
            JOptionPane.showMessageDialog(null, "Mã email không đúng");
            txtCode.setText("");
            return false;
        }
        if (txtTaiKhoan.getText().length() == 0) {
            JOptionPane.showMessageDialog(null, "Không được để trống tài khoản");
            txtTaiKhoan.requestFocus();
            return false;
        }
        if (Arrays.toString(txtPassNew.getPassword()).length() == 0) {
            JOptionPane.showMessageDialog(null, "Không được để trống mật khẩu mới");
            txtPassNew.requestFocus();
            return false;
        }
        if (Arrays.toString(txtComfimPass.getPassword()).length() == 0) {
            JOptionPane.showMessageDialog(null, "Vui lòng xác nhận lại mật khẩu");
            txtComfimPass.requestFocus();
            return false;
        }
        String pass = String.valueOf(txtPassNew.getPassword());
        String passNew = String.valueOf(txtComfimPass.getPassword());
        System.out.println(pass + " " + passNew);
        if (!pass.equals(passNew)) {
            JOptionPane.showMessageDialog(null, "Nhập lại mật khẩu để xác nhận");
            txtComfimPass.requestFocus();
            txtComfimPass.setText("");
            return false;
        }
        return true;
    }

    private void baoLoi(Exception ex) throws IOException {
        Log log = new Log("hieupro.txt");
        JOptionPane.showMessageDialog(null, "gặp lỗi rồi! Quay lại để gửi lỗi cho admin nha");
        log.logger.setLevel(Level.WARNING);
        log.logger.info(ex.getMessage());
        log.logger.warning("Lỗi ở form quên mật khẩu");
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        ex.printStackTrace(pw);
        String sStackTrace = sw.toString(); // stack trace as a string
        log.logger.severe(sStackTrace);
    }


    // phương thức đếm thời gian để ngăn người dùng gửi nhiều code quá
    private void run() {
        Thread thread = new Thread() {
            @Override
            public void run() {
                int i = 90;
                while (i-- > 0) {

                    try {
                        sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    lblTime.setText(String.valueOf(i));
                }
            }
        };
        thread.start();
    }

}
