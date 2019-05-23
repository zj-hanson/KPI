/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tw.hanbell.kpi.ejb.erp;

import cn.hanbell.kpi.comm.MailNotification;
import cn.hanbell.kpi.entity.Indicator;
import com.lightshell.comm.BaseLib;
import java.util.Calendar;
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
public class MailNotificationBean extends MailNotification {

    public MailNotificationBean() {
        super();
    }

    public void init(String subject, String content) {
        super.init(); //To change body of generated methods, choose Tools | Templates.

    }

    public void addTo(String mailAddress) {
        this.to.add(mailAddress);
    }

    public void setMailContent(String a) {
        mailContent = getMailHead() + a + getMailFooter();
    }
    
    @Override
    protected String getMailHead() {
        StringBuilder sb = new StringBuilder();
        Calendar now = Calendar.getInstance();
        Date date = now.getTime();
        sb.append("<html><head><title>Hanbell</title>");
        sb.append(css);
        sb.append("</head><body><div style=\"margin: auto;text-align: center;\">");
        sb.append("<div style=\"width:100%\" class=\"title\">");
        sb.append("<div style=\"text-align:center;width:100%\">上海汉钟精机股份有限公司</div>");
        sb.append("<div style=\"text-align:center;width:100%; color:Red;\">日期:").append(BaseLib.formatDate("yyyy-MM-dd HH:mm:ss", date)).append("</div>");
        sb.append("</div>");
        return sb.toString();
    }

    @Override
    protected String getMailFooter() {
        return super.getMailFooter();
    }

    @Override
    protected String getHtmlTable(List<Indicator> indicatorList, int y, int m, Date d, boolean needsum) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    protected String getHtmlTableRow(Indicator indicator, int y, int m, Date d) throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    protected String getMailBody() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
