/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.hanbell.kpi.evaluation;

import cn.hanbell.kpi.entity.MaterialQCUpload;
import com.lightshell.comm.BaseLib;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.Query;

/**
 *
 * @author C1749
 */
public class FeedstockTotalShipment extends FeedstockTotal {

    public FeedstockTotalShipment() {
    }

    @Override
    public BigDecimal getValue(int y, int m, Date d, int type, LinkedHashMap<String, Object> map) {
        String LINE = map.get("LINE") != null ? map.get("LINE").toString() : ""; //产品别
        FeedstockMaterialQCUpload FS = new FeedstockMaterialQCUpload();
        FeedstockRule FR = new FeedstockRule();
        String tSUPPLYID = "";
        String tSUPPLYNAME = "";
        String tITDSCFIELDS = "";
        String tITNBRFIELDS = "";
        int sum = 0;
        List<MaterialQCUpload> mqcuList = new ArrayList<>();//遍历厂商ID
        mqcuList = FS.getMqcuValue(LINE);
        for (int i = 0; i < mqcuList.size(); i++) {
            if (!mqcuList.isEmpty()) {
                tSUPPLYID = mqcuList.get(i).getSUPPLYID();//厂商规格表--ID
                tSUPPLYNAME = mqcuList.get(i).getSUPPLYNAME();//厂商--简称
                tITDSCFIELDS = mqcuList.get(i).getITDSCFIELDS();//厂商--件号规格
                tITNBRFIELDS = mqcuList.get(i).getITNBRFIELDS();//厂商--品名规则
                tITDSCFIELDS = "|止回阀;|关断阀;|一体阀;|耳阀;";
                String tITDSCFIELDSNew = FR.gettITDSCFIELDSNew(tSUPPLYID, tITDSCFIELDS, LINE);
                String tITNBRFIELDSNew = FR.gettITNBRFIELDSNew(tITNBRFIELDS);
                //件号筛选
                String ITDSCFIELDS = "";
                ITDSCFIELDS = FR.getITDSCFIELDS(tSUPPLYID, tITDSCFIELDSNew, tITDSCFIELDS, LINE);
                //品名筛选
                String ITNBRFIELDS = "";
                ITNBRFIELDS = FR.getITNBRFIELDS(tSUPPLYID, tITNBRFIELDSNew, tITNBRFIELDS, LINE);
                StringBuilder sb = new StringBuilder();
                BigDecimal avgShip = BigDecimal.ZERO;
                sb.append(" SELECT sum(A.accqy1) FROM puracd A,scminvmas B WHERE A.itnbr=B.itnbr ");
                sb.append(" AND SUBSTRING(A.itnbr,CHARINDEX('-',A.itnbr),3) not in ('-GB','-GA') ");
                if (!"".equals(tSUPPLYID.trim())) {
                    sb.append(" and A.vdrno in ('");
                    sb.append(tSUPPLYID);
                    sb.append("')");
                }
                if (!"".equals(ITNBRFIELDS)) {
                    sb.append(ITNBRFIELDS).append("  ");//品名规则
                }
                if (!"".equals(ITDSCFIELDS)) {
                    sb.append(ITDSCFIELDS).append("  ");//件号规则
                }
                sb.append(" and year(A.acceptdate) = ${y} and month(A.acceptdate)= ${m} ");
                switch (type) {
                    case 2:
                        //月
                        sb.append(" and A.acceptdate<= '${d}' ");
                        break;
                    case 5:
                        //日
                        sb.append(" and A.acceptdate= '${d}' ");
                        break;
                    default:
                        sb.append(" and A.acceptdate<= '${d}' ");
                }
                String sql = sb.toString().replace("${y}", String.valueOf(y)).replace("${m}", String.valueOf(m)).replace("${d}", BaseLib.formatDate("yyyyMMdd", d));
                Query query = superEJB.getEntityManager().createNativeQuery(sql);
                Object o = query.getSingleResult();
                sum += (int) o;
            }
        }
        try {
            return (BigDecimal.valueOf(sum));
        } catch (Exception ex) {
            Logger.getLogger(Shipment.class.getName()).log(Level.SEVERE, null, ex);
        }
        return BigDecimal.ZERO;
    }

}
