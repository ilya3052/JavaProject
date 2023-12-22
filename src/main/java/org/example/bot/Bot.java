package org.example.bot;

import org.example.taskStruct;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.List;

public class Bot extends TelegramLongPollingBot {
    private List<taskStruct> taskStructsList;
    private String chatID;
    private String response;
    private taskStruct taskObject;
    public Bot()
    {
        taskStructsList = new ArrayList<>();
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
                chatID = message.getChatId().toString();
                response = parseMessage(message.getText());
                SendMessage sendMessage = new SendMessage();
                sendMessage.setChatId(chatID);
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
        keyboardRow.add(new KeyboardButton("Показать список"));
        keyboardRow.add(new KeyboardButton("Очистить список"));
        replyKeyboardMarkup.setKeyboard(keyboardRows);
    }
    public String parseMessage(String text) {
        String response;
        if (text.equals("/start")) {
            response = "Тестовое сообщение бота (изменить)";
        }
        else if(text.contains("/addTask") && text.contains("/addTaskTime") && text.contains("/addTaskDescription")) {
            text = text.replace("/addTask ", "").replace("/addTaskTime ", "")
                    .replace("/addTaskDescription ", "");
            String[] parts = text.split("\n", 3);
            taskObject = new taskStruct(parts[0], parts[2], parts[1]);
            taskStructsList.add(taskObject);
            response = "Задача добавлена в список";
        }
        else if (text.startsWith("/addTask")) {
            String[] parts = text.split(" ", 2);
            taskObject = new taskStruct(parts[1]);
            taskStructsList.add(taskObject);
            response = "Запись добавлена в список";
        }
        else if (text.equals("/show") || text.equals("Показать список"))
        {
            response = "Список текущих дел\n";
            if (!taskStructsList.isEmpty())
            {
                int i = 1;
                for (taskStruct task : taskStructsList)
                {
                    response += ("Номер задачи в списке " + i + "\nИмя задачи: " + task.getNameTask()
                    + "\nОписание задачи: " + task.getTaskDescription() + "\nУстановленное время: "
                    + task.getTime() + "\n\n");
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
            taskStructsList.clear();
        }
        else {
            response = "Сообщение не распознано";
        }
        return response;
    }
}
