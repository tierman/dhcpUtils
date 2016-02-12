package com.tombit.dhcp.utils.parameters;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Tomasz Jonczyk on 03.02.2016.
 */
public enum Prefix {
	OPTION("option"),
	BLANK(""),
	ALLOW("allow"),
	DENY("deny"),
	IGNORE("ignore"),
	HARDWARE("hardware");

	public String getName() {
		return name;
	}

	Prefix(String name) {
		this.name = name;
	}

	private final String name;

	public static Prefix getByName(String name) {
		return lookup.get(name);
	}

	private static final Map<String, Prefix> lookup = new HashMap<>();

	static {
		for (Prefix prefix : Prefix.values()) {
			lookup.put(prefix.getName(), prefix);
		}
	}

}
