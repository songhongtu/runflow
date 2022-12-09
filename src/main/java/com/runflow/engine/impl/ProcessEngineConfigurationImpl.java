package com.runflow.engine.impl;

import com.runflow.engine.ActivitiEngineAgendaFactory;
import com.runflow.engine.ActivitiException;
import com.runflow.engine.CommandExecutorImpl;
import com.runflow.engine.ExecutionEntityImpl;
import com.runflow.engine.bpmn.deployer.BpmnDeployer;
import com.runflow.engine.bpmn.entity.impl.DefaultDeploymentCache;
import com.runflow.engine.bpmn.entity.impl.ProcessDefinitionCacheEntry;
import com.runflow.engine.cache.SessionFactory;
import com.runflow.engine.cache.impl.CurrentHashMapCache;
import com.runflow.engine.el.ExpressionManager;
import com.runflow.engine.interceptor.*;
import com.runflow.engine.parse.BpmnParseHandler;
import com.runflow.engine.parse.BpmnParseHandlers;
import com.runflow.engine.parse.BpmnParser;
import com.runflow.engine.parse.handler.*;
import de.odysseus.el.util.SimpleContext;
import org.activiti.image.ProcessDiagramGenerator;
import org.activiti.image.impl.DefaultProcessDiagramGenerator;

import javax.el.ELContext;
import javax.el.ExpressionFactory;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

public class ProcessEngineConfigurationImpl {

    protected String processEngineName = "6.0.0";
    public static final String NO_TENANT_ID = "";
    protected BpmnParser bpmnParser;
    private CommandExecutorImpl commandExecutor;
    protected CommandInterceptor commandInvoker;
    protected CommandContextFactory commandContextFactory;
    protected ActivitiEngineAgendaFactory engineAgendaFactory;

    protected CommandConfig defaultCommandConfig;
    protected List<CommandInterceptor> commandInterceptors;

    protected ProcessDiagramGenerator processDiagramGenerator;

    protected ClassLoader classLoader;
    protected BpmnDeployer bpmnDeployer;
    protected RunTimeServiceImpl repositoryService = new RunTimeServiceImpl();
    protected DefaultDeploymentCache<ProcessDefinitionCacheEntry> processDefinitionCache;

    protected  ExpressionManager expressionManager;
    private CurrentHashMapCache<ExecutionEntityImpl> runTimeExecution = new CurrentHashMapCache<>();

    protected ThreadPoolExecutor executorService;



    public ProcessEngineConfigurationImpl() {
//        this.init();

    }


    public ProcessEngineImpl buildProcessEngine() {
        init();
        ProcessEngineImpl processEngine = new ProcessEngineImpl(this);


        return processEngine;
    }


    public void init() {

        initCommandContextFactory();
        initCommandExecutors();
        initCache();
        initServices();
        initBpmnParser();
        initDeployers();
        processDiagramGenerator = new DefaultProcessDiagramGenerator();
        initExpression();

        if (executorService == null) {
            executorService = (ThreadPoolExecutor) Executors.newCachedThreadPool();
        }


    }

    public void initExpression() {
        expressionManager=     new ExpressionManager();

    }

    public void initCache() {
        processDefinitionCache = new DefaultDeploymentCache<>();
    }


    public void initDeployers() {
        if (bpmnDeployer == null) {
            bpmnDeployer = new BpmnDeployer();
        }
    }


    public void initCommandContextFactory() {
        if (commandContextFactory == null) {
            commandContextFactory = new CommandContextFactory();
        }
        commandContextFactory.setProcessEngineConfiguration(this);
    }


    public void initServices() {
        initService(repositoryService);
    }

    public void initService(Object service) {
        if (service instanceof ServiceImpl) {
            ((ServiceImpl) service).setCommandExecutor(commandExecutor);
        }
    }

    public void initBpmnParser() {
        if (bpmnParser == null) {
            bpmnParser = new BpmnParser();
        }
        BpmnParseHandlers bpmnParseHandlers = new BpmnParseHandlers();
        bpmnParseHandlers.addHandlers(getDefaultBpmnParseHandlers());
        bpmnParser.setBpmnParserHandlers(bpmnParseHandlers);
    }

    public void initCommandExecutor() {
        if (commandExecutor == null) {
            CommandInterceptor first = initInterceptorChain(commandInterceptors);
            commandExecutor = new CommandExecutorImpl(defaultCommandConfig, first);
        }
    }

    public void initCommandExecutors() {
        initDefaultCommandConfig();
        initCommandInvoker();
        initCommandInterceptors();
        initCommandExecutor();
    }


