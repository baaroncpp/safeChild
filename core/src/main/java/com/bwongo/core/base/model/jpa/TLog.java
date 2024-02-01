package com.bwongo.core.base.model.jpa;

import com.bwongo.core.base.model.enums.LogLevelEnum;
import lombok.Setter;

import javax.persistence.*;

/**
 * @Author bkaaron
 * @Project nc
 * @Date 12/8/23
 **/
@Entity
@Table(name = "t_log", schema = "core")
@Setter
public class TLog extends BaseEntity{
    private String resourceUrl;
    private String httpStatus;
    private LogLevelEnum logLevel;
    private String note;
    private String entityName;

    @Column(name = "resource_url")
    public String getResourceUrl() {
        return resourceUrl;
    }

    @Column(name = "http_status")
    public String getHttpStatus() {
        return httpStatus;
    }

    @Column(name = "log_level")
    @Enumerated(EnumType.STRING)
    public LogLevelEnum getLogLevel() {
        return logLevel;
    }

    @Column(name = "note")
    public String getNote() {
        return note;
    }

    @Column(name = "entity_name")
    public String getEntityName() {
        return entityName;
    }
}
