package se.hupoker.handhistory;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.Collection;
import java.util.List;

/**
 * 
 * @author Alexander Nyberg
 *
 */
class HistoryMain {
	/*
	 * args[0] is the path of the directory to parse
	 */
	public static void main(String[] args) {
		if (args.length < 1) {
			System.out.println("Not enough input parameters");
			return;
		}

		HandParser par = new HandParser();
		par.parse(args[0]);
		Collection<HeadsUp> hh = par.getHandList();

		try {
	         FileOutputStream fileOut = new FileOutputStream("hh.ser");
	         ObjectOutputStream out = new ObjectOutputStream(fileOut);

	         out.writeObject(hh);
	         out.close();
	         fileOut.close();
	      } catch(IOException e) {
	          e.printStackTrace();
	      }
	}
}