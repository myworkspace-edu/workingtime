package mks.mws.tool.workingtime.service;

import java.util.List;

import mks.mws.tool.workingtime.entity.Task;
import mks.mws.tool.workingtime.repository.AppRepository;
import mks.mws.tool.workingtime.repository.TaskRepository;

public interface StorageService {
	AppRepository getAppRepo();

	TaskRepository getTaskRepo();

	List<Task> saveOrUpdate(List<Task> lstTasks);

}
