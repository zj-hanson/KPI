/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.hanbell.kpi.control;

import cn.hanbell.kpi.ejb.DataRecordAssistedBean;
import cn.hanbell.kpi.ejb.DataRecordBean;
import cn.hanbell.kpi.entity.DataRecordAssisted;
import cn.hanbell.kpi.entity.DataRecord;
import cn.hanbell.kpi.lazy.DataRecordModel;
import cn.hanbell.kpi.web.SuperSingleBean;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;

/**
 *
 * @author C1879
 */
@ManagedBean(name = "cashFlowManagedBean")
@SessionScoped
public class CashFlowManagedBean extends SuperSingleBean<DataRecord> {

    @EJB
    protected DataRecordBean dataRecordBean;

    @EJB
    protected DataRecordAssistedBean assistedBean;

    protected Date queryDate;
    protected Date createDate;
    protected String queryCompany;
    protected String createCompany;
    protected DataRecordAssisted dataRecordAssisted;
    private boolean querywhethershow;

    public CashFlowManagedBean() {
        super(DataRecord.class);
    }

    public Calendar getCalendar(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        return c;
    }

    @Override
    public void update() {
        currentEntity.setOptuser(getUserManagedBean().getCurrentUser().getUsername());
        currentEntity.setOptdateToNow();
        assistedBean.update(currentEntity.getDataRecordAssisted());
        super.update();

    }

    @Override
    public void init() {
        superEJB = dataRecordBean;
        model = new DataRecordModel(superEJB);
        model.getSortFields().put("facno", "DESC");
        model.getSortFields().put("yea", "DESC");
        model.getSortFields().put("mon", "DESC");
        model.getSortFields().put("itemno", "ASC");
        model.getFilterFields().put("type =", "cashflow");
        model.getFilterFields().put("facno =", "C");
        queryCompany = "C";
        createCompany = "C";
        createDate = userManagedBean.getBaseDate();
        querywhethershow = false;
        super.init();
    }

    @Override
    public void query() {
        if (model != null) {
            model.getFilterFields().clear();
            if (queryCompany != null && !"".equals(queryCompany)) {
                model.getFilterFields().put("facno =", queryCompany);
            }
            if (queryDate != null && !"".equals(queryDate)) {
                model.getFilterFields().put("yea =", getCalendar(queryDate).get(Calendar.YEAR));
            }
            if (queryDate != null && !"".equals(queryDate)) {
                model.getFilterFields().put("mon =", getCalendar(queryDate).get((Calendar.MONTH)) + 1);
            }
            if (queryName != null && !"".equals(queryName)) {
                model.getFilterFields().put("itemname", queryName);
            }
            if (querywhethershow) {
                model.getFilterFields().put("dataRecordAssisted.whethershow", querywhethershow);
            }

            model.getFilterFields().put("type =", "cashflow");
        }
    }

    @Override
    public void reset() {
        queryDate = null;
        queryName = null;
        queryCompany = "C";
        createCompany = "C";
        createDate = userManagedBean.getBaseDate();
        querywhethershow = false;
        super.reset();
    }

