package com.springboot.websocket.controller;


import com.springboot.websocket.util.WebSocketServer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;

/**
 * @Author:tangh
 * @since 1.0
 */
@Controller
@Slf4j
public class WebsocketController {

    //作为服务器向前端推送数据的接口,请求该接口,服务器调用sendInfo()方法发送消息到前端页面。
    @RequestMapping("/websocket/send/{cid}/{message}")
    public String pushToWeb(@PathVariable String cid, @PathVariable String message) {
        log.info(message);
        System.out.println("成功");
        try {
            WebSocketServer.sendInfo(message, cid);
            log.info(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "success.html";
    }
}

