package com.wasoftware.bigdata.rest.controller;

import com.wasoftware.bigdata.rest.models.Menu;
import com.wasoftware.bigdata.rest.models.MenuDao;
import com.wasoftware.bigdata.rest.domain.UserMenu;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import java.util.logging.Logger;

@RestController
@Configurable
public class MenuController {

    private static Logger log = Logger.getLogger(DashboardController.class.getName());

    @Autowired
    MenuDao menuDao;

    HttpStatus httpStatus;

    @RequestMapping(value = "/menu", method = RequestMethod.POST)
    public @ResponseBody ResponseEntity<UserMenu> getMenu()
    {
        String groupName = "WSI"; //Hard coded for now until LDAP complete
        log.info("Request /menu");
        UserMenu respondMenu = null;
        Menu menu = menuDao.findByGroupName(groupName);

        if(menu != null) {
            respondMenu = new UserMenu(menu.getId(), menu.getMenuObject(), menu.getCreatedAt());
            httpStatus = httpStatus.OK;
        } else {
            httpStatus = httpStatus.BAD_REQUEST;
        }

        return new ResponseEntity<>(respondMenu, httpStatus);
    }
}
