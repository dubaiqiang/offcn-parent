package com.offcn.order.service.impl;

import com.offcn.dycommon.enums.OrderStatusEnumes;
import com.offcn.dycommon.response.AppResponse;
import com.offcn.order.mapper.TOrderMapper;
import com.offcn.order.pojo.TOrder;
import com.offcn.order.service.OrderService;
import com.offcn.order.service.ProjectInfoServiceFeign;
import com.offcn.order.vo.req.OrderInfoSubmitVo;
import com.offcn.order.vo.resp.TReturn;
import com.offcn.utils.AppDateUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.UUID;


@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private ProjectInfoServiceFeign  projectInfoServiceFeign;

    @Autowired
    private TOrderMapper tOrderMapper;



    //保存订单方法
    @Override
    public TOrder saveOder(OrderInfoSubmitVo vo) {

        //1.通过登录令牌获得会员id
        String memberId = stringRedisTemplate.opsForValue().get(vo.getAccessToken());
        if (memberId == null) {
            throw new RuntimeException("登录失败，无此操作");
        }
        //2.向订单对象设置属性
        TOrder tOrder = new TOrder();
        BeanUtils.copyProperties(vo, tOrder);
        tOrder.setMemberid(Integer.parseInt(memberId));         //会员ID
        tOrder.setCreatedate(AppDateUtils.getFormatTime());      //创建时间
        String orderNum = UUID.randomUUID().toString().replace("-", "");
        tOrder.setOrdernum(orderNum);                       //订单编号
        tOrder.setStatus(OrderStatusEnumes.UNPAY.getCode() + "");             //支付状态 未支付
        tOrder.setInvoice(vo.getInvoice() + "");               //发票类型
        //3.调用查询回报增量列表，得到回报增量
        AppResponse<List<TReturn>> response = projectInfoServiceFeign.getReturnList(vo.getProjectid());
        List<TReturn> returnList = response.getData();
        Integer money = 0;
        if (!CollectionUtils.isEmpty(returnList)) {
            //默认取得回报增量集合中的第一条
            TReturn tReturn = returnList.get(0);
            //4.计算支付金额     回报单价*回报数量+运费
            money = tReturn.getSupportmoney() * vo.getRtncount() + tReturn.getFreight();
            tOrder.setMoney(money);
        }
        //5.执行保存
        tOrderMapper.insertSelective(tOrder);
        return tOrder;
    }
}
