package com.tombit.dhcp.utils.common;

/**
 * Created by Tomasz Jonczyk on 01.02.2016.
 * Host tag.
 */
public interface Host extends Chunk {

	ChunkType TYPE = ChunkType.HOST;

	String getHostname();
	void setHostname(String hostname);

}
