package com.tensquare.base.controller;

import com.tensquare.javabean.Person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author: polarbear
 * @Date: 2019/4/25 14:14
 * @Description: tensquare_parent
 */
@EnableConfigurationProperties({Person.class})
@RequestMapping("/user")
@RestController
public class PersonController {


    @Autowired
    Person person;

    @RequestMapping(value = "getPerson")
    public String getPerson(){
        return person.toString();
    }

}


