/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.hanbell.kpi.ejb;

import cn.hanbell.kpi.comm.SuperEJBForERP;
import cn.hanbell.kpi.comm.SuperEJBForKPI;
import cn.hanbell.kpi.entity.InventoryDepartment;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.Query;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author C1749
 */
@Stateless
@LocalBean
public class InventoryDepartmentBean extends SuperEJBForKPI<InventoryDepartment> {

    @EJB
    SuperEJBForERP superEJBForERP;

    protected LinkedHashMap<String, String> queryStringParams;

    protected final Logger log4j = LogManager.getLogger();

    public InventoryDepartmentBean() {
        super(InventoryDepartment.class);
        queryStringParams = new LinkedHashMap<>();
    }

    // 获取最终的库存List 给到KPI调用
    public List<InventoryDepartment> getInvamountDepartmentList(int y, int m,String facno) {
        queryStringParams.clear();
        queryStringParams.put("facno", "C");
        queryStringParams.put("prono", "1");
        List<InventoryDepartment> resultData = getDataForInventoryProductList(y, m, queryStringParams,facno);
        return resultData;
    }
    
    // 获取ERP的当前月的库存金额明细
    private List<InventoryDepartment> getDataForInventoryProductList(int y, int m, LinkedHashMap<String, String> map,String facno) {
        StringBuilder sb = new StringBuilder();
        List<InventoryDepartment> DataList = new ArrayList<>();
        InventoryDepartment ibu;
        sb.append(" select a.facno,substring(a.yearmon,1,4),a.wareh,a.whdsc,a.categories,a.genre,ifnull(sum(a.amount + a.amamount) ,0)  ");
        sb.append(" from inventoryproduct a  WHERE a.facno = '${facno}'  ");
        sb.append(" AND a.yearmon = '${y}${m}' ");
        sb.append(" GROUP BY a.facno,substring(a.yearmon,1,4),a.wareh,a.whdsc,a.categories,a.genre ");
        String sql = sb.toString().replace("${facno}", String.valueOf(facno)).replace("${y}", String.valueOf(y))
                .replace("${m}", String.valueOf(getMon(m)));
        try {
            Query query = this.getEntityManager().createNativeQuery(sql);
            List result = query.getResultList();
            if (result != null && !result.isEmpty()) {
                for (int i = 0; i < result.size(); i++) {
                    Object[] row = (Object[]) result.get(i);
                    facno = row[0] != null ? row[0].toString() : " ";
                    String creyear = row[1] != null ? row[1].toString() : " ";
                    String wareh = row[2] != null ? row[2].toString() : " ";
                    String whdsc = row[3] != null ? row[3].toString() : " ";
                    String categories = row[4] != null ? row[4].toString() : " ";
                    String genre = row[5] != null ? row[5].toString() : " ";
                    ibu = new InventoryDepartment(facno, "1", creyear, wareh, whdsc, categories, genre);
                    switch (m) {
                        case 1:
                            ibu.setN01(BigDecimal.valueOf(Double.valueOf(row[6].toString())));
                            break;
                        case 2:
                            ibu.setN02(BigDecimal.valueOf(Double.valueOf(row[6].toString())));
                            break;
                        case 3:
                            ibu.setN03(BigDecimal.valueOf(Double.valueOf(row[6].toString())));
                            break;
                        case 4:
                            ibu.setN04(BigDecimal.valueOf(Double.valueOf(row[6].toString())));
                            break;
                        case 5:
                            ibu.setN05(BigDecimal.valueOf(Double.valueOf(row[6].toString())));
                            break;
                        case 6:
                            ibu.setN06(BigDecimal.valueOf(Double.valueOf(row[6].toString())));
                            break;
                        case 7:
                            ibu.setN07(BigDecimal.valueOf(Double.valueOf(row[6].toString())));
                            break;
                        case 8:
                            ibu.setN08(BigDecimal.valueOf(Double.valueOf(row[6].toString())));
                            break;
                        case 9:
                            ibu.setN09(BigDecimal.valueOf(Double.valueOf(row[6].toString())));
                            break;
                        case 10:
                            ibu.setN10(BigDecimal.valueOf(Double.valueOf(row[6].toString())));
                            break;
                        case 11:
                            ibu.setN11(BigDecimal.valueOf(Double.valueOf(row[6].toString())));
                            break;
                        case 12:
                            ibu.setN12(BigDecimal.valueOf(Double.valueOf(row[6].toString())));
                            break;
                    }
                    ibu.setDifference(BigDecimal.ZERO);
                    ibu.setProportion(BigDecimal.ZERO);
                    DataList.add(ibu);
                }
            }
            return DataList;
        } catch (Exception ex) {
            System.out.println("在执行InventoryDepartmentBean.getDataForInventoryProductList()方法时失败！！！" + ex.toString());
        }
        return null;

    }
    
