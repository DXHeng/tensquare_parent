package com.base.service;

import com.base.controller.AppPushUtil;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * @author: polarbear
 * @Date: 2019/5/15 15:49
 * @Description: tensquare_parent
 */
@Service
public class AppPushService {

    public Map pushMessageToApp() {
       return AppPushUtil.pushMessageToApp("系统通知", "哈哈哈哈", "嘻嘻嘻嘻");
    }
}
