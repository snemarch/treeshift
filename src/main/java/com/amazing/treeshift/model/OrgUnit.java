package com.amazing.treeshift.model;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "org_units")
public class OrgUnit {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private long id;

	@Column(name = "root_id", nullable = false)
	private long rootId;

	@Column(name = "height", nullable = false)
	private long height;

	@Column(name = "parent_id")
	private Long parentId;

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

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		OrgUnit orgUnit = (OrgUnit) o;
		return id == orgUnit.id;
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}
}
