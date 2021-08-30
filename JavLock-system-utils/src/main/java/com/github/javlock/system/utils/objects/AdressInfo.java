package com.github.javlock.system.utils.objects;

import java.io.Serializable;
import java.util.Objects;

import lombok.Getter;
import lombok.Setter;

public class AdressInfo implements Serializable {
	private static final long serialVersionUID = 4576102446813259301L;
	private @Getter @Setter String host;
	private @Getter @Setter int port;

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("AdressInfo [host=");
		builder.append(host);
		builder.append(", port=");
		builder.append(port);
		builder.append("]");
		return builder.toString();
	}

	@Override
	public int hashCode() {
		return Objects.hash(host, port);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		AdressInfo other = (AdressInfo) obj;
		return Objects.equals(host, other.host) && port == other.port;
	}

}
