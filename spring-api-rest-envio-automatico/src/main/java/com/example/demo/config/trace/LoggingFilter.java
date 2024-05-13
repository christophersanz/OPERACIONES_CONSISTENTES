package com.example.demo.config.trace;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Enumeration;
import java.util.concurrent.atomic.AtomicLong;

@Component
@Log4j2
public class LoggingFilter extends OncePerRequestFilter {

    public static final String SPACE = " " ;
    public static final String EMPTY = "";
    public static final String NEXT_LINE = "\n";
    public static final String APPLICATION_JSON_UTF8 = "application/json;charset=utf8";
    public static final String ENCODING_UTF8 = "UTF-8";

    private static final String REQUEST_LAYER = "Service Request";
    private static final String RESPONSE_LAYER = "Service Response";
    private static final String URI_LAYER = "URI           : ";
    private static final String METHOD_LAYER = "Method        : ";
    private static final String HEADERS_LAYER = "Headers       : ";
    private static final String STATUS_CODE_LAYER = "Status code   : ";
    private static final String STATUS_TEXT_LAYER = "Status text   : ";
    private static final String REQUEST_BODY_LAYER = "Request body  : ";
    private static final String RESPONSE_BODY_LAYER = "Response body : ";
    private static final String SEPARATOR = "---------------";

    private static final String REQUEST_PREFIX = "Request: ";
    private static final String RESPONSE_PREFIX = "Response: ";
    private static final String REQUEST_ID = "request id=";
    private static final String AUTHORIZATION = "authorization";
    private AtomicLong id = new AtomicLong(0);

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        long requestId = id.incrementAndGet();
        request = new RequestWrapper(requestId, request);
        response = new ResponseWrapper(requestId, response);

        request.setCharacterEncoding( ENCODING_UTF8 );
        response.setCharacterEncoding( ENCODING_UTF8 );

        try {
            filterChain.doFilter(request, response);
        } finally {
            if (!request.getRequestURI().contains("swagger")) { // not swagger log
                logRequest (request, (RequestWrapper)request);
                logResponse(request, (ResponseWrapper)response);
            }
        }
    }

    private void logRequest(final HttpServletRequest request, final RequestWrapper requestWrapper) {
        StringBuilder logBuilder = new StringBuilder();
        logBuilder.append(REQUEST_PREFIX);
        logBuilder.append(REQUEST_ID).append(((RequestWrapper)request).getId()).append("; ").append(SPACE);
        logBuilder.append(REQUEST_LAYER).append(NEXT_LINE);
        logBuilder.append(URI_LAYER).append(request.getRequestURL()).append(NEXT_LINE);
        logBuilder.append(METHOD_LAYER).append(request.getMethod()).append(NEXT_LINE);
        logBuilder.append(HEADERS_LAYER).append(new ServletServerHttpRequest(request).getHeaders()).append(NEXT_LINE);
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            Object requestObject = objectMapper.readValue(requestWrapper.toByteArray(), Object.class);
            String requestBody = objectMapper.writeValueAsString(requestObject);
            logBuilder.append(REQUEST_BODY_LAYER).append(formatJsonString(requestBody)).append(NEXT_LINE); // Formatear JSON
        } catch (IOException e) {
            log.warn("Error al leer o escribir el cuerpo de la solicitud JSON", e);
        }
        logBuilder.append(SEPARATOR);
        request.setAttribute("startTime", System.currentTimeMillis());
        log.info(logBuilder);
    }

    private void logResponse(final HttpServletRequest request, final ResponseWrapper responseWrapper) {
        StringBuilder logBuilder = new StringBuilder();
        logBuilder.append(RESPONSE_PREFIX);
        long startTime = (Long) request.getAttribute("startTime");
        long endTime = System.currentTimeMillis() - startTime;
        logBuilder.append(REQUEST_ID).append(((RequestWrapper)request).getId()).append("; ").append("Time Taken=" + endTime).append(SPACE);
        logBuilder.append(RESPONSE_LAYER).append(NEXT_LINE);
        logBuilder.append(STATUS_CODE_LAYER).append(responseWrapper.getStatus()).append(SPACE).append(HttpStatus.valueOf(responseWrapper.getStatus()).getReasonPhrase()).append(NEXT_LINE);
        logBuilder.append(STATUS_TEXT_LAYER).append(HttpStatus.valueOf(responseWrapper.getStatus()).getReasonPhrase()).append(NEXT_LINE);
        StringBuilder headers = new StringBuilder();
        Enumeration<String> headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String key = (String) headerNames.nextElement();
            String value = request.getHeader(key);
            if(!AUTHORIZATION.equals(key)) {
                headers.append(key).append(":").append('"').append(value).append('"').append(", ");
            }
        }
        headers.delete(headers.length()-2, headers.length());
        logBuilder.append(HEADERS_LAYER).append("[").append(headers).append("]").append(NEXT_LINE);
        ObjectMapper objectMapper = new ObjectMapper();
        byte[] responseBodyBytes = responseWrapper.toByteArray();
        String responseBody = new String(responseBodyBytes);
        if (isJsonString(responseBody)) {
            responseBody = formatJsonString(responseBody);
        }
        logBuilder.append(RESPONSE_BODY_LAYER).append(responseBody).append(NEXT_LINE);
        request.setAttribute("startTime", startTime);
        logBuilder.append(SEPARATOR);
        log.info(logBuilder);
    }

    // Método para verificar si una cadena es JSON
    private boolean isJsonString(String str) {
        try {
            new ObjectMapper().readTree(str);
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    // Método para formatear la cadena JSON para que se muestre en una sola línea
    private String formatJsonString(String jsonString) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            Object jsonObject = objectMapper.readValue(jsonString, Object.class);
            return objectMapper.writeValueAsString(jsonObject);
        } catch (IOException e) {
            log.warn("Error al formatear la cadena JSON", e);
            return jsonString; // Si hay un error, devolver la cadena original
        }
    }

}
