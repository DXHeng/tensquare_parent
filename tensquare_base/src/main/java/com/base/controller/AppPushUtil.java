package com.base.controller;

import com.gexin.rp.sdk.base.IPushResult;
import com.gexin.rp.sdk.base.impl.AppMessage;
import com.gexin.rp.sdk.base.impl.SingleMessage;
import com.gexin.rp.sdk.base.impl.Target;
import com.gexin.rp.sdk.exceptions.RequestException;
import com.gexin.rp.sdk.http.IGtPush;
import com.gexin.rp.sdk.template.NotificationTemplate;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;

import org.springframework.stereotype.Component;

import java.util.*;

/**
 * @author polarbear
 * @Date 2019/05/15
 */
@Component
@Data
public class AppPushUtil {

    //定义常量, appId、appKey、masterSecret 采用本文档 "第二步 获取访问凭证 "中获得的应用配置
    private static String appId;
    private static String appKey;
    private static String masterSecret;
    private static String url;

    @Value("${getui.appId}")
    public void setAppId(String appId2) {
        appId = appId2;
    }

    @Value("${getui.appKey}")
    public void setAppKey(String appKey2) {
        appKey = appKey2;
    }

    @Value("${getui.masterSecret}")
    public void setMasterSecret(String masterSecret2) {
        masterSecret = masterSecret2;
    }

    @Value("${getui.url}")
    public void setUrl(String url2) {
        url = url2;
    }

    /**
     * 推送到所有app用户
     * @param title 通知栏主题
     * @param content 通知栏内容
     * @param transmissionContent 透传消息
     * @return
     */
    public static Map pushMessageToApp(String title,String content,String transmissionContent){

        try{
            IGtPush push = new IGtPush(url, appKey, masterSecret);
            push.connect();

            // 定义"点击链接打开通知模板"，并设置标题、内容、链接
            NotificationTemplate template=notificationTemplate(title,content,transmissionContent);

            List<String> appIds = new ArrayList<String>();
            appIds.add(appId);

            // 定义"AppMessage"类型消息对象，设置消息内容模板、发送的目标App列表、是否支持离线发送、以及离线消息有效期(单位毫秒)
            AppMessage message = new AppMessage();
            message.setData(template);
            message.setAppIdList(appIds);
            message.setOffline(true);
            //离线过期时间 24h
            message.setOfflineExpireTime(1000 * 60 * 24);

            IPushResult ret = push.pushMessageToApp(message);
            System.out.println(ret.getResponse().toString());
            return ret.getResponse();
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 推送到单个app用户
     * @param title 通知栏主题
     * @param content 通知栏内容
     * @param transmissionContent 透传消息
     * @param clientId 推送客户端id
     * @return
     */
    public static Map pushMessageToSingle(String title,String content,String transmissionContent,String clientId) throws Exception{
        IGtPush push = new IGtPush(url, appKey, masterSecret);
        NotificationTemplate template=notificationTemplate(title,content,transmissionContent);
        SingleMessage message = new SingleMessage();
        message.setOffline(true);
        // 离线有效时间，单位为毫秒，可选
        message.setOfflineExpireTime(24 * 3600 * 1000);
        message.setData(template);
        // 可选，1为wifi，0为不限制网络环境。根据手机处于的网络情况，决定是否下发
        message.setPushNetWorkType(0);
        Target target = new Target();
        target.setAppId(appId);
        target.setClientId(clientId);
        //别名推送
        //target.setAlias(Alias);
        IPushResult ret = null;
        try {
            ret = push.pushMessageToSingle(message, target);
        } catch (RequestException e) {
            e.printStackTrace();
            ret = push.pushMessageToSingle(message, target, e.getRequestId());
        }
        if (ret != null) {
            return ret.getResponse();
        } else {
            return null;
        }
    }

    private static NotificationTemplate notificationTemplate(String title,String content,String transmissionContent) {
        NotificationTemplate template = new NotificationTemplate();
        // 设置APPID与APPKEY
        template.setAppId(appId);
        template.setAppkey(appKey);
        // 设置通知栏标题与内容
        template.setTitle(title);
        template.setText(content);
        // 配置通知栏图标
        template.setLogo("icon.png");
        // 配置通知栏网络图标
        template.setLogoUrl("");
        // 设置通知是否响铃，震动，或者可清除
        template.setIsRing(true);
        template.setIsVibrate(true);
        template.setIsClearable(true);
        // 透传消息设置，1为强制启动应用，客户端接收到消息后就会立即启动应用；2为等待应用启动
        template.setTransmissionType(1);
        template.setTransmissionContent(transmissionContent);
        // 设置定时展示时间
        // template.setDuration("2015-01-16 11:40:00", "2015-01-16 12:24:00");
        return template;
    }
}
