package com.csye6225.spring2018.controller;

import com.csye6225.spring2018.Repo.UserRepository;
import com.csye6225.spring2018.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.File;
import java.io.IOException;
import java.util.Date;


@Profile("dev")
@Controller
public class LocalController {

    @Autowired
    private UserRepository userRepository;

    @RequestMapping(method = RequestMethod.POST, value = "/loggedin")
    public String handleFileUpload(@RequestParam("content") String content,
                                   @RequestParam("filechooser") MultipartFile file,
                                   @RequestParam("username") String username,
                                   @RequestParam("picURL") String picURL,
                                   RedirectAttributes redirectAttributes,
                                   Model model) {
        String originalFileName = file.getOriginalFilename();
        String extension = "";

        int i = originalFileName.lastIndexOf('.');
        if (i > 0) {
            extension = originalFileName.substring(i + 1);
        }

        try{
            String pic_path = System.getProperty("user.home") + "/";
            String newFileName = username + "." + extension;
            File newFile = new File(pic_path + newFileName);
            file.transferTo(newFile);

            User existingUser = userRepository.findByUsername(username);
            existingUser.setAboutMe(content);
            userRepository.save(existingUser);

            model.addAttribute("time", new Date());
            model.addAttribute("username", username);
            model.addAttribute("content", content);

            File dir = new File(System.getProperty("user.home") + "/");
            File[] directoryListing = dir.listFiles();
            if (directoryListing != null) {
                for(File eachFile: directoryListing) {

                    String picName = eachFile.getName();

                    int j = picName.lastIndexOf('.');
                    if(j == -1){
                        continue;
                    }
                    String ownerName = picName.substring(0, j);
                    if(ownerName.isEmpty()) {
                        continue;
                    }

                    if (ownerName.equals(username)){

                        String abPath = eachFile.getAbsolutePath();
                        File todisplay = new File(abPath);
                        if(todisplay.exists()){
                            System.out.println("exist!");
                            model.addAttribute("picURL", abPath);
                        }else {
                            model.addAttribute("picURL", "http://via.placeholder.com/350x150");
                        }

                        break;
                    }
                }

            }


        } catch (IOException e) {
            e.printStackTrace();
        }

        return "loggedin";
    }
}
