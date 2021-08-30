package com.github.javlock.system.kernel.config;

import lombok.Getter;
import lombok.Setter;

public class KernelConfig {
	private @Getter @Setter boolean listenSystemdJournal = true;
	private @Getter @Setter boolean listenerIptables = true;
	private @Getter @Setter boolean listenerNetStat = true;

}
