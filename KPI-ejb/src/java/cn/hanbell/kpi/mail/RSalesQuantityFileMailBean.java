/*
 * To change this license header, choose License Headers in Project Properties. To change this template file, choose
 * Tools | Templates and open the template in the editor.
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
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.ArrayList;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import java.util.Date;
import java.util.List;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;

/**
 *
 * @author C1879 R冷媒订单、出货统计表
 */
@Stateless
@LocalBean
public class RSalesQuantityFileMailBean extends MailNotification {

    private List<Indicator> shipmentQuantity;
    private List<Indicator> salesOrderQuantity;

    public RSalesQuantityFileMailBean() {

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

    private List<Indicator> getIndicatorList(String formid, int year, String deptno) {
        Indicator indicator = indicatorBean.findByFormidYearAndDeptno(formid, year, deptno);
        if (indicator != null) {
            return indicatorBean.findByPIdAndYear(indicator.getId(), year);
        }
        return null;
    }

    @Override
    protected String getMailBody() {
        attachments.clear();
        String finalFilePath = "";
        try {

            finalFilePath = this.getClass().getProtectionDomain().getCodeSource().getLocation().getPath();
            // 测试路径
            // int index = finalFilePath.indexOf("dist/gfdeploy");
            // InputStream is = new FileInputStream(finalFilePath.substring(1, index) +
            // "Hanbell-KPI/web/rpt/R冷媒订单出货统计表模板.xlsx");
            // 正式路径
            int index = finalFilePath.indexOf("KPI-ejb");
            InputStream is =
                new FileInputStream(finalFilePath.substring(1, index) + "Hanbell-KPI_war/rpt/R冷媒订单出货统计表模板.xlsx");
            Workbook workbook = WorkbookFactory.create(is);

            Sheet sheet;
            String annotation;
            indicators = getIndicatorList("R-R冷媒销售均价", y, "1F000");
            setShipmentlist();
            salesOrderQuantity = getIndicatorList("Q-R产品订单", y, "1F000");
            for (int i = 12; 1 <= i; i--) {
                if (i <= m) {
                    sheet = workbook.getSheetAt(i - 1);
                    annotation =
                        "注：本月指" + y + "年" + i + "月，上月指" + (i == 1 ? (y - 1 + "年12月") : (y + "年" + (i - 1) + "月"));
                    sheet.createRow(11).createCell(0).setCellValue(annotation);
                    CellRangeAddress merge = new CellRangeAddress(11, 11, 0, 4);
                    sheet.addMergedRegion(merge);
                    setCellValue(i, sheet, shipmentQuantity, salesOrderQuantity);
                    sheet.setForceFormulaRecalculation(true);
                } else {
                    workbook.removeSheetAt(i - 1);
                }

            }
            String path = "../" + String.format("%d年%d月%s", y, m, mailSubject) + ".xlsx";// 新建文件保存路径
            FileOutputStream out = null;
            File file = new File(path);
            // 如果文件存在,则删除已有的文件,重新创建一份新的
            try {
                if (file.exists() && file.isFile()) {
                    file.delete();
                    file = new File(path);
                }
                // 写入新File
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
            // 添加到邮件发送
            addAttachments(file);
        } catch (Exception e) {
            return e.toString() + "Path:" + finalFilePath;
        }
        return BaseLib.formatDate("yyyy-MM-dd HH:mm", new Date());
    }

    private void setShipmentlist() throws Exception {
        shipmentQuantity = new ArrayList<>();
        String col, mon;
        Field f;
        mon = indicatorBean.getIndicatorColumn("N", getM());
        BigDecimal v;
        Method setMethod;
        Indicator quantity;
        String[] arr;
        for (Indicator e : indicators) {
            String associatedIndicator = e.getAssociatedIndicator();
            if (associatedIndicator != null && !"".equals(associatedIndicator)) {
                arr = associatedIndicator.split(";");
                quantity = indicatorBean.findByFormidYearAndDeptno(arr[0].trim(), y, arr[2].trim());
                indicatorBean.getEntityManager().clear();
                // 得到去年需增减柯茂数据
                Indicator lastIndicator = indicatorBean.findByFormidYearAndDeptno(e.getFormid(), y - 1, e.getDeptno());
                if (e.getOther3Indicator() != null && e.getOther4Indicator() != null) {
                    for (int i = 1; i <= 12; i++) {
                        //2021年3月前的逻辑 实际台数 + 录入柯茂数据 - 销往柯茂数据
                        //实际台数
                        v = getNValue(quantity.getActualIndicator(), i);
                        setMethod = quantity.getActualIndicator().getClass().getDeclaredMethod("set" + indicatorBean.getIndicatorColumn("N", i).toUpperCase(), BigDecimal.class);
                        setMethod.invoke(quantity.getActualIndicator(), v);
                    }
                    // quantity指标数据中12月基准+ 去年录入12月柯茂数据 - 去年销往12月柯茂数据
                    if (lastIndicator != null) {
                        v = getNValue(quantity.getBenchmarkIndicator(), 12)
                            .add(getNValue(lastIndicator.getOther1Indicator(), 12))
                            .subtract(getNValue(lastIndicator.getOther3Indicator(), 12));
                        setMethod = quantity.getBenchmarkIndicator().getClass().getDeclaredMethod(
                            "set" + indicatorBean.getIndicatorColumn("N", 12).toUpperCase(), BigDecimal.class);
                        setMethod.invoke(quantity.getBenchmarkIndicator(), v);
                    }
                } else {
                    for (int i = 1; i <= 12; i++) {
                        ///实际台数
                        v = getNValue(quantity.getActualIndicator(), i);
                        setMethod = quantity.getActualIndicator().getClass().getDeclaredMethod("set" + indicatorBean.getIndicatorColumn("N", i).toUpperCase(), BigDecimal.class);
                        setMethod.invoke(quantity.getActualIndicator(), v);
                    }
                    // quantity指标数据中12月基准+ 去年录入12月柯茂数据
                    if (lastIndicator != null) {
                        v = getNValue(quantity.getBenchmarkIndicator(), 12)
                            .add(getNValue(lastIndicator.getOther1Indicator(), 12));
                        setMethod = quantity.getBenchmarkIndicator().getClass().getDeclaredMethod(
                            "set" + indicatorBean.getIndicatorColumn("N", 12).toUpperCase(), BigDecimal.class);
                        setMethod.invoke(quantity.getBenchmarkIndicator(), v);
                    }

                }

                shipmentQuantity.add(quantity);
            }
        }
    }

    private void setCellValue(int month, Sheet sheet, List<Indicator> shipmentQuantity,
        List<Indicator> salesOrderQuantity) {
        String mon;
        Field f;
        Row row;
        Cell cell;
        shipmentQuantity.sort((Indicator o1, Indicator o2) -> {
            if (o1.getSortid() > o2.getSortid()) {
                return 1;
            } else {
                return -1;
            }
        });
        int i = 3;
        for (Indicator shipment : shipmentQuantity) {
            if (i < 10) {
                row = sheet.getRow(i);
                if ("1T100".equals(shipment.getDeptno())) {
                    row.getCell(0).setCellValue("国际营销");
                } else {
                    row.getCell(0).setCellValue(shipment.getDeptname().substring(0, 2));
                }
                try {
                    for (Indicator salesOrde : salesOrderQuantity) {
                        if (salesOrde.getDeptno().equals(shipment.getDeptno())) {
                            mon = indicatorBean.getIndicatorColumn("N", month);
                            // 出货、订单当月
                            f = shipment.getActualIndicator().getClass().getDeclaredField(mon);
                            f.setAccessible(true);
                            cell = row.getCell(2);
                            cell.setCellValue(Double.valueOf(f.get(shipment.getActualIndicator()).toString()));
                            f = salesOrde.getActualIndicator().getClass().getDeclaredField(mon);
                            f.setAccessible(true);
                            cell = row.getCell(1);
                            cell.setCellValue(Double.valueOf(f.get(salesOrde.getActualIndicator()).toString()));
                            // 出货、订单上月
                            if (month == 1) {
                                mon = indicatorBean.getIndicatorColumn("N", 12);
                                f = shipment.getBenchmarkIndicator().getClass().getDeclaredField(mon);
                                f.setAccessible(true);
                                cell = row.getCell(4);
                                cell.setCellValue(Double.valueOf(f.get(shipment.getBenchmarkIndicator()).toString()));
                                f = salesOrde.getBenchmarkIndicator().getClass().getDeclaredField(mon);
                                f.setAccessible(true);
                                cell = row.getCell(3);
                                cell.setCellValue(Double.valueOf(f.get(salesOrde.getBenchmarkIndicator()).toString()));
                            } else {
                                mon = indicatorBean.getIndicatorColumn("N", month - 1);
                                f = shipment.getActualIndicator().getClass().getDeclaredField(mon);
                                f.setAccessible(true);
                                cell = row.getCell(4);
                                cell.setCellValue(Double.valueOf(f.get(shipment.getActualIndicator()).toString()));
                                f = salesOrde.getActualIndicator().getClass().getDeclaredField(mon);
                                f.setAccessible(true);
                                cell = row.getCell(3);
                                cell.setCellValue(Double.valueOf(f.get(salesOrde.getActualIndicator()).toString()));
                            }

                        }
                    }
                } catch (IllegalAccessException | IllegalArgumentException | NoSuchFieldException
                    | SecurityException e) {
                    System.out.println("cn.hanbell.kpi.mail.RAchievingRateFileMailBean.setCellValue()" + e.toString());
                }
            }
            i++;
        }

    }

    public BigDecimal getNValue(IndicatorDetail entity, int m) {
        String mon;
        BigDecimal value = BigDecimal.ZERO;
        Field f;
        try {
            mon = indicatorBean.getIndicatorColumn("N", m);
            f = entity.getClass().getDeclaredField(mon);
            f.setAccessible(true);
            value = BigDecimal.valueOf(Double.valueOf(f.get(entity).toString()));
        } catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException ex) {
            value = BigDecimal.ZERO;
        }
        return value;
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
