/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.hanbell.kpi.evaluation;

import cn.hanbell.kpi.comm.Actual;
import cn.hanbell.kpi.comm.SuperEJBForERP;
import cn.hanbell.kpi.ejb.IndicatorBean;
import cn.hanbell.kpi.ejb.IndicatorDetailBean;
import cn.hanbell.kpi.entity.Indicator;
import cn.hanbell.kpi.entity.IndicatorDetail;
import com.lightshell.comm.BaseLib;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.logging.Level;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.persistence.Query;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author C1879 应收账款周转天数 月初更新上月
 */
public class TurnoverDays implements Actual {

    protected SuperEJBForERP superEJB;

    protected LinkedHashMap<String, Object> queryParams;

    protected BigDecimal arm232 = BigDecimal.ZERO;
    protected BigDecimal arm235 = BigDecimal.ZERO;
    protected BigDecimal arm270 = BigDecimal.ZERO;
    protected BigDecimal arm423 = BigDecimal.ZERO;

    protected final Logger log4j = LogManager.getLogger();

    IndicatorDetailBean indicatorDailyBean = lookupIndicatorDetailBeanBean();

    public TurnoverDays() {
        queryParams = new LinkedHashMap<>();
    }

    public SuperEJBForERP getEJB() {
        return superEJB;
    }

    @Override
    public void setEJB(String JNDIName) throws Exception {
        InitialContext c = new InitialContext();
        Object objRef = c.lookup(JNDIName);
        superEJB = (SuperEJBForERP) objRef;
    }

    public Date getFirstDayOfMonth(Date d) {
        Calendar c = Calendar.getInstance();
        c.setTime(d);
        c.set(Calendar.DATE, 1);
        return c.getTime();
    }

    @Override
    public BigDecimal getValue(int y, int m, Date d, int type, LinkedHashMap<String, Object> map) {
        String facno = map.get("facno") != null ? map.get("facno").toString() : "";
        String[] arr = facno.split(",");
        map.remove("facno");
        //应收帐款周转天数计算公式: 期间天数/(不含税销售收入*2/(2*期末应收账款 + 实收款-含税出货))
        String id = map.get("id") != null ? map.get("id").toString() : "";
        IndicatorBean indicatorBean = lookupIndicatorBeanBean();
        BigDecimal value = BigDecimal.ZERO;
        BigDecimal cdrhads = BigDecimal.ZERO;
        BigDecimal cdrhad = BigDecimal.ZERO;
        BigDecimal rectotalValue = BigDecimal.ZERO;
        BigDecimal booamtValue = BigDecimal.ZERO;
        BigDecimal valuelj = BigDecimal.ZERO;
        BigDecimal cdrhadslj = BigDecimal.ZERO;
        BigDecimal cdrhadlj = BigDecimal.ZERO;
        BigDecimal rectotalValuelj = BigDecimal.ZERO;
        for (String arr1 : arr) {
            map.put("facno", arr1);
            //含税出货 status代表是否含税  type代表是否累计
            map.put("status", "Y");
            map.put("type", "");
            cdrhads = cdrhads.add(getCdrhadValue(y, m, map));
            //累计含税出货
            map.put("type", "LJ");
            cdrhadslj = cdrhadslj.add(getCdrhadValue(y, m, map));
            //不含税出货
            map.put("status", "N");
            map.put("type", "");
            cdrhad = cdrhad.add(getCdrhadValue(y, m, map));
            //期间总收款(实收)
            rectotalValue = rectotalValue.add(getRectotalValue(y, m, map));
            //累计出货不含税出货
            map.put("type", "LJ");
            cdrhadlj = cdrhadlj.add(getCdrhadValue(y, m, map));
            //期间总收款(实收)累计
            rectotalValuelj = rectotalValuelj.add(getRectotalValue(y, m, map));
            //期末应收账款
            booamtValue = booamtValue.add(getBooamtValue(y, m, getFirstDayOfMonth(d), map));

        }
        //期间天数
        BigDecimal day = BigDecimal.valueOf(day(y, m));
        BigDecimal aa = booamtValue.multiply(BigDecimal.valueOf(2)).add(rectotalValue).subtract(cdrhads);
        BigDecimal bb = cdrhad.multiply(BigDecimal.valueOf(2));
        if (aa.compareTo(BigDecimal.ZERO) != 0 && bb.compareTo(BigDecimal.ZERO) != 0) {
            value = day.divide((bb.divide(aa, 4, RoundingMode.HALF_UP)), 4, RoundingMode.HALF_UP);
        }
        //累计
        BigDecimal days = BigDecimal.valueOf(days(y, m));
        BigDecimal cc = booamtValue.multiply(BigDecimal.valueOf(2)).add(rectotalValuelj).subtract(cdrhadslj);
        BigDecimal dd = cdrhadlj.multiply(BigDecimal.valueOf(2));
        if (cc.compareTo(BigDecimal.ZERO) != 0 && dd.compareTo(BigDecimal.ZERO) != 0) {
            valuelj = days.divide((dd.divide(cc, 4, RoundingMode.HALF_UP)), 4, RoundingMode.HALF_UP);
        }
        if (!"".equals(id)) {
            Indicator entity = indicatorBean.findById(Integer.valueOf(id));
            if (entity != null) {
                //周转天数累计
                updateIndicatorDaily(m, valuelj, entity.getOther1Indicator());
                //期间总收款(实收)
                updateIndicatorDaily(m, rectotalValue, entity.getOther2Indicator());
                //期末应收账款
                updateIndicatorDaily(m, booamtValue, entity.getOther3Indicator());
                //含税出货
                updateIndicatorDaily(m, cdrhads, entity.getOther4Indicator());
                //不含税出货
                updateIndicatorDaily(m, cdrhad, entity.getOther5Indicator());
                //期间总收款(实收)累计
                updateIndicatorDaily(m, rectotalValuelj, entity.getOther6Indicator());
            }
        }
        return value;
    }

