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

public class Latest {

    private int confirmed;
    private int deaths;
    private int recovered;

	public Latest(int confirmed, int deaths, int recovered) {
		super();
		this.confirmed = confirmed;
		this.deaths = deaths;
		this.recovered = recovered;
	}
    
    public Latest() {
		// TODO Auto-generated constructor stub
	}

	public int getConfirmed() {
        return confirmed;
    }

    public int getDeaths() {
        return deaths;
    }

    public int getRecovered() {
        return recovered;
    }

	public void setConfirmed(int confirmed) {
		this.confirmed = confirmed;
	}

	public void setDeaths(int deaths) {
		this.deaths = deaths;
	}

	public void setRecovered(int recovered) {
		this.recovered = recovered;
	}

    

}
