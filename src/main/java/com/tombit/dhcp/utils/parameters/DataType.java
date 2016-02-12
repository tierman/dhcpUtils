package com.tombit.dhcp.utils.parameters;

/**
 * Created by tomek on 03.02.2016.
 */
public enum DataType {
	IP_ADDRESS,
	IP6_ADDRESS,
	INT32,
	UINT32,
	INT16,
	UINT16,
	INT8,
	UINT8,
	TEXT,
	DOMAIN_NAME,
	DOMAIN_LIST,
	FLAG,
	STRING,
	DESTINATION_DESCRIPTOR,
	PERCENTAGE,
	MAC_ADDRESS;

	//TODO: write validators for each data type
}
