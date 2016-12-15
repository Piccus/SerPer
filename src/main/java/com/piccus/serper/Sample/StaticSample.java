package com.piccus.serper.Sample;

/**
 * Created by Piccus on 2016/12/9.
 */
public class StaticSample implements IStaticSample{

    private int field;

    private boolean toogle;

    private String mark;

    public StaticSample() {
        this.toogle = true;
        this.field = 0;
        this.mark = "Create instance of StaticSample";
    }

    public StaticSample(String mark) {
        this.toogle = true;
        this.field = 0;
        this.mark = "Create instance of staticSample with mark : " + mark;
    }

    @Override
    public String toString() {
        return "StaticSample : field: " + field + " toggle: " + toogle + " \r\n  mark:" + mark;
    }

    @Override
    public String test() {
        return toString();
    }

}
