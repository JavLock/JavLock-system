package com.github.javlock.system.utils.objects;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

public class ProgrammInfo implements Serializable {
	private static final long serialVersionUID = 5365393543977062864L;
	private @Getter @Setter int pid;
	private @Getter @Setter int uid;
	private @Getter @Setter String cmdLine;

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("ProgrammInfo [uid=");
		builder.append(uid);
		builder.append(", pid=");
		builder.append(pid);
		builder.append(", cmdLine=");
		builder.append(cmdLine);
		builder.append("]");
		return builder.toString();
	}

}
