package com.amazing.treeshift.controller;

import com.amazing.treeshift.model.OrgUnit;
import com.amazing.treeshift.repository.OrgUnitRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RestController
@RequestMapping("/api")
public class OrgUnitController {
	@Autowired
	private OrgUnitRepository our;

	@GetMapping(path = "/org-units/{id}/flat", produces = "application/json")
	public Collection<OrgUnit> getOrgUnitsFlat(@PathVariable long id) {
		return our.getRecursiveById(id);
	}
}
