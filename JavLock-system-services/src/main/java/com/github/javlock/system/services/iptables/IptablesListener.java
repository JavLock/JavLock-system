package com.github.javlock.system.services.iptables;

import com.github.javlock.system.services.journalctl.JournalctlEvent;
import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;

public class IptablesListener {

	public void listen(EventBus eventBus) {
		System.out.println("IptablesListener.listen():" + eventBus);
		eventBus.register(this);
	}

	@Subscribe
	private void input(JournalctlEvent event) {
		System.err.println("IptablesListener:input:" + event);
	}
}
