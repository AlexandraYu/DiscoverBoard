package com.example.discoverboard;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class MainActivity extends Activity {

	private ArrayList<NetworkAPInfo> apList; 
	private List<ScanResult> wifiList; 
	private ListView listView; 
	private Context context;
//	private OnClickListener startDiscovery;
	ConnectivityManager connectManager;
	WifiManager wifiManager;
	
	public boolean onCreateOptionsMenu(Menu menu) { 
		MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.main, menu);
	    return super.onCreateOptionsMenu(menu);
    }
    
    /**
     * Called when a menu item is selected.
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
    	switch (item.getItemId()) {
    	case R.id.action_scan_network:
    		apList.clear(); 
    		startDiscoverDevice(); 
    		return true; 
    		
    	default:
            return super.onOptionsItemSelected(item);
    	}
    }
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		context = this;
		apList = new ArrayList<NetworkAPInfo>(); 
		listView = (ListView) findViewById(R.id.listview); 
		wifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
	}
	
	protected void onResume() {
		super.onResume();
		apList.clear(); 
		startDiscoverDevice(); 
		listView.setAdapter(new DeviceListAdapter(context, apList));
		clickItem(listView); 
	}

	protected void onPause() {
		super.onPause(); 
	}
	
	/*
	public void startScanConfig() {
		String networkSSID = "KCodes-04ec24";

		WifiConfiguration conf = new WifiConfiguration();
		conf.SSID = "\"" + networkSSID + "\""; 
		conf.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
		wifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE); 
		wifiManager.addNetwork(conf);
		List<WifiConfiguration> list = wifiManager.getConfiguredNetworks();
		for( WifiConfiguration i : list ) {
		    if(i.SSID != null && i.SSID.equals("\"" + networkSSID + "\"")) {
		         wifiManager.disconnect();
		         wifiManager.enableNetwork(i.networkId, true);
		         wifiManager.reconnect();               
		         break;
		    }           
		 }
	}
*/
	public void startDiscoverDevice() {		
		// Inside BroadcastReceiver()
		wifiList = wifiManager.getScanResults();
		for (int i=0; i<wifiList.size(); i++){
		     ScanResult scanresult = wifiManager.getScanResults().get(i);
/*		     Log.d("Alex", "SSID: "+scanresult.SSID);
		     Log.d("Alex", "RSSI: "+scanresult.level);
		     Log.d("Alex", "Frequency: "+scanresult.frequency);
		     Log.d("Alex", "BSSID: "+scanresult.BSSID);
		     Log.d("Alex", "Capability: "+scanresult.capabilities); */
		     apList.add(new NetworkAPInfo(scanresult.SSID, scanresult.BSSID));
		}
	}
	
	class DeviceListAdapter extends BaseAdapter {
		private ArrayList<NetworkAPInfo> myAPList = null;
//		private LayoutInflater inflater; 
		private Context context;
		public DeviceListAdapter(Context ctx, ArrayList<NetworkAPInfo> inputList) {
			myAPList=inputList;
			context=ctx;
		}
		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return myAPList.size();
		}
		
		@Override
		public NetworkAPInfo getItem(int position) {
			// TODO Auto-generated method stub
			return myAPList.get(position);
		}
		
		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			//map to ap info file layout
			
			ViewHolderPic viewHolderPic;
			if(convertView==null){
				// inflate the layout
				LayoutInflater inflater = ((Activity) context).getLayoutInflater();
				convertView = inflater.inflate(R.layout.device_info, parent, false);
				
				//set up the ViewHolder
				viewHolderPic = new ViewHolderPic();
				viewHolderPic.deviceName = (TextView) convertView.findViewById(R.id.device_name);
				viewHolderPic.deviceMac = (TextView) convertView.findViewById(R.id.device_mac);
				// store the holder with the view.
				convertView.setTag(viewHolderPic);
			}
			else{
				viewHolderPic = (ViewHolderPic) convertView.getTag();
			}
			//name and mac based on the position
			NetworkAPInfo apInfo = getItem(position);
			// assign values if the object is not null
			if(apInfo != null) {
				// get the TextView from the ViewHolder and then set the text (item name) and tag (item ID) values
				viewHolderPic.deviceName.setText(apInfo.getName());
				viewHolderPic.deviceMac.setText(apInfo.getMac());
			}
			return convertView;
		}
	}
	
	static class ViewHolderPic {
		TextView deviceName;
		TextView deviceMac;
	}
	
	public void clickItem(ListView currentView) {
		currentView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				// TODO Auto-generated method stub
				String name=apList.get(position).getName();
				String mac=apList.get(position).getMac(); 
				Log.d("Alex", "name is: "+name); 
				connectDevice(name, mac); 
				
			}
		});
	}
	
	public void connectDevice(String name, String mac) {
		String networkSSID = name;

		WifiConfiguration conf = new WifiConfiguration();
		conf.SSID = "\"" + networkSSID + "\""; 
		conf.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
		wifiManager.addNetwork(conf);
		List<WifiConfiguration> list = wifiManager.getConfiguredNetworks();
		for( WifiConfiguration i : list ) {
		    if(i.SSID != null && i.SSID.equals("\"" + networkSSID + "\"")) {
		         wifiManager.disconnect();
		         wifiManager.enableNetwork(i.networkId, true);
		         wifiManager.reconnect();               
		         break;
		    }           
		 }
	}
}
