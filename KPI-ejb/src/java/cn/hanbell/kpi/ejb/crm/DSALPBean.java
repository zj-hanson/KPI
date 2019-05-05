/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.hanbell.kpi.ejb.crm;

import cn.hanbell.kpi.comm.SuperEJBForERP;
import cn.hanbell.kpi.entity.crm.DSALP;
import com.lightshell.comm.BaseLib;
import com.lightshell.comm.SuperEJB;
import java.io.Serializable;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author C1879
 */
@Stateless
@LocalBean
public class DSALPBean extends SuperEJB<DSALP> {

    @PersistenceContext(unitName = "CRM-ejbPU")
    private EntityManager em;

    @EJB
    private SuperEJBForERP erpEJB;

    public DSALPBean() {
        super(DSALP.class);
    }

    public List<DSALP> findByParams(String type, String userid, String DA, Date date) throws Exception {
        try {
            SimpleDateFormat sf = new SimpleDateFormat("yyyyMMdd");
            String dateend = sf.format(date);
            Calendar cale = Calendar.getInstance();
            cale.setTime(date);
            cale.set(Calendar.DAY_OF_MONTH, 1);
            String datebegin = sf.format(cale.getTime());
            Query query = em.createNamedQuery("DSALP.findByParams");
            query.setParameter("type", type);  //数据类型 Shipment出货 SalesOrder订单
            query.setParameter("userid", userid);//业务员
            query.setParameter("DA", DA);//事业别
            query.setParameter("datebegin", datebegin);
            query.setParameter("dateend", dateend);
            List<DSALP> list = query.getResultList();
            return list;
        } catch (Exception e) {
            System.out.println("cn.hanbell.crm.ejb.DsalpBean.queryDsalyList()" + e);
        }
        return null;
    }

    public boolean queryDsalpIsExist(Date date, LinkedHashMap<String, Object> map) {
        try {
            String facno = map.get("facno") != null ? map.get("facno").toString() : "";
            String type = map.get("type") != null ? map.get("type").toString() : "";
            String userid = map.get("userid") != null ? map.get("userid").toString() : "";
            String n_code_DA = map.get("n_code_DA") != null ? map.get("n_code_DA").toString() : "";
            String n_code_DC = map.get("n_code_DC") != null ? map.get("n_code_DC").toString() : "";

            StringBuilder sb = new StringBuilder();
            sb.append(" SELECT DS002,DS003,DS004,DS005,DS006,DS007,DS008,DS009,DS010,DS011,DS012,DS015,DS016,DS017,DS019 FROM DSALP WHERE DS002='${facno}' ");
            sb.append(" AND DS003='${type}' and DS005='${userid}' ");
            if (!"".equals(n_code_DA)) {
                sb.append(" and DS015 ").append(n_code_DA);
            }
            if (!"".equals(n_code_DC)) {
                sb.append(" and DS016 ").append(n_code_DC);
            }
            sb.append(" and DS006 ='${d}'");
            String sql = sb.toString().replace("${facno}", facno).replace("${type}", type).replace("${userid}", userid).replace("${d}", BaseLib.formatDate("yyyyMMdd", date));
            Query query = em.createNativeQuery(sql);
            List list = query.getResultList();
            if (list.isEmpty()) {
                return true;
            }
        } catch (Exception e) {
            System.out.println("cn.hanbell.crm.ejb.DsalpBean.queryDsalpIsExist()" + e);
        }
        return false;
    }

