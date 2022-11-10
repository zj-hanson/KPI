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

    private List<Object[]> shbList;
    private List<Object[]> thbList;
    private List<Object[]> hsList;
    private List<Object[]> scmList;
    private List<Object[]> zcmList;
    private List<Object[]> hyList;

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
            sb.setItcls((String) o[6]);
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
                System.out.println(entity.getFormid() + "更新成功");
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
        BigDecimal jxgbSum = new BigDecimal(0);
        BigDecimal jxhSum = new BigDecimal(0);
        BigDecimal zjSum = new BigDecimal(0);
        BigDecimal djSum = new BigDecimal(0);
        BigDecimal zcSum = new BigDecimal(0);
        BigDecimal ypSum = new BigDecimal(0);
        BigDecimal zzSum = new BigDecimal(0);
        BigDecimal flSum = new BigDecimal(0);
        BigDecimal dj1Sum = new BigDecimal(0);
        BigDecimal cdSum = new BigDecimal(0);
        BigDecimal mjSum = new BigDecimal(0);
        BigDecimal qtSum = new BigDecimal(0);
        BigDecimal clSum = new BigDecimal(0);
        BigDecimal lcSum = new BigDecimal(0);
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
             
        Iterator<Object[]> iterator = shbList.iterator();
        while (iterator.hasNext()) {
            try {
                Object[] o = iterator.next();
                //优先处理厂商配比,后处理品号大类的
                if (jxgbList.contains((String) o[6])) {
                    o[8] = "接线盖板";
                    jxgbSum = jxgbSum.add((BigDecimal) o[5]);
                } else if (jxhList.contains((String) o[6])) {
                    o[8] = "接线盒";
                    jxhSum = jxhSum.add((BigDecimal) o[5]);
                } else if (zjList.contains((String) o[6])) {
                    o[8] = "铸加";
                    zjSum = zjSum.add((BigDecimal) o[5]);
                } else if (djList.contains((String) o[6])) {
                    o[8] = "电机";
                    djSum = djSum.add((BigDecimal) o[5]);
                } else if (zcList.contains((String) o[6])) {
                    o[8] = "轴承";
                    zcSum = zcSum.add((BigDecimal) o[5]);
                } else if (ypList.contains((String) o[6])) {
                    o[8] = "油品";
                    ypSum = ypSum.add((BigDecimal) o[5]);
                } else if (zzList.contains((String) o[6])) {
                    o[8] = "转子";
                    zzSum = zzSum.add((BigDecimal) o[5]);
                } else if (flList.contains((String) o[6])) {
                    o[8] = "阀类";
                    flSum = flSum.add((BigDecimal) o[5]);
                } else if (dj1List.contains((String) o[6])) {
                    o[8] = "刀具";
                    dj1Sum = dj1Sum.add((BigDecimal) o[5]);
                } else if (cdList.contains((String) o[6])) {
                    o[8] = "衬垫";
                    cdSum = cdSum.add((BigDecimal) o[5]);
                } else if (mjList.contains((String) o[6])) {
                    o[8] = "模具";
                    mjSum = mjSum.add((BigDecimal) o[5]);
                }  else if (clList.contains((String) o[6])) {
                    o[8] = "齿轮";
                    clSum = clSum.add((BigDecimal) o[5]);
                }else if (lcList.contains((String) o[6])) {
                    o[8] = "滤材";
                    lcSum = lcSum.add((BigDecimal) o[5]);
                }else {
                    o[8] = "其他";
                    qtSum = qtSum.add((BigDecimal) o[5]);
                }
            } catch (Exception e) {
                throw e;
            }
        }
        updateIndicatorDetail(indicatorBean.findByFormidYearAndDeptno("A-铸加类采购金额", y, "1X000").getOther1Indicator(), zjSum);
        updateIndicatorDetail(indicatorBean.findByFormidYearAndDeptno("A-电机类采购金额", y, "1X000").getOther1Indicator(), djSum);
        updateIndicatorDetail(indicatorBean.findByFormidYearAndDeptno("A-轴承类采购金额", y, "1X000").getOther1Indicator(), zcSum);
        updateIndicatorDetail(indicatorBean.findByFormidYearAndDeptno("A-油品类采购金额", y, "1X000").getOther1Indicator(), ypSum);
        updateIndicatorDetail(indicatorBean.findByFormidYearAndDeptno("A-转子类采购金额", y, "1X000").getOther1Indicator(), zzSum);
        updateIndicatorDetail(indicatorBean.findByFormidYearAndDeptno("A-阀类采购金额", y, "1X000").getOther1Indicator(), flSum);
        updateIndicatorDetail(indicatorBean.findByFormidYearAndDeptno("A-刀具类采购金额", y, "1X000").getOther1Indicator(), dj1Sum);
        updateIndicatorDetail(indicatorBean.findByFormidYearAndDeptno("A-衬垫类采购金额", y, "1X000").getOther1Indicator(), cdSum);
        updateIndicatorDetail(indicatorBean.findByFormidYearAndDeptno("A-接线盖板类采购金额", y, "1X000").getOther1Indicator(), jxgbSum);
        updateIndicatorDetail(indicatorBean.findByFormidYearAndDeptno("A-接线盒类采购金额", y, "1X000").getOther1Indicator(), jxhSum);
        updateIndicatorDetail(indicatorBean.findByFormidYearAndDeptno("A-模具类采购金额", y, "1X000").getOther1Indicator(), mjSum);
        updateIndicatorDetail(indicatorBean.findByFormidYearAndDeptno("A-其他类采购金额", y, "1X000").getOther1Indicator(), qtSum);
         updateIndicatorDetail(indicatorBean.findByFormidYearAndDeptno("A-模具类采购金额", y, "1X000").getOther1Indicator(), mjSum);
        updateIndicatorDetail(indicatorBean.findByFormidYearAndDeptno("A-其他类采购金额", y, "1X000").getOther1Indicator(), qtSum);
        updateIndicatorDetail(indicatorBean.findByFormidYearAndDeptno("A-齿轮类采购金额", y, "1X000").getOther1Indicator(), clSum); 
        updateIndicatorDetail(indicatorBean.findByFormidYearAndDeptno("A-滤材类采购金额", y, "1X000").getOther1Indicator(), lcSum);
    }

    private void initHsAmount() throws NoSuchMethodException, Exception {
        BigDecimal zjSum = new BigDecimal(0);
        BigDecimal zzSum = new BigDecimal(0);
        BigDecimal mjSum = new BigDecimal(0);
        BigDecimal qtSum = new BigDecimal(0);
          BigDecimal djSum = new BigDecimal(0);
        List<String> zjList = getItclsString(ShoppingAccomuntBean.HS_ITCLS_ZHUJIA);
        List<String> zzList = getItclsString(ShoppingAccomuntBean.HS_ITCLS_ZHUANZI);
        List<String> mjList = getItclsString(ShoppingAccomuntBean.HS_ITCLS_MOJU);
        List<String> djList = getItclsString(ShoppingAccomuntBean.HS_ITCLS_DAOJU);
        Iterator<Object[]> iterator = hsList.iterator();
        while (iterator.hasNext()) {
            try {
                Object[] o = iterator.next();
                if (zjList.contains((String) o[6])) {
                    o[8] = "铸加";
                    zjSum = zjSum.add((BigDecimal) o[5]);
                } else if (zzList.contains((String) o[6])) {
                    o[8] = "转子";
                    zzSum = zzSum.add((BigDecimal) o[5]);
                } else if (mjList.contains((String) o[6])) {
                    o[8] = "模具";
                    mjSum = mjSum.add((BigDecimal) o[5]);
                } else if (djList.contains((String) o[6])) {
                    o[8] = "刀具";
                    djSum = djSum.add((BigDecimal) o[5]);
                } else {
                    o[8] = "其他";
                    qtSum = qtSum.add((BigDecimal) o[5]);
                }

            } catch (Exception e) {
                throw e;
            }
        }
        updateIndicatorDetail(indicatorBean.findByFormidYearAndDeptno("A-铸加类采购金额", y, "1X000").getOther2Indicator(), zjSum);
        updateIndicatorDetail(indicatorBean.findByFormidYearAndDeptno("A-刀具类采购金额", y, "1X000").getOther2Indicator(), djSum);
        updateIndicatorDetail(indicatorBean.findByFormidYearAndDeptno("A-转子类采购金额", y, "1X000").getOther2Indicator(), zzSum);
        updateIndicatorDetail(indicatorBean.findByFormidYearAndDeptno("A-模具类采购金额", y, "1X000").getOther2Indicator(), mjSum);
        updateIndicatorDetail(indicatorBean.findByFormidYearAndDeptno("A-其他类采购金额", y, "1X000").getOther2Indicator(), qtSum);

    }

    private void initThbAmount() throws NoSuchMethodException, Exception {
        BigDecimal jxgbSum = new BigDecimal(0);
        BigDecimal zjSum = new BigDecimal(0);
        BigDecimal djSum = new BigDecimal(0);
        BigDecimal zcSum = new BigDecimal(0);
        BigDecimal ypSum = new BigDecimal(0);
        BigDecimal jk1Sum = new BigDecimal(0);
        BigDecimal zzSum = new BigDecimal(0);
        BigDecimal flSum = new BigDecimal(0);
        BigDecimal dj1Sum = new BigDecimal(0);
        BigDecimal cdSum = new BigDecimal(0);
        BigDecimal qtSum = new BigDecimal(0);
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
        Iterator<Object[]> iterator = thbList.iterator();
        while (iterator.hasNext()) {
            try {
                Object[] o = iterator.next();
                //台湾汉钟优先处理转子
                if (zzList.contains((String) o[6])) {
                    o[8] = "转子";
                    zzSum = zzSum.add((BigDecimal) o[5]);
                } else if (zjList.contains((String) o[1])) {
                    o[8] = "铸加";
                    zjSum = zjSum.add((BigDecimal) o[5]);
                } else if (djList.contains((String) o[1])) {
                    o[8] = "电机";
                    djSum = djSum.add((BigDecimal) o[5]);
                } else if (zcList.contains((String) o[1])) {
                    o[8] = "轴承";
                    zcSum = zcSum.add((BigDecimal) o[5]);
                } else if (ypList.contains((String) o[1])) {
                    o[8] = "油品";
                    ypSum = ypSum.add((BigDecimal) o[5]);
                } else if (jk1List.contains((String) o[1])) {
                    o[8] = "进口1";
                    jk1Sum = jk1Sum.add((BigDecimal) o[5]);
                } else if (flList.contains((String) o[1])) {
                    o[8] = "阀类";
                    flSum = flSum.add((BigDecimal) o[5]);
                } else if (dj1List.contains((String) o[1])) {
                    o[8] = "刀具";
                    dj1Sum = dj1Sum.add((BigDecimal) o[5]);
                } else if (cdList.contains((String) o[1])) {
                    o[8] = "衬垫";
                    cdSum = cdSum.add((BigDecimal) o[5]);
                } else if (jxgbList.contains((String) o[1])) {
                    o[8] = "蓋板";
                    jxgbSum = jxgbSum.add((BigDecimal) o[5]);
                } else {
                    o[8] = "其他";
                    qtSum = qtSum.add((BigDecimal) o[5]);
                }

            } catch (Exception e) {
                throw e;
            }
        }
        updateIndicatorDetail(indicatorBean.findByFormidYearAndDeptno("A-铸加类采购金额", y, "1X000").getOther3Indicator(), zjSum);
        updateIndicatorDetail(indicatorBean.findByFormidYearAndDeptno("A-电机类采购金额", y, "1X000").getOther3Indicator(), djSum);
        updateIndicatorDetail(indicatorBean.findByFormidYearAndDeptno("A-轴承类采购金额", y, "1X000").getOther3Indicator(), zcSum);
        updateIndicatorDetail(indicatorBean.findByFormidYearAndDeptno("A-油品类采购金额", y, "1X000").getOther3Indicator(), ypSum);
        updateIndicatorDetail(indicatorBean.findByFormidYearAndDeptno("A-转子类采购金额", y, "1X000").getOther3Indicator(), zzSum);
        updateIndicatorDetail(indicatorBean.findByFormidYearAndDeptno("A-阀类采购金额", y, "1X000").getOther3Indicator(), flSum);
        updateIndicatorDetail(indicatorBean.findByFormidYearAndDeptno("A-刀具类采购金额", y, "1X000").getOther3Indicator(), dj1Sum);
        updateIndicatorDetail(indicatorBean.findByFormidYearAndDeptno("A-衬垫类采购金额", y, "1X000").getOther3Indicator(), cdSum);
        updateIndicatorDetail(indicatorBean.findByFormidYearAndDeptno("A-接线盖板类采购金额", y, "1X000").getOther3Indicator(), jxgbSum);
        updateIndicatorDetail(indicatorBean.findByFormidYearAndDeptno("A-进口1类采购金额", y, "1X000").getOther3Indicator(), jk1Sum);
        updateIndicatorDetail(indicatorBean.findByFormidYearAndDeptno("A-其他类采购金额", y, "1X000").getOther3Indicator(), qtSum);

    }

    private void initScmAmount() throws NoSuchMethodException, Exception {
        BigDecimal zjSum = new BigDecimal(0);
        BigDecimal djSum = new BigDecimal(0);
        BigDecimal zcSum = new BigDecimal(0);
        BigDecimal mjSum = new BigDecimal(0);
         BigDecimal cdSum = new BigDecimal(0);
        BigDecimal qtSum = new BigDecimal(0);
        List<String> zjList = getItclsString(ShoppingAccomuntBean.SCM_ITCLS_ZHUANZI);
        List<String> djList = getItclsString(ShoppingAccomuntBean.SCM_ITCLS_DIANJI);
        List<String> zcList = getItclsString(ShoppingAccomuntBean.SCM_ITCLS_ZHOUCHENG);
        List<String> mjList = getItclsString(ShoppingAccomuntBean.SCM_ITCLS_MOJU);
          List<String> cdList = getItclsString(ShoppingAccomuntBean.SCM_ITCLS_CHENGDIAN);
        Iterator<Object[]> iterator = scmList.iterator();
        while (iterator.hasNext()) {
            try {
                Object[] o = iterator.next();
                if (zjList.contains((String) o[6])) {
                    o[8] = "铸件";
                    zjSum = zjSum.add((BigDecimal) o[5]);
                } else if (djList.contains((String) o[6])) {
                    o[8] = "电机";
                    djSum = djSum.add((BigDecimal) o[5]);
                } else if (zcList.contains((String) o[6])) {
                    o[8] = "轴承";
                    zcSum = zcSum.add((BigDecimal) o[5]);
                } else if (mjList.contains((String) o[6])) {
                    o[8] = "模具";
                    mjSum = mjSum.add((BigDecimal) o[5]);
                }else if (cdList.contains((String) o[6])) {
                    o[8] = "衬垫";
                    cdSum = cdSum.add((BigDecimal) o[5]);
                }  else {
                    o[8] = "其他";
                    qtSum = qtSum.add((BigDecimal) o[5]);
                }

            } catch (Exception e) {
                throw e;
            }
        }
        updateIndicatorDetail(indicatorBean.findByFormidYearAndDeptno("A-铸加类采购金额", y, "1X000").getOther4Indicator(), zjSum);
        updateIndicatorDetail(indicatorBean.findByFormidYearAndDeptno("A-衬垫类采购金额", y, "1X000").getOther4Indicator(), cdSum);
        updateIndicatorDetail(indicatorBean.findByFormidYearAndDeptno("A-电机类采购金额", y, "1X000").getOther4Indicator(), djSum);
        updateIndicatorDetail(indicatorBean.findByFormidYearAndDeptno("A-轴承类采购金额", y, "1X000").getOther4Indicator(), zcSum);
        updateIndicatorDetail(indicatorBean.findByFormidYearAndDeptno("A-模具类采购金额", y, "1X000").getOther4Indicator(), mjSum);
        updateIndicatorDetail(indicatorBean.findByFormidYearAndDeptno("A-其他类采购金额", y, "1X000").getOther4Indicator(), qtSum);
    }

    private void initZcmAmount() throws NoSuchMethodException, Exception {
        BigDecimal qtSum = new BigDecimal(0);
        Iterator<Object[]> iterator = zcmList.iterator();
        while (iterator.hasNext()) {
            try {
                Object[] o = iterator.next();
                o[8] = "其他";
                qtSum = qtSum.add((BigDecimal) o[5]);
            } catch (Exception e) {
                throw e;
            }
        }
        updateIndicatorDetail(indicatorBean.findByFormidYearAndDeptno("A-其他类采购金额", y, "1X000").getOther5Indicator(), qtSum);
    }

    private void initHyAmount() throws NoSuchMethodException, Exception {
           BigDecimal zjSum = new BigDecimal(0);
             BigDecimal djSum = new BigDecimal(0);
        BigDecimal qtSum = new BigDecimal(0);
        
        List<String> zjList = getItclsString(ShoppingAccomuntBean.HY_ITCLS_ZHUJIA);
        List<String> djList = getItclsString(ShoppingAccomuntBean.HY_ITCLS_DAOJU);
        Iterator<Object[]> iterator = hyList.iterator();
        while (iterator.hasNext()) {
            try {
                Object[] o = iterator.next();
                if (zjList.contains((String) o[6])) {
                    o[8] = "铸件";
                    zjSum = zjSum.add((BigDecimal) o[5]);
                } else if (djList.contains((String) o[6])) {
                    o[8] = "刀具";
                    djSum = djSum.add((BigDecimal) o[5]);
                }   else {
                    o[8] = "其他";
                    qtSum = qtSum.add((BigDecimal) o[5]);
                }
            } catch (Exception e) {
                throw e;
            }
        }
        updateIndicatorDetail(indicatorBean.findByFormidYearAndDeptno("A-铸加类采购金额", y, "1X000").getOther6Indicator(), zjSum);
        updateIndicatorDetail(indicatorBean.findByFormidYearAndDeptno("A-刀具类采购金额", y, "1X000").getOther6Indicator(), djSum);
        updateIndicatorDetail(indicatorBean.findByFormidYearAndDeptno("A-其他类采购金额", y, "1X000").getOther6Indicator(), qtSum);
    }

    public boolean updateIndicatorDetail(IndicatorDetail detail, BigDecimal account) throws Exception {
//           IndicatorDetail detail1 = indicatorBean.findByFormidYearAndDeptno("A-铸加类采购金额", y, "1X000").getOther1Indicator();
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
