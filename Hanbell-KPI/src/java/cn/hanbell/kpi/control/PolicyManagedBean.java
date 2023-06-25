/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.hanbell.kpi.control;

import cn.hanbell.kpi.ejb.IndicatorBean;
import cn.hanbell.kpi.ejb.PolicyBean;
import cn.hanbell.kpi.ejb.PolicyContentBean;
import cn.hanbell.kpi.ejb.tms.ProjectBean;
import cn.hanbell.kpi.entity.Indicator;
import cn.hanbell.kpi.entity.Policy;
import cn.hanbell.kpi.entity.PolicyContent;
import cn.hanbell.kpi.lazy.PolicyContentModel;
import cn.hanbell.kpi.web.SuperSingleBean;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Calendar;
import java.util.List;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author C2082
 */
@ManagedBean(name = "policyManagedBean")
@ViewScoped
public class PolicyManagedBean extends SuperSingleBean<PolicyContent> {

    @EJB
    private PolicyContentBean policyContentBean;

    @EJB
    private PolicyBean policyBean;

    @EJB
    private IndicatorBean indicatorBean;

    @EJB
    private ProjectBean projectBean;

    private List<PolicyContent> policyDetail;
    private PolicyContent selectPolicyDetail;
    private Policy policy;
    protected Calendar c;

    public PolicyManagedBean() {
        super(PolicyContent.class);
        c = Calendar.getInstance();
    }

    @Override
    public void construct() {
        if (fc == null) {
            fc = FacesContext.getCurrentInstance();
        }
        if (ec == null) {
            ec = fc.getExternalContext();
        }
        HttpServletRequest request = (HttpServletRequest) ec.getRequest();
        String id = request.getParameter("id");
        if (id == null) {
            fc.getApplication().getNavigationHandler().handleNavigation(fc, null, "error");
        }
        policy = policyBean.findById(Integer.valueOf(id));
        if (policy == null) {
            fc.getApplication().getNavigationHandler().handleNavigation(fc, null, "error");
        }
        init();
        super.construct();
    }

    @Override
    public void init() {
        c.setTime(userManagedBean.getBaseDate());
        this.superEJB = policyBean;
        model = new PolicyContentModel(policyContentBean, this.userManagedBean);
        model.getSortFields().put("seq", "ASC");
        model.getFilterFields().put("parent.year", c.get(Calendar.YEAR));
        if (this.getPolicy() != null && this.getPolicy().getId() != null) {
            model.getFilterFields().put("pid", getPolicy().getId());
        }
        super.init();
        policyDetail = policyContentBean.findByFilters(model.getFilterFields(), model.getSortFields());
    }

    @Override
    protected boolean doBeforeUpdate() throws Exception {
        return true;
    }

    @Override
    public void update() {
        if (this.selectPolicyDetail != null) {
            try {
                if (this.doBeforeUpdate()) {
                    this.getSuperEJB().update(this.selectPolicyDetail);
                    this.doAfterUpdate();
                    this.showInfoMsg("Info", "更新成功!");
                } else {
                    this.showWarnMsg("Warn", "更新前检查失败!");
                }
            } catch (Exception var2) {
                this.showErrorMsg("Error", var2.toString());
            }
        } else {
            this.showWarnMsg("Warn", "没有可更新数据!");
        }

    }

