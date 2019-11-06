package com.amazing.treeshift.repository;

import com.amazing.treeshift.model.OrgUnit;
import org.springframework.data.jpa.repository.*;

import java.util.Collection;

public interface OrgUnitRepository extends JpaRepository<OrgUnit, Long> {
	/***
	 * Gets an organisational unit subtree, including the given parent, as a flat list.
	 *
	 * @param id id of the top-level subtree node
	 * @return list of organisational units
	 */
	@Query(nativeQuery = true, value = "WITH RECURSIVE children AS(" +
		"SELECT id, root_id, parent_id, height FROM org_units WHERE id = ?1 " +
		"UNION " +
		"SELECT ou.id, ou.root_id, ou.parent_id, ou.height FROM org_units ou " +
		"INNER JOIN children c ON c.id = ou.parent_id " +
		") SELECT * FROM children;")
	Collection<OrgUnit> getRecursiveById(Long id);

	/***
	 * Returns true if a node is a descendant of a specified subtree.
	 *
	 * @param parentId top-level "possible ancestor" node id
	 * @param nodeId id of node that is to heritage test
	 * @return true if node is a descendant of parent
	 */
	@Query(nativeQuery = true, value = "SELECT exists(WITH RECURSIVE children AS(" +
		"SELECT id, root_id, parent_id, height FROM org_units WHERE id = ?1 " +
		"UNION " +
		"SELECT ou.id, ou.root_id, ou.parent_id, ou.height FROM org_units ou " +
		"INNER JOIN children c ON c.id = ou.parent_id " +
		") SELECT true FROM children WHERE id = ?2);")
	boolean isNodeDescendantOf(long parentId, long nodeId);

	/***
	 * Sets the root node id and adjusts the height a node and its subtree.
	 *
	 * @param nodeId id of node for which to perform subtree adjustment
	 * @param newRootId id of new root to set on the subtree
	 * @param heightDifference height adjustment (can be zero and negative) to add to height the subtree
	 */
	@Modifying
	@Query(nativeQuery = true, value = "UPDATE org_units " +
		"SET root_id = ?2, height = height + ?3 " +
		"where id in( " +
		"with recursive children as(" +
		"select root_id, id, parent_id from org_units where id = ?1 " +
		"union " +
		"select n.root_id, n.id, n.parent_id from org_units n " +
		"inner join children c on c.id = n.parent_id " +
		") select id from children);"
	)
	void adjustSubtreeRootAndHeight(long nodeId, long newRootId, long heightDifference);
}
