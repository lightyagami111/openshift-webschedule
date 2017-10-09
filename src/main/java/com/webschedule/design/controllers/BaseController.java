package com.webschedule.design.controllers;

import com.webschedule.design.datastructure.CalendarUIEventDTO;
import com.webschedule.design.datastructure.GroupSortEntity;
import com.webschedule.design.datastructure.LabelEntity;
import com.webschedule.design.datastructure.LabelTaskDTO;
import com.webschedule.design.datastructure.LinkEntity;
import com.webschedule.design.datastructure.ProjectEntity;
import com.webschedule.design.datastructure.SaveRequestTaskDTO;
import com.webschedule.design.datastructure.TaskEntity;
import com.webschedule.design.datastructure.TaskRepeatDataDTO;
import com.webschedule.design.datastructure.TaskRepeatDataEntity;
import com.webschedule.design.services.Copier;
import com.webschedule.design.services.DaoService;
import com.webschedule.design.services.RepeatableService;
import com.webschedule.design.services.Utils;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@Controller
public class BaseController {

    @Autowired
    private DaoService daoService;

    @Autowired
    private RepeatableService repeatableService;

    //--------------------------------------------------------------------------
    // PROJECTS
    //--------------------------------------------------------------------------
    @RequestMapping(value = {"/loadProjects"}, method = RequestMethod.GET)
    public @ResponseBody
    List<ProjectEntity> loadProjects() {
        return daoService.findAllProjects();
    }

    @RequestMapping(value = {"/getDefaultProject"}, method = RequestMethod.GET)
    public @ResponseBody
    Map getDefaultProject() {
        String res = "";
        ProjectEntity findDefaultProject = daoService.findDefaultProject();
        if (findDefaultProject != null) {
            res = findDefaultProject.getId().toString();
        }
        return Utils.mapOf("project_id", res);
    }

    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(value = {"/setDefaultProject"}, method = RequestMethod.PUT)
    public void setDefaultProject(@RequestParam(value = "id") Long id) {
        daoService.setDefaultProject(id);
    }

    @RequestMapping(value = {"/getProjectByid"}, method = RequestMethod.GET)
    public @ResponseBody
    ProjectEntity getProjectByid(@RequestParam(value = "id") Long id) {
        return daoService.findProjectById(id);
    }

    @RequestMapping(value = {"/addNewProject"}, method = RequestMethod.POST)
    public @ResponseBody
    ProjectEntity addNewProject(@RequestBody ProjectEntity dto) {
        daoService.persist(dto);
        return dto;
    }

    @RequestMapping(value = {"/updateProject"}, method = RequestMethod.PUT)
    public @ResponseBody
    ProjectEntity updateProject(@RequestBody ProjectEntity dto) {
        daoService.updateProject(dto);
        return dto;
    }


    @RequestMapping(value = {"/deleteProject"}, method = RequestMethod.DELETE)
    public ResponseEntity deleteProject(@RequestParam(value = "id") Long id) {
        if (!loadTasksByProject(id).isEmpty()) {
            return new ResponseEntity(">> this project can't be deleted. Please delete project tasks first!", HttpStatus.CONFLICT);
        }
        daoService.deleteProject(id);
        return new ResponseEntity(HttpStatus.OK);
    }

    //--------------------------------------------------------------------------
    // TASKS
    //--------------------------------------------------------------------------
    @RequestMapping(value = {"/loadTasksByProject"}, method = RequestMethod.GET)
    public @ResponseBody
    List<TaskEntity> loadTasksByProject(@RequestParam(value = "id") Long id) {
        return daoService.loadTasksByProject(id);
    }

