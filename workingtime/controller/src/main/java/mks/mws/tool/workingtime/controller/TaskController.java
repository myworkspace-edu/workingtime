package mks.mws.tool.workingtime.controller;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import lombok.extern.slf4j.Slf4j;
import mks.mws.tool.workingtime.common.model.TableStructure;
import mks.mws.tool.workingtime.entity.Task;
import mks.mws.tool.workingtime.service.StorageService;
import mks.mws.tool.workingtime.transformer.JpaTransformer;
import mks.mws.tool.workingtime.validate.TaskValidator;

/**
 * Handles requests for Tasks.
 */
@Controller
@Slf4j
@RequestMapping("/task")
public class TaskController extends BaseController {

	@Value("classpath:task/task-demo.json")
	private Resource resTaskDemo;

	@Autowired
	StorageService storageService;

	@GetMapping("")
	public ModelAndView displayHome(HttpServletRequest request, HttpSession httpSession) {
		ModelAndView mav = new ModelAndView("task");

		initSession(request, httpSession);
		return mav;
	}

	@GetMapping("/load")
	@ResponseBody
	public Object getTaskData() throws IOException {
		log.debug("Get sample data from configuration file.");
		String jsonTaskTable = getDefaultTaskData();

		List<Task> lstTasks = storageService.getTaskRepo().findAll();

		if (lstTasks == null || lstTasks.isEmpty()) {
			return jsonTaskTable;
		} else {
			JSONObject jsonObjTableTask = new JSONObject(jsonTaskTable);

			JSONArray jsonObjColWidths = jsonObjTableTask.getJSONArray("colWidths");
			int len = (jsonObjColWidths != null) ? jsonObjColWidths.length() : 0;
			int[] colWidths = new int[len];
			for (int i = 0; i < jsonObjColWidths.length(); i++) {
				colWidths[i] = jsonObjColWidths.getInt(i);
			}

			JSONArray jsonObjColHeaders = jsonObjTableTask.getJSONArray("colHeaders");
			len = (jsonObjColHeaders != null) ? jsonObjColHeaders.length() : 0;
			String[] colHeaders = new String[len];
			for (int i = 0; i < jsonObjColHeaders.length(); i++) {
				colHeaders[i] = jsonObjColHeaders.getString(i);
			}

			List<Object[]> tblData = JpaTransformer.convert2D(lstTasks);

			TableStructure tblTask = new TableStructure(colWidths, colHeaders, tblData);

			return tblTask;
		}
	}

	@PostMapping(value = "/save")
	@ResponseBody
	public TableStructure saveTask(@RequestBody TableStructure tableData) {
		log.debug("saveTask...{}", tableData);

		try {
			List<Task> lstTasks = TaskValidator.validateAndCleasing(tableData.getData());
			lstTasks = storageService.saveOrUpdate(lstTasks);

			List<Object[]> tblData = JpaTransformer.convert2D(lstTasks);
			tableData.setData(tblData);
		} catch (Exception ex) {
			log.error("Could not save task.", ex);
		}

		return tableData;
	}

	private String getDefaultTaskData() throws IOException {
		return IOUtils.toString(resTaskDemo.getInputStream(), StandardCharsets.UTF_8);
	}
}

//
//@Controller
//@Slf4j
//@RequestMapping("/task")
//public class TaskController {
//
//  @Autowired
//  StorageService storageService;
//
//  @GetMapping("")
//  public ModelAndView displayHome(HttpServletRequest request, HttpSession httpSession) {
//      ModelAndView mav = new ModelAndView("task");
//      
//      return mav;
//  }
//
//  @GetMapping("/load")
//  @ResponseBody
//  public Object getTaskData() throws IOException {
//      log.debug("Get sample data from configuration file.");
//      String jsonTaskTable = getDefaultTaskData();
//
//      List<Task> lstTasks = storageService.getTaskRepo().findAll();
//
//      if (lstTasks == null || lstTasks.isEmpty()) {
//          return jsonTaskTable;
//      } else {
//          JSONObject jsonObjTableTask = new JSONObject(jsonTaskTable);
//
//          JSONArray jsonObjColWidths = jsonObjTableTask.getJSONArray("colWidths");
//          int len = (jsonObjColWidths != null) ? jsonObjColWidths.length() : 0;
//          int[] colWidths = new int[len];
//          for (int i = 0; i < jsonObjColWidths.length(); i++) {
//              colWidths[i] = jsonObjColWidths.getInt(i);
//          }
//
//          JSONArray jsonObjColHeaders = jsonObjTableTask.getJSONArray("colHeaders");
//          len = (jsonObjColHeaders != null) ? jsonObjColHeaders.length() : 0;
//          String[] colHeaders = new String[len];
//          for (int i = 0; i < jsonObjColHeaders.length(); i++) {
//              colHeaders[i] = jsonObjColHeaders.getString(i);
//          }
//
//          List<Object[]> tblData = JpaTransformer.convert2D(lstTasks);
//          TableStructure tblTask = new TableStructure(colWidths, colHeaders, tblData);
//
//          return tblTask;
//      }
//  }
//
//  @PostMapping(value = "/save")
//  @ResponseBody
//  public TableStructure saveTask(@RequestBody TableStructure tableData) {
//      log.debug("saveTask...{}", tableData);
//
//      try {            
//          List<Task> lstTasks = TaskValidator.validateAndCleasing(tableData.getData());
//          lstTasks = storageService.saveOrUpdate(lstTasks);
//
//          List<Object[]> tblData = JpaTransformer.convert2D(lstTasks);
//          tableData.setData(tblData);
//      } catch (Exception ex) {
//          log.error("Could not save task.", ex);
//      }
//
//      return tableData;
//  }
//}
