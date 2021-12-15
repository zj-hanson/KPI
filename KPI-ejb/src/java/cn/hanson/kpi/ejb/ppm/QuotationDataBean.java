/*
 * To change this license header, choose License Headers in Project Properties. To change this template file, choose
 * Tools | Templates and open the template in the editor.
 */

package cn.hanson.kpi.ejb.ppm;

import cn.hanbell.kpi.comm.SuperEJBForPPM;
import cn.hanson.kpi.entity.ppm.QuotationData;
import java.math.BigDecimal;
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
            QuotationData pp = data.get(0);
            return pp.getProcessingPrice();
        }
        // 后续需要处理多个有效价格逻辑
        return BigDecimal.ZERO;
    }

}
