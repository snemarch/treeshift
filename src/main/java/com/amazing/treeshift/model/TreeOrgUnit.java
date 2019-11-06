package com.amazing.treeshift.model;

import java.util.*;

/**
 * Object model class for the Awesome Company organisational unit.
 * <p>
 * Decoupled from the database entity model, has a proper tree structure, expected to be used in collections
 * like hashmaps, and as data transfer objects.
 */
public class TreeOrgUnit {
	private long id;
	private long rootId;
	private long height;
	private List<TreeOrgUnit> children = new ArrayList<>();

	public void setRootId(long rootId) {
		this.rootId = rootId;
	}

	public void setHeight(long height) {
		this.height = height;
	}

	public TreeOrgUnit(long id) {
		this.id = id;
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

	public List<TreeOrgUnit> getChildren() {
		return children;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		TreeOrgUnit orgUnit = (TreeOrgUnit) o;
		return id == orgUnit.id;
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}
}
