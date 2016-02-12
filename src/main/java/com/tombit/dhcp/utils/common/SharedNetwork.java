package com.tombit.dhcp.utils.common;

import java.util.LinkedList;

/**
 * Created by Tomasz Jonczyk on 01.02.2016.
 */
public interface SharedNetwork extends Chunk{

	ChunkType TYPE = ChunkType.SHARED_NETWORK;

	String getName();
	LinkedList<Subnet> getSubnets();

}
