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

import mks.mws.tool.workingtime.entity.TeamWorkingCalendar;

@Repository
public class WorkingCalendarRepository {

	@Autowired
	@Qualifier("jdbcTemplate0")
	private JdbcTemplate jdbcTemplate0;

	public List<Long> saveOrUpdate(List<TeamWorkingCalendar> entities) {
	    List<Long> ids = new ArrayList<>();
	    SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate0).withTableName("wt_teamcal")
	            .usingGeneratedKeyColumns("id");

	    long id;
	    for (TeamWorkingCalendar e : entities) {
	        boolean existingUser = existingUser(e.getId());

	        if (existingUser == false) {
	            id = simpleJdbcInsert.executeAndReturnKey(new BeanPropertySqlParameterSource(e)).longValue();
	        } else {
	            id = e.getId();
	            update(e);
	        }

	        ids.add(id);
	    }
	    return ids;
	}

	private void update(TeamWorkingCalendar e) {
		String updateSql = "UPDATE wt_teamcal SET from_date = ?, to_date = ?, account = ?, section = ?, "
				+ "mon = ?, tue = ?, wed = ?, thur = ?, fri = ?, sat = ?, sun = ?, note = ? WHERE id = ?";

		jdbcTemplate0.update(updateSql, e.getFromDate(), e.getToDate(), e.getAccount(), e.getSection(), e.getMon(),
				e.getTue(), e.getWed(), e.getThur(), e.getFri(), e.getSat(), e.getSun(), e.getNote(), e.getId());
	}
	
	private boolean existingUser(Long id) {
	    String querySql = "SELECT COUNT(*) FROM wt_teamcal WHERE id = ?";
	    int count = jdbcTemplate0.queryForObject(querySql, Integer.class, id);
	    
	    return count > 0;
	}
}