    public void updateIndicatorDaily(int m, BigDecimal value, IndicatorDetail detail) {
        if (detail != null) {
            updateValue(m, value, detail);
            indicatorDailyBean.update(detail);
        }
    }

    //出货
    public BigDecimal getCdrhadValue(int y, int m, LinkedHashMap<String, Object> map) {
        String facno = map.get("facno") != null ? map.get("facno").toString() : "";
        String n_code_DA = map.get("n_code_DA") != null ? map.get("n_code_DA").toString() : "";
        String n_code_CD = map.get("n_code_CD") != null ? map.get("n_code_CD").toString() : "";
        String issevdta = map.get("issevdta") != null ? map.get("issevdta").toString() : "";
        //判断是否含税
        String status = map.get("status") != null ? map.get("status").toString() : "";
        //判断是否累计
        String type = map.get("type") != null ? map.get("type").toString() : "";

        BigDecimal shp1 = BigDecimal.ZERO;
        BigDecimal bshp1 = BigDecimal.ZERO;

        StringBuilder sb = new StringBuilder();
        if ("Y".equals(status)) {
            sb.append("SELECT isnull(convert(decimal(16,4),sum(d.shpamts * h.ratio)),0) ");
        } else {
            sb.append("SELECT isnull(convert(decimal(16,4),sum((d.shpamts * h.ratio)/(h.taxrate + 1))),0) ");
        }
        sb.append(" from cdrhad h,cdrdta d where h.facno=d.facno and h.shpno=d.shpno and h.houtsta<>'W' ");
        sb.append(" and h.cusno NOT IN ('SSD00107','SGD00088','SJS00254','SCQ00146','KZJ00029') ");
        sb.append(" and h.facno='${facno}' ");
        if ("WX".equals(n_code_CD)) {
            sb.append(" and d.shpno like 'HC%' ");
        } else {
            sb.append(" and d.shpno not like 'HC%' ");
            if (!"".equals(n_code_CD)) {
                sb.append(" and d.n_code_CD ").append(n_code_CD);
            }
        }
        if (!"".equals(n_code_DA)) {
            sb.append(" and d.n_code_DA ").append(n_code_DA);
        }
        if (!"".equals(issevdta)) {
            sb.append(" and d.issevdta ").append(issevdta);
        }
        if (!"LJ".equals(type)) {
            sb.append(" and year(h.shpdate) = ${y} and month(h.shpdate)= ${m} ");
        } else {
            sb.append(" and year(h.shpdate) = ${y} and month(h.shpdate) BETWEEN 1 AND ${m} ");
        }
        String cdrdta = sb.toString().replace("${y}", String.valueOf(y)).replace("${m}", String.valueOf(m)).replace("${facno}", facno);

        sb.setLength(0);
        if ("Y".equals(status)) {
            sb.append("SELECT isnull(convert(decimal(16,4),sum(d.bakamts * h.ratio)),0) ");
        } else {
            sb.append("select isnull(convert(decimal(16,4),sum((d.bakamts * h.ratio)/(h.taxrate + 1))),0) ");
        }
        sb.append(" from cdrbhad h,cdrbdta d where h.facno=d.facno and h.bakno=d.bakno and h.baksta<>'W' ");
        sb.append(" and h.cusno NOT IN ('SSD00107','SGD00088','SJS00254','SCQ00146','KZJ00029') ");
        sb.append(" and h.facno='${facno}' ");
        if ("WX".equals(n_code_CD)) {
            sb.append(" and d.shpno like 'HC%' ");
        } else {
            sb.append(" and d.shpno not like 'HC%' ");
            if (!"".equals(n_code_CD)) {
                sb.append(" and d.n_code_CD ").append(n_code_CD);
            }
        }
        if (!"".equals(n_code_DA)) {
            sb.append(" and d.n_code_DA ").append(n_code_DA);
        }
        if (!"".equals(issevdta)) {
            sb.append(" and d.issevdta ").append(issevdta);
        }
        if (!"LJ".equals(type)) {
            sb.append(" and year(h.bakdate) = ${y} and month(h.bakdate)= ${m} ");
        } else {
            sb.append(" and year(h.bakdate) = ${y} and month(h.bakdate) BETWEEN 1 AND ${m} ");
        }
        String cdrbdta = sb.toString().replace("${y}", String.valueOf(y)).replace("${m}", String.valueOf(m)).replace("${facno}", facno);

        superEJB.setCompany(facno);
        Query query1 = superEJB.getEntityManager().createNativeQuery(cdrdta);
        Query query2 = superEJB.getEntityManager().createNativeQuery(cdrbdta);
        try {
            Object o1 = query1.getSingleResult();
            Object o2 = query2.getSingleResult();
            shp1 = (BigDecimal) o1;
            bshp1 = (BigDecimal) o2;
        } catch (Exception ex) {
            java.util.logging.Logger.getLogger(Shipment.class.getName()).log(Level.SEVERE, null, ex);
        }
        if (queryParams != null && !queryParams.isEmpty()) {
            //计算其他金额
            this.arm232 = this.getARM232Value(y, m, getQueryParams());
            //ARM235不算事业部
            this.arm235 = this.getARM235Value(y, m, getQueryParams());
            this.arm270 = this.getARM270Value(y, m, getQueryParams());
            this.arm423 = this.getARM423Value(y, m, getQueryParams());
        }
        return shp1.subtract(bshp1).add(arm232).add(arm235).add(arm270).add(arm423);
    }

