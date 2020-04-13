/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.hanbell.kpi.mail;

import cn.hanbell.kpi.comm.MailNotification;
import cn.hanbell.kpi.ejb.erp.BscGroupHSSaleOrderBean;
import cn.hanbell.kpi.ejb.erp.BscGroupHSShipmentBean;
import cn.hanbell.kpi.ejb.erp.BscGroupSHSaleOrderBean;
import cn.hanbell.kpi.ejb.erp.BscGroupSHServiceBean;
import cn.hanbell.kpi.ejb.erp.BscGroupSHShipmentBean;
import cn.hanbell.kpi.entity.Indicator;
import java.util.Date;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.LocalBean;
import tw.hanbell.kpi.ejb.erp.GrpsdailytmpBean;

/**
 *
 * @author C1749
 * @description：只更新出货和服务的
 */
@Stateless
@LocalBean
public class BscGroupShipmentMailBean extends MailNotification {

    @EJB
    private BscGroupSHShipmentBean bscGroupSHShipmentBean;
    @EJB
    private BscGroupSHServiceBean bscGroupSHServiceBean;
    @EJB
    private BscGroupHSShipmentBean bscGroupHSShipmentBean;
    @EJB
    private GrpsdailytmpBean grpsdailytmpBean;

    public BscGroupShipmentMailBean() {

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
            //汉钟的出货
            bscGroupSHShipmentBean.updataShpimentActualValue(y, m, d);
            log4j.info("End Execute Job updataShpimentActualValue");
            //汉钟的服务
            bscGroupSHServiceBean.updataServerActualValue(y, m, d);
            log4j.info("End Execute Job updataServerActualValue");
            //汉声的出货
            bscGroupHSShipmentBean.updataShpimentActualValue(y, m, d);
            log4j.info("End Execute Job updataShpimentActualValue");
            //再更新到台湾ERP
            StringBuilder sb = new StringBuilder();
            int num;
            num = grpsdailytmpBean.updateActualList(y, m, d);
            log4j.info("End Execute Job updateActualList");
            if (num > 0) {
                sb.append("<div style=\"text-align:center;width:100%\">资料上传台湾ERP成功！</div>");
                sb.append("<div style=\"text-align:center;width:100%\">一共").append(num).append("笔资料！</div>");
            } else if (num == 0) {
                sb.append("<div style=\"text-align:center;width:100%\">本日无新数据！</div>");
            } else {
                sb.append("<div style=\"text-align:center;width:100%\">资料上传台湾ERP失败，请重新手动执行！</div>");
            }
            sb.append(getMailFooter());
            return "更新集团报表（汉钟中间表）数据成功" + sb.toString();
        } catch (Exception ex) {
            return ex.toString() + "捕获异常，请检查排程！！！";
        }
    }

}
