/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.hanbell.kpi.control;

import cn.hanbell.kpi.ejb.IndicatorBean;
import cn.hanbell.kpi.ejb.InvtrtrdayBean;
import cn.hanbell.kpi.entity.Indicator;
import cn.hanbell.kpi.entity.Invtrtrday;
import cn.hanbell.kpi.lazy.InvtrtrdayModel;
import cn.hanbell.kpi.web.SuperSingleBean;
import com.lightshell.comm.BaseLib;
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
 * @author C1749
 */
@ManagedBean(name = "invtrtrdayManagedBean")
@SessionScoped
public class InvtrtrdayManagedBean extends SuperSingleBean<Invtrtrday> {

    @EJB
    private InvtrtrdayBean invtrtrdayBean;
    @EJB
    private IndicatorBean indicatorBean;

    List<Indicator> indicators;

    protected int y;
    protected int m;
    private int requestId;
    protected String queryCompany = "C";

    public InvtrtrdayManagedBean() {
        super(Invtrtrday.class);
    }

    @Override
    public void init() {
        superEJB = invtrtrdayBean;
        model = new InvtrtrdayModel(superEJB);
        model.getFilterFields().put("company =", queryCompany);
        Calendar c = Calendar.getInstance();
        c.setTime(BaseLib.getDate());
        c.add(Calendar.DATE, 0 - c.get(Calendar.DATE));
        setQueryDateBegin(c.getTime());
        y = c.get(Calendar.YEAR);
        m = c.get(Calendar.MONTH) + 1;
        super.init();
    }

    @Override
    public void query() {
        if (model != null) {
            model.getFilterFields().clear();
            model.getFilterFields().put("company =", queryCompany);
            model.getFilterFields().put("seq =", y);
            model.getFilterFields().put("mon =", m);
        }
    }

    @Override
    public void handleFileUploadWhenNew(FileUploadEvent event) {
        if (queryDateBegin != null) {
            Calendar c = Calendar.getInstance();
            c.setTime(queryDateBegin);
            y = c.get(Calendar.YEAR);
            m = c.get(Calendar.MONTH) + 1;
        } else {
            showErrorMsg("Error", "请先选择日期");
            return;
        }
        List<Invtrtrday> addList = new ArrayList<>();
        UploadedFile file = event.getFile();
        String a = "";
        if (file != null) {
            try {
                InputStream inputStream = file.getInputstream();
                Workbook excel = WorkbookFactory.create(inputStream);
                Sheet sheet = excel.getSheetAt(0);
                for (int i = 4; i <= sheet.getLastRowNum(); i++) {
                    Cell c;
                    a = (i + 1) + "";
                    int sortid = 1;
                    newEntity = new Invtrtrday();
                    newEntity.setSortid(sortid);
                    newEntity.setCompany(queryCompany);
                    newEntity.setSeq(y);
                    newEntity.setMon(m);
                    c = sheet.getRow(i).getCell(0);
                    newEntity.setName(BaseLib.convertExcelCell(String.class, c));
                    c = sheet.getRow(i).getCell(1);
                    newEntity.setDeptno(BaseLib.convertExcelCell(String.class, c));
                    c = sheet.getRow(i).getCell(2);
                    newEntity.setDeptname(BaseLib.convertExcelCell(String.class, c));
                    c = sheet.getRow(i).getCell(3);
                    newEntity.setType(BaseLib.convertExcelCell(String.class, c));
                    c = sheet.getRow(i).getCell(4);
                    newEntity.setResponsible(BaseLib.convertExcelCell(String.class, c));
                    //销售金额
                    c = sheet.getRow(i).getCell(5);
                    newEntity.setCost(BaseLib.convertExcelCell(BigDecimal.class, c));
                    //周转天数
                    c = sheet.getRow(i).getCell(6);
                    newEntity.setTuningday(BaseLib.convertExcelCell(BigDecimal.class, c));
                    //库存金额
                    c = sheet.getRow(i).getCell(7);
                    newEntity.setInvamount(BaseLib.convertExcelCell(BigDecimal.class, c));
                    //记录上传人员
                    newEntity.setCfmuser(userManagedBean.getUserid());
                    //记录上传日期
                    newEntity.setCfmdate(queryDateBegin);
                    addList.add(newEntity);
                    sortid++;
                }
                //导入
                if (!addList.isEmpty()) {
                    List<Invtrtrday> delList = invtrtrdayBean.findByCompanyAndSeqAndMon(newEntity);
                    if (!delList.isEmpty()) {
                        invtrtrdayBean.delete(delList);
                    }
                    for (Invtrtrday item : addList) {
                        invtrtrdayBean.persist(item);
                    }
                    showInfoMsg("Info", "数据导入成功!");
                    //数据上传完之后继续执行更新
                    //updateToTurnoverdays();
                }
            } catch (Exception ex) {
                showErrorMsg("Error", "导入失败,找不到文件或格式错误--第" + a + "行附近栏位发生错误" + ex.toString());
            }
        }
    }

