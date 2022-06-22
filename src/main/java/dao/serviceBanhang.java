package dao;

import model.BanHang;
import untils.Connectt;

import javax.swing.table.DefaultTableModel;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class serviceBanhang {
    List<BanHang> _lst;
    Connectt con = new Connectt();
    public serviceBanhang(){
        _lst = new ArrayList<>();
    }

    // tạo hóa đơn bán hàng
    public void inHoaDon(String manv, int idKhach,String date, int giaTriDon, int tienKhach, int tienThua, int giamGia) throws SQLException{
        String sql = "insert into hoaDonBanHang values (?,?,?,?,?,?, ?)";
        PreparedStatement pm = con.con().prepareStatement(sql);
        pm.setString(1, manv);
        pm.setInt(2, idKhach);
        pm.setDate(3, Date.valueOf(date));
        pm.setInt(4,giaTriDon);
        pm.setInt(5,tienKhach);
        pm.setInt(6, tienThua);
        pm.setInt(7,giamGia);
        pm.executeUpdate();
    }


    // tìm id hóa đơn bán hàng
    public int timHoaDon(String manv, int idKhach,String date, int giaTriDon, int tienKhach, int tienThua, int giamGia) throws SQLException{
        String sql = "select idHoaDonBanHang from hoaDonBanHang where manv = ? and idKhachHang = ? and ngayBan = ? and giaTriDonHang = ? and tienKhachDua = ? and tienThua = ? and giaGiam = ?";
        PreparedStatement pm = con.con().prepareStatement(sql);
        pm.setString(1, manv);
        pm.setInt(2, idKhach);
        pm.setDate(3, Date.valueOf(date));
        pm.setInt(4,giaTriDon);
        pm.setInt(5,tienKhach);
        pm.setInt(6, tienThua);
        pm.setInt(7,giamGia);
        ResultSet rs = pm.executeQuery();
        if(rs.next()){
            return rs.getInt(1);
        }
        return -1;
    }

    // khởi tạo chi tiết đơn hàng
    public void taoChiTietDonHang(int idHoaDon, int idChiTietSp, int soLuong, long giaSanPham) throws SQLException{
        String sql = "insert into chiTietHoaDonBan values (?,?,?,?, ?)";
        PreparedStatement pm = con.con().prepareStatement(sql);
        pm.setInt(1, idHoaDon);
        pm.setInt(2, idChiTietSp);
        pm.setInt(3, soLuong);
        pm.setLong(4, giaSanPham);
        pm.setDate(5, Date.valueOf(String.valueOf(LocalDate.now())));
        pm.executeUpdate();
    }

    // in ra chi tiết đơn hàng
    public void inChiTietDon(int id, DefaultTableModel dtm) throws SQLException{
            String sql = "select idHoaDon, manv, tenKH, tensp, sizesp, color, chiTietHoaDonBan.soLuong, giaSanPham from chiTietHoaDonBan join chiTietSP cTS on cTS.id = chiTietHoaDonBan.idChiTietSP join SanPham SP on SP.id = cTS.id_sp join hoaDonBanHang hDBH on hDBH.idHoaDonBanHang = chiTietHoaDonBan.idHoaDon join khachhang k on hDBH.idKhachHang = k.id where idHoaDon = ?";
            PreparedStatement pm = con.con().prepareStatement(sql);
            pm.setInt(1, id);
            dtm.setRowCount(0);
            ResultSet rs = pm.executeQuery();
            while (rs.next()){
                dtm.addRow(new Object[]{
                        rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5), rs.getString(6),rs.getString(7), toCurrency(rs.getLong(8))
                });
            }
    }

    public void inChiTietDonDoi(int id, DefaultTableModel dtm) throws SQLException {
        String sql = "select idHoaDon, idChiTietSP, tensp, sizesp, color, chiTietHoaDonBan.soLuong, giaSanPham from chiTietHoaDonBan join chiTietSP cTS on cTS.id = chiTietHoaDonBan.idChiTietSP join SanPham SP on SP.id = cTS.id_sp join hoaDonBanHang hDBH on hDBH.idHoaDonBanHang = chiTietHoaDonBan.idHoaDon join khachhang k on hDBH.idKhachHang = k.id where idHoaDon = ?";
        PreparedStatement pm = con.con().prepareStatement(sql);
        pm.setInt(1, id);
        dtm.setRowCount(0);
        ResultSet rs = pm.executeQuery();
        while (rs.next()){
            dtm.addRow(new Object[]{
                    rs.getString(1), rs.getString(2),rs.getString(3),rs.getString(4),rs.getString(5),rs.getString(6),toCurrency(rs.getLong(7))
            });
        }
    }







    // quy đổi tiền
    public static String toCurrency(long tienTe){
        Locale lc = new Locale("vi","VN");
        NumberFormat nf = NumberFormat.getCurrencyInstance(lc);
        return  nf.format(tienTe);
    }

    // lấy danh sách hóa đơn từ db đổi vào list
    public List<BanHang> get_lst() throws SQLException {
        String sql = "select * from hoaDonBanHang";
        PreparedStatement pm = con.con().prepareStatement(sql);
        _lst.clear();
        ResultSet rs = pm.executeQuery();
        while (rs.next()){
            _lst.add(new BanHang(rs.getInt(1), rs.getString(2), rs.getInt(3), rs.getString(4), rs.getInt(5),
                    rs.getInt(6), rs.getInt(7), rs.getInt(8) ));
        }
        return _lst;
    }



    // lấy danh sách hóa đơn của 1 khách hàng
    public List<BanHang> getList(int year, int month, String name, String sdt) throws SQLException {
        String sql = "select idHoaDonBanHang, manv,idKhachHang, ngayBan, giaTriDonhang, tienKhachDua, tienThua, giaGiam from hoaDonBanHang join khachhang k on k.id = hoaDonBanHang.idKhachHang where year(ngayBan) = ? and MONTH(ngayBan) = ? and tenKH = ? and sdt = ?";
        PreparedStatement pm = con.con().prepareStatement(sql);
        pm.setInt(1,year);
        pm.setInt(2, month);
        pm.setString(3, name);
        pm.setString(4, sdt);
        _lst.clear();
        ResultSet rs = pm.executeQuery();
        while (rs.next()){
            _lst.add(new BanHang(rs.getInt(1), rs.getString(2), rs.getInt(3), rs.getString(4), rs.getInt(5),
                    rs.getInt(6), rs.getInt(7), rs.getInt(8) ));
        }
        return _lst;
    }

    // lấy danh sách hóa đơn của 1 khách hàng
    public List<BanHang> getListKhongTime( String name, String sdt) throws SQLException {
        String sql = "select idHoaDonBanHang, manv,idKhachHang, ngayBan, giaTriDonhang, tienKhachDua, tienThua, giaGiam from hoaDonBanHang join khachhang k on k.id = hoaDonBanHang.idKhachHang where  tenKH = ? and sdt = ?";
        PreparedStatement pm = con.con().prepareStatement(sql);
        pm.setString(1, name);
        pm.setString(2, sdt);
        _lst.clear();
        ResultSet rs = pm.executeQuery();
        while (rs.next()){
            _lst.add(new BanHang(rs.getInt(1), rs.getString(2), rs.getInt(3), rs.getString(4), rs.getInt(5),
                    rs.getInt(6), rs.getInt(7), rs.getInt(8) ));
        }
        return _lst;
    }


}