    //加扣款,关联4个分类
    public BigDecimal getARM232Value(int y, int m, LinkedHashMap<String, Object> map) {
        String facno = map.get("facno") != null ? map.get("facno").toString() : "";
        String n_code_DA = map.get("n_code_DA") != null ? map.get("n_code_DA").toString() : "";
        String n_code_CD = map.get("n_code_CD") != null ? map.get("n_code_CD").toString() : "";
        String issevdta = map.get("issevdta") != null ? map.get("issevdta").toString() : "";
        //判断是否含税
        String status = map.get("status") != null ? map.get("status").toString() : "";
        //判断是否累计
        String type = map.get("type") != null ? map.get("type").toString() : "";

        StringBuilder sb = new StringBuilder();
        if ("Y".equals(status)) {
            sb.append(" SELECT ISNULL(SUM(CASE armpmm.amtco WHEN 'P' THEN armacq.psamt WHEN 'M' THEN armacq.psamt *(-1) *(cdrhad.taxrate + 1) ELSE 0 END),0)  ");
        } else {
            sb.append("SELECT ISNULL(SUM(CASE armpmm.amtco WHEN 'P' THEN armacq.psamt WHEN 'M' THEN armacq.psamt *(-1) ELSE 0 END),0) ");
        }
        sb.append(" FROM armpmm,armacq,cdrdta,cdrhad ");
        sb.append(" WHERE armpmm.facno = armacq.facno AND armpmm.trno = armacq.trno AND armacq.facno = cdrdta.facno AND  armacq.shpno = cdrdta.shpno ");
        sb.append(" AND armacq.shpseq = cdrdta.trseq AND cdrdta.facno = cdrhad.facno AND cdrdta.shpno = cdrhad.shpno  ");
        sb.append(" AND armpmm.facno = '${facno}'  ");
        if ("WX".equals(n_code_CD)) {
            sb.append(" and cdrdta.shpno like 'HC%' ");
        } else {
            sb.append(" and cdrdta.shpno not like 'HC%' ");
            if (!"".equals(n_code_CD)) {
                sb.append(" and cdrdta.n_code_CD ").append(n_code_CD);
            }
        }
        if (!"".equals(n_code_DA)) {
            sb.append(" AND cdrdta.n_code_DA ").append(n_code_DA);
        }
        if (!"".equals(issevdta)) {
            sb.append(" AND cdrdta.issevdta ").append(issevdta);
        }
        if (!"LJ".equals(type)) {
            sb.append(" AND year(armpmm.trdat) = ${y} AND month(armpmm.trdat)= ${m} ");
        } else {
            sb.append(" AND year(armpmm.trdat) = ${y} AND month(armpmm.trdat) BETWEEN 1 AND ${m} ");
        }
        String sqlstr = sb.toString().replace("${y}", String.valueOf(y)).replace("${m}", String.valueOf(m)).replace("${facno}", facno);
        superEJB.setCompany(facno);
        Query query = superEJB.getEntityManager().createNativeQuery(sqlstr);
        try {
            Object o = query.getSingleResult();
            return (BigDecimal) o;
        } catch (Exception ex) {
            log4j.error(ex);
        }
        return BigDecimal.ZERO;
    }

