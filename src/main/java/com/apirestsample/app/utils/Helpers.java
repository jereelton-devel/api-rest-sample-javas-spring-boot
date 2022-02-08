package com.apirestsample.app.utils;

import net.minidev.json.JSONObject;
import org.springframework.util.DigestUtils;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Helpers {

    public static String getDataFromQueryString(String queryString, String field) {

        String str = (queryString.split(field+"=")[1]);

        if (str.contains("&")) {
            str = str.split("&")[0];
        }

        return str;
    }

    public static JSONObject queryStringToJson(String input) {

        String[] splitter = input.split("&");
        JSONObject jsonData = new JSONObject();

        for (String split : splitter) {
            String[] splitter2 = split.split("=");
            jsonData.appendField(splitter2[0], splitter2[1]);
        }

        return jsonData;

    }

    public static String jsonToString(JSONObject json) {
        return json.toJSONString();
    }

    public static JSONObject stringToJson(String str) {

        JSONObject jsonData = new JSONObject();
        String strClean = str.replaceAll("([\"{}]+)", "");

        try {
            String[] splitter = strClean.split(",");

            for (String split : splitter) {
                String[] splitter2 = split.split(":");
                jsonData.appendField(splitter2[0], splitter2[1]);
            }
        } catch (Exception e) {
            try {
                String[] splitter = strClean.split(":");
                jsonData.appendField(splitter[0], splitter[1]);
            } catch (Exception er) {
                jsonData.appendField("message", null);
            }
        }

        return jsonData;
    }

    public static Properties extractProps() {
        Properties properties = new Properties();

        try {
            File file = ResourceUtils.getFile("application.properties");
            InputStream in = new FileInputStream(file);
            properties.load(in);
        } catch (IOException ioe) {
            System.out.println(ioe.getMessage());
        }

        return properties;
    }

    public static String md5(String data) {
        return DigestUtils.md5DigestAsHex(data.getBytes());
    }

}
