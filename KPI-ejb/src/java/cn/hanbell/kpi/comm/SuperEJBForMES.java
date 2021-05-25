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
 * @author C0160
 */
@Stateless
@LocalBean
public class SuperEJBForMES implements Serializable {

    protected String company = "C";

    @PersistenceContext(unitName = "SHBMES-ejbPU")
    private EntityManager em_shbmes;

    @PersistenceContext(unitName = "PU_HansonMES")
    private EntityManager em_hansonmes;

    @PersistenceContext(unitName = "PU_HanyoungMES")
    private EntityManager em_hanyoungmes;

    public SuperEJBForMES() {

    }

    public EntityManager getEntityManager() {
        return getEntityManager(getCompany());
    }

    protected EntityManager getEntityManager(String facno) {
        switch (facno) {
            case "C":
                return em_shbmes;
            case "H":
                return em_hansonmes;
            case "Y":
                return em_hanyoungmes;
            default:
                return em_shbmes;
        }
    }

    /**
     * @return the company
     */
    public String getCompany() {
        return company;
    }

    /**
     * @param company the company to set
     */
    public void setCompany(String company) {
        this.company = company;
    }

}
