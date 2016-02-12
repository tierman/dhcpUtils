package com.tombit.dhcp.utils.common.impl;

import com.tombit.dhcp.utils.common.*;

import java.util.LinkedList;

/**
 * Created by tomek on 06.02.2016.
 */
public class HostImpl implements Host {

    private String hostName;
    private LinkedList<Parameter> parameters;
    private LinkedList<String> comments;
    private boolean completed;

    @Override
    public String getHostname() {
        return hostName;
    }

    @Override
    public void setHostname(String hostname) {
        this.hostName = hostname;
    }

    @Override
    public boolean isCompleted() {
        return completed;
    }

    @Override
    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    @Override
    public LinkedList<Parameter> getParameters() {
        if (parameters == null) {
            parameters = new LinkedList<>();
        }
        return parameters;
    }

    @Override
    public void addParameter(Parameter parameter) {
        getParameters().addLast(parameter);
    }

    @Override
    public LinkedList<String> getComments() {
        if (comments == null) {
            comments = new LinkedList<>();
        }
        return comments;
    }

    @Override
    public void addComment(String comment) {
        getComments().addLast(comment);
    }

    @Override
    public LinkedList<Chunk> getChunks() {
        return null;
    }

    @Override
    public void addChunk(Chunk chunk) {
        throw new IllegalArgumentException("Cannot add chunk to Host.");
    }

    @Override
    public ChunkType getType() {
        return TYPE;
    }

    @Override
    public Chunk getOpenChunk() {
        return null;
    }

    @Override
    public String getOpenStatement() {
        return TYPE.getPrefix() + " " + hostName + " {";
    }

    @Override
    public String getCloseStatement() {
        return CLOSE_STATEMENT;
    }

    @Override
    public String toString() {
        return "HostImpl{" +
                "hostName='" + hostName + '\'' +
                ", parameters=" + parameters +
                ", comments=" + comments +
                ", completed=" + completed +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof HostImpl)) return false;

        HostImpl host = (HostImpl) o;

        if (isCompleted() != host.isCompleted()) return false;
        if (hostName != null ? !hostName.equals(host.hostName) : host.hostName != null) return false;
        if (getParameters() != null ? !getParameters().equals(host.getParameters()) : host.getParameters() != null)
            return false;
        return getComments() != null ? getComments().equals(host.getComments()) : host.getComments() == null;

    }

    @Override
    public int hashCode() {
        int result = hostName != null ? hostName.hashCode() : 0;
        result = 31 * result + (getParameters() != null ? getParameters().hashCode() : 0);
        result = 31 * result + (getComments() != null ? getComments().hashCode() : 0);
        result = 31 * result + (isCompleted() ? 1 : 0);
        return result;
    }
}
