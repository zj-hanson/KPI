/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.hanbell.kpi.comm;

import cn.hanbell.kpi.ejb.IndicatorBean;
import cn.hanbell.kpi.ejb.MailSettingBean;
import cn.hanbell.kpi.entity.Indicator;
import cn.hanbell.kpi.entity.IndicatorDetail;
import cn.hanbell.kpi.entity.MailSetting;
import com.lightshell.comm.BaseLib;
import java.io.File;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import javax.ejb.EJB;

/**
 *
 * @author C0160
 */
public abstract class MailNotification {

    @EJB
    protected MailSettingBean mailSettingBean;

    @EJB
    protected IndicatorBean indicatorBean;

    protected MailNotify mail;

    protected String mailFrom = "noreply@hanbell.com.cn";
    protected String mailContent;
    protected String mailSubject;

    protected String company;

    protected MailSetting mailSetting;
    protected List<String> to;
    protected List<String> cc;
    protected List<String> bcc;
    protected List<File> attachments;

    protected List<Indicator> indicators;
    protected HashMap<String, BigDecimal> data;

    protected Calendar c;
    protected int y;
    protected int m;
    protected Date d;

    protected DecimalFormat decimalFormat;

    protected Actual updateActual;

    public MailNotification() {
        this.c = Calendar.getInstance();
        this.to = new ArrayList();
        this.cc = new ArrayList();
        this.bcc = new ArrayList();
        this.attachments = new ArrayList();
        this.indicators = new ArrayList();
        this.data = new HashMap<>();
        this.decimalFormat = new DecimalFormat("#,###");
    }

    public void init() {
        Calendar c = Calendar.getInstance();
        this.y = c.get(Calendar.YEAR);
        this.m = c.get(Calendar.MONTH) + 1;
        this.d = c.getTime();
        if (this.indicators != null) {
            this.indicators.clear();
        }
        this.to = mailSettingBean.findRecipientTo(mailSetting.getFormid());
        this.cc = mailSettingBean.findRecipientCc(mailSetting.getFormid());
        this.bcc = mailSettingBean.findRecipientBcc(mailSetting.getFormid());
        this.mailSubject = mailSetting.getName();
    }

    public void addAttachments(File f) {
        this.attachments.add(f);
    }

    public void addIndicators(Indicator i) {
        this.indicators.add(i);
    }

    protected String getHtmlTable(List<Indicator> indicatorList, int y, int m, Date d, boolean needsum) {
        getData().clear();
        getData().put("sum1", BigDecimal.ZERO);
        //取得月份字段
        StringBuilder sb = new StringBuilder();
        try {
            sb.append("<div class=\"tbl\"><table width=\"100%\">");
            sb.append("<tr><th rowspan=\"2\" colspan=\"1\">产品别</th><th rowspan=\"2\" colspan=\"1\">本日</th>");
            sb.append("<th rowspan=\"1\" colspan=\"5\">本月</th><th rowspan=\"1\" colspan=\"5\">年累计</th>");
            sb.append("<th rowspan=\"2\" colspan=\"1\">年度目标</th><th rowspan=\"2\" colspan=\"1\">年度达成率</th><th rowspan=\"2\" colspan=\"1\">订单未交</th></tr>");
            sb.append("<tr><th colspan=\"1\">实际</th><th colspan=\"1\">目标</th><th colspan=\"1\">达成率</th><th colspan=\"1\">去年同期</th><th colspan=\"1\">成长率</th>");
            sb.append("<th colspan=\"1\">实际</th><th colspan=\"1\">目标</th><th colspan=\"1\">达成率</th><th colspan=\"1\">去年同期</th><th colspan=\"1\">成长率</th>");
            sb.append("</tr>");
            for (Indicator i : indicatorList) {
                sb.append(getHtmlTableRow(i, y, m, d));
            }
            if (needsum) {
                Indicator sum = indicatorBean.getSumValue(indicators);
                if (sum != null) {
                    indicatorBean.updatePerformance(sum);
                    sb.append(getHtmlTableRow(sum, y, m, d));
                }
            }
            sb.append("</table></div>");
        } catch (Exception ex) {
            return ex.toString();
        }
        return sb.toString();
    }

