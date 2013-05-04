package se.hupoker.handhistory.fulltilt;


import se.hupoker.handhistory.Formatter;
import se.hupoker.handhistory.HeadsUp;
import se.hupoker.handhistory.IllegalHandException;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;

/**
 * 
 * @author Alexander Nyberg
 *
 */
public class FtpParser {
    /*
    * The different parsing states in a FTP hand.
    */
	private enum State { Description, Preamble, Actions, Showdown, Done, Error; }
    private State parseState = State.Description;
	private final List<HeadsUp> results = new ArrayList<>();

    public List<HeadsUp> getResults() {
        return results;
    }

	/**
	 * See if we have an introduction to a hand.
	 * 
	 * @param line
	 * @param hand Current hand.
	 * @return Good beginning of a new hand.
	 */
	private boolean parseDescription(String line, HeadsUp hand) {
		Matcher matcher;

		if (line.contains("partial")) {
			return false;
		}

		matcher = FtpRegex.descPattern.matcher(line);
		if (matcher.find()) {
			//System.out.println(matcher.group(0));
		
			//hand.setSite("FtpParser");
			hand.setHandId(matcher.group(1));
			//hand.setTableName(matcher.group(2));
			hand.setSmallBlind(Formatter.toDecimal(matcher.group(3)));
			hand.setBigBlind(Formatter.toDecimal(matcher.group(4)));
			//hand.setTime(matcher.group(6));
			//hand.setDate(matcher.group(7));

			parseState = State.Preamble;

			return true;
		}

		return false;
	}

	/**
	 * 
	 * @param line
	 * @param hand
	 * @return Any Preamble regex matched.
	 */
	private boolean parsePreamble(String line, HeadsUp hand) throws IllegalHandException {
		Matcher matcher;

		matcher = FtpRegex.seatPattern.matcher(line);
		if (matcher.find()) {
//			System.out.println(matcher.group(0));
			String name = matcher.group(2);
		
			Integer seat = new Integer(matcher.group(1));
			hand.addPlayer(seat, name, matcher.group(3));

			return true;
		}

		matcher = FtpRegex.buttonPattern.matcher(line);
		if (matcher.find()) {
			//System.out.println(matcher.group(0));
			Integer seat = new Integer(matcher.group(1));
			hand.setButton(seat);

			return true;
		}

		matcher = FtpRegex.postsPattern.matcher(line);
		if (matcher.find()) {
			//System.out.println(matcher.group(0));
			String bbtype = matcher.group(2);
			
			// Pretty useless for HU
			if (bbtype.contentEquals("small")) {
			} else if (bbtype.contentEquals("big")) {
			} else {
				System.out.println("posts:" + bbtype);
			}
			
			return true;
		}

		matcher = FtpRegex.cancelledPattern.matcher(line);
		if (matcher.find()) {
			//System.out.println(matcher.group(1));
			parseState = State.Error;
			return true;
		}

		if (line.contains("HOLE CARDS")) {
			/*
			 * Button should be set, players should've been added.
			 */
			hand.setSeated();

			parseState = State.Actions;
			return true;
		}

		return false;
	}

