package com.wasoftware.bigdata.rest.controller;

import com.wasoftware.bigdata.rest.domain.MessageResponse;
import com.wasoftware.bigdata.rest.domain.UserDashboard;
import com.wasoftware.bigdata.rest.models.Dashboard;
import com.wasoftware.bigdata.rest.models.DashboardDao;
import com.wasoftware.bigdata.rest.models.UserDao;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.ws.rs.DefaultValue;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

import static org.apache.avro.generic.GenericData.StringType.String;

@RestController
@Configurable
public class DashboardController {

    private static Logger log = Logger.getLogger(DashboardController.class.getName());

    @Autowired
    DashboardDao dashboardDao;

    @Autowired
    UserDao userdao;

    int ACTIVE = 1;
    HttpStatus httpStatus;

    /**
     * Rest call to save the current dashboard setting
     * @param dashboard - Dashboard object contains userid, dashboardName, and dashboardObject
     * @return message object about the action result along with http status code.
     */
    @RequestMapping(value = "/dashboard/save", method = RequestMethod.POST)
    public @ResponseBody ResponseEntity<MessageResponse> saveUserDashboard(@RequestBody Dashboard dashboard)
    {
        log.info("Request /dashboard/save");
        MessageResponse returnObject;
        long userid = dashboard.getUserid();
        String dashboardName = dashboard.getDashboardname();
        String dashboardSetting = dashboard.getDashboardobject();
        boolean isDefault = dashboard.getIsdefault();
        boolean isSystemDefault = false;
        log.info("Timestamp:" + new Date() + " userId=" + userid + ",dashboardName=" + dashboardName);

        if(userdao.findOne(userid) != null && dashboardName != null && dashboardSetting != null ) {
            if (isDefault == true) {
                dashboardDao.updateDefaultByUserid(userid);
            }
            dashboardDao.save(new Dashboard(userid, dashboardName, ACTIVE, dashboardSetting, isDefault, isSystemDefault));
            returnObject = new MessageResponse("'" + dashboardName + "' is saved.");
            httpStatus = HttpStatus.OK;
        } else {
            returnObject = new MessageResponse("Either userId, dashboard, or dashboardObject is invalid.");
            httpStatus = HttpStatus.BAD_REQUEST;
        }  //End if-else
        return new ResponseEntity<>(returnObject, httpStatus);
    }

    /**
     * Rest call to retrieve a list of saved and system default dashboards from specific user account.
     * @param userId - a string value represents user ID
     * @return A list of dashboard object along with http status code.
     */
    @RequestMapping(value = "/dashboard/findByUserId", method = RequestMethod.GET)
    public ResponseEntity<List<UserDashboard>> findDashboardByUserId(
            @RequestParam(value = "userId", defaultValue = "0") String userId)
    {
        System.out.println("Request /dashboard/findByUserId");
        log.info("Request /dashboard/findByUserId");
        List<UserDashboard> userDashboardList = new ArrayList<>();
        List<Dashboard> systemDefaultDashboardList = dashboardDao.findByIssystemdefault(true);
        List<Dashboard> dashboardList;
        log.info("Timestamp: " + new Date() + " userId=" + userId);

        if(userdao.findOne(Long.valueOf(userId)) != null ) {
            dashboardList = dashboardDao.findByUserid(Long.valueOf(userId));
            dashboardList.addAll(systemDefaultDashboardList);
            if(dashboardList.size() > 0) {
                for(Dashboard dashboard : dashboardList) {
                    if(dashboard.getActive() == ACTIVE) {
                        UserDashboard userDashboard = new UserDashboard(dashboard.getId(), dashboard.getDashboardname(),
                                 dashboard.getDashboardobject(), dashboard.getCreatedat());
                        userDashboardList.add(userDashboard);
                    } //End if
                } //End for-loop
            } else {
                log.info("There is problem retrieving dashboard in account '" + userId + "'.");
            } //End if-else
            httpStatus = HttpStatus.OK;

        } else {
            log.severe("Error: User id no match.");
            httpStatus = HttpStatus.BAD_REQUEST;
        } //End if-else
        return new ResponseEntity<>(userDashboardList, httpStatus);
    }

