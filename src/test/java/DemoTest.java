import com.runflow.engine.ExecutionEntityImpl;
import com.runflow.engine.impl.ProcessEngineConfigurationImpl;
import com.runflow.engine.impl.RunTimeServiceImpl;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class DemoTest {


    ProcessEngineConfigurationImpl conf = new ProcessEngineConfigurationImpl();
    RunTimeServiceImpl runTimeService;

    {
        //初始化
        conf.init();
        runTimeService = conf.getRunTimeService();
        conf.addPath("/bpmn/leave.bpmn")
                .addPath("/bpmn/parallelLeave.bpmn")
                .addPath("/bpmn/ParallelGatewayTest.bpmn")
                .addPath("/bpmn/diagram.bpmn")
                .addPath("/bpmn/t.bpmn");
        //bpmn位置
        conf.addPath("/bpmn/demo1.bpmn");
    }


    @Test
    public void demo1() {
        //a.incrementAndGet()
        AtomicInteger integer = new AtomicInteger(0);
        Map map = new HashMap();
        map.put("a", integer);
        ExecutionEntityImpl executionEntity = runTimeService.startWorkflow("Process_1671936597549", map);
        System.out.println("integer:"+integer);
    }

}
