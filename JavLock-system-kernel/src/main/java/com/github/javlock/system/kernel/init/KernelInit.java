package com.github.javlock.system.kernel.init;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.attribute.PosixFilePermission;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.github.javlock.system.kernel.config.KernelConfig;
import com.github.javlock.system.kernel.handlers.ConnectionsHandler;
import com.github.javlock.system.services.SystemRepository;
import com.github.javlock.system.services.iptables.IptablesListener;
import com.github.javlock.system.services.journalctl.JournalctlListener;
import com.github.javlock.system.services.netstat.NetstatListener;
import com.github.javlock.system.utils.CommandUtils;
import com.github.javlock.system.utils.ProcessOutputEvent;
import com.google.common.eventbus.Subscribe;

public final class KernelInit {

	public static void main(String[] args) {
		try {
			KernelInit kernelInit = new KernelInit();
			kernelInit.init();
			kernelInit.start();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void init() throws IOException {
		checks();
		initConfig();
	}

	private ObjectMapper objectMapper = new ObjectMapper(new YAMLFactory());
	private File dir = new File("javlock-system-kernel");
	private File configFile = new File(dir, "config.yaml");

	private KernelConfig kernelConfig = new KernelConfig();

	SystemRepository systemRepository = new SystemRepository();

	private void initConfig() throws IOException {
		if (!configFile.exists()) {
			dir.mkdirs();
			if (configFile.createNewFile()) {
				System.out.println("empty config file created");
			}

			Set<PosixFilePermission> perms2 = new HashSet<>();
			perms2.add(PosixFilePermission.OWNER_READ);
			perms2.add(PosixFilePermission.OWNER_WRITE);
			Files.setPosixFilePermissions(configFile.toPath(), perms2);

			objectMapper.writeValue(configFile, kernelConfig);
		} else {
			kernelConfig = objectMapper.readValue(configFile, KernelConfig.class);
		}
	}

	private void checks() {
		rootCheck();
	}

	private void rootCheck() {
		try {
			ArrayList<String> out = CommandUtils.executeCommandAsStringList("id -u");
			int uid = Integer.parseInt(out.get(0));
			System.out.println("KernelInit.rootCheck(" + uid + ")");
			if (uid != 0) {
				System.out.println("User is not root UID:(" + uid + "), exit status=1");
				java.lang.Runtime.getRuntime().exit(1);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	ConnectionsHandler connectionsHandler = new ConnectionsHandler();

	private void start() {
		System.out.println("KernelInit.start()");
		if (kernelConfig.isListenerNetStat()) {
			NetstatListener netstatListener = new NetstatListener();

			systemRepository.register(JournalctlListener.class, netstatListener.netstatBus);
			netstatListener.netstatBus.register(connectionsHandler);

			netstatListener.init();
			netstatListener.start();
		}

		if (kernelConfig.isListenSystemdJournal()) {
			JournalctlListener journalctlListener = new JournalctlListener();
			systemRepository.register(JournalctlListener.class, journalctlListener.getJournalBus());

			journalctlListener.init();
			journalctlListener.start();

			if (kernelConfig.isListenerIptables()) {
				IptablesListener iptablesListener = new IptablesListener();
				iptablesListener.listen(systemRepository.get(JournalctlListener.class));
			}
		}
	}

	@Subscribe
	private void output(ProcessOutputEvent event) {
		System.err.println(event);
	}

}
