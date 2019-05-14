/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.hanbell.kpi.ejb;

import cn.hanbell.kpi.comm.SuperEJBForERP;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.Query;

/**
 *
 * @author C1879
 */
@Stateless
@LocalBean
public class StorageBean implements Serializable {

    @EJB
    private SuperEJBForERP erpEJB;

    public StorageBean() {
    }
   
    //占用储位
    public List<String[]> getStorageOccupyList() {
        StringBuilder sb = new StringBuilder();
        sb.append(" select c.wareh ,count(c.loc) as num from ( select top 50 a.loc,b.wareh from asrs_tb_loc_mst a,asrs_tb_loc_dtl b ");
        sb.append(" where a.loc_sts in ('S') and a.loc<'090101' and a.loc=b.loc group by a.loc,b.wareh) c group by c.wareh ");

        erpEJB.setCompany("C");
        try {
            Query query = erpEJB.getEntityManager().createNativeQuery(sb.toString());
            List result = query.getResultList();
            List<String[]> list = new ArrayList<>();
            if (result != null && !result.isEmpty()) {
                for (int i = 0; i < result.size(); i++) {
                    Object[] row = (Object[]) result.get(i);
                    String[] arr = new String[2];
                    arr[0] = row[0].toString();
                    arr[1] = row[1].toString();
                    list.add(arr);
                }
                return list;
            }
        } catch (Exception e) {
        }
        return null;
    }

    //空余储位
    public List<String[]> getStorageUnoccupiedList() {
        StringBuilder sb = new StringBuilder();
        sb.append(" select linecode ,count(linecode) as num from ( select (CASE convert(VARCHAR(2),loc,2) ");
        sb.append(" when '01' then '1号线' when '02' then '1号线' when '03' then '2号线' when '04' then '2号线' when '05' then '3号线' when '06' then '3号线' when '07' then '4号线' when '08' then '4号线' end ");
        sb.append(" ) as linecode from asrs_tb_loc_mst where loc_sts ='N' and loc<'090101' ) as a GROUP BY linecode ");
        
        erpEJB.setCompany("C");
        try {
            Query query = erpEJB.getEntityManager().createNativeQuery(sb.toString());
            List result = query.getResultList();
            List<String[]> list = new ArrayList<>();
            if (result != null && !result.isEmpty()) {
                for (int i = 0; i < result.size(); i++) {
                    Object[] row = (Object[]) result.get(i);
                    String[] arr = new String[2];
                    arr[0] = row[0].toString();
                    arr[1] = row[1].toString();
                    list.add(arr);
                }
                return list;
            }
        } catch (Exception e) {
        }
        return null;
    }
}