    // 为了判断当前表中是否存在相同数据
    private List<InventoryDepartment> findByPK(String facno, String prono, String creyear, String wareh, String whdsc,
            String categories, String genre) {
        Query query = this.getEntityManager().createNamedQuery("InventoryDepartment.findByPK");
        query.setParameter("facno", facno);
        query.setParameter("prono", prono);
        query.setParameter("creyear", creyear);
        query.setParameter("wareh", wareh);
        query.setParameter("whdsc", whdsc);
        query.setParameter("categories", categories);
        query.setParameter("genre", genre);
        try {
            return query.getResultList();
        } catch (Exception ex) {
            return null;
        }
    }

    // 更新数据到KPI的getInvamountDepartment表中
    public boolean updateInventoryDepartment(int y, int m,String facno) {
        List<InventoryDepartment> newList = getInvamountDepartmentList(y, m,facno);
        try {
            if (!newList.isEmpty()) {
                for (InventoryDepartment ib : newList) {
                    // 判断资料库是否存在相同的数据
                    facno = ib.getInventoryDepartmentPK().getFacno();
                    String prono = ib.getInventoryDepartmentPK().getProno();
                    String creyear = ib.getInventoryDepartmentPK().getCreyear();
                    String wareh = ib.getInventoryDepartmentPK().getWareh();
                    String whdsc = ib.getInventoryDepartmentPK().getWhdsc();
                    String categories = ib.getInventoryDepartmentPK().getCategories();
                    String genre = ib.getInventoryDepartmentPK().getGenre();
                    // 循环每一行数据 判断当前插入数据 数据库是否存在 如果存在就更新数据 不存在就插入新的数据行
                    List<InventoryDepartment> flagList = findByPK(facno, prono, creyear, wareh, whdsc, categories, genre);
                    if (!flagList.isEmpty() && flagList.size() > 0) {
                        InventoryDepartment a = flagList.get(0);
                        switch (m) {
                            case 1:
                                a.setN01(ib.getN01());
                                break;
                            case 2:
                                a.setN02(ib.getN02());
                                break;
                            case 3:
                                a.setN03(ib.getN03());
                                break;
                            case 4:
                                a.setN04(ib.getN04());
                                break;
                            case 5:
                                a.setN05(ib.getN05());
                                break;
                            case 6:
                                a.setN06(ib.getN06());
                                break;
                            case 7:
                                a.setN07(ib.getN07());
                                break;
                            case 8:
                                a.setN08(ib.getN08());
                                break;
                            case 9:
                                a.setN09(ib.getN09());
                                break;
                            case 10:
                                a.setN10(ib.getN10());
                                break;
                            case 11:
                                a.setN11(ib.getN11());
                                break;
                            case 12:
                                a.setN12(ib.getN12());
                                break;
                        }
                        // 如果存在就更新当前这一笔数据
                        this.update(a);
                    } else {
                        // 当前数据库不存在就插入该笔数据
                        this.persist(ib);
                    }
                }
                return true;
            }
        } catch (Exception ex) {
            System.out.println(ex.toString());
        }
        return false;
    }
    