    @Override
    public void handleFileUploadWhenNew(FileUploadEvent event) {
        List<DataRecord> addlist = new ArrayList<>();
        String a = "";
        UploadedFile file = event.getFile();
        if (file != null) {
            try {
                InputStream is = file.getInputstream();
                Workbook excel = WorkbookFactory.create(is);
                Sheet sheet = excel.getSheetAt(0);

                int itemno = 1;
                int y = getCalendar(createDate).get(Calendar.YEAR);
                int m = getCalendar(createDate).get(Calendar.MONTH) + 1;
                Cell cell;
                for (int i = 9; i <= sheet.getLastRowNum(); i++) {

                    a = (i + 1) + "";
                    newEntity = new DataRecord();
                    newEntity.setStatus("N");
                    newEntity.setCreator(getUserManagedBean().getCurrentUser().getUsername());
                    newEntity.setCredateToNow();
                    newEntity.setFacno(createCompany);
                    newEntity.setType("cashflow");
                    newEntity.setItemno(itemno);
                    newEntity.setItemname(sheet.getRow(i).getCell(0).getStringCellValue());
                    newEntity.setYea(y);
                    newEntity.setMon(m);
                    cell = sheet.getRow(i).getCell(1);
                    //数字
                    if (cell.getCellType() == Cell.CELL_TYPE_NUMERIC) {
                        newEntity.setAmt(BigDecimal.valueOf(cell.getNumericCellValue()));
                        newEntity.setAdjamt(BigDecimal.valueOf(cell.getNumericCellValue()));
                    } else {
                        newEntity.setAmt(BigDecimal.ZERO);
                        newEntity.setAdjamt(BigDecimal.ZERO);
                    }
                    itemno++;
                    addlist.add(newEntity);
                }
                //导入数据
                if (!addlist.isEmpty()) {
                    List<DataRecord> deletelist = dataRecordBean.findByYeaAndMon(newEntity);
                    if (!deletelist.isEmpty()) {
                        dataRecordBean.delete(deletelist);
                    }
                    for (DataRecord dr : addlist) {
                        dataRecordBean.persist(dr);
                        dataRecordAssisted = assistedBean.findByFacnoAndTypeAndItemname(dr.getFacno(), dr.getType(), dr.getItemname());
                        if (dataRecordAssisted == null) {
                            dataRecordAssisted = new DataRecordAssisted();
                            dataRecordAssisted.setItemname(dr.getItemname());
                            dataRecordAssisted.setAdjitemname(dr.getItemname());
                            dataRecordAssisted.setFacno(dr.getFacno());
                            dataRecordAssisted.setType(dr.getType());
                            assistedBean.persist(dataRecordAssisted);
                        }
                    }
                    showInfoMsg("Info", "数据导入成功");
                } else {
                    showErrorMsg("Error", "数据导入失败");
                }
            } catch (Exception ex) {
                showErrorMsg("Error", "导入失败,找不到文件或格式错误----" + ex.toString());
                if (!"".equals(a)) {
                    showErrorMsg("Error", "第" + a + "行附近栏位发生错误");
                }
            }
        }
    }

    /**
     * @return the queryDate
     */
    public Date getQueryDate() {
        return queryDate;
    }

    /**
     * @param queryDate the queryDate to set
     */
    public void setQueryDate(Date queryDate) {
        this.queryDate = queryDate;
    }

    /**
     * @return the queryCompany
     */
    public String getQueryCompany() {
        return queryCompany;
    }

    /**
     * @param queryCompany the queryCompany to set
     */
    public void setQueryCompany(String queryCompany) {
        this.queryCompany = queryCompany;
    }

    /**
     * @return the createDate
     */
    public Date getCreateDate() {
        return createDate;
    }

    /**
     * @param createDate the createDate to set
     */
    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    /**
     * @return the createCompany
     */
    public String getCreateCompany() {
        return createCompany;
    }

    /**
     * @param createCompany the createCompany to set
     */
    public void setCreateCompany(String createCompany) {
        this.createCompany = createCompany;
    }

    /**
     * @return the querywhethershow
     */
    public boolean isQuerywhethershow() {
        return querywhethershow;
    }

    /**
     * @param querywhethershow the querywhethershow to set
     */
    public void setQuerywhethershow(boolean querywhethershow) {
        this.querywhethershow = querywhethershow;
    }

    @Override
    public void setCurrentEntity(DataRecord currentEntity) {
        if (currentEntity != null) {
            if (currentEntity.getDataRecordAssisted() != null) {
                if (currentEntity.getDataRecordAssisted().getShowno() == null) {
                    currentEntity.getDataRecordAssisted().setShowno(assistedBean.findByShownoMax(currentEntity.getFacno()));
                }
            }
        }
        super.setCurrentEntity(currentEntity);
    }

}