    //代收其他款项,关联4个分类
    public BigDecimal getARM235Value(int y, int m, LinkedHashMap<String, Object> map) {
        String facno = map.get("facno") != null ? map.get("facno").toString() : "";
        String n_code_DA = map.get("n_code_DA") != null ? map.get("n_code_DA").toString() : "";
        String n_code_CD = map.get("n_code_CD") != null ? map.get("n_code_CD").toString() : "";
        String issevdta = map.get("issevdta") != null ? map.get("issevdta").toString() : "";
        //判断是否含税
        String status = map.get("status") != null ? map.get("status").toString() : "";
        //判断是否累计
        String type = map.get("type") != null ? map.get("type").toString() : "";

        StringBuilder sb = new StringBuilder();
        if ("Y".equals(status)) {
            sb.append(" SELECT ISNULL(sum(apmamt *(cdrhad.taxrate + 1) ),0) ");
        } else {
            sb.append("SELECT ISNULL(sum(apmamt),0) ");
        }
        sb.append("  FROM armicdh,cdrhad,cdrdta  where armicdh.facno = cdrhad.facno AND armicdh.shpno = cdrhad.shpno  ");
        sb.append("  AND cdrhad.facno = cdrdta.facno AND cdrhad.shpno = cdrdta.shpno AND armicdh.apmamt <> 0  ");
        sb.append("  AND cdrhad.houtsta <> 'W' AND cdrhad.facno='${facno}' AND cdrdta.trseq = 1 ");
        if ("WX".equals(n_code_CD)) {
            sb.append(" and cdrhad.shpno like 'HC%' ");
        } else {
            sb.append(" and cdrhad.shpno not like 'HC%' ");
            if (!"".equals(n_code_CD)) {
                sb.append(" and cdrdta.n_code_CD ").append(n_code_CD);
            }
        }
        if (!"".equals(n_code_DA)) {
            sb.append(" and cdrdta.n_code_DA ").append(n_code_DA);
        }
        if (!"".equals(issevdta)) {
            sb.append(" and cdrdta.issevdta ").append(issevdta);
        }
        if (!"LJ".equals(type)) {
            sb.append(" AND year(cdrhad.shpdate) = ${y}  AND month(cdrhad.shpdate)= ${m} ");
        } else {
            sb.append(" AND year(cdrhad.shpdate) = ${y}  AND month(cdrhad.shpdate) BETWEEN 1 AND ${m} ");
        }
        String sqlstr = sb.toString().replace("${y}", String.valueOf(y)).replace("${m}", String.valueOf(m)).replace("${facno}", facno);
        superEJB.setCompany(facno);
        Query query = superEJB.getEntityManager().createNativeQuery(sqlstr);
        try {
            Object o = query.getSingleResult();
            if (o != null) {
                return (BigDecimal) o;
            }
        } catch (Exception ex) {
            log4j.error(ex);
        }
        return BigDecimal.ZERO;
    }

    //它项金额,关联部门
    public BigDecimal getARM270Value(int y, int m, LinkedHashMap<String, Object> map) {
        String facno = map.get("facno") != null ? map.get("facno").toString() : "";
        String n_code_DA = map.get("n_code_DA") != null ? map.get("n_code_DA").toString() : "";
        String n_code_CD = map.get("n_code_CD") != null ? map.get("n_code_CD").toString() : "";
        String issevdta = map.get("issevdta") != null ? map.get("issevdta").toString() : "";
        //判断是否含税
        String status = map.get("status") != null ? map.get("status").toString() : "";
        //判断是否累计
        String type = map.get("type") != null ? map.get("type").toString() : "";
        StringBuilder sb = new StringBuilder();

        if ("Y".equals(status)) {
            sb.append(" SELECT ISNULL(SUM(totamts),0) from ( ");
        } else {
            sb.append("SELECT ISNULL(SUM(totamt),0) from ( ");
        }
        sb.append(" SELECT (CASE WHEN armbil.address4 is null THEN '' ELSE armbil.address4 END ) as n_code_DA, ");
        sb.append(" (CASE WHEN armbil.address5 is null THEN '' ELSE armbil.address5 END ) as issevdta, ");
        sb.append(" (CASE WHEN armbil.address3 is null THEN '' ELSE armbil.address3 END ) as n_code_CD, ");
        sb.append(" ISNULL((armbil.shpamt * (armbil.taxrate + 1)),0) as totamts, ");
        sb.append(" ISNULL((armbil.shpamt),0) as totamt FROM armbil WHERE armbil.rkd IN ('RQ11','RQ12') ");
        sb.append(" AND armbil.facno = '${facno}' ");
        if (!"LJ".equals(type)) {
            sb.append("  AND year(armbil.bildat) = ${y} AND month(armbil.bildat)= ${m} ");
        } else {
            sb.append("  AND year(armbil.bildat) = ${y} AND month(armbil.bildat) BETWEEN 1 AND ${m} ");
        }
        sb.append(" ) a where 1=1 ");
        if ("WX".equals(n_code_CD)) {
            sb.append(" and n_code_DA = '' and issevdta = '' and n_code_CD = '' ");
        } else {
            if (!"".equals(n_code_CD)) {
                sb.append(" and n_code_CD ").append(n_code_CD);
            }
        }
        if (!"".equals(n_code_DA)) {
            sb.append(" and n_code_DA ").append(n_code_DA);
        }
        if (!"".equals(issevdta)) {
            sb.append(" and issevdta ").append(issevdta);
        }

        String sqlstr = sb.toString().replace("${y}", String.valueOf(y)).replace("${m}", String.valueOf(m)).replace("${facno}", facno);
        superEJB.setCompany(facno);
        Query query = superEJB.getEntityManager().createNativeQuery(sqlstr);
        try {
            Object o = query.getSingleResult();
            return (BigDecimal) o;
        } catch (Exception ex) {
            log4j.error(ex);
        }
        return BigDecimal.ZERO;
    }

