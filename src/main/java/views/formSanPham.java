package views;

import dao.Log;
import dao.RendererHighlighted;
import dao.serviceLoaiSP;
import dao.serviceSanPham;
import model.LoaiSP;
import model.SanPham;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.SQLException;
import java.util.logging.Level;

public class formSanPham extends JFrame {
    private String user;
    private int role;
    private JPanel mainPanel;
    private JTabbedPane tabbedPane1;
    private JComboBox cbcLoaiSP;
    private JButton btnThemLoai;
    private JButton btnSuaLoai;
    private JTextField txtTenSP;
    private JTextArea txtMoTa;
    private JButton btnThemSP;
    private JButton btnSuaSP;
    private JButton btnNew;
    private JTable tblSanPham;
    private JTextField txtTimKiem;
    DefaultTableModel _dtm;
    serviceLoaiSP _list = new serviceLoaiSP();
    serviceSanPham _lstSP = new serviceSanPham();

    public formSanPham() throws IOException, SQLException {
        this.setTitle("Quản lí sản phẩm");
        this.setContentPane(mainPanel);
        this.setSize(600, 450);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(2);
        this.setResizable(false); // chống chỉnh sửa size frame
        this.setVisible(true);
        _dtm = (DefaultTableModel) tblSanPham.getModel();
        _dtm.setColumnIdentifiers(new String[]{
                "ID Sản Phẩm", "Tên Sản Phẩm", "Loại Sản Phẩm", "Mô Tả"
        });
        tblSanPham.setModel(_dtm);
        RendererHighlighted renderer = new RendererHighlighted(txtTimKiem);
        tblSanPham.setDefaultEditor(Object.class, null);
        TableRowSorter<TableModel> rowSorter
                = new TableRowSorter<>(tblSanPham.getModel());

        tblSanPham.setRowSorter(rowSorter);
        tblSanPham.setDefaultRenderer(Object.class, renderer);
        loadCBC();
        loadTBL();
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

        // thêm loại sản phẩm
        btnThemLoai.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String input = JOptionPane.showInputDialog("Mời bạn nhập tên loại cần thêm: ");
                try {
                    JOptionPane.showMessageDialog(null, _list.themLSP(input));
                    loadCBC();
                } catch (SQLException ex) {
                    try {
                        baoLoi(ex);
                    } catch (IOException exc) {
                        exc.printStackTrace();
                    }
                }
            }
        });

        // sửa loại sản phẩm
        btnSuaLoai.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String input = JOptionPane.showInputDialog("Mời bạn nhập tên mới: ");

                try {
                    System.out.println(input.length());
                    JOptionPane.showMessageDialog(null, _list.suaLSP(input, cbcLoaiSP.getSelectedItem().toString()));
                    loadCBC();
                } catch (SQLException ex) {
                    try {
                        baoLoi(ex);
                    } catch (IOException exc) {
                        exc.printStackTrace();
                    }
                }
            }
        });

        // thêm sản phẩm
        btnThemSP.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (loi()) {
                    try {
                        JOptionPane.showMessageDialog(null, _lstSP.themSP(sanPham()));
                        loadTBL();
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

        // nút làm mới form
        btnNew.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                xoaForm();
            }
        });

        // nút sửa sản phẩm
        btnSuaSP.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (loi()) {
                    int i = tblSanPham.getSelectedRow();
                    try {
                        JOptionPane.showMessageDialog(null, _lstSP.suaSP(sanPham(), _lstSP.get1Loai(String.valueOf(tblSanPham.getValueAt(i, 1)))));
                        loadTBL();
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

        // click 1 thằng ở tbl đẩy lên form
        tblSanPham.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int i = tblSanPham.getSelectedRow();
                txtTenSP.setText(String.valueOf(tblSanPham.getValueAt(i, 1)));
                txtMoTa.setText(String.valueOf(tblSanPham.getValueAt(i, 3)));
                cbcLoaiSP.setSelectedItem(String.valueOf(tblSanPham.getValueAt(i, 2)));
            }
        });
    }

    // load loại sản phẩm cbc
    private void loadCBC() throws SQLException {
        cbcLoaiSP.removeAllItems();
        for (LoaiSP a : _list.get_list()
        ) {
            cbcLoaiSP.addItem(a.getTen());
        }

    }

    // laod sản phẩm lên table
    private void loadTBL() throws SQLException {
        _dtm = (DefaultTableModel) tblSanPham.getModel();
        if (_dtm.getRowCount() > 0) {
            _dtm.setRowCount(0);
        }
        int stt = 1;
        for (SanPham a : _lstSP.get_list()
        ) {
            _dtm.addRow(new Object[]{
                    stt, a.getName(), a.getTen(), a.getMoTa()
            });
            stt++;
        }
    }

    // phương thức xóa form
    private void xoaForm() {
        txtTenSP.setText("");
        txtMoTa.setText("");
    }

    // phương thức khởi tạo 1 sản phẩm
    private SanPham sanPham() {
        SanPham sanPham = new SanPham();
        sanPham.setName(txtTenSP.getText());
        sanPham.setMoTa(txtMoTa.getText());
        sanPham.setTen(cbcLoaiSP.getSelectedItem().toString());

        try {
            sanPham.setId(_list.get1Loai(cbcLoaiSP.getSelectedItem().toString()));
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return sanPham;
    }


    // phương thức check lỗi ở form
    private boolean loi() {
        if (txtTenSP.getText().isEmpty() || txtTenSP.getText().isBlank()) {
            JOptionPane.showMessageDialog(null, "tên không được để trốn");
            return false;
        }
        return true;
    }


    // set quyền + báo lỗi form
    public void setUser(String user) {
        this.user = user;
    }

    public void setRole(int role) {
        this.role = role;
    }

    // báo lỗi ngoại lệ
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
        System.out.println(user + " bên form sản phẩm");
    }


    public static void main(String[] args) throws IOException, SQLException {
        new formSanPham();
    }

}
