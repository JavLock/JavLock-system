package com.github.javlock.system.services.journalctl;

import java.util.HashMap;

import com.github.javlock.system.utils.ProcessOutputEvent;

import lombok.Getter;

public class JournalctlEvent extends ProcessOutputEvent {

	private static final long serialVersionUID = 1L;
	private @Getter HashMap<String, Object> parametersMap = new HashMap<>();

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("JournalctlEvent [parametersMap=");
		builder.append(parametersMap);
		builder.append("]");
		return builder.toString();
	}

}
