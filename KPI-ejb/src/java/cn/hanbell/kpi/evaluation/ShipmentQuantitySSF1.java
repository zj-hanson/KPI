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
public class ShipmentQuantitySSF1 extends ShipmentQuantity {

    public ShipmentQuantitySSF1() {
        super();
        queryParams.put("facno", "C");
        queryParams.put("decode", "1");
        queryParams.put("deptno", " '1U000' ");
        queryParams.put("n_code_DA", "='S' ");
        //2020年3月17日金杰提出无油大巴并入无油内销中
        queryParams.put("n_code_DC", " IN ('SF','SM') ");
        queryParams.put("n_code_DD", " ='00' ");
    }

}
