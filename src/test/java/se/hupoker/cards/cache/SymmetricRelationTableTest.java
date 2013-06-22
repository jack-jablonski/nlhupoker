package se.hupoker.cards.cache;

import com.google.common.base.Optional;
import org.junit.Before;
import org.junit.Test;
import se.hupoker.cards.CardSet;
import se.hupoker.cards.HoleCards;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * @author Alexander Nyberg
 */
public class SymmetricRelationTableTest {
    private SymmetricRelationTable table;

    @Before
    public void setUp() {
        table = new SymmetricRelationTable();
    }

    @Test (expected = NullPointerException.class)
    public void addingNullThrowsException() {
        table.add(null, null, null, null);
    }

    @Test
    public void getReturnsEmptyWithoutPut() {
//        Optional<Pair<HoleCards>> pair = table.get(HoleCards.from("2c2d"), HoleCards.from("4c4d"));
//
//        assertFalse(pair.isPresent());
    }

    @Test
    public void addAndGet() {
//        HoleCards one = HoleCards.from("AsKs");
//        HoleCards two = HoleCards.from("AdQd");
//
//        HoleCards oneEquivalent = HoleCards.from("AhKh");
//        HoleCards twoEquivalent = HoleCards.from("AdQd");
//
//        table.add(one, two, oneEquivalent, twoEquivalent);
//
//        Optional<Pair<HoleCards>> pair = table.get(oneEquivalent, twoEquivalent);
//        assertTrue(pair.isPresent());
    }
}