/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this tgetCurrentSession()plate file, choose Tools | TgetCurrentSession()plates
 * and open the tgetCurrentSession()plate in the editor.
 */
package com.webschedule.design.services;

import ch.lambdaj.Lambda;
import com.webschedule.design.datastructure.CalendarEntity;
import com.webschedule.design.datastructure.EventException;
import com.webschedule.design.datastructure.GroupSortEntity;
import com.webschedule.design.datastructure.LabelEntity;
import com.webschedule.design.datastructure.LinkEntity;
import com.webschedule.design.datastructure.ProjectEntity;
import com.webschedule.design.datastructure.TaskEntity;
import com.webschedule.design.datastructure.TaskRepeatDataEntity;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author ivaylo
 */
@Transactional
@Component
public class DaoService {
    
    static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(DaoService.class.getName());

    @Autowired
    private MongoTemplate mongoTemplate;

    public List<TaskEntity> search(String searchTerm) {
        throw new UnsupportedOperationException("search");
    }

    public void persist(ProjectEntity p) {
        mongoTemplate.save(p);
    }

    public List<ProjectEntity> findAllProjects() {
        return mongoTemplate.findAll(ProjectEntity.class);
    }

    public ProjectEntity findProjectById(Long id) {
        return mongoTemplate.findById(id, ProjectEntity.class);
    }

    public void updateProject(ProjectEntity p) {
        mongoTemplate.save(p);
    }

    public void deleteProject(Long id) {
        ProjectEntity p = findProjectById(id);
        Query query1 = new Query();
        query1.addCriteria(Criteria.where("parent").is(id.toString()));
        List<ProjectEntity> children = mongoTemplate.find(query1, ProjectEntity.class);
        if (children != null && !children.isEmpty()) {
            for (ProjectEntity children1 : children) {
                mongoTemplate.remove(children1);
            }
        }
        mongoTemplate.remove(p);

        deleteGroupAndSort("projects", String.valueOf(id));
    }

    public ProjectEntity findDefaultProject() {
        Query query1 = new Query();
        query1.addCriteria(Criteria.where("defaultProject").is(Boolean.TRUE));
        return mongoTemplate.findOne(query1, ProjectEntity.class);
    }

    public void setDefaultProject(Long id) {
        List<ProjectEntity> findAllProjects = findAllProjects();
        for (ProjectEntity findAllProject : findAllProjects) {
            findAllProject.setDefaultProject(Boolean.FALSE);
            updateProject(findAllProject);
        }

        ProjectEntity findProjectById = findProjectById(id);
        findProjectById.setDefaultProject(Boolean.TRUE);
        updateProject(findProjectById);
    }

    //--------------------------------------------------------------------------
    // CALENDARS
    //--------------------------------------------------------------------------
    public void persist(CalendarEntity ce) {
        mongoTemplate.save(ce);
    }

    public void update(CalendarEntity ce) {
        mongoTemplate.save(ce);
    }

    public List<CalendarEntity> findAllCalendars() {
        return mongoTemplate.findAll(CalendarEntity.class);
    }

    public List<CalendarEntity> findSelectedCalendars() {
        Query query1 = new Query();
        query1.addCriteria(Criteria.where("selected").is(Boolean.TRUE));
        return mongoTemplate.find(query1, CalendarEntity.class);
    }

    public CalendarEntity findCalendarById(Long id) {
        return mongoTemplate.findById(id, CalendarEntity.class);
    }

    public void deleteCalendar(Long id) {
        
        Query query1 = new Query();
        query1.addCriteria(Criteria.where("calendar.id").is(id));
        
        Update updateFields = new Update();
        updateFields.set("calendar", findDefaultCalendar());
        
        mongoTemplate.updateMulti(query1, updateFields, TaskEntity.class);
        
       CalendarEntity ce = findCalendarById(id);       
       mongoTemplate.remove(ce);
       
    }

    
    public CalendarEntity findDefaultCalendar() {
        Query query1 = new Query();
        query1.addCriteria(Criteria.where("defaultCalendar").is(Boolean.TRUE));
        
        return mongoTemplate.findOne(query1, CalendarEntity.class);
    }

