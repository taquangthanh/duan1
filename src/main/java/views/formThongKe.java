package views;

import dao.Log;
import dao.serviceThongKe;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.*;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;

public class formThongKe extends JFrame {
    private String user;
    private int role;
    private JPanel mainPanel;
    private JTabbedPane tabbedPane1;
    private JComboBox cbcNamDoanhThu;
    private JTable tblDoanhThu;
    private JTable tblDoanhSo;
    private JComboBox cbcNamDoanhSo;
    private JButton btnExcel;
    private JButton btnExcelDS;
    DefaultTableModel _dtm;
    DefaultTableModel _dtmDoanhSO;
    serviceThongKe serviceThongKe = new serviceThongKe();
    public formThongKe() throws IOException, SQLException {

        this.setTitle("Cửa sổ thống kê");
        this.setContentPane(mainPanel);
        this.setVisible(true);
        this.setSize(700, 400);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(2);
        _dtm = (DefaultTableModel) tblDoanhThu.getModel();
        _dtmDoanhSO = (DefaultTableModel) tblDoanhSo.getModel();
        _dtm.setColumnIdentifiers(new String[]{
              "Tháng", "Số sản phẩm bán", "Tổng giá bán", "Số sản phẩm mua", "Tổng giá vốn", "Doanh thu"
        });
        _dtmDoanhSO.setColumnIdentifiers(new String[]{
                "Mã sản phẩm", "Tên sản phẩm", "Số lượng bán"
        });
        loadCBC();
        loadTBLDoanhThu();
        loadDoanhSo();

        // mở chương trình và lưu giá trị
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowOpened(WindowEvent e) {
                luuText();
            }
        });

        //tắt chương trình quay lại form chính
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                formChinh formChinh = null;
                try {
                    formChinh = new formChinh();
                    formChinh.setUser(user);
                    formChinh.setRole(role);
                    dispose();
                } catch (IOException ex) {
                    try {
                        baoLoi(ex);
                    } catch (IOException exc) {
                        exc.printStackTrace();
                    }
                }

            }
        });

        cbcNamDoanhThu.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    loadTBLDoanhThu();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }

            }
        });
        cbcNamDoanhSo.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    loadDoanhSo();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        });

        // nút in ra excel
        btnExcel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                XSSFWorkbook wk = new XSSFWorkbook();
                XSSFSheet sheet = wk.createSheet("sheet 1");
                XSSFRow row = null;
                Cell cell = null;
                row = sheet.createRow(0);
                cell = row.createCell(0, CellType.STRING);
                cell.setCellValue("Thang");
                cell = row.createCell(1, CellType.STRING);
                cell.setCellValue("So San Pham");
                cell = row.createCell(2, CellType.STRING);
                cell.setCellValue("Tong Gia Ban");
                cell = row.createCell(3, CellType.STRING);
                cell.setCellValue("So San Pham mua");
                cell = row.createCell(4, CellType.STRING);
                cell.setCellValue("Tong Gia Von");
                cell = row.createCell(5, CellType.STRING);
                cell.setCellValue("Doanh Thu");

                for (int i = 1; i <= _dtm.getRowCount(); i++) {
                    row = sheet.createRow(i);
                    cell = row.createCell(0, CellType.STRING);
                    cell.setCellValue(String.valueOf(tblDoanhThu.getValueAt(i-1, 0)));
                    cell = row.createCell(1, CellType.STRING);
                    cell.setCellValue(String.valueOf(tblDoanhThu.getValueAt(i-1, 1)));
                    cell = row.createCell(2, CellType.STRING);
                    cell.setCellValue(String.valueOf(tblDoanhThu.getValueAt(i-1, 2)));
                    cell = row.createCell(3, CellType.STRING);
                    cell.setCellValue(String.valueOf(tblDoanhThu.getValueAt(i-1, 3)));
                    cell = row.createCell(4, CellType.STRING);
                    cell.setCellValue(String.valueOf(tblDoanhThu.getValueAt(i-1, 4)));
                    cell = row.createCell(5, CellType.STRING);
                    cell.setCellValue(String.valueOf(tblDoanhThu.getValueAt(i-1, 5)));
                }

                File file = new File("D:/hehe.xlsx");
                FileOutputStream fis = null;
                try {
                    fis = new FileOutputStream(file);
                } catch (FileNotFoundException ex) {
                    ex.printStackTrace();
                }
                try {
                    wk.write(fis);
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
                try {
                    fis.close();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }


            }
        });
        btnExcelDS.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                XSSFWorkbook wk = new XSSFWorkbook();
                XSSFSheet sheet = wk.createSheet("sheet 1");
                XSSFRow row = null;
                Cell cell = null;
                row = sheet.createRow(0);
                cell = row.createCell(0, CellType.STRING);
                cell.setCellValue("Ma San Pham");
                cell = row.createCell(1, CellType.STRING);
                cell.setCellValue("Ten San Pham");
                cell = row.createCell(2, CellType.STRING);
                cell.setCellValue("So Lượng Bán");

                for (int i = 1; i <= _dtmDoanhSO.getRowCount(); i++) {
                    row = sheet.createRow(i);
                    cell = row.createCell(0, CellType.STRING);
                    cell.setCellValue(String.valueOf(tblDoanhSo.getValueAt(i - 1, 0)));
                    cell = row.createCell(1, CellType.STRING);
                    cell.setCellValue(String.valueOf(tblDoanhSo.getValueAt(i - 1, 0)));
                    cell = row.createCell(2, CellType.STRING);
                    cell.setCellValue(String.valueOf(tblDoanhSo.getValueAt(i - 1, 0)));
                }

                File file = new File("./home/hihi.xlsx");
                FileOutputStream fis = null;
                try {
                    fis = new FileOutputStream(file);
                } catch (FileNotFoundException ex) {
                    ex.printStackTrace();
                }
                try {
                    wk.write(fis);
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
                try {
                    fis.close();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }

            }
        });
    }

    private void loadTBLDoanhThu() throws SQLException {
        _dtm.setRowCount(0);
        int stt = 1;

        while (stt <= 12){
            String a [] = serviceThongKe.loadDoanhSo(Integer.parseInt(cbcNamDoanhThu.getSelectedItem().toString()), stt);
            int tong = ((a[1] == null) ? 0 : Integer.parseInt(a[1])) - ((a[3] == null) ? 0 : Integer.parseInt(a[3]));
                _dtm.addRow(new Object[]{
                         stt,a[0],a[1], a[2],a[3], tong
                });
            stt++;
        }


    }

    private void loadDoanhSo()throws SQLException{
        _dtmDoanhSO.setRowCount(0);
        serviceThongKe.loadSP(Integer.parseInt(cbcNamDoanhSo.getSelectedItem().toString()), _dtmDoanhSO);
    }



    private void loadCBC(){
        cbcNamDoanhSo.removeAllItems();
        cbcNamDoanhThu.removeAllItems();
            Date date = new Date();
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat();
            simpleDateFormat.applyPattern("yyyy");
            String format = simpleDateFormat.format(date);
        for (int i = Integer.parseInt(format); i >= 2010; i--) {
                cbcNamDoanhThu.addItem(i);
                cbcNamDoanhSo.addItem(i);
        }
    }




    // set quyền + báo lỗi form
    public void setUser(String user) {
        this.user = user;
    }

    public void setRole(int role) {
        this.role = role;
    }

    // báo lỗi ngoại lệ
    private void baoLoi(Exception ex) throws IOException {
        Log log = new Log("hieupro.txt");
        log.logger.setLevel(Level.WARNING);
        JOptionPane.showMessageDialog(null, "gặp lỗi rồi! Quay lại để gửi lỗi cho admin nha");
        log.logger.info(ex.getMessage());
        log.logger.warning("lỗi bên form khách hàng");

        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        ex.printStackTrace(pw);
        String sStackTrace = sw.toString(); // stack trace as a string
        log.logger.severe(sStackTrace);
    }

    // đọc dữ liệu phân quyền lên form
    private void luuText() {
        System.out.println(user + " bên form thống kê");
    }


    public static void main(String[] args) throws IOException, SQLException {
        new formThongKe();
    }

}
