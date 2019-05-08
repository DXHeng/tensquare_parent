//package com.settle.zuul.filters;
//
//import com.alibaba.fastjson.JSONObject;
//import com.netflix.zuul.ZuulFilter;
//import com.netflix.zuul.context.RequestContext;
//import com.settle.common.authorization.vo.Token;
//import com.settle.common.utils.ResultUtil;
//import com.settle.common.constant.TokenConstant;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.data.redis.core.RedisTemplate;
//import org.springframework.http.HttpStatus;
//import org.springframework.stereotype.Component;
//import org.springframework.util.AntPathMatcher;
//import org.springframework.util.ObjectUtils;
//import org.springframework.util.StringUtils;
//import javax.servlet.http.HttpServletRequest;
//import java.util.List;
//import static org.springframework.cloud.netflix.zuul.filters.support.FilterConstants.PRE_DECORATION_FILTER_ORDER;
//import static org.springframework.cloud.netflix.zuul.filters.support.FilterConstants.PRE_TYPE;
//
//@Component
//@Slf4j
//public class AuthFilter extends ZuulFilter {
//
//
//    @Autowired
//    RedisTemplate<String, String> redisTemplate;
//
//    @Value("${maintenance.state}")
//    private Boolean isMaintenance;
//
//	private static AntPathMatcher antPathMatcher = new AntPathMatcher();
//
//    //排除过滤的 uri 地址
//    /**
//     * 授权登录接口：/v1/user/wxAuth/login
//     * 微信推送服务器接口：/v1/user/home/index
//     */
//    private static final String[] ignoreUrls = {
//            "/v1/user/wxAuth/login",
//            "/v1/user/home/index",
//            "/v1/user/wxAuth/getAccessToken",
//            "/v1/youzan/api/message",
//            "/v1/merch/**",
//            "/v1/pay/wx/**",
//    };
//
//    @Override
//    public String filterType() {
//        return PRE_TYPE;
//    }
//
//    @Override
//    public int filterOrder() {
//        return PRE_DECORATION_FILTER_ORDER - 1;
//    }
//
//	/**
//	 * 判断是否对当前请求过滤
//	 * modify by zhaohq 2019/03/21 白名单支持通配符
//	 */
//	@Override
//    public boolean shouldFilter() {
//        RequestContext requestContext = RequestContext.getCurrentContext();
//        HttpServletRequest request = requestContext.getRequest();
//        if (StringUtils.startsWithIgnoreCase(request.getRequestURI(), "/v1/youzan/api")) {
//            return false;
//        }
//
//        //与白名单中路径是否匹配（与任意一个匹配）
//        boolean isMatch=false;
//	    for(String item:ignoreUrls){
//	    	if(antPathMatcher.match(item, request.getRequestURI())){
//	    		isMatch=true;
//	    		log.info("[{}]匹配[{}]成功",request.getRequestURI(),item);
//	    		break;
//		    }
//	    }
//
//        //IGNORE_URIS中的接口不拦截，其他接口都要拦截校验 token
//        //return !ArrayUtils.contains(ignoreUrls, request.getRequestURI());
//	    return !isMatch;
//    }
//
//    @Override
//    public Object run() {
//        RequestContext requestContext = RequestContext.getCurrentContext();
//        HttpServletRequest request = requestContext.getRequest();
//
//        //从头部获取token校验
//        String token = request.getHeader(TokenConstant.TOKEN_PARAM_NAME);
//        if (StringUtils.isEmpty(token)) {
//            setUnauthorizedResponse(requestContext);
//        } else {
//            verifyToken(requestContext, token);
//        }
//
//        return null;
//    }
//
//
//    /**
//     * 从Redis中校验token
//     *
//     * @param requestContext 请求上下文
//     * @param token          用户token
//     */
//    private void verifyToken(RequestContext requestContext, String token) {
//        String redisToken = redisTemplate.opsForValue().get(TokenConstant.TOKEN_KEY_PREFIX + token);
//        if (StringUtils.isEmpty(redisToken)) {
//            setUnauthorizedResponse(requestContext);
//        } else {
//            //判断是否是维护模式，如果是，只允许白名单内人访问
//            if (isMaintenance) {
//                //当前用户的id
//                Token tokenObj = JSONObject.toJavaObject(JSONObject.parseObject(redisToken), Token.class);
//                Integer userId = tokenObj.getUserId();
//                //redis中白名单
//                String whiteListStr = redisTemplate.opsForValue().get("maintenanceWhiteList");
//                if (ObjectUtils.isEmpty(whiteListStr)) {
//                    setMaintenanceRefuse(requestContext);
//                    return;
//                }
//                List<Integer> whiteList = JSONObject.parseArray(whiteListStr, Integer.class);
//                if (ObjectUtils.isEmpty(whiteList) || !whiteList.contains(userId)) {
//                    setMaintenanceRefuse(requestContext);
//                }
//            }
//        }
//    }
//
//    /**
//     * 设置 10001 设置维护模式
//     *
//     * @param requestContext 请求上下文
//     */
//    private void setMaintenanceRefuse(RequestContext requestContext) {
//        requestContext.setSendZuulResponse(false);
//        requestContext.setResponseStatusCode(200);
//        String result = JSONObject.
//                toJSONString(new ResultUtil<>().setErrorMsg(10001, "网站正在维护中"));
//        requestContext.setResponseBody(result);
//        requestContext.getResponse().setContentType("application/json;charset=UTF-8");
//    }
//
//    /**
//     * 设置 401 无权限状态
//     *
//     * @param requestContext 请求上下文
//     */
//    private void setUnauthorizedResponse(RequestContext requestContext) {
//        requestContext.setSendZuulResponse(false);
//        requestContext.setResponseStatusCode(HttpStatus.UNAUTHORIZED.value());
//        String result = JSONObject.
//                toJSONString(new ResultUtil<>().setErrorMsg(HttpStatus.UNAUTHORIZED.value(), HttpStatus.UNAUTHORIZED.getReasonPhrase()));
//        requestContext.setResponseBody(result);
//        requestContext.getResponse().setContentType("application/json;charset=UTF-8");
//    }
//}