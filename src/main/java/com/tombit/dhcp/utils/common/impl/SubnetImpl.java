package com.tombit.dhcp.utils.common.impl;

import com.tombit.dhcp.utils.common.Chunk;
import com.tombit.dhcp.utils.common.ChunkType;
import com.tombit.dhcp.utils.common.Parameter;
import com.tombit.dhcp.utils.common.Subnet;
import com.tombit.dhcp.utils.parameters.Prefix;

import java.util.LinkedList;

/**
 * Created by Tomasz Jonczyk on 04.02.2016.
 */
public class SubnetImpl implements Subnet {

	private String ipAddress;
	private String netmask;
	private LinkedList<Chunk> chunks;
	private LinkedList<Parameter> parameters;
	private LinkedList<String> comments;
	private boolean completed;

	@Override
	public String getIpAddress() {
		return ipAddress;
	}

	@Override
	public String getNetmask() {
		return netmask;
	}

	@Override
	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}

	public void setNetmask(String netmask) {
		this.netmask = netmask;
	}

	@Override
	public LinkedList<Chunk> getChunks() {
		if(chunks == null) {
			chunks = new LinkedList<>();
		}
		return chunks;
	}

	@Override
	public void addChunk(Chunk chunk) {
		if (chunk.getType() != ChunkType.HOST) {
			throw new IllegalArgumentException("Cannot add another type of chunk like host to subnet.");
		}
		getChunks().addLast(chunk);
	}

	@Override
	public ChunkType getType() {
		return TYPE;
	}

	@Override
	public Chunk getOpenChunk() {
		Chunk result = null;

		if (getChunks().isEmpty()) {
			if (!this.isCompleted()) {
				result = this;
			}
		} else {
			Chunk last = getChunks().getLast();
			if (!last.isCompleted()) {
				result = last;
			} else if (!this.isCompleted()) {
				result = this;
			}
		}

		return result;
	}

	@Override
	public String getOpenStatement() {
		return TYPE.getPrefix() + ' ' + ipAddress + ' ' +
				Subnet.NETMASK_NAME + ' ' + netmask + " {";
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
	public String toString() {
		return 	ChunkType.SUBNET.getPrefix() + ' ' + ipAddress + ' ' +
				Subnet.NETMASK_NAME + ' ' + netmask + " {";
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof SubnetImpl)) return false;

		SubnetImpl subnet = (SubnetImpl) o;

		if (isCompleted() != subnet.isCompleted()) return false;
		if (getIpAddress() != null ? !getIpAddress().equals(subnet.getIpAddress()) : subnet.getIpAddress() != null)
			return false;
		if (getNetmask() != null ? !getNetmask().equals(subnet.getNetmask()) : subnet.getNetmask() != null)
			return false;
		if (getChunks() != null ? !getChunks().equals(subnet.getChunks()) : subnet.getChunks() != null) return false;
		if (getParameters() != null ? !getParameters().equals(subnet.getParameters()) : subnet.getParameters() != null)
			return false;
		return getComments() != null ? getComments().equals(subnet.getComments()) : subnet.getComments() == null;

	}

	@Override
	public int hashCode() {
		int result = getIpAddress() != null ? getIpAddress().hashCode() : 0;
		result = 31 * result + (getNetmask() != null ? getNetmask().hashCode() : 0);
		result = 31 * result + (getChunks() != null ? getChunks().hashCode() : 0);
		result = 31 * result + (getParameters() != null ? getParameters().hashCode() : 0);
		result = 31 * result + (getComments() != null ? getComments().hashCode() : 0);
		result = 31 * result + (isCompleted() ? 1 : 0);
		return result;
	}
}
