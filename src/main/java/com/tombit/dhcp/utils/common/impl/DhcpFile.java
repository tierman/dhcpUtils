package com.tombit.dhcp.utils.common.impl;

import com.tombit.dhcp.utils.common.Chunk;
import com.tombit.dhcp.utils.common.Parameter;

import java.util.LinkedList;

/**
 * Created by Tomasz Jonczyk on 01.02.2016.
 */
public class DhcpFile {

	private LinkedList<String> comments;
	private LinkedList<Parameter> parameters;
	private LinkedList<Chunk> chunks;

	public LinkedList<String> getComments() {
		if (comments == null) {
			comments = new LinkedList<>();
		}
		return comments;
	}

	public void addComment(String comment) {
		getComments().addLast(comment);
	}

	public LinkedList<Parameter> getParameters() {
		if (parameters == null) {
			parameters = new LinkedList<>();
		}
		return parameters;
	}

	public void addParameter(Parameter parameter) {
		getParameters().addLast(parameter);
	}

	public void setParameters(LinkedList<Parameter> parameters) {
		this.parameters = parameters;
	}

	public LinkedList<Chunk> getChunks() {
		if (chunks == null) {
			chunks = new LinkedList<>();
		}
		return chunks;
	}

	public void addChunk(Chunk chunk) {
		getChunks().addLast(chunk);
	}

	public Chunk getOpenChunk() {
		if (chunks.isEmpty()) {
			return null;
		}

		return chunks.getLast().getOpenChunk();
	}

}
