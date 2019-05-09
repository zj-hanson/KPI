/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.hanbell.kpi.mail;

import cn.hanbell.kpi.comm.BscProductiontMail;
import cn.hanbell.kpi.entity.Indicator;
import cn.hanbell.kpi.entity.IndicatorDaily;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;

/**
 *
 * @author C1879
 */
@Stateless
@LocalBean
public class KORCPorductionMailBean extends BscProductiontMail {

    public KORCPorductionMailBean() {
        
    }

    @Override
    public void init() {
        this.mailSetting = mailSettingBean.findByMailClazz(this.getClass().getName());
        super.init();
    }   
    
    @Override
    protected String getMailBody() {
        indicator = indicatorBean.findByFormidYearAndDeptno("Q-ORC机组每日生产", y, "1P000");
        if (indicator == null) {
            throw new UnsupportedOperationException("Not found 每日生产");
        }
        indicators.clear();
        indicators.add(indicator);
        StringBuilder sb = new StringBuilder();
        sb.append("<div class=\"tableTitle\">单位：").append(indicator.getUnit()).append("</div>");
        sb.append(getHtmlTable(indicators, y, m, d, true));
        return sb.toString();
    }

    
}
