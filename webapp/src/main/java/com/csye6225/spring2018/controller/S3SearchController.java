package com.csye6225.spring2018.controller;


import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.iterable.S3Objects;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import com.csye6225.spring2018.S3Configure;
import dbDriver.Driver;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.sql.SQLException;

@Profile("aws")
@Controller
public class S3SearchController {
    @RequestMapping("/search")
    public String search(@RequestParam("username") String username,
                         Model model) throws SQLException {

        Driver d = new Driver();
        String aboutme = d.searchForAboutMe(username);
        S3Configure s3Configure = new S3Configure();
        if(aboutme.equals("Not Found Such User")){
            return "noResult";
        }

        AWSCredentials credentials = new BasicAWSCredentials(s3Configure.getAccessKey(), s3Configure.getSecretKey());
        AmazonS3 s3client = new AmazonS3Client(credentials);
        S3Object retrievedPic = null;
        String toRetrieve = "";
        for(S3ObjectSummary summary: S3Objects.inBucket(s3client, s3Configure.getBucketName())){
            String picName = summary.getKey();
            int i = picName.lastIndexOf('.');
            String ownerName = picName.substring(0, i);
            if(ownerName.equals(username)){
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
        model.addAttribute("content", aboutme);
        model.addAttribute("username", username);
        return "searchResult";
    }
}
