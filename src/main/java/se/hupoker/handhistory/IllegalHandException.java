package se.hupoker.handhistory;

/**
 * 
 * Only used in parsing. 
 * After that everything about the hand history should be tip-top.
 *  
 * @author Alexander Nyberg
 *
 */
public class IllegalHandException extends Exception {
	public IllegalHandException(String message) {
		super(message);
	}

	public IllegalHandException(String message, Exception e) {
		super(message, e);
	}
}