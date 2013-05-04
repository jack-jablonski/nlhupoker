package se.hupoker.cards.handeval;

import org.paukov.combinatorics.Factory;
import org.paukov.combinatorics.Generator;
import org.paukov.combinatorics.ICombinatoricsVector;
import se.hupoker.cards.Card;
import se.hupoker.cards.CardSet;
import se.hupoker.cards.DeckSet;
import se.hupoker.cards.HoleCards;

import java.util.Arrays;


/**
 * 
 * @author Alexander Nyberg
 *
 */
final public class EquityVector  {
    private final EquityMeasure measure = new EquityMeasure();
    private final EquityAdapter adapter = new EquityAdapter();
	private final double ahead[] = new double[HoleCards.TexasCombinations];
	private final double equities[] = new double[HoleCards.TexasCombinations];

	public EquityVector() {
		Arrays.fill(ahead, EquityMeasure.BADEQUITY);
		Arrays.fill(equities, EquityMeasure.BADEQUITY);
	}

	public double getEquity() {
		return measure.getEquity(equities);
	}
	
	public double getHS() {
		return measure.getHS(ahead);
	}
	
	public double getPPOT() {
		return measure.getPPOT(ahead, equities);
	}
	
	public double getNPOT() {
		return measure.getNPOT(ahead, equities);
	}	

	/**
	 * 
	 * @param deck Is without board & myHole.
	 * @param board
	 * @param myHole
	 */
	private void iterateOpponentHoleCards(DeckSet deck, CardSet board, HoleCards myHole) {
		final int remainingCards = adapter.getNumberOfRemainingCards(board);

		/*
		 * All his possible myHole cards
		 */
        ICombinatoricsVector<Card> initialVector = Factory.createVector(deck);
        Generator<Card> generator = Factory.createSimpleCombinationGenerator(initialVector, HoleCards.TexasHoleCards);
        for (ICombinatoricsVector<Card> comb : generator) {
			HoleCards opHole = HoleCards.of(comb.getVector());

            DeckSet newDeck = new DeckSet(deck);
			newDeck.removeAll(opHole);

			double equitySum = EquityMeasure.BADEQUITY;
			if (remainingCards > 0) {
				equitySum = adapter.iterateBoards(remainingCards, newDeck, board, myHole, opHole);
			}

            ahead[opHole.ordinal()] = adapter.get(board, myHole, opHole);
			equities[opHole.ordinal()] = equitySum;
		}
	}

	/**
	 * Build equity vector against hand.
	 * 
	 * @param board possibly existing board
	 * @param myHole my current hand
	 */
	public void buildEquities(CardSet board, HoleCards myHole) {
        DeckSet deck = DeckSet.freshDeck();

		deck.removeAll(board);
		deck.removeAll(myHole);

		iterateOpponentHoleCards(deck, board, myHole);
	}
}