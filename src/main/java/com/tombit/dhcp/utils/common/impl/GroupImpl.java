package com.tombit.dhcp.utils.common.impl;

import com.tombit.dhcp.utils.common.*;

import java.util.LinkedList;

/**
 * Created by Tomasz Jonczyk on 05.02.2016.
 */
public class GroupImpl implements Group {

    private LinkedList<Chunk> chunks;
    private LinkedList<Parameter> parameters;
    private LinkedList<String> comments;
    private boolean completed;

    @Override
    public LinkedList<Chunk> getChunks() {
        if (chunks == null) {
            chunks = new LinkedList<>();
        }
        return chunks;
    }

    @Override
    public void addChunk(Chunk chunk) {
        if (chunk.getType() != ChunkType.HOST) {
            throw new IllegalArgumentException("Cannot add another type of chunk like host to subnet.");
        }
        chunks.addLast(chunk);
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
        return TYPE.getPrefix() + " {";
    }

    @Override
    public String getCloseStatement() {
        return CLOSE_STATEMENT;
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof GroupImpl)) return false;

        GroupImpl group = (GroupImpl) o;

        if (isCompleted() != group.isCompleted()) return false;
        if (getChunks() != null ? !getChunks().equals(group.getChunks()) : group.getChunks() != null) return false;
        if (getParameters() != null ? !getParameters().equals(group.getParameters()) : group.getParameters() != null)
            return false;
        return getComments() != null ? getComments().equals(group.getComments()) : group.getComments() == null;

    }

    @Override
    public int hashCode() {
        int result = getChunks() != null ? getChunks().hashCode() : 0;
        result = 31 * result + (getParameters() != null ? getParameters().hashCode() : 0);
        result = 31 * result + (getComments() != null ? getComments().hashCode() : 0);
        result = 31 * result + (isCompleted() ? 1 : 0);
        return result;
    }
}
