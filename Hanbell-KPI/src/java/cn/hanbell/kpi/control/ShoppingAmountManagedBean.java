/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.hanbell.kpi.control;

import cn.hanbell.kpi.comm.MailNotification;
import cn.hanbell.kpi.comm.MailNotify;
import cn.hanbell.kpi.ejb.IndicatorBean;
import cn.hanbell.kpi.ejb.IndicatorChartBean;
import cn.hanbell.kpi.ejb.IndicatorDetailBean;
import cn.hanbell.kpi.ejb.SalesTableBean;
import cn.hanbell.kpi.ejb.SalesTableUpdateBean;
import cn.hanbell.kpi.ejb.ShoppingAccomuntBean;
import cn.hanbell.kpi.ejb.ShoppingManufacturerBean;
import cn.hanbell.kpi.ejb.ShoppingTableBean;
import cn.hanbell.kpi.entity.Indicator;
import cn.hanbell.kpi.entity.IndicatorChart;
import cn.hanbell.kpi.entity.IndicatorDetail;
import cn.hanbell.kpi.entity.MailSetting;
import cn.hanbell.kpi.entity.SalesTable;
import cn.hanbell.kpi.entity.ShoppingManufacturer;
import cn.hanbell.kpi.entity.ShoppingTable;
import cn.hanbell.kpi.lazy.ScorecardContentModel;
import cn.hanbell.kpi.lazy.ShoppingMenuWeightModel;
import cn.hanbell.kpi.lazy.ShoppingTableModel;
import cn.hanbell.kpi.web.SuperSingleBean;
import cn.hanbell.util.BaseLib;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Serializable;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Timer;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

/**
 *
 * @author C1879
 */
@ManagedBean(name = "shoppingAmountManagedBean")
@ViewScoped
public class ShoppingAmountManagedBean extends SuperSingleBean<ShoppingTable> {

    @EJB
    private ShoppingTableBean shoppingTableBean;
    @EJB
    private ShoppingAccomuntBean shoppoingAccoumuntBean;
    protected Date btnDate;

    private List<ShoppingTable> shbList;
    private List<ShoppingTable> thbList;
    private List<ShoppingTable> hsList;
    private List<ShoppingTable> scmList;
    private List<ShoppingTable> zcmList;
    private List<ShoppingTable> hyList;

    @EJB
    private ShoppingManufacturerBean shoppingManufacturerBean;
    @EJB
    private IndicatorDetailBean indicatorDetailBean;
    @EJB
    private IndicatorBean indicatorBean;
    int y;
    int m;

    public ShoppingAmountManagedBean() {
        super(ShoppingTable.class);
    }

    @Override
    public void init() {
        btnDate = userManagedBean.getBaseDate();
        model = new ShoppingTableModel(shoppingTableBean);
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
        init();
        super.construct();
    }

    public void download() {
        try {
            shoppingTableBean.deleteByYearmon(BaseLib.formatDate("yyyyMM", btnDate));
            List<Object[]> shbList1 = shoppoingAccoumuntBean.getShbDateDetail("C", this.btnDate, "", "");
            persist(shbList1);
            List hsList1 = shoppoingAccoumuntBean.getShbDateDetail("H", this.btnDate, "", "");
            persist(hsList1);
            List scmList1 = shoppoingAccoumuntBean.getShbDateDetail("K", this.btnDate, "", "");
            persist(scmList1);
            List zcmList1 = shoppoingAccoumuntBean.getShbDateDetail("E", this.btnDate, "", "");
            persist(zcmList1);
            List hyList1 = shoppoingAccoumuntBean.getShbDateDetail("Y", this.btnDate, "", "");
            persist(hyList1);
            this.showInfoMsg("信息", "下载成功！");
        } catch (Exception e) {
            e.printStackTrace();
            this.showInfoMsg("信息", "下载失败！");
        }
    }

