package com.github.javlock.system.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public final class CommandUtils {

	public static ArrayList<String> executeCommandAsStringList(String cmd) throws IOException {
		List<String> command = Arrays.asList(cmd.split(" "));

		ProcessBuilder processBuilder = new ProcessBuilder(command);
		processBuilder.redirectErrorStream(true);

		Process process = processBuilder.start();

		ArrayList<String> outputList = new ArrayList<>();
		try (BufferedReader in = new BufferedReader(
				new InputStreamReader(process.getInputStream(), StandardCharsets.UTF_8))) {
			String line;
			while ((line = in.readLine()) != null) {
				outputList.add(line);
			}
		}
		process.destroy();
		return outputList;
	}
}
