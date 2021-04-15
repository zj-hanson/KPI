/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.hanson.kpi.mail;

import cn.hanbell.kpi.entity.Indicator;
import cn.hanson.kpi.evaluation.SalesOrderTon;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.Stateless;
import javax.ejb.LocalBean;

/**
 *
 * @author C0160
 */
@Stateless
@LocalBean
public class SalesSheetMailBean extends SheetMail {

    protected List<Indicator> sumIndicatorList;

    public SalesSheetMailBean() {
        this.sumIndicatorList = new ArrayList();
    }

    @Override
    public void init() {
        this.mailSetting = mailSettingBean.findByMailClazz(this.getClass().getName());
        if (sumIndicatorList != null) {
            this.sumIndicatorList.clear();
        }
        salesOrder = new SalesOrderTon();
        super.init();
    }

    @Override
    protected String getMailBody() throws Exception {
        StringBuilder sb = new StringBuilder();
        sb.append("<div class=\"tableTitle\">单位：吨</div>");
        sb.append(getSalesOrderTonTable());
        sb.append("<div class=\"tableTitle\" style='text-align:center'>主要客户订单统计</div>");
        sb.append("<div class=\"tableTitle\">单位：吨</div>");
        sb.append(getCustomerOrderTonTable());
        sb.append("<div class=\"tableTitle\">本月实际: 本月累计订单量</div>");
        sb.append("<div class=\"tableTitle\">本月目标: 年度方针设定的月完成目标</div>");
        sb.append("<div class=\"tableTitle\">本月达成: (本月实际/本月目标) ×100% </div>");
        sb.append("<div class=\"tableTitle\">年累计实际: 累计至报表查询日的订单量</div>");
        sb.append("<div class=\"tableTitle\">年累计目标: 累计至报表查询日的目标值</div>");
        sb.append("<div class=\"tableTitle\">年累计达成: (年累计实际/年累计目标) ×100% </div>");
        sb.append("<div class=\"tableTitle\"><span style=\"color:red\">注：报表数据已做合并抵消，扣除汉扬销售汉声部分</span></div>");
        return sb.toString();
    }

    private String getSalesOrderTonTable() {
        sumIndicatorList.clear();

        StringBuilder sb = new StringBuilder();

        this.indicators.clear();
        this.indicators = indicatorBean.findByCategoryAndYear("HSHY订单重量", y);
        indicatorBean.getEntityManager().clear();
        //指标排序
        indicators.sort((Indicator o1, Indicator o2) -> {
            if (o1.getSortid() > o2.getSortid()) {
                return 1;
            } else {
                return -1;
            }
        });
        if (indicators != null && !indicators.isEmpty()) {
            for (Indicator i : indicators) {
                //按换算率计算结果
                indicatorBean.divideByRate(i, 2);
            }
            sb.append(getHtmlTable(indicators, y, m, d, true, "HS/HY合计"));
        } else {
            sb.append("");
        }

        return sb.toString();
    }

    private String getCustomerOrderTonTable() {
        sumIndicatorList.clear();

        StringBuilder sb = new StringBuilder();

        this.indicators.clear();
        this.indicators = indicatorBean.findByCategoryAndYear("主要客户订单重量", y);
        indicatorBean.getEntityManager().clear();
        //指标排序
        indicators.sort((Indicator o1, Indicator o2) -> {
            if (o1.getSortid() > o2.getSortid()) {
                return 1;
            } else {
                return -1;
            }
        });
        if (indicators != null && !indicators.isEmpty()) {
            for (Indicator i : indicators) {
                //按换算率计算结果
                indicatorBean.divideByRate(i, 2);
            }
            sb.append(getHtmlTable(indicators, y, m, d, false));
        } else {
            sb.append("");
        }

        return sb.toString();
    }

}
