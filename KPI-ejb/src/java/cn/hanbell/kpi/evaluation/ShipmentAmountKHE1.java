/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.hanbell.kpi.evaluation;

import com.lightshell.comm.BaseLib;
import java.math.BigDecimal;
import java.util.Date;
import java.util.LinkedHashMap;
import javax.persistence.Query;

/**
 *
 * @author C0160
 */
public class ShipmentAmountKHE1 extends ShipmentAmount {

    public ShipmentAmountKHE1() {
        super();
        queryParams.put("facno", "K");
        queryParams.put("deptno", " '5B000' ");
        queryParams.put("ogdkid", "RL01");
        queryParams.put("n_code_DA", " ='OH' ");
        queryParams.put("n_code_DC", " ='HE' ");
        queryParams.put("n_code_DD", "  IN ('00','02') ");
    }

    @Override
    public BigDecimal getARM270Value(int y, int m, Date d, int type, LinkedHashMap<String, Object> map) {
        BigDecimal rq51, rq11;
        String facno = map.get("facno") != null ? map.get("facno").toString() : "";
        String deptno = map.get("deptno") != null ? map.get("deptno").toString() : "";
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT ISNULL(SUM(h.shpamt),0) FROM armbil h WHERE h.rkd='RQ51' AND h.facno='${facno}' AND h.depno IN (${deptno}) ");
        sb.append(" AND year(h.bildat) = ${y} AND month(h.bildat)= ${m} ");
        switch (type) {
            case 2:
                //月
                sb.append(" AND h.bildat<= '${d}' ");
                break;
            case 5:
                //日
                sb.append(" AND h.bildat= '${d}' ");
                break;
            default:
                sb.append(" AND h.bildat<= '${d}' ");
        }
        String sqlstr = sb.toString().replace("${y}", String.valueOf(y)).replace("${m}", String.valueOf(m)).replace("${d}", BaseLib.formatDate("yyyyMMdd", d)).replace("${facno}", facno).replace("${deptno}", deptno);
        superEJB.setCompany(facno);
        Query query = superEJB.getEntityManager().createNativeQuery(sqlstr);
        try {
            // 关于柯茂北京中矿之芦南芦北项目分期收款，期限为三年，每季度为1期，共12期（2018年4月至2021年3月），调整逻辑抓取金额/(1+7%*3)
            //2019年6月6日 财务周建芳调整此部分逻辑
            Object o = query.getSingleResult();
            double a = Double.parseDouble(o.toString()) / 1.21;
            rq51 = BigDecimal.valueOf(a);
            rq11 = super.getARM270Value(y, m, d, type, map);
            return rq51.add(rq11);
        } catch (Exception ex) {
            log4j.error(ex);
        }
        return BigDecimal.ZERO;
    }

    @Override
    public BigDecimal getValue(int y, int m, Date d, int type, LinkedHashMap<String, Object> map) {
        BigDecimal temp1, temp2;
        //ComerERP
        temp1 = super.getValue(y, m, d, type, map);
        queryParams.remove("facno");
        queryParams.remove("deptno");
        queryParams.put("facno", "E");
        queryParams.put("deptno", " '8A000' ");
        //ZJComerERP
        temp2 = super.getValue(y, m, d, type, queryParams);
        //ComerERP + ZJComerERP
        return temp1.add(temp2);
    }

}
