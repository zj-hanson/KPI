/*
 * To change this license header, choose License Headers in Project Properties. To change this template file, choose
 * Tools | Templates and open the template in the editor.
 */

package cn.hanson.kpi.evaluation;

import cn.hanbell.kpi.comm.Actual;
import cn.hanbell.kpi.comm.SuperEJBForMES;
import cn.hanbell.kpi.ejb.IndicatorBean;
import cn.hanbell.kpi.entity.Indicator;
import cn.hanbell.kpi.entity.IndicatorDetail;
import com.lightshell.comm.BaseLib;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.persistence.Query;
import org.apache.logging.log4j.LogManager;

/**
 *
 * @author C0160
 */
public class ScrapAnalysis implements Actual {

    IndicatorBean indicatorBean = lookupIndicatorBean();

    SuperEJBForMES superEJBForMES = lookupSuperEJBForMES();

    protected LinkedHashMap<String, Object> queryParams;

    protected final org.apache.logging.log4j.Logger log4j = LogManager.getLogger("cn.hanbell.kpi");

    public ScrapAnalysis() {
        queryParams = new LinkedHashMap<>();
        queryParams.put("facno", "H");
        queryParams.put("step", " = '造型' ");
    }

    @Override
    public void setEJB(String JNDIName) throws Exception {

    }

    @Override
    public LinkedHashMap<String, Object> getQueryParams() {
        return queryParams;
    }

