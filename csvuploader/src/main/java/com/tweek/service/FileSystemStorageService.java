package com.tweek.service;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import javax.annotation.PostConstruct;

import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.tweek.exception.FileNotFoundException;
import com.tweek.exception.FileStorageException;
import com.tweek.properties.FileUploadProperties;

@Service
public class FileSystemStorageService implements IFileSytemStorage {
	private static final String FILE_NAME_SEPARATOR = "_";
	//private static final String FILE_NAME_PREFIX = "object";
	private final Path dirLocation;

	@Autowired
	public FileSystemStorageService(FileUploadProperties fileUploadProperties) {
		this.dirLocation = Paths.get(fileUploadProperties.getLocation()).toAbsolutePath().normalize();
	}

	@Override
	@PostConstruct
	public void init() {
		try {
			Files.createDirectories(this.dirLocation);
		} catch (Exception ex) {
			throw new FileStorageException("Could not create upload dir!");
		}
	}

	@Override
	public String saveFile(MultipartFile file, String collectionName, String key) {
		try {
			String fileName = file.getOriginalFilename();
			Path dfile = this.dirLocation.resolve(constructNewFileName(collectionName, key));
			Files.copy(file.getInputStream(), dfile, StandardCopyOption.REPLACE_EXISTING);
			return fileName;

		} catch (Exception e) {
			throw new FileStorageException("Could not upload file");
		}
	}

	private String constructNewFileName(String collectionName, String key) {
		return System.currentTimeMillis() + FILE_NAME_SEPARATOR + key + FILE_NAME_SEPARATOR + collectionName;
	}

	@Override
	public Resource loadFile(String fileName) {
		// TODO Auto-generated method stub
		try {
			Path file = this.dirLocation.resolve(fileName).normalize();
			Resource resource = new UrlResource(file.toUri());

			if (resource.exists() || resource.isReadable()) {
				return resource;
			} else {
				throw new FileNotFoundException("Could not find file");
			}
		} catch (MalformedURLException e) {
			throw new FileNotFoundException("Could not download file");
		}
	}
}