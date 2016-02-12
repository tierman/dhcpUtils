package com.tombit.dhcp.utils.parameters;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Tomasz Jonczyk on 03.02.2016.
 */
public enum ParameterType {

	ALL_SUBNETS_LOCAL(Prefix.OPTION, "all-subnets-local", DataType.FLAG),
	ARP_CACHE_TIMEOUT(Prefix.OPTION, "arp-cache-timeout", DataType.UINT32),
	DOMAIN_NAME(Prefix.OPTION, "domain-name", DataType.TEXT),
	DOMAIN_NAME_SERVERS(Prefix.OPTION, "domain-name-servers", DataType.IP_ADDRESS),
	ROUTERS(Prefix.OPTION, "routers", DataType.IP_ADDRESS),
	TFTP_SERVER_NAME(Prefix.OPTION, "tftp-server-name", DataType.TEXT),
	TIME_SERVERS(Prefix.OPTION, "time-servers", DataType.IP_ADDRESS),
	TIME_OFFSET(Prefix.OPTION, "time-offset", DataType.INT32),
	NTP_SERVERS(Prefix.OPTION, "ntp-servers", DataType.IP_ADDRESS),
	LOG_SERVERS(Prefix.OPTION, "log-servers", DataType.IP_ADDRESS),
	ADAPTIVE_LEASE_TIME_THRESHOLD(Prefix.BLANK, "adaptive-lease-time-threshold", DataType.PERCENTAGE),
	NEXT_SERVER(Prefix.BLANK, "next-server", DataType.IP_ADDRESS),
	HARDWARE_ETHERNET(Prefix.HARDWARE, "ethernet", DataType.MAC_ADDRESS),
	FIXED_ADDRESS(Prefix.BLANK, "fixed-address", DataType.IP_ADDRESS),
	FILENAME(Prefix.BLANK, "filename", DataType.TEXT);

	ParameterType(Prefix prefix, String name, DataType dataType) {
		this.prefix = prefix;
		this.name = name;
		this.dataType = dataType;
	}

	public Prefix getPrefix() {
		return prefix;
	}

	public String getName() {
		return name;
	}

	public DataType getDataType() {
		return dataType;
	}

	public static ParameterType getByName(String name) {
		return lookup.get(name);
	}

	private final Prefix prefix;
	private final String name;
	private final DataType dataType;
	private static final Map<String, ParameterType> lookup = new HashMap<>();

	static {
		for (ParameterType param : ParameterType.values()) {
			lookup.put(param.getName(), param);
		}
	}

}
