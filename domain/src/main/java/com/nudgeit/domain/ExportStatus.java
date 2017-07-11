package com.nudgeit.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class ExportStatus {

    @Id
    @GeneratedValue
    private Long id;

    private Long current;

    private Long total;

    private String error;

    private long lastUpdate;

    public ExportStatus(Long total) {
        this.current = 0L;
        this.total = total;
        this.lastUpdate = System.currentTimeMillis();
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public Long getTotal() {
        return total;
    }

    public Long getCurrent() {
        return current;
    }

    public void incCurrent(Long delta) {
        this.lastUpdate = System.currentTimeMillis();
        this.current += delta;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public long getLastUpdate() {
        return lastUpdate;
    }
}
