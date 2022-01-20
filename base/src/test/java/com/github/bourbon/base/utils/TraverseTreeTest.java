package com.github.bourbon.base.utils;

import com.github.bourbon.base.logger.Logger;
import com.github.bourbon.base.logger.LoggerFactory;
import org.junit.Test;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/12/20 16:11
 */
public class TraverseTreeTest {

    private final Logger logger = LoggerFactory.getLogger(TraverseTreeTest.class);

    @Test
    public void test() {
        Tree tree1 = new Tree(1);
        Tree tree2 = new Tree(2);
        Tree tree3 = new Tree(3);
        Tree tree4 = new Tree(4);
        Tree tree5 = new Tree(5);
        Tree tree6 = new Tree(6);
        Tree tree7 = new Tree(7);

        tree1.left = tree2;
        tree1.right = tree3;

        tree2.left = tree4;
        tree2.right = tree5;

        tree3.left = tree6;
        tree3.right = tree7;

        logger.error(left(new ArrayList<>(), tree1));
        logger.error(middle(new ArrayList<>(), tree1));
        logger.error(right(new ArrayList<>(), tree1));
        logger.error(level(new ArrayList<>(), tree1));
    }

    private List<Integer> left(List<Integer> list, Tree tree) {
        if (tree != null) {
            list.add(tree.val);
            left(list, tree.left);
            left(list, tree.right);
        }
        return list;
    }

    private List<Integer> middle(List<Integer> list, Tree tree) {
        if (tree != null) {
            middle(list, tree.left);
            list.add(tree.val);
            middle(list, tree.right);
        }
        return list;
    }

    private List<Integer> right(List<Integer> list, Tree tree) {
        if (tree != null) {
            right(list, tree.left);
            right(list, tree.right);
            list.add(tree.val);
        }
        return list;
    }

    private List<Integer> level(List<Integer> list, Tree tree) {
        if (tree != null) {
            Queue<Tree> queue = new LinkedList<>();
            queue.offer(tree);
            Tree t;
            while ((t = queue.poll()) != null) {
                list.add(t.val);
                if (t.left != null) {
                    queue.offer(t.left);
                }
                if (t.right != null) {
                    queue.offer(t.right);
                }
            }
        }
        return list;
    }

    private class Tree {
        private final int val;
        private Tree left;
        private Tree right;

        private Tree(int val) {
            this.val = val;
        }

        @Override
        public String toString() {
            return "Tree{" +
                    "val=" + val +
                    ", left=" + left +
                    ", right=" + right +
                    '}';
        }
    }
}