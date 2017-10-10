/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.webschedule.design.services;

import com.webschedule.design.datastructure.GroupSortEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author asd
 */
@Service
public class GroupAndSortService {
    
    @Autowired
    private DaoService daoService;
    
    
    public void saveOrUpdate(String view, String id, String group, String sort) {
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
    
    public GroupSortEntity getExistingOrDefault(String view, String id) {
        GroupSortEntity selectedGS = daoService.findGSBySelectedViewAndId(view, id);
        if (selectedGS == null) {
            selectedGS = new GroupSortEntity();
            selectedGS.setSelectedView(view);
            selectedGS.setSelectedId(id);
            selectedGS.setGroup_("dont-group");
            selectedGS.setSort_("sort-sdate-up");
        }
        return selectedGS;
    }
    
    
    
}
