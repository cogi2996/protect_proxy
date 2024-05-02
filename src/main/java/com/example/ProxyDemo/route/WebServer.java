package com.example.ProxyDemo.route;

import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;

import java.io.IOException;

public interface WebServer {
    void makeRequest(ServletRequest request, ServletResponse response) throws IOException;
}
