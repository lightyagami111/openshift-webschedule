/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this tgetCurrentSession()plate file, choose Tools | TgetCurrentSession()plates
 * and open the tgetCurrentSession()plate in the editor.
 */
package com.webschedule.design.services;

import ch.lambdaj.Lambda;
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
import java.util.Map;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate4.HibernateTransactionManager;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author ivaylo
 */
@Transactional
@Component
public class DaoService {

    @Autowired
    private HibernateTransactionManager hibernateTransactionManager;

    private Session getCurrentSession() {
        return hibernateTransactionManager.getSessionFactory().getCurrentSession();
    }

    public void persist(ProjectEntity p) {
        getCurrentSession().persist(p);
    }

    public List<ProjectEntity> findAllProjects() {
        return getCurrentSession().createQuery("SELECT p FROM ProjectEntity p").list();
    }

    public ProjectEntity findProjectById(Long id) {
        ProjectEntity p = null;
        List list = getCurrentSession().createQuery("SELECT p FROM ProjectEntity p WHERE p.id = :_id").setParameter("_id", id).list();
        if (list != null && !list.isEmpty()) {
            p = (ProjectEntity) list.get(0);
        }
        return p;
    }

    public void updateProject(ProjectEntity p) {
        getCurrentSession().merge(p);
    }

    public void deleteProject(Long id) {
        ProjectEntity p = findProjectById(id);
        List<ProjectEntity> children = getCurrentSession().createQuery("SELECT p FROM ProjectEntity p WHERE p.parent = :_pid").setParameter("_pid", String.valueOf(id)).list();
        if (children != null && !children.isEmpty()) {
            for (ProjectEntity children1 : children) {
                getCurrentSession().delete(children1);
            }
        }
        getCurrentSession().delete(p);
        
        deleteGroupAndSort("projects", String.valueOf(id));
    }

    public ProjectEntity findDefaultProject() {
        ProjectEntity p = null;
        List list = getCurrentSession().createQuery("SELECT p FROM ProjectEntity p WHERE p.defaultProject = TRUE").list();
        if (list != null && !list.isEmpty()) {
            p = (ProjectEntity) list.get(0);
        }
        return p;
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
    // LABELS
    //--------------------------------------------------------------------------
    public void persist(LabelEntity p) {
        getCurrentSession().persist(p);
    }

    public List<LabelEntity> findAllLabels() {
        return getCurrentSession().createQuery("SELECT p FROM LabelEntity p").list();
    }

    public LabelEntity findLabelById(Long id) {
        LabelEntity p = null;
        List list = getCurrentSession().createQuery("SELECT p FROM LabelEntity p WHERE p.id = :_id").setParameter("_id", id).list();
        if (list != null && !list.isEmpty()) {
            p = (LabelEntity) list.get(0);
        }
        return p;
    }

    public void updateLabel(LabelEntity p) {
        getCurrentSession().merge(p);
    }

    public void deleteLabel(Long id) {        
        LabelEntity p = findLabelById(id);        
        getCurrentSession().delete(p);
        
        deleteGroupAndSort("labels", String.valueOf(id));
    }

    //--------------------------------------------------------------------------
    // TASKS
    //--------------------------------------------------------------------------
    public Long save(TaskEntity p) {
        return (Long) getCurrentSession().save(p);
    }

    public void saveOrUpdate(TaskEntity t) {
        getCurrentSession().saveOrUpdate(t);
    }

    public void deleteDateData(Long task_id) {
        TaskEntity t = findTaskById(task_id);
        t.setStart(null);
        t.setEnd(null);
        t.setAllDay(Boolean.TRUE);
        saveOrUpdate(t);
    }

    public TaskEntity findTaskById(Long id) {
        TaskEntity p = null;
        List list = getCurrentSession().createQuery("SELECT p FROM TaskEntity p WHERE p.id = :_id").setParameter("_id", id).list();
        if (list != null && !list.isEmpty()) {
            p = (TaskEntity) list.get(0);
        }
        return p;
    }

    public List<TaskEntity> loadTasksByProject(Long project_id) {
        return getCurrentSession().createQuery("SELECT p FROM TaskEntity p WHERE p.project.id = :_id").setParameter("_id", project_id).list();
    }
    
    public Long countTasksById(Long project_id) {
        return (Long) getCurrentSession().createQuery("SELECT COUNT(*) FROM TaskEntity p WHERE p.project.id = :_id").setParameter("_id", project_id).uniqueResult();
    }

    public List<TaskEntity> loadTaskByLabel(Long label_id) {
        return getCurrentSession().createQuery("SELECT p FROM TaskEntity p JOIN p.labels pl WHERE pl.id = :_id").setParameter("_id", label_id).list();
    }

    public List<TaskEntity> findAllSubtasks(String parent_id) {
        List<TaskEntity> resultList = new ArrayList<>();
        List<TaskEntity> list = getCurrentSession().createQuery("SELECT p FROM TaskEntity p WHERE p.parent = :_id").setParameter("_id", parent_id).list();
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
            getCurrentSession().delete(subTask);
        }
        deleteTaskRepeatDataEntity(t.getId());
        getCurrentSession().delete(t);
    }

