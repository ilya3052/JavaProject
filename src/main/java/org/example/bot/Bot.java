package org.example.bot;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class Bot extends TelegramLongPollingBot {
    public Bot()
    {
        initKeyboard();
    }

    private ReplyKeyboardMarkup replyKeyboardMarkup;
    private static Scanner scanner;
    private static long time;
    String chatID, response;
    private static Timer timer = new Timer();
    TimerTask timerTask = new TimerTask() {
        @Override
        public void run() {
            try {
                sendMessage("Время пришло!");
            } catch (TelegramApiException e) {
                throw new RuntimeException(e);
            }
        }
    };
    private void startTimer (String text) throws TelegramApiException, ParseException {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm");
        time = simpleDateFormat.parse(getTime(text)).getTime();
        timer = new Timer(text);
        timer.schedule(timerTask, time - new Date().getTime());
    }

    public static String getTime(String text) {
        text = text.replace("поставь напоминание на ", "");
        return text;
    }

    public void sendMessage(String text) throws TelegramApiException {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setText(text);
        sendMessage.setChatId(chatID);
        execute(sendMessage);
    }

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
        } catch (ParseException e) {
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
    private String parseMessage(String text) throws TelegramApiException, ParseException {
        String response;
        if (text.equals("/start")) {
            response = "Тестовое сообщение бота";
        }
        else if (text.equals("просвяти")) {
            response = "Ты не достоин просвящения!";
        } else if (text.equals("нажми на меня")) {
            response = "Ну и зачем ты на меня нажал ебалдуй?";
        }
        else if (text.contains("поставь напоминание на"))
        {
            startTimer(text);
            response = "Напоминание установлено!";
        }
        else if (text.equals("время"))
        {
            Date date = new Date();
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss");
            response = "Текущее время: " + simpleDateFormat.format(date);
        }
        else {
            response = "Сообщение не распознано";
        }
        return response;
    }
}
