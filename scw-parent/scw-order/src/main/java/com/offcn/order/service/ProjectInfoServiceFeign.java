package com.offcn.order.service;

import com.offcn.dycommon.response.AppResponse;
import com.offcn.order.service.impl.ProjectInfoServiceFeignException;
import com.offcn.order.vo.resp.TReturn;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(value = "SCWPROJECT",fallback = ProjectInfoServiceFeignException.class)
public interface ProjectInfoServiceFeign {
    @GetMapping("/details/returns/{projectId}")
    public AppResponse<List<TReturn>> getReturnList(@PathVariable("projectId") Integer projectId);

}
