/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.hanbell.kpi.ejb;

import cn.hanbell.kpi.comm.SuperEJBForERP;
import cn.hanbell.kpi.entity.InventoryStatement;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.LocalBean;
import javax.persistence.Query;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author C1749
 */
@Stateless
@LocalBean
public class InventoryStatementBean implements Serializable {

    @EJB
    private SuperEJBForERP erpEJB;

    protected Logger log4j = LogManager.getLogger("cn.hanbell.kpi");

    public InventoryStatementBean() {

    }

    // type表示事业部 genre表示产品别 y 年 m 月
    public List<InventoryStatement> GetInventoryStatementList(String type, String genre, int y, int m) {
        List<InventoryStatement> InventoryStatementList = new ArrayList<>();
        if (!InventoryStatementList.isEmpty()) {
            InventoryStatementList.clear();
        }
        try {
            InventoryStatementList = addProportionValue(type, genre, y, m);
            Iterator<InventoryStatement> itr = InventoryStatementList.iterator();
            InventoryStatement ist = new InventoryStatement();
            ist.setWareh("合计");
            while (itr.hasNext()) {
                InventoryStatement it = itr.next();
                ist.setAmount1(it.getAmount1().add(ist.getAmount1() == null ? new BigDecimal(0) : ist.getAmount1()));
                ist.setAmount2(it.getAmount2().add(ist.getAmount2() == null ? new BigDecimal(0) : ist.getAmount2()));
                ist.setAmount3(it.getAmount3().add(ist.getAmount3() == null ? new BigDecimal(0) : ist.getAmount3()));
                ist.setDifference(it.getDifference().add(ist.getDifference() == null ? new BigDecimal(0) : ist.getDifference()));
                ist.setProportion(it.getProportion().add(ist.getProportion() == null ? new BigDecimal(0) : ist.getProportion()));
            }
            InventoryStatementList.add(ist);
        } catch (Exception ex) {
            log4j.error("GetInventoryStatementList()异常！", ex);
        }
        return InventoryStatementList;
    }

