package com.tombit.dhcp.utils.out;

import com.tombit.dhcp.utils.common.Chunk;
import com.tombit.dhcp.utils.common.DhcpFile;
import com.tombit.dhcp.utils.common.Parameter;

import java.io.IOException;
import java.nio.file.*;
import java.util.LinkedList;
import java.util.logging.Logger;

/**
 * Created by Tomasz Jonczyk on 01.02.2016.
 */
public class SaveToFile {
    private final static String BASE_TAB = "    ";

    private final static Logger LOGGER = Logger.getLogger(SaveToFile.class.getName());

    public void writeDhcpFile(String outputFile, DhcpFile dhcpFile) throws IOException {
        Path path = Paths.get(outputFile);
        writeComment(path, dhcpFile.getComments(), 0);
        writeParameters(path, dhcpFile.getParameters(), 0);
        writeChunks(path, dhcpFile.getChunks(), 0);
    }

    private void writeChunks(Path path, LinkedList<Chunk> chunks, int depth) throws IOException {
        if (chunks == null || chunks.size() == 0) {
            return;
        }

        for (Chunk chunk : chunks) {
            addNewLine(path);
            writeComment(path, chunk.getComments(), depth);
            wiriteOpenChunk(path, chunk.getOpenStatement(), depth);
            ++depth;
            writeParameters(path, chunk.getParameters(), depth);
            writeChunks(path, chunk.getChunks(), depth);
            --depth;
            writeCloseChunk(path, chunk.getCloseStatement(), depth);
        }
    }

    private void writeCloseChunk(Path outputFile, String closeStatement, int depth) throws IOException {
        writeToFile(outputFile, closeStatement, depth);
    }

    private void wiriteOpenChunk(Path outputFile, String openStatement, int depth) throws IOException {
        writeToFile(outputFile, openStatement, depth);
    }

    private void writeParameters(Path outputFile, LinkedList<Parameter> parameters, int depth) throws IOException {
        if (parameters.size() == 0) {
            return;
        }

        for (Parameter parameter : parameters) {
            writeToFile(outputFile, parameter.toString(), depth);
        }
    }

    private void writeComment(Path outputFile, LinkedList<String> comments, int depth) throws IOException {
        if (comments.size() == 0) {
            return;
        }

        for (String comment : comments) {
            if (!comment.startsWith("#")) {
                comment = "# " + comment;
            }
            writeToFile(outputFile, comment, depth);
        }
    }

    private StringBuilder prepareTab(int depth) {
        StringBuilder sb = new StringBuilder();

        for (int i = 1; i <= depth; ++i) {
            sb.append(BASE_TAB);
        }

        return sb;
    }

    private void writeToFile(Path outputFile, String statement, int depth) throws IOException {
        statement = prepareTab(depth).append(statement).toString();
        Files.write(outputFile, statement.getBytes(), StandardOpenOption.APPEND, StandardOpenOption.CREATE);
        addNewLine(outputFile);
    }

    private void addNewLine(Path outputFile) throws IOException {
        Files.write(outputFile, "\n".getBytes(), StandardOpenOption.APPEND, StandardOpenOption.CREATE);
    }
}
