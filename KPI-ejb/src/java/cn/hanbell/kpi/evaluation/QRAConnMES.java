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
import java.util.List;
import javax.naming.InitialContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author C1749 MES数据连接
 */
public abstract class QRAConnMES implements Actual {

    protected SuperEJBForMES superEJB;
    protected LinkedHashMap<String, Object> queryParams;
    protected final Logger log4j = LogManager.getLogger();

    public QRAConnMES() {
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
        int month;
        if (m == 1) {
            month = 12;
        } else {
            month = m - 1;
        }
        return month;
    }

    @Override
    public int getUpdateYear(int y, int m) {
        int year;
        if (m == 1) {
            year = y - 1;
        } else {
            year = y;
        }
        return year;
    }

    @Override
    public BigDecimal getValue(int y, int m, Date d, int type, LinkedHashMap<String, Object> map) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    //删除list中重复的制造号码
    public List removeDuplicate(List list) {
        for (int i = 0; i < list.size() - 1; i++) {
            for (int j = list.size() - 1; j > i; j--) {
                if (list.get(j).equals(list.get(i))) {
                    list.remove(j);
                }
            }
        }
        return list;
    }
}
