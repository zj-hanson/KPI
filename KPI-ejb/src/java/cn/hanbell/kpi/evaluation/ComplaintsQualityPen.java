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
 * @author C1749
 */
public class ComplaintsQualityPen extends ComplaintsKS {

    //当月有效客诉
    @Override
    public BigDecimal getValue(int y, int m, Date d, int type, LinkedHashMap<String, Object> map) {
        //定义变量属性 获得查询参数
        String BQ197 = map.get("BQ197") != null ? map.get("BQ197").toString() : ""; //产品别
        String BQ003 = map.get("BQ003") != null ? map.get("BQ003").toString() : ""; //品號類別代號
        String BQ134 = map.get("BQ134") != null ? map.get("BQ134").toString() : ""; //冷媒特殊处理 不算BQ134为-1的情况
        String BQ110 = map.get("BQ110") != null ? map.get("BQ110").toString() : ""; //是否客诉
        int ksship = 0;
        StringBuilder sb = new StringBuilder();
//        sb.append("select  count(BQ001)  from SERBQ");
//        sb.append(" LEFT JOIN  (SELECT SERCA.CA001,CA009, CASE WHEN datediff(DAY,REPMI.MI009,CONVERT(VARCHAR(8),GETDATE(),112))>0 THEN 'N' ELSE 'Y' END AS MI017");
//        sb.append(" FROM SERCA AS SERCA ");
//        sb.append(" LEFT JOIN WARMB WARMB ON rtrim(MB001) = rtrim(CA003) ");
//        sb.append(" LEFT JOIN REPMQ REPMQ ON rtrim(MQ001) = rtrim(CA010) AND MQ003='a1'");
//        sb.append(" LEFT JOIN SERAC SERAC ON rtrim(AC001) = rtrim(CA019) ");
//        sb.append(" LEFT JOIN REPTA REPTA ON rtrim(REPTA.TA001) = rtrim(SERCA.CA010) AND rtrim(REPTA.TA002) = rtrim(SERCA.CA011)");
//        sb.append(" LEFT JOIN REPMI REPMI ON rtrim(MI002) = rtrim(CA009) ) a ON rtrim(a.CA001)=rtrim(SERBQ.BQ001) ");
//        sb.append(" where BQ197 = '${BQ197}' AND BQ130 ='${BQ130}' and MI017 = '${MI017}' and BQ035 = '${BQ035}' and BQ110 = '${BQ110}'");
        sb.append("select count(a.CA009) as num from (  ");
        sb.append(" select  DISTINCT CA009,BQ001 from SERBQ,SERCA where  BQ001= CA001 and  BQ197 like '${BQ197}'  ");
        if (!"".equals(BQ003)) {
            sb.append(" and BQ003 ").append(BQ003);
        }
        if (!"".equals(BQ134)) {
            sb.append(" and BQ505 ").append(BQ134);
        }
        if (!"".equals(BQ110)) {
            sb.append(" and BQ110 ").append(BQ110);
        }
        sb.append(" and year(BQ021) = '${y}' and month(BQ021)= '${m}' ");
        switch (type) {
            case 2:
                //月
                sb.append(" and BQ021 <= '${d}' ");
                break;
            case 5:
                //日
                sb.append(" and BQ021 = '${d}' ");
                break;
            default:
                sb.append(" and BQ021 <= '${d}' ");
        }
        sb.append(") as a ");
        String sql = sb.toString().replace("${y}", String.valueOf(y)).replace("${m}", String.valueOf(m)).replace("${d}", BaseLib.formatDate("yyyyMMdd", d))
                .replace("${BQ197}", BQ197);

        Query query = superEJBForCRM.getEntityManager().createNativeQuery(sql);
        try {
            Object o = query.getSingleResult();
            ksship = (int) o;
            return BigDecimal.valueOf(ksship);
        } catch (Exception e) {
        }
        return BigDecimal.ZERO;
    }

}
