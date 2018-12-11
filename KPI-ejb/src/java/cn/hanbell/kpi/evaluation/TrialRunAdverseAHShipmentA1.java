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
 * 机体出货台数
 */
public class TrialRunAdverseAHShipmentA1 extends TrialRun {

    public TrialRunAdverseAHShipmentA1() {
        super();
    }

    @Override
    public BigDecimal getValue(int y, int m, Date d, int type, LinkedHashMap<String, Object> map) {
        String facno = map.get("facno") != null ? map.get("facno").toString() : "";
        String typecode = map.get("typecode") != null ? map.get("typecode").toString() : "";//机型
        StringBuilder sb = new StringBuilder();
        int count = 0;
        Double pqTRAVGVALUE = 0.0;
        Double zsTRAVGVALUE = 0.0;
        String TESTRUNITEMID = "";
        TrialRunAdverseAHGetERPShipment trah = new TrialRunAdverseAHGetERPShipment();
        try {
            List varnrList = trah.getVarnrList(y, m, d, type, map);//循环ERP里的制造号码到MES的试车记录做判定
            if (!varnrList.isEmpty()) {
                for (int i = 0; i < varnrList.size(); i++) {
                    String varnr = varnrList.get(i).toString();
                    sb.append(" SELECT TRAVGVALUE ,TESTRUNITEMID FROM PROCESS_TR_A_P A where 1=1  ");
                    if (!"".equals(varnr)) {
                        sb.append(" and PRODUCTCOMPID = '").append(varnr).append("'");
                    }
                    sb.append(" and (TESTRUNITEMID  like '%排气压力%' or TESTRUNITEMID like  '%被试机转速%') ");
                    //sb.append(" AND year(A.MODIFYTIME)=${y} and month(A.MODIFYTIME)=${m} ");
                    //sb.append(" ORDER BY SEQ ");
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
                        if (TESTRUNITEMID.contains("排气压力")) {
                            pqTRAVGVALUE = Double.valueOf(str[0][0]);//比功率
                        }
                        TESTRUNITEMID = str[1][1];
                        if (TESTRUNITEMID.contains("转速")) {
                            zsTRAVGVALUE = Double.valueOf(str[1][0]);//转速
                        }
                        if ((pqTRAVGVALUE>=0.78 && pqTRAVGVALUE <= 8.08) && zsTRAVGVALUE >= 2940 && zsTRAVGVALUE <= 2960) {
                            count++;
                        }
                    }
                }
            }
        } catch (Exception ex) {
            Logger.getLogger(TrialRunAdverseAHShipmentA1.class.getName()).log(Level.SEVERE, null, ex);
        }
        return BigDecimal.valueOf(Double.valueOf(count));
    }

}
