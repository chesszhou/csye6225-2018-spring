package com.csye6225.spring2018.controller;

import dbDriver.Driver;
import org.springframework.ui.Model;
import com.csye6225.spring2018.entity.*;
import com.csye6225.spring2018.UpdateAccount;
import org.hibernate.sql.Update;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.sql.SQLException;
import java.util.Date;


@Controller
public class IndexController {

  private final static Logger logger = LoggerFactory.getLogger(IndexController.class);
  private AccountDirectory accountDirectory;
  private Driver driver;

  public IndexController() {
    accountDirectory = new AccountDirectory();
    this.driver = new Driver();
  }

  @RequestMapping("/")
  public String index() {
    logger.info("Loading home page.");
    return "index";
  }



  @RequestMapping("/signUp")
  public String sign(){
    logger.info("Loading signUp page.");
    return "signup";

  }

  @RequestMapping("/success")
  public String success(HttpServletRequest request){
    logger.info("Loading success page.");
    String email = request.getParameter("username");
    String password = request.getParameter("password");
    boolean flag = false;
    try {
      flag = driver.registerUser(email, password);
    } catch (SQLException e) {
      e.printStackTrace();
    }

   if(flag){
      return "success";
   }
   return "exist";

  }

  @RequestMapping("/login")
  public String login(HttpServletRequest request, Model model) throws SQLException {
    logger.info("Loading login page.");
    String email = request.getParameter("username");
    String password = request.getParameter("password");
    boolean falg = false;
    if(driver.isValidUser(email, password)){
      model.addAttribute("time", new Date());
      model.addAttribute("username", email);

      return "loggedin";
    }
    return "false";

  }

  @RequestMapping("/logout")
  public String logout(){
    logger.info("Loading logout page.");
    return "index";

  }

  /**
  @PostMapping("/loginPost")
  public @ResponseBody
  Map<String, Object> loginPost(String account, String password, HttpSession session) {
    Map<String, Object> map = new HashMap<>();
    if (!"123456".equals(password)) {
      map.put("success", false);
      map.put("message", "Invalid Information!");
      return map;
    }


    session.setAttribute(SESSION_KEY, account);

    map.put("success", true);
    map.put("message", "Success");
    map.put("time", new Date());
    return map;
  }**/



  /**
  @GetMapping("/logout")
  public String logout(HttpSession session) {

    session.removeAttribute(SESSION_KEY);
    return "redirect:/";
  }**/


}
