package com.harry.carrental.harrycarrental.controller.view;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Created by harryzhu on 2022/7/26
 */
@Controller
public class PortalController {

    @GetMapping({"", "/"})
    public String index() {
        return "index";
    }
}
