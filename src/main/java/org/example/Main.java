package org.example;

import java.io.File;
import org.example.bot.Bot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

public class Main {
    public static void main(String[] args) {
        try {
            String taskDirName = "Tasks";
            File dir = new File(taskDirName);
            if (!dir.exists()) dir.mkdir();
            TelegramBotsApi telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);
            telegramBotsApi.registerBot(new Bot());
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }
}
