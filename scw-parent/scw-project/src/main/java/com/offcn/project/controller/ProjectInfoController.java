package com.offcn.project.controller;

import com.offcn.dycommon.response.AppResponse;
import com.offcn.project.pojo.*;
import com.offcn.project.service.ProjectInfoService;
import com.offcn.project.vo.resp.ProjectDetailVo;
import com.offcn.project.vo.resp.ProjectVo;
import com.offcn.utils.OssTemplate;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import io.swagger.annotations.ApiOperation;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/project")
public class ProjectInfoController {

    @Autowired
    private OssTemplate ossTemplate;

    @Autowired
    private ProjectInfoService projectInfoService;

    @ApiOperation("上传图片")
    @PostMapping("/uploadFile")
    public AppResponse uploadFile(@RequestParam(name = "file") MultipartFile[] multipartFiles) {

        Map map = new HashMap();
        List<String> urls = new ArrayList<>();
        if (null != multipartFiles && multipartFiles.length > 0) {
            //1.遍历上传文件流集合
            for (MultipartFile file : multipartFiles) {
                //2.得到上传文件的对象
                //3.得到上传文件的全名称
                String fileName = file.getOriginalFilename();
                //4.调用上传文件模板完成上传
                try {
                    String url = ossTemplate.upload(file.getInputStream(), fileName);
                    urls.add(url);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }
        map.put("urls", urls);
        return AppResponse.ok(map);
    }


    @ApiOperation("获取项目回报列表")
    @GetMapping("/details/returns/{projectId}")
    public AppResponse<List<TReturn>> getReturnList(@PathVariable("projectId") Integer projectId) {
        List<TReturn> returns = projectInfoService.getReturnList(projectId);
        return AppResponse.ok(returns);
    }

    @ApiOperation("获取系统所有的项目")
    @GetMapping("/all")
    public AppResponse<List<ProjectVo>> findAllProject() {
        // 1、创建集合存储全部项目的VO
        ArrayList<ProjectVo> prosVo = new ArrayList<>();
        //2.查询全部项目
        List<TProject> pros = projectInfoService.findAllProject();
        //3.遍历项目集合
        for (TProject tProject : pros) {
            //获取项目编号
            Integer id = tProject.getId();
            //根据项目编号获取项目配图
            List<TProjectImages> images = projectInfoService.getProjectImages(id);
            ProjectVo projectVo = new ProjectVo();
            BeanUtils.copyProperties(tProject, projectVo);
            //遍历项目配图集合
            for (TProjectImages tProjectImages : images) {
                //如果图片类型是头部图片，则设置头部图片路径到项目VO
                if (tProjectImages.getImgtype() == 0) {
                    projectVo.setHeaderImage(tProjectImages.getImgurl());
                }
            }
            //把项目vo添加到项目vo集合
            prosVo.add(projectVo);
        }
        return AppResponse.ok(prosVo);
    }

    @ApiOperation("获取项目信息详情")
    @GetMapping("/findProjectInfo/{projectId}")
    public AppResponse<ProjectDetailVo> findProjectInfo(@PathVariable("projectId") Integer projectId) {
        TProject projectInfo = projectInfoService.findProjectInfo(projectId);
        ProjectDetailVo projectVo = new ProjectDetailVo();
        // 1、查出这个项目的所有图片
        List<TProjectImages> projectImages = projectInfoService.getProjectImages(projectInfo.getId());
        List<String> detailsImage = projectVo.getDetailsImage();
        if (detailsImage == null) {
            detailsImage = new ArrayList<>();
        }
        for (TProjectImages tProjectImages : projectImages) {
            if (tProjectImages.getImgtype() == 0) {
                projectVo.setHeaderImage(tProjectImages.getImgurl());
            } else {
                detailsImage.add(tProjectImages.getImgurl());
            }
        }
        projectVo.setDetailsImage(detailsImage);

        // 2、项目的所有支持回报；
        List<TReturn> returns = projectInfoService.getReturnList(projectInfo.getId());
        projectVo.setProjectReturns(returns);
        BeanUtils.copyProperties(projectInfo, projectVo);
        return AppResponse.ok(projectVo);
    }

    @ApiOperation("获取系统所有的项目标签")
    @GetMapping("/findAllTag")
    public AppResponse<List<TTag>> findAllTag() {
        List<TTag> tags = projectInfoService.findAllTag();
        return AppResponse.ok(tags);
    }

    @ApiOperation("获取系统所有的项目分类")
    @GetMapping("/findAllType")
    public AppResponse<List<TType>> findAllType() {
        List<TType> types = projectInfoService.findAllType();
        return AppResponse.ok(types);
    }


    @ApiOperation("获取回报信息")
    @GetMapping("/returns/info/{returnId}")
    public AppResponse<TReturn> findReturnInfo(@PathVariable("returnId") Integer returnId){
        TReturn tReturn = projectInfoService. findReturnInfo(returnId);
        return AppResponse.ok(tReturn);
    }
}