package org.example.bot;

import org.example.TaskStruct;
import org.example.TaskParser;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

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
                if (response == "Задача добавлена в список" || response == "Запись добавлена в список") {
                    TaskParser parser = new TaskParser(); 
                    parser.parseToJSON(chatID, taskObject);

                }
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
        thirdRow.add(new KeyboardButton("Сохранить в файл"));
        keyboardRows.add(firstRow);
        keyboardRows.add(secondRow);
        keyboardRows.add(thirdRow);
        replyKeyboardMarkup.setKeyboard(keyboardRows);
    }

    public TaskStruct findTask(String text, long chatID) {
        String id = text.replace("/find ", "");
        List<TaskStruct> currTask = chatTaskStructsMap.get(chatID);
        for (TaskStruct taskStruct : currTask) {
            if (String.valueOf(taskStruct.getId()).equals(id)) {
                return taskStruct;
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
                    1) /addTask Название задачи - добавляет задачу только с названием
                    2) Связка команд вида
                    /addTask Название задачи
                    /addTaskTime 19:00
                    /addTaskDescription Описание задачи
                    Добавляет в список дел полную информацию о задаче.
                    3) /find идентификатор задачи (позже допилю еще поиск по названию)
                    /clear - аналогично нажатию на кнопку Очистить список полностью очищает текущий список задач""";
        }
        else if(text.contains("/addTask") && text.contains("/addTaskTime") && text.contains("/addTaskDescription")) {
            text = text.replace("/addTask ", "").replace("/addTaskTime ", "")
                    .replace("/addTaskDescription ", "");
            String[] parts = text.split("\n", 3);
            taskObject = new TaskStruct(parts[0], parts[2], parts[1]);
            chatTaskObjectMap.put(chatID, taskObject);
            chatTaskStructsMap.computeIfAbsent(chatID, k -> new ArrayList<>()).add(taskObject);
            response = "Задача добавлена в список";
        }
        else if (text.startsWith("/addTask")) {
            String[] parts = text.split(" ", 2);
            taskObject = new TaskStruct(parts[1]);
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
        else {
            response = "Сообщение не распознано";
        }
        
        return response;
    }
}
