package com.axaet.sdk;


import java.text.DecimalFormat;
import java.util.ArrayList;

import java.util.Timer;
import java.util.TimerTask;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;

import android.content.Context;
import android.content.Intent;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import android.widget.RelativeLayout;
import android.widget.TextView;


public class Ibeacon extends Activity {

	String ret_show;

	private BluetoothAdapter mBluetoothAdapter;
	private Handler mHandler;
	private static final long SCAN_PERIOD = 10000;
	LinearLayout lin_device;
	Button so;
	
	private ArrayList<Ibeacon_device> ibeacon;
	DecimalFormat df = new DecimalFormat("0.00");

	Timer timer = new Timer();
	TimerTask sendPasswordTask = new TimerTask() {
		public void run() {
			scanLeDevice(true);
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setview();

		if (!mBluetoothAdapter.isEnabled()) {
			Intent enableBtIntent = new Intent(
					BluetoothAdapter.ACTION_REQUEST_ENABLE);
			startActivityForResult(enableBtIntent, 1);
		} else {
			timer.schedule(sendPasswordTask, 500, 1000);
		}
	}

	@Override
	protected void onDestroy() {
		
		super.onDestroy();
		timer.cancel();
		timer.purge();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == 1) {
			if (resultCode == RESULT_OK) {
				timer.schedule(sendPasswordTask, 500, 1000);
			} else if (resultCode == RESULT_CANCELED) {

				finish();
			}
		}

	}

	@SuppressLint("NewApi")
	private void scanLeDevice(final boolean enable) {
		if (enable) {
			// Stops scanning after a pre-defined scan period.
			mHandler.postDelayed(new Runnable() {
				@SuppressLint("NewApi")
				@Override
				public void run() {
					mBluetoothAdapter.stopLeScan(mLeScanCallback);
					invalidateOptionsMenu();
				}
			}, SCAN_PERIOD);

			mBluetoothAdapter.startLeScan(mLeScanCallback);
		} else {
			mBluetoothAdapter.stopLeScan(mLeScanCallback);
		}
		invalidateOptionsMenu();

	}

