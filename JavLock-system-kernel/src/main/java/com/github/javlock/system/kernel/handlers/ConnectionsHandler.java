package com.github.javlock.system.kernel.handlers;

import com.github.javlock.system.services.netstat.NetStatEvent;
import com.google.common.eventbus.Subscribe;

public class ConnectionsHandler {

	@Subscribe
	private void inputNetstat(NetStatEvent netStatEvent) {
	}
}
