package com.nc.safechild.student.models.enums;

/**
 * @Author bkaaron
 * @Project cyclos-web-service
 * @Date 6/28/23
 **/
public enum NotificationRoleEnum {
    DROP_OFF("Can drop off students, to their homes"),
    PICK_UP("Can pick up students, from their homes"),
    ON_SCHOOL("Can sign in a student into school"),
    BULK_ON_SCHOOL("Can sign in students into school"),
    OFF_SCHOOL("can sign out students from school");

    final String description;

    NotificationRoleEnum(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
