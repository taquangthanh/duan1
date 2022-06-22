package model;

public class SanPham extends LoaiSP{
    private int idSP;
    private String name, moTa;

    public SanPham(int id, String ten, int id1, String name, String moTa) {
        super(id, ten);
        this.idSP = id1;
        this.name = name;
        this.moTa = moTa;
    }

    public SanPham() {
    }


    public int getIdSP() {
        return idSP;
    }


    public void setIdSP(int id) {
        this.idSP = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMoTa() {
        return moTa;
    }

    public void setMoTa(String moTa) {
        this.moTa = moTa;
    }
}
