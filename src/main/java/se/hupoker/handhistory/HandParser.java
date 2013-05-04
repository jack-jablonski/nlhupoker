package se.hupoker.handhistory;


import se.hupoker.handhistory.fulltilt.FtpParser;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * 
 * @author Alexander Nyberg
 *
 */
public class HandParser {
	private final Charset charset = Charset.forName("latin1");
    private final List<HeadsUp> handList = new ArrayList<>();

    public Collection<HeadsUp> getHandList() {
        return Collections.unmodifiableCollection(handList);
    }

	/**
	 * 
	 * @param file
	 * @throws IOException
	 */
	private void parseFile(Path file) throws IOException {
		System.out.println(file);

		try (BufferedReader br = Files.newBufferedReader(file, charset)) {
			FtpParser ftpParser = new FtpParser();
			ftpParser.parseFile(br);

			List<HeadsUp> hh = ftpParser.getResults();
			handList.addAll(hh);

			System.out.println("Parsed #" + hh.size() + " Total #" + handList.size());
		}
	}

	/**
	 * 
	 * @param dir Assumed directory.
	 */
	private void parseDirectory(Path dir) {
		try (DirectoryStream<Path> dirStream = Files.newDirectoryStream(dir)) {
		    for (Path file: dirStream) {
		    	if (Files.isRegularFile(file)) {
		    		parseFile(file);
		    	} else if (Files.isDirectory(file)) {
		    		parseDirectory(file);
		    	}
		    }
		} catch (IOException | DirectoryIteratorException e) {
		    // IOException can never be thrown by the boarditerator.
		    // In this snippet, it can only be thrown by newDirectoryStream.
		    System.err.println(e);
		}
	}

	/**
	 * 
	 * @param directory
	 */
	public void parse(String directory) {
		Path dir = Paths.get(directory);

		if (Files.isDirectory(dir)) {
			parseDirectory(dir);
		}
	}
}