/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.hanbell.kpi.evaluation;

/**
 *采购中心买汉声铸件的金额
 * @author C2082
 */

public class ShoppingCenterAmountSHBToHS extends ShoppingCenterAmount{
    
        
     public ShoppingCenterAmountSHBToHS() {
        super();
        queryParams.put("facno", "C");
        queryParams.put("prono", "1");
        //汉声
        queryParams.put("vdrno", " ='SZJ00065'");  
    }
}
