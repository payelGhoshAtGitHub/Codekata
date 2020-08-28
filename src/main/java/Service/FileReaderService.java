package Service;
import exception.CodeKataException;
import java.io.*;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;

public class FileReaderService implements Callable<Integer> {

	private BlockingQueue<String> lineQueue;
	private File file;
	private CountDownLatch latch;

	public FileReaderService(BlockingQueue<String> queue, String filePath,
                           CountDownLatch latch) throws CodeKataException {
		this.lineQueue = queue;
		this.latch = latch;
		file = new File(filePath);
		if (lineQueue == null || latch == null || !file.exists())
			throw new CodeKataException("FileReaderService initialization failed! Please check the file path provided");
	}

	@Override
	public Integer call() throws CodeKataException {
		Integer lineCount = 0;
		BufferedReader fileReader = null;
		String line;
		try {
			fileReader = new BufferedReader(new java.io.FileReader(file));
			while (!Thread.currentThread().isInterrupted() && (line = fileReader.readLine()) != null) {
				lineCount += 1;
				lineQueue.put(line);
			}
      if (Thread.interrupted()) {
        throw new InterruptedException();
      }
		} catch (Exception e) {
      throw new CodeKataException("Error in reading file", e);
    } finally {
			if (fileReader != null) {
				try {
					fileReader.close();
				} catch (IOException e) {
          System.out.println("Error in closing file reader");
				}
			}
			latch.countDown();
		}
		return lineCount;
	}

	public void cancelReading() {
		Thread.currentThread().interrupt();
	}
}
