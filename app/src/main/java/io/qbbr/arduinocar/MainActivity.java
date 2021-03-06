package io.qbbr.arduinocar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final int REQUEST_ENABLE_BT = 1;

    TextView tvCurrentDevice;
    Button btnControls;
    Button btnChooseDevice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        G.bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        if (G.bluetoothAdapter == null) {
            Toast.makeText(this, "You device doesn't support Bluetooth", Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        if (!G.bluetoothAdapter.isEnabled()) {
            Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableIntent, REQUEST_ENABLE_BT);
        }

        btnChooseDevice = findViewById(R.id.btnChooseDevice);
        btnChooseDevice.setOnClickListener(this);

        tvCurrentDevice = findViewById(R.id.tvCurrentDevice);

        btnControls = findViewById(R.id.btnControls);
        btnControls.setOnClickListener(this);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_ENABLE_BT:
                if (resultCode == Activity.RESULT_OK) {
                    Log.d(G.LOG_TAG, "BT enabled");
                    Toast.makeText(this, "Bluetooth has turned ON", Toast.LENGTH_SHORT).show();

                } else {
                    Log.d(G.LOG_TAG, "BT not enabled");
                    Toast.makeText(this, "Problem in BT Turning ON", Toast.LENGTH_SHORT).show();
                    finish();
                }
                break;
            default:
                Log.e(G.LOG_TAG, "wrong request code");
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.action_exit:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnChooseDevice:
                Intent intent2 = new Intent(this, DeviceListActivity.class);
                startActivity(intent2);
                break;
            case R.id.btnControls:
                Intent intent = new Intent(this, ControlsActivity.class);
                startActivity(intent);
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        Log.d(G.LOG_TAG, "onResume");

        if (G.connectThread != null && G.connectThread.isConnected()) {
            tvCurrentDevice.setText("connected to device " + G.bluetoothDevice.getName());
            btnControls.setEnabled(true);
        } else {
            tvCurrentDevice.setText(R.string.device_not_chosen);
            btnControls.setEnabled(false);
        }
    }
}
