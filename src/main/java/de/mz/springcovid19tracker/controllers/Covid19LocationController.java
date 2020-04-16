/*******************************************************************************
 * MIT License
 * 
 * Copyright (C) 2020 Moctar Zaidane https://github.com/mzdn
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 ******************************************************************************/
package de.mz.springcovid19tracker.controllers;

import java.util.ArrayList;


import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import de.mz.springcovid19tracker.exceptions.ResourceNotFoundException;
import de.mz.springcovid19tracker.models.Latest;
import de.mz.springcovid19tracker.models.Location;
import de.mz.springcovid19tracker.models.Locations;
import de.mz.springcovid19tracker.services.Covid19TrackerService;


@RestController
@Validated
@RequestMapping("/api/v1")
public class Covid19LocationController {
	@Autowired
	private final Covid19TrackerService covid19TrackerService;
	
	public Covid19LocationController(Covid19TrackerService covid19TrackerService) {
		this.covid19TrackerService=covid19TrackerService;
	}
	
	@GetMapping("/locations")
	public ArrayList<Locations> locations() {
		return covid19TrackerService.getAllLocations();
	}
	@GetMapping("/locations/{id}")
	public @ResponseBody ResponseEntity<Location> 
		getLocationsById(@PathVariable("id") @Min(0) @Max(265) @NotNull Integer id) throws ResourceNotFoundException{
		//if(id == null ) {throw new ResourceNotFoundException("No Location found with id="+id);}
		ArrayList<Locations> all = covid19TrackerService.getAllLocations();
		for(Locations location: all) {
			ArrayList<Location> locList = location.getLocations();
			for(Location l: locList) {
				if(l.getId() == id) {
					return new ResponseEntity<Location>(l, HttpStatus.OK);
				}
			}
		}
		return new ResponseEntity<Location>(HttpStatus.NOT_FOUND);
	}
	
	@GetMapping("/locations/country/{countryCode}")
	public @ResponseBody ResponseEntity<Location> 
		getLocationsByCountryCode(@PathVariable("countryCode") @NotBlank @Size(max = 2) String countryCode) throws ResourceNotFoundException {
		if (countryCode == null || countryCode.isEmpty()) {throw new ResourceNotFoundException("No Location code found with code="+countryCode);}
		ArrayList<Locations> all = covid19TrackerService.getAllLocations();
		for(Locations location: all) {
			ArrayList<Location> locList = location.getLocations();
			for(Location l: locList) {
				if(l.getCountryCode().toString().toLowerCase().equals(countryCode.toLowerCase())) {
					return new ResponseEntity<Location>(l, HttpStatus.OK);
				}
			}
		}
		return new ResponseEntity<Location>(HttpStatus.NOT_FOUND);
	}
	@GetMapping("/locations/country/latest/{countryCode}")
	public @ResponseBody ResponseEntity<Latest> 
		getLocationsCountryLatestByCountryCode(@PathVariable("countryCode") @NotBlank @Size(max = 2) String countryCode) throws ResourceNotFoundException {
		if (countryCode == null || countryCode.isEmpty()) {throw new ResourceNotFoundException("No Location code found with code="+countryCode);}
		ArrayList<Locations> all = covid19TrackerService.getAllLocations();
		for(Locations location: all) {
			ArrayList<Location> locList = location.getLocations();
			for(Location l: locList) {
				if(l.getCountryCode().toString().toLowerCase().equals(countryCode.toLowerCase())) {		
					return new ResponseEntity<Latest>(l.getLatest(), HttpStatus.OK);
				}
			}
		}
		return new ResponseEntity<Latest>(HttpStatus.NOT_FOUND);
	}
	
	@GetMapping("/confirmed")
	public ArrayList<Location> confirmed() {
		//TODO
		return null;
	}
	@GetMapping("/deaths")
	public ArrayList<Location> deaths() {
		//TODO
		return null;
		
	}
	@GetMapping("/recovered")
	public ArrayList<Location> recovered() {
		//TODO
		return null;
	}
	
	@GetMapping("/latest")
	public Latest latest() {
		return covid19TrackerService.getLatest();
	}
	

	
}
