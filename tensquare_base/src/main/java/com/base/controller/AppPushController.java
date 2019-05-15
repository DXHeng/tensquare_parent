package com.base.controller;

import com.base.service.AppPushService;
import com.commom.entity.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * @author: polarbear
 * @Date: 2019/5/15 15:15
 * @Description: tensquare_parent
 */
@RestController
@RequestMapping("/push")
public class AppPushController {


    @Autowired
    private AppPushService appPushService;

    /**
     * 1. 对单个用户推送消息
     * 2. 对指定列表用户推送消息
     * 3. 对指定应用群推消息
     * 插入消息推送记录
     */
    @PostMapping("/pushMessageToApp")
    public Result pushMessageToApp(){
        Map map = appPushService.pushMessageToApp();
        return new Result(true, HttpStatus.OK.value(),"推送到所有app用户",map);
    }


//    @PostMapping
//    public Result pushMessageToApp(){
//
//        return null;
//    }


}
