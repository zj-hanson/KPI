/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.hanson.kpi.mail;

import cn.hanbell.kpi.entity.Indicator;
import com.lightshell.comm.BaseLib;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;

/**
 *
 * @author C1749
 */
@Stateless
@LocalBean
public class CPShipmentMailBean extends ShipmentMail {

    public List<Indicator> sumIndicatorList;//大合计集合
    public Indicator total;//大合计对象
    protected BigDecimal sum1 = BigDecimal.ZERO;
    protected BigDecimal sum2 = BigDecimal.ZERO;
    private BigDecimal totalActualValue;

    public CPShipmentMailBean() {
        this.sumIndicatorList = new ArrayList();
    }

    @Override
    public void init() {
        this.mailSetting = mailSettingBean.findByMailClazz(this.getClass().getName());
        if (sumIndicatorList != null) {
            this.sumIndicatorList.clear();
        }
        super.init();
    }

    @Override
    protected String getMailHead() {
        StringBuilder sb = new StringBuilder();
        sb.append("<html><head><title>Hanson</title>");
        sb.append(css);
        sb.append("</head><body><div style=\"margin: auto;text-align: center;\">");
        sb.append("<div style=\"width:100%\" class=\"title\">");
        sb.append("<div style=\"text-align:center;width:100%\">浙江汉声精密机械有限公司</div>");
        sb.append("<div style=\"text-align:center;width:100%\">").append(mailSubject).append("</div>");
        sb.append("<div style=\"text-align:center;width:100%; color:Red;\">日期:").append(BaseLib.formatDate("yyyy-MM-dd", d)).append("</div>");
        sb.append("</div>");
        return sb.toString();
    }

    //表头
    protected String getTableHead() {
        StringBuilder sb = new StringBuilder();
        sb.append("<div class=\"tbl\"><table width=\"100%\">");
        sb.append("<tr><th rowspan=\"2\" colspan=\"1\">产品别</th><th rowspan=\"2\" colspan=\"1\">本日</th>");
        sb.append("<th rowspan=\"1\" colspan=\"5\">本月</th><th rowspan=\"1\" colspan=\"5\">年累计</th>");
        sb.append("<th rowspan=\"2\" colspan=\"1\">年度目标</th><th rowspan=\"2\" colspan=\"1\">年度达成率</th><th rowspan=\"2\" colspan=\"1\">订单未交</th></tr>");
        sb.append("<tr><th colspan=\"1\">实际</th><th colspan=\"1\">目标</th><th colspan=\"1\">达成率</th><th colspan=\"1\">去年同期</th><th colspan=\"1\">成长率</th>");
        sb.append("<th colspan=\"1\">实际</th><th colspan=\"1\">目标</th><th colspan=\"1\">达成率</th><th colspan=\"1\">去年同期</th><th colspan=\"1\">成长率</th>");
        sb.append("</tr>");
        return sb.toString();
    }

    @Override
    protected String getHtmlTable(List<Indicator> indicatorList, int y, int m, Date d, boolean needsum) {
        getData().clear();
        getData().put("sum1", BigDecimal.ZERO);
        getData().put("sum2", BigDecimal.ZERO);
        StringBuilder sb = new StringBuilder();
        try {
            for (Indicator i : indicatorList) {
                sb.append(getHtmlTableRow(i, y, m, d));
            }
            if (needsum) {
                sumIndicator = indicatorBean.getSumValue(indicators);
                if (sumIndicator != null) {
                    sumIndicatorList.add(sumIndicator);
                    indicatorBean.updatePerformance(sumIndicator);
                    sb.append(getHtmlTableRow(sumIndicator, y, m, d, "'background-color:#ff8e67';"));
                }
            }
        } catch (Exception ex) {
            return ex.toString();
        }
        return sb.toString();
    }

    public void getSumBzValue(String aa) {
        String[] arr = aa.split(",");
        try {
            totalActualValue = BigDecimal.ZERO;
            List<Indicator> list = new ArrayList<>();
            for (int i = 0; i < arr.length; i++) {
                List<Indicator> data = indicatorBean.findByCategoryAndYear(arr[i], y);
                indicatorBean.getEntityManager().clear();
                for (Indicator entity : data) {
                    indicatorBean.divideByRate(entity, 2);
                    list.add(entity);
                }
            }
            if (!list.isEmpty()) {
                total = indicatorBean.getSumValue(list);
                totalActualValue = total.getActualIndicator().getNfy();
            }
        } catch (Exception ex) {
            log4j.error(ex);
        }
    }

