package mks.mws.tool.workingtime.repository;

import java.util.Date;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;

import mks.mws.tool.workingtime.entity.TeamWorkingCalendar;

public interface TeamWorkingCalendarRepository extends JpaRepository<TeamWorkingCalendar, Long> {
	List<TeamWorkingCalendar> findByFromDateAndToDate(Date fromDateStart, Date toDateEnd);

	@Transactional
	void deleteByFromDateAndToDate(Date fromDate, Date toDate);

	@Transactional
	void deleteByAccountAndFromDateAndToDate(String name, Date fromDate, Date toDate);

	List<TeamWorkingCalendar> findByAccountAndFromDateBetween(String name, Date fromDate, Date toDate);

	List<TeamWorkingCalendar> findAllByToDateLessThanEqual(Date toDate);

	List<TeamWorkingCalendar> findAllByFromDateGreaterThanEqual(Date fromDate);

	List<TeamWorkingCalendar> findAllByFromDateGreaterThanEqualAndToDateLessThanEqual(Date fromDate, Date toDate);

}
