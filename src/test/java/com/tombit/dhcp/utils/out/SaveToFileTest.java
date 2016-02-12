package com.tombit.dhcp.utils.out;

import com.tombit.dhcp.utils.common.*;
import com.tombit.dhcp.utils.common.impl.HostImpl;
import com.tombit.dhcp.utils.common.impl.ParameterImpl;
import com.tombit.dhcp.utils.common.impl.SubnetImpl;
import com.tombit.dhcp.utils.parameters.ParameterType;
import com.tombit.dhcp.utils.parameters.Prefix;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by tomek on 07.02.2016.
 */
public class SaveToFileTest {

    private static final String TEST_COMMENT = "# TEST";
    private static final String TEST_FILE = "test.txt";
    private static final String TEST_VALUE = "VALUE";
    private static final String TEST_EMPTY = "";
    private static final String TEST_FILENAME = ParameterType.FILENAME.getName() + " " + TEST_VALUE + " ;";
    private static final String TEST_LOG_SERVERS = Prefix.OPTION.getName() + " " + ParameterType.LOG_SERVERS.getName() + " " + TEST_VALUE + " ;";
    private static final String TEST_TIME_SERVERS = Prefix.OPTION.getName() + " " + ParameterType.TIME_SERVERS.getName() + " " + TEST_VALUE + " ;";
    private static final String TEST_IP_PATTERN = "192.168.%d.%d";
    private static final String TEST_HOSTNAME = "hostname_%d";

    @Before
    public void setUp() throws Exception {
        if (Files.exists(Paths.get(TEST_FILE))) {
            Files.delete(Paths.get(TEST_FILE));
        }
    }

    @After
    public void tearDown() throws Exception {
        //Files.delete(Paths.get(TEST_FILE));
    }

    @Test
    public void testWriteDhcpFile_withCommentAndParameters() throws Exception {
        DhcpFile dhcpFile = prepareFileWithCommentAndThreeParameters();
        SaveToFile stf = new SaveToFile();
        stf.writeDhcpFile(TEST_FILE, dhcpFile);

        assertTrue(Files.exists(Paths.get(TEST_FILE)));
        List<String> lines = Files.readAllLines(Paths.get(TEST_FILE));
        assertEquals(4, lines.size());
        assertEquals(TEST_COMMENT, lines.get(0));
        assertEquals(TEST_FILENAME, lines.get(1));
        assertEquals(TEST_LOG_SERVERS, lines.get(2));
        assertEquals(TEST_TIME_SERVERS, lines.get(3));
    }

    @Test
    public void testWriteDhcpFile_withChunks() throws Exception {
        DhcpFile dhcpFile = prepareFileWithChunks();
        SaveToFile stf = new SaveToFile();
        stf.writeDhcpFile(TEST_FILE, dhcpFile);

        assertTrue(Files.exists(Paths.get(TEST_FILE)));
        List<String> lines = Files.readAllLines(Paths.get(TEST_FILE));
        assertEquals(97, lines.size());
        assertEquals(TEST_COMMENT, lines.get(0));
        assertEquals(TEST_FILENAME, lines.get(1));
        assertEquals(TEST_LOG_SERVERS, lines.get(2));
        assertEquals(TEST_TIME_SERVERS, lines.get(3));
        assertEquals(TEST_EMPTY, lines.get(4));
    }

    private DhcpFile prepareFileWithChunks() {
        DhcpFile df = prepareFileWithCommentAndThreeParameters();

        addChunks(df);

        return df;
    }

    private void addChunks(DhcpFile df) {
        for (int subnetIndex = 0; subnetIndex < 3; ++subnetIndex) {
            df.addChunk(addSubnetWithHosts(subnetIndex));
        }
    }

    private Chunk addSubnetWithHosts(int subnetIndex) {
        Subnet subnet = new SubnetImpl();
        subnet.setIpAddress(String.format(TEST_IP_PATTERN, subnetIndex, 0));
        subnet.setNetmask("255.255.255.0");

        for (int hostIndex = 1; hostIndex < 5; ++hostIndex) {
            subnet.addChunk(createHost(subnetIndex, hostIndex));
        }

        return subnet;
    }

    private Chunk createHost(int subnetIndex, int hostIndex) {
        Host host = new HostImpl();

        host.setHostname(String.format(TEST_HOSTNAME, hostIndex));
        host.addComment(String.format(TEST_HOSTNAME, hostIndex));

        for (ParameterType pt : new ParameterType[]{ParameterType.FILENAME, ParameterType.HARDWARE_ETHERNET, ParameterType.FIXED_ADDRESS}) {
            Parameter param = new ParameterImpl();
            param.setType(pt);
            param.setValue(pt == ParameterType.FIXED_ADDRESS ? String.format(TEST_IP_PATTERN, subnetIndex, hostIndex) : TEST_VALUE);

            host.addParameter(param);
        }

        host.setCompleted(true);
        return host;
    }

    private DhcpFile prepareFileWithCommentAndThreeParameters() {
        DhcpFile df = prepareEmptyFileWithComment();

        for (ParameterType pt : new ParameterType[]{ParameterType.FILENAME, ParameterType.LOG_SERVERS, ParameterType.TIME_SERVERS}) {
            Parameter param = new ParameterImpl();
            param.setType(pt);
            param.setValue(TEST_VALUE);

            df.addParameter(param);
        }

        return df;
    }

    private DhcpFile prepareEmptyFileWithComment() {
        DhcpFile df = new DhcpFile();
        df.addComment(TEST_COMMENT);
        return df;
    }
}