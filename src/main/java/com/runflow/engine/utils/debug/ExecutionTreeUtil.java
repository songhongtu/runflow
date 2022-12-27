package com.runflow.engine.utils.debug;

import com.runflow.engine.RunFlowException;
import com.runflow.engine.ExecutionEntityImpl;

import java.util.*;

public class ExecutionTreeUtil {


    public static ExecutionTree buildExecutionTree(ExecutionEntityImpl executionEntity) {

        // Find highest parent
        ExecutionEntityImpl parentExecution = executionEntity;
        while (parentExecution.getParentId() != null || parentExecution.getSuperExecution() != null) {
            if (parentExecution.getParentId() != null) {
                parentExecution = parentExecution.getParent();
            } else {
                parentExecution = parentExecution.getSuperExecution();
            }
        }

        // Collect all child executions now we have the parent
        List<ExecutionEntityImpl> allExecutions = new ArrayList<ExecutionEntityImpl>();
        allExecutions.add(parentExecution);
        collectChildExecutions(parentExecution, allExecutions);
        return buildExecutionTree(allExecutions);
    }



    public static ExecutionTree buildExecutionTree(Collection<ExecutionEntityImpl> executions) {
        ExecutionTree executionTree = new ExecutionTree();

        // Map the executions to their parents. Catch and store the root element (process instance execution) while were at it
        Map<String, List<ExecutionEntityImpl>> parentMapping = new HashMap<String, List<ExecutionEntityImpl>>();
        for (ExecutionEntityImpl executionEntity : executions) {
            String parentId = executionEntity.getParentId();

            // Support for call activity
            if (parentId == null&&executionEntity.getSuperExecution()!=null) {
                parentId = executionEntity.getSuperExecution().getParentId();
            }

            if (parentId != null) {
                if (!parentMapping.containsKey(parentId)) {
                    parentMapping.put(parentId, new ArrayList<ExecutionEntityImpl>());
                }
                parentMapping.get(parentId).add(executionEntity);
            } else if (executionEntity.getSuperExecution() == null){
                executionTree.setRoot(new ExecutionTreeNode(executionEntity));
            }
        }

        fillExecutionTree(executionTree, parentMapping);
        return executionTree;
    }



    protected static void collectChildExecutions(ExecutionEntityImpl rootExecutionEntity, List<ExecutionEntityImpl> allExecutions) {
        for (ExecutionEntityImpl childExecutionEntity : rootExecutionEntity.getExecutions()) {
            allExecutions.add(childExecutionEntity);
            collectChildExecutions(childExecutionEntity, allExecutions);
        }

        if (rootExecutionEntity.getSubProcessInstance() != null) {
            allExecutions.add(rootExecutionEntity.getSubProcessInstance());
            collectChildExecutions(rootExecutionEntity.getSubProcessInstance(), allExecutions);
        }
    }


    public static ExecutionTree buildExecutionTreeForProcessInstance(Collection<ExecutionEntityImpl> executions) {
        ExecutionTree executionTree = new ExecutionTree();
        if (executions.size() == 0) {
            return executionTree;
        }

        // Map the executions to their parents. Catch and store the root element (process instance execution) while were at it
        Map<String, List<ExecutionEntityImpl>> parentMapping = new HashMap<String, List<ExecutionEntityImpl>>();
        for (ExecutionEntityImpl executionEntity : executions) {
            String parentId = executionEntity.getParentId();

            if (parentId != null) {
                if (!parentMapping.containsKey(parentId)) {
                    parentMapping.put(parentId, new ArrayList<ExecutionEntityImpl>());
                }
                parentMapping.get(parentId).add(executionEntity);
            } else {
                executionTree.setRoot(new ExecutionTreeNode(executionEntity));
            }
        }

        fillExecutionTree(executionTree, parentMapping);
        return executionTree;
    }
    protected static void fillExecutionTree(ExecutionTree executionTree, Map<String, List<ExecutionEntityImpl>> parentMapping) {
        if (executionTree.getRoot() == null) {
            throw new RunFlowException("Programmatic error: the list of passed executions in the argument of the method should contain the process instance execution");
        }

        // Now build the tree, top-down
        LinkedList<ExecutionTreeNode> executionsToHandle = new LinkedList<ExecutionTreeNode>();
        executionsToHandle.add(executionTree.getRoot());

        while (!executionsToHandle.isEmpty()) {
            ExecutionTreeNode parentNode = executionsToHandle.pop();
            String parentId = parentNode.getExecutionEntity().getId();
            if (parentMapping.containsKey(parentId)) {
                List<ExecutionEntityImpl> childExecutions = parentMapping.get(parentId);
                List<ExecutionTreeNode> childNodes = new ArrayList<ExecutionTreeNode>(childExecutions.size());

                for (ExecutionEntityImpl childExecutionEntity : childExecutions) {

                    ExecutionTreeNode childNode = new ExecutionTreeNode(childExecutionEntity);
                    childNode.setParent(parentNode);
                    childNodes.add(childNode);

                    executionsToHandle.add(childNode);
                }

                parentNode.setChildren(childNodes);

            }
        }
    }


}
