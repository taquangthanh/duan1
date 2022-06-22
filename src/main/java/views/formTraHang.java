package views;

import dao.Log;
import dao.RendererHighlighted;
import dao.serviceKhachHang;
import dao.serviceSanPhamChiTiet;
import model.BanHang;
import model.Trahang;

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
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
import java.util.Locale;
import java.util.logging.Level;

public class formTraHang extends JFrame {
    private String user;
    private int role;
    private JPanel mainPanel;
    private JTextField txtTimKiem;
    private JComboBox cbcNam;
    private JComboBox cbcThang;
    private JTable tblHoaDon;
    private JTextField txtSDT;
    private JTextField txtName;
    private JCheckBox ckcRemember;
    private JTable tblChiTiet;
    private JButton btnFindHoaDon;
    private JTabbedPane tabbedPane1;
    private JTable tblhoaDonTraHang;
    private JTable tblChiTietDonHang;
    private JLabel lblTong;
    private JButton btnHoaDon;
    private JTable tblChiTietDon;
    private JButton btnXoa;
    DefaultTableModel _dtmLuuTru;
    DefaultTableModel _dtmHoaDon;
    DefaultTableModel _dtmChiTiet;
    DefaultTableModel _dtmHoaDonTraHang;
    DefaultTableModel _dtmChiTietHoaDonTrahang;
    serviceBanhang serviceBanhang = new serviceBanhang();
    serviceTrahang serviceTrahang = new serviceTrahang();
    serviceKhachHang serviceKhachHang = new serviceKhachHang();
    serviceSanPhamChiTiet serviceSanPhamChiTiet = new serviceSanPhamChiTiet();
    public formTraHang() throws SQLException {
        this.setTitle("Cửa sổ trả hàng");
        this.setContentPane(mainPanel);
        this.setSize(1400, 800);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(2);
        this.setResizable(false); // chống chỉnh sửa size frame
        this.setVisible(true);
        loadCBC();
        lblTong.setText(toCurrency(0));
        _dtmHoaDon = (DefaultTableModel) tblHoaDon.getModel();
        _dtmChiTiet = (DefaultTableModel) tblChiTiet.getModel();
        _dtmLuuTru = (DefaultTableModel) tblChiTietDon.getModel();
        _dtmHoaDonTraHang = (DefaultTableModel) tblhoaDonTraHang.getModel();
        _dtmChiTietHoaDonTrahang = (DefaultTableModel) tblChiTietDonHang.getModel();
        _dtmChiTietHoaDonTrahang.setColumnIdentifiers(new String[]{
                "Mã hóa đơn", "Tên sản phẩm", "Số Lượng", "Giá sản phẩm"
        });
        _dtmHoaDonTraHang.setColumnIdentifiers(new String[]{
                "Mã hóa đơn", "Nhân viên" , "Tên khách hàng", "Ngày Trả", "Tổng tiền"
        });
        _dtmHoaDon.setColumnIdentifiers(new String[]{
                "Mã hóa đơn", "Nhân viên" , "Tên khách hàng", "Ngày", "Tổng tiền", "Tiền Khách Đưa", "Tiền Thừa", "Giảm giá"
        });

        _dtmChiTiet.setColumnIdentifiers(new String []{
                "Mã hóa đơn" , "Mã nhân viên", "Tên khách hàng", "Tên Sản Phẩm", "Size", "Màu sắc", "Số lượng bán", "Đơn giá"
        });
        _dtmLuuTru.setColumnIdentifiers(new String []{
                "Mã hóa đơn" , "Mã nhân viên", "Tên khách hàng", "Tên Sản Phẩm", "Size", "Màu sắc", "Số lượng bán", "Đơn giá"
        });
        loadtblHoaDonTra();

        RendererHighlighted renderer = new RendererHighlighted(txtTimKiem);
        tblHoaDon.setDefaultEditor(Object.class, null);
        TableRowSorter<TableModel> rowSorter
                = new TableRowSorter<>(tblHoaDon.getModel());
        tblHoaDon.setRowSorter(rowSorter);
        tblHoaDon.setDefaultRenderer(Object.class, renderer);

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

        btnFindHoaDon.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(ckcRemember.isSelected()){
                    try {
                        loadtblHoaDon();
                    } catch (SQLException ex) {
                        try {
                            baoLoi(ex);
                        } catch (IOException exc) {
                            exc.printStackTrace();
                        }
                    }
                }else {
                    try {
                        loadtblHoaDonKhongTime();
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

        tblHoaDon.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if(_dtmLuuTru.getRowCount() > 0){
                    _dtmLuuTru.setRowCount(0);
                }
                int i = tblHoaDon.getSelectedRow();
                try {
                    serviceBanhang.inChiTietDon(Integer.parseInt(String.valueOf(tblHoaDon.getValueAt(i,0))), _dtmChiTiet);
                } catch (SQLException ex) {
                    try {
                        baoLoi(ex);
                    } catch (IOException exc) {
                        exc.printStackTrace();
                    }
                }
            }
        });
        tblChiTiet.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = tblChiTiet.getSelectedRow();
                String input = "a";
                while (!input.matches("[0-9]{1,}")) {
                    input = JOptionPane.showInputDialog(null, "Vui lòng nhập số lượng trả của sản phẩm " + tblChiTiet.getValueAt(row,3) + " Size : " + tblChiTiet.getValueAt(row,4) + " Color : " + tblChiTiet.getValueAt(row,5));
                    if (input == null) {
                        return;
                    }
                    if (!input.matches("[0-9]{1,}")) {
                        JOptionPane.showMessageDialog(null, "số lượng phải nhập số");
                        continue;
                    }
                    if (Integer.parseInt(input) > Integer.parseInt(String.valueOf(tblChiTiet.getValueAt(row,6)))) {
                        JOptionPane.showMessageDialog(null, "Số lượng này đã vượt quá hàng trong đơn cũ");
                        input = "a";
                    }
                }
                if(_dtmLuuTru.getRowCount() == 0){
                    _dtmLuuTru.addRow(new Object[]{
                            tblChiTiet.getValueAt(row,0),tblChiTiet.getValueAt(row,1),tblChiTiet.getValueAt(row,2),tblChiTiet.getValueAt(row,3),tblChiTiet.getValueAt(row,4),
                            tblChiTiet.getValueAt(row,5),input, toCurrency(soNguyen(String.valueOf(tblChiTiet.getValueAt(row,7))) /Integer.parseInt(String.valueOf(tblChiTiet.getValueAt(row,6))) * Integer.parseInt(input))
                    });
                    long a = soNguyen((String) tblChiTietDon.getValueAt(0,7));
                    lblTong.setText(toCurrency(a));
                    return;
                }
                if(_dtmLuuTru.getRowCount() > 0){
                    long tong = 0;
                    boolean flag = false;
                    for (int j = 0; j < _dtmLuuTru.getRowCount(); j++) {
                        if(String.valueOf(tblChiTiet.getValueAt(row,3)).equals(String.valueOf(tblChiTietDon.getValueAt(j,3))) &&
                                String.valueOf(tblChiTiet.getValueAt(row,4)).equals(String.valueOf(tblChiTietDon.getValueAt(j,4))) &&
                                String.valueOf(tblChiTiet.getValueAt(row,5)).equals(String.valueOf(tblChiTietDon.getValueAt(j,5)))){
                            if(Integer.parseInt(String.valueOf(tblChiTietDon.getValueAt(j,6))) + Integer.parseInt(input) > Integer.parseInt(String.valueOf(tblChiTiet.getValueAt(j,6)))){
                                JOptionPane.showMessageDialog(null , "Đã vượt quá lượt hàng trong hóa đơn");
                                return;
                            }
                            _dtmLuuTru.setValueAt(Integer.parseInt(String.valueOf(tblChiTietDon.getValueAt(j,6))) + Integer.parseInt(input), j,6);
                            _dtmLuuTru.setValueAt(toCurrency(soNguyen(String.valueOf(tblChiTiet.getValueAt(row,7))) /Integer.parseInt(String.valueOf(tblChiTiet.getValueAt(row,6))) * Integer.parseInt(String.valueOf(tblChiTietDon.getValueAt(j,6)))), j , 7);
                            flag = true;
                        }
                        long a = soNguyen(String.valueOf(tblChiTietDon.getValueAt(j,7)));
                        tong += a;
                    }
                    if(!flag){
                        _dtmLuuTru.addRow(new Object[]{
                                tblChiTiet.getValueAt(row,0),tblChiTiet.getValueAt(row,1),tblChiTiet.getValueAt(row,2),tblChiTiet.getValueAt(row,3),tblChiTiet.getValueAt(row,4),
                                tblChiTiet.getValueAt(row,5),input, toCurrency(soNguyen(String.valueOf(tblChiTiet.getValueAt(row,7))) /Integer.parseInt(String.valueOf(tblChiTiet.getValueAt(row,6))) * Integer.parseInt(input))
                        });

                        tong += soNguyen(toCurrency(soNguyen(String.valueOf(tblChiTiet.getValueAt(row,7))) /Integer.parseInt(String.valueOf(tblChiTiet.getValueAt(row,6))) * Integer.parseInt(input)));
                    }
                    lblTong.setText(toCurrency(tong));
                }

            }

        });
        btnHoaDon.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(_dtmHoaDon.getRowCount() == 0 || _dtmChiTiet.getRowCount() == 0){
                    JOptionPane.showMessageDialog(null, "chưa có thông tin hóa đơn bán");
                    return;
                }
                if(_dtmLuuTru.getRowCount() == 0){
                    JOptionPane.showMessageDialog(null, "Chưa có sản phẩm trả lại");
                    return;
                }
                if(txtSDT.getText().isEmpty() || txtSDT.getText().isBlank() || txtName.getText().isBlank() || txtName.getText().isEmpty()){
                    JOptionPane.showMessageDialog(null, "Chưa có thông tin của khách hàng");
                    return;
                }
                try {

                    serviceTrahang.taoMotHoaDonTraHang(user, serviceKhachHang.timTen(txtSDT.getText()).getId(),String.valueOf(LocalDate.now()), soNguyen(lblTong.getText()));

                    int id = serviceTrahang.idHoaDon(user, serviceKhachHang.timTen(txtSDT.getText()).getId(),String.valueOf(LocalDate.now()), soNguyen(lblTong.getText()));

                    for (int i = 0; i < _dtmLuuTru.getRowCount(); i++) {
                        int idSP = serviceSanPhamChiTiet.getIDSP(String.valueOf(tblChiTietDon.getValueAt(i,3)),String.valueOf(tblChiTietDon.getValueAt(i,5)), String.valueOf(tblChiTietDon.getValueAt(i,4)));
                        serviceTrahang.taoChiTietHoaDonTra(id, idSP, Integer.parseInt(String.valueOf(tblChiTietDon.getValueAt(i,6))),  soNguyen(String.valueOf(tblChiTietDon.getValueAt(i,7))));
                    }
                    loadtblHoaDonTra();
                } catch (SQLException ex) {
                    try {
                        baoLoi(ex);
                    } catch (IOException exc) {
                        exc.printStackTrace();
                    }
                }
                JOptionPane.showMessageDialog(null, "Tạo hóa đơn thành công");
            }
        });
        tblhoaDonTraHang.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                    int i = tblhoaDonTraHang.getSelectedRow();
                try {
                    serviceTrahang.inChiTietHoaDon(Integer.parseInt(String.valueOf(tblhoaDonTraHang.getValueAt(i,0))), _dtmChiTietHoaDonTrahang);
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        });

        // xóa sản phẩm ở form
        btnXoa.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                    int rows [] = tblChiTietDon.getSelectedRows();
                for (int i = rows.length - 1; i >= 0; i--) {
                    _dtmLuuTru.removeRow(rows[i]);
                }

                if (_dtmLuuTru.getRowCount() > 0) {
                    long tong = 0;
                    for (int j = 0; j < _dtmLuuTru.getRowCount(); j++) {
                        tong += soNguyen(String.valueOf(_dtmLuuTru.getValueAt(j, 7)));
                    }
                    lblTong.setText(toCurrency(tong));
                }
                if(_dtmLuuTru.getRowCount() == 0){
                    lblTong.setText("0");
                }
            }
        });
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
        return Long.parseLong(tien);
    }



    // đổ dữ liệu vào tbl hóa đơn
    private void loadtblHoaDon() throws SQLException {
        _dtmHoaDon = (DefaultTableModel) tblHoaDon.getModel();
        if(_dtmHoaDon.getRowCount() > 0){
            _dtmHoaDon.setRowCount(0);
        }
        for (BanHang a : serviceBanhang.getList(Integer.parseInt(cbcNam.getSelectedItem().toString()), Integer.parseInt(cbcThang.getSelectedItem().toString()), txtName.getText(), txtSDT.getText())) {
            _dtmHoaDon.addRow(new Object[]{
                    a.getIdHoaDon(), a.getManv(), serviceKhachHang.timTenTheoID(a.getIdKH()), a.getNgayBan(), toCurrency(a.getGiaTriDon()), toCurrency(a.getTienKhachDua()), toCurrency(a.getTienThua()), toCurrency(a.getGiamGia())
            });
        }
        if(_dtmHoaDon.getRowCount() == 0){
            JOptionPane.showMessageDialog(null, "Không tìm thấy hóa đơn nào với điều kiện trên");
        }
    }

    // đổ dữ liệu vào tbl hóa đơn
    private void loadtblHoaDonKhongTime() throws SQLException {
        _dtmHoaDon = (DefaultTableModel) tblHoaDon.getModel();
        if(_dtmHoaDon.getRowCount() > 0){
            _dtmHoaDon.setRowCount(0);
        }
        for (BanHang a : serviceBanhang.getListKhongTime(txtName.getText(), txtSDT.getText())) {
            _dtmHoaDon.addRow(new Object[]{
                    a.getIdHoaDon(), a.getManv(), serviceKhachHang.timTenTheoID(a.getIdKH()), a.getNgayBan(), toCurrency(a.getGiaTriDon()), toCurrency(a.getTienKhachDua()), toCurrency(a.getTienThua()), toCurrency(a.getGiamGia())
            });
        }
        if(_dtmHoaDon.getRowCount() == 0){
            JOptionPane.showMessageDialog(null, "Không tìm thấy hóa đơn nào với điều kiện trên");
        }
    }

    private void loadCBC(){
        cbcNam.removeAllItems();
        cbcThang.removeAllItems();
        Date date = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat();
        simpleDateFormat.applyPattern("yyyy");
        String format = simpleDateFormat.format(date);
        for (int i = Integer.parseInt(format); i >= 2010; i--) {
            cbcNam.addItem(i);
        }
        for (int i = 1; i <= 12 ; i++) {
            cbcThang.addItem(i);
        }
    }

    private void loadtblHoaDonTra() throws SQLException {
        _dtmHoaDonTraHang = (DefaultTableModel) tblhoaDonTraHang.getModel();
        if(_dtmHoaDonTraHang.getRowCount() > 0){
            _dtmHoaDonTraHang.setRowCount(0);
        }
        for (Trahang a: serviceTrahang.getList()
             ) {
            _dtmHoaDonTraHang.addRow(new Object[]{
                a.getIdHoaDOn(), a.getManv(), serviceKhachHang.timTenTheoID(a.getIdKhachHang()) , a.getNgayTra(), toCurrency(a.getGiaTriDon())
            });
        }
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

        System.out.println(user + " bên form trả hàng");
    }

    public static void main(String[] args) throws SQLException {
        new formTraHang();
    }
}
