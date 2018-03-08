package com.csye6225.spring2018.controller;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.File;
import java.util.Date;

@Profile("dev")
@Controller
public class DeleteLocalController {
    @RequestMapping(method = RequestMethod.POST, value = "/delete")
    public String handleS3Delete(@RequestParam("username") String userName,
                                 @RequestParam("content") String content,
                                 RedirectAttributes redirectAttributes, Model model){
        File dir = new File(System.getProperty("user.home") + "/");
        File[] directoryListing = dir.listFiles();
        if (directoryListing != null) {
            for(File eachFile: directoryListing) {

                String picName = eachFile.getName();
                System.out.println("pic" + picName);
                int i = picName.lastIndexOf('.');
                if(i == -1){
                    continue;
                }
                String ownerName = picName.substring(0, i);
                if(ownerName.isEmpty()) {
                    continue;
                }
                System.out.println("userName:" + userName);
                System.out.println("owner" + ownerName);
                if (ownerName.equals(userName)){
                    System.out.println("userName:" + userName);
                    System.out.println("owner" + ownerName);
                    String abPath = eachFile.getAbsolutePath();
                    File todelete = new File(abPath);
                    if(todelete.exists()){
                        System.out.println("exist!");
                        todelete.delete();
                    }

                    break;
                }
            }

        }
        model.addAttribute("time", new Date());
        model.addAttribute("username", userName);
        model.addAttribute("content", content);
        return "loggedin";
    }
}
