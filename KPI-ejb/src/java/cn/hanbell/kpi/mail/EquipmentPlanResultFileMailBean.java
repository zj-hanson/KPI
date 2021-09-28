/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.hanbell.kpi.mail;

import cn.hanbell.kpi.comm.MailNotification;
import cn.hanbell.kpi.ejb.eam.EquipmentAnalyResultBean;
import cn.hanbell.kpi.ejb.eam.EquipmentAnalyResultDtaBean;
import cn.hanbell.kpi.ejb.eam.EquipmentStandardBean;
import cn.hanbell.kpi.entity.Indicator;
import cn.hanbell.kpi.entity.eam.EquipmentAnalyResult;
import cn.hanbell.kpi.entity.eam.EquipmentAnalyResultDta;
import cn.hanbell.kpi.entity.eam.EquipmentStandard;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;

/**
 *
 * @author C2079
 */
@Stateless
@LocalBean
public class EquipmentPlanResultFileMailBean extends MailNotification {

    @EJB
    protected EquipmentAnalyResultBean equipmentAnalyResultBean;

    @EJB
    protected EquipmentAnalyResultDtaBean equipmentAnalyResultDtaBean;

    @EJB
    protected EquipmentStandardBean equipmentStandardBean;

    public EquipmentPlanResultFileMailBean() {

    }

    @Override
    public void init() {
        this.mailSetting = mailSettingBean.findByMailClazz(this.getClass().getName());
        super.init();
    }

    @Override
    protected String getMailHead() {
        return "";
    }

    @Override
    protected String getMailFooter() {
        return "";
    }

    @Override
    protected String getMailBody() throws ParseException {
        attachments.clear();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String dateTime = simpleDateFormat.format(c.getTime());//将日期转成特定格式
        List<EquipmentStandard> eStandard = equipmentAnalyResultBean.getMonthlyReport(dateTime, "");
        //List<Object[]> equipmentstandardList = equipmentAnalyResultBean.getMonthlyReport(dateTime, "一级");
        Map<String, List<EquipmentStandard>> moMap = new LinkedHashMap<>();
        //将同一设备的基准归类
        eStandard.forEach(sobj -> {
            List<EquipmentStandard> assetno = new ArrayList<>();
            String anKey = sobj.getAssetno() + sobj.getStandardlevel();//获取资产编号和等级相同的基准
            if (moMap.containsKey(anKey)) {//判断map中是否已经存在该资产编号,不存在就存在新的KEY里否则加入原来的KEY里
                for (Map.Entry<String, List<EquipmentStandard>> entry : moMap.entrySet()) {
                    if (entry.getKey().equals(anKey)) {
                        assetno = entry.getValue();
                        assetno.add(sobj);
                        moMap.put(anKey, assetno);
                    }
                }
            } else {
                assetno.add(sobj);
                moMap.put(anKey, assetno);
            }
        });
        //给保全主表和子表赋值保存
        for (Map.Entry<String, List<EquipmentStandard>> entry : moMap.entrySet()) {
            List<EquipmentStandard> itemList = new ArrayList<>();
            List<EquipmentStandard> itemList1 = entry.getValue();
            EquipmentAnalyResult eAnaly = new EquipmentAnalyResult();
            String formid = equipmentAnalyResultBean.getFormId(Calendar.getInstance().getTime(), "BQ", "YYMM", 4);
            eAnaly.setFormid(formid);
            Object[] o = equipmentAnalyResultBean.findByAssetno(itemList1.get(0).getAssetno());
            eAnaly.setFormdate(Calendar.getInstance().getTime());
            //主表只取机型编号等信息，所以只取第一条数据赋值
            eAnaly.setAssetno(itemList1.get(0).getAssetno());
            eAnaly.setCompany(itemList1.get(0).getCompany());
            eAnaly.setAssetdesc(itemList1.get(0).getAssetdesc());
            eAnaly.setSpareno(itemList1.get(0).getItemno());
            eAnaly.setDeptno(itemList1.get(0).getDeptno());
            eAnaly.setDeptname(itemList1.get(0).getDeptname());
            eAnaly.setStandardlevel(itemList1.get(0).getStandardlevel());
            eAnaly.setCreator("admin");
            eAnaly.setCredate(Calendar.getInstance().getTime());
            eAnaly.setStatus("N");
            int seq = 1;
            List<EquipmentAnalyResultDta> eDtaList = new ArrayList<>();
            for (EquipmentStandard obj : itemList1) {
                EquipmentAnalyResultDta eDta = new EquipmentAnalyResultDta();
                eDta.setPid(formid);
                eDta.setCompany(obj.getCompany());
                eDta.setSeq(seq);
                eDta.setStandardtype(obj.getStandardtype());
                eDta.setRespondept(obj.getRespondept());
                eDta.setCheckarea(obj.getCheckarea());
                eDta.setCheckcontent(obj.getCheckcontent());
                eDta.setJudgestandard(obj.getJudgestandard());
                eDta.setMethod(obj.getMethod());
                eDta.setMethodname(obj.getMethodname());
                eDta.setDowntime(obj.getDowntime());
                eDta.setDownunit(obj.getDownunit());
                eDta.setManpower(obj.getManpower());
                eDta.setManhour(obj.getManhour());
                if (obj.getRespondept().equals("维修")) {
                    eDta.setAnalysisuser(o[45].toString());
                    eDta.setLastanalysisuser(o[46].toString());
                } else if (obj.getRespondept().equals("现场")) {
                    eDta.setAnalysisuser(o[20].toString());
                    eDta.setLastanalysisuser(o[21].toString());
                }
                eDta.setAreaimage(obj.getAreaimage());
                eDta.setStatus("N");
                eDta.setCreator("admin");
                eDta.setCredate(Calendar.getInstance().getTime());
                seq++;
                eDtaList.add(eDta);
                obj.setLasttime(obj.getNexttime());
                Calendar cal = Calendar.getInstance();
                cal.setTime(obj.getNexttime());//设置起时间0
                //生成保全计划后更新下次生成时间
                switch (obj.getFrequencyunit()) {
                    case "天":
                        cal.add(Calendar.DATE, obj.getFrequency());
                        obj.setNexttime(cal.getTime());
                        break;
                    case "周":
                        cal.add(Calendar.DATE, obj.getFrequency() * 7);
                        obj.setNexttime(cal.getTime());
                        break;
                    case "月":
                        cal.add(Calendar.MONTH, obj.getFrequency());
                        obj.setNexttime(cal.getTime());
                        break;
                    case "季":
                        cal.add(Calendar.MONTH, obj.getFrequency() * 3);
                        obj.setNexttime(cal.getTime());
                        break;
                    case "年":
                        cal.add(Calendar.YEAR, 1);
                        obj.setNexttime(cal.getTime());
                        break;
                    default:
                        break;
                }
                itemList.add(obj);
            }
            equipmentAnalyResultBean.persist(eAnaly);
            equipmentAnalyResultDtaBean.update(eDtaList);//使用update可以批量保存，只有主键不同时使用，否则就更新
            equipmentStandardBean.update(itemList);//添加后更新下次生成时间
        }
        return null;
    }

    @Override
    protected String getHtmlTable(List<Indicator> indicatorList, int y, int m, Date d, boolean needsum) {
        return "";
    }

    @Override
    protected String getHtmlTableRow(Indicator indicator, int y, int m, Date d) throws Exception {
        return "";
    }

}
