package dao;

import model.Trahang;
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

public class serviceTrahang {
    List<Trahang> _lst;
    Connectt con = new Connectt();
    public serviceTrahang(){
        _lst = new ArrayList<>();
    }


    public void taoMotHoaDonTraHang(String ma, int idKH, String ngay, long giaTriDon) throws SQLException {
        String sql = "insert into hoaDonTraHang values ( ? ,?,?,?)";
        PreparedStatement pm = con.con().prepareStatement(sql);
        pm.setString(1,ma);
        pm.setInt(2,idKH);
        pm.setDate(3, Date.valueOf(ngay));
        pm.setLong(4,giaTriDon);
        pm.executeUpdate();
    }

    public List<Trahang> getList() throws SQLException{
        String sql = "select * from hoadontrahang";
        PreparedStatement pm = con.con().prepareStatement(sql);
        ResultSet rs = pm.executeQuery();
        _lst.clear();
        while (rs.next()){
            _lst.add(new Trahang(rs.getInt(1),rs.getString(2), rs.getInt(3), rs.getString(4), rs.getLong(5) ));

        }
        return _lst;
    }

    public void inChiTietHoaDon (int id, DefaultTableModel _dtm) throws SQLException{
        String sql = "select idHoaDon, tensp, chiTietHoaDonTra.soLuong, giaSanPham from chiTietHoaDonTra join chiTietSP cTS on cTS.id = chiTietHoaDonTra.idChiTietSP join SanPham SP on SP.id = cTS.id_sp where idHoaDon = ?";
        PreparedStatement pm = con.con().prepareStatement(sql);
        pm.setInt(1,id);
        ResultSet rs = pm.executeQuery();
        _dtm.setRowCount(0);
        while (rs.next()){
            _dtm.addRow(new Object[]{
                    rs.getString(1),rs.getString(2),rs.getString(3),toCurrency(rs.getLong(4))
            });
        }

    }
    // quy đổi tiền
    public static String toCurrency(long tienTe){
        Locale lc = new Locale("vi","VN");
        NumberFormat nf = NumberFormat.getCurrencyInstance(lc);
        return  nf.format(tienTe);
    }

    public int idHoaDon(String ma, int idKH, String ngay, long giaTriDon) throws SQLException {
        String sql = "select * from hoaDonTraHang where manv = ? and idKhachHang = ? and ngaytra = ? and giaTriDon = ?";
        PreparedStatement pm = con.con().prepareStatement(sql);
        pm.setString(1,ma);
        pm.setInt(2,idKH);
        pm.setDate(3, Date.valueOf(ngay));
        pm.setLong(4,giaTriDon);
        ResultSet rs = pm.executeQuery();
        if(rs.next()){
            return rs.getInt(1);
        }
        return  -1;
    }

    public void taoChiTietHoaDonTra(int idHoaDon, int idSp, int soLuong, long gia) throws SQLException{
        String sql = "insert into chiTietHoaDonTra values (?,?,?,?)";
        PreparedStatement pm = con.con().prepareStatement(sql);
        pm.setInt(1,idHoaDon);
        pm.setInt(2,idSp);
        pm.setInt(3, soLuong);
        pm.setLong(4,gia);
        pm.executeUpdate();
    }
}
