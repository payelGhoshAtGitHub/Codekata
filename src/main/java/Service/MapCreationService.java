package Service;

import model.MasterMap;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;

public class MapCreationService implements Callable {

    private BlockingQueue<String> lines;
    private MasterMap masterMap;
    private CountDownLatch latch;

    public MapCreationService(BlockingQueue<String> lines, CountDownLatch latch)
    {
        this.masterMap =new MasterMap();
        this.lines=lines;
        this.latch=latch;
    }
    @Override
    public Object call()
    {
        while(!lines.isEmpty()|| latch.getCount() > 1)
        {
            String line=lines.poll();
            if (StringUtils.isEmpty(line)) continue;
            String[]  words = line.split("\\s+");
            //Arrays.stream(words).forEach(s -> System.out.println(s));
            for(int i=0;i<words.length-2;i++)
            {
                String key=words[i]+" "+words[i+1];
                String value=words[i+2];
                masterMap.addMapping(key,value);
            }
        }
        latch.countDown();
        return masterMap;
    }
}
