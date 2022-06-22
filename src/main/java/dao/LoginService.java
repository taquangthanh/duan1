package dao;

import model.NhanVien;
import untils.Connectt;

import javax.swing.*;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LoginService {
    Connectt con = new Connectt();
    String acconut[] = new String[2];




    // phương thức đăng nhập
    public NhanVien login (String username, String password) throws SQLException {
        NhanVien nv = new NhanVien();
        String sql = "select * from nhanvien where manv = ? and matkhau = ? and isActive = 1";
        PreparedStatement pm = con.con().prepareStatement(sql);
        pm.setString(1, username);
        pm.setString(2, password);
        ResultSet rs = pm.executeQuery();
        if(rs.next()) {
            nv.setManv(rs.getString(1));
            nv.setMatkhau(rs.getString(6));
            nv.setRole(rs.getInt(7));
            return nv;
        }
        JOptionPane.showMessageDialog(null, "Bạn đã nhập sai acconut, vui lòng nhập lại");
        return null;
    }

    //phương thức khóa tài khoản
    public String khoaTK(String username) throws SQLException {
        String sql = "update nhanvien set isActive = 0 where manv = ?";
        PreparedStatement pm = con.con().prepareStatement(sql);
        pm.setString(1, username);
        if(pm.executeUpdate() > 0){
            return "tài khoản của bạn đã bị khóa";
        }
        return null;
    }


    // phương thức này dùng để nhớ mk
    public void remember(String username, String password) throws SQLException {
        String sql = "update remeber set manv = ?, matkhau = ? WHERE id = 1";
        PreparedStatement pm = con.con().prepareStatement(sql);
        pm.setString(1, username);
        pm.setString(2, password);
        pm.executeUpdate();
    }

    // xài mảng lưu acc
    public String[] acconutDaLuu() throws SQLException{
        String sql1 = "select * from remeber where id = 1";
        PreparedStatement pmm = con.con().prepareStatement(sql1);
        ResultSet rs = pmm.executeQuery();
        if (rs.next()) {
            acconut[0] = rs.getString(2);
            acconut[1] = rs.getString(3);
        }
        return acconut;
    }
}
