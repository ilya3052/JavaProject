package org.example;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.annotation.*;
import org.apache.commons.io.FileUtils;

import java.io.IOException;
import java.util.*;
import java.io.File;

public class TaskParser {

    public void parseToJSON(long chatId, TaskStruct chatTaskObj)  {
        String dirName = "Tasks/" + Long.toString(chatId);
        File dir = new File(dirName);
        if (!dir.exists()) dir.mkdir();
        
        // Создание объекта ObjectMapper
        ObjectMapper objectMapper = new ObjectMapper();

        // Создание объекта JSON
        ObjectNode jsonNode = objectMapper.createObjectNode();
        jsonNode.put("Название задачи", chatTaskObj.getNameTask());
        jsonNode.put("Время", chatTaskObj.getTime());
        jsonNode.put("Описание задачи", chatTaskObj.getTaskDescription());
        jsonNode.put("Идентификатор задачи", chatTaskObj.getId());
        String nameJSONFile = dirName + "/" + chatTaskObj.getNameTask() + ".json";
        // Запись JSON в файл
        try {
            objectMapper.writeValue(new File(nameJSONFile), jsonNode);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Чтение JSON из файла для проверки правильного создания .json файла
        try {
            JsonNode readNode = objectMapper.readTree(new File(nameJSONFile));
            System.out.println("Итоговый JSON файл: " + readNode);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
