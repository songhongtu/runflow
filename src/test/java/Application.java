import com.runflow.engine.ExecutionEntityImpl;
import com.runflow.engine.impl.ProcessEngineConfigurationImpl;
import com.runflow.engine.impl.ProcessEngineImpl;
import com.runflow.engine.impl.RunTimeServiceImpl;
import com.runflow.engine.impl.agenda.TakeOutgoingSequenceFlowsOperation;
import com.runflow.engine.utils.ConditionUtil;
import de.odysseus.el.ExpressionFactoryImpl;
import de.odysseus.el.ObjectValueExpression;
import de.odysseus.el.util.SimpleContext;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.el.ValueExpression;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.atomic.AtomicInteger;


public class Application {
    private static final Logger logger = LoggerFactory.getLogger(Application.class);

    ProcessEngineConfigurationImpl conf = new ProcessEngineConfigurationImpl();

    RunTimeServiceImpl repositoryService = conf.getRunTimeService();

    {
        conf.init();
        String path = (System.getProperty("user.dir") + "\\src\\test\\resources\\");//user.dir指定了当前的路径
        File file = new File(path + "/bpmn");
        for (File f : file.listFiles()) {
            try {
                repositoryService.createDeployment().name(f.getName()).addInputStream(f.getName(), new FileInputStream(f)).deploy();
            } catch (Exception e) {
                e.printStackTrace();
                logger.error("文件：{}部署错误，错误日志{}", f.getPath(),e.getMessage());
            }
        }


    }


    @Test
    public void executeLeave() throws FileNotFoundException {
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
        RunTimeServiceImpl repositoryService = conf.getRunTimeService();
        repositoryService.createDeployment().name(fileName).addInputStream(fileName, fileInputStream).deploy();
        ExecutionEntityImpl leave = repositoryService.startWorkflow("parallelLeave");


    }

    @Test
    public void testCondition() throws InterruptedException {
        ExpressionFactoryImpl factory = new ExpressionFactoryImpl();
        SimpleContext elContext = new SimpleContext();
        AtomicInteger a = new AtomicInteger(0);
        AtomicInteger b = new AtomicInteger(0);
        AtomicInteger c = new AtomicInteger(0);
        AtomicInteger d = new AtomicInteger(0);
        AtomicInteger e = new AtomicInteger(0);
        for (int i = 0; i < 500; i++) {
            Thread thread = new Thread(() -> {
                for (int j = 0; j < 50; j++) {
                    Map map = new HashMap();
                    map.put("a", a);
                    map.put("b", b);
                    map.put("c", c);
                    map.put("d", d);
                    map.put("e", e);
                    ObjectValueExpression valueExpression = factory.createValueExpression(map, Object.class);
                    elContext.setVariable("map", valueExpression);
                    ValueExpression expression1 = factory.createValueExpression(elContext, "${map.a.incrementAndGet()}", Object.class);
                    ValueExpression expression2 = factory.createValueExpression(elContext, "${map.b.incrementAndGet()}", Object.class);
                    ValueExpression expression3 = factory.createValueExpression(elContext, "${map.c.incrementAndGet()}", Object.class);
                    ValueExpression expression4 = factory.createValueExpression(elContext, "${map.d.incrementAndGet()}", Object.class);
                    ValueExpression expression5 = factory.createValueExpression(elContext, "${map.e.incrementAndGet()}", Object.class);
                    expression1.getValue(elContext);
                    expression2.getValue(elContext);
                    expression3.getValue(elContext);
                    expression4.getValue(elContext);
                    expression5.getValue(elContext);
                }
            });
            thread.start();
        }
        while (true) {
            Thread.sleep(1000 * 3);
            System.out.println("a:" + a);
            System.out.println("b:" + b);
            System.out.println("c:" + c);
            System.out.println("d:" + d);
            System.out.println("e:" + e);
        }
    }


    @Test
    public void t() throws FileNotFoundException, InterruptedException {

        AtomicInteger a = new AtomicInteger(0);
        AtomicInteger b = new AtomicInteger(0);

        AtomicInteger c = new AtomicInteger(0);
        AtomicInteger d = new AtomicInteger(0);
        AtomicInteger e = new AtomicInteger(0);
        Map map = new HashMap();
        map.put("a", a);
        map.put("b", b);
        map.put("c", c);
        map.put("d", d);
        map.put("e", e);
        for (int i = 0; i < 500; i++) {
            Thread thread = new Thread(() -> {
                for (int j = 0; j < 5000; j++) {
                    ExecutionEntityImpl leave = repositoryService.startWorkflow("Process_1671936597549", map);
                }

            });
            thread.start();
        }


        while (true) {
            Thread.sleep(1000 * 3);
            System.out.println("a:" + a);
            System.out.println("b:" + b);
            System.out.println("c:" + c);
            System.out.println("d:" + d);
            System.out.println("e:" + e);


        }


    }


    @Test
    public void ParallelGatewayTest() throws FileNotFoundException, InterruptedException {

        String fileName = "ParallelGatewayTest.bpmn";
        FileInputStream fileInputStream = new FileInputStream("C:\\Users\\songhongtu\\Desktop\\ParallelGatewayTest.bpmn");
        RunTimeServiceImpl repositoryService = conf.getRunTimeService();
        repositoryService.createDeployment().name(fileName).addInputStream(fileName, fileInputStream).deploy();
        AtomicInteger a = new AtomicInteger(0);
        AtomicInteger b = new AtomicInteger(0);

        AtomicInteger c = new AtomicInteger(0);
        AtomicInteger d = new AtomicInteger(0);
        AtomicInteger e = new AtomicInteger(0);
        Map map = new HashMap();
        map.put("a", a);
        map.put("b", b);
        map.put("c", c);
        map.put("d", d);
        map.put("e", e);
        for (int i = 0; i < 500; i++) {
            Thread thread = new Thread(() -> {
                for (int j = 0; j < 200; j++) {
                    ExecutionEntityImpl leave = repositoryService.startWorkflow("ParallelGatewayTest01", map);
                }

            });
            thread.start();
        }


        while (true) {
            Thread.sleep(1000 * 3);
            System.out.println("a:" + a);
            System.out.println("b:" + b);
            System.out.println("c:" + c);
            System.out.println("d:" + d);
            System.out.println("e:" + e);


        }


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
        RunTimeServiceImpl repositoryService = conf.getRunTimeService();
        repositoryService.createDeployment().name(fileName).addInputStream(fileName, fileInputStream).deploy();
        ExecutionEntityImpl leave = repositoryService.startWorkflow("Process_1");

    }


    @Test
    public void generater() throws FileNotFoundException, InterruptedException {
        String fileName = "diagram.bpmn";
        FileInputStream fileInputStream = new FileInputStream("C:\\Users\\songhongtu\\Desktop\\" + fileName);
        RunTimeServiceImpl repositoryService = conf.getRunTimeService();
        repositoryService.createDeployment().name(fileName).addInputStream(fileName, fileInputStream).deploy();
        conf.getRunTimeService().generaImages("Process_1");

    }

    @Test
    public void te() throws InterruptedException {

        Thread daemon = new Thread(() -> {
            try {
                Thread.sleep(10000);
                int sum = 0;
                for (int i = 0; i < 100; i++) {
                    sum += i;
                }
                System.out.println(sum);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        // 设置守护线程
        daemon.setDaemon(false);
        daemon.start();
//   daemon.join();/
        System.out.println("主线程结束");
    }


}
