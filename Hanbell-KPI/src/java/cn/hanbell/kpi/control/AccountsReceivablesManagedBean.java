/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.hanbell.kpi.control;

import cn.hanbell.kpi.ejb.IndicatorBean;
import cn.hanbell.kpi.ejb.AccountsReceivablesBean;
import cn.hanbell.kpi.entity.Indicator;
import cn.hanbell.kpi.entity.AccountsReceivables;
import cn.hanbell.kpi.lazy.AccountsReceivablesModel;
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
import javax.servlet.http.HttpServletRequest;
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
@ManagedBean(name = "accountsReceivablesManagedBean")
@SessionScoped
public class AccountsReceivablesManagedBean extends SuperSingleBean<AccountsReceivables> {

    @EJB
    private AccountsReceivablesBean accountsReceivablesBean;
    @EJB
    private IndicatorBean indicatorBean;

    protected String queryCompany;
    protected int y;
    protected int m;
    private int requestId;

    public AccountsReceivablesManagedBean() {
        super(AccountsReceivables.class);
    }

    @Override
    public void init() {
        HttpServletRequest request = (HttpServletRequest) ec.getRequest();
        requestId = Integer.valueOf(request.getParameter("id"));
        superEJB = accountsReceivablesBean;
        model = new AccountsReceivablesModel(superEJB);
        model.getFilterFields().put("company =", "C");
        Calendar c = Calendar.getInstance();
        setQueryCompany("C");
        query();
        super.init();
    }

