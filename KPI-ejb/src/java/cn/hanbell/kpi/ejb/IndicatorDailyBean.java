/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.hanbell.kpi.ejb;

import cn.hanbell.kpi.comm.SuperEJBForKPI;
import cn.hanbell.kpi.entity.IndicatorDaily;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.Query;

/**
 *
 * @author C1879
 */
@Stateless
@LocalBean
public class IndicatorDailyBean extends SuperEJBForKPI<IndicatorDaily> {

    public IndicatorDailyBean() {
        super(IndicatorDaily.class);
    }

    public IndicatorDaily findByPIdDateAndType(int pid, int seq, int m, String type) {
        Query query = getEntityManager().createNamedQuery("IndicatorDaily.findByPidDateAndType");
        query.setParameter("pid", pid);
        query.setParameter("seq", seq);
        query.setParameter("mth", m);
        query.setParameter("type", type);
        try {
            Object o = query.getSingleResult();
            return (IndicatorDaily) o;
        } catch (Exception ex) {
            return null;
        }
    }

    
    public void deleteByPId(int pid) {
        StringBuilder sb = new StringBuilder();
        sb.append(" DELETE FROM indicatordaily  WHERE pid = ${pid} ");
        String sql = sb.toString().replace("${pid}", String.valueOf(pid));
        try {
                Query query = getEntityManager().createNativeQuery(sql);
                int count = query.executeUpdate();
                System.out.println("cn.hanbell.kpi.ejb.IndicatorDailyBean.deleteByPId()受影响行数：" + count);
        } catch (Exception e) {
            System.out.println("cn.hanbell.kpi.ejb.IndicatorDailyBean.deleteByPId()" + e);
        }

    }

    public boolean queryIndicatorDailyIsExist(int pid, int seq) {
        Query query = getEntityManager().createNamedQuery("IndicatorDaily.findByPidAndSeq");
        query.setParameter("pid", pid);
        query.setParameter("seq", seq);
        try {
            return !query.getResultList().isEmpty();
        } catch (Exception e) {
            return false;
        }
    }
}