    //折让,关联4个分类
    public BigDecimal getARM423Value(int y, int m, LinkedHashMap<String, Object> map) {
        String facno = map.get("facno") != null ? map.get("facno").toString() : "";
        String n_code_DA = map.get("n_code_DA") != null ? map.get("n_code_DA").toString() : "";
        String n_code_CD = map.get("n_code_CD") != null ? map.get("n_code_CD").toString() : "";
        String issevdta = map.get("issevdta") != null ? map.get("issevdta").toString() : "";
        //判断是否含税
        String status = map.get("status") != null ? map.get("status").toString() : "";
        //判断是否累计
        String type = map.get("type") != null ? map.get("type").toString() : "";

        StringBuilder sb = new StringBuilder();

        if ("Y".equals(status)) {
            sb.append(" SELECT ISNULL(SUM(totamts),0) from ( ");
        } else {
            sb.append("SELECT ISNULL(SUM(totamt),0) from ( ");
        }
        sb.append(" SELECT armrech.n_code_DA,(CASE WHEN armrech.n_code_DD <> '01' THEN 'N' ELSE 'Y' END)  as issevdta,armrech.n_code_CD,ISNULL((armrec.recamt),0) as totamts, ");
        sb.append(" (CASE WHEN  armrec.raccno in ('6001','6002') THEN ISNULL((armrec.recamt),0) ELSE 0 END)as totamt  ");
        sb.append(" FROM armrec, armrech where armrec.facno=armrech.facno AND armrec.recno=armrech.recno AND armrech.prgno='ARM423'   ");
        sb.append(" AND armrech.recstat='1' AND armrec.raccno in ('6001','6002','2226') AND armrech.facno='${facno}' ");
        if (!"LJ".equals(type)) {
            sb.append(" AND year(armrech.recdate) = ${y} and month(armrech.recdate)= ${m} ");
        } else {
            sb.append(" AND year(armrech.recdate) = ${y} and month(armrech.recdate) BETWEEN 1 AND ${m} ");
        }
        sb.append(" ) a where 1=1 ");
        if ("WX".equals(n_code_CD)) {
            sb.append(" AND n_code_DA = '' AND issevdta = '' AND n_code_CD = '' ");
        } else {
            if (!"".equals(n_code_CD)) {
                sb.append(" and n_code_CD ").append(n_code_CD);
            }
        }
        if (!"".equals(n_code_DA)) {
            sb.append(" and n_code_DA ").append(n_code_DA);
        }
        if (!"".equals(issevdta)) {
            sb.append(" and issevdta ").append(issevdta);
        }
        String sqlstr = sb.toString().replace("${y}", String.valueOf(y)).replace("${m}", String.valueOf(m)).replace("${facno}", facno);
        superEJB.setCompany(facno);
        Query query = superEJB.getEntityManager().createNativeQuery(sqlstr);
        try {
            Object o = query.getSingleResult();
            return (BigDecimal) o;
        } catch (Exception ex) {
            log4j.error(ex);
        }
        return BigDecimal.ZERO;
    }

