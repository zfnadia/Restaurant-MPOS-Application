package com.nadiaFerdoush.recieptgenerator;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.print.PrintHelper;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

public class InvoiceActivity extends AppCompatActivity {

    ArrayAdapter<Item> mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invoice);

        int billId = getIntent().getIntExtra("bill_id", 0);

        List<Item> items = AppDatabase.getInstance(this).getBillItems(billId);

        ListView invoiceList = (ListView) findViewById(R.id.invoice_list_view);
        mAdapter = new InvoiceActivity.InvoiceListAdapter(this, R.layout.invoice_row, items);
        invoiceList.setAdapter(mAdapter);

        Bill bill = AppDatabase.getInstance(this).getBill(billId);

        TextView grossAmountView = (TextView) findViewById(R.id.gross_amount);
        grossAmountView.setText(String.format("%.2f", bill.getNetAmount()));

        TextView vatAMountView = (TextView) findViewById(R.id.vat_amount);
        vatAMountView.setText(String.format("%.2f", bill.getVatPt()));

        TextView discountAmountView = (TextView) findViewById(R.id.discount);
        discountAmountView.setText(String.format("%.2f", bill.getDiscountPt()));

        TextView netAmountView = (TextView) findViewById(R.id.net_amount);
        netAmountView.setText(String.format("%.2f", bill.getGrossAmount()));

        TextView paidAmountView = (TextView) findViewById(R.id.paid_amount);
        paidAmountView.setText(String.format("%.2f", bill.getPaidAmount()));

        TextView changeAmountView = (TextView) findViewById(R.id.change_amount);
        changeAmountView.setText(String.format("%.2f", bill.getChangeAmount()));

        TextView paymentMethodView = (TextView) findViewById(R.id.payment_method);
        paymentMethodView.setText(new String[]{"", "Cash", "Cheque", "Credit Card", "Debit Card"}[bill.getPaymentType()]);

        TextView timeView = (TextView) findViewById(R.id.time);
        timeView.setText(String.valueOf(bill.getTimeCreated()));

        TextView tableNumView = (TextView) findViewById(R.id.table_no);
        tableNumView.setText("Table No : " + String.valueOf(bill.getTableNumber()));

        TextView billIdView = (TextView) findViewById(R.id.bill_id);
        billIdView.setText("Bill Id : " + String.valueOf(billId));

    }

    static class InvoiceListAdapter extends ArrayAdapter<Item> {

        public InvoiceListAdapter(Context context, int resource, List<Item> objects) {
            super(context, resource, objects);
        }

        @NonNull
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View v = LayoutInflater.from(getContext()).inflate(R.layout.invoice_row, null);

            Item item = getItem(position);

            TextView itemNameView = (TextView) v.findViewById(R.id.item_name);
            itemNameView.setText(item.getName());

            TextView itemPriceView = (TextView) v.findViewById(R.id.rate);
            itemPriceView.setText(String.valueOf(item.getPrice()));

            TextView itemQuantityView = (TextView) v.findViewById(R.id.quantity);
            itemQuantityView.setText(String.valueOf(item.count));

            TextView itemTotalPriceView = (TextView) v.findViewById(R.id.price);
            itemTotalPriceView.setText(String.format("%.2f", (item.count * item.getPrice())));
            return v;
        }
    }

    public void print(View v){
        View printBtn = findViewById(R.id.btnPrint);
        printBtn.setVisibility(View.GONE);

        View captureRelativeLayout = findViewById(R.id.whole);
        captureRelativeLayout.setDrawingCacheEnabled(true);
        Bitmap bitmap = captureRelativeLayout.getDrawingCache(true).copy(Bitmap.Config.ARGB_8888, false);
        captureRelativeLayout.destroyDrawingCache();

        printBtn.setVisibility(View.VISIBLE);

        PrintHelper photoPrinter = new PrintHelper(this);
        photoPrinter.setScaleMode(PrintHelper.SCALE_MODE_FIT);
        photoPrinter.printBitmap("billPrint", bitmap);
    }
}
