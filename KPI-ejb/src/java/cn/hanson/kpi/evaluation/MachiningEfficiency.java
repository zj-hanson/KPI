/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.hanson.kpi.evaluation;

import cn.hanbell.kpi.comm.Actual;
import cn.hanbell.kpi.comm.SuperEJBForERP;
import cn.hanbell.kpi.comm.SuperEJBForKPI;
import cn.hanbell.kpi.comm.SuperEJBForMES;
import cn.hanbell.kpi.ejb.IndicatorBean;
import cn.hanbell.kpi.ejb.ProcessStepBean;
import cn.hanbell.kpi.entity.Indicator;
import cn.hanbell.kpi.entity.IndicatorDaily;
import cn.hanbell.kpi.entity.IndicatorDetail;
import cn.hanbell.kpi.entity.ProcessStep;
import com.lightshell.comm.BaseLib;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.logging.Level;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.persistence.Query;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author C0160
 */
public class MachiningEfficiency implements Actual {

    protected IndicatorBean indicatorBean = lookupIndicatorBean();
    protected ProcessStepBean processStepBean = lookupProcessStepBean();
    protected SuperEJBForERP superEJBForERP = lookupSuperEJBForERP();
    protected SuperEJBForKPI superEJBForKPI = lookupSuperEJBForKPI();
    protected SuperEJBForMES superEJBForMES = lookupSuperEJBForMES();

    protected LinkedHashMap<String, Object> queryParams;

    protected final Logger log4j = LogManager.getLogger("cn.hanbell.kpi");

    public MachiningEfficiency() {
        queryParams = new LinkedHashMap<>();
    }

    @Override
    public void setEJB(String JNDIName) throws Exception {

    }

    private IndicatorBean lookupIndicatorBean() {
        try {
            Context c = new InitialContext();
            return (IndicatorBean) c.lookup("java:global/KPI/KPI-ejb/IndicatorBean!cn.hanbell.kpi.ejb.IndicatorBean");
        } catch (NamingException ne) {
            java.util.logging.Logger.getLogger(getClass().getName()).log(Level.SEVERE, "exception caught", ne);
            throw new RuntimeException(ne);
        }
    }

    private ProcessStepBean lookupProcessStepBean() {
        try {
            Context c = new InitialContext();
            return (ProcessStepBean) c
                    .lookup("java:global/KPI/KPI-ejb/ProcessStepBean!cn.hanbell.kpi.ejb.ProcessStepBean");
        } catch (NamingException ne) {
            java.util.logging.Logger.getLogger(getClass().getName()).log(Level.SEVERE, "exception caught", ne);
            throw new RuntimeException(ne);
        }
    }

    protected SuperEJBForERP lookupSuperEJBForERP() {
        try {
            Context c = new InitialContext();
            return (SuperEJBForERP) c
                    .lookup("java:global/KPI/KPI-ejb/SuperEJBForERP!cn.hanbell.kpi.comm.SuperEJBForERP");
        } catch (NamingException ne) {
            throw new RuntimeException(ne);
        }
    }

    protected SuperEJBForKPI lookupSuperEJBForKPI() {
        try {
            Context c = new InitialContext();
            return (SuperEJBForKPI) c.lookup("java:global/KPI/KPI-ejb/IndicatorBean!cn.hanbell.kpi.ejb.IndicatorBean");
        } catch (NamingException ne) {
            throw new RuntimeException(ne);
        }
    }

    protected SuperEJBForMES lookupSuperEJBForMES() {
        try {
            Context c = new InitialContext();
            return (SuperEJBForMES) c
                    .lookup("java:global/KPI/KPI-ejb/SuperEJBForMES!cn.hanbell.kpi.comm.SuperEJBForMES");
        } catch (NamingException ne) {
            throw new RuntimeException(ne);
        }
    }

    @Override
    public LinkedHashMap<String, Object> getQueryParams() {
        return queryParams;
    }

    @Override
    public int getUpdateMonth(int y, int m) {
        return m;
    }

    @Override
    public int getUpdateYear(int y, int m) {
        return y;
    }

