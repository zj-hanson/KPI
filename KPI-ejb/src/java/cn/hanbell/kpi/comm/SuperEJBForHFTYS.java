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
 * @author C1749
 */
@Stateless
@LocalBean
public class SuperEJBForHFTYS implements Serializable {

    protected String conn = "H";
    public SuperEJBForHFTYS() {

    }
    @PersistenceContext(unitName = "HFTYS-ejbPU")
    private EntityManager hftys;

    @PersistenceContext(unitName = "PHFTYS-ejbPU2")
    private EntityManager air;

    public EntityManager getEntityManager() {
        return getEntityManager(getConn());
    }
    //H表示冷媒的数据库HFTYSDB  P表示真空的数据库AIRDB
    protected EntityManager getEntityManager(String conn) {
        switch (conn) {
            case "H":
                return hftys;
            case "P":
                return air;
            default:
                return hftys;
        }
    }

    public String getConn() {
        return conn;
    }

    public void setConn(String conn) {
        this.conn = conn;
    }

}
