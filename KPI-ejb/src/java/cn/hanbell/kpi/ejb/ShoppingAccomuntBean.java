/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.hanbell.kpi.ejb;

import cn.hanbell.kpi.comm.SuperEJBForERP;
import cn.hanbell.kpi.comm.SuperEJBForKPI;
import cn.hanbell.kpi.entity.AccountsReceivables;
import cn.hanbell.kpi.entity.ShoppingManufacturer;
import cn.hanbell.util.BaseLib;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.Query;

/**
 *
 * @author C2082
 */
@Stateless
@LocalBean
public class ShoppingAccomuntBean implements Serializable {

    @EJB
    private SuperEJBForERP erpEJB;
    @EJB
    private ShoppingManufacturerBean shoppingManufacturerBean;
    private final DecimalFormat df;
    private final DecimalFormat dfpercent;

    public ShoppingAccomuntBean() {
        this.df = new DecimalFormat("#,###");
        this.dfpercent = new DecimalFormat("0.00％");
    }

    private int findYear(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        return c.get(Calendar.YEAR);
    }

    private int findMonth(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        return c.get(Calendar.MONTH) + 1;
    }

    //
    public List<Object[]> getList(Date date) {
        List<Object[]> list = new ArrayList<>();
        list.add(getShbDate("SHB全部", "C", date, "", ""));
        list.add(getShbDate("SHB采购中心", "C", date, getWhereVdrnos("C").toString(), ""));
        list.add(getShbDate("THB全部", "A", date, "", ""));
        list.add(getShbDate("THB采购中心", "A", date, getWhereVdrnos("A").toString(), ""));
        list.add(getShbDate("HS全部", "H", date, "", ""));
        list.add(getShbDate("HS采购中心", "H", date, getWhereVdrnos("H").toString(), ""));
        list.add(getShbDate("SCM全部", "K", date, "", ""));
        list.add(getShbDate("SCM采购中心", "K", date, getWhereVdrnos("K").toString(), ""));
        list.add(getShbDate("ZCM全部", "E", date, "", ""));
        list.add(getShbDate("ZCM采购中心", "E", date, getWhereVdrnos("E").toString(), ""));
        list.add(getShbDate("HY全部", "Y", date, "", ""));
        list.add(getShbDate("HY采购中心", "Y", date, getWhereVdrnos("Y").toString(), ""));
        //计算合计指标
        Object[] o1 = new Object[]{"集团合计", BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO,
            BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO};
        for (int i = 1; i <= 13; i++) {
            for (int j = 0; j < list.size(); j = j + 2) {
                o1[i] = ((BigDecimal) o1[i]).add((BigDecimal) list.get(j)[i]);
            }
        }
        list.add(o1);
        System.out.print("集团合计");
        Object[] o2 = new Object[]{"采购中心合计", BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO,
            BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO};
        for (int i = 1; i <= 13; i++) {
            for (int j = 1; j < list.size(); j = j + 2) {
                o2[i] = ((BigDecimal) o2[i]).add((BigDecimal) list.get(j)[i]);
            }
        }
        list.add(o2);
        System.out.print("采购中心合计");
        //调整占比：各分公司占集团的百分比
        for (int j = 0; j < list.size(); j = j + 2) {
            try {
                BigDecimal value = ((BigDecimal) list.get(j)[13]).divide(((BigDecimal) list.get(list.size() - 2)[13]), 2).multiply(new BigDecimal(100));
                list.get(j)[14] = value.toString() + "%";
            } catch (ArithmeticException e) {
                list.get(j)[14] = "0%";
            }
        }
        for (int j = 1; j < list.size(); j = j + 2) {
            try {
                BigDecimal value = ((BigDecimal) list.get(j)[13]).divide(((BigDecimal) list.get(j - 1)[13]), 2).multiply(new BigDecimal(100));
                list.get(j)[14] = value.toString() + "%";
            } catch (ArithmeticException e) {
                list.get(j)[14] = "0%";
            }
        }
        return list;
    }

