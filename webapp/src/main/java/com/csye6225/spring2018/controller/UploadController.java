package com.csye6225.spring2018.controller;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.*;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.UUID;


@Profile("aws")
@Controller
public class UploadController {
    @RequestMapping(method = RequestMethod.POST, value = "/loggedin")
    public String handleFileUpload(@RequestParam("content") String content,
                                   @RequestParam("filechooser") MultipartFile file,
                                   @RequestParam("username") String username,
                                   RedirectAttributes redirectAttributes, Model model) {
        AWSCredentials credentials = new BasicAWSCredentials("AKIAJYRJRH6MYNFWM5CA", "Je05pI284KdSIZj2zlyL3QrPh1PPX+u+Fy16la18");
        AmazonS3 s3client = new AmazonS3Client(credentials);

        String fileName = file.getOriginalFilename();

        String extension = "";

        int i = fileName.lastIndexOf('.');
        if (i > 0) {
           extension = fileName.substring(i+1);
        }

        String bucketName = "s3.csye6225-spring2018-zhuzheny.me";
        s3client.createBucket(bucketName);
        String picName = username + "." + extension;

        model.addAttribute("time", new Date());
        model.addAttribute("username", username);

        try{
            InputStream is = file.getInputStream();
            //save on s3 with public read access
            s3client.putObject(new PutObjectRequest(bucketName, picName, is, new ObjectMetadata()).withCannedAcl(CannedAccessControlList.PublicRead));
            //get a reference to the image object
            S3Object s3object = s3client.getObject(new GetObjectRequest(
                    bucketName, picName));
            //add to model
            RedirectAttributes picUrl = redirectAttributes.addAttribute("picUrl", s3object.getObjectContent().getHttpRequest().getURI().toString());
            System.out.println(s3object.getObjectContent().getHttpRequest().getURI().toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "loggedin";
    }
}