    //更新实际与达成
    public void calcItemScore() {
        try {
            if (selectPolicyDetail != null) {
                //更新KPI数据
                String col = policyContentBean.getColumn("q", userManagedBean.getQ());
                String target, actual, projectSeq;
                BigDecimal value;
                if (selectPolicyDetail.getFromkpi() != null && !"".equals(selectPolicyDetail.getFromkpi()) && selectPolicyDetail.getFromkpiname() != null && !"".equals(selectPolicyDetail.getFromkpiname())) {
                    Indicator i = indicatorBean.findByIdAndYear(Integer.valueOf(selectPolicyDetail.getFromkpi()), userManagedBean.getY());;
                    if (i != null) {
                        switch (userManagedBean.getQ()) {
                            case 1:
                                selectPolicyDetail.setAq1(i.getActualIndicator().getNq1().divide(selectPolicyDetail.getIndicatorrate(), 2, BigDecimal.ROUND_HALF_UP).toString());
                                selectPolicyDetail.setPq1(i.getPerformanceIndicator().getNq1());
                                break;
                            case 2:
                                selectPolicyDetail.setAq2(i.getActualIndicator().getNq2().divide(selectPolicyDetail.getIndicatorrate(), 2, BigDecimal.ROUND_HALF_UP).toString());
                                selectPolicyDetail.setPq2(i.getPerformanceIndicator().getNq2());
                                selectPolicyDetail.setAhy(i.getActualIndicator().getNh1().divide(selectPolicyDetail.getIndicatorrate(), 2, BigDecimal.ROUND_HALF_UP).toString());
                                selectPolicyDetail.setPhy(i.getPerformanceIndicator().getNh1());
                                break;
                            case 3:
                                selectPolicyDetail.setAq3(i.getActualIndicator().getNq3().divide(selectPolicyDetail.getIndicatorrate(), 2, BigDecimal.ROUND_HALF_UP).toString());
                                selectPolicyDetail.setPq3(i.getPerformanceIndicator().getNq3());
                                break;
                            case 4:
                                selectPolicyDetail.setAq4(i.getActualIndicator().getNq4().divide(selectPolicyDetail.getIndicatorrate(), 2, BigDecimal.ROUND_HALF_UP).toString());
                                selectPolicyDetail.setPq4(i.getPerformanceIndicator().getNq4());
                                selectPolicyDetail.setAfy(i.getActualIndicator().getNfy().divide(selectPolicyDetail.getIndicatorrate(), 2, BigDecimal.ROUND_HALF_UP).toString());
                                selectPolicyDetail.setPfy(i.getPerformanceIndicator().getNfy());
                                break;
                        }
                        showInfoMsg("Info", "更新实际值成功");
                    } else {
                        showErrorMsg("Error", "找不到相关指标,更新失败");
                    }
                    return;
                }
                //更新PLM数据
                if (selectPolicyDetail.getFromplm() != null && !"".equals(selectPolicyDetail.getFromplm())) {
                    projectSeq = projectBean.findByProjectSeq(selectPolicyDetail.getFromplm());
                    if (projectSeq == null || "".equals(projectSeq)) {
                        showErrorMsg("Error", "请确认PLM是否有进度");
                        return;
                    }
                    if(!"B".equals(selectPolicyDetail.getCalculationtype())){
                        showErrorMsg("Error", "文字类型不能使用plm更新，请调整");
                        return;
                    }
                    switch (col) {
                        case "q1":
                            selectPolicyDetail.setAq1( projectSeq);
                            target = selectPolicyDetail.getTq1();
                            actual = selectPolicyDetail.getAq1();
                            value = calculateScore(target, actual);
                            selectPolicyDetail.setPq1(value);
                            break;
                        case "q2":
                            selectPolicyDetail.setAq2( projectSeq);
                            //Q2
                            target = selectPolicyDetail.getTq2();
                            actual = selectPolicyDetail.getAq2();
                            value = calculateScore(target, actual);
                            selectPolicyDetail.setPq2(value);
                            //上半年
                            selectPolicyDetail.setAhy(projectSeq);
                            target = selectPolicyDetail.getThy();
                            actual = selectPolicyDetail.getAhy();
                            value = calculateScore(target, actual);
                            selectPolicyDetail.setPhy(value);
                            break;
                        case "q3":
                            selectPolicyDetail.setAq3(projectSeq);
                            target = selectPolicyDetail.getTq3();
                            actual = selectPolicyDetail.getAq3();
                            value = calculateScore(target, actual);
                            selectPolicyDetail.setPq3(value);
                            break;
                        case "q4":
                            selectPolicyDetail.setAq4(projectSeq);
                            target = selectPolicyDetail.getTq4();
                            actual = selectPolicyDetail.getAq4();
                            value = calculateScore(target, actual);
                            selectPolicyDetail.setPq4(value);

                            selectPolicyDetail.setAfy(projectSeq);
                            target = selectPolicyDetail.getTfy();
                            actual = selectPolicyDetail.getAfy();
                            value = calculateScore(target, actual);
                            selectPolicyDetail.setPfy(value);
                            break;
                    }
                    return;
                }
                //数字型更新达成率
                if (!selectPolicyDetail.getCalculationtype().equals("B")) {
                    showErrorMsg("Warn", "数值型才能按计算公式更新");
                    return;
                }
                //A: 实际/目标
                switch (col) {
                    case "q1":
                        target = selectPolicyDetail.getTq1();
                        actual = selectPolicyDetail.getAq1();
                        value = calculatePerformance(target, actual);
                        selectPolicyDetail.setPq1(value);
                        break;
                    case "q2":
                        //Q2
                        target = selectPolicyDetail.getTq2();
                        actual = selectPolicyDetail.getAq2();
                        value = calculatePerformance(target, actual);
                        selectPolicyDetail.setPq2(value);
                        //上半年
                        target = selectPolicyDetail.getThy();
                        actual = selectPolicyDetail.getAhy();
                        value = calculatePerformance(target, actual);
                        selectPolicyDetail.setPhy(value);
                        break;
                    case "q3":
                        target = selectPolicyDetail.getTq3();
                        actual = selectPolicyDetail.getAq3();
                        value = calculatePerformance(target, actual);
                        selectPolicyDetail.setPq3(value);
                        break;
                    case "q4":
                        target = selectPolicyDetail.getTq4();
                        actual = selectPolicyDetail.getAq4();
                        value = calculatePerformance(target, actual);
                        selectPolicyDetail.setPq4(value);

                        target = selectPolicyDetail.getTfy();
                        actual = selectPolicyDetail.getAfy();
                        value = calculatePerformance(target, actual);
                        selectPolicyDetail.setPfy(value);
                        break;
                }

                //B: 目标/实际
            }
        } catch (Exception e) {
            e.printStackTrace();
            showErrorMsg("Error", "更新失败！！");
            return;
        }
    }

