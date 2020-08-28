import java.time.Instant;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.*;

import Service.FileReaderService;
import Service.MapCreationService;
import exception.CodeKataException;
import Service.FileWriterService;
import model.MasterMap;
import org.apache.commons.lang3.StringUtils;

public class CodeKataApplication {

  public static void main(String[] args) {
    try {
      if (args.length!=1 || StringUtils.isEmpty(args[0])) return;
      MasterMap masterMap;
      final String srcFile = args[0];
      String dstFile = args[0].substring(0, args[0].indexOf('.'))+ "-" + Instant.now().getEpochSecond() + ".txt";
      Integer fileWordLength=0;
      ArrayBlockingQueue<String> lineQueue=new ArrayBlockingQueue<String>(100000);

      System.out.println("Reading line by line from file and putting to queue. Building map from the queue");
      List <Object> returnList = new CodeKataApplication().buildMapFromFile(srcFile, 100000, lineQueue);
      fileWordLength=(Integer) returnList.get(0);
      masterMap=(MasterMap) returnList.get(1);
      System.out.println("Map building completed");

      System.out.println("Writing file from the map started");
      new FileWriterService().buildFileFromMap(masterMap, dstFile, fileWordLength);
      System.out.println("Writing file from the map completed");
    }
    catch (CodeKataException e) {
      e.printStackTrace();
    }
  }



  public List<Object> buildMapFromFile(String filePath, int readerQueueCapacity, ArrayBlockingQueue<String> lineQueue)
          throws CodeKataException {
    ExecutorService executorService = Executors.newFixedThreadPool(2);
    CountDownLatch latch = new CountDownLatch(2);
    MasterMap result;
    Integer fileWordLength;
    FileReaderService fileReaderService = new FileReaderService(lineQueue, filePath, latch);
    MapCreationService mapCreationService = new MapCreationService(lineQueue,latch);

    Future<Integer> fileReaderResult = executorService.submit(fileReaderService);
    Future<MasterMap> masterMap = executorService.submit(mapCreationService);

    try {
      latch.await(30, TimeUnit.SECONDS);
      fileWordLength = fileReaderResult.get();
      result=masterMap.get();
      System.out.println(fileWordLength + " processed from " + filePath);
    } catch (Exception e) {
      throw new CodeKataException(e.getLocalizedMessage(), e.getCause());
    } finally {
      executorService.shutdown();
    }
    return Arrays.asList(fileWordLength, result);
  }
}
