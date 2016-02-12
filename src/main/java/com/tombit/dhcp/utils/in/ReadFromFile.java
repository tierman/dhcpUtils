package com.tombit.dhcp.utils.in;

import com.tombit.dhcp.utils.common.*;
import com.tombit.dhcp.utils.common.impl.*;
import com.tombit.dhcp.utils.parameters.ParameterType;
import com.tombit.dhcp.utils.parameters.Prefix;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.IllegalFormatFlagsException;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;

import static com.tombit.dhcp.utils.common.ChunkType.*;

/**
 * Created by Tomasz Jonczyk on 01.02.2016.
 */
public class ReadFromFile {

	private final static Logger LOGGER = Logger.getLogger(ReadFromFile.class.getName());
	private final static String COMMENT_SIGN = "#";
	private final static String CHUNK_CLOSE = "}";
	private final static String CHUNK_OPEN = "{";
	private final static String SUFFIX = ";";
	private final static String SPACE = " ";

	public DhcpFile readDhcpFile(BufferedReader dhcpFile) {
		if (dhcpFile == null) {
			throw new IllegalArgumentException("Parameter BufferedReader dhcpFile cannot be null");
		}

		String line;
		DhcpFile mappedFile = null;

		try {
			mappedFile = new DhcpFile();
			LinkedList<String> commentsBuffer = new LinkedList<>();
			int lineIndex = 0;
			while ((line = dhcpFile.readLine()) != null) {
				if (line.isEmpty()) { //reject empty lines
					continue;
				}
				CurrentLine cl = new CurrentLine(++lineIndex, line.trim());
				processLine(mappedFile, commentsBuffer, cl);
			}

			if (!commentsBuffer.isEmpty()) {
				mappedFile.getComments().addAll(commentsBuffer);
				commentsBuffer.clear();
			}

		} catch (IOException e) {
			LOGGER.log(Level.WARNING, e.getLocalizedMessage());
		}

		return mappedFile;
	}

	private void processLine(DhcpFile dhcpFile, LinkedList<String> commentsBuffer, CurrentLine line) {
		if (line.getContent().startsWith(COMMENT_SIGN)) {
			commentsBuffer.addLast(line.getContent());
			return;
		}

		Chunk openChunk = dhcpFile.getOpenChunk();

		if (line.getContent().equalsIgnoreCase(CHUNK_CLOSE)) {
			openChunk.setCompleted(true);
			return;
		}

		Chunk chunkInLine = prepareChunkFromLine(line);
		Parameter parameterInLine = getParameter(line, chunkInLine);

		if (chunkInLine == null && parameterInLine == null) { // not parameter and not chunk = incorrect line
			throw new IllegalArgumentException("Cannot identify chunk or parameter in line: " + line);
		} else if (chunkInLine == null) {	// in line is defined parameter - so i need add it to file or opened chunk
			if (openChunk == null) {
				dhcpFile.addParameter(parameterInLine);
			} else {
				openChunk.addParameter(parameterInLine);
			}
		} else if (parameterInLine == null) { // in line is defined chunk. Add new chunk to file, or parent opened chunk
			switch (chunkInLine.getType()) {
				case HOST :
					addHostToParrentChunk(openChunk, commentsBuffer, chunkInLine);
					break;
				case SUBNET:
					addSubnetToMappedFile(dhcpFile, commentsBuffer, openChunk, chunkInLine);
					break;
				case SHARED_NETWORK:
				case GROUP:
					addCommentsToChunk(chunkInLine, commentsBuffer);
					dhcpFile.addChunk(chunkInLine);
					break;
				default:
					throw new IllegalArgumentException("Not implemented chunk type: " + chunkInLine.getType() + ", line: " + line);
			}
		}
	}

	private void addSubnetToMappedFile(DhcpFile dhcpFile, LinkedList<String> commentsBuffer, Chunk openChunk, Chunk chunkInLine) {
		if (openChunk == null || openChunk.getType() == SHARED_NETWORK) {
            addCommentsToChunk(chunkInLine, commentsBuffer);
            dhcpFile.addChunk(chunkInLine);
        }
	}

	private void addHostToParrentChunk(Chunk openChunk, LinkedList<String> commentsBuffer, Chunk chunkInLine) {
		if (openChunk.getType() == GROUP || openChunk.getType() == SUBNET) {
            addCommentsToChunk(chunkInLine, commentsBuffer);
            openChunk.addChunk(chunkInLine);
        }
	}

	private Parameter getParameter(CurrentLine line, Chunk chunkInLine) {
		Parameter parameterInLine = null;
		if (chunkInLine == null) {
			parameterInLine = parameterInLine(line);
		}
		return parameterInLine;
	}

	private void addCommentsToChunk(Chunk chunkInLine, LinkedList<String> commentsBuffer) {
		chunkInLine.getComments().addAll(commentsBuffer);
		commentsBuffer.clear();
	}

