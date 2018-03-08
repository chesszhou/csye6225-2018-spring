package com.csye6225.spring2018.controller;



import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.iterable.S3Objects;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import com.csye6225.spring2018.Repo.UserRepository;
import com.csye6225.spring2018.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.sql.SQLException;

@Profile("aws")
@Controller
public class S3SearchController {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private Environment env;

    @RequestMapping("/search")
    public String search(@RequestParam("username") String username,
                         Model model) throws SQLException {

        User existingUser = userRepository.findByUsername(username);
        if (existingUser == null) {
            return "noResult";
        } else {
            String aboutMe = existingUser.getAboutMe();
            if (aboutMe == null) {
                aboutMe = "";
            }
            AmazonS3 s3client = AmazonS3ClientBuilder.standard().build();
            S3Object retrievedPic = null;
            String toRetrieve = "";
            for (S3ObjectSummary summary : S3Objects.inBucket(s3client, env.getProperty("bucket.name"))) {
                String picName = summary.getKey();
                int i = picName.lastIndexOf('.');
                String ownerName = picName.substring(0, i);
                if (ownerName.equals(username)) {
                    toRetrieve = picName;
                    retrievedPic = s3client.getObject(env.getProperty("bucket.name"), toRetrieve);
                    break;
                }
            }

            if (retrievedPic != null) {
                model.addAttribute("picURL", retrievedPic.getObjectContent().getHttpRequest().getURI().toString());
            } else {
                model.addAttribute("picURL", "http://via.placeholder.com/350x150");
            }
            model.addAttribute("content", aboutMe);
            model.addAttribute("username", username);
            return "searchResult";
        }
    }
}