    /**
     * Rest call to find dashboard object by dashboard ID
     * @param dashboardID - a string value of dashboard object ID
     * @param userId - a string value of user ID
     * @return A Dashboard object along with http status code.
     */
    @RequestMapping(value = "/dashboard/findByDashboardID/{dashboardID}", method = RequestMethod.GET)
    public ResponseEntity<Dashboard> findDashboardByDashboardID (
            @PathVariable("dashboardID") String dashboardID,
            @RequestParam(value = "userId", defaultValue = "0") String userId)
    {
        log.info("Request /dashboard/findByDashboardID/{dashboardID}");
        Dashboard dashboard = new Dashboard();
        log.info("Timestamp: " + new Date() + " userId=" + userId + "dashboardID=" + dashboardID);

        if(dashboardDao.findByUseridAndId(Long.valueOf(userId), Long.valueOf(dashboardID)) != null) {
            dashboard = dashboardDao.findByUseridAndId(Long.valueOf(userId), Long.valueOf(dashboardID));
            httpStatus = HttpStatus.OK;
        } else {
            httpStatus = HttpStatus.BAD_REQUEST;
        } //End if-else
        return new ResponseEntity<>(dashboard, httpStatus);
    }

    /**
     * Rest call to find default dashboard by user id
     * @param userId - a string value of user ID
     * @return A default dashboard object along with http status code.
     */
    @RequestMapping(value = "/dashboard/findDefaultByUserId", method = RequestMethod.GET)
    public ResponseEntity<Dashboard> findDefaultDashboardByUserID (
            @RequestParam(value = "userId", defaultValue = "0") String userId)
    {
        log.info("Request /dashboard/findDefaultByUserId");
        log.info("Timestamp: " + new Date() + " userId=" + userId );

        Dashboard dashboard = dashboardDao.findByUseridAndIsdefault(Long.valueOf(userId), true);
        httpStatus = HttpStatus.OK;

        if(dashboard == null) {
            dashboard = dashboardDao.findByUseridAndIsdefault(0, true);
            if (dashboard == null) {
                httpStatus = HttpStatus.BAD_REQUEST; }
        } //End if-else
        return new ResponseEntity<>(dashboard, httpStatus);
    }

    /**
     * Rest call to delete dashboard
     * @param dashboardID - a string value of dashboard ID
     * @param userId - a string value of user ID
     * @return a message object of action result along with http status code.
     */
    @RequestMapping(value = "/dashboard/delete/{dashboardID}", method = RequestMethod.DELETE)
    public ResponseEntity<MessageResponse> deleteSpecificDashboard (
            @PathVariable("dashboardID") String dashboardID,
            @RequestParam(value = "userId", defaultValue = "0") String userId)
    {
        log.info("Request /dashboard/delete/{dashboardID}");
        MessageResponse returnObject;
        log.info("Timestamp: " + new Date() + " userId=" + userId + "dashboardID=" + dashboardID);

        if(Long.valueOf(userId) != 0) {
            Dashboard dashboard = dashboardDao.findByUseridAndId(Long.valueOf(userId), Long.valueOf(dashboardID));

            if (dashboard != null) {
                returnObject = new MessageResponse("'" + dashboard.getDashboardname() + "' is now removed.");
                dashboardDao.deleteByUseridAndId(Long.valueOf(userId), Long.valueOf(dashboardID));
                httpStatus = HttpStatus.OK;
            } else {
                log.info("Dashboard '" + dashboardID + "' does not found in account: '" + userId + "'.");
                returnObject = new MessageResponse("Dashboard '" + dashboardID + "' does not found in account: '" + userId + "'.");
                httpStatus = HttpStatus.BAD_REQUEST;
            } //End if-else
        } else {
            log.warning("System Default Dashboard is not allow to removed.");
            httpStatus = HttpStatus.BAD_REQUEST;
            returnObject = new MessageResponse("System Default Dashboard is not allow to removed.");
        }
        return new ResponseEntity<>(returnObject, httpStatus);
    }
}
