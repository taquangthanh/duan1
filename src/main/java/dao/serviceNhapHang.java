package dao;

import model.Nhaphang;
import untils.Connectt;

import javax.swing.table.DefaultTableModel;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class serviceNhapHang {
    List<Nhaphang> _lst;
    Connectt con = new Connectt();

    public serviceNhapHang(){
        _lst = new ArrayList<>();
    }


    // khởi tạo 1 đơn hàng
    public void khoiTaoDonHang(String ma, int idNguonHang, String date, long giaTri) throws SQLException {
        String sql = "insert into hoaDonNhapHang values (?,?,?,?)";
        PreparedStatement pm = con.con().prepareStatement(sql);
        pm.setString(1, ma);
        pm.setInt(2, idNguonHang);
        pm.setDate(3, Date.valueOf(date));
        pm.setLong(4, giaTri);
        pm.executeUpdate();
    }

    // khởi tạo hóa đơn chi tiết
    public void khoiTaoChiTietDonHang(int idHoaDon, int idSP, int soLuong, long gia)throws SQLException{
        String sql = "insert into chiTietHoaDonNhap values (?,?,?,?)";
        PreparedStatement pm = con.con().prepareStatement(sql);
        pm.setInt(1, idHoaDon);
        pm.setInt(2, idSP);
        pm.setInt(3, soLuong);
        pm.setLong(4, gia);
        pm.executeUpdate();

    }

    // in ra hóa đơn chi tiết
    public void inHoaDonChiTiet(int id, DefaultTableModel dtm) throws SQLException{
        String sql = "select idHoaDon, manv,tensp,sizesp, color,chiTietHoaDonNhap.soLuong,giaSanPham from chiTietHoaDonNhap join chiTietSP cTS on cTS.id = chiTietHoaDonNhap.idChiTietSp join hoaDonNhapHang hDNH on hDNH.idHoaDonNhapHang = chiTietHoaDonNhap.idHoaDon join SanPham SP on SP.id = cTS.id_sp where idHoaDon = ?";
        PreparedStatement pm = con.con().prepareStatement(sql);
        pm.setInt(1, id);
        ResultSet rs = pm.executeQuery();
        dtm.setRowCount(0);
        while (rs.next()){
            dtm.addRow(new Object[]{
                    rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5), rs.getString(6), toCurrency(rs.getLong(7))
            });
        }
    }

    // quy đổi tiền
    public static String toCurrency(long tienTe){
        Locale lc = new Locale("vi","VN");
        NumberFormat nf = NumberFormat.getCurrencyInstance(lc);
        return  nf.format(tienTe);
    }



    // tìm id của 1 hóa đơn
    public int hoaDon(String ma, int idNguonHang, String a, long giaTri) throws SQLException {
        String sql = "select * from hoaDonNhapHang where manv = ? and idNguonHang = ? and giaTriDonHang = ? and ngayNhap = ?";
        PreparedStatement pm = con.con().prepareStatement(sql);
        pm.setString(1, ma);
        pm.setInt(2, idNguonHang);
        pm.setLong(3, giaTri);
        pm.setDate(4, Date.valueOf(a));
        ResultSet rs = pm.executeQuery();
        if(rs.next()){
            return rs.getInt(1);
        }
        return -1;
    }





    // xuất danh sách đơn hàng từ db ra

    public List<Nhaphang> get_lst() throws SQLException {
        String sql = "select * from hoaDonNhapHang";
        PreparedStatement pm = con.con().prepareStatement(sql);
        _lst.clear();
        ResultSet rs = pm.executeQuery();
        while (rs.next()){
            _lst.add(new Nhaphang(rs.getInt(1),rs.getString(2),rs.getInt(3), rs.getString(4), rs.getInt(5) ));
        }
        return _lst;
    }
}
