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

public class Bot extends TelegramLongPollingBot {
    public Bot()
    {
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
                String chatID = message.getChatId().toString();
                String response = parseMessage(message.getText());
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
        keyboardRow.add(new KeyboardButton("Просвяти"));
        keyboardRow.add(new KeyboardButton("Нажми на меня"));
        replyKeyboardMarkup.setKeyboard(keyboardRows);
    }
    private String parseMessage(String text) {
        String response;
        if (text.equals("/start")) {
            response = "Тестовое сообщение бота";
        }
        else if (text.equals("Просвяти")) {
            response = "Ты не достоин просвящения!";
        } else if (text.equals("Нажми на меня")) {
            response = "Ну и зачем ты на меня нажал ебалдуй?";
        } else {
            response = "Сообщение не распознано";
        }
        return response;
    }
}