    @RequestMapping(value = {"/loadTasksByLabel"}, method = RequestMethod.GET)
    public @ResponseBody
    List<LabelTaskDTO> loadTasksByLabel(@RequestParam(value = "id") Long id) {
        List<LabelTaskDTO> resultList = new ArrayList<>();

        List<TaskEntity> loadTaskByLabel = daoService.loadTaskByLabel(id);
        for (TaskEntity loadTaskByLabel1 : loadTaskByLabel) {
            LabelTaskDTO labelTaskDTO = new LabelTaskDTO();
            Copier.copy(loadTaskByLabel1, labelTaskDTO);
            labelTaskDTO.setParent("#");
            labelTaskDTO.setParent_text("");
            resultList.add(labelTaskDTO);

            List<TaskEntity> findAllSubtasks = daoService.findAllSubtasks(String.valueOf(loadTaskByLabel1.getId()));
            for (TaskEntity findAllSubtask : findAllSubtasks) {
                LabelTaskDTO labelSubTaskDTO = new LabelTaskDTO();
                Copier.copy(findAllSubtask, labelSubTaskDTO);
                resultList.add(labelSubTaskDTO);
            }
        }

        return resultList;
    }

    @RequestMapping(value = {"/loadTaskData"}, method = RequestMethod.GET)
    public @ResponseBody
    TaskEntity loadTaskData(@RequestParam(value = "id") Long id) {
        TaskEntity result = daoService.findTaskById(id);
        
        String parent_id = String.valueOf(result.getId());
        List<TaskEntity> findAllSubtasks = daoService.findAllSubtasks(parent_id);
        for (TaskEntity findAllSubtask : findAllSubtasks) {
            if (findAllSubtask.getParent().equals(parent_id)) {
                findAllSubtask.setParent("#");
            }
        }
        result.setSubTasks(new HashSet(findAllSubtasks));
        
        
        return result;
    }

