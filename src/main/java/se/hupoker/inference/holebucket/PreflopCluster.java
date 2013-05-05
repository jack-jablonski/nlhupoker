package se.hupoker.inference.holebucket;

import se.hupoker.cards.HoleCards;

import java.util.List;

public class PreflopCluster {
    public String range;
    public HandStrength strength = HandStrength.NONE;
    public List<HoleCards> holes;
}