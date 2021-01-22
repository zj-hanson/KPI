/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.hanbell.kpi.comm;

import java.io.Serializable;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author C749
 */
@Stateless
@LocalBean
public class SuperEJBForTMS implements Serializable {

    @PersistenceContext(unitName = "SHBTMS-ejbPU")
    private EntityManager em;

    public SuperEJBForTMS() {

    }

    public EntityManager getEntityManager() {
        return em;
    }

}
