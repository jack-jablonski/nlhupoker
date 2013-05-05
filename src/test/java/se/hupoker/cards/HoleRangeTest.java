package se.hupoker.cards;

import org.junit.Test;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

/**
 * @author Alexander Nyberg
 */
public class HoleRangeTest {
    private final int ONEPAIR = 6;
    private final int UNSUITED = 12;
    private final int SUITED = 4;
    private final int EGAL = UNSUITED + SUITED;
    private HoleRange holeRange = new HoleRange();

    private void assertThatElementsAreUnique(List<HoleCards> list) {
        Set<HoleCards> set = new HashSet<>(list);
        assertThat(set.size(), is(list.size()));
    }

    @Test
    public void numberOfUnsuitedInRange() {
        List<HoleCards> result = holeRange.parse("AJs-A9s");
        assertEquals(SUITED * 3, result.size());
        assertThatElementsAreUnique(result);
    }

    @Test
    public void numberOfEgalInRange() {
        List<HoleCards> result = holeRange.parse("AJe-A9e");
        assertEquals(EGAL * 3, result.size());
        assertThatElementsAreUnique(result);
    }

    @Test
    public void numberOfSuitedInRange() {
        List<HoleCards> result = holeRange.parse("AJo-A7o");
        assertEquals(UNSUITED*5, result.size());
        assertThatElementsAreUnique(result);
    }

    @Test
    public void numberOfPairs() {
        List<HoleCards> result = holeRange.parse("TT");
        assertEquals(ONEPAIR, result.size());
        assertThatElementsAreUnique(result);
    }

    @Test
    public void numberOfUnsuited() {
        List<HoleCards> result = holeRange.parse("AKo");
        assertEquals(UNSUITED, result.size());
        assertThatElementsAreUnique(result);
    }

    @Test
    public void numberOfSuited() {
        List<HoleCards> result = holeRange.parse("AKs");
        assertEquals(SUITED, result.size());
        assertThatElementsAreUnique(result);
    }

    @Test
    public void numberOfPairsThatAreBetter() {
        List<HoleCards> result = holeRange.parse("TT+");
        assertEquals(ONEPAIR*5, result.size());
        assertThatElementsAreUnique(result);
    }

    @Test
    public void numberOfUnsuitedThatAreBetter() {
        List<HoleCards> result = holeRange.parse("AJs+");
        assertEquals(SUITED*3, result.size());
        assertThatElementsAreUnique(result);
    }

    @Test
    public void numberOfEgalThatAreBetter() {
        List<HoleCards> result = holeRange.parse("AJe+");
        assertEquals(EGAL*3, result.size());
        assertThatElementsAreUnique(result);
    }

    @Test
    public void numberOfSuitedThatAreBetter() {
        List<HoleCards> result = holeRange.parse("K2o+");
        assertEquals(UNSUITED*11, result.size());
        assertThatElementsAreUnique(result);
    }
}