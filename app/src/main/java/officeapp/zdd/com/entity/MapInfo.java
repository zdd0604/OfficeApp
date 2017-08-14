package officeapp.zdd.com.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Admin on 2017/2/21.
 */

public class MapInfo implements Serializable {

    private static final long serialVersionUID = 1240634929532755277L;
    private int mapID;
    private String mapMemoryTiltle;
    private String mapAddress;
    private double mapLongitude;
    private double mapLatitude;
    private String mapTime;
    private String mapMemoryContent;
    private String maPhoeos;

    public MapInfo() {
    }

    public MapInfo(int mapID, String mapMemoryTiltle, String mapAddress, double mapLongitude, double mapLatitude, String mapTime, String mapMemoryContent, String maPhoeos) {
        this.mapID = mapID;
        this.mapMemoryTiltle = mapMemoryTiltle;
        this.mapAddress = mapAddress;
        this.mapLongitude = mapLongitude;
        this.mapLatitude = mapLatitude;
        this.mapTime = mapTime;
        this.mapMemoryContent = mapMemoryContent;
        this.maPhoeos = maPhoeos;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public int getMapID() {
        return mapID;
    }

    public void setMapID(int mapID) {
        this.mapID = mapID;
    }

    public String getMapMemoryTiltle() {
        return mapMemoryTiltle;
    }

    public void setMapMemoryTiltle(String mapMemoryTiltle) {
        this.mapMemoryTiltle = mapMemoryTiltle;
    }

    public String getMapAddress() {
        return mapAddress;
    }

    public void setMapAddress(String mapAddress) {
        this.mapAddress = mapAddress;
    }

    public double getMapLongitude() {
        return mapLongitude;
    }

    public void setMapLongitude(double mapLongitude) {
        this.mapLongitude = mapLongitude;
    }

    public double getMapLatitude() {
        return mapLatitude;
    }

    public void setMapLatitude(double mapLatitude) {
        this.mapLatitude = mapLatitude;
    }

    public String getMapTime() {
        return mapTime;
    }

    public void setMapTime(String mapTime) {
        this.mapTime = mapTime;
    }

    public String getMapMemoryContent() {
        return mapMemoryContent;
    }

    public void setMapMemoryContent(String mapMemoryContent) {
        this.mapMemoryContent = mapMemoryContent;
    }

    public String getMaPhoeos() {
        return maPhoeos;
    }

    public void setMaPhoeos(String maPhoeos) {
        this.maPhoeos = maPhoeos;
    }

    @Override
    public String toString() {
        return "MapInfo{" +
                "mapID=" + mapID +
                ", mapMemoryTiltle='" + mapMemoryTiltle + '\'' +
                ", mapAddress='" + mapAddress + '\'' +
                ", mapLongitude=" + mapLongitude +
                ", mapLatitude=" + mapLatitude +
                ", mapTime='" + mapTime + '\'' +
                ", mapMemoryContent='" + mapMemoryContent + '\'' +
                ", maPhoeos='" + maPhoeos + '\'' +
                '}';
    }
}