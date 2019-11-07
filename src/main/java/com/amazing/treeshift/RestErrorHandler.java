/**
 * Main error handler for the REST API.
 *
 * Has two responsibilities: don't leak stack traces to the client (but log them on the server), and translate
 * business logic exceptions to proper error replies to the client.
 *
 * The code is currently pretty raw — in a real application, we should be throwing business logic exceptions
 * that all have a 'client friendly' message and HTTP status code. That way, a uniform error JSON reply with
 * a proper error message and status code can be sent to the client, while logging a stack trace for
 * exceptions that weren't expected, or a shorter non-stack-trace message for expected errors.
 */
package com.amazing.treeshift;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.lang.invoke.MethodHandles;

@ControllerAdvice
public class RestErrorHandler {
	private Logger log = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

	@ExceptionHandler(IllegalArgumentException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ResponseBody
	String handleApplicationErrors(IllegalArgumentException ex) {
		log.info("Client sent a bad request: {}", ex.getMessage());

		return ex.getMessage();
	}

	@ExceptionHandler(Exception.class)
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	@ResponseBody
	String handleApplicationErrors(Exception ex) {
		log.error("Unhandled exception!", ex);

		return "Internal Server Error";
	}
}
