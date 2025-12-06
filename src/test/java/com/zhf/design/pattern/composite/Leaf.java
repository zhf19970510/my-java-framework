package com.zhf.design.pattern.composite;

/**
 * 叶子节点：
 *  不能包含子节点
 */
public class Leaf extends Component{
    @Override
    public void add(Component c) {

    }

    @Override
    public void remove(Component c) {

    }

    @Override
    public Component getChild(int i) {
        return null;
    }

    @Override
    public void operation() {
        // 叶子节点中的具体方法
    }
}
