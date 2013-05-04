package se.hupoker.inference;

import se.hupoker.inference.handinformation.HandInfo;
import se.hupoker.handhistory.HeadsUp;
import se.hupoker.handhistory.IllegalHandException;
import se.hupoker.inference.tree.Extraction;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 *
 * @author Alexander Nyberg
 */
public class SerialExtraction implements Extraction {
    @Override
    public Collection<HandInfo> extract(Collection<HeadsUp> list) {
        final List<HandInfo> extracted = new ArrayList<>();

        for (HeadsUp headsup : list) {
            try {
                HandInfo info = HandInfo.factory(headsup);
                extracted.add(info);
            } catch (IllegalHandException e) {
                e.printStackTrace();
            }
        }

        return extracted;
    }
}
