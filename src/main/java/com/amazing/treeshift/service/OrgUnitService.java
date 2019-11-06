package com.amazing.treeshift.service;

import javax.transaction.Transactional;

public interface OrgUnitService {
	/**
	 * Moves a node (keeping its subtrees) to a new parent, and adjusts root and height of affected nodes as
	 * needed. Can move node to a different tree, but cannot move it to one of its own descendants or itself.
	 *
	 * @param id id of the node to move
	 * @param newParentId id of the new parent node
	 * @throws IllegalArgumentException if preconditions aren't met
	 */
	@Transactional
	void moveNodeToNewParent(long id, long newParentId);
}
