/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.hanbell.kpi.ejb;

import cn.hanbell.kpi.comm.SuperEJBForKPI;
import cn.hanbell.kpi.entity.QualityLevelTarget;
import java.lang.reflect.Field;
import java.util.List;
import javax.ejb.Stateless;
import javax.ejb.LocalBean;
import javax.persistence.Query;

/**
 *
 * @author C1749
 */
@Stateless
@LocalBean
public class QualityLevelTargetBean extends SuperEJBForKPI<QualityLevelTarget> {

    public QualityLevelTargetBean() {
        super(QualityLevelTarget.class);
    }

    public List<QualityLevelTarget> findByIndicatorIdAndSeq(int indicatorId, int seq) {
        Query query = getEntityManager().createNamedQuery("QualityLevelTarget.findByIndicatorIdAndSeq");
        query.setParameter("indicatorId", indicatorId);
        query.setParameter("seq", seq);
        try {
            return query.getResultList();
        } catch (Exception ex) {
            log4j.error("QualityLevelTargetBean类的findByIndicatorIdAndSeq()方法异常：",ex);
            return null;
        }
    }
    
        public List<QualityLevelTarget> findBySeq(int seq) {
        Query query = getEntityManager().createNamedQuery("QualityLevelTarget.findBySeq");
        query.setParameter("seq", seq);
        try {
            return query.getResultList();
        } catch (Exception ex) {
            log4j.error("QualityLevelTargetBean类的findByIndicatorIdAndSeq()方法异常：",ex);
            return null;
        }
    }
    
    

}
