package com.tombit.dhcp.utils.in;

import com.tombit.dhcp.utils.common.Chunk;
import com.tombit.dhcp.utils.common.ChunkType;
import com.tombit.dhcp.utils.common.Host;
import com.tombit.dhcp.utils.common.Subnet;
import com.tombit.dhcp.utils.common.impl.DhcpFileImpl;
import com.tombit.dhcp.utils.common.impl.HostImpl;
import com.tombit.dhcp.utils.parameters.ParameterType;
import org.junit.Test;
import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by tomek on 01.02.2016.
 */
public class ReadFromFileTest {

	@Test(expected = IllegalArgumentException.class)
	public void testReadNullFile_ExceptionThrown() {
		ReadFromFile readFromFile = new ReadFromFile();

		readFromFile.readDhcpFile(null);
	}

	@Test
	public void testReadEmptyFileWithComment_AllCorrect() {
		InputStream is = getClass().getClassLoader().getResourceAsStream("empty_file");
		BufferedReader emptyFile = new BufferedReader(new InputStreamReader(is));
		ReadFromFile readFromFile = new ReadFromFile();

		DhcpFileImpl result = readFromFile.readDhcpFile(emptyFile);

		assert result != null;
		assert result.getChunks().isEmpty();
		assert result.getParameters() == null;
		assert result.getComments() != null;
		assert result.getComments().size() == 1;
		assert result.getComments().getFirst().equalsIgnoreCase("#empty file");
	}

	@Test
	public void testReadCorrectDhcpFileCheckSubnets_AllCorrect() {
		InputStream is = getClass().getClassLoader().getResourceAsStream("correct_dhcp_file");
		BufferedReader emptyFile = new BufferedReader(new InputStreamReader(is));
		ReadFromFile readFromFile = new ReadFromFile();

		DhcpFileImpl result = readFromFile.readDhcpFile(emptyFile);

		assert result != null;
		assert result.getChunks() != null;
		assert result.getChunks().size() == 3;
	}

	@Test
	public void testReadCorrectDhcpFileSubnetWithParameters_AllCorrect() {
		InputStream is = getClass().getClassLoader().getResourceAsStream("correct_dhcp_file");
		BufferedReader emptyFile = new BufferedReader(new InputStreamReader(is));
		ReadFromFile readFromFile = new ReadFromFile();

		DhcpFileImpl result = readFromFile.readDhcpFile(emptyFile);

		assertNotNull(result);
		assertNotNull(result.getChunks());
		assertEquals(3, result.getChunks().size());
		assertEquals(9, result.getChunks().get(0).getChunks().size());
		assertEquals(8, result.getChunks().get(1).getChunks().size());
		assertEquals(7, result.getChunks().get(2).getChunks().size());

		for (Chunk chunk : result.getChunks()) {
			assertTrue(chunk.isCompleted());
			assertEquals(ChunkType.SUBNET, chunk.getType());
			assertNotNull(((Subnet)chunk).getIpAddress());
			assertNotNull(((Subnet)chunk).getNetmask());
			assertEquals(7, chunk.getParameters().size());

			for (Chunk host : chunk.getChunks()) {
				assertEquals(ChunkType.HOST, host.getType());
				assertTrue(host.isCompleted());
				assertNotNull("Empty hostname in host: " + ((Host) host), ((Host) host).getHostname());
				assertNull(host.getChunks());
				assertEquals(3, host.getParameters().size());
				assertEquals(ParameterType.HARDWARE_ETHERNET, host.getParameters().get(0).getType());
				assertEquals(ParameterType.FIXED_ADDRESS, host.getParameters().get(1).getType());
				assertEquals(ParameterType.FILENAME, host.getParameters().get(2).getType());
			}
		}
	}

	@Test(expected = IllegalArgumentException.class)
	public void testExceptionWhenTryToAddChunkIntoHost() {
		Host host = new HostImpl();
		host.addChunk(host);
	}
}
