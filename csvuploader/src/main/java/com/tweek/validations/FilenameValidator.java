package com.tweek.validations;

import org.apache.commons.io.FilenameUtils;

import com.tweek.exception.UnsupportedFileFormatException;

public class FilenameValidator {

	public static void validate(String filename) {
		if (!FilenameUtils.isExtension(filename, "csv")) {
			throw new UnsupportedFileFormatException("Only csv file format is supported.");
		}
	}
}