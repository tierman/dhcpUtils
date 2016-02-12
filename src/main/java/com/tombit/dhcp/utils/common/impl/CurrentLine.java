package com.tombit.dhcp.utils.common.impl;

/**
 * Created by Tomasz Jonczyk on 06.02.2016.
 */
public class CurrentLine {

    private int number;
    private String content;

    public CurrentLine(int number, String content) {
        this.number = number;
        this.content = content;
    }

    public String getContent() {
        return content;
    }

    public int getNumber() {
        return number;
    }

    @Override
    public String toString() {
        return "CurrentLine{" +
                "number=" + number +
                ", content='" + content + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CurrentLine)) return false;

        CurrentLine that = (CurrentLine) o;

        if (getNumber() != that.getNumber()) return false;
        return getContent() != null ? getContent().equals(that.getContent()) : that.getContent() == null;

    }

    @Override
    public int hashCode() {
        int result = getNumber();
        result = 31 * result + (getContent() != null ? getContent().hashCode() : 0);
        return result;
    }
}
