package se.hupoker.cards.handeval;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import se.hupoker.cards.CardSet;
import se.hupoker.common.Serializer;
import se.hupoker.common.Street;

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

    private final CacheLoader<CardSet, EquityMatrix> cacheLoader = new CacheLoader<CardSet, EquityMatrix>() {
        @Override
        public EquityMatrix load(CardSet board) throws RuntimeException {
            final String boardFileLocation = getFileLocation(board);

            EquityMatrix matrix = serializer.deserialize(EquityMatrix.class, boardFileLocation);
            if (matrix != null) {
                return matrix;
            }

            matrix = EquityMatrixFactory.factory(board);
            serializer.serialize(matrix, boardFileLocation);

            return matrix;
        }
    };

    private final LoadingCache<CardSet, EquityMatrix> cache = CacheBuilder.newBuilder()
            .weakValues()
            .build(cacheLoader);

    private String getFileLocation(CardSet board) {
        return equityDirectory + "/" + board.toString() + ".ser";
    }

    @Override
    public EquityMatrix get(Street Street, CardSet board) {
        return cache.getUnchecked(board);
    }
}
