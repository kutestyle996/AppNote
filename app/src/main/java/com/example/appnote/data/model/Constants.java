package com.example.appnote.data.model;

public interface Constants {
    String days[] = {"Day", "Today", "Tomorrow", "After Tomorrow", "Other.."};
    String hours[] = {"Time", "08:00", "12:00", "16:00", "20:00", "Other.."};

    interface Type {
        String TYPE_IMAGE = "image/*";
        String TYPE_SHARE = "text/plain";
    }

    interface Extra {
        String EXTRA_POSITION = "EXTRA_POSITION";
        String EXTRA_ACTION = "EXTRA_ACTION";
        String EXTRA_NOTIFICATION_ID = "EXTRA_NOTIFICATION_ID";
    }
}
