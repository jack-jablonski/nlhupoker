package se.hupoker.cards.handeval;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import se.hupoker.cards.CardSet;
import se.hupoker.common.Serializer;
import se.hupoker.poker.Street;

/**
 * Double-cached repository: First in memory, then on disk.
 *
 * @author Alexander Nyberg
 */
public class CachedEquityRepository implements EquityRepository {
    private final Serializer serializer;
    private final String equityDirectory;

    public CachedEquityRepository(Serializer serializer, String equityDirectory) {
        this.serializer = serializer;
        this.equityDirectory = equityDirectory;
    }

    private final CacheLoader<CardSet, EquityTable> cacheLoader = new CacheLoader<CardSet, EquityTable>() {
        @Override
        public EquityTable load(CardSet board) throws RuntimeException {
            final String boardFileLocation = getFileLocation(board);

            EquityTable table = serializer.deserialize(EquityTable.class, boardFileLocation);
            if (table != null) {
                return table;
            }

            table = EquityTableFactory.calculate(board);
            serializer.serialize(table, boardFileLocation);

            return table;
        }
    };

    private final LoadingCache<CardSet, EquityTable> cache = CacheBuilder.newBuilder()
            .weakValues()
            .build(cacheLoader);

    private String getFileLocation(CardSet board) {
        return equityDirectory + "/" + board.toString() + ".ser";
    }

    @Override
    public EquityMatrix get(Street street, CardSet board) {
        EquityTable equityTable = cache.getUnchecked(board);

        return new EquityMatrix(equityTable);
    }
}