package ua.com.besqueet.mtwain.separpicker.controllers;


import de.halfbit.tinybus.TinyBus;
import de.halfbit.tinybus.wires.ConnectivityWire;

public enum BusController {
    INSTANCE;
    private TinyBus bus;

    public TinyBus getBus() {
        return bus;
    }

    public void setBus(TinyBus bus) {
        this.bus = bus;
        this.bus.wire(new ConnectivityWire(ConnectivityWire.ConnectionStateEvent.class));
    }
}
