package com.tweek.validations;

import org.apache.commons.io.FilenameUtils;

import com.tweek.exception.UnsupportedFileFormatException;

public class FilenameValidator {

	public static void validate(String filename) {
		String extension = FilenameUtils.getExtension(filename);
		if (!"csv".equalsIgnoreCase(extension)) {
			throw new UnsupportedFileFormatException("Only csv file format is supported.");
		}
	}
}