package mks.mws.tool.workingtime.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import mks.mws.tool.workingtime.model.WorkingCalendar;

@Repository
public interface WorkingCalendarRepository extends JpaRepository<WorkingCalendar, Long> {
}
