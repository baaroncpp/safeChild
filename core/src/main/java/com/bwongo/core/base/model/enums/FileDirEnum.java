package com.bwongo.core.base.model.enums;

/**
 * @Author bkaaron
 * @Project nc
 * @Date 5/11/24
 * @LocalTime 12:45 PM
 **/
public enum FileDirEnum {
    SCHOOL_BUDGE("budge"),
    PROFILE("profile");

    final String value;

    FileDirEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
