package com.springboot.websocket.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Author:tangh 这是服务器端处理向前端发送消息和接收前端消息的类。同样也是通过WebSocket对象以及该对象下封装的方法。所以前端和后台所用的方法是类似的
 * @since 1.0
 */
//该注解是基于WS协议的，用来供前端进行访问,下面这个路径就是前端发送消息到服务器的请求路径
@ServerEndpoint("/websocket/{sid}")
@Component
@Slf4j
public class WebSocketServer {
    //静态变量，用来记录当前在线连接数。应该把它设计成线程安全的。
    private static int onlineCount = 0;
    //concurrent包的线程安全Map，sid作为key，用来存放每个客户端对应的WebSocketServer对象。
    private static ConcurrentHashMap<String, WebSocketServer> webSocketMap = new ConcurrentHashMap<>();

    //与某个客户端的连接会话，需要通过它来给客户端发送数据
    private Session session;

    //接收sid，该sid 是前端向后台发送消息的窗口的编号
    private String sid = "";


    /**
     * 连接建立成功调用的方法
     */
    @OnOpen
    public void onOpen(Session session, @PathParam("sid") String sid) {
        this.session = session;
        webSocketMap.put(sid, this);     //加入set中,将sid作为对象
        addOnlineCount();               //在线数加1
        log.info("有新窗口开始监听:" + sid + ",当前在线人数为" + getOnlineCount());
        this.sid = sid;
        try {
            sendMessage("连接成功");
        } catch (IOException e) {
            log.error("websocket IO异常");
        }
    }

    /**
     * 连接关闭调用的方法
     */
    @OnClose
    public void onClose() {
        webSocketMap.remove(sid);  //从set中删除
        subOnlineCount();           //在线数减1
        log.info("有一连接关闭！当前在线人数为" + getOnlineCount());
    }
    /**
     * 收到客户端消息后调用的方法
     *
     * @param message 客户端发送过来的消息
     */
    @OnMessage
    public void onMessage(String message, Session session) {
        log.info("收到来自窗口" + sid + "的信息:" + message);
        //遍历收到客户端的所有信息
        for (String sid : webSocketMap.keySet()) {
            try {
                webSocketMap.get(sid).sendMessage(message);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * @param session
     * @param error
     */
    @OnError
    public void onError(Session session, Throwable error) {
        log.error("发生错误");
        error.printStackTrace();
    }
    /**
     * 真正实现服务器主动推送消息的方法
     */
    public void sendMessage(String message) throws IOException {
        this.session.getBasicRemote().sendText(message);
    }
    /**
     * 该方法做服务器端发送消息情况判断，判断是群发还是私发
     */
    public static void sendInfo(String message, @PathParam("sid") String sid) throws IOException {
        //当指定sid为null 时，则群发消息
        if ("null".equals(sid)) {
            webSocketMap.keySet().forEach(key -> {
                try {
                    webSocketMap.get(key).sendMessage(message);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
            log.info("群发消息内容:" + message);
        } else {
            //否则向指定的窗口sid发送消息
            webSocketMap.get(sid).sendMessage(message);
            log.info("推送消息到窗口" + sid + "，推送内容:" + message);
        }
    }

    public static synchronized int getOnlineCount() {
        return onlineCount;
    }

    public static synchronized void addOnlineCount() {
        WebSocketServer.onlineCount++;
    }

    public static synchronized void subOnlineCount() {
        WebSocketServer.onlineCount--;
    }
}
