package com.amazing.treeshift.model;

import javax.persistence.*;

/**
 * Database entity class for the Awesome Company organisational unit.
 *
 * The organisational units form a directed acyclic graph (simple tree), and
 * are stored in a single database using a parentId -> id relation. It is
 * defined as a simple type with primitive values, since we're relying on native
 * SQL queries and not using advanced mapping capabilities of the data layer.
 *
 * Only necessary setters have been added.
 */
@Entity
@Table(name = "org_units")
public class OrgUnit {
	@Id
//	@GeneratedValue(strategy = GenerationType.IDENTITY)
	// We need to be able to manually specify the id, see note in OrgUnitServiceImpl.persistTree.
	@Column(name = "id")
	private long id;

	@Column(name = "root_id", nullable = false)
	private long rootId;

	@Column(name = "height", nullable = false)
	private long height;

	@Column(name = "parent_id")
	private Long parentId;

	public OrgUnit() {
	}

	public OrgUnit(long id, long rootId, long height, Long parentId) {
		this.id = id;
		this.rootId = rootId;
		this.height = height;
		this.parentId = parentId;
	}

	public long getId() {
		return id;
	}

	public long getRootId() {
		return rootId;
	}

	public long getHeight() {
		return height;
	}

	public Long getParentId() {
		return parentId;
	}

	public void setParentId(long id) {
		this.parentId = id;
	}
}
