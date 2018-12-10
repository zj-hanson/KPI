/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.hanbell.kpi.evaluation;

import java.math.BigDecimal;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.Query;

/**
 *
 * @author C1749
 * 达标台数
 */
public class TrialRunAdverseAHReachmentA1 extends TrialRun {

    public TrialRunAdverseAHReachmentA1() {
        super();
    }

    @Override
    public BigDecimal getValue(int y, int m, Date d, int type, LinkedHashMap<String, Object> map) {
        String typecode = map.get("typecode") != null ? map.get("typecode").toString() : "";//机型
        StringBuilder sb = new StringBuilder();
        int count = 0;
        Double bglTRAVGVALUE = 0.0;
        Double zsTRAVGVALUE = 0.0;
        String TESTRUNITEMID = "";
        sb.append(" SELECT A.PRODUCTORDERID,A.PRODUCTCOMPID FROM PROCESS_TR A ");
        sb.append(" INNER JOIN MPRODUCT B ON A.PRODUCTID=B.PRODUCTID ");
        sb.append(" WHERE 1=1  ");
        if (!"".equals(typecode)) {
            if (typecode.contains("480")) {
                sb.append(" and (B.PRODUCTMODEL like 'AB-077%' or B.PRODUCTMODEL like 'AB-130%' or B.PRODUCTMODEL like 'AB-240%' or B.PRODUCTMODEL like 'AB-420%' or B.PRODUCTMODEL like 'AB-480R%') ");
            }
            if (typecode.contains("600")) {
                sb.append(" and (B.PRODUCTMODEL like 'AB-600R%' or B.PRODUCTMODEL like 'AAB-780R%') ");
            }
            if (typecode.contains("1030")) {
                sb.append(" and (B.PRODUCTMODEL like 'AB-1030R%' or B.PRODUCTMODEL like 'AB-1200R%') ");
            }
            if (typecode.contains("1320")) {
                sb.append(" and (B.PRODUCTMODEL like 'AB-1320%' or B.PRODUCTMODEL like 'AB-1560%' or B.PRODUCTMODEL like 'AB-1900R%' or B.PRODUCTMODEL like 'AB-2600%') ");
            }
        }
        sb.append(" AND A.STEPID LIKE '%机体试车站%'  ");
        sb.append(" AND year(A.MODIFYTIME)=${y} and month(A.MODIFYTIME)=${m} ");
        sb.append(" GROUP BY A.PRODUCTORDERID,A.PRODUCTCOMPID ");
        String varnrSql = sb.toString().replace("${y}", String.valueOf(y)).replace("${m}", String.valueOf(m));
        sb.setLength(0);
        Query varnrQuery = superEJB.getEntityManager().createNativeQuery(varnrSql);
        try {
            List varnrList = varnrQuery.getResultList();//制造号码集合
            if (!varnrList.isEmpty()) {
                for (int i = 0; i < varnrList.size(); i++) {
                    Object[] row = (Object[]) varnrList.get(i);
                    String PRODUCTORDERID = row[0].toString();
                    String PRODUCTCOMPID = row[1].toString();
                    sb.append(" SELECT TRAVGVALUE ,TESTRUNITEMID FROM PROCESS_TR_A_P A where 1=1  ");
                    if (!"".equals(PRODUCTORDERID)) {
                        sb.append(" AND PRODUCTORDERID = '").append(PRODUCTORDERID).append("'");
                    }
                    if (!"".equals(PRODUCTCOMPID)) {
                        sb.append(" and PRODUCTCOMPID = '").append(PRODUCTCOMPID).append("'");
                    }
                    sb.append(" and (TESTRUNITEMID  like '%比功率%' or TESTRUNITEMID like  '%被试机转速%') ");
                    sb.append(" AND year(A.MODIFYTIME)=${y} and month(A.MODIFYTIME)=${m} ");
                    sb.append(" ORDER BY SEQ ");
                    String bgvSql = sb.toString().replace("${y}", String.valueOf(y)).replace("${m}", String.valueOf(m));
                    Query bgvQuery = superEJB.getEntityManager().createNativeQuery(bgvSql);
                    sb.setLength(0);
                    List bgvList = bgvQuery.getResultList();
                    String[][] str = new String[bgvList.size()][];
                    if (!bgvList.isEmpty()) {
                        for (int k = 0; k < bgvList.size(); k++) {
                            Object[] s = (Object[]) bgvList.get(k);
                            str[k] = new String[s.length];
                            for (int l = 0; l < s.length; l++) {
                                str[k][l] = s[l].toString();
                            }
                        }
                        TESTRUNITEMID = str[0][1];
                        if (TESTRUNITEMID.contains("比功率")) {
                            bglTRAVGVALUE = Double.valueOf(str[0][0]);//比功率
                        }
                        TESTRUNITEMID = str[1][1];
                        if (TESTRUNITEMID.contains("转速")) {
                            zsTRAVGVALUE = Double.valueOf(str[1][0]);//转速
                        }
                        if (bglTRAVGVALUE <= 6.48 && zsTRAVGVALUE >= 2940 && zsTRAVGVALUE <= 2960) {
                            count++;
                        }
                    }
                }
            }

        } catch (Exception ex) {
            Logger.getLogger(TrialRunAdverseAHReachmentA1.class.getName()).log(Level.SEVERE, null, ex);
        }
        return BigDecimal.valueOf(Double.valueOf(count));
    }

}
