/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.hanbell.kpi.control;


import cn.hanbell.kpi.ejb.ShoppingManufacturerBean;
import cn.hanbell.kpi.entity.ShoppingManufacturer;
import cn.hanbell.kpi.web.SuperSingleBean;
import com.lightshell.comm.BaseLib;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Date;
import java.util.List;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;

/**
 *
 * @author C2082
 */
@ManagedBean(name = "facturerManagedBean")
@SessionScoped
public class FacturerManagedBean extends SuperSingleBean<ShoppingManufacturer> {

    @EJB
    protected ShoppingManufacturerBean shoppingManufacturerBean;

    private List<ShoppingManufacturer> list;
    private String queryFacno;
    private String queryUserna;
    private String queryVdrna;
    private ShoppingManufacturer selectedPrupcm;

    private String inputFacno;
    private String inputVdrno;
    private String inputVdrna;
    private String inputUserno;
    private String inputUserna;
    private String inputMaterialTypeName;

    public FacturerManagedBean() {
        super(ShoppingManufacturer.class);
    }

    @Override
    public void init() {
        queryFacno="C";
        list = shoppingManufacturerBean.findByUsernaAndVdrna(queryFacno,queryUserna, queryVdrna);

    }

    @Override
    public void query() {
        try {
            list = shoppingManufacturerBean.findByUsernaAndVdrna(queryFacno,queryUserna, queryVdrna);
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "error", e.toString()));
        }
    }

    @Override
    public void persist() {
        ShoppingManufacturer p = new ShoppingManufacturer();
        p.setFacno(this.inputFacno);
        p.setVdrno(this.inputVdrno);
        p.setVdrna(this.inputVdrna);
        p.setUserno(this.inputUserno);
        p.setUserna(this.inputUserna);
        p.setMaterialTypeName(this.inputMaterialTypeName);
        try {
            shoppingManufacturerBean.persist(p);
            showInfoMsg("Info", "添加成功");
            this.inputFacno = "C";
            this.inputVdrno = "";
            this.inputVdrna = "";
            this.inputUserno = "";
            this.inputUserna = "";
            init();
        } catch (Exception ex) {
            showInfoMsg("Warn", "添加失败" + ex);
        }
    }

    @Override
    public void print() throws Exception {
        if (list == null || list.isEmpty()) {
            showInfoMsg("Warn", "暂无数据");
        }
        fileName = "采购中心厂商" + BaseLib.formatDate("yyyyMMddHHmmss", BaseLib.getDate()) + ".xls";
        String fileFullName = reportOutputPath + fileName;
        HSSFWorkbook wb = new HSSFWorkbook();
        CreationHelper createHelper = wb.getCreationHelper();
        CellStyle cellStyle = wb.createCellStyle();
        //创建内容
        Sheet sheet = wb.createSheet("采购中心厂商");
        Cell cell;
        Row row;
        row = sheet.createRow(0);
        row.createCell(0).setCellValue("公司别");
        row.createCell(1).setCellValue("厂商编号");
        row.createCell(2).setCellValue("厂商名称");
        row.createCell(3).setCellValue("物料");
        row.createCell(4).setCellValue("工号");
        row.createCell(5).setCellValue("名称");
        int i = 1;
        for (ShoppingManufacturer e : list) {
            row = sheet.createRow(i);
            row.createCell(0).setCellValue(e.getFacno());
            row.createCell(1).setCellValue(e.getVdrno());
            row.createCell(2).setCellValue(e.getVdrna());
            row.createCell(3).setCellValue(e.getMaterialTypeName());
            row.createCell(4).setCellValue(e.getUserno());
            row.createCell(5).setCellValue(e.getUserna());
            i++;
        }
        OutputStream os = null;
        try {
            os = new FileOutputStream(fileFullName);
            wb.write(os);
            this.reportViewPath = reportViewContext + fileName;
            this.preview();
        } catch (Exception ex) {
            showInfoMsg("Warn", "导出失败" + ex);
        } finally {
            try {
                if (null != os) {
                    os.flush();
                    os.close();
                }
            } catch (IOException ex) {
                showInfoMsg("Warn", "导出失败" + ex);

            }
        }
    }

    @Override
    public void reset() {

    }

    @Override
    public void update() {
        try {
            shoppingManufacturerBean.update(this.selectedPrupcm);
            showInfoMsg("Info", "修改成功");
        } catch (Exception ex) {
            showInfoMsg("Warn", "修改失败" + ex);
        }
    }

    public void handleFileUploadWhenNew(FileUploadEvent event) {
        UploadedFile file1 = event.getFile();
        Integer a = 0;
        InputStream inputStream = null;
        if (file1 != null) {
            try {
                inputStream = file1.getInputstream();
                upload(event);
                Workbook excel = WorkbookFactory.create(inputStream);
                Sheet sheet = excel.getSheetAt(0);
                for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                    Row row = sheet.getRow(i);
                    String facno = cellToVlaue(row.getCell(0));
                    String vdrno = cellToVlaue(row.getCell(1));
                    ShoppingManufacturer k = shoppingManufacturerBean.findByVdrno(vdrno);
                    if (k != null) {
                        shoppingManufacturerBean.delete(k);
                    }
                    ShoppingManufacturer purpcm = new ShoppingManufacturer();
                    purpcm.setFacno(facno);
                    purpcm.setVdrno(vdrno);
                    purpcm.setVdrna(cellToVlaue(row.getCell(2)));
                    purpcm.setMaterialTypeName(cellToVlaue(row.getCell(3)));
                    purpcm.setUserno(cellToVlaue(row.getCell(4)));
                    purpcm.setUserna(cellToVlaue(row.getCell(5)));
                    shoppingManufacturerBean.persist(purpcm);
                }
                FacesContext.getCurrentInstance().addMessage((String) null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Info", "上传成功"));
            } catch (Exception ex) {
                FacesContext.getCurrentInstance().addMessage((String) null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Info", "上传失败"));
                
                ex.printStackTrace();
            } finally {
                try {
                    inputStream.close();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        }
        init();
    }

    public boolean upload(FileUploadEvent event) throws FileNotFoundException, IOException {
        OutputStream output = null;
        InputStream input = null;
        UploadedFile file1 = event.getFile();
        try {
            input = file1.getInputstream();
            String finalFilePath = this.getClass().getProtectionDomain().getCodeSource().getLocation().getPath();
            int index = finalFilePath.indexOf("WEB-INF");
            String filePath = new String(finalFilePath.substring(1, index));
            StringBuffer pathString = new StringBuffer(filePath.concat("rpt/"));
            pathString.append(String.valueOf(new Date().getTime()));
            pathString.append(".xls");
            String path = pathString.substring(0, pathString.indexOf("KPI")).concat("FileUploadServer/resources").concat(String.valueOf(new Date().getTime()));
            StringBuffer url = new StringBuffer(pathString.substring(0, pathString.indexOf("KPI")));
            url.append("FileUploadServer/resources/").append(String.valueOf(new Date().getTime())).append(".xls");
            File dest = new File(url.toString());
            byte[] buf = new byte[1024];
            int bytesRead;
            output = new FileOutputStream(dest);
            while ((bytesRead = input.read(buf)) > 0) {
                output.write(buf, 0, bytesRead);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            input.close();
            output.close();
        }
        return true;
    }

    public String cellToVlaue(Cell cell) {
        if (cell == null) {
            return "";
        }
        int type = cell.getCellType();
        switch (type) {
            case 0:
                double d = cell.getNumericCellValue();
                //整数去掉小数点
                if (d == (int) d) {
                    return String.valueOf((int) d);
                }
                return String.valueOf(cell.getNumericCellValue());
            case 1:
                return cell.getStringCellValue();
            case 2:
                return cell.getCellFormula();
            case 3:
                return "0";
            case 4:
                return String.valueOf(cell.getBooleanCellValue());
            case 5:
                return String.valueOf(cell.getErrorCellValue());
        }
        return "";
    }

    public List<ShoppingManufacturer> getList() {
        return list;
    }

    public void setList(List<ShoppingManufacturer> list) {
        this.list = list;
    }

    public String getQueryFacno() {
        return queryFacno;
    }

    public void setQueryFacno(String queryFacno) {
        this.queryFacno = queryFacno;
    }

    public String getQueryUserna() {
        return queryUserna;
    }

    public void setQueryUserna(String queryUserna) {
        this.queryUserna = queryUserna;
    }

    public String getQueryVdrna() {
        return queryVdrna;
    }

    public void setQueryVdrna(String queryVdrna) {
        this.queryVdrna = queryVdrna;
    }

    public String getInputFacno() {
        return inputFacno;
    }

    public void setInputFacno(String inputFacno) {
        this.inputFacno = inputFacno;
    }

    public String getInputVdrno() {
        return inputVdrno;
    }

    public void setInputVdrno(String inputVdrno) {
        this.inputVdrno = inputVdrno;
    }

    public String getInputVdrna() {
        return inputVdrna;
    }

    public void setInputVdrna(String inputVdrna) {
        this.inputVdrna = inputVdrna;
    }

    public String getInputUserno() {
        return inputUserno;
    }

    public void setInputUserno(String inputUserno) {
        this.inputUserno = inputUserno;
    }

    public String getInputUserna() {
        return inputUserna;
    }

    public void setInputUserna(String inputUserna) {
        this.inputUserna = inputUserna;
    }

    public String getInputMaterialTypeName() {
        return inputMaterialTypeName;
    }

    public void setInputMaterialTypeName(String inputMaterialTypeName) {
        this.inputMaterialTypeName = inputMaterialTypeName;
    }

    public ShoppingManufacturerBean getShoppingManufacturerBean() {
        return shoppingManufacturerBean;
    }

    public void setShoppingManufacturerBean(ShoppingManufacturerBean shoppingManufacturerBean) {
        this.shoppingManufacturerBean = shoppingManufacturerBean;
    }

    public ShoppingManufacturer getSelectedPrupcm() {
        return selectedPrupcm;
    }

    public void setSelectedPrupcm(ShoppingManufacturer selectedPrupcm) {
        this.selectedPrupcm = selectedPrupcm;
    }



}
