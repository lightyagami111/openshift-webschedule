/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.webschedule.design.services;

import com.webschedule.design.datastructure.CalendarUIEventDTO;
import com.webschedule.design.datastructure.GroupSortDTO;
import com.webschedule.design.datastructure.GroupSortEntity;
import com.webschedule.design.datastructure.LabelEntity;
import com.webschedule.design.datastructure.ProjectEntity;
import com.webschedule.design.datastructure.SaveRequestTaskDTO;
import com.webschedule.design.datastructure.TaskAndSubTasksDTO;
import com.webschedule.design.datastructure.TaskEntity;
import com.webschedule.design.datastructure.TaskRepeatDataDTO;
import com.webschedule.design.datastructure.TaskRepeatDataEntity;
import com.webschedule.design.datastructure.TaskTreeDTO;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author asd
 */
@Service
public class TasksService {

    @Autowired
    private DaoService daoService;
    
    @Autowired
    private RepeatableService repeatableService;
    
    public List<GroupSortDTO> groupAndSort(GroupSortEntity gs, List<TaskEntity> tasks) {
        List<GroupSortDTO> result = new ArrayList<>();

        if (gs.getGroup_().equals("group-label")) {
            LabelEntity noGroup = new LabelEntity();
            noGroup.setId(null);
            noGroup.setText("no-group");
            Map<LabelEntity, List<TaskEntity>> map = new HashMap<>();
            for (TaskEntity task : tasks) {
                if (task.getLabels() != null && !task.getLabels().isEmpty()) {
                    for (LabelEntity key : task.getLabels()) {
                        List<TaskEntity> value = map.get(key);
                        if (value == null) {
                            value = new ArrayList<>();
                        }
                        value.add(task);
                        map.put(key, value);
                    }
                }
                else {
                    List<TaskEntity> value = map.get(noGroup);
                    if (value == null) {
                        value = new ArrayList<>();
                    }
                    value.add(task);
                    map.put(noGroup, value);
                }
            }

            for (Map.Entry<LabelEntity, List<TaskEntity>> entry : map.entrySet()) {
                LabelEntity key = entry.getKey();
                List<TaskEntity> value = entry.getValue();
                GroupSortDTO groupProjectTasksDTO = new GroupSortDTO();
                groupProjectTasksDTO.setGroupBy(key.getText());
                groupProjectTasksDTO.setGroupById(String.valueOf(key.getId()));

                List<TaskTreeDTO> list = new ArrayList<>();
                for (TaskEntity taskEntity : value) {
                    list.addAll(taskEntityToTree(taskEntity, false));
                }
                groupProjectTasksDTO.setTasks(list);
                result.add(groupProjectTasksDTO);
            }
            
        } 
        
        else if (gs.getGroup_().equals("group-project")) {
            Map<ProjectEntity, List<TaskEntity>> map = new HashMap<>();
            for (TaskEntity task : tasks) {
                ProjectEntity key = task.getProject();
                List<TaskEntity> value = map.get(key);
                if (value == null) {
                    value = new ArrayList<>();
                }
                value.add(task);
                map.put(key, value);
            }
            for (Map.Entry<ProjectEntity, List<TaskEntity>> entry : map.entrySet()) {
                ProjectEntity key = entry.getKey();
                List<TaskEntity> value = entry.getValue();
                GroupSortDTO groupProjectTasksDTO = new GroupSortDTO();
                groupProjectTasksDTO.setGroupBy(key.getText());
                groupProjectTasksDTO.setGroupById(String.valueOf(key.getId()));
                
                List<TaskTreeDTO> list = new ArrayList<>();
                for (TaskEntity taskEntity : value) {
                    list.addAll(taskEntityToTree(taskEntity, false));
                }
                groupProjectTasksDTO.setTasks(list);
                result.add(groupProjectTasksDTO);
            }
        }
        
        else if (gs.getGroup_().equals("group-sdate")) {
            Map<String, List<TaskEntity>> map = new HashMap<>();
            String noDate = "";
            for (TaskEntity task : tasks) {
                String key;
                if (task.getStart() != null) {
                    Calendar c = Calendar.getInstance();
                    c.setTime(task.getStart());
                    c.set(Calendar.HOUR_OF_DAY, 0);
                    key = Utils.format(c.getTime(),"yyyy-MM-dd");                    
                }
                else {
                    key = noDate;
                }
                List<TaskEntity> value = map.get(key);
                if (value == null) {
                    value = new ArrayList<>();
                }
                value.add(task);
                map.put(key, value);                
            }

            for (Map.Entry<String, List<TaskEntity>> entry : map.entrySet()) {
                String key = entry.getKey();
                List<TaskEntity> value = entry.getValue();
                GroupSortDTO groupProjectTasksDTO = new GroupSortDTO();
                groupProjectTasksDTO.setGroupBy(key);
                groupProjectTasksDTO.setGroupById(key);

                List<TaskTreeDTO> list = new ArrayList<>();
                for (TaskEntity taskEntity : value) {
                    list.addAll(taskEntityToTree(taskEntity, false));
                }
                groupProjectTasksDTO.setTasks(list);
                result.add(groupProjectTasksDTO);
            }

        } 
        
        else { // dont-group
            GroupSortDTO groupProjectTasksDTO = new GroupSortDTO();
            groupProjectTasksDTO.setGroupBy("");

            List<TaskTreeDTO> list = new ArrayList<>();
            for (TaskEntity taskEntity : tasks) {
                list.addAll(taskEntityToTree(taskEntity, true));
            }
            groupProjectTasksDTO.setTasks(list);
            result.add(groupProjectTasksDTO);
        }
        
        return result;
    }

    
    
