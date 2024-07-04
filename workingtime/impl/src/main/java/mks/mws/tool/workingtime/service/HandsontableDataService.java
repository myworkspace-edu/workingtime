package mks.mws.tool.workingtime.service;

import mks.mws.tool.workingtime.model.HandsontableData;

import java.util.List;

public interface HandsontableDataService {
    List<HandsontableData> getAllData();
    void saveData(HandsontableData data);
}
