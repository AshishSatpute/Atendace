package com.kbpluse.atendance.atendace.Activity.AddTasks.dataModel;

public class AddTasksDataModel {
    private int id;
    private String tasksName;
    private String TasksDecription;

    public AddTasksDataModel() {
    }

    @Override
    public String toString() {
        return "AddTasksDataModel{" +
                "id=" + id +
                ", tasksName='" + tasksName + '\'' +
                ", TasksDecription='" + TasksDecription + '\'' +
                '}';
    }

    public AddTasksDataModel(int id, String tasksName, String tasksDecription) {
        this.id = id;
        this.tasksName = tasksName;
        TasksDecription = tasksDecription;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTasksName() {
        return tasksName;
    }

    public void setTasksName(String tasksName) {
        this.tasksName = tasksName;
    }

    public String getTasksDecription() {
        return TasksDecription;
    }

    public void setTasksDecription(String tasksDecription) {
        TasksDecription = tasksDecription;
    }
}
