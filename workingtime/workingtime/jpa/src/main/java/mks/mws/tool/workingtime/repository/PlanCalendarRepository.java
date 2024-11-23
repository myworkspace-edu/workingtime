package mks.mws.tool.workingtime.repository;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import mks.mws.tool.workingtime.entity.PlanCalendar;

/**  
 * This repository provide interfaces to save and update data.
 */
@Repository
public class PlanCalendarRepository {
	@Autowired
	@Qualifier("jdbcTemplate0")
	private JdbcTemplate jdbcTemplate0;

	public Long save(PlanCalendar e) {
		List<PlanCalendar> entities = new ArrayList<PlanCalendar>(1);
		entities.add(e);
		
		List<Long> ids = saveOrUpdate(entities);
		
		return ids.get(0);
	}
	
	/**
	 * @param entities
	 * @return
	 */
	public List<Long> saveOrUpdate(List<PlanCalendar> entities) {
		List<Long> ids = new ArrayList<Long>(); // Id of records after save or update.
		
		SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate0).withTableName("wt_plancal").usingGeneratedKeyColumns("id");
		
		Long id;
		for (PlanCalendar e : entities) {
			if (e.getId() == null) {
				if (e.getCreatedDate() == null) {
					e.setCreatedDate(new Date());
				}
				id = simpleJdbcInsert.executeAndReturnKey(new BeanPropertySqlParameterSource(e)).longValue();
			} else {
				// Update
				update(e);
				id = e.getId();
			}

			ids.add(id);
		}
		
		return ids;
	}

	private int update(PlanCalendar e) {
		String sql = "UPDATE wt_plancal SET user_id=?, section=?, from_date=?, to_date=?, mon=?, tue=?, wed=?, thu=?, fri=?, sat=?, sun=?, note=?, modified_date=? WHERE id=?";

		return jdbcTemplate0.update(sql,
				e.getUserId(),
				e.getSection(),
				e.getFromDate(),
				e.getToDate(),
				e.getMon(),
				e.getTue(),
				e.getWed(),
				e.getThu(),
				e.getFri(),
				e.getSat(),
				e.getSun(),
				new Date(),
				e.getId());
		
	}

}
