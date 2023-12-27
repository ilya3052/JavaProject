package org.example;

public class TaskStruct {
    private String taskDescription, taskTime, taskName;
    private static String chatId;
    private static int count = 1;
    private int id;

    public TaskStruct(String taskName, String taskDescription, String taskTime, String chatId){
        this.taskName = taskName;
        this.taskDescription = taskDescription;
        this.taskTime = taskTime;
        this.id = count++;
        TaskStruct.chatId = chatId;
    }
    public TaskStruct(String taskName, String chatId){
        this.taskName = taskName;
        this.id = count++;
        TaskStruct.chatId = chatId;
    }
    public void updateTaskDescription(String additionalText){
        if (this.taskDescription.endsWith(".") || this.taskDescription.endsWith("?") || this.taskDescription.endsWith("!")) {
            this.taskDescription += (" " + additionalText);
        }
        else{
            this.taskDescription += (". " + additionalText);
        }
    }
    public void updateTaskName(String newTaskName){
        this.taskName = newTaskName;
    }
    public void setNewTaskDescription(String newTaskDescription){
        this.taskDescription = newTaskDescription;
    }
    public void updateTime(String newTaskTime){
        this.taskTime = newTaskTime;
    }
    public String getNameTask(){
        return this.taskName;
    }
    public String getTaskDescription(){
        if (this.taskDescription != null){
            return this.taskDescription;
        }
        else {
            return "Не установлено";
        }
    }
    public String getTime(){
        if (this.taskTime != null)
        {
            return this.taskTime;
        }
        else
        {
            return "Не установлено";
        }
    }
    public int getId()
    {
        return this.id;
    }
    public void setId(int newID) {
      this.id = newID;
    }

}
