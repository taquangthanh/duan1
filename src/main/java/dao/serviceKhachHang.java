package dao;

import model.KhachHang;
import untils.Connectt;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class serviceKhachHang {
    List<KhachHang> _lst;
    Connectt con = new Connectt();

    public serviceKhachHang(){
        _lst = new ArrayList<>();
    }

    // phương thức thêm khách hàng
    public String themKhachHang(KhachHang khachHang) throws SQLException {
        String sql  = "insert into khachhang values (?, ?,?) ";
        PreparedStatement pm = con.con().prepareStatement(sql);
        pm.setString(1, khachHang.getTen());
        pm.setString(2, khachHang.getSdt());
        pm.setInt(3, khachHang.getDiem());
//        if(getIndex(khachHang.getSdt()) != -1){
//            return "khách hàng  này đã có";
//        }
        if (pm.executeUpdate() > 0) {
            return "Thêm khách hàng thành công";
        }
        return "Thêm không thành công";
    }


    // phương thức sửa khách hàng
    public String suaKhachHang(KhachHang khachHang) throws SQLException {
        String sql  = "update khachhang set tenKH = ?, sdt = ? where id = ?";
        System.out.println(khachHang.getId());
        PreparedStatement pm = con.con().prepareStatement(sql);
        pm.setString(1, khachHang.getTen());
        pm.setString(2, khachHang.getSdt());
        pm.setInt(3, khachHang.getId());
        if(getIndex(khachHang.getSdt()) == -2){
            return "số điện thoại muốn sửa đã tồn tại";
        }
        if (pm.executeUpdate() > 0) {
            return "sửa khách hàng thành công";
        }
        return "sửa không thành công";
    }

    //Lấy danh sách khách hàng từ database cho vô list
    public List<KhachHang> getlist() throws SQLException {
        String sql = "select * from khachhang ";
        PreparedStatement pm = con.con().prepareStatement(sql);
        ResultSet rs = pm.executeQuery();
        _lst.clear();
        while (rs.next()) {
            KhachHang khachHang = new KhachHang(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getInt(4));
            _lst.add(khachHang);
        }
        return _lst;
    }

    // Tìm nguồn hàng  đang làm việc theo mã
    public int getIndex(String sdt) {
        int dem = 0;
        int i = 0;
        for (; i < _lst.size(); i++) {
            if (_lst.get(i).getSdt().equals(sdt)) {
                dem++;
            }
            if(dem == 2){
                break;
            }
        }
        if(dem == 1){
            return i;
        }else if (dem == 2){
            return -2;
        }
        return -1;
    }
}
