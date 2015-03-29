package com.vdurmont.vdmail.model;

import org.hibernate.annotations.Type;
import org.joda.time.DateTime;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;

@MappedSuperclass
public abstract class Entity {
    @Id
    @GeneratedValue
    private Integer id;
    @NotNull
    @Temporal(TemporalType.TIMESTAMP)
    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    @Column(name = "created_date", nullable = false)
    private DateTime createdDate;

    @PrePersist
    public void beforeCreate() {
        DateTime now = DateTime.now();
        if (this.createdDate == null) {
            this.createdDate = now;
        }
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public DateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(DateTime createdDate) {
        this.createdDate = createdDate;
    }

    @Override public String toString() {
        return this.getClass().getSimpleName() + "{" + "id=" + id + ", createdDate=" + createdDate + "}";
    }
}
