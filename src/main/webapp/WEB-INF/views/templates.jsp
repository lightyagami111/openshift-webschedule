<%-- 
    Document   : templates
    Created on : Oct 9, 2017, 11:01:16 PM
    Author     : asd
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<div style="display : none;">
    

    <div id="link_item_template">
        <li class="list-group-item">
            <table style="width : 100%">
                <tr>
                    <td>
                        <a href="" target="_blank"></a>
                    </td>
                    <td style="width : 20%">
                        <span class="glyphicon glyphicon-remove fr"></span>
                        <a class="glyphicon glyphicon-edit fr" data-toggle="modal" data-target="#editLink_Modal"></a>
                    </td>
                </tr>
            </table>
        </li>
    </div>



    <div id="label_item_menu_template_0">
        <li>
            <a href="#"><div><strong>&nbsp;</strong></div></a>
        </li>        
    </div>
    <div id="label_item_menu_template_1">
        <li class="divider"></li>
        <li>
            <a href="#"><div><strong>&nbsp;</strong></div></a>
        </li>        
    </div>

    
    <div id="calendar_item_menu_template_0">
        <li>
            <a href="#"><input type="checkbox"/>&nbsp;<strong></strong></a>
        </li>        
    </div>
    <div id="calendar_item_menu_template_1">
        <li class="divider"></li>
        <li>
            <a href="#"><input type="checkbox"/>&nbsp;<strong></strong></a>
        </li>        
    </div>



    <div id="tasks_list_template">
        <div class="panel panel-default">
            <div class="panel-heading">
                <span class="selected_view"></span>
                <span class="selected_grouping"></span>
                <span class="selected_grouping_id"></span>
                <span class="selected_sorting"></span>                
                <span class="newTaskButtonsPosition">
                    <a href="#" class="btn btn-default" role="button" action="newTaskToRoot"><span class="glyphicon glyphicon-plus">R</span></a>
                    <a href="#" class="btn btn-default" role="button" action="newSubTask"><span class="glyphicon glyphicon-plus">S</span></a>
                    <a href="#" class="btn btn-default" role="button" action="newTaskToSameLevel"><span class="glyphicon glyphicon-plus">L</span></a>
                </span>                                      
            </div>
            <div  style="overflow-x: auto; white-space: nowrap;">
                <div class="panel-body" action="taks_list" style="display: inline-block; float: none;">

                </div>
            </div>                                
        </div>
    </div>
    
    <div id="qwe_temp">
        
    </div>


</div>
