package com.offcn.project.service;

import com.offcn.project.pojo.*;

import java.util.List;

public interface ProjectInfoService {
    //获取项目回报列表
    public List<TReturn> getReturnList(Integer projectId);

    //获取系统中所有项目
    public List<TProject> findAllProject();

    //获取系统中的所有项目
    public List<TProjectImages> getProjectImages(Integer id);

    //获取项目信息
    public TProject findProjectInfo(Integer projectId);

    //获取项目标签
    public List<TTag> findAllTag();

    //获取项目分类
    public List<TType> findAllType();

    //获取回报信息
    public TReturn findReturnInfo (Integer returnId);
}
