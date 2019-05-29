package com.mia.phase10.network.threads;

import android.app.Activity;
import android.util.Log;

import com.mia.phase10.GameStartActivity;
import com.mia.phase10.network.Host;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Connections {
    private List<Connection> connections;
    private final static String TAG = "Connections";
    private final Host host;
    private static final String HOST_NOT_NULL = "Host must not be NULL";
    private static final String LIST_NOT_NULL = "List must not be NULL";
    private Activity activity;

    private Connections(Host host, List<Connection> connections, Activity activity) {
        Objects.requireNonNull(host, HOST_NOT_NULL);
        Objects.requireNonNull(connections, LIST_NOT_NULL);
        this.host = host;
        this.connections = connections;
        this.activity = activity;
    }

    public void addConnection(Connection connection) {
        this.connections.add(connection);
        Log.i(TAG, "Connection Added");
//
        GameStartActivity.runOnUI(new Runnable() {
            public void run() {
                ( (GameStartActivity) activity).addNewConnection();
            }
        });

    }

    public static Connections emptyList(Host host, Activity activity) {

        return new Connections(host, new ArrayList<Connection>(), activity);
    }

    public void sendObjectToAll(Serializable obj) {
        for (Connection connection : connections) {
            connection.sendObject(obj);
        }


    }

    public void sendObjectToAllAndCloseAll(Serializable obj) {
        for (Connection connection : connections) {
            connection.sendObjectAndCloseConnection(obj);
        }
        Log.i(TAG, "All connections closed.\n");
        host.closeServer();
    }

}