    // 获取KPI的数据源 为KPI的系统最终显示的数据源
    private List getDataList(String type, String genre, int y, int m) {
        StringBuilder sb = new StringBuilder();
        switch (m) {
            //当月份为1月时，前两个月的数据为去年的最后两个月的数据
            case 1:
                sb.append(" select * from ( ");
                sb.append(" select whdsc, ");
                sb.append("0 as num1,");
                sb.append("0 as num2,");
                sb.append("sum(n").append(getMon(m)).append(") as num3 ");
                sb.append(sql1(type, genre, y, m));
                sb.append(" AND creyear = '${y}'");
                sb.append(" GROUP BY whdsc ");
                sb.append(" UNION all ");
                sb.append(" select whdsc, ");
                sb.append("sum(n").append(getMon(11)).append(") as num1, ");
                sb.append("sum(n").append(getMon(12)).append(") as num2, ");
                sb.append("0 as num3 ");
                sb.append(sql1(type, genre, y, m));
                sb.append(" AND creyear = '").append(y - 1).append("'");
                sb.append(" GROUP BY whdsc ");
                sb.append(" ) a ");
                // 服务在制和生产在制的数据
                if (!type.equals("N") && !type.equals("")) {
                    sb.append(" UNION ");
                    sb.append(" select * from ( ");
                    sb.append(" select whdsc, ");
                    sb.append("0 as num1,");
                    sb.append("0 as num2,");
                    sb.append("sum(n").append(getMon(m)).append(") as num3 ");
                    sb.append(sql2(type, genre, y, m));
                    sb.append(" AND creyear = '${y}'");
                    sb.append(" GROUP BY whdsc ");
                    sb.append(" UNION All ");
                    sb.append(" select whdsc, ");
                    sb.append("sum(n").append(getMon(11)).append(") as num1, ");
                    sb.append("sum(n").append(getMon(12)).append(") as num2, ");
                    sb.append("0 as num3 ");
                    sb.append(sql2(type, genre, y, m));
                    sb.append(" AND creyear = '").append(y - 1).append("'");
                    sb.append(" GROUP BY whdsc ");
                    sb.append(" ) b ");
                }
                // 借出总仓 借厂商的数据归生产目标；借客户借员工的数据归服务目标
                if (!type.equals("N") && !type.equals("")) {
                    switch (type) {
                        case "SC":
                            sb.append(" UNION ");
                            sb.append(" select * from ( ");
                            sb.append(" select whdsc, ");
                            sb.append("0 as num1,");
                            sb.append("0 as num2,");
                            sb.append("sum(n").append(getMon(m)).append(") as num3 ");
                            sb.append(sql3(type, genre, y, m));
                            sb.append(" AND creyear = '${y}'");
                            sb.append(" GROUP BY whdsc ");
                            sb.append(" UNION All ");
                            sb.append(" select whdsc, ");
                            sb.append("sum(n").append(getMon(11)).append(") as num1, ");
                            sb.append("sum(n").append(getMon(12)).append(") as num2, ");
                            sb.append("0 as num3 ");
                            sb.append(sql3(type, genre, y, m));
                            sb.append(" AND creyear = '").append(y - 1).append("'");
                            sb.append(" GROUP BY whdsc ");
                            sb.append(" ) c ");
                            break;
                        case "FW":
                            sb.append(" UNION ");
                            sb.append(" select * from ( ");
                            sb.append(" select whdsc, ");
                            sb.append("0 as num1,");
                            sb.append("0 as num2,");
                            sb.append("sum(n").append(getMon(m)).append(") as num3 ");
                            sb.append(sql3(type, genre, y, m));
                            sb.append(" AND creyear = '${y}'");
                            sb.append(" GROUP BY whdsc ");
                            sb.append(" UNION All ");
                            sb.append(" select whdsc, ");
                            sb.append("sum(n").append(getMon(11)).append(") as num1, ");
                            sb.append("sum(n").append(getMon(12)).append(") as num2, ");
                            sb.append("0 as num3 ");
                            sb.append(sql3(type, genre, y, m));
                            sb.append(" AND creyear = '").append(y - 1).append("'");
                            sb.append(" GROUP BY whdsc ");
                            sb.append(" ) c ");
                            break;
                    }
                }
                break;
            //当月份为2月时，第一个月份的数据为去年的最后一月的数据
            case 2:
                sb.append(" select * from ( ");
                sb.append(" select whdsc, ");
                sb.append("0 as num1,");
                sb.append("sum(n").append(getMon(m - 1)).append(") as num2, ");
                sb.append("sum(n").append(getMon(m)).append(") as num3 ");
                sb.append(sql1(type, genre, y, m));
                sb.append(" AND creyear = '${y}'");
                sb.append(" GROUP BY whdsc ");
                sb.append(" UNION all ");
                sb.append(" select whdsc, ");
                sb.append("sum(n").append(getMon(12)).append(") as num1, ");
                sb.append("0 as num2, ");
                sb.append("0 as num3 ");
                sb.append(sql1(type, genre, y, m));
                sb.append(" AND creyear = '").append(y - 1).append("'");
                sb.append(" GROUP BY whdsc ");
                sb.append(" ) a ");
                // 服务在制和生产在制的数据
                if (!type.equals("N") && !type.equals("")) {
                    sb.append(" UNION ");
                    sb.append(" select * from ( ");
                    sb.append(" select whdsc, ");
                    sb.append("0 as num1,");
                    sb.append("sum(n").append(getMon(m - 1)).append(") as num2, ");
                    sb.append("sum(n").append(getMon(m)).append(") as num3 ");
                    sb.append(sql2(type, genre, y, m));
                    sb.append(" AND creyear = '${y}'");
                    sb.append(" GROUP BY whdsc ");
                    sb.append(" UNION All ");
                    sb.append(" select whdsc, ");
                    sb.append("sum(n").append(getMon(12)).append(") as num1, ");
                    sb.append("0 as num2, ");
                    sb.append("0 as num3 ");
                    sb.append(sql2(type, genre, y, m));
                    sb.append(" AND creyear = '").append(y - 1).append("'");
                    sb.append(" GROUP BY whdsc ");
                    sb.append(" ) b ");
                }
                // 借出总仓 借厂商的数据归生产目标；借客户借员工的数据归服务目标
                if (!type.equals("N") && !type.equals("")) {
                    switch (type) {
                        case "SC":
                            sb.append(" UNION ");
                            sb.append(" select * from ( ");
                            sb.append(" select whdsc, ");
                            sb.append("0 as num1,");
                            sb.append("sum(n").append(getMon(m - 1)).append(") as num2, ");
                            sb.append("sum(n").append(getMon(m)).append(") as num3 ");
                            sb.append(sql3(type, genre, y, m));
                            sb.append(" AND creyear = '${y}'");
                            sb.append(" GROUP BY whdsc ");
                            sb.append(" UNION All ");
                            sb.append(" select whdsc, ");
                            sb.append("sum(n").append(getMon(12)).append(") as num1, ");
                            sb.append("0 as num2, ");
                            sb.append("0 as num3 ");
                            sb.append(sql3(type, genre, y, m));
                            sb.append(" AND creyear = '").append(y - 1).append("'");
                            sb.append(" GROUP BY whdsc ");
                            sb.append(" ) c ");
                            break;
                        case "FW":
                            sb.append(" UNION ");
                            sb.append(" select * from ( ");
                            sb.append(" select whdsc, ");
                            sb.append("0 as num1,");
                            sb.append("sum(n").append(getMon(m - 1)).append(") as num2, ");
                            sb.append("sum(n").append(getMon(m)).append(") as num3 ");
                            sb.append(sql3(type, genre, y, m));
                            sb.append(" AND creyear = '${y}'");
                            sb.append(" GROUP BY whdsc ");
                            sb.append(" UNION All ");
                            sb.append(" select whdsc, ");
                            sb.append("sum(n").append(getMon(12)).append(") as num1, ");
                            sb.append("0 as num2, ");
                            sb.append("0 as num3 ");
                            sb.append(sql3(type, genre, y, m));
                            sb.append(" AND creyear = '").append(y - 1).append("'");
                            sb.append(" GROUP BY whdsc ");
                            sb.append(" ) c ");
                            break;
                    }
                }
                break;
            default:
                sb.append(" select whdsc, ");
                sb.append("sum(n").append(getMon(m - 2)).append("),");
                sb.append("sum(n").append(getMon(m - 1)).append("),");
                sb.append("sum(n").append(getMon(m)).append(") ");
                sb.append(sql1(type, genre, y, m));
                sb.append(" AND creyear = '${y}'");
                sb.append(" GROUP BY whdsc ");
                // 服务在制和生产在制的数据
                if (!type.equals("N") && !type.equals("")) {
                    sb.append(" UNION ");
                    sb.append(" select whdsc, ");
                    sb.append("sum(n").append(getMon(m - 2)).append("),");
                    sb.append("sum(n").append(getMon(m - 1)).append("),");
                    sb.append("sum(n").append(getMon(m)).append(") ");
                    sb.append(sql2(type, genre, y, m));
                    sb.append(" AND creyear = '${y}'");
                    sb.append(" GROUP BY whdsc ");
                }
                // 借出总仓 借厂商的数据归生产目标；借客户借员工的数据归服务目标
                if (!type.equals("N") && !type.equals("")) {
                    switch (type) {
                        case "SC":
                            sb.append(" UNION ");
                            sb.append(" select whdsc, ");
                            sb.append("sum(n").append(getMon(m - 2)).append("),");
                            sb.append("sum(n").append(getMon(m - 1)).append("),");
                            sb.append("sum(n").append(getMon(m)).append(") ");
                            sb.append(sql3(type, genre, y, m));
                            sb.append(" AND creyear = '${y}'");
                            sb.append(" GROUP BY whdsc ");
                            break;
                        case "FW":
                            sb.append(" UNION ");
                            sb.append(" select whdsc, ");
                            sb.append("sum(n").append(getMon(m - 2)).append("),");
                            sb.append("sum(n").append(getMon(m - 1)).append("),");
                            sb.append("sum(n").append(getMon(m)).append(") ");
                            sb.append(sql3(type, genre, y, m));
                            sb.append(" AND creyear = '${y}'");
                            sb.append(" GROUP BY whdsc ");
                            break;
                    }
                }
                break;
        }
        String sql = sb.toString().replace("${y}", String.valueOf(y));
        Query query = this.getEntityManager().createNativeQuery(sql);
        try {
            List result = query.getResultList();
            return result;
        } catch (Exception ex) {
            log4j.error("InventoryDepartmentBean.getDataList()异常！", ex);
        }
        return null;
    }
    
