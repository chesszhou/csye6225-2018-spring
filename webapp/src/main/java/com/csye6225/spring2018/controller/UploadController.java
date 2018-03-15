package com.csye6225.spring2018.controller;


import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.iterable.S3Objects;
import com.amazonaws.services.s3.model.*;
import com.csye6225.spring2018.Repo.UserRepository;
import com.csye6225.spring2018.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.Date;


@Profile("aws")
@Controller
public class UploadController {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private Environment env;

    @RequestMapping(method = RequestMethod.POST, value = "/loggedin")
    public String handleFileUpload(@RequestParam("content") String content,
                                   @RequestParam("filechooser") MultipartFile file,
                                   @RequestParam("username") String username,
                                   @RequestParam("picURL") String picURL,
                                   RedirectAttributes redirectAttributes, Model model) throws SQLException {

        AmazonS3 s3client = AmazonS3ClientBuilder.standard().build();

        String fileName = file.getOriginalFilename();

        String extension = "";

        int i = fileName.lastIndexOf('.');
        if (i > 0) {
           extension = fileName.substring(i+1);
        }

        String bucketName = env.getProperty("bucket.name");
        s3client.createBucket(bucketName);
        String picName = username + "." + extension;

        model.addAttribute("time", new Date());
        model.addAttribute("username", username);
        model.addAttribute("content", content);

        String toRetrieve = "";
        S3Object retrievedPic = null;


        try{
            InputStream is = file.getInputStream();
            //save on s3 with public read access
            if(!extension.isEmpty()){
                s3client.putObject(new PutObjectRequest(bucketName, picName, is, new ObjectMetadata()).withCannedAcl(CannedAccessControlList.PublicRead));
            }

            User existingUser = userRepository.findByUsername(username);
            existingUser.setAboutMe(content);
            userRepository.save(existingUser);

            for(S3ObjectSummary summary: S3Objects.inBucket(s3client, bucketName)){
                String repicName = summary.getKey();
                int j = repicName.lastIndexOf('.');
                String ownerName = picName.substring(0, j);
                if(ownerName.equals(username)){
                    toRetrieve = picName;
                    retrievedPic = s3client.getObject(bucketName, toRetrieve);
                    break;
                }
            }
            if(retrievedPic != null){
                picURL = retrievedPic.getObjectContent().getHttpRequest().getURI().toString();
                model.addAttribute("picURL",retrievedPic.getObjectContent().getHttpRequest().getURI().toString());
            }else {
                model.addAttribute("picURL", "http://via.placeholder.com/350x150");
            }
            //get a reference to the image object

        } catch (IOException e) {
            e.printStackTrace();
        }
        return "loggedin";
    }
}
