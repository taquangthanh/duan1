package views;

import dao.*;
import model.NguonHang;
import model.Nhaphang;
import model.SanPhamChiTiet;

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
import java.text.NumberFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Locale;
import java.util.logging.Level;

public class formNhapHang extends JFrame {
    private String user;
    private int role;
    private JPanel mainPanel;
    private JTabbedPane tabbedPane1;
    private JTextField txtTimKiem;
    private JTable tblHangHoa;
    private JTextField txtMaSP;
    private JComboBox cbcSoLuong;
    private JTextField txtGiaNhap;
    private JComboBox cbcNguonhang;
    private JButton btnXacNhan;
    private JTable tblHoaDonNhap;
    private JLabel lblTongTien;
    private JTable tblLuuTru;
    private JButton btnTaoDon;
    private JLabel lblTienLe;
    private JButton btnXoa;
    private JTable tblChiTiet;
    DefaultTableModel _dtm;
    DefaultTableModel _dtmHoaDon;
    DefaultTableModel _dtmLuuTru;
    DefaultTableModel _dtmChiTiet;
    serviceNguonHang serviceNguonHang = new serviceNguonHang();
    serviceSanPhamChiTiet serviceSanPhamChiTiet = new serviceSanPhamChiTiet();
    serviceNhapHang serviceNhapHang = new serviceNhapHang();
    boolean check = false;
    boolean check1 = false;

