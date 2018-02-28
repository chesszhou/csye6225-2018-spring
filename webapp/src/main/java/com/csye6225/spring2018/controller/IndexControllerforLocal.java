package com.csye6225.spring2018.controller;

import com.csye6225.spring2018.entity.AccountDirectory;
import dbDriver.Driver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.sql.SQLException;
import java.util.Date;

@Profile("dev")
@Controller
public class IndexControllerforLocal {

    private final static Logger logger = LoggerFactory.getLogger(IndexControllerforLocal.class);
    private AccountDirectory accountDirectory;
    private Driver driver;

    public IndexControllerforLocal() {
        accountDirectory = new AccountDirectory();
//        this.driver = new Driver();
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

        if (driver.isValidUser(email, password)) {
            File dir = new File(System.getProperty("user.home") + "/");
            File[] directoryListing = dir.listFiles();
            if (directoryListing != null) {
                for (File eachFile : directoryListing) {

                    String picName = eachFile.getName();

                    int i = picName.lastIndexOf('.');
                    if (i == -1) {
                        continue;
                    }
                    String ownerName = picName.substring(0, i);
                    if (ownerName.isEmpty()) {
                        continue;
                    }
                    if (ownerName.equals(email)) {

                        String abPath = eachFile.getAbsolutePath();
                        System.out.println(abPath);
                        File todisplay = new File(abPath);
                        String tempUrl = abPath;
                        System.out.println(tempUrl);

                        if (todisplay.exists()) {
                            System.out.println("exist!");
                            model.addAttribute("picURL", tempUrl);
                        } else {
                            model.addAttribute("picURL", "http://via.placeholder.com/350x150");
                        }
                        break;
                    }
                }
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
}
