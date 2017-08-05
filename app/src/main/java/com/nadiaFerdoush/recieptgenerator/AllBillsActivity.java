package com.nadiaFerdoush.recieptgenerator;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

public class AllBillsActivity extends AppCompatActivity {
    ArrayAdapter<Bill> mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_bills);

        AppDatabase db = AppDatabase.getInstance(this);

        List<Bill> bills = db.getBills();

        ListView billList = (ListView) findViewById(R.id.bill_list_view);
        mAdapter = new AllBillsActivity.AllBillsListAdapter(this, R.layout.bill_row, bills);
        billList.setAdapter(mAdapter);

        billList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Bill bill = mAdapter.getItem(position);

                Intent intent = new Intent(AllBillsActivity.this, InvoiceActivity.class);
                intent.putExtra("bill_id", bill.id);
                startActivity(intent);
            }
        });

    }

    static class AllBillsListAdapter extends ArrayAdapter<Bill> {

        public AllBillsListAdapter(Context context, int resource, List<Bill> objects) {
            super(context, resource, objects);
        }

        @NonNull
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View v = LayoutInflater.from(getContext()).inflate(R.layout.bill_row, null);

            Bill bill = getItem(position);

            TextView billIdView = (TextView) v.findViewById(R.id.bill_id);
            billIdView.setText(bill.id + "");

            TextView billCreationView = (TextView) v.findViewById(R.id.creation_time);
            billCreationView.setText(bill.getTimeCreated());

            TextView netAmountView = (TextView) v.findViewById(R.id.net_amount);
            netAmountView.setText("BDT " + String.format("%.2f", bill.getNetAmount()));
            return v;
        }
    }
}


