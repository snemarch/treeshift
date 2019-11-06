package com.amazing.treeshift.service;

import com.amazing.treeshift.repository.OrgUnitRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;

@Component
public class OrgUnitServiceImpl implements OrgUnitService {
	@Autowired
	private OrgUnitRepository repository;

	@Override
	@Transactional
	public void moveNodeToNewParent(long id, long newParentId) {
		if (id == newParentId) {
			throw new IllegalArgumentException("Cannot move node to itself");
		}

		var movingNode = repository.findById(id)
			.orElseThrow(() -> new IllegalArgumentException("Source node doesn't exist"));

		if (movingNode.getParentId() == newParentId) {
			// We're not really moving the node if we're specifying its existing parent as the destination.
			// Not actually doing a move, but state is valid — do a successful early-out instead of throwing.
			return;
		}

		var newParent = repository.findById(newParentId)
			.orElseThrow(() -> new IllegalArgumentException("Target parent doesn't exist"));

		var differentTree = movingNode.getRootId() != newParent.getRootId();
		var noChildOfMine = movingNode.getHeight() <= newParent.getHeight();

		if (!(differentTree || noChildOfMine)) {
			// There's a possibility we're trying to move a node to one of its descendants.
			// Perform (expensive) recursive check to see if this is the case, in which case we abort.
			if (repository.isNodeDescendantOf(id, newParentId)) {
				throw new IllegalArgumentException("Trying to move node to its descendant");
			}
		}

		movingNode.setParentId(newParent.getId());
		movingNode = repository.save(movingNode);

		long heightDifference = newParent.getHeight() - movingNode.getHeight() + 1;
		if (differentTree || (heightDifference != 0)) {
			// If moving the node to a different tree, or another depth, adjust both for the entire subtree.
			// Not having two separate methods for rootId and height adjustments is a compromise between clean
			// design and speed.
			repository.adjustSubtreeRootAndHeight(id, newParent.getRootId(), heightDifference);
		}
	}
}
