/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.hanbell.kpi.evaluation;

import java.math.BigDecimal;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.Query;

/**
 *
 * @author C1879
 */
public class FreeServiceOuterFW5C extends FreeServiceOuterFW {

    public FreeServiceOuterFW5C() {
        super();
        queryParams.put("facno", "K");
//        queryParams.put("hmark1", "='HD' ");
        queryParams.put("hmark2", " ='RT' ");
    }

}
