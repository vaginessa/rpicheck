package de.eidottermihi.rpicheck.test;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.sshd.server.Command;
import org.apache.sshd.server.Environment;
import org.apache.sshd.server.ExitCallback;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;

public class OutputStreamSshCommand implements Command, Runnable {

    private OutputStream outputStream;
    private ExitCallback exitCallback;
    private String output;

    public OutputStreamSshCommand(File outputFile) throws IOException {
        output = FileUtils.readFileToString(outputFile);
    }

    @Override
    public void setInputStream(InputStream in) {
    }

    @Override
    public void setOutputStream(OutputStream out) {
        this.outputStream = out;
    }

    @Override
    public void setErrorStream(OutputStream err) {
    }

    @Override
    public void setExitCallback(ExitCallback callback) {
        this.exitCallback = callback;
    }

    @Override
    public void start(Environment env) throws IOException {
        new Thread(this).start();
    }

    @Override
    public void destroy() throws Exception {
        this.outputStream.close();
    }

    @Override
    public void run() {
        PrintWriter pw = new PrintWriter(outputStream);
        pw.print(this.output);
        pw.close();
        try {
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.exitCallback.onExit(0);
    }
}
