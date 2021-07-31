/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.hanbell.kpi.ejb.eam;

import cn.hanbell.kpi.comm.SuperEJBForEAM;
import cn.hanbell.kpi.entity.eam.EquipmentAnalyResult;
import cn.hanbell.kpi.entity.eam.EquipmentStandard;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
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
        sbSql.append(" SELECT * FROM equipmentstandard WHERE  status='V' and nexttime>='").append(date).append("' ");
        if (standardlevel != null && !"".equals(standardlevel)) {
            sbSql.append(" AND  standardlevel='").append(standardlevel).append("'");
        } else {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            Date d = formatter.parse(date);//将String格式转为日期格式
            Calendar cal = Calendar.getInstance();
            cal.setTime(d);
            cal.add(Calendar.MONTH,1);//获取下个月1号时间,因计划排程为每月1号所以直接月份相加
            sbSql.append(" AND  nexttime<'").append(formatter.format(cal.getTime())).append("'");
            sbSql.append(" AND  standardlevel!='一级'");
        }
        Query query = this.getEntityManager().createNativeQuery(sbSql.toString(), EquipmentStandard.class);
        List<EquipmentStandard> sList = query.getResultList();
        return sList;
    }

    public List<Object[]> getMonthlyReport(int y, String string, String h) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