    //柯茂 获取KPI的数据源 为KPI的系统最终显示的数据源
    private List getDataList_Comer(String type, String genre, int y, int m,String facno) {
        StringBuilder sb = new StringBuilder();
        switch (m) {
            //当月份为1月时，前两个月的数据为去年的最后两个月的数据
            case 1:
                sb.append(" select * from ( ");
                sb.append(" select whdsc, ");
                sb.append("0 as num1,");
                sb.append("0 as num2,");
                sb.append("sum(n").append(getMon(m)).append(") as num3 ");
                sb.append(" from inventorydepartment WHERE 1=1  ");
                sb.append(" AND creyear = '${y}' and facno = '${facno}' ");
                sb.append(" GROUP BY whdsc ");
                sb.append(" UNION all ");
                sb.append(" select whdsc, ");
                sb.append("sum(n").append(getMon(11)).append(") as num1, ");
                sb.append("sum(n").append(getMon(12)).append(") as num2, ");
                sb.append("0 as num3 ");
                sb.append(" from inventorydepartment WHERE 1=1 ");
                sb.append(" AND creyear = '").append(y - 1).append("'");
                sb.append(" AND facno = '${facno}'");
                sb.append(" GROUP BY whdsc ");
                sb.append(" ) a ");
            //当月份为2月时，第一个月份的数据为去年的最后一月的数据
            case 2:
                sb.append(" select * from ( ");
                sb.append(" select whdsc, ");
                sb.append("0 as num1,");
                sb.append("sum(n").append(getMon(m - 1)).append(") as num2, ");
                sb.append("sum(n").append(getMon(m)).append(") as num3 ");
                sb.append(" from inventorydepartment WHERE 1=1 ");
                sb.append(" AND creyear = '${y}' and facno = '${facno}' ");
                sb.append(" GROUP BY whdsc ");
                sb.append(" UNION all ");
                sb.append(" select whdsc, ");
                sb.append("sum(n").append(getMon(12)).append(") as num1, ");
                sb.append("0 as num2, ");
                sb.append("0 as num3 ");
                sb.append(" from inventorydepartment WHERE 1=1 ");
                sb.append(" AND creyear = '").append(y - 1).append("'");
                sb.append(" AND facno = '${facno}' ");
                sb.append(" GROUP BY whdsc ");
                sb.append(" ) a ");
            default:
                sb.append(" select whdsc, ");
                sb.append("sum(n").append(getMon(m - 2)).append("),");
                sb.append("sum(n").append(getMon(m - 1)).append("),");
                sb.append("sum(n").append(getMon(m)).append(") ");
                sb.append(" from inventorydepartment WHERE 1=1  ");
                sb.append(" AND creyear = '${y}'");
                sb.append(" AND facno = '${facno}' ");
                sb.append(" GROUP BY whdsc ");
                break;
        }
        String sql = sb.toString().replace("${y}", String.valueOf(y)).replace("${facno}", String.valueOf(facno));
        Query query = this.getEntityManager().createNativeQuery(sql);
        try {
            List result = query.getResultList();
            return result;
        } catch (Exception ex) {
            log4j.error("InventoryDepartmentBean.getDataList()异常！", ex);
        }
        return null;
    }
    

