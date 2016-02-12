package com.tombit.dhcp.utils.common;

import java.util.LinkedList;

/**
 * Created by tomek on 01.02.2016.
 * Common interface for all objects which aggregate other. Example group, pool, subnet and shared network.
 */
public interface Chunk {

	String CLOSE_STATEMENT = "}";

	boolean isCompleted();
	void setCompleted(boolean completed);

	LinkedList<Parameter> getParameters();
	void addParameter(Parameter parameter);

	LinkedList<String> getComments();
	void addComment(String comment);

	LinkedList<Chunk> getChunks();
	void addChunk(Chunk chunk);

	ChunkType getType();

	Chunk getOpenChunk();

	String getOpenStatement();
	String getCloseStatement();
}
