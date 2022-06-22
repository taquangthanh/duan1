package dao;

import model.LoaiSP;
import untils.Connectt;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class serviceLoaiSP {
    Connectt con = new Connectt();
    List<LoaiSP> _list;
    public serviceLoaiSP(){
        _list = new ArrayList<>();
    }

    // thêm loại sản phẩm
    public String themLSP(String name) throws SQLException {
        if(name.equals("")){
             return "Không được để trống tên loại";

        }
        String sql = "INSERT INTO loaisanpham VALUES (N'" +name+"')";
        PreparedStatement pm = con.con().prepareStatement(sql);
        if(!checkTen(name)){
            return "Loại này đã tồn tại";
        }
        if (pm.executeUpdate() > 0) {
            return "Thêm thành công";
        }
        return "Thêm không thành công";
    }

    // sửa loại sản phẩm
    public String suaLSP(String name, String oldName) throws SQLException {
        if(name.equals("")){
            return "Không được để trống tên loại";

        }
        String sql = "update loaisanpham set tenloai = ? where tenloai = ?";
        PreparedStatement pm = con.con().prepareStatement(sql);
        pm.setString(1, name);
        pm.setString(2, oldName);
        if(!checkTen(name)){
            return "Loại này đã tồn tại";
        }
        if (pm.executeUpdate() > 0) {
            return "Sửa thành công";
        }
        return "Sửa không thành công";
    }

    // lấy tên nguồn hàng theo tên sản phẩm
    public String layTenNguonHang(String ten)throws SQLException{
        String sql = "select tenLoai from loaiSanPham join SanPham SP on loaiSanPham.id = SP.id_loaisp where tensp = N'" + ten +"'";
        PreparedStatement pm = con.con().prepareStatement(sql);
        ResultSet rs = pm.executeQuery();
        if(rs.next()){
            return rs.getString(1);
        }
        return null;
    }


    // lấy hết loại hàng từ db ra để đổ vào list
    public List<LoaiSP> get_list() throws SQLException {
        String sql = "select * from loaisanpham";
        PreparedStatement pm = con.con().prepareStatement(sql);
        ResultSet rs = pm.executeQuery();
        _list.clear();
        while (rs.next()) {
            LoaiSP loaiSP = new LoaiSP(rs.getInt(1), rs.getString(2));
            _list.add(new LoaiSP(rs.getInt(1), rs.getString(2)));
        }
        return _list;
    }


    // lấy ra id 1 loại hàng
    public int get1Loai(String name) throws SQLException {
        String sql = "select * from loaisanpham where tenLoai = N'" + name +"'";
        PreparedStatement pm = con.con().prepareStatement(sql);
        ResultSet rs = pm.executeQuery();
        if(rs.next()){
            return rs.getInt(1);
        }
        return -1;
    }

    // tìm tên loại để tránh trùng tên
    public boolean checkTen(String name){
        for (int i = 0; i < _list.size(); i++) {
            if(_list.get(i).getTen().equals(name)){
                return false;
            }
        }
        return true;
    }
}
