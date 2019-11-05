package com.amazing.treeshift.repository;

import com.amazing.treeshift.model.OrgUnit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Collection;

public interface OrgUnitRepository extends JpaRepository<OrgUnit, Long> {
	@Query(nativeQuery = true, value = "WITH RECURSIVE children AS(" +
		"SELECT id, root_id, parent_id, height FROM org_units WHERE id = ?1 " +
		"UNION " +
		"SELECT ou.id, ou.root_id, ou.parent_id, ou.height FROM org_units ou " +
		"INNER JOIN children c ON c.id = ou.parent_id " +
		") SELECT * FROM children;")
	Collection<OrgUnit> getRecursiveById(Long id);
}
