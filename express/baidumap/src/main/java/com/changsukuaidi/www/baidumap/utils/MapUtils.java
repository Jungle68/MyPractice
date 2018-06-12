package com.changsukuaidi.www.baidumap.utils;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.net.Uri;

import com.baidu.location.BDLocation;
import com.baidu.mapapi.CoordType;
import com.baidu.mapapi.map.ArcOptions;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.CircleOptions;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.PolygonOptions;
import com.baidu.mapapi.map.PolylineOptions;
import com.baidu.mapapi.map.Stroke;
import com.baidu.mapapi.map.TextOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.utils.CoordinateConverter;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 百度地图图层
 * 1、基础底图（包括底图、底图道路、卫星图、室内图等）；
 * 2、瓦片图层（TileOverlay）；
 * 3、地形图图层（GroundOverlay）；
 * 4、热力图图层（HeatMap）；
 * 5、实时路况图图层（BaiduMap.setTrafficEnabled(true);）；
 * 6、百度城市热力图（BaiduMap.setBaiduHeatMapEnabled(true);）；
 * 7、底图标注（指的是底图上面自带的那些POI元素）；
 * 8、几何图形图层（点、折线、弧线、圆、多边形）；
 * 9、标注图层（Marker），文字绘制图层（Text）；
 * 10、指南针图层（当地图发生旋转和视角变化时，默认出现在左上角的指南针）；
 * 11、定位图层（BaiduMap.setMyLocationEnabled(true);）；
 * 12、弹出窗图层（InfoWindow）；
 * 13、自定义View（MapView.addView(View);）；
 */
public class MapUtils {

    /**
     * 移动到指定的位置
     *
     * @param map      需要在哪个地图上移动
     * @param location 需要移动到的点
     */
    public static void moveToLocation(BaiduMap map, BDLocation location) {
        // 移动到定位地点
        MapStatus status = new MapStatus.Builder()
                .target(new LatLng(location.getLatitude(), location.getLongitude()))
                .zoom(15)
                .build();
        MapStatusUpdate update = MapStatusUpdateFactory.newMapStatus(status);
        map.animateMapStatus(update);
    }

    /**
     * 移动到指定的位置
     *
     * @param map      需要在哪个地图上移动
     * @param latLng 需要移动到的点
     */
    public static void moveToLocation(BaiduMap map, LatLng latLng) {
        // 移动到定位地点
        MapStatus status = new MapStatus.Builder()
                .target(latLng)
                .zoom(15)
                .build();
        MapStatusUpdate update = MapStatusUpdateFactory.newMapStatus(status);
        map.animateMapStatus(update);
    }

    /**
     * 添加若各个覆盖物
     *
     * @param map     需要添加覆盖物的地图
     * @param options 覆盖物
     */
    public static void addOverlays(BaiduMap map, OverlayOptions... options) {
        ArrayList<OverlayOptions> overlayOptions = new ArrayList<>();
        int length = options.length;
        overlayOptions.addAll(Arrays.asList(options).subList(0, length));
        map.addOverlays(overlayOptions);
    }

    /**
     * 创建一个Maker
     *
     * @param point      添加点
     * @param resourceId 需要添加的资源图
     * @return 需要绘制的覆盖物
     */
    public static OverlayOptions createMarker(LatLng point, int resourceId) {
        //构建Marker图标
        BitmapDescriptor bitmap = BitmapDescriptorFactory.fromResource(resourceId);
        //构建MarkerOption，用于在地图上添加Marker
        OverlayOptions option = new MarkerOptions()
                .perspective(true)// 近大远小的效果
                .position(point)  //设置Marker的位置
                .icon(bitmap)  //设置Marker图标
//                .anchor(0.5f, 1f) //锚点
                .zIndex(9)  //设置Marker所在层级
                .draggable(false);  //设置手势拖拽

        return option;
    }

    /**
     * 创建一个Maker
     *
     * @param point  添加点
     * @param bitmap 需要添加的图
     * @return 需要绘制的覆盖物
     */
    public static OverlayOptions createMarker(LatLng point, Bitmap bitmap) {
        //构建Marker图标
        BitmapDescriptor bitmapDescriptor = BitmapDescriptorFactory.fromBitmap(bitmap);
        //构建MarkerOption，用于在地图上添加Marker
        OverlayOptions option = new MarkerOptions()
                .perspective(true)// 近大远小的效果
                .position(point)  //设置Marker的位置
                .icon(bitmapDescriptor)  //设置Marker图标
//                .anchor(0.5f, 1f) //锚点
                .zIndex(9)  //设置Marker所在层级
                .draggable(false);  //设置手势拖拽

        return option;
    }

    /**
     * 创建折线
     *
     * @param color      折线的颜色
     * @param latLngs    折线的点
     * @param dottedLine 是否为虚线
     * @return 需要绘制的折线对象
     */
    public static OverlayOptions createPolylineOptions(int color, List<LatLng> latLngs, boolean dottedLine) {
        //绘制折线
        OverlayOptions ooPolyline = new PolylineOptions()
                .width(10)
                .zIndex(8)
                .color(color)
                .dottedLine(dottedLine)
                .points(latLngs);
        return ooPolyline;
    }