    @Override
    public String getMailBody() {
        StringBuilder sb = new StringBuilder();
        sb.append("<div class=\"tableTitle\" ></div>");
        sb.append("<div class=\"tableTitle\" style='text-align:center'>出货统计報表(依客户材质别)</div>");
        sb.append("<div class=\"tableTitle\">单位：吨</div>");
        sb.append(getQuantityTable());
        sb.append("<div class=\"tableTitle\">单位：万元</div>");
        sb.append(getAmountTable());
        sb.append("<div class=\"tableTitle\" style='text-align:center'>出货统计報表(依产品别)</div>");
        sb.append("<div class=\"tableTitle\">单位：吨</div>");
        sb.append(getVarietyTonTable());
        sb.append("<div class=\"tableTitle\">单位：万元</div>");
        sb.append(getVarietyAmtsTable());
        sb.append("<div class=\"tableTitle\">預估訂單量: 客戶提供的意向訂單量&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;月實際出貨量: 本月累计出貨量</div>");
        sb.append("<div class=\"tableTitle\"><span>实际催货訂單: 客戶已通知截至昨日需出貨訂單量</span>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;年累計目標: 本年月累计至本月目标的目标值</div>");
        sb.append("<div class=\"tableTitle\"><span>未来几天催货訂單: 客戶已通知今日起需出貨訂單量</span>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;年累計達成率: (年累計實際/ 年累計目標) ×100%</div>");
        sb.append("<div class=\"tableTitle\">本月目標: 與查詢日期同月日累計至月底的目標值&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;年比重 ：(年实际/年合计) ×100%。</div>");
        sb.append("<div class=\"tableTitle\">本月達成率: (本月實際/ 本月目標) ×100%   </div>");
        sb.append("<div class=\"tableTitle\"><span style=\"color:red\">注：因當日出貨單會延後確認，故報表抓取截至昨日之出貨數據。</span></div>");
        return sb.toString();
    }

    protected String getQuantityTable() {
        sum1 = BigDecimal.ZERO;
        sum2 = BigDecimal.ZERO;
        sumIndicatorList.clear();

        getSumBzValue("汉声依SHB客户材质出货重量,汉声依THB客户材质出货重量,汉声依OTH客户材质出货重量");

        StringBuilder sb = new StringBuilder();
        sb.append("<div class=\"tbl\"><table width=\"100%\">");
        sb.append("<tr><th rowspan=\"2\" colspan=\"1\">客户</th><th rowspan=\"2\" colspan=\"1\">本日</th>");
        sb.append("<th rowspan=\"1\" colspan=\"5\">本月</th><th rowspan=\"1\" colspan=\"5\">年累计</th>");
        sb.append("<th rowspan=\"2\" colspan=\"1\">年度目标</th><th rowspan=\"2\" colspan=\"1\">年度达成率</th><th rowspan=\"2\" colspan=\"1\">订单未交</th></tr>");
        sb.append("<tr><th colspan=\"1\">实际</th><th colspan=\"1\">目标</th><th colspan=\"1\">达成率</th><th colspan=\"1\">去年同期</th><th colspan=\"1\">成长率</th>");
        sb.append("<th colspan=\"1\">实际</th><th colspan=\"1\">目标</th><th colspan=\"1\">达成率</th><th colspan=\"1\">去年同期</th><th colspan=\"1\">成长率</th>");
        sb.append("</tr>");

        //SHB
        this.indicators.clear();
        this.indicators = indicatorBean.findByCategoryAndYear("汉声依SHB客户材质出货重量", y);
        indicatorBean.getEntityManager().clear();
        if (indicators != null && !indicators.isEmpty()) {
            for (Indicator i : indicators) {
                indicatorBean.divideByRate(i, 2);
            }
            sb.append(getHtmlTable(indicators, y, m, d, true));
            total = getSumIndicator();
            total.setName("小计");
            sumIndicatorList.add(total);
            sum1 = sum1.add(getData().get("sum1"));
            sum2 = sum2.add(getData().get("sum2"));
        } else {
            sb.append("");
        }
        //THB
        this.indicators.clear();
        this.indicators = indicatorBean.findByCategoryAndYear("汉声依THB客户材质出货重量", y);
        indicatorBean.getEntityManager().clear();
        if (indicators != null && !indicators.isEmpty()) {
            for (Indicator i : indicators) {
                indicatorBean.divideByRate(i, 2);
            }
            sb.append(getHtmlTable(indicators, y, m, d, true));
            total = getSumIndicator();
            total.setName("小计");
            sumIndicatorList.add(total);
            sum1 = sum1.add(getData().get("sum1"));
            sum2 = sum2.add(getData().get("sum2"));
        } else {
            sb.append("");
        }
        //OTH
        this.indicators.clear();
        this.indicators = indicatorBean.findByCategoryAndYear("汉声依OTH客户材质出货重量", y);
        indicatorBean.getEntityManager().clear();
        if (indicators != null && !indicators.isEmpty()) {
            for (Indicator i : indicators) {
                indicatorBean.divideByRate(i, 2);
            }
            sb.append(getHtmlTable(indicators, y, m, d, true));
            total = getSumIndicator();
            total.setName("小计");
            sumIndicatorList.add(total);
            sum1 = sum1.add(getData().get("sum1"));
            sum2 = sum2.add(getData().get("sum2"));
        } else {
            sb.append("");
        }
        //最后的总合计 sumIndicator
        try {
            total = indicatorBean.getSumValue(sumIndicatorList);
            if (total != null) {
                total.setName("合计");
                getData().put("sum1", sum1);
                getData().put("sum2", sum2);
                sb.append(getHtmlTableRow(total, y, m, d, "'background-color:#ff8e67';"));
            }
        } catch (Exception ex) {
            return ex.toString();
        }
        sb.append("</table></div>");
        return sb.toString();

    }

