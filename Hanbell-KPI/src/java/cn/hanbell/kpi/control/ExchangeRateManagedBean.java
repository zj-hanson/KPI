/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.hanbell.kpi.control;

import cn.hanbell.kpi.ejb.ExchangeRateBean;
import cn.hanbell.kpi.entity.ExchangeRate;
import cn.hanbell.kpi.lazy.ExchangeRateModel;
import cn.hanbell.kpi.web.SuperSingleBean;
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

/**
 *
 * @author C1879
 */
@ManagedBean(name = "exchangeRateManagedBean")
@SessionScoped
public class ExchangeRateManagedBean extends SuperSingleBean<ExchangeRate> {

    @EJB
    protected ExchangeRateBean exchangeRateBean;

    protected String queryCurrency;
    protected Date queryDateBegin;
    protected Date queryDateEnd;
    protected BigDecimal rate;
    protected int id;

    public ExchangeRateManagedBean() {
        super(ExchangeRate.class);
    }

    @Override
    protected boolean doBeforePersist() throws Exception {
        if (newEntity != null) {
            setDefaultValue();
            if (exchangeRateBean.queryRateIsExist(newEntity)) {
                showErrorMsg("Error", "添加失败当前日期已有该货币汇率数据");
                return false;
            }
        }
        return super.doBeforePersist();
    }

    @Override
    protected boolean doAfterPersist() throws Exception {
        super.doAfterPersist(); 
        create();
        return true;
    }
    
    

    public Calendar getNowDateBegin() {
        Calendar c = Calendar.getInstance();
        c.setTime(new Date());
        c.add(Calendar.MONTH, -1);
        return c;
    }

    public Calendar getNowDateEnd() {
        Calendar c = Calendar.getInstance();
        c.setTime(new Date());
        return c;
    }

    @Override
    public void init() {
        this.superEJB = exchangeRateBean;
        this.model = new ExchangeRateModel(superEJB);
        this.model.getSortFields().put("rateday", "DESC");
        queryDateBegin = getNowDateBegin().getTime();
        queryDateEnd = getNowDateEnd().getTime();
        queryCurrency = "0";
        newEntity = new ExchangeRate();
        newEntity.setRateday(getNowDateEnd().getTime());
        super.init();
    }

    @Override
    public void query() {
        if (model != null) {
            model.getFilterFields().clear();
            if (queryCurrency != null && !"0".equals(queryCurrency)) {
                model.getFilterFields().put("rpttype", queryCurrency);
            }
            if (queryDateBegin != null) {
                model.getFilterFields().put("ratedayBegin", queryDateBegin);
            }
            if (queryDateEnd != null) {
                model.getFilterFields().put("ratedayEnd", queryDateEnd);
            }
        }
    }

    @Override
    public void reset() {
        super.reset();
        queryDateBegin = getNowDateBegin().getTime();
        queryDateEnd = getNowDateEnd().getTime();
        queryCurrency = "0";
    }

    private void setDefaultValue() {
        newEntity.setFacno("C");
        switch (newEntity.getRpttype()) {
            case "1":
                newEntity.setCoin("USD");
                newEntity.setCoinna("美元");
                newEntity.setExcoin("RMB");
                newEntity.setExcoinna("人民币");
                newEntity.setExchangena("美金/人民币");
                break;
            case "2":
                newEntity.setCoin("EUR");
                newEntity.setCoinna("欧元");
                newEntity.setExcoin("RMB");
                newEntity.setExcoinna("人民币");
                newEntity.setExchangena("欧元/人民币");
                break;
            case "3":
                newEntity.setCoin("JPY");
                newEntity.setCoinna("日圆");
                newEntity.setExcoin("RMB");
                newEntity.setExcoinna("人民币");
                newEntity.setExchangena("日圆/人民币");
                break;
            case "4":
                newEntity.setCoin("RMB");
                newEntity.setCoinna("人民币");
                newEntity.setExcoin("NTD");
                newEntity.setExcoinna("台币");
                newEntity.setExchangena("人民币/台币");
                break;
            case "5":
                newEntity.setCoin("GOLD");
                newEntity.setCoinna("黄金");
                newEntity.setExcoin("USD");
                newEntity.setExcoinna("美元");
                newEntity.setExchangena("黄金盎司/美元");
                break;
            case "6":
                newEntity.setCoin("USD");
                newEntity.setCoinna("美元");
                newEntity.setExcoin("JPY");
                newEntity.setExcoinna("日圆");
                newEntity.setExchangena("美金/日圆");
                break;
            case "7":
                newEntity.setCoin("EUR");
                newEntity.setCoinna("欧元");
                newEntity.setExcoin("USD");
                newEntity.setExcoinna("美金");
                newEntity.setExchangena("欧元/美金");
                break;
            case "8":
                newEntity.setCoin("USD");
                newEntity.setCoinna("美元");
                newEntity.setExcoin("NTD");
                newEntity.setExcoinna("台币");
                newEntity.setExchangena("美金/台币");
                break;
            default:
                newEntity = null;
        }
    }

    /**
     * @return the queryCurrency
     */
    public String getQueryCurrency() {
        return queryCurrency;
    }

    /**
     * @param queryCurrency the queryCurrency to set
     */
    public void setQueryCurrency(String queryCurrency) {
        this.queryCurrency = queryCurrency;
    }

    /**
     * @return the queryDateBegin
     */
    @Override
    public Date getQueryDateBegin() {
        return queryDateBegin;
    }

    /**
     * @param queryDateBegin the queryDateBegin to set
     */
    @Override
    public void setQueryDateBegin(Date queryDateBegin) {
        this.queryDateBegin = queryDateBegin;
    }

    /**
     * @return the queryDateEnd
     */
    @Override
    public Date getQueryDateEnd() {
        return queryDateEnd;
    }

    /**
     * @param queryDateEnd the queryDateEnd to set
     */
    @Override
    public void setQueryDateEnd(Date queryDateEnd) {
        this.queryDateEnd = queryDateEnd;
    }

    /**
     * @return the newEntity
     */
    public ExchangeRate getExchangEntity() {
        return newEntity;
    }

    /**
     * @param newEntity the newEntity to set
     */
    public void setExchangEntity(ExchangeRate newEntity) {
        this.newEntity = newEntity;
    }

}
