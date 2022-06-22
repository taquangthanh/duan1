package model;

public class KhachHang {
    private int id;
    private String ten, sdt;
    private int diem;

    public KhachHang() {
    }

    public KhachHang(int id, String ten, String sdt, int diem) {
        this.id = id;
        this.ten = ten;
        this.sdt = sdt;
        this.diem = diem;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTen() {
        return ten;
    }

    public void setTen(String ten) {
        this.ten = ten;
    }

    public String getSdt() {
        return sdt;
    }

    public void setSdt(String sdt) {
        this.sdt = sdt;
    }

    public int getDiem() {
        return diem;
    }

    public void setDiem(int diem) {
        this.diem = diem;
    }
}
