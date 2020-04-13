/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vn.hanbell.kpi.mail;

import cn.hanbell.kpi.comm.MailNotification;
import cn.hanbell.kpi.ejb.erp.BscGroupVHSaleOrderBean;
import cn.hanbell.kpi.ejb.erp.BscGroupVHShipmentBean;
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
public class BscGroupVNShipmentMailBean extends MailNotification {

    @EJB
    private BscGroupVHShipmentBean bscGroupVHShipmentBean;
    @EJB
    private BscGroupVHSaleOrderBean bscGroupVHSaleOrderBean;
    
    public BscGroupVNShipmentMailBean() {

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
            //越南出货
            bscGroupVHShipmentBean.updataShpimentActualValue(y, m, d);
             log4j.info("End Execute Job updataShpimentActualValue");
             //越南订单
             bscGroupVHSaleOrderBean.updataSaleOrderActualValue(y, m, d);
            log4j.info("End Execute Job updataSaleOrderActualValue");
             //越南服务
             bscGroupVHShipmentBean.updataServerActualValue(y, m, d);
             log4j.info("End Execute Job updataServerActualValue");
            return "越南数据更新集团报表数据成功";
        } catch (Exception ex) {
            return ex.toString();
        }
    }

}
