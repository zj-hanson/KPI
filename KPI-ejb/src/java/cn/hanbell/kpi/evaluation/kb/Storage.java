/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.hanbell.kpi.evaluation.kb;

import cn.hanbell.kpi.comm.Actual;
import cn.hanbell.kpi.comm.SuperEJBForERP;
import cn.hanbell.kpi.ejb.kb.PaneldataBean;
import cn.hanbell.kpi.entity.kb.Paneldata;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.logging.Level;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author C1879
 */
public abstract class Storage implements Actual {

    protected SuperEJBForERP superEJB;

    protected LinkedHashMap<String, Object> queryParams;

    protected final Logger log4j = LogManager.getLogger();

    protected PaneldataBean paneldataBean = lookupPaneldataBean();

    public Storage() {
        queryParams = new LinkedHashMap<>();
    }

    public SuperEJBForERP getEJB() {
        return superEJB;
    }

    public List<Paneldata> getPaneldataList(Date d, int pid, int type) {
        return null;
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

    @Override
    public int getUpdateMonth(int y, int m) {
        return m;
    }

    @Override
    public int getUpdateYear(int y, int m) {
        return y;
    }

    private PaneldataBean lookupPaneldataBean() {
        try {
            Context c = new InitialContext();
            return (PaneldataBean) c.lookup("java:global/KPI/KPI-ejb/PaneldataBean!cn.hanbell.kpi.ejb.kb.PaneldataBean");
        } catch (NamingException ne) {
            java.util.logging.Logger.getLogger(getClass().getName()).log(Level.SEVERE, "exception caught", ne);
            throw new RuntimeException(ne);
        }
    }

}
