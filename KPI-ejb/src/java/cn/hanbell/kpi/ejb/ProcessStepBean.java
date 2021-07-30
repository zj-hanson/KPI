/*
 * To change this license header, choose License Headers in Project Properties. To change this template file, choose
 * Tools | Templates and open the template in the editor.
 */
package cn.hanbell.kpi.ejb;

import cn.hanbell.kpi.comm.SuperEJBForKPI;
import cn.hanbell.kpi.entity.ProcessStep;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.ejb.Stateless;
import javax.ejb.LocalBean;
import javax.persistence.Query;

/**
 *
 * @author C0160
 */
@Stateless
@LocalBean
public class ProcessStepBean extends SuperEJBForKPI<ProcessStep> {

    public ProcessStepBean() {
        super(ProcessStep.class);
    }

    public void delete(String company, Date date, int type, String equipment) {
        List<ProcessStep> entities = findByEndTimeAndEquipment(company, date, type, equipment);
        if (entities != null && !entities.isEmpty()) {
            delete(entities);
        }
    }

    public List<ProcessStep> findByEndTimeAndEquipment(String company, Date endTime, int type, String equipment) {
        Calendar c = Calendar.getInstance();
        c.setTime(endTime);
        int y = c.get(Calendar.YEAR);
        int m = c.get(Calendar.MONTH) + 1;
        int d = c.get(Calendar.DAY_OF_MONTH);
        StringBuilder sb = new StringBuilder();
        sb.append(
            "SELECT p FROM ProcessStep p WHERE p.company = :company AND FUNCTION('YEAR',p.endTime) = :y AND FUNCTION('MONTH',p.endTime) = :m ");
        switch (type) {
            case 2:
                // 月
                sb.append(" AND FUNCTION('DAY',p.endTime) <= :d ");
                break;
            case 5:
                // 日
                sb.append(" AND FUNCTION('DAY',p.endTime) = :d ");
                break;
            default:
                sb.append(" AND FUNCTION('DAY',p.endTime) = :d ");
        }
        sb.append(" AND p.equipment = :equipment");
        Query query = getEntityManager().createQuery(sb.toString());
        query.setParameter("company", company);
        query.setParameter("y", y);
        query.setParameter("m", m);
        query.setParameter("d", d);
        query.setParameter("equipment", equipment);
        try {
            return query.getResultList();
        } catch (Exception ex) {
            log4j.error(ex);
            return null;
        }
    }

    /**
     *
     * @param company
     *                      公司
     * @param endTime
     *                      报工完成时间
     * @param type
     *                      Calendar枚举 2-按月 5-按天
     * @param equipment
     *                      设备编号
     * @return 工号 姓名 标准工时 报工工时 标准产值
     */
    public List getEmployeeOperationTime(String company, Date endTime, int type, String equipment) {
        Calendar c = Calendar.getInstance();
        c.setTime(endTime);
        int y = c.get(Calendar.YEAR);
        int m = c.get(Calendar.MONTH) + 1;
        int d = c.get(Calendar.DAY_OF_MONTH);
        StringBuilder sb = new StringBuilder();
        sb.append(
            "SELECT p.userid,p.user,sum(p.standardMachineTime * p.qty) as standardMachineTime,sum(p.processingTime) as processingTime,sum(p.standardMachineTime * p.qty * p.standCost) as productionValue FROM ProcessStep p WHERE p.company = '${company}' AND YEAR(p.endTime) = ${y} AND MONTH(p.endTime) = ${m} ");
        switch (type) {
            case 2:
                // 月
                sb.append(" AND DAY(p.endTime) <= ${d} ");
                break;
            case 5:
                // 日
                sb.append(" AND DAY(p.endTime) = ${d} ");
                break;
            default:
                sb.append(" AND DAY(p.endTime) = ${d} ");
        }
        if (equipment != null && !"".equals(equipment)) {
            sb.append(" AND p.equipment = '").append(equipment).append("' ");
        }
        sb.append(" GROUP BY p.userid,p.user ORDER BY sum(p.processingTime) DESC");
        String sql = sb.toString().replace("${company}", company).replace("${y}", String.valueOf(y))
            .replace("${m}", String.valueOf(m)).replace("${d}", String.valueOf(d));

        Query query = getEntityManager().createNativeQuery(sql);
        try {
            return query.getResultList();
        } catch (Exception ex) {
            log4j.error(ex);
            return null;
        }
    }

    public void save(List<ProcessStep> data) {
        for (ProcessStep e : data) {
            persist(e);
        }
    }

}
