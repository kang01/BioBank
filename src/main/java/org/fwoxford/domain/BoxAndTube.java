package org.fwoxford.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A BoxAndTube.
 */
@Entity
@Table(name = "box_and_tube")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class BoxAndTube extends AbstractAuditingEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
//    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Size(max = 20)
    @Column(name = "status", length = 20, nullable = false)
    private String status;

    @Size(max = 1024)
    @Column(name = "memo", length = 1024)
    private String memo;

    @ManyToOne(optional = false)
    @NotNull
    private FrozenBox frozenBox;

    @ManyToOne(optional = false)
    @NotNull
    private FrozenTube frozenTube;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public BoxAndTube status(String status) {
        this.status = status;
        return this;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMemo() {
        return memo;
    }

    public BoxAndTube memo(String memo) {
        this.memo = memo;
        return this;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public FrozenBox getFrozenBox() {
        return frozenBox;
    }

    public BoxAndTube frozenBox(FrozenBox frozenBox) {
        this.frozenBox = frozenBox;
        return this;
    }

    public void setFrozenBox(FrozenBox frozenBox) {
        this.frozenBox = frozenBox;
    }

    public FrozenTube getFrozenTube() {
        return frozenTube;
    }

    public BoxAndTube frozenTube(FrozenTube frozenTube) {
        this.frozenTube = frozenTube;
        return this;
    }

    public void setFrozenTube(FrozenTube frozenTube) {
        this.frozenTube = frozenTube;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        BoxAndTube boxAndTube = (BoxAndTube) o;
        if (boxAndTube.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, boxAndTube.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "BoxAndTube{" +
            "id=" + id +
            ", status='" + status + "'" +
            ", memo='" + memo + "'" +
            '}';
    }
}