	private Chunk prepareChunkFromLine(CurrentLine line) {
		if (line.getContent().toLowerCase().startsWith(SUBNET.getPrefix())) {
			Subnet subnet = new SubnetImpl();
			prepareSubnet(subnet, line);
			return subnet;
		} else if (line.getContent().toLowerCase().startsWith(GROUP.getPrefix())) {
			throw new IllegalArgumentException("Not implemented. Cannot parse line: " + line);
		} else if (line.getContent().toLowerCase().startsWith(SHARED_NETWORK.getPrefix())) {
			throw new IllegalArgumentException("Not implemented. Cannot parse line: " + line);
		} else if (line.getContent().toLowerCase().startsWith(POOL.getPrefix())) {
			throw new IllegalArgumentException("Not implemented. Cannot parse line: " + line);
		} else if (line.getContent().toLowerCase().startsWith(HOST.getPrefix())) {
			Host host = new HostImpl();
			preapreHost(host, line);
			return host;
		}

		return null;
	}

	private void prepareSubnet(Subnet subnet, CurrentLine line) {
		String[] subnetLine = splitLine(line.getContent(), " ");
		if (!subnetLine[0].equalsIgnoreCase(SUBNET.getPrefix()) && !subnetLine[2].equalsIgnoreCase(Subnet.NETMASK_NAME)) {
			throw new IllegalArgumentException("Unknown option in line: " + line);
		}

		subnet.setIpAddress(subnetLine[1]);
		subnet.setNetmask(subnetLine[3]);
	}

	private void preapreHost(Host host, CurrentLine line) {
		String[] hostLine = splitLine(line.getContent(), " ");
		if (!hostLine[0].equalsIgnoreCase(HOST.getPrefix())) {
            throw new IllegalArgumentException("Unknown option in line: " + line);
        }
		host.setHostname(hostLine[1]);

		if (!line.getContent().endsWith(CHUNK_CLOSE)) {
			return;
		}

		String[] hostParams = getDefinedHostParams(line);

		for (String hostParameter : hostParams) {
			hostParameter = hostParameter.trim();

			if (!hostParameter.isEmpty()) {
				hostParameter = hostParameter.concat(";");
				host.addParameter(parameterInLine(new CurrentLine(line.getNumber(), hostParameter)));
			}
		}

		host.setCompleted(true);
	}

	private String[] getDefinedHostParams(CurrentLine line) {
		int openSignIndex = line.getContent().indexOf(CHUNK_OPEN) + 1;
		int closeSignIndex = line.getContent().indexOf(CHUNK_CLOSE);

		String hostParameters = line.getContent().substring(openSignIndex, closeSignIndex);
		return hostParameters.split(";");
	}

	private String[] splitLine(String line, String splitBy) {
		String[] split = line.split(splitBy);
		StringBuilder sb = new StringBuilder();
		for (String s : split) {
			if (!s.isEmpty()) {
				sb.append(s.trim()).append(splitBy);
			}
		}
		return sb.toString().split(splitBy);
	}

	private Parameter parameterInLine(CurrentLine line) {
		Prefix prefix;
		if (line.getContent().toLowerCase().startsWith(Prefix.OPTION.getName())) {
			prefix = Prefix.OPTION;
		} else if(line.getContent().toLowerCase().startsWith(Prefix.HARDWARE.getName())) {
			prefix = Prefix.HARDWARE;
		} else {
			prefix = Prefix.BLANK;
		}

		return getParameterWithPrefix(line, prefix);
	}

	private Parameter getParameterWithPrefix(CurrentLine line, Prefix prefix) {
		String[] content = line.getContent().split(SPACE);
		int nameIndex = prefix == Prefix.BLANK ? 0 : 1;
		ParameterType parameterType = findParameterType(line, content[nameIndex], prefix);
		if (parameterType == null) {
			return null;
		}

		checkLineSuffix(line);
		Parameter parameter = new ParameterImpl();
		parameter.setType(parameterType);
		parameter.setValue(getContentValue(content, nameIndex + 1));
		return parameter;
	}

	private ParameterType findParameterType(CurrentLine line, String name, Prefix prefix) {
		ParameterType parameterType = ParameterType.getByName(name);
		if (parameterType == null) {
			LOGGER.info("Cannot find parameter type for name: " + name + ", and line: " + line);
			return null;
		}
		if (parameterType.getPrefix() != prefix) {
			throw new IllegalFormatFlagsException("Illegal parameter type, cannot find parameter in ParameterType. Line content: " + line);
		}
		return parameterType;
	}

	private void checkLineSuffix(CurrentLine line) {
		if (!line.getContent().endsWith(SUFFIX)) {
            throw new IllegalFormatFlagsException("Illegal end of line with parameter definition. Line content: " + line);
        }
	}

	private String getContentValue(String[] content, int start) {
		StringBuilder value = new StringBuilder();

		for (int i = start; i<content.length; ++i) {
			value.append(content[i].replace(";", ""));
			if (content.length > i + 1) {
				value.append(" ");
			}
		}

		return value.toString();
	}
}
