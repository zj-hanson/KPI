/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.hanbell.kpi.evaluation;

import cn.hanbell.kpi.comm.SuperEJBForMES;
import cn.hanbell.kpi.entity.MaterialQCUpload;
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
 * @author C1749 FeedstockMaterialQCUpload
 */
public class FeedstockMaterialQCUpload extends FeedstockBad{

    public FeedstockMaterialQCUpload() {
        super();
    }
    SuperEJBForMES superEJBForMES = lookupSuperEJBForMESBean();

    //获取供应商ID数据集
    public List<MaterialQCUpload> getMqcuValue(String LINE) {
        StringBuilder sb = new StringBuilder();
        //把对象作为数据类型 
        List<MaterialQCUpload> list = new ArrayList<>();
        //查询当前已订好规则的供应商 获取ID数据集
        sb.append("SELECT A.SUPPLYID,B.SUPPLYNAME,A.ITDSCFIELDS,A.ITNBRFIELDS FROM RPT_MATERIALQCUPLOAD A LEFT JOIN MSTKSUPPLY B on A.SUPPLYID=B.SUPPLYID  WHERE  ");
        if (!"".equals(LINE)) {
            sb.append(" LINE = ").append(LINE);
        }
        String sql = sb.toString();
        Query query = superEJBForMES.getEntityManager().createNativeQuery(sql);
        try {
            List result = query.getResultList();
            if (result != null && !result.isEmpty()) {
                for (int i = 0; i < result.size(); i++) {
                    Object[] row = (Object[]) result.get(i);
                    MaterialQCUpload mqcu = new MaterialQCUpload();
                    mqcu.setSUPPLYID(row[0].toString());
                    mqcu.setSUPPLYNAME(row[1].toString());
                    mqcu.setITDSCFIELDS(row[2].toString());
                    mqcu.setITNBRFIELDS(row[3].toString());
                    list.add(mqcu);
                }
            }
            return list;
        } catch (Exception ex) {
            Logger.getLogger(Shipment.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    private SuperEJBForMES lookupSuperEJBForMESBean() {
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
        return BigDecimal.ZERO;
    }
}