    public void persist(List<Object[]> list) {
        for (Object[] o : list) {
            ShoppingTable sb = new ShoppingTable();
            sb.setYearmon(BaseLib.formatDate("yyyyMM", btnDate));
            sb.setFacno((String) o[0]);
            sb.setVdrno((String) o[1]);
            sb.setVdrna((String) o[2]);
            sb.setItnbr((String) o[3]);
            sb.setItdsc((String) o[4]);
            sb.setAcpamt((BigDecimal) o[5]);
            sb.setPayqty((BigDecimal) o[6]);
            sb.setUserno((String) o[7]);
            sb.setItcls((String) o[8]);
            if ((String) o[6] != null && !"".equals((String) o[6])) {
                sb.setIscenter(Boolean.TRUE);
            } else {
                sb.setIscenter(false);
            }
            shoppingTableBean.persist(sb);
        }
    }

    public void update() {
        Calendar cal = Calendar.getInstance();
        cal.setTime(btnDate);
        y = cal.get(Calendar.YEAR);
        m = cal.get(Calendar.MONTH) + 1;
        shbList = shoppingTableBean.findByFacnoAndYearmon("C", BaseLib.formatDate("yyyyMM", btnDate));
        thbList = shoppingTableBean.findByFacnoAndYearmon("A", BaseLib.formatDate("yyyyMM", btnDate));
        hsList = shoppingTableBean.findByFacnoAndYearmon("H", BaseLib.formatDate("yyyyMM", btnDate));
        scmList = shoppingTableBean.findByFacnoAndYearmon("K", BaseLib.formatDate("yyyyMM", btnDate));
        zcmList = shoppingTableBean.findByFacnoAndYearmon("E", BaseLib.formatDate("yyyyMM", btnDate));
        hyList = shoppingTableBean.findByFacnoAndYearmon("Y", BaseLib.formatDate("yyyyMM", btnDate));
        try {
            //先更新分类。other指标
            initSHBAmount();
            initHsAmount();
            initThbAmount();
            initScmAmount();
            initZcmAmount();
            initHyAmount();
            //利用other更新全部
            List<Indicator> list = indicatorBean.findByCategoryAndYear("物料采购金额", y);
            for (Indicator entity : list) {
                indicatorBean.updateActual(entity.getId(), cal.get(Calendar.YEAR), cal.get(Calendar.MONTH) + 1,
                        cal.getTime(), Calendar.MONTH);
            }
            FacesContext.getCurrentInstance().addMessage((String) null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Info", "更新成功！"));
        } catch (Exception ex) {
            FacesContext.getCurrentInstance().addMessage((String) null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Info", "更新失败！"));
            ex.printStackTrace();
            Logger.getLogger(ShoppingAmountManagedBean.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * 导出明细报表
     *
     * @throws Exception
     */
    public void print() throws Exception {
        fileName = "采购中心各分公司明细" + com.lightshell.comm.BaseLib.formatDate("yyyyMMddHHmmss", com.lightshell.comm.BaseLib.getDate()) + ".xls";
        String fileFullName = reportOutputPath + fileName;
        HSSFWorkbook wb = new HSSFWorkbook();
        CreationHelper createHelper = wb.getCreationHelper();
        CellStyle cellStyle = wb.createCellStyle();
        Map map = new LinkedHashMap<String, List<Object[]>>();
        map.put("上海汉钟采购明细", shbList);
        map.put("上海柯茂采购明细", scmList);
        map.put("浙江柯茂采购明细", zcmList);
        map.put("浙江汉声采购明细", hsList);
        map.put("安徽汉扬采购明细", hyList);
        map.put("台湾汉钟采购明细", thbList);

        Iterator iter = map.entrySet().iterator();
        while (iter.hasNext()) {
            Map.Entry entry = (Map.Entry) iter.next();

            //创建内容
            Sheet sheet = wb.createSheet((String) entry.getKey());
            Cell cell;
            Row row;
            row = sheet.createRow(0);
            row.createCell(0).setCellValue("验收单号");
            row.createCell(1).setCellValue("采购单号");
            row.createCell(2).setCellValue("公司别");
            row.createCell(3).setCellValue("厂商编号");
            row.createCell(4).setCellValue("厂商名称");
            row.createCell(5).setCellValue("品号");
            row.createCell(6).setCellValue("品名");
            row.createCell(7).setCellValue("金额");
            row.createCell(8).setCellValue("品号大类");
            row.createCell(9).setCellValue("物料分类");
            int i = 1;
            List<Object[]> val = (List<Object[]>) entry.getValue();
            for (Object[] e : val) {
                row = sheet.createRow(i);
                row.createCell(0).setCellValue((String) e[9]);
                row.createCell(1).setCellValue((String) e[10]);
                row.createCell(2).setCellValue((String) e[0]);
                row.createCell(3).setCellValue((String) e[1]);
                row.createCell(4).setCellValue((String) e[2]);
                row.createCell(5).setCellValue((String) e[3]);
                row.createCell(6).setCellValue((String) e[4]);
                row.createCell(7).setCellValue(((BigDecimal) e[5]).doubleValue());
                row.createCell(8).setCellValue((String) e[6]);
                row.createCell(9).setCellValue((String) e[8]);
                i++;
            }
        }
        OutputStream os = null;
        try {
            os = new FileOutputStream(fileFullName);
            wb.write(os);
            this.reportViewPath = reportViewContext + fileName;
            this.preview();
        } catch (Exception ex) {
            showInfoMsg("Warn", "导出失败" + ex);
        } finally {
            try {
                if (null != os) {
                    os.flush();
                    os.close();
                }
            } catch (IOException ex) {
                showInfoMsg("Warn", "导出失败" + ex);

            }
        }
    }

    public List<String> getItclsString(String itcls) {
        List<String> list = new ArrayList<>();
        StringTokenizer stzj = new StringTokenizer(itcls, "/");
        while (stzj.hasMoreTokens()) {
            list.add(stzj.nextToken());
        }
        return list;
    }

    public List<String> getmaterialTypeName(String facno, String materialTypeName) {
        List<String> list = new ArrayList<>();
        shoppingManufacturerBean.findByMaterialTypeName(facno, materialTypeName).forEach(m -> list.add(m.getVdrno()));
        return list;
    }

    private void initSHBAmount() throws NoSuchMethodException, Exception {
        List<String> zjList = getItclsString(ShoppingAccomuntBean.SHB_ITCLS_ZHUJIA);
        List<String> djList = getItclsString(ShoppingAccomuntBean.SHB_ITCLS_DIANJI);//电机
        List<String> zcList = getItclsString(ShoppingAccomuntBean.SHB_ITCLS_ZHOUCHENG);
        List<String> ypList = getItclsString(ShoppingAccomuntBean.SHB_ITCLS_YOUPING);
        List<String> zzList = getItclsString(ShoppingAccomuntBean.SHB_ITCLS_ZHUANZI);
        List<String> flList = getItclsString(ShoppingAccomuntBean.SHB_ITCLS_FALEI);
        List<String> dj1List = getItclsString(ShoppingAccomuntBean.SHB_ITCLS_DAOJU);//刀具
        List<String> cdList = getItclsString(ShoppingAccomuntBean.SHB_ITCLS_CHENGDIAN);
        List<String> jxgbList = getItclsString(ShoppingAccomuntBean.SHB_FACT_JIEXIANGAIBAN);
        List<String> jxhList = getItclsString(ShoppingAccomuntBean.SHB_FACT_JIEXIANGHE);
        List<String> mjList = getItclsString(ShoppingAccomuntBean.SHB_ITCLS_MOJU);
        List<String> clList = getItclsString(ShoppingAccomuntBean.SHB_ITCLS_CHILUN);
        List<String> lcList = getItclsString(ShoppingAccomuntBean.SHB_ITCLS_LVCAI);
        Iterator<ShoppingTable> iterator = shbList.iterator();
        while (iterator.hasNext()) {
            try {
                ShoppingTable o = iterator.next();
                //优先处理厂商配比,后处理品号大类的
                if (jxgbList.contains(o.getItcls())) {
                    o.setType("接线盖板");
                } else if (jxhList.contains(o.getItcls())) {
                    o.setType("接线盒");
                } else if (zjList.contains(o.getItcls())) {
                    o.setType("铸加");
                } else if (djList.contains(o.getItcls())) {
                    o.setType("电机");
                } else if (zcList.contains(o.getItcls())) {
                    o.setType("轴承");
                } else if (ypList.contains(o.getItcls())) {
                    o.setType("油品");
                } else if (zzList.contains(o.getItcls())) {
                    o.setType("转子");
                } else if (flList.contains(o.getItcls())) {
                    o.setType("阀类");
                } else if (dj1List.contains(o.getItcls())) {
                    o.setType("刀具");
                } else if (cdList.contains(o.getItcls())) {
                    o.setType("衬垫");
                } else if (mjList.contains(o.getItcls())) {
                    o.setType("模具");
                } else if (clList.contains(o.getItcls())) {
                    o.setType("齿轮");
                } else if (lcList.contains(o.getItcls())) {
                    o.setType("滤材");
                } else {
                    o.setType("其他");
                }
                o.setIscenter(true);
            } catch (Exception e) {
                throw e;
            }
        }
        shoppingTableBean.update(shbList);
    }

    private void initHsAmount() throws NoSuchMethodException, Exception {
        List<String> zjList = getItclsString(ShoppingAccomuntBean.HS_ITCLS_ZHUJIA);
        List<String> zzList = getItclsString(ShoppingAccomuntBean.HS_ITCLS_ZHUANZI);
        List<String> mjList = getItclsString(ShoppingAccomuntBean.HS_ITCLS_MOJU);
        List<String> djList = getItclsString(ShoppingAccomuntBean.HS_ITCLS_DAOJU);
        Iterator<ShoppingTable> iterator = hsList.iterator();
        while (iterator.hasNext()) {
            try {
                ShoppingTable o = iterator.next();
                if (zjList.contains(o.getItcls())) {
                    o.setType("铸加");
                } else if (zzList.contains(o.getItcls())) {
                    o.setType("转子");
                } else if (mjList.contains(o.getItcls())) {
                    o.setType("模具");
                } else if (djList.contains(o.getItcls())) {
                    o.setType("刀具");
                } else {
                    o.setType("其他");
                }
                o.setIscenter(true);
            } catch (Exception e) {
                throw e;
            }
        }
        shoppingTableBean.update(hsList);
    }

    private void initThbAmount() throws NoSuchMethodException, Exception {
        List<String> zjList = getmaterialTypeName("A", ShoppingAccomuntBean.THB_FACT_ZHUJIA);
        List<String> djList = getmaterialTypeName("A", ShoppingAccomuntBean.THB_FACT_DIANJI);
        List<String> zcList = getmaterialTypeName("A", ShoppingAccomuntBean.THB_FACT_ZHOUCHENG);
        List<String> ypList = getmaterialTypeName("A", ShoppingAccomuntBean.THB_FACT_YOUPING);
        List<String> jk1List = getmaterialTypeName("A", ShoppingAccomuntBean.THB_FACT_JINGKOU);
        List<String> zzList = getItclsString(ShoppingAccomuntBean.THB_ITCLS_ZHUANZI);
        List<String> flList = getmaterialTypeName("A", ShoppingAccomuntBean.THB_FACT_FALEI);
        List<String> dj1List = getmaterialTypeName("A", ShoppingAccomuntBean.THB_FACT_DAOJU);
        List<String> cdList = getmaterialTypeName("A", ShoppingAccomuntBean.THB_FACT_CHENGDIAN);
        List<String> jxgbList = getmaterialTypeName("A", ShoppingAccomuntBean.THB_FACT_JIEXIANGAIBAN);
        Iterator<ShoppingTable> iterator = thbList.iterator();
        while (iterator.hasNext()) {
            try {
                ShoppingTable o = iterator.next();
                //台湾汉钟优先处理转子
                if (zzList.contains(o.getItcls())) {
                    o.setType("转子");
                } else if (zjList.contains(o.getVdrno())) {
                    o.setType("铸加");
                } else if (djList.contains(o.getVdrno())) {
                    o.setType("电机");
                } else if (zcList.contains(o.getVdrno())) {
                    o.setType("轴承");
                } else if (ypList.contains(o.getVdrno())) {
                    o.setType("油品");
                } else if (jk1List.contains(o.getVdrno())) {
                    o.setType("进口1");
                } else if (flList.contains(o.getVdrno())) {
                    o.setType("阀类");
                } else if (dj1List.contains(o.getVdrno())) {
                    o.setType("刀具");
                } else if (cdList.contains(o.getVdrno())) {
                    o.setType("衬垫");
                } else if (jxgbList.contains(o.getVdrno())) {
                    o.setType("接线盖板");
                } else {
                    o.setType("其他");
                }
                o.setIscenter(true);
            } catch (Exception e) {
                throw e;
            }
        }
        shoppingTableBean.update(thbList);
    }

    private void initScmAmount() throws NoSuchMethodException, Exception {
        List<String> zjList = getItclsString(ShoppingAccomuntBean.SCM_ITCLS_ZHUANZI);
        List<String> djList = getItclsString(ShoppingAccomuntBean.SCM_ITCLS_DIANJI);
        List<String> zcList = getItclsString(ShoppingAccomuntBean.SCM_ITCLS_ZHOUCHENG);
        List<String> mjList = getItclsString(ShoppingAccomuntBean.SCM_ITCLS_MOJU);
        List<String> cdList = getItclsString(ShoppingAccomuntBean.SCM_ITCLS_CHENGDIAN);
        Iterator<ShoppingTable> iterator = scmList.iterator();
        while (iterator.hasNext()) {
            try {
                ShoppingTable o = iterator.next();
                if (zjList.contains(o.getItcls())) {
                    o.setType("铸加");
                } else if (djList.contains(o.getItcls())) {
                    o.setType("电机");
                } else if (zcList.contains(o.getItcls())) {
                    o.setType("轴承");
                } else if (mjList.contains(o.getItcls())) {
                    o.setType("模具");
                } else if (cdList.contains(o.getItcls())) {
                    o.setType("衬垫");
                } else {
                    o.setType("其他");
                }
                o.setIscenter(true);
            } catch (Exception e) {
                throw e;
            }
        }
        shoppingTableBean.update(scmList);
    }

    private void initZcmAmount() throws NoSuchMethodException, Exception {
        Iterator<ShoppingTable> iterator = zcmList.iterator();
        while (iterator.hasNext()) {
            try {
                ShoppingTable o = iterator.next();
                o.setType("其他");
                o.setIscenter(true);
            } catch (Exception e) {
                throw e;
            }
        }
        shoppingTableBean.update(zcmList);
    }

    private void initHyAmount() throws NoSuchMethodException, Exception {
        List<String> zjList = getItclsString(ShoppingAccomuntBean.HY_ITCLS_ZHUJIA);
        List<String> djList = getItclsString(ShoppingAccomuntBean.HY_ITCLS_DAOJU);
        Iterator<ShoppingTable> iterator = hyList.iterator();
        while (iterator.hasNext()) {
            try {
                ShoppingTable o = iterator.next();
                if (zjList.contains(o.getItcls())) {
                    o.setType("铸加");
                } else if (djList.contains(o.getItcls())) {
                    o.setType("刀具");
                } else {
                    o.setType("其他");
                }
                o.setIscenter(true);
            } catch (Exception e) {
                throw e;
            }
        }
        shoppingTableBean.update(hyList);
    }

    public boolean updateIndicatorDetail(IndicatorDetail detail, BigDecimal account) throws Exception {
        Method setMethod = detail.getClass()
                .getDeclaredMethod("set" + getIndicatorColumn("N", m).toUpperCase(), BigDecimal.class);
        setMethod.invoke(detail, account);
        indicatorDetailBean.update(detail);
        return true;

    }

    public String getIndicatorColumn(String formtype, int m) {
        if (formtype.equals("N")) {
            return "n" + String.format("%02d", m);
        } else if (formtype.equals("D")) {
            return "d" + String.format("%02d", m);
        } else if (formtype.equals("NQ")) {
            return "nq" + String.valueOf(m);
        } else {
            return "";

        }
    }

    /**
     * @return the btnDate
     */
    public Date getBtnDate() {
        return btnDate;
    }

    /**
     * @param btnDate the btnDate to set
     */
    public void setBtnDate(Date btnDate) {
        this.btnDate = btnDate;
    }
}