    @Override
    public BigDecimal getValue(int y, int m, Date d, int type, LinkedHashMap<String, Object> map) {
        BigDecimal actualHour = BigDecimal.ZERO;
        BigDecimal plannedHour = BigDecimal.ZERO;
        BigDecimal standardHour = BigDecimal.ZERO;
        BigDecimal tempValue = BigDecimal.ZERO;
        Calendar c = Calendar.getInstance();
        c.setTime(d);
        int day = c.get(Calendar.DAY_OF_MONTH);
        int id = Integer.parseInt(map.get("id").toString());
        Indicator indicator = indicatorBean.findById(id);
        if (indicator == null) {
            return actualHour;
        }
        String company = indicator.getCompany();
        String machine = indicator.getProduct();
        // 每日归档
        processStepBean.delete(company, d, type, machine);
        processStepBean.getEntityManager().flush();
        List<ProcessStep> stepList = getProcessStep(company, d, type, machine);
        if (stepList != null && !stepList.isEmpty()) {
            processStepBean.save(stepList);
            processStepBean.getEntityManager().flush();
        }

        StringBuilder sb = new StringBuilder();
        sb.append("SELECT SUM(datediff(mi,TRACKINTIME,TRACKOUTTIME)) FROM PROCESS_STEP WHERE EQPID = '${machine}' ");
        sb.append(" AND year(TRACKOUTTIME)=${y} AND month(TRACKOUTTIME)=${m} ");
        switch (type) {
            case 2:
                // 月
                sb.append(" AND datepart(DAY ,TRACKOUTTIME) <= ${d} ");
                break;
            case 5:
                // 日
                sb.append(" AND datepart(DAY ,TRACKOUTTIME) = ${d} ");
                break;
            default:
                sb.append(" AND datepart(DAY ,TRACKOUTTIME) = ${d} ");
        }
        String sql = sb.toString().replace("${machine}", machine).replace("${y}", String.valueOf(y))
                .replace("${m}", String.valueOf(m)).replace("${d}", String.valueOf(day));
        superEJBForMES.setCompany(company);
        Query query = superEJBForMES.getEntityManager().createNativeQuery(sql);
        try {
            Object o = query.getSingleResult();
            actualHour = BigDecimal.valueOf(Double.parseDouble(o.toString()));

            Method setMethod = IndicatorDetail.class.getDeclaredMethod(
                    "set" + indicatorBean.getIndicatorColumn("N", m).toUpperCase(), BigDecimal.class);
            // 计划工时
            IndicatorDetail t = indicator.getTargetIndicator();
            plannedHour = updatePlannedHour(t, y, m, day, type, machine);
            setMethod.invoke(t, plannedHour);
            indicatorBean.updateIndicatorDetail(t);
            // 标准工时
            IndicatorDetail b = indicator.getBenchmarkIndicator();
            standardHour = updateStandardHour(b, y, m, day, type, machine);
            setMethod.invoke(b, standardHour);
            indicatorBean.updateIndicatorDetail(b);
            // 停机时间
            IndicatorDetail o1 = indicator.getOther1Indicator();
            if (o1 != null) {
                tempValue = updateDowntime(o1, y, m, day, type, machine);
                setMethod.invoke(o1, tempValue);
                indicatorBean.updateIndicatorDetail(o1);
            }
            // 停线时间
            IndicatorDetail o2 = indicator.getOther2Indicator();
            if (o2 != null) {
                tempValue = updateWaintingTime(o2, y, m, day, type, machine);
                setMethod.invoke(o2, tempValue);
                indicatorBean.updateIndicatorDetail(o2);
            }
            // 产出数量
            IndicatorDetail o5 = indicator.getOther5Indicator();
            if (o5 != null) {
                tempValue = updateMachiningQuantity(o5, y, m, day, type, machine);
                setMethod.invoke(o5, tempValue);
                indicatorBean.updateIndicatorDetail(o5);
            }
        } catch (Exception ex) {
            log4j.error("MachiningEfficiency.getValue()异常", ex);
        }
        return actualHour;
    }