    //ERP取到数据集（没有最后一列占比值） 
    private List getAmtsList(String type, String genre, int y, int m) {
        StringBuilder sb = new StringBuilder();
        sb.append(" select a.whdsc, ");
        sb.append(" isnull(convert(DECIMAL(18,2),sum(CASE yearmon when ");
        sb.append("'").append(getYear3(y, m)).append(getMon3(m)).append("'");
        sb.append(" then amount end),0),0)  as amts1, ");
        sb.append(" isnull(convert(DECIMAL(18,2),sum(CASE yearmon when ");
        sb.append("'").append(getYear2(y, m)).append(getMon2(m)).append("'");
        sb.append(" then amount end),0),0) as amts2, ");
        sb.append(" isnull(convert(DECIMAL(18,2),sum(CASE yearmon when ");
        sb.append("'").append("${y}").append(getMon1(m)).append("'");
        sb.append(" then amount end),0),0) as amts3, ");
        sb.append(" isnull(convert(DECIMAL(18,2),(sum(CASE yearmon when ");
        sb.append("'").append("${y}").append(getMon1(m)).append("'");
        sb.append(" then amount end) - sum(CASE yearmon when ");
        sb.append("'").append(getYear2(y, m)).append(getMon2(m)).append("'");
        sb.append(" then amount end)),0),0) AS amt4 ");
        sb.append(" from invamount a LEFT OUTER JOIN invwhs w on w.facno = a.facno and w.prono = a.prono and w.wareh = a.wareh ");
        sb.append(" where w.costyn = 'Y' and  a.facno = 'C' and a.prono = '1' ");
        if (!type.equals("") && type != null) {
            switch (type) {
                case "SC":
                    sb.append("AND w.whcls  = 'A1'");
                    break;
                case "YY":
                    sb.append("AND w.whcls  = 'A2'");
                    break;
                case "FW":
                    sb.append("AND w.whcls  = 'A3'");
                    break;
                case "YF":
                    sb.append("AND w.whcls  = 'A6'");
                    break;
                default:
                    sb.append(" ");
                    break;
            }
        }
        if (!genre.equals("") && genre != null) {
            sb.append(" and a.genre = '").append(genre).append("'");
        }
        sb.append(" GROUP BY a.whdsc ");
        sb.append(" UNION ");
        //服务在制和生产在制的数据
        sb.append(" select a.whdsc, ");
        sb.append(" isnull(convert(DECIMAL(18,2),sum(CASE yearmon when ");
        sb.append("'").append(getYear3(y, m)).append(getMon3(m)).append("'");
        sb.append(" then amount end),0),0)  as amts1, ");
        sb.append(" isnull(convert(DECIMAL(18,2),sum(CASE yearmon when ");
        sb.append("'").append(getYear2(y, m)).append(getMon2(m)).append("'");
        sb.append(" then amount end),0),0) as amts2, ");
        sb.append(" isnull(convert(DECIMAL(18,2),sum(CASE yearmon when ");
        sb.append("'").append("${y}").append(getMon1(m)).append("'");
        sb.append(" then amount end),0),0) as amts3, ");
        sb.append(" isnull(convert(DECIMAL(18,2),(sum(CASE yearmon when ");
        sb.append("'").append("${y}").append(getMon1(m)).append("'");
        sb.append(" then amount end) - sum(CASE yearmon when ");
        sb.append("'").append(getYear2(y, m)).append(getMon2(m)).append("'");
        sb.append(" then amount end)),0),0) AS amt4 ");
        sb.append(" from invamount a LEFT OUTER JOIN invwhs w on w.facno = a.facno and w.prono = a.prono and w.wareh = a.wareh ");
        sb.append(" where a.facno = 'C' and a.prono = '1' ");
        //判断当前事业部性质
        if (!type.equals("")) {
            switch (type) {
                case "SC":
                    sb.append(" AND a.wareh = 'SCZZ' ");
                    break;
                case "FW":
                    sb.append(" AND a.wareh = 'FWZZ' ");
                    break;
                default:
                    sb.append(" AND a.wareh = 'YFZZ' ");
            }
        } else {
            sb.append(" and a.trtype in ('ZZ') ");
        }
        //取到产品别
        if (!genre.equals("") && genre != null) {
            sb.append(" and a.genre = '").append(genre).append("'");
        }
        sb.append(" GROUP BY a.whdsc ");
        //借厂商的数据归生产目标；借客户借员工的数据归服务目标
        if (!type.equals("")) {
            switch (type) {
                case "SC":
                    sb.append(" UNION ");
                    sb.append(" select a.whdsc, ");
                    sb.append(" isnull(convert(DECIMAL(18,2),sum(CASE yearmon when ");
                    sb.append("'").append(getYear3(y, m)).append(getMon3(m)).append("'");
                    sb.append(" then amount end),0),0)  as amts1, ");
                    sb.append(" isnull(convert(DECIMAL(18,2),sum(CASE yearmon when ");
                    sb.append("'").append(getYear2(y, m)).append(getMon2(m)).append("'");
                    sb.append(" then amount end),0),0) as amts2, ");
                    sb.append(" isnull(convert(DECIMAL(18,2),sum(CASE yearmon when ");
                    sb.append("'").append("${y}").append(getMon1(m)).append("'");
                    sb.append(" then amount end),0),0) as amts3, ");
                    sb.append(" isnull(convert(DECIMAL(18,2),(sum(CASE yearmon when ");
                    sb.append("'").append("${y}").append(getMon1(m)).append("'");
                    sb.append(" then amount end) - sum(CASE yearmon when ");
                    sb.append("'").append(getYear2(y, m)).append(getMon2(m)).append("'");
                    sb.append(" then amount end)),0),0) AS amt4 ");
                    sb.append(" from invamount a LEFT OUTER JOIN invwhs w on w.facno = a.facno and w.prono = a.prono and w.wareh = a.wareh ");
                    sb.append(" where w.costyn = 'Y' and a.facno = 'C' and a.prono = '1' ");
                    sb.append(" and a.whdsc LIKE ('借厂商%') ");
                    sb.append(" GROUP BY a.whdsc  ");
                    break;
                case "FW":
                    sb.append(" UNION ");
                    sb.append(" select a.whdsc, ");
                    sb.append(" isnull(convert(DECIMAL(18,2),sum(CASE yearmon when ");
                    sb.append("'").append(getYear3(y, m)).append(getMon3(m)).append("'");
                    sb.append(" then amount end),0),0)  as amts1, ");
                    sb.append(" isnull(convert(DECIMAL(18,2),sum(CASE yearmon when ");
                    sb.append("'").append(getYear2(y, m)).append(getMon2(m)).append("'");
                    sb.append(" then amount end),0),0) as amts2, ");
                    sb.append(" isnull(convert(DECIMAL(18,2),sum(CASE yearmon when ");
                    sb.append("'").append("${y}").append(getMon1(m)).append("'");
                    sb.append(" then amount end),0),0) as amts3, ");
                    sb.append(" isnull(convert(DECIMAL(18,2),(sum(CASE yearmon when ");
                    sb.append("'").append("${y}").append(getMon1(m)).append("'");
                    sb.append(" then amount end) - sum(CASE yearmon when ");
                    sb.append("'").append(getYear2(y, m)).append(getMon2(m)).append("'");
                    sb.append(" then amount end)),0),0) AS amt4 ");
                    sb.append(" from invamount a LEFT OUTER JOIN invwhs w on w.facno = a.facno and w.prono = a.prono and w.wareh = a.wareh ");
                    sb.append(" where w.costyn = 'Y' and a.facno = 'C' and a.prono = '1' ");
                    sb.append(" AND a.whdsc IN ('借客户','借员工') ");
                    sb.append(" GROUP BY a.whdsc  ");
                    break;
                default:
                    sb.append("  ");
            }
        }
        String sql = sb.toString().replace("${y}", String.valueOf(y)).replace("${m}", String.valueOf(m));
        try {
            Query query = erpEJB.getEntityManager().createNativeQuery(sql);
            List result = query.getResultList();
            return result;
        } catch (Exception ex) {
            log4j.error("getAmtsList1()异常！", ex);
        }
        return null;
    }

