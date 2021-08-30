package com.github.javlock.system.services.netstat;

import com.github.javlock.system.utils.ListenerForExecutedProcess;
import com.github.javlock.system.utils.ProcessOutputEvent;
import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;

public class NetstatListener extends ListenerForExecutedProcess {
	public final EventBus netstatBus = new EventBus();

	public void init() {
		processCommand = "netstat -natupc";
		getMainEventBuss().register(this);
	}

	@Subscribe
	private void inputRaw(ProcessOutputEvent inputEvent) {
		NetStatEvent netStatEvent = NetStatEvent.parse(inputEvent.getMessage());
		if (netStatEvent == null) {
			return;
		}
		long time = inputEvent.getTime();
		netStatEvent.setTime(time);
		netstatBus.post(netStatEvent);
	}

}
