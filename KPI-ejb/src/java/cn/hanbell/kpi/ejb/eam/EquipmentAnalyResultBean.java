/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.hanbell.kpi.ejb.eam;

import cn.hanbell.kpi.comm.SuperEJBForEAM;
import cn.hanbell.kpi.comm.SuperEJBForMES;
import cn.hanbell.kpi.entity.eam.EquipmentAnalyResult;
import cn.hanbell.kpi.entity.eam.EquipmentStandard;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.LocalBean;
import javax.persistence.Query;

/**
 *
 * @author C2079
 */
@Stateless
@LocalBean
public class EquipmentAnalyResultBean extends SuperEJBForEAM<EquipmentAnalyResult> {

    @EJB
    private SuperEJBForMES mesEJB;

    public EquipmentAnalyResultBean() {
        super(EquipmentAnalyResult.class);
    }

    /**
     * 自动生成自主点检排程 根据传入的等级及日期来获取生成的基准
     *
     * @param date
     * @param standardlevel
     * @return
     */
    public List<EquipmentStandard> getMonthlyReport(String date, String standardlevel) throws ParseException {
        StringBuilder sbSql = new StringBuilder();
        StringBuilder sbMES = new StringBuilder();
        String str = "";
        sbMES.append(" select EQPID FROM EQP_AVAILABLETIME_SCHEDULE WHERE PLANDATE ='").append(date.replace('-', '/')).append("' AND AVAILABLEMINS=0");
        Query query = mesEJB.getEntityManager().createNativeQuery(sbMES.toString());
        List<String> sMES = query.getResultList();
        for (String objects : sMES) {
            str = str + "'" + objects + "',";
        }
        str = str.substring(0, str.length() - 1);
        sbSql.append(" SELECT E.* FROM equipmentstandard E  LEFT JOIN assetcard A  ON E.assetno=A.formid  WHERE  E.status='V' ");
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        Date d = formatter.parse(date);//将String格式转为日期格式
        Calendar cal = Calendar.getInstance();
        cal.setTime(d);
        if (standardlevel != null && !"".equals(standardlevel)) {
            cal.add(Calendar.DATE, 1);//获取明天日期
            sbSql.append(" AND E.nexttime>='").append(date).append("' ");
            sbSql.append(" AND E.nexttime<'").append(formatter.format(cal.getTime())).append("'");
            sbSql.append(" AND E.standardlevel='").append(standardlevel).append("'");
            sbSql.append(" AND A.remark  NOT IN(").append(str).append(")");
        } else {
            cal.add(Calendar.MONTH, 1);//获取下个月1号时间,因计划排程为每月1号所以直接月份相加
            sbSql.append(" AND E.nexttime>='").append(date).append("' ");
            sbSql.append(" AND E.nexttime<'").append(formatter.format(cal.getTime())).append("'");
            sbSql.append(" AND E.standardlevel!='一级'");
        }
        query = this.getEntityManager().createNativeQuery(sbSql.toString(), EquipmentStandard.class);
        List<EquipmentStandard> sList = query.getResultList();
        return sList;
    }

    //根据资产编号获取对应资产
    public Object[] findByAssetno(String value) {
        StringBuilder sbSql = new StringBuilder();
        sbSql.append(" SELECT * FROM assetcard WHERE  formid='").append(value).append("' ");
        Query query = this.getEntityManager().createNativeQuery(sbSql.toString());
        try {
            Object o = query.getSingleResult();
            return (Object[]) o;
        } catch (Exception ex) {
            return null;
        }
    }

    public List<EquipmentAnalyResult> getUnqualifiedEquipmentAnalyResult(String deptname, String staDate) {
        StringBuffer eSql = new StringBuffer();
        eSql.append(" SELECT * FROM equipmentanalyresult  WHERE TIMESTAMPDIFF(MINUTE,startdate,enddate  )<2");
        eSql.append("  AND formdate ='").append(staDate).append("' and deptname='").append(deptname).append("'  AND standardlevel='一级'");
        Query query = this.getEntityManager().createNativeQuery(eSql.toString(), EquipmentAnalyResult.class);
        List<EquipmentAnalyResult> list = query.getResultList();
        return list;
    }

}
