package com.bwongo.core.base.model.enums;

/**
 * @Author bkaaron
 * @Project nc
 * @Date 9/7/23
 **/
public enum SchoolCategory {
    DAY_CARE("Daycare"), PRIMARY("Primary"), KINDERGARTEN("Kindergarten"), NURSERY("Nursery"), PRE_SCHOOL("Preschool");

    private String note;
    SchoolCategory(String note) {
        this.note = note;
    }

    public String getNote() {
        return note;
    }
}
