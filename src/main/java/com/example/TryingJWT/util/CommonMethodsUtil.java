package com.example.TryingJWT.util;

import com.fasterxml.jackson.databind.ObjectMapper;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

public class CommonMethodsUtil {

    public static void errorResponse(HttpServletResponse response, Exception exc) throws IOException {
        response.setHeader("error", exc.getMessage());
        response.setStatus(FORBIDDEN.value());
        response.setContentType(APPLICATION_JSON_VALUE);

        Map<String, String> error = new HashMap<>();
        error.put("error_message", exc.getMessage());
        new ObjectMapper().writeValue(response.getOutputStream() , error);
    }

    public static void tokensJSONResponse(HttpServletResponse response, Map<String, String> tokens) throws IOException {
        response.setContentType(APPLICATION_JSON_VALUE);
        new ObjectMapper().writeValue(response.getOutputStream(), tokens);
    }
}
