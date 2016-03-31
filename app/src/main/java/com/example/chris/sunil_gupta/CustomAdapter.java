
package com.example.chris.sunil_gupta;

import java.util.Date;
import java.util.List;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class CustomAdapter extends ArrayAdapter<CallData>{

    private List <CallData> listdata=null;
    private LayoutInflater mInflater=null;
    public CustomAdapter(Activity context, List <CallData> calldata) {
        super(context, 0);
        this.listdata=calldata;
        mInflater = context.getLayoutInflater();
    }


    @Override
    public int getCount() {
// find out how many items there are in our list
//it will return the number of objects/ cells in listdata
        return listdata.size();
    }


// For each cell in our list, we need to create what is going to happen.
//We need to create the cell (View) and populate it.
//convertView is a java parameter used to increase app performance. I think that instead of
//creating lots of views, it creates them only when needed, like when a person scrolls down
//through the list, so saves memory, increases speed

    public View getView(int position, View convertView, ViewGroup parent) {

        final ViewHolder holder;
//   if convertView is null, that is, if the cell is empty, then
        if (convertView == null || convertView.getTag() == null) {

//            Inflate list_row.xml file into each cell ( Defined below )
//            We're calling each cell convertView
            convertView = mInflater.inflate(R.layout.list_row, null);

//          View Holder Object to contain list_row.xml file elements
//           We need to do this in case a cell is null
            holder = new ViewHolder();
            holder.callnumber = (TextView) convertView.findViewById(R.id.textView_callnumber);
            holder.calltype = (TextView) convertView.findViewById(R.id.textView_calltype);
            holder.calldate = (TextView) convertView.findViewById(R.id.textView_calldate);
            holder.callduration = (TextView) convertView.findViewById(R.id.textView_callduration);

//            Set holder with LayoutInflater
            convertView.setTag(holder);
        }
        else {
            holder = (ViewHolder) convertView.getTag();
        }

        CallData calldatalist = listdata.get(position);
        String callnumber=calldatalist.getCallnumber();
        String calltype=calldatalist.getCalltype();
        Date calldate= calldatalist.getCalldatetime();
        String callduration=calldatalist.getCallduration();

//actually create physical things that we will see on the screen, in each cell.
        holder.callnumber.setText("Call Number: "+callnumber);
        holder.calltype.setText("Call Type: "+calltype);
        holder.calldate.setText("Call Date: "+String.valueOf(calldate));
        holder.callduration.setText("Duration: "+callduration);

        return convertView;
    }

//static class doesn't need reference of Outer class, nice and simple

    private static class ViewHolder {
        TextView callnumber;
        TextView calltype;
        TextView calldate;
        TextView callduration;
    }

}




