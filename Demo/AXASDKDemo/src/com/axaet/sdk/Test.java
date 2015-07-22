package com.axaet.sdk;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothAdapter.LeScanCallback;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.inputmethodservice.Keyboard;
import android.inputmethodservice.KeyboardView;
import android.inputmethodservice.KeyboardView.OnKeyboardActionListener;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.text.Editable;
import android.text.InputType;
import android.transition.Visibility;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;



import com.axaet.ibeacon.A_BluetoothLeService;
import com.axaet.sdk.Ibeacon.Ibeacon_device;

/**
 * Created by hebiao on 14-7-23.
 */
public class Test extends Activity {
	
	private Button gj;
	
	EditText edit_MajorId;
	EditText edit_MinorId;
	EditText edit_period;
	EditText et_pwd;
	private LinearLayout ll_name;
	private LinearLayout ll_pwd;
	int MajorId = 0;
	int MinorId = 0;
	int TxPower = 0;
	String Tx = "";
	int period = 0;
	private List<String> list;
	private Spinner mySpinner;
	private ArrayAdapter<String> adapter;
	TextView t1;

	EditText edit;
	KeyboardView keyboardView;
	TextView text_uuid;
	private String mode;

	private EditText et_name;
	

	

	

	

	private String mDeviceAddress;
	public static final String EXTRAS_DEVICE_NAME = "DEVICE_NAME";
	public static final String EXTRAS_DEVICE_ADDRESS = "DEVICE_ADDRESS";
	private A_BluetoothLeService mBluetoothLeService;
	
	
	// Code to manage Service lifecycle.
	private final ServiceConnection mServiceConnection = new ServiceConnection() {

		@Override
		public void onServiceConnected(ComponentName componentName,
				IBinder service) {
			mBluetoothLeService = ((A_BluetoothLeService.LocalBinder) service)
					.getService();
			if (!mBluetoothLeService.initialize()) {
				finish();
			}
			// Automatically connects to the device upon successful start-up
			// initialization.
			mBluetoothLeService.connect(mDeviceAddress);
		}

		@Override
		public void onServiceDisconnected(ComponentName componentName) {
			mBluetoothLeService = null;
		}
	};

	

	



	public void setedit() {
		edit = (EditText) findViewById(R.id.edit);
		if (android.os.Build.VERSION.SDK_INT <= 10) {
			edit.setInputType(InputType.TYPE_NULL);
		} else {
			this.getWindow().setSoftInputMode(
					WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
			try {
				Class<EditText> cls = EditText.class;
				Method setShowSoftInputOnFocus;
				setShowSoftInputOnFocus = cls.getMethod(
						"setShowSoftInputOnFocus", boolean.class);
				setShowSoftInputOnFocus.setAccessible(true);
				setShowSoftInputOnFocus.invoke(edit, false);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		edit.setCursorVisible(true);
		edit.setSingleLine(false);
		

	

		
				
	}

	public void setSpinner(int x) {
		mySpinner = (Spinner) findViewById(R.id.spinner1);
		list = new ArrayList<String>();
		list.add("0");
		list.add("-6");
		list.add("-23");
		if (x == 19) {
			list.add("4");
		}
		mySpinner = (Spinner) findViewById(R.id.spinner1);
		
		adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, list);
		
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		
		mySpinner.setAdapter(adapter);
		mySpinner.setSelection(0);
		
		mySpinner
				.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
					public void onItemSelected(AdapterView<?> arg0, View arg1,
							int arg2, long arg3) {

						if (list.get(arg2).equals("0")) {
							TxPower = 0;
						} else if (list.get(arg2).equals("-6")) {
							TxPower = 1;
						} else if (list.get(arg2).equals("-23")) {
							TxPower = 2;
						} else if (list.get(arg2).equals("4")) {
							TxPower = 3;
						}

					}

					public void onNothingSelected(AdapterView<?> arg0) {
					}
				});
		
		mySpinner.setOnTouchListener(new Spinner.OnTouchListener() {
			public boolean onTouch(View v, MotionEvent event) {
				return false;
			}
		});
		
		mySpinner.setOnFocusChangeListener(new Spinner.OnFocusChangeListener() {
			public void onFocusChange(View v, boolean hasFocus) {

			}
		});
	}

