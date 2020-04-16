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
package de.mz.springcovid19tracker.models;

import java.util.ArrayList;
import java.util.HashMap;


public class Location {
	
	private int id;
	private String province;
	private String country;
	private String countryCode;
	private Coordinates coordinates;
	private HashMap<String, ArrayList<History>> histories;
	private Latest latest;
	private String lastUpdated;
	

	public Location(int id, String province, String country, String countryCode, Coordinates coordinates,
			HashMap<String, ArrayList<History>> histories, Latest latest, String lastUpdated) {
		super();
		this.id = id;
		this.province = province;
		this.country = country;
		this.countryCode = countryCode;
		this.coordinates = coordinates;
		this.histories = histories;
		this.latest = latest;
		this.lastUpdated = lastUpdated;
	}

	public Location() {
		super();
	}

	public String getProvince() {
		return province;
	}
	public void setProvince(String province) {
		this.province = province;
	}
	public String getCountry() {
		return country;
	}
	public void setCountry(String country) {
		this.country = country;
	}
	public Coordinates getCoordinates() {
		return coordinates;
	}
	public void setCoordinates(Coordinates coordinates) {
		this.coordinates = coordinates;
	}



	public String getLastUpdated() {
		return lastUpdated;
	}

	public void setLastUpdated(String lastUpdated) {
		this.lastUpdated = lastUpdated;
	}

	public Latest getLatest() {
		return latest;
	}

	public void setLatest(Latest latest) {
		this.latest = latest;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public HashMap<String, ArrayList<History>> getHistories() {
		return histories;
	}

	public void setHistories(HashMap<String, ArrayList<History>> histories) {
		this.histories = histories;
	}

	public String getCountryCode() {
		return countryCode;
	}

	public void setCountryCode(String countryCode) {
		this.countryCode = countryCode;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Location [id=");
		builder.append(id);
		builder.append(", province=");
		builder.append(province);
		builder.append(", country=");
		builder.append(country);
		builder.append(", countryCode=");
		builder.append(countryCode);
		builder.append(", coordinates=");
		builder.append(coordinates);
		builder.append(", histories=");
		builder.append(histories);
		builder.append(", latest=");
		builder.append(latest);
		builder.append(", lastUpdated=");
		builder.append(lastUpdated);
		builder.append("]");
		return builder.toString();
	}




	

}
