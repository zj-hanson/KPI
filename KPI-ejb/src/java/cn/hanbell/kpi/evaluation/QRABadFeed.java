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
import javax.naming.InitialContext;

/**
 *
 * @author C1749
 */
public class QRABadFeed implements Actual {

    protected SuperEJBForMES superEJB;
    protected LinkedHashMap<String, Object> queryParams;

    public QRABadFeed() {
        queryParams = new LinkedHashMap<>();
    }

    public SuperEJBForMES getSuperEJB() {
        return superEJB;
    }

    public void setSuperEJB(SuperEJBForMES superEJB) {
        this.superEJB = superEJB;
    }

    @Override
    public void setEJB(String JNDIName) throws Exception {
        InitialContext c = new InitialContext();
        Object objRef = c.lookup(JNDIName);
        superEJB = (SuperEJBForMES) objRef;
    }

    @Override
    public LinkedHashMap<String, Object> getQueryParams() {
        return queryParams;
    }

    @Override
    public int getUpdateMonth(int y, int m) {
        return m;
    }

    @Override
    public int getUpdateYear(int y, int m) {
        return y;
    }

    @Override
    public BigDecimal getValue(int y, int m, Date d, int type, LinkedHashMap<String, Object> map) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
