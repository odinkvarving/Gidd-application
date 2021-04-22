export const weatherService = {
    getWeather,
    hello
}

let weather = null;

function hello(text) {
    console.log(text);
}

function getWeather(latitude, longitude, time) {
    console.log("TIME: " + time);
    const startTime = new Date(time);
    console.log("Start time: " + startTime);
    const startDate = new Date(startTime.getFullYear, startTime.getMonth, startTime.getDate);
    console.log("Start date: " + startDate);
    const currentTime = Date.now();
    console.log("Current time: " + currentTime);
    /*const current = new Date();
    const currentDate = current.getFullYear() + "-" 
        + (current.getMonth() + 1) + "-" 
        + current.getDate + "T" 
        + current.getHours + ":"
        + current.getMinutes + ":"
        + current.getSeconds + "."
        + current.getMilliseconds;*/
    const diff = startTime - currentTime; //Difference in milliseconds between current date and start date of activity
    console.log(diff);
    if (diff <= 1209600000) { //Checking if difference is bigger than 14 days
        /*fetch("https://api.openweathermap.org/data/2.5/onecall?lat=" + latitude
        + "&lon="+ longitude
        + "&appid=3e2762909a752f554a04ad7972f1a13d") //&exclude=current,minutely*/
        fetch("https://api.openweathermap.org/data/2.5/forecast/daily?lat=" + latitude
         + "&lon=" + longitude
         + "&cnt=" + 14
         + "&appid=3e2762909a752f554a04ad7972f1a13d")
        .then(response => response.json())
        .then(data => {
            for (let i = 0; i < 14; i++) {
                if ((data["list"][i]["dt"] <= startDate) && (data["list"][i+1]["dt"] >= startDate)) { //Checking to see if startdate is in interval
                    weather = {
                        name: data["city"]["name"],
                        temp: data["list"][i]["temp"]["day"],
                        description: data["list"][i]["weather"]["description"],
                        icon: "http://openweathermap.org/img/wn/" + data["list"][i]["weather"]["icon"]
                    }
                }
            }
            /*data["list"].array.forEach(day => {
                if (day["dt"] == startDate) {
                    //Eventuelt sjekke om startDate ligger mellom ulike dt i daily
                }
            });*/
        })
        .catch(error => console.log(error));
    } else {
        console.log("Differanse mellom datoene er mere enn 7 dager, og værvarsel er ikke tilgjengelig");
        return null;
    }
    console.log(weather);
    return weather;
}