    @RequestMapping(value = {"/getParentTaskProject"}, method = RequestMethod.GET)
    public @ResponseBody
    Map getParentTaskProject(@RequestParam(value = "parent_id") Long parent_id) {
        final TaskEntity result = daoService.findTaskById(parent_id);

        return Utils.mapOf("parent_project_id", result.getProject_id());
    }

    
    
    
    @RequestMapping(value = {"/loadInitialTaskData"}, method = RequestMethod.GET)
    public @ResponseBody
    ResponseEntity loadInitialTaskData(
            @RequestParam(value = "project_id") Long project_id,
            @RequestParam(value = "parent_id") String parent_id,
            @RequestParam(value = "insert") Boolean insert,
            @RequestParam(value = "labels", required = false) Long[] labels,
            @RequestParam(value = "start", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date start,
            @RequestParam(value = "end", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date end
    ) {
        ProjectEntity findProjectById = daoService.findProjectById(project_id);
        if (findProjectById == null) {
            return new ResponseEntity("Default project not found (project_id="+project_id+")", HttpStatus.NOT_FOUND);
        }
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

        return ResponseEntity.ok(t);
    }

    @RequestMapping(value = {"/saveTaskData"}, method = RequestMethod.POST)
    public @ResponseBody
    ResponseEntity saveTaskData(@RequestBody SaveRequestTaskDTO dTO) throws ParseException {
        if (dTO.getAllDay()==false) {
            if (StringUtils.isBlank(dTO.getStart())) {
                return new ResponseEntity(">> allDay is false ; start date/time is null", HttpStatus.BAD_REQUEST);
            }
        }

        ProjectEntity project = daoService.findProjectById(dTO.getProject_id());
        if (project == null) {
            return new ResponseEntity("Project not found (project_id="+dTO.getProject_id()+")", HttpStatus.NOT_FOUND);
        }

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

        return ResponseEntity.ok(dTO);
    }

    
    
    
    
    
    @RequestMapping(value = {"/loadTaskRepeatData"}, method = RequestMethod.GET)
    public @ResponseBody
    TaskRepeatDataDTO loadTaskRepeatData(@RequestParam(value = "id", required = false) Long task_id) {

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
    
    
    @RequestMapping(value = {"/saveTaskRepeatData"}, method = RequestMethod.POST)
    public @ResponseBody
    ResponseEntity saveTaskRepeatData(@RequestBody TaskRepeatDataDTO dTO) {
        TaskRepeatDataEntity t = daoService.findRepeatDataByTaskId(dTO.getTask_id());
        TaskEntity task = daoService.findTaskById(dTO.getTask_id());
        
        if (task == null) {
            return new ResponseEntity("task not found", HttpStatus.BAD_REQUEST);
        }
        if (StringUtils.isBlank(dTO.getMode())) {
            return new ResponseEntity("'mode' is null", HttpStatus.BAD_REQUEST);
        }
        if (dTO.getMode_start() == null) {
            return new ResponseEntity("'starting on' is null", HttpStatus.BAD_REQUEST);
        }
        
        if (t == null) {
            t = new TaskRepeatDataEntity();
        }
        else {
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

        return ResponseEntity.ok(dTO);
    }
    
    
    
    
    
    
    
    
    @RequestMapping(value = {"/loadTaskRepeatDataCurrentEvent"}, method = RequestMethod.GET)
    public @ResponseBody Map loadTaskRepeatDataCurrentEvent(
            @RequestParam(value = "id") Long task_id,
            @RequestParam(value = "start") @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss") Date start,
            @RequestParam(value = "end", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss") Date end
    ) {
        TaskEntity t = daoService.findTaskById(task_id);
        t.setId(null);
        t.setStart(start);
        t.setEnd(end);
        t.setParent(String.valueOf(task_id));
        t.setParent_text(t.getText());
        t.setAllDay(daoService.findRepeatDataByTaskId(task_id).getAllDay());
        
        return Utils.mapOf( "taskData", t, 
                            "task_id", task_id, 
                            "repeatTaskStart", Utils.format(start)
                        );
    }
    
    
    
    
    
    

    @RequestMapping(value = {"/updateTaskTitle"}, method = RequestMethod.PUT)
    public @ResponseBody
    TaskEntity updateTaskTitle(
            @RequestParam(value = "id") Long id,
            @RequestParam(value = "new_title") String new_title) {

        TaskEntity t = daoService.findTaskById(id);
        t.setText(new_title);
        daoService.saveOrUpdate(t);

        return t;
    }

    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(value = {"/deleteTaskData"}, method = RequestMethod.DELETE)
    public void deleteTaskData(@RequestParam(value = "id") Long id) {
        daoService.deleteTask(id);
    }

    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(value = {"/deleteRCurrentTaskData"}, method = RequestMethod.DELETE)
    public void deleteRCurrentTaskData(@RequestParam(value = "id") Long id, @RequestParam(value = "date") @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss") Date date) {
        daoService.deleteCurrentEvent(id, date);
    }

    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(value = {"/deleteRAllTaskData"}, method = RequestMethod.DELETE)
    public void deleteRAllTaskData(@RequestParam(value = "id") Long id) {
        daoService.deleteTaskRepeatDataEntity(id);
    }

    //--------------------------------------------------------------------------
    // LABELS
    //--------------------------------------------------------------------------
    @RequestMapping(value = "/loadLabels", method = RequestMethod.GET)
    public @ResponseBody
    List<LabelEntity> loadLabels() {
        return daoService.findAllLabels();
    }

    @RequestMapping(value = {"/getLabelByid"}, method = RequestMethod.GET)
    public @ResponseBody
    LabelEntity getLabelByid(@RequestParam(value = "id") Long id) {
        return daoService.findLabelById(id);
    }

    @RequestMapping(value = {"/addNewLabel"}, method = RequestMethod.POST)
    public @ResponseBody
    LabelEntity addNewLabel(@RequestBody LabelEntity dto) {
        daoService.persist(dto);
        return dto;
    }

    @RequestMapping(value = {"/updateLabel"}, method = RequestMethod.PUT)
    public @ResponseBody
    LabelEntity updateLabel(@RequestBody LabelEntity dto) {
        daoService.updateLabel(dto);
        return dto;
    }

    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(value = {"/deleteLabel"}, method = RequestMethod.DELETE)
    public void deleteLabel(@RequestParam(value = "id") Long id) {
        daoService.deleteLabel(id);
    }

    //--------------------------------------------------------------------------
    // EVENTS
    //--------------------------------------------------------------------------
    @RequestMapping(value = "/loadEvents", method = RequestMethod.GET)
    public @ResponseBody
    List<CalendarUIEventDTO> loadEvents(
            @RequestParam(value = "start") @DateTimeFormat(pattern = "yyyy-MM-dd") Date start,
            @RequestParam(value = "end") @DateTimeFormat(pattern = "yyyy-MM-dd") Date end
    ) {
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
            }
            else {
                if (f.getEnd() != null) {
                    Calendar c = Calendar.getInstance();
                    c.setTime(f.getEnd());                    
                    int h = c.get(Calendar.HOUR);
                    int m = c.get(Calendar.MINUTE);
                    
                    c.setTime(f.getStart());
                    c.set(Calendar.HOUR, h);
                    c.set(Calendar.MINUTE, m);
                    
                    if (c.get(Calendar.HOUR)!=0 || c.get(Calendar.MINUTE)!=0) {
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
    
    
    //--------------------------------------------------------------------------
    // LINKS
    //--------------------------------------------------------------------------

    @RequestMapping(value = "/link", method = RequestMethod.POST)
    public @ResponseBody
    LinkEntity getTitleFromUrlLink(@RequestBody Map<String, Object> body) throws IOException {
        Long taskId = Long.valueOf(body.get("taskId").toString());
        String url = body.get("url").toString();

        Document doc = Jsoup.connect(url).get();
        String title = doc.title();

        LinkEntity le = new LinkEntity();
        le.setTitle(title);
        le.setUrl(url);

        le.setId(daoService.save(le));

        TaskEntity te = daoService.findTaskById(taskId);
        te.getLinks().add(le);
        daoService.saveOrUpdate(te);

        return le;
    }

    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(value = "/link", method = RequestMethod.DELETE)
    public void deleteLink(@RequestParam(value = "link") Long link, @RequestParam(value = "task") Long task) {
        daoService.deleteLink(link, task);
    }

    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(value = "/link", method = RequestMethod.PUT)
    public void updateLink(@RequestBody Map<String, Object> body) {
        Long id = Long.valueOf(body.get("id").toString());
        String url = body.get("url").toString();
        String title = body.get("title").toString();

        LinkEntity le = daoService.findLinkById(id);
        le.setTitle(title);
        le.setUrl(url);
        daoService.updateLink(le);
    }
    
    
    
    
    //--------------------------------------------------------------------------
    // GROUP && SORT 
    //--------------------------------------------------------------------------
    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(value = "/gs", method = RequestMethod.POST)
    public void saveOrUpdateGS(@RequestBody Map<String, String> body) {
        String view = body.get("view");
        String id = body.get("id");
        String group = body.get("group");
        String sort = body.get("sort");
        
        GroupSortEntity selectedGS = daoService.findGSBySelectedViewAndId(view, id);
        if (selectedGS == null) {
            selectedGS = new GroupSortEntity();            
        }
        selectedGS.setSelectedView(view);
        selectedGS.setSelectedId(id);
        selectedGS.setGroup_(group);
        selectedGS.setSort_(sort);
        
        daoService.saveOrUpdate(selectedGS);
    }
    
    @RequestMapping(value = "/gs", method = RequestMethod.GET)
    public @ResponseBody ResponseEntity getGS(@RequestParam(value = "selectedView") String view, @RequestParam(value = "selectedId") String id) {
        GroupSortEntity selectedGS = daoService.findGSBySelectedViewAndId(view, id);
        if (selectedGS == null) {
            selectedGS = new GroupSortEntity();
            selectedGS.setSelectedView(view);
            selectedGS.setSelectedId(id);
            selectedGS.setGroup_("dont-group");
            selectedGS.setSort_("sort-sdate-up");
        }
        return ResponseEntity.ok(selectedGS);
    }

}
