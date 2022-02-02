package com.apirestsample.app.services;

import com.apirestsample.app.utils.Helpers;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.Properties;

@Service
public class AccessControlService {

    private static final Properties props = Helpers.extractProps();
    private static final String basicAuth = props.getProperty("application.basic-authorization");

    public static Boolean authorization(HttpServletRequest headers) {
        String authorization = headers.getHeader("Authorization");

        if (authorization == null) {
            return false;
        }

        return authorization.equals(basicAuth);
    }

}
