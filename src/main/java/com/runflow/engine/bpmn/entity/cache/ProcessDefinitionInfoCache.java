package com.runflow.engine.bpmn.entity.cache;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.runflow.engine.CommandExecutorImpl;
import com.runflow.engine.context.Context;
import com.runflow.engine.impl.Command;
import com.runflow.engine.impl.CommandContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class ProcessDefinitionInfoCache {

    private static final Logger logger = LoggerFactory.getLogger(ProcessDefinitionInfoCache.class);

    protected Map<String, ProcessDefinitionInfoCacheObject> cache;
    protected CommandExecutorImpl commandExecutor;

    /** Cache with no limit */
    public ProcessDefinitionInfoCache(CommandExecutorImpl commandExecutor) {
        this.commandExecutor = commandExecutor;
        this.cache = Collections.synchronizedMap(new HashMap<String, ProcessDefinitionInfoCacheObject>());
    }

    /** Cache which has a hard limit: no more elements will be cached than the limit. */
    public ProcessDefinitionInfoCache(CommandExecutorImpl commandExecutor, final int limit) {
        this.commandExecutor = commandExecutor;
        this.cache = Collections.synchronizedMap(new LinkedHashMap<String, ProcessDefinitionInfoCacheObject>(limit + 1, 0.75f, true) {
            // +1 is needed, because the entry is inserted first, before it is removed
            // 0.75 is the default (see javadocs)
            // true will keep the 'access-order', which is needed to have a real LRU cache
            private static final long serialVersionUID = 1L;

            protected boolean removeEldestEntry(Map.Entry<String, ProcessDefinitionInfoCacheObject> eldest) {
                boolean removeEldest = size() > limit;
                if (removeEldest) {
                    logger.trace("Cache limit is reached, {} will be evicted",  eldest.getKey());
                }
                return removeEldest;
            }

        });
    }

//    public ProcessDefinitionInfoCacheObject get(final String processDefinitionId) {
//        ProcessDefinitionInfoCacheObject infoCacheObject = null;
//        Command<ProcessDefinitionInfoCacheObject> cacheCommand = new Command<ProcessDefinitionInfoCacheObject>() {
//
//            @Override
//            public ProcessDefinitionInfoCacheObject execute(CommandContext commandContext) {
//                return retrieveProcessDefinitionInfoCacheObject(processDefinitionId, commandContext);
//            }
//        };
//
//        if (Context.getCommandContext() != null) {
//            infoCacheObject = retrieveProcessDefinitionInfoCacheObject(processDefinitionId, Context.getCommandContext());
//        } else {
//            infoCacheObject = commandExecutor.execute(cacheCommand);
//        }
//
//        return infoCacheObject;
//    }

    public void add(String id, ProcessDefinitionInfoCacheObject obj) {
        cache.put(id, obj);
    }

    public void remove(String id) {
        cache.remove(id);
    }

    public void clear() {
        cache.clear();
    }

    // For testing purposes only
    public int size() {
        return cache.size();
    }

//    protected ProcessDefinitionInfoCacheObject retrieveProcessDefinitionInfoCacheObject(String processDefinitionId, CommandContext commandContext) {
//        ProcessDefinitionInfoEntityManager infoEntityManager = commandContext.getProcessDefinitionInfoEntityManager();
//        ObjectMapper objectMapper = commandContext.getProcessEngineConfiguration().getObjectMapper();
//
//        ProcessDefinitionInfoCacheObject cacheObject = null;
//        if (cache.containsKey(processDefinitionId)) {
//            cacheObject = cache.get(processDefinitionId);
//        } else {
//            cacheObject = new ProcessDefinitionInfoCacheObject();
//            cacheObject.setRevision(0);
//            cacheObject.setInfoNode(objectMapper.createObjectNode());
//        }
//
//        ProcessDefinitionInfoEntity infoEntity = infoEntityManager.findProcessDefinitionInfoByProcessDefinitionId(processDefinitionId);
//        if (infoEntity != null && infoEntity.getRevision() != cacheObject.getRevision()) {
//            cacheObject.setRevision(infoEntity.getRevision());
//            if (infoEntity.getInfoJsonId() != null) {
//                byte[] infoBytes = infoEntityManager.findInfoJsonById(infoEntity.getInfoJsonId());
//                try {
//                    ObjectNode infoNode = (ObjectNode) objectMapper.readTree(infoBytes);
//                    cacheObject.setInfoNode(infoNode);
//                } catch (Exception e) {
//                    throw new ActivitiException("Error reading json info node for process definition " + processDefinitionId, e);
//                }
//            }
//        } else if (infoEntity == null) {
//            cacheObject.setRevision(0);
//            cacheObject.setInfoNode(objectMapper.createObjectNode());
//        }
//
//        return cacheObject;
//    }

}
