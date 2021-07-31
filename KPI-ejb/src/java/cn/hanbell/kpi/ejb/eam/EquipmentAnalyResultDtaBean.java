/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.hanbell.kpi.ejb.eam;

import cn.hanbell.kpi.comm.SuperEJBForEAM;
import cn.hanbell.kpi.entity.eam.EquipmentAnalyResultDta;
import javax.ejb.Stateless;
import javax.ejb.LocalBean;

/**
 *
 * @author C2079
 */
@Stateless
@LocalBean
public class EquipmentAnalyResultDtaBean extends SuperEJBForEAM<EquipmentAnalyResultDta> {

    public EquipmentAnalyResultDtaBean() {
        super(EquipmentAnalyResultDta.class);
    }

}
