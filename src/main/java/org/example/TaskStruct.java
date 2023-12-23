package org.example;

public class TaskStruct {
    private String taskDescription, taskTime, taskName;
//    private InlineKeyboardButton inlineKeyboardButton; кнопки на сообщении готовой записи, оставить Владосу
    /*предположительно для каждой записи будет несколько кнопок
    * 1) Изменить имя
    * 2) Изменить описание
    * 3) Добавить текст к описанию задачи
    * 4) Изменить время записи
    * 5) Удалить запись
    *
    * Кнопки для общего списка задач пропишу позже, там они чуть иные будут*/
    public TaskStruct(String taskName, String taskDescription, String taskTime){
        this.taskName = taskName;
        this.taskDescription = taskDescription;
        this.taskTime = taskTime;
    }
    public TaskStruct(String taskName){
        this.taskName = taskName;
    }
    public void updateText(String additionalText){
        this.taskDescription += additionalText;
    }
    public void updateName(String newTaskName){
        this.taskName = newTaskName;
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
    public void updateTime(String newTaskTime){
        this.taskTime = newTaskTime;
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

}
