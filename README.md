# spring-covid19-tracker
Java Spring boot API for tracking coronavirus (COVID-19) based on JHU

## Data source:
JHU data - https://github.com/CSSEGISandData/COVID-19 - Worldwide Data repository operated by the Johns Hopkins University Center for Systems Science and Engineering (JHU CSSE). 

## Development && Run
´´´
git clone https://github.com/MZDN/spring-covid19-tracker.git
cd spring-covid19-tracker

´´´
## Run
To build and run from a packaged jar locally:

´´´
mvn spring-boot:run

´´´

OR

´´´
mvn clean package -Dboot
java -jar target/spring-covid19-tracker-0.0.1-SNAPSHOT.jar

´´´

### API Access
´´´
[http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)
´´´

## Test
Get latest global data
´´´
curl http://localhost:8080/api/v1/latest

{"confirmed":2152646,"deaths":143800,"recovered":542107}

´´´
Get country latest data by given country code

´´´

curl http://localhost:8080/api/v1/locations/country/latest/{countryCode}

´´´

