package se.hupoker.inference.tree;

import se.hupoker.inference.handinformation.HandInfo;
import se.hupoker.handhistory.HeadsUp;

import java.util.Collection;

/**
 * @author Alexander Nyberg
 */
public interface Extraction {
    Collection<HandInfo> extract(Collection<HeadsUp> list);
}
