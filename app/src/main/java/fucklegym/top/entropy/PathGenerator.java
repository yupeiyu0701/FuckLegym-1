package fucklegym.top.entropy;

import android.content.SharedPreferences;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class PathGenerator {
    //设置跑步范围经纬度坐标
    private static double[] latitude;
    private static double[] longitude;
    private static double base_lat, base_lon;

    public static HashMap<String, HashMap<String, double[]>> RunMaps = new HashMap<String, HashMap<String, double[]>>(){
        {
            put("电子科技大学（清水河校区）", new HashMap<String, double[]>(){{
                put("latitude", new double[]{104.1888,104.188747,104.188666,104.188677,104.188859,104.188854,104.188682,104.187803,104.187492,104.186778,104.185816,});
                put("longitude", new double[]{30.830532,30.830338,30.830062,30.830103,30.830661,30.830785,30.830997,30.831204,30.829914,30.830053,30.830053,30.82849,30.82849});
                put("base", new double[]{104.188827,30.830614});
            }});
            put("电子科技大学（沙河校区）", new HashMap<String, double[]>(){{
                put("latitude", new double[]{30.676965, 30.676988, 30.67703, 30.677035, 30.677016, 30.676998, 30.676924, 30.67685, 30.676725, 30.676532, 30.676315, 30.676103, 30.67607, 30.676006, 30.675987, 30.675858, 30.675738, 30.675715, 30.675687, 30.675683, 30.675641, 30.675637, 30.675604, 30.675567, 30.675567, 30.675586, 30.675609, 30.675646, 30.67578, 30.675807, 30.675918, 30.675964, 30.676038, 30.676144, 30.676259, 30.676296, 30.676379, 30.676536, 30.676721, 30.676785, 30.676832, 30.676887});
                put("longitude", new double[]{104.097982, 104.098036, 104.098132, 104.098256, 104.098385, 104.098454, 104.098519, 104.098626, 104.098669, 104.098658, 104.098658, 104.098631, 104.098637, 104.098637, 104.098647, 104.098621, 104.098572, 104.098551, 104.098497, 104.098486, 104.09846, 104.098406, 104.098374, 104.098293, 104.098272, 104.098149, 104.098057, 104.097993, 104.097859, 104.097832, 104.097805, 104.097821, 104.097821, 104.097827, 104.097827, 104.097864, 104.097853, 104.097848, 104.097853, 104.097864, 104.097886, 104.097891});
                put("base", new double[]{30.676965, 104.097982});
            }});
//"xx大学（xx校区）": {
//  "latitude": [],
//  "longitude": []
//}
        }
    };

    public static void getLocalMaps(SharedPreferences local_maps){
        String[] maps = new String[]{};
        Map<String, ?> all = local_maps.getAll();
        for(String str: all.keySet()){
            double[] attr = new double[]{};
            JSONArray latitude = JSON.parseObject((String) all.get(str)).getJSONArray("latitude");
            JSONArray longitude = JSON.parseObject((String) all.get(str)).getJSONArray("longitude");
            double[] latitude_double = latitude.toJavaObject(double[].class);
            double[] longitude_double = longitude.toJavaObject(double[].class);
            double[] base = new double[]{latitude_double[0], longitude_double[0]};
            HashMap<String, double[]> theMap = new HashMap<>();
            theMap.put("latitude", latitude_double);
            theMap.put("longitude", longitude_double);
            theMap.put("base", base);
            RunMaps.put(str, theMap);
        }
    }


    private static void setAttr(String map){
        HashMap<String, double[]> currentMap = RunMaps.get(map);
        latitude = currentMap.get("latitude");
        longitude = currentMap.get("longitude");
        base_lat = currentMap.get("base")[0];
        base_lon = currentMap.get("base")[1];
    }

//    public static ArrayList<Pair<Double,Double>> genPointsInUESTC(int count){
//        ArrayList<Pair<Double,Double>> points = new ArrayList<>();
//        Random rad = new Random(System.currentTimeMillis());
//        for(int i = 1;i<=count;i++){
//            points.add(new Pair(base_lat + rad.nextInt(10000) / 1000000.0/2.0,base_lon + rad.nextInt(10000) / 1000000.0/2.0));
//        }
//        return points;
//    }
    public static ArrayList<Pair<Double,Double>> genRegularRoutine(String map, double totalMile){
        int cycleMeter = 400;//操场一圈的长度
        int totalMeter = (int)(totalMile * 1000);
        int offset = 6;//经纬度随机偏移量
        setAttr(map);
        ArrayList<Pair<Double,Double>> points = new ArrayList<>();
        Random rad = new Random(System.currentTimeMillis());
        for(int j = 0;j <= totalMeter/cycleMeter;j ++){
            if(totalMeter/cycleMeter - j - 1 >= 0) {
                for (int i = 0; i < latitude.length; i++) {
                    points.add(new Pair(latitude[i] + rad.nextInt(offset) * 1e-5, longitude[i] + rad.nextInt(offset) * 1e-5));
                }
            }else {
                int lastMeter = totalMeter - j * cycleMeter;
                double rate = ((double) lastMeter)/((double)cycleMeter);
                Log.d("run_rate", "genRegularRoutine: " + rate);
                for (int i = 0; i < latitude.length*rate; i++) {
                    points.add(new Pair(latitude[i] + rad.nextInt(offset) * 1e-5, longitude[i] + rad.nextInt(offset) * 1e-5));
                }
            }
        }

        return points;
    }

}
