/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.webschedule.design.datastructure;

import java.io.Serializable;
import java.util.Date;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 *
 * @author asd
 */
@Document
public class EventException implements Serializable {
    
    @Id
    private Long id;
    
    private Date dateException;   
        
    private TaskRepeatDataEntity taskRepeatDataEntity;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getDateException() {
        return dateException;
    }

    public void setDateException(Date dateException) {
        this.dateException = dateException;
    }

    public TaskRepeatDataEntity getTaskRepeatDataEntity() {
        return taskRepeatDataEntity;
    }

    public void setTaskRepeatDataEntity(TaskRepeatDataEntity taskRepeatDataEntity) {
        this.taskRepeatDataEntity = taskRepeatDataEntity;
    }
    
    
}
