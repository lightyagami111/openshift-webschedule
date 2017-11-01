package com.webschedule.design.controllers;

import com.webschedule.design.datastructure.CalendarEntity;
import com.webschedule.design.datastructure.CalendarUIEventDTO;
import com.webschedule.design.datastructure.GroupSortDTO;
import com.webschedule.design.datastructure.GroupSortEntity;
import com.webschedule.design.datastructure.GroupSortResponseDTO;
import com.webschedule.design.datastructure.LabelEntity;
import com.webschedule.design.datastructure.ProjectEntity;
import com.webschedule.design.datastructure.TaskEntity;
import com.webschedule.design.datastructure.TaskRepeatDataDTO;
import com.webschedule.design.datastructure.TaskRequestLoadDTO;
import com.webschedule.design.datastructure.TaskRequestSaveDTO;
import com.webschedule.design.services.DaoService;
import com.webschedule.design.services.GroupAndSortService;
import com.webschedule.design.services.TasksService;
import com.webschedule.design.services.Utils;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;
import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
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
    
    static Logger log = Logger.getLogger(BaseController.class.getName());
    

    @Autowired
    private DaoService daoService;

    @Autowired
    private TasksService tasksService;

    @Autowired
    private GroupAndSortService groupAndSortService;

    @RequestMapping(value = {"/cacheManager"}, method = RequestMethod.GET)
    public @ResponseBody
    String cacheManager() {
        StringBuilder sb = new StringBuilder();
        StringBuilder sb1 = new StringBuilder();
        CacheManager cacheManager = CacheManager.getInstance();
        long totalEvictedCount = 0l;
        long totalExpiredCount = 0l;
        long totalHitCount = 0l;
        long totalMissCount = 0l;
        long totalMissExpiredCount = 0l;
        long totalMissNotFoundCount = 0l;
        long totalPutAddedCount = 0l;
        long totalPutCount = 0l;
        long totalPutUpdatedCount = 0l;
        long totalRemoveCount = 0l;
        long totalLocalHeapSize = 0l;
        long totalLocalHeapSizeInBytes = 0l;
        long totalLocalOffHeapSize = 0l;
        long totalLocalOffHeapSizeInBytes = 0l;
        long totalRemoteSize = 0l;
        long totalSize = 0l;
        long totalHeapHitCount = 0l;
        long totalHeapMissCount = 0l;
        long totalHeapPutAddedCount = 0l;
        long totalHeapPutCount = 0l;
        long totalHeapPutUpdatedCount = 0l;
        long totalHeapRemoveCount = 0l;

        /* get stats for all known caches */
        for (String name : cacheManager.getCacheNames()) {
            Cache cache = cacheManager.getCache(name);
            totalEvictedCount = totalEvictedCount + cache.getStatistics().cacheEvictedCount();
            totalExpiredCount = totalExpiredCount + cache.getStatistics().cacheExpiredCount();
            totalHitCount = totalHitCount + cache.getStatistics().cacheHitCount();
            totalMissCount = totalMissCount + cache.getStatistics().cacheMissCount();
            totalMissExpiredCount = totalMissExpiredCount + cache.getStatistics().cacheMissExpiredCount();
            totalMissNotFoundCount = totalMissNotFoundCount + cache.getStatistics().cacheMissNotFoundCount();
            totalPutAddedCount = totalPutAddedCount + cache.getStatistics().cachePutAddedCount();
            totalPutCount = totalPutCount + cache.getStatistics().cachePutCount();
            totalPutUpdatedCount = totalPutUpdatedCount + cache.getStatistics().cachePutUpdatedCount();
            totalRemoveCount = totalRemoveCount + cache.getStatistics().cacheRemoveCount();
            totalLocalHeapSize = totalLocalHeapSize + cache.getStatistics().getLocalHeapSize();
            totalLocalHeapSizeInBytes = totalLocalHeapSizeInBytes + cache.getStatistics().getLocalHeapSizeInBytes();
            totalLocalOffHeapSize = totalLocalOffHeapSize + cache.getStatistics().getLocalOffHeapSize();
            totalLocalOffHeapSizeInBytes = totalLocalOffHeapSizeInBytes + cache.getStatistics().getLocalOffHeapSizeInBytes();
            totalRemoteSize = totalRemoteSize + cache.getStatistics().getRemoteSize();
            totalSize = totalSize + cache.getStatistics().getSize();
            totalHeapHitCount = totalHeapHitCount + cache.getStatistics().localHeapHitCount();
            totalHeapMissCount = totalHeapMissCount + cache.getStatistics().localHeapMissCount();
            totalHeapPutAddedCount = totalHeapPutAddedCount + cache.getStatistics().localHeapPutAddedCount();
            totalHeapPutCount = totalHeapPutCount + cache.getStatistics().localHeapPutCount();
            totalHeapPutUpdatedCount = totalHeapPutUpdatedCount + cache.getStatistics().localHeapPutUpdatedCount();
            totalHeapRemoveCount = totalHeapRemoveCount + cache.getStatistics().localHeapRemoveCount();

            sb.append(name)
                    .append("\n cacheEvictedCount = " + cache.getStatistics().cacheEvictedCount())
                    .append("\n cacheExpiredCount = " + cache.getStatistics().cacheExpiredCount())
                    .append("\n cacheHitCount = " + cache.getStatistics().cacheHitCount())
                    .append("\n cacheMissCount = " + cache.getStatistics().cacheMissCount())
                    .append("\n cacheMissExpiredCount = " + cache.getStatistics().cacheMissExpiredCount())
                    .append("\n cacheMissNotFoundCount = " + cache.getStatistics().cacheMissNotFoundCount())
                    .append("\n cachePutAddedCount = " + cache.getStatistics().cachePutAddedCount())
                    .append("\n cachePutCount = " + cache.getStatistics().cachePutCount())
                    .append("\n cachePutUpdatedCount = " + cache.getStatistics().cachePutUpdatedCount())
                    .append("\n cacheRemoveCount = " + cache.getStatistics().cacheRemoveCount())
                    .append("\n getLocalHeapSize = " + cache.getStatistics().getLocalHeapSize())
                    .append("\n getLocalHeapSizeInBytes = " + cache.getStatistics().getLocalHeapSizeInBytes())
                    .append("\n getLocalOffHeapSize = " + cache.getStatistics().getLocalOffHeapSize())
                    .append("\n getLocalOffHeapSizeInBytes = " + cache.getStatistics().getLocalOffHeapSizeInBytes())
                    .append("\n getRemoteSize = " + cache.getStatistics().getRemoteSize())
                    .append("\n getSize = " + cache.getStatistics().getSize())
                    .append("\n localHeapHitCount = " + cache.getStatistics().localHeapHitCount())
                    .append("\n localHeapMissCount = " + cache.getStatistics().localHeapMissCount())
                    .append("\n localHeapPutAddedCount = " + cache.getStatistics().localHeapPutAddedCount())
                    .append("\n localHeapPutCount = " + cache.getStatistics().localHeapPutCount())
                    .append("\n localHeapPutUpdatedCount = " + cache.getStatistics().localHeapPutUpdatedCount())
                    .append("\n localHeapRemoveCount = " + cache.getStatistics().localHeapRemoveCount())
                    .append("\n\n");
        }

        sb1.append("TOTAL : ")
                .append("\n cacheEvictedCount = " + totalEvictedCount)
                .append("\n cacheExpiredCount = " + totalExpiredCount)
                .append("\n cacheHitCount = " + totalHitCount)
                .append("\n cacheMissCount = " + totalMissCount)
                .append("\n cacheMissExpiredCount = " + totalMissExpiredCount)
                .append("\n cacheMissNotFoundCount = " + totalMissNotFoundCount)
                .append("\n cachePutAddedCount = " + totalPutAddedCount)
                .append("\n cachePutCount = " + totalPutCount)
                .append("\n cachePutUpdatedCount = " + totalPutUpdatedCount)
                .append("\n cacheRemoveCount = " + totalRemoveCount)
                .append("\n getLocalHeapSize = " + totalLocalHeapSize)
                .append("\n getLocalHeapSizeInBytes = " + totalLocalHeapSizeInBytes)
                .append("\n getLocalOffHeapSize = " + totalLocalOffHeapSize)
                .append("\n getLocalOffHeapSizeInBytes = " + totalLocalOffHeapSizeInBytes)
                .append("\n getRemoteSize = " + totalRemoteSize)
                .append("\n getSize = " + totalSize)
                .append("\n localHeapHitCount = " + totalHeapHitCount)
                .append("\n localHeapMissCount = " + totalHeapMissCount)
                .append("\n localHeapPutAddedCount = " + totalHeapPutAddedCount)
                .append("\n localHeapPutCount = " + totalHeapPutCount)
                .append("\n localHeapPutUpdatedCount = " + totalHeapPutUpdatedCount)
                .append("\n localHeapRemoveCount = " + totalHeapRemoveCount)
                .append("\n\n");

        return sb1.append(sb).toString();

    }

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
        String res = "null";
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
        ProjectEntity findProjectById = daoService.findProjectById(id);
        if (findProjectById.getDefaultProject()) {
            return new ResponseEntity(">> default project can't be deleted!", HttpStatus.CONFLICT);
        }
        if (daoService.countTasksById(id) > 0) {
            return new ResponseEntity(">> this project can't be deleted. Please delete project tasks first!", HttpStatus.CONFLICT);
        }
        daoService.deleteProject(id);

        return new ResponseEntity(HttpStatus.OK);
    }

    //--------------------------------------------------------------------------
    // CALENDARS
    //--------------------------------------------------------------------------
    @RequestMapping(value = {"/loadCalendars"}, method = RequestMethod.GET)
    public @ResponseBody
    List<CalendarEntity> loadCalendars() {
        return daoService.findAllCalendars();
    }

    @RequestMapping(value = {"/getCalendarByid"}, method = RequestMethod.GET)
    public @ResponseBody
    CalendarEntity getCalendarByid(@RequestParam(value = "id") Long id) {
        return daoService.findCalendarById(id);
    }

    @RequestMapping(value = {"/addNewCalendar"}, method = RequestMethod.POST)
    public @ResponseBody
    CalendarEntity addNewCalendar(@RequestBody CalendarEntity dto) {
        daoService.persist(dto);
        return dto;
    }

    @RequestMapping(value = {"/updateCalendar"}, method = RequestMethod.PUT)
    public @ResponseBody
    CalendarEntity updateCalendar(@RequestBody CalendarEntity dto) {
        daoService.update(dto);
        return dto;
    }

    @RequestMapping(value = {"/deleteCalendar"}, method = RequestMethod.DELETE)
    public ResponseEntity deleteCalendar(@RequestParam(value = "id") Long id) {
        CalendarEntity findById = daoService.findCalendarById(id);
        if (findById.getDefaultCalendar()) {
            return new ResponseEntity(">> default calendar can't be deleted!", HttpStatus.CONFLICT);
        }
        daoService.deleteCalendar(id);
        return new ResponseEntity(HttpStatus.OK);
    }

    @RequestMapping(value = {"/getDefaultCalendar"}, method = RequestMethod.GET)
    public @ResponseBody
    Map getDefaultCalendar() {
        String res = "null";
        CalendarEntity findDefaultCalendar = daoService.findDefaultCalendar();
        if (findDefaultCalendar != null) {
            res = findDefaultCalendar.getId().toString();
        }
        return Utils.mapOf("calendar_id", res);
    }

    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(value = {"/setDefaultCalendar"}, method = RequestMethod.PUT)
    public void setDefaultCalendar(@RequestParam(value = "id") Long id) {
        daoService.setDefaultCalendar(id);
    }

    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(value = {"/setSelectedCalendar"}, method = RequestMethod.PUT)
    public void setSelectedCalendars(@RequestParam(value = "ids") List<Long> ids) {
        List<CalendarEntity> findAll = daoService.findAllCalendars();
        for (CalendarEntity ce : findAll) {
            if (ids.contains(ce.getId())) {
                ce.setSelected(Boolean.TRUE);
            } else {
                ce.setSelected(Boolean.FALSE);
            }
            daoService.update(ce);
        }
    }

    //--------------------------------------------------------------------------
    // TASKS
    //--------------------------------------------------------------------------
    @RequestMapping(value = {"/loadTasksByProject/group"}, method = RequestMethod.GET)
    public @ResponseBody
    GroupSortResponseDTO loadTasksByProjectGroup(@RequestParam(value = "id") Long id) {
        ProjectEntity pe = daoService.findProjectById(id);
        GroupSortEntity gs = groupAndSortService.getExistingOrDefault("projects", String.valueOf(id));
        List<TaskEntity> tasks = daoService.loadTasksByProject(id);
        List<GroupSortDTO> groups = tasksService.groupAndSort(gs, tasks);

        GroupSortResponseDTO result = new GroupSortResponseDTO();
        result.setView(pe.getText());
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
    GroupSortResponseDTO loadTasksByLabel(@RequestParam(value = "id") Long id) {
        LabelEntity le = daoService.findLabelById(id);
        GroupSortEntity gs = groupAndSortService.getExistingOrDefault("labels", String.valueOf(id));
        List<TaskEntity> tasks = daoService.loadTaskByLabel(id);
        List<GroupSortDTO> groups = tasksService.groupAndSort(gs, tasks);

        GroupSortResponseDTO result = new GroupSortResponseDTO();
        result.setView(le.getText());
        result.setGroups(groups);
        result.setAllowNewTaskAction(groupAndSortService.allowNewTaskAction(gs));

        return result;
    }

    @RequestMapping(value = {"/getParentTaskProject"}, method = RequestMethod.GET)
    public @ResponseBody
    Map getParentTaskProject(@RequestParam(value = "parent_id") Long parent_id) {
        final TaskEntity result = daoService.findTaskById(parent_id);

        return Utils.mapOf("parent_project_id", result.getProject_id());
    }

    @RequestMapping(value = {"/loadTaskData"}, method = RequestMethod.GET)
    public @ResponseBody
    TaskRequestLoadDTO loadTaskData(@RequestParam(value = "id") Long id) {
        return tasksService.loadTaskData(id);
    }

    @RequestMapping(value = {"/loadInitialTaskData"}, method = RequestMethod.GET)
    public @ResponseBody
    ResponseEntity loadInitialTaskData(
            @RequestParam(value = "calendar_id") Long calendar_id,
            @RequestParam(value = "project_id") Long project_id,
            @RequestParam(value = "parent_id") String parent_id,
            @RequestParam(value = "insert") Boolean insert,
            @RequestParam(value = "labels", required = false) Long[] labels,
            @RequestParam(value = "start", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date start,
            @RequestParam(value = "end", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date end
    ) {
        ProjectEntity findProjectById = daoService.findProjectById(project_id);
        if (findProjectById == null) {
            return new ResponseEntity("Project not found (project_id=" + project_id + ")", HttpStatus.NOT_FOUND);
        }
        CalendarEntity findCalendarById = daoService.findCalendarById(calendar_id);
        if (findCalendarById == null) {
            return new ResponseEntity("Calendar not found (calendar_id=" + calendar_id + ")", HttpStatus.NOT_FOUND);
        }
        TaskRequestLoadDTO t = tasksService.loadInitialTaskData(findCalendarById, findProjectById, parent_id, insert, labels, start, end);

        return ResponseEntity.ok(t);
    }

    @RequestMapping(value = {"/saveTaskData"}, method = RequestMethod.POST)
    public @ResponseBody
    ResponseEntity saveTaskData(@RequestBody TaskRequestSaveDTO dTO) {

        ProjectEntity project = daoService.findProjectById(dTO.getProject_id());
        if (project == null) {
            return new ResponseEntity("Project not found (project_id=" + dTO.getProject_id() + ")", HttpStatus.NOT_FOUND);
        }
        CalendarEntity calendar = daoService.findCalendarById(dTO.getCalendar_id());
        if (calendar == null) {
            return new ResponseEntity("Calendar not found (calendar_id=" + dTO.getCalendar_id() + ")", HttpStatus.NOT_FOUND);
        }

        if (dTO.getRepeatData() != null) {
            TaskRepeatDataDTO repeatData = dTO.getRepeatData();
            if (StringUtils.isBlank(repeatData.getMode())) {
                return new ResponseEntity("'mode' is null", HttpStatus.BAD_REQUEST);
            }
            if (repeatData.getMode_start() == null) {
                return new ResponseEntity("'mode_start' is null", HttpStatus.BAD_REQUEST);
            }
            if (repeatData.getEndson_until() != null) {
                if (repeatData.getMode_start().after(repeatData.getEndson_until())) {
                    return new ResponseEntity("'mode_start' is after 'endson_until'", HttpStatus.BAD_REQUEST);
                }
            }
            if (repeatData.getMode_end() != null) {
                if (repeatData.getMode_start().after(repeatData.getMode_end())) {
                    return new ResponseEntity("'mode_start' is after 'mode_end'", HttpStatus.BAD_REQUEST);
                }
            }
        } else {
            if (dTO.getAllDay() != null) {
                if (dTO.getAllDay() == false) {
                    if (StringUtils.isBlank(dTO.getStart())) {
                        return new ResponseEntity("allDay is false ; start date/time is null", HttpStatus.BAD_REQUEST);
                    }
                }
            }
            if (StringUtils.isNotBlank(dTO.getEnd())) {
                if (StringUtils.isBlank(dTO.getStart())) {
                    return new ResponseEntity("please specify start date", HttpStatus.BAD_REQUEST);
                }
                else {
                    Date st = Utils.parse(dTO.getStart());
                    Date en = Utils.parse(dTO.getEnd());
                    if (st.after(en)) {
                        return new ResponseEntity("'start' is after 'end'", HttpStatus.BAD_REQUEST);
                    }
                }
            }
        }

        tasksService.saveTaskData(dTO, project, calendar);

        return ResponseEntity.ok(dTO);
    }

    @RequestMapping(value = {"/loadTaskRepeatDataCurrentEvent"}, method = RequestMethod.GET)
    public @ResponseBody
    Map loadTaskRepeatDataCurrentEvent(
            @RequestParam(value = "id") Long task_id,
            @RequestParam(value = "start") @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss") Date start,
            @RequestParam(value = "end", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss") Date end
    ) {
        TaskRequestLoadDTO t = tasksService.loadTaskRepeatDataCurrentEvent(task_id, start, end);

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
    @RequestMapping(value = "/link/title", method = RequestMethod.POST)
    public @ResponseBody
    Map getTitleFromUrlLink(@RequestBody Map<String, Object> body) {
        String url = body.get("url").toString();

        Document doc;
        String title;
        try {
            doc = Jsoup.connect(url).get();
            title = doc.title();
        } catch (IOException ex) {
            log.error(ex.getMessage(), ex);
            title = url;
        }

        return Utils.mapOf("title", title);
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

    @RequestMapping(value = {"/createIndex"}, method = RequestMethod.GET)
    public @ResponseBody
    ResponseEntity createIndex() {
        int r = daoService.createIndex();
        return ResponseEntity.ok(r);
    }

    @RequestMapping(value = {"/loadTasksBySearch"}, method = RequestMethod.GET)
    public @ResponseBody
    GroupSortResponseDTO loadTasksBySearch(@RequestParam(value = "searchTerm") String searchTerm) {
        GroupSortResponseDTO gsDTO = new GroupSortResponseDTO();
        gsDTO.setView("SEARCH");
        gsDTO.setGroups(tasksService.searchAndGroup(searchTerm));
        gsDTO.setAllowNewTaskAction(groupAndSortService.allowNewTaskAction(groupAndSortService.getSearchGroupSort()));

        return gsDTO;
    }
}
