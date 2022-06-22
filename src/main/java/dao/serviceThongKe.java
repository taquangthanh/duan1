package dao;

import untils.Connectt;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class serviceThongKe {
    Connectt con = new Connectt();

    // doanh số
    public String [] loadDoanhSo(int nam, int thang) throws SQLException {
        String [] a = new String[4];
        String sql = "select  count(distinct chiTietSP.id) soSPBanDuoc, sum(cTHDB.giaSanPham) tongDonHangBan from chiTietSP join chiTietHoaDonBan cTHDB on chiTietSP.id = cTHDB.idChiTietSP where year(cTHDB.ngayBan) = " + nam + " and month(cTHDB.ngayBan) = " + thang + " group by cTHDB.ngayBan";
        PreparedStatement pm = con.con().prepareStatement(sql);
        ResultSet rs = pm.executeQuery();
        a[0] = "0";
        a[1] = "0";
        while (rs.next()){
            a[0] = String.valueOf(Integer.parseInt(a[0]) + rs.getInt(1));
            a[1] = String.valueOf(Integer.parseInt(a[1]) + rs.getInt(2));
        }

        if(a[0].equals("0")) {
            a[0] = "00";
            a[1] = "00";
        }

        String sql1 = "select count(distinct chiTietSP.id) soSPNhap, sum(cTHDN.giaSanPham) tongVon from chiTietSP join chiTietHoaDonNhap cTHDN on chiTietSP.id = cTHDN.idChiTietSp where year(chiTietSP.ngaynhap) = " + nam + " and month(chiTietSP.ngaynhap) = " + thang;
        PreparedStatement pm1 = con.con().prepareStatement(sql1);
        ResultSet rs1 = pm1.executeQuery();
        if(rs1.next()){
            a[2] = rs1.getString(1);
            a[3] = rs1.getString(2);
        }else {
            a[2] = "00";
            a[3] = "00";
        }
        return a;
    }

    public void loadSP(int year, DefaultTableModel dtm) throws SQLException{
        String a [] = new String[3];
        String sql = "select SP.id,SP.tensp, count( chiTietSP.id) from chiTietSP join chiTietHoaDonBan cTHDB on chiTietSP.id = cTHDB.idChiTietSP join SanPham SP on chiTietSP.id_sp = SP.id where year(cTHDB.ngayBan) = "+ year +" group by SP.tensp,cTHDB.ngayBan, SP.id, chiTietSP.id";
        PreparedStatement pm = con.con().prepareStatement(sql);
        ResultSet rs = pm.executeQuery();
        while (rs.next()){
            a[0] = rs.getString(1);
            a[1] = rs.getString(2);
            a[2] = rs.getString(3);
            dtm.addRow(new Object[]{
                    a[0], a[1], a[2]
            });
        }
        if(a[0] == null){
            dtm.setRowCount(0);
            JOptionPane.showMessageDialog(null, "Chưa có dữ liệu cho thời gian này");
        }
    }
}
