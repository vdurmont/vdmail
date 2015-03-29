package com.vdurmont.vdmail.filter;


import com.vdurmont.vdmail.exception.NotFoundException;
import com.vdurmont.vdmail.exception.SessionExpiredException;
import com.vdurmont.vdmail.model.Session;
import com.vdurmont.vdmail.service.SessionService;
import org.apache.log4j.Logger;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collection;

public class TokenFilter implements Filter {
    private static final Logger LOGGER = Logger.getLogger(TokenFilter.class);
    private SessionService sessionService;

    @Override public void init(FilterConfig filterConfig) throws ServletException {
        WebApplicationContext context = WebApplicationContextUtils.getWebApplicationContext(filterConfig.getServletContext());
        this.sessionService = context.getBean(SessionService.class);
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        boolean shouldFilter = true;
        Session session = null;
        String authHeader = req.getHeader("Authorization");
        if (authHeader != null) {
            String token = authHeader.replace("Token ", "").replace("token ", "");
            if (token != null) {
                SecurityContext context = SecurityContextHolder.getContext();
                try {
                    session = this.sessionService.getByToken(token);
                    context.setAuthentication(new VDMailAuthentication(session));
                } catch (SessionExpiredException e) {
                    HttpServletResponse res = (HttpServletResponse) response;
                    res.setStatus(e.getStatus().code);
                    res.setHeader("Content-Type", "application/json");
                    res.getWriter().write("{\"error\": \"" + e.getClass().getSimpleName() + "\", \"message\": \"" + e.getMessage() + "\"}");
                    shouldFilter = false;
                } catch (NotFoundException ignored) {
                }
            }
        }

        if (session != null) {
            LOGGER.trace("Current user is User#" + session.getUser().getId());
        } else {
            LOGGER.trace("Current user is anonymous");
        }

        if (shouldFilter) {
            chain.doFilter(req, response);
        }
    }

    @Override public void destroy() {}

    public class VDMailAuthentication implements Authentication {
        private final Session session;

        public VDMailAuthentication(Session session) {
            this.session = session;
        }

        @Override public Collection<? extends GrantedAuthority> getAuthorities() {
            return null;
        }

        @Override public Object getCredentials() {
            return null;
        }

        @Override public Object getDetails() {
            return null;
        }

        @Override public Object getPrincipal() {
            return this.session.getUser();
        }

        @Override public boolean isAuthenticated() {
            return true;
        }

        @Override public void setAuthenticated(boolean b) throws IllegalArgumentException {}

        @Override public String getName() {
            return null;
        }
    }
}
