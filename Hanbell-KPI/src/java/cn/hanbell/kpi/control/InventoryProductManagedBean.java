/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.hanbell.kpi.control;

import cn.hanbell.kpi.ejb.IndicatorBean;
import cn.hanbell.kpi.ejb.IndicatorDetailBean;
import cn.hanbell.kpi.ejb.InventoryDepartmentBean;
import cn.hanbell.kpi.ejb.InventoryProductBean;
import cn.hanbell.kpi.ejb.InvindexBean;
import cn.hanbell.kpi.ejb.InvindexDetailBean;
import cn.hanbell.kpi.entity.Indicator;
import cn.hanbell.kpi.entity.IndicatorDetail;
import cn.hanbell.kpi.entity.InventoryProduct;
import cn.hanbell.kpi.entity.Invindex;
import cn.hanbell.kpi.entity.InvindexDetail;
import cn.hanbell.kpi.lazy.InventoryProductModel;
import cn.hanbell.kpi.web.SuperSingleBean;
import com.lightshell.comm.BaseLib;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.logging.Level;
import java.util.stream.Collectors;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.persistence.Query;
import org.apache.commons.lang.StringUtils;
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

    @EJB
    private IndicatorDetailBean indicatorDetailBean;
    @EJB
    private IndicatorBean indicatorBean;

    @EJB
    private InvindexDetailBean invindexDetailBean;

    @EJB
    private InvindexBean invindexBean;
    private List<Indicator> indicators;

    protected String facno;
    protected String queryYearmon;
    protected String queryWhdsc;
    protected String queryWareh;
    protected String queryGenre;
    protected String fileFullName;

    protected List<InventoryProduct> editInventoryProductList;
    protected List<InventoryProduct> inventoryProductList;

    protected final DecimalFormat doubleFormat;

    protected final Logger log4j = LogManager.getLogger("cn.hanbell.eap");

    protected List<InventoryProduct> inventoryProductList1;//生产库存

    protected List<InventoryProduct> inventoryProductList2;//营业库存

    protected List<InventoryProduct> inventoryProductList3;//服务库存

    protected List<InventoryProduct> inventoryProductList4;//借出未归

    protected List<InventoryProduct> inventoryProductList5;//其他工具库等

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
        this.facno="C";
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
        setEditInventoryProductList(new ArrayList<>());
        setInventoryProductList(new ArrayList<>());
        inventoryProductList1 = new ArrayList<>();
        inventoryProductList2 = new ArrayList<>();
        inventoryProductList3 = new ArrayList<>();
        inventoryProductList4 = new ArrayList<>();
        inventoryProductList5 = new ArrayList<>();
        indicators = new ArrayList<>();
        query();
        super.init();
    }

    @Override
    public void query() {
        super.query();
        if (model != null) {
            model.getFilterFields().clear();
            if (!"".equals(facno) && facno != null) {
                model.getFilterFields().put("facno", facno);
            }
            if (!"".equals(queryYearmon) && queryYearmon != null) {
                model.getFilterFields().put("yearmon", queryYearmon);
            }
            if (!"".equals(queryWareh) && queryWareh != null) {
                model.getFilterFields().put("wareh", queryWareh);
            }
            if (!"".equals(queryWhdsc) && queryWhdsc != null) {
                model.getFilterFields().put("whdsc", queryWhdsc);
            }
                      if (!"".equals(queryGenre) && queryGenre != null) {
                model.getFilterFields().put("genre", queryGenre);
            }
        }
    }

    @Override
    public void reset() {
        super.reset();
        queryYearmon = "";
        queryWhdsc = "";
         queryWareh = "";
        queryGenre = "";
    }

    @Override
    public void update() {
        super.update();
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
            flag = inventoryDepartmentBean.updateInventoryDepartment(y, m, facno);
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
        HSSFSheet sheet1 = workbook.createSheet("生产目标合计");
        HSSFSheet sheet2 = workbook.createSheet("营业目标合计");
        HSSFSheet sheet3 = workbook.createSheet("服务目标合计");
        HSSFSheet sheet4 = workbook.createSheet("借出未归合计");
        HSSFSheet sheet5 = workbook.createSheet("其他目标合计");
        HSSFSheet sheet6 = workbook.createSheet("库存金额合计");
        // 表格宽度
        int[] wt = getInventoryProductWidth();
        //标题行
        String[] title = getInventoryProductTitle();
        //创建标题行
        List<List<InventoryProduct>> list = new ArrayList<>();
        list.add(inventoryProductList1);
        list.add(inventoryProductList2);
        list.add(inventoryProductList3);
        list.add(inventoryProductList4);
        list.add(inventoryProductList5);
        list.add(inventoryProductList);
        int index = 1;
        for (List<InventoryProduct> ips : list) {
            HSSFSheet sheet = sheet1;
            Row row;
            switch (index) {
                case 1:
                    sheet = sheet1;
                    break;
                case 2:
                    sheet = sheet2;
                    break;
                case 3:
                    sheet = sheet3;
                    break;
                case 4:
                    sheet = sheet4;
                    break;
                case 5:
                    sheet = sheet5;
                    break;
                case 6:
                    sheet = sheet6;
                    break;
            }
            index++;
            row = sheet.createRow(0);
            row.setHeight((short) 800);
            for (int i = 0; i < title.length; i++) {
                Cell cell = row.createCell(i);
                cell.setCellStyle(style.get("head"));
                cell.setCellValue(title[i]);
            }
            for (int i = 0; i < wt.length; i++) {
                sheet.setColumnWidth(i, wt[i] * 256);
            }
            int j = 1;
            for (InventoryProduct ip : ips) {
                row = sheet.createRow(j);
                j++;
                row.setHeight((short) 400);
                Cell cell0 = row.createCell(0);
                cell0.setCellStyle(style.get("cell"));
                cell0.setCellValue(ip.getId());
                Cell cell1 = row.createCell(1);
                cell1.setCellStyle(style.get("cell"));
                cell1.setCellValue(ip.getFacno());
                Cell cell2 = row.createCell(2);
                cell2.setCellStyle(style.get("cell"));
                cell2.setCellValue(ip.getYearmon());
                Cell cell3 = row.createCell(3);
                cell3.setCellStyle(style.get("cell"));
                cell3.setCellValue(ip.getWareh());
                Cell cell4 = row.createCell(4);
                cell4.setCellStyle(style.get("cell"));
                cell4.setCellValue(ip.getWhdsc());
                Cell cell5 = row.createCell(5);
                cell5.setCellStyle(style.get("cell"));
                cell5.setCellValue(ip.getGenre());
                Cell cell6 = row.createCell(6);
                cell6.setCellStyle(style.get("cell"));
                cell6.setCellValue(ip.getTrtype());
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
                cell10.setCellValue(ip.getAmount().doubleValue());
                Cell cell11 = row.createCell(11);
                cell11.setCellStyle(style.get("cell"));
                cell11.setCellValue(ip.getAmamount().doubleValue());
            }
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
        return new String[]{"编号", "公司别", "日期", "库号", "库名", "产品别", "类别", "物料归类码", "大类编号", "中类编号", "库存金额", "调差金额"};
    }

    private int[] getInventoryProductWidth() {
        return new int[]{15, 10, 15, 15, 15, 15, 10, 20, 10, 10, 20, 20};
    }

    public void updateIndicator() {
        int y = Integer.parseInt(queryYearmon.substring(0, 4), 10);
        int m = Integer.parseInt(queryYearmon.substring(queryYearmon.length() - 2, queryYearmon.length()), 10);
        try {
            List<Object[]> nocingifList = inventoryProductBean.getNoConfigWareh(y, m, facno);
            if (nocingifList != null && !nocingifList.isEmpty()) {
                StringBuffer exceptionMsg = new StringBuffer();
                for (Object[] o : nocingifList) {
                    exceptionMsg.append((String) o[0]).append("仓库中，产品别为").append((String) o[2]).append("的共计金额为").append(o[3]).append("元未配置；");
                }
                exceptionMsg.append("请调整仓库");
                throw new Exception(exceptionMsg.toString());
            }
            Indicator pEntity = indicatorBean.findByFormidYearAndDeptno("库存金额合计", y, "1K000");
            List<Indicator> list = indicatorBean.findByPId(pEntity.getId());
            updateActual(pEntity, m);

            //生产库存=生产在制+实际库存+借厂商
            inventoryProductList1.clear();
            this.inventoryProductList1 = inventoryProductBean.getDetailsByWareh(y, m, facno, this.InListToString(invindexDetailBean.findByGenerno("A1")));
            //更新指标
            updateInventory1(y, m);

            inventoryProductList2.clear();
            this.inventoryProductList2 = inventoryProductBean.getDetailsByWareh(y, m, facno, this.InListToString(invindexDetailBean.findByGenerno("A2")));
            updateInventory2(y, m);

            inventoryProductList3.clear();
            this.inventoryProductList3 = inventoryProductBean.getDetailsByWareh(y, m, facno, this.InListToString(invindexDetailBean.findByGenerno("A3")));
            updateInventory3(y, m);

            inventoryProductList4.clear();
            this.inventoryProductList4 = inventoryProductBean.getDetailsByWareh(y, m, facno, this.InListToString(invindexDetailBean.findByGenerno("A4")));
            updateInventory4(y, m);

            inventoryProductList5.clear();
            this.inventoryProductList5 = inventoryProductBean.getDetailsByWareh(y, m, facno, this.InListToString(invindexDetailBean.findByGenerno("A5")));
            updateInventory5(y, m);

            //先更新真空   真空库存金额=真空成品+真空零件
            Indicator entity = indicatorBean.findByFormidYearAndDeptno("真空库存金额", y, "1H000");
            indicatorBean.updateActual(entity, m);
            indicatorBean.updatePerformance(entity);
            indicatorBean.update(entity);

            for (Indicator indicator : list) {
                indicatorBean.updateActual(indicator, m);
                indicatorBean.updatePerformance(indicator);
                indicatorBean.update(indicator);
            }

            indicatorBean.updateActual(pEntity, m);
            indicatorBean.updatePerformance(pEntity);
            indicatorBean.update(pEntity);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Info", "数据更新成功，请导出月报表核查"));
        } catch (Exception ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "ERROR", ex.getMessage()));
//            java.util.logging.Logger.getLogger(InventoryProductManagedBean.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void updateActual(Indicator entity, int m) throws Exception {
        // 递归更新某个月份的实际值,不调用ActualInterface计算方法
        if (entity.isAssigned()) {
            List<Indicator> details = indicatorBean.findByPId(entity.getId());
            if (details != null && !details.isEmpty()) {
                for (Indicator d : details) {
                    updateActual(d, m);
                }
            } // 先计算子项值
            updateIndicatorDetail(entity.getActualIndicator(), BigDecimal.ZERO, m);
        } else {
            updateIndicatorDetail(entity.getActualIndicator(), BigDecimal.ZERO, m);
        }
    }

    public void updateInventory1(int y, int m) throws Exception {
        List<Object[]> list = invindexDetailBean.getWarehs("A1");
        for (InventoryProduct entity : this.inventoryProductList1) {
            for (Object[] o : list) {
                if (entity.getWareh().equals(o[1]) && this.genzlsToList((String) o[0]).contains(entity.getGenre())) {
                    entity.setIndicatorno((String) o[2]);
                    continue;
                }
            }
        }
        Map<String, Double> map = inventoryProductList1.stream().collect(Collectors.groupingBy(InventoryProduct::getIndicatorno, Collectors.summingDouble(InventoryProduct::getDoubleAmount)));
        Iterator<Entry<String, Double>> it2 = map.entrySet().iterator();
        while (it2.hasNext()) {
            Entry<String, Double> entry = it2.next();
            String key = entry.getKey();
            Double value = entry.getValue();
            if (!"".equals(key)) {
                Indicator indicator = indicatorBean.findByFormidYearAndDeptno(key, y, "1P000");
                if (indicator != null) {
                    updateIndicatorDetail(indicator.getActualIndicator(), BigDecimal.valueOf(value), m);
                } else {
                    throw new Exception(key + " 指标有异常。请检核");
                }
            }
        }
    }

    public void updateInventory2(int y, int m) throws Exception {
        List<Object[]> list = invindexDetailBean.getWarehs("A2");
        String mon;
        Field f;
        BigDecimal v1;
        Double a1, a2, a3, a4, a5, a6;
        //检查制冷分摊比率是否维护
        Indicator i = indicatorBean.findByFormidYearAndDeptno("制冷库存分摊比率", y, "1F000");
        IndicatorDetail o1 = i.getOther1Indicator();
        IndicatorDetail o2 = i.getOther2Indicator();
        IndicatorDetail o3 = i.getOther3Indicator();
        IndicatorDetail o4 = i.getOther4Indicator();
        IndicatorDetail o5 = i.getOther5Indicator();
        IndicatorDetail o6 = i.getOther6Indicator();
        mon = indicatorBean.getIndicatorColumn("N", m);
        f = o1.getClass().getDeclaredField(mon);
        f.setAccessible(true);
        a1 = Double.valueOf(f.get(o1).toString());

        f = o2.getClass().getDeclaredField(mon);
        f.setAccessible(true);
        a2 = Double.valueOf(f.get(o2).toString());

        f = o3.getClass().getDeclaredField(mon);
        f.setAccessible(true);
        a3 = Double.valueOf(f.get(o3).toString());

        f = o4.getClass().getDeclaredField(mon);
        f.setAccessible(true);
        a4 = Double.valueOf(f.get(o4).toString());

        f = o5.getClass().getDeclaredField(mon);
        f.setAccessible(true);
        a5 = Double.valueOf(f.get(o5).toString());

        f = o6.getClass().getDeclaredField(mon);
        f.setAccessible(true);
        a6 = Double.valueOf(f.get(o6).toString());
        if (a1 == 0.0 && a2 == 0.0 && a3 == 0.0 && a4 == 0.0 && a5 == 0.0 && a6 == 0.0) {
            throw new Exception("请维护制冷分摊比率");
        }
        if (a1 + a2 + a3 + a4 + a5 + a6 != 1) {
            throw new Exception("分摊比率不等于100%，请维护制冷分摊比率");
        }
        //开始计算
        for (InventoryProduct entity : this.inventoryProductList2) {
            for (Object[] o : list) {
                if (entity.getWareh().equals(o[1]) && this.genzlsToList((String) o[0]).contains(entity.getGenre())) {
                    entity.setIndicatorno((String) o[2]);
                    continue;
                }
            }
        }
        Map<String, Double> map = inventoryProductList2.stream().collect(Collectors.groupingBy(InventoryProduct::getIndicatorno, Collectors.summingDouble(InventoryProduct::getDoubleAmount)));
        Iterator<Entry<String, Double>> it2 = map.entrySet().iterator();
        while (it2.hasNext()) {
            Entry<String, Double> entry = it2.next();
            String key = entry.getKey();
            Double value = entry.getValue();
            if (!"".equals(key)) {
                List<Indicator> indicatorList = indicatorBean.findByFormidAndYear(key, y);
                if (indicatorList != null && indicatorList.size() == 1) {
                    updateIndicatorDetail(indicatorList.get(0).getActualIndicator(), BigDecimal.valueOf(value), m);
                } else {
                    throw new Exception(key + " 指标有异常。请检核");
                }
            }
        }
        // 制冷的营业金额需要从制冷分摊金额中调整并加入。
        IndicatorDetail atualIndicator = i.getActualIndicator();
        f = atualIndicator.getClass().getDeclaredField(mon);
        f.setAccessible(true);
        double share = Double.valueOf(f.get(atualIndicator).toString());
        updateAuthDetail(indicatorBean.findByFormidYearAndDeptno("冷媒库存金额", y, "1F000").getActualIndicator(), share * a1, m);
        updateAuthDetail(indicatorBean.findByFormidYearAndDeptno("制冷营销课库存金额", y, "1F000").getActualIndicator(), share * a2, m);
        updateAuthDetail(indicatorBean.findByFormidYearAndDeptno("南京制冷库存金额", y, "1E000").getActualIndicator(), share * a3, m);
        updateAuthDetail(indicatorBean.findByFormidYearAndDeptno("广州制冷库存金额", y, "1D000").getActualIndicator(), share * a4, m);
        updateAuthDetail(indicatorBean.findByFormidYearAndDeptno("重庆制冷库存金额", y, "1V000").getActualIndicator(), share * a5, m);
        updateAuthDetail(indicatorBean.findByFormidYearAndDeptno("济南制冷库存金额", y, "1C000").getActualIndicator(), share * a6, m);
    }

    public void updateInventory3(int y, int m) throws Exception {
        List<Object[]> list = invindexDetailBean.getWarehs("A3");
        for (InventoryProduct entity : this.inventoryProductList3) {
            for (Object[] o : list) {
                if (entity.getWareh().equals(o[1])) {
                    entity.setIndicatorno((String) o[2]);
                    continue;
                }
            }
        }
        Map<String, Double> map = inventoryProductList3.stream().collect(Collectors.groupingBy(InventoryProduct::getIndicatorno, Collectors.summingDouble(InventoryProduct::getDoubleAmount)));
        Iterator<Entry<String, Double>> it2 = map.entrySet().iterator();
        while (it2.hasNext()) {
            Entry<String, Double> entry = it2.next();
            String key = entry.getKey();
            Double value = entry.getValue();
            if (!"".equals(key)) {
                List<Indicator> indicator = indicatorBean.findByFormidAndYear(key, y);
                if (indicator != null && indicator.size() == 1) {
                    updateIndicatorDetail(indicator.get(0).getActualIndicator(), BigDecimal.valueOf(value), m);
                } else {
                    throw new Exception(key + " 指标有异常。请检核");
                }
            }
        }
        List<InventoryProduct> nanjing = this.inventoryProductBean.findByFacnoAndYearmon("N", y, m);
        List<InventoryProduct> guangzhou = this.inventoryProductBean.findByFacnoAndYearmon("G", y, m);
        List<InventoryProduct> chongqing = this.inventoryProductBean.findByFacnoAndYearmon("C4", y, m);
        List<InventoryProduct> jinan = this.inventoryProductBean.findByFacnoAndYearmon("J", y, m);
        //分公司的服务库存金额=总公司的库+分公司的所有库存金额
        updateAuthDetail(indicatorBean.findByFormidYearAndDeptno("南京服务库存金额", y, "1E000").getActualIndicator(), getSum(nanjing, "南京服务库存金额").doubleValue(), m);
        updateAuthDetail(indicatorBean.findByFormidYearAndDeptno("广州服务库存金额", y, "1D000").getActualIndicator(), getSum(guangzhou, "广州服务库存金额").doubleValue(), m);
        updateAuthDetail(indicatorBean.findByFormidYearAndDeptno("重庆服务库存金额", y, "1V000").getActualIndicator(), getSum(chongqing, "重庆服务库存金额").doubleValue(), m);
        updateAuthDetail(indicatorBean.findByFormidYearAndDeptno("济南服务库存金额", y, "1C000").getActualIndicator(), getSum(jinan, "济南服务库存金额").doubleValue(), m);
    }

    public BigDecimal getSum(List<InventoryProduct> list, String formid) {
        BigDecimal sum = new BigDecimal(0);
        for (InventoryProduct entity : list) {
            entity.setIndicatorno(formid);
            sum = sum.add(entity.getAmount()).add(entity.getAmamount());
        }
        inventoryProductList3.addAll(list);
        return sum;
    }

    public void updateInventory4(int y, int m) throws Exception {
        List<Object[]> list = invindexDetailBean.getWarehs("A4");
        for (InventoryProduct entity : this.inventoryProductList4) {
            for (Object[] o : list) {
                if (!"".equals((String) o[0]) && entity.getWareh().equals(o[1]) && entity.getDeptno().startsWith((String) o[0])) {
                    entity.setIndicatorno((String) o[2]);
                    continue;
                }
            }
        }
        Map<String, Double> map = inventoryProductList4.stream().collect(Collectors.groupingBy(InventoryProduct::getIndicatorno, Collectors.summingDouble(InventoryProduct::getDoubleAmount)));
        Iterator<Entry<String, Double>> it2 = map.entrySet().iterator();
        while (it2.hasNext()) {
            Entry<String, Double> entry = it2.next();
            String key = entry.getKey();
            Double value = entry.getValue();
            if (!"".equals(key)) {
                List<Indicator> indicator = indicatorBean.findByFormidAndYear(key, y);
                if (indicator != null && indicator.size() == 1) {
                    updateIndicatorDetail(indicator.get(0).getActualIndicator(), BigDecimal.valueOf(value), m);
                } else {
                    throw new Exception(key + " 指标有异常。请检核");
                }
            }
        }
    }

    public void updateInventory5(int y, int m) throws Exception {
        List<Object[]> list = invindexDetailBean.getWarehs("A5");
        for (InventoryProduct entity : this.inventoryProductList5) {
            for (Object[] o : list) {
                if (entity.getWareh().equals(o[1])) {
                    entity.setIndicatorno((String) o[2]);
                    continue;
                }
            }
        }
        Map<String, Double> map = inventoryProductList5.stream().collect(Collectors.groupingBy(InventoryProduct::getIndicatorno, Collectors.summingDouble(InventoryProduct::getDoubleAmount)));
        Iterator<Entry<String, Double>> it2 = map.entrySet().iterator();
        while (it2.hasNext()) {
            Entry<String, Double> entry = it2.next();
            String key = entry.getKey();
            Double value = entry.getValue();
            if (!"".equals(key)) {
                List<Indicator> indicator = indicatorBean.findByFormidAndYear(key, y);
                if (indicator != null && indicator.size() == 1) {
                    updateIndicatorDetail(indicator.get(0).getActualIndicator(), BigDecimal.valueOf(value), m);
                } else {
                    throw new Exception(key + " 指标有异常。请检核");
                }
            }
        }
    }

    public boolean updateAuthDetail(IndicatorDetail detail, Double account, int m) throws Exception {
        Field f;
        String mon = indicatorBean.getIndicatorColumn("N", m);
        f = detail.getClass().getDeclaredField(mon);
        f.setAccessible(true);
        Double a = Double.valueOf(f.get(detail).toString()) + account;
        Method setMethod = detail.getClass()
                .getDeclaredMethod("set" + getIndicatorColumn("N", m).toUpperCase(), BigDecimal.class);
        setMethod.invoke(detail, BigDecimal.valueOf(a));
        indicatorDetailBean.update(detail);
        return true;
    }

    public boolean updateIndicatorDetail(IndicatorDetail detail, BigDecimal account, int m) throws Exception {
        Method setMethod = detail.getClass()
                .getDeclaredMethod("set" + getIndicatorColumn("N", m).toUpperCase(), BigDecimal.class);
        setMethod.invoke(detail, account);
        indicatorDetailBean.update(detail);
        return true;
    }

    public String getIndicatorColumn(String formtype, int m) {
        if (formtype.equals("N")) {
            return "n" + String.format("%02d", m);
        } else if (formtype.equals("D")) {
            return "d" + String.format("%02d", m);
        } else if (formtype.equals("NQ")) {
            return "nq" + String.valueOf(m);
        } else {
            return "";

        }
    }

    public String InListToString(List<InvindexDetail> invindexDetail) {
        StringBuilder sb = new StringBuilder();
        try {
            sb.setLength(0);
            Set warehList = new HashSet();
            if (invindexDetail != null && !invindexDetail.isEmpty()) {
                for (InvindexDetail entity : invindexDetail) {
                    warehList.add(entity.getWareh());
                }
                sb.append("  in ('").append(StringUtils.join(warehList.toArray(), "\',\'")).append("')");
                return sb.toString();
            }
            return null;
        } catch (Exception e) {
            return null;
        }
    }

    public List<String> genzlsToList(String genzls) {
        List<String> list = new ArrayList();
        if (genzls != null && !"".equals(genzls)) {
            genzls = genzls.replaceAll("，", ",");
            String[] st = genzls.split(",");
            for (int i = 0; i < st.length; i++) {
                list.add(st[i]);
            }
            return list;
        }
        return list;
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

    public String getQueryWareh() {
        return queryWareh;
    }

    public void setQueryWareh(String queryWareh) {
        this.queryWareh = queryWareh;
    }

    public String getQueryGenre() {
        return queryGenre;
    }

    public void setQueryGenre(String queryGenre) {
        this.queryGenre = queryGenre;
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

    public List<InventoryProduct> getInventoryProductList1() {
        return inventoryProductList1;
    }

    public void setInventoryProductList1(List<InventoryProduct> inventoryProductList1) {
        this.inventoryProductList1 = inventoryProductList1;
    }

    public String getFacno() {
        return facno;
    }

    public void setFacno(String facno) {
        this.facno = facno;
    }

}
