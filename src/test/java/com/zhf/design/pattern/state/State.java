package com.zhf.design.pattern.state;

public interface State {


    // 声明抽象方法，不同具体类可以有不同的视线
    void handle(Context context);

}
