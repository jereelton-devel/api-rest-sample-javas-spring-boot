package com.apirestsample.app.services;

import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;

@Service
public class AccessControlService {

    public static Boolean authorization(HttpServletRequest headers) {
        String authorization = headers.getHeader("Authorization");

        if (authorization == null) {
            return false;
        }

        return authorization.equals("");
    }

}
