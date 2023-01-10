import com.runflow.engine.ExecutionEntityImpl;
import com.runflow.engine.impl.ProcessEngineConfigurationImpl;
import com.runflow.engine.impl.RunTimeServiceImpl;
import com.runflow.engine.utils.ConditionUtil;
import de.odysseus.el.ExpressionFactoryImpl;
import de.odysseus.el.ObjectValueExpression;
import de.odysseus.el.util.SimpleContext;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.el.ValueExpression;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;


public class ApplicationTest {
    private static final Logger logger = LoggerFactory.getLogger(ApplicationTest.class);

    ProcessEngineConfigurationImpl conf = new ProcessEngineConfigurationImpl();

    RunTimeServiceImpl repositoryService;

    {
        conf.init();
        repositoryService = conf.getRunTimeService();
        conf.addPath("/bpmn/leave.bpmn")
                .addPath("/bpmn/parallelLeave.bpmn")
                .addPath("/bpmn/ParallelGatewayTest.bpmn")
                .addPath("/bpmn/diagram.bpmn")
                .addPath("/bpmn/t.bpmn") ;
    }


    AtomicInteger integer = new AtomicInteger(0);


    public void incrementAndGet() throws InterruptedException {
        Thread.sleep(200);
        integer.incrementAndGet();
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
        Map<String, Object> variableInstances = leave.getVariableInstances();
        System.out.println(variableInstances.get("exclusivegateway1"));
        System.out.println(variableInstances.get("reapply"));
        System.out.println(variableInstances.get("hrapprove"));

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


        AtomicInteger a = new AtomicInteger(0);
        AtomicInteger b = new AtomicInteger(0);
        AtomicInteger c = new AtomicInteger(0);
        AtomicInteger d = new AtomicInteger(0);
        AtomicInteger e = new AtomicInteger(0);
        for (int i = 0; i < 200; i++) {
            Thread thread = new Thread(() -> {
                for (int j = 0; j < 500; j++) {
                    ExpressionFactoryImpl factory = new ExpressionFactoryImpl();
                    SimpleContext elContext = new SimpleContext();
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

    /**
     * 单线程
     *
     * @throws FileNotFoundException
     * @throws InterruptedException
     */
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
        for (int i = 0; i < 200; i++) {
            Thread thread = new Thread(() -> {
                for (int j = 0; j < 500; j++) {
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
    public void t1() throws FileNotFoundException, InterruptedException {

        Map map = new HashMap();
        map.put("a", this);
        map.put("b", this);
        map.put("c", this);
        map.put("d", this);
        map.put("e", this);

        long start = System.currentTimeMillis();
        for (int j = 0; j < 5; j++) {
            ExecutionEntityImpl leave = repositoryService.startWorkflow("Process_1671936597549", map);
        }
        long end = System.currentTimeMillis();
        System.out.println(end - start);
        System.out.println(integer.get());

    }

    /**
     * 多线程
     *
     * @throws FileNotFoundException
     * @throws InterruptedException
     */
    @Test
    public void ParallelGatewayTest() throws FileNotFoundException, InterruptedException {

        RunTimeServiceImpl repositoryService = conf.getRunTimeService();
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
        for (int i = 0; i < 200; i++) {
            Thread thread = new Thread(() -> {
                for (int j = 0; j < 500; j++) {
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
    public void ParallelGatewayTest1() throws FileNotFoundException, InterruptedException {
        RunTimeServiceImpl repositoryService = conf.getRunTimeService();
        Map map = new HashMap();
        map.put("a", this);
        map.put("b", this);
        map.put("c", this);
        map.put("d", this);
        map.put("e", this);
        long start = System.currentTimeMillis();
        for (int j = 0; j < 5; j++) {
            ExecutionEntityImpl leave = repositoryService.startWorkflow("ParallelGatewayTest01", map);
        }


        long end = System.currentTimeMillis();
        System.out.println(end - start);
        System.out.println(integer.get());

    }



    /**
     * 生成图片
     * @throws FileNotFoundException
     * @throws InterruptedException
     */
    @Test
    public void generaImages() throws FileNotFoundException, InterruptedException {
        String fileName = "diagram.bpmn";
        InputStream inputStream = conf.getRunTimeService().generaImages("Process_1");


    }



}
