package dao;

import com.microsoft.sqlserver.jdbc.SQLServerException;
import model.NguonHang;
import model.NhanVien;
import untils.Connectt;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class serviceNguonHang {
    Connectt con = new Connectt();
    List<NguonHang> _list;
    List<NguonHang> _lstXoa;

    public serviceNguonHang() {
        _list = new ArrayList<>();
        _lstXoa = new ArrayList<>();
    }


    // phương thức thêm nguồn hàng
    public String themNguonHang(NguonHang nguonHang) throws SQLException {
        String sql  = "insert into nguonhang values (?, ?,?,1) ";
        PreparedStatement pm = con.con().prepareStatement(sql);
        pm.setString(1, nguonHang.getTenNguonHang());
        pm.setString(2, nguonHang.getDiaChi());
        pm.setString(3, nguonHang.getSdt());
        if(getIndex(nguonHang.getSdt()) != -1){
            return "số điện thoại nguồn hàng này đã tồn tại";
        }
        if (pm.executeUpdate() > 0) {
            return "Thêm nguồn hàng thành công";
        }
        return "Thêm không thành công";
    }


    //Thêm lại một nguồn hàng từ thùng rác
    public String themLai(int ma) throws SQLException {
        String sql = "update nguonhang set  isActive = 1 WHERE id = ?";
        PreparedStatement pm = con.con().prepareStatement(sql);
        pm.setInt(1, ma);
        if (pm.executeUpdate() > 0) {
            return "Thêm lại thành công";
        }
        return "Thêm lại thất bại";
    }



    //Xóa nguồn hàng vào thùng rác
    public String deleteNH(int ma) throws SQLException {
        String sql = "update nguonhang set  isActive = 0 WHERE id = ?";
        PreparedStatement pm = con.con().prepareStatement(sql);
        pm.setInt(1, ma);
        if (pm.executeUpdate() > 0) {
            return "Xóa thành công";
        }
        return "Xóa thất bại";
    }


    //Sửa Nguồn hàng
    public String updateNH(NguonHang nguonHang, int id) throws SQLException {
        String sql = "update nguonhang set tenNguonHang= ?, diachi = ?, sdt = ? WHERE id = ?";
        PreparedStatement pm = con.con().prepareStatement(sql);
        pm.setString(1, nguonHang.getTenNguonHang());
        pm.setString(2, nguonHang.getDiaChi());
        pm.setString(3, nguonHang.getSdt());
        pm.setInt(4, id);
        if (pm.executeUpdate() > 0) {
            return "Sửa Thành công";
        }
        return "Sửa Thất Bại";

    }



    //Lấy danh sách nguồn hàng từ database cho vô list
    public List<NguonHang> getlist() throws SQLException {
        String sql = "select * from nguonhang where isActive = 1";
        PreparedStatement pm = con.con().prepareStatement(sql);
        ResultSet rs = pm.executeQuery();
        _list.clear();
        while (rs.next()) {
            NguonHang nguonHang = new NguonHang(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4));
            _list.add(nguonHang);
        }
        return _list;
    }



    // Tìm nguồn hàng  đang làm việc theo mã
    public int getIndex(String sdt) {
        for (int i = 0; i < _list.size(); i++) {
            if (_list.get(i).getSdt().equals(sdt)) {
                return _list.get(i).getId();
            }
        }
        return -1;
    }


    // Tìm nguồn hàng  đang làm việc theo tên
    public int getID(String name) {
        for (int i = 0; i < _list.size(); i++) {
            if (_list.get(i).getTenNguonHang().equals(name)) {
                return _list.get(i).getId();
            }
        }
        return -1;
    }

    // Tìm nguồn hàng tên nguồn hàng theo ID
    public String getTen(int id) {
        for (int i = 0; i < _list.size(); i++) {
            if (_list.get(i).getId() == id) {
                return _list.get(i).getTenNguonHang();
            }
        }
        return null;
    }



    //Lấy danh sách nguồn hàng bị xóa từ database cho vô list
    public List<NguonHang> getlistXoa() throws SQLException {
        String sql = "select * from nguonhang where isActive = 0";
        PreparedStatement pm = con.con().prepareStatement(sql);
        ResultSet rs = pm.executeQuery();
        _lstXoa.clear();
        while (rs.next()) {
            NguonHang nguonHang = new NguonHang(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4));
            _lstXoa.add(nguonHang);
        }
        return _lstXoa;
    }
}
