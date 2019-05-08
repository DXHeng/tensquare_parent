package com.tensquare.javabean;

import lombok.Data;
import lombok.NonNull;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author: polarbear
 * @Date: 2019/4/25 13:54
 * @Description: Person.class
 */
@ConfigurationProperties(prefix = "person")
@Component
@Data
@ToString(of = {"name", "gender"}, exclude = {"age", "number"})
public class Person {

    @NonNull
    private String name;

    @NonNull
    private int gender;

    @NonNull
    private int age;

    @NonNull
    private int number;


}