	// Device scan callback.
	@SuppressLint("NewApi")
	private BluetoothAdapter.LeScanCallback mLeScanCallback = new BluetoothAdapter.LeScanCallback() {

		@Override
		public void onLeScan(final BluetoothDevice device, final int rssi,
				final byte[] scanRecord) {
			runOnUiThread(new Runnable() {
				public void run() {
					String a = device.getName();
					Log.e("name", a + ":" + device.getAddress());

					if ((a != null && a.length() > 0)) {

						Boolean isdevic = false;
						for (int i = 0; i < ibeacon.size(); i++) {
							if (ibeacon.get(i).device.getAddress().equals(
									device.getAddress())) {
								ibeacon.get(i).setname(device.getName());
								ibeacon.get(i).setrssi(
										df.format(calculateAccuracy(
												scanRecord[29], rssi)), rssi);
								isdevic = true;
							}
						}
						if (!isdevic) {
							String uuid = "";
							for (int i = 9; i < 25; i++) {
								String ret = Integer
										.toHexString(scanRecord[i] & 0xff);
								if (ret.length() == 1) {
									ret = "0" + ret;
								}

								// uuid=uuid+Integer.toHexString(scanRecord[i]&0xff);
								uuid = uuid + ret;
							}

							String ret1 = Integer
									.toHexString(scanRecord[25] & 0xff);
							String ret2 = Integer
									.toHexString(scanRecord[26] & 0xff);
							String ret3 = Integer
									.toHexString(scanRecord[27] & 0xff);
							String ret4 = Integer
									.toHexString(scanRecord[28] & 0xff);

							String ret5 = Integer
									.toHexString(scanRecord[29] & 0xff);
							String ret6 = Integer
									.toHexString(scanRecord[30] & 0xff);

							ret_show = Integer
									.toHexString(scanRecord[3] & 0xff);
							Log.e("ret_show", ret_show);

							
							if (ret1.length() == 1) {
								ret1 = "0" + ret1;
							}
							if (ret2.length() == 1) {
								ret2 = "0" + ret2;
							}
							if (ret3.length() == 1) {
								ret3 = "0" + ret3;
							}
							if (ret4.length() == 1) {
								ret4 = "0" + ret4;
							}

							if (ret5.length() == 1) {
								ret5 = "0" + ret5;
							}

							if (ret6.length() == 1) {
								ret6 = "0" + ret6;
							}

							Log.e("1616161", ret1 + ":" + ret2 + ":" + ret3
									+ ":" + ret4 + ":" + ret5 + ":" + ret6);
							String a1 = ret1.substring(0, 1);
							String a2 = ret1.substring(1, 2);
							String a3 = ret2.substring(0, 1);
							String a4 = ret2.substring(1, 2);
							int a11 = 0;
							int a22 = 0;
							int a33 = 0;
							int a44 = 0;

							

							if (a1.equals("0")) {
								a11 = 0;
							}
							if (a1.equals("1")) {
								a11 = 1;
							}
							if (a1.equals("2")) {
								a11 = 2;
							}
							if (a1.equals("3")) {
								a11 = 3;
							}
							if (a1.equals("4")) {
								a11 = 4;
							}
							if (a1.equals("5")) {
								a11 = 5;
							}

							if (a1.equals("6")) {
								a11 = 6;
							}
							if (a1.equals("7")) {
								a11 = 7;
							}
							if (a1.equals("8")) {
								a11 = 8;
							}
							if (a1.equals("9")) {
								a11 = 9;
							}

							if (a1.equals("A") || a1.equals("a")) {
								a11 = 10;
							}
							if (a1.equals("B") || a1.equals("b")) {
								a11 = 11;
							}
							if (a1.equals("C") || a1.equals("c")) {
								a11 = 12;
							}
							if (a1.equals("D") || a1.equals("d")) {
								a11 = 13;
							}
							if (a1.equals("E") || a1.equals("e")) {
								a11 = 14;
							}
							if (a1.equals("F") || a1.equals("f")) {
								a11 = 15;
							}

							if (a2.equals("0")) {
								a22 = 0;
							}
							if (a2.equals("1")) {
								a22 = 1;
							}
							if (a2.equals("2")) {
								a22 = 2;
							}
							if (a2.equals("3")) {
								a22 = 3;
							}
							if (a2.equals("4")) {
								a22 = 4;
							}
							if (a2.equals("5")) {
								a22 = 5;
							}

							if (a2.equals("6")) {
								a22 = 6;
							}
							if (a2.equals("7")) {
								a22 = 7;
							}
							if (a2.equals("8")) {
								a22 = 8;
							}
							if (a2.equals("9")) {
								a22 = 9;
							}

							if (a2.equals("A") || a2.equals("a")) {
								a22 = 10;
							}
							if (a2.equals("B") || a2.equals("b")) {
								a22 = 11;
							}
							if (a2.equals("C") || a2.equals("c")) {
								a22 = 12;
							}
							if (a2.equals("D") || a2.equals("d")) {
								a22 = 13;
							}
							if (a2.equals("E") || a2.equals("e")) {
								a22 = 14;
							}
							if (a2.equals("F") || a2.equals("f")) {
								a22 = 15;
							}

							if (a3.equals("0")) {
								a33 = 0;
							}
							if (a3.equals("1")) {
								a33 = 1;
							}
							if (a3.equals("2")) {
								a33 = 2;
							}
							if (a3.equals("3")) {
								a33 = 3;
							}
							if (a3.equals("4")) {
								a33 = 4;
							}
							if (a3.equals("5")) {
								a33 = 5;
							}

							if (a3.equals("6")) {
								a33 = 6;
							}
							if (a3.equals("7")) {
								a33 = 7;
							}
							if (a3.equals("8")) {
								a33 = 8;
							}
							if (a3.equals("9")) {
								a33 = 9;
							}

							if (a3.equals("A") || a3.equals("a")) {
								a33 = 10;
							}
							if (a3.equals("B") || a3.equals("b")) {
								a33 = 11;
							}
							if (a3.equals("C") || a3.equals("c")) {
								a33 = 12;
							}
							if (a3.equals("D") || a3.equals("d")) {
								a33 = 13;
							}
							if (a3.equals("E") || a3.equals("e")) {
								a33 = 14;
							}
							if (a3.equals("F") || a3.equals("f")) {
								a33 = 15;
							}

							if (a4.equals("0")) {
								a44 = 0;
							}
							if (a4.equals("1")) {
								a44 = 1;
							}
							if (a4.equals("2")) {
								a44 = 2;
							}
							if (a4.equals("3")) {
								a44 = 3;
							}
							if (a4.equals("4")) {
								a44 = 4;
							}
							if (a4.equals("5")) {
								a44 = 5;
							}

							if (a4.equals("6")) {
								a44 = 6;
							}
							if (a4.equals("7")) {
								a44 = 7;
							}
							if (a4.equals("8")) {
								a44 = 8;
							}
							if (a4.equals("9")) {
								a44 = 9;
							}

							if (a4.equals("A") || a4.equals("a")) {
								a44 = 10;
							}
							if (a4.equals("B") || a4.equals("b")) {
								a44 = 11;
							}
							if (a4.equals("C") || a4.equals("c")) {
								a44 = 12;
							}
							if (a4.equals("D") || a4.equals("d")) {
								a44 = 13;
							}
							if (a4.equals("E") || a4.equals("e")) {
								a44 = 14;
							}
							if (a4.equals("F") || a4.equals("f")) {
								a44 = 15;
							}
							int max = a11 * 4096 + a22 * 256 + a33 * 16 + a44;

							Log.e("a11", a11 + ":" + a22 + ":" + a33 + ":"
									+ a44);

							//
							//
							String b1 = ret3.substring(0, 1);
							String b2 = ret3.substring(1, 2);
							String b3 = ret4.substring(0, 1);
							String b4 = ret4.substring(1, 2);

							int b11 = 0;
							int b22 = 0;
							int b33 = 0;
							int b44 = 0;

							if (b1.equals("0")) {
								b11 = 0;
							}
							if (b1.equals("1")) {
								b11 = 1;
							}
							if (b1.equals("2")) {
								b11 = 2;
							}
							if (b1.equals("3")) {
								b11 = 3;
							}
							if (b1.equals("4")) {
								b11 = 4;
							}
							if (b1.equals("5")) {
								b11 = 5;
							}

							if (b1.equals("6")) {
								b11 = 6;
							}
							if (b1.equals("7")) {
								b11 = 7;
							}
							if (b1.equals("8")) {
								b11 = 8;
							}
							if (b1.equals("9")) {
								b11 = 9;
							}

							if (b1.equals("A") || b1.equals("a")) {
								b11 = 10;
							}
							if (b1.equals("B") || b1.equals("b")) {
								b11 = 11;
							}
							if (b1.equals("C") || b1.equals("c")) {
								b11 = 12;
							}
							if (b1.equals("D") || b1.equals("d")) {
								b11 = 13;
							}
							if (b1.equals("E") || b1.equals("e")) {
								b11 = 14;
							}
							if (b1.equals("F") || b1.equals("f")) {
								b11 = 15;
							}

							if (b2.equals("0")) {
								b22 = 0;
							}
							if (b2.equals("1")) {
								b22 = 1;
							}
							if (b2.equals("2")) {
								b22 = 2;
							}
							if (b2.equals("3")) {
								b22 = 3;
							}
							if (b2.equals("4")) {
								b22 = 4;
							}
							if (b2.equals("5")) {
								b22 = 5;
							}

							if (b2.equals("6")) {
								b22 = 6;
							}
							if (b2.equals("7")) {
								b22 = 7;
							}
							if (b2.equals("8")) {
								b22 = 8;
							}
							if (b2.equals("9")) {
								b22 = 9;
							}

							if (b2.equals("A") || b2.equals("a")) {
								b22 = 10;
							}
							if (b2.equals("B") || b2.equals("b")) {
								b22 = 11;
							}
							if (b2.equals("C") || b2.equals("c")) {
								b22 = 12;
							}
							if (b2.equals("D") || b2.equals("d")) {
								b22 = 13;
							}
							if (b2.equals("E") || b2.equals("e")) {
								b22 = 14;
							}
							if (b2.equals("F") || b2.equals("f")) {
								b22 = 15;
							}

							if (b3.equals("0")) {
								b33 = 0;
							}
							if (b3.equals("1")) {
								b33 = 1;
							}
							if (b3.equals("2")) {
								b33 = 2;
							}
							if (b3.equals("3")) {
								b33 = 3;
							}
							if (b3.equals("4")) {
								b33 = 4;
							}
							if (b3.equals("5")) {
								b33 = 5;
							}

							if (b3.equals("6")) {
								b33 = 6;
							}
							if (b3.equals("7")) {
								b33 = 7;
							}
							if (b3.equals("8")) {
								b33 = 8;
							}
							if (b3.equals("9")) {
								b33 = 9;
							}

							if (b3.equals("A") || b3.equals("a")) {
								b33 = 10;
							}
							if (b3.equals("B") || b3.equals("b")) {
								b33 = 11;
							}
							if (b3.equals("C") || b3.equals("c")) {
								b33 = 12;
							}
							if (b3.equals("D") || b3.equals("d")) {
								b33 = 13;
							}
							if (b3.equals("E") || b3.equals("e")) {
								b33 = 14;
							}
							if (b3.equals("F") || b3.equals("f")) {
								b33 = 15;
							}

							if (b4.equals("0")) {
								b44 = 0;
							}
							if (b4.equals("1")) {
								b44 = 1;
							}
							if (b4.equals("2")) {
								b44 = 2;
							}
							if (b4.equals("3")) {
								b44 = 3;
							}
							if (b4.equals("4")) {
								b44 = 4;
							}
							if (b4.equals("5")) {
								b44 = 5;
							}

							if (b4.equals("6")) {
								b44 = 6;
							}
							if (b4.equals("7")) {
								b44 = 7;
							}
							if (b4.equals("8")) {
								b44 = 8;
							}
							if (b4.equals("9")) {
								b44 = 9;
							}

							if (b4.equals("A") || b4.equals("a")) {
								b44 = 10;
							}
							if (b4.equals("B") || b4.equals("b")) {
								b44 = 11;
							}
							if (b4.equals("C") || b4.equals("c")) {
								b44 = 12;
							}
							if (b4.equals("D") || b4.equals("d")) {
								b44 = 13;
							}
							if (b4.equals("E") || b4.equals("e")) {
								b44 = 14;
							}
							if (b4.equals("F") || b4.equals("f")) {
								b44 = 15;
							}

							Log.e("b11", b11 + ":" + b22 + ":" + b33 + ":"
									+ b44);
							//
							int min = b11 * 4096 + b22 * 256 + b33 * 16 + b44;
							//
							Log.e("aaaa", a1 + ":" + a2 + ":" + a3 + ":" + a4);
							Log.e("bbbb", b1 + ":" + b2 + ":" + b3 + ":" + b4);

							String c1 = ret6.substring(0, 1);
							String c2 = ret6.substring(1, 2);

							int c11 = 0;
							int c22 = 0;

							if (c1.equals("0")) {
								c11 = 0;
							}
							if (c1.equals("1")) {
								c11 = 1;
							}
							if (c1.equals("2")) {
								c11 = 2;
							}
							if (c1.equals("3")) {
								c11 = 3;
							}
							if (c1.equals("4")) {
								c11 = 4;
							}
							if (c1.equals("5")) {
								c11 = 5;
							}

							if (c1.equals("6")) {
								c11 = 6;
							}
							if (c1.equals("7")) {
								c11 = 7;
							}
							if (c1.equals("8")) {
								c11 = 8;
							}
							if (c1.equals("9")) {
								c11 = 9;
							}

							if (c1.equals("A") || c1.equals("a")) {
								c11 = 10;
							}
							if (c1.equals("B") || c1.equals("b")) {
								c11 = 11;
							}
							if (c1.equals("C") || c1.equals("c")) {
								c11 = 12;
							}
							if (c1.equals("D") || c1.equals("d")) {
								c11 = 13;
							}
							if (c1.equals("E") || c1.equals("e")) {
								c11 = 14;
							}
							if (c1.equals("F") || c1.equals("f")) {
								c11 = 15;
							}

							if (c2.equals("0")) {
								c22 = 0;
							}
							if (c2.equals("1")) {
								c22 = 1;
							}
							if (c2.equals("2")) {
								c22 = 2;
							}
							if (c2.equals("3")) {
								c22 = 3;
							}
							if (c2.equals("4")) {
								c22 = 4;
							}
							if (c2.equals("5")) {
								c22 = 5;
							}

							if (c2.equals("6")) {
								c22 = 6;
							}
							if (c2.equals("7")) {
								c22 = 7;
							}
							if (c2.equals("8")) {
								c22 = 8;
							}
							if (c2.equals("9")) {
								c22 = 9;
							}

							if (c2.equals("A") || c2.equals("a")) {
								c22 = 10;
							}
							if (c2.equals("B") || c2.equals("b")) {
								c22 = 11;
							}
							if (c2.equals("C") || c2.equals("c")) {
								c22 = 12;
							}
							if (c2.equals("D") || c2.equals("d")) {
								c22 = 13;
							}
							if (c2.equals("E") || c2.equals("e")) {
								c22 = 14;
							}
							if (c2.equals("F") || c2.equals("f")) {
								c22 = 15;
							}

							Log.e("cccc", c1 + ":" + c2);

							// }

							

							Ibeacon_device devic = new Ibeacon_device(device,
									uuid, df.format(calculateAccuracy(
											scanRecord[29], rssi)) + "", max
											+ "", min + "", rssi);
							Log.e("power", scanRecord[29] + "");

							ibeacon.add(devic);

						}
					}
				}
			});
		}
	};

