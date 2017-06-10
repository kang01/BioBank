package org.fwoxford.security;

import org.fwoxford.config.listeners.SessionEventListener;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.HashMap;

/**
 * Created by zhuyu on 2017-06-07.
 */

public class SingleSessionCheckFilter extends GenericFilterBean {


    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {


        HttpServletRequest req = (HttpServletRequest) request;
        HttpSession httpSession = req.getSession(false);
        String requestUrl = req.getServletPath();
        if (requestUrl.startsWith("/api/")){
            HashMap<String, String> userSessions = SessionEventListener.getUserSessions();

            if (httpSession != null && userSessions != null){
                Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
                if (authentication != null && !authentication.getName().equals("anonymous")){
                    String sessionId = userSessions.get(authentication.getName());
                    if (requestUrl.contains("logout")){
                        SessionEventListener.removeSession(sessionId);
                    } else if (sessionId == null){
                        userSessions.put(authentication.getName(), httpSession.getId());
                    } else if (!sessionId.equals(httpSession.getId())){
                        SecurityContextHolder.getContext().setAuthentication(null);
                        throw new AccessDeniedException(authentication.getName() + " 已经在另一个终端或浏览器登录。");
                    }
                }
            }
        }



        chain.doFilter(request, response);
        return;
    }

}
