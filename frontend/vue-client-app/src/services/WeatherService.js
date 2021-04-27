/**
 * WeatherService is a script which finds the weather in a given place at a given time.
 * NB: the startTime of activity needs to be within the next 7 days of the current date.
 * If not, the method will return null.
 * 
 * @author Scott Rydberg Sonen
 */

/**
 * WeatherService is exporting getWeather-function
 */
export const weatherService = {
    getWeather,
}

/**
 * weather is variable which will contain return value of fetch
 */
let weather = null;

/**
 * getWeather is an asynchronous function which finds the weather in a given place,
 * if startTime is within 7 days of current time.
 * 
 * @param latitude: latitude of activity place
 * @param longitude: longitude of activity place
 * @param time: startTime of activity
 * @returns weather of activity place
 */
async function getWeather(latitude, longitude, time) {
    const startTime = new Date(time).getTime();
    const start = (startTime-(startTime%1000))/1000; //Start time
    console.log("Start time: " + start);
    const currentTime = Date.now();
    const current = (currentTime-(currentTime%1000))/1000; //Current time
    console.log("Current time: " + current);
    const diff = start - current; //Difference in milliseconds between current date and start date of activity
    if ((diff >= 0) && (diff <= 604800000)) { //Checking if difference is bigger than 7 days
        //Fetching weather from openweathermap API
        await fetch("https://api.openweathermap.org/data/2.5/onecall?lat=" + latitude
        + "&lon="+ longitude
        + "&appid=3e2762909a752f554a04ad7972f1a13d"
        + "&lang=no&units=metric")
        .then(response => response.json())
        .then(data => {
            console.log(data);
            let dailyDt = 0;
            let nextDt = 0;
            for (let i = 0; i < 7; i++) {
                dailyDt = data["daily"][i]["dt"];
                nextDt = data["daily"][i+1]["dt"];
                //Checking if startTime of activity is in interval between two daily object
                if ((dailyDt <= start) && (nextDt >= start)) {
                    //Defining the weather with relevant variables
                    weather = {
                        temp: Math.round(data["daily"][i]["temp"]["day"]),
                        description: data["daily"][i]["weather"][0]["description"],
                        icon: data["daily"][i]["weather"][0]["icon"]
                    }
                    console.log("Daily dt: " + data["daily"][i]["dt"]);
                    console.log("Found weather: " + data["daily"][i]);
                }
            }
        })
        .catch(error => console.log(error));
    } else {
        console.log("Værvarsel er ikke tilgjengelig da differansen mellom nåværende tid og starttid er mer enn 7 dager");
        return null;
    }
    console.log(weather);
    return weather;
}