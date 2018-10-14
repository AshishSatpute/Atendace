
package com.kbpluse.atendance.atendace.Activity.Domain;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TasksDomain {

    @SerializedName("task_title")
    @Expose
    private String taskTitle;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("emp_id")
    @Expose
    private Integer empId;

    public String getTaskTitle() {
        return taskTitle;
    }

    public void setTaskTitle(String taskTitle) {
        this.taskTitle = taskTitle;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getEmpId() {
        return empId;
    }

    public void setEmpId(Integer empId) {
        this.empId = empId;
    }

}