    public List<ProcessStep> getProcessStep(String company, Date date, int type, String machine) {
        List<ProcessStep> processStepList = new ArrayList<>();
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        int y = c.get(Calendar.YEAR);
        int m = c.get(Calendar.MONTH) + 1;
        int d = c.get(Calendar.DAY_OF_MONTH);
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT PRODUCTSERIALNUMBER,PRODUCTTIME,PRODUCTORDERID,PRODUCTCOMPID,PRODUCTID,STEPID,STEPSEQ,EQPID,");
        sb.append("TRACKINTIME,TRACKOUTTIME,datediff(mi,TRACKINTIME,TRACKOUTTIME),TRACKOUTQTY,RULEID,MODIFYUSER,MODIFYTIME ");
        sb.append("  FROM PROCESS_STEP WHERE EQPID = '${machine}' AND year(TRACKOUTTIME)=${y} AND month(TRACKOUTTIME)=${m} ");
        switch (type) {
            case 2:
                // 月
                sb.append(" AND datepart(DAY ,TRACKOUTTIME) <= ${d} ");
                break;
            case 5:
                // 日
                sb.append(" AND datepart(DAY ,TRACKOUTTIME) = ${d} ");
                break;
            default:
                sb.append(" AND datepart(DAY ,TRACKOUTTIME) = ${d} ");
        }
        String sql = sb.toString().replace("${machine}", machine).replace("${y}", String.valueOf(y))
                .replace("${m}", String.valueOf(m)).replace("${d}", String.valueOf(d));
        superEJBForMES.setCompany(company);
        Query query = superEJBForMES.getEntityManager().createNativeQuery(sql);
        try {
            ProcessStep ps;
            Date time;
            List result = query.getResultList();
            if (result != null && !result.isEmpty()) {
                for (int i = 0; i < result.size(); i++) {
                    ps = new ProcessStep();
                    Object[] row = (Object[]) result.get(i);
                    ps.setCompany(company);
                    ps.setFormid(row[0].toString());
                    ps.setFormdate(BaseLib.getDate("yyyy/MM/dd", row[1].toString()));
                    ps.setManno(row[2].toString());
                    ps.setComponent(row[3].toString());
                    ps.setItemno(row[4].toString());
                    ps.setStep(row[5].toString());
                    ps.setStepSeq(Integer.parseInt(row[6].toString()));
                    ps.setEquipment(row[7].toString());
                    time = BaseLib.getDate("yyyy/MM/dd HH:mm:ss", row[8].toString());
                    if (time != null) {
                        ps.setStartTime(time);
                    }
                    time = BaseLib.getDate("yyyy/MM/dd HH:mm:ss", row[9].toString());
                    if (time != null) {
                        ps.setEndTime(time);
                    }
                    ps.setProcessingTime(BigDecimal.valueOf(Double.parseDouble(row[10].toString())));
                    ps.setQty(BigDecimal.valueOf(Double.parseDouble(row[11].toString())));
                    ps.setRule(row[12].toString());
                    ps.setUser(row[13].toString());
                    // 获取标准工时
                    Object[] std = getERPStandardHour(company, ps.getItemno(), ps.getStep());
                    if (std != null) {
                        ps.setStandardLaborTime(BigDecimal.valueOf(Double.parseDouble(std[0].toString())));
                        ps.setStandardMachineTime(BigDecimal.valueOf(Double.parseDouble(std[1].toString())));
                    } else {
                        ps.setStandardLaborTime(BigDecimal.ZERO);
                        ps.setStandardMachineTime(BigDecimal.ZERO);
                    }
                    ps.setStandCost(BigDecimal.ZERO);
                    processStepList.add(ps);
                }
            }
        } catch (Exception ex) {
            log4j.error("MachiningEfficiency.getProcessStep()异常", ex);
        }
        return processStepList;
    }

    /**
     * 返回ERP中的标准工时
     *
     * @param company 公司
     * @param itnbr 件号
     * @param prosscode 制程
     * @return 标准人工工时 标准机器工时
     */
    public Object[] getERPStandardHour(String company, String itnbr, String prosscode) {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT boroph.manstdtm,boroph.mchstdtm FROM boroph,borgrp WHERE boroph.itnbrgrp = borgrp.itnbrgrp AND borgrp.itnbr ='${itnbr}' AND prosscode = '${prosscode}' ");
        String sql = sb.toString().replace("${itnbr}", itnbr).replace("${prosscode}", prosscode);
        try {
            superEJBForERP.setCompany(company);
            Query query = superEJBForERP.getEntityManager().createNativeQuery(sql);
            List result = query.getResultList();
            if (result != null && !result.isEmpty() && result.size() == 1) {
                return (Object[]) result.get(0);
            }
        } catch (Exception ex) {
            log4j.error("MachiningEfficiency.getERPStandardHour()异常", ex);
        }
        return null;
    }

    protected BigDecimal updateDowntime(IndicatorDetail entity, int uy, int um, int ud, int type, String machine) {
        BigDecimal value = BigDecimal.ZERO;
        try {
            IndicatorDaily daily = indicatorBean.findIndicatorDailyByPIdDateAndType(entity.getId(), entity.getSeq(), um,
                    entity.getType());
            if (daily != null) {
                Method setMethod = daily.getClass().getDeclaredMethod(
                        "set" + indicatorBean.getIndicatorColumn("D", ud).toUpperCase(), BigDecimal.class);
                setMethod.invoke(daily, value);
                indicatorBean.updateIndicatorDaily(daily);
                return daily.getTotal();
            }
        } catch (Exception ex) {
            log4j.error("updateDowntime" + ex);
        }
        return BigDecimal.ZERO;
    }

