package com.offcn.order.controller;

import com.offcn.dycommon.response.AppResponse;
import com.offcn.order.pojo.TOrder;
import com.offcn.order.service.OrderService;
import com.offcn.order.vo.req.OrderInfoSubmitVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(tags="保存订单")
@RestController
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    private OrderService orderService;


    /*
    *
    *
    *
    * */
    @PostMapping("/createOrder")
    @ApiOperation("保存订单")
    public AppResponse<TOrder> createOrder(@RequestBody OrderInfoSubmitVo vo) {

        String memberId = redisTemplate.opsForValue().get(vo.getAccessToken());
        if (StringUtils.isEmpty(memberId)) {

            AppResponse fail = AppResponse.fail(null);
            fail.setMsg("无此权限,请先登录");
            return fail;
        }
        try {
            TOrder tOrder = orderService.saveOder(vo);
            return AppResponse.ok(tOrder);
        } catch (Exception e) {
            e.printStackTrace();
            return AppResponse.fail(null);
        }
    }
}