    //依指标自动更新进入此方法
    public void addDsalpList(int y, int m, Date date, LinkedHashMap<String, Object> map) throws Exception {
        String n_code_DA = map.get("n_code_DA") != null ? map.get("n_code_DA").toString() : "";
        try {
            if (n_code_DA.contains("R")) {
                if (queryDsalpIsExist(date, map)) {
                    List<DSALP> list = getDsalpList(y, m, date, map);
                    if (list != null && !list.isEmpty()) {
                        for (DSALP dsalp : list) {
                            em.persist(dsalp);
                        }
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("cn.hanbell.crm.ejb.DsalpBean.addDsalpList()" + e);
        }
    }

    //手动更新数据进入此方法
    public boolean addDsalpList(List<DSALP> deleteList, List<DSALP> addList) throws Exception {
        try {
            if (deleteList != null && !deleteList.isEmpty()) {
                delete(deleteList);
            }
            if (addList != null && !addList.isEmpty()) {
                for (DSALP dsalp : addList) {
                    persist(dsalp);
                }
            }
            return true;
        } catch (Exception e) {
            System.out.println("cn.hanbell.crm.ejb.DsalpBean.addDsalpList()" + e);
        }
        return false;
    }

    public Calendar getdate(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        return c;
    }

    public List<DSALP> getShipmentListSum(Date date, String da, LinkedHashMap<String, Object> map) {
        List<DSALP> list = new ArrayList<>();
        if ("".equals(da.trim()) || "R".equals(da.trim())) {
            map.put("facno", "C,C4,N,G,J");
            map.put("deptno", " ('1B000','1C000','1D000','1E000','1V000') ");
            map.put("n_code_DA", " ='R' ");
            map.put("ogdkid", " IN ('RL01') ");
            list = getDsalpList(getdate(date).get(Calendar.YEAR), getdate(date).get(Calendar.MONTH) + 1, date, map);
        }

        return list;
    }

    public List<DSALP> getSalesOrderListSum(Date date, String da, LinkedHashMap<String, Object> map) {
        List<DSALP> list = new ArrayList<>();
        if ("".equals(da.trim()) || "R".equals(da.trim())) {
            map.put("facno", "C,C4,N,G,J");
            map.put("n_code_DA", " ='R' ");
            list = getDsalpList(getdate(date).get(Calendar.YEAR), getdate(date).get(Calendar.MONTH) + 1, date, map);
        }
        return list;
    }

    public List<DSALP> getDsalpList(int y, int m, Date date, LinkedHashMap<String, Object> map) {
        String facno = map.get("facno") != null ? map.get("facno").toString() : "";
        String type = map.get("type") != null ? map.get("type").toString() : "";
        String[] arr = facno.split(",");
        List<DSALP> returnlist = new ArrayList<>();
        List list = new ArrayList();
        DSALP st;
        try {
            for (String arr1 : arr) {
                //出货数据部分
                if ("Shipment".equals(type.trim())) {
                    list = getShipment(y, m, date, map, arr1);
                }
                //订单数据部分
                if ("SalesOrder".equals(type.trim())) {
                    list = getSalesOrder(y, m, date, map, arr1);
                }
                if (list != null && !list.isEmpty()) {
                    for (int i = 0; i < list.size(); i++) {
                        st = new DSALP();
                        Object[] row = (Object[]) list.get(i);
                        st.setDs002(row[0].toString());//公司别
                        st.setDs003(type);//单据类型
                        st.setDs004(row[1].toString());//区域
                        st.setDs005(row[2].toString());//业务员no
                        st.setDs006(row[3].toString());//单据日期 yyyyMMdd
                        st.setDs007(row[4].toString());//机型
                        st.setDs008(row[5].toString());//客户代号
                        st.setDs009(row[6].toString());//客户名称
                        st.setDs010(row[7].toString());//部门
                        st.setDs011(BigDecimal.valueOf(Double.parseDouble(row[8].toString())));//台数
                        st.setDs012(BigDecimal.valueOf(Double.parseDouble(row[9].toString())));//金额
                        st.setDs015(row[10].toString());//事业部别
                        st.setDs016(row[11].toString());//产品别
                        st.setDs017(row[12].toString());//整机/零件
                        st.setDs019(row[13].toString());//业务员姓名
                        returnlist.add(st);
                    }
                }
            }
            return returnlist;
        } catch (Exception e) {
            System.out.println("cn.hanbell.crm.ejb.DsalpUpdateBean.getDsalpList()" + e);
            return null;
        }

    }

    private List getSalesOrder(int y, int m, Date date, LinkedHashMap<String, Object> map, String facno) {
        String userid = map.get("userid") != null ? map.get("userid").toString() : "";
        String status = map.get("status") != null ? map.get("status").toString() : "";
        String n_code_DA = map.get("n_code_DA") != null ? map.get("n_code_DA").toString() : "";
        StringBuilder sb = new StringBuilder();

        sb.append(" SELECT facno ,n_code_CD,mancode,convert(NVARCHAR(8),cdrdate,112),itnbrcus,a.cusno,s.cusna,depno,quantity,amount ");
        sb.append(" ,n_code_DA,n_code_DC,n_code_DD,e.username AS manname FROM ( select d.facno,itnbrcus,h.cusno,h.recdate AS cdrdate,depno, ");
        sb.append(" isnull(sum(d.cdrqy1),0) AS quantity,isnull(convert(decimal(16,6),sum((d.tramts*h.ratio)/(h.taxrate+1))),0) as amount, ");
        sb.append(" d.n_code_DA,d.n_code_CD,d.n_code_DC,d.n_code_DD,mancode from cdrdmas d inner join cdrhmas h on h.facno=d.facno and h.cdrno=d.cdrno ");
        sb.append(" WHERE  isnull(h.hmark2,'') <> 'FW' AND h.hrecsta <> 'W' AND h.cusno not in ('SSD00107','SGD00088','SJS00254','SCQ00146') and  h.facno='${facno}' ");
        if (!"".equals(userid)) {
            sb.append(" and h.mancode ='").append(userid).append("' ");
        }
        if (!"".equals(n_code_DA)) {
            sb.append(" and d.n_code_DA ").append(n_code_DA);
        }
        sb.append(" and d.n_code_DD='00' and year(h.recdate) = ${y} and month(h.recdate)= ${m} ");
        //status 1 代表人工更新
        if (!"1".equals(status)) {
            sb.append(" and h.recdate= '${d}' ");
        }
        sb.append(" GROUP BY d.facno,itnbrcus,h.cusno,h.recdate,depno,d.n_code_DA,d.n_code_CD,d.n_code_DC,d.n_code_DD ,mancode ");
        sb.append(" union all ");
        sb.append(" select d.facno,itnbrcus,h.cusno,h.recdate AS cdrdate,depno,isnull(-sum(d.cdrqy1),0) AS quantity, ");
        sb.append(" isnull(convert(decimal(16,6),-sum((d.tramts*h.ratio)/(h.taxrate+1))),0) as amount,d.n_code_DA,d.n_code_CD,d.n_code_DC,d.n_code_DD,mancode ");
        sb.append(" from cdrdmas d inner join cdrhmas h on h.facno=d.facno and h.cdrno=d.cdrno ");
        sb.append(" WHERE  isnull(h.hmark2,'') <> 'FW' AND h.hrecsta <> 'W' AND h.cusno not in ('SSD00107','SGD00088','SJS00254','SCQ00146') and  h.facno='${facno}' ");
        sb.append(" and d.drecsta in ('98','99','10') ");
        if (!"".equals(userid)) {
            sb.append(" and h.mancode ='").append(userid).append("' ");
        }
        if (!"".equals(n_code_DA)) {
            sb.append(" and d.n_code_DA ").append(n_code_DA);
        }
        sb.append(" and d.n_code_DD='00' and year(h.recdate) = ${y} and month(h.recdate)= ${m} ");
        //status 1 代表人工更新
        if (!"1".equals(status)) {
            sb.append(" and h.recdate<= '${d}' AND convert(NVARCHAR(8),d.enddate,112)='${d}' ");
        } else {
            sb.append(" and month(d.enddate)= ${m} ");
        }
        sb.append(" GROUP BY d.facno,itnbrcus,h.cusno,h.recdate,depno,d.n_code_DA,d.n_code_CD,d.n_code_DC,d.n_code_DD ,mancode ");
        sb.append(" ) a,cdrcus s ,secuser e WHERE a.cusno=s.cusno AND a.mancode=e.userno ORDER BY cdrdate ASC ");

        String sql = sb.toString().replace("${facno}", facno).replace("${y}", String.valueOf(y)).replace("${m}", String.valueOf(m)).replace("${d}", BaseLib.formatDate("yyyyMMdd", date));

        try {
            erpEJB.setCompany(facno);
            Query query = erpEJB.getEntityManager().createNativeQuery(sql);
            List result = query.getResultList();
            return result;
        } catch (Exception e) {
            System.out.println("cn.hanbell.crm.ejb.DsalpUpdateBean.getSalesOrder()" + e);
            return null;
        }
    }

    private List getShipment(int y, int m, Date date, LinkedHashMap<String, Object> map, String facno) {
        String deptno = map.get("deptno") != null ? map.get("deptno").toString() : "";
        String ogdkid = map.get("ogdkid") != null ? map.get("ogdkid").toString() : "";
        String userid = map.get("userid") != null ? map.get("userid").toString() : "";
        String status = map.get("status") != null ? map.get("status").toString() : "";
        String n_code_DA = map.get("n_code_DA") != null ? map.get("n_code_DA").toString() : "";
        String n_code_DD = map.get("n_code_DD") != null ? map.get("n_code_DD").toString() : "";
        StringBuilder sb = new StringBuilder();

        sb.append("SELECT facno ,n_code_CD,mancode,convert(NVARCHAR(8),cdrdate,112),itnbrcus,a.cusno,s.cusna,depno, ");
        sb.append("quantity,amount,n_code_DA,n_code_DC,n_code_DD,e.username AS manname FROM ( ");
        //第一部分为整机出货销退
        sb.append(" select h.facno,itnbrcus,h.cusno,h.shpdate AS cdrdate,depno, ");
        sb.append(" isnull(sum(CASE  when d.n_code_DA='AA' AND left(d.itnbr,1)='3' THEN shpqy1 when d.n_code_DA!='AA' THEN shpqy1 ELSE 0 END ),0) as quantity, ");
        sb.append(" isnull(convert(decimal(16,6),sum((d.shpamts * h.ratio)/(h.taxrate + 1))),0) as amount,d.n_code_DA,d.n_code_CD,d.n_code_DC,d.n_code_DD ");
        sb.append(" ,mancode from cdrdta d left join cdrhad h on d.shpno=h.shpno where h.facno='${facno}' and h.houtsta <> 'W'");
        sb.append(" and h.cusno NOT IN ('SSD00107','SGD00088','SJS00254','SCQ00146') and d.issevdta='N' and d.n_code_DD ='00' ");
        if (!"".equals(userid)) {
            sb.append(" and h.mancode ='").append(userid).append("' ");
        }
        if (!"".equals(n_code_DA)) {
            sb.append(" and d.n_code_DA ").append(n_code_DA);
        }
        sb.append(" AND  year(h.shpdate)=${y} AND month(h.shpdate)=${m} ");
        if (!"1".equals(status)) {
            sb.append(" and h.shpdate= '${d}' ");
        }
        sb.append(" group by  h.facno,itnbrcus,h.cusno,h.shpdate,depno,d.n_code_DA,d.n_code_CD,d.n_code_DC,d.n_code_DD,mancode ");
        sb.append(" union all ");
        sb.append("  select h.facno,itnbrcus,h.cusno,h.bakdate AS cdrdate,depno,");
        sb.append(" isnull(-sum(CASE  when d.n_code_DA='AA' AND left(d.itnbr,1)='3' THEN bshpqy1 when d.n_code_DA!='AA' THEN bshpqy1 ELSE 0 END ),0)quantity, ");
        sb.append(" isnull(convert(decimal(16,6),-sum((d.bakamts * h.ratio)/(h.taxrate + 1))),0) as amount,d.n_code_DA,d.n_code_CD,d.n_code_DC,d.n_code_DD ");
        sb.append(" ,mancode  from cdrbdta d left join cdrbhad h on  h.bakno=d.bakno  where h.baksta <> 'W'  and h.facno='${facno}'");
        sb.append(" and h.cusno NOT IN ('SSD00107','SGD00088','SJS00254','SCQ00146') and d.issevdta='N' and d.n_code_DD ='00' ");
        if (!"".equals(userid)) {
            sb.append(" and h.mancode ='").append(userid).append("' ");
        }
        if (!"".equals(n_code_DA)) {
            sb.append(" and d.n_code_DA ").append(n_code_DA);
        }
        sb.append(" AND  year(h.bakdate)=${y} AND month(h.bakdate)=${m} ");
        if (!"1".equals(status)) {
            sb.append(" and h.bakdate= '${d}' ");
        }
        sb.append(" group by  h.facno,itnbrcus,h.cusno,h.bakdate,depno,d.n_code_DA,d.n_code_CD,d.n_code_DC,d.n_code_DD,mancode ");

        //如果算入后处理金额则单独处理，只算金额不算台数
        if (n_code_DD.contains("'02'")) {
            sb.append(" select h.facno,itnbrcus,h.cusno,h.shpdate AS cdrdate,depno,0 as quantity, ");
            sb.append(" isnull(convert(decimal(16,6),sum((d.shpamts * h.ratio)/(h.taxrate + 1))),0) as amount,d.n_code_DA,d.n_code_CD,d.n_code_DC,d.n_code_DD ");
            sb.append(" ,mancode from cdrdta d left join cdrhad h on d.shpno=h.shpno where h.facno='${facno}' and h.houtsta <> 'W'");
            sb.append(" and h.cusno NOT IN ('SSD00107','SGD00088','SJS00254','SCQ00146') and d.issevdta='N' ");
            if (!"".equals(userid)) {
                sb.append(" and h.mancode ='").append(userid).append("' ");
            }
            if (!"".equals(n_code_DA)) {
                sb.append(" and d.n_code_DA ").append(n_code_DA);
            }
            sb.append(" and d.n_code_DD ='02'");
            sb.append(" AND  year(h.shpdate)=${y} AND month(h.shpdate)=${m} ");
            if (!"1".equals(status)) {
                sb.append(" and h.shpdate= '${d}' ");
            }
            sb.append(" group by  h.facno,itnbrcus,h.cusno,h.shpdate,depno,d.n_code_DA,d.n_code_CD,d.n_code_DC,d.n_code_DD,mancode ");
            sb.append(" union all ");
            sb.append("  select h.facno,itnbrcus,h.cusno,h.bakdate AS cdrdate,depno,0 as quantity,");
            sb.append(" isnull(convert(decimal(16,6),-sum((d.bakamts * h.ratio)/(h.taxrate + 1))),0) as amount,d.n_code_DA,d.n_code_CD,d.n_code_DC,d.n_code_DD ");
            sb.append(" ,mancode  from cdrbdta d left join cdrbhad h on  h.bakno=d.bakno  where h.baksta <> 'W'  and h.facno='${facno}'");
            sb.append(" and h.cusno NOT IN ('SSD00107','SGD00088','SJS00254','SCQ00146') and d.issevdta='N'");
            if (!"".equals(userid)) {
                sb.append(" and h.mancode ='").append(userid).append("' ");
            }
            if (!"".equals(n_code_DA)) {
                sb.append(" and d.n_code_DA ").append(n_code_DA);
            }
            sb.append(" and d.n_code_DD d.n_code_DD ='02'");
            sb.append(" AND  year(h.bakdate)=${y} AND month(h.bakdate)=${m} ");
            if (!"1".equals(status)) {
                sb.append(" and h.bakdate= '${d}' ");
            }
            sb.append(" group by  h.facno,itnbrcus,h.cusno,h.bakdate,depno,d.n_code_DA,d.n_code_CD,d.n_code_DC,d.n_code_DD,mancode ");
        }
        //getARM232Value 加扣款单独列出来 只算金额不算台数
        sb.append(" union all ");
        sb.append(" SELECT h.facno,itnbrcus,h.cusno,h.trdat AS cdrdate ,d.depno,0 AS quantity, ");
        sb.append(" ISNULL(SUM(CASE h.amtco WHEN 'P' THEN d.psamt WHEN 'M' THEN d.psamt *(-1) ELSE 0 END),0) AS amount ");
        sb.append(" ,s.n_code_DA,s.n_code_CD,s.n_code_DC,s.n_code_DD,mancode  FROM armpmm h,armacq d,cdrdta s,cdrhad c ");
        sb.append(" WHERE h.facno=d.facno AND h.trno = d.trno AND d.facno = s.facno  AND d.shpno=s.shpno AND d.shpseq = s.trseq AND s.shpno=c.shpno ");
        if (!"".equals(userid)) {
            sb.append(" and c.mancode ='").append(userid).append("' ");
        }
        if (!"".equals(n_code_DA)) {
            sb.append(" and s.n_code_DA ").append(n_code_DA);
        }
        if (n_code_DD.contains("'02'")) {
            sb.append(" AND (s.n_code_DD  ='00'  or s.n_code_DD d.n_code_DD ='02'");
        } else {
            sb.append("  AND s.n_code_DD  ='00' ");
        }
        sb.append(" and year(h.trdat) = ${y}  and month(h.trdat) = ${m} ");
        if (!"1".equals(status)) {
            sb.append(" and h.trdat= '${d}' ");
        }
        sb.append(" group by  h.facno,itnbrcus,h.cusno,h.trdat,d.depno,s.n_code_DA,s.n_code_CD,s.n_code_DC,s.n_code_DD,mancode ");

        //getARM423Value 折让
        sb.append(" union all ");
        sb.append(" SELECT h.facno,'ARM423' as itnbrcus,d.ivocus AS 'cusno',h.recdate AS cdrdate,h.depno,0 AS quantity, ");
        sb.append(" ISNULL(sum(d.recamt),0) AS amount ,h.n_code_DA,h.n_code_CD,h.n_code_DC,h.n_code_DD,mancode ");
        sb.append(" FROM armrec d,armrech h where d.facno=h.facno AND d.recno=h.recno ");
        sb.append(" AND h.prgno='ARM423' AND h.recstat='1' AND d.raccno IN ('6001','6002') ");
        sb.append(" AND h.ogdkid  ").append(ogdkid);
        if (!"".equals(userid)) {
            sb.append(" and d.mancode ='").append(userid).append("' ");
        }
        if (!"".equals(n_code_DA)) {
            sb.append(" and h.n_code_DA ").append(n_code_DA);
        }
        if (n_code_DD.contains("'02'")) {
            sb.append(" AND (h.n_code_DD  ='00'  or h.n_code_DD d.n_code_DD ='02'");
        } else {
            sb.append("  AND h.n_code_DD  ='00' ");
        }
        sb.append(" and year(h.recdate) = ${y}   and month(h.recdate) =${m} ");
        if (!"1".equals(status)) {
            sb.append(" and h.recdate= '${d}' ");
        }
        sb.append(" GROUP BY h.facno,d.ivocus,h.recdate,h.depno,h.n_code_DA,h.n_code_CD,h.n_code_DC,h.n_code_DD,mancode ");

        //getARM270Value 其它项金额 关联部门
        if (!"".equals(deptno)) {
            sb.append(" union all ");
            sb.append(" SELECT h.facno,'' as itnbrcus,h.cusno,h.bildat AS cdrdate ,h.depno,0 AS quantity,ISNULL(sum(h.shpamt),0) AS amount, ");
            sb.append(getDA(deptno)).append(" as n_code_DA, ");
            sb.append(" '' as  n_code_CD,");
            sb.append(getDC(deptno)).append(" as n_code_DC, ");
            sb.append(" '00' as  n_code_DD,mancode FROM armbil h WHERE 1=1 ");
            if (deptno.contains("5B000")) {
                sb.append(" and h.rkd in ('RQ51','RQ11') ");
            } else {
                sb.append(" and h.rkd='RQ11' ");
            }
            if (!"".equals(userid)) {
                sb.append(" and h.mancode ='").append(userid).append("' ");
            }
            sb.append(" AND h.depno in ").append(deptno);
            sb.append(" and year(h.bildat) = ${y}   and month(h.bildat) =${m} ");
            if (!"1".equals(status)) {
                sb.append(" and h.bildat= '${d}' ");
            }
            sb.append(" group by  h.facno,h.cusno,h.depno,h.bildat,mancode ");
        }
        sb.append(" ) a,cdrcus s ,secuser e WHERE a.cusno=s.cusno AND a.mancode=e.userno ORDER BY cdrdate ASC ");

        String sql = sb.toString().replace("${facno}", facno).replace("${y}", String.valueOf(y)).replace("${m}", String.valueOf(m)).replace("${d}", BaseLib.formatDate("yyyyMMdd", date));

        try {
            erpEJB.setCompany(facno);
            Query query = erpEJB.getEntityManager().createNativeQuery(sql);
            List result = query.getResultList();
            return result;
        } catch (Exception e) {
            System.out.println("cn.hanbell.crm.ejb.DsalpUpdateBean.getShipment()" + e);
            return null;
        }
    }

    private String getDA(String depno) {
        String aa = "'ARM270'";
        if (depno.contains("1B")) {
            aa = "'R'";
        }
        if (depno.contains("1C")) {
            aa = "'R'";
        }
        if (depno.contains("1D")) {
            aa = "'R'";
        }
        if (depno.contains("1E")) {
            aa = "'R'";
        }
        if (depno.contains("1V")) {
            aa = "'R'";
        }
        if (depno.contains("1Q")) {
            aa = "'AA'";
        }
        if (depno.contains("1G")) {
            aa = "'AH'";
        }
        if (depno.contains("1H")) {
            aa = "'P'";
        }
        if (depno.contains("1U")) {
            aa = "'S'";
        }
        if (depno.contains("5A")) {
            aa = "'RT'";
        }
        if (depno.contains("5B")) {
            aa = "'OH'";
        }
        if (depno.contains("5C")) {
            aa = "'RT'";
        }
        return aa;
    }

    private String getDC(String depno) {
        String aa = "'ARM270'";
        if (depno.contains("1G1")) {
            aa = "'AJ'";
        }
        if (depno.contains("1G5")) {
            aa = "'SDS'";
        }
        return aa;
    }

    @Override
    public EntityManager getEntityManager() {
        return em;
    }

}
