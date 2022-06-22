package dao;

import model.NhanVien;
import untils.Connectt;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class serviceNhanVien {
    Connectt con = new Connectt();
    List<NhanVien> _list;
    List<NhanVien> _lstXoa;


    public serviceNhanVien() {
        _list = new ArrayList<>();
        _lstXoa = new ArrayList<>();
    }


    // Thêm Nhân Viên
    public String addNV(NhanVien nv) throws SQLException {
        String sql = "INSERT INTO nhanvien VALUES (?,? ,?,? ,?, ?,?,?)";
        PreparedStatement pm = con.con().prepareStatement(sql);
        pm.setString(1, nv.getManv());
        pm.setString(2, nv.getHoten());
        pm.setString(3, nv.getDaichi());
        pm.setString(4, nv.getSdt());
        pm.setString(5, nv.getEmail());
        pm.setString(6,nv.getMatkhau());
        pm.setInt(7,nv.getRole());
        pm.setInt(8,1);
        if(getIndex(nv.getManv()) != -1){
            return "Mã nhân viên này đã tồn tại";
        }
        if (pm.executeUpdate() > 0) {
            _list.add(nv);
            return "Thêm nhân viên thành công";
        }
        return "Thêm không thành công";
    }



    //Sửa Nhân Viên
    public String updateNV(NhanVien nv) throws SQLException {
        String sql = "update nhanvien set hoten= ?, diachi = ?, sdt = ?, email = ?, matkhau = ?, isrole = ? WHERE manv = ?";
        PreparedStatement pm = con.con().prepareStatement(sql);
        pm.setString(7, nv.getManv());
        pm.setString(1, nv.getHoten());
        pm.setString(2, nv.getDaichi());
        pm.setString(3, nv.getSdt());
        pm.setString(4, nv.getEmail());
        pm.setString(5,nv.getMatkhau());
        pm.setInt(6,nv.getRole());
        if (getIndex(nv.getManv()) == -1) {
            return "Không tìm thấy nhân viên này";
        }
        if (pm.executeUpdate() > 0) {

            _list.set(getIndex(nv.getManv()), nv);
            return "Sửa Thành công";
        }
        return "Sửa Thất Bại";

    }


    //Thêm lại 1 nhân viên từ thùng rác
    public String themLai(String ma) throws SQLException {
        String sql = "update nhanvien set  isActive = 1 WHERE manv = ?";
        PreparedStatement pm = con.con().prepareStatement(sql);
        pm.setString(1, ma);
        if (getIndexXoa(ma) == -1){
            return "Không tìm thấy nhân viên này ở thùng rác";
        }
        if (pm.executeUpdate() > 0) {
            return "Thêm lại thành công";
        }
        return "Thêm lại thất bại";
    }

    // xóa nhân viên vĩnh viễn
    public boolean boNhanVien(String ma) throws SQLException{
        String sql = "select * from hoaDonNhapHang where manv = ?";
        PreparedStatement pm = con.con().prepareStatement(sql);
        pm.setString(1, ma);
        ResultSet rs = pm.executeQuery();
        if(rs.next()){
            return false;
        }
        String sql1 = "select * from hoaDonBanHang where manv = ?";
        PreparedStatement pm1 = con.con().prepareStatement(sql1);
        pm1.setString(1, ma);
        ResultSet rs1 = pm.executeQuery();
        if(rs1.next()){
            return false;
        }
        String sql2 = "delete from nhanvien where manv = ?";
        PreparedStatement pm2 = con.con().prepareStatement(sql2);
        pm2.setString(1, ma);
        pm2.executeUpdate();
        return true;
    }


    //Xóa nhân viên vào thùng rác
    public String deleteNV(String ma) throws SQLException {
        String sql = "update nhanvien set  isActive = 0 WHERE manv = ?";
        PreparedStatement pm = con.con().prepareStatement(sql);
        pm.setString(1, ma);
        int index = getIndex(ma);
        if (index == -1) {
            return "Không tìm thấy nhân viên này";
        }
        if (pm.executeUpdate() > 0) {
            _list.remove(index);
            return "Xóa thành công";
        }
        return "Xóa thất bại";
    }

    //Lấy danh sách nhân viên từ database
    public List<NhanVien> getlist() throws SQLException {
        String sql = "select * from nhanvien where isActive = 1";
        PreparedStatement pm = con.con().prepareStatement(sql);
        ResultSet rs = pm.executeQuery();
        while (rs.next()) {
            NhanVien nv = new NhanVien(rs.getString(1),rs.getString(2),rs.getString(3),rs.getString(4),rs.getString(5),rs.getString(6),rs.getInt(7));
            if (getIndex(nv.getManv()) != -1) {
                continue;
            } else {
                _list.add(nv);
            }
        }
        return _list;
    }

    //Lấy danh sách nhân viên đã bị xóa từ database
    public List<NhanVien> getlistXoa() throws SQLException {
        String sql = "select * from nhanvien where isActive = 0";
        PreparedStatement pm = con.con().prepareStatement(sql);
        ResultSet rs = pm.executeQuery();
        _lstXoa.clear();
        while (rs.next()) {
            NhanVien nv = new NhanVien(rs.getString(1),rs.getString(2),rs.getString(3),rs.getString(4),rs.getString(5),rs.getString(6),rs.getInt(7));
            _lstXoa.add(nv);

        }
        return _lstXoa;
    }




    // phương thức đổi mật khẩu ở form đổi mật khẩu
    public String doiMatKhau(String manv, String passwordNew, String passOld) throws SQLException{
        String sql = "update nhanvien set matkhau = ? WHERE manv = ? and matkhau = ?";
        PreparedStatement pm = con.con().prepareStatement(sql);
        pm.setString(2, manv);
        pm.setString(1, passwordNew);
        pm.setString(3, passOld);
        if (pm.executeUpdate() > 0) {
            return "Đổi Mật Khẩu Thành Công";
        }
        return "Đổi Mật Khẩu Thất Bại";
    }

    //Phương thức đổi mật khẩu khi quên ở form quên mật khẩu
    public String updatePassNVQuen(String manv, String passwordNew, String email) throws SQLException {
        String sql = "update nhanvien set matkhau = ? WHERE manv = ? and email = ?";
        PreparedStatement pm = con.con().prepareStatement(sql);
        pm.setString(2, manv);
        pm.setString(1, passwordNew);
        pm.setString(3, email);

        if (pm.executeUpdate() > 0) {
            return "Đổi Mật Khẩu Thành Công";
        }
        return "Đổi Mật Khẩu Thất Bại";

    }

    // Tìm nhân viên đang làm việc theo mã
    public int getIndex(String ma) {
        for (int i = 0; i < _list.size(); i++) {
            if (_list.get(i).getManv().equals(ma)) {
                return i;
            }
        }
        return -1;
    }


    // Tìm nhân viên đã nghỉ việc theo mã
    public int getIndexXoa(String ma) {
        for (int i = 0; i < _lstXoa.size(); i++) {
            if (_lstXoa.get(i).getManv().equals(ma)) {
                return i;
            }
        }
        return -1;
    }
}
