/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.hanbell.kpi.evaluation;

import cn.hanbell.kpi.comm.SuperEJBForMES;
import cn.hanbell.kpi.entity.MaterialQCUpload;
import com.lightshell.comm.BaseLib;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.persistence.Query;

/**
 *
 * @author C1749 //QC和QJ不良
 */
public class FeedstockBadShipmentQC extends FeedstockBad {

    SuperEJBForMES superEJBForMES = lookupSuperEJBForMESBean();

    SuperEJBForMES lookupSuperEJBForMESBean() {
        try {
            Context c = new InitialContext();
            return (SuperEJBForMES) c.lookup("java:global/KPI/KPI-ejb/SuperEJBForMES!cn.hanbell.kpi.comm.SuperEJBForMES");
        } catch (NamingException ne) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "exception caught", ne);
            throw new RuntimeException(ne);
        }
    }

    @Override
    public BigDecimal getValue(int y, int m, Date d, int type, LinkedHashMap<String, Object> map) {
        String LINE = map.get("LINE") != null ? map.get("LINE").toString() : ""; //那个部门的物料
        String SOURCEDPIP = map.get("SOURCEDPIP") != null ? map.get("SOURCEDPIP").toString() : ""; //那个部门的物料  --冷媒%
        //厂商的数据集
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
            if (mqcuList.isEmpty()) {
                tSUPPLYID = mqcuList.get(i).getSUPPLYID();
                tSUPPLYNAME = mqcuList.get(i).getSUPPLYNAME();
                tITDSCFIELDS = mqcuList.get(i).getITDSCFIELDS();
                tITNBRFIELDS = mqcuList.get(i).getITNBRFIELDS();
                String tITDSCFIELDSNew = FR.gettITDSCFIELDSNew(tSUPPLYID, tITDSCFIELDS, LINE, 1);
                String tITNBRFIELDSNew = FR.gettITNBRFIELDSNew(tITNBRFIELDS);
                //品名件号筛选
                String ITDSCFIELDS = "";
                ITDSCFIELDS = FR.getITDSCFIELDS(tSUPPLYID, tITDSCFIELDSNew, tITDSCFIELDS, LINE);

                String ITNBRFIELDS = "";
                ITNBRFIELDS = FR.getITNBRFIELDS(tSUPPLYID, tITNBRFIELDSNew, tITNBRFIELDS, 1, LINE);
                StringBuilder sb = new StringBuilder();
                sb.append(" SELECT sum(D.DEFECTNUM)  FROM FLOW_FORM_UQF_S_NOW F ");
                sb.append(" LEFT JOIN ANALYSISRESULT_QCD D on F.PROJECTID=D.PROJECTID ");
                sb.append(" LEFT JOIN FLOW_PROJECT_HISTORY P on F.PROJECTID = P.PROJECTID ");
                sb.append(" INNER JOIN FLOW_FORM_UQF_COMP_NOW N on F.PROJECTID = N.PROJECTID and D.PROJECTID = N.PROJECTID and D.PRODUCTID =  N.PRODUCTID ");
                sb.append(" where P.PROCESSNODEID='UQFN0002' AND P.EFFECTIVEFLAG='Y' AND P.ADDFLAG='N' ");
                sb.append(" and F.FINALQCRESULT in  ('不合格')  ");
                sb.append(" and F.RESPONSIBILITYCOMPANY in (SELECT  B.SUPPLYNAME FROM RPT_MATERIALQCUPLOAD A LEFT JOIN MSTKSUPPLY B on A.SUPPLYID=B.SUPPLYID  ");
                if (!"".equals(LINE)) {
                    sb.append(" WHERE LINE = ").append(LINE);
                }
                sb.append(" ) ");
                if (!"".equals(SOURCEDPIP)) {
                    sb.append(" and F.SOURCEDPIP like  '%").append(SOURCEDPIP).append("%' ");
                }
                //sb.append(" and F.PROJECTCREATEUSERNAME in (SELECT USERNAME FROM  MPROCESSUSERGROUP_USER  WHERE GROUPID='GU001')  ");//QZ单过滤开单人
                sb.append(" and year(P.NODESTARTTIME) = ${y} and month(P.NODESTARTTIME)= ${m} ");
                switch (type) {
                    case 2:
                        //月
                        sb.append(" and P.NODESTARTTIME<= '${d}' ");
                        break;
                    case 5:
                        //日
                        sb.append(" and P.NODESTARTTIME= '${d}' ");
                        break;
                    default:
                        sb.append(" and P.NODESTARTTIME<= '${d}' )) ");
                }
                if (LINE.contains("冷媒")) {//特殊处理 嘉善二加一、缘牵 不考虑不合格单的判定结果
                    if (mqcuList.get(i).getSUPPLYID().equals("SZJ00304") || mqcuList.get(i).getSUPPLYID().equals("SSH00834")) {
                        sb.append(" AND F.RESPONSIBILITYCOMPANY LIKE '%").append(mqcuList.get(i).getSUPPLYNAME()).append("%' ");//厂商简称
                        if (!"".equals(ITNBRFIELDS)) {
                            sb.append(ITNBRFIELDS).append("  ");//品名规则
                        }
                        if (!"".equals(ITDSCFIELDS)) {
                            sb.append(ITDSCFIELDS).append("  ");//件号规则
                        }
                        sb.append(" AND D.PRODUCTID NOT LIKE  '%-GB%'  AND D.PRODUCTID NOT LIKE  '%-GA%' ");

                    } else {
                        sb.append(" AND F.RESPONSIBILITYCOMPANY LIKE '%").append(mqcuList.get(i).getSUPPLYNAME()).append("%' ");//厂商简称
                        sb.append(" AND F.ANALYSISJUDGEMENTRESULT NOT IN ('告知','特采')  AND SUBSTRING(F.PROJECTID,1,2)  in ('QC','QJ')  ");
                        if (!"".equals(ITNBRFIELDS)) {
                            sb.append(ITNBRFIELDS).append("  ");//品名规则
                        }
                        if (!"".equals(ITDSCFIELDS)) {
                            sb.append(ITDSCFIELDS).append("  ");//件号规则
                        }
                        sb.append(" AND D.PRODUCTID NOT LIKE  '%-GB%' AND D.PRODUCTID NOT LIKE  '%-GA%' ");
                    }
                } else if (LINE.contains("机体")) {//特殊处理 嘉善二加一、上海如春 不考虑不合格单的判定结果
                    if (mqcuList.get(i).getSUPPLYID().equals("SZJ00304") || mqcuList.get(i).getSUPPLYID().equals("SSH00557")) {
                        sb.append(" AND F.RESPONSIBILITYCOMPANY LIKE %").append(mqcuList.get(i).getSUPPLYNAME()).append("% ");//厂商简称
                        if (!"".equals(ITNBRFIELDS)) {
                            sb.append(ITNBRFIELDS);//品名规则
                        }
                        if (!"".equals(ITDSCFIELDS)) {
                            sb.append(ITDSCFIELDS).append("  ");//件号规则
                        }
                        sb.append(" AND D.PRODUCTID NOT LIKE  '%-GB%' AND D.PRODUCTID NOT LIKE  '%-GA%' ");
                    } else {
                        sb.append(" AND F.RESPONSIBILITYCOMPANY LIKE '%").append(mqcuList.get(i).getSUPPLYNAME()).append("%' ");//厂商简称
                        sb.append(" AND F.ANALYSISJUDGEMENTRESULT NOT IN ('告知','特采')  AND SUBSTRING(F.PROJECTID,1,2)  in ('QC','QJ') ");
                        if (!"".equals(ITNBRFIELDS)) {
                            sb.append(ITNBRFIELDS);//品名规则
                        }
                        if (!"".equals(ITDSCFIELDS)) {
                            sb.append(ITDSCFIELDS).append("  ");//件号规则
                        }
                        sb.append(" AND D.PRODUCTID NOT LIKE  '%-GB%' AND D.PRODUCTID NOT LIKE  '%-GA%'  ");
                    }
                }
                String sql = sb.toString().replace("${y}", String.valueOf(y)).replace("${m}", String.valueOf(m)).replace("${d}", BaseLib.formatDate("yyyyMMdd", d));
                Query query = superEJBForMES.getEntityManager().createNativeQuery(sql);
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
