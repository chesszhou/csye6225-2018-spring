package com.csye6225.spring2018.controller;


import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.iterable.S3Objects;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import com.amazonaws.services.sns.AmazonSNS;
import com.amazonaws.services.sns.AmazonSNSClient;
import com.amazonaws.services.sns.AmazonSNSClientBuilder;
import com.amazonaws.services.sns.model.PublishRequest;
import com.csye6225.spring2018.BCrypt;
import com.csye6225.spring2018.Repo.UserRepository;
import com.csye6225.spring2018.S3Configure;
import com.csye6225.spring2018.entity.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.sql.SQLException;
import java.util.Date;

@Profile("aws")
@Controller
public class IndexController{

    private final static Logger logger = LoggerFactory.getLogger(IndexController.class);
    @Autowired
    // This means to get the bean called userRepository
    // Which is auto-generated by Spring, we will use it to handle the data
    private UserRepository userRepository;
    @Autowired
    private Environment env;


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

    @RequestMapping("/resetPassword")
    public String resetPassword(@RequestParam("username") String username) {
        AmazonSNS snsClient = AmazonSNSClientBuilder.standard().withRegion(Regions.US_EAST_1).build();
        String msg = username;
        logger.info(username);
        logger.info(username);
        logger.info(username);
        PublishRequest publishRequest = new PublishRequest(env.getProperty("amazon.sns.topic.arn"), msg);
        snsClient.publish(publishRequest);
        return "resetPassword";
    }

    @PostMapping("/success")
    public String addNewUser (HttpServletRequest request) {
      logger.info("Loading success page.");
      String username = request.getParameter("username");
      String password = request.getParameter("password");
      User existingUser = userRepository.findByUsername(username);
      if(existingUser == null){
        User n = new User();
        n.setUsername(username);
        String passwordS = BCrypt.hashpw(password, BCrypt.gensalt());
        n.setPassword(passwordS);
        userRepository.save(n);
        return "success";
      }else{
        return "exist";
      }
    }

    @RequestMapping("/login")
    public String login(HttpServletRequest request, Model model) throws SQLException {
      logger.info("Loading login page.");
      String username = request.getParameter("username");
      String password = request.getParameter("password");

      User existingUser = userRepository.findByUsername(username);
      if(existingUser == null){
        return "false";
      }else{
        if(BCrypt.checkpw(password, existingUser.getPassword())){
          //AWSCredentialsProvider defaultAWSCredentialsProvider = new DefaultAWSCredentialsProviderChain();
          AmazonS3 s3client = AmazonS3ClientBuilder.standard().build();
          S3Object retrievedPic = null;
          String toRetrieve = "";
          for(S3ObjectSummary summary: S3Objects.inBucket(s3client, env.getProperty("bucket.name"))){
            String picName = summary.getKey();
            int i = picName.lastIndexOf('.');
            String ownerName = picName.substring(0, i);
            if(ownerName.equals(username)){
              toRetrieve = picName;
              retrievedPic = s3client.getObject(env.getProperty("bucket.name"), toRetrieve);
              break;
            }
          }

          if(retrievedPic != null){
            model.addAttribute("picURL",retrievedPic.getObjectContent().getHttpRequest().getURI().toString());
          }else {
            model.addAttribute("picURL", "http://via.placeholder.com/350x150");
          }

          String content = existingUser.getAboutMe();
          model.addAttribute("time", new Date());
          model.addAttribute("username", username);
          model.addAttribute("content", content);

          return "loggedin";
        }else{
          return "false";
        }
      }
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
}