    public CommandInterceptor initInterceptorChain(List<CommandInterceptor> chain) {
        if (chain == null || chain.isEmpty()) {
            throw new ActivitiException("invalid command interceptor chain configuration: " + chain);
        }
        for (int i = 0; i < chain.size() - 1; i++) {
            chain.get(i).setNext(chain.get(i + 1));
        }
        return chain.get(0);
    }

    public Collection<? extends CommandInterceptor> getDefaultCommandInterceptors() {
        List<CommandInterceptor> interceptors = new ArrayList<CommandInterceptor>();
        interceptors.add(new LogInterceptor());
        interceptors.add(new CommandContextInterceptor(commandContextFactory, this));

        return interceptors;
    }


    public void initDefaultCommandConfig() {
        defaultCommandConfig = new CommandConfig();
    }

    public void initCommandInvoker() {
        if (commandInvoker == null) {
            commandInvoker = new CommandInvoker();
        }
    }

    public void initCommandInterceptors() {
        if (commandInterceptors == null) {
            commandInterceptors = new ArrayList<CommandInterceptor>();
            commandInterceptors.addAll(this.getDefaultCommandInterceptors());
            commandInterceptors.add(commandInvoker);
        }
    }

    public void initHandlers() {


    }



    public CommandExecutorImpl getCommandExecutor() {
        return commandExecutor;
    }

    public void setCommandExecutor(CommandExecutorImpl commandExecutor) {
        this.commandExecutor = commandExecutor;
    }

    public void setEngineAgendaFactory(ActivitiEngineAgendaFactory engineAgendaFactory) {
        this.engineAgendaFactory = engineAgendaFactory;
    }

    public ActivitiEngineAgendaFactory getEngineAgendaFactory() {
        return engineAgendaFactory;
    }

    public List<BpmnParseHandler> getDefaultBpmnParseHandlers() {
        List<BpmnParseHandler> bpmnParserHandlers = new ArrayList<BpmnParseHandler>();
        bpmnParserHandlers.add(new EndEventParseHandler());
        bpmnParserHandlers.add(new ExclusiveGatewayParseHandler());
        bpmnParserHandlers.add(new StartEventParseHandler());
        bpmnParserHandlers.add(new UserTaskParseHandler());
        bpmnParserHandlers.add(new ProcessParseHandler());
        bpmnParserHandlers.add(new ParallelGatewayParseHandler());
        bpmnParserHandlers.add(new SequenceFlowParseHandler());
        bpmnParserHandlers.add(new CallActivityParseHandler());
        return bpmnParserHandlers;
    }

    public ProcessDiagramGenerator getProcessDiagramGenerator() {
        return this.processDiagramGenerator;
    }

    public ProcessEngineConfigurationImpl setProcessDiagramGenerator(ProcessDiagramGenerator processDiagramGenerator) {
        this.processDiagramGenerator = processDiagramGenerator;
        return this;
    }

    public ClassLoader getClassLoader() {
        return classLoader;
    }

    public ProcessEngineConfigurationImpl setClassLoader(ClassLoader classLoader) {
        this.classLoader = classLoader;
        return this;
    }

    public BpmnDeployer getBpmnDeployer() {
        return bpmnDeployer;
    }

    public void setBpmnDeployer(BpmnDeployer bpmnDeployer) {
        this.bpmnDeployer = bpmnDeployer;
    }


    public BpmnParser getBpmnParser() {
        return bpmnParser;
    }

    public void setBpmnParser(BpmnParser bpmnParser) {
        this.bpmnParser = bpmnParser;
    }

    public String getProcessEngineName() {
        return processEngineName;
    }

    public void setProcessEngineName(String processEngineName) {
        this.processEngineName = processEngineName;
    }

    public RunTimeServiceImpl getRepositoryService() {
        return repositoryService;
    }

    public void setRepositoryService(RunTimeServiceImpl repositoryService) {
        this.repositoryService = repositoryService;
    }

    public DefaultDeploymentCache<ProcessDefinitionCacheEntry> getProcessDefinitionCache() {
        return processDefinitionCache;
    }

    //
    public void setProcessDefinitionCache(DefaultDeploymentCache<ProcessDefinitionCacheEntry> processDefinitionCache) {
        this.processDefinitionCache = processDefinitionCache;
    }

    public ExpressionManager getExpressionManager() {
        return expressionManager;
    }

    public ThreadPoolExecutor getExecutorService() {
        return executorService;
    }

    public void setExecutorService(ThreadPoolExecutor executorService) {
        this.executorService = executorService;
    }

    public CurrentHashMapCache getRunTimeExecution() {
        return runTimeExecution;
    }

    public void setRunTimeExecution(CurrentHashMapCache runTimeExecution) {
        this.runTimeExecution = runTimeExecution;
    }
}
