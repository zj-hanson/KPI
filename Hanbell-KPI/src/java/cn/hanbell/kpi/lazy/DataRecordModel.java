/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.hanbell.kpi.lazy;

import cn.hanbell.kpi.entity.DataRecord;
import com.lightshell.comm.BaseLazyModel;
import com.lightshell.comm.SuperEJB;

/**
 *
 * @author C1879
 */
public class DataRecordModel extends BaseLazyModel<DataRecord> {

    public DataRecordModel(SuperEJB superEJB) {
        this.superEJB = superEJB;
    }

}
