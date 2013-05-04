package se.hupoker.cards;

import org.junit.Test;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.hamcrest.core.Is.is;
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
        assertThat(result.size(), is(SUITED*3));
        assertThatElementsAreUnique(result);
    }

    @Test
    public void numberOfEgalInRange() {
        List<HoleCards> result = holeRange.parse("AJe-A9e");
        assertThat(result.size(), is(EGAL*3));
        assertThatElementsAreUnique(result);
    }

    @Test
    public void numberOfSsuitedInRange() {
        List<HoleCards> result = holeRange.parse("AJo-A7o");
        assertThat(result.size(), is(UNSUITED*5));
        assertThatElementsAreUnique(result);
    }

    @Test
    public void numberOfPairs() {
        List<HoleCards> result = holeRange.parse("TT");
        assertThat(result.size(), is(ONEPAIR));
        assertThatElementsAreUnique(result);
    }

    @Test
    public void numberOfUnsuited() {
        List<HoleCards> result = holeRange.parse("AKo");
        assertThat(result.size(), is(UNSUITED));
        assertThatElementsAreUnique(result);
    }

    @Test
    public void numberOfSuited() {
        List<HoleCards> result = holeRange.parse("AKs");
        assertThat(result.size(), is(SUITED));
        assertThatElementsAreUnique(result);
    }

    @Test
    public void numberOfPairsThatAreBetter() {
        List<HoleCards> result = holeRange.parse("TT+");
        assertThat(result.size(), is(ONEPAIR*5));
        assertThatElementsAreUnique(result);
    }

    @Test
    public void numberOfUnsuitedThatAreBetter() {
        List<HoleCards> result = holeRange.parse("AJs+");
        assertThat(result.size(), is(SUITED*3));
        assertThatElementsAreUnique(result);
    }

    @Test
    public void numberOfEgalThatAreBetter() {
        List<HoleCards> result = holeRange.parse("AJe+");
        assertThat(result.size(), is(EGAL*3));
        assertThatElementsAreUnique(result);
    }

    @Test
    public void numberOfSuitedThatAreBetter() {
        List<HoleCards> result = holeRange.parse("K2o+");
        assertThat(result.size(), is(UNSUITED*11));
        assertThatElementsAreUnique(result);
    }
}