package com.github.javlock.system.services.netstat;

import com.github.javlock.system.utils.ProcessOutputEvent;
import com.github.javlock.system.utils.objects.AdressInfo;
import com.github.javlock.system.utils.objects.ProgrammInfo;

import lombok.Getter;
import lombok.Setter;

public class NetStatEvent extends ProcessOutputEvent {

	private static final long serialVersionUID = 1L;

	enum connectionProto {
		TCP, TCP6, UDP, UDP6
	}

	enum connectionState {
		LISTEN, ESTABLISHED, TIME_WAIT, CLOSE_WAIT, FIN_WAIT1, FIN_WAIT2, SYN_SENT, LAST_ACK, SYN_RECV, CLOSING
	}

	public static connectionProto getProtoByMessage(String message) {
		String udp6LC = connectionProto.UDP6.toString().toLowerCase();
		if (message.toLowerCase().startsWith(udp6LC)) {
			return connectionProto.UDP6;
		}

		String udpLC = connectionProto.UDP.toString().toLowerCase();
		if (message.toLowerCase().startsWith(udpLC)) {
			return connectionProto.UDP;
		}

		String tcp6LC = connectionProto.TCP6.toString().toLowerCase();
		if (message.toLowerCase().startsWith(tcp6LC)) {
			return connectionProto.TCP6;
		}
		String tcpLC = connectionProto.TCP.toString().toLowerCase();
		if (message.toLowerCase().startsWith(tcpLC)) {
			return connectionProto.TCP;
		}

		return null;
	}

	public static NetStatEvent parse(String message) {

		String m = message.replaceAll("[ ]{2,}", " ");
		String[] parameters = m.split(" ");

		int length = parameters.length;

		connectionProto conProto = getProtoByMessage(parameters[0]);
		if (conProto == null) {
			return null;
		}

		NetStatEvent netStatEvent = new NetStatEvent();
		netStatEvent.proto = conProto;

		try {
			String localAddrString = parameters[3];
			String[] localAddrStringArray = localAddrString.split(":");
			String lHost = localAddrStringArray[0];
			String lportString = localAddrStringArray[localAddrStringArray.length - 1];
			int lPort = -1;
			if (!lportString.equals("*")) {
				lPort = Integer.parseInt(lportString);
			}
			netStatEvent.localaddress.setHost(lHost);
			netStatEvent.localaddress.setPort(lPort);

			String remoteAddrString = parameters[4];
			String[] remoteAddrStringArray = remoteAddrString.split(":");
			String rHost = remoteAddrStringArray[0];
			String rportString = remoteAddrStringArray[remoteAddrStringArray.length - 1];
			int rPort = -1;
			if (!rportString.equals("*")) {
				rPort = Integer.parseInt(rportString);
			}
			netStatEvent.foreignAddress.setHost(rHost);
			netStatEvent.foreignAddress.setPort(rPort);

			connectionState conState = null;

			int progPid = -10;
			String progLine = null;
			try {
				String procInfo = null;
				if (length == 6) {
					procInfo = parameters[5];
				} else if (length >= 7) {

					if (!conProto.equals(connectionProto.UDP6) && !conProto.equals(connectionProto.UDP)) {
						conState = NetStatEvent.connectionState.valueOf(parameters[5]);
					}

					StringBuilder aBuilder = new StringBuilder();
					for (int i = 0; i < parameters.length; i++) {
						String parameter = parameters[i];
						if (i >= 6) {
							aBuilder.append(parameter).append(" ");
						}
						procInfo = aBuilder.toString();
					}
				} else {
					System.err.println(m);
					Runtime.getRuntime().exit(9);
				}

				if (procInfo != null && !(procInfo.equals("- ") || procInfo.equals("-"))) {
					try {
						progPid = Integer.parseInt(procInfo.split("/")[0]);
					} catch (Exception e) {
					}
				}
				progLine = getSecondPartString("/", procInfo);
			} catch (Exception e) {
				e.printStackTrace();
				System.err.println();

				int id = 0;
				for (String string : parameters) {
					System.err.println("ID:" + id + " " + string);
					id++;
				}
				Runtime.getRuntime().exit(10);
			}

			netStatEvent.state = conState;

			netStatEvent.programmInfo.setPid(progPid);
			netStatEvent.programmInfo.setCmdLine(progLine);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return netStatEvent;
	}

	private static String getSecondPartString(String del, String progName) {
		String pid = progName.split(del)[0];
		return progName.replace(pid + del, "");
	}

	private @Getter @Setter connectionProto proto;

	private @Getter @Setter AdressInfo localaddress = new AdressInfo();
	private @Getter @Setter AdressInfo foreignAddress = new AdressInfo();

	private @Getter @Setter connectionState state;
	private @Getter @Setter ProgrammInfo programmInfo = new ProgrammInfo();

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("NetStatEvent [time=");
		builder.append(getTime());
		builder.append(", proto=");
		builder.append(proto);
		builder.append(", localaddress=");
		builder.append(localaddress);
		builder.append(", foreignAddress=");
		builder.append(foreignAddress);
		builder.append(", state=");
		builder.append(state);
		builder.append(", programmInfo=");
		builder.append(programmInfo);
		builder.append("]");
		return builder.toString();
	}

}
