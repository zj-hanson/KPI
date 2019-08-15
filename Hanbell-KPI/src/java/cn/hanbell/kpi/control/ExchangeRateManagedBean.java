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
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.primefaces.event.FileUploadEvent;

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
    protected BigDecimal rate;
    protected int id;

    public ExchangeRateManagedBean() {
        super(ExchangeRate.class);
    }

    @Override
    protected boolean doBeforePersist() throws Exception {
        if (newEntity != null) {
            setDefaultValue();
            ExchangeRate exchangeRate = exchangeRateBean.queryExchangeRate(newEntity);
            if (exchangeRate != null) {
                exchangeRateBean.update(newEntity);
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

    public void readFile() {
    }

    @Override
    public void handleFileUploadWhenNew(FileUploadEvent event) {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        DecimalFormat dmf=new DecimalFormat("#");
        List<ExchangeRate> addlist = new ArrayList<>();
        String a = "";
        String b = "";
        super.handleFileUploadWhenNew(event);
        if (this.fileName != null) {
            try {
                InputStream is = new FileInputStream(getAppResPath() + "/" + fileName);
                Workbook excel = WorkbookFactory.create(is);
                Sheet sheet = excel.getSheetAt(0);

                int cols = sheet.getRow(0).getLastCellNum();
                for (int i = 1; i < cols; i++) {
                    for (int j = 1; j < sheet.getLastRowNum() + 1; j++) {
                        a = df.format(sheet.getRow(0).getCell(i).getDateCellValue());
                        b = j + "";
                        newEntity = new ExchangeRate();
                        newEntity.setRateday(sheet.getRow(0).getCell(i).getDateCellValue());
                        newEntity.setRate((BigDecimal.valueOf(sheet.getRow(j).getCell(i).getNumericCellValue())));
                        newEntity.setRpttype(dmf.format(sheet.getRow(j).getCell(0).getNumericCellValue()));
                        setDefaultValue();
                        addlist.add(newEntity);
                    }
                }
                newEntity = new ExchangeRate();
                //导入数据
                if (!addlist.isEmpty()) {
                    try {
                        ExchangeRate exchangeRate;
                        for (int i = 0; i < addlist.size(); i++) {
                            exchangeRate = exchangeRateBean.queryExchangeRate(addlist.get(i));
                            if (exchangeRate != null) {
                                exchangeRate.setRate(addlist.get(i).getRate());
                                exchangeRateBean.update(exchangeRate);
                            } else {
                                exchangeRateBean.persist(addlist.get(i));
                            }
                        }
                        showInfoMsg("Info", "数据导入成功");
                    } catch (Exception el) {
                        showInfoMsg("Info", "数据导入失败");
                        System.out.println("cn.hanbell.kpi.control.ExchangeRateManagedBean.handleFileUploadWhenNew()" + el.toString());
                    }
                }
            } catch (Exception ex) {
                showErrorMsg("Error", "导入失败,找不到文件或格式错误----" + ex.toString());
                if (!"".equals(a) && !"".equals(b)) {
                    showErrorMsg("Error", "时间为：" + a + "第" + b + "行附近栏位发生错误");
                }
            }
            //将导入文件删除掉
            File file = new File(getAppResPath() + "/" + fileName);
            if (file.isFile()) {
                file.delete();
            }
        }

    }

    private void setDefaultValue() {
        newEntity.setFacno("C");
        newEntity.setStatus("N");
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
                newEntity.setCoinna("日元");
                newEntity.setExcoin("RMB");
                newEntity.setExcoinna("人民币");
                newEntity.setExchangena("100日元/人民币");
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
                newEntity.setExcoinna("日元");
                newEntity.setExchangena("美金/日元");
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
