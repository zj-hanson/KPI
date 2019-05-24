/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tw.hanbell.kpi.ejb.erp;

import cn.hanbell.kpi.comm.SuperEJBForERP;
import cn.hanbell.kpi.ejb.erp.GroupShipmentBean;
import tw.hanbell.kpi.entity.erp.Grpsdailytmp;
import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;

/**
 *
 * @author C1749
 */
@Stateless
@LocalBean
public class GrpsdailytmpBean implements Serializable {

    @EJB
    private SuperEJBForERP erpEJB;

    @EJB
    private GroupShipmentBean groupShipmentBean;

    private List<Grpsdailytmp> grpsdailytmpList;

    public GrpsdailytmpBean() {

    }

    public void init() {
    }

    public int updateActualList(int y, int m, Date d) {
        int count = 0;
        try {
            String hangrp = "";
            String trdate = "";
            String series = "";
            String type = "";
            String intertragrp = "";
            Grpsdailytmp gt = null;
            List grpslist = groupShipmentBean.findNeedRow(y, m, d);
            grpsdailytmpList = new ArrayList<>();
            Date date = new Date();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Timestamp timestamp = new Timestamp(date.getTime());
            System.out.println(sdf.format(date));
            if (!grpslist.isEmpty()) {
                for (Object e : grpslist) {
                    Object[] o = (Object[]) e;
                    gt = new Grpsdailytmp();
                    hangrp = o[0].toString();
                    trdate = o[1].toString().substring(0, 10);
                    series = o[2].toString();
                    type = o[3].toString();
                    if (o[4] != null) {
                        intertragrp = o[4].toString();
                    } else {
                        intertragrp = " ";
                    }
                    gt = new Grpsdailytmp(hangrp, trdate, series, type, intertragrp);
                    gt.setSaleqty(BigDecimal.valueOf(Double.valueOf(o[5].toString())));
                    gt.setSaleamtfs(BigDecimal.valueOf(Double.valueOf(o[6].toString())));
                    gt.setOrderqty(BigDecimal.valueOf(Double.valueOf(o[7].toString())));
                    gt.setOrderamtfs(BigDecimal.valueOf(Double.valueOf(o[8].toString())));
                    if (hangrp.equals("V")) {
                        gt.setCoin("VND");
                    } else {
                        gt.setCoin("RMB");
                    }

                    gt.setCreatetime(timestamp);
                    gt.setArea("");
                    grpsdailytmpList.add(gt);
                }
            }
            if (grpsdailytmpList != null) {
                erpEJB.setCompany("A");
                erpEJB.getEntityManager().createNativeQuery("delete from N_RPT_grpsdailytmp where year(trdate) = " + y + " and month(trdate) =" + m).executeUpdate();
                for (Grpsdailytmp e : grpsdailytmpList) {
                    erpEJB.getEntityManager().persist(e);
                }
            }
            if (grpsdailytmpList.size() > 0) {
                count = grpsdailytmpList.size();
            } else {
                count = 0;
            }
        } catch (NumberFormatException ex) {
            count = -1;
        }
        return count;
    }

}
