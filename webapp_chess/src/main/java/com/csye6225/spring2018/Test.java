package com.csye6225.spring2018;

public class Test {
    private String name;
    private String password;

    public Test(String name, String password) {
        this.name = name;
        this.password = password;
        System.out.println(name);
        System.out.println(password);
    }
}
