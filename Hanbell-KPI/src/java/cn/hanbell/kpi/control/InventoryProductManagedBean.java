/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.hanbell.kpi.control;

import cn.hanbell.kpi.ejb.InventoryDepartmentBean;
import cn.hanbell.kpi.ejb.InventoryProductBean;
import cn.hanbell.kpi.entity.InventoryProduct;
import cn.hanbell.kpi.lazy.InventoryProductModel;
import cn.hanbell.kpi.web.SuperSingleBean;
import com.lightshell.comm.BaseLib;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;

/**
 *
 * @author C1749
 */
@ManagedBean(name = "inventoryProductManagedBean")
@SessionScoped
public class InventoryProductManagedBean extends SuperSingleBean<InventoryProduct> {

    @EJB
    protected InventoryProductBean inventoryProductBean;

    @EJB
    private InventoryDepartmentBean inventoryDepartmentBean;

    protected String queryYearmon;
    protected String queryWhdsc;
    protected String queryGenre;
    protected String queryItclscode;
    protected String fileFullName;
    protected String facno;

    protected List<InventoryProduct> editInventoryProductList;
    protected List<InventoryProduct> inventoryProductList;

    protected final DecimalFormat doubleFormat;

    protected final Logger log4j = LogManager.getLogger("cn.hanbell.eap");

    public InventoryProductManagedBean() {
        super(InventoryProduct.class);
        this.doubleFormat = new DecimalFormat("###,###.##");
    }

    @Override
    protected boolean doBeforePersist() throws Exception {
        if (newEntity != null) {
            if (inventoryProductBean.queryInventoryProductIsExist(newEntity)) {
                showErrorMsg("Error", "添加失败,数据库已存在相同的数据，请在确认是否添加？？？");
                return false;
            }
        }
        return super.doBeforePersist(); // To change body of generated methods, choose Tools | Templates.
    }

    @Override
    protected boolean doAfterPersist() throws Exception {
        super.doAfterPersist(); // To change body of generated methods, choose Tools | Templates.
        create();
        return true;
    }

    @Override
    public void init() {
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        String y = String.valueOf(year);
        String m = String.valueOf(month);
        this.superEJB = inventoryProductBean;
        this.model = new InventoryProductModel(superEJB);
        model.getFilterFields().put("facno =", facno);
        queryYearmon = y.concat(month < 10 ? "0" + m : m);
        queryWhdsc = "";
        queryItclscode = "";
        setEditInventoryProductList(new ArrayList<>());
        setInventoryProductList(new ArrayList<>());
        query();
        super.init(); // To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void query() {
        super.query(); // To change body of generated methods, choose Tools | Templates.
        if (model != null) {
            model.getFilterFields().clear();
            if (!"".equals(facno) && facno != null) {
                model.getFilterFields().put("facno", facno);
            }
            if (!"".equals(queryYearmon) && queryYearmon != null) {
                model.getFilterFields().put("yearmon", queryYearmon);
            }
            if (!"".equals(queryWhdsc) && queryWhdsc != null) {
                model.getFilterFields().put("whdsc", queryWhdsc);
            }
            if (!"".equals(queryGenre) && queryGenre != null) {
                model.getFilterFields().put("genre", queryGenre);
            }
            if (!"".equals(queryItclscode) && queryItclscode != null) {
                model.getFilterFields().put("itclscode", queryItclscode);
            }
        }
    }

    @Override
    public void reset() {
        super.reset(); // To change body of generated methods, choose Tools | Templates.
        queryYearmon = "";
        queryWhdsc = "";
        queryGenre = "";
        queryItclscode = "";
    }

    @Override
    public void update() {
        super.update(); // To change body of generated methods, choose Tools | Templates.
        unverify();
    }

    public void queryEdit() {
        if (currentEntity != null) {
            List<InventoryProduct> list = inventoryProductBean.getEditList(currentEntity);
            if (!list.isEmpty()) {
                setEditInventoryProductList(list);
            } else {
                showErrorMsg("Error", "当前数据异常，请重试！！！");
            }
        }
    }

    public void updateEdit() {
        // 取到当前修改的集合
        List<InventoryProduct> list = getEditInventoryProductList();
        if (!list.isEmpty()) {
            for (InventoryProduct ip : list) {
                ip.setAmamount(ip.getEditamount() != null ? ip.getEditamount() : BigDecimal.ZERO);
                ip.setEditamount(BigDecimal.ZERO);
                ip.setOptuser(getUserManagedBean().getCurrentUser().getUsername());
                ip.setOptdateToNow();
                inventoryProductBean.update(ip);
            }
            showErrorMsg("Info", "更新数据成功！！！");
        } else {
            showErrorMsg("Error", "更新数据失败，请重试！！！");
        }
    }

