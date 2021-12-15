package cn.hanbell.kpi.comm;

import com.lightshell.comm.SuperEJB;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author C1879
 * @param <T>
 */
public abstract class SuperEJBForPPM<T> extends SuperEJB<T> {

    @PersistenceContext(unitName = "PU_hsppm")
    private EntityManager em;

    protected Logger log4j = LogManager.getLogger("cn.hanbell.kpi");

    public SuperEJBForPPM(Class<T> entityClass) {
        super(entityClass);
    }

    @Override
    public EntityManager getEntityManager() {
        return em;
    }

}
