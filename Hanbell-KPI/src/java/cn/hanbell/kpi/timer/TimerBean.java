/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.hanbell.kpi.timer;

import cn.hanbell.kpi.comm.MailNotify;
import cn.hanbell.kpi.ejb.IndicatorBean;
import cn.hanbell.kpi.ejb.MailSettingBean;
import cn.hanbell.kpi.entity.Indicator;
import cn.hanbell.kpi.mail.AJMailBean;
import java.util.Calendar;
import java.util.List;
import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.Schedule;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.ejb.TimerService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author C0160
 */
@Singleton
@Startup
public class TimerBean {

    @EJB
    private AJMailBean ajMailBean;

    @EJB
    private IndicatorBean indicatorBean;

    @EJB
    private MailSettingBean mailSettingBean;

    @Resource
    TimerService timerService;

    private final Logger logger = LogManager.getLogger();

    public TimerBean() {

    }

    @Schedule(hour = "*/1", persistent = false)
    public void updateIndicatorActualValue() {
        Calendar c = Calendar.getInstance();
        c.add(Calendar.DATE, -1);
        List<Indicator> indicatorList = indicatorBean.findByObjtypeAndYear("P", c.get(Calendar.YEAR));
        if (indicatorList != null && !indicatorList.isEmpty()) {
            for (Indicator e : indicatorList) {
                if (e.getActualInterface() != null && !"".equals(e.getActualInterface())) {
                    try {
                        indicatorBean.updateActual(e.getId(), c.get(Calendar.YEAR), c.get(Calendar.MONTH) + 1, c.getTime(), Calendar.MONTH);
                        logger.info(String.format("成功执行%s:更新指标%s实际值:Id:%d", "updateIndicatorActualValue", e.getName(), e.getId()));
                        indicatorBean.getEntityManager().refresh(e);
                        indicatorBean.updatePerformance(e);
                        indicatorBean.update(e);
                        logger.info(String.format("成功执行%s:更新指标%s达成率:Id:%d", "updateIndicatorActualValue", e.getName(), e.getId()));
                    } catch (Exception ex) {
                        logger.error(String.format("执行%s:更新指标%s:Id:%d时异常", "updateIndicatorActualValue", e.getName(), e.getId()), ex);
                    }
                }
            }
        }
        logger.info("updateIndicatorActualValue轮询");
    }

    @Schedule(hour = "10", persistent = true)
    public void sendKPIReport() {
        Calendar c = Calendar.getInstance();
        c.add(Calendar.DATE, -1);
        try {
            ajMailBean.init();
            ajMailBean.setD(c.getTime());
            ajMailBean.setMailContent();
            ajMailBean.setMailSubject();
            ajMailBean.notify(new MailNotify());
            logger.info(String.format("成功执行%s:发送报表%s", "sendKPIReport", "AAMailBean"));
        } catch (Exception ex) {
            logger.error(String.format("执行%s:发送报表%s时异常", "sendKPIReport", "AAMailBean"), ex);
        }
    }

}
