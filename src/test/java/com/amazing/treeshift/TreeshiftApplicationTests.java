package com.amazing.treeshift;

import com.amazing.treeshift.repository.OrgUnitRepository;
import com.amazing.treeshift.service.OrgUnitService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.transaction.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class TreeshiftApplicationTests {
	@Autowired
	private OrgUnitService ouService;

	@Autowired
	private OrgUnitRepository ouRepository;

	@Test
	void contextLoads() {
	}

	@Test
	@Transactional
	void failsOnMissingSource() {
		var ex = assertThrows(IllegalArgumentException.class, () ->
			ouService.moveNodeToNewParent(1_000_000, 0)
		);

		assertEquals("Source node doesn't exist", ex.getMessage());
	}

	@Test
	@Transactional
	void failsOnMissingTarget() {
		var ex = assertThrows(IllegalArgumentException.class, () ->
			ouService.moveNodeToNewParent(0, 1_000_000)
		);

		assertEquals("Target parent doesn't exist", ex.getMessage());
	}

	@Test
	@Transactional
	void moveTreeSucceedsOtherTreeFastPath() {
		ouService.moveNodeToNewParent(120, 1000);
		var ou = ouRepository.getOne(120L);

		assertEquals(1000, ou.getParentId(), "parent");
		assertEquals(1, ou.getHeight(), "height");
	}

	@Test
	@Transactional
	void moveTreeSucceedsNotChildFastPath() {
		ouService.moveNodeToNewParent(120, 3);
		var ou = ouRepository.getOne(120L);

		assertEquals(3, ou.getParentId(), "parent");
		assertEquals(2, ou.getHeight(), "height");
	}

	@Test
	@Transactional
	void moveTreeSucceedsSlowPath() {
		ouService.moveNodeToNewParent(2, 122);
		var ou = ouRepository.getOne(2L);

		assertEquals(122, ou.getParentId(), "parent");
		assertEquals(4, ou.getHeight(), "height");
	}

	@Test
	@Transactional
	void moveTreeToSelfFails() {
		var ex = assertThrows(IllegalArgumentException.class, () ->
			ouService.moveNodeToNewParent(0, 0)
		);

		assertTrue(ex.getMessage().contains("Cannot move node to itself"));
	}

	@Test
	@Transactional
	void moveTreeToDescendantFails() {
		var ex = assertThrows(IllegalArgumentException.class, () ->
			ouService.moveNodeToNewParent(1, 1213)
		);

		assertTrue(ex.getMessage().contains("Trying to move node to its descendant"));
	}
}
