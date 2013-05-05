package se.hupoker.inference.holebucket;

import java.util.List;

/**
 * @author Alexander Nyberg
 */
public interface ClusterRepository {

    public List<PreflopCluster> preflop();

    public List<FlopCluster> flop();

    public List<TurnCluster> turn();

    public List<RiverCluster> river();
}