    /**
     * 创建折线,颜色分段
     *
     * @param colors     折线的颜色
     * @param latLngs    折线的点
     * @param dottedLine 是否为虚线
     * @return 需要绘制的折线对象
     */
    public static OverlayOptions createPolylineOptions(List<Integer> colors, List<LatLng> latLngs, boolean dottedLine) {
        //绘制折线
        OverlayOptions ooPolyline = new PolylineOptions()
                .width(10)
                .zIndex(8)
                .colorsValues(colors)
                .dottedLine(dottedLine)
                .points(latLngs);
        return ooPolyline;
    }

    /**
     * 创建纹理折线
     *
     * @param latLngs       折线的点
     * @param textureList   需要绘制的纹理图片
     * @param textureIndexs 纹理索引队列
     * @param dottedLine    是否为虚线
     * @return 需要绘制的折线对象
     */
    public static OverlayOptions createPolylineOptions(List<LatLng> latLngs, List<BitmapDescriptor> textureList, List<Integer> textureIndexs, boolean dottedLine) {
        //构造PolylineOptions对象，并绘制
        OverlayOptions ooPolyline = new PolylineOptions()
                .dottedLine(dottedLine)
                .width(16)
                .points(latLngs)
                .customTextureList(textureList)
                .textureIndex(textureIndexs);
        return ooPolyline;
    }

    /**
     * 创建弧线
     *
     * @param p1    弧线的起点
     * @param p2    弧线的中点
     * @param p3    弧线的终点
     * @param color 弧线的颜色和透明度,均使用16进制显示
     * @return 需要绘制的覆盖物
     */
    public static OverlayOptions createArcOptions(LatLng p1, LatLng p2, LatLng p3, int color) {
        OverlayOptions ooArc = new ArcOptions()
                .color(color)
                .width(4) // 线条宽度
                .points(p1, p2, p3) // 弧线上的三个点
                .zIndex(8) // 设置绘制的图层
                //.extraInfo(Bundle bundle) // 设置弧线的额外信息
                .visible(true); // 设置弧线是否可见, 默认可见
        return ooArc;
    }

    /**
     * 创建一个圆形
     *
     * @param latLng    圆心
     * @param lineColor 线的颜色
     * @param fillColor 中间的颜色
     * @return 需要绘制的覆盖物
     */
    public static OverlayOptions createCircleOptions(LatLng latLng, int lineColor, int fillColor, int radius) {
        OverlayOptions ooCircle = new CircleOptions()
                .fillColor(fillColor) // 设置颜色和透明度,均使用16进制显示,0xAARRGGBB,如0xAA00FF00,其中AA是透明度,00FF00为颜色
                .stroke(new Stroke(5, lineColor)) // 设置颜色和透明度,均使用16进制显示,0xAARRGGBB,如0xAA00FF00,其中AA是透明度,00FF00为颜色
                .center(latLng) // 中心点
                .zIndex(8) // 设置绘制的图层
                //.extraInfo(Bundle bundle) // 设置弧线的额外信息
                .visible(true) // 设置弧线是否可见, 默认可见
                .radius(radius); // 半径

        return ooCircle;
    }

    /**
     * 创建一个多边形
     *
     * @param lineColor 线的颜色
     * @param fillColor 中间的颜色
     * @param latLngs   多边形的角点
     * @return 需要绘制的覆盖物
     */
    public static OverlayOptions createPolygonOption(int lineColor, int fillColor, List<LatLng> latLngs) {
        //构建用户绘制多边形的Option对象
        OverlayOptions polygonOption = new PolygonOptions()
                .points(latLngs)
                .zIndex(8) // 设置绘制的图层
                //.extraInfo(Bundle bundle) // 设置弧线的额外信息
                .visible(true) // 设置弧线是否可见, 默认可见
                .stroke(new Stroke(5, lineColor))
                .fillColor(fillColor);

        return polygonOption;
    }

    /**
     * 绘制文字
     *
     * @param latLng 需要绘制的地点
     * @param text   需要绘制的文字
     * @return 需要绘制的覆盖物
     */
    public static OverlayOptions createTextOptions(LatLng latLng, String text) {
        //构建文字Option对象，用于在地图上添加文字
        OverlayOptions textOption = new TextOptions()
                .bgColor(0xAAFFFF00)
                .fontSize(24)
                .fontColor(0xFFFF00FF)
                .text(text)
                .rotate(-30)
                .position(latLng);

        return textOption;
    }

    public static boolean isBaiduMapInstalled() {
        return isInstallPackage("com.baidu.BaiduMap");
    }
    public static boolean isGaoDeMapInstalled() {
        return isInstallPackage("com.autonavi.minimap");
    }

    @SuppressLint("SdCardPath")
    private static boolean isInstallPackage(String packageName) {
        return new File(String.format("/data/data/%s", packageName)).exists();
    }

