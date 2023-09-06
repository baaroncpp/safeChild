package com.bwongo.core.user_mgt.model.jpa;

import com.bwongo.core.base.model.jpa.BaseEntity;
import javax.persistence.*;

/**
 * @Author bkaaron
 * @Project kabangali
 * @Date 27/05/2022
 */
@Entity
@Table(name = "t_role", schema = "core")
public class TRole extends BaseEntity {
    private String name;
    private String note;

    @Column(name = "name", unique = true)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Column(name = "note")
    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

}
