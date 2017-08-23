package org.fwoxford.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A SerialNo.
 */
@Entity
@Table(name = "serial_no")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class SerialNo extends AbstractAuditingEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_serial_no")
    @SequenceGenerator(name = "seq_serial_no",sequenceName = "seq_serial_no",allocationSize = 1,initialValue = 1)
    private Long id;

    @NotNull
    @Column(name = "serial_no", nullable = false)
    private String serialNo;

    @NotNull
    @Column(name = "machine_no", nullable = false)
    private String machineNo;

    @NotNull
    @Column(name = "status", nullable = false)
    private String status;

    @Size(max = 1024)
    @Column(name = "memo", length = 1024)
    private String memo;

    @NotNull
    @Column(name = "used_date", nullable = false)
    private LocalDate usedDate;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSerialNo() {
        return serialNo;
    }

    public SerialNo serialNo(String serialNo) {
        this.serialNo = serialNo;
        return this;
    }

    public void setSerialNo(String serialNo) {
        this.serialNo = serialNo;
    }

    public String getMachineNo() {
        return machineNo;
    }

    public SerialNo machineNo(String machineNo) {
        this.machineNo = machineNo;
        return this;
    }

    public void setMachineNo(String machineNo) {
        this.machineNo = machineNo;
    }

    public String getStatus() {
        return status;
    }

    public SerialNo status(String status) {
        this.status = status;
        return this;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMemo() {
        return memo;
    }

    public SerialNo memo(String memo) {
        this.memo = memo;
        return this;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public LocalDate getUsedDate() {
        return usedDate;
    }

    public SerialNo usedDate(LocalDate usedDate) {
        this.usedDate = usedDate;
        return this;
    }

    public void setUsedDate(LocalDate usedDate) {
        this.usedDate = usedDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        SerialNo serialNo = (SerialNo) o;
        if (serialNo.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, serialNo.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "SerialNo{" +
            "id=" + id +
            ", serialNo='" + serialNo + "'" +
            ", machineNo='" + machineNo + "'" +
            ", status='" + status + "'" +
            ", memo='" + memo + "'" +
            ", usedDate='" + usedDate + "'" +
            '}';
    }
}
