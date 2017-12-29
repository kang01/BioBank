package org.fwoxford.domain;

import org.fwoxford.config.audit.EntityAuditEventListener;
import org.hibernate.envers.Audited;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import javax.validation.constraints.Size;
import java.io.Serializable;

/**
 * Created by gengluying on 2017/12/29.
 */
@MappedSuperclass
@Audited
@EntityListeners({AuditingEntityListener.class, EntityAuditEventListener.class})
public abstract class FrozenTubeLabel extends AbstractAuditingEntity implements Serializable {

    @Size(max = 255)
    @Column(name = "tag1", length = 255)
    private String tag1;
    @Size(max = 255)
    @Column(name = "tag2", length = 255)
    private String tag2;
    @Size(max = 255)
    @Column(name = "tag3", length = 255)
    private String tag3;
    @Size(max = 1024)
    @Column(name = "tag4", length = 1024)
    private String tag4;

    public String getTag1() {
        return tag1;
    }

    public void setTag1(String tag1) {
        this.tag1 = tag1;
    }

    public String getTag2() {
        return tag2;
    }

    public void setTag2(String tag2) {
        this.tag2 = tag2;
    }

    public String getTag3() {
        return tag3;
    }

    public void setTag3(String tag3) {
        this.tag3 = tag3;
    }

    public String getTag4() {
        return tag4;
    }

    public void setTag4(String tag4) {
        this.tag4 = tag4;
    }
}
