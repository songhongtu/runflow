import com.runflow.engine.impl.ProcessEngineConfigurationImpl;
import com.runflow.engine.impl.RunTimeServiceImpl;

public class InitTest {


    protected static ProcessEngineConfigurationImpl conf = new ProcessEngineConfigurationImpl();

    static RunTimeServiceImpl repositoryService;

    static  {
        conf.init();
        repositoryService = conf.getRunTimeService();
        conf.addPath("/bpmn/leave.bpmn")
                .addPath("/bpmn/parallelLeave.bpmn")
                .addPath("/bpmn/ParallelGatewayTest.bpmn")
                .addPath("/bpmn/diagram.bpmn")
                .addPath("/bpmn/t.bpmn").addPath("/bpmn/demo1.bpmn");
    }


    //线程数量
    protected static int THREADCOUNT = 20;
    //循环数量
    protected static int SECOND = 50000;
    //总数
    protected static int TOTALCOUNT = THREADCOUNT * SECOND;




}
