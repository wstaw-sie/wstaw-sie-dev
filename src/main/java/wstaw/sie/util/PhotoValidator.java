package wstaw.sie.util;

import java.awt.image.BufferedImage;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.web.multipart.MultipartFile;

public class PhotoValidator {
	
	public static final Set<String> acceptedFileExtensions = Stream.of("jpg", "jpeg", "png", "gif").collect(Collectors.toCollection(HashSet::new));
	public static final long MAX_FILE_LENGTH_IN_BYTES = 500 * 1024; 
	public static final int MAX_WIDTH = 500;
	public static final int MAX_HEIGHT = 500;
	
	public static boolean isFileAPhoto(MultipartFile myFile)
	{
		String fileExtension = FilenameExtensionExtractor.extractFileExtension(myFile.getOriginalFilename());
		if(null != myFile && isContentTypeOk(myFile.getContentType()) && isFileExtensionOk(fileExtension))
		{
			return true;
		}
		return false;
	}
	
	private static boolean isContentTypeOk(String contentType)
	{
		return contentType != null && contentType.startsWith("image");
	}
	
	private static boolean isFileExtensionOk(String fileExtension)
	{
		return fileExtension != null && acceptedFileExtensions.contains(fileExtension);
	}
	
	public static boolean hasImageAcceptableDimensions(BufferedImage bufferedImage)
	{
		return null != bufferedImage && bufferedImage.getHeight() <= MAX_HEIGHT && bufferedImage.getWidth() <= MAX_WIDTH;
	}
	
	public static boolean isSizeAccetable (long size)
	{
		return size < MAX_FILE_LENGTH_IN_BYTES;
	}
}

