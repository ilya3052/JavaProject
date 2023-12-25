package org.example;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.annotation.*;

import java.io.File;
import java.io.IOException;

public class TaskParser {

    public void parseToJSON(long chatId, TaskStruct chatTaskObj)  {
        // Создание объекта ObjectMapper
        ObjectMapper objectMapper = new ObjectMapper();

        // Создание объекта JSON
        ObjectNode jsonNode = objectMapper.createObjectNode();
        jsonNode.put("Название задачи", chatTaskObj.getNameTask());
        jsonNode.put("Время", chatTaskObj.getTime());
        jsonNode.put("Описание задачи", chatTaskObj.getTaskDescription());
        jsonNode.put("Идентификатор задачи", chatTaskObj.getId());

        // Запись JSON в файл
        try {
            String nameJSONFile = chatId + ".json";
            objectMapper.writeValue(new File(nameJSONFile), jsonNode);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Чтение JSON из файла
        try {
            JsonNode readNode = objectMapper.readTree(new File(chatId + ".json"));
            System.out.println("Итоговый JSON файл: " + readNode);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
