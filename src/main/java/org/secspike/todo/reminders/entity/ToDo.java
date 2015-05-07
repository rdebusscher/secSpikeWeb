package org.secspike.todo.reminders.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author airhacks.com
 */
@Entity
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@NamedQuery(name = ToDo.findAll, query = "SELECT t FROM ToDo t")
public class ToDo {

    public static final String findAll = "org.secspike.todo.reminders.entity.ToDo.all";
    @Id
    @GeneratedValue
    private Long id;
    private String description;
    private String createdBy;
    private String changedBy;
    private boolean done;

    public ToDo(String description) {
        this.description = description;
    }

    public ToDo() {
    }

    public Long getId() {
        return id;
    }

    public boolean isNew() {
        return this.id == null;
    }

    public String getDescription() {
        return description;
    }

    public boolean isDone() {
        return done;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getChangedBy() {
        return changedBy;
    }

    public void setChangedBy(String changedBy) {
        this.changedBy = changedBy;
    }

}
