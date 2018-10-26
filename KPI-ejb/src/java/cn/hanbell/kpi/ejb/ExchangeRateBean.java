/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.hanbell.kpi.ejb;

import cn.hanbell.kpi.comm.SuperEJBForKPI;
import com.lightshell.comm.BaseLib;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.Query;
import cn.hanbell.kpi.entity.ExchangeRate;
import java.text.SimpleDateFormat;

/**
 *
 * @author C1879
 */
@Stateless
@LocalBean
public class ExchangeRateBean extends SuperEJBForKPI<ExchangeRate> {

    private final DecimalFormat df;
    private final DecimalFormat doubledf;

    public ExchangeRateBean() {
        super(ExchangeRate.class);
        this.df = new DecimalFormat("0.####");
        this.doubledf = new DecimalFormat("0.00％");
    }

    public boolean queryRateIsExist(ExchangeRate rate) {
        Query query = getEntityManager().createQuery(" SELECT e FROM ExchangeRate e WHERE e.rpttype = :rpttype and e.rateday = :rateday ");
        try {
            query.setParameter("rpttype", rate.getRpttype());
            query.setParameter("rateday", rate.getRateday());
            if (query.getResultList().isEmpty()) {
                return false;
            }
        } catch (Exception e) {
            System.out.println("cn.hanbell.kpi.ejb.ExchangeRateBean.queryRateIsExist()" + e);
        }
        return true;
    }

    public List<String[]> getlList(String queryCurrency, Date queryDateBegin, Date queryDateEnd) {
        List<String[]> list;
        StringBuilder sb = new StringBuilder();
        sb.append(" SELECT rateday,rate FROM  exchangerate where rpttype =${queryCurrency} ");
        sb.append(" and rateday >= '${queryDateBegin}' and  rateday <= '${queryDateEnd}' ORDER BY rateday ASC ");
        String sql = sb.toString().replace("${queryCurrency}", queryCurrency).replace("${queryDateBegin}", BaseLib.formatDate("yyyyMMdd", queryDateBegin)).replace("${queryDateEnd}", BaseLib.formatDate("yyyyMMdd", queryDateEnd));
        try {
            Query query = getEntityManager().createNativeQuery(sql);
            List result = query.getResultList();
            list = new ArrayList<>();
            int size = 1;
            //图表显示最多30条数据
            while (((double) result.size() / (double) size) > 30) {
                size = size * 2;
            }
            if (result != null && !result.isEmpty()) {
                for (int i = 0; i < result.size(); i = i + size) {
                    Object[] row = (Object[]) result.get(i);
                    String[] arr = new String[2];
                    arr[0] = new SimpleDateFormat("MM/dd").format(row[0]);
                    arr[1] = df.format(Double.parseDouble(row[1].toString()));
                    list.add(arr);
                    if (i + size >= result.size()) {
                        row = (Object[]) result.get(result.size() - 1);
                        arr = new String[2];
                        arr[0] = new SimpleDateFormat("MM/dd").format(row[0]);
                        arr[1] = df.format(Double.parseDouble(row[1].toString()));
                        list.add(arr);
                    }

                }
                getEntityManager().clear();
                return list;
            }
        } catch (Exception e) {
            System.out.println("cn.hanbell.kpi.ejb.ExchangeRateBean.getlList()" + e);
        }
        return null;
    }