    public formNhapHang() throws IOException, SQLException {
        this.setTitle("Cửa sổ nhập hàng");
        this.setContentPane(mainPanel);
        this.setVisible(true);
        this.setSize(1400, 800);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(2);
        _dtm = (DefaultTableModel) tblHangHoa.getModel();
        _dtmHoaDon = (DefaultTableModel) tblHoaDonNhap.getModel();
        _dtmLuuTru = (DefaultTableModel) tblLuuTru.getModel();
        _dtmChiTiet = (DefaultTableModel) tblChiTiet.getModel();
        _dtm.setColumnIdentifiers(new String[]{
                "Mã sản phẩm", "Tên sản phẩm", "Loại", "Size", "Màu sắc", "Số lượng trong kho", "Giá nhập"
        });
        _dtmHoaDon.setColumnIdentifiers(new String[]{
                "Mã hóa đơn", "Người nhập", "Nguồn hàng", "Ngày", "Tổng tiền"
        });
        _dtmLuuTru.setColumnIdentifiers(new String[]{
                "Mã sản phẩm", "Tên sản phẩm", "Nguồn Hàng", "Loại", "Size", "Màu sắc", "Số lượng", "Giá nhập"
        });
        _dtmChiTiet.setColumnIdentifiers(new String []{
                "Mã hóa đơn","Mã nhân viên", "Tên Sản Phẩm", "Size", "Màu sắc", "Số lượng", "Giá nhập"
        });

        loadCbcNguonHang();
        loadTblNhapHang();
        loadCBCSoLuong();
        loadTblHoaDonNhapHang();
        RendererHighlighted renderer = new RendererHighlighted(txtTimKiem);
        tblHangHoa.setDefaultEditor(Object.class, null);
        TableRowSorter<TableModel> rowSorter
                = new TableRowSorter<>(tblHangHoa.getModel());
        tblHangHoa.setRowSorter(rowSorter);
        tblHangHoa.setDefaultRenderer(Object.class, renderer);



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
                    formChinh.setUser(user);
                    formChinh.setRole(role);
                    dispose();
                } catch (IOException ex) {
                    try {
                        baoLoi(ex);
                    } catch (IOException exc) {
                        exc.printStackTrace();
                    }
                }

            }
        });


        // nút tìm kiếm sản phẩm
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



        // click tbl đẩy lên form
        tblHangHoa.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {

                int i = tblHangHoa.getSelectedRow();
                txtMaSP.setText(String.valueOf(tblHangHoa.getValueAt(i, 0)));
                txtGiaNhap.setText(String.valueOf(tblHangHoa.getValueAt(i, 6)));
                lblTienLe.setText(toCurrency(soNguyen(txtGiaNhap.getText()) * Integer.parseInt(cbcSoLuong.getSelectedItem().toString())));


            }
        });

        // tính tổng tiền theo số lượng
        cbcSoLuong.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(lblTienLe.getText().equals("")){
                    return;
                }
                lblTienLe.setText(toCurrency(soNguyen(txtGiaNhap.getText()) * Integer.parseInt(cbcSoLuong.getSelectedItem().toString())));

            }
        });

        // thêm 1 hàng vào tbl để làm hóa đơn
        btnXacNhan.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                int row[] = tblHangHoa.getSelectedRows();
                if (row.length == 1) {
                    if (check) {
                        int dem = 0;
                        for (int j = 0; j < _dtmLuuTru.getRowCount(); j++) {
                            if (!cbcNguonhang.getSelectedItem().toString().equals(String.valueOf(tblLuuTru.getValueAt(j, 2)))) {
                                dem++;
                                JOptionPane.showMessageDialog(null, "Đơn hàng phải tạo ở cùng một nguồn hàng");
                                return;
                            }
                        }
                        if (dem == 0) {
                            check = false;
                        }
                    }
                    if (check1) {
                        int dem = 0;
                        for (int j = 0; j < _dtmLuuTru.getRowCount(); j++) {
                            if (String.valueOf(tblHangHoa.getValueAt(row[0], 0)).equals(String.valueOf(tblLuuTru.getValueAt(j, 0)))) {
                                int soLuong = Integer.parseInt(cbcSoLuong.getSelectedItem().toString()) + Integer.parseInt(String.valueOf(tblLuuTru.getValueAt(j, 6)));
                                tblLuuTru.setValueAt(soLuong, j, 6);
                                long tong =  (soLuong * soNguyen(String.valueOf(tblHangHoa.getValueAt(row[0], 6))));
                                tblLuuTru.setValueAt(toCurrency(tong), j, 7);
                                dem++;
                                break;
                            }
                        }
                        if (dem == 0) {
                            check1 = false;
                        }

                    }
                    if (!check && !check1) {
                        _dtmLuuTru.addRow(new Object[]{
                                String.valueOf(tblHangHoa.getValueAt(row[0], 0)), String.valueOf(tblHangHoa.getValueAt(row[0], 1)), cbcNguonhang.getSelectedItem().toString(),
                                String.valueOf(tblHangHoa.getValueAt(row[0], 2)), String.valueOf(tblHangHoa.getValueAt(row[0], 3)),
                                String.valueOf(tblHangHoa.getValueAt(row[0], 4)), cbcSoLuong.getSelectedItem().toString(), lblTienLe.getText()
                        });
                        check = true;
                        check1 = true;
                    }
                }
                if (row.length > 1) {
                    if (_dtmLuuTru.getRowCount() > 0) {
                        if (!cbcNguonhang.getSelectedItem().toString().equals(tblLuuTru.getValueAt(0, 2))) {
                            JOptionPane.showMessageDialog(null, "phải chung một nguồn hàng ở 1 đơn");
                            return;
                        }
                        ArrayList<Integer> arr = new ArrayList<>();
                        for (int i = 0; i < row.length; i++) {
                            arr.add(row[i]);
                        }
                        for (int j = 0; j < _dtmLuuTru.getRowCount(); j++) {
                            for (int i = 0; i < arr.size(); i++) {
                                if (String.valueOf(tblHangHoa.getValueAt(arr.get(i), 0)).equals(String.valueOf(tblLuuTru.getValueAt(j, 0)))) {
                                    int soLuong = Integer.parseInt(cbcSoLuong.getSelectedItem().toString()) + Integer.parseInt(String.valueOf(tblLuuTru.getValueAt(j, 6)));
                                    tblLuuTru.setValueAt(soLuong, j, 6);
                                    long tong =  (soLuong * soNguyen(String.valueOf(tblHangHoa.getValueAt(arr.get(i), 6))));
                                    tblLuuTru.setValueAt(toCurrency(tong), j, 7);
                                    arr.remove(i);
                                    break;
                                }
                            }
                        }
                        for (int i = 0; i < arr.size(); i++) {
                            long a =  soNguyen(String.valueOf(tblHangHoa.getValueAt(arr.get(i), 6))) * Integer.parseInt(String.valueOf(cbcSoLuong.getSelectedItem().toString()));
                            _dtmLuuTru.addRow(new Object[]{
                                    String.valueOf(tblHangHoa.getValueAt(arr.get(i), 0)), String.valueOf(tblHangHoa.getValueAt(arr.get(i), 1)), cbcNguonhang.getSelectedItem().toString(),
                                    String.valueOf(tblHangHoa.getValueAt(arr.get(i), 2)), String.valueOf(tblHangHoa.getValueAt(arr.get(i), 3)),
                                    String.valueOf(tblHangHoa.getValueAt(arr.get(i), 4)), cbcSoLuong.getSelectedItem().toString(), toCurrency(a)
                            });
                        }

                    } else {
                        for (int rows : row
                        ) {
                            long a = soNguyen(String.valueOf(tblHangHoa.getValueAt(rows, 6))) * Integer.parseInt(String.valueOf(cbcSoLuong.getSelectedItem().toString()));
                            _dtmLuuTru.addRow(new Object[]{
                                    String.valueOf(tblHangHoa.getValueAt(rows, 0)), String.valueOf(tblHangHoa.getValueAt(rows, 1)), cbcNguonhang.getSelectedItem().toString(),
                                    String.valueOf(tblHangHoa.getValueAt(rows, 2)), String.valueOf(tblHangHoa.getValueAt(rows, 3)),
                                    String.valueOf(tblHangHoa.getValueAt(rows, 4)), cbcSoLuong.getSelectedItem().toString(), toCurrency(a)
                            });
                        }
                    }
                }


                if (_dtmLuuTru.getRowCount() > 0) {
                    int tong = 0;
                    for (int j = 0; j < _dtmLuuTru.getRowCount(); j++) {
                        tong += soNguyen(String.valueOf(tblLuuTru.getValueAt(j, 7)));
                    }
                    lblTongTien.setText(toCurrency(tong));
                }
            }
        });

        // khởi tạo đơn hàng
        btnTaoDon.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(_dtmLuuTru.getRowCount() == 0){
                    JOptionPane.showMessageDialog(null, "chưa có sản phẩm để tạo đơn");
                    return;
                }
                try {
                    serviceNhapHang.khoiTaoDonHang(user, serviceNguonHang.getID(cbcNguonhang.getSelectedItem().toString()), String.valueOf(LocalDate.now()), soNguyen(lblTongTien.getText()));
                    int id = serviceNhapHang.hoaDon(user, serviceNguonHang.getID(cbcNguonhang.getSelectedItem().toString()), String.valueOf(LocalDate.now()), soNguyen(lblTongTien.getText()));
                    for (int i = 0; i < _dtmLuuTru.getRowCount(); i++) {
                        serviceSanPhamChiTiet.updateDay(Integer.parseInt(String.valueOf(tblLuuTru.getValueAt(i, 0))));
                        serviceNhapHang.khoiTaoChiTietDonHang(id, Integer.parseInt(String.valueOf(tblLuuTru.getValueAt(i, 0))), Integer.parseInt(String.valueOf(tblLuuTru.getValueAt(i, 6))), soNguyen(String.valueOf(tblLuuTru.getValueAt(i, 7))));
                        for (int j = 0; j < _dtm.getRowCount(); j++) {
                            if(Integer.parseInt(String.valueOf(tblLuuTru.getValueAt(i,0))) == Integer.parseInt(String.valueOf(tblHangHoa.getValueAt(j,0)))){
                                int a = Integer.parseInt(String.valueOf(tblLuuTru.getValueAt(i,6))) + Integer.parseInt(String.valueOf(tblHangHoa.getValueAt(j,5)));
                                serviceSanPhamChiTiet.updateSoLuong(a,Integer.parseInt(String.valueOf(tblHangHoa.getValueAt(j,0))));
                                _dtm.setValueAt(a,j,5);
                                break;
                            }
                        }
                    }
                    JOptionPane.showMessageDialog(null, "Đã tạo hóa đơn nhập hàng");
                    loadTblHoaDonNhapHang();
                    _dtmLuuTru.setRowCount(0);


                } catch (SQLException ex) {
                    try {
                        baoLoi(ex);
                    } catch (IOException exc) {
                        exc.printStackTrace();
                    }
                }
            }
        });

        // nút xóa
        btnXoa.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int row[] = tblLuuTru.getSelectedRows();
                for (int i = row.length - 1; i >= 0; i--) {
                    _dtmLuuTru.removeRow(row[i]);
                }
                if (_dtmLuuTru.getRowCount() > 0) {
                    long tong = 0;
                    for (int j = 0; j < _dtmLuuTru.getRowCount(); j++) {
                        tong += soNguyen(String.valueOf(tblLuuTru.getValueAt(j, 7)));
                    }
                    lblTongTien.setText(toCurrency(tong));
                }
                if(_dtmLuuTru.getRowCount() == 0){
                    lblTongTien.setText(toCurrency(0));
                }
            }
        });

        // chọn hóa đơn để xem chi tiết
        tblHoaDonNhap.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int i = tblHoaDonNhap.getSelectedRow();
                try {
                    serviceNhapHang.inHoaDonChiTiet(Integer.parseInt(String.valueOf(tblHoaDonNhap.getValueAt(i,0))) , _dtmChiTiet);

                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        });
    }


    // đổ dữ liệu vào tbl nhập hàng
    private void loadTblNhapHang() throws SQLException {
        _dtm = (DefaultTableModel) tblHangHoa.getModel();
        if (_dtm.getRowCount() > 0) {
            _dtm.setRowCount(0);
        }

        for (SanPhamChiTiet a : serviceSanPhamChiTiet.get_list()
        ) {
            _dtm.addRow(new Object[]{
                    a.getIdChiTiet(), a.getName(), a.getTen(), a.getSize(), a.getColor(), a.getSoLuong(), toCurrency((long) a.getGiaVon())
            });
        }


    }


    // quy đổi tiền
    public static String toCurrency(long tienTe){
        Locale lc = new Locale("vi","VN");
        NumberFormat nf = NumberFormat.getCurrencyInstance(lc);
        return  nf.format(tienTe);
    }

    // quy đổi tiền về long
    public static long soNguyen(String tien){
        tien = tien.substring(0, tien.length() - 2).trim();
        tien = tien.replace(".", "");
        System.out.println(tien);
        return Long.parseLong(tien);
    }


    // đổ dữ liệu vào tbl hóa đơn nhập hàng
    private void loadTblHoaDonNhapHang() throws SQLException {
        _dtmHoaDon = (DefaultTableModel) tblHoaDonNhap.getModel();
        if (_dtmHoaDon.getRowCount() > 0) {
            _dtmHoaDon.setRowCount(0);
        }
        for (Nhaphang a : serviceNhapHang.get_lst()
        ) {
            _dtmHoaDon.addRow(new Object[]{
                    a.getIdNhapHang(), a.getMaNhanVien(), serviceNguonHang.getTen(a.getIdNguonHang()), a.getNgayNhap(), toCurrency(a.getGiaNhap())
            });
        }
    }

    // đổ dữ liệu vào cbc số lượng
    private void loadCBCSoLuong() {
        cbcSoLuong.removeAllItems();

        for (int i = 1; i <= 500; i++) {
            cbcSoLuong.addItem(i);
        }
    }


    // đổ dữ liệu vào cbc nguồn hàng
    private void loadCbcNguonHang() throws SQLException {
        cbcNguonhang.removeAllItems();
        for (NguonHang a : serviceNguonHang.getlist()
        ) {
            cbcNguonhang.addItem(a.getTenNguonHang());
        }
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
        System.out.println(user + " bên form hàng hóa");
    }

    public static void main(String[] args) throws IOException, SQLException {
        new formNhapHang();
    }

}
