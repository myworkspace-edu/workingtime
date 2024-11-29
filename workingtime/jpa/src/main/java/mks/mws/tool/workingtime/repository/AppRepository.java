package mks.mws.tool.workingtime.repository;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import mks.mws.tool.workingtime.entity.Task;

/**  
 * This repository provide interfaces to save and update data.
 */
@Repository
public class AppRepository {
	@Autowired
	@Qualifier("jdbcTemplate0")
	private JdbcTemplate jdbcTemplate0;

	/**
	 * @param entities
	 * @return
	 */
	public List<Long> saveOrUpdate(List<Task> entities) {
		List<Long> ids = new ArrayList<Long>(); // Id of records after save or update.
		
		SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate0).withTableName("wt_task").usingGeneratedKeyColumns("id");
		
		Long id;
		for (Task e : entities) {
			if (e.getId() == null) {
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

	private void update(Task e) {
		// TODO Auto-generated method stub
		
	}

}
