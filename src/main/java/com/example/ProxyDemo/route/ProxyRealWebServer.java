package com.example.ProxyDemo.route;

import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

public class ProxyRealWebServer implements WebServer{
    private RealWebServer realWebServer;
    private final ConcurrentHashMap<String, RateLimitEntry> requestCounts = new ConcurrentHashMap<>();
    private final long rateLimit = 5; // Maximum requests allowed within the time window
    @Override
    public void makeRequest(ServletRequest request, ServletResponse response) throws IOException {
        if(realWebServer == null){
            realWebServer = new RealWebServer();
        }
        String ipAddress = request.getRemoteAddr();
        RateLimitEntry entry = requestCounts.computeIfAbsent(ipAddress, k -> new RateLimitEntry());
        entry.incrementCount();
        System.out.println(entry.count);
        if (entry.isRequestOverLimit(rateLimit)) {
            HttpServletResponse httpResponse = (HttpServletResponse) response;
            httpResponse.setStatus(HttpServletResponse.SC_SERVICE_UNAVAILABLE);
            httpResponse.getWriter().write("Rate limit exceeded. Please try again later.");
            return;
        }
        realWebServer.makeRequest(request, response);
    }
}

class RateLimitEntry {
    public final AtomicLong count = new AtomicLong();
    private  long lastResetTime = System.currentTimeMillis();
    private final long timeWindow = TimeUnit.SECONDS.toMillis(10); // 10-second time window

    public boolean isRequestOverLimit(long limit) {
        // Check if the time window has elapsed since the last request
        if (isTimeWindowReset()) {
            count.set(0);
            lastResetTime = System.currentTimeMillis();
        }
        return this.getCount() > limit;
    }
    private boolean isTimeWindowReset() {
        return System.currentTimeMillis() - lastResetTime >= timeWindow;
    }
    public void incrementCount() {
        count.incrementAndGet();
    }
    public long getCount(){
        return count.get();
    }
}