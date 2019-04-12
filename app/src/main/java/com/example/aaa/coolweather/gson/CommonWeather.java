package com.example.aaa.coolweather.gson;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by aaa on 2019/4/11.
 */

public class CommonWeather {
    /**
     * basic : {"cid":"CN101010100","location":"北京","parent_city":"北京","admin_area":"北京","cnty":"中国","lat":"39.90498734","lon":"116.4052887","tz":"+8.00"}
     * update : {"loc":"2019-04-11 14:55","utc":"2019-04-11 06:55"}
     * status : ok
     * now : {"cloud":"91","cond_code":"101","cond_txt":"多云","fl":"13","hum":"29","pcpn":"0.0","pres":"1014","tmp":"17","vis":"9","wind_deg":"273","wind_dir":"西风","wind_sc":"3","wind_spd":"19"}
     * daily_forecast : [{"cond_code_d":"101","cond_code_n":"101","cond_txt_d":"多云","cond_txt_n":"多云","date":"2019-04-11","hum":"36","mr":"09:38","ms":"00:00","pcpn":"0.0","pop":"0","pres":"1009","sr":"05:41","ss":"18:49","tmp_max":"19","tmp_min":"6","uv_index":"4","vis":"25","wind_deg":"179","wind_dir":"南风","wind_sc":"1-2","wind_spd":"2"},{"cond_code_d":"101","cond_code_n":"101","cond_txt_d":"多云","cond_txt_n":"多云","date":"2019-04-12","hum":"58","mr":"10:32","ms":"00:45","pcpn":"0.0","pop":"2","pres":"1007","sr":"05:40","ss":"18:50","tmp_max":"22","tmp_min":"9","uv_index":"4","vis":"25","wind_deg":"252","wind_dir":"西南风","wind_sc":"3-4","wind_spd":"12"},{"cond_code_d":"101","cond_code_n":"101","cond_txt_d":"多云","cond_txt_n":"多云","date":"2019-04-13","hum":"17","mr":"11:34","ms":"01:42","pcpn":"0.0","pop":"3","pres":"1017","sr":"05:38","ss":"18:51","tmp_max":"23","tmp_min":"8","uv_index":"3","vis":"24","wind_deg":"345","wind_dir":"西北风","wind_sc":"3-4","wind_spd":"17"}]
     * lifestyle : [{"type":"comf","brf":"舒适","txt":"白天不太热也不太冷，风力不大，相信您在这样的天气条件下，应会感到比较清爽和舒适。"},{"type":"drsg","brf":"较舒适","txt":"建议着薄外套、开衫牛仔衫裤等服装。年老体弱者应适当添加衣物，宜着夹克衫、薄毛衣等。"},{"type":"flu","brf":"较易发","txt":"昼夜温差较大，较易发生感冒，请适当增减衣服。体质较弱的朋友请注意防护。"},{"type":"sport","brf":"适宜","txt":"天气较好，赶快投身大自然参与户外运动，尽情感受运动的快乐吧。"},{"type":"trav","brf":"适宜","txt":"天气较好，但丝毫不会影响您出行的心情。温度适宜又有微风相伴，适宜旅游。"},{"type":"uv","brf":"弱","txt":"紫外线强度较弱，建议出门前涂擦SPF在12-15之间、PA+的防晒护肤品。"},{"type":"cw","brf":"较适宜","txt":"较适宜洗车，未来一天无雨，风力较小，擦洗一新的汽车至少能保持一天。"},{"type":"air","brf":"中","txt":"气象条件对空气污染物稀释、扩散和清除无明显影响，易感人群应适当减少室外活动时间。"}]
     */

    private BasicBean basic;
    private UpdateBean update;
    private String status;
    private NowBean now;
    private List<DailyForecastBean> daily_forecast;
    private List<LifestyleBean> lifestyle;

    public BasicBean getBasic() {
        return basic;
    }

    public void setBasic(BasicBean basic) {
        this.basic = basic;
    }

    public UpdateBean getUpdate() {
        return update;
    }

