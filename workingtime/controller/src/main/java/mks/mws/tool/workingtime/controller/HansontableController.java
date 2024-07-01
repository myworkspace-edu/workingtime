package mks.mws.tool.workingtime.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import mks.mws.tool.workingtime.common.model.TableStructure;



@Controller
public class HansontableController extends BaseController {
	
	private String[] productListColHeaders = {"Account", "Section", "Mon", "Tue", "Wed", "Thur", "Fri", "Sat", "Sun"};
	

	private int[] productListColWidths = {100, 100, 100, 100, 100, 100, 100, 100, 100};

	private List<Object[]> lstProducts = new ArrayList<>();

	public HansontableController() {
		// Initialize with demo data
		lstProducts.add(new Object[] {"", "", "", "", "", "", "", "", ""});
	}

	@GetMapping(value = "/handsontable")
	public ModelAndView displayHome(HttpServletRequest request, HttpSession httpSession) {
		ModelAndView mav = new ModelAndView("handsontable");

		return mav;
	}
	
	@GetMapping(value = {"/handsontable/loaddata"}, produces="application/json")
	@ResponseBody
	public TableStructure getProductTableData() {
		TableStructure productTable = new TableStructure(productListColWidths, productListColHeaders, lstProducts);
		return productTable;
	}

	@PostMapping(value = "/handsontable/savedata", consumes = "application/json", produces = "application/json")
	@ResponseBody
	public String saveProductTableData(@RequestBody List<Object[]> newProductData) {
		if (newProductData != null && !newProductData.isEmpty()) {
			// Lưu dữ liệu mới vào danh sách hoặc cơ sở dữ liệu
			lstProducts.clear();
			lstProducts.addAll(newProductData);
			return "Data saved successfully";
		}
		return "No data to save";
	}
}
