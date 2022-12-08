package com.runflow.engine.utils.debug;

import java.util.Iterator;
import java.util.List;

public class ExecutionTree implements Iterable<ExecutionTreeNode> {

    protected ExecutionTreeNode root;

    public ExecutionTree() {

    }

    public ExecutionTreeNode getRoot() {
        return root;
    }

    public void setRoot(ExecutionTreeNode root) {
        this.root = root;
    }

    /**
     * Looks up the {@link ExecutionEntity} for a given id.
     */
    public ExecutionTreeNode getTreeNode(String executionId) {
        return getTreeNode(executionId, root);
    }

    protected ExecutionTreeNode getTreeNode(String executionId, ExecutionTreeNode currentNode) {
        if (currentNode.getExecutionEntity().getId().equals(executionId)) {
            return currentNode;
        }

        List<ExecutionTreeNode> children = currentNode.getChildren();
        if (currentNode.getChildren() != null && children.size() > 0) {
            int index = 0;
            while (index < children.size()) {
                ExecutionTreeNode result = getTreeNode(executionId, children.get(index));
                if (result != null) {
                    return result;
                }
                index++;
            }
        }

        return null;
    }

    @Override
    public Iterator<ExecutionTreeNode> iterator() {
        return new ExecutionTreeBfsIterator(this.getRoot());
    }

    public ExecutionTreeBfsIterator bfsIterator() {
        return new ExecutionTreeBfsIterator(this.getRoot());
    }

    /**
     * Uses an {@link ExecutionTreeBfsIterator}, but returns the leafs first (so flipped order of BFS)
     */
    public ExecutionTreeBfsIterator leafsFirstIterator() {
        return new ExecutionTreeBfsIterator(this.getRoot(), true);
    }

    @Override
    public String toString() {
        return root != null ? root.toString() : "";
    }

}
