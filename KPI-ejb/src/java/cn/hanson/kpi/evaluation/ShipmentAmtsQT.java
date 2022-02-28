/*
 * To change this license header, choose License Headers in Project Properties. To change this template file, choose
 * Tools | Templates and open the template in the editor.
 */
package cn.hanson.kpi.evaluation;

/**
 *
 * @author C1749
 */
public class ShipmentAmtsQT extends ShipmentAmount {

    public ShipmentAmtsQT() {
        super();
        queryParams.put("facno", "H");
        queryParams.put("protype", "in ('QT')");
        queryParams.put("cuspono", " and c.cuspono NOT LIKE '%恒工%' and c.cuspono NOT LIKE '%上海卓准%' ");
    }

}