    public void updateToTurnoverdays() {
        try {
            if (queryDateBegin != null) {
                Calendar c = Calendar.getInstance();
                c.setTime(queryDateBegin);
                y = c.get(Calendar.YEAR);
                m = c.get(Calendar.MONTH) + 1;
            } else {
                showErrorMsg("Error", "请先选择日期");
                return;
            }
            List<Invtrtrday> invtrtrdayList = invtrtrdayBean.findByCompanyAndSeqAndMon(queryCompany, y, m);
            if (invtrtrdayList.isEmpty()) {
                showErrorMsg("Error", "请先上传本月的数据");
                return;
            }
            //生产库存周转天数
            indicators = indicatorBean.findByCategoryAndYear("生产周转天数", y);
            indicatorBean.getEntityManager().clear();
            setToTurnoverdaysValue(indicators, invtrtrdayList, "生产");
            //营业库存周转天数
            indicators = indicatorBean.findByCategoryAndYear("营业周转天数", y);
            indicatorBean.getEntityManager().clear();
            setToTurnoverdaysValue(indicators, invtrtrdayList, "营业");
            //服务库存周转天数
            indicators = indicatorBean.findByCategoryAndYear("服务周转天数", y);
            indicatorBean.getEntityManager().clear();
            setToTurnoverdaysValue(indicators, invtrtrdayList, "服务");
            //服务库存周转天数
            indicators = indicatorBean.findByCategoryAndYear("周转天数总计", y);
            indicatorBean.getEntityManager().clear();
            setToTurnoverdaysValue(indicators, invtrtrdayList, "总计");
            indicators.clear();
        } catch (Exception e) {
            showInfoMsg("Info", "更新周转天数指标数据成功!" + e.toString());
        }
    }

    public void setToTurnoverdaysValue(List<Indicator> indicators, List<Invtrtrday> invtrtrdayList, String category) {
        try {
            if (!indicators.isEmpty() && !invtrtrdayList.isEmpty()) {
                for (Indicator i : indicators) {
                    for (Invtrtrday t : invtrtrdayList) {
                        if (t.getName().contains(category) && t.getDeptno().trim().equals(i.getDeptno().trim())) {
                            switch (m) {
                                case 1:
                                    i.getActualIndicator().setN01(t.getTuningday());
                                    break;
                                case 2:
                                    i.getActualIndicator().setN02(t.getTuningday());
                                    break;
                                case 3:
                                    i.getActualIndicator().setN03(t.getTuningday());
                                    break;
                                case 4:
                                    i.getActualIndicator().setN04(t.getTuningday());
                                    break;
                                case 5:
                                    i.getActualIndicator().setN05(t.getTuningday());
                                    break;
                                case 6:
                                    i.getActualIndicator().setN06(t.getTuningday());
                                    break;
                                case 7:
                                    i.getActualIndicator().setN07(t.getTuningday());
                                    break;
                                case 8:
                                    i.getActualIndicator().setN08(t.getTuningday());
                                    break;
                                case 9:
                                    i.getActualIndicator().setN09(t.getTuningday());
                                    break;
                                case 10:
                                    i.getActualIndicator().setN10(t.getTuningday());
                                    break;
                                case 11:
                                    i.getActualIndicator().setN11(t.getTuningday());
                                    break;
                                case 12:
                                    i.getActualIndicator().setN12(t.getTuningday());
                                    break;
                                default:
                                    break;
                            }
                            indicatorBean.update(i);
                        }
                    }
                }
            }
        } catch (Exception ex) {
            ex.toString();
        }
    }

    public Calendar getCalendar(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        return c;
    }

    public String getQueryCompany() {
        return queryCompany;
    }

    public void setQueryCompany(String queryCompany) {
        this.queryCompany = queryCompany;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getM() {
        return m;
    }

    public void setM(int m) {
        this.m = m;
    }

}