    public LinkedHashMap getHashMap(String queryCurrency, Date queryDateBegin, Date queryDateEnd) {
        //标题兰name
        String title;
        //币种name
        String name;
        //环比时间
        String HBtime;
        //环比汇率
        String HBrate;
        //定比时间
        String DBtime;
        //定比汇率
        String DBrate;
        //环比、定比结束时间
        String Endtime;
        //环比、定比结束汇率
        String Endrate;
        //环比涨跌额
        String HBamount;
        //环比涨跌幅度
        String HBrange;
        //环比涨幅颜色；
        String HBcolor;
        //定比涨跌额
        String DBamount;
        //定比涨跌幅度
        String DBrange;
        //定比涨幅颜色
        String DBcolor;
        LinkedHashMap<String, String> map = new LinkedHashMap<>();

        List<String> hbList = getDateBegin(queryCurrency, queryDateBegin, queryDateEnd);
        List<String> endList = getDateEnd(queryCurrency, queryDateBegin, queryDateEnd);
        List<String> dbList = getDBdate(queryCurrency, queryDateBegin);
        if (hbList != null || endList != null) {
            try {
                title = getTitle(queryCurrency);
                name = dbList.get(0);
                DBtime = dbList.get(1);
                DBrate = df.format(Double.valueOf(dbList.get(2)));
                HBtime = hbList.get(0);
                HBrate = df.format(Double.valueOf(hbList.get(1)));
                Endtime = endList.get(0);
                Endrate = df.format(Double.valueOf(endList.get(1)));
                //环比
                // 涨跌额=当前选定的结束日期的金额 — 当前选定的开始日期的金额
                // 涨跌幅度=涨跌额/当前选定的开始日期的金额*100
                HBamount = df.format(BigDecimal.valueOf(Double.parseDouble(Endrate)).subtract(BigDecimal.valueOf(Double.parseDouble(HBrate))));
                HBrange = doubledf.format((Double.parseDouble(Endrate) - Double.parseDouble(HBrate)) / Double.parseDouble(HBrate) * 100);
                HBcolor = (Double.parseDouble(Endrate) - Double.parseDouble(HBrate)) < 0 ? "green" : "red";
                //定比
                //涨跌额=当前选定结束日期的金额 — 当前年度第一天正常班制的的对应金额
                //涨跌幅度=涨跌额 /  当前年度第一天正常班制的的对应金额*100
                DBamount = df.format(BigDecimal.valueOf(Double.parseDouble(Endrate)).subtract(BigDecimal.valueOf(Double.parseDouble(DBrate))));
                DBrange = doubledf.format((Double.parseDouble(Endrate) - Double.parseDouble(DBrate)) / Double.parseDouble(DBrate) * 100);
                DBcolor = Double.parseDouble(Endrate) - Double.parseDouble(DBrate) < 0 ? "green" : "red";
                map.put("title", title);
                map.put("name", name);
                map.put("DBtime", DBtime);
                map.put("DBrate", DBrate);
                map.put("DBamount", DBamount);
                map.put("DBrange", DBrange);
                map.put("HBtime", HBtime);
                map.put("HBrate", HBrate);
                map.put("HBamount", HBamount);
                map.put("HBrange", HBrange);
                map.put("Endtime", Endtime);
                map.put("Endrate", Endrate);
                map.put("HBcolor", HBcolor);
                map.put("DBcolor", DBcolor);
                return map;
            } catch (Exception e) {
                System.out.println("cn.hanbell.kpi.ejb.ExchangeRateBean.getHashMap():" + e);
            }
        }
        return null;
    }

    //得出table标题
    private String getTitle(String queryCurrency) {
        String title;
        switch (queryCurrency) {
            case "1":
                title = "美金兑换人民币汇率";
                break;
            case "2":
                title = "欧元兑换人民币汇率";
                break;
            case "3":
                title = "日元兑换人民币汇率";
                break;
            case "4":
                title = "人民币兑换台币汇率";
                break;
            case "5":
                title = "国际黄金价格走势";
                break;
            case "6":
                title = "美金兑换日元汇率";
                break;
            case "7":
                title = "欧元兑换美金汇率";
                break;
            case "8":
                title = "美金兑换台币汇率";
                break;
            default:
                title = "";
        }
        return title;
    }