    public void setDefaultCalendar(Long id) {
        List<CalendarEntity> findAllCalendars = findAllCalendars();
        for (CalendarEntity findAllCalendar : findAllCalendars) {
            findAllCalendar.setDefaultCalendar(Boolean.FALSE);
            update(findAllCalendar);
        }

        CalendarEntity findCalendarById = findCalendarById(id);
        findCalendarById.setDefaultCalendar(Boolean.TRUE);
        findCalendarById.setSelected(Boolean.TRUE);
        update(findCalendarById);
    }


    //--------------------------------------------------------------------------
    // LABELS
    //--------------------------------------------------------------------------
    public void persist(LabelEntity p) {
        mongoTemplate.save(p);
    }

    public List<LabelEntity> findAllLabels() {
        return mongoTemplate.findAll(LabelEntity.class);
    }

    public LabelEntity findLabelById(Long id) {
        return mongoTemplate.findById(id, LabelEntity.class);
    }

    public void updateLabel(LabelEntity p) {
        mongoTemplate.save(p);
    }

    public void deleteLabel(Long id) {
        LabelEntity p = findLabelById(id);
        mongoTemplate.remove(p);

        deleteGroupAndSort("labels", String.valueOf(id));
    }

    //--------------------------------------------------------------------------
    // TASKS
    //--------------------------------------------------------------------------
    public Long save(TaskEntity p) {
        mongoTemplate.save(p);
        return p.getId();
    }

    public void saveOrUpdate(TaskEntity t) {
        mongoTemplate.save(t);
    }

    public void deleteDateData(Long task_id) {
        TaskEntity t = findTaskById(task_id);
        t.setStart(null);
        t.setEnd(null);
        t.setAllDay(Boolean.TRUE);
        saveOrUpdate(t);
    }

    public TaskEntity findTaskById(Long id) {
        return mongoTemplate.findById(id, TaskEntity.class);
    }

    public List<TaskEntity> loadTasksByProject(Long project_id) {
        Query query1 = new Query();
        query1.addCriteria(Criteria.where("project.id").is(project_id));
        
        return mongoTemplate.find(query1, TaskEntity.class);
    }

    public Long countTasksById(Long project_id) {
        Query query1 = new Query();
        query1.addCriteria(Criteria.where("project.id").is(project_id));
        
        return mongoTemplate.count(query1, TaskEntity.class);        
    }

    public List<TaskEntity> loadTaskByLabel(Long label_id) {
        Query query1 = new Query();
        query1.addCriteria(Criteria.where("labels.id").is(label_id));
        
        return mongoTemplate.find(query1, TaskEntity.class);
    }

    public List<TaskEntity> findAllSubtasks(String parent_id) {
        List<TaskEntity> resultList = new ArrayList<>();
        
        Query query1 = new Query();
        query1.addCriteria(Criteria.where("parent").is(parent_id));
        
        List<TaskEntity> list = mongoTemplate.find(query1, TaskEntity.class);
        if (!list.isEmpty()) {
            resultList.addAll(list);
            for (TaskEntity taskEntity : list) {
                resultList.addAll(findAllSubtasks(String.valueOf(taskEntity.getId())));
            }
        }
        return resultList;
    }

    public void deleteTask(Long task_id) {
        TaskEntity t = findTaskById(task_id);
        List<TaskEntity> findAllSubtasks = findAllSubtasks(String.valueOf(t.getId()));
        for (TaskEntity subTask : findAllSubtasks) {
            deleteTaskRepeatDataEntity(subTask.getId());
            mongoTemplate.remove(subTask);
        }
        deleteTaskRepeatDataEntity(t.getId());
        mongoTemplate.remove(t);
    }

    //--------------------------------------------------------------------------
    // TASK REPEAT DATA
    //--------------------------------------------------------------------------
    public TaskRepeatDataEntity findRepeatDataByTaskId(Long id) {
        Query query1 = new Query();
        query1.addCriteria(Criteria.where("task.id").is(id));
        
        return mongoTemplate.findOne(query1, TaskRepeatDataEntity.class);
    }

    public void saveOrUpdate(TaskRepeatDataEntity t) {
        mongoTemplate.save(t);
    }

