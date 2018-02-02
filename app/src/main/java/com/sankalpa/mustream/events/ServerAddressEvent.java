package com.sankalpa.mustream.events;

public class ServerAddressEvent {
    String server;
    public ServerAddressEvent(String s){
        server = s;
    }

    public String getServer(){
        return server;
    }
}