    //得到定比兑换名称、定比开始时间、兑换率
    private List<String> getDBdate(String queryCurrency, Date queryDateBegin) {
        Calendar c = Calendar.getInstance();
        c.setTime(queryDateBegin);
        List<String> list = new ArrayList<>();

        StringBuilder sb = new StringBuilder();
        sb.append(" SELECT exchangena,rateday,rate FROM  exchangerate where rpttype =${queryCurrency} ");
        sb.append(" and year(rateday) = ${y}  ORDER BY rateday ASC LIMIT 1 ");
        String sql = sb.toString().replace("${queryCurrency}", queryCurrency).replace("${y}", String.valueOf(c.get(Calendar.YEAR)));
        try {
            Query query = getEntityManager().createNativeQuery(sql);
            List result = query.getResultList();
            if (result != null && !result.isEmpty()) {
                for (int i = 0; i < result.size(); i++) {
                    Object[] row = (Object[]) result.get(i);
                    list.add(row[0].toString());
                    list.add(new SimpleDateFormat("yyyy/MM/dd").format(row[1]));
                    list.add(row[2].toString());
                }
                return list;
            }
        } catch (Exception e) {
            System.out.println("cn.hanbell.kpi.ejb.ExchangeRateBean.getDBdate()" + e);
        }
        return null;
    }

    //得到环比、定比结束时间、兑换率
    private List<String> getDateEnd(String queryCurrency, Date queryDateBegin, Date queryDateEnd) {
        List<String> list = new ArrayList<>();

        StringBuilder sb = new StringBuilder();
        sb.append(" SELECT  rateday,rate FROM  exchangerate where rpttype =${queryCurrency} ");
        sb.append(" and rateday >= '${queryDateBegin}' and  rateday <= '${queryDateEnd}'  ORDER BY rateday DESC LIMIT 1 ");
        String sql = sb.toString().replace("${queryCurrency}", queryCurrency).replace("${queryDateBegin}", BaseLib.formatDate("yyyyMMdd", queryDateBegin)).replace("${queryDateEnd}", BaseLib.formatDate("yyyyMMdd", queryDateEnd));
        try {
            Query query = getEntityManager().createNativeQuery(sql);
            List result = query.getResultList();
            if (result != null && !result.isEmpty()) {
                for (int i = 0; i < result.size(); i++) {
                    Object[] row = (Object[]) result.get(i);
                    list.add(new SimpleDateFormat("yyyy/MM/dd").format(row[0]));
                    list.add(row[1].toString());
                }
                return list;
            }
        } catch (Exception e) {
            System.out.println("cn.hanbell.kpi.ejb.ExchangeRateBean.getDateEnd()" + e);
        }
        return null;
    }

    //得到环比开始时间、兑换率
    private List<String> getDateBegin(String queryCurrency, Date queryDateBegin, Date queryDateEnd) {
        List<String> list = new ArrayList<>();

        StringBuilder sb = new StringBuilder();
        sb.append(" SELECT  rateday,rate FROM  exchangerate where rpttype =${queryCurrency} ");
        sb.append(" and rateday >= '${queryDateBegin}' and  rateday <= '${queryDateEnd}'  ORDER BY rateday ASC LIMIT 1 ");
        String sql = sb.toString().replace("${queryCurrency}", queryCurrency).replace("${queryDateBegin}", BaseLib.formatDate("yyyyMMdd", queryDateBegin)).replace("${queryDateEnd}", BaseLib.formatDate("yyyyMMdd", queryDateEnd));
        try {
            Query query = getEntityManager().createNativeQuery(sql);
            List result = query.getResultList();
            if (result != null && !result.isEmpty()) {
                for (int i = 0; i < result.size(); i++) {
                    Object[] row = (Object[]) result.get(i);
                    list.add(new SimpleDateFormat("yyyy/MM/dd").format(row[0]));
                    list.add(row[1].toString());
                }
                return list;
            }
        } catch (Exception e) {
            System.out.println("cn.hanbell.kpi.ejb.ExchangeRateBean.getDateBegin()" + e);
        }
        return null;
    }

}
