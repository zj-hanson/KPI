/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.hanbell.kpi.mail;

import cn.hanbell.kpi.comm.MailNotification;
import cn.hanbell.kpi.entity.Indicator;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import tw.hanbell.kpi.ejb.erp.GrpsdailytmpBean;

/**
 *
 * @author C1749
 */
@Stateless
@LocalBean
public class GrpsdailytmpMailBean extends MailNotification {

    @EJB
    private GrpsdailytmpBean grpsdailytmpBean;

    public GrpsdailytmpMailBean() {

    }

    @Override
    public void init() {
        this.y = c.get(Calendar.YEAR);
        this.m = c.get(Calendar.MONTH) + 1;
        this.d = c.getTime();
        this.mailSetting = mailSettingBean.findByMailClazz(this.getClass().getName());
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
        boolean a;
        try {
            a = grpsdailytmpBean.updateActualList(y, m, d);
            if (a == true) {
                return "数据上传成功！！！";
            }
            return "数据上传失败！！！";
        } catch (Exception ex) {
            return ex.toString();
        }
    }

}
