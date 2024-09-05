package mks.mws.tool.workingtime.controller;

import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * LanguageController is responsible for handling language change requests. It
 * allows users to change the language of the application by specifying a
 * language code.
 */
@Controller
public class LanguageController {

	private final LocaleResolver localeResolver;
	private final MessageSource messageSource;

	public LanguageController(LocaleResolver localeResolver, MessageSource messageSource) {
		this.localeResolver = localeResolver;
		this.messageSource = messageSource;
	}

	@RequestMapping("/change-language")
	public String changeLanguage(@RequestParam("lang") String lang, HttpServletRequest request,
			HttpServletResponse response, RedirectAttributes redirectAttributes) {
		// Validate the language parameter
		if (lang == null || lang.isEmpty()) {
			// Handle invalid language code (optional)
			redirectAttributes.addFlashAttribute("error", "Invalid language code.");
			return "redirect:/"; // Redirect to the home page or error page
		}

		// Set the locale based on the language parameter
		Locale locale = new Locale(lang);
		localeResolver.setLocale(request, response, locale);

		// Add error messages to the redirect attributes
		redirectAttributes.addFlashAttribute("fromDateRequiredMessage",
				messageSource.getMessage("FROM_DATE_REQUIRED", null, locale));
		redirectAttributes.addFlashAttribute("toDateRequiredMessage",
				messageSource.getMessage("TO_DATE_REQUIRED", null, locale));
		redirectAttributes.addFlashAttribute("specialCharNotAllowedMessage",
				messageSource.getMessage("SPECIAL_CHAR_NOT_ALLOWED", null, locale));
		redirectAttributes.addFlashAttribute("nameRequiredMessage",
				messageSource.getMessage("NAME_REQUIRED", null, locale));
		redirectAttributes.addFlashAttribute("toDateAfterFromDateMessage",
				messageSource.getMessage("TO_DATE_AFTER_FROM_DATE", null, locale));
		redirectAttributes.addFlashAttribute("allCellsFilledMessage",
				messageSource.getMessage("ALL_CELLS_FILLED", null, locale));
		redirectAttributes.addFlashAttribute("invalidCellValueMessage",
				messageSource.getMessage("INVALID_CELL_VALUE", null, locale));
		redirectAttributes.addFlashAttribute("mustRegisterNoteMessage",
				messageSource.getMessage("MUST_REGISTER_NOTE", null, locale));

		// Redirect to the home page or the page where messages should be displayed
		return "redirect:/"; // Change to the appropriate path if needed
	}

	private boolean isValidLanguageCode(String lang) {
		// Add the valid language codes you support here
		return lang.equals("en") || lang.equals("ja") || lang.equals("vi") || lang.equals("ko");
	}
}
