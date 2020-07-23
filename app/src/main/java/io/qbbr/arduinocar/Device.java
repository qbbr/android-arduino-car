package io.qbbr.arduinocar;

public class Device {
    private String name;
    private String address;
    private boolean connected;

    public Device(String name, String address, boolean connected) {
        this.name = name;
        this.address = address;
        this.connected = connected;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
