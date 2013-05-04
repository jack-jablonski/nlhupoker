package se.hupoker.cards.boardcategories;

import com.google.common.base.Functions;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.ImmutableSortedMap;
import com.google.common.collect.Multimap;
import com.google.common.collect.Ordering;
import se.hupoker.cards.HoleCards;
import se.hupoker.cards.boardenumerator.BoardRunner;
import se.hupoker.cards.boardenumerator.EnumerateBoard;
import se.hupoker.common.Street;
import se.hupoker.lut.FlopTable;
import se.hupoker.lut.LutKey;
import se.hupoker.lut.LutPath;
import se.hupoker.cards.CardSet;

import java.util.*;

/**
 * @author Alexander Nyberg
 */
public class DrawyBoards implements BoardRunner {
    private final FlopTable ppotTable;

    public static void main(String[] args) {
        DrawyBoards drawy = new DrawyBoards();
        EnumerateBoard enumerator = new EnumerateBoard(drawy, Street.FLOP);

        enumerator.iterateAllBoards();

        drawy.sortAndShow();
    }

    public DrawyBoards() {
        ppotTable = new FlopTable();
        ppotTable.load(LutPath.getFlopPpot());
    }

    public void sortAndShow() {
        Map<CardSet, Float> summedMap = new HashMap<>();

        for (CardSet board : map.keySet()) {
            Collection<Float> values = map.get(board);
            float sum = 0;

            for (float ppotValue : values) {
                sum += ppotValue;
            }

            summedMap.put(board, sum);
        }

        Comparator<CardSet> valueComparator = Ordering.natural().onResultOf(Functions.forMap(summedMap));
        Map<CardSet, Float> sorted = ImmutableSortedMap.copyOf(summedMap, valueComparator);

        for (Map.Entry<CardSet, Float> entry : sorted.entrySet()) {
            System.out.println(entry.getKey() + " ppotValue: " + entry.getValue());
        }

    }

    private final Multimap<CardSet, Float> map = HashMultimap.create();

    @Override
    public void evaluateBoard(CardSet board) {
        Collection<Float> ppot = new ArrayList<>();

        for (HoleCards hole : HoleCards.allOf()) {
            if (board.containsAny(hole)) {
                continue;
            }

            LutKey key = new LutKey(board, hole);
            float ppotValue = ppotTable.lookupOne(key);
            ppot.add(ppotValue);

            System.out.println(hole + ": " + ppotValue);
        }

        System.out.println("=== Board:" + board);

        map.putAll(board, ppot);
        System.exit(0);
    }
}