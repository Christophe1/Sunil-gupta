package com.example.chris.sunil_gupta;

import java.io.Serializable;
import java.util.Date;

public class CallData implements Serializable{

    private String calltype;
    private String callnumber;
    private Date calldatetime;
    private String callduration;

//  this is a constructor, has the same name as our CallData object
//this is the source of our information that is going to populate our views

    public CallData(String calltype, String callnumber, Date calldatetime, String callduration)
    {
//        pass values over to our constructor
        this.calldatetime=calldatetime;
        this.callduration=callduration;
        this.callnumber=callnumber;
        this.calltype=calltype;
    }

    public String getCalltype() {
        return calltype;
    }

    public void setCalltype(String calltype) {this.calltype = calltype;}

    public String getCallnumber() {
        return callnumber;
    }

    public void setCallnumber(String callnumber) {
        this.callnumber = callnumber;
    }

    public Date getCalldatetime() {
        return calldatetime;
    }

    public void setCalldatetime(Date calldatetime) {
        this.calldatetime = calldatetime;
    }

    public String getCallduration() {
        return callduration;
    }

    public void setCallduration(String callduration) {
        this.callduration = callduration;
    }


}

