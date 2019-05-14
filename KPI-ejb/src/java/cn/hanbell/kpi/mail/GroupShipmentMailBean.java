/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.hanbell.kpi.mail;

import cn.hanbell.kpi.comm.MailNotification;
import cn.hanbell.kpi.ejb.erp.GroupHSShipmentBean;
import cn.hanbell.kpi.ejb.erp.GroupServiceBean;
import cn.hanbell.kpi.ejb.erp.GroupShipmentBean;
import cn.hanbell.kpi.ejb.erp.GroupVHServiceBean;
import cn.hanbell.kpi.ejb.erp.GroupVHShipmentBean;
import cn.hanbell.kpi.entity.Indicator;
import java.util.Date;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.LocalBean;

/**
 *
 * @author C0160
 */
@Stateless
@LocalBean
public class GroupShipmentMailBean extends MailNotification {

    @EJB
    private GroupShipmentBean bscGroupShipmentBean;
    @EJB
    private GroupServiceBean bscGroupServiceBean;
    @EJB
    private GroupHSShipmentBean bscGroupHSShipmentBean;
    @EJB
    private GroupVHShipmentBean bscGroupVHShipmentBean;
    @EJB
    private GroupVHServiceBean bscGroupVHServiceBean;

    public GroupShipmentMailBean() {

    }

    @Override
    public void init() {
        this.mailSetting = mailSettingBean.findByMailClazz(this.getClass().getName());
        super.init();
    }

    @Override
    protected String getHtmlTable(List<Indicator> indicatorList, int y, int m, Date d, boolean needsum) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    protected String getHtmlTableRow(Indicator indicator, int y, int m, Date d) throws Exception {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    protected String getMailBody() {
        try {
            bscGroupShipmentBean.updataActualValue(y, m, d);
            bscGroupServiceBean.updataActualValue(y, m, d);
            bscGroupVHShipmentBean.updataActualValue(y, m, d);
            bscGroupVHServiceBean.updataActualValue(y, m, d);
            bscGroupHSShipmentBean.updataActualValue(y, m, d);
            return "更新集团报表数据成功";
        } catch (Exception ex) {
            return ex.toString();
        }
    }

}
