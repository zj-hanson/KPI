/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.hanbell.kpi.evaluation;

/**
 *
 * @author C1879 柯茂
 */
public class ProductionQuantityKM extends ProductionQuantity {

    public ProductionQuantityKM() {
        super();
        queryParams.put("facno", "K");
        queryParams.put("prono", "1");
        queryParams.put("linecode", " = '01' ");
        queryParams.put("typecode", "= '01' ");
        queryParams.put("itcls", " in ('3H80','3W76','3W80')");
        queryParams.put("itnbrf", " and itnbrf not in (select itnbr from invmas where itcls in ('3176','3179','3180')) ");
        
    }

}
