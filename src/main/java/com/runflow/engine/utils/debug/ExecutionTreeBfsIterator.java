package com.runflow.engine.utils.debug;

import java.util.Iterator;
import java.util.LinkedList;

public class ExecutionTreeBfsIterator  implements Iterator<ExecutionTreeNode> {

    protected ExecutionTreeNode rootNode;
    protected boolean reverseOrder;

    protected LinkedList<ExecutionTreeNode> flattenedList;
    protected Iterator<ExecutionTreeNode> flattenedListIterator;

    public ExecutionTreeBfsIterator(ExecutionTreeNode executionTree) {
        this(executionTree, false);
    }

    public ExecutionTreeBfsIterator(ExecutionTreeNode rootNode, boolean reverseOrder) {
        this.rootNode = rootNode;
        this.reverseOrder = reverseOrder;
    }

    protected void flattenTree() {
        flattenedList = new LinkedList<ExecutionTreeNode>();

        LinkedList<ExecutionTreeNode> nodesToHandle = new LinkedList<ExecutionTreeNode>();
        nodesToHandle.add(rootNode);
        while (!nodesToHandle.isEmpty()) {

            ExecutionTreeNode currentNode = nodesToHandle.pop();
            if (reverseOrder) {
                flattenedList.addFirst(currentNode);
            } else {
                flattenedList.add(currentNode);
            }

            if (currentNode.getChildren() != null && currentNode.getChildren().size() > 0) {
                for (ExecutionTreeNode childNode : currentNode.getChildren()) {
                    nodesToHandle.add(childNode);
                }
            }
        }

        flattenedListIterator = flattenedList.iterator();
    }

    @Override
    public boolean hasNext() {
        if (flattenedList == null) {
            flattenTree();
        }
        return flattenedListIterator.hasNext();
    }

    @Override
    public ExecutionTreeNode next() {
        if (flattenedList == null) {
            flattenTree();
        }
        return flattenedListIterator.next();
    }

    @Override
    public void remove() {
        if (flattenedList == null) {
            flattenTree();
        }
        flattenedListIterator.remove();
    }

}
