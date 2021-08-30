package com.github.javlock.system.services.journalctl.objects;

import lombok.Getter;
import lombok.Setter;

public class JournalCURSORObject {
	private @Getter @Setter String b;
	private @Getter @Setter String i;
	private @Getter @Setter String m;
	private @Getter @Setter String s;
	private @Getter @Setter String t;
	private @Getter @Setter String x;

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("b=").append(b);
		builder.append(",s=").append(s);
		builder.append(",i=").append(i);
		builder.append(",m=").append(m);
		builder.append(",t=").append(t);
		builder.append(",x=").append(x);
		return builder.toString();
	}

	public JournalCURSORObject parse(String value) {
		String[] arr = (value).split(";");
		for (int j = 0; j < arr.length; j++) {
			String string = arr[j];
			String[] pair = string.split("=");
			String sKey = pair[0];
			String sValue = pair[1];
			if (sKey.equals("b")) {
				setB(sValue);
			} else if (sKey.equals("i")) {
				setI(sValue);
			} else if (sKey.equals("m")) {
				setM(sValue);
			} else if (sKey.equals("s")) {
				setS(sValue);
			} else if (sKey.equals("t")) {
				setT(sValue);
			} else if (sKey.equals("x")) {
				setX(sValue);
			} else {
				System.err.println("sKey:" + sKey + " sValue:" + sValue);
			}
		}
		return this;
	}

}
