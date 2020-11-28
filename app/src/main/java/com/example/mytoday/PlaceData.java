package com.example.mytoday;

import java.io.Serializable;

// 장소검색했을때 리스트뷰에 뿌려주기 위한 데이터클래스
// 장소명, 지번주소, 도로명주소
public class PlaceData implements Serializable {

    String title;
    String address;
    String roadAddress;

//    public PlaceData(String title, String address, String roadAddress) {
//        this.title = title;
//        this.address = address;
//        this.roadAddress = roadAddress;
//    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getRoadAddress() {
        return roadAddress;
    }

    public void setRoadAddress(String roadAddress) {
        this.roadAddress = roadAddress;
    }


}
