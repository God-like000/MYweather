package com.example.myweather;

public class  weather {
private String province;
private String city;
private String adcode;
private String weather;
private String temperature;
private String winddirection;
private String windpower;
private String humidity;
private String reporttime;

    public String getProvince() { return province; }
    public void setProvince(String province) { this.province = province; }

    public String getAdcode() {
        return adcode;
    }
    public void setAdcode(String adcode) {
        this.adcode = adcode;
    }

    public String getCity() { return city; }
    public void setCity(String city) {
        this.city = city;
    }

    public String getWeather() {
        return weather;
    }
    public void setWeather(String weather) {
        this.weather = weather;
    }

    public String getTemperature() {
        return temperature;
    }
    public void setTemperature(String temperature) {
        this.temperature = temperature;
    }

    public String getWinddirection() {
        return winddirection;
    }
    public void setWinddirection(String winddirection) {
        this.winddirection = winddirection;
    }

    public String getWindpower() {
        return windpower;
    }
    public void setWindpower(String windpower) {
        this.windpower = windpower;
    }

    public String getHumidity() {
        return humidity;
    }
    public void setHumidity(String humidity) {
        this.humidity = humidity;
    }

    public String getReporttime() {
        return reporttime;
    }
    public void setReporttime(String reporttime) {
        this.reporttime = reporttime;
    }

    @Override
    public String toString() {
        return "天气情况{" + '\n' +
                "省 : " + province + '\n' +
                "城市 : " + city + '\n' +
                "城市编码 : " + adcode + '\n' +
                "天气 : " + weather + '\n' +
                "温度= : " + temperature + '\n' +
                "风向 : " + winddirection + '\n' +
                "风力 : " + windpower + '\n' +
                "湿度 : " + humidity + '\n' +
                "时间 : " + reporttime + '\n' +
                '}';
    }
}
