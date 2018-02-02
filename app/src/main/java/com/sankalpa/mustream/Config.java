package com.sankalpa.mustream;

public class Config {
    public static final int DISCOVERY_PORT_ADDRESS = 9863;
    public static final int STREAM_PORT_ADDRESS = 9864;
    public static final int WEBSOCKET_PORT = 9865;
    private String ipAddress;
    private static Config instance = null;
    int latency;

    private Mode mode;

    private Config(){
    }

    public static Config getInstance(){
        if(instance == null){
            instance = new Config();
        }
        return instance;
    }

    public String getIpAddress(){
        return ipAddress;
    }

    public Mode getMode() {
        return mode;
    }

    public void setLatency(int latency){
        this.latency = latency;
    }
    public int getLatency(){
        return latency;
    }

    public void setMode(Mode mode) {
        this.mode = mode;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }
}
