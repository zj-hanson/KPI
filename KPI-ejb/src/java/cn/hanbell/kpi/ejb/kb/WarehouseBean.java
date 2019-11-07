/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.hanbell.kpi.ejb.kb;

import cn.hanbell.kpi.comm.SuperEJBForERP;
import cn.hanbell.kpi.evaluation.Shipment;
import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
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
public class WarehouseBean implements Serializable {

    @EJB
    private SuperEJBForERP erpEJB;

    private final DecimalFormat dmf;

    public WarehouseBean() {
        this.dmf = new DecimalFormat("#0.00%");
    }
    
    public LinkedHashMap<String, List> getMap(int row_x) {
        LinkedHashMap<String, List> map = new LinkedHashMap<>();
        StringBuilder sb = new StringBuilder();
        sb.append(" SELECT lvl_z FROM asrs_tb_loc_mst  where row_x =${x}  and loc<'090101' GROUP BY row_x,lvl_z ORDER BY lvl_z DESC ");
        String sql = sb.toString().replace("${x}", String.valueOf(row_x));

        erpEJB.setCompany("C");
        Query query = erpEJB.getEntityManager().createNativeQuery(sql);
        try {
            List list = query.getResultList();
            for (Object object : list) {
                map.put(String.valueOf(object), getTierList(String.valueOf(row_x),String.valueOf(object)));
            }
        } catch (Exception ex) {
            Logger.getLogger(Shipment.class.getName()).log(Level.SEVERE, null, ex);
        }
        return map;
    }
    
    protected List getTierList(String row_x, String lvl_z) {
        List list = new ArrayList();
        StringBuilder sb = new StringBuilder();
        sb.append(" SELECT loc_sts FROM asrs_tb_loc_mst  where row_x =${x} and lvl_z=${z}  and loc<'090101' ORDER BY bay_y ASC ");
        String sql = sb.toString().replace("${x}", row_x).replace("${z}", lvl_z);
        
        erpEJB.setCompany("C");
        Query query = erpEJB.getEntityManager().createNativeQuery(sql);
        try {
            list = query.getResultList();
        } catch (Exception ex) {
        }
        return list;
    }

    private String[] getArr(int i, List<String[]> list) {
        String[] arr = new String[4];
        for (int j = 0; j < list.size(); j++) {
            list.get(j);
            arr[j] = list.get(j)[i];
        }
        return arr;
    }

    public LinkedHashMap<String, String[]> getTableMap() {
        LinkedHashMap<String, String[]> map = new LinkedHashMap<>();
        StringBuilder sb = new StringBuilder();
        //总储位、禁用储位、空储位、使用中储位、使用率、空储位低、空储位高、
        sb.append(" select linecode,count(linecode) as zong,SUM(jing) as jing, SUM(kong) as kong,SUM(yong) as yong,SUM(di) as di,SUM(gao) as gao  from ( ");
        sb.append(" select (CASE when row_x in (1,2) then '1' when row_x in (3,4) then '2' when row_x in (5,6) then '3' when row_x in (7,8) then '4' end) as linecode, ");
        sb.append(" (CASE WHEN (loc_sts ='X')  THEN 1 ELSE 0 END ) AS jing,(CASE WHEN (loc_sts not in ('X','N'))  THEN 1 ELSE 0 END ) AS yong,(CASE WHEN (loc_sts ='N')  THEN 1 ELSE 0 END ) AS kong, ");
        sb.append(" (CASE WHEN (loc_sts ='N') AND (right(asrs_tb_loc_mst.loc,2)) BETWEEN '01' and '06' THEN 1 ELSE 0 END ) AS di, ");
        sb.append(" (CASE WHEN (loc_sts ='N') AND (right(asrs_tb_loc_mst.loc,2)) BETWEEN '07' and '17' THEN 1 ELSE 0 END ) as gao ");
        sb.append(" from asrs_tb_loc_mst where loc<'090101' ) as a GROUP BY linecode ");
        String sql = sb.toString();
        erpEJB.setCompany("C");
        Query query = erpEJB.getEntityManager().createNativeQuery(sql);
        try {
            List result = query.getResultList();
            if (result != null && !result.isEmpty()) {
                String[] arr;
                List<String[]> list = new ArrayList<>();
                for (int i = 0; i < result.size(); i++) {
                    arr = new String[7];
                    Object[] row = (Object[]) result.get(i);
                    arr[0] = row[1].toString();
                    arr[1] = row[2].toString();
                    arr[2] = row[3].toString();
                    arr[3] = row[4].toString();
                    arr[4] = dmf.format(Double.parseDouble(arr[3]) / Double.parseDouble(arr[0]));                  
                    arr[5] = row[5].toString();
                    arr[6] = row[6].toString();
                    list.add(arr);
                }
                map.put("总储位", getArr(0, list));
                map.put("禁用储位", getArr(1, list));
                map.put("空储位", getArr(2, list));
                map.put("使用中储位", getArr(3, list));
                map.put("使用率", getArr(4, list));              
                map.put("空储位低", getArr(5, list));
                map.put("空储位高", getArr(6, list));
            }
        } catch (Exception ex) {
        }
        return map;
    }

}
