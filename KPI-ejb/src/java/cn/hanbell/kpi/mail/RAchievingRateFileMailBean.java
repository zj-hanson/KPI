/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.hanbell.kpi.mail;

import cn.hanbell.kpi.comm.MailNotification;
import cn.hanbell.kpi.entity.Indicator;
import cn.hanbell.kpi.entity.IndicatorDetail;
import com.lightshell.comm.BaseLib;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import java.util.Date;
import java.util.List;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;

/**
 *
 * @author C1879 R冷媒目标与实际达成率表按月附件发出
 */
@Stateless
@LocalBean
public class RAchievingRateFileMailBean extends MailNotification {

    public RAchievingRateFileMailBean() {

    }

    @Override
    public void init() {
        this.mailSetting = mailSettingBean.findByMailClazz(this.getClass().getName());
        super.init();
    }

    @Override
    protected String getMailHead() {
        return "";
    }

    @Override
    protected String getMailFooter() {
        return "";
    }

    private List<Indicator> getIndicatorList(String fromid, int year, String deptno) {
        Indicator indicator = indicatorBean.findByFormidYearAndDeptno(fromid, year, deptno);
        if (indicator != null) {
            return indicatorBean.findByPIdAndYear(indicator.getId(), year);
        }
        return null;
    }

    @Override
    protected String getMailBody() {
        attachments.clear();
        try {
            String finalFilePath = this.getClass().getResource("").getPath();
            int index = finalFilePath.indexOf("dist/gfdeploy");
            InputStream is = new FileInputStream(finalFilePath.substring(1, index) + "Hanbell-KPI/web/rpt/R冷媒目标与实际达成率模板.xlsx");
            Workbook workbook = WorkbookFactory.create(is);
            Row row;
            Cell cell;

            //sheet1为台数
            Sheet sheet1 = workbook.getSheetAt(0);
            //设置年份
            cell = sheet1.getRow(1).createCell(1);
            cell.setCellValue(y + "年");
            //本年度
            indicators = getIndicatorList("Q-R产品出货", y, "1F000");
            //setCellValue(赋值开始行 展现的月份 表页 集合 单元格样式 换算率)
            setCellValue(5, m, sheet1, indicators, 1);
            indicators.clear();
            //去年同期
            indicators = getIndicatorList("Q-R产品出货", y - 1, "1F000");
            //setCellValue(赋值开始行 展现的月份 表页 集合 单元格样式 换算率)
            setCellValue(12, 12, sheet1, indicators,1);
            //更新页面公式
            sheet1.setForceFormulaRecalculation(true);

            //sheet2金额
            Sheet sheet2 = workbook.getSheetAt(1);
            //设置年份
            cell = sheet2.getRow(1).createCell(1);
            cell.setCellValue(y + "年");
            //本年度
            indicators = getIndicatorList("A-R产品出货", y, "1F000");
            //setCellValue(赋值开始行 展现的月份 表页 集合 单元格样式 换算率)
            setCellValue(5, m, sheet2, indicators, 10000);
            indicators.clear();
            //去年同期
            indicators = getIndicatorList("A-R产品出货", y - 1, "1F000");
            setCellValue(12, 12, sheet2, indicators, 10000);
            sheet2.setForceFormulaRecalculation(true);

            String path = "../" + y + "年 R冷媒目标与实际达成率.xlsx";//新建文件保存路径
            FileOutputStream out = null;
            File file = new File(path);
            //如果文件存在,则删除已有的文件,重新创建一份新的
            try {
                if (file.exists() && file.isFile()) {
                    file.delete();
                    file = new File(path);
                }
                //写入新File
                out = new FileOutputStream(file);
                workbook.write(out);
            } catch (IOException e) {
                log4j.error(e.getMessage());
            } finally {
                if (null != out) {
                    out.flush();
                    out.close();
                }
            }
            //添加到邮件发送
            addAttachments(file);
        } catch (Exception e) {
            return e.toString();
        }
        return BaseLib.formatDate("yyyy-MM-dd HH:mm", new Date());
    }

    /**
     *
     * @param rowcount 赋值开始行
     * @param month 展现的月份
     * @param sheet 表页
     * @param list 集合
     * @param rate 换算率
     */
    private void setCellValue(int rowcount, int month, Sheet sheet, List<Indicator> list, int rate) {
        String mon;
        Field f;
        IndicatorDetail actual, target;
        int i = rowcount;
        Row row;
        Cell cell;
        list.sort((Indicator o1, Indicator o2) -> {
            if (o1.getSortid() > o2.getSortid()) {
                return 1;
            } else {
                return -1;
            }
        });
        for (Indicator indicator : list) {
            actual = indicator.getActualIndicator();
            target = indicator.getTargetIndicator();
            if (i < (rowcount + 6)) {
                row = sheet.getRow(i);
                if ("1T100".equals(indicator.getDeptno())) {
                    row.getCell(1).setCellValue("国际营销");
                } else {
                    row.getCell(1).setCellValue(indicator.getDeptname().substring(0, 2));
                }
                //根据月份赋值,目标值起始单元格下标为2 实际值为3；每月下标加3
                int t = 2;
                int a = 3;
                //目标实际赋值
                for (int j = 1; j <= month; j++) {
                    try {
                        //目标
                        mon = indicatorBean.getIndicatorColumn("N", j);
                        f = target.getClass().getDeclaredField(mon);
                        f.setAccessible(true);
                        cell = row.getCell(t);
                        cell.setCellValue(Double.valueOf(f.get(target).toString()) / rate);
                        //实际
                        f = actual.getClass().getDeclaredField(mon);
                        f.setAccessible(true);
                        cell =  row.getCell(a);
                        cell.setCellValue(Double.valueOf(f.get(actual).toString()) / rate);
                    } catch (IllegalAccessException | IllegalArgumentException | NoSuchFieldException | SecurityException e) {
                        System.out.println("cn.hanbell.kpi.mail.RAchievingRateFileMailBean.setCellValue()" + e.toString());
                    }
                    t = t + 3;
                    a = a + 3;
                }
            }
            i++;
        }
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
