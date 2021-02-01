/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tw.hanbell.kpi.mail;

import cn.hanbell.kpi.comm.MailNotification;
import cn.hanbell.kpi.entity.Indicator;
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
        this.mailSetting = mailSettingBean.findByMailClazz(this.getClass().getName());
        super.init();
    }

    @Override
    protected String getMailBody() {
        StringBuilder sb = new StringBuilder();
        int num;
        try {
            num = grpsdailytmpBean.updateActualList(y, m, d);
            if (num > 0) {
                
                sb.append("<div style=\"text-align:center;width:100%\">资料上传成功！</div>");
                sb.append("<div style=\"text-align:center;width:100%\">一共").append(num).append("笔资料！</div>");
                if (!grpsdailytmpBean.isFlag()) {
                    sb.append("<div style=\"text-align:center;width:100%\">产品别有空值！！！</div>");
                }
            } else if (num == 0) {
                sb.append("<div style=\"text-align:center;width:100%\">本日无新数据！</div>");
            } else {
                sb.append("<div style=\"text-align:center;width:100%\">资料上传失败，请重新手动执行！</div>");
            }
            sb.append(getMailFooter());
            return sb.toString();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return "资料写入台湾ERP失败！";
    }

    @Override
    protected String getHtmlTable(List<Indicator> indicatorList, int y, int m, Date d, boolean needsum) {
        return " ";
    }

    @Override
    protected String getHtmlTableRow(Indicator indicator, int y, int m, Date d) throws Exception {
        return " ";
    }

}