	@SuppressLint({ "SimpleDateFormat", "NewApi" })
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layoutest);

		setedit();
		edit_MajorId = (EditText) findViewById(R.id.edit_MajorId);
		edit_MinorId = (EditText) findViewById(R.id.edit_MinorId);
		edit_period = (EditText) findViewById(R.id.edit_period);
		et_pwd = (EditText) findViewById(R.id.et_pwd);
		
		t1 = (TextView) findViewById(R.id.textView1);
		text_uuid = (TextView) findViewById(R.id.text_uuid);
		
		
		setSpinner(18);

		
		
		
	
		registerReceiver(mGattUpdateReceiver, makeGattUpdateIntentFilter());
		final Intent intent = getIntent();
		
		
		mDeviceAddress = intent.getStringExtra(EXTRAS_DEVICE_ADDRESS);
		Intent gattServiceIntent = new Intent(this, A_BluetoothLeService.class);
		bindService(gattServiceIntent, mServiceConnection, BIND_AUTO_CREATE);

	}

	

	@Override
	protected void onResume() {
		super.onResume();

	}

	

	private static IntentFilter makeGattUpdateIntentFilter() {
		final IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction(A_BluetoothLeService.ACTION_GATT_CONNECTED);
		intentFilter.addAction(A_BluetoothLeService.ACTION_GATT_DISCONNECTED);
		intentFilter
				.addAction(A_BluetoothLeService.ACTION_GATT_SERVICES_DISCOVERED);
		intentFilter.addAction(A_BluetoothLeService.ACTION_DATA_AVAILABLE);
		return intentFilter;
	}

	@Override
	protected void onPause() {
		super.onPause();
		// scanLeDevice(false);

	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		unregisterReceiver(mGattUpdateReceiver);
		unbindService(mServiceConnection);
		mBluetoothLeService.disconnect();
		mBluetoothLeService = null;
	}

	private final BroadcastReceiver mGattUpdateReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			final String action = intent.getAction();
			if (A_BluetoothLeService.ACTION_GATT_CONNECTED.equals(action)) {
;
				Log.d("test", "ACTION_GATT_CONNECTED");
			
			} else if (A_BluetoothLeService.ACTION_GATT_DISCONNECTED
					.equals(action)) {
				Log.d("test", "ACTION_GATT_DISCONNECTED");
				
				t1.setText(R.string.Disconnect);
				

				Intent intent1 = new Intent(Test.this, Ibeacon.class);
				startActivity(intent1);
				finish();
			} else if (A_BluetoothLeService.ACTION_GATT_SERVICES_DISCOVERED
					.equals(action)) {
				// Show all the supported services and characteristics on the
				// user interface.
				Log.d("test", "ACTION_GATT_SERVICES_DISCOVERED");
				mBluetoothLeService.getSupportedGattServices();
				mBluetoothLeService.enableLostNoti();
				
				t1.setText(R.string.Connected);
				

			} else if (A_BluetoothLeService.ACTION_DATA_AVAILABLE
					.equals(action)) {
				Log.d("test111", "ACTION_DATA_AVAILABLE");

				Log.e("DATA", mBluetoothLeService.data_s[0] + "");

				
				

			

				if (mBluetoothLeService.data_s[0] == -86
						&& mBluetoothLeService.data_s[1] == 3) {
				} else if (mBluetoothLeService.data_s[0] == -86
						&& mBluetoothLeService.data_s[1] == 1) {
				} else if (mBluetoothLeService.data_s[0] == -86
						&& mBluetoothLeService.data_s[1] == 2) {
				} else if (mBluetoothLeService.data_s[0] == 18) {
					if (mBluetoothLeService.data_s[5] == 0) {
						Tx = "0";
						mySpinner.setSelection(0);
					} else if (mBluetoothLeService.data_s[5] == 1) {
						Tx = "-6";
						mySpinner.setSelection(1);
					} else if (mBluetoothLeService.data_s[5] == 2) {
						Tx = "-23";
						mySpinner.setSelection(2);
					} else if (mBluetoothLeService.data_s[5] == 3) {
						Tx = "4";
						mySpinner.setSelection(3);
					}
					edit_MajorId.setText((mBluetoothLeService.data_s[1] * 256)
							+ mBluetoothLeService.data_s[2] + "");
					edit_MinorId.setText((mBluetoothLeService.data_s[3] * 256)
							+ mBluetoothLeService.data_s[4] + "");
					edit_period.setText((mBluetoothLeService.data_s[6] * 256)
							+ mBluetoothLeService.data_s[7] + "");
				} else if (mBluetoothLeService.data_s[0] == 19) {
					setSpinner(19);
					if (mBluetoothLeService.data_s[5] == 0) {
						Tx = "0";
						mySpinner.setSelection(0);
					} else if (mBluetoothLeService.data_s[5] == 1) {
						Tx = "-6";
						mySpinner.setSelection(1);
					} else if (mBluetoothLeService.data_s[5] == 2) {
						Tx = "-23";
						mySpinner.setSelection(2);
					} else if (mBluetoothLeService.data_s[5] == 3) {
						Tx = "4";
						mySpinner.setSelection(3);
					}
					edit_MajorId.setText((mBluetoothLeService.data_s[1] * 256)
							+ mBluetoothLeService.data_s[2] + "");
					edit_MinorId.setText((mBluetoothLeService.data_s[3] * 256)
							+ mBluetoothLeService.data_s[4] + "");
					edit_period.setText((mBluetoothLeService.data_s[6] * 256)
							+ mBluetoothLeService.data_s[7] + "");
				} else if (mBluetoothLeService.data_s[0] == 17) {
					String uuid_str = "";
					for (int i = 1; i < 17; i++) {
						// uuid_str+=Integer.toHexString(mBluetoothLeService.data_uuid[i]);
						String ret = Integer
								.toHexString(mBluetoothLeService.data_uuid[i]);

						if (ret.length() == 1) {
							ret = "0" + ret;
						}

						// uuid=uuid+Integer.toHexString(scanRecord[i]&0xff);
						uuid_str = uuid_str + ret;
					}
					if (uuid_str.length() == 32) {
						text_uuid.setText(R.string.Correct);
					}
					edit.setText(uuid_str);

					Log.e("uuid_str", uuid_str + "");

				}

			}
		}

	};

	

	

	public boolean onKeyDown(int keyCode, KeyEvent event) {

		if (event.getKeyCode() == event.KEYCODE_BACK) {
			
			Intent intent = new Intent(Test.this, Ibeacon.class);
			startActivity(intent);
			finish();
		}
		return false;

	}
}
