package com.amazing.treeshift.service;

import com.amazing.treeshift.model.OrgUnit;
import com.amazing.treeshift.model.TreeOrgUnit;

import javax.transaction.Transactional;
import java.util.Collection;

public interface OrgUnitService {
	/**
	 * Moves a node (keeping its subtrees) to a new parent, and adjusts root and height of affected nodes as
	 * needed. Can move node to a different tree, but cannot move it to one of its own descendants or itself.
	 *
	 * @param id          id of the node to move
	 * @param newParentId id of the new parent node
	 * @throws IllegalArgumentException if preconditions aren't met
	 */
	@Transactional
	void moveNodeToNewParent(long id, long newParentId);

	/**
	 * Builds a tree structure from a list of OrgUnit nodes.
	 * Input must be sane! The top-level parent and all the nodes by parent relations must be present in the
	 * collection. Meant to be called with a node list from a strongly consistent database, not random user
	 * input.
	 *
	 * @param parentId id of top-level node
	 * @param nodes    list of nodes to build tree from
	 * @return the tree representation of the organisational unit subtree
	 */
	TreeOrgUnit buildTreeFromNodes(long parentId, Collection<OrgUnit> nodes);
}