    // 取到当月的合计值 算占比
    private BigDecimal getTotalAmts(String type, String genre, int y, int m,List tempData) {
        //List itsList = getDataList(type, genre, y, m);
        BigDecimal totalAmts = BigDecimal.ZERO;
        if (!tempData.isEmpty()) {
            for (int i = 0; i < tempData.size(); i++) {
                Object[] row = (Object[]) tempData.get(i);
                totalAmts = totalAmts.add((BigDecimal) row[3] == null ? new BigDecimal(0) : (BigDecimal) row[3]);
            }
        }
        return totalAmts;
    }

    // 增加合计项和差异项和最后一行的合计 返回最终呈现的List
    public List<InventoryDepartment> getInventoryDepartmentResultList(String type, String genre, int y, int m,String facno) {
        List<InventoryDepartment> tempList = new ArrayList<>();
        List dataList = null;
        //根据公司别来显示来做不同处理
        if(facno.equals("C")){
            dataList = getDataList(type, genre, y, m);
        }else{
            dataList = getDataList_Comer(type, genre, y, m,facno);
        }
        BigDecimal totalAmts = getTotalAmts(type, genre, y, m,dataList);
        InventoryDepartment ibu;
        try {
            if (!dataList.isEmpty()) {
                for (int i = 0; i < dataList.size(); i++) {
                    Object[] row = (Object[]) dataList.get(i);
                    ibu = new InventoryDepartment("", "", "", "", row[0].toString(), "", "");
                    ibu.setN01((BigDecimal) row[1] == null ? new BigDecimal(0) : (BigDecimal) row[1]);
                    ibu.setN02((BigDecimal) row[2] == null ? new BigDecimal(0) : (BigDecimal) row[2]);
                    ibu.setN03((BigDecimal) row[3] == null ? new BigDecimal(0) : (BigDecimal) row[3]);
                    BigDecimal difference = ((BigDecimal) row[3] == null ? new BigDecimal(0) : (BigDecimal) row[3])
                            .subtract((BigDecimal) row[2] == null ? new BigDecimal(0) : (BigDecimal) row[2]);
                    ibu.setDifference(difference);
                    if (totalAmts.compareTo(BigDecimal.ZERO) > 0) {
                        ibu.setProportion((((BigDecimal) row[3] == null ? new BigDecimal(0) : (BigDecimal) row[3])
                                .divide(totalAmts, 5, BigDecimal.ROUND_UP).multiply(BigDecimal.valueOf(100))).setScale(3,
                                BigDecimal.ROUND_HALF_UP));
                    } else {
                        ibu.setProportion(BigDecimal.ZERO);
                    }
                    tempList.add(ibu);
                }
            }
            // 再添加最后一列合计项
            Iterator<InventoryDepartment> itr = tempList.iterator();
            InventoryDepartment ibu1 = new InventoryDepartment("", "", "", "", "合计", "", "");
            while (itr.hasNext()) {
                InventoryDepartment ibu2 = itr.next();
                ibu1.setN01(ibu2.getN01().add(ibu1.getN01() == null ? new BigDecimal(0) : ibu1.getN01()));
                ibu1.setN02(ibu2.getN02().add(ibu1.getN02() == null ? new BigDecimal(0) : ibu1.getN02()));
                ibu1.setN03(ibu2.getN03().add(ibu1.getN03() == null ? new BigDecimal(0) : ibu1.getN03()));
                ibu1.setDifference(
                        ibu2.getDifference().add(ibu1.getDifference() == null ? new BigDecimal(0) : ibu1.getDifference()));
                ibu1.setProportion(BigDecimal.valueOf(100));
            }
            tempList.add(ibu1);
            return tempList;
        } catch (Exception ex) {
            log4j.error("InventoryDepartmentBean.getInventoryDepartmentResultList()异常！", ex);
        }
        return null;
    }