    public List<TaskTreeDTO> taskEntityToTree(TaskEntity loadTaskByLabel1, boolean addSubtasks) {
        List<TaskTreeDTO> resultList = new ArrayList<>();

        TaskTreeDTO labelTaskDTO = new TaskTreeDTO();
        Copier.copy(loadTaskByLabel1, labelTaskDTO);
        labelTaskDTO.setParent("#");
        labelTaskDTO.setParent_text("");
        resultList.add(labelTaskDTO);

        if (addSubtasks) {
            List<TaskEntity> findAllSubtasks = daoService.findAllSubtasks(String.valueOf(loadTaskByLabel1.getId()));
            for (TaskEntity findAllSubtask : findAllSubtasks) {
                TaskTreeDTO labelSubTaskDTO = new TaskTreeDTO();
                Copier.copy(findAllSubtask, labelSubTaskDTO);
                resultList.add(labelSubTaskDTO);
            }
        }

        return resultList;
    }
    
    
    public TaskAndSubTasksDTO loadTaskData(Long task_id) {
        TaskEntity task = daoService.findTaskById(task_id);
        TaskAndSubTasksDTO result = new TaskAndSubTasksDTO();
        Copier.copy(task, result);

        String parent_id = String.valueOf(task.getId());
        List<TaskEntity> findAllSubtasks = daoService.findAllSubtasks(parent_id);
        for (TaskEntity findAllSubtask : findAllSubtasks) {
            if (findAllSubtask.getParent().equals(parent_id)) {
                findAllSubtask.setParent("#");
            }
        }
        result.setSubTasks(new HashSet(findAllSubtasks));
        
        return result;
    }
    
    
    public TaskEntity loadInitialTaskData(ProjectEntity findProjectById, String parent_id, Boolean insert, Long[] labels, Date start, Date end) {
        TaskEntity t = new TaskEntity();
        t.setProject(findProjectById);

        t.setParent(parent_id);
        if (!parent_id.equals("#")) {
            t.setParent_text(daoService.findTaskById(Long.parseLong(parent_id)).getText());
        }

        t.setStart(start);
        t.setEnd(end);
        t.setAllDay(true);

        if (labels != null) {
            for (Long label_id : labels) {
                LabelEntity label = daoService.findLabelById(label_id);
                t.getLabels().add(label);
            }
        }

        t.setPriority(1);

        if (insert) {
            t.setId(daoService.save(t));
        }
        return t;
    }
    
    
    
    public void saveTaskData(SaveRequestTaskDTO dTO, ProjectEntity project) throws ParseException {
        TaskEntity t;
        if (dTO.getId() != null) {
            t = daoService.findTaskById(dTO.getId());

            TaskRepeatDataEntity tr = daoService.findRepeatDataByTaskId(dTO.getId());
            if (tr != null) {
                tr.setDo_repeat(dTO.getDo_repeat());
                daoService.saveOrUpdate(tr);
            }

        } else {
            t = new TaskEntity();
        }

        if (dTO.getFromRepeatTaskId() != null) {
            daoService.deleteCurrentEvent(dTO.getFromRepeatTaskId(), Utils.parse(dTO.getFromRepeatTaskStart()));
        }

        t.setText(dTO.getText());
        t.setStart(Utils.parse(dTO.getStart()));
        t.setEnd(Utils.parse(dTO.getEnd()));
        t.setParent(dTO.getParent());
        t.setParent_text(dTO.getParent_text());
        t.setProject(project);
        t.setNotes(dTO.getNotes());
        t.setPriority(dTO.getPriority());
        t.setAllDay(dTO.getAllDay());

        t.getLabels().clear();
        if (dTO.getLabels() != null) {
            for (Long label_id : dTO.getLabels()) {
                LabelEntity label = daoService.findLabelById(label_id);
                t.getLabels().add(label);
            }
        }

        daoService.saveOrUpdate(t);

        List<TaskEntity> subTasks = daoService.findAllSubtasks(String.valueOf(t.getId()));
        for (TaskEntity subTask : subTasks) {
            subTask.setProject(project);
            daoService.saveOrUpdate(subTask);
        }
    }
    
    
    
