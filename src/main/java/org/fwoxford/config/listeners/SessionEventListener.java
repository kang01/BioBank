package org.fwoxford.config.listeners;

import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;
import java.util.HashMap;

/**
 * Created by zhuyu on 2017-06-08.
 */
public class SessionEventListener implements HttpSessionListener {

    private final static HashMap<String, String> userSessions = new HashMap<>();

    public static HashMap<String, String> getUserSessions(){
        return userSessions;
    }

    @Override
    public void sessionCreated(HttpSessionEvent se) {
        int timeout = se.getSession().getMaxInactiveInterval();
        // 10分钟后用户Session过期，用户可以在其他机器进行登录。
        se.getSession().setMaxInactiveInterval(600);
    }

    @Override
    public void sessionDestroyed(HttpSessionEvent se) {
        String sessionId = se.getSession().getId();
        removeSession(sessionId);
    }

    public static void removeSession(String sessionId){
        userSessions.forEach((k,v) ->{
            if (v != null && v.equals(sessionId)){
                userSessions.put(k, null);
            }
        });
    }
}
