/**
 * Licensed to MKS Group under one or more contributor license
 * agreements. See the NOTICE file distributed with this work
 * for additional information regarding copyright ownership.
 * MKS Group licenses this file to you under the Apache License,
 * Version 2.0 (the "License"); you may not use this file
 * except in compliance with the License. You may obtain a
 * copy of the License at:
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package mks.mws.tool.workingtime.controller;

import java.time.LocalDate;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

/**
 * Handles requests for the application home page.
 */
@Controller
public class HomeController extends BaseController {
 
	/** 
	 * Left menu
	 * @see /workingtime-web/src/main/resources/configuration.json
	 */
	@Value("classpath:configuration.json")
    private Resource leftMenuData;
	/**
     * This method is called when binding the HTTP parameter to bean (or model).
     * 
     * @param binder result of load data from HTML to Java
     */
    @InitBinder
    protected void initBinder(WebDataBinder binder) {
        // Sample init of Custom Editor

		// Class<List<ItemKine>> collectionType = (Class<List<ItemKine>>)(Class<?>)List.class;
		// PropertyEditor orderNoteEditor = new MotionRuleEditor(collectionType);
		// binder.registerCustomEditor((Class<List<ItemKine>>)(Class<?>)List.class, orderNoteEditor);

    }
    
	/**
	 * Simply selects the home view to render by returning its name.
	 * Prepare data for view:
	 * currentSiteId: current site id which is running this tool (apply for Platform runtime)
	 * userDisplayName: Full name of logged in user.
     * @return Home page
	 */
	@RequestMapping(value = {"/", "/home"}, method = RequestMethod.GET)
	public ModelAndView displayHome(HttpServletRequest request, HttpSession httpSession) {
		LocalDate firstDayOfMonth = LocalDate.now().withDayOfMonth(1);
	    String fromDate = firstDayOfMonth.toString();
	    
	    String toDate = (String) httpSession.getAttribute("toDate");

	    // Lưu lại vào session để sử dụng sau này
	    httpSession.setAttribute("fromDate", fromDate);
	    httpSession.setAttribute("toDate", toDate);
	 
		ModelAndView mav = new ModelAndView("home");

		initSession(request, httpSession);
	    mav.addObject("fromDate", fromDate);
	    mav.addObject("toDate", toDate);
		mav.addObject("currentSiteId", getCurrentSiteId());
		mav.addObject("userDisplayName", getCurrentUserDisplayName());

		return mav;
	}
	
//    /**
//     * Provide data for Left menu
//     * @see /workingtime-web/src/main/webapp/resources/js/left_menu.js
//     * @param id null means get root node of the tree.
//     * @return json data for tree. Refer format from configuration file "/workingtime-web/src/main/resources/configuration.json"
//     * @throws IOException in case of reading configuration file "/workingtime-web/src/main/resources/configuration.json"
//     */
//    @GetMapping("/menu/getNodeRoot")
//    @ResponseBody
//    public String getNodeRoot(@RequestParam("id") Long id) throws IOException {
//    	// Read content of configuration file
//    	String jsonTreeNodes = IOUtils.toString(leftMenuData.getInputStream(), StandardCharsets.UTF_8);
//
//        return jsonTreeNodes;
//    }
//    
//    /**
//     * Process ajax request from Tree of Configuration.
//     * @see /workingtime-web/src/main/webapp/resources/js/left_menu.js
//     * @param id Identifier of the node.
//     * @return json data of sub nodes.
//     */
//    @GetMapping("/menu/getNodeChildren")
//    @ResponseBody
//    public String getNodeChildren(@RequestParam("id") Long id) {
//    	return "Child" + id;
//    }
}
