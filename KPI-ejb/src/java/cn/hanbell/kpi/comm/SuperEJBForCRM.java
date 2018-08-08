/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.hanbell.kpi.comm;

import com.lightshell.comm.SuperEJB;
import java.io.Serializable;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author C0160
 */
@Stateless
@LocalBean
public class SuperEJBForCRM implements Serializable {

    @PersistenceContext(unitName = "CRM-ejbPU")
    private EntityManager em;

    public SuperEJBForCRM() {

    }

    public EntityManager getEntityManager() {
        return em;
    }

}
