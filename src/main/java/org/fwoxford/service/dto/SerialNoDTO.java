package org.fwoxford.service.dto;

import java.time.LocalDate;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the SerialNo entity.
 */
public class SerialNoDTO extends AbstractAuditingDTO implements Serializable {

    private Long id;

    @NotNull
    private String serialNo;

    @NotNull
    private String machineNo;

    @NotNull
    private String status;

    @Size(max = 1024)
    private String memo;

    @NotNull
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

    public void setSerialNo(String serialNo) {
        this.serialNo = serialNo;
    }
    public String getMachineNo() {
        return machineNo;
    }

    public void setMachineNo(String machineNo) {
        this.machineNo = machineNo;
    }
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }
    public LocalDate getUsedDate() {
        return usedDate;
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

        SerialNoDTO serialNoDTO = (SerialNoDTO) o;

        if ( ! Objects.equals(id, serialNoDTO.id)) { return false; }

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "SerialNoDTO{" +
            "id=" + id +
            ", serialNo='" + serialNo + "'" +
            ", machineNo='" + machineNo + "'" +
            ", status='" + status + "'" +
            ", memo='" + memo + "'" +
            ", usedDate='" + usedDate + "'" +
            '}';
    }
}
