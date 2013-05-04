package se.hupoker.inference.tree;

import se.hupoker.common.Street;
import se.hupoker.inference.states.PathElement;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkState;


/**
 *
 * @author Alexander Nyberg
 *
 */
public class StreetStateSet implements Iterable<StateNode> {
    // Basically a list of StateNodes
    private final Collection<StateNode> nodes = new LinkedList<>();
    private final Street street;

	public StreetStateSet(Street st) {
		this.street = st;
	}

    protected void addNode(StateNode node) {
        checkArgument(node.getCenter().getStreet() == street);
        nodes.add(node);
    }

    public Iterator<StateNode> iterator() {
        return nodes.iterator();
    }

	/**
	 * 
	 * @param element
	 * @return The node closest to element
	 */
    protected StateNode findNode(PathElement element) {
        StateNode minNode = null;
        double minDistance = Double.POSITIVE_INFINITY;

        for (StateNode node : this) {
            if (!node.isCompareable(element)) {
                continue;
            }

            double distance = node.getDistance(element);
            if (distance < minDistance) {
                minDistance = distance;
                minNode = node;
            }
        }
        checkState(minNode != null, element.toString());
        return minNode;
    }

    /**
     * For visualization, ability to print the tree.
     *
     * @param prefix
     */
	public void print(String prefix) {
		final String nextPrefix = prefix + "->";

        for (StateNode node : nodes) {
			/*
			 * Only print visited nodes.
			 */
			if (node.numberOfVisits == 0) {
//				continue;
			}

			String buttonString = node.toString();
			System.out.println(prefix + street.toString() + " " + buttonString);

			StreetStateSet nodeBase = node.getChild();
			if (nodeBase != null) {
				nodeBase.print(nextPrefix);
			}			
		}
	}
}