	protected static double calculateAccuracy(int txPower, double rssi) {
		if (rssi == 0) {
			return -1.0; // if we cannot determine accuracy, return -1.
		}

		double ratio = rssi * 1.0 / txPower;
		if (ratio < 1.0) {
			return Math.pow(ratio, 10);
		} else {
			double accuracy = (0.89976) * Math.pow(ratio, 7.7095) + 0.111;
			return accuracy;
		}
	}

	@SuppressLint("NewApi")
	public void setview() {
		setContentView(R.layout.ibeacon);
		lin_device = (LinearLayout) findViewById(R.id.lin_device);
		so = (Button) findViewById(R.id.bt_so);
		so.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				ibeacon.clear();
				lin_device.removeAllViews();
			}
		});

		mHandler = new Handler();
		ibeacon = new ArrayList<Ibeacon_device>();

		if (!getPackageManager().hasSystemFeature(
				PackageManager.FEATURE_BLUETOOTH_LE)) {
			finish();
		}
		// Initializes a Bluetooth adapter. For API level 18 and above, get a
		// reference to
		// BluetoothAdapter through BluetoothManager.
		final BluetoothManager bluetoothManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
		mBluetoothAdapter = bluetoothManager.getAdapter();

		// Checks if Bluetooth is supported on the device.
		if (mBluetoothAdapter == null) {
			finish();
			return;
		}

	}

	public class Ibeacon_device {
		RelativeLayout rel;
		TextView text_name;
		TextView text_address;
		TextView text_jl;
		TextView text_MajorId;
		TextView text_MinorId;
		TextView text_rssi;

		int rssi;

		public int getRssi() {
			return rssi;
		}

		public void setRssi(int rssi) {
			this.rssi = rssi;
		}

		BluetoothDevice device;

		// String name;
		Ibeacon_device(final BluetoothDevice device, String uuid, String jl,
				String MajorId, String MinorId, int rssi) {
			this.device = device;
			this.rssi = rssi;
			rel = (RelativeLayout) LayoutInflater.from(Ibeacon.this).inflate(
					R.layout.listitem_device, null);
			text_name = (TextView) rel.findViewById(R.id.device_name);
			text_address = (TextView) rel.findViewById(R.id.device_uuid);
			text_jl = (TextView) rel.findViewById(R.id.text_jl);
			text_MajorId = (TextView) rel.findViewById(R.id.text_MajorId);
			text_MinorId = (TextView) rel.findViewById(R.id.text_MinorId);
			text_rssi = (TextView) rel.findViewById(R.id.text_rssi);

			rel.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					String name = device.getName().toString();
					Log.e("device_name", name);

					Intent intent = new Intent(Ibeacon.this, Test.class);

					intent.putExtra(Test.EXTRAS_DEVICE_NAME, text_name
							.getText().toString());
					intent.putExtra(Test.EXTRAS_DEVICE_ADDRESS,
							device.getAddress());
					startActivity(intent);
					finish();

				}

			});

			lin_device.addView(rel);

			text_name.setText(device.getName());
			text_address.setText(uuid);
			text_jl.setText(jl);
			text_MajorId.setText(MajorId);
			text_MinorId.setText(MinorId);
			text_rssi.setText(rssi + "");

		}

		public void setrssi(String jl, int rssi) {
			text_jl.setText(jl);
			text_rssi.setText(rssi + "");
		}

		public void setname(String name) {
			text_name.setText(name);
		}

	}
}
