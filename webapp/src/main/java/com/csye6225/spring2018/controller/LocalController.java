package com.csye6225.spring2018.controller;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@Profile("dev")
@Controller
public class LocalController {
    @RequestMapping(method = RequestMethod.POST, value = "/loggedin")
    public String handleFileUpload(@RequestParam("content") String content,
                                   @RequestParam("filechooser") MultipartFile file,
                                   RedirectAttributes redirectAttributes) {
        String originalFileName = file.getOriginalFilename();
        String extension = "";

        int i = originalFileName.lastIndexOf('.');
        if (i > 0) {
            extension = originalFileName.substring(i+1);
        }

        try{
            String pic_path = System.getProperty("user.home");
            String newFileName = "/ahahha" + "." + extension;
            File newFile = new File(pic_path + newFileName);
            file.transferTo(newFile);
            System.out.println(newFileName);
            System.out.println(newFileName);
            System.out.println(newFileName);
            System.out.println(newFileName);
            System.out.println(newFileName);


        } catch (IOException e) {
            e.printStackTrace();
        }

        return "loggedin";
    }
}
