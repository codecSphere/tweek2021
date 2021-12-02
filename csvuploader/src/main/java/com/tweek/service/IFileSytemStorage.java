package com.tweek.service;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface IFileSytemStorage {
	void init();

	String saveFile(MultipartFile file, String collectionName, String key);

	Resource loadFile(String fileName);
}