package com.example.ProxyDemo.route;

import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

public class RealWebServer implements WebServer{
    @Override
    public void makeRequest(ServletRequest request, ServletResponse response) throws IOException {
        String ipAddress = request.getRemoteAddr();
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        httpResponse.setStatus(HttpServletResponse.SC_SERVICE_UNAVAILABLE);
        httpResponse.getWriter().write("hello from : "+ipAddress);

    }
}
