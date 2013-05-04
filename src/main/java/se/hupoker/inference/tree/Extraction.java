package se.hupoker.inference.tree;

import se.hupoker.inference.handinformation.HandInfo;
import se.hupoker.handhistory.HeadsUp;

import java.util.Collection;

/**
 * Created with IntelliJ IDEA.
 * User: philippe
 * Date: 2012-12-28
 * Time: 19:58
 * To change this template use File | Settings | File Templates.
 */
public interface Extraction {
    Collection<HandInfo> extract(Collection<HeadsUp> list);
}
