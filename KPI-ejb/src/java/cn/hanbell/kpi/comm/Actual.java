/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.hanbell.kpi.comm;

import java.math.BigDecimal;
import java.util.Date;
import java.util.LinkedHashMap;

/**
 *
 * @author C0160
 */
public interface Actual {

    void setEJB(String JNDIName) throws Exception;

    LinkedHashMap<String, Object> getQueryParams();

    int getUpdateMonth(int y, int m);

    int getUpdateYear(int y, int m);

    BigDecimal getValue(int y, int m, Date d, int type, LinkedHashMap<String, Object> map);

}
