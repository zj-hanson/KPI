/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.hanbell.kpi.control;

import cn.hanbell.eap.entity.Department;
import cn.hanbell.eap.entity.SystemUser;
import cn.hanbell.kpi.ejb.IndicatorBean;
import cn.hanbell.kpi.ejb.ScorecardBean;
import cn.hanbell.kpi.ejb.ScorecardDetailBean;
import cn.hanbell.kpi.entity.Indicator;
import cn.hanbell.kpi.entity.Scorecard;
import cn.hanbell.kpi.entity.ScorecardDetail;
import cn.hanbell.kpi.lazy.ScorecardModel;
import cn.hanbell.kpi.web.SuperMultiBean;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import org.primefaces.event.SelectEvent;

/**
 *
 * @author C0160
 */
@ManagedBean(name = "scorecardSetManagedBean")
@javax.faces.bean.SessionScoped
public class ScorecardSetManagedBean extends SuperMultiBean<Scorecard, ScorecardDetail> {

    @EJB
    private IndicatorBean indicatorBean;
    @EJB
    private ScorecardBean scorecardBean;
    @EJB
    private ScorecardDetailBean scorecardDetailBean;

    protected Calendar c;

    protected String queryDeptno;
    protected String queryDeptname;
    protected String queryUserid;
    protected String queryUsername;
    protected int queryYear;

    protected List<String> paramDeptno = null;

    public ScorecardSetManagedBean() {
        super(Scorecard.class, ScorecardDetail.class);
        c = Calendar.getInstance();
        openParams = new HashMap();
        openOptions = new HashMap();
    }

    @Override
    public void create() {
        super.create();
        this.newEntity.setCompany(userManagedBean.getCompany());
        this.newEntity.setSeq(queryYear);
    }