    //期末应收账款
    public BigDecimal getBooamtValue(int y, int m, Date d, LinkedHashMap<String, Object> map) {
        String facno = map.get("facno") != null ? map.get("facno").toString() : "";
        String n_code_DA = map.get("n_code_DA") != null ? map.get("n_code_DA").toString() : "";
        String n_code_CD = map.get("n_code_CD") != null ? map.get("n_code_CD").toString() : "";
        String issevdta = map.get("issevdta") != null ? map.get("issevdta").toString() : "";

        BigDecimal booamt = BigDecimal.ZERO;

        StringBuilder sb = new StringBuilder();
        sb.append(" SELECT ISNULL(sum(booamt),0) FROM ( ");
        sb.append(" SELECT (CASE WHEN armbil.address4 is null THEN '' ELSE armbil.address4 END ) as n_code_DA , ");
        sb.append(" (CASE WHEN armbil.address5 is null THEN '' ELSE armbil.address5 END ) as issevdta, ");
        sb.append(" (CASE WHEN armbil.address3 is null THEN '' ELSE armbil.address3 END ) as n_code_CD,(booamt - recamt) as booamt ");
        sb.append(" FROM armhad ,armbil where armhad.facno=armbil.facno AND armhad.hadno=armbil.bilno ");
        sb.append(" AND ((armhad.booamt - armhad.recamt)<> 0) AND armhad.facno = '${facno}' ");
        sb.append(" AND (armhad.accno='1122') AND (armhad.difcod in ('1','3')) AND armhad.cusno NOT IN ('SSD00107','SGD00088','SJS00254','SCQ00146','KZJ00029') ");
        sb.append(" and armhad.bildat< '${d}' ");
        sb.append(" union all ");
        sb.append(" SELECT (CASE WHEN armhad.n_code_DA is null THEN '' ELSE armhad.n_code_DA END ) as n_code_DA, ");
        sb.append(" (CASE WHEN armhad.isserve is null THEN '' ELSE armhad.isserve END ) as issevdta,(armhad.booamt - armhad.recamt) as booamt ");
        sb.append(" FROM armhad where ((armhad.booamt - armhad.recamt)<> 0) AND armhad.facno = '${facno}' ");
        sb.append(" AND (armhad.accno='1122') AND (armhad.difcod ='6') AND armhad.cusno NOT IN ('SSD00107','SGD00088','SJS00254','SCQ00146','KZJ00029') ");
        sb.append(" and armhad.bildat< '${d}' ");
        sb.append(" union all ");
        sb.append(" SELECT  '' as n_code_DA , '' as issevdta, (armhad.booamt - armhad.recamt) as booamt ");
        sb.append(" FROM armhad where ((armhad.booamt - armhad.recamt)<> 0)  AND armhad.facno = '${facno}'  AND (armhad.accno='1122') ");
        sb.append(" AND (armhad.difcod not in ('1','3','6')) AND armhad.cusno NOT IN ('SSD00107','SGD00088','SJS00254','SCQ00146','KZJ00029') ");
        sb.append(" and armhad.bildat< '${d}' ");
        sb.append(" ) a where 1=1 ");
        if ("WX".equals(n_code_CD)) {
            sb.append(" and a.n_code_DA = '' and a.issevdta = '' and a.n_code_CD = '' ");
        } else {
            if (!"".equals(n_code_CD)) {
                sb.append(" and a.n_code_CD ").append(n_code_DA);
            }
        }
        if (!"".equals(n_code_DA)) {
            sb.append(" and a.n_code_DA ").append(n_code_DA);
        }
        if (!"".equals(issevdta)) {
            sb.append(" and a.issevdta ").append(issevdta);
        }
        String sql = sb.toString().replace("${y}", String.valueOf(y)).replace("${m}", String.valueOf(m)).replace("${facno}", facno).replace("${d}", BaseLib.formatDate("yyyyMMdd", d));

        superEJB.setCompany(facno);
        Query query = superEJB.getEntityManager().createNativeQuery(sql);
        try {
            Object o = query.getSingleResult();
            booamt = (BigDecimal) o;
        } catch (Exception ex) {
            java.util.logging.Logger.getLogger(Shipment.class.getName()).log(Level.SEVERE, null, ex);
        }
        return booamt;
    }

