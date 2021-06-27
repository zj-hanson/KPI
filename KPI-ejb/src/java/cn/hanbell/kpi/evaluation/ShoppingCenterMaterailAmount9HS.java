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
public class ShoppingCenterMaterailAmount9HS extends ShoppingCenterMaterailAmount {

    public ShoppingCenterMaterailAmount9HS() {
        super();
        queryParams.put("facno", "'H'");
        queryParams.put("prono", "1");
        queryParams.put("material", "select vdrno from shoppingmanufacturer where facno='H' and materialTypeName='衬垫'");
    }

}
