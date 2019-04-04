package com.yang.chathelper;

/**
 * @author: ASUS
 * @date: 2019/4/3
 * @description:
 */
public enum AppType {

    TULIAO("兔聊", 0),

    CESHI("测试", 1);

    private String name;

    private int index;

    AppType(String name, int index) {
        this.name = name;
        this.index = index;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }
}
