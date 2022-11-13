// CENG-322-0NC Francisco Santos n01423860, Pradeep Singh n00975892
// CENG-322-0NB Ralph Robes n01410324, Elijah Tanimowo n01433560
package ca.nika.it.gear5;

import static android.app.Activity.RESULT_OK;
import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class BluetoothFragment extends Fragment {

    private static final int REQUEST_ENABLE_BT = 0;
    private static final int REQUEST_DISCOVER_BT = 1;
    private View v;
    TextView mStatusBlueTv;

    ImageView mBlueIv;
    Button mOnBtn;
    Button mOffBtn;
    Button mDiscoverBtn;


    BluetoothAdapter mBlueAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        v = inflater.inflate(R.layout.fragment_bluetooth, container, false);


        this.mStatusBlueTv = (TextView) this.v.findViewById(R.id.statusBluetoothTv);
        this.mBlueIv = (ImageView) this.v.findViewById(R.id.bluetoothIv);
        this.mOnBtn = (Button) this.v.findViewById(R.id.onBtn);
        this.mOffBtn = (Button) this.v.findViewById(R.id.offBtn);
        this.mDiscoverBtn = (Button) this.v.findViewById(R.id.discoverableBtn);


        //adapter
        mBlueAdapter = BluetoothAdapter.getDefaultAdapter();

        //check if bluetooth is available or not
        if (mBlueAdapter == null) {
            mStatusBlueTv.setText(R.string.bluetoothUnavailable);
        } else {
            mStatusBlueTv.setText(R.string.bluetoothAvailable);
        }

        //set image according to bluetooth status(on/off)
        if (mBlueAdapter.isEnabled()) {
            mBlueIv.setImageResource(R.drawable.b_on);
        } else {
            mBlueIv.setImageResource(R.drawable.b_off);
        }

        //on btn click
        mOnBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!mBlueAdapter.isEnabled()) {
                    showToast(getString(R.string.turningOnBluetooth));
                    //intent to on bluetooth
                    Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                    startActivityForResult(intent, REQUEST_ENABLE_BT);
                } else {
                    showToast(getString(R.string.bluetoothAlreadyOn));
                }
            }
        });
        //discover bluetooth btn click
        mDiscoverBtn.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("MissingPermission")
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
                startActivityForResult(intent, REQUEST_DISCOVER_BT);
                if (mBlueAdapter.isDiscovering()) {
                    showToast(getString(R.string.deviceDiscoverable));

                }
            }
        });
        //off btn click
        mOffBtn.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("MissingPermission")
            @Override
            public void onClick(View v) {
                if (mBlueAdapter.isEnabled()) {
                    mBlueAdapter.disable();
                    showToast(getString(R.string.turningOffBluetooth));
                    mBlueIv.setImageResource(R.drawable.b_off);
                } else {
                    showToast(getString(R.string.bluetoothAlreadyOff));
                }
            }
        });

        return v;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode){
            case REQUEST_ENABLE_BT:
                if (resultCode == RESULT_OK){
                    //bluetooth is on
                    mBlueIv.setImageResource(R.drawable.b_on);
                    showToast(getString(R.string.bluetoothOn));
                }
                else {
                    //user denied to turn bluetooth on
                    showToast(getString(R.string.bluetoothDenied));
                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    //toast message function
    private void showToast(String msg){
        Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
    }


}