    protected BigDecimal updateMachiningQuantity(IndicatorDetail entity, int uy, int um, int ud, int type, String machine) {
        BigDecimal value = BigDecimal.ZERO;
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT COUNT(PRODUCTCOMPID) FROM PROCESS_STEP WHERE EQPID = '${machine}' ");
        sb.append(" AND year(TRACKOUTTIME)=${y} AND month(TRACKOUTTIME)=${m} ");
        switch (type) {
            case 2:
                // 月
                sb.append(" AND datepart(DAY ,TRACKOUTTIME) <= ${d} ");
                break;
            case 5:
                // 日
                sb.append(" AND datepart(DAY ,TRACKOUTTIME) = ${d} ");
                break;
            default:
                sb.append(" AND datepart(DAY ,TRACKOUTTIME) = ${d} ");
        }
        String sql = sb.toString().replace("${machine}", machine).replace("${y}", String.valueOf(uy))
                .replace("${m}", String.valueOf(um)).replace("${d}", String.valueOf(ud));
        superEJBForMES.setCompany(entity.getParent().getCompany());
        Query query = superEJBForMES.getEntityManager().createNativeQuery(sql);
        try {
            Object obj = query.getSingleResult();
            value = BigDecimal.valueOf(Double.parseDouble(obj.toString()));
            IndicatorDaily daily = indicatorBean.findIndicatorDailyByPIdDateAndType(entity.getId(), entity.getSeq(), um, entity.getType());
            if (daily != null) {
                Method setMethod = daily.getClass().getDeclaredMethod(
                        "set" + indicatorBean.getIndicatorColumn("D", ud).toUpperCase(), BigDecimal.class);
                setMethod.invoke(daily, value);
                indicatorBean.updateIndicatorDaily(daily);
                return daily.getTotal();
            }
        } catch (Exception ex) {
            log4j.error("MachiningEfficiency.updateMachiningQuantity()异常", ex);
        }
        return value;
    }

    protected BigDecimal updatePlannedHour(IndicatorDetail entity, int uy, int um, int ud, int type, String machine) {
        BigDecimal value = BigDecimal.ZERO;
        try {
            IndicatorDaily daily = indicatorBean.findIndicatorDailyByPIdDateAndType(entity.getId(), entity.getSeq(), um,
                    entity.getType());
            if (daily != null) {
                Method setMethod = daily.getClass().getDeclaredMethod(
                        "set" + indicatorBean.getIndicatorColumn("D", ud).toUpperCase(), BigDecimal.class);
                setMethod.invoke(daily, value);
                indicatorBean.updateIndicatorDaily(daily);
                return daily.getTotal();
            }
        } catch (Exception ex) {
            log4j.error("MachiningEfficiency.updatePlannedHour()异常", ex);
        }
        return BigDecimal.ZERO;
    }

    protected BigDecimal updateStandardHour(IndicatorDetail entity, int uy, int um, int ud, int type, String machine) {
        BigDecimal value = BigDecimal.ZERO;
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT SUM(standardMachineTime * qty) FROM processstep WHERE equipment = '${machine}' ");
        sb.append(" AND year(endTime)=${y} AND month(endTime)=${m} ");
        switch (type) {
            case 2:
                // 月
                sb.append(" AND DAY(endTime) <= ${d} ");
                break;
            case 5:
                // 日
                sb.append(" AND DAY(endTime) = ${d} ");
                break;
            default:
                sb.append(" AND DAY(endTime) = ${d} ");
        }
        String sql = sb.toString().replace("${machine}", machine).replace("${y}", String.valueOf(uy))
                .replace("${m}", String.valueOf(um)).replace("${d}", String.valueOf(ud));
        Query query = superEJBForKPI.getEntityManager().createNativeQuery(sql);
        try {
            Object obj = query.getSingleResult();
            value = BigDecimal.valueOf(Double.parseDouble(obj.toString()));
            IndicatorDaily daily = indicatorBean.findIndicatorDailyByPIdDateAndType(entity.getId(), entity.getSeq(), um,
                    entity.getType());
            if (daily != null) {
                Method setMethod = daily.getClass().getDeclaredMethod(
                        "set" + indicatorBean.getIndicatorColumn("D", ud).toUpperCase(), BigDecimal.class);
                setMethod.invoke(daily, value);
                indicatorBean.updateIndicatorDaily(daily);
                return daily.getTotal();
            }
        } catch (Exception ex) {
            log4j.error("MachiningEfficiency.updateStandardHour()异常", ex);
        }
        return BigDecimal.ZERO;
    }

    protected BigDecimal updateWaintingTime(IndicatorDetail entity, int uy, int um, int ud, int type, String machine) {
        BigDecimal value = BigDecimal.ZERO;
        try {
            IndicatorDaily daily = indicatorBean.findIndicatorDailyByPIdDateAndType(entity.getId(), entity.getSeq(), um,
                    entity.getType());
            if (daily != null) {
                Method setMethod = daily.getClass().getDeclaredMethod(
                        "set" + indicatorBean.getIndicatorColumn("D", ud).toUpperCase(), BigDecimal.class);
                setMethod.invoke(daily, value);
                indicatorBean.updateIndicatorDaily(daily);
                return daily.getTotal();
            }
        } catch (Exception ex) {
            log4j.error("updateWaintingTime" + ex);
        }
        return BigDecimal.ZERO;
    }

}
