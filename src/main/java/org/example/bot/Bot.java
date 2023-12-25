package org.example.bot;

import org.apache.commons.lang3.StringUtils;
import org.example.TaskStruct;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import javax.validation.constraints.NotNull;
import java.util.*;

public class Bot extends TelegramLongPollingBot {
    private Map<Long, List<TaskStruct>> chatTaskStructsMap;
    private Map<Long, TaskStruct> chatTaskObjectMap;
    private TaskStruct taskObject;
    public Bot()
    {
        chatTaskStructsMap = new HashMap<>();
        chatTaskObjectMap = new HashMap<>();
        initKeyboard();
    }

    private ReplyKeyboardMarkup replyKeyboardMarkup;
    @Override
    public String getBotUsername() {
        return "TeStTeXt111443242_bot";
    }

    @Override
    public String getBotToken() {
        return "6776303945:AAG9UnD2lGG8-FSOuIs5ZRudQIQhEoRNRbU";
    }

    @Override
    public void onUpdateReceived(Update update) {
        try {
            if (update.hasMessage() && update.getMessage().hasText()) {
                Message message = update.getMessage();
                long chatID = message.getChatId();
                String response = parseMessage(message.getText(), chatID);
                SendMessage sendMessage = new SendMessage();
                sendMessage.setChatId(String.valueOf(chatID));
                sendMessage.setText(response);
                sendMessage.setReplyMarkup(replyKeyboardMarkup);
                execute(sendMessage);
            }
        }catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }
    public void initKeyboard() {
        replyKeyboardMarkup = new ReplyKeyboardMarkup();
        replyKeyboardMarkup.setResizeKeyboard(true);
        ArrayList<KeyboardRow> keyboardRows = new ArrayList<>();
        KeyboardRow firstRow = new KeyboardRow();
        firstRow.add(new KeyboardButton("Показать краткий список"));
        firstRow.add(new KeyboardButton("Показать подробный список"));
        KeyboardRow secondRow = new KeyboardRow();
        secondRow.add(new KeyboardButton("Очистить список"));
        secondRow.add(new KeyboardButton("Помощь"));
        KeyboardRow thirdRow = new KeyboardRow();
        thirdRow.add(new KeyboardButton("Сохранить задачи в файл"));
        thirdRow.add(new KeyboardButton("Загрузить задачи из файла"));
        keyboardRows.add(firstRow);
        keyboardRows.add(secondRow);
        keyboardRows.add(thirdRow);
        replyKeyboardMarkup.setKeyboard(keyboardRows);
    }

    public TaskStruct findTask(String text, long chatID) {
        String name = text.replace("/find ", "").trim();
        List<TaskStruct> currTask = chatTaskStructsMap.get(chatID);
        if (StringUtils.isNumeric(name))
        {
            for (TaskStruct taskStruct : currTask) {
                if (String.valueOf(taskStruct.getId()).equals(name)) {
                    return taskStruct;
                }
            }
        }
        else {
            for (TaskStruct taskStruct : currTask) {
                if (taskStruct.getNameTask().equals(name)) {
                    return taskStruct;
                }
            }
        }
        return null;
    }

