package com.apirestsample.app.utils;

import net.minidev.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public abstract class ResponseHandler {

    protected final JSONObject jsonResponse;

    @Autowired
    protected ResponseHandler() {
        this.jsonResponse = new JSONObject();
    }

    protected JSONObject setResponseError(String message) {
        jsonResponse.appendField("status", "error");
        jsonResponse.appendField("message", message);

        return jsonResponse;
    }

    protected ResponseEntity<?> retrieveInternalServerError() {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                setResponseError("Occurs an error not expected in the server")
        );
    }

    protected JSONObject setAnyResponse(String message) {
        jsonResponse.appendField("message", message);
        return jsonResponse;
    }

    protected ResponseEntity<?> retrieveAnyResponse(HttpStatus status, String message) {
        return ResponseEntity.status(status).body(setAnyResponse(message));
    }

    protected JSONObject setResponseSuccess(String message, Object data) {
        jsonResponse.appendField("status", "success");
        jsonResponse.appendField("message", message);
        jsonResponse.appendField("data", data);

        return jsonResponse;
    }

    protected ResponseEntity<?> retrieveSuccessResponse(HttpStatus status, String message, Object data) {
        return ResponseEntity.status(status).body(setResponseSuccess(message, data));
    }

}
