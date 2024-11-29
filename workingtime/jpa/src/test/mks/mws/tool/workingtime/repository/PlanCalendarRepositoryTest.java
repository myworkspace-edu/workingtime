package mks.mws.tool.workingtime.repository;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import mks.mws.tool.workingtime.entity.PlanCalendar;

public class PlanCalendarRepositoryTest {
	PlanCalendarRepository repo = new PlanCalendarRepository();

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testSave() {
		PlanCalendar e = new PlanCalendar();

		Long id = repo.save(e);
		
		assertNotNull(id);
	}

}
