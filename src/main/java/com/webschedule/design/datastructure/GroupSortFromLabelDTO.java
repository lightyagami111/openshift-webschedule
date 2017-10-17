/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.webschedule.design.datastructure;

import java.util.List;

/**
 *
 * @author asd
 */
public class GroupSortFromLabelDTO {
    
    private List<GroupSortDTO> groups;
    private LabelEntity e;
    private boolean allowNewTaskAction;

    public List<GroupSortDTO> getGroups() {
        return groups;
    }

    public void setGroups(List<GroupSortDTO> groups) {
        this.groups = groups;
    }

    public LabelEntity getE() {
        return e;
    }

    public void setE(LabelEntity e) {
        this.e = e;
    }
    
    public boolean getAllowNewTaskAction() {
        return allowNewTaskAction;
    }

    public void setAllowNewTaskAction(boolean allowNewTaskAction) {
        this.allowNewTaskAction = allowNewTaskAction;
    }
    
}
