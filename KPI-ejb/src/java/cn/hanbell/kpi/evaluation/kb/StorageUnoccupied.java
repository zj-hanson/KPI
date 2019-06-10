/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.hanbell.kpi.evaluation.kb;

import cn.hanbell.kpi.entity.kb.Paneldata;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import javax.persistence.Query;

/**
 *
 * @author C1879
 */
public class StorageUnoccupied extends Storage {

    public StorageUnoccupied() {
        super();
    }

    @Override
    public List<Paneldata> getPaneldataList(Date d, int pid, int type) {
        Paneldata pd;
        List<Paneldata> list = new ArrayList<>();
        StringBuilder sb = new StringBuilder();
        sb.append(" select linecode ,count(linecode),sum(ds),sum(gs) from ( select (CASE convert(VARCHAR(2),loc,2) ");
        sb.append(" when '01' then '1号线' when '02' then '1号线' when '03' then '2号线' when '04' then '2号线' ");
        sb.append(" when '05' then '3号线'when '06' then '3号线' when '07' then '4号线' when '08' then '4号线' end) as linecode, ");
        sb.append(" (CASE WHEN (right(asrs_tb_loc_mst.loc,2)) BETWEEN '01' and '06' THEN 1 ELSE 0 END ) AS ds, ");
        sb.append(" (CASE WHEN (right(asrs_tb_loc_mst.loc,2)) BETWEEN '07' and '17' THEN 1 ELSE 0 END ) as gs ");
        sb.append(" from asrs_tb_loc_mst where loc_sts ='N' and loc<'090101' ) as a GROUP BY linecode ");
        superEJB.setCompany("C");
        try {
            Query query = superEJB.getEntityManager().createNativeQuery(sb.toString());
            List result = query.getResultList();
            if (result != null && !result.isEmpty() && pid != 0) {
                for (int i = 0; i < result.size(); i++) {
                    pd = new Paneldata();
                    Object[] row = (Object[]) result.get(i);
                    pd.setPddate(d);
                    pd.setPdname(row[0].toString());
                    pd.setPid(pid);
                    pd.setValue1(BigDecimal.valueOf(Double.parseDouble(row[1].toString())));
                    //低储位
                    pd.setValue2(BigDecimal.valueOf(Double.parseDouble(row[2].toString())));
                    //高储位
                    pd.setValue3(BigDecimal.valueOf(Double.parseDouble(row[3].toString())));
                    list.add(pd);
                    if (type == 1) {
                        paneldataBean.persist(pd);
                    }
                }
            }
        } catch (Exception e) {
        }
        return list;
    }

    @Override
    public BigDecimal getValue(int y, int m, Date d, int type, LinkedHashMap<String, Object> map) {
        String pid = queryParams.get("id").toString() != null ? queryParams.get("id").toString() : "0";
        List<Paneldata> list = this.getPaneldataList(d, Integer.valueOf(pid), type);
        BigDecimal value = BigDecimal.ZERO;
        if (list != null && !list.isEmpty()) {
            for (Paneldata paneldata : list) {
                value = value.add(paneldata.getValue1());
            }
        }
        return value;
    }

}
