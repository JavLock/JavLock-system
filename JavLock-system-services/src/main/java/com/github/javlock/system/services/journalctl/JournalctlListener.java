package com.github.javlock.system.services.journalctl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONObject;

import com.github.javlock.system.services.journalctl.objects.JournalCURSORObject;
import com.github.javlock.system.utils.CommandUtils;
import com.github.javlock.system.utils.ListenerForExecutedProcess;
import com.github.javlock.system.utils.ProcessOutputEvent;
import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;

import lombok.Getter;

public class JournalctlListener extends ListenerForExecutedProcess {
	//
	// не время
	// __MONOTONIC_TIMESTAMP
	// _SOURCE_MONOTONIC_TIMESTAMP=
	//

	// время
	// __REALTIME_TIMESTAMP -время в журнале
	// _SOURCE_REALTIME_TIMESTAMP -время до получения журналом

	public static final String _AUDIT_FIELD_A0 = "_AUDIT_FIELD_A0";
	public static final String _AUDIT_FIELD_A10 = "_AUDIT_FIELD_A10";
	public static final String _AUDIT_FIELD_A11 = "_AUDIT_FIELD_A11";
	public static final String _AUDIT_FIELD_A12 = "_AUDIT_FIELD_A12";
	public static final String _AUDIT_FIELD_A13 = "_AUDIT_FIELD_A13";
	public static final String _AUDIT_FIELD_A14 = "_AUDIT_FIELD_A14";
	public static final String _AUDIT_FIELD_A15 = "_AUDIT_FIELD_A15";
	public static final String _AUDIT_FIELD_A1 = "_AUDIT_FIELD_A1";
	public static final String _AUDIT_FIELD_A2 = "_AUDIT_FIELD_A2";
	public static final String _AUDIT_FIELD_A3 = "_AUDIT_FIELD_A3";
	public static final String _AUDIT_FIELD_A4 = "_AUDIT_FIELD_A4";
	public static final String _AUDIT_FIELD_A5 = "_AUDIT_FIELD_A5";
	public static final String _AUDIT_FIELD_A6 = "_AUDIT_FIELD_A6";
	public static final String _AUDIT_FIELD_A7 = "_AUDIT_FIELD_A7";
	public static final String _AUDIT_FIELD_A8 = "_AUDIT_FIELD_A8";
	public static final String _AUDIT_FIELD_A9 = "_AUDIT_FIELD_A9";
	public static final String AUDIT_FIELD_ACCT = "AUDIT_FIELD_ACCT";
	public static final String AUDIT_FIELD_ADDR = "AUDIT_FIELD_ADDR";
	public static final String _AUDIT_FIELD_APPARMOR = "_AUDIT_FIELD_APPARMOR";
	public static final String _AUDIT_FIELD_ARCH = "_AUDIT_FIELD_ARCH";
	public static final String _AUDIT_FIELD_ARGC = "_AUDIT_FIELD_ARGC";
	public static final String _AUDIT_FIELD_AUDIT_ENABLED = "_AUDIT_FIELD_AUDIT_ENABLED";
	public static final String _AUDIT_FIELD_AUDIT_PID = "_AUDIT_FIELD_AUDIT_PID";
	public static final String _AUDIT_FIELD_CAPABILITY = "_AUDIT_FIELD_CAPABILITY";
	public static final String _AUDIT_FIELD_CAP_FE = "_AUDIT_FIELD_CAP_FE";
	public static final String _AUDIT_FIELD_CAP_FI = "_AUDIT_FIELD_CAP_FI";
	public static final String _AUDIT_FIELD_CAP_FP = "_AUDIT_FIELD_CAP_FP";
	public static final String _AUDIT_FIELD_CAP_FROOTID = "_AUDIT_FIELD_CAP_FROOTID";
	public static final String _AUDIT_FIELD_CAP_FVER = "_AUDIT_FIELD_CAP_FVER";
	public static final String _AUDIT_FIELD_CAPNAME = "_AUDIT_FIELD_CAPNAME";
	public static final String AUDIT_FIELD_COMM = "AUDIT_FIELD_COMM";
	public static final String _AUDIT_FIELD_CWD = "_AUDIT_FIELD_CWD";
	public static final String _AUDIT_FIELD_DENIED_MASK = "_AUDIT_FIELD_DENIED_MASK";
	public static final String _AUDIT_FIELD_DEV = "_AUDIT_FIELD_DEV";
	public static final String _AUDIT_FIELD_ENTRIES = "_AUDIT_FIELD_ENTRIES";
	public static final String _AUDIT_FIELD_ERROR = "_AUDIT_FIELD_ERROR";
	public static final String AUDIT_FIELD_EXE = "AUDIT_FIELD_EXE";
	public static final String _AUDIT_FIELD_EXIT = "_AUDIT_FIELD_EXIT";
	public static final String _AUDIT_FIELD_FADDR = "_AUDIT_FIELD_FADDR";
	public static final String _AUDIT_FIELD_FAMILY = "_AUDIT_FIELD_FAMILY";
	public static final String _AUDIT_FIELD_FPORT = "_AUDIT_FIELD_FPORT";
	public static final String AUDIT_FIELD_GRANTORS = "AUDIT_FIELD_GRANTORS";
	public static final String AUDIT_FIELD_HOSTNAME = "AUDIT_FIELD_HOSTNAME";
	public static final String _AUDIT_FIELD_INFO = "_AUDIT_FIELD_INFO";
	public static final String _AUDIT_FIELD_INODE = "_AUDIT_FIELD_INODE";
	public static final String _AUDIT_FIELD_ITEM = "_AUDIT_FIELD_ITEM";
	public static final String _AUDIT_FIELD_ITEMS = "_AUDIT_FIELD_ITEMS";
	public static final String _AUDIT_FIELD_KEY = "_AUDIT_FIELD_KEY";
	public static final String _AUDIT_FIELD_LADDR = "_AUDIT_FIELD_LADDR";
	public static final String _AUDIT_FIELD_LPORT = "_AUDIT_FIELD_LPORT";
	public static final String _AUDIT_FIELD_MODE = "_AUDIT_FIELD_MODE";
	public static final String _AUDIT_FIELD_NAME = "_AUDIT_FIELD_NAME";
	public static final String AUDIT_FIELD_NAME = "AUDIT_FIELD_NAME";
	public static final String _AUDIT_FIELD_NAMETYPE = "_AUDIT_FIELD_NAMETYPE";
	public static final String _AUDIT_FIELD_NL_MCGRP = "_AUDIT_FIELD_NL_MCGRP";
	public static final String _AUDIT_FIELD_OGID = "_AUDIT_FIELD_OGID";
	public static final String _AUDIT_FIELD_OLD = "_AUDIT_FIELD_OLD";
	public static final String _AUDIT_FIELD_OP = "_AUDIT_FIELD_OP";
	public static final String AUDIT_FIELD_OP = "AUDIT_FIELD_OP";
	public static final String _AUDIT_FIELD_OPERATION = "_AUDIT_FIELD_OPERATION";
	public static final String _AUDIT_FIELD_OUID = "_AUDIT_FIELD_OUID";
	public static final String _AUDIT_FIELD_PEER = "_AUDIT_FIELD_PEER";
	public static final String AUDIT_FIELD_PID = "AUDIT_FIELD_PID";
	public static final String _AUDIT_FIELD_PROFILE = "_AUDIT_FIELD_PROFILE";
	public static final String _AUDIT_FIELD_PROG_ID = "_AUDIT_FIELD_PROG_ID";
	public static final String _AUDIT_FIELD_PROTOCOL = "_AUDIT_FIELD_PROTOCOL";
	public static final String _AUDIT_FIELD_RDEV = "_AUDIT_FIELD_RDEV";
	public static final String _AUDIT_FIELD_REQUESTED_MASK = "_AUDIT_FIELD_REQUESTED_MASK";
	public static final String _AUDIT_FIELD_RES = "_AUDIT_FIELD_RES";
	public static final String AUDIT_FIELD_RES = "AUDIT_FIELD_RES";
	public static final String AUDIT_FIELD_RESULT = "AUDIT_FIELD_RESULT";
	public static final String _AUDIT_FIELD_SADDR = "_AUDIT_FIELD_SADDR";
	public static final String _AUDIT_FIELD_SGID = "_AUDIT_FIELD_SGID";
	public static final String _AUDIT_FIELD_SIG = "_AUDIT_FIELD_SIG";
	public static final String _AUDIT_FIELD_SIGNAL = "_AUDIT_FIELD_SIGNAL";
	public static final String _AUDIT_FIELD_SOCK_TYPE = "_AUDIT_FIELD_SOCK_TYPE";
	public static final String _AUDIT_FIELD_SUCCESS = "_AUDIT_FIELD_SUCCESS";
	public static final String _AUDIT_FIELD_SUID = "_AUDIT_FIELD_SUID";
	public static final String _AUDIT_FIELD_SYSCALL = "_AUDIT_FIELD_SYSCALL";
	public static final String _AUDIT_FIELD_TABLE = "_AUDIT_FIELD_TABLE";
	public static final String _AUDIT_FIELD_TARGET = "_AUDIT_FIELD_TARGET";
	public static final String AUDIT_FIELD_TERMINAL = "AUDIT_FIELD_TERMINAL";
	public static final String AUDIT_FIELD_UID = "AUDIT_FIELD_UID";
	public static final String AUDIT_FIELD_UNIT = "AUDIT_FIELD_UNIT";
	public static final String AUDIT_FIELD_UUID = "AUDIT_FIELD_UUID";
	public static final String _AUDIT_ID = "_AUDIT_ID";
	public static final String _AUDIT_LOGINUID = "_AUDIT_LOGINUID";
	public static final String _AUDIT_SESSION = "_AUDIT_SESSION";
	public static final String _AUDIT_TYPE = "_AUDIT_TYPE";
	public static final String _AUDIT_TYPE_NAME = "_AUDIT_TYPE_NAME";
	public static final String AVAILABLE = "AVAILABLE";
	public static final String AVAILABLE_PRETTY = "AVAILABLE_PRETTY";
	public static final String _BOOT_ID = "_BOOT_ID";
	public static final String _BOOT_ID_VALUE = "_BOOT_ID VALUE";
	public static final String _CAP_EFFECTIVE = "_CAP_EFFECTIVE";
	public static final String _CMDLINE = "_CMDLINE";
	public static final String CODE_FILE = "CODE_FILE";
	public static final String CODE_FUNC = "CODE_FUNC";
	public static final String CODE_LINE = "CODE_LINE";
	public static final String COMMAND = "COMMAND";
	public static final String _COMM = "_COMM";
	public static final String CONFIG_FILE = "CONFIG_FILE";
	public static final String CONFIG_LINE = "CONFIG_LINE";
	public static final String COREDUMP_CGROUP = "COREDUMP_CGROUP";
	public static final String COREDUMP_CMDLINE = "COREDUMP_CMDLINE";
	public static final String COREDUMP_COMM = "COREDUMP_COMM";
	public static final String COREDUMP_CWD = "COREDUMP_CWD";
	public static final String COREDUMP_ENVIRON = "COREDUMP_ENVIRON";
	public static final String COREDUMP_EXE = "COREDUMP_EXE";
	public static final String COREDUMP_FILENAME = "COREDUMP_FILENAME";
	public static final String COREDUMP_GID = "COREDUMP_GID";
	public static final String COREDUMP_HOSTNAME = "COREDUMP_HOSTNAME";
	public static final String COREDUMP_OPEN_FDS = "COREDUMP_OPEN_FDS";
	public static final String COREDUMP_OWNER_UID = "COREDUMP_OWNER_UID";
	public static final String COREDUMP_PID = "COREDUMP_PID";
	public static final String COREDUMP_PROC_CGROUP = "COREDUMP_PROC_CGROUP";
	public static final String COREDUMP_PROC_LIMITS = "COREDUMP_PROC_LIMITS";
	public static final String COREDUMP_PROC_MAPS = "COREDUMP_PROC_MAPS";
	public static final String COREDUMP_PROC_MOUNTINFO = "COREDUMP_PROC_MOUNTINFO";
	public static final String COREDUMP_PROC_STATUS = "COREDUMP_PROC_STATUS";
	public static final String COREDUMP_RLIMIT = "COREDUMP_RLIMIT";
	public static final String COREDUMP_ROOT = "COREDUMP_ROOT";
	public static final String COREDUMP_SESSION = "COREDUMP_SESSION";
	public static final String COREDUMP_SIGNAL = "COREDUMP_SIGNAL";
	public static final String COREDUMP_SIGNAL_NAME = "COREDUMP_SIGNAL_NAME";
	public static final String COREDUMP_SLICE = "COREDUMP_SLICE";
	public static final String COREDUMP_TIMESTAMP = "COREDUMP_TIMESTAMP";
	public static final String COREDUMP_UID = "COREDUMP_UID";
	public static final String COREDUMP_UNIT = "COREDUMP_UNIT";
	public static final String CPU_USAGE_NSEC = "CPU_USAGE_NSEC";
	public static final String CURRENT_USE = "CURRENT_USE";
	public static final String CURRENT_USE_PRETTY = "CURRENT_USE_PRETTY";
	public static final String __CURSOR = "__CURSOR";// eventID
	public static final String DEVICE = "DEVICE";
	public static final String DISK_AVAILABLE = "DISK_AVAILABLE";
	public static final String DISK_AVAILABLE_PRETTY = "DISK_AVAILABLE_PRETTY";
	public static final String DISK_KEEP_FREE = "DISK_KEEP_FREE";
	public static final String DISK_KEEP_FREE_PRETTY = "DISK_KEEP_FREE_PRETTY";
	public static final String _EGID = "_EGID";
	public static final String _EUID = "_EUID";
	public static final String _EXE = "_EXE";
	public static final String EXIT_CODE = "EXIT_CODE";
	public static final String EXIT_STATUS = "EXIT_STATUS";
	public static final String _FSGID = "_FSGID";
	public static final String _FSUID = "_FSUID";
	public static final String _GID = "_GID";
	public static final String GLIB_DOMAIN = "GLIB_DOMAIN";
	public static final String GLIB_OLD_LOG_API = "GLIB_OLD_LOG_API";
	public static final String _HOSTNAME = "_HOSTNAME";
	public static final String INVOCATION_ID = "INVOCATION_ID";
	public static final String JOB_ID = "JOB_ID";
	public static final String JOB_RESULT = "JOB_RESULT";
	public static final String JOB_TYPE = "JOB_TYPE";
	public static final String JOURNAL_NAME = "JOURNAL_NAME";
	public static final String JOURNAL_PATH = "JOURNAL_PATH";
	public static final String _KERNEL_DEVICE = "_KERNEL_DEVICE";
	public static final String _KERNEL_SUBSYSTEM = "_KERNEL_SUBSYSTEM";
	public static final String KERNEL_USEC = "KERNEL_USEC";
	public static final String LEADER = "LEADER";
	public static final String LIMIT = "LIMIT";
	public static final String LIMIT_PRETTY = "LIMIT_PRETTY";
	public static final String _MACHINE_ID = "_MACHINE_ID";
	public static final String MAX_USE = "MAX_USE";
	public static final String MAX_USE_PRETTY = "MAX_USE_PRETTY";
	public static final String MESSAGE_ID = "MESSAGE_ID";
	public static final String MESSAGE = "MESSAGE";
	public static final String MESSAGE_VALUE = "MESSAGE VALUE";
	public static final String __MONOTONIC_TIMESTAMP = "__MONOTONIC_TIMESTAMP";
	public static final String NM_CONNECTION = "NM_CONNECTION";
	public static final String NM_DEVICE = "NM_DEVICE";
	public static final String NM_LOG_DOMAINS = "NM_LOG_DOMAINS";
	public static final String NM_LOG_LEVEL = "NM_LOG_LEVEL";
	public static final String N_RESTARTS = "N_RESTARTS";
	public static final String OFFENDING_USER = "OFFENDING_USER";
	public static final String _PID = "_PID";
	public static final String _PPID = "_PPID";
	public static final String PRIORITY = "PRIORITY";
	public static final String PULSE_BACKTRACE = "PULSE_BACKTRACE";
	public static final String QT_CATEGORY = "QT_CATEGORY";
	public static final String __REALTIME_TIMESTAMP = "__REALTIME_TIMESTAMP";
	public static final String SEAT_ID = "SEAT_ID";
	public static final String _SELINUX_CONTEXT = "_SELINUX_CONTEXT";
	public static final String SESSION_ID = "SESSION_ID";
	public static final String SHUTDOWN = "SHUTDOWN";
	public static final String _SOURCE_MONOTONIC_TIMESTAMP = "_SOURCE_MONOTONIC_TIMESTAMP";
	public static final String _SOURCE_REALTIME_TIMESTAMP = "_SOURCE_REALTIME_TIMESTAMP";
	public static final String _SOURCE_REALTIME_TIMESTAMP_VALUE = "_SOURCE_REALTIME_TIMESTAMP VALUE";
	public static final String _STREAM_ID = "_STREAM_ID";
	public static final String SYSLOG_FACILITY = "SYSLOG_FACILITY";
	public static final String SYSLOG_IDENTIFIER = "SYSLOG_IDENTIFIER";
	public static final String SYSLOG_PID = "SYSLOG_PID";
	public static final String SYSLOG_RAW = "SYSLOG_RAW";
	public static final String SYSLOG_TIMESTAMP = "SYSLOG_TIMESTAMP";
	public static final String SYSLOG_TIMESTAMP_VALUE = "SYSLOG_TIMESTAMP VALUE";
	public static final String _SYSTEMD_CGROUP = "_SYSTEMD_CGROUP";
	public static final String _SYSTEMD_INVOCATION_ID = "_SYSTEMD_INVOCATION_ID";
	public static final String _SYSTEMD_OWNER_UID = "_SYSTEMD_OWNER_UID";
	public static final String _SYSTEMD_SESSION = "_SYSTEMD_SESSION";
	public static final String _SYSTEMD_SLICE = "_SYSTEMD_SLICE";
	public static final String _SYSTEMD_UNIT = "_SYSTEMD_UNIT";
	public static final String _SYSTEMD_USER_SLICE = "_SYSTEMD_USER_SLICE";
	public static final String _SYSTEMD_USER_UNIT = "_SYSTEMD_USER_UNIT";
	public static final String THREAD_ID = "THREAD_ID";
	public static final String TID = "TID";
	public static final String TIMESTAMP_BOOTTIME = "TIMESTAMP_BOOTTIME";
	public static final String TIMESTAMP_MONOTONIC = "TIMESTAMP_MONOTONIC";
	public static final String _TRANSPORT = "_TRANSPORT";
	public static final String _TTY = "_TTY";
	public static final String _UDEV_DEVLINK = "_UDEV_DEVLINK";
	public static final String _UDEV_DEVNODE = "_UDEV_DEVNODE";
	public static final String _UDEV_SYSNAME = "_UDEV_SYSNAME";
	public static final String _UID = "_UID";
	public static final String UNIT_RESULT = "UNIT_RESULT";
	public static final String UNIT = "UNIT";
	public static final String USER_ID = "USER_ID";
	public static final String USER_INVOCATION_ID = "USER_INVOCATION_ID";
	public static final String USERSPACE_USEC = "USERSPACE_USEC";
	public static final String USER_UNIT = "USER_UNIT";

