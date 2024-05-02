package com.example.ProxyDemo.Controller;

import com.example.ProxyDemo.route.ProxyRealWebServer;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.io.IOException;

@Controller
public class testProxyController {
    private ProxyRealWebServer proxyRealWebServer;
    @PostConstruct
    public void init() {
        proxyRealWebServer = new ProxyRealWebServer();
    }
    @GetMapping("/test")
    public void test(ServletRequest request, ServletResponse response) throws IOException {
        proxyRealWebServer.makeRequest(request, response);
    }
}
