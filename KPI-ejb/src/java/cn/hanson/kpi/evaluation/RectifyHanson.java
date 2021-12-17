/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.hanson.kpi.evaluation;

import cn.hanbell.kpi.comm.Actual;
import cn.hanbell.kpi.ejb.IndicatorBean;
import cn.hanbell.kpi.entity.Indicator;
import cn.hanbell.kpi.entity.IndicatorDetail;
import cn.hanson.kpi.ejb.ppm.QualityBean;
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
 * @author FredJie
 */
public class RectifyHanson implements Actual {
    IndicatorBean indicatorBean = lookupIndicatorBean();

    QualityBean qualityBean = lookupQualityBean();

    protected LinkedHashMap<String, Object> queryParams;

    protected final org.apache.logging.log4j.Logger log4j = LogManager.getLogger("cn.hanbell.kpi");

    public RectifyHanson() {
        queryParams = new LinkedHashMap<>();
        queryParams.put("facno", "H");
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
            return (IndicatorBean) c.lookup("java:global/KPI/KPI-ejb/IndicatorBean!cn.hanbell.kpi.ejb.IndicatorBean");
        } catch (NamingException ne) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "exception caught", ne);
            throw new RuntimeException(ne);
        }
    }

    private QualityBean lookupQualityBean() {
        try {
            Context c = new InitialContext();
            return (QualityBean) c
                    .lookup("java:global/KPI/KPI-ejb/QualityBean!cn.hanson.kpi.ejb.ppm.QualityBean");
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
        //上月结案个数
        BigDecimal finishNum = BigDecimal.ZERO;
        //上月结案个数+所有结案个数
        BigDecimal allNum = BigDecimal.ZERO;

        int id = Integer.parseInt(map.get("id").toString());
        Indicator indicator = indicatorBean.findById(id);
        if (indicator == null) {
            return BigDecimal.ZERO;
        }
        try {
            Method setMethod = IndicatorDetail.class
                    .getDeclaredMethod("set" + indicatorBean.getIndicatorColumn("N", m).toUpperCase(), BigDecimal.class);
            //  o1上月结案个数
            IndicatorDetail o1 = indicator.getOther1Indicator();
            if (o1 != null) {
                finishNum = qualityBean.getFinishNum(y, m, d, type, map);
                setMethod.invoke(o1, finishNum);
                indicatorBean.updateIndicatorDetail(o1);
            }
            // o2上月结案个数+所有结案个数
            IndicatorDetail o2 = indicator.getOther2Indicator();
            if (o2 != null) {
                allNum = qualityBean.getAllNum(map);
                allNum=allNum.add(finishNum);
                setMethod.invoke(o2, allNum);
                indicatorBean.updateIndicatorDetail(o2);
            }
            if (allNum.compareTo(BigDecimal.ZERO) != 0) {
                return finishNum.divide(allNum, 4, RoundingMode.HALF_UP).multiply(BigDecimal.valueOf(100d));
            }
            if (finishNum.compareTo(BigDecimal.ZERO) != 0) {
                return BigDecimal.valueOf(100d);
            }
        } catch (Exception ex) {
            log4j.error(ex);
        }
        return BigDecimal.ZERO;
    }


}
