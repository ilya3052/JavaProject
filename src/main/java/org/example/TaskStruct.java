package org.example;

public class TaskStruct {
    private String taskDescription, taskTime, taskName;
    private static String chatId;
    private static int count = 1;
    private int id;
//    private InlineKeyboardButton inlineKeyboardButton; кнопки на сообщении готовой записи, оставить Владосу
    /*предположительно для каждой записи будет несколько кнопок
    * 1) Изменить имя задачи
    * 2) Изменить описание задачи
    * 3) Добавить текст к описанию задачи
    * 4) Изменить время записи
    * 5) Удалить запись
    *
    * Кнопки для общего списка задач пропишу позже, там они чуть иные будут*/
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
    public int getId()
    {
        return this.id;
    }
    public String getChatId(){
        return this.chatId;
    }

}
