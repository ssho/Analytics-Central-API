package com.wasoftware.bigdata.rest.controller;

import com.wasoftware.bigdata.rest.domain.Greeting;
import io.swagger.annotations.*;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.atomic.AtomicLong;
import java.util.logging.Logger;

@RestController
public class GreetingController {


	static Logger log = Logger.getLogger(GreetingController.class.getName());

	private static final String template = "Hello, %s!";

	private final AtomicLong counter = new AtomicLong();



	@ApiOperation(value = "getGreeting", nickname = "getGreeting")
	@RequestMapping(method = RequestMethod.GET, value="/greeting", produces = "application/json")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "name", value = "User's name",
					required = false, dataType = "string",
					paramType = "query", defaultValue="Niklas")
	})
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Success", response = Greeting.class),
			@ApiResponse(code = 401, message = "Unauthorized"),
			@ApiResponse(code = 403, message = "Forbidden"),
			@ApiResponse(code = 404, message = "Not Found"),
			@ApiResponse(code = 500, message = "Failure")})
	public Greeting greeting() {
		return new Greeting(counter.incrementAndGet(),
				String.format(template, "Roy"));
	}

}