    @Override
    public void construct() {
        Calendar c = Calendar.getInstance();
        c.setTime(BaseLib.getDate());
        c.add(Calendar.DATE, 0 - c.get(Calendar.DATE));
        setQueryDateBegin(c.getTime());
        super.construct(); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void query() {
        if (queryDateBegin != null) {
            Calendar c = Calendar.getInstance();
            c.setTime(queryDateBegin);
            y = c.get(Calendar.YEAR);
            m = c.get(Calendar.MONTH) + 1;
        } else {
            showErrorMsg("Error", "请先选择日期");
            return;
        }
        if (model != null) {
            model.getFilterFields().clear();
            if (queryCompany != null && !"".equals(queryCompany)) {
                model.getFilterFields().put("company =", queryCompany);
            }
            if (queryDateBegin != null) {
                model.getFilterFields().put("seq =", y);
            }
            if (queryDateBegin != null) {
                model.getFilterFields().put("mon =", m);
            }
        }
    }

    @Override
    public void handleFileUploadWhenNew(FileUploadEvent event) {
        List<AccountsReceivables> addlist = new ArrayList<>();
        UploadedFile file = event.getFile();
        String a = "";
        if (file != null) {
            try {
                InputStream inputStream = file.getInputstream();
                Workbook excel = WorkbookFactory.create(inputStream);
                Sheet sheet = excel.getSheetAt(0);
                if (queryDateBegin != null) {
                    Calendar c = Calendar.getInstance();
                    c.setTime(queryDateBegin);
                    y = c.get(Calendar.YEAR);
                    m = c.get(Calendar.MONTH) + 1;
                } else {
                    showErrorMsg("Error", "请先选择日期");
                    return;
                }
                for (int i = 4; i <= sheet.getLastRowNum(); i++) {
                    a = (i + 1) + "";
                    int sortid = 1;
                    newEntity = new AccountsReceivables();
                    newEntity.setSortid(sortid);
                    newEntity.setCompany(queryCompany);
                    newEntity.setSeq(y);
                    newEntity.setMon(m);
                    newEntity.setDeptname(sheet.getRow(i).getCell(0).getStringCellValue());
                    newEntity.setDeptno(sheet.getRow(i).getCell(1).toString());
                    newEntity.setName(sheet.getRow(i).getCell(2).getStringCellValue());
                    newEntity.setType(sheet.getRow(i).getCell(3).getStringCellValue());
                    //保证每一行为数字格式，否则赋值为零
                    newEntity.setSaleTarget(sheet.getRow(i).getCell(4).getCellType() == Cell.CELL_TYPE_NUMERIC ? BigDecimal.valueOf(sheet.getRow(i).getCell(4).getNumericCellValue()) : BigDecimal.ZERO);
                    newEntity.setSaleActual(sheet.getRow(i).getCell(5).getCellType() == Cell.CELL_TYPE_NUMERIC ? BigDecimal.valueOf(sheet.getRow(i).getCell(5).getNumericCellValue()) : BigDecimal.ZERO);
                    newEntity.setSaleRatio(sheet.getRow(i).getCell(6).getCellType() == Cell.CELL_TYPE_NUMERIC ? BigDecimal.valueOf(sheet.getRow(i).getCell(6).getNumericCellValue()) : BigDecimal.ZERO);
                    newEntity.setSaleCost(sheet.getRow(i).getCell(7).getCellType() == Cell.CELL_TYPE_NUMERIC ? BigDecimal.valueOf(sheet.getRow(i).getCell(7).getNumericCellValue()) : BigDecimal.ZERO);
                    newEntity.setGincmrt(sheet.getRow(i).getCell(8).getCellType() == Cell.CELL_TYPE_NUMERIC ? BigDecimal.valueOf(sheet.getRow(i).getCell(8).getNumericCellValue()) : BigDecimal.ZERO);
                    newEntity.setARTdayTarget(sheet.getRow(i).getCell(9).getCellType() == Cell.CELL_TYPE_NUMERIC ? BigDecimal.valueOf(sheet.getRow(i).getCell(9).getNumericCellValue()) : BigDecimal.ZERO);
                    newEntity.setARTday(sheet.getRow(i).getCell(10).getCellType() == Cell.CELL_TYPE_NUMERIC ? BigDecimal.valueOf(sheet.getRow(i).getCell(10).getNumericCellValue()) : BigDecimal.ZERO);
                    newEntity.setBeginAR(sheet.getRow(i).getCell(11).getCellType() == Cell.CELL_TYPE_NUMERIC ? BigDecimal.valueOf(sheet.getRow(i).getCell(11).getNumericCellValue()) : BigDecimal.ZERO);
                    newEntity.setEndAR(sheet.getRow(i).getCell(12).getCellType() == Cell.CELL_TYPE_NUMERIC ? BigDecimal.valueOf(sheet.getRow(i).getCell(12).getNumericCellValue()) : BigDecimal.ZERO);
                    newEntity.setOverdueAccount(sheet.getRow(i).getCell(13).getCellType() == Cell.CELL_TYPE_NUMERIC ? BigDecimal.valueOf(sheet.getRow(i).getCell(13).getNumericCellValue()) : BigDecimal.ZERO);
                    newEntity.setOpeCass(sheet.getRow(i).getCell(14).getCellType() == Cell.CELL_TYPE_NUMERIC ? BigDecimal.valueOf(sheet.getRow(i).getCell(14).getNumericCellValue()) : BigDecimal.ZERO);
                    newEntity.setSaleInTax(sheet.getRow(i).getCell(15).getCellType() == Cell.CELL_TYPE_NUMERIC ? BigDecimal.valueOf(sheet.getRow(i).getCell(15).getNumericCellValue()) : BigDecimal.ZERO);
                    newEntity.setBillAR3(sheet.getRow(i).getCell(16).getCellType() == Cell.CELL_TYPE_NUMERIC ? BigDecimal.valueOf(sheet.getRow(i).getCell(16).getNumericCellValue()) : BigDecimal.ZERO);
                    newEntity.setBillAR6(sheet.getRow(i).getCell(17).getCellType() == Cell.CELL_TYPE_NUMERIC ? BigDecimal.valueOf(sheet.getRow(i).getCell(17).getNumericCellValue()) : BigDecimal.ZERO);
                    newEntity.setCashAR(sheet.getRow(i).getCell(18).getCellType() == Cell.CELL_TYPE_NUMERIC ? BigDecimal.valueOf(sheet.getRow(i).getCell(18).getNumericCellValue()) : BigDecimal.ZERO);
                    newEntity.setBenchmarkARTday(sheet.getRow(i).getCell(19).getCellType() == Cell.CELL_TYPE_NUMERIC ? BigDecimal.valueOf(sheet.getRow(i).getCell(19).getNumericCellValue()) : BigDecimal.ZERO);
                    newEntity.setBenchmarkOpeCass(sheet.getRow(i).getCell(20).getCellType() == Cell.CELL_TYPE_NUMERIC ? BigDecimal.valueOf(sheet.getRow(i).getCell(20).getNumericCellValue()) : BigDecimal.ZERO);
                    addlist.add(newEntity);
                    sortid++;
                }
                //导入
                if (!addlist.isEmpty()) {
                    List<AccountsReceivables> delList = accountsReceivablesBean.findByCompanyAndSeqAndMon(newEntity);
                    if (!delList.isEmpty()) {
                        accountsReceivablesBean.delete(delList);
                    }
                    for (AccountsReceivables item : addlist) {
                        accountsReceivablesBean.persist(item);
                    }
                    showInfoMsg("Info", "数据导入成功");
                }
            } catch (Exception ex) {
                showErrorMsg("Error", "导入失败,找不到文件或格式错误--第" + a + "行附近栏位发生错误" + ex.toString());
                System.out.println(a);
            }
        }
        //数据上传完之后继续执行更新
        updateToTurnoverdays();
    }

    /**
     * @description 更新到现金周期指标的表里去
     * @remark 资金回收率的本月值=本年累计值
     */
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
            List<AccountsReceivables> receivablesList = accountsReceivablesBean.findByCompanyAndSeqAndMon(queryCompany, y, m);
            if (receivablesList.isEmpty()) {
                showErrorMsg("Error", "请先上传本月的数据");
                return;
            }
            //用指标关联的形式确认指标名称
            Indicator indicator;
            //透过ID找到对应的指标
            indicator = indicatorBean.findById(requestId);
            if (indicator == null) {
                showErrorMsg("Error", "未找到指标");
                return;
            }
            //确认指标中的关联指标
            String[] arr = indicator.getAssociatedIndicator().split(";");
            //D-应收账款周转的指标
            Indicator indicator1 = indicatorBean.findByFormidYearAndDeptno(arr[0], y, indicator.getDeptno());
            //D-应收账款周转指标的集合
            List<Indicator> indicator1List1 = indicatorBean.findByPId(indicator1.getId());
            if (!indicator1List1.isEmpty() && !receivablesList.isEmpty()) {
                for (Indicator i : indicator1List1) {
                    for (AccountsReceivables r : receivablesList) {
                        if (r.getDeptno().trim().equals(i.getDeptno().trim())) {
                            switch (m) {
                                case 1:
                                    i.getActualIndicator().setN01(r.getARTday());
                                    break;
                                case 2:
                                    i.getActualIndicator().setN02(r.getARTday());
                                    break;
                                case 3:
                                    i.getActualIndicator().setN03(r.getARTday());
                                    break;
                                case 4:
                                    i.getActualIndicator().setN04(r.getARTday());
                                    break;
                                case 5:
                                    i.getActualIndicator().setN05(r.getARTday());
                                    break;
                                case 6:
                                    i.getActualIndicator().setN06(r.getARTday());
                                    break;
                                case 7:
                                    i.getActualIndicator().setN07(r.getARTday());
                                    break;
                                case 8:
                                    i.getActualIndicator().setN08(r.getARTday());
                                    break;
                                case 9:
                                    i.getActualIndicator().setN09(r.getARTday());
                                    break;
                                case 10:
                                    i.getActualIndicator().setN10(r.getARTday());
                                    break;
                                case 11:
                                    i.getActualIndicator().setN11(r.getARTday());
                                    break;
                                case 12:
                                    i.getActualIndicator().setN12(r.getARTday());
                                    break;
                                default:
                                    break;
                            }
                            indicatorBean.update(i);
                        }
                    }
                }
                showInfoMsg("Info", "更新周转天数指标数据成功！");
            }
            //资金回收率 R-总产品资金回收指标
            Indicator indicator2 = indicatorBean.findByFormidYearAndDeptno(arr[1], y, indicator.getDeptno());
            //R-总产品资金回收指标的集合
            List<Indicator> indicator1List2 = indicatorBean.findByPId(indicator2.getId());
            if (!indicator1List2.isEmpty() && !receivablesList.isEmpty()) {
                for (Indicator i : indicator1List2) {
                    for (AccountsReceivables r : receivablesList) {
                        if (r.getDeptno().trim().equals(i.getDeptno().trim())) {
                            switch (m) {
                                case 1:
                                    i.getActualIndicator().setN01(r.getOpeCass());
                                    i.getOther1Indicator().setN01(r.getOpeCass());
                                    break;
                                case 2:
                                    i.getActualIndicator().setN02(r.getOpeCass());
                                    i.getOther1Indicator().setN02(r.getOpeCass());
                                    break;
                                case 3:
                                    i.getActualIndicator().setN03(r.getOpeCass());
                                    i.getOther1Indicator().setN02(r.getOpeCass());
                                    break;
                                case 4:
                                    i.getActualIndicator().setN04(r.getOpeCass());
                                    i.getOther1Indicator().setN02(r.getOpeCass());
                                    break;
                                case 5:
                                    i.getActualIndicator().setN05(r.getOpeCass());
                                    i.getOther1Indicator().setN02(r.getOpeCass());
                                    break;
                                case 6:
                                    i.getActualIndicator().setN06(r.getOpeCass());
                                    i.getOther1Indicator().setN02(r.getOpeCass());
                                    break;
                                case 7:
                                    i.getActualIndicator().setN07(r.getOpeCass());
                                    i.getOther1Indicator().setN02(r.getOpeCass());
                                    break;
                                case 8:
                                    i.getActualIndicator().setN08(r.getOpeCass());
                                    i.getOther1Indicator().setN02(r.getOpeCass());
                                    break;
                                case 9:
                                    i.getActualIndicator().setN09(r.getOpeCass());
                                    i.getOther1Indicator().setN02(r.getOpeCass());
                                    break;
                                case 10:
                                    i.getActualIndicator().setN10(r.getOpeCass());
                                    i.getOther1Indicator().setN02(r.getOpeCass());
                                    break;
                                case 11:
                                    i.getActualIndicator().setN11(r.getOpeCass());
                                    i.getOther1Indicator().setN02(r.getOpeCass());
                                    break;
                                case 12:
                                    i.getActualIndicator().setN12(r.getOpeCass());
                                    i.getOther1Indicator().setN02(r.getOpeCass());
                                    break;
                                default:
                                    break;
                            }
                            indicatorBean.update(i);
                        }
                    }
                }
                showInfoMsg("Info", "更新资金回收率指标数据成功！");
            }
        } catch (Exception ex) {
            showErrorMsg("Error", "捕获异常，更新数据失败，请重试！");
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
