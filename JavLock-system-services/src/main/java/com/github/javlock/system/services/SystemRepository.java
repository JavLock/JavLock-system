package com.github.javlock.system.services;

import java.util.concurrent.ConcurrentHashMap;

import com.github.javlock.system.services.journalctl.JournalctlListener;
import com.google.common.eventbus.EventBus;

public class SystemRepository {
	private ConcurrentHashMap<Class<?>, EventBus> repMap = new ConcurrentHashMap<>();;

	public void register(Class<?> class1, EventBus journalBus) {
		repMap.put(class1, journalBus);
	}

	public EventBus get(Class<JournalctlListener> class1) {
		return repMap.get(class1);
	}
}
