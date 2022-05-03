package nl.tudelft.cse1110.andy.execution.externalprocess;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Scanner;
import java.util.concurrent.CountDownLatch;

public class CommandExternalProcess implements ExternalProcess {

    private final String command;
    private final String initSignal;
    private final CountDownLatch initSignalLatch;
    private final CountDownLatch exitLatch;
    private String errorMessages;

    private Process process;

    public CommandExternalProcess(String command, String initSignal) {
        this.command = command;
        this.initSignal = initSignal;
        this.initSignalLatch = new CountDownLatch(initSignal != null ? 1 : 0);
        this.exitLatch = new CountDownLatch(1);
    }

    @Override
    public void launch() throws IOException {
        process = Runtime.getRuntime().exec(command);

        process.onExit().thenAccept(p -> {
            initSignalLatch.countDown();
            exitLatch.countDown();
        });

        if (initSignal != null) {
            Thread thread = new Thread(new OutputHandler());
            thread.start();
        }
    }

    @Override
    public void awaitInitialization() {
        // In case await was called but no process was launched.
        if (process == null) {
            return;
        }

        // Block thread until process has initialized
        try {
            this.initSignalLatch.await();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    @Override
    public void kill() {
        boolean processIsRunning = process != null && process.isAlive();
        if (!processIsRunning) {
            return;
        }

        process.destroy();

        // Block thread until process has exited
        try {
            this.exitLatch.await();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    @Override
    public int getExitCode() {
        // Crashes after the tests have executed do not affect anything, so assume that the process exited normally
        boolean aliveOrTerminated = this.process.isAlive() || this.process.exitValue() == 143;

        if (aliveOrTerminated) {
            return 0;
        }

        return this.process.exitValue();
    }

    @Override
    public boolean hasExitedNormally() {
        return this.getExitCode() == 0;
    }

    @Override
    public void extractErrorMessages() {
        if (errorMessages == null) {
            // stderr messages can only be retrieved from the process once

            try {
                StringBuilder sb = new StringBuilder();
                BufferedReader reader = new BufferedReader(new InputStreamReader(process.getErrorStream()));
                while (reader.ready()) {
                    String next = reader.readLine();
                    sb.append(next);
                }
                errorMessages = sb.toString();
            } catch (IOException ex) {
                ex.printStackTrace();
                errorMessages = ex.getMessage();
            }
        }
    }

    @Override
    public String getErrorMessages() {
        this.extractErrorMessages();

        return errorMessages;
    }

    private class OutputHandler implements Runnable {
        @Override
        public void run() {
            Scanner data = new Scanner(process.getInputStream());

            while (data.hasNextLine()) {
                String line = data.nextLine();
                if (line.contains(initSignal)) {
                    initSignalLatch.countDown();
                }
            }
        }
    }
}
