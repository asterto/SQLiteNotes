package com.example.sqlitenotes.db;

import com.example.sqlitenotes.adapter.ListItem;

import java.util.List;

public interface OnDataReceived {
    void onReceived(List<ListItem> list);
}
