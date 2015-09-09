package pageMonitor3.errorClassifaction;

import java.io.File;
import java.io.FileFilter;

public class isNewError implements FileFilter {

	@Override
	public boolean accept(File arg0) {
		String fileName = arg0.getName();
		String[] peices = fileName.split("\\.");
		if (peices.length > 0 && peices[1].equals("html"))
			return true;
		else
			return false;
	}

}
