package io.qbbr.arduinocar;

import android.bluetooth.BluetoothDevice;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Set;

public class DeviceListActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_list);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        ArrayList<Device> devices = new ArrayList<Device>();
        DeviceAdapter adapter = new DeviceAdapter(this, devices);

        ListView listView = findViewById(R.id.list);
        listView.setOnItemClickListener(this);
        listView.setAdapter(adapter);

        Set<BluetoothDevice> pairedDevices = G.bluetoothAdapter.getBondedDevices();
        if (pairedDevices.size() > 0) {
            for (BluetoothDevice device : pairedDevices) {
                Device newDevice = new Device(device.getName(), device.getAddress(), false);
                adapter.add(newDevice);
            }
        }
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Device device = (Device) view.getTag();

        if (!G.connect(device)) {
            Toast.makeText(this, "Could'n connect", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(this, "Connected", Toast.LENGTH_LONG).show();
        }

        finish();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }

        return super.onOptionsItemSelected(item);
    }
}
