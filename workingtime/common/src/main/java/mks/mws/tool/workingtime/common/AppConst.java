package mks.mws.tool.workingtime.common;

import static java.util.Map.entry;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;

public class AppConst {
	/**
	 * On development environment such as Windows + Eclispe IDE + Tomcat, temporary path is 
	 */
	public final static Path TMP_PATH = Paths.get(System.getProperty("java.io.tmpdir"), "searchmos");

	public final static String[] STATUS_NAMES = {"Open", "Doing", "Done"};
	
	/**  */
	public final static int STATUS_OPEN= 0;
	public final static int STATUS_DOING = 1;
	public final static int STATUS_DONE = 2;

	public final static Map<String, Integer> STATUS_MAP = Map.ofEntries(
		    entry(STATUS_NAMES[STATUS_OPEN], STATUS_OPEN),
		    entry(STATUS_NAMES[STATUS_DOING], STATUS_DOING),
		    entry(STATUS_NAMES[STATUS_DONE], STATUS_DONE)
		);
}
