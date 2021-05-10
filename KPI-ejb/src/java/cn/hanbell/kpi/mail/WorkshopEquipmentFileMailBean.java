/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.hanbell.kpi.mail;

import cn.hanbell.kpi.comm.MailNotification;
import cn.hanbell.kpi.ejb.eam.WorkshopEquipmentBean;
import cn.hanbell.kpi.entity.Indicator;
import com.lightshell.comm.BaseLib;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;

/**
 *
 * @author C2079 车间设备月报
 */
@Stateless
@LocalBean
public class WorkshopEquipmentFileMailBean extends MailNotification {

    @EJB
    protected WorkshopEquipmentBean workShopEquipmentBean;

    public WorkshopEquipmentFileMailBean() {

    }

    @Override
    public void init() {
        this.mailSetting = mailSettingBean.findByMailClazz(this.getClass().getName());
        super.init();
    }

    @Override
    protected String getMailHead() {
        StringBuilder sb = new StringBuilder();
        sb.append("<div class=\"tableTitle\">各位主管、各位同事：大家好！</div>");
        sb.append("<div class=\"tableTitle\" style=\"margin-top: 30px\" >附件是车间设备月报报表，请查收。");
        sb.append("<div class=\"tableTitle\" style=\"margin-top: 30px\">谢谢!");
        sb.append("<div class=\"tableTitle\" style=\"margin-top: 30px\">此邮件是系统自动发送，请不要回复。");
        sb.append("<div class=\"tableTitle\" style=\"margin-top: 30px\">报表责任人：唐帆。");
        sb.append("<div class=\"tableTitle\" style=\"margin-top: 30px\">");
        return sb.toString();
    }

    @Override
    protected String getMailFooter() {
        return "";
    }

    @Override
    protected String getMailBody() {
        attachments.clear();
        String finalFilePath = "";
        String[] str = new String[2];//车间类型
        str[0] = "半成品方型件";
        str[1] = "半成品圆型件";
        try {
            for (int x = 0; x < str.length; x++) {
                finalFilePath = this.getClass().getProtectionDomain().getCodeSource().getLocation().getPath();
                int index = finalFilePath.indexOf("KPI-ejb");
                InputStream is = new FileInputStream(finalFilePath.substring(1, index) + "Hanbell-KPI_war/rpt/汉钟版车间设备月报模板.xls");
                Workbook workbook = WorkbookFactory.create(is);
                //获得表格样式
                Map<String, CellStyle> style = createStyles(workbook);
                Sheet sheet;
                sheet = workbook.getSheetAt(0);
                Row row;
                Row row1;
                row = sheet.createRow(0);
                row.setHeight((short) 900);
                row1 = sheet.createRow(14);
                row1.setHeight((short) 800);
                List<Object[]> workshopEquipmentList = workShopEquipmentBean.getMonthlyReport(y + "", str[x], "H");
                sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 12));
                Cell cellTitle = row.createCell(0);
                cellTitle.setCellStyle(style.get("title"));
                cellTitle.setCellValue(y + "年" + "车间设备月报-----" + str[x]);
                Cell cellTime = row1.createCell(13);
                cellTime.setCellStyle(style.get("right"));
                cellTime.setCellValue("汉钟版");
                List<?> itemList = workshopEquipmentList;
                int j = 2;
                List<Object[]> list = (List<Object[]>) itemList;
                for (Object[] eq : list) {
                    row = sheet.getRow(j);
                    j++;
                    row.setHeight((short) 400);
                    Cell cell0 = row.getCell(0);
                    cell0.setCellValue(eq[0].toString());
                    for (int i = 1; i < 13; i++) {
                        cell0 = row.getCell(i);
                        if (eq[i] != null) {
                            cell0.setCellValue(Double.parseDouble(eq[i].toString()));
                        }
                    }
                }
                String path = "../" + String.format("%d年%d月%s", y, m, "车间设备月报--") + str[x] + ".xls";//新建文件保存路径
                FileOutputStream out = null;
                File file = new File(path);
                try {
                    if (file.exists() && file.isFile()) {
                        file.delete();
                        file = new File(path);
                    }
                    //写入新File
                    out = new FileOutputStream(file);
                    workbook.write(out);

                } catch (Exception e) {
                    log4j.error(e.getMessage());
                } finally {
                    if (null != out) {
                        out.flush();
                        out.close();
                    }
                }
                //添加到邮件发送
                addAttachments(file);
            }
        } catch (IOException | NumberFormatException | InvalidFormatException e) {
            return e.toString() + "Path:" + finalFilePath;
        }
        return BaseLib.formatDate("yyyy-MM-dd HH:mm", new Date());
    }

    /**
     * 设置导出EXCEL表格样式
     */
    private Map<String, CellStyle> createStyles(Workbook wb) {
        Map<String, CellStyle> styles = new LinkedHashMap<>();
        // 文件头样式
        CellStyle headStyle = wb.createCellStyle();
        headStyle.setWrapText(true);//设置自动换行
        headStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        headStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
        headStyle.setFillForegroundColor(IndexedColors.WHITE.getIndex());//单元格背景颜色
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
        headFont.setFontHeightInPoints((short) 11);
        headFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
        headStyle.setFont(headFont);
        styles.put("head", headStyle);

        // 正文样式
        CellStyle cellStyle = wb.createCellStyle();
        Font cellFont = wb.createFont();
        cellFont.setFontHeightInPoints((short) 10);
        cellStyle.setFont(cellFont);
        cellStyle.setWrapText(true);//设置自动换行
        cellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
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

        CellStyle leftStyle = wb.createCellStyle();
        leftStyle.setWrapText(true);//设置自动换行
        leftStyle.setAlignment(HSSFCellStyle.ALIGN_LEFT);
        leftStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
        leftStyle.setBorderLeft(CellStyle.BORDER_THIN);
        leftStyle.setBorderBottom(CellStyle.BORDER_THIN);
        leftStyle.setBorderRight(CellStyle.BORDER_THIN);
        styles.put("left", leftStyle);

        CellStyle rightStyle = wb.createCellStyle();
        rightStyle.setAlignment(HSSFCellStyle.ALIGN_RIGHT);
        rightStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
        styles.put("right", rightStyle);

        CellStyle titleStyle = wb.createCellStyle();
        titleStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        titleStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
        Font headFont2 = wb.createFont();
        headFont2.setFontHeightInPoints((short) 20);
        headFont2.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
        titleStyle.setFont(headFont2);
        styles.put("title", titleStyle);

        CellStyle dateStyle = wb.createCellStyle();
        dateStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        dateStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
        Font dateFont = wb.createFont();
        dateFont.setFontHeightInPoints((short) 11);
        dateFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
        dateStyle.setFont(headFont);
        styles.put("date", dateStyle);
        return styles;
    }

    @Override
    protected String getHtmlTable(List<Indicator> indicatorList, int y, int m, Date d, boolean needsum) {
        return "";
    }

    @Override
    protected String getHtmlTableRow(Indicator indicator, int y, int m, Date d) throws Exception {
        return "";
    }

}