    public String parseMessage(String text, long chatID) {
        String response;
        if (text.equals("/start")) {
            response = "Для вызова списка команд введите /help";
        }
        else if (text.equals("/help") || text.equals("Помощь")) {
            response = """
                    Команды бота
                    1) /addTask "Название задачи" - добавляет задачу только с названием
                    2) Связка команд вида
                    /addTask "Название задачи"
                    /addTaskTime "Время задачи"
                    /addTaskDescription "Описание задачи"
                    Добавляет в список дел полную информацию о задаче.
                    3) /updateTaskDescription "Номер задачи в списке/Имя задачи в списке" - "Дополнение к описанию задачи": добавляет к описанию задачи новые пункты
                    4) /updateTaskName "Номер задачи в списке/Имя задачи в списке" - "Новое имя задачи": изменяет имя задачи на указанное пользователем
                    5) /updateTime "Номер задачи в списке/Имя задачи в списке" - "Новое время": изменяет время в задаче на указанное пользователем
                    6) /setNewTaskDescription "Номер задачи в списке" - "Новое описание задачи": полностью меняет описание задачи на указанное пользователем
                    7) /find "Номер задачи в списке/Имя задачи в списке" (позже допилю еще поиск по названию)
                    8) /clear - аналогично нажатию на кнопку Очистить список полностью очищает текущий список задач""";
        }
        else if(text.contains("/addTask") && text.contains("/addTaskTime") && text.contains("/addTaskDescription")) {
            text = text.replace("/addTask ", "").replace("/addTaskTime ", "")
                    .replace("/addTaskDescription ", "");
            String[] parts = text.split("\n", 3);
            taskObject = new TaskStruct(parts[0], parts[2], parts[1], String.valueOf(chatID));
            chatTaskObjectMap.put(chatID, taskObject);
            chatTaskStructsMap.computeIfAbsent(chatID, k -> new ArrayList<>()).add(taskObject);
            response = "Задача добавлена в список";
        }
        else if (text.startsWith("/addTask")) {
            String[] parts = text.split(" ", 2);
            taskObject = new TaskStruct(parts[1], String.valueOf(chatID));
            chatTaskObjectMap.put(chatID, taskObject);
            chatTaskStructsMap.computeIfAbsent(chatID, k -> new ArrayList<>()).add(taskObject);
            response = "Запись добавлена в список";
        }
        else if (text.equals("Показать краткий список")) {
            response = "Список текущих дел\n";
            List<TaskStruct> taskStructsList = chatTaskStructsMap.get(chatID);
            if (taskStructsList != null && !taskStructsList.isEmpty())
            {
                int i = 1;
                for (TaskStruct task : taskStructsList)
                {
                    response += ("№" + i + ": " + task.getNameTask() + "\n");
                    i++;
                }
            }
            else {
                response = "Массив пуст";
            }
        }
        else if (text.contains("/find")) {
            TaskStruct task = findTask(text, chatID);
            response = ("Имя задачи: " + task.getNameTask() + "\nОписание задачи: "
                    + task.getTaskDescription() + "\nУстановленное время: " + task.getTime() + "\nНомер задачи: " + task.getId() + "\n\n");
            if (task == null){
                response = "Искомая запись не найдена";
            }
        }
        else if (text.equals("Показать подробный список")) {
            response = "Список текущих дел\n\n";
            List<TaskStruct> taskStructsList = chatTaskStructsMap.get(chatID);
            if (taskStructsList != null && !taskStructsList.isEmpty())
            {
                int i = 1;
                for (TaskStruct task : taskStructsList)
                {
                    response += ("№" + i + "\n" + "Имя задачи: " + task.getNameTask() + "\nОписание задачи: "
                            + task.getTaskDescription() + "\nУстановленное время: " + task.getTime() + "\n\n");
                    i++;
                }
            }
            else {
                response = "Массив пуст";
            }
        }
        else if (text.equals("/clear") || text.equals("Очистить список")) {
            response = "Массив очищен";
            chatTaskStructsMap.remove(chatID);
        }
        else if (text.contains("/update")) {
            response = "Объект не найден";
            TaskStruct task = null;
            String name = text.split("-")[0].split(" ", 2)[1].trim();
            List<TaskStruct> currTask = chatTaskStructsMap.get(chatID);
            if (StringUtils.isNumeric(name))
            {
                for (TaskStruct taskStruct : currTask) {
                    if (String.valueOf(taskStruct.getId()).equals(name)) {
                        task = taskStruct;
                        break;
                    }
                }
            }
            else {
                for (TaskStruct taskStruct : currTask) {
                    if (taskStruct.getNameTask().equals(name)) {
                        task = taskStruct;
                        break;
                    }
                }
            }
            if (task != null) {
                updateTask(text, task);
                response = "Описание обновлено";
            }

        } else {
            response = "Сообщение не распознано";
        }
        return response;
    }

    private void updateTask(String text, TaskStruct task) {
        String switch_text = text.split(" ", 2)[0], taskUpdate;
        switch (switch_text){
            case "/updateTaskDescription":
                taskUpdate = text.replace("/updateTaskDescription ", "")
                        .split(" ", 2)[1]
                        .split("-", 2)[1].trim();
                task.updateTaskDescription(taskUpdate);
                break;
            case "/updateTaskName":
                taskUpdate = text.replace("/updateTaskName ", "")
                        .split(" ", 2)[1]
                        .split("-", 2)[1].trim();
                task.updateTaskName(taskUpdate);
                break;
            case "/updateTime":
                taskUpdate = text.replace("/updateTime ", "")
                        .split(" ", 2)[1]
                        .split("-", 2)[1].trim();
                task.updateTime(taskUpdate);
                break;
            case "/setNewTaskDescription":
                taskUpdate = text.replace("/setNewTaskDescription ", "")
                        .split(" ", 2)[1]
                        .split("-", 2)[1].trim();
                task.setNewTaskDescription(taskUpdate);
                break;
        }
    }
}
