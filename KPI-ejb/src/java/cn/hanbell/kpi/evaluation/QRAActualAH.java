/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.hanbell.kpi.evaluation;

/**
 *
 * @author C1749
 */
public class QRAActualAH extends QRAActual {

    public QRAActualAH() {
        super();
        queryParams.put("formid", "QRA-机体物料");
        queryParams.put("deptno", "1M000");

    }

}
