package com.tweek.controller;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import com.tweek.properties.EmailSourceProperties;
import com.tweek.properties.FileUploadProperties;
import com.tweek.utils.CommonUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.tweek.UserRegistration;
import com.tweek.model.FileResponse;
import com.tweek.model.User;
import com.tweek.service.IFileSytemStorage;
import com.tweek.validations.FilenameValidator;
import org.springframework.web.multipart.MultipartHttpServletRequest;

@RestController
@RequestMapping("/api")
public class FileController {

	final static Logger logger = LoggerFactory.getLogger(FileController.class);
	@Autowired
    IFileSytemStorage fileSystemStorage;

	private final String emailWriteKey;

	@Autowired
	public FileController(EmailSourceProperties emailSourceProperties) {
		emailWriteKey = emailSourceProperties.getWriteKey();
	}

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
    public ResponseEntity<FileResponse> uploadSingleFile (@RequestParam("file") MultipartFile file, @RequestParam("collectionName") String collectionName, @RequestParam("key") String key) {
		String fileName = file.getOriginalFilename();
		FilenameValidator.validate(fileName);
		System.out.println("key is: " + key);
		String upfile = fileSystemStorage.saveFile(file, collectionName, key);
		return ResponseEntity.status(HttpStatus.OK).body(new FileResponse(upfile,"File uploaded successfully!"));
    }

	@PostMapping("email-inbound")
    public ResponseEntity<FileResponse> emailInbound(MultipartHttpServletRequest request) {

		logger.info("Got email inbound request.");
		int attachments = 0;
		String identifier = emailWriteKey;

//		if(!request.getParameterMap().containsKey("to") || request.getParameterMap().get("to").length == 0) {
//			logger.error("'to' parameter missing.");
//			return ResponseEntity.status(HttpStatus.OK).build();
//		}
//
//		identifier =  request.getParameterMap().get("to")[0].split("@")[0];
//
//		if(identifier == null || identifier.isEmpty()) {
//			logger.error("Couldn't parse identifier");
//			return ResponseEntity.status(HttpStatus.OK).build();
//		}

		logger.info("Identifier: {}", identifier);

		if(request.getParameterMap().containsKey("attachments")) {
			try {
				attachments = Integer.parseInt(request.getParameter("attachments"));
			} catch (Exception e) {
				logger.warn("Couldn't parse attachment: {}", e.getMessage());
			}
		}

		logger.info("attachments={}", attachments);

		if(attachments == 0) {
			logger.warn("No attachment to process");
			return ResponseEntity.status(HttpStatus.OK).build();
		}

		if(request.getFileMap().size() == 0) {
			logger.warn("File map is empty");
			return ResponseEntity.status(HttpStatus.OK).build();
		}

		for(int i = 1; i <= attachments; i++) {
			String key = "attachment" + i;

			if(!request.getFileMap().containsKey(key)) {
				logger.error("Attachment not found: {}", key);
				continue;
			}

			try {
				MultipartFile file = request.getFile(key);
				String collectionName = CommonUtils.getCollectionName(file.getOriginalFilename());
				fileSystemStorage.saveFile(file, collectionName, identifier);
			} catch (Exception e) {
				logger.error("Unable to save file: ", e);
			}
		}
        
        return ResponseEntity.status(HttpStatus.OK).build();
    }
	
}