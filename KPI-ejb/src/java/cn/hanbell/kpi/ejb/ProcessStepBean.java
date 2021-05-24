/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
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
        Query query = getEntityManager().createQuery("SELECT p FROM ProcessStep p WHERE p.company = :company AND FUNCTION('YEAR',p.endTime) = :y AND FUNCTION('MONTH',p.endTime) = :m AND FUNCTION('DAY',p.endTime) = :d AND p.equipment = :equipment");
        query.setParameter("company", company);
        query.setParameter("y", y);
        query.setParameter("m", m);
        query.setParameter("d", d);
        query.setParameter("equipment", equipment);
        try {
            return query.getResultList();
        } catch (Exception ex) {
            return null;
        }
    }

    public void save(List<ProcessStep> data) {
        for (ProcessStep e : data) {
            persist(e);
        }
    }

}
