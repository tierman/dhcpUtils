package com.tombit.dhcp.utils.common;

import java.util.LinkedList;

/**
 * Created by tomek on 01.02.2016.
 */
public interface Subnet extends Chunk {

	ChunkType TYPE = ChunkType.SUBNET;
	String NETMASK_NAME = "netmask";

	String getIpAddress();
	void setIpAddress(String ip);
	String getNetmask();
	void setNetmask(String mask);

	LinkedList<Chunk> getChunks();

}
