package com.bwongo.core.user_mgt.model.jpa;

import com.bwongo.core.base.model.jpa.BaseEntity;
import lombok.ToString;

import javax.persistence.*;

/**
 * @Author bkaaron
 * @Project kabangali
 * @Date 27/05/2022
 */
@Entity
@Table(name = "t_user_group", schema = "core")
@ToString
public class TUserGroup extends BaseEntity {
    private String name;
    private String note;

    @Column(name = "user_group_name", nullable = false, unique = true)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Column(name = "group_note")
    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
}
