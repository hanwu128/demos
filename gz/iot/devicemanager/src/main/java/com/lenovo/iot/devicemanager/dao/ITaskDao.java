package com.lenovo.iot.devicemanager.dao;

import com.lenovo.iot.devicemanager.model.bo.TaskVO;

import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by root on 2017/7/19.
 */
@Repository
public interface ITaskDao {


    //doInsertTasktoNodeBo
    public void doInsertTask(TaskVO _TaskVO);

    //doUpdateTask
    public void doUpdateTask(TaskVO _TaskVO);

    //findAllTask
    public List<TaskVO> findAllTask();

    //findTasklistbyparameters
    public List<TaskVO> findTasklistbyparameters(TaskVO _TaskVO);
    public Integer findTasklistcountbyparameters(TaskVO _TaskVO);

    //doDeleteTask
    public void doDeleteTask(TaskVO _TaskVO);

    public void doUpdateTaskByAppname(TaskVO _TaskVO);


    //doDeleteApp
    public void doDeleteApp(TaskVO _TaskVO);

}
