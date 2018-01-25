package com.csye6225.spring2018.controller;

import org.springframework.ui.Model;
import com.csye6225.spring2018.entity.*;
import com.csye6225.spring2018.UpdateAccount;
import org.hibernate.sql.Update;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;


@Controller
public class IndexController {

  private final static Logger logger = LoggerFactory.getLogger(IndexController.class);
  private AccountDirectory accountDirectory;

  public IndexController() {
    accountDirectory = new AccountDirectory();
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
    if(!accountDirectory.getAccountDirectory().isEmpty()) {
      for(Account a: accountDirectory.getAccountDirectory()) {
        if(a.getName().equals(email)) {
          return "exist";
        }
      }
    }

    UpdateAccount upa = new UpdateAccount(accountDirectory, email, password);
    upa.addAccount();
    return "success";

  }

  @RequestMapping("/login")
  public String login(HttpServletRequest request, Model model){
    logger.info("Loading login page.");
    String email = request.getParameter("username");
    String password = request.getParameter("password");
    UpdateAccount upa = new UpdateAccount(accountDirectory, email, password);
    if(upa.checkAccount()){
      model.addAttribute("time", new Date());
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