    @Override
    protected boolean doBeforeVerify() throws Exception {
        if (super.doBeforeVerify()) {
            //super.doBeforeVerify()会重设detailList
            if (!currentEntity.getTemplate()) {
                //不是模板需要检查权重是否100
                int weight = 0;
                for (ScorecardDetail d : detailList) {
                    weight += d.getWeight();
                }
                if (weight != 100) {
                    showErrorMsg("Error", "权重合计需要等于100");
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public void handleDialogReturnWhenNew(SelectEvent event) {
        if (event.getObject() != null && newEntity != null) {
            Department e = (Department) event.getObject();
            newEntity.setDeptno(e.getDeptno());
            newEntity.setDeptname(e.getDept());
        }
    }

    @Override
    public void handleDialogReturnWhenEdit(SelectEvent event) {
        if (event.getObject() != null && currentEntity != null) {
            Department e = (Department) event.getObject();
            currentEntity.setDeptno(e.getDeptno());
            currentEntity.setDeptname(e.getDept());
        }
    }

    public void handleDialogReturnTemplateWhenEdit(SelectEvent event) {
        if (event.getObject() != null && currentEntity != null) {
            Object o = event.getObject();
            Scorecard sc = (Scorecard) o;
            currentEntity.setTemplateId(sc.getName());
            currentEntity.setApi(sc.getApi());
            List<ScorecardDetail> templateDetail = scorecardDetailBean.findByPId(sc.getId());
            if (templateDetail != null && !templateDetail.isEmpty()) {
                for (ScorecardDetail d : templateDetail) {
                    this.createDetail();
                    this.currentDetail.setPid(currentEntity.getId());
                    this.currentDetail.setContent(d.getContent());
                    this.currentDetail.setWeight(d.getWeight());
                    this.currentDetail.setType(d.getType());
                    this.currentDetail.setKind(d.getKind());
                    this.currentDetail.setValueMode(d.getValueMode());
                    this.currentDetail.setBfy(d.getBfy());
                    this.currentDetail.setBh2(d.getBh2());
                    this.currentDetail.setBh1(d.getBh1());
                    this.currentDetail.setBq1(d.getBq1());
                    this.currentDetail.setBq2(d.getBq2());
                    this.currentDetail.setBq3(d.getBq3());
                    this.currentDetail.setBq4(d.getBq4());
                    this.currentDetail.setTfy(d.getTfy());
                    this.currentDetail.setTh2(d.getTh2());
                    this.currentDetail.setTh1(d.getTh1());
                    this.currentDetail.setTq1(d.getTq1());
                    this.currentDetail.setTq2(d.getTq2());
                    this.currentDetail.setTq3(d.getTq3());
                    this.currentDetail.setTq4(d.getTq4());
                    this.doConfirmDetail();
                }
            }
        }
    }

    public void handleDialogReturnUserWhenNew(SelectEvent event) {
        if (event.getObject() != null && newEntity != null) {
            Object o = event.getObject();
            SystemUser user = (SystemUser) o;
            newEntity.setUserid(user.getUserid());
            newEntity.setUsername(user.getUsername());
        }
    }

    public void handleDialogReturnUserWhenEdit(SelectEvent event) {
        if (event.getObject() != null && currentEntity != null) {
            Object o = event.getObject();
            SystemUser user = (SystemUser) o;
            currentEntity.setUserid(user.getUserid());
            currentEntity.setUsername(user.getUsername());
        }
    }

    @Override
    public void handleDialogReturnWhenDetailNew(SelectEvent event) {
        if (event.getObject() != null && currentDetail != null) {
            Indicator e = (Indicator) event.getObject();
            currentDetail.setIndicator(e.getFormid());
            currentDetail.setDeptno(e.getDeptno());
            currentDetail.setDeptname(e.getDeptname());
            currentDetail.setUserid(e.getUserid());
            currentDetail.setUsername(e.getUsername());
            //从指标代入基准
            currentDetail.setBq1(e.getBenchmarkIndicator().getNq1().toString());
            currentDetail.setBq2(e.getBenchmarkIndicator().getNq2().toString());
            currentDetail.setBh1(e.getBenchmarkIndicator().getNh1().toString());
            currentDetail.setBq3(e.getBenchmarkIndicator().getNq3().toString());
            currentDetail.setBq4(e.getBenchmarkIndicator().getNq4().toString());
            currentDetail.setBh2(e.getBenchmarkIndicator().getNh2().toString());
            currentDetail.setBfy(e.getBenchmarkIndicator().getNfy().toString());
            //从指标代入目标
            currentDetail.setTq1(e.getTargetIndicator().getNq1().toString());
            currentDetail.setTq2(e.getTargetIndicator().getNq2().toString());
            currentDetail.setTh1(e.getTargetIndicator().getNh1().toString());
            currentDetail.setTq3(e.getTargetIndicator().getNq3().toString());
            currentDetail.setTq4(e.getTargetIndicator().getNq4().toString());
            currentDetail.setTh2(e.getTargetIndicator().getNh2().toString());
            currentDetail.setTfy(e.getTargetIndicator().getNfy().toString());
        }
    }

    @Override
    public void handleDialogReturnWhenDetailEdit(SelectEvent event) {
        if (event.getObject() != null && currentDetail != null) {
            Indicator e = (Indicator) event.getObject();
            currentDetail.setIndicator(e.getFormid());
            currentDetail.setDeptno(e.getDeptno());
            currentDetail.setDeptname(e.getDeptname());
            currentDetail.setUserid(e.getUserid());
            currentDetail.setUsername(e.getUsername());
            //从指标代入基准
            currentDetail.setBq1(e.getBenchmarkIndicator().getNq1().toString());
            currentDetail.setBq2(e.getBenchmarkIndicator().getNq2().toString());
            currentDetail.setBh1(e.getBenchmarkIndicator().getNh1().toString());
            currentDetail.setBq3(e.getBenchmarkIndicator().getNq3().toString());
            currentDetail.setBq4(e.getBenchmarkIndicator().getNq4().toString());
            currentDetail.setBh2(e.getBenchmarkIndicator().getNh2().toString());
            currentDetail.setBfy(e.getBenchmarkIndicator().getNfy().toString());
            //从指标代入目标
            currentDetail.setTq1(e.getTargetIndicator().getNq1().toString());
            currentDetail.setTq2(e.getTargetIndicator().getNq2().toString());
            currentDetail.setTh1(e.getTargetIndicator().getNh1().toString());
            currentDetail.setTq3(e.getTargetIndicator().getNq3().toString());
            currentDetail.setTq4(e.getTargetIndicator().getNq4().toString());
            currentDetail.setTh2(e.getTargetIndicator().getNh2().toString());
            currentDetail.setTfy(e.getTargetIndicator().getNfy().toString());
        }
    }

    public void handleDialogReturnDeptWhenDetailNew(SelectEvent event) {
        if (event.getObject() != null && currentDetail != null) {
            Department e = (Department) event.getObject();
            currentDetail.setDeptno(e.getDeptno());
            currentDetail.setDeptname(e.getDept());
        }
    }

    public void handleDialogReturnDeptWhenDetailEdit(SelectEvent event) {
        if (event.getObject() != null && currentDetail != null) {
            Object o = event.getObject();
            Department e = (Department) event.getObject();
            currentDetail.setDeptno(e.getDeptno());
            currentDetail.setDeptname(e.getDept());
        }
    }

    public void handleDialogReturnUserWhenDetailNew(SelectEvent event) {
        if (event.getObject() != null && currentDetail != null) {
            Object o = event.getObject();
            SystemUser user = (SystemUser) o;
            currentDetail.setUserid(user.getUserid());
            currentDetail.setUsername(user.getUsername());
        }
    }

    public void handleDialogReturnUserWhenDetailEdit(SelectEvent event) {
        if (event.getObject() != null && currentDetail != null) {
            Object o = event.getObject();
            SystemUser user = (SystemUser) o;
            currentDetail.setUserid(user.getUserid());
            currentDetail.setUsername(user.getUsername());
        }
    }

    @Override
    public void init() {
        superEJB = scorecardBean;
        detailEJB = scorecardDetailBean;
        model = new ScorecardModel(scorecardBean, this.userManagedBean);
        model.getSortFields().put("seq", "DESC");
        model.getSortFields().put("sortid", "ASC");
        model.getSortFields().put("deptno", "ASC");
        c.setTime(userManagedBean.getBaseDate());
        queryYear = c.get(Calendar.YEAR);
        super.init();
    }

    public void moveDown() {
        if (currentDetail != null) {
            int i, n, m;
            n = currentDetail.getSeq();
            i = detailList.indexOf(currentDetail);
            if (i < detailList.size() - 1) {
                ScorecardDetail scd = detailList.get(i + 1);
                m = scd.getSeq();
                scd.setSeq(-1);
                currentDetail.setSeq(m);
                this.doConfirmDetail();
                scd.setSeq(n);
                currentDetail = scd;
                this.doConfirmDetail();
                //重新排序
                detailList.sort((ScorecardDetail o1, ScorecardDetail o2) -> {
                    if (o1.getSeq() > o2.getSeq()) {
                        return 1;
                    } else {
                        return -1;
                    }
                });
                showInfoMsg("Info", "MoveDown");
            } else {
                showWarnMsg("Warn", "已是最后");
            }
        }
    }

    public void moveUp() {
        if (currentDetail != null) {
            int i, n, m;
            n = currentDetail.getSeq();
            i = detailList.indexOf(currentDetail);
            if (i > 0) {
                ScorecardDetail scd = detailList.get(i - 1);
                m = scd.getSeq();
                scd.setSeq(-1);
                currentDetail.setSeq(m);
                this.doConfirmDetail();
                scd.setSeq(n);
                currentDetail = scd;
                this.doConfirmDetail();
                //重新排序
                detailList.sort((ScorecardDetail o1, ScorecardDetail o2) -> {
                    if (o1.getSeq() > o2.getSeq()) {
                        return 1;
                    } else {
                        return -1;
                    }
                });
                showInfoMsg("Info", "MoveUp");
            } else {
                showWarnMsg("Warn", "已是最前");
            }
        }
    }

    @Override
    public void openDialog(String view) {
        switch (view) {
            case "indicatorSelect":
                openParams.clear();
                if (paramDeptno == null) {
                    paramDeptno = new ArrayList<>();
                } else {
                    paramDeptno.clear();
                }
                paramDeptno.add(currentEntity.getDeptno());
                openParams.put("deptno", paramDeptno);
                openOptions.clear();
                openOptions.put("modal", true);
                openOptions.put("contentWidth", "900");
                super.openDialog(view, openOptions, openParams);
                break;
            default:
                super.openDialog(view);
        }
    }

    /**
     * @return the queryDeptno
     */
    public String getQueryDeptno() {
        return queryDeptno;
    }

    /**
     * @param queryDeptno the queryDeptno to set
     */
    public void setQueryDeptno(String queryDeptno) {
        this.queryDeptno = queryDeptno;
    }

    /**
     * @return the queryDeptname
     */
    public String getQueryDeptname() {
        return queryDeptname;
    }

    /**
     * @param queryDeptname the queryDeptname to set
     */
    public void setQueryDeptname(String queryDeptname) {
        this.queryDeptname = queryDeptname;
    }

    /**
     * @return the queryUserid
     */
    public String getQueryUserid() {
        return queryUserid;
    }

    /**
     * @param queryUserid the queryUserid to set
     */
    public void setQueryUserid(String queryUserid) {
        this.queryUserid = queryUserid;
    }

    /**
     * @return the queryUsername
     */
    public String getQueryUsername() {
        return queryUsername;
    }

    /**
     * @param queryUsername the queryUsername to set
     */
    public void setQueryUsername(String queryUsername) {
        this.queryUsername = queryUsername;
    }

    /**
     * @return the queryYear
     */
    public int getQueryYear() {
        return queryYear;
    }

    /**
     * @param queryYear the queryYear to set
     */
    public void setQueryYear(int queryYear) {
        this.queryYear = queryYear;
    }

}
