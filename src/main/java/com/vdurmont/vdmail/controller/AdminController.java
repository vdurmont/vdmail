package com.vdurmont.vdmail.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("admin")
public class AdminController {
    @RequestMapping(value = "", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, String> getStatus() {
        Map<String, String> map = new HashMap<>();
        map.put("everything", "ok");
        return map;
    }
}
