package com.offcn.user.controller;

import com.offcn.dycommon.response.AppResponse;

import com.offcn.user.component.SmsTemplate;
import com.offcn.user.pojo.TMember;
import com.offcn.user.service.UserService;
import com.offcn.user.vo.req.UserRegisVo;
import com.offcn.user.vo.resq.UserRespVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;
import java.util.concurrent.TimeUnit;


@RestController
@RequestMapping("/user")
@Api(tags = "用户登录")    //在类上加说明
public class UserLoginController {

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    private SmsTemplate smsTemplate;

    @Autowired
    private UserService userService;

    @ApiOperation("获取短信验证码")
    @ApiImplicitParams({@ApiImplicitParam(name = "phone", value = "手机号", required = true)})
    @GetMapping("/sendCode")
    public AppResponse<String> sendCode(String phone) {
        //1.生成随机验证码
        String code = UUID.randomUUID().toString().substring(0, 4);
        //2.验证码暂存到缓存中   5分钟后超时，自动清空缓存
        redisTemplate.opsForValue().set(phone, code,60*5, TimeUnit.SECONDS);
        //3.发送短信验证码
        String smsCode = smsTemplate.sendSms(phone, code);
        if (StringUtils.isEmpty(smsCode)) {
            return AppResponse.fail("获取验证码失败");
        } else {
            return AppResponse.ok(smsCode);
        }

    }


    @ApiOperation("用户注册")
    @PostMapping("/regist")
    public AppResponse<Object> regist(UserRegisVo regisVo){
        //1.校验验证码
        String code = redisTemplate.opsForValue().get(regisVo.getLoginacct());
        //判断是否和页面输入的验证码相等
        if (StringUtils.isNotEmpty(code)){
            //判断是否相等并忽略大小写 equalsIgnoreCase
            if (code.equalsIgnoreCase(regisVo.getCode())){
                //2.赋值属性
                TMember tMember = new TMember();
                //参数一：数据源  参数二：目标数据
                BeanUtils.copyProperties(regisVo,tMember);
                //3.执行用户注册
                userService.registerUser(tMember);
                //4.清空缓存
                redisTemplate.delete(regisVo.getLoginacct());

                return AppResponse.ok("注册成功");
            }else {
                return AppResponse.fail("验证码错误");
            }
        }else {
            return AppResponse.fail("验证码不存在");
        }
    }


    @ApiOperation("用户登录")
    @PostMapping("/login")
    public AppResponse<UserRespVo> login(String username, String password) {
        //1.判断用户登录是否成功
        TMember member = userService.login(username, password);
        if (member == null){
            //登录失败
            AppResponse<UserRespVo> fail = AppResponse.fail(null);
            fail.setMsg("用户名密码错误");
            return fail;
        }
        //2.随机生成一个访问令牌
        String token = UUID.randomUUID().toString().replace("-", "");
        //3.将令牌和用户信息存入缓存
        redisTemplate.opsForValue().set(token,member.getId()+ "", 2, TimeUnit.HOURS);
        //4.复制属性
        UserRespVo vo = new UserRespVo();
        BeanUtils.copyProperties(member,vo);
        vo.setAccessToken(token);
        //5.将用户信息返回
        return AppResponse.ok(vo);
    }

    //根据用户编号获取用户信息
    @ApiOperation("根据主键id查询信息")
    @GetMapping("/findUser/{id}")
    public AppResponse<UserRespVo> findUser(@PathVariable("id") Integer id){
        TMember tmember = userService.findTmemberById(id);
        UserRespVo userRespVo = new UserRespVo();
        BeanUtils.copyProperties(tmember,userRespVo);
        return AppResponse.ok(userRespVo);
    }

}

