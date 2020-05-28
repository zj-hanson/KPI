/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.hanbell.kpi.lazy;

import cn.hanbell.kpi.control.UserManagedBean;
import cn.hanbell.kpi.entity.ScorecardContent;
import com.lightshell.comm.BaseLazyModel;
import com.lightshell.comm.SuperEJB;
import java.util.List;
import java.util.Map;
import org.primefaces.model.SortOrder;

/**
 *
 * @author C0160
 */
public class ScorecardContentModel extends BaseLazyModel<ScorecardContent> {

    private final UserManagedBean userManagedBean;

    public ScorecardContentModel(SuperEJB superEJB, UserManagedBean userManagedBean) {
        this.superEJB = superEJB;
        this.userManagedBean = userManagedBean;
    }

    @Override
    public List<ScorecardContent> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, Object> filters) {
        filterFields.put("parent.company =", userManagedBean.getCompany());
        return super.load(first, pageSize, sortField, sortOrder, filters);
    }

}
