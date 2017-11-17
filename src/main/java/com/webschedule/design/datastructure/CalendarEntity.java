/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.webschedule.design.datastructure;

import java.io.Serializable;
import javax.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 *
 * @author asd
 */
@Document
public class CalendarEntity implements Serializable {
    
    @Id
    private Long id;
    
    @NotNull
    @Length(max = 255)
    private String title;
    
    @Length(max = 255)
    private String bckgColor;
    
    private Boolean defaultCalendar = false;
    
    private Boolean selected = false;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBckgColor() {
        return bckgColor;
    }

    public void setBckgColor(String bckgColor) {
        this.bckgColor = bckgColor;
    }

    public Boolean getDefaultCalendar() {
        return defaultCalendar;
    }

    public void setDefaultCalendar(Boolean defaultCalendar) {
        this.defaultCalendar = defaultCalendar;
    }

    public Boolean getSelected() {
        return selected;
    }

    public void setSelected(Boolean selected) {
        this.selected = selected;
    }
    
    
}
