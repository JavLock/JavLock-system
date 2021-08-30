package com.github.javlock.system.utils;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

public class ProcessOutputEvent implements Serializable {

	private static final long serialVersionUID = -5873148817387924941L;
	private @Getter @Setter String message;
	private @Getter @Setter long time;

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("ProcessOutputEvent [time=");
		builder.append(time);
		builder.append(", message=");
		builder.append(message);
		builder.append("]");
		return builder.toString();
	}

}
