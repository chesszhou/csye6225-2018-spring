package com.csye6225.spring2018.controller;

import dbDriver.Driver;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.File;
import java.sql.SQLException;

@Profile("dev")
@Controller
public class LocalSearchController {
    @RequestMapping("/search")
    public String search(@RequestParam("username") String username,
                         Model model) throws SQLException {

        Driver d = new Driver();
        String aboutme = d.searchForAboutMe(username);
        if (aboutme.equals("Not Found Such User")) {
            return "noResult";
        }

        File dir = new File(System.getProperty("user.home") + "/");
        File[] directoryListing = dir.listFiles();
        File todelete;
        String path = "";
        int flag = 0;
        if (directoryListing != null) {
            for (File eachFile : directoryListing) {

                String picName = eachFile.getName();
                System.out.println("pic" + picName);
                int i = picName.lastIndexOf('.');
                if (i == -1) {
                    continue;
                }
                String ownerName = picName.substring(0, i);
                if (ownerName.isEmpty()) {
                    continue;
                }


                if (ownerName.equals(username)) {

                    String abPath = eachFile.getAbsolutePath();
                    todelete = new File(abPath);
                    if (todelete.exists()) {
                        flag = 1;
                        System.out.println("exist!");
                    }

                    break;
                }
            }

        }
        if (flag == 1) {
            model.addAttribute("picURL", path);
        } else {
            model.addAttribute("picURL", "http://via.placeholder.com/350x150");
        }
        model.addAttribute("content", aboutme);
        model.addAttribute("username", username);
        return "searchResult";
    }
}
