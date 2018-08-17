/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.hanbell.kpi.evaluation;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author C1879
 */
public class ProductivitySumMinute extends Productivity {

    @Override
    public BigDecimal getValue(int y, int m, Date d, int type, LinkedHashMap<String, Object> map) {
        //获得查询参数
        String equipmentnumber = map.get("equipmentnumber") != null ? map.get("equipmentnumber").toString() : "";
        BigDecimal summinute = BigDecimal.ZERO;

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(d);
        int day = calendar.get(calendar.DAY_OF_MONTH);
        try {
            summinute = BigDecimal.valueOf(Integer.parseInt(equipmentnumber) * day * 24 * 60);
        } catch (Exception ex) {
            Logger.getLogger(Shipment.class.getName()).log(Level.SEVERE, null, ex);
        }
        return summinute;

    }

}