    //汉声和大陆的采购金额逻辑相同
    public Object[] getShbDate(String name, String facno, Date date, String vdrnos, String itcls) {
        Object[] row = new Object[15];
        row[0] = name;
        //循环获取12个月的数据
        StringBuffer sql = new StringBuffer();
        sql.append(" select CAST(right(yearmon,2) AS SIGNED) ,sum(acpamt)");
        sql.append(" from shoppingtable");
        sql.append(" where facno ='").append(facno).append("'");
        if (vdrnos != null && !"".equals(vdrnos)) {
            sql.append(" AND vdrno ").append(vdrnos);
        }
        if (itcls != null && !"".equals(itcls)) {
            sql.append(" AND itcls ").append(itcls);
        }
        sql.append(" and yearmon like '").append(String.valueOf(findYear(date))).append("%' group by yearmon order by yearmon ASC");
        Query query = shoppingManufacturerBean.getEntityManager().createNativeQuery(sql.toString());
        BigDecimal sum = BigDecimal.ZERO;
        try {
            List<Object[]> data = query.getResultList();
            for (int i = 1; i <= 12; i++) {
                if (i <= data.size()) {
                    row[i] = (java.math.BigDecimal) data.get(i - 1)[1];
                } else {
                    row[i] = new BigDecimal(0.0);
                }
                sum = sum.add((java.math.BigDecimal) row[i]);
            }
            row[13] = sum;
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
        return row;
    }

    /**
     * 按照年月分类获取重量
     *
     * @param name
     * @param facno
     * @param date
     * @param vdrnos
     * @param itcls
     * @return
     */
    public Object[] getGroupWeightDate(String name1,String name2, String facno, Date date, String vdrnos, String itcls) throws Exception {
        //先判断是否有漏的重量件号
        List list=getWeightDate(facno,date,vdrnos,itcls);
        if(list.size()>0){
            throw new Exception("未配置件号重量，请维护");
        }
        Object[] row = new Object[16];
        row[0] = name1;
         row[1] = name2;
        StringBuffer sql = new StringBuffer();
        sql.append(" select CAST(right(yearmon,2) AS SIGNED) ,sum(head.payqty*detail.weight)");
        sql.append(" from (select *");
        sql.append(" from shoppingtable where itnbr<>'9' and  facno='").append(facno).append("'");
        sql.append(" and yearmon like '").append(BaseLib.formatDate("yyyy", date)).append("%'");
        if (vdrnos != null && !"".equals(vdrnos)) {
            sql.append(" and vdrno").append(vdrnos);
        }
        if (itcls != null && !"".equals(itcls)) {
            sql.append(" and itcls").append(itcls);
        }
        sql.append(" ) head left join shoppingmenuweight detail on head.facno=detail.facno and head.itnbr=detail.itnbr where detail.id is not null");
        sql.append(" group by yearmon order by yearmon asc");
        Query query = shoppingManufacturerBean.getEntityManager().createNativeQuery(sql.toString());
        BigDecimal sum = BigDecimal.ZERO;
        try {
            List<Object[]> data = query.getResultList();
            for (int i = 1; i <= 12; i++) {
                if (i <= data.size()) {
                   
                    row[i+1] = (java.math.BigDecimal) data.get(i - 1)[1];
                } else {
                    row[i+1] = new BigDecimal(0.0);
                }
                sum = sum.add((java.math.BigDecimal) row[i+1]);
            }
            row[14] = sum;
        } catch (Exception e) {
            throw e;
        }
        return row;
    }

    public List<Object[]> getWeightDate(String facno, Date date, String vdrnos, String itcls) throws Exception {
        StringBuffer sql = new StringBuffer();
        sql.append(" select  head.facno,head.itnbr,head.itdsc");
        sql.append(" from (select *");
        sql.append(" from shoppingtable a where exists(select id from( select max(id) as id from shoppingtable where itnbr<>'9' and  facno='").append(facno).append("'");
        sql.append(" and yearmon like '").append(BaseLib.formatDate("yyyy", date)).append("%'");
        if (vdrnos != null && !"".equals(vdrnos)) {
            sql.append(" and vdrno ").append(vdrnos);
        }
        if (itcls != null && !"".equals(itcls)) {
            sql.append(" and itcls").append(itcls);
        }
        sql.append(" group by itnbr,facno)b where a.id=b.id)) head left join shoppingmenuweight detail on head.facno=detail.facno and head.itnbr=detail.itnbr where detail.id is  null");
        Query query = shoppingManufacturerBean.getEntityManager().createNativeQuery(sql.toString());
        BigDecimal sum = BigDecimal.ZERO;
        try {
            List<Object[]> data = query.getResultList();
            return data;
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    public List<Object[]> getThbDateDetail(String facno, Date date, String vdrnos, String itcls) {
        StringBuffer sql = new StringBuffer();
        sql.append(" select a.*,b.itcls,''");
        sql.append(" from (");
        sql.append(" select  'A'  as 'facno',apmpyh.vdrno , purvdr.vdrna , apmpyh.itnbr,apmpyh.itdsc, round(apmpyh.acpamt/4.4,2) as 'acpamt',apmpyh.payqty ");
        sql.append(" from apmpyh ,purvdr ");
        sql.append(" where purvdr.vdrno = apmpyh.vdrno ");
        sql.append(" and purkind not in ('9999') ");
        sql.append(" and apmpyh.pyhkind='1' and itnbr <> '9' ");
        if (vdrnos != null && !"".equals(vdrnos)) {
            sql.append(" AND apmpyh.vdrno ").append(vdrnos);
        }
        if (itcls != null && !"".equals(itcls)) {
            sql.append(" AND apmpyh.itcls ").append(itcls);
        }
        sql.append(" and year(apmpyh.trdat) =").append(String.valueOf(findYear(date)));
        sql.append(" and month(apmpyh.trdat) =").append(String.valueOf(findMonth(date)));
        sql.append("  )a left join invmas b on a.itnbr=b.itnbr");
        erpEJB.setCompany(facno);
        Query query = erpEJB.getEntityManager().createNativeQuery(sql.toString());
        List<Object[]> list = query.getResultList();
        return list;
    }

    public List<Object[]> getShbDateDetail(String facno, Date date, String vdrnos, String itcls) {
        StringBuffer sql = new StringBuffer();
        sql.append(" select a.*,b.itcls,''");
        sql.append(" from (");
        sql.append(" SELECT apmpyh.facno ,apmpyh.vdrno , purvdr.vdrna , apmpyh.itnbr,apmpyh.itdsc,  apmpyh.acpamt ,apmpyh.payqty");
        sql.append(" FROM apmpyh ,purvdr ,purhad");
        sql.append(" WHERE apmpyh.vdrno = purvdr.vdrno  and  purhad.facno = apmpyh.facno  and  purhad.prono = apmpyh.prono");
        sql.append(" and  purhad.pono = apmpyh.pono  and  apmpyh.pyhkind = '1'");
        sql.append(" AND apmpyh.facno =  '").append(facno).append("'  and apmpyh.prono ='1'");
        if (vdrnos != null && !"".equals(vdrnos)) {
            sql.append(" AND apmpyh.vdrno ").append(vdrnos);
        }
        if (itcls != null && !"".equals(itcls)) {
            sql.append(" AND apmpyh.itcls ").append(itcls);
        }
        sql.append(" and year(apmpyh.trdat) =").append(String.valueOf(findYear(date)));
        sql.append(" and month(apmpyh.trdat) =").append(String.valueOf(findMonth(date)));
        sql.append("  )a left join invmas b on a.itnbr=b.itnbr");
        erpEJB.setCompany(facno);
        Query query = erpEJB.getEntityManager().createNativeQuery(sql.toString());
        List<Object[]> list = query.getResultList();
        return list;
    }

    public StringBuffer getWhereVdrnos(String facno) {
        StringBuffer sql = new StringBuffer("");
        try {
            List<ShoppingManufacturer> list = shoppingManufacturerBean.findByFacno(facno);
            if (list == null || list.isEmpty()) {
                sql.append(" in ('') ");
                return sql;
            }
            sql.setLength(0);
            sql.append(" in (");
            for (ShoppingManufacturer m : list) {
                sql.append("'").append(m.getVdrno()).append("',");
            }
            sql.replace(sql.length() - 1, sql.length(), "").append(")");
            return sql;
        } catch (Exception e) {
            throw e;
        }
    }
}
