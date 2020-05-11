package de.mz.springcovid19tracker.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import de.mz.springcovid19tracker.services.Covid19TrackerService;

@Controller
public class HomeController {
	
	@Autowired
	Covid19TrackerService covid19TrackerService;
	
	@GetMapping("/")
	public String home(Model model) {
		model.addAttribute("latest", covid19TrackerService.getLatest());
		model.addAttribute("locations", covid19TrackerService.getAllLocations().get(0).getLocations());
		return "home";
	}

}
