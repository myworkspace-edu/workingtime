package mks.mws.tool.workingtime.service.impl;

import mks.mws.tool.workingtime.model.HandsontableData;
import mks.mws.tool.workingtime.repository.HandsontableDataRepository;
import mks.mws.tool.workingtime.service.HandsontableDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HandsontableDataServiceImpl implements HandsontableDataService {

    @Autowired
    private HandsontableDataRepository handsontableDataRepository;

    @Override
    public List<HandsontableData> getAllData() {
        return handsontableDataRepository.findAll();
    }

    @Override
    public void saveData(HandsontableData data) {
        handsontableDataRepository.save(data);
    }
}