    //实收
    public BigDecimal getRectotalValue(int y, int m, LinkedHashMap<String, Object> map) {
        String facno = map.get("facno") != null ? map.get("facno").toString() : "";
        String n_code_DA = map.get("n_code_DA") != null ? map.get("n_code_DA").toString() : "";
        String n_code_CD = map.get("n_code_CD") != null ? map.get("n_code_CD").toString() : "";
        String issevdta = map.get("issevdta") != null ? map.get("issevdta").toString() : "";

        //判断是否累计
        String type = map.get("type") != null ? map.get("type").toString() : "";
        BigDecimal rectotal = BigDecimal.ZERO;

        StringBuilder sb = new StringBuilder();
        sb.append(" SELECT ISNULL(sum(b.recamt),0)  FROM ( ");
        sb.append(" SELECT  (CASE WHEN armbil.address4 is null THEN '' ELSE armbil.address4 END ) as n_code_DA , ");
        sb.append(" (CASE WHEN armbil.address5 is null THEN '' ELSE armbil.address5 END ) as issevdta,");
        sb.append(" (CASE WHEN armbil.address3 is null THEN '' ELSE armbil.address3 END ) as n_code_CD,a.tnfamt as recamt ");
        sb.append(" from ( SELECT armtnf.hadno, armtnf.cusno, sum(armtnf.tnfamt) as tnfamt FROM armtnf,armrec,armrech ");
        sb.append(" where ( armrech.facno = armtnf.facno ) and ( armrech.vouno = armtnf.vouno ) and ( armrech.facno = armrec.facno ) AND ( armtnf.accno = armrec.raccno )");
        sb.append(" and ( armrech.recno = armrec.recno ) and armrec.raccno='1122' and armrec.rectype = '2' and armtnf.hadno not like 'HC%' and armrec.recno in  ");
        sb.append(" ( SELECT armrec.recno  FROM armrec,armrech  where ( armrech.facno = armrec.facno ) and ( armrech.recno = armrec.recno ) and  ( armrec.facno = '${facno}' )  ");
        sb.append(" and  armrec.rectype = '1' and armrec.ivocus NOT IN ('SSD00107','SGD00088','SJS00254','SCQ00146','KZJ00029') and   ( armrec.raccno in ('1121','6806') or left(armrec.raccno,2)='10')");
        if (!"LJ".equals(type)) {
            sb.append(" and year(armrech.recdate) = ${y} and month(armrech.recdate)= ${m} ");
        } else {
            sb.append(" and year(armrech.recdate) = ${y} and month(armrech.recdate) BETWEEN 1 AND ${m} ");
        }
        sb.append(" GROUP BY armrec.recno) GROUP BY  armtnf.hadno,armtnf.cusno ) a LEFT JOIN armbil on a.hadno= armbil.bilno ");
        sb.append(" union all ");
        sb.append(" SELECT  '' as n_code_DA , '' as issevdta, '' as n_code_CD, a.tnfamt as recamt");
        sb.append(" from ( SELECT armtnf.hadno, armtnf.cusno, sum(armtnf.tnfamt) as tnfamt FROM armtnf,armrec,armrech ");
        sb.append(" where ( armrech.facno = armtnf.facno ) and ( armrech.vouno = armtnf.vouno ) and ( armrech.facno = armrec.facno ) ");
        sb.append(" and ( armrech.recno = armrec.recno ) and armrec.raccno='1122' and armrec.rectype = '2' and armtnf.hadno like 'HC%' and armrec.recno in  ");
        sb.append(" ( SELECT armrec.recno  FROM armrec,armrech  where ( armrech.facno = armrec.facno ) and ( armrech.recno = armrec.recno ) and  ( armrec.facno = '${facno}' )  ");
        sb.append(" and  armrec.rectype = '1' and armrec.ivocus NOT IN ('SSD00107','SGD00088','SJS00254','SCQ00146','KZJ00029') and   ( armrec.raccno in ('1121','6806') or left(armrec.raccno,2)='10')");
        if (!"LJ".equals(type)) {
            sb.append(" and year(armrech.recdate) = ${y} and month(armrech.recdate)= ${m} ");
        } else {
            sb.append(" and year(armrech.recdate) = ${y} and month(armrech.recdate) BETWEEN 1 AND ${m} ");
        }
        sb.append(" GROUP BY armrec.recno) GROUP BY  armtnf.hadno,armtnf.cusno ) a LEFT JOIN armbil on a.hadno= armbil.bilno ");
        sb.append(" union all ");
        sb.append(" SELECT  ( CASE WHEN armhad.n_code_DA is null THEN '' ELSE armhad.n_code_DA END ) as n_code_DA, ");
        sb.append(" ( CASE WHEN armhad.isserve is null THEN '' ELSE armhad.isserve END ) as issevdta,");
        sb.append(" ( CASE WHEN armhad.n_code_CD is null THEN '' ELSE armhad.n_code_CD END ) as n_code_CD,(armrec.recamt) as recamt ");
        sb.append(" FROM armhad,armrec where armhad.facno=armrec.facno and armhad.hadno=armrec.recno and armrec.raccno='2203' AND armrec.rectype = '2' and armrec.recno in ");
        sb.append(" ( SELECT armrec.recno  FROM armrec,armrech  where ( armrech.facno = armrec.facno ) and ( armrech.recno = armrec.recno ) and  ( armrec.facno = '${facno}' )  ");
        sb.append(" and  armrec.rectype = '1' and armrec.ivocus NOT IN ('SSD00107','SGD00088','SJS00254','SCQ00146','KZJ00029') and   ( armrec.raccno in ('1121','6806') or left(armrec.raccno,2)='10')");
        if (!"LJ".equals(type)) {
            sb.append(" and year(armrech.recdate) = ${y} and month(armrech.recdate)= ${m} ");
        } else {
            sb.append(" and year(armrech.recdate) = ${y} and month(armrech.recdate) BETWEEN 1 AND ${m} ");
        }
        sb.append(" GROUP BY armrec.recno ) ");
        sb.append(" union all ");
        sb.append(" SELECT   '' as n_code_DA , '' as issevdta,'' as n_code_CD, (case when armrec.rectype = '2' then  armrec.recamt ELSE  -armrec.recamt end) as recamt ");
        sb.append(" FROM armrec where armrec.raccno='6805' and armrec.recno in  ");
        sb.append(" ( SELECT armrec.recno  FROM armrec,armrech  where ( armrech.facno = armrec.facno ) and ( armrech.recno = armrec.recno ) and  ( armrec.facno = '${facno}' )  ");
        sb.append(" and  armrec.rectype = '1' and armrec.ivocus NOT IN ('SSD00107','SGD00088','SJS00254','SCQ00146','KZJ00029') and   ( armrec.raccno in ('1121','6806') or left(armrec.raccno,2)='10')");
        if (!"LJ".equals(type)) {
            sb.append(" and year(armrech.recdate) = ${y} and month(armrech.recdate)= ${m} ");
        } else {
            sb.append(" and year(armrech.recdate) = ${y} and month(armrech.recdate) BETWEEN 1 AND ${m} ");
        }
        sb.append(" GROUP BY armrec.recno ) ");
        sb.append(" ) b where 1=1 ");
        if ("WX".equals(n_code_CD)) {
            sb.append(" and b.n_code_DA = '' and b.issevdta = '' and b.n_code_CD = '' ");
        } else {
            if (!"".equals(n_code_CD)) {
                sb.append(" and b.n_code_CD ").append(n_code_CD);
            }
        }
        if (!"".equals(n_code_DA)) {
            sb.append(" and b.n_code_DA ").append(n_code_DA);
        }
        if (!"".equals(issevdta)) {
            sb.append(" and b.issevdta ").append(issevdta);
        }

        String sql = sb.toString().replace("${y}", String.valueOf(y)).replace("${m}", String.valueOf(m)).replace("${facno}", facno);

        superEJB.setCompany(facno);
        Query query = superEJB.getEntityManager().createNativeQuery(sql);
        try {
            Object o = query.getSingleResult();
            rectotal = (BigDecimal) o;
        } catch (Exception ex) {
            java.util.logging.Logger.getLogger(Shipment.class.getName()).log(Level.SEVERE, null, ex);
        }
        return rectotal;
    }

