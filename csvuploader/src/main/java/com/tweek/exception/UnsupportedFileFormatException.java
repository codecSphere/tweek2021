package com.tweek.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class UnsupportedFileFormatException extends RuntimeException {
	private String message;
}