	private @Getter EventBus journalBus = new EventBus(this.getClass().getSimpleName());

	static final String regex = "^[ ]?[-]?[0-9]{1,}";

	public void init() {
		getMainEventBuss().register(this);
		try {
			ArrayList<String> listBoots = CommandUtils.executeCommandAsStringList("journalctl --list-boots");
			System.out.println("listBoots size:" + listBoots.size());

			for (String string : listBoots) {
				int idReal = parseId(string);

				ArrayList<String> outputList = CommandUtils
						.executeCommandAsStringList("journalctl -b " + idReal + " -o json");

				System.out.println("journalctl:idReal:" + idReal + " size:" + outputList.size());
				for (String output : outputList) {
					newLineReceived(output);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.err.println("#######################################################################");
		processCommand = "journalctl -f -o json";
	}

	private int parseId(String string) {
		final Pattern pattern = Pattern.compile(regex, Pattern.MULTILINE);
		final Matcher matcher = pattern.matcher(string);

		int id = 0;
		while (matcher.find()) {
			String idString = matcher.group(0);
			idString = idString.replaceAll("[ ]{1,}", "");
			id = Integer.parseInt(idString);
		}
		return id;
	}

	@Subscribe
	private void inputRaw(ProcessOutputEvent inputEvent) {
		String message = inputEvent.getMessage();
		JSONObject jsonObject = new JSONObject(message);

		JournalctlEvent journalctlEvent = new JournalctlEvent();
		HashMap<String, Object> resultHashMap = journalctlEvent.getParametersMap();
		for (String key : jsonObject.keySet()) {
			Object value = jsonObject.get(key);

			if (key.equals(__CURSOR)) {
				resultHashMap.put(__CURSOR, new JournalCURSORObject().parse((String) value));
			} else if (key.equals(_AUDIT_FIELD_A0)) {
				resultHashMap.put(_AUDIT_FIELD_A0, value);
			} else if (key.equals(_AUDIT_FIELD_A1)) {
				resultHashMap.put(_AUDIT_FIELD_A1, value);
			} else if (key.equals(_AUDIT_FIELD_A10)) {
				resultHashMap.put(_AUDIT_FIELD_A10, value);
			} else if (key.equals(_AUDIT_FIELD_A11)) {
				resultHashMap.put(_AUDIT_FIELD_A11, value);
			} else if (key.equals(_AUDIT_FIELD_A12)) {
				resultHashMap.put(_AUDIT_FIELD_A12, value);
			} else if (key.equals(_AUDIT_FIELD_A13)) {
				resultHashMap.put(_AUDIT_FIELD_A13, value);
			} else if (key.equals(_AUDIT_FIELD_A14)) {
				resultHashMap.put(_AUDIT_FIELD_A14, value);
			} else if (key.equals(_AUDIT_FIELD_A15)) {
				resultHashMap.put(_AUDIT_FIELD_A15, value);
			} else if (key.equals(_AUDIT_FIELD_A2)) {
				resultHashMap.put(_AUDIT_FIELD_A2, value);
			} else if (key.equals(_AUDIT_FIELD_A3)) {
				resultHashMap.put(_AUDIT_FIELD_A3, value);
			} else if (key.equals(_AUDIT_FIELD_A4)) {
				resultHashMap.put(_AUDIT_FIELD_A4, value);
			} else if (key.equals(_AUDIT_FIELD_A5)) {
				resultHashMap.put(_AUDIT_FIELD_A5, value);
			} else if (key.equals(_AUDIT_FIELD_A6)) {
				resultHashMap.put(_AUDIT_FIELD_A6, value);
			} else if (key.equals(_AUDIT_FIELD_A7)) {
				resultHashMap.put(_AUDIT_FIELD_A7, value);
			} else if (key.equals(_AUDIT_FIELD_A8)) {
				resultHashMap.put(_AUDIT_FIELD_A8, value);
			} else if (key.equals(_AUDIT_FIELD_A9)) {
				resultHashMap.put(_AUDIT_FIELD_A9, value);
			} else if (key.equals(AUDIT_FIELD_ACCT)) {
				resultHashMap.put(AUDIT_FIELD_ACCT, value);
			} else if (key.equals(AUDIT_FIELD_ADDR)) {
				resultHashMap.put(AUDIT_FIELD_ADDR, value);
			} else if (key.equals(_AUDIT_FIELD_APPARMOR)) {
				resultHashMap.put(_AUDIT_FIELD_APPARMOR, value);
			} else if (key.equals(_AUDIT_FIELD_ARCH)) {
				resultHashMap.put(_AUDIT_FIELD_ARCH, value);
			} else if (key.equals(_AUDIT_FIELD_ARGC)) {
				resultHashMap.put(_AUDIT_FIELD_ARGC, value);
			} else if (key.equals(_AUDIT_FIELD_AUDIT_ENABLED)) {
				resultHashMap.put(_AUDIT_FIELD_AUDIT_ENABLED, value);
			} else if (key.equals(_AUDIT_FIELD_AUDIT_PID)) {
				resultHashMap.put(_AUDIT_FIELD_AUDIT_PID, value);
			} else if (key.equals(_AUDIT_FIELD_CAPABILITY)) {
				resultHashMap.put(_AUDIT_FIELD_CAPABILITY, value);
			} else if (key.equals(_AUDIT_FIELD_CAP_FE)) {
				resultHashMap.put(_AUDIT_FIELD_CAP_FE, value);
			} else if (key.equals(_AUDIT_FIELD_CAP_FI)) {
				resultHashMap.put(_AUDIT_FIELD_CAP_FI, value);
			} else if (key.equals(_AUDIT_FIELD_CAP_FP)) {
				resultHashMap.put(_AUDIT_FIELD_CAP_FP, value);
			} else if (key.equals(_AUDIT_FIELD_CAP_FROOTID)) {
				resultHashMap.put(_AUDIT_FIELD_CAP_FROOTID, value);
			} else if (key.equals(_AUDIT_FIELD_CAP_FVER)) {
				resultHashMap.put(_AUDIT_FIELD_CAP_FVER, value);
			} else if (key.equals(_AUDIT_FIELD_CAPNAME)) {
				resultHashMap.put(_AUDIT_FIELD_CAPNAME, value);
			} else if (key.equals(AUDIT_FIELD_COMM)) {
				resultHashMap.put(AUDIT_FIELD_COMM, value);
			} else if (key.equals(_AUDIT_FIELD_CWD)) {
				resultHashMap.put(_AUDIT_FIELD_CWD, value);
			} else if (key.equals(_AUDIT_FIELD_DENIED_MASK)) {
				resultHashMap.put(_AUDIT_FIELD_DENIED_MASK, value);
			} else if (key.equals(_AUDIT_FIELD_DEV)) {
				resultHashMap.put(_AUDIT_FIELD_DEV, value);
			} else if (key.equals(_AUDIT_FIELD_ENTRIES)) {
				resultHashMap.put(_AUDIT_FIELD_ENTRIES, value);
			} else if (key.equals(_AUDIT_FIELD_ERROR)) {
				resultHashMap.put(_AUDIT_FIELD_ERROR, value);
			} else if (key.equals(AUDIT_FIELD_EXE)) {
				resultHashMap.put(AUDIT_FIELD_EXE, value);
			} else if (key.equals(_AUDIT_FIELD_EXIT)) {
				resultHashMap.put(_AUDIT_FIELD_EXIT, value);
			} else if (key.equals(_AUDIT_FIELD_FADDR)) {
				resultHashMap.put(_AUDIT_FIELD_FADDR, value);
			} else if (key.equals(_AUDIT_FIELD_FAMILY)) {
				resultHashMap.put(_AUDIT_FIELD_FAMILY, value);
			} else if (key.equals(_AUDIT_FIELD_FPORT)) {
				resultHashMap.put(_AUDIT_FIELD_FPORT, value);
			} else if (key.equals(AUDIT_FIELD_GRANTORS)) {
				resultHashMap.put(AUDIT_FIELD_GRANTORS, value);
			} else if (key.equals(AUDIT_FIELD_HOSTNAME)) {
				resultHashMap.put(AUDIT_FIELD_HOSTNAME, value);
			} else if (key.equals(_AUDIT_FIELD_INFO)) {
				resultHashMap.put(_AUDIT_FIELD_INFO, value);
			} else if (key.equals(_AUDIT_FIELD_INODE)) {
				resultHashMap.put(_AUDIT_FIELD_INODE, value);
			} else if (key.equals(_AUDIT_FIELD_ITEM)) {
				resultHashMap.put(_AUDIT_FIELD_ITEM, value);
			} else if (key.equals(_AUDIT_FIELD_ITEMS)) {
				resultHashMap.put(_AUDIT_FIELD_ITEMS, value);
			} else if (key.equals(_AUDIT_FIELD_KEY)) {
				resultHashMap.put(_AUDIT_FIELD_KEY, value);
			} else if (key.equals(_AUDIT_FIELD_LADDR)) {
				resultHashMap.put(_AUDIT_FIELD_LADDR, value);
			} else if (key.equals(_AUDIT_FIELD_LPORT)) {
				resultHashMap.put(_AUDIT_FIELD_LPORT, value);
			} else if (key.equals(_AUDIT_FIELD_MODE)) {
				resultHashMap.put(_AUDIT_FIELD_MODE, value);
			} else if (key.equals(_AUDIT_FIELD_NAME)) {
				resultHashMap.put(_AUDIT_FIELD_NAME, value);
			} else if (key.equals(AUDIT_FIELD_NAME)) {
				resultHashMap.put(AUDIT_FIELD_NAME, value);
			} else if (key.equals(_AUDIT_FIELD_NAMETYPE)) {
				resultHashMap.put(_AUDIT_FIELD_NAMETYPE, value);
			} else if (key.equals(_AUDIT_FIELD_NL_MCGRP)) {
				resultHashMap.put(_AUDIT_FIELD_NL_MCGRP, value);
			} else if (key.equals(_AUDIT_FIELD_OGID)) {
				resultHashMap.put(_AUDIT_FIELD_OGID, value);
			} else if (key.equals(_AUDIT_FIELD_OLD)) {
				resultHashMap.put(_AUDIT_FIELD_OLD, value);
			} else if (key.equals(_AUDIT_FIELD_OP)) {
				resultHashMap.put(_AUDIT_FIELD_OP, value);
			} else if (key.equals(AUDIT_FIELD_OP)) {
				resultHashMap.put(AUDIT_FIELD_OP, value);
			} else if (key.equals(_AUDIT_FIELD_OPERATION)) {
				resultHashMap.put(_AUDIT_FIELD_OPERATION, value);
			} else if (key.equals(_AUDIT_FIELD_OUID)) {
				resultHashMap.put(_AUDIT_FIELD_OUID, value);
			} else if (key.equals(_AUDIT_FIELD_PEER)) {
				resultHashMap.put(_AUDIT_FIELD_PEER, value);
			} else if (key.equals(AUDIT_FIELD_PID)) {
				resultHashMap.put(AUDIT_FIELD_PID, value);
			} else if (key.equals(_AUDIT_FIELD_PROFILE)) {
				resultHashMap.put(_AUDIT_FIELD_PROFILE, value);
			} else if (key.equals(_AUDIT_FIELD_PROG_ID)) {
				resultHashMap.put(_AUDIT_FIELD_PROG_ID, value);
			} else if (key.equals(_AUDIT_FIELD_PROTOCOL)) {
				resultHashMap.put(_AUDIT_FIELD_PROTOCOL, value);
			} else if (key.equals(_AUDIT_FIELD_RDEV)) {
				resultHashMap.put(_AUDIT_FIELD_RDEV, value);
			} else if (key.equals(_AUDIT_FIELD_REQUESTED_MASK)) {
				resultHashMap.put(_AUDIT_FIELD_REQUESTED_MASK, value);
			} else if (key.equals(_AUDIT_FIELD_RES)) {
				resultHashMap.put(_AUDIT_FIELD_RES, value);
			} else if (key.equals(AUDIT_FIELD_RES)) {
				resultHashMap.put(AUDIT_FIELD_RES, value);
			} else if (key.equals(AUDIT_FIELD_RESULT)) {
				resultHashMap.put(AUDIT_FIELD_RESULT, value);
			} else if (key.equals(_AUDIT_FIELD_SADDR)) {
				resultHashMap.put(_AUDIT_FIELD_SADDR, value);
			} else if (key.equals(_AUDIT_FIELD_SGID)) {
				resultHashMap.put(_AUDIT_FIELD_SGID, value);
			} else if (key.equals(_AUDIT_FIELD_SIG)) {
				resultHashMap.put(_AUDIT_FIELD_SIG, value);
			} else if (key.equals(_AUDIT_FIELD_SIGNAL)) {
				resultHashMap.put(_AUDIT_FIELD_SIGNAL, value);
			} else if (key.equals(_AUDIT_FIELD_SOCK_TYPE)) {
				resultHashMap.put(_AUDIT_FIELD_SOCK_TYPE, value);
			} else if (key.equals(_AUDIT_FIELD_SUCCESS)) {
				resultHashMap.put(_AUDIT_FIELD_SUCCESS, value);
			} else if (key.equals(_AUDIT_FIELD_SUID)) {
				resultHashMap.put(_AUDIT_FIELD_SUID, value);
			} else if (key.equals(_AUDIT_FIELD_SYSCALL)) {
				resultHashMap.put(_AUDIT_FIELD_SYSCALL, value);
			} else if (key.equals(_AUDIT_FIELD_TABLE)) {
				resultHashMap.put(_AUDIT_FIELD_TABLE, value);
			} else if (key.equals(_AUDIT_FIELD_TARGET)) {
				resultHashMap.put(_AUDIT_FIELD_TARGET, value);
			} else if (key.equals(AUDIT_FIELD_TERMINAL)) {
				resultHashMap.put(AUDIT_FIELD_TERMINAL, value);
			} else if (key.equals(AUDIT_FIELD_UID)) {
				resultHashMap.put(AUDIT_FIELD_UID, value);
			} else if (key.equals(AUDIT_FIELD_UNIT)) {
				resultHashMap.put(AUDIT_FIELD_UNIT, value);
			} else if (key.equals(AUDIT_FIELD_UUID)) {
				resultHashMap.put(AUDIT_FIELD_UUID, value);
			} else if (key.equals(_AUDIT_ID)) {
				resultHashMap.put(_AUDIT_ID, value);
			} else if (key.equals(_AUDIT_LOGINUID)) {
				resultHashMap.put(_AUDIT_LOGINUID, value);
			} else if (key.equals(_AUDIT_SESSION)) {
				resultHashMap.put(_AUDIT_SESSION, value);
			} else if (key.equals(_AUDIT_TYPE)) {
				resultHashMap.put(_AUDIT_TYPE, value);
			} else if (key.equals(_AUDIT_TYPE_NAME)) {
				resultHashMap.put(_AUDIT_TYPE_NAME, value);
			} else if (key.equals(AVAILABLE)) {
				resultHashMap.put(AVAILABLE, value);
			} else if (key.equals(AVAILABLE_PRETTY)) {
				resultHashMap.put(AVAILABLE_PRETTY, value);
			} else if (key.equals(_BOOT_ID)) {
				resultHashMap.put(_BOOT_ID, value);
			} else if (key.equals(_BOOT_ID_VALUE)) {
				resultHashMap.put(_BOOT_ID_VALUE, value);
			} else if (key.equals(_CAP_EFFECTIVE)) {
				resultHashMap.put(_CAP_EFFECTIVE, value);
			} else if (key.equals(_CMDLINE)) {
				resultHashMap.put(_CMDLINE, value);
			} else if (key.equals(CODE_FILE)) {
				resultHashMap.put(CODE_FILE, value);
			} else if (key.equals(CODE_FUNC)) {
				resultHashMap.put(CODE_FUNC, value);
			} else if (key.equals(CODE_LINE)) {
				resultHashMap.put(CODE_LINE, value);
			} else if (key.equals(_COMM)) {
				resultHashMap.put(_COMM, value);
			} else if (key.equals(COMMAND)) {
				resultHashMap.put(COMMAND, value);
			} else if (key.equals(CONFIG_FILE)) {
				resultHashMap.put(CONFIG_FILE, value);
			} else if (key.equals(CONFIG_LINE)) {
				resultHashMap.put(CONFIG_LINE, value);
			} else if (key.equals(COREDUMP_CGROUP)) {
				resultHashMap.put(COREDUMP_CGROUP, value);
			} else if (key.equals(COREDUMP_CMDLINE)) {
				resultHashMap.put(COREDUMP_CMDLINE, value);
			} else if (key.equals(COREDUMP_COMM)) {
				resultHashMap.put(COREDUMP_COMM, value);
			} else if (key.equals(COREDUMP_CWD)) {
				resultHashMap.put(COREDUMP_CWD, value);
			} else if (key.equals(COREDUMP_ENVIRON)) {
				resultHashMap.put(COREDUMP_ENVIRON, value);
			} else if (key.equals(COREDUMP_EXE)) {
				resultHashMap.put(COREDUMP_EXE, value);
			} else if (key.equals(COREDUMP_FILENAME)) {
				resultHashMap.put(COREDUMP_FILENAME, value);
			} else if (key.equals(COREDUMP_GID)) {
				resultHashMap.put(COREDUMP_GID, value);
			} else if (key.equals(COREDUMP_HOSTNAME)) {
				resultHashMap.put(COREDUMP_HOSTNAME, value);
			} else if (key.equals(COREDUMP_OPEN_FDS)) {
				resultHashMap.put(COREDUMP_OPEN_FDS, value);
			} else if (key.equals(COREDUMP_OWNER_UID)) {
				resultHashMap.put(COREDUMP_OWNER_UID, value);
			} else if (key.equals(COREDUMP_PID)) {
				resultHashMap.put(COREDUMP_PID, value);
			} else if (key.equals(COREDUMP_PROC_CGROUP)) {
				resultHashMap.put(COREDUMP_PROC_CGROUP, value);
			} else if (key.equals(COREDUMP_PROC_LIMITS)) {
				resultHashMap.put(COREDUMP_PROC_LIMITS, value);
			} else if (key.equals(COREDUMP_PROC_MAPS)) {
				resultHashMap.put(COREDUMP_PROC_MAPS, value);
			} else if (key.equals(COREDUMP_PROC_MOUNTINFO)) {
				resultHashMap.put(COREDUMP_PROC_MOUNTINFO, value);
			} else if (key.equals(COREDUMP_PROC_STATUS)) {
				resultHashMap.put(COREDUMP_PROC_STATUS, value);
			} else if (key.equals(COREDUMP_RLIMIT)) {
				resultHashMap.put(COREDUMP_RLIMIT, value);
			} else if (key.equals(COREDUMP_ROOT)) {
				resultHashMap.put(COREDUMP_ROOT, value);
			} else if (key.equals(COREDUMP_SESSION)) {
				resultHashMap.put(COREDUMP_SESSION, value);
			} else if (key.equals(COREDUMP_SIGNAL)) {
				resultHashMap.put(COREDUMP_SIGNAL, value);
			} else if (key.equals(COREDUMP_SIGNAL_NAME)) {
				resultHashMap.put(COREDUMP_SIGNAL_NAME, value);
			} else if (key.equals(COREDUMP_SLICE)) {
				resultHashMap.put(COREDUMP_SLICE, value);
			} else if (key.equals(COREDUMP_TIMESTAMP)) {
				resultHashMap.put(COREDUMP_TIMESTAMP, value);
			} else if (key.equals(COREDUMP_UID)) {
				resultHashMap.put(COREDUMP_UID, value);
			} else if (key.equals(COREDUMP_UNIT)) {
				resultHashMap.put(COREDUMP_UNIT, value);
			} else if (key.equals(CPU_USAGE_NSEC)) {
				resultHashMap.put(CPU_USAGE_NSEC, value);
			} else if (key.equals(CURRENT_USE)) {
				resultHashMap.put(CURRENT_USE, value);
			} else if (key.equals(CURRENT_USE_PRETTY)) {
				resultHashMap.put(CURRENT_USE_PRETTY, value);
			} else if (key.equals(DEVICE)) {
				resultHashMap.put(DEVICE, value);
			} else if (key.equals(DISK_AVAILABLE)) {
				resultHashMap.put(DISK_AVAILABLE, value);
			} else if (key.equals(DISK_AVAILABLE_PRETTY)) {
				resultHashMap.put(DISK_AVAILABLE_PRETTY, value);
			} else if (key.equals(DISK_KEEP_FREE)) {
				resultHashMap.put(DISK_KEEP_FREE, value);
			} else if (key.equals(DISK_KEEP_FREE_PRETTY)) {
				resultHashMap.put(DISK_KEEP_FREE_PRETTY, value);
			} else if (key.equals(_EGID)) {
				resultHashMap.put(_EGID, value);
			} else if (key.equals(_EUID)) {
				resultHashMap.put(_EUID, value);
			} else if (key.equals(_EXE)) {
				resultHashMap.put(_EXE, value);
			} else if (key.equals(EXIT_CODE)) {
				resultHashMap.put(EXIT_CODE, value);
			} else if (key.equals(EXIT_STATUS)) {
				resultHashMap.put(EXIT_STATUS, value);
			} else if (key.equals(_FSGID)) {
				resultHashMap.put(_FSGID, value);
			} else if (key.equals(_FSUID)) {
				resultHashMap.put(_FSUID, value);
			} else if (key.equals(_GID)) {
				resultHashMap.put(_GID, value);
			} else if (key.equals(GLIB_DOMAIN)) {
				resultHashMap.put(GLIB_DOMAIN, value);
			} else if (key.equals(GLIB_OLD_LOG_API)) {
				resultHashMap.put(GLIB_OLD_LOG_API, value);
			} else if (key.equals(_HOSTNAME)) {
				resultHashMap.put(_HOSTNAME, value);
			} else if (key.equals(INVOCATION_ID)) {
				resultHashMap.put(INVOCATION_ID, value);
			} else if (key.equals(JOB_ID)) {
				resultHashMap.put(JOB_ID, value);
			} else if (key.equals(JOB_RESULT)) {
				resultHashMap.put(JOB_RESULT, value);
			} else if (key.equals(JOB_TYPE)) {
				resultHashMap.put(JOB_TYPE, value);
			} else if (key.equals(JOURNAL_NAME)) {
				resultHashMap.put(JOURNAL_NAME, value);
			} else if (key.equals(JOURNAL_PATH)) {
				resultHashMap.put(JOURNAL_PATH, value);
			} else if (key.equals(_KERNEL_DEVICE)) {
				resultHashMap.put(_KERNEL_DEVICE, value);
			} else if (key.equals(_KERNEL_SUBSYSTEM)) {
				resultHashMap.put(_KERNEL_SUBSYSTEM, value);
			} else if (key.equals(KERNEL_USEC)) {
				resultHashMap.put(KERNEL_USEC, value);
			} else if (key.equals(LEADER)) {
				resultHashMap.put(LEADER, value);
			} else if (key.equals(LIMIT)) {
				resultHashMap.put(LIMIT, value);
			} else if (key.equals(LIMIT_PRETTY)) {
				resultHashMap.put(LIMIT_PRETTY, value);
			} else if (key.equals(_MACHINE_ID)) {
				resultHashMap.put(_MACHINE_ID, value);
			} else if (key.equals(MAX_USE)) {
				resultHashMap.put(MAX_USE, value);
			} else if (key.equals(MAX_USE_PRETTY)) {
				resultHashMap.put(MAX_USE_PRETTY, value);
			} else if (key.equals(MESSAGE)) {
				resultHashMap.put(MESSAGE, value);
			} else if (key.equals(MESSAGE_ID)) {
				resultHashMap.put(MESSAGE_ID, value);
			} else if (key.equals(MESSAGE_VALUE)) {
				resultHashMap.put(MESSAGE_VALUE, value);
			} else if (key.equals(__MONOTONIC_TIMESTAMP)) {
				resultHashMap.put(__MONOTONIC_TIMESTAMP, value);
			} else if (key.equals(NM_CONNECTION)) {
				resultHashMap.put(NM_CONNECTION, value);
			} else if (key.equals(NM_DEVICE)) {
				resultHashMap.put(NM_DEVICE, value);
			} else if (key.equals(NM_LOG_DOMAINS)) {
				resultHashMap.put(NM_LOG_DOMAINS, value);
			} else if (key.equals(NM_LOG_LEVEL)) {
				resultHashMap.put(NM_LOG_LEVEL, value);
			} else if (key.equals(N_RESTARTS)) {
				resultHashMap.put(N_RESTARTS, value);
			} else if (key.equals(OFFENDING_USER)) {
				resultHashMap.put(OFFENDING_USER, value);
			} else if (key.equals(_PID)) {
				resultHashMap.put(_PID, value);
			} else if (key.equals(_PPID)) {
				resultHashMap.put(_PPID, value);
			} else if (key.equals(PRIORITY)) {
				resultHashMap.put(PRIORITY, value);
			} else if (key.equals(PULSE_BACKTRACE)) {
				resultHashMap.put(PULSE_BACKTRACE, value);
			} else if (key.equals(QT_CATEGORY)) {
				resultHashMap.put(QT_CATEGORY, value);
			} else if (key.equals(__REALTIME_TIMESTAMP)) {
				resultHashMap.put(__REALTIME_TIMESTAMP, value);
			} else if (key.equals(SEAT_ID)) {
				resultHashMap.put(SEAT_ID, value);
			} else if (key.equals(_SELINUX_CONTEXT)) {
				resultHashMap.put(_SELINUX_CONTEXT, value);
			} else if (key.equals(SESSION_ID)) {
				resultHashMap.put(SESSION_ID, value);
			} else if (key.equals(SHUTDOWN)) {
				resultHashMap.put(SHUTDOWN, value);
			} else if (key.equals(_SOURCE_MONOTONIC_TIMESTAMP)) {
				resultHashMap.put(_SOURCE_MONOTONIC_TIMESTAMP, value);
			} else if (key.equals(_SOURCE_REALTIME_TIMESTAMP)) {
				resultHashMap.put(_SOURCE_REALTIME_TIMESTAMP, value);
			} else if (key.equals(_SOURCE_REALTIME_TIMESTAMP_VALUE)) {
				resultHashMap.put(_SOURCE_REALTIME_TIMESTAMP_VALUE, value);
			} else if (key.equals(_STREAM_ID)) {
				resultHashMap.put(_STREAM_ID, value);
			} else if (key.equals(SYSLOG_FACILITY)) {
				resultHashMap.put(SYSLOG_FACILITY, value);
			} else if (key.equals(SYSLOG_IDENTIFIER)) {
				resultHashMap.put(SYSLOG_IDENTIFIER, value);
			} else if (key.equals(SYSLOG_PID)) {
				resultHashMap.put(SYSLOG_PID, value);
			} else if (key.equals(SYSLOG_RAW)) {
				resultHashMap.put(SYSLOG_RAW, value);
			} else if (key.equals(SYSLOG_TIMESTAMP)) {
				resultHashMap.put(SYSLOG_TIMESTAMP, value);
			} else if (key.equals(SYSLOG_TIMESTAMP_VALUE)) {
				resultHashMap.put(SYSLOG_TIMESTAMP_VALUE, value);
			} else if (key.equals(_SYSTEMD_CGROUP)) {
				resultHashMap.put(_SYSTEMD_CGROUP, value);
			} else if (key.equals(_SYSTEMD_INVOCATION_ID)) {
				resultHashMap.put(_SYSTEMD_INVOCATION_ID, value);
			} else if (key.equals(_SYSTEMD_OWNER_UID)) {
				resultHashMap.put(_SYSTEMD_OWNER_UID, value);
			} else if (key.equals(_SYSTEMD_SESSION)) {
				resultHashMap.put(_SYSTEMD_SESSION, value);
			} else if (key.equals(_SYSTEMD_SLICE)) {
				resultHashMap.put(_SYSTEMD_SLICE, value);
			} else if (key.equals(_SYSTEMD_UNIT)) {
				resultHashMap.put(_SYSTEMD_UNIT, value);
			} else if (key.equals(_SYSTEMD_USER_SLICE)) {
				resultHashMap.put(_SYSTEMD_USER_SLICE, value);
			} else if (key.equals(_SYSTEMD_USER_UNIT)) {
				resultHashMap.put(_SYSTEMD_USER_UNIT, value);
			} else if (key.equals(THREAD_ID)) {
				resultHashMap.put(THREAD_ID, value);
			} else if (key.equals(TID)) {
				resultHashMap.put(TID, value);
			} else if (key.equals(TIMESTAMP_BOOTTIME)) {
				resultHashMap.put(TIMESTAMP_BOOTTIME, value);
			} else if (key.equals(TIMESTAMP_MONOTONIC)) {
				resultHashMap.put(TIMESTAMP_MONOTONIC, value);
			} else if (key.equals(_TRANSPORT)) {
				resultHashMap.put(_TRANSPORT, value);
			} else if (key.equals(_TTY)) {
				resultHashMap.put(_TTY, value);
			} else if (key.equals(_UDEV_DEVLINK)) {
				resultHashMap.put(_UDEV_DEVLINK, value);
			} else if (key.equals(_UDEV_DEVNODE)) {
				resultHashMap.put(_UDEV_DEVNODE, value);
			} else if (key.equals(_UDEV_SYSNAME)) {
				resultHashMap.put(_UDEV_SYSNAME, value);
			} else if (key.equals(_UID)) {
				resultHashMap.put(_UID, value);
			} else if (key.equals(UNIT)) {
				resultHashMap.put(UNIT, value);
			} else if (key.equals(UNIT_RESULT)) {
				resultHashMap.put(UNIT_RESULT, value);
			} else if (key.equals(USER_ID)) {
				resultHashMap.put(USER_ID, value);
			} else if (key.equals(USER_INVOCATION_ID)) {
				resultHashMap.put(USER_INVOCATION_ID, value);
			} else if (key.equals(USERSPACE_USEC)) {
				resultHashMap.put(USERSPACE_USEC, value);
			} else if (key.equals(USER_UNIT)) {
				resultHashMap.put(USER_UNIT, value);
			}

			else {
				System.err.println("JournalctlListener KEY:[" + key + "] VALUE:" + value);
			}
		}

		journalBus.post(journalctlEvent);

	}
}
