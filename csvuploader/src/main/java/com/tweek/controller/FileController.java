package com.tweek.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.tweek.UserRegistration;
import com.tweek.model.FileResponse;
import com.tweek.model.User;
import com.tweek.service.IFileSytemStorage;

@RestController
@RequestMapping("/api")
public class FileController {
	final static Logger logger = LoggerFactory.getLogger(FileController.class);
	@Autowired
    IFileSytemStorage fileSytemStorage;

	@GetMapping("users")
	public List<User> getUsers() {
		return UserRegistration.getInstance().getUserRecords();
	}

	@PostMapping("users")
	@ResponseBody
	public User addUser(@RequestBody User user) {
		User newUser = new User();
		UserRegistration.getInstance().add(user);
		newUser.setName(user.getName());
		newUser.setAge(user.getAge());
		newUser.setGender(user.getGender());
		logger.info("User added successfully!");
		return newUser;
	}
	
	@PostMapping("uploadfile")
    public ResponseEntity<FileResponse> uploadSingleFile (@RequestParam("file") MultipartFile file) {
        String upfile = fileSytemStorage.saveFile(file);
        
        return ResponseEntity.status(HttpStatus.OK).body(new FileResponse(upfile,"File uploaded with success!"));
    }
	
}