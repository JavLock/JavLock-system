package com.github.javlock.system.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

import com.google.common.eventbus.EventBus;

import lombok.Getter;
import lombok.Setter;

public class ListenerForExecutedProcess extends Thread {
	public String processCommand;
	private Process process;
	private @Getter @Setter EventBus mainEventBuss = new EventBus();

	@Override
	public void run() {
		try {
			processCommand = prepareCommand(processCommand);
			ProcessBuilder processBuilder = new ProcessBuilder(processCommand.split(" "));
			processBuilder.redirectErrorStream(true);
			this.process = processBuilder.start();

			try (BufferedReader in = new BufferedReader(
					new InputStreamReader(process.getInputStream(), StandardCharsets.UTF_8))) {
				String line;
				while ((line = in.readLine()) != null) {
					newLineReceived(line);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	protected void newLineReceived(String line) {
		long time = System.currentTimeMillis();
		ProcessOutputEvent processOutputEvent = new ProcessOutputEvent();
		processOutputEvent.setMessage(line);
		processOutputEvent.setTime(time);

		mainEventBuss.post(processOutputEvent);
	}

	private String prepareCommand(String command) {
		command = command.replaceAll("[ ]{2,}", " ");
		return command;
	}
}
