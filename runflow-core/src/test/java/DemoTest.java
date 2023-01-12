import com.runflow.engine.ExecutionEntityImpl;
import com.runflow.engine.impl.ProcessEngineConfigurationImpl;
import com.runflow.engine.impl.RunTimeServiceImpl;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class DemoTest {



    private static RunTimeServiceImpl repositoryService = InitTest.repositoryService;



    @Test
    public void demo1() {
        AtomicInteger integer = new AtomicInteger(0);
        Map map = new HashMap();
        map.put("a", integer);
        ExecutionEntityImpl executionEntity = repositoryService.startWorkflow("Process_16719365975", map);
        System.out.println("integer:"+integer);
    }

}
