package com.androidfragmant.tourxyz.banglatourism.train;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import com.androidfragmant.tourxyz.banglatourism.BuildConfig;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

/**
 * Created by Ripon on 8/18/16.
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    private static String DB_NAME = "bdrailway3.db";
    private static int DB_VERSION = 1;
    public static final String DAY_OFF = "day_off";
    private static String DB_PATH = null;
    public static final String DTRAIN_PRICE = "train_price";
    public static final String TRAINNAME = "trainName";
    public static final String TRAIN_NAME = "train_name";
    public static final String TRAIN_TIME = "train_time";

    private Context context;
    private SQLiteDatabase database;

    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);

        this.context = context;
        String packagename = context.getPackageName();
        DB_PATH = "/data/data/" + packagename + "/databases/";
        this.database = openDatabase();
    }

    public SQLiteDatabase openDatabase() {
        String path = DB_PATH + DB_NAME;
        if (database == null) {
            createDatabase();
            database = SQLiteDatabase.openDatabase(path, null,
                    SQLiteDatabase.OPEN_READWRITE);
        }
        return database;
    }

    public void createDatabase() {
        boolean exists = checkDatabase();
        if (!exists) {
            this.getReadableDatabase();
            Toast.makeText(context, "Copying database...", Toast.LENGTH_LONG).show();
            copyDatabase();
        } else {
            Toast.makeText(context, "Database already exists", Toast.LENGTH_LONG).show();
        }
    }

    public boolean checkDatabase() {
        String path = DB_PATH + DB_NAME;
        File file = new File(path);
        return file.exists();
    }

    private void copyDatabase() {
        try {
            InputStream is = context.getAssets().open("data/bdrailway3.db");
            String path = DB_PATH + DB_NAME;
            OutputStream os = new FileOutputStream(path);
            byte[] buffer = new byte[4096];
            int readCount = 0;
            readCount = is.read(buffer);
            while (readCount > 0) {
                os.write(buffer, 0, readCount);
                readCount = is.read(buffer);

            }
            is.close();
            os.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void close() {
        if (this.database != null) {
            this.database.close();
        }
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public ArrayList<Train> getTrainLocation() {
        ArrayList<Train> tracking = new ArrayList<>();
        Cursor cursor = this.database.rawQuery("Select  train_name,name,t_from,t_to,current_station,delay_hour,delay_min,time,note,t_id   from \n(SELECT  tracking_live.id as t_id,train_name,trainname_id,tracking_live.train_from as t_from,tracking_live.train_to as t_to,current_station,delay_hour,delay_min,time,note  FROM tracking_live  inner join  train_time  on train_time.train_no=tracking_live.train_name) as ttable   inner join  trainName  on trainName.id=ttable.trainname_id   group  by  train_name order by  t_id  DESC", new String[0]);
        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            for (int i = 0; i < cursor.getCount(); i++) {
                tracking.add(new Train(cursor.getString(cursor.getColumnIndex(DatabaseHelper.TRAIN_NAME)), cursor.getString(cursor.getColumnIndex("name")), cursor.getString(cursor.getColumnIndex("t_from")), cursor.getString(cursor.getColumnIndex("t_to")), cursor.getString(cursor.getColumnIndex("current_station")), cursor.getString(cursor.getColumnIndex("delay_hour")), cursor.getString(cursor.getColumnIndex("delay_min")), cursor.getString(cursor.getColumnIndex("time")), cursor.getString(cursor.getColumnIndex("note")), cursor.getString(cursor.getColumnIndex("t_id"))));
                cursor.moveToNext();
            }
        }
        cursor.close();
        return tracking;
    }

    public ArrayList<Train> searchTraintime(String t_from, String t_to) {
        ArrayList<Train> traintime = new ArrayList<>();
        String q = BuildConfig.FLAVOR;
        if (t_from == null && t_to == null) {
            q = "select tname,train_from,train_to,train_no,start_time,end_time,day_off.day_name as dayoff  from day_off  inner join (select TrainName.name as tname ,train_from,train_to,train_no,start_time,end_time,offday_id  from train_time inner join trainName on train_time.trainname_id=trainName.id ) as ttrain  on ttrain.offday_id=day_off.id  order by tname";
        } else if (t_to == null && t_from == t_from) {
            q = "select tname,train_from,train_to,train_no,start_time,end_time,day_off.day_name as dayoff  from day_off  inner join (select TrainName.name as tname ,train_from,train_to,train_no,start_time,end_time,offday_id  from train_time inner join trainName on train_time.trainname_id=trainName.id ) as ttrain  on ttrain.offday_id=day_off.id  where   train_from='" + t_from + "'    order by tname";
        } else if (t_to == t_to && t_from == null) {
            q = "select tname,train_from,train_to,train_no,start_time,end_time,day_off.day_name as dayoff  from day_off  inner join (select TrainName.name as tname ,train_from,train_to,train_no,start_time,end_time,offday_id  from train_time inner join trainName on train_time.trainname_id=trainName.id ) as ttrain  on ttrain.offday_id=day_off.id  where train_to='" + t_to + "'   order by tname";
        } else {
            q = "select tname,train_from,train_to,train_no,start_time,end_time,day_off.day_name as dayoff  from day_off  inner join (select TrainName.name as tname ,train_from,train_to,train_no,start_time,end_time,offday_id  from train_time inner join trainName on train_time.trainname_id=trainName.id ) as ttrain  on ttrain.offday_id=day_off.id  where train_from='" + t_from + "' and  train_to='" + t_to + "' order by tname";
        }
        Cursor cursor = this.database.rawQuery(q, new String[0]);
        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            for (int i = 0; i < cursor.getCount(); i++) {
                traintime.add(new Train(cursor.getString(cursor.getColumnIndex("tname")), cursor.getString(cursor.getColumnIndex("train_from")), cursor.getString(cursor.getColumnIndex("train_to")), cursor.getString(cursor.getColumnIndex("start_time")), cursor.getString(cursor.getColumnIndex("end_time")), cursor.getString(cursor.getColumnIndex("dayoff")), cursor.getString(cursor.getColumnIndex("train_no"))));
                cursor.moveToNext();
            }
        }
        cursor.close();
        return traintime;
    }

    public ArrayList<Train> searchTrain() {
        ArrayList<Train> traintime = new ArrayList<>();
        String str = BuildConfig.FLAVOR;
        Cursor cursor = this.database.rawQuery("select TrainName.name as tname ,train_from,train_to,code_number  from train_name inner join trainName on train_name.tid=trainName.id group by code_number order by tname", new String[0]);
        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            for (int i = 0; i < cursor.getCount(); i++) {
                traintime.add(new Train(cursor.getString(cursor.getColumnIndex("tname")), cursor.getString(cursor.getColumnIndex("train_from")), cursor.getString(cursor.getColumnIndex("train_to")), cursor.getString(cursor.getColumnIndex("code_number"))));
                cursor.moveToNext();
            }
        }
        cursor.close();
        return traintime;
    }

    public ArrayList<Train> searchTrain1(String key) {
        ArrayList<Train> traintime = new ArrayList<>();
        String q = BuildConfig.FLAVOR;
        Cursor cursor = this.database.rawQuery("Select  name,train_from,train_to,start_time,end_time,day_name  from (select name,english_name,train_from,train_to,start_time,end_time,offday_id  from  train_time  inner join trainName on train_time.trainname_id=trainName.id) as ttable   inner join  day_off on  ttable.offday_id=day_off.id  where name like '%" + key + "%' or english_name like '%" + key + "%'  order by name ", new String[0]);
        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            for (int i = 0; i < cursor.getCount(); i++) {
                traintime.add(new Train(cursor.getString(cursor.getColumnIndex("name")), cursor.getString(cursor.getColumnIndex("train_from")), cursor.getString(cursor.getColumnIndex("train_to")), cursor.getString(cursor.getColumnIndex("start_time")), cursor.getString(cursor.getColumnIndex("end_time")), cursor.getString(cursor.getColumnIndex("day_name"))));
                cursor.moveToNext();
            }
        }
        cursor.close();
        return traintime;
    }

    public ArrayList<Train> searchTrainKey(String key) {
        ArrayList<Train> traintime = new ArrayList<>();
        String q = BuildConfig.FLAVOR;
        Cursor cursor = this.database.rawQuery("select TrainName.name as tname ,train_from,train_to,code_number  from train_name inner join trainName on train_name.tid=trainName.id  where tname like  '%" + key + "%' or english_name  like  '%" + key + "%' group by code_number order by tname", new String[0]);
        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            for (int i = 0; i < cursor.getCount(); i++) {
                traintime.add(new Train(cursor.getString(cursor.getColumnIndex("tname")), cursor.getString(cursor.getColumnIndex("train_from")), cursor.getString(cursor.getColumnIndex("train_to")), cursor.getString(cursor.getColumnIndex("code_number"))));
                cursor.moveToNext();
            }
        }
        cursor.close();
        return traintime;
    }

    public ArrayList<PriceEvant> getTrainPrice() {
        ArrayList<PriceEvant> trainprice = new ArrayList<>();
        Cursor cursor = this.database.rawQuery("select * from train_price", new String[0]);
        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            for (int i = 0; i < cursor.getCount(); i++) {
                trainprice.add(new PriceEvant(cursor.getString(cursor.getColumnIndex("distance")), cursor.getString(cursor.getColumnIndex("second_sadaron")), cursor.getString(cursor.getColumnIndex("second_mail")), cursor.getString(cursor.getColumnIndex("comiutar")), cursor.getString(cursor.getColumnIndex("sulov")), cursor.getString(cursor.getColumnIndex("shuvon")), cursor.getString(cursor.getColumnIndex("shuvon_chair")), cursor.getString(cursor.getColumnIndex("first_chair")), cursor.getString(cursor.getColumnIndex("first_berth")), cursor.getString(cursor.getColumnIndex("snigdha")), cursor.getString(cursor.getColumnIndex("ac_seat")), cursor.getString(cursor.getColumnIndex("ac_berth")), cursor.getString(cursor.getColumnIndex("train_from")), cursor.getString(cursor.getColumnIndex("train_to"))));
                cursor.moveToNext();
            }
        }
        cursor.close();
        return trainprice;
    }

    public ArrayList<PriceEvant> searchTrainPriceFrom(String key, String key1) {
        ArrayList<PriceEvant> trainprice = new ArrayList<>();
        String[] strArr = new String[0];
        Cursor cursor = this.database.rawQuery("select * from train_price where train_from like '%" + key + "%' and train_to like '%" + key1 + "%'", strArr);
        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            for (int i = 0; i < cursor.getCount(); i++) {
                trainprice.add(new PriceEvant(cursor.getString(cursor.getColumnIndex("distance")), cursor.getString(cursor.getColumnIndex("second_sadaron")), cursor.getString(cursor.getColumnIndex("second_mail")), cursor.getString(cursor.getColumnIndex("comiutar")), cursor.getString(cursor.getColumnIndex("sulov")), cursor.getString(cursor.getColumnIndex("shuvon")), cursor.getString(cursor.getColumnIndex("shuvon_chair")), cursor.getString(cursor.getColumnIndex("first_chair")), cursor.getString(cursor.getColumnIndex("first_berth")), cursor.getString(cursor.getColumnIndex("snigdha")), cursor.getString(cursor.getColumnIndex("ac_seat")), cursor.getString(cursor.getColumnIndex("ac_berth")), cursor.getString(cursor.getColumnIndex("train_from")), cursor.getString(cursor.getColumnIndex("train_to"))));
                cursor.moveToNext();
            }
        }
        cursor.close();
        return trainprice;
    }

    public ArrayList<IdName> searchPriceFrom() {
        ArrayList<IdName> trainprice = new ArrayList<>();
        Cursor cursor = this.database.rawQuery("select train_from  from train_price group by train_from ", new String[0]);
        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            for (int i = 0; i < cursor.getCount(); i++) {
                trainprice.add(new IdName(cursor.getString(cursor.getColumnIndex("train_from"))));
                cursor.moveToNext();
            }
        }
        cursor.close();
        return trainprice;
    }

    public ArrayList<IdName> searchPriceFromKey(String key) {
        ArrayList<IdName> trainprice = new ArrayList<>();
        Cursor cursor = this.database.rawQuery("select train_from  from train_price  where train_from like '%" + key + "%' or train_from_english  like '%" + key + "%'group  by train_from  ", new String[0]);
        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            for (int i = 0; i < cursor.getCount(); i++) {
                trainprice.add(new IdName(cursor.getString(cursor.getColumnIndex("train_from"))));
                cursor.moveToNext();
            }
        }
        cursor.close();
        return trainprice;
    }

    public ArrayList<IdName> searchPriceTo() {
        ArrayList<IdName> trainprice = new ArrayList<>();
        Cursor cursor = this.database.rawQuery("select train_to  from train_price group by train_to ", new String[0]);
        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            for (int i = 0; i < cursor.getCount(); i++) {
                trainprice.add(new IdName(cursor.getString(cursor.getColumnIndex("train_to"))));
                cursor.moveToNext();
            }
        }
        cursor.close();
        return trainprice;
    }

    public ArrayList<IdName> searchPriceTokey(String key) {
        ArrayList<IdName> trainprice = new ArrayList<>();
        Cursor cursor = this.database.rawQuery("select train_to  from train_price  where train_to like '%" + key + "%' or train_to_english  like '%" + key + "%'group  by train_to ", new String[0]);
        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            for (int i = 0; i < cursor.getCount(); i++) {
                trainprice.add(new IdName(cursor.getString(cursor.getColumnIndex("train_to"))));
                cursor.moveToNext();
            }
        }
        cursor.close();
        return trainprice;
    }

    public ArrayList<PriceEvant> searchTrainPriceTo(String key, String key1) {
        ArrayList<PriceEvant> trainprice = new ArrayList<>();
        String q = BuildConfig.FLAVOR;
        if (key == null || key.isEmpty() || key1 == null || key1.isEmpty()) {
            q = "select * from train_price where train_from = '" + key + "' or train_to = '" + key1 + "'";
        } else {
            q = "select * from train_price where train_from = '" + key + "' and train_to = '" + key1 + "'";
        }
        Cursor cursor = this.database.rawQuery(q, new String[0]);
        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            for (int i = 0; i < cursor.getCount(); i++) {
                trainprice.add(new PriceEvant(cursor.getString(cursor.getColumnIndex("distance")), cursor.getString(cursor.getColumnIndex("second_sadaron")), cursor.getString(cursor.getColumnIndex("second_mail")), cursor.getString(cursor.getColumnIndex("comiutar")), cursor.getString(cursor.getColumnIndex("sulov")), cursor.getString(cursor.getColumnIndex("shuvon")), cursor.getString(cursor.getColumnIndex("shuvon_chair")), cursor.getString(cursor.getColumnIndex("first_chair")), cursor.getString(cursor.getColumnIndex("first_berth")), cursor.getString(cursor.getColumnIndex("snigdha")), cursor.getString(cursor.getColumnIndex("ac_seat")), cursor.getString(cursor.getColumnIndex("ac_berth")), cursor.getString(cursor.getColumnIndex("train_from")), cursor.getString(cursor.getColumnIndex("train_to"))));
                cursor.moveToNext();
            }
        }
        cursor.close();
        return trainprice;
    }

    public ArrayList<IdName> searchArea() {
        ArrayList<IdName> traintime = new ArrayList<>();
        Cursor cursor = this.database.rawQuery("select id,name from train_area  order by name", new String[0]);
        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            for (int i = 0; i < cursor.getCount(); i++) {
                traintime.add(new IdName(cursor.getString(cursor.getColumnIndex("id")), cursor.getString(cursor.getColumnIndex("name"))));
                cursor.moveToNext();
            }
        }
        cursor.close();
        return traintime;
    }

    public ArrayList<Train> searchAreaDetails(String key) {
        ArrayList<Train> traintime = new ArrayList<>();
        Cursor cursor = this.database.rawQuery("select  name,train_from,train_to,start_time,end_time,day_name,area_id  from day_off  inner join  (SELECT * FROM  train_time  inner join trainName on train_time.trainname_id=trainName.id ) as ttrain  on ttrain.offday_id=day_off.id  where area_id='" + key + "' order by name ", new String[0]);
        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            for (int i = 0; i < cursor.getCount(); i++) {
                traintime.add(new Train(cursor.getString(cursor.getColumnIndex("name")), cursor.getString(cursor.getColumnIndex("train_from")), cursor.getString(cursor.getColumnIndex("train_to")), cursor.getString(cursor.getColumnIndex("start_time")), cursor.getString(cursor.getColumnIndex("end_time")), cursor.getString(cursor.getColumnIndex("day_name")), cursor.getString(cursor.getColumnIndex("area_id"))));
                cursor.moveToNext();
            }
        }
        cursor.close();
        return traintime;
    }

    public ArrayList<IdName> getFrom(String pos) {
        ArrayList<IdName> names = new ArrayList<>();
        String q = BuildConfig.FLAVOR;
        if (pos == null) {
            q = "SELECT   trainname_id,train_from  FROM  train_time  group  BY train_from";
        } else {
            q = "SELECT   trainname_id,train_from  FROM  train_time where  train_to='" + pos + "' group  BY train_from";
        }
        Cursor cursor = this.database.rawQuery(q, new String[0]);
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            for (int i = 0; i < cursor.getCount(); i++) {
                names.add(new IdName(cursor.getString(cursor.getColumnIndex("trainname_id")), cursor.getString(cursor.getColumnIndex("train_from"))));
                cursor.moveToNext();
            }
        }
        cursor.close();
        return names;
    }

    public ArrayList<IdName> getFromKey(String key) {
        ArrayList<IdName> names = new ArrayList<>();
        Cursor cursor = this.database.rawQuery("SELECT   train_from  FROM  train_time where train_from like '%" + key + "%' or train_from_english like '%" + key + "%'group  BY train_from ", new String[0]);
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            for (int i = 0; i < cursor.getCount(); i++) {
                names.add(new IdName(cursor.getString(cursor.getColumnIndex("train_from"))));
                cursor.moveToNext();
            }
        }
        cursor.close();
        return names;
    }

    public ArrayList<IdName> getTo(String pos) {
        ArrayList<IdName> names = new ArrayList<>();
        String q = BuildConfig.FLAVOR;
        if (pos == null) {
            q = "SELECT   trainname_id,train_to  FROM  train_time  group  BY train_to";
        } else {
            q = "SELECT   trainname_id,train_to  FROM  train_time where  train_from='" + pos + "' group  BY train_to";
        }
        Cursor cursor = this.database.rawQuery(q, new String[0]);
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            for (int i = 0; i < cursor.getCount(); i++) {
                names.add(new IdName(cursor.getString(cursor.getColumnIndex("trainname_id")), cursor.getString(cursor.getColumnIndex("train_to"))));
                cursor.moveToNext();
            }
        }
        cursor.close();
        return names;
    }

    public ArrayList<IdName> getToKey(String key) {
        ArrayList<IdName> names = new ArrayList<>();
        Cursor cursor = this.database.rawQuery("SELECT   train_to  FROM  train_time where train_to like '%" + key + "%' or train_to_english like '%" + key + "%'group  BY train_to ", new String[0]);
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            for (int i = 0; i < cursor.getCount(); i++) {
                names.add(new IdName(cursor.getString(cursor.getColumnIndex("train_to"))));
                cursor.moveToNext();
            }
        }
        cursor.close();
        return names;
    }

    public ArrayList<PriceEvant> getTrainPrice1(String key) {
        ArrayList<PriceEvant> trainprice = new ArrayList<>();
        String[] strArr = new String[0];
        Cursor cursor = this.database.rawQuery("SELECT  id,distance,name,train_from,train_to,shuvon,shuvon_chair,snigdha,first_chair,first_berth,ac_seat,ac_berth,kumutur  FROM  train_price1 inner join trainName on trainName.id=train_price1.train_id  where id='" + key + "' order by train_from", strArr);
        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            for (int i = 0; i < cursor.getCount(); i++) {
                String id = cursor.getString(cursor.getColumnIndex("id"));
                String distance = cursor.getString(cursor.getColumnIndex("distance"));
                String name = cursor.getString(cursor.getColumnIndex("name"));
                String from = cursor.getString(cursor.getColumnIndex("train_from"));
                String to = cursor.getString(cursor.getColumnIndex("train_to"));
                String shuvon = cursor.getString(cursor.getColumnIndex("shuvon"));
                String shuvon_chair = cursor.getString(cursor.getColumnIndex("shuvon_chair"));
                String snigdha = cursor.getString(cursor.getColumnIndex("snigdha"));
                String first_chair = cursor.getString(cursor.getColumnIndex("first_chair"));
                String first_berth = cursor.getString(cursor.getColumnIndex("first_berth"));
                String kumutur = cursor.getString(cursor.getColumnIndex("kumutur"));
                trainprice.add(new PriceEvant(id, distance, name, from, to, shuvon, shuvon_chair, snigdha, first_chair, first_berth, cursor.getString(cursor.getColumnIndex("ac_seat")), cursor.getString(cursor.getColumnIndex("ac_berth")), kumutur));
                cursor.moveToNext();
            }
        }
        cursor.close();
        return trainprice;
    }

    public ArrayList<PriceEvant> getTrainPrice1From(String key, String key1, String key2) {
        ArrayList<PriceEvant> trainprice = new ArrayList<>();
        String[] strArr = new String[0];
        Cursor cursor = this.database.rawQuery("SELECT  id,distance,name,train_from,train_to,shuvon,shuvon_chair,snigdha,first_chair,first_berth,ac_seat,ac_berth,kumutur  FROM  train_price1 inner join trainName on trainName.id=train_price1.train_id  where id='" + key + "' and train_from like '" + key1 + "%' and train_to like '" + key2 + "%' order by train_from", strArr);
        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            for (int i = 0; i < cursor.getCount(); i++) {
                String id = cursor.getString(cursor.getColumnIndex("id"));
                String distance = cursor.getString(cursor.getColumnIndex("distance"));
                String name = cursor.getString(cursor.getColumnIndex("name"));
                String from = cursor.getString(cursor.getColumnIndex("train_from"));
                String to = cursor.getString(cursor.getColumnIndex("train_to"));
                String shuvon = cursor.getString(cursor.getColumnIndex("shuvon"));
                String shuvon_chair = cursor.getString(cursor.getColumnIndex("shuvon_chair"));
                String snigdha = cursor.getString(cursor.getColumnIndex("snigdha"));
                String first_chair = cursor.getString(cursor.getColumnIndex("first_chair"));
                String first_berth = cursor.getString(cursor.getColumnIndex("first_berth"));
                String kumutur = cursor.getString(cursor.getColumnIndex("kumutur"));
                trainprice.add(new PriceEvant(id, distance, name, from, to, shuvon, shuvon_chair, snigdha, first_chair, first_berth, cursor.getString(cursor.getColumnIndex("ac_seat")), cursor.getString(cursor.getColumnIndex("ac_berth")), kumutur));
                cursor.moveToNext();
            }
        }
        cursor.close();
        return trainprice;
    }

    public ArrayList<PriceEvant> getTrainPrice1To(String key, String key1, String key2) {
        ArrayList<PriceEvant> trainprice = new ArrayList<>();
        String q = BuildConfig.FLAVOR;
        if (key1 != null && !key1.isEmpty() && key2 != null && !key2.isEmpty()) {
            q = "SELECT  id,distance,name,train_from,train_to,shuvon,shuvon_chair,snigdha,first_chair,first_berth,ac_seat,ac_berth,kumutur  FROM  train_price1 inner join trainName on trainName.id=train_price1.train_id  where id='" + key + "'  and train_from like '" + key1 + "%' and train_to like '" + key2 + "%' order by train_to";
        } else if (key1 != null && !key1.isEmpty()) {
            q = "SELECT  id,distance,name,train_from,train_to,shuvon,shuvon_chair,snigdha,first_chair,first_berth,ac_seat,ac_berth,kumutur  FROM  train_price1 inner join trainName on trainName.id=train_price1.train_id  where id='" + key + "'  and train_from like '" + key1 + "%'  order by train_from";
        } else if (key2 == null || key2.isEmpty()) {
            q = "SELECT  id,distance,name,train_from,train_to,shuvon,shuvon_chair,snigdha,first_chair,first_berth,ac_seat,ac_berth,kumutur  FROM  train_price1 inner join trainName on trainName.id=train_price1.train_id  where id='" + key + "'  and train_from like '" + key1 + "%' or train_to like '" + key2 + "%' order by train_to";
        } else {
            q = "SELECT  id,distance,name,train_from,train_to,shuvon,shuvon_chair,snigdha,first_chair,first_berth,ac_seat,ac_berth,kumutur  FROM  train_price1 inner join trainName on trainName.id=train_price1.train_id  where id='" + key + "'  and train_to like '" + key2 + "%' order by train_to";
        }
        Cursor cursor = this.database.rawQuery(q, new String[0]);
        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            for (int i = 0; i < cursor.getCount(); i++) {
                String id = cursor.getString(cursor.getColumnIndex("id"));
                String distance = cursor.getString(cursor.getColumnIndex("distance"));
                String name = cursor.getString(cursor.getColumnIndex("name"));
                String from = cursor.getString(cursor.getColumnIndex("train_from"));
                String to = cursor.getString(cursor.getColumnIndex("train_to"));
                String shuvon = cursor.getString(cursor.getColumnIndex("shuvon"));
                String shuvon_chair = cursor.getString(cursor.getColumnIndex("shuvon_chair"));
                String snigdha = cursor.getString(cursor.getColumnIndex("snigdha"));
                String first_chair = cursor.getString(cursor.getColumnIndex("first_chair"));
                String first_berth = cursor.getString(cursor.getColumnIndex("first_berth"));
                String kumutur = cursor.getString(cursor.getColumnIndex("kumutur"));
                trainprice.add(new PriceEvant(id, distance, name, from, to, shuvon, shuvon_chair, snigdha, first_chair, first_berth, cursor.getString(cursor.getColumnIndex("ac_seat")), cursor.getString(cursor.getColumnIndex("ac_berth")), kumutur));
                cursor.moveToNext();
            }
        }
        cursor.close();
        return trainprice;
    }

    public ArrayList<IdName> searchPrice1From(String key) {
        ArrayList<IdName> trainprice = new ArrayList<>();
        Cursor cursor = this.database.rawQuery("select train_from  from train_price1  where train_id='" + key + "' group by train_from ", new String[0]);
        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            for (int i = 0; i < cursor.getCount(); i++) {
                trainprice.add(new IdName(cursor.getString(cursor.getColumnIndex("train_from"))));
                cursor.moveToNext();
            }
        }
        cursor.close();
        return trainprice;
    }

    public ArrayList<IdName> searchPrice1FromKey(String key, String key1) {
        ArrayList<IdName> trainprice = new ArrayList<>();
        Cursor cursor = this.database.rawQuery("select train_from  from train_price1  where train_id='" + key + "'  and train_from like '%" + key1 + "%' or train_from_english like '%" + key1 + "%' group by train_from ", new String[0]);
        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            for (int i = 0; i < cursor.getCount(); i++) {
                trainprice.add(new IdName(cursor.getString(cursor.getColumnIndex("train_from"))));
                cursor.moveToNext();
            }
        }
        cursor.close();
        return trainprice;
    }

    public ArrayList<IdName> searchPrice1To(String key) {
        ArrayList<IdName> trainprice = new ArrayList<>();
        Cursor cursor = this.database.rawQuery("select train_to  from train_price1 where train_id='" + key + "'  group by train_to ", new String[0]);
        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            for (int i = 0; i < cursor.getCount(); i++) {
                trainprice.add(new IdName(cursor.getString(cursor.getColumnIndex("train_to"))));
                cursor.moveToNext();
            }
        }
        cursor.close();
        return trainprice;
    }

    public ArrayList<IdName> searchPrice1ToKey(String key, String key1) {
        ArrayList<IdName> trainprice = new ArrayList<>();
        Cursor cursor = this.database.rawQuery("select train_to  from train_price1 where train_id='" + key + "' and train_to like '%" + key1 + "%' or train_to_english like '%" + key1 + "%'  group by train_to ", new String[0]);
        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            for (int i = 0; i < cursor.getCount(); i++) {
                trainprice.add(new IdName(cursor.getString(cursor.getColumnIndex("train_to"))));
                cursor.moveToNext();
            }
        }
        cursor.close();
        return trainprice;
    }

    public ArrayList<PriceEvant> getTrainPrice11() {
        ArrayList<PriceEvant> trainprice = new ArrayList<>();
        Cursor cursor = this.database.rawQuery("SELECT  id,name  FROM  train_price1 inner join trainName on trainName.id=train_price1.train_id group by name", new String[0]);
        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            for (int i = 0; i < cursor.getCount(); i++) {
                trainprice.add(new PriceEvant(cursor.getString(cursor.getColumnIndex("id")), cursor.getString(cursor.getColumnIndex("name"))));
                cursor.moveToNext();
            }
        }
        cursor.close();
        return trainprice;
    }

    public ArrayList<Train> presentStation(String key) {
        ArrayList<Train> trainprice = new ArrayList<>();
        Cursor cursor = this.database.rawQuery("select train_from  from train_time where train_no=" + key + " group by train_from ", new String[0]);
        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            for (int i = 0; i < cursor.getCount(); i++) {
                trainprice.add(new Train(cursor.getString(cursor.getColumnIndex("train_from"))));
                cursor.moveToNext();
            }
        }
        cursor.close();
        return trainprice;
    }

    public ArrayList<Train> searchPresentStation(String key1, String key) {
        ArrayList<Train> trainprice = new ArrayList<>();
        Cursor cursor = this.database.rawQuery("select train_from  from train_time   where train_no=" + key1 + " and train_from  like '%" + key + "%'  or  train_from_english  like  '%" + key + "%' group by train_from ", new String[0]);
        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            for (int i = 0; i < cursor.getCount(); i++) {
                trainprice.add(new Train(cursor.getString(cursor.getColumnIndex("train_from"))));
                cursor.moveToNext();
            }
        }
        cursor.close();
        return trainprice;
    }

}
