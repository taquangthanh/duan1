package model;

public class NhanVien {
    private String manv, hoten, daichi, sdt, email, matkhau;
    private int role;

    public NhanVien() {
    }

    public NhanVien(String manv, String hoten, String daichi, String sdt, String email, String matkhau, int role) {
        this.manv = manv;
        this.hoten = hoten;
        this.daichi = daichi;
        this.sdt = sdt;
        this.email = email;
        this.matkhau = matkhau;
        this.role = role;
    }

    public String getManv() {
        return manv;
    }

    public void setManv(String manv) {
        this.manv = manv;
    }

    public String getHoten() {
        return hoten;
    }

    public void setHoten(String hoten) {
        this.hoten = hoten;
    }

    public String getDaichi() {
        return daichi;
    }

    public void setDaichi(String daichi) {
        this.daichi = daichi;
    }

    public String getSdt() {
        return sdt;
    }

    public void setSdt(String sdt) {
        this.sdt = sdt;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMatkhau() {
        return matkhau;
    }

    public void setMatkhau(String matkhau) {
        this.matkhau = matkhau;
    }

    public int getRole() {
        return role;
    }

    public void setRole(int role) {
        this.role = role;
    }
}
