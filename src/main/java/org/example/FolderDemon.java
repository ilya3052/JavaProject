package org.example;

import java.io.File;
import java.io.IOException;
import java.util.*;
import org.apache.commons.io.FileUtils;
public class FolderDemon {
    public void clearDir(long chatId) {
        String dirName = "Tasks/" + Long.toString(chatId);
        try {
            FileUtils.cleanDirectory(new File(dirName));
            System.out.println("Директория " + dirName + " успешно удалена");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public void changeUserFolder(long oldChatId, long newChatId) {
      if (oldChatId != newChatId) {}
  }
}
