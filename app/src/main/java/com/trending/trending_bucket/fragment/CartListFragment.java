package com.trending.trending_bucket.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.database.SQLException;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

import com.trending.trending_bucket.activity.Cartz;
import com.trending.trending.R;
import com.trending.trending_bucket.adapters.AdapterCart;

import com.trending.trending_bucket.util.DBHelper;
import com.trending.trending_bucket.util.TotalPriceCallback;
import com.trending.trending_bucket.util.utilMethods;

import java.text.DecimalFormat;
import java.util.ArrayList;

import static com.trending.trending_bucket.util.utilMethods.showNoInternetDialog;

public class CartListFragment extends Fragment implements utilMethods.InternetConnectionListener{

    Cartz cart;

    private final int CART_ACTION = 1;

    private TotalPriceCallback mCallback;

    private ListView cartListView;

    public TextView TxtTotal;

    private utilMethods.InternetConnectionListener internetConnectionListener;

    // declate dbhelper and adapter objects

    DBHelper dbhelper;

    AdapterCart mola;

    ArrayList<ArrayList<Object>> data;

    static ArrayList<Integer> Menu_ID = new ArrayList<Integer>();

    static ArrayList<String> Order_name = new ArrayList<String>();

    static ArrayList<Integer> Quantity = new ArrayList<Integer>();

    static ArrayList<Double> Sub_total_price = new ArrayList<Double>();

    static ArrayList<String> Image = new ArrayList<String>();

    ProgressDialog progressDialog;

    double Total_price = 0;

    // create price format

    DecimalFormat formatData = new DecimalFormat("#.##");

    public CartListFragment() {
    }

    public static CartListFragment newInstance() {

        CartListFragment fragment = new CartListFragment();

        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        cart = (Cartz) getActivity();


    }


    public void onAttach(Context context) {
        super.onAttach(context);

        mCallback = (TotalPriceCallback) getActivity();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.listcart, container, false);

        cartListView = (ListView) rootView.findViewById(R.id.products_listview);

        TxtTotal = (TextView) rootView.findViewById(R.id.TotalPrice);

        dbhelper = new DBHelper(getActivity());

        // open database

        try
        {

            dbhelper.openDataBase();
        }
        catch(SQLException sqle){
            throw sqle;
        }

        cart.getSupportActionBar().setTitle("Cart");

        // selecting single ListView item

        ListView lv = cartListView;

        // listening to single listitem click

        lv.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
            }
        });
        setHasOptionsMenu(true);
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (utilMethods.isConnectedToInternet(getActivity())) {
          initCartList();
        } else {
            internetConnectionListener = CartListFragment.this;
            showNoInternetDialog(getActivity(), internetConnectionListener,
                    getResources().getString(R.string.no_internet),
                    getResources().getString(R.string.no_internet_text),
                    getResources().getString(R.string.retry_string),
                    getResources().getString(R.string.exit_string), CART_ACTION);
        }
    }




    private void initCartList() {

        progressDialog = new ProgressDialog(getActivity());

        progressDialog.setCancelable(false);
        progressDialog.setTitle("Processing");
        progressDialog.setMessage("Please wait...");
        progressDialog.show();

        new getDataTask().execute();
    }


    // clear arraylist variables before used

    void clearData(){
        Order_name.clear();
        Menu_ID.clear();
        Quantity.clear();
        Sub_total_price.clear();
        Image.clear();

    }

    // asynctask class to handle parsing json in background
    public class getDataTask extends AsyncTask<Void, Void, Void> {

        // show progressbar first
        getDataTask(){


        }

        @Override
        protected Void doInBackground(Void... params) {
            // TODO Auto-generated method stub
            // get data from database
            getDataFromDatabase();
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            // TODO Auto-generated method stub
            // show data
            progressDialog.dismiss();



        }
    }

    @Override
    public void onConnectionEstablished(int code) {
        if (code == CART_ACTION) {
            initCartList();
        }
    }

    @Override
    public void onUserCanceled(int code) {
        if (code == CART_ACTION) {
            getActivity().finish();
        }
    }

    // method to get data from server
    public void getDataFromDatabase() {

        Total_price = 0;
        clearData();
        try {
            data = dbhelper.getAllData();


            // store data to arraylist variables
            for (int i = 0; i < data.size(); i++) {
                ArrayList<Object> row = data.get(i);

                Menu_ID.add(Integer.parseInt(row.get(1).toString()));
                Order_name.add(row.get(2).toString());
                Sub_total_price.add(Double.parseDouble(formatData.format(Double.parseDouble(row.get(3).toString()))));
                Image.add(row.get(4).toString());
                // Quantity.add(Integer.parseInt(row.get(2).toString()));
                Total_price += Sub_total_price.get(i);
            }


            mola = new AdapterCart(getActivity(), mCallback, Menu_ID, Order_name, Sub_total_price, Image, Total_price);
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    cartListView.setAdapter(mola);
                }
            });
            // count total order

        }
        catch (NullPointerException ex)
        {}


    }
}