    protected String getAmountTable() {
        sum1 = BigDecimal.ZERO;
        sum2 = BigDecimal.ZERO;
        sumIndicatorList.clear();

        getSumBzValue("汉声依SHB客户材质出货金额,汉声依THB客户材质出货金额,汉声依OTH客户材质出货金额");

        StringBuilder sb = new StringBuilder();
        sb.append(getTableHead());

        //SHB
        indicators.clear();
        indicators = indicatorBean.findByCategoryAndYear("汉声依SHB客户材质出货金额", y);
        indicatorBean.getEntityManager().clear();
        if (indicators != null && !indicators.isEmpty()) {
            for (Indicator i : indicators) {
                indicatorBean.divideByRate(i, 2);
            }
            sb.append(getHtmlTable(this.indicators, y, m, d, true));
            total = getSumIndicator();
            total.setName("小计");
            sumIndicatorList.add(total);
            sum1 = sum1.add(getData().get("sum1"));
            sum2 = sum2.add(getData().get("sum2"));
        } else {
            sb.append("");
        }
        //THB
        indicators.clear();
        indicators = indicatorBean.findByCategoryAndYear("汉声依THB客户材质出货金额", y);
        indicatorBean.getEntityManager().clear();
        if (indicators != null && !indicators.isEmpty()) {
            for (Indicator i : indicators) {
                indicatorBean.divideByRate(i, 2);
            }
            sb.append(getHtmlTable(this.indicators, y, m, d, true));
            total = getSumIndicator();
            total.setName("小计");
            sumIndicatorList.add(total);
            sum1 = sum1.add(getData().get("sum1"));
            sum2 = sum2.add(getData().get("sum2"));
        } else {
            sb.append("");
        }
        //OTH
        this.indicators.clear();
        indicators = indicatorBean.findByCategoryAndYear("汉声依OTH客户材质出货金额", y);
        indicatorBean.getEntityManager().clear();
        if (indicators != null && !indicators.isEmpty()) {
            for (Indicator i : indicators) {
                indicatorBean.divideByRate(i, 2);
            }
            sb.append(getHtmlTable(this.indicators, y, m, d, true));
            total = getSumIndicator();
            total.setName("小计");
            sumIndicatorList.add(total);
            sum1 = sum1.add(getData().get("sum1"));
            sum2 = sum2.add(getData().get("sum2"));
        } else {
            sb.append("");
        }

        //最后的总合计
        try {
            total = indicatorBean.getSumValue(sumIndicatorList);
            if (total != null) {
                getData().put("sum1", sum1);
                getData().put("sum2", sum2);
                total.setName("合计");
                sb.append(getHtmlTableRow(total, y, m, d, "'background-color:#ff8e67';"));
            }
        } catch (Exception ex) {
            return ex.toString();
        }
        sb.append("</table></div>");
        return sb.toString();
    }

    //按产品别分类重量
    protected String getVarietyTonTable() {
        getSumBzValue("汉声依种类别出货重量");
        StringBuilder sb = new StringBuilder();
        sb.append(getTableHead());
        this.indicators.clear();
        this.indicators = indicatorBean.findByCategoryAndYear("汉声依种类别出货重量", y);
        indicatorBean.getEntityManager().clear();
        if (indicators != null && !indicators.isEmpty()) {
            for (Indicator i : indicators) {
                indicatorBean.divideByRate(i, 2);
            }
            sb.append(getHtmlTable(this.indicators, y, m, d, true));
            sb.append("</table></div>");
            return sb.toString();
        } else {
            return "汉声依种类别出货重量";
        }
    }

    //按产品别分类 金额
    protected String getVarietyAmtsTable() {
        getSumBzValue("汉声依种类别出货金额");
        StringBuilder sb = new StringBuilder();
        sb.append(getTableHead());
        this.indicators.clear();
        this.indicators = indicatorBean.findByCategoryAndYear("汉声依种类别出货金额", y);
        indicatorBean.getEntityManager().clear();
        if (indicators != null && !indicators.isEmpty()) {
            for (Indicator i : indicators) {
                indicatorBean.divideByRate(i, 2);
            }
            sb.append(getHtmlTable(this.indicators, y, m, d, true));
            sb.append("</table></div>");
            return sb.toString();
        } else {
            return "汉声依种类别出货金额";
        }
    }

    public List<Indicator> getSumIndicatorList() {
        return sumIndicatorList;
    }

    public Indicator getTotal() {
        return total;
    }

}