    public int day(int year, int month) {
        int day = 0;
        if (month != 2) {
            switch (month) {
                case 1:
                case 3:
                case 5:
                case 7:
                case 8:
                case 10:
                case 12:
                    day = 31;
                    break;
                case 4:
                case 6:
                case 9:
                case 11:
                    day = 30;
            }
        } else {
            // 闰年
            if (year % 4 == 0 && year % 100 != 0 || year % 400 == 0) {
                day = 29;
            } else {
                day = 28;
            }
        }
        return day;
    }

    public int days(int year, int month) {
        int days = 0;
        for (int i = 1; i <= month; i++) {
            days = days + day(year, i);
        }
        return days;
    }

    @Override
    public LinkedHashMap<String, Object> getQueryParams() {
        return queryParams;
    }

    @Override
    public int getUpdateMonth(int y, int m) {
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
        int year;
        if (m == 1) {
            year = y - 1;
        } else {
            year = y;
        }
        return year;
    }

    public IndicatorBean lookupIndicatorBeanBean() {
        try {
            Context c = new InitialContext();
            return (IndicatorBean) c.lookup("java:global/KPI/KPI-ejb/IndicatorBean!cn.hanbell.kpi.ejb.IndicatorBean");
        } catch (NamingException ne) {
            java.util.logging.Logger.getLogger(getClass().getName()).log(Level.SEVERE, "exception caught", ne);
            throw new RuntimeException(ne);
        }
    }

    public IndicatorDetailBean lookupIndicatorDetailBeanBean() {
        try {
            Context c = new InitialContext();
            return (IndicatorDetailBean) c.lookup("java:global/KPI/KPI-ejb/IndicatorDetailBean!cn.hanbell.kpi.ejb.IndicatorDetailBean");
        } catch (NamingException ne) {
            java.util.logging.Logger.getLogger(getClass().getName()).log(Level.SEVERE, "exception caught", ne);
            throw new RuntimeException(ne);
        }
    }

    public void updateValue(int m, BigDecimal na, IndicatorDetail detail) {
        String col = "setN" + String.format("%02d", m);
        try {
            Method setMethod = detail.getClass().getDeclaredMethod(col, BigDecimal.class);
            setMethod.invoke(detail, na);
        } catch (Exception ex) {
            java.util.logging.Logger.getLogger(ProductionPlanOrder.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
