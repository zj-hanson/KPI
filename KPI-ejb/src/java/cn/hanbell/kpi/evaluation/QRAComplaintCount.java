/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.hanbell.kpi.evaluation;

import java.math.BigDecimal;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.Query;

/**
 *
 * @author C1749 有效客诉笔数
 */
public class QRAComplaintCount extends QRA {

    public QRAComplaintCount() {
        super();
    }

    //当月有效客诉
    @Override
    public BigDecimal getValue(int y, int m, Date d, int type, LinkedHashMap<String, Object> map) {
        //定义变量属性 获得查询参数
        String BQ197 = map.get("BQ197") != null ? map.get("BQ197").toString() : ""; //产品别
        String BQ003 = map.get("BQ003") != null ? map.get("BQ003").toString() : ""; //品號類別代號
        String BQ505 = map.get("BQ505") != null ? map.get("BQ505").toString() : ""; //冷媒特殊处理 不算BQ134为-1的情况
        String BQ110 = map.get("BQ110") != null ? map.get("BQ110").toString() : ""; //是否客诉
        String CA500 = map.get("CA500") != null ? map.get("CA500").toString() : ""; //机型
        int ksship = 0;
        StringBuilder sb = new StringBuilder();
        sb.append(" select count(a.CA009) as num from (  ");
        sb.append(" select  DISTINCT CA009,BQ001 from SERBQ,SERCA where  BQ001= CA001 ");
        if (!"".equals(BQ197)) {
            sb.append(" and  rtrim(BQ197) ").append(BQ197);
        }
        if (!"".equals(BQ003)) {
            sb.append(" and BQ003 ").append(BQ003);
        }
        if (!"".equals(BQ505)) {
            sb.append(" and BQ505 ").append(BQ505);
        }
        if (!"".equals(BQ110)) {
            sb.append(" and BQ110 ").append(BQ110);
        }
        if (CA500.contains("干式")) {
            sb.append(" and (CA500 not like 'PX%' and CA500 not like 'PZ%') ");
        }
        if (CA500.contains("湿式")) {
            sb.append(" and (CA500  like 'PX%' or CA500  like 'PZ%') ");
        }
        sb.append(" and year(BQ021) = ${y} and month(BQ021)= ${m} ");
        sb.append(") as a ");
        String sql = sb.toString().replace("${y}", String.valueOf(y)).replace("${m}", String.valueOf(m));
        Query query = superEJBForCRM.getEntityManager().createNativeQuery(sql);
        try {
            Object o = query.getSingleResult();
            ksship = Integer.parseInt(o.toString());
            return BigDecimal.valueOf((int) ksship);
        } catch (Exception ex) {
            Logger.getLogger(QRAComplaintCount.class.getName()).log(Level.SEVERE, null, ex);
        }
        return BigDecimal.ZERO;
    }

    @Override
    public int getUpdateMonth(int y, int m) {
        //每月3-5号自动更新上个月的数据
        int month;
        if (m == 1) {
            month = 12;
        } else {
            month = m - 1;
        }
        return month;
    }

    @Override
    public int getUpdateYear(int y, int m) {
        //每月3-5号自动更新上个月的数据
        int year;
        if (m == 1) {
            year = y - 1;
        } else {
            year = y;
        }
        return year;
    }

}