    @Override
    public void create() {
        if (newEntity != null) {
            newEntity.setAmamount(BigDecimal.ZERO);
            newEntity.setStatus("N");
            newEntity.setCreator(getUserManagedBean().getCurrentUser().getUsername());
            newEntity.setCredateToNow();
            if (queryYearmon.equals("")) {
                newEntity.setYearmon(queryYearmon);
            }
        }
        setCurrentEntity(newEntity);
        super.create(); // To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void unverify() {
        if (null != getCurrentEntity()) {
            try {
                if (doBeforeUnverify()) {
                    newEntity.setStatus("N");// 简化查询条件,此处不再提供修改状态(M)
                    newEntity.setOptuser(getUserManagedBean().getCurrentUser().getUsername());
                    newEntity.setOptdateToNow();
                    superEJB.unverify(currentEntity);
                    doAfterUnverify();
                }
            } catch (Exception e) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(null, e.getMessage()));
            }
        } else {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_WARN, "Warn", "没有可更新数据!"));
        }
    }

    public void updateInventoryDepartment() {
        boolean flag = false;
        try {
            int y = Integer.parseInt(queryYearmon.substring(0, 4), 10);
            int m = Integer.parseInt(queryYearmon.substring(queryYearmon.length() - 2, queryYearmon.length()), 10);
            flag = inventoryDepartmentBean.updateInventoryDepartment(y, m,facno);
            if (flag) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Info", "数据保存成功！"));
            } else {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "数据保存失败，请重试！！！"));
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    
    public void updateInventoryProduct() {
        boolean flag;
        try {
            int y = Integer.parseInt(queryYearmon.substring(0, 4), 10);
            int m = Integer.parseInt(queryYearmon.substring(queryYearmon.length() - 2, queryYearmon.length()), 10);
            flag = inventoryProductBean.updateInventoryProduct(y, m, facno);
            //这里需增加一个判断，确认当前公司当前月份有没有数据，如果有提示用户是否继续执行，如果没有直接更新
            if (flag) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Info", "数据保存成功！"));
            } else {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "数据保存失败，请重试！！！"));
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    
    //导出成报表
    @Override
    public void print() throws Exception {
        inventoryProductList = inventoryProductBean.getDataByFindByYearmon(queryYearmon);
        if (inventoryProductList == null || inventoryProductList.isEmpty()) {
            return;
        }
        //设置报表名称
        fileName = "库存金额" + BaseLib.formatDate("yyyyMMddHHmmss", BaseLib.getDate()) + ".xls";
        fileFullName = reportOutputPath + fileName;
        HSSFWorkbook workbook = new HSSFWorkbook();
        //获得表格样式
        Map<String, CellStyle> style = createStyles(workbook);
        // 生成一个表格
        HSSFSheet sheet1 = workbook.createSheet("库存金额明细");
        // 设置表格宽度
        int[] wt = getInventoryProductWidth();
        for (int i = 0; i < wt.length; i++) {
            sheet1.setColumnWidth(i, wt[i] * 256);
        }
        //创建标题行
        Row row;
        //Sheet1
        String[] title = getInventoryProductTitle();
        row = sheet1.createRow(0);
        row.setHeight((short) 800);
        for (int i = 0; i < title.length; i++) {
            Cell cell = row.createCell(i);
            cell.setCellStyle(style.get("head"));
            cell.setCellValue(title[i]);
        }
        int j = 1;
        //添加数据内容
        for (InventoryProduct ip : inventoryProductList) {
            row = sheet1.createRow(j);
            j++;
            row.setHeight((short) 400);
            Cell cell0 = row.createCell(0);
            cell0.setCellStyle(style.get("cell"));
            cell0.setCellValue(ip.getFacno());
            Cell cell1 = row.createCell(1);
            cell1.setCellStyle(style.get("cell"));
            cell1.setCellValue(ip.getYearmon());
            Cell cell2 = row.createCell(2);
            cell2.setCellStyle(style.get("cell"));
            cell2.setCellValue(ip.getWareh());
            Cell cell3 = row.createCell(3);
            cell3.setCellStyle(style.get("cell"));
            cell3.setCellValue(ip.getWhdsc());
            Cell cell4 = row.createCell(4);
            cell4.setCellStyle(style.get("cell"));
            cell4.setCellValue(ip.getGenre());
            Cell cell5 = row.createCell(5);
            cell5.setCellStyle(style.get("cell"));
            cell5.setCellValue(ip.getTrtype());
            Cell cell6 = row.createCell(6);
            cell6.setCellStyle(style.get("cell"));
            cell6.setCellValue(ip.getDeptno());
            Cell cell7 = row.createCell(7);
            cell7.setCellStyle(style.get("cell"));
            cell7.setCellValue(ip.getItclscode());
            Cell cell8 = row.createCell(8);
            cell8.setCellStyle(style.get("cell"));
            cell8.setCellValue(ip.getCategories() != null ? ip.getCategories() : "");
            Cell cell9 = row.createCell(9);
            cell9.setCellStyle(style.get("cell"));
            cell9.setCellValue(ip.getIndicatorno() != null ? ip.getIndicatorno() : "");
            Cell cell10 = row.createCell(10);
            cell10.setCellStyle(style.get("cell"));
            cell10.setCellValue(String.valueOf(ip.getAmount()));
            Cell cell11 = row.createCell(11);
            cell11.setCellStyle(style.get("cell"));
            cell11.setCellValue(String.valueOf(ip.getAmamount()));
        }
        OutputStream os = null;
        try {
            os = new FileOutputStream(fileFullName);
            workbook.write(os);
            this.reportViewPath = reportViewContext + fileName;
            this.preview();
        } catch (Exception ex) {
            log4j.error(ex);
        } finally {
            try {
                if (null != os) {
                    os.flush();
                    os.close();
                }
            } catch (IOException ex) {
                log4j.error(ex.getMessage());
            }
        }
    }

    private Map<String, CellStyle> createStyles(Workbook wb) {
        Map<String, CellStyle> styles = new LinkedHashMap<>();

        // 文件头样式
        CellStyle headStyle = wb.createCellStyle();
        headStyle.setWrapText(true);//设置自动换行
        headStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        headStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
        headStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());//单元格背景颜色
        headStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        headStyle.setBorderRight(CellStyle.BORDER_THIN);
        headStyle.setRightBorderColor(IndexedColors.BLACK.getIndex());
        headStyle.setBorderLeft(CellStyle.BORDER_THIN);
        headStyle.setLeftBorderColor(IndexedColors.BLACK.getIndex());
        headStyle.setBorderTop(CellStyle.BORDER_THIN);
        headStyle.setTopBorderColor(IndexedColors.BLACK.getIndex());
        headStyle.setBorderBottom(CellStyle.BORDER_THIN);
        headStyle.setBottomBorderColor(IndexedColors.BLACK.getIndex());
        Font headFont = wb.createFont();
        headFont.setFontHeightInPoints((short) 12);
        headFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
        headStyle.setFont(headFont);
        styles.put("head", headStyle);

        // 正文样式
        CellStyle cellStyle = wb.createCellStyle();
        Font cellFont = wb.createFont();
        cellFont.setFontHeightInPoints((short) 10);
        cellStyle.setFont(cellFont);
        cellStyle.setWrapText(true);//设置自动换行
        cellStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
        cellStyle.setFillForegroundColor(IndexedColors.WHITE.getIndex());//单元格背景颜色
        cellStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        cellStyle.setBorderRight(CellStyle.BORDER_THIN);
        cellStyle.setRightBorderColor(IndexedColors.BLACK.getIndex());
        cellStyle.setBorderLeft(CellStyle.BORDER_THIN);
        cellStyle.setLeftBorderColor(IndexedColors.BLACK.getIndex());
        cellStyle.setBorderTop(CellStyle.BORDER_THIN);
        cellStyle.setTopBorderColor(IndexedColors.BLACK.getIndex());
        cellStyle.setBorderBottom(CellStyle.BORDER_THIN);
        cellStyle.setBottomBorderColor(IndexedColors.BLACK.getIndex());
        styles.put("cell", cellStyle);

        return styles;
    }

    public String[] getInventoryProductTitle() {
        return new String[]{"公司别", "日期", "库号", "库名", "产品别", "类别", "部门代号", "物料归类码", "大类编号", "中类编号", "库存金额", "调差金额"};
    }

    private int[] getInventoryProductWidth() {
        return new int[]{15, 10, 15, 15, 15, 15, 10, 20, 10, 10, 20, 20};
    }

    public String getQueryYearmon() {
        return queryYearmon;
    }

    public void setQueryYearmon(String queryYearmon) {
        this.queryYearmon = queryYearmon;
    }

    public String getQueryWhdsc() {
        return queryWhdsc;
    }

    public void setQueryWhdsc(String queryWhdsc) {
        this.queryWhdsc = queryWhdsc;
    }

    public String getQueryGenre() {
        return queryGenre;
    }

    public void setQueryGenre(String queryGenre) {
        this.queryGenre = queryGenre;
    }

    public String getQueryItclscode() {
        return queryItclscode;
    }

    public void setQueryItclscode(String queryItclscode) {
        this.queryItclscode = queryItclscode;
    }

    public InventoryProduct getInventoryProduct() {
        return newEntity;
    }

    public void setExchangEntity(InventoryProduct newEntity) {
        this.newEntity = newEntity;
    }

    public List<InventoryProduct> getEditInventoryProductList() {
        return editInventoryProductList;
    }

    public void setEditInventoryProductList(List<InventoryProduct> editInventoryProductList) {
        this.editInventoryProductList = editInventoryProductList;
    }

    public String format(BigDecimal b) {
        return doubleFormat.format(b);
    }

    public List<InventoryProduct> getInventoryProductList() {
        return inventoryProductList;
    }

    public void setInventoryProductList(List<InventoryProduct> inventoryProductList) {
        this.inventoryProductList = inventoryProductList;
    }

    public String getFacno() {
        return facno;
    }

    public void setFacno(String facno) {
        this.facno = facno;
    }

}
