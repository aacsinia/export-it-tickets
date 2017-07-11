package com.nudgeit.domain;

import javax.persistence.*;

@Entity
public class Ticket {

    Ticket() {
    }

    public Ticket(String title, String description, String acceptanceCriteria, TicketState state) {
        this.title = title;
        this.description = description;
        this.acceptanceCriteria = acceptanceCriteria;
        this.state = state;
    }

    @Id
    @GeneratedValue
    private Long id;

    private String title;

    private String description;

    private String acceptanceCriteria;

    @Enumerated(EnumType.STRING)
    private TicketState state;

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAcceptanceCriteria() {
        return acceptanceCriteria;
    }

    public void setAcceptanceCriteria(String acceptanceCriteria) {
        this.acceptanceCriteria = acceptanceCriteria;
    }

    public TicketState getState() {
        return state;
    }

    public void setState(TicketState state) {
        this.state = state;
    }
}
