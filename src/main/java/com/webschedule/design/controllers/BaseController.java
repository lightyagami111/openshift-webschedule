package com.webschedule.design.controllers;

import com.webschedule.design.datastructure.CalendarUIEventDTO;
import com.webschedule.design.datastructure.GroupSortDTO;
import com.webschedule.design.datastructure.GroupSortEntity;
import com.webschedule.design.datastructure.GroupSortFromLabelDTO;
import com.webschedule.design.datastructure.GroupSortFromProjectDTO;
import com.webschedule.design.datastructure.LabelEntity;
import com.webschedule.design.datastructure.LinkEntity;
import com.webschedule.design.datastructure.ProjectEntity;
import com.webschedule.design.datastructure.SaveRequestTaskDTO;
import com.webschedule.design.datastructure.TaskAndSubTasksDTO;
import com.webschedule.design.datastructure.TaskEntity;
import com.webschedule.design.datastructure.TaskRepeatDataDTO;
import com.webschedule.design.services.DaoService;
import com.webschedule.design.services.GroupAndSortService;
import com.webschedule.design.services.TasksService;
import com.webschedule.design.services.Utils;
import java.io.IOException;
import java.text.ParseException;
import java.util.Date;
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
    private TasksService tasksService;

    @Autowired
    private GroupAndSortService groupAndSortService;

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
        if (daoService.countTasksById(id) > 0) {
            return new ResponseEntity(">> this project can't be deleted. Please delete project tasks first!", HttpStatus.CONFLICT);
        }
        daoService.deleteProject(id);

        return new ResponseEntity(HttpStatus.OK);
    }

    //--------------------------------------------------------------------------
    // TASKS
    //--------------------------------------------------------------------------
    @RequestMapping(value = {"/loadTasksByProject/group"}, method = RequestMethod.GET)
    public @ResponseBody
    GroupSortFromProjectDTO loadTasksByProjectGroup(@RequestParam(value = "id") Long id) {
        ProjectEntity pe = daoService.findProjectById(id);
        GroupSortEntity gs = groupAndSortService.getExistingOrDefault("projects", String.valueOf(id));
        List<TaskEntity> tasks = daoService.loadTasksByProject(id);
        List<GroupSortDTO> groups = tasksService.groupAndSort(gs, tasks);

        GroupSortFromProjectDTO result = new GroupSortFromProjectDTO();
        result.setE(pe);
        result.setGroups(groups);
        result.setAllowNewTaskAction(groupAndSortService.allowNewTaskAction(gs));

        return result;
    }

    @RequestMapping(value = {"/loadTasksByProject"}, method = RequestMethod.GET)
    public @ResponseBody
    List<TaskEntity> loadTasksByProject(@RequestParam(value = "id") Long id) {
        return daoService.loadTasksByProject(id);
    }

    @RequestMapping(value = {"/loadTasksByLabel"}, method = RequestMethod.GET)
    public @ResponseBody
    GroupSortFromLabelDTO loadTasksByLabel(@RequestParam(value = "id") Long id) {
        LabelEntity le = daoService.findLabelById(id);
        GroupSortEntity gs = groupAndSortService.getExistingOrDefault("labels", String.valueOf(id));
        List<TaskEntity> tasks = daoService.loadTaskByLabel(id);
        List<GroupSortDTO> groups = tasksService.groupAndSort(gs, tasks);

        GroupSortFromLabelDTO result = new GroupSortFromLabelDTO();
        result.setE(le);
        result.setGroups(groups);
        result.setAllowNewTaskAction(groupAndSortService.allowNewTaskAction(gs));

        return result;
    }

    @RequestMapping(value = {"/loadTaskData"}, method = RequestMethod.GET)
    public @ResponseBody
    TaskAndSubTasksDTO loadTaskData(@RequestParam(value = "id") Long id) {
        return tasksService.loadTaskData(id);
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
            return new ResponseEntity("Default project not found (project_id=" + project_id + ")", HttpStatus.NOT_FOUND);
        }
        TaskEntity t = tasksService.loadInitialTaskData(findProjectById, parent_id, insert, labels, start, end);

        return ResponseEntity.ok(t);
    }

    @RequestMapping(value = {"/saveTaskData"}, method = RequestMethod.POST)
    public @ResponseBody
    ResponseEntity saveTaskData(@RequestBody SaveRequestTaskDTO dTO) throws ParseException {
        if (dTO.getAllDay() == false) {
            if (StringUtils.isBlank(dTO.getStart())) {
                return new ResponseEntity(">> allDay is false ; start date/time is null", HttpStatus.BAD_REQUEST);
            }
        }

        ProjectEntity project = daoService.findProjectById(dTO.getProject_id());
        if (project == null) {
            return new ResponseEntity("Project not found (project_id=" + dTO.getProject_id() + ")", HttpStatus.NOT_FOUND);
        }

        tasksService.saveTaskData(dTO, project);

        return ResponseEntity.ok(dTO);
    }

    @RequestMapping(value = {"/loadTaskRepeatData"}, method = RequestMethod.GET)
    public @ResponseBody
    TaskRepeatDataDTO loadTaskRepeatData(@RequestParam(value = "id", required = false) Long task_id) {
        return tasksService.loadTaskRepeatData(task_id);
    }

    @RequestMapping(value = {"/saveTaskRepeatData"}, method = RequestMethod.POST)
    public @ResponseBody
    ResponseEntity saveTaskRepeatData(@RequestBody TaskRepeatDataDTO dTO) {

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

        tasksService.saveTaskRepeatData(dTO, task);

        return ResponseEntity.ok(dTO);
    }

    @RequestMapping(value = {"/loadTaskRepeatDataCurrentEvent"}, method = RequestMethod.GET)
    public @ResponseBody
    Map loadTaskRepeatDataCurrentEvent(
            @RequestParam(value = "id") Long task_id,
            @RequestParam(value = "start") @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss") Date start,
            @RequestParam(value = "end", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss") Date end
    ) {
        TaskAndSubTasksDTO t = tasksService.loadTaskRepeatDataCurrentEvent(task_id, start, end);

        return Utils.mapOf(
                "taskData", t,
                "task_id", task_id,
                "repeatTaskStart", Utils.format(start)
        );
    }

    @RequestMapping(value = {"/updateTaskTitle"}, method = RequestMethod.PUT)
    public @ResponseBody
    TaskEntity updateTaskTitle(
            @RequestParam(value = "id") Long id,
            @RequestParam(value = "new_title") String new_title) {
        return tasksService.updateTaskTitle(id, new_title);
    }

    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(value = {"/deleteTaskData"}, method = RequestMethod.DELETE)
    public @ResponseBody
    ResponseEntity deleteTaskData(@RequestParam(value = "id") Long id) {
        daoService.deleteTask(id);
        return ResponseEntity.ok(id);
    }

    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(value = {"/deleteRCurrentTaskData"}, method = RequestMethod.DELETE)
    public @ResponseBody
    ResponseEntity deleteRCurrentTaskData(@RequestParam(value = "id") Long id, @RequestParam(value = "date") @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss") Date date) {
        daoService.deleteCurrentEvent(id, date);
        return ResponseEntity.ok(id);
    }

    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(value = {"/deleteRAllTaskData"}, method = RequestMethod.DELETE)
    public @ResponseBody
    ResponseEntity deleteRAllTaskData(@RequestParam(value = "id") Long id) {
        daoService.deleteTaskRepeatDataEntity(id);
        return ResponseEntity.ok(id);
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

    @RequestMapping(value = {"/deleteLabel"}, method = RequestMethod.DELETE)
    public @ResponseBody
    ResponseEntity deleteLabel(@RequestParam(value = "id") Long id) {
        daoService.deleteLabel(id);
        return ResponseEntity.ok(id);
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
        return tasksService.loadEvents(start, end);
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

    @RequestMapping(value = "/link", method = RequestMethod.DELETE)
    public @ResponseBody
    ResponseEntity deleteLink(@RequestParam(value = "link") Long link, @RequestParam(value = "task") Long task) {
        daoService.deleteLink(link, task);
        return ResponseEntity.ok(link);
    }

    @RequestMapping(value = "/link", method = RequestMethod.PUT)
    public @ResponseBody
    ResponseEntity updateLink(@RequestBody Map<String, Object> body) {
        Long id = Long.valueOf(body.get("id").toString());
        String url = body.get("url").toString();
        String title = body.get("title").toString();

        LinkEntity le = daoService.findLinkById(id);
        le.setTitle(title);
        le.setUrl(url);
        daoService.updateLink(le);
        return ResponseEntity.ok(le);
    }

    //--------------------------------------------------------------------------
    // GROUP && SORT 
    //--------------------------------------------------------------------------
    @RequestMapping(value = "/gs", method = RequestMethod.POST)
    public @ResponseBody
    GroupSortEntity saveOrUpdateGS(@RequestBody Map<String, String> body) {
        String view = body.get("view");
        String id = body.get("id");
        String group = body.get("group");
        String sort = body.get("sort");

        return groupAndSortService.saveOrUpdate(view, id, group, sort);
    }

    @RequestMapping(value = "/gs", method = RequestMethod.GET)
    public @ResponseBody
    ResponseEntity getGS(@RequestParam(value = "selectedView") String view, @RequestParam(value = "selectedId") String id) {
        return ResponseEntity.ok(groupAndSortService.getExistingOrDefault(view, id));
    }

}
