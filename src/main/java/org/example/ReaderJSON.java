package org.example;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.annotation.*;
import org.apache.commons.io.FileUtils;

import java.io.IOException;
import java.io.File;

import java.util.*;

public class ReaderJSON {
  public List<TaskStruct> jsonFileToPojo(File rootFolder) {
    File[] folderEntries = rootFolder.listFiles();
    List<TaskStruct> oldObjMap = new ArrayList<>();
      for (File entry : folderEntries)
      {
        try {
        System.out.println(entry.getName());
        ObjectMapper objectMapper = new ObjectMapper();
        TaskStruct task = new TaskStruct(null, null, null, null);
        JsonNode rootNode = objectMapper.readTree(entry);
        task.updateTaskName(rootNode.get("Название задачи").asText());
        task.updateTime(rootNode.get("Время").asText());
        task.updateTaskDescription(rootNode.get("Описание задачи").asText());
        task.setId(Integer.parseInt(rootNode.get("Идентификатор задачи").asText()));
        oldObjMap.add(task);
        } catch (IOException e) {
          e.printStackTrace();
          System.out.println("Нет файлов или ошибка чтения. alert()\n");
        }
    }
    return oldObjMap;
  }
  public Map<Long, List<TaskStruct>> deserialize(File rootFolder){
    Map<Long, List<TaskStruct>> oldStructsMap = new HashMap();
    File[] folders = rootFolder.listFiles();
    for (File folder : folders) { 
      if (folder.exists() && folder.isDirectory()) {
        oldStructsMap.put(Long.parseLong(folder.getName()), jsonFileToPojo(folder));
      }
    }
    return oldStructsMap;
  }
}
