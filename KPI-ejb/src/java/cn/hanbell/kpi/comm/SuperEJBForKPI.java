/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.hanbell.kpi.comm;

import com.lightshell.comm.SuperEJB;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author C0160
 * @param <T>
 */
public abstract class SuperEJBForKPI<T> extends SuperEJB<T> {

    protected final String url = "http://127.0.0.1:8480/EAP/EAPWebService";
    protected final String nameSpace = "http://jws.hanbell.cn/";

    @PersistenceContext(unitName = "KPI-ejbPU")
    private EntityManager em_shbkpi;

    public SuperEJBForKPI(Class<T> entityClass) {
        super(entityClass);
    }

    @Override
    public EntityManager getEntityManager() {
        return em_shbkpi;
    }

    public String formatString(String value, String format) {
        if (value.length() >= format.length()) {
            return value;
        }
        return format.substring(0, format.length() - value.length()) + value;
    }

}
