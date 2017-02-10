package com.meadowsapps.jgameboy.gbc.core;

/**
 * Created by dmeadows on 2/10/2017.
 */
public class Opcode {

    private String id;

    private String label;

    private int length;

    private int timing;

    public Opcode() {
    }

    public Opcode(String id, String label, int length, int timing) {
        this.id = id;
        this.label = label;
        this.length = length;
        this.timing = timing;
    }

    @Override
    public String toString() {
        return "Opcode{" +
                "id='" + id + '\'' +
                ", label='" + label + '\'' +
                ", length=" + length +
                ", timing=" + timing +
                '}';
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public int getTiming() {
        return timing;
    }

    public void setTiming(int timing) {
        this.timing = timing;
    }
}
