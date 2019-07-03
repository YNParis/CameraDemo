package com.yy.camerademo;

/**
 * Created by yangna on 2019/5/21.
 */
public class UploadResultBean {

    private String code;
    private Data data;
    private String msg;

    public void setCode(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public Data getData() {
        return data;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getMsg() {
        return msg;
    }

    public class Data {

        private String docRealName;
        private String url;

        public void setDocRealName(String docRealName) {
            this.docRealName = docRealName;
        }

        public String getDocRealName() {
            return docRealName;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getUrl() {
            return url;
        }

        @Override public String toString() {
            return "Data{" +
                "docRealName='" + docRealName + '\'' +
                ", url='" + url + '\'' +
                '}';
        }
    }

    @Override public String toString() {
        return "UploadResultBean{" +
            "code='" + code + '\'' +
            ", data=" + data +
            ", msg='" + msg + '\'' +
            '}';
    }
}
