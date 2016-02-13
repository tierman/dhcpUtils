package com.tombit.dhcp.utils.common;

import java.util.LinkedList;

/**
 * Created by Tomasz Jonczyk on 13.02.2016.
 */
public interface DhcpFile {

    LinkedList<String> getComments();
    LinkedList<Parameter> getParameters();
    LinkedList<Chunk> getChunks();
    Chunk getOpenChunk();
    void addParameter(Parameter parameter);
    void addChunk(Chunk chunk);

}
