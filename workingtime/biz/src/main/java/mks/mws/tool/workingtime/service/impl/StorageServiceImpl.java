package mks.mws.tool.workingtime.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.Getter;
import mks.mws.tool.workingtime.entity.Task;
import mks.mws.tool.workingtime.repository.AppRepository;
import mks.mws.tool.workingtime.repository.TaskRepository;
import mks.mws.tool.workingtime.service.StorageService;

@Service
public class StorageServiceImpl implements StorageService {

	@Autowired
	@Getter
	AppRepository appRepo;

	@Autowired
	@Getter
	TaskRepository taskRepo;

	@Override
	public List<Task> saveOrUpdate(List<Task> lstTasks) {
		List<Long> lstIds = appRepo.saveOrUpdate(lstTasks);

		// Update the Id of saved task
		int len = (lstIds != null) ? lstIds.size() : 0;
		for (int i = 0; i < len; i++) {
			lstTasks.get(i).setId(lstIds.get(i));
		}

		return lstTasks;
	}
}
