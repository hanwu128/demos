package com.security.spring_security.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Controller
public class HelloController {

    /*@RequestMapping(value = "/login", method = RequestMethod.GET)
    public String login(ModelMap model) {

        return "login";

    }*/

    @RequestMapping("/logout")
    public String logout(HttpServletRequest request) {
        HttpSession session = request.getSession();
        session.removeAttribute("user");
        return "redirect:/";

    }

}
