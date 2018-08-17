/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.hanbell.kpi.evaluation;

import cn.hanbell.kpi.comm.Actual;
import cn.hanbell.kpi.comm.SuperEJBForERP;
import java.util.LinkedHashMap;
import javax.naming.InitialContext;

/**
 *
 * @author C1749
 */
public abstract class Complaints implements Actual {

    protected SuperEJBForERP superEJB;
    protected LinkedHashMap<String, Object> queryParams;

    public Complaints() {
        queryParams = new LinkedHashMap<>();
    }

    public SuperEJBForERP getSuperEJB() {
        return superEJB;
    }

    public void setSuperEJB(SuperEJBForERP superEJB) {
        this.superEJB = superEJB;
    }

    @Override
    public void setEJB(String JNDIName) throws Exception {
        InitialContext c = new InitialContext();
        Object objRef = c.lookup(JNDIName);
        superEJB = (SuperEJBForERP) objRef;
    }

    @Override
    public LinkedHashMap<String, Object> getQueryParams() {
        return queryParams;
    }
    
}
