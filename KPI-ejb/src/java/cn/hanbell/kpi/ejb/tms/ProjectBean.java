/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.hanbell.kpi.ejb.tms;

import cn.hanbell.kpi.comm.SuperEJBForTMS;
import cn.hanbell.kpi.entity.tms.Project;
import com.lightshell.comm.SuperEJB;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.LocalBean;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author C1749
 */
@Stateless
@LocalBean
public class ProjectBean implements Serializable {

    @PersistenceContext(unitName = "SHBTMS-ejbPU")
    private EntityManager em;

    @EJB
    private SuperEJBForTMS tmsEJB;

    public ProjectBean() {
        
    }

    public List<Project> getProjectData() {
        try {
            List<Project> result = new ArrayList<>();
            Query query = tmsEJB.getEntityManager().createNamedQuery("Project.findAll");
            List data = query.getResultList();
            for (int i = 0; i < data.size(); i++) {
                Object o[] = (Object[]) data.get(i);
                Project p = new Project();
                p.setId(String.valueOf(i+1));
                p.setProjectSeq(Integer.parseInt(o[0].toString()));
                p.setProjectName(o[1].toString());
                p.setPMUser(o[2].toString());
                p.setPMUserId(o[3].toString());
                result.add(p);
            }
            return result;
        } catch (NumberFormatException ex) {
            return null;
        }
    }
    
    public String findByProjectSeq(String projectSeq){
        try {
            Query query = tmsEJB.getEntityManager().createNamedQuery("Project.findByProjectSeq");
            query.setParameter("projectSeq", Integer.valueOf(projectSeq));
            Object o = query.getSingleResult();
            return o.toString();
        } catch (NumberFormatException e) {
            return "";
        }
    }

}
