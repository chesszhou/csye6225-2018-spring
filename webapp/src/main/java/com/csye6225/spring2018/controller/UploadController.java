package com.csye6225.spring2018.controller;

import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;


@Controller
public class UploadController {
    @RequestMapping(method = RequestMethod.POST, value = "/loggedin")
    public String handleFileUpload(@RequestParam("content") String content,
                                   @RequestParam("file") MultipartFile file,
                                   @RequestParam("username") String username,
                                   RedirectAttributes redirectAttributes) {
          AmazonS3 s3client = new AmazonS3Client(new ProfileCredentialsProvider());


          String bucketName = "renda-" + UUID.randomUUID();
          s3client.createBucket(bucketName);

          try{
              InputStream is = file.getInputStream();

              //save on s3 with public read access
              s3client.putObject(new PutObjectRequest(username, content, is, new ObjectMetadata()).withCannedAcl(CannedAccessControlList.PublicRead));

              //get a reference to the image object
              S3Object s3object = s3client.getObject(new GetObjectRequest(
                      username, content));

              //add to model
              RedirectAttributes picUrl = redirectAttributes.addAttribute("picUrl", s3object.getObjectContent().getHttpRequest().getURI().toString());

              System.out.println(s3object.getObjectContent().getHttpRequest().getURI().toString());
          } catch (IOException e) {
              e.printStackTrace();
          }

          return "loggedin";
    }
}
