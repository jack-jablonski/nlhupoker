package se.hupoker.cards.cache;

/**
 *
 * @author Alexander Nyberg
 *
 */
@Deprecated
enum FlushConfiguration {
    None,
    /**
     * Possibly there should be "One" here if it is rainbow and blocking one/two suits.
     */
    BackDraw,
    Draw,
    Flush,
}