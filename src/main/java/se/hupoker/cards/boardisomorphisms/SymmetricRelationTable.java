package se.hupoker.cards.boardisomorphisms;

import com.google.common.base.Optional;
import se.hupoker.cards.HoleCards;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;

/**
* 
* @author Alexander Nyberg
*
*/
class SymmetricRelationTable {
	private final Table<HoleRelation, HoleRelation, Pair<HoleCards>> table;

	public SymmetricRelationTable() {
		table = HashBasedTable.create();
	}

	public Optional<Pair<HoleCards>> get(HoleRelation one, HoleRelation two) {
		return Optional.fromNullable(table.get(one, two));
	}

    /**
     * Add in both orders.
     */
	public void add(HoleCards holeOne, HoleCards holeTwo, HoleRelation relOne, HoleRelation relTwo) {
		 // Standard order
		Pair<HoleCards> standardOrder = new Pair<>(holeOne, holeTwo);
		table.put(relOne, relTwo, standardOrder);

		 // Reverse order
		Pair<HoleCards> reverseOrder = new Pair<>(holeTwo, holeOne);
		table.put(relTwo, relOne, reverseOrder);
	}
}