package se.hupoker.cards.boardisomorphisms;

/**
 *
 * @author Alexander Nyberg
 *
 */
public enum FlushConfiguration {
    None,
    /**
     * Possibly there should be "One" here if it is rainbow and blocking one/two suits.
     */
    BackDraw,
    Draw,
    Flush,
}