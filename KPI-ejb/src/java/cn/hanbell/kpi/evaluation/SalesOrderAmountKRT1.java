/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.hanbell.kpi.evaluation;

import cn.hanbell.kpi.comm.Actual;
import com.lightshell.comm.BaseLib;
import java.math.BigDecimal;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.Query;

/**
 *
 * @author C1879 
 * 2020年2月13日RT新统计逻辑需要删除上海柯茂销售给上海汉钟RT部分排除 厂商KSH00004
 */
public class SalesOrderAmountKRT1 extends SalesOrderAmount {

    protected Actual actualInterface;

    public SalesOrderAmountKRT1() {
        super();
        queryParams.put("facno", "K");
        queryParams.put("deptno", " '5C000' ");
        queryParams.put("ogdkid", "RL01");
        queryParams.put("n_code_DA", " ='RT' ");
        queryParams.put("n_code_DC", " ='RT' ");
        queryParams.put("n_code_DD", " IN('00','02') ");
    }

    @Override
    public BigDecimal getValue(int y, int m, Date d, int type, LinkedHashMap<String, Object> map) {
        //获得查询参数
        String facno = map.get("facno") != null ? map.get("facno").toString() : "";
        String decode = map.get("decode") != null ? map.get("decode").toString() : "";
        String deptno = map.get("deptno") != null ? map.get("deptno").toString() : "";
        String n_code_DA = map.get("n_code_DA") != null ? map.get("n_code_DA").toString() : "";
        String n_code_CD = map.get("n_code_CD") != null ? map.get("n_code_CD").toString() : "";
        String n_code_DC = map.get("n_code_DC") != null ? map.get("n_code_DC").toString() : "";
        String n_code_DD = map.get("n_code_DD") != null ? map.get("n_code_DD").toString() : "";

        BigDecimal tram = BigDecimal.ZERO;

        StringBuilder sb = new StringBuilder();
        sb.append(" SELECT  isnull(convert(decimal(16,2),sum((d.tramts*h.ratio)/(h.taxrate+1))),0) from cdrdmas d inner join cdrhmas h on h.facno=d.facno and h.cdrno=d.cdrno");
        sb.append(" WHERE  isnull(h.hmark2,'') <> 'FW' AND h.hrecsta <> 'W' AND h.cusno not in ('SSD00107','SGD00088','SJS00254','SCQ00146','KSH00004') ");
        sb.append(" AND  h.facno='${facno}' ");
        sb.append(" and d.drecsta not in ('98','99','10') ");
        if (!"".equals(decode)) {
            sb.append(" and h.decode ='").append(decode).append("' ");
        }
        if (!"".equals(n_code_DA)) {
            sb.append(" and d.n_code_DA ").append(n_code_DA);
        }
        if (!"".equals(n_code_CD)) {
            sb.append(" and d.n_code_CD ").append(n_code_CD);
        }
        if (!"".equals(n_code_DC)) {
            sb.append(" and d.n_code_DC ").append(n_code_DC);
        }
        if (!"".equals(n_code_DD)) {
            sb.append(" and d.n_code_DD ").append(n_code_DD);
        }
        sb.append(" and year(h.recdate) = ${y} and month(h.recdate)= ${m} ");
        switch (type) {
            case 2:
                //月
                sb.append(" and h.recdate<= '${d}' ");
                break;
            case 5:
                //日
                sb.append(" and h.recdate= '${d}' ");
                break;
            default:
                sb.append(" and h.recdate<= '${d}' ");
        }

        String cdrdmas = sb.toString().replace("${y}", String.valueOf(y)).replace("${m}", String.valueOf(m)).replace("${d}", BaseLib.formatDate("yyyyMMdd", d))
                .replace("${facno}", facno);

        superEJB.setCompany(facno);
        Query query = superEJB.getEntityManager().createNativeQuery(cdrdmas);
        try {
            Object o1 = query.getSingleResult();
            tram = (BigDecimal) o1;
        } catch (Exception ex) {
            Logger.getLogger(Shipment.class.getName()).log(Level.SEVERE, null, ex);
        }
        BigDecimal rt = BigDecimal.ZERO;
        try {
            //各分公司数据
            String ejb="java:global/KPI/KPI-ejb/SuperEJBForERP!cn.hanbell.kpi.comm.SuperEJBForERP";
            actualInterface = (Actual) Class.forName("cn.hanbell.kpi.evaluation.SalesOrderAmountR1B4").newInstance();
            actualInterface.setEJB(ejb);
            BigDecimal hd = actualInterface.getValue(y, m, d, type, actualInterface.getQueryParams());
            actualInterface = (Actual) Class.forName("cn.hanbell.kpi.evaluation.SalesOrderAmountR1C4").newInstance();
            actualInterface.setEJB(ejb);
            BigDecimal jn = actualInterface.getValue(y, m, d, type, actualInterface.getQueryParams());
            actualInterface = (Actual) Class.forName("cn.hanbell.kpi.evaluation.SalesOrderAmountR1D4").newInstance();
            actualInterface.setEJB(ejb);
            BigDecimal gz = actualInterface.getValue(y, m, d, type, actualInterface.getQueryParams());
            actualInterface = (Actual) Class.forName("cn.hanbell.kpi.evaluation.SalesOrderAmountR1E4").newInstance();
            actualInterface.setEJB(ejb);
            BigDecimal nj = actualInterface.getValue(y, m, d, type, actualInterface.getQueryParams());
            actualInterface = (Actual) Class.forName("cn.hanbell.kpi.evaluation.SalesOrderAmountR1V4").newInstance();
            actualInterface.setEJB(ejb);
            BigDecimal cq = actualInterface.getValue(y, m, d, type, actualInterface.getQueryParams());
            actualInterface = (Actual) Class.forName("cn.hanbell.kpi.evaluation.SalesOrderAmountR1T4").newInstance();
            actualInterface.setEJB(ejb);
            BigDecimal wx = actualInterface.getValue(y, m, d, type, actualInterface.getQueryParams());
            rt.add(hd).add(jn).add(gz).add(cq).add(nj).add(wx);
        } catch (Exception ex) {
            Logger.getLogger(SalesOrderAmountKRT1.class.getName()).log(Level.SEVERE, null, ex);
        }
        return tram.add(rt);
    }
}
