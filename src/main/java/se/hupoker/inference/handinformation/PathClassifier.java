package se.hupoker.inference.handinformation;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import se.hupoker.common.ActionClassifier;
import se.hupoker.inference.states.PathElement;
import se.hupoker.inference.states.PathHistory;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static se.hupoker.common.Street.PREFLOP;

/**
 * @author Alexander Nyberg
 */
public class PathClassifier {
    private static class Category extends ArrayList<ActionClassifier> {
    }

    private Multimap<Category, PathHistory> multimap = ArrayListMultimap.create();

    private Category compressedCategory(PathHistory history) {
        Category category = new Category();
        List<PathElement> street = history.get(PREFLOP);

        for (PathElement element : street) {
            category.add(element.getAction().getClassifier());
        }

        return category;
    }

    public void classify(Collection<PathHistory> histories) {
        for (PathHistory history : histories) {
            buildMap(history);
        }

        for (Category category : multimap.keySet()) {
            int categorySize = multimap.get(category).size();
            System.out.println("#" + categorySize + " with " + category.toString());
        }
    }

    private void buildMap(PathHistory history) {
        Category category = compressedCategory(history);

        multimap.put(category, history);
    }
}