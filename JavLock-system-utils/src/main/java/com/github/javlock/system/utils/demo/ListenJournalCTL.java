package com.github.javlock.system.utils.demo;

import com.github.javlock.system.utils.ListenerForExecutedProcess;
import com.github.javlock.system.utils.ProcessOutputEvent;
import com.google.common.eventbus.Subscribe;

public class ListenJournalCTL {

	public static void main(String[] args) {
		try {
			ListenJournalCTL demoApp = new ListenJournalCTL();
			demoApp.init();
			demoApp.start();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private ListenerForExecutedProcess listenerForExecutedProcess;

	private void init() throws Exception {
		this.listenerForExecutedProcess = new ListenerForExecutedProcess();
		listenerForExecutedProcess.processCommand = "journalctl   -f  ";
		registerListener();
	}

	@Subscribe
	private void output(ProcessOutputEvent event) {
		System.err.println(event);
	}

	private void registerListener() throws Exception {
		listenerForExecutedProcess.getMainEventBuss().register(this);
	}

	private void start() {
		listenerForExecutedProcess.start();
	}

}