	/**
	 * 
	 * @param line
	 * @param hand
	 * @return Any action regex matched.
	 */
	private boolean parseAction(String line, HeadsUp hand) throws IllegalHandException {
		Matcher matcher;

		matcher = FtpRegex.streetPattern.matcher(line);
		if (matcher.find()) {
//			System.out.println(matcher.group(0));
			hand.updateForNewStreet(matcher.group(2));
			return true;
		}

		matcher = FtpRegex.foldPattern.matcher(line);
		if (matcher.find()) {
//			System.out.println(matcher.group(0));
			hand.addFold(matcher.group(1));
			return true;
		}

		matcher = FtpRegex.checkPattern.matcher(line);
		if (matcher.find()) {
//			System.out.println(matcher.group(0));
			hand.addCheck(matcher.group(1));
			return true;
		}

		matcher = FtpRegex.callPattern.matcher(line);
		if (matcher.find()) {
//			System.out.println(matcher.group(2));
			hand.addCall(matcher.group(1), matcher.group(2));
			return true;
		}
		
		matcher = FtpRegex.betPattern.matcher(line);
		if (matcher.find()) {
//			System.out.println(matcher.group(0));
			hand.addBet(matcher.group(1), matcher.group(2));
			return true;
		}
		
		matcher = FtpRegex.raisePattern.matcher(line);
		if (matcher.find()) {
//			System.out.println(matcher.group(0));
			hand.addRaise(matcher.group(1), matcher.group(2));
			return true;
		}

		matcher = FtpRegex.uncalledPattern.matcher(line);
		if (matcher.find()) {
//			System.out.println(matcher.group(2));
			return true;
		}		

		matcher = FtpRegex.winPattern.matcher(line);
		if (matcher.find()) {
//			System.out.println(matcher.group(0));
			return true;
		}

		if (line.contentEquals("*** SHOW DOWN ***")) {
			parseState = State.Showdown;
			return true;
		}	

		if (line.contentEquals("*** SUMMARY ***")) {
			parseState = State.Done;
			return true;
		}

		return false;
	}

	/**
	 * 
	 * @param line
	 * @param hand
	 * @return Any showdown regex matched.
	 */
	private boolean parseShowdown(String line, HeadsUp hand) throws IllegalHandException {
		Matcher matcher;

		matcher = FtpRegex.showsPattern.matcher(line);
		if (matcher.find()) {
//			System.out.println(matcher.group(0));
			hand.setHoleCards(matcher.group(1), matcher.group(2));
			return true;
		}

		matcher = FtpRegex.winPattern.matcher(line);
		if (matcher.find()) {
//			System.out.println(matcher.group(0));
			return true;
		}

		matcher = FtpRegex.tiesPattern.matcher(line);
		if (matcher.find()) {
//			System.out.println(matcher.group(0));
			return true;
		}

		if (line.contentEquals("*** SUMMARY ***")) {
			parseState = State.Done;
			return true;
		}

		return false;
	}

	/**
	 * There isn't always *** SHOW DOWN *** even if cards are displayed. 
	 * 
	 * @param line
	 * @param hand
	 * @return Any showdown regex matched.
	 */
	private boolean parseSummary(String line, HeadsUp hand) {
		Matcher matcher;

		matcher = FtpRegex.tiesPattern.matcher(line);
		if (matcher.find()) {
//			System.out.println(matcher.group(0));
			return true;
		}

		return false;
	}

	/**
	 * Entry point for parsing a complete file.
	 * 
	 * @param bufRead
	 */
	public void parseFile(BufferedReader bufRead) throws IOException {
		final String prefix = "FtpParser.parseFile:";
		HeadsUp hand = new HeadsUp();
		String line = bufRead.readLine();

		while (line != null){
			/*
			 *  Did we finish a hand?
			 */
			if (parseState == State.Done) {
				if (hand.verifyHand()) {
					getResults().add(hand);
				}

				parseState = State.Description;
				hand = new HeadsUp();
			}

			if (parseState == State.Error) {
				hand = new HeadsUp();
				parseState = State.Description;
			}

			try {
				if (parseState == State.Description) {
					parseDescription(line, hand);
				} else if (parseState == State.Preamble) {
					parsePreamble(line, hand);
				} else if (parseState == State.Actions) {
					parseAction(line, hand);
				} else if (parseState == State.Showdown) {
					parseShowdown(line, hand);
				}
			} catch (IllegalHandException ns) {
				//ns.printStackTrace();
				System.out.println("IllegalHandException:" + ns.getMessage());
				parseState = State.Error;
			} catch (Exception e) {
				System.out.println(prefix + "unhandled exception");
				e.printStackTrace();
				parseState = State.Error;
			}

			line = bufRead.readLine();
		}
	}
}