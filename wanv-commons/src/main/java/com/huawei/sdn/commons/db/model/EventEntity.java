package com.huawei.sdn.commons.db.model;

import org.codehaus.jackson.annotate.JsonIgnore;

/**
 * Created by Ofer Ben-Yacov (oWX212574) on 4/13/2015.
 */
public class EventEntity {

    private long id;
    private String eventTime;
    private Severity severity;
    private EventCategory category;
    private String description;
    private EventStatus status;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getEventTime() {
        return eventTime;
    }

    public void setEventTime(String eventTime) {
        this.eventTime = eventTime;
    }

    public Severity getSeverity() {
        return severity;
    }

    public void setSeverity(Severity severity) {
        this.severity = severity;
    }

    public EventCategory getCategory() {
        return category;
    }

    public void setCategory(EventCategory category) {
        this.category = category;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public EventStatus getStatus() {
        return status;
    }

    public void setStatus(EventStatus status) {
        this.status = status;
    }

    @JsonIgnore
    @Override
    public String toString() {
        return "EventEntity{" +
                "id=" + id +
                ", severity=" + severity +
                ", category=" + category +
                ", description='" + description + '\'' +
                ", status=" + status +
                '}';
    }
}