    //--------------------------------------------------------------------------
    // TASK REPEAT DATA
    //--------------------------------------------------------------------------
    public TaskRepeatDataEntity findRepeatDataByTaskId(Long id) {
        TaskRepeatDataEntity p = null;
        List list = getCurrentSession().createQuery("SELECT p FROM TaskRepeatDataEntity p WHERE p.task.id = :_id").setParameter("_id", id).list();
        if (list != null && !list.isEmpty()) {
            p = (TaskRepeatDataEntity) list.get(0);
        }
        return p;
    }

    public void saveOrUpdate(TaskRepeatDataEntity t) {
        getCurrentSession().saveOrUpdate(t);
    }

    public List<TaskRepeatDataEntity> findRepeatableTasks() {
        return getCurrentSession().createQuery("SELECT p FROM TaskRepeatDataEntity p").list();
    }

    public void deleteCurrentEvent(Long task_id, Date date) {
        TaskRepeatDataEntity rep = findRepeatDataByTaskId(task_id);
        if (rep != null) {
            EventException ee = new EventException();
            ee.setTaskRepeatDataEntity(rep);
            ee.setDateException(date);
            save(ee);
        } else {
            deleteDateData(task_id);
        }
    }

    public void deleteTaskRepeatDataEntity(Long task_id) {
        TaskRepeatDataEntity rep = findRepeatDataByTaskId(task_id);
        if (rep != null) {
            deleteEventExceptions(rep);
            getCurrentSession().delete(rep);
        }
        deleteDateData(task_id);
    }

    public void save(EventException ee) {
        getCurrentSession().save(ee);
    }

    public List<EventException> findEventExceptions(TaskRepeatDataEntity te) {
        return getCurrentSession().createQuery("SELECT e FROM EventException e WHERE e.taskRepeatDataEntity.id = :_id")
                .setParameter("_id", te.getId())
                .list();
    }

    public void deleteEventExceptions(TaskRepeatDataEntity rep) {
        List<EventException> findEventExceptions = findEventExceptions(rep);
        for (EventException findEventException : findEventExceptions) {
            getCurrentSession().delete(findEventException);
        }
    }

    //--------------------------------------------------------------------------
    // LINKS
    //--------------------------------------------------------------------------

    public void deleteLinks(TaskEntity task) {
        for (Iterator iterator1 = task.getLinks().iterator(); iterator1.hasNext();) {
            LinkEntity next = (LinkEntity) iterator1.next();
            iterator1.remove();
            saveOrUpdate(task);
            getCurrentSession().delete(next);
        }
    }

    //--------------------------------------------------------------------------
    // EVENTS
    //--------------------------------------------------------------------------
    public List<TaskEntity> findTasksBetween(Date start, Date end) {
        List<Long> trs = getCurrentSession().createQuery("SELECT p.task.id FROM TaskRepeatDataEntity p").list();
        trs.add(Long.MIN_VALUE); //just to make sure list is not empty

        List<TaskEntity> list1 = getCurrentSession()
                .createQuery("SELECT p FROM TaskEntity p WHERE (:_start <= p.start OR p.start <= :_end) AND p.id NOT IN (:_trs)")
                .setParameter("_start", start)
                .setParameter("_end", end)
                .setParameterList("_trs", trs)
                .list();

        List<Long> excludeTasksList1 = Lambda.extract(list1, Lambda.on(TaskEntity.class).getId());
        excludeTasksList1.add(Long.MIN_VALUE); //just to make sure list is not empty

        List<TaskEntity> list2 = getCurrentSession()
                .createQuery("SELECT p FROM TaskEntity p WHERE (:_start <= p.end OR p.end <= :_end) AND p.id NOT IN (:_trs) AND p.id NOT IN (:_list1)")
                .setParameter("_start", start)
                .setParameter("_end", end)
                .setParameterList("_trs", trs)
                .setParameterList("_list1", excludeTasksList1)
                .list();

        list1.addAll(list2);

        Map<Long, TaskEntity> m = Lambda.index(list1, Lambda.on(TaskEntity.class).getId());

        return new ArrayList<>(m.values());
    }
    
    
    //--------------------------------------------------------------------------
    // GROUP && SORT 
    //--------------------------------------------------------------------------
    public void saveOrUpdate(GroupSortEntity t) {
        getCurrentSession().saveOrUpdate(t);
    }
    
    public GroupSortEntity findGSBySelectedViewAndId(String view, String id) {
        return (GroupSortEntity) getCurrentSession()
                .createQuery("FROM GroupSortEntity gs WHERE gs.selectedView = :_selectedView AND gs.selectedId = :_selectedId")
                .setParameter("_selectedView", view)
                .setParameter("_selectedId", id)
                .uniqueResult();
    }
    
    public void deleteGroupAndSort(String view, String id) {
        getCurrentSession().createQuery("DELETE FROM GroupSortEntity gs WHERE gs.selectedView = :_selectedView AND gs.selectedId = :_selectedId")
                .setParameter("_selectedView", view)
                .setParameter("_selectedId", id)
                .executeUpdate();
    }
    
}
