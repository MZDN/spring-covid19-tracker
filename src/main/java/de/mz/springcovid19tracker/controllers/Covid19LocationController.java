/*******************************************************************************
 * BSD 3-Clause License
 * 
 * Copyright (C) 2020, Moctar Zaidane https://github.com/mzdn
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * 
 * 1. Redistributions of source code must retain the above copyright notice,
 *    this list of conditions and the following disclaimer.
 * 
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 * 
 * 3. Neither the name of the copyright holder nor the names of its contributors
 *    may be used to endorse or promote products derived from this software
 *    without specific prior written permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
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
	
	@GetMapping("/latest")
	public Latest latest() {
		return covid19TrackerService.getLatest();
	}
	

	
}
