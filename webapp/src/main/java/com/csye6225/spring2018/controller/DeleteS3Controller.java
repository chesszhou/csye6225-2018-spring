package com.csye6225.spring2018.controller;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.iterable.S3Objects;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Date;

@Profile("aws")
@Controller
public class DeleteS3Controller {
    @RequestMapping(method = RequestMethod.POST, value = "/delete")
    public String handleS3Delete(
            @RequestParam("username") String userName,
            @RequestParam("content") String content,
            @RequestParam("picURL") String picURL,
            RedirectAttributes redirectAttributes, Model model){
        AWSCredentials credentials = new BasicAWSCredentials("AKIAJYRJRH6MYNFWM5CA", "Je05pI284KdSIZj2zlyL3QrPh1PPX+u+Fy16la18");
        AmazonS3 s3client = new AmazonS3Client(credentials);
        String toDelete = "";
        for(S3ObjectSummary summary: S3Objects.inBucket(s3client, "s3.csye6225-spring2018-profilepics.me")){
            String picName = summary.getKey();
            int i = picName.lastIndexOf('.');
            String ownerName = picName.substring(0, i);
            if(ownerName.equals(userName)){
                toDelete = picName;
                break;
            }
        }
        if(!toDelete.equals("")){
            s3client.deleteObject("s3.csye6225-spring2018-profilepics.me", toDelete);
        }
        model.addAttribute("time", new Date());
        model.addAttribute("username", userName);
        model.addAttribute("content", content);
        model.addAttribute("picURL", picURL);
        return "loggedin";
    }
}
