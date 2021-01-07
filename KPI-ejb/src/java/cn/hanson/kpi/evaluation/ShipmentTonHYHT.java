/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.hanson.kpi.evaluation;

import com.lightshell.comm.BaseLib;
import java.math.BigDecimal;
import java.util.Date;
import java.util.LinkedHashMap;
import javax.persistence.Query;

/**
 *
 * @author C1749
 */
public class ShipmentTonHYHT extends ShipmentTonHY {

    public ShipmentTonHYHT() {
        super();
        queryParams.put("facno", "Y");
        queryParams.put("protype", "in ('HT')");
    }

}
