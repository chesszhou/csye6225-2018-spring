package com.csye6225.spring2018.controller;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.iterable.S3Objects;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import com.csye6225.spring2018.S3Configure;
import dbDriver.Driver;
import org.springframework.context.annotation.Profile;
import org.springframework.ui.Model;
import com.csye6225.spring2018.entity.*;
import com.csye6225.spring2018.UpdateAccount;
import org.hibernate.sql.Update;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.sql.SQLException;
import java.util.Date;

@Profile("aws")
@Controller
public class IndexController {

  private final static Logger logger = LoggerFactory.getLogger(IndexController.class);
  private AccountDirectory accountDirectory;
  private Driver driver;
  S3Configure s3Configure = new S3Configure();


  public IndexController() {
    accountDirectory = new AccountDirectory();
//    this.driver = new Driver();
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
    String content = driver.getAboutMe(email);


    if(driver.isValidUser(email, password)){

      AWSCredentials credentials = new BasicAWSCredentials(s3Configure.getAccessKey(), s3Configure.getSecretKey());
      AmazonS3 s3client = new AmazonS3Client(credentials);
      S3Object retrievedPic = null;
      String toRetrieve = "";
      for(S3ObjectSummary summary: S3Objects.inBucket(s3client, s3Configure.getBucketName())){
        String picName = summary.getKey();
        int i = picName.lastIndexOf('.');
        String ownerName = picName.substring(0, i);
        if(ownerName.equals(email)){
          toRetrieve = picName;
          retrievedPic = s3client.getObject(s3Configure.getBucketName(), toRetrieve);
          break;
        }
      }



      if(retrievedPic != null){
        model.addAttribute("picURL",retrievedPic.getObjectContent().getHttpRequest().getURI().toString());
      }else {
        model.addAttribute("picURL", "http://via.placeholder.com/350x150");
      }

      model.addAttribute("time", new Date());
      model.addAttribute("username", email);
      model.addAttribute("content", content);

      return "loggedin";
    }
    return "false";

  }




  @RequestMapping("/logout")
  public String logout(){
    logger.info("Loading logout page.");
    return "index";

  }

  @RequestMapping("/backHome")
    public String backHome(){
        logger.info("Go back to home.");
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
