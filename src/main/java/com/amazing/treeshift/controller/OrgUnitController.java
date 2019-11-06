package com.amazing.treeshift.controller;

import com.amazing.treeshift.model.OrgUnit;
import com.amazing.treeshift.model.TreeOrgUnit;
import com.amazing.treeshift.repository.OrgUnitRepository;
import com.amazing.treeshift.service.OrgUnitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RestController
@RequestMapping("/api")
public class OrgUnitController {
	@Autowired
	private OrgUnitRepository ouRepository;

	@Autowired
	private OrgUnitService ouService;

	@GetMapping(path = "/org-units/{id}/flat", produces = "application/json")
	public Collection<OrgUnit> getOrgUnitsFlat(@PathVariable long id) {
		return ouRepository.getRecursiveById(id);
	}

	@GetMapping(path = "/org-units/{id}/tree", produces = "application/json")
	public TreeOrgUnit getOrgUnitsTree(@PathVariable long id) {
		return ouService.buildTreeFromNodes(id, ouRepository.getRecursiveById(id));
	}

	@PostMapping(path = "/org-units/{id}/move-to/{newParentId}")
	public void moveOrgUnit(@PathVariable long id, @PathVariable long newParentId) {
		ouService.moveNodeToNewParent(id, newParentId);
	}

	@GetMapping(path = "/org-units/{id}/is-descendant-of/{parentId}")
	public boolean isDescendantOf(@PathVariable long id, @PathVariable long parentId) {
		return ouRepository.isNodeDescendantOf(parentId, id);
	}
}
