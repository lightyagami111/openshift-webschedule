/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.webschedule.design.datastructure;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Cacheable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 *
 * @author asd
 */
@Entity
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class EventException implements Serializable {
    
    @Id
    @GeneratedValue
    private Long id;
    
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date dateException;   
    
    @ManyToOne
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
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
