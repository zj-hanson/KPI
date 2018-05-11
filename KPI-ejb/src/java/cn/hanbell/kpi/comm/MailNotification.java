/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.hanbell.kpi.comm;

import cn.hanbell.kpi.ejb.IndicatorBean;
import cn.hanbell.kpi.ejb.MailSettingBean;
import cn.hanbell.kpi.entity.Indicator;
import cn.hanbell.kpi.entity.MailSetting;
import com.lightshell.comm.BaseLib;
import java.io.File;
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

    protected String mailFrom = "sys@hanbell.cn";
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

    protected abstract String getHtmlTable(List<Indicator> indicatorList, int y, int m, Date d, boolean needsum);

    protected abstract String getHtmlTableRow(Indicator indicator, int y, int m, Date d) throws Exception;

    protected String getMailHead() {
        StringBuilder sb = new StringBuilder();
        sb.append("<html><head><title>Hanbell</title>");
        sb.append("<link rel=\"stylesheet\" href=\"http://jws.hanbell.com.cn:8480/Hanbell-war/resources/css/mail.css\" type=\"text/css\"/>");
        sb.append("</head><body><div style=\"margin: auto;text-align: center;\">");
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
