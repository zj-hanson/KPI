/*
 * To change this license header, choose License Headers in Project Properties. To change this template file, choose
 * Tools | Templates and open the template in the editor.
 */

package cn.hanson.kpi.ejb.ppm;

import cn.hanbell.kpi.comm.SuperEJBForPPM;
import cn.hanson.kpi.entity.ppm.QuotationData;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;
import java.util.List;
import javax.ejb.Stateless;
import javax.ejb.LocalBean;
import javax.persistence.Query;

/**
 *
 * @author C0160
 */
@Stateless
@LocalBean
public class QuotationDataBean extends SuperEJBForPPM<QuotationData> {

    public QuotationDataBean() {
        super(QuotationData.class);
    }

    public List<QuotationData> findByCompanyItemnoEffectiveDateAndStatus(String company, String itemno, Date day,
        String status) {
        String jpql =
            "SELECT e FROM QuotationData e WHERE e.company = :company AND e.itemno = :itemno AND e.startDate <= :startDate AND e.endDate >= :endDate AND e.status = :status ";
        Query query = getEntityManager().createQuery(jpql);
        query.setParameter("company", company);
        query.setParameter("itemno", itemno);
        query.setParameter("startDate", day);
        query.setParameter("endDate", day);
        query.setParameter("status", status);
        try {
            return query.getResultList();
        } catch (Exception ex) {
            log4j.error(ex);
        }
        return null;
    }

    /**
     * 获取加工费报价
     * 
     * @param company
     * @param itemno
     * @param day
     * @return
     */
    public BigDecimal getProcessingPrice(String company, String itemno, Date day) {
        List<QuotationData> data = findByCompanyItemnoEffectiveDateAndStatus(company, itemno, day, "V");
        if (data != null && data.size() == 1) {
            QuotationData pd = data.get(0);
            return pd.getProcessingPrice();
        }
        // 多个有效价格采用平均价格
        if (data != null && data.size() > 1) {
            BigDecimal value = BigDecimal.ZERO;
            int cnt = data.size();
            for (int i = 0; i < cnt; i++) {
                QuotationData pd = data.get(i);
                value = value.add(pd.getProcessingPrice());
            }
            return value.divide(BigDecimal.valueOf(Double.parseDouble(String.valueOf(cnt))), 4, RoundingMode.HALF_UP);
        }
        return BigDecimal.ZERO;
    }

}
