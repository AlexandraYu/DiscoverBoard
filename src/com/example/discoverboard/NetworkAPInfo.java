package com.example.discoverboard;


public class NetworkAPInfo {
	private String name;
	private String mac;
	
	public NetworkAPInfo(String ssid, String bssid) {
		name=ssid;
		mac=bssid;
	}
	public String getName() {
		return name;
	}
	
	public String getMac() {
		return mac;
	}

}
