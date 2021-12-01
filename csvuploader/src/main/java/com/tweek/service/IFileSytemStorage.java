package com.tweek.service;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface IFileSytemStorage {
	void init();

	String saveFile(MultipartFile file);

	void saveFile(String identifier, MultipartFile file) throws IOException;

	Resource loadFile(String fileName);
}