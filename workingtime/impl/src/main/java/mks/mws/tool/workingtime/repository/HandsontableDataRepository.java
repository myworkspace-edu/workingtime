package mks.mws.tool.workingtime.repository;

import mks.mws.tool.workingtime.model.HandsontableData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HandsontableDataRepository extends JpaRepository<HandsontableData, Long> {
}
