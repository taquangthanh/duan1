package dao;

import model.SanPhamChiTiet;
import untils.Connectt;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class serviceSanPhamChiTiet {
    List<SanPhamChiTiet> _list;
    Connectt con = new Connectt();
    public serviceSanPhamChiTiet(){
        _list = new ArrayList<>();
    }

    // lấy hết dữ liệu từ db đổ vào list
    public List<SanPhamChiTiet> get_list() throws SQLException {
        String sql = "select * from chiTietSP join SanPham SP on SP.id = chiTietSP.id_sp join loaiSanPham lSP on lSP.id = SP.id_loaisp";
        PreparedStatement pm = con.con().prepareStatement(sql);
        ResultSet rs = pm.executeQuery();
        _list.clear();
        while (rs.next()) {
            _list.add(new SanPhamChiTiet(rs.getInt(15), rs.getString(16), rs.getInt(2),rs.getString(13), rs.getString(14), rs.getInt(1),rs.getString(3), rs.getString(4), rs.getInt(5),rs.getDouble(6), rs.getDouble(7), rs.getString(8), rs.getInt(9), rs.getString(10)));
        }
        return _list;
    }

    // thêm chi tiết cho sản phẩm
    public String addChiTiet(SanPhamChiTiet sp) throws SQLException {
        String sql = "INSERT INTO chiTietSP (id_sp, color, sizesp, soluong, giaban, giavon, ngaynhap,trangthai, hinh) VALUES (?,?,?,0,?,?,null,1, ?)";
        PreparedStatement pm = con.con().prepareStatement(sql);
        pm.setInt(1, sp.getIdSP());
        pm.setString(2, sp.getColor());
        pm.setString(3, sp.getSize());
        pm.setInt(4, (int) sp.getGiaBan());
        pm.setInt(5, (int) sp.getGiaVon());
        pm.setString(6, sp.getHinh());
        if(checkAnh(sp.getHinh())){
            return "Hình này đã thuộc về sản phẩm khác";
        }
        if(checkThuocTinh(sp) > 0){
            return "đã tồn tại mặt hàng này";
        }
        if (pm.executeUpdate() > 0) {
            return "Thêm thành công";
        }
        return "Thêm không thành công";
    }

    // sửa chi tiết cho sản phảm
    public String updateSanPham(SanPhamChiTiet sp) throws  SQLException{
        String sql = "update chiTietSP set id_sp = ?, color = ?, sizesp = ?, giaban = ?, giavon = ?, hinh = ? where id = ?";
        PreparedStatement pm = con.con().prepareStatement(sql);
        pm.setInt(1, sp.getIdSP());
        pm.setString(2, sp.getColor());
        pm.setString(3, sp.getSize());
        pm.setInt(4, (int) sp.getGiaBan());
        pm.setInt(5, (int) sp.getGiaVon());
        pm.setString(6, sp.getHinh());
        pm.setInt(7, sp.getIdChiTiet());
        if(checkAnh(sp.getHinh())){
            return "Hình này đã thuộc về sản phẩm khác";
        }
        if(checkThuocTinh(sp) > 2){
            return "đã tồn tại mặt hàng này";
        }
        System.out.println(sp.getIdChiTiet());
        if (pm.executeUpdate() > 0) {
            return "Sửa thành công";
        }
        return "Sửa không thành công";
    }


    // update lại ngày nhập khi đã nhập đơn hàng
    public void updateDay(int id) throws SQLException{
        String sql = "update chiTietSP set ngaynhap = ? where id = ? ";
        PreparedStatement pm = con.con().prepareStatement(sql);
        pm.setDate(1, Date.valueOf(String.valueOf(java.time.LocalDate.now())));
        pm.setInt(2, id);
        pm.executeUpdate();
    }

    // phương thức check trùng ảnh
    public boolean checkAnh(String hinh) throws SQLException {
        String sql = "select * from chiTietSP where hinh = ?";
        PreparedStatement pm1 = con.con().prepareStatement(sql);
        pm1.setString(1, hinh);
        ResultSet rs = pm1.executeQuery();
        return rs.next();
    }

    // phương thức check trùng thuộc tính
    public int checkThuocTinh(SanPhamChiTiet sp) throws  SQLException{
        String sql1 = "select count(*) from chiTietSp where id_sp = ? and color = ? and sizesp = ?";
        PreparedStatement pm1 = con.con().prepareStatement(sql1);
        pm1.setInt(1, sp.getIdSP());
        pm1.setString(2, sp.getColor());
        pm1.setString(3, sp.getSize());
        ResultSet rs = pm1.executeQuery();
        if(rs.next()){
            return  rs.getInt(1);
        }
        return -1;
    }

}
