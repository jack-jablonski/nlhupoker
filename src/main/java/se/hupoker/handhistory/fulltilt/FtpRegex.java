package se.hupoker.handhistory.fulltilt;
import java.util.regex.Pattern;

/**
 * http://www.gskinner.com/RegExr/
 * 
 * Some regex explanation
 * Amounts: [\\d\\.\\,]+
 * Names: [\\w\\s\\-\\_]+
 * 
 * @author Alexander Nyberg
 *
 */
final class FtpRegex {
	/*
	 * Examples:
	 * FullTiltPoker Game #5441707416: Table Cranwood (6 max) - $2/$4 - No Limit Hold'em - 10:17:28 ET - 2008/02/29
	 * Full Tilt Poker Game #8889440965: Table Lint (heads up) - $0.50/$1 - No Limit Hold'em - 15:58:25 ET - 2008/1
	 */
	static final public Pattern descPattern = 
		Pattern.compile(".+ Game \\#(\\d+): Table (\\w+).*\\$([\\d\\.]+)\\/\\$([\\d\\.]+) - (.+) - (.+) - (.+)$");

	// Hand #6910308478 has been canceled
	static final public Pattern cancelledPattern = 
		Pattern.compile("^Hand #(\\d+) has been canceled$");
	
	/*
	 * Examples:
	 * Seat 4: speedy0308 ($123.20)
	 * Seat 4: SilentSupra ($1,768.85), is sitting out
	 */
	static final public Pattern seatPattern = 
		Pattern.compile("^Seat (\\d+): ([\\w\\s\\-\\_]+) \\(\\$([\\d\\.\\,]+)\\)(, is sitting out)?");

	// The button is in seat #4
	static final public Pattern buttonPattern = 
		Pattern.compile("^The button is in seat #(\\d+)$");

	/*
	 * Examples:
	 * ungar2000 posts the small blind of $3
	 * Peephany posts the big blind of $6
	 */
	static final public Pattern postsPattern =
		Pattern.compile("^([\\w\\s\\-\\_]+) posts the (small|big) blind of \\$([\\d\\.\\,]+)");

	/**
	 * ACTIONS
	 */
	
	// juergensuz folds  \\*\\*\\* .* [.*]$
	static final public Pattern streetPattern = 
		Pattern.compile("\\*\\*\\* (FLOP|TURN|RIVER) \\*\\*\\*.*\\[(.*)\\]");
	
	// juergensuz folds
	static final public Pattern foldPattern = 
		Pattern.compile("^([\\w\\s\\-\\_]+) folds$");
	
	// fractional checks
	static final public Pattern checkPattern = 
		Pattern.compile("^([\\w\\s\\-\\_]+) checks$");
	
	/*
	 * nomad8980 calls $12
	 * tjmac72 calls $25.50, and is all in
	 */
	static final public Pattern callPattern = 
		Pattern.compile("^([\\w\\s\\-\\_]+) calls \\$([\\d\\.\\,]+)");

	/*
	 * reeve68 bets $10
	 * hiltz006 bets $31, and is all in
	 */
	static final public Pattern betPattern = 
		Pattern.compile("^([\\w\\s\\-\\_]+) bets \\$([\\d\\.\\,]+)");	
	
	/*
	 * fractional raises to $9
	 * Box of Legos raises to $125.60, and is all in 
	 */
	static final public Pattern raisePattern = 
		Pattern.compile("^([\\w\\s\\-\\_]+) raises to \\$([\\d\\.\\,]+)");

	/*
	 * Examples:
	 * Uncalled bet of $20 returned to AAKid7
	 */
	static final public Pattern uncalledPattern = 
		Pattern.compile("^Uncalled bet of \\$([\\d\\.\\,]+) returned to ([\\w\\s\\-\\_]+)");	

	/*
	 * Examples: 
	 * AAKid7 wins the pot ($53.20)
	 * Bernie1337 wins the pot ($13.50) with a pair of Twos
	 * nofoldforme wins the side pot ($884) with two pair, Tens and Nines
	 * CRAZYBOY68 wins the main pot ($257.55) with three of a kind, Nines
	 */
	static final public Pattern winPattern = 
		Pattern.compile("^([\\w\\s\\-\\_]+) wins the( side| main)? pot \\(\\$([\\d\\.\\,]+)\\)");
	
	// MAMMOS ties for the pot ($2.85) with Ace Queen high
	static final public Pattern tiesPattern = 
		Pattern.compile("^([\\w\\s\\-\\_]+) ties for the( side| main)? pot \\(\\$([\\d\\.\\,]+)\\)");
	
	/**
	 * SHOWDOWN
	 */
	/*
	 * EOMann shows [7d 8c] a pair of Eights
	 * fractional shows [Ad 7c] a pair of Queens
	 */
	static final public Pattern showsPattern = 
		Pattern.compile("^([\\w\\s\\-\\_]+) shows \\[(.+)\\]");
	
	/**
	 * SUMMARY
	 */
	
	/*
	 * Seat 1: Oliver7878 (small blind) showed [Kh 7h] and won ($62.30) with three of a kind, Sevens
	 * Seat 2: Dozman (big blind) showed [Jh Qh] and lost with two pair, Jacks and Sevens
	 */
	static final public Pattern showedPattern = 
			Pattern.compile("^Seat (\\d+): ([\\w\\s\\-\\_]+) showed \\[(.+)\\]");
}
