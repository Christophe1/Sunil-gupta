


package com.example.chris.sunil_gupta;

import android.app.Activity;
import android.app.Service;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.CallLog;
import android.provider.ContactsContract;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MainActivity extends Activity {
//ArrayList is an implementation of List.
//ArrayList provides a resizable-array, which means that items can be added and removed from the list. An ArrayList is a
// dynamic data structure so it can be used when there is no upper bound on the number of elements, ideal for the Call
// history. From the other side, a simple Array in java is a static data structure, because the initial size of array cannot be
// changed, so it can be used only when the data has a known number of elements.

//We are making a list called listofphonehistory, which is called in the Getcalldetails method,
// and we are using CallData as the datasource

    private List<CallData> listofphonehistory = new ArrayList<CallData>();


//    playing around with autocomplete here
    ArrayList<String> emailAddressCollection = new ArrayList<String>();

//    ContentResolver cr = getContentResolver();

    Cursor emailCur = getContentResolver().query(ContactsContract.CommonDataKinds.Email.CONTENT_URI, null, null, null, null);
//    ------------


    //    Context is an abstract class...which means what exactly?
    private Context context = null;




    @Override
    protected void onCreate(Bundle savedInstanceState) {

//create a ListView object called listview
        final ListView listview;

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        context = this;

//initialise the object called listview, it's a ListView object and the exact id it will appear in is called ListView_CallData
        listview = (ListView) findViewById(R.id.ListView_CallData);

//        call the function getCallDetails which will sort our number, name, call date, call type, duration
        getCallDetails();
//ps: would this work for the code below? I saw it on https://developer.xamarin.com/guides/android/user_interface/working_with_listviews_and_adapters/part_3_//-_customizing_a_listview's_appearance/  seems shorter
// populate the listview with data
//        listView.Adapter = new HomeScreenAdapter(this, tableItems);
//        listView.ItemClick += OnListItemClick;  // to be defined


//populate ListView_CallData with data
//CustomAdapter is the class we are going to use. We will use it to create CustomAdapter
//objects which will appear in the MainActivity activity, using the listofphonehistory
        CustomAdapter adapter = new CustomAdapter(MainActivity.this, listofphonehistory);
        listview.setAdapter(adapter);

//Here we define LinearLayout2, which contains the edittext where the user enters a phone number and
//        contains the Call button, with the image of the phone

//        this is the layout
        final LinearLayout LinearLayoutNumberandCall = (LinearLayout) findViewById(R.id.LinearLayout2);

//      this is the edittext in the layout
        final EditText editText = (EditText) findViewById(R.id.editText);
        editText.requestFocus();


//        the code below tells us whether or not the soft keyboard is showing. It's a bit of a hack, realy. We tell
//        if the keyboars is showing by setting a listener and seeing if there is something 100 pixels or more on the screen.
//        If there is, it#s probably a keyboard that has popped up.

//        If it is showing, we will remove the softkeyboard button, if not showing, then we will
//        display the softkeyboard button.
//        basically we have set a global listener on the activity layout. If there's any change in the layout,
//        this code is called.
//        If the difference in height between the original height of the layout, and the new changed state of the
//        layout, is more than 100 pixels, then we assume a keyboard is showing

//        set our layout
        final View activityRootView = findViewById(R.id.LinearLayout1);

//        set a listener on the original size of the layout
        activityRootView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {

//                if the difference in height is 100 pixels or more....
                int heightDiff = activityRootView.getRootView().getHeight() - activityRootView.getHeight();

//                set our keyboard button
                final ImageButton thekeyboard = (ImageButton) findViewById(R.id.keyboardButton);

//                set our settings button
                final ImageButton thesettingsButton = (ImageButton)findViewById(R.id.settingsButton);

//                set our call button
                final ImageButton thecallButton = (ImageButton)findViewById(R.id.callButton);

                if (heightDiff > 100) {
                    // if more than 100 pixels, its probably a keyboard...
//                    ... so we don't need to show the 'Show keyboard' icon,
//                as the keyboard is already showing - hide it completely.
                    thekeyboard.setVisibility(View.GONE);
                    Log.e("button", "keyboard");


//                    also,

//                  set callbutton width to 80 pixels
//                  the callbutton is set to 0px in the xml - as is the linearlayout that holds it. Apparently this
//                    saves on resources, instead of writing twice. So set it to 80 pixels now.
                    LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) thecallButton.getLayoutParams();
                    params.width = 80;

                    thecallButton.setLayoutParams(params);

//                    make the settingsbutton the same width as the callbutton
                    thesettingsButton.setLayoutParams(thecallButton.getLayoutParams());
                }
                else {
//                    if the keyboard is not visible...
                    Log.e("button", "no keyboard");

//                    make the keyboard visible
                    thekeyboard.setVisibility(View.VISIBLE);

//                    also,

//                  set callbutton width to 80 pixels
//                  the callbutton is set to 0px in the xml - as is the linearlayout that holds it. Apparently this
//                    saves on resources, instead of writing twice. So set it to 80 pixels now.

                    LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) thecallButton.getLayoutParams();
                    params.width = 80;

                    thecallButton.setLayoutParams(params);

//                    make the settingsbutton the same width as the callbutton

                    thesettingsButton.setLayoutParams(thecallButton.getLayoutParams());
                }
            }
        });

