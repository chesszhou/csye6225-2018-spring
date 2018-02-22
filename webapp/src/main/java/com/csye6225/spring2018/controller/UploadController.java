package com.csye6225.spring2018.controller;

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
    @RequestMapping(method = RequestMethod.POST, value = "/uploadSuc")
    public String handleFileUpload(@RequestParam("about") String about,
                                   @RequestParam("file") MultipartFile file,
                                   RedirectAttributes redirectAttributes) {
          AmazonS3 s3client = new AmazonS3Client(new ProfileCredentialsProvider());

          String bucketName = "renda-" + UUID.randomUUID();
          s3client.createBucket(bucketName);

          try{
              InputStream is = file.getInputStream();

              //save on s3 with public read access
              s3client.putObject(new PutObjectRequest(bucketName, about, is, new ObjectMetadata()).withCannedAcl(CannedAccessControlList.PublicRead));

              //get a reference to the image object
              S3Object s3object = s3client.getObject(new GetObjectRequest(
                      bucketName, about));

              //add to model
              redirectAttributes.addAttribute("picUrl", s3object.getObjectContent().getHttpRequest().getURI.toString();

              System.out.println(s3object.getObjectContent().getHttpRequest().getURI.toString());
          } catch (IOException e) {
              e.printStackTrace();
          }

          return ""
    }
}
