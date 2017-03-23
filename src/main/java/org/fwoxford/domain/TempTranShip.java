package org.fwoxford.domain;

import com.fasterxml.jackson.annotation.JsonView;
import jdk.nashorn.internal.objects.annotations.Getter;
import jdk.nashorn.internal.objects.annotations.Setter;
import org.springframework.data.jpa.datatables.mapping.DataTablesOutput;
import javax.persistence.*;

/**
 * Created by gengluying on 2017/3/22.
 */
@Entity
public class TempTranShip {

    @Id
    @JsonView(DataTablesOutput.View.class)
    private Integer id;
    @JsonView(DataTablesOutput.View.class)
    private String mail;
    public TempTranShip() {
        super();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }
}