    public void setUpdate(UpdateBean update) {
        this.update = update;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public NowBean getNow() {
        return now;
    }

    public void setNow(NowBean now) {
        this.now = now;
    }

    public List<DailyForecastBean> getDaily_forecast() {
        return daily_forecast;
    }

    public void setDaily_forecast(List<DailyForecastBean> daily_forecast) {
        this.daily_forecast = daily_forecast;
    }

    public List<LifestyleBean> getLifestyle() {
        return lifestyle;
    }

    public void setLifestyle(List<LifestyleBean> lifestyle) {
        this.lifestyle = lifestyle;
    }

    @Override
    public String toString() {
        return "CommonWeather{" +
                "basic=" + basic +
                ", update=" + update +
                ", status='" + status + '\'' +
                ", now=" + now +
                ", daily_forecast=" + daily_forecast +
                ", lifestyle=" + lifestyle +
                '}';
    }

    public static class BasicBean {
        /**
         * cid : CN101010100
         * location : 北京
         * parent_city : 北京
         * admin_area : 北京
         * cnty : 中国
         * lat : 39.90498734
         * lon : 116.4052887
         * tz : +8.00
         */

        private String cid;
        @SerializedName("location")
        private String city;

        public String getCid() {
            return cid;
        }

        public void setCid(String cid) {
            this.cid = cid;
        }

        public String getCity() {
            return city;
        }

        public void setCity(String city) {
            this.city = city;
        }

        @Override
        public String toString() {
            return "BasicBean{" +
                    "cid='" + cid + '\'' +
                    ", city='" + city + '\'' +
                    '}';
        }
    }

    public static class UpdateBean {
        /**
         * loc : 2019-04-11 14:55
         * utc : 2019-04-11 06:55
         */

        @SerializedName("loc")
        private String update_time;

        public String getUpdate_time() {
            return update_time;
        }

        public void setUpdate_time(String update_time) {
            this.update_time = update_time;
        }

        @Override
        public String toString() {
            return "UpdateBean{" +
                    "update_time='" + update_time + '\'' +
                    '}';
        }
    }

    public static class NowBean {
        /**
         * cloud : 91
         * cond_code : 101
         * cond_txt : 多云
         * fl : 13
         * hum : 29
         * pcpn : 0.0
         * pres : 1014
         * tmp : 17
         * vis : 9
         * wind_deg : 273
         * wind_dir : 西风
         * wind_sc : 3
         * wind_spd : 19
         */

        private String cond_code;
        private String cond_txt;
        @SerializedName("fl")
        private String body_tmp;
        private String hum;
        @SerializedName("pcpn")
        private String rainy_amount;
        @SerializedName("pres")
        private String pressure;
        private String tmp;
        private String wind_dir;
        private String wind_sc;

        public String getCond_code() {
            return cond_code;
        }

        public void setCond_code(String cond_code) {
            this.cond_code = cond_code;
        }

        public String getCond_txt() {
            return cond_txt;
        }

        public void setCond_txt(String cond_txt) {
            this.cond_txt = cond_txt;
        }

        public String getBody_tmp() {
            return body_tmp;
        }

        public void setBody_tmp(String body_tmp) {
            this.body_tmp = body_tmp;
        }

        public String getHum() {
            return hum;
        }

        public void setHum(String hum) {
            this.hum = hum;
        }

        public String getRainy_amount() {
            return rainy_amount;
        }

        public void setRainy_amount(String rainy_amount) {
            this.rainy_amount = rainy_amount;
        }

        public String getPressure() {
            return pressure;
        }

        public void setPressure(String pressure) {
            this.pressure = pressure;
        }

        public String getTmp() {
            return tmp;
        }

        public void setTmp(String tmp) {
            this.tmp = tmp;
        }

        public String getWind_dir() {
            return wind_dir;
        }

        public void setWind_dir(String wind_dir) {
            this.wind_dir = wind_dir;
        }

        public String getWind_sc() {
            return wind_sc;
        }

        public void setWind_sc(String wind_sc) {
            this.wind_sc = wind_sc;
        }

        @Override
        public String toString() {
            return "NowBean{" +
                    "cond_code='" + cond_code + '\'' +
                    ", cond_txt='" + cond_txt + '\'' +
                    ", body_tmp='" + body_tmp + '\'' +
                    ", hum='" + hum + '\'' +
                    ", rainy_amount='" + rainy_amount + '\'' +
                    ", pressure='" + pressure + '\'' +
                    ", tmp='" + tmp + '\'' +
                    ", wind_dir='" + wind_dir + '\'' +
                    ", wind_sc='" + wind_sc + '\'' +
                    '}';
        }
    }

    public static class DailyForecastBean {
        /**
         * cond_code_d : 101
         * cond_code_n : 101
         * cond_txt_d : 多云
         * cond_txt_n : 多云
         * date : 2019-04-11
         * hum : 36
         * mr : 09:38
         * ms : 00:00
         * pcpn : 0.0
         * pop : 0
         * pres : 1009
         * sr : 05:41
         * ss : 18:49
         * tmp_max : 19
         * tmp_min : 6
         * uv_index : 4
         * vis : 25
         * wind_deg : 179
         * wind_dir : 南风
         * wind_sc : 1-2
         * wind_spd : 2
         */

        private String cond_code_d;
        private String cond_txt_d;
        private String date;
        private String tmp_max;
        private String tmp_min;

        public String getCond_code_d() {
            return cond_code_d;
        }

        public void setCond_code_d(String cond_code_d) {
            this.cond_code_d = cond_code_d;
        }

        public String getCond_txt_d() {
            return cond_txt_d;
        }

        public void setCond_txt_d(String cond_txt_d) {
            this.cond_txt_d = cond_txt_d;
        }

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public String getTmp_max() {
            return tmp_max;
        }

        public void setTmp_max(String tmp_max) {
            this.tmp_max = tmp_max;
        }

        public String getTmp_min() {
            return tmp_min;
        }

        public void setTmp_min(String tmp_min) {
            this.tmp_min = tmp_min;
        }

        @Override
        public String toString() {
            return "DailyForecastBean{" +
                    "cond_code_d='" + cond_code_d + '\'' +
                    ", cond_txt_d='" + cond_txt_d + '\'' +
                    ", date='" + date + '\'' +
                    ", tmp_max='" + tmp_max + '\'' +
                    ", tmp_min='" + tmp_min + '\'' +
                    '}';
        }
    }

    public static class LifestyleBean {
        /**
         * type : comf
         * brf : 舒适
         * txt : 白天不太热也不太冷，风力不大，相信您在这样的天气条件下，应会感到比较清爽和舒适。
         */

        private String type;
        @SerializedName("brf")
        private String main_idea;
        private String txt;

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getMain_idea() {
            return main_idea;
        }

        public void setMain_idea(String mian_idea) {
            this.main_idea = mian_idea;
        }

        public String getTxt() {
            return txt;
        }

        public void setTxt(String txt) {
            this.txt = txt;
        }

        @Override
        public String toString() {
            return "LifestyleBean{" +
                    "type='" + type + '\'' +
                    ", main_idea='" + main_idea + '\'' +
                    ", txt='" + txt + '\'' +
                    '}';
        }
    }
}

