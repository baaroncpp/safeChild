package com.bwongo.core.base.model.enums;

/**
 * @Author bkaaron
 * @Project kabangali
 * @Date 27/05/2022
 */
public enum UserTypeEnum {
    ADMIN("Admin"),
    DRIVER("Driver"),
    SCHOOL_ADMIN("School Admin"),
    SCHOOL_STAFF("School Staff");

    String note;

    UserTypeEnum(String note) {
        this.note = note;
    }

    public String getNote() {
        return note;
    }
}
