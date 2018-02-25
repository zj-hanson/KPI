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
public class SuperEJBForERP implements Serializable {

    protected String company = "C";

    @PersistenceContext(unitName = "PU_shberp")
    private EntityManager em_shberp;

    @PersistenceContext(unitName = "PU_cqerp")
    private EntityManager em_cqerp;

    @PersistenceContext(unitName = "PU_gzerp")
    private EntityManager em_gzerp;

    @PersistenceContext(unitName = "PU_jnerp")
    private EntityManager em_jnerp;

    @PersistenceContext(unitName = "PU_njerp")
    private EntityManager em_njerp;

    @PersistenceContext(unitName = "PU_hansonerp")
    private EntityManager em_hansonerp;

    @PersistenceContext(unitName = "PU_comererp")
    private EntityManager em_comererp;

    @PersistenceContext(unitName = "PU_qtcerp")
    private EntityManager em_qtcerp;

    @PersistenceContext(unitName = "PU_sderp")
    private EntityManager em_sderp;

    @PersistenceContext(unitName = "PU_hkerp")
    private EntityManager em_hkerp;

    @PersistenceContext(unitName = "PU_thberp")
    private EntityManager em_thberp;

    public SuperEJBForERP() {

    }

    public EntityManager getEntityManager() {
        return getEntityManager(getCompany());
    }

    protected EntityManager getEntityManager(String facno) {
        switch (facno) {
            case "A":
                return em_thberp;
            case "C":
                return em_shberp;
            case "G":
                return em_gzerp;
            case "J":
                return em_jnerp;
            case "N":
                return em_njerp;
            case "C4":
                return em_cqerp;
            case "H":
                return em_hansonerp;
            case "K":
                return em_comererp;
            case "Q":
                return em_qtcerp;
            case "W":
                return em_sderp;
            case "X":
                return em_hkerp;
            default:
                return em_shberp;
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
