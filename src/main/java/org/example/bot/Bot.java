package org.example.bot;

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
    private List<String> stringList;
    private static String chatID;
    private static String response;
    public Bot()
    {
        stringList = new ArrayList<>();
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
                response = parseMessage(message.getText().toLowerCase());
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
    void initKeyboard()
    {
        replyKeyboardMarkup = new ReplyKeyboardMarkup();
        replyKeyboardMarkup.setResizeKeyboard(true);
        ArrayList<KeyboardRow> keyboardRows = new ArrayList<>();
        KeyboardRow keyboardRow = new KeyboardRow();
        keyboardRows.add(keyboardRow);
        keyboardRow.add(new KeyboardButton("Показать список"));
        keyboardRow.add(new KeyboardButton("Очистить список"));
        replyKeyboardMarkup.setKeyboard(keyboardRows);
    }
    private void sendMessage()
    {
        SendMessage message = new SendMessage();
        message.setChatId(chatID);
        message.setText("Введите ");
    }
    private String parseMessage(String text) {
        String response;
        if (text.equals("/start")) {
            response = "Тестовое сообщение бота";
        }
        else if (text.startsWith("/add")) {
            String[] parts = text.split(" ", 2);
            stringList.add(parts[1]);
            response = "Запись добавлена в массив";
        }
        else if (text.equals("/show") || text.equals("показать список"))
        {
            response = "Список текущих дел\n";
            if (!stringList.isEmpty())
            {
                int i = 1;
                for (String string : stringList)
                {
                    response += "Запись № " + i + " " + string + "\n" + "---------------------------------------" + "\n";
                    i++;
                }
            }
            else {
                response = "Массив пуст";
            }
        }
        else if (text.equals("/delete") || text.equals("очистить список"))
        {
            response = "Массив очищен";
            stringList.clear();
        }
        else {
            response = "Сообщение не распознано";
        }
        return response;
    }
}