    public BigDecimal calculatePerformance(String target, String actual) {
        //实际/目标
        BigDecimal t = BigDecimal.valueOf(Double.valueOf(target));
        BigDecimal a = BigDecimal.valueOf(Double.valueOf(actual));
        if (selectPolicyDetail.getPerformancecalculation().equals("A")) {
            return a.multiply(new BigDecimal(100)).divide(t, 2, RoundingMode.HALF_UP);
        } else {
            return t.multiply(new BigDecimal(100)).divide(a, 2, RoundingMode.HALF_UP);
        }
    }

    /**
     * @desc 截取字符的数字计算得分、达成率
     * @param target
     * @param acutal
     * @return value
     */
    public BigDecimal calculateScore(String target, String acutal) {

        BigDecimal value = BigDecimal.ZERO;
     
            String str1, str2;
            // 先判断有值
            if ((!"".equals(target) || target != null) && (!"".equals(acutal) || acutal != null)) {
//                str1 = target.substring(target.indexOf("#") + 1, target.indexOf("%"));
//                str2 = acutal.substring(acutal.indexOf("#") + 1, acutal.indexOf("%"));
                //判断截取出来的数据是否为数字
                
                if (target.matches("^(\\-|\\+)?\\d+(\\.\\d+)?$") && acutal.matches("^(\\-|\\+)?\\d+(\\.\\d+)?$")) {
                    Double t = Double.valueOf(target);
                    Double a = Double.valueOf(acutal);
                    // 分母不为零
                    if (t > 0.00001) {
                        // 达成率、得分
                        value = BigDecimal.valueOf(a / t * 100);
                    }
                } else {
                    showErrorMsg("Error", "基准目标值格式不正确！！");
                    return BigDecimal.ZERO;
                }
            }
        
        return value;
    }

    public List<PolicyContent> getPolicyDetail() {
        return policyDetail;
    }

    public void setPolicyDetail(List<PolicyContent> policyDetail) {
        this.policyDetail = policyDetail;
    }

    public PolicyContent getSelectPolicyDetail() {
        return selectPolicyDetail;
    }

    public void setSelectPolicyDetail(PolicyContent selectPolicyDetail) {
        this.selectPolicyDetail = selectPolicyDetail;
    }

    public Policy getPolicy() {
        return policy;
    }

    public void setPolicy(Policy policy) {
        this.policy = policy;
    }
}
