/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.hanbell.kpi.evaluation;

import cn.hanbell.kpi.comm.Actual;
import cn.hanbell.kpi.comm.SuperEJBForMES;
import java.math.BigDecimal;
import java.util.Date;
import java.util.LinkedHashMap;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.persistence.Query;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author C1749 三次元MES中的不合格单数据
 */
public class QRACubicElementAmountBad implements Actual {

    protected SuperEJBForMES superEJB = lookupSuperEJBForMES();
    protected LinkedHashMap<String, Object> queryParams;
    protected final Logger log4j = LogManager.getLogger();

    public QRACubicElementAmountBad() {
        queryParams = new LinkedHashMap<>();
    }

    @Override
    public BigDecimal getValue(int y, int m, Date d, int type, LinkedHashMap<String, Object> map) {
        String SOURCEDPIP = map.get("SOURCEDPIP") != null ? map.get("SOURCEDPIP").toString() : "";//所属物料
        BigDecimal result1 = BigDecimal.ZERO;//圆形件精加工数
        BigDecimal result2 = BigDecimal.ZERO;//方形件精加工数
        StringBuilder sb = new StringBuilder();
        ////圆形件精加工数
        sb.append(" select isnull(count(1),0) as num from ( ");
        sb.append(" SELECT DISTINCT A.SYSID,A.PROJECTID,SOURCEDPIP,SOURCESTEPIP,DEFECTDESCRIPTION,FINALQCRESULT,ANALYSISJUDGEMENTRESULT,HIS.MODIFYTIME ");
        sb.append(" FROM FLOW_PROJECT_NOW A ");
        sb.append(" INNER JOIN  FLOW_FORM_UQF_S_HISTORY B ON A.PROJECTID=B.PROJECTID ");
        sb.append(" LEFT JOIN MFLOW_ROLEGROUP D ON B.UQFTYPE =D.ROLEGROUPID ");
        sb.append(" LEFT JOIN  ANALYSISRESULT_QCD F on A.PROJECTID=F.PROJECTID and F.PROJECTID = B.PROJECTID ");
        sb.append(" LEFT JOIN (SELECT PROJECTID,MODIFYTIME from FLOW_PROJECT_HISTORY where PREVIOUSNODE = 'UQFN0002' and  EFFECTIVEFLAG = 'Y' and ADDFLAG = 'N') HIS on HIS.PROJECTID = A.PROJECTID ");
        sb.append(" and F.PROJECTID = HIS.PROJECTID and B.PROJECTID = HIS.PROJECTID ");
        sb.append(" where A.PROJECTID like 'QC%' and SOURCESTEPIP  like '圆型件精研KAPP%' ");
        sb.append(" and F.DEFECTID in ('QCZZCXBL','QCZZDCBL') ");
        sb.append(" and F.PRODUCTID not like '%GB%' ");
        sb.append(" and D.ROLEGROUPID like 'UQFG0003%' ");
        sb.append(" and year(HIS.MODIFYTIME)=${y} and month(dateadd(HOUR,-8,HIS.MODIFYTIME))=${m} ");
        sb.append(" ) as a where a.FINALQCRESULT = '不合格' ");
        if (!"".equals(SOURCEDPIP)) {//判断所属的物料
            sb.append(" and a.SOURCEDPIP like'").append(SOURCEDPIP).append("%' ");
        }
        String yxsql = sb.toString().replace("${y}", String.valueOf(y)).replace("${m}", String.valueOf(m));
        sb.setLength(0);//两段SQL 清空sb的数据重新组合SQL
        //方形件精加工数
        sb.append(" select isnull(count(1),0) as num from ( ");
        sb.append(" SELECT DISTINCT A.SYSID,A.PROJECTID,SOURCEDPIP,SOURCESTEPIP,DEFECTDESCRIPTION,FINALQCRESULT,ANALYSISJUDGEMENTRESULT,HIS.MODIFYTIME ");
        sb.append(" FROM FLOW_PROJECT_NOW A ");
        sb.append(" INNER JOIN  FLOW_FORM_UQF_S_HISTORY B ON A.PROJECTID=B.PROJECTID ");
        sb.append(" LEFT JOIN MFLOW_ROLEGROUP D ON B.UQFTYPE =D.ROLEGROUPID ");
        sb.append(" LEFT JOIN  ANALYSISRESULT_QCD F on A.PROJECTID=F.PROJECTID and F.PROJECTID = B.PROJECTID ");
        sb.append(" LEFT JOIN (SELECT PROJECTID,MODIFYTIME from FLOW_PROJECT_HISTORY where PREVIOUSNODE = 'UQFN0002' and  EFFECTIVEFLAG = 'Y' and ADDFLAG = 'N') HIS on HIS.PROJECTID = A.PROJECTID ");
        sb.append(" and F.PROJECTID = HIS.PROJECTID and B.PROJECTID = HIS.PROJECTID ");
        sb.append(" where A.PROJECTID like 'QC%' and SOURCESTEPIP like '方型件精加工%' ");
        sb.append(" and D.ROLEGROUPID like 'UQFG0003%' ");
        sb.append(" and F.PRODUCTID not like '%GB%' ");
        sb.append(" and (B.ANALYSISJUDGEMENTRESULT like '特采%' or ANALYSISJUDGEMENTRESULT like '自行重工%' or ANALYSISJUDGEMENTRESULT like '就地报废%') ");
        sb.append(" and year(HIS.MODIFYTIME)=${y} and month(dateadd(HOUR,-8,HIS.MODIFYTIME))=${m} ");
        sb.append(" )as b where  b.FINALQCRESULT = '不合格' ");
        if (!"".equals(SOURCEDPIP)) {//判断所属的物料
            sb.append(" and b.SOURCEDPIP like'").append(SOURCEDPIP).append("%' ");
        }
        String fxsql = sb.toString().replace("${y}", String.valueOf(y)).replace("${m}", String.valueOf(m));
        Query query1 = superEJB.getEntityManager().createNativeQuery(yxsql);
        Query query2 = superEJB.getEntityManager().createNativeQuery(fxsql);
        try {
            Object o1 = query1.getSingleResult();
            Object o2 = query2.getSingleResult();
            result1 = BigDecimal.valueOf(Double.valueOf(o1.toString()));//圆型不合格数量
            result2 = BigDecimal.valueOf(Double.valueOf(o2.toString()));//方型不合格数量
            return result1.add(result2);
        } catch (Exception ex) {
            log4j.error("QRACubicElementAmountBad", ex);
        }
        //总数等于圆型 + 方型 的数量
        return BigDecimal.ZERO;
    }

    @Override
    public LinkedHashMap<String, Object> getQueryParams() {
        return queryParams;
    }

    protected SuperEJBForMES lookupSuperEJBForMES() {
        try {
            Context c = new InitialContext();
            return (SuperEJBForMES) c.lookup("java:global/KPI/KPI-ejb/SuperEJBForMES!cn.hanbell.kpi.comm.SuperEJBForMES");
        } catch (NamingException ne) {
            throw new RuntimeException(ne);
        }
    }

    @Override
    public void setEJB(String JNDIName) throws Exception {
        InitialContext c = new InitialContext();
        Object objRef = c.lookup("java:global/KPI/KPI-ejb/SuperEJBForMES!cn.hanbell.kpi.comm.SuperEJBForMES");
        superEJB = (SuperEJBForMES) objRef;
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

}
