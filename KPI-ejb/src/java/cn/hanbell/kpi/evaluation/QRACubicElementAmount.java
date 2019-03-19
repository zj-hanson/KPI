/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.hanbell.kpi.evaluation;

import java.math.BigDecimal;
import java.util.Date;
import java.util.LinkedHashMap;
import javax.persistence.Query;

/**
 *
 * @author C1749 三次元完工入库总数
 */
public class QRACubicElementAmount extends QRAAConnERP {

    public QRACubicElementAmount() {
        super();
    }
    @Override
    public BigDecimal getValue(int y, int m, Date d, int type, LinkedHashMap<String, Object> map) {
        String genre1 = map.get("genre1") != null ? map.get("genre1").toString() : "";//产品别
        String SOURCEDPIP = map.get("SOURCEDPIP") != null ? map.get("SOURCEDPIP").toString() : "";//所属物料
        BigDecimal result = BigDecimal.ZERO;//总加工数
        BigDecimal Badresult = BigDecimal.ZERO;//不合格数
        StringBuilder sb = new StringBuilder();
        //圆型件精加工和方形件精加工总数量
        sb.append(" select sum(a.al) from ( ");
        sb.append(" SELECT manmas.itnbrf as '件号',invmas.itdsc as '品名',invmas.spdsc as '规格',invmas.genre1,sum(sfcwad.attqty1) as  al ");
        sb.append(" FROM invmas,manmas,sfcwad,sfcwah ");
        sb.append(" WHERE ( sfcwah.facno = sfcwad.facno ) and ");
        sb.append(" ( sfcwah.prono = sfcwad.prono ) and ");
        sb.append(" ( sfcwah.inpno = sfcwad.inpno ) and ");
        sb.append(" ( sfcwad.manno = manmas.manno ) and ");
        sb.append(" ( sfcwad.facno = manmas.facno ) and ");
        sb.append(" ( sfcwad.prono = manmas.prono ) and ");
        sb.append(" ( invmas.itnbr = manmas.itnbrf ) ");
        sb.append(" and sfcwah.facno = 'C' and sfcwah.prono = '1' and sfcwah.stats <>'3' ");
        sb.append(" and manmas.typecode in ('01','02','05') and manmas.linecode in ('FX','YX') and manmas.itnbrf not like '%GB%' ");
        if(!"".equals(genre1)){
            if(genre1.equals("R")){//取ERPinvmas表里的genre1（产品别）判断
                sb.append(" and (invmas.genre1 = 'R'  or invmas.genre1 = 'L' or invmas.genre1 = 'RG' or invmas.genre1 = 'RT' ) ");
            }
            if(genre1.equals("AH")){
                sb.append(" and (invmas.genre1 = 'A'  or invmas.genre1 = 'AA' or invmas.genre1 = 'AH' or invmas.genre1 = 'AJ' ) ");
            }
            if(genre1.equals("P")){
                sb.append(" and (invmas.genre1 = 'P'  or invmas.genre1 = 'PA' or invmas.genre1 = 'PH' ) ");
            }
        }
        sb.append(" and year(sfcwah.indat) = ${y}  and  month(dateadd(hour,-8,sfcwah.indat))=${m} ");
        sb.append(" GROUP BY manmas.itnbrf,invmas.itdsc,invmas.spdsc,invmas.genre1 ");
        sb.append(" ) as a ");
        String sql = sb.toString().replace("${y}", String.valueOf(y)).replace("${m}", String.valueOf(m));
        Query query = superEJB.getEntityManager().createNativeQuery(sql);
        try {
            Object o1 = query.getSingleResult();
            result = (BigDecimal) o1;
            //实例化MES不良数据的类 调用getBadValue方法得到不良数据
            QRACubicElementAmountBad qb = new QRACubicElementAmountBad();
            Badresult = qb.getBadValue(y, m, d, type, map);
            //判断分母不为零的情况下返回实际合格率
            if(result.compareTo(BigDecimal.ZERO)!=0){
            //不良数除总入库数再乘以100
            result = Badresult.divide(result).multiply(BigDecimal.valueOf(100));
            return result;
            }
        } catch (Exception ex) {
            log4j.error("QRACubicElementAmount", ex);
        }
        return BigDecimal.ZERO;
    }

}
