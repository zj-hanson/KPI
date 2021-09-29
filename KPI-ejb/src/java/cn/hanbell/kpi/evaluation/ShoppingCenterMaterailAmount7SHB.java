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
public class ShoppingCenterMaterailAmount7SHB extends ShoppingCenterMaterailAmount {

    public ShoppingCenterMaterailAmount7SHB() {
        super();
        queryParams.put("facno", "C");
        queryParams.put("prono", "1");
        queryParams.put("material", "select vdrno from shoppingmanufacturer where facno='C' and materialTypeName='阀类'");
    }
}
