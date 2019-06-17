/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.hanbell.kpi.mail;

import cn.hanbell.kpi.entity.Indicator;
import cn.hanbell.kpi.entity.IndicatorDetail;
import com.lightshell.comm.BaseLib;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import org.apache.poi.ss.usermodel.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.CellRangeAddress;

/**
 *
 * @author C1879 R冷媒均价
 */
@Stateless
@LocalBean
public class RAveragePriceFileMailBean extends RAveragePriceMailBean {

    public RAveragePriceFileMailBean() {

    }

    @Override
    public void init() {
        this.mailSetting = mailSettingBean.findByMailClazz(this.getClass().getName());
        super.init();
    }

    public String[] getTitle() {
        return new String[]{"项目", "统计项", "全年目标", "01月", "02月", "03月", "04月", "05月", "06月", "07月", "08月", "09月", "10月", "11月", "12月", "合计", "达成率"};
    }

    @Override
    protected String getMailHead() {
        return "";
    }

    @Override
    protected String getMailFooter() {
        return "";
    }

    @Override
    protected String getMailBody() {
        indicator = indicatorBean.findByFormidYearAndDeptno("R-R冷媒销售均价", y, "1F000");
        if (indicator == null) {
            throw new NullPointerException(String.format("指标编号%s:考核部门%s:不存在", "R-R冷媒销售均价", "1F000"));
        }
        indicators.clear();
        indicators = indicatorBean.findByPId(indicator.getId());
        indicatorBean.getEntityManager().clear();
        //指标排序
        indicators.sort((Indicator o1, Indicator o2) -> {
            if (o1.getSortid() > o2.getSortid()) {
                return 1;
            } else {
                return -1;
            }
        });
        List<Map<String, Object>> list = getParentList(indicators);
        if (list != null && !list.isEmpty()) {
            try {
                attachments.clear();
                File file = getFile(indicator, getTitle(), list);
                addAttachments(file);
            } catch (IOException ex) {
                Logger.getLogger(RAveragePriceFileMailBean.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return BaseLib.formatDate("yyyy-MM-dd hh:mm:ss", new Date());
    }

    public File getFile(Indicator indicator, String[] title, List<Map<String, Object>> list) throws FileNotFoundException, IOException {
        HSSFWorkbook workbook = new HSSFWorkbook();
        Short bgcolor = null;
        // 生成一个表格
        HSSFSheet sheet = workbook.createSheet("Sheet1");
        // 设置表格默认列宽度为14个字节
        sheet.setDefaultColumnWidth(14);
        //创建标题行
        Row row;
        for (int j = 0; j < 4; j++) {
            row = sheet.createRow(j);
            row.setHeight((short) 450);
            //创建标题行
            for (int i = 0; i < title.length; i++) {
                Cell cell = row.createCell(i);
                if (j == 3) {
                    cell.setCellStyle(createStyles(workbook, IndexedColors.WHITE.getIndex()).get("head"));
                    cell.setCellValue(title[i]);
                } else {
                    cell.setCellStyle(createStyles(workbook, IndexedColors.WHITE.getIndex()).get("title"));
                    if (j == 0 && i == 5) {
                        cell.setCellValue(mailSubject);
                    } else {
                        cell.setCellValue("");
                    }

                }
            }
        }
        CellRangeAddress region1 = new CellRangeAddress(0, 2, 5, 10);
        sheet.addMergedRegion(region1);
        int index = 3;

        for (int i = 0; i < list.size(); i++) {//6
            Iterator<Map.Entry<String, Object>> map = list.get(i).entrySet().iterator();
            if (i % 2 == 0) {
                bgcolor = IndexedColors.GREY_25_PERCENT.getIndex();
            } else {
                bgcolor = IndexedColors.WHITE.getIndex();
            }
            while (map.hasNext()) {
                Map.Entry<String, Object> entry = map.next();
                List<String[]> cellList = (List<String[]>) entry.getValue();
                for (int j = 0; j < cellList.size(); j++) {
                    index++;
                    row = sheet.createRow(index);
                    row.setHeight((short) 450);
                    if (index % 3 == 1) {
                        Cell cell1 = row.createCell(0);
                        cell1.setCellStyle(createStyles(workbook, bgcolor).get("head"));
                        cell1.setCellValue(entry.getKey());
                    }
                    //合并三行
                    if (index % 3 == 0) {
                        CellRangeAddress region = new CellRangeAddress(index - 2, index, 0, 0);
                        sheet.addMergedRegion(region);
                    }
                    String[] cellStrings = cellList.get(j);
                    for (int k = 0; k < cellStrings.length; k++) {
                        Cell cell = row.createCell(k + 1);
                        cell.setCellStyle(createStyles(workbook, bgcolor).get("cell"));
                        cell.setCellValue(cellStrings[k]);
                    }
                }

            }
        }
        String finalFilePath = "../" + mailSubject + ".xls";//最终保存的文件路径
        FileOutputStream out = null;
        File file = new File(finalFilePath);
        // 如果文件存在,则删除已有的文件,重新创建一份新的
        try {
            if (file.exists() && file.isFile()) {
                file.delete();
                file = new File(finalFilePath);
            }
            out = new FileOutputStream(file);
            workbook.write(out);
        } catch (IOException e) {
            log4j.error(e.getMessage());
        } finally {
            try {
                if (null != out) {
                    out.flush();
                    out.close();
                }
            } catch (IOException e) {
                log4j.error(e.getMessage());
            }
        }
        return file;
    }

    public List<Map<String, Object>> getParentList(List<Indicator> indicatorList) {
        List<Map<String, Object>> parentList = new ArrayList<Map<String, Object>>();
        sumlistIndicators = new ArrayList<>();
        try {
            for (Indicator i : indicatorList) {
                parentList.add(getChildList(i));
            }
            //合计
            sumIndicator = indicatorBean.getSumValue(sumlistIndicators);
            parentList.add(getChildList(sumIndicator));
            return parentList;
        } catch (Exception e) {
            System.out.println("cn.hanbell.kpi.mail.AveragePriceFJRMailBean.getParentList()" + e.toString());
        }
        return parentList;
    }

    public Map<String, Object> getChildList(Indicator e) throws NoSuchMethodException, InvocationTargetException, Exception {
        //获取需要取值栏位
        Map<String, Object> map = new LinkedHashMap<>();
        String col, mon;
        StringBuilder sb = new StringBuilder();
        Field f;
        mon = indicatorBean.getIndicatorColumn("N", getM());
        BigDecimal v;
        Method setMethod;
        try {
            String associatedIndicator = e.getAssociatedIndicator();
            if (associatedIndicator != null && !"".equals(associatedIndicator) && e.getId() != -1) {
                Indicator quantityi, amounti;
                String[] arr = associatedIndicator.split(";");
                quantityi = indicatorBean.findByFormidYearAndDeptno(arr[0].trim(), y, arr[2].trim());
                amounti = indicatorBean.findByFormidYearAndDeptno(arr[1].trim(), y, arr[2].trim());
                //实际台数
                IndicatorDetail qa = new IndicatorDetail();
                qa.setType("A");
                qa.setParent(quantityi);
                //实际金额
                IndicatorDetail ab = new IndicatorDetail();
                ab.setType("A");
                ab.setParent(amounti);
                if (e.getOther3Indicator() != null && e.getOther4Indicator() != null) {
                    for (int i = getM(); i > 0; i--) {
                        ///实际台数 + 录入柯茂数据 - 销往柯茂数据
                        v = getNValue(quantityi.getActualIndicator(), i).add(getNValue(e.getOther1Indicator(), i)).subtract(getNValue(e.getOther3Indicator(), i));
                        setMethod = qa.getClass().getDeclaredMethod("set" + indicatorBean.getIndicatorColumn("N", i).toUpperCase(), BigDecimal.class);
                        setMethod.invoke(qa, v);
                        //实际金额  + 录入柯茂数据 - 销往柯茂数据
                        v = getNValue(amounti.getActualIndicator(), i).add(getNValue(e.getOther2Indicator(), i)).subtract(getNValue(e.getOther4Indicator(), i));
                        setMethod = ab.getClass().getDeclaredMethod("set" + indicatorBean.getIndicatorColumn("N", i).toUpperCase(), BigDecimal.class);
                        setMethod.invoke(ab, v);
                    }
                } else {
                    for (int i = getM(); i > 0; i--) {
                        ///实际台数
                        v = getNValue(quantityi.getActualIndicator(), i).add(getNValue(e.getOther1Indicator(), i));
                        setMethod = qa.getClass().getDeclaredMethod("set" + indicatorBean.getIndicatorColumn("N", i).toUpperCase(), BigDecimal.class);
                        setMethod.invoke(qa, v);
                        //实际金额
                        v = getNValue(amounti.getActualIndicator(), i).add(getNValue(e.getOther2Indicator(), i));
                        setMethod = ab.getClass().getDeclaredMethod("set" + indicatorBean.getIndicatorColumn("N", i).toUpperCase(), BigDecimal.class);
                        setMethod.invoke(ab, v);
                    }
                }
                //实际台数
                e.setActualIndicator(qa);
                //实际金额
                e.setBenchmarkIndicator(ab);
                //目标台数
                e.setTargetIndicator(quantityi.getTargetIndicator());
                //目标金额
                e.setForecastIndicator(amounti.getTargetIndicator());
                sumlistIndicators.add(e);
            }
            //实际台数
            IndicatorDetail q = e.getActualIndicator();
            //实际金额
            IndicatorDetail a = e.getBenchmarkIndicator();
            //目标台数
            IndicatorDetail tq = e.getTargetIndicator();
            //目标金额
            IndicatorDetail ta = e.getForecastIndicator();
            //均价
            IndicatorDetail avg = new IndicatorDetail();
            avg.setType("A");
            avg.setParent(e);
            for (int i = getM(); i > 0; i--) {
                //实际台数
                v = getAvgPrice(getNValue(q, i), getNValue(a, i));
                setMethod = avg.getClass().getDeclaredMethod("set" + indicatorBean.getIndicatorColumn("N", i).toUpperCase(), BigDecimal.class);
                setMethod.invoke(avg, v);
            }

            List<String[]> list = new ArrayList<>();

            String[] arr1 = new String[16];
            arr1[0] = "台数";
            arr1[1] = decimalFormat.format(tq.getNfy());
            for (int i = 1; i < 13; i++) {
                col = indicatorBean.getIndicatorColumn(e.getFormtype(), i);
                f = q.getClass().getDeclaredField(col);
                f.setAccessible(true);
                if (i > m) {
                    arr1[i + 1] = decimalFormat.format(f.get(q)).equals("0") ? "" : decimalFormat.format(f.get(q));
                } else {
                    arr1[i + 1] = decimalFormat.format(f.get(q));
                }
            }
            arr1[14] = decimalFormat.format(q.getNfy());
            arr1[15] = percentFormat(getPerformance(q.getNfy(), tq.getNfy()));
            list.add(arr1);

            String[] arr2 = new String[16];
            arr2[0] = "金额";
            arr2[1] = decimalFormat.format(ta.getNfy());
            for (int i = 1; i < 13; i++) {
                col = indicatorBean.getIndicatorColumn(e.getFormtype(), i);
                f = a.getClass().getDeclaredField(col);
                f.setAccessible(true);
                if (i > m) {
                    arr2[i + 1] = decimalFormat.format(f.get(a)).equals("0") ? "" : decimalFormat.format(f.get(a));
                } else {
                    arr2[i + 1] = decimalFormat.format(f.get(a));
                }
            }
            arr2[14] = decimalFormat.format(a.getNfy());
            arr2[15] = percentFormat(getPerformance(a.getNfy(), ta.getNfy()));
            list.add(arr2);

            String[] arr3 = new String[16];
            arr3[0] = "平均售价";
            arr3[1] = decimalFormat.format(getAvgPrice(tq.getNfy(), ta.getNfy()));
            for (int i = 1; i < 13; i++) {
                col = indicatorBean.getIndicatorColumn(e.getFormtype(), i);
                f = avg.getClass().getDeclaredField(col);
                f.setAccessible(true);
                if (i > m) {
                    arr3[i + 1] = decimalFormat.format(f.get(avg)).equals("0") ? "" : decimalFormat.format(f.get(avg));
                } else {
                    arr3[i + 1] = decimalFormat.format(f.get(avg));
                }
            }
            arr3[14] = decimalFormat.format(getAvgPrice(q.getNfy(), a.getNfy()));
            arr3[15] = percentFormat(getPerformance(getAvgPrice(q.getNfy(), a.getNfy()), getAvgPrice(tq.getNfy(), ta.getNfy())));
            list.add(arr3);

            map.put(e.getName().replace("R销售均价", ""), list);
        } catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException ex) {
            throw new Exception(ex);
        }

        return map;
    }

    private Map<String, CellStyle> createStyles(Workbook wb, Short style) {
        Map<String, CellStyle> styles = new LinkedHashMap<>();

        // 标题样式
        CellStyle titleStyle = wb.createCellStyle();
        titleStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        titleStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
        Font titleFont = wb.createFont();
        titleFont.setFontHeightInPoints((short) 23);
        titleFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
        titleFont.setFontName("宋体");
        titleStyle.setFont(titleFont);
        styles.put("title", titleStyle);

        // 文件头样式
        CellStyle headStyle = wb.createCellStyle();
        headStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        headStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
        headStyle.setFillForegroundColor(style);//单元格背景颜色
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
        headFont.setFontHeightInPoints((short) 10);
        headStyle.setFont(headFont);
        styles.put("head", headStyle);

        // 正文样式
        CellStyle cellStyle = wb.createCellStyle();
        Font cellFont = wb.createFont();
        cellFont.setFontHeightInPoints((short) 10);
        cellStyle.setFont(cellFont);
        cellStyle.setAlignment(HSSFCellStyle.ALIGN_RIGHT);
        cellStyle.setFillForegroundColor(style);//单元格背景颜色
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

}
