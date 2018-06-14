/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.hanbell.kpi.evaluation;

import cn.hanbell.kpi.comm.Actual;
import cn.hanbell.kpi.comm.SuperEJBForERP;
import com.lightshell.comm.BaseLib;
import java.math.BigDecimal;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.InitialContext;
import javax.persistence.Query;

/**
 *
 * @author C0160
 */
public abstract class SalesOrder implements Actual {

    protected SuperEJBForERP superEJB;

    protected LinkedHashMap<String, Object> queryParams;

    public SalesOrder() {
        queryParams = new LinkedHashMap<>();
    }

    public abstract BigDecimal getNotDelivery(Date d, LinkedHashMap<String, Object> map);

    public SuperEJBForERP getEJB() {
        return superEJB;
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
