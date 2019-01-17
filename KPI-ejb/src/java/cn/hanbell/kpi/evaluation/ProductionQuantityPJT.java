/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.hanbell.kpi.evaluation;

/**
 *
 * @author C1879
 */
public class ProductionQuantityPJT extends ProductionQuantity {

    public ProductionQuantityPJT() {
        super();
        queryParams.put("facno", "C");
        queryParams.put("prono", "1");
        queryParams.put("typecode", "= '01' ");
        queryParams.put("linecode", " = 'VP' ");
        queryParams.put("mankind", " in ('WOS','MOS')  ");

    }
}