    /**
     * 百度地图路线规划，网页，不带途经点
     *
     * @param endLatitude  终点纬度
     * @param endLongitude 终点经度
     * @param endName      终点名字
     * @return 生成的uri
     */
    public static Uri getBaiduDirectionUri(double endLatitude, double endLongitude, String endName) {
        String path = "http://api.map.baidu.com/direction?destination=latlng:" + endLatitude + "," + endLongitude + "|name:" + endName + "&mode=driving&output=html&src=畅速快递";
        return Uri.parse(path);
    }

    /**
     * 百度地图路线规划，App版，带途经点
     *
     * @param pointLatitude  途经点纬度（起点）
     * @param pointLongitude 途经点经度（起点）
     * @param endLatitude    终点纬度
     * @param endLongitude   终点经度
     * @return 生成的Uri
     */
    public static Uri getBaiduDirectionAppUri(double pointLatitude, double pointLongitude, String pointName, double endLatitude, double endLongitude, String endName) {
        String path = "baidumap://map/direction?mode=driving&destination=latlng:" + endLatitude + "," + endLongitude + "|name:" + endName +
                "&src=push&viaPoints={'viaPoints':[{'name':'" + pointName + "','lat':" + pointLatitude + ",'lng':" + pointLongitude + "}]}";
        return Uri.parse(path);
    }

    /**
     * 百度地图路线规划，App版，不带途径点
     *
     * @param endLatitude  终点纬度
     * @param endLongitude 终点经度
     * @return 生成的Uri
     */
    public static Uri getBaiduDirectionAppUri(double endLatitude, double endLongitude, String endName) {
        String path = "baidumap://map/direction?mode=driving&destination=latlng:" + endLatitude + "," + endLongitude + "|name:" + endName + "&src=push";
        return Uri.parse(path);
    }

    /**
     * 高德路线规划，网页版，带途经点
     *
     * @param pointLatitude  途经点纬度（起点）
     * @param pointLongitude 途经点经度（起点）
     * @param endLatitude    终点纬度
     * @param endLongitude   终点经度
     * @return 生成的Uri
     */
    public static Uri getGaodeDirectionUri(double pointLatitude, double pointLongitude, String pointName, double endLatitude, double endLongitude, String endName) {
        CoordinateConverter converter = new CoordinateConverter();
        converter.from(CoordinateConverter.CoordType.BD09LL).coord(new LatLng(endLatitude, endLongitude));
        LatLng end = converter.convert();
        converter.coord(new LatLng(pointLatitude, pointLongitude));
        LatLng point = converter.convert();
        String path = "http://uri.amap.com/navigation?to=" + end.longitude + "," + end.latitude + "," + endName + "&via=" + point.longitude + "," + point.latitude + "," + pointName + "&mode=car&policy=1&src=畅速快递&coordinate=gaode&callnative=1";
        return Uri.parse(path);
    }

    /**
     * 高德路线规划，网页版，不带途经点
     *
     * @param endLatitude  终点纬度
     * @param endLongitude 终点经度
     * @return 生成的Uri
     */
    public static Uri getGaodeDirectionUri(double endLatitude, double endLongitude, String endName) {
        CoordinateConverter converter = new CoordinateConverter();
        converter.from(CoordinateConverter.CoordType.BD09LL).coord(new LatLng(endLatitude, endLongitude));
        LatLng latLng = converter.convert();
        String path = "http://uri.amap.com/navigation?to=" + latLng.longitude + "," + latLng.latitude + "," + endName + "&mode=car&policy=1&src=畅速快递&coordinate=gaode&callnative=1";
        return Uri.parse(path);
    }

    /**
     * 高德路线规划，App版，不带途经点
     *
     * @param endLatitude  终点纬度
     * @param endLongitude 终点经度
     * @return 生成的Uri
     */
    public static Uri getGaodeDirectionAppUri(double endLatitude, double endLongitude, String endName) {
        CoordinateConverter converter = new CoordinateConverter();
        converter.from(CoordinateConverter.CoordType.BD09LL).coord(new LatLng(endLatitude, endLongitude));
        LatLng latLng = converter.convert();
        String s = "amapuri://route/plan/?dlat=" + latLng.latitude + "&dlon=" + latLng.longitude + "&dname=" + endName + "&dev=0&t=0";
        return Uri.parse(s);
    }

    /**
     * 腾讯地图路线规划
     *
     * @param endLatitude  终点纬度
     * @param endLongitude 终点经度
     * @return 生成的Uri
     */
    public static Uri getTencentDirectionUri(double endLatitude, double endLongitude, String endName) {
        CoordinateConverter converter = new CoordinateConverter();
        converter.from(CoordinateConverter.CoordType.BD09LL).coord(new LatLng(endLatitude, endLongitude));
        LatLng latLng = converter.convert();
        String path = "http://apis.map.qq.com/uri/v1/routeplan?type=drive&to=" + endName + "&tocoord=" + latLng.latitude + "," + latLng.longitude + "&policy=1&referer=畅速快递";
        return Uri.parse(path);
    }
}
