package Service;

import exception.CodeKataException;
import model.MasterMap;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class FileWriterService {

  public StringBuilder buildFileFromMap(MasterMap masterMap, String dstFilePath, Integer fileWordLength) throws CodeKataException {


    BufferedWriter bookWriter = null;
    try {
      HashMap<String, ArrayList<String>> keyValuePair = masterMap.keyValuePair;
      StringBuilder newLine = new StringBuilder();
      String startingPoint=null;
      int currentWordCount=0;
      while (currentWordCount<fileWordLength) {
        if (startingPoint==null || !keyValuePair.containsKey(startingPoint))
        {
          startingPoint = masterMap.getRandomKey();
          newLine.append("\n\n").append(startingPoint);
        }
        String nextWord = masterMap.nextWord(startingPoint);
        newLine.append(" ").append(nextWord);
        currentWordCount+=3;
        String firstWord = startingPoint.split("\\s+")[1];
        startingPoint = firstWord + " " + nextWord;
      }
      System.out.println(newLine);
      File dstFile = new File(dstFilePath);
      bookWriter = new BufferedWriter(new FileWriter(dstFile));
      bookWriter.append(newLine);
      return newLine;
    } catch (Exception e) {
      throw new CodeKataException(e.getLocalizedMessage(), e.getCause());
    } finally {
      if (bookWriter != null) {
        try {
          bookWriter.close();
        } catch (IOException e) {
          throw new CodeKataException(e.getLocalizedMessage(), e.getCause());
        }
      }
    }
  }
}
