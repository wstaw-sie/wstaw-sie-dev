package wstaw.sie.util;

public class FilenameExtensionExtractor {

	public static String extractFileExtension (String myFilename)
	{
		String[] filename = myFilename.split("\\.");
		String fileExtension = filename.length > 1 ? filename[filename.length-1] : null;
		return fileExtension;
	}
}
