/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.hanbell.kpi.comm;

import com.lightshell.comm.SuperEJB;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;

/**
 *
 * @author C0160
 * @param <T>
 */
public abstract class SuperEJBForKPI<T> extends SuperEJB<T> {
    
    // 生产环境
    // private final String URL = "http://jrs.hanbell.com.cn/Hanbell-WCO/api/sendmsg/send";
    // 测试环境
    private final String WECHAT_URL = "http://jrs.hanbell.com.cn:8480/Hanbell-WCO/api/sendmsg/send";


    private final String WECHAT_OPENID = "oJJhp5GvX45x3nZgoX9Ae9DyWak4";
    private final String WECHAT_SESSIONID = "dc3fd5651b0646c68b42dd38e76eb088";

    protected final String URL = "http://127.0.0.1:8480/EAP/EAPWebService";
    protected final String NAME_SPACE = "http://jws.hanbell.cn/";

    @PersistenceContext(unitName = "KPI-ejbPU")
    private EntityManager em_shbkpi;

    protected Logger log4j = LogManager.getLogger("cn.hanbell.kpi");

    public SuperEJBForKPI(Class<T> entityClass) {
        super(entityClass);
    }

    @Override
    public EntityManager getEntityManager() {
        return em_shbkpi;
    }

    public String formatString(String value, String format) {
        if (value.length() >= format.length()) {
            return value;
        }
        return format.substring(0, format.length() - value.length()) + value;
    }

    /**
     * @return the log4j
     */
    public Logger getLog4j() {
        return log4j;
    }

    // 发送企业微信信息
    public String sendMsgString(String userId, String msg) {
        StringBuilder jsonString = new StringBuilder("{'userId':'");
        jsonString.append(userId).append("','msg':'").append(msg).append("','sessionkey':'").append(WECHAT_SESSIONID)
                .append("','openid':'").append(WECHAT_OPENID).append("'}");
        JSONObject jo = new JSONObject(jsonString.toString());
        HttpPost httpPost = new HttpPost(WECHAT_URL);
        httpPost.setHeader("content-type", "application/json");
        httpPost.setEntity(new StringEntity(jo.toString(), "UTF-8"));
        try {
            CloseableHttpClient httpClient = HttpClients.createDefault();
            CloseableHttpResponse response = httpClient.execute(httpPost);
            if (response.getStatusLine().getStatusCode() == 200) {
                return "200";
            } else {
                return "500";
            }
        } catch (Exception var5) {
            return null;
        }
    }
}