    private String getMon(int m) {
        if (m < 10) {
            return "0" + m;
        }
        return String.valueOf(m);
    }

    public LinkedHashMap<String, String> getQueryStringParams() {
        return queryStringParams;
    }

    //封装代码 正常库存
    private String sql1(String type, String genre, int y, int m) {
        StringBuilder sb = new StringBuilder();
        sb.append(" from inventorydepartment WHERE 1=1  ");
        if (!type.equals("N") && !type.equals("")) {
            switch (type) {
                case "SC":
                    sb.append("AND categories  = 'A1'");
                    break;
                case "YY":
                    sb.append("AND categories  = 'A2'");
                    break;
                case "FW":
                    sb.append("AND categories  = 'A3'");
                    break;
                case "YF":
                    sb.append("AND categories  = 'A6'");
                    break;
                default:
                    sb.append(" ");
                    break;
            }
        }
        if (!genre.equals("N") && !genre.equals("")) {
            if (genre.equals("R")) {
                sb.append(" and genre in ('R','RG') ");
            } else {
                sb.append(" and genre = '").append(genre).append("'");
            }
        }
        return sb.toString();
    }

    //封装代码 服务在制和生产在制
    private String sql2(String type, String genre, int y, int m) {
        // 服务在制和生产在制的数据
        StringBuilder sb = new StringBuilder();
        sb.append(" from inventorydepartment WHERE 1=1  ");
        // 判断当前事业部性质
        switch (type) {
            case "SC":
                sb.append(" AND wareh = 'SCZZ' ");
                break;
            case "FW":
                sb.append(" AND wareh = 'FWZZ' ");
                break;
            default:
                sb.append(" AND wareh = 'YFZZ' ");
        }
        // 取到产品别
        if (!genre.equals("N") && !genre.equals("")) {
            if (genre.equals("R")) {
                sb.append(" and genre in ('R','RG') ");
            } else {
                sb.append(" and genre = '").append(genre).append("'");
            }
        }
        return sb.toString();
    }

    //封装代码 借出未归部分
    private String sql3(String type, String genre, int y, int m) {
        StringBuilder sb = new StringBuilder();
        sb.append(" from inventorydepartment WHERE 1=1  ");
        if (type.equals("SC")) {
            sb.append(" AND whdsc LIKE ('借厂商%') ");
        } else {
            sb.append(" AND whdsc IN ('借客户','借员工') ");
        }
        if (!genre.equals("")) {
            if (genre.equals("R")) {
                sb.append(" and genre in ('R','RG') ");
            } else {
                sb.append(" and genre = '").append(genre).append("'");
            }
        }
        return sb.toString();
    }

}
