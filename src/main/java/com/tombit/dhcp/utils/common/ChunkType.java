package com.tombit.dhcp.utils.common;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by tomek on 06.02.2016.
 */
public enum ChunkType {
    HOST("host"),
    GROUP("group"),
    POOL("pool"),
    SHARED_NETWORK("shared-network"),
    SUBNET("subnet");

    public String getPrefix() {
        return prefix;
    }

    private final String prefix;

    ChunkType(String prefix) {
        this.prefix = prefix;
    }

    public static ChunkType getByPrefix(String prefix) {
        return lookup.get(prefix);
    }

    private static final Map<String, ChunkType> lookup = new HashMap<>();

    static {
        for (ChunkType chunkType : ChunkType.values()) {
            lookup.put(chunkType.getPrefix(), chunkType);
        }
    }

}