    //取到当月的合计值 算占比
    private BigDecimal getTotalAmts(String type, String genre, int y, int m) {
        List itsList = getAmtsList(type, genre, y, m);
        BigDecimal totalAmts = BigDecimal.ZERO;
        if (!itsList.isEmpty()) {
            for (int i = 0; i < itsList.size(); i++) {
                Object[] row = (Object[]) itsList.get(i);
                totalAmts = totalAmts.add((BigDecimal) row[3] == null ? new BigDecimal(0) : (BigDecimal) row[3]);
            }
        }
        return totalAmts;
    }

    //添加最后一列的占比值
    private List<InventoryStatement> addProportionValue(String type, String genre, int y, int m) {
        List<InventoryStatement> istmentList = new ArrayList<>();
        List itsList = getAmtsList(type, genre, y, m);
        InventoryStatement its;
        BigDecimal totalAmts = getTotalAmts(type, genre, y, m);
        try {
            if (!itsList.isEmpty()) {
                for (int i = 0; i < itsList.size(); i++) {
                    its = new InventoryStatement();
                    Object[] row = (Object[]) itsList.get(i);
                    its.setWareh(row[0].toString());
                    its.setAmount1((BigDecimal) row[1] == null ? new BigDecimal(0) : (BigDecimal) row[1]);
                    its.setAmount2((BigDecimal) row[2] == null ? new BigDecimal(0) : (BigDecimal) row[2]);
                    its.setAmount3((BigDecimal) row[3] == null ? new BigDecimal(0) : (BigDecimal) row[3]);
                    its.setDifference((BigDecimal) row[4] == null ? new BigDecimal(0) : (BigDecimal) row[4]);
                    if (totalAmts.compareTo(BigDecimal.ZERO) > 0) {
                        its.setProportion(((BigDecimal) row[3] == null ? new BigDecimal(0) : (BigDecimal) row[3]).divide(totalAmts, 2, BigDecimal.ROUND_HALF_UP));
                    } else {
                        its.setProportion(BigDecimal.ZERO);
                    }
                    istmentList.add(its);
                }
            }
            return istmentList;
        } catch (Exception ex) {
            log4j.error("addProportionValue()异常！", ex);
        }
        return null;
    }

    //当前月份 m
    private String getMon1(int m) {
        if (m < 10) {
            return "0" + m;
        }
        return String.valueOf(m);
    }

    //前一个月份 m
    private String getMon2(int m) {
        if ((m - 1) == 0) {
            m = 12;
        } else {
            m = m - 1;
        }
        if (m < 10) {
            return "0" + m;
        }
        return String.valueOf(m);
    }

    //前一个月份的年 y
    private String getYear2(int y, int m) {
        if ((m - 1) == 0) {
            y = y - 1;
        }
        return String.valueOf(y);
    }

    //前两个月份 m
    private String getMon3(int m) {
        switch (m - 2) {
            case 0:
                m = 12;
                break;
            case -1:
                m = 11;
                break;
            default:
                m = m - 2;
                break;
        }
        if (m < 10) {
            return "0" + m;
        }
        return String.valueOf(m);
    }

    //前两个月份的年份 y
    private String getYear3(int y, int m) {
        if ((m - 2) <= 0) {
            y = y - 1;
        }
        return String.valueOf(y);
    }

}
