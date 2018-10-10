package com.lenovo.iot.devicemanager.service;

import com.lenovo.iot.devicemanager.model.bo.TaskVO;

import java.util.List;

/**
 * Created by root on 2017/8/21.
 */
public interface ITaskService {

    public TaskVO doInsertTask(TaskVO _TaskVO);

    public void doUpdateTask(TaskVO _TaskVO);


    //findAllTask
    public List<TaskVO> findAllTask();

    public List<TaskVO> findTasklistbyparameters(TaskVO _TaskVO);
    public Integer findTasklistcountbyparameters(TaskVO _TaskVO);


    public void doDeleteTaskAndApp(TaskVO _TaskVO);


    public void doUpdateTaskByAppname(TaskVO _TaskVO);

    public boolean run_app(String device_id, String app_name, boolean status);
}
