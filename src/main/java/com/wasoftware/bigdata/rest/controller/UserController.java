package com.wasoftware.bigdata.rest.controller;

import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import com.wasoftware.bigdata.rest.models.*;
import java.util.*;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Configurable
public class UserController {

    private static Logger log = Logger.getLogger(UserController.class.getName());

    @Autowired
    UserDao userDao;

    HttpStatus httpStatus;

    @RequestMapping("/user/index")
    public void index() {
        try {
            List<User> users = (List<User>) userDao.findAll();
            for (User eachUser : users) {
                System.out.println("---------" + eachUser.toString());
            }
        } catch (Exception e) {
            System.out.println(e.toString());
        }
    }

    /**
     * Helper Function: getUserIdByUsername() takes a string of username and return the assigned user ID.
     * @param username - a string value that represents the account's name
     * @return ResponseEntity includes an userid and a http status code
     */
    @RequestMapping("/user/getUserId")
    public ResponseEntity<String> getUserIdByUsername(@RequestParam(value = "username", defaultValue = "0") String username)
    {
        log.info("Request /user/getUserId");
        String userid = "";
        log.info("Timestamp: " + new Date() + " username=" + username);

        if(username != "0" && userDao.findByUsername(username) != null) {
            User user = userDao.findByUsername(username);
            userid = String.valueOf(user.getId());
            httpStatus = HttpStatus.OK;
        } else {
            log.info("'" + userid + "'' is not valid.");
            httpStatus = HttpStatus.BAD_REQUEST;
        }
        return new ResponseEntity<>(userid, httpStatus);
    }


}
