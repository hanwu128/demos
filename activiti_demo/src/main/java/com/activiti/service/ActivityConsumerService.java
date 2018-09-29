package com.activiti.service;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
public interface ActivityConsumerService {

    @RequestMapping(value = "/activitiDemo", method = RequestMethod.GET)
    public boolean startActivityDemo();
}
