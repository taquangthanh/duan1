package dao;

import model.SanPham;
import untils.Connectt;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class serviceSanPham {
    Connectt con = new Connectt();
    List<SanPham> _list;
    public serviceSanPham(){
        _list = new ArrayList<>();
    }

    // thêm sản phẩm
    public String themSP(SanPham sanPham) throws SQLException {
        String sql = "INSERT INTO sanpham VALUES (?,?,?)";
        PreparedStatement pm = con.con().prepareStatement(sql);
        pm.setInt(1, sanPham.getId());
        pm.setString(2, sanPham.getName());
        pm.setString(3, sanPham.getMoTa());
        if(!checkTen(sanPham.getName())){
            return "Sản phẩm này đã tồn tại";
        }
        if (pm.executeUpdate() > 0) {
            return "Thêm thành công";
        }
        return "Thêm không thành công";
    }

    // phương thức sửa sản phẩm
    public String suaSP(SanPham sanPham, int id) throws SQLException{
        String sql = "update sanpham set tensp = ?, mota = ?, id_loaisp = ? where id = ?";
        PreparedStatement pm = con.con().prepareStatement(sql);
        pm.setString(1, sanPham.getName());
        pm.setString(2, sanPham.getMoTa());
        pm.setInt(3, sanPham.getId());
        pm.setInt(4, id);
        if(!checkTen(sanPham.getName())){
            return "Sản phẩm này đã tồn tại";
        }
        if (pm.executeUpdate() > 0) {
            return "Sửa thành công";
        }
        return "Sửa không thành công";
    }

    // lấy hết loại hàng từ db ra để đổ vào list
    public List<SanPham> get_list() throws SQLException {
        String sql = "select * from SanPham join loaiSanPham lSP on SanPham.id_loaisp = lSP.id";
        PreparedStatement pm = con.con().prepareStatement(sql);
        ResultSet rs = pm.executeQuery();
        _list.clear();
        while (rs.next()) {
            _list.add(new SanPham(rs.getInt(5), rs.getString(6), rs.getInt(1), rs.getString(3), rs.getString(4)));
        }
        return _list;
    }


    // lấy ra id 1 sản phẩm
    public int get1Loai(String name) throws SQLException {
        String sql = "select * from sanpham where tensp = N'" + name +"'";
        PreparedStatement pm = con.con().prepareStatement(sql);
        ResultSet rs = pm.executeQuery();
        if(rs.next()){
            return rs.getInt(1);
        }
        return -1;
    }

    // lấy sản phẩm theo tên loại
    public List<SanPham> getSP(String name) throws SQLException{
        String sql = "select * from SanPham JOIN loaiSanPham lSP on SanPham.id_loaisp = lSP.id and lSP.tenLoai = ?";
        PreparedStatement pm = con.con().prepareStatement(sql);
        pm.setString(1, name);
        ResultSet rs = pm.executeQuery();
        _list.clear();
        while (rs.next()) {
            _list.add(new SanPham(rs.getInt(5), rs.getString(6), rs.getInt(1), rs.getString(3), rs.getString(4)));
        }
        return _list;
    }





    // tìm tên loại để tránh trùng tên
    public boolean checkTen(String name){
        for (int i = 0; i < _list.size(); i++) {
            if(_list.get(i).getName().equals(name)){
                return false;
            }
        }
        return true;
    }
}
