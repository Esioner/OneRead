package com.esioner.oneread.bean;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Esioner on 2018/6/15.
 */

public class SerialIdListData {

    private int res;
    private SerialIdData data;

    public SerialIdData getData() {
        return data;
    }

    public void setData(SerialIdData data) {
        this.data = data;
    }

    public int getRes() {
        return res;
    }

    public void setRes(int res) {
        this.res = res;
    }


    public class SerialIdData {
        private String finished;
        private String id;
        private String title;
        @SerializedName("list")
        private List<SerialIdInfo> serialIdInfoList;

        public String getFinished() {
            return finished;
        }

        public void setFinished(String finished) {
            this.finished = finished;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public List<SerialIdInfo> getSerialIdInfoList() {
            return serialIdInfoList;
        }

        public void setSerialIdInfoList(List<SerialIdInfo> serialIdInfoList) {
            this.serialIdInfoList = serialIdInfoList;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public class SerialIdInfo {
            private String id;
            private String number;
            @SerializedName("serial_id")
            private String serialId;

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getNumber() {
                return number;
            }

            public void setNumber(String number) {
                this.number = number;
            }

            public String getSerialId() {
                return serialId;
            }

            public void setSerialId(String serialId) {
                this.serialId = serialId;
            }
        }

    }


}
