package se.hupoker.inference.handinformation;

import com.google.common.collect.Iterables;
import se.hupoker.cards.HoleCards;
import se.hupoker.cards.Card;
import se.hupoker.cards.CardSet;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import static com.google.common.base.Preconditions.checkState;

/**
 *
 * Information telling us which hole cards are even possible.
 *
 * @author Alexander Nyberg
 *
 */
public class HolePossible implements Iterable<HoleCards> {
    private final Set<HoleCards> possible = new HashSet<>(HoleCards.allOf());

    public Iterator<HoleCards> iterator() {
        return possible.iterator();
    }

	private void remove(Card card) {
		for (Card other : Card.allOf()) {
			if (card.equals(other)) {
				continue;
			}

			HoleCards hole = HoleCards.of(card, other);
            possible.remove(hole);
		}
	}

	public void remove(CardSet set) {
		for (Card card : set) {
			remove(card);
		}
	}

    public int numberOfPossible() {
        return possible.size();
    }

    public HoleCards getUnique() {
        return Iterables.getOnlyElement(possible);
    }

    /**
     *
     * @param hole We know for sure these are the cards!
     */
	public void setKnownHole(HoleCards hole) {
		possible.clear();
        possible.add(hole);
	}
}