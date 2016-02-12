package com.tombit.dhcp.utils.common;

import com.tombit.dhcp.utils.parameters.ParameterType;

/**
 * Created by Tomasz Jonczyk on 01.02.2016.
 * This class describe options like max-lease-time or routers etc. (Example option domain-name-servers ns1.example.com)
 */
public interface Parameter {

	/**
	 * @param type is a ParameterType, for example: domain-name-servers
	 * */
	void setType(ParameterType type);
	ParameterType getType();

	/**
	 * @param value is a option value, for example: ns1.example.com
	 * */
	void setValue(String value);
	String getValue();
}
