
package com.kbpluse.atendance.atendace.Activity.Domain;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LogoutDomain {

    @SerializedName("emp_id")
    @Expose
    private Integer empId;
    @SerializedName("login_id")
    @Expose
    private Integer loginId;
    @SerializedName("logout_time")
    @Expose
    private String logoutTime;

    public Integer getEmpId() {
        return empId;
    }

    public void setEmpId(Integer empId) {
        this.empId = empId;
    }

    public Integer getLoginId() {
        return loginId;
    }

    public void setLoginId(Integer loginId) {
        this.loginId = loginId;
    }

    public String getLogoutTime() {
        return logoutTime;
    }

    public void setLogoutTime(String logoutTime) {
        this.logoutTime = logoutTime;
    }

}
