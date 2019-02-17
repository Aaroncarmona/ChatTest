package com.gmail.chattest.common.enums;

public enum EResourceType {
    PNG(".png"),
    JPG(".jpg"),
    JPEG(".jpeg"),
    MP4(".mp4"),
    MP3(".mp3")
    ;

    public String type;

    EResourceType(String type){
        this.type = type;
    }

    public boolean is( String type){
        return (this.type.contains(type));
    }
}
