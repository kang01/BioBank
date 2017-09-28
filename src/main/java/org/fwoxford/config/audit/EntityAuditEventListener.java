package org.fwoxford.config.audit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.PostPersist;
import javax.persistence.PostRemove;
import javax.persistence.PostUpdate;

public class EntityAuditEventListener extends AuditingEntityListener {

    private final Logger log = LoggerFactory.getLogger(EntityAuditEventListener.class);

    private static BeanFactory beanFactory;

    /**
     * 由于在执行大数据量的CREATE,UPDATE,DELETE操作时，审计日志耗时很长，所以暂时注释掉
     * @param target
     */
    @PostPersist
    public void onPostCreate(Object target) {
        try {
            AsyncEntityAuditEventWriter asyncEntityAuditEventWriter = beanFactory.getBean(AsyncEntityAuditEventWriter.class);
//            asyncEntityAuditEventWriter.writeAuditEvent(target, EntityAuditAction.CREATE);
        } catch (NoSuchBeanDefinitionException e) {
            log.error("No bean found for AsyncEntityAuditEventWriter");
        } catch (Exception e) {
            log.error("Exception while persisting create audit entity {}", e);
        }
    }

    @PostUpdate
    public void onPostUpdate(Object target) {
        try {
            AsyncEntityAuditEventWriter asyncEntityAuditEventWriter = beanFactory.getBean(AsyncEntityAuditEventWriter.class);
//            asyncEntityAuditEventWriter.writeAuditEvent(target, EntityAuditAction.UPDATE);
        } catch (NoSuchBeanDefinitionException e) {
            log.error("No bean found for AsyncEntityAuditEventWriter");
        } catch (Exception e) {
            log.error("Exception while persisting update audit entity {}", e);
        }
    }

    @PostRemove
    public void onPostRemove(Object target) {
        try {
            AsyncEntityAuditEventWriter asyncEntityAuditEventWriter = beanFactory.getBean(AsyncEntityAuditEventWriter.class);
//            asyncEntityAuditEventWriter.writeAuditEvent(target, EntityAuditAction.DELETE);
        } catch (NoSuchBeanDefinitionException e) {
            log.error("No bean found for AsyncEntityAuditEventWriter");
        } catch (Exception e) {
            log.error("Exception while persisting delete audit entity {}", e);
        }
    }

    static void setBeanFactory(BeanFactory beanFactory) {
        EntityAuditEventListener.beanFactory = beanFactory;
    }

}
