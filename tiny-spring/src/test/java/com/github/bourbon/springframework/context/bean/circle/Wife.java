package com.github.bourbon.springframework.context.bean.circle;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/12/9 14:19
 */
public class Wife {
    private Husband husband;
    private IMother mother;

    public String queryHusband() {
        return "Wife.husband、Mother.callMother：" + mother.callMother();
    }

    public Husband getHusband() {
        return husband;
    }

    public void setHusband(Husband husband) {
        this.husband = husband;
    }

    public IMother getMother() {
        return mother;
    }

    public void setMother(IMother mother) {
        this.mother = mother;
    }
}