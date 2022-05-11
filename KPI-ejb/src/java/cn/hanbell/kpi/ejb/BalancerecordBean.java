/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.hanbell.kpi.ejb;

import cn.hanbell.kpi.comm.SuperEJBForKPI;
import cn.hanbell.kpi.entity.Balancerecord;
import java.util.List;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.Query;

/**
 *
 * @author C2082
 */
@Stateless
@LocalBean
public class BalancerecordBean extends SuperEJBForKPI<Balancerecord> {

    public BalancerecordBean() {
        super(Balancerecord.class);
    }

    public List<Balancerecord> findByYeaAndMon(Balancerecord dr) throws Exception {
        Query query = getEntityManager().createNamedQuery("Balancerecord.findByYearAndMon");
        query.setParameter("year", dr.getYear());
        query.setParameter("mon", dr.getMon());
        try {
            return query.getResultList();
        } catch (Exception ex) {
            throw new Exception("找不到当前时间的资产负债");
        }
    }
//    
//    select a.itemname,yearmon,monthmon,difference,scale+'%',b.leftshow,b.highlight,b.whethershow
//from balancerecord a left join balancerecordassisted b on a.facno=b.facno and a.type=b.type and a.itemname=b.itemname where a.year=2022 and a.mon=4
//and b.whethershow=true

    public List<Object[]> findByYeaAndMonAndWhethershow(int year, int mon, boolean whethershow) throws Exception {
        StringBuffer sql = new StringBuffer();
        sql.append(" select a.itemname,yearmon,monthmon,difference,concat(scale,'%'),b.leftshow,b.highlight,b.whethershow");
        sql.append(" from balancerecord a left join balancerecordassisted b on a.facno=b.facno and a.type=b.type and a.itemname=b.itemname where a.year=").append(year);
        sql.append(" and a.mon=").append(mon);
        if (whethershow==false) {
            sql.append(" and b.whethershow=true");
        }
        Query query = getEntityManager().createNativeQuery(sql.toString());
        try {
            return query.getResultList();
        } catch (Exception ex) {
            throw new Exception("找不到当前时间的资产负债");
        }
    }
}
