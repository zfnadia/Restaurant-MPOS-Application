package com.nadiaFerdoush.recieptgenerator;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class AppDatabase extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 4;
    public static final String DATABASE_NAME = "myapp12.db";

    private static AppDatabase instance = null; // = new AppDatabase()
    private final Context context;

    private static String PATH = Environment.getExternalStorageDirectory().getAbsolutePath() + "/";

    //singleton class
    public static AppDatabase getInstance(Context context) {
        if (instance == null) {
            instance = new AppDatabase(context);
        }
        return instance;
    }

    private AppDatabase() {
        this(null);
    }

    private AppDatabase(Context context) {
        super(context, PATH + DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    public void insertCategoryItems(Category category) {
        ContentValues values = new ContentValues();
        values.put("name", category.getName());
        values.put("available_time", category.getTime());

        this.getWritableDatabase().insert("item_category", null, values);
    }

    public List<Category> getCategoryItems() {
        List<Category> categoryItems = new ArrayList<>();
        Cursor cursor = getReadableDatabase().rawQuery("SELECT * FROM item_category ORDER BY name COLLATE NOCASE ASC", new String[]{});
        if (cursor.moveToFirst()) {
            do {

                String categoryName = cursor.getString(1);
                String categoryTime = cursor.getString(2);

                Category category = new Category(categoryName, categoryTime);
                category.id = cursor.getInt(0);
                categoryItems.add(category);

            } while (cursor.moveToNext());
        }

        return categoryItems;
    }

    public void insertItems(Item item, int id) {
        ContentValues values = new ContentValues();
        values.put("name", item.getName());
        values.put("price", item.getPrice());
        values.put("description", item.getDescription());
        values.put("item_category_id", id);

        this.getWritableDatabase().insert("item", null, values);
    }

    public List<Item> getItems(String searchText) {

        List<Item> items = new ArrayList<>();
        Cursor cursor = null;
        if (searchText == null || searchText.length() < 1)
            cursor = getReadableDatabase().rawQuery("SELECT * FROM item ORDER BY name COLLATE NOCASE ASC", new String[]{});
        else
            cursor = getReadableDatabase().rawQuery("SELECT * FROM item WHERE name LIKE ?", new String[]{"%" + searchText + "%"});

        if (cursor.moveToFirst()) {
            do {

                String itemName = cursor.getString(cursor.getColumnIndex("name"));
                float itemPrice = cursor.getFloat(cursor.getColumnIndex("price"));
                String itemDescription = cursor.getString(cursor.getColumnIndex("description"));

                Item item = new Item(itemName, itemPrice, itemDescription);
                item.id = cursor.getInt(cursor.getColumnIndex("id"));
                items.add(item);

            } while (cursor.moveToNext());
        }

        return items;
    }

    public List<Item> getBillItems(int billId) {

        List<Item> items = new ArrayList<>();

        Cursor cursor = getReadableDatabase().rawQuery("SELECT i.name, bi.rate, bi.quantity FROM bill_item bi JOIN item i ON bi.item_id = i.id WHERE bi.bill_id = ?", new String[]{String.valueOf(billId)});

        if (cursor.moveToFirst()) {
            do {

                String itemName = cursor.getString(0);
                float itemPrice = cursor.getFloat(1);

                Item item = new Item(itemName, itemPrice, "");
                item.count = cursor.getInt(2);
                items.add(item);

            } while (cursor.moveToNext());
        }

        return items;
    }

    public List<Bill> getBills() {

        List<Bill> bills = new ArrayList<>();

        Cursor cursor = getReadableDatabase().rawQuery("SELECT * FROM bill ORDER BY datetime(creation_time) DESC", null);

        if (cursor.moveToFirst()) {
            do {

                int id = cursor.getInt(cursor.getColumnIndex("id"));
                String creationTime = cursor.getString(cursor.getColumnIndex("creation_time"));
                float netAmount = cursor.getFloat(cursor.getColumnIndex("net_amount"));

                Bill bill = new Bill(0, 0, netAmount, 0, 0, 0, creationTime, 0, 0);
                bill.id = id;
                bills.add(bill);

            } while (cursor.moveToNext());
        }

        return bills;
    }

    public Bill getBill(int billId) {

        Bill bill = null;

        Cursor cursor = getReadableDatabase().rawQuery("SELECT * FROM bill WHERE id = ?", new String[]{String.valueOf(billId)});

        if (cursor.moveToFirst()) {

            float grossAmount = cursor.getFloat(cursor.getColumnIndex("gross_amount"));
            float vatPt = cursor.getFloat(cursor.getColumnIndex("vat_pt"));
            float discountPt = cursor.getFloat(cursor.getColumnIndex("discount_pt"));
            float netAmount = cursor.getFloat(cursor.getColumnIndex("net_amount"));
            float paidAmount = cursor.getFloat(cursor.getColumnIndex("paid_amount"));
            float changeAmount = cursor.getFloat(cursor.getColumnIndex("change_amount"));
            String creationTime = cursor.getString(cursor.getColumnIndex("creation_time"));
            int tableNumber = cursor.getInt(cursor.getColumnIndex("table_number"));
            int paymentMethod = cursor.getInt(cursor.getColumnIndex("payment_method"));


            bill = new Bill(grossAmount, paidAmount, netAmount, changeAmount, vatPt, discountPt, creationTime, tableNumber, paymentMethod);

        }

        return bill;
    }


    public void insertEmployee(Employee employee) {
        ContentValues values = new ContentValues();
        values.put("name", employee.getName());
        values.put("birth_date", employee.getBirthDate());
        values.put("address", employee.getAddress());
        values.put("email", employee.getEmail());
        values.put("password", employee.getPassword());
        values.put("phone_number", employee.getPhone());
        values.put("salary", employee.getSalary());
        values.put("type", employee.getType());

        this.getWritableDatabase().insert("employee", null, values);
    }

    public List<Employee> getEmployees() {
        List<Employee> employees = new ArrayList<>();
        Cursor cursor = getReadableDatabase().rawQuery("SELECT * FROM employee ORDER BY name COLLATE NOCASE ASC", new String[]{});
        if (cursor.moveToFirst()) {
            do {

                String name = cursor.getString(cursor.getColumnIndex("name"));
                String birthDate = cursor.getString(cursor.getColumnIndex("birth_date"));
                String address = cursor.getString(cursor.getColumnIndex("address"));
                String email = cursor.getString(cursor.getColumnIndex("email"));
                String password = cursor.getString(cursor.getColumnIndex("password"));
                String phoneNumber = cursor.getString(cursor.getColumnIndex("phone_number"));
                int salary = cursor.getInt(cursor.getColumnIndex("salary"));
                int type = cursor.getInt(cursor.getColumnIndex("type"));

                Employee employee = new Employee(name, birthDate, address, email, password, phoneNumber, salary, type);
                employee.id = cursor.getInt(cursor.getColumnIndex("id"));
                employees.add(employee);

            } while (cursor.moveToNext());
        }

        return employees;
    }

    public long insertInBill(Bill bill) {
        ContentValues values = new ContentValues();
        values.put("gross_amount", bill.getGrossAmount());
        values.put("paid_amount", bill.getPaidAmount());
        values.put("net_amount", bill.getNetAmount());
        values.put("change_amount", bill.getChangeAmount());
        values.put("vat_pt", bill.getVatPt());
        values.put("discount_pt", bill.getDiscountPt());
        values.put("creation_time", bill.getTimeCreated());
        values.put("table_number", bill.getTableNumber());
        values.put("payment_method", bill.getPaymentType());

        return this.getWritableDatabase().insert("bill", null, values);
    }

    public void insertBillItem(int billId, Item item) {
        ContentValues values = new ContentValues();
        values.put("bill_id", billId);
        values.put("item_id", item.id);
        values.put("rate", item.getPrice());
        values.put("quantity", item.count);

        this.getWritableDatabase().insert("bill_item", null, values);

    }


    public void onCreate(SQLiteDatabase db) {
        try {
            Log.e("LL", "here");
            db.execSQL(readFromAssets(this.context, "sql/item_category.ddl"));
            db.execSQL(readFromAssets(this.context, "sql/item.ddl"));
            db.execSQL(readFromAssets(this.context, "sql/availability.ddl"));
            db.execSQL(readFromAssets(this.context, "sql/employee.ddl"));
            db.execSQL(readFromAssets(this.context, "sql/bill.ddl"));
            db.execSQL(readFromAssets(this.context, "sql/bill_item.ddl"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //db.execSQL("");
        //onCreate(db);
    }

    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // onUpgrade(db, oldVersion, newVersion);
    }

    public static String readFromAssets(Context context, String filename) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(context.getAssets().open(filename)));

        // do reading, usually loop until end of file reading
        StringBuilder sb = new StringBuilder();
        String mLine = reader.readLine();
        while (mLine != null) {
            sb.append(mLine); // process line
            mLine = reader.readLine();
        }
        reader.close();
        return sb.toString();
    }
}