package com.example.sweethub.Model;

public class FeedBack {
    private String _id,noi_dung,user;

    public FeedBack(String _id, String noi_dung, String user) {
        this._id = _id;
        this.noi_dung = noi_dung;
        this.user = user;
    }

    public FeedBack() {
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getNoi_dung() {
        return noi_dung;
    }

    public void setNoi_dung(String noi_dung) {
        this.noi_dung = noi_dung;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }
}
