package views;

import dao.Log;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;

public class formChinh extends JFrame{
    private String user;
    private int role;
    private JPanel mainForm;
    private JMenu mnuHeThong;
    private JMenuItem mniDangXuat;
    private JMenuItem mniDoiPass;
    private JMenuItem mniKetThuc;
    private JMenu mnuQuanLi;
    private JMenuItem mniNhanVien;
    private JMenuItem mniNguonHang;
    private JMenuItem mniLoaiHang;
    private JMenuItem mniSanPham;
    private JMenuItem mniNhapHang;
    private JMenuItem mniThongKe;
    private JButton btnDangXuat;
    private JLabel lblTime;
    private JMenuItem mniXuatHang;
    private JButton btnNhapHang;
    private JButton btnbanHang;
    private JButton btnSanPham;
    private JButton btnBaoLoi;
    private JButton btnKhachHang;

    public formChinh() throws IOException {
        Log log = new Log("hieupro.txt");
        this.setTitle("Cửa sổ chính");
        this.setContentPane(mainForm);
        pack();
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(2);
        this.setResizable(false); // chống chỉnh sửa size frame
        this.setVisible(true);
        run();

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
                System.exit(0);
            }
        });

        // nút đăng xuất
        mniDangXuat.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int i = JOptionPane.showConfirmDialog(null, "Bạn có muốn đăng xuất không?", "Thông báo", JOptionPane.YES_NO_OPTION);
                if (i == JOptionPane.YES_OPTION) {
                    try {
                        loginFormm loginForm = new loginFormm();
                    } catch (SQLException ex) {
                        try {
                            baoLoi(ex);
                        } catch (IOException exc) {
                            exc.printStackTrace();
                        }
                    }
                    dispose();
                }

            }
        });

        // nút đăng xuất
        btnDangXuat.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int i = JOptionPane.showConfirmDialog(null, "Bạn có muốn đăng xuất không?", "Thông báo", JOptionPane.YES_NO_OPTION);
                if (i == JOptionPane.YES_OPTION) {
                    try {
                        loginFormm loginForm = new loginFormm();
                    } catch (SQLException ex) {
                        try {
                            baoLoi(ex);
                        } catch (IOException exc) {
                            exc.printStackTrace();
                        }
                    }
                    dispose();
                }
            }
        });

        //nút tắt chương trình
        mniKetThuc.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });

        // nút đổi mật khẩu
        mniDoiPass.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });

        // nút nhân viên
        mniNhanVien.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });


        // nút nhân viên
        mniNguonHang.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    formNguonHang formNguonHang = new formNguonHang();
                    formNguonHang.setUser(user);
                    formNguonHang.setRole(role);
                    dispose();
                } catch (SQLException | IOException ex) {
                    try {
                        baoLoi(ex);
                    } catch (IOException exc) {
                        exc.printStackTrace();
                    }
                }
            }
        });

        // nút khách hàng
        btnKhachHang.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });


        // nút loại hàng
        mniLoaiHang.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });

        // vào nút hàng hóa
        btnSanPham.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });


        // vào nút hàng hóa
        mniSanPham.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });

        // nút nhập hàng
        mniNhapHang.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });

    }




    // báo lỗi trên form
    private void baoLoi(Exception ex) throws IOException {
        Log log = new Log("hieupro.txt");
        JOptionPane.showMessageDialog(null, "gặp lỗi rồi! Quay lại để gửi lỗi cho admin nha");
        log.logger.setLevel(Level.WARNING);
        log.logger.info(ex.getMessage());
        log.logger.warning("Lỗi ở form chính");
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

        if (role != 1) {
            mniNhanVien.setEnabled(false);
            mniThongKe.setEnabled(false);
        }
        System.out.println(user + " bên form chính");
    }


    // đồng hồ
    private void run() {
        Thread thread = new Thread() {
            @Override
            public void run() {
                int i = 90;
                while (true) {
                    Date t = new Date();
                    SimpleDateFormat sdf = new SimpleDateFormat();
                    sdf.applyPattern("HH:mm:ss");
                    String ta = sdf.format(t);
                    lblTime.setText(ta);
                    try {
                        sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                }
            }
        };
        thread.start();
    }

    public static void main(String[] args) throws IOException {
        new formChinh();
    }
}
