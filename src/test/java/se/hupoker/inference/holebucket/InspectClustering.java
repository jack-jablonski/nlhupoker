package se.hupoker.inference.holebucket;

import com.google.common.collect.ArrayListMultimap;
import org.junit.Test;
import se.hupoker.cards.CardSet;
import se.hupoker.cards.HoleCards;
import se.hupoker.cards.boardisomorphisms.SingleManager;

import java.util.Collection;

/**
 * @author Alexander Nyberg
 */
public class InspectClustering {
    @Test
    public void inspectFlopClustering() {
        CardSet board = CardSet.from("2c7dKh");
//        HoleBucketMap bucketMap = new FlopBucketMap(board);
//
//        ArrayListMultimap<Integer, HoleCards> multiMap = findClustering(board, bucketMap);
//        printClustering(multiMap, board);
    }

    @Test
    public void inspectTurnClustering() {
        CardSet board = CardSet.from("2c7dKhKd");
//        HoleBucketMap bucketMap = new TurnBucketMap(board);
//
//        ArrayListMultimap<Integer, HoleCards> multiMap = findClustering(board, bucketMap);
//        printClustering(multiMap, board);
    }

    @Test
    public void inspectRiverClustering() {
        CardSet board = CardSet.from("2c4c7dKhAd");
//        HoleBucketMap bucketMap = new RiverBucketMap(board);
//
//        ArrayListMultimap<Integer, HoleCards> multiMap = findClustering(board, bucketMap);
//        printClustering(multiMap, board);
    }

    private ArrayListMultimap<Integer, HoleCards> findClustering(CardSet board) {
        ArrayListMultimap<Integer, HoleCards> multiMap = ArrayListMultimap.create();
        /*for (HoleCards hole : HoleCards.allOf()) {
            if (board.containsAny(hole)) {
                continue;
            }

            int bucketIndex = bucketMap.get(hole);
            multiMap.put(bucketIndex, hole);
        }*/
        return multiMap;
    }

    private void printClustering(ArrayListMultimap<Integer, HoleCards> multiMap, CardSet board) {
        SingleManager manager = new SingleManager(board);
        int numberOfPrinted = 0;

        for (Integer key : multiMap.keySet()) {
            System.out.println("======== Printing for bucket #" + key);
            Collection<HoleCards> values = multiMap.get(key);

            for (HoleCards hole : values) {
                if (!manager.addIfNotExists(hole)) {
                    System.out.println(hole);
                    numberOfPrinted++;
                }
            }
        }
        System.out.println("Printed #" + numberOfPrinted);
    }
}