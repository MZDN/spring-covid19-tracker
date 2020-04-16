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
package de.mz.springcovid19tracker.services;

import java.io.IOException;
import java.io.StringReader;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import javax.annotation.PostConstruct;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import de.mz.springcovid19tracker.models.Coordinates;
import de.mz.springcovid19tracker.models.History;
import de.mz.springcovid19tracker.models.Latest;
import de.mz.springcovid19tracker.models.Location;
import de.mz.springcovid19tracker.models.Locations;

@Service
public class Covid19TrackerService {

	private String confirmedDataURL = "https://raw.githubusercontent.com/CSSEGISandData/COVID-19/master/csse_covid_19_data/csse_covid_19_time_series/time_series_covid19_confirmed_global.csv";
	private String deathsDataURL = "https://raw.githubusercontent.com/CSSEGISandData/COVID-19/master/csse_covid_19_data/csse_covid_19_time_series/time_series_covid19_deaths_global.csv";
	private String recoveredDataURL = "https://raw.githubusercontent.com/CSSEGISandData/COVID-19/master/csse_covid_19_data/csse_covid_19_time_series/time_series_covid19_recovered_global.csv";

	
	private Latest latest = null;

	private HashMap<Latest, ArrayList<Locations>> locationsData = new HashMap<Latest, ArrayList<Locations>>();

	private ArrayList<Locations> allLocations = new ArrayList<>();
	private int totalConfirmed = 0;
	private int totalDeaths = 0;
	private int totalRecovered = 0;

	private Instant updateTimeUTC;

	public Latest getLatest() {
		return latest;
	}


	@PostConstruct
	@Scheduled(cron = "* * 1 * * *")
	public void fetchCovid19Data() throws IOException, InterruptedException {
		updateTimeUTC = Instant.now();
		CSVParser parserConfirmed = getData(confirmedDataURL);
		CSVParser parserDeaths = getData(deathsDataURL);
		CSVParser parserRecovered = getData(recoveredDataURL);

		final List<CSVRecord> recordsConfirmed = parserConfirmed.getRecords();
		Set<String> headers = recordsConfirmed.iterator().next().toMap().keySet();

		final List<CSVRecord> recordsDeaths = parserDeaths.getRecords();

		final List<CSVRecord> recordsRecovered = parserRecovered.getRecords();

		int countryConfirmed = 0;
		int countryDeaths = 0;
		int countryRecovered = 0;

		// System.out.println("hearders-size: "+headers.size());
		System.out.println("c-size: " + recordsConfirmed.size() + " d-size: " + recordsDeaths.size() + " r-size: "
				+ recordsRecovered.size());

		ArrayList<Locations> newAllLocations = new ArrayList<Locations>();
		ArrayList<Location> newLocations = new ArrayList<Location>();

		for (int i = 0; i < recordsConfirmed.size(); i++) {

			CSVRecord confirmed = recordsConfirmed.get(i);
			CSVRecord deaths = recordsDeaths.get(i);
			CSVRecord recovered = null;
			if (i < recordsRecovered.size()) {
				recovered = recordsRecovered.get(i);
				// System.out.println(recovered.get(headers.size()-1));
				totalRecovered+= Integer.valueOf(recovered.get(headers.size()-1));

			}

			String province = confirmed.get("Province/State");
			String country = confirmed.get("Country/Region");
			String lat = confirmed.get("Lat");
			String lon = confirmed.get("Long");

			Coordinates coordinates = new Coordinates(lat, lon);
 
			totalConfirmed += Integer.valueOf(confirmed.get(headers.size() - 1));
			totalDeaths += Integer.valueOf(deaths.get(headers.size() - 1));
			//totalRecovered += Integer.valueOf(recovered.get(headers.size() - 1));

			ArrayList<History> histConfirmedArrayList = new ArrayList<History>();
			ArrayList<History> histDeathsArrayList = new ArrayList<History>();
			ArrayList<History> histRecoveredArrayList = new ArrayList<History>();

			histConfirmedArrayList = getCountryHistories(country, recordsConfirmed);
			histDeathsArrayList = getCountryHistories(country, recordsDeaths);
			histRecoveredArrayList = getCountryHistories(country, recordsRecovered);
			
			countryConfirmed = histConfirmedArrayList.get(histConfirmedArrayList.size() - 1).getNumber();
			countryDeaths = histDeathsArrayList.get(histDeathsArrayList.size() - 1).getNumber();
			countryRecovered = histRecoveredArrayList.get(histRecoveredArrayList.size() - 1).getNumber();
			
			HashMap<String, ArrayList<History>> histories = new HashMap<String, ArrayList<History>>();
			histories.put("confirmedHistory", histConfirmedArrayList);
			histories.put("deathsHistory", histDeathsArrayList);
			histories.put("recoveredHistory", histRecoveredArrayList);
			
			
			Latest countryLatest = new Latest(countryConfirmed, countryDeaths, countryRecovered);
			String countryCode = getCountryCodeByName(country);
			Location location = new Location(i,province, country,countryCode, coordinates, histories, countryLatest,updateTimeUTC.toString());
			newLocations.add(location);
		}
		
		Latest totalLatest = new Latest(totalConfirmed, totalDeaths, totalRecovered);
		Locations locations = new Locations(totalLatest, newLocations);
		newAllLocations.add(locations);
		
		this.latest = totalLatest;		
		this.allLocations = newAllLocations;
	}
	
		
	private ArrayList<History> getCountryHistories(String countryName, List<CSVRecord> records) {
		ArrayList<History> histArrayList = new ArrayList<History>();
		
		Set<String> headers = records.iterator().next().toMap().keySet();
		Object[] headersArray = headers.toArray();
	
		for(CSVRecord record : records ) 	{
			if(countryName.equals(record.get("Country/Region"))){
				
				for (int k = 4; k < headers.size(); k++) {

					String date = headersArray[k].toString();
					int number = Integer.valueOf(record.get(k));
					History history = new History(date, number);
					histArrayList.add(history);
				}
				
			}	
		}
		return histArrayList;

	}

	private CSVParser getData(String URL) throws IOException, InterruptedException {
		HttpClient client = HttpClient.newHttpClient();

		HttpRequest request = HttpRequest.newBuilder().uri(URI.create(URL)).build();
		HttpResponse<String> httpResponse = client.send(request, HttpResponse.BodyHandlers.ofString());
		// System.out.println(httpResponse.body());
		StringReader csvBodyReader = new StringReader(httpResponse.body());
		CSVFormat format = CSVFormat.DEFAULT.withFirstRecordAsHeader();
		CSVParser parser = new CSVParser(csvBodyReader, format);
		return parser;
	}

	public HashMap<Latest, ArrayList<Locations>> getLocationsData() {
		return locationsData;
	}

	public ArrayList<Locations> getAllLocations() {
		return allLocations;
	}
	
	public String getCountryCodeByName(String countryName) {

		String[] locales = Locale.getISOCountries();
		HashMap <String,String> NOT_FOUND_IN_ISO = new HashMap<String, String>();
		NOT_FOUND_IN_ISO.put("Antigua and Barbuda", "AG");
		NOT_FOUND_IN_ISO.put("Bosnia and Herzegovina", "BA");
		NOT_FOUND_IN_ISO.put("Cabo Verde", "CV");
		for (String countryCode : locales) {
			Locale obj = new Locale("", countryCode);
			if(obj.getDisplayCountry().equals(countryName)) {
				return countryCode;
			}else if(NOT_FOUND_IN_ISO.containsKey(countryName)) {
				return NOT_FOUND_IN_ISO.get(countryName);
			}
		}
		return "XX";
	}

}
