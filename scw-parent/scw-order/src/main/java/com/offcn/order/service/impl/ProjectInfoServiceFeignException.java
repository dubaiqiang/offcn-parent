package com.offcn.order.service.impl;

import com.offcn.dycommon.response.AppResponse;
import com.offcn.order.service.ProjectInfoServiceFeign;
import com.offcn.order.vo.resp.TReturn;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ProjectInfoServiceFeignException implements ProjectInfoServiceFeign {


    @Override
    public AppResponse<List<TReturn>> getReturnList(Integer projectId) {
        AppResponse<List<TReturn>> fail = AppResponse.fail(null);
        fail.setMsg("调用远程服务器失败{订单}");
        return fail;
    }
}
