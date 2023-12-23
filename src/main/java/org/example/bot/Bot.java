package org.example.bot;

import org.checkerframework.checker.units.qual.K;
import org.example.TaskStruct;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        KeyboardRow keyboardRow = new KeyboardRow();
        keyboardRows.add(keyboardRow);
        keyboardRow.add(new KeyboardButton("Показать краткий список"));
        keyboardRow.add(new KeyboardButton("Показать подробный список"));
        keyboardRow.add(new KeyboardButton("Очистить список"));
        replyKeyboardMarkup.setKeyboard(keyboardRows);
    }
    public String parseMessage(String text, long chatID) {
        String response;
        if (text.equals("/start")) {
            response = "Тестовое сообщение бота (изменить)";
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
        else if (text.equals("Показать краткий список"))
        {
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
        else if (text.equals("Показать подробный список"))
        {
            response = "Список текущих дел\n";
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
        else if (text.equals("/delete") || text.equals("Очистить список"))
        {
            response = "Массив очищен";
            chatTaskStructsMap.remove(chatID);
        }
        else {
            response = "Сообщение не распознано";
        }
        return response;
    }
}
