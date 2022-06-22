package model;

public class NguonHang {
    private int id;
    private String tenNguonHang, diaChi, sdt;

    public NguonHang() {
    }

    public NguonHang(int id, String tenNguonHang, String diaChi, String sdt) {
        this.id = id;
        this.tenNguonHang = tenNguonHang;
        this.diaChi = diaChi;
        this.sdt = sdt;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTenNguonHang() {
        return tenNguonHang;
    }

    public void setTenNguonHang(String tenNguonHang) {
        this.tenNguonHang = tenNguonHang;
    }

    public String getDiaChi() {
        return diaChi;
    }

    public void setDiaChi(String diaChi) {
        this.diaChi = diaChi;
    }

    public String getSdt() {
        return sdt;
    }

    public void setSdt(String sdt) {
        this.sdt = sdt;
    }
}
