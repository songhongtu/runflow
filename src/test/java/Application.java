import com.runflow.engine.ExecutionEntityImpl;
import com.runflow.engine.impl.ProcessEngineConfigurationImpl;
import com.runflow.engine.impl.ProcessEngineImpl;
import com.runflow.engine.impl.RunTimeServiceImpl;
import com.runflow.engine.impl.agenda.TakeOutgoingSequenceFlowsOperation;
import com.runflow.engine.utils.ConditionUtil;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;


public class Application {
    private static final Logger logger = LoggerFactory.getLogger(Application.class);

    ProcessEngineConfigurationImpl conf = new ProcessEngineConfigurationImpl();
    ProcessEngineImpl processEngine = conf.buildProcessEngine();


    @Test
    public void executeLeave() throws FileNotFoundException {
        String fileName = "purchase.bpmn";
        FileInputStream fileInputStream = new FileInputStream("C:\\Users\\songhongtu\\Desktop\\leave.bpmn");
        RunTimeServiceImpl repositoryService = conf.getRepositoryService();
        repositoryService.createDeployment().name(fileName).addInputStream(fileName, fileInputStream).deploy();
        Map<String, Object> map = new HashMap();
        map.put("conditionUtil", new ConditionUtil());
        map.put("exclusivegateway1", true);
        map.put("deptleaderapprove", true);
        map.put("hrapprove", false);
        map.put("reapply", true);
        ExecutionEntityImpl leave = repositoryService.startWorkflow("leave", map);
    }


    @Test
    public void executeParallelLeave() throws FileNotFoundException, InterruptedException {
        String fileName = "parallelLeave.bpmn";
        FileInputStream fileInputStream = new FileInputStream("C:\\Users\\songhongtu\\Desktop\\parallelLeave.bpmn");
        RunTimeServiceImpl repositoryService = conf.getRepositoryService();
        repositoryService.createDeployment().name(fileName).addInputStream(fileName, fileInputStream).deploy();
        ExecutionEntityImpl leave = repositoryService.startWorkflow("parallelLeave");


    }


    @Test
    public void ParallelGatewayTest() throws FileNotFoundException, InterruptedException {

        String fileName = "ParallelGatewayTest.bpmn";
        FileInputStream fileInputStream = new FileInputStream("C:\\Users\\songhongtu\\Desktop\\ParallelGatewayTest.bpmn");
        RunTimeServiceImpl repositoryService = conf.getRepositoryService();
        repositoryService.createDeployment().name(fileName).addInputStream(fileName, fileInputStream).deploy();

        Long b = 0L;
        while (true) {
            Thread thread = new Thread(() -> {
                ExecutionEntityImpl leave = repositoryService.startWorkflow("ParallelGatewayTest01");
            });
            thread.start();
            b++;
            logger.info("数量：{}", b);
            logger.info("线程池状态:{}", conf.getExecutorService().getPoolSize());
            Thread.sleep((long) (100 * Math.random()));
        }


//        ExecutionEntityImpl leave = repositoryService.startWorkflow("ParallelGatewayTest01");
    }

    @Test
    public void diagram() throws FileNotFoundException, InterruptedException {
        try {
            ParallelGatewayTest();
        } catch (Exception e) {
            e.printStackTrace();
        }
        String fileName = "diagram.bpmn";
        FileInputStream fileInputStream = new FileInputStream("C:\\Users\\songhongtu\\Desktop\\" + fileName);
        RunTimeServiceImpl repositoryService = conf.getRepositoryService();
        repositoryService.createDeployment().name(fileName).addInputStream(fileName, fileInputStream).deploy();
        ExecutionEntityImpl leave = repositoryService.startWorkflow("Process_1");

    }


}
