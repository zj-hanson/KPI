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
public class StorageOccupy extends Storage {

    public StorageOccupy() {
        super();
    }

    @Override
    public List<Paneldata> getPaneldataList(Date d, int pid, int type) {
        Paneldata pd;
        List<Paneldata> list = new ArrayList<>();
        StringBuilder sb = new StringBuilder();
        sb.append(" select c.wareh ,count(c.loc) as num from ( select top 50 a.loc,b.wareh from asrs_tb_loc_mst a,asrs_tb_loc_dtl b ");
        sb.append(" where a.loc_sts in ('S') and a.loc<'090101' and a.loc=b.loc group by a.loc,b.wareh) c group by c.wareh ");

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
