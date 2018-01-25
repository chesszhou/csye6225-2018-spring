package com.csye6225.spring2018.controller;

import com.csye6225.spring2018.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.io.*;
import java.util.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
public class IndexController {

  private final static Logger logger = LoggerFactory.getLogger(IndexController.class);

  @RequestMapping("/")
  public String index() {
    logger.info("Loading home page.");
    return "index";
  }



  @RequestMapping("/signUp")
  public String signup(HttpServletRequest request){
    logger.info("Loading signUp page.");
    String email = request.getParameter("username");
    String password = request.getParameter("password");
    Test t = new Test(email,password);

    return "signup";

  }

}
