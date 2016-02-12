package com.tombit.dhcp.utils.common.impl;

import com.tombit.dhcp.utils.common.Parameter;
import com.tombit.dhcp.utils.parameters.ParameterType;
import com.tombit.dhcp.utils.parameters.Prefix;

/**
 * Created by tomek on 04.02.2016.
 */
public class ParameterImpl implements Parameter {

	private ParameterType type;
	private String value;

	@Override
	public void setType(ParameterType type) {
		this.type = type;
	}

	@Override
	public ParameterType getType() {
		return type;
	}

	@Override
	public void setValue(String value) {
		this.value = value;
	}

	@Override
	public String getValue() {
		return value;
	}

	@Override
	public String toString() {
		if (type == null) {
			return null;
		}
		StringBuffer sb = new StringBuffer();
		if (type.getPrefix() != Prefix.BLANK) {
			sb.append(type.getPrefix().getName());
			sb.append(" ");
		}
		sb.append(type.getName());
		sb.append(" ");
		sb.append(value);
		sb.append(" ;");

		return sb.toString();
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof ParameterImpl)) return false;

		ParameterImpl parameter = (ParameterImpl) o;

		if (getType() != parameter.getType()) return false;
		return getValue() != null ? getValue().equals(parameter.getValue()) : parameter.getValue() == null;

	}

	@Override
	public int hashCode() {
		int result = getType() != null ? getType().hashCode() : 0;
		result = 31 * result + (getValue() != null ? getValue().hashCode() : 0);
		return result;
	}
}
