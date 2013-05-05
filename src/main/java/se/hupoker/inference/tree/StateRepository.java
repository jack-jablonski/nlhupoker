package se.hupoker.inference.tree;

import se.hupoker.common.Street;
import se.hupoker.inference.states.GenericState;

import java.util.Collection;

/**
 * @author Alexander Nyberg
 */
public interface StateRepository {

    public Collection<GenericState> getPreflop();

    public Collection<GenericState> getPostFlop(Street street);
}