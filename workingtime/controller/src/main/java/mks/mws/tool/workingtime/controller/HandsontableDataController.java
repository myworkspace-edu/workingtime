package mks.mws.tool.workingtime.controller;

import mks.mws.tool.workingtime.model.HandsontableData;
import mks.mws.tool.workingtime.service.HandsontableDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/handsontable")
public class HandsontableDataController {

    @Autowired
    private HandsontableDataService handsontableDataService;

    @GetMapping("/loaddata")
    public ResponseEntity<List<HandsontableData>> loadData() {
        List<HandsontableData> data = handsontableDataService.getAllData();
        return ResponseEntity.ok(data);
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerData(@RequestBody HandsontableData data) {
        try {
            handsontableDataService.saveData(data);
            return ResponseEntity.ok().body("{\"success\": true}");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("{\"success\": false, \"message\": \"" + e.getMessage() + "\"}");
        }
    }
}
