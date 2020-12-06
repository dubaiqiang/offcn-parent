package com.offcn.project.service.impl;

import com.alibaba.fastjson.JSON;
import com.offcn.dycommon.enums.ProjectStatusEnume;
import com.offcn.project.contants.ProjectConstant;
import com.offcn.project.enums.ProjectImageTypeEnume;
import com.offcn.project.mapper.*;
import com.offcn.project.pojo.*;
import com.offcn.project.service.ProjectCreateService;
import com.offcn.project.vo.rep.ProjectRedisStorageVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import springfox.documentation.spring.web.json.Json;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.logging.SimpleFormatter;


@Service
public class ProjectCreateServiceImpl implements ProjectCreateService {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private TProjectMapper projectMapper;

    @Autowired
    private TProjectImagesMapper projectImagesMapper;

    @Autowired
    private TProjectTagMapper projectTagMapper;

    @Autowired
    private TProjectTypeMapper projectTypeMapper;

    @Autowired
    private TReturnMapper tReturnMapper;


    //同意协议功能的实现
    @Override
    public String initCreateProject(Integer memberId) {
        //1.初始化临时对象
        ProjectRedisStorageVo initVo = new ProjectRedisStorageVo();
        //2.设置用户id临时对象
        initVo.setMemberid(memberId);
        //3.创建一个随机项目令牌
        String token = UUID.randomUUID().toString().replace("-", "");
        //4.将临时项目存到缓存中(在这里要做一下转换)
        String jsonString = JSON.toJSONString(initVo);
        //存redis
        stringRedisTemplate.opsForValue().set(ProjectConstant.TEMP_PROJECT_PREFIX+token,jsonString);
        return token;
    }

    //保存项目信息
    @Override
    public void saveProjectInfo(ProjectStatusEnume auth, ProjectRedisStorageVo projectvo) {
        TProject projectBase = new TProject();
        BeanUtils.copyProperties(projectvo,projectBase);
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        projectBase.setCreatedate(df.format(new Date()));
        projectBase.setStatus(auth.getCode()+"");
        //1.1)、基本的信息插入，获取到刚才保存好的项目的自增id
        projectMapper.insertSelective(projectBase);
        //获取刚才的id
        Integer projectId = projectBase.getId();
        String headerImage = projectvo.getHeaderImage();
        TProjectImages images = new TProjectImages(null,projectId,headerImage, ProjectImageTypeEnume.HEADER.getCode().byteValue());

        //将项目上传的图片保存起来
        projectImagesMapper.insertSelective(images);
        List<String> detailsImage = projectvo.getDetailsImage();
        //保存详情图
        for (String string:detailsImage){
            TProjectImages img = new TProjectImages(null, projectId, string, ProjectImageTypeEnume.DETAILS.getCode().byteValue());
            projectImagesMapper.insertSelective(img);
        }
        //3.保存项目的标签信息
        List<Integer> tagids = projectvo.getTagids();
        for (Integer tagid:tagids){
            TProjectTag tProjectTag = new TProjectTag(null, projectId, tagid);
            projectTagMapper.insertSelective(tProjectTag);
        }
        //4.保存项目的分类信息
        List<Integer> typeids = projectvo.getTypeids();
        for (Integer tid:typeids) {
            TProjectType tProjectType = new TProjectType(null,projectId,tid);
            projectTypeMapper.insertSelective(tProjectType);
        }
        //5.保存回报信息
        List<TReturn> returns = projectvo.getProjectReturns();
        //设置项目的id
        for (TReturn tReturn:returns){
            tReturn.setProjectid(projectId);
            tReturnMapper.insertSelective(tReturn);
        }
        //6.删除临时数据
        stringRedisTemplate.delete(ProjectConstant.TEMP_PROJECT_PREFIX+projectvo.getProjectToken());
    }
}
