package mks.mws.tool.workingtime.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import mks.mws.tool.workingtime.entity.Task;

public interface TaskRepository extends JpaRepository<Task, Long> {
	
	List<Task> findAll();
}
