/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.hanbell.kpi.ejb.kb;

import cn.hanbell.kpi.comm.Actual;
import cn.hanbell.kpi.comm.SuperEJBForKPI;
import cn.hanbell.kpi.entity.kb.Panels;
import java.math.BigDecimal;
import java.util.Date;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;

/**
 *
 * @author C1879
 */
@Stateless
@LocalBean
public class PanelsBean extends SuperEJBForKPI<Panels> {

    protected Actual otherInterface;

    public PanelsBean() {
        super(Panels.class);
    }

    public void updateValue(Panels entity, int y, int m, Date d, int type) {
        int uy, um;
        BigDecimal na;
        //先计算Other
        if (entity != null && entity.getHasOther() != null && entity.getHasOther() > 0) {
            if (entity.getOther1Interface() != null && !"".equals(entity.getOther1Interface())) {
                try {
                    otherInterface = (Actual) Class.forName(entity.getOther1Interface()).newInstance();
                    uy = otherInterface.getUpdateYear(y, m);
                    um = otherInterface.getUpdateMonth(y, m);
                    otherInterface.setEJB(entity.getOther1EJB());
                    otherInterface.getQueryParams().put("id", entity.getId());
                    na = otherInterface.getValue(uy, um, d, type, otherInterface.getQueryParams());
                    entity.setOther1(na);
                } catch (Exception ex) {
                    log4j.error(ex);
                }
            }
            if (entity.getOther2Interface() != null && !"".equals(entity.getOther2Interface())) {
                try {
                    otherInterface = (Actual) Class.forName(entity.getOther1Interface()).newInstance();
                    uy = otherInterface.getUpdateYear(y, m);
                    um = otherInterface.getUpdateMonth(y, m);
                    otherInterface.setEJB(entity.getOther1EJB());
                    otherInterface.getQueryParams().put("id", entity.getId());
                    na = otherInterface.getValue(uy, um, d, 0, otherInterface.getQueryParams());
                    entity.setOther2(na);
                } catch (Exception ex) {
                    log4j.error(ex);
                }
            }
            if (entity.getOther3Interface() != null && !"".equals(entity.getOther3Interface())) {
                try {
                    otherInterface = (Actual) Class.forName(entity.getOther1Interface()).newInstance();
                    uy = otherInterface.getUpdateYear(y, m);
                    um = otherInterface.getUpdateMonth(y, m);
                    otherInterface.setEJB(entity.getOther1EJB());
                    otherInterface.getQueryParams().put("id", entity.getId());
                    na = otherInterface.getValue(uy, um, d, 0, otherInterface.getQueryParams());
                    entity.setOther3(na);
                } catch (Exception ex) {
                    log4j.error(ex);
                }
            }
            if (entity.getOther4Interface() != null && !"".equals(entity.getOther4Interface())) {
                try {
                    otherInterface = (Actual) Class.forName(entity.getOther1Interface()).newInstance();
                    uy = otherInterface.getUpdateYear(y, m);
                    um = otherInterface.getUpdateMonth(y, m);
                    otherInterface.setEJB(entity.getOther1EJB());
                    otherInterface.getQueryParams().put("id", entity.getId());
                    na = otherInterface.getValue(uy, um, d, 0, otherInterface.getQueryParams());
                    entity.setOther4(na);
                } catch (Exception ex) {
                    log4j.error(ex);
                }
            }
            if (entity.getOther5Interface() != null && !"".equals(entity.getOther5Interface())) {
                try {
                    otherInterface = (Actual) Class.forName(entity.getOther1Interface()).newInstance();
                    uy = otherInterface.getUpdateYear(y, m);
                    um = otherInterface.getUpdateMonth(y, m);
                    otherInterface.setEJB(entity.getOther1EJB());
                    otherInterface.getQueryParams().put("id", entity.getId());
                    na = otherInterface.getValue(uy, um, d, 0, otherInterface.getQueryParams());
                    entity.setOther4(na);
                } catch (Exception ex) {
                    log4j.error(ex);
                }
            }
            if (entity.getOther6Interface() != null && !"".equals(entity.getOther6Interface())) {
                try {
                    otherInterface = (Actual) Class.forName(entity.getOther1Interface()).newInstance();
                    uy = otherInterface.getUpdateYear(y, m);
                    um = otherInterface.getUpdateMonth(y, m);
                    otherInterface.setEJB(entity.getOther1EJB());
                    otherInterface.getQueryParams().put("id", entity.getId());
                    na = otherInterface.getValue(uy, um, d, 0, otherInterface.getQueryParams());
                    entity.setOther6(na);
                } catch (Exception ex) {
                    log4j.error(ex);
                }
            }
            update(entity);
        }
    }

}