    public TaskRepeatDataDTO loadTaskRepeatData(Long task_id) {
        TaskRepeatDataDTO obj = new TaskRepeatDataDTO();

        TaskRepeatDataEntity t = null;
        if (task_id != null) {
            t = daoService.findRepeatDataByTaskId(task_id);
        }

        if (t != null) {
            obj.setDo_repeat(t.getDo_repeat());
            obj.setEndson(t.getEndson());
            obj.setEndson_count(t.getEndson_count());
            obj.setEndson_until(Utils.format(t.getEndson_until()));
            obj.setMode(t.getMode());
            obj.setMode_start(Utils.format(t.getMode_start()));
            obj.setMode_end(Utils.format(t.getMode_end()));
            obj.setAllDay(t.getAllDay());
            obj.setRepeat_days(t.getRepeat_days());
            obj.setRepeat_months(t.getRepeat_months());
            obj.setRepeat_wdays(t.getRepeat_wdays_asArray());
            obj.setRepeat_weeks(t.getRepeat_weeks());
            obj.setRepeatby(t.getRepeatby());
            obj.setTask_id(t.getTask().getId());
        } else {
            obj.setTask_id(task_id);
            obj.setDo_repeat(Boolean.FALSE);
            obj.setMode("d");
            obj.setEndson("never");
            obj.setRepeat_days(1);
            obj.setRepeat_weeks(1);
            obj.setRepeat_months(1);
            obj.setAllDay(Boolean.TRUE);
            obj.setMode_start(Utils.format(new Date()));
        }
        obj.setDbExist(t != null);
        
        return obj;
    }
    
    
    public void saveTaskRepeatData(TaskRepeatDataDTO dTO, TaskEntity task) {
        TaskRepeatDataEntity t = daoService.findRepeatDataByTaskId(dTO.getTask_id());
        
        if (t == null) {
            t = new TaskRepeatDataEntity();
        } else {
            if (Utils.checkForChangesToDeleteEventExceptions(t, dTO)) {
                daoService.deleteEventExceptions(t);
            }
        }

        t.setDo_repeat(dTO.getDo_repeat());
        t.setEndson(dTO.getEndson());
        t.setEndson_count(dTO.getEndson_count());
        t.setEndson_until(dTO.getEndson_until());
        t.setMode(dTO.getMode());
        t.setMode_start(dTO.getMode_start());
        t.setMode_end(dTO.getMode_end());
        t.setAllDay(dTO.getAllDay());
        t.setRepeat_days(dTO.getRepeat_days());
        t.setRepeat_months(dTO.getRepeat_months());
        t.setRepeat_wdays(String.join(",", dTO.getRepeat_wdays()));
        t.setRepeat_weeks(dTO.getRepeat_weeks());
        t.setRepeatby(dTO.getRepeatby());
        t.setTask(task);

        daoService.saveOrUpdate(t);
    }
    
    
    
    public TaskEntity loadTaskRepeatDataCurrentEvent(Long task_id, Date start, Date end) {
        TaskEntity t = daoService.findTaskById(task_id);
        t.setId(null);
        t.setStart(start);
        t.setEnd(end);
        t.setParent(String.valueOf(task_id));
        t.setParent_text(t.getText());
        t.setAllDay(daoService.findRepeatDataByTaskId(task_id).getAllDay());
        
        return t;
    }
    
    
    public TaskEntity updateTaskTitle(Long id, String new_title) {
        TaskEntity t = daoService.findTaskById(id);
        t.setText(new_title);
        daoService.saveOrUpdate(t);
        
        return t;
    }
    
    
    
    public List<CalendarUIEventDTO> loadEvents(Date start, Date end) {
        List<CalendarUIEventDTO> res = new ArrayList<>();

        List<TaskEntity> findTasksBetween = daoService.findTasksBetween(start, end);
        for (TaskEntity f : findTasksBetween) {
            CalendarUIEventDTO d = new CalendarUIEventDTO();
            d.setId(f.getId());
            d.setTitle(f.getText());
            d.setAllDay(f.getAllDay());
            d.setStart(Utils.format(f.getStart()));
            d.setColor(f.getProject_color());

            if (f.getAllDay()) {
                if (f.getEnd() != null) {
                    Calendar c = Calendar.getInstance();
                    c.setTime(f.getEnd());
                    c.add(Calendar.DATE, 1);
                    d.setEnd(Utils.format(c.getTime()));
                }
            } else {
                if (f.getEnd() != null) {
                    Calendar c = Calendar.getInstance();
                    c.setTime(f.getEnd());
                    int h = c.get(Calendar.HOUR);
                    int m = c.get(Calendar.MINUTE);

                    c.setTime(f.getStart());
                    c.set(Calendar.HOUR, h);
                    c.set(Calendar.MINUTE, m);

                    if (c.get(Calendar.HOUR) != 0 || c.get(Calendar.MINUTE) != 0) {
                        d.setEnd(Utils.format(c.getTime()));
                    }
                    f.setEnd(c.getTime());
                    daoService.saveOrUpdate(f);
                }
            }

            res.add(d);
        }

        List<TaskRepeatDataEntity> findRepeatableTasks = daoService.findRepeatableTasks(Boolean.TRUE);
        for (TaskRepeatDataEntity findRepeatableTask : findRepeatableTasks) {
            res.addAll(repeatableService.computeFireTimesBetween(findRepeatableTask, start, end));
        }

        return res;
    }
    
    
    
    

}