    private IndicatorBean lookupIndicatorBean() {
        try {
            Context c = new InitialContext();
            return (IndicatorBean)c.lookup("java:global/KPI/KPI-ejb/IndicatorBean!cn.hanbell.kpi.ejb.IndicatorBean");
        } catch (NamingException ne) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "exception caught", ne);
            throw new RuntimeException(ne);
        }
    }

    private SuperEJBForMES lookupSuperEJBForMES() {
        try {
            Context c = new InitialContext();
            return (SuperEJBForMES)c
                .lookup("java:global/KPI/KPI-ejb/SuperEJBForMES!cn.hanbell.kpi.comm.SuperEJBForMES");
        } catch (NamingException ne) {
            throw new RuntimeException(ne);
        }
    }

    @Override
    public int getUpdateMonth(int y, int m) {
        return m;
    }

    @Override
    public int getUpdateYear(int y, int m) {
        return y;
    }

    @Override
    public BigDecimal getValue(int y, int m, Date d, int type, LinkedHashMap<String, Object> map) {
        BigDecimal scrapWeight = BigDecimal.ZERO;
        BigDecimal prodWeight = BigDecimal.ZERO;

        int id = Integer.parseInt(map.get("id").toString());
        Indicator indicator = indicatorBean.findById(id);
        if (indicator == null) {
            return BigDecimal.ZERO;
        }
        try {
            Method setMethod = IndicatorDetail.class
                .getDeclaredMethod("set" + indicatorBean.getIndicatorColumn("N", m).toUpperCase(), BigDecimal.class);
            // o1报废重量
            IndicatorDetail o1 = indicator.getOther1Indicator();
            if (o1 != null) {
                scrapWeight = getScrapWeight(y, m, d, type, map);
                setMethod.invoke(o1, scrapWeight);
                indicatorBean.updateIndicatorDetail(o1);
            }
            // o2生产总量
            IndicatorDetail o2 = indicator.getOther2Indicator();
            if (o2 != null) {
                prodWeight = getProductWeight(y, m, d, type, map);
                setMethod.invoke(o2, prodWeight);
                indicatorBean.updateIndicatorDetail(o2);
            }
            if (prodWeight.compareTo(BigDecimal.ZERO) != 0) {
                return scrapWeight.divide(prodWeight, 4, RoundingMode.HALF_UP).multiply(BigDecimal.valueOf(100d));
            }
            if (scrapWeight.compareTo(BigDecimal.ZERO) != 0) {
                return BigDecimal.valueOf(100d);
            }
        } catch (Exception ex) {
            log4j.error(ex);
        }
        return BigDecimal.ZERO;
    }

    protected BigDecimal getScrapWeight(int y, int m, Date d, int type, LinkedHashMap<String, Object> map) {
        // 公司
        String facno = map.get("facno") != null ? map.get("facno").toString() : "";
        // 新品试模 SMCasting 产品铸造 LCCasting
        String CASTINGTYPE = map.get("CASTINGTYPE") != null ? map.get("CASTINGTYPE").toString() : "";
        // 厂内厂外
        String INOUTFACTORY = map.get("INOUTFACTORY") != null ? map.get("INOUTFACTORY").toString() : "";
        // 铸造加工
        String DEFECTGROUPID = map.get("DEFECTGROUPID") != null ? map.get("DEFECTGROUPID").toString() : "";

        BigDecimal scrapWeight = BigDecimal.ZERO;

        StringBuilder sb = new StringBuilder();
        sb.append("SELECT COALESCE(SUM(convert(FLOAT, UQF.CASTINGWEIGHT) * convert(FLOAT, Q.DEFECTNUM)),0) ");
        sb.append(" FROM FLOW_FORM_UQF_S_NOW  S ");
        sb.append(" INNER JOIN FLOW_PROJECT_HISTORY H ON S.PROJECTID = H.PROJECTID ");
        sb.append(" INNER JOIN FLOW_FORM_UQF_COMP_NOW UQF ON S.PROJECTID = UQF.PROJECTID ");
        sb.append(" INNER JOIN ANALYSISRESULT_QCD Q ON Q.PROJECTID = UQF.PROJECTID ");
        sb.append(" AND Q.PRODUCTSERIALNUMBER = UQF.PRODUCTSERIALNUMBER AND Q.PRODUCTID = UQF.PRODUCTID ");
        sb.append(" LEFT JOIN MPRODUCT M ON M.PRODUCTID = Q.PRODUCTID ");
        sb.append(" WHERE FLOWPROCESSID = 'UQF' AND PROCESSNODEID = 'UQFN0002' AND S.UNIT='${f}' ");
        sb.append(" AND EFFECTIVEFLAG = 'Y' AND ADDFLAG = 'N' AND ANALYSISJUDGEMENTRESULT='就地报废' ");
        sb.append(" AND YEAR(NODESTARTTIME)=${y} AND MONTH(NODESTARTTIME)=${m} ");
        switch (type) {
            case 2:
                // 月
                sb.append(" AND datepart(DAY ,NODESTARTTIME) <= ${d} ");
                break;
            case 5:
                // 日
                sb.append(" AND datepart(DAY ,NODESTARTTIME) = ${d} ");
                break;
            default:
                sb.append(" AND datepart(DAY ,NODESTARTTIME) <= ${d} ");
        }
        if (!"".equals(CASTINGTYPE)) {
            sb.append(" AND CASTINGTYPE ").append(CASTINGTYPE);
        }
        if (!"".equals(INOUTFACTORY)) {
            sb.append(" and INOUTFACTORY ").append(INOUTFACTORY);
        }
        if (!"".equals(DEFECTGROUPID)) {
            sb.append(" and Q.DEFECTGROUPID ").append(DEFECTGROUPID);
        }
        String sql = sb.toString().replace("${y}", String.valueOf(y)).replace("${m}", String.valueOf(m)).replace("${d}",
            BaseLib.formatDate("dd", d)).replace("${f}", facno);

        superEJBForMES.setCompany(facno);
        Query query = superEJBForMES.getEntityManager().createNativeQuery(sql);
        try {
            Object o1 = query.getSingleResult();
            scrapWeight = BigDecimal.valueOf(Double.valueOf(o1.toString()));
            //汉扬的部分不合格单在汉声mes中开。
            if("Y".equals(facno)){
                superEJBForMES.setCompany("H");
                query = superEJBForMES.getEntityManager().createNativeQuery(sql);
                o1 = query.getSingleResult();
                scrapWeight= scrapWeight.add(BigDecimal.valueOf(Double.valueOf(o1.toString())));;
            }
        } catch (Exception ex) {
            log4j.error(ex);
        }
        return scrapWeight;
    }

    protected BigDecimal getProductWeight(int y, int m, Date d, int type, LinkedHashMap<String, Object> map) {
        // 公司
        String facno = map.get("facno") != null ? map.get("facno").toString() : "";
        // 产线
        String line = map.get("line") != null ? map.get("line").toString() : "";
        // 工序
        String step = map.get("step") != null ? map.get("step").toString() : "";

        BigDecimal ton = BigDecimal.ZERO;

        StringBuilder sb = new StringBuilder();
        sb.append(" SELECT ISNULL(SUM(CAST(A.CASTINGWEIGHT AS FLOAT)*CAST(A.TRACKOUTQTY AS FLOAT)),0) ");
        sb.append(" FROM CAST_PROCESS_STEP_P A   WHERE 1=1     ");
        if (!"".equals(line)) {
            sb.append(" AND A.PROCESSLINE ").append(line);
        }
        if (!"".equals(step)) {
            sb.append(" AND A.STEPID ").append(step);
        }
        sb.append(" AND year(A.TRACKOUTTIME) = ${y} AND month(A.TRACKOUTTIME)= ${m} ");
        switch (type) {
            case 2:
                // 月
                sb.append(" AND datepart(DAY ,A.TRACKOUTTIME) <= ${d} ");
                break;
            case 5:
                // 日
                sb.append(" AND datepart(DAY ,A.TRACKOUTTIME) = ${d} ");
                break;
            default:
                sb.append(" AND datepart(DAY ,A.TRACKOUTTIME) = ${d} ");
        }

        String sql = sb.toString().replace("${y}", String.valueOf(y)).replace("${m}", String.valueOf(m)).replace("${d}",
            BaseLib.formatDate("dd", d));

        superEJBForMES.setCompany(facno);
        Query query = superEJBForMES.getEntityManager().createNativeQuery(sql);
        try {
            Object o1 = query.getSingleResult();
            ton = BigDecimal.valueOf(Double.valueOf(o1.toString()));
        } catch (Exception ex) {
            log4j.error(ex);
        }
        return ton;
    }

}