//setOnScrollListener has two parts, OnScrollStateChanged and onScroll. We are
//        interested in the first one - if the user scrolls, we will hide the softkeyboard.
        listview.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {


                Log.e("scrollState", "Yep, scroll worked.");

//        If the user scrolls the listview then hide the softkeyboard
                if (scrollState != 0) {
                    InputMethodManager inputMethodManager = (InputMethodManager)
                            getSystemService(Activity.INPUT_METHOD_SERVICE);
                    inputMethodManager.hideSoftInputFromWindow(listview.getWindowToken(), 0);

                }



            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

            }
        });


//This will deal with our editText box, display it if needs be
        editText.addTextChangedListener(new TextWatcher() {


            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
//          If there are changes typed in the edittext, then change the height of LinearLayout2 from 0,
//            as described in the activity_main xml file
            public void afterTextChanged(Editable s) {

                float density = getResources().getDisplayMetrics().density;



//                if editText has characters in it...
                if (editText.length() > 0) {


//                    not sure what density and layoutparams do. I think we're
//                    converting pixels to dp
                    LinearLayoutNumberandCall.getLayoutParams().height = (int) (50 * density);
//                    editText.requestLayout();



                    Log.e("scrollState", "Yep, it worked.");

                }
//                if there is nothing in edittext, make the layout that contains it, Linear
// Layout2, invisible. This will also make the Call button invisible, as it's in the layout 2
                else if (editText.length() == 0) {

//                    float density=getResources().getDisplayMetrics().density;
//                    editText.getLayoutParams().width =(int)(0*density);
                    LinearLayoutNumberandCall.getLayoutParams().height = (int) (0 * density);
//                    editText.requestLayout();
                }
                LinearLayoutNumberandCall.requestLayout();

            }
        });


    }
    //show the softkeyboard when the user click the image with the keyboard
    public void softkeyboardButton(View v) {

        final EditText editText = (EditText) findViewById(R.id.editText);
//  softkeyboard start up code
        InputMethodManager imm = (InputMethodManager) this.getSystemService(Service.INPUT_METHOD_SERVICE);
        imm.showSoftInput(editText, 0);
//        give editText the focus
        editText.requestFocus();

        final ImageButton ButtonTwo = (ImageButton)findViewById(R.id.callButton);

    }

    //This clears the edittext next time user starts the application, rather than
//    having the same numbers there, which the user probably doesn't want anymore
    protected void onResume() {
        final EditText editText = (EditText) findViewById(R.id.editText);
        super.onResume();
        editText.setText("");


    }
    public void getCallDetails() {


        //        cursor1 gets all the items in the calllog and arranges them from newest call down
        Cursor cursor1 = getContentResolver().query(
                CallLog.Calls.CONTENT_URI, null, null, null, CallLog.Calls.DATE + " DESC");

//looks like all the cell values in the calllog database are integers
        int number = cursor1.getColumnIndex(CallLog.Calls.NUMBER);
        int type = cursor1.getColumnIndex(CallLog.Calls.TYPE);
        int date = cursor1.getColumnIndex(CallLog.Calls.DATE);
        int duration = cursor1.getColumnIndex(CallLog.Calls.DURATION);

        int name = cursor1.getColumnIndex(CallLog.Calls.CACHED_NAME);

//declare some new variables here; we're going to convert the integers into these
        int callType;
        String phoneNumber;
        String callDate;
        String callDuration;
        Date callDateTime;

        String cachedName;


        while (cursor1.moveToNext()) {
//        go through all the rows in the db and convert the values to strings or whatever
//        It's important that these are inside the while loop. Otherwise it will try to read
//        the value of a column while the cursor is at an invalid position (-1) because moveToNext()
//        hasn't been called yet.

            callType = cursor1.getInt(type);
            phoneNumber = cursor1.getString(number);
            callDate = cursor1.getString(date);
            callDateTime = new Date(Long.valueOf(callDate));
            callDuration = cursor1.getString(duration);

            cachedName = cursor1.getString(name);


// If the contact has a name, then show the name in the calllog instead of the number
            if (cachedName != null) {
                phoneNumber = cachedName;
            } else {
                phoneNumber = phoneNumber;
            }

//            the string cType will give us text of either outgoing, incoming or missed calls
            String cType = null;


//          callType will either be 1,2 or 3
            switch (callType) {
                case CallLog.Calls.OUTGOING_TYPE:
                    cType = "OUTGOING";
                    break;

                case CallLog.Calls.INCOMING_TYPE:
                    cType = "INCOMING";
                    break;

                case CallLog.Calls.MISSED_TYPE:
                    cType = "MISSED";
                    break;
            }
//          CallData is a constructor
//            We are passing the values cType, phoneNumber, callDateTime and callDuration, in the While Loop of above,
//              to the CallData object, which I have defined in another java file,
//          and this will show us calltype, callnumber, calldatetime and callduration in our cells.
            CallData calldata = new CallData(cType, phoneNumber, callDateTime, callDuration);
//            add new call data info to the list, moving on down through the values in Calllog
            listofphonehistory.add(calldata);
        }

        cursor1.close();
    }

}







