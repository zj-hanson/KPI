/*
 * To change this license header, choose License Headers in Project Properties. To change this template file, choose
 * Tools | Templates and open the template in the editor.
 */
package cn.hanson.kpi.evaluation;

/**
 *
 * @author C1749
 */
public class ShipmentAmtsThirdParty extends ShipmentAmount {

    public ShipmentAmtsThirdParty() {
        super();
        queryParams.put("facno", "H");
        queryParams.put("cuspono", " and (c.cuspono LIKE '%恒工%' or c.cuspono LIKE '%上海卓准%') ");
    }

}