    protected String getHtmlTableRow(Indicator indicator, int y, int m, Date d) throws Exception {
        //获取需要取值栏位
        String mon = indicatorBean.getIndicatorColumn(indicator.getFormtype(), m);
        StringBuilder sb = new StringBuilder();
        IndicatorDetail a = indicator.getActualIndicator();
        IndicatorDetail b = indicator.getBenchmarkIndicator();
        IndicatorDetail p = indicator.getPerformanceIndicator();
        IndicatorDetail t = indicator.getTargetIndicator();
        Field f;
        try {
            Actual actualInterface = (Actual) Class.forName(indicator.getActualInterface()).newInstance();
            actualInterface.setEJB(indicator.getActualEJB());
            BigDecimal num1 = actualInterface.getValue(y, m, d, Calendar.DATE, actualInterface.getQueryParams()).divide(indicator.getRate(), 2, RoundingMode.HALF_UP);
            if (indicator.getId() != -1) {
                sumAdditionalData("sum1", num1);
            }
            sb.append("<tr>");
            sb.append("<td>").append(indicator.getName()).append("</td>");
            sb.append("<td>").append(decimalFormat.format(indicator.getId() != -1 ? num1 : getData().get("sum1"))).append("</td>");
            //当月
            //实际
            f = a.getClass().getDeclaredField(mon);
            f.setAccessible(true);
            sb.append("<td>").append(decimalFormat.format(f.get(a))).append("</td>");
            //目标
            f = t.getClass().getDeclaredField(mon);
            f.setAccessible(true);
            sb.append("<td>").append(decimalFormat.format(f.get(t))).append("</td>");
            //达成
            f = p.getClass().getDeclaredField(mon);
            f.setAccessible(true);
            sb.append("<td>").append(percentFormat(f.get(p))).append("</td>");
            //同期
            f = b.getClass().getDeclaredField(mon);
            f.setAccessible(true);
            sb.append("<td>").append(decimalFormat.format(f.get(b))).append("</td>");
            //成长
            sb.append("<td>").append(percentFormat(indicatorBean.getGrowth(a, b, m))).append("</td>");
            //累计
            //实际
            sb.append("<td>").append(decimalFormat.format(indicatorBean.getAccumulatedValue(a, m))).append("</td>");
            //目标
            sb.append("<td>").append(decimalFormat.format(indicatorBean.getAccumulatedValue(t, m))).append("</td>");
            //达成
            sb.append("<td>").append(percentFormat(indicatorBean.getAccumulatedPerformance(a, t, m))).append("</td>");
            //同期
            sb.append("<td>").append(decimalFormat.format(indicatorBean.getAccumulatedValue(b, m))).append("</td>");
            //成长
            sb.append("<td>").append(percentFormat(indicatorBean.getAccumulatedGrowth(a, b, m))).append("</td>");
            //年度目标
            f = t.getClass().getDeclaredField("nfy");
            f.setAccessible(true);
            sb.append("<td>").append(decimalFormat.format(f.get(t))).append("</td>");
            //年度达成
            f = p.getClass().getDeclaredField("nfy");
            f.setAccessible(true);
            sb.append("<td>").append(percentFormat(f.get(p))).append("</td>");
            sb.append("<td>订单未交</td>");
            sb.append("</tr>");
        } catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException ex) {
            throw new Exception(ex);
        }
        return sb.toString();
    }

    protected String getMailHead() {
        StringBuilder sb = new StringBuilder();
        sb.append("<html><head><title></title>");
        sb.append("<link rel=\"stylesheet\" href=\"http://jws.hanbell.com.cn:8480/Hanbell-KPI/resources/css/mail.css\" type=\"text/css\"/>");
        sb.append("</head><body><div class=\"content\">");
        sb.append("<div style=\"width:100%\" class=\"title\">");
        sb.append("<div style=\"text-align:center;width:100%\">上海汉钟精机股份有限公司</div>");
        sb.append("<div style=\"text-align:center;width:100%\">").append(mailSubject).append("</div>");
        sb.append("<div style=\"text-align:center;width:100%; color:Red;\">日期:").append(BaseLib.formatDate("yyyy-MM-dd", d)).append("</div>");
        sb.append("</div>");
        return sb.toString();
    }

    protected abstract String getMailBody();

    protected String getMailFooter() {
        StringBuilder sb = new StringBuilder();
        sb.append("</div>");//对应Head中的div.content
        sb.append("<div class=\"divFoot\">此报表由系统自动发送,请不要直接回复</div>");
        sb.append("<div class=\"divFoot\">报表管理员</div>");
        sb.append("</div></body></html>");
        return sb.toString();
    }

    public void setMailContent() {
        mailContent = getMailHead() + getMailBody() + getMailFooter();
    }

    public void setMailSubject() {
        this.mailSubject = this.mailSubject + BaseLib.formatDate("yyyy/MM/dd", d);
    }

    public BigDecimal getPercent(BigDecimal up, BigDecimal down, int scale) {
        return up.divide(down, scale, RoundingMode.HALF_UP);
    }

    public void notify(MailNotify mail) {
        this.mail = mail;
        this.mail.sendMail(this);
    }

    public String percentFormat(double value) {
        return percentFormat(value, 2);
    }

    public String percentFormat(double value, int scale) {
        BigDecimal b = new BigDecimal(value);
        return percentFormat(b, scale);
    }

    public String percentFormat(BigDecimal value) {
        return percentFormat(value, 2);
    }

    public String percentFormat(BigDecimal value, int scale) {
        return String.format("%s%%", value.setScale(scale, RoundingMode.HALF_UP).toString());
    }

    public String percentFormat(Object o) {
        return percentFormat(o, 2);
    }

    public String percentFormat(Object o, int scale) {
        double value = Double.valueOf(o.toString());
        return percentFormat(value, scale);
    }

    public void setDecimalFormat(String format) {
        this.decimalFormat.applyPattern(format);
    }

    public void sumAdditionalData(String k, BigDecimal value) {
        try {
            getData().merge(k, value, (K, V) -> {
                return K.add(V);
            });
        } catch (Exception ex) {

        }
    }

    /**
     * @return the attachments
     */
    public List<File> getAttachments() {
        return attachments;
    }

    /**
     * @return the indicator
     */
    public List<Indicator> getIndicators() {
        return indicators;
    }

    /**
     * @return the company
     */
    public String getCompany() {
        return company;
    }

    /**
     * @param company the company to set
     */
    public void setCompany(String company) {
        this.company = company;
    }

    /**
     * @return the y
     */
    public int getY() {
        return y;
    }

    /**
     * @return the m
     */
    public int getM() {
        return m;
    }

    /**
     * @return the d
     */
    public Date getD() {
        return d;
    }

    /**
     * @param d the d to set
     */
    public void setD(Date d) {
        this.d = d;
        c.setTime(d);
        this.y = c.get(Calendar.YEAR);
        this.m = c.get(Calendar.MONTH) + 1;
    }

    /**
     * @return the data
     */
    public HashMap<String, BigDecimal> getData() {
        return data;
    }

}