    public List<TaskRepeatDataEntity> findRepeatableTasks() {
        List<CalendarEntity> findSelected = findSelectedCalendars();
        List<Long> findSelectedIds = Lambda.extract(findSelected, Lambda.on(CalendarEntity.class).getId());
        findSelectedIds.add(Long.MIN_VALUE);
        
        Query query1 = new Query();
        query1.addCriteria(Criteria.where("task.calendar.id").in(findSelectedIds));
        
        return mongoTemplate.find(query1, TaskRepeatDataEntity.class);
    }

    public void deleteCurrentEvent(Long task_id, Date date) {
        TaskRepeatDataEntity rep = findRepeatDataByTaskId(task_id);
        if (rep != null) {
            EventException ee = new EventException();
            ee.setTaskRepeatDataEntity(rep);
            ee.setDateException(date);
            save(ee);
            rep.getEventsEx().add(ee);
            saveOrUpdate(rep);
        } else {
            deleteDateData(task_id);
        }
    }

    public void deleteTaskRepeatDataEntity(Long task_id) {
        TaskRepeatDataEntity rep = findRepeatDataByTaskId(task_id);
        if (rep != null) {
            deleteEventExceptions(rep);
            mongoTemplate.remove(rep);
        }
        deleteDateData(task_id);
    }

    public void save(EventException ee) {
        mongoTemplate.remove(ee);
    }

    public List<EventException> findEventExceptions(TaskRepeatDataEntity te) {
        Query query1 = new Query();
        query1.addCriteria(Criteria.where("taskRepeatDataEntity.id").is(te.getId()));
        
        return mongoTemplate.find(query1, EventException.class);
    }

    public void deleteEventExceptions(TaskRepeatDataEntity rep) {
        rep.setEventsEx(null);
        saveOrUpdate(rep);    
        
        Query query1 = new Query();
        query1.addCriteria(Criteria.where("taskRepeatDataEntity.id").is(rep.getId()));
        
        mongoTemplate.remove(query1, TaskRepeatDataEntity.class);
    }

    //--------------------------------------------------------------------------
    // LINKS
    //--------------------------------------------------------------------------

    public void deleteLinks(TaskEntity task) {
        for (Iterator iterator1 = task.getLinks().iterator(); iterator1.hasNext();) {
            LinkEntity next = (LinkEntity) iterator1.next();
            iterator1.remove();
            saveOrUpdate(task);
            mongoTemplate.remove(next);
        }
    }

    //--------------------------------------------------------------------------
    // EVENTS
    //--------------------------------------------------------------------------
    public List<TaskEntity> findTasksBetween(Date start, Date end) {
        List<CalendarEntity> findSelected = findSelectedCalendars();
        List<Long> findSelectedIds = Lambda.extract(findSelected, Lambda.on(CalendarEntity.class).getId());
        findSelectedIds.add(Long.MIN_VALUE);
        
        Criteria c0 = Criteria.where("calendar.id").in(findSelectedIds);
        
        Criteria c1 = Criteria.where("start").gte(start).lte(end);
        Criteria c2 = Criteria.where("end").gte(start).lte(end);
        Criteria c3 = Criteria.where("start").lte(start).and("end").gte(start);
        Criteria c4 = Criteria.where("start").lte(end).and("end").gte(end);                                
        
        Query query1 = new Query();
        query1.addCriteria(c0.orOperator(c1.orOperator(c2,c3,c4)));
        
        return mongoTemplate.find(query1, TaskEntity.class);
    }

    
    //--------------------------------------------------------------------------
    // GROUP && SORT 
    //--------------------------------------------------------------------------
    public void saveOrUpdate(GroupSortEntity t) {
        mongoTemplate.save(t);
    }

    public GroupSortEntity findGSBySelectedViewAndId(String view, String id) {
        Query query1 = new Query();
        query1.addCriteria(Criteria.where("selectedView").is(view).and("selectedId").is(id));
        
        return mongoTemplate.findOne(query1, GroupSortEntity.class);
    }

    public void deleteGroupAndSort(String view, String id) {
        Query query1 = new Query();
        query1.addCriteria(Criteria.where("selectedView").is(view).and("selectedId").is(id));
        
        mongoTemplate.remove(query1, GroupAndSortService.class);
    }

}
