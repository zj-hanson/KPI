/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.hanbell.kpi.evaluation;

/**
 *
 * @author C2082
 */
public class ShoppingCenterAmountHS1X extends ShoppingCenterAmount{
    
     public ShoppingCenterAmountHS1X() {
        super();
        queryParams.put("facno", "H");
        queryParams.put("prono", "1");
        queryParams.put("vdrno", " select Vdrno from N_KpiPurPcm ");  
    }
}
