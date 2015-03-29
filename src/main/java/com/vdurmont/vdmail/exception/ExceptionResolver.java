package com.vdurmont.vdmail.exception;

import com.vdurmont.vdmail.dto.HttpStatus;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Service
public class ExceptionResolver implements HandlerExceptionResolver {
    private static final Logger LOGGER = Logger.getLogger(ExceptionResolver.class);

    @Override
    public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        ModelAndView mav = new ModelAndView();

        mav.setView(new MappingJackson2JsonView());
        mav.addObject("error", ex.getClass().getSimpleName());
        mav.addObject("message", ex.getMessage());

        HttpStatus status = getStatus(ex);
        response.setHeader("Content-Type", "application/json");
        response.setStatus(status.code);

        print(ex);

        return mav;
    }

    private void print(Exception e) {
        if (e instanceof NoConnectedUserException) {
            LOGGER.error(e.getMessage());
        } else {
            LOGGER.error(e.getMessage(), e);
        }
    }

    private static HttpStatus getStatus(Exception e) {
        if (e instanceof VDMailException) {
            return ((VDMailException) e).getStatus();
        } else {
            return HttpStatus.INTERNAL_SERVER_ERROR;
        }
    }
}
