package com.springapp.mvc.Interceptors;

import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.DatatypeConverter;
import java.io.UnsupportedEncodingException;

public class TweetInterceptor implements HandlerInterceptor{

    boolean checkCredentials(String authorizationStr,HttpServletRequest request) throws UnsupportedEncodingException {
        System.out.println("checkCred Called");
        byte[] authbytes= DatatypeConverter.parseBase64Binary(authorizationStr) ;
        String decodedCredentials=new String(authbytes,"UTF-8");

        String splittedCredentials[]=decodedCredentials.split(":");
        if(splittedCredentials.length!=2)
            return false;
        String email=splittedCredentials[0];
        if(email!=null)
        {
            System.out.println("email is "+email);
            request.setAttribute("currentEmail",email);
            return true;
        }
        return false;

    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object o) throws Exception {
        System.out.println("Prehandle");
        String authorizationStr=request.getHeader("Authorization");
        if(authorizationStr==null|| !authorizationStr.startsWith("Basic"))
        {
            System.out.println("Null or not basic");
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            return false;
        }
        if(checkCredentials(authorizationStr.substring(6),request))
            return  true;
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        return false;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object o, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object o, Exception e) throws Exception {

    }
}
