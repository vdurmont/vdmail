package com.vdurmont.vdmail.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * This controller serves the static pages
 */
@Controller
@RequestMapping("")
public class IndexController {
    @RequestMapping(value = "", method = RequestMethod.GET)
    public String getStatus() {
        return "/static/index.html";
    }
}
