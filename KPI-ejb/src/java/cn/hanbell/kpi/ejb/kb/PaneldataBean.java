/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.hanbell.kpi.ejb.kb;

import cn.hanbell.kpi.comm.SuperEJBForKPI;
import cn.hanbell.kpi.entity.kb.Paneldata;
import com.lightshell.comm.BaseLib;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.Query;

/**
 *
 * @author C1879
 */
@Stateless
@LocalBean
public class PaneldataBean extends SuperEJBForKPI<Paneldata> {

    public PaneldataBean() {
        super(Paneldata.class);
    }

    public void deleteYesterdayData(int pid, Date date) {
        StringBuilder sb = new StringBuilder();
        sb.append(" DELETE FROM paneldata WHERE pid='${pid}' AND date_format(pddate, '%Y-%m-%d')<='${d}'");
        String sql = sb.toString().replace("${pid}", String.valueOf(pid)).replace("${d}", BaseLib.formatDate("yyyy-MM-dd", date));
        Query query = getEntityManager().createNativeQuery(sql);
        int count = query.executeUpdate();
    }

    public void deleteTheSameData(int pid, Date date) {
        StringBuilder sb = new StringBuilder();
        sb.append(" DELETE FROM paneldata WHERE pid='${pid}' AND date_format(pddate, '%Y-%m-%d %H:%i')='${d}'");
        String sql = sb.toString().replace("${pid}", String.valueOf(pid)).replace("${d}", BaseLib.formatDate("yyyy-MM-dd HH:mm", date));
        Query query = getEntityManager().createNativeQuery(sql);
        query.executeUpdate();
    }

    public List<Paneldata> findByPidAndDate(int pid, Date date) {
        StringBuilder sb = new StringBuilder();
        sb.append(" Select * FROM paneldata WHERE pid='${pid}' AND date_format(pddate, '%Y-%m-%d %H:%i')='${d}' ORDER BY pdname ASC ");
        String sql = sb.toString().replace("${pid}", String.valueOf(pid)).replace("${d}", BaseLib.formatDate("yyyy-MM-dd HH:mm", date));
        Query query = getEntityManager().createNativeQuery(sql);
        List list = query.getResultList();
        List<Paneldata> paneldatas = new ArrayList<>();
        Paneldata pd;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            if (list != null && !list.isEmpty()) {
                for (Object object : list) {
                    Object[] row = (Object[]) object;
                    pd = new Paneldata();
                    pd.setId(Integer.parseInt(row[0].toString()));
                    pd.setPid(Integer.parseInt(row[1].toString()));
                    pd.setPddate(sdf.parse(row[2].toString()));
                    pd.setPdname(row[3].toString());
                    pd.setValue1(BigDecimal.valueOf(Double.parseDouble(row[4].toString())));
                    pd.setValue2(BigDecimal.valueOf(Double.parseDouble(row[5].toString())));
                    pd.setValue3(BigDecimal.valueOf(Double.parseDouble(row[6].toString())));
                    pd.setValue4(BigDecimal.valueOf(Double.parseDouble(row[7].toString())));
                    pd.setValue5(BigDecimal.valueOf(Double.parseDouble(row[8].toString())));
                    pd.setValue6(BigDecimal.valueOf(Double.parseDouble(row[9].toString())));
                    pd.setRemark(row[10]==null?"":row[10].toString());
                    paneldatas.add(pd);
                }
            }
        } catch (ParseException ex) {
            Logger.getLogger(PaneldataBean.class.getName()).log(Level.SEVERE, null, ex);
        }
        return paneldatas;

    }

}
