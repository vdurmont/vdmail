package com.vdurmont.vdmail.dto;

import org.joda.time.DateTime;

public abstract class EntityDTO {
    private int id;
    private DateTime createdDate;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public DateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(DateTime createdDate) {
        this.createdDate = createdDate;
    }
}
