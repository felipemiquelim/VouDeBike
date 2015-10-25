package br.com.puc.sqlite.br.com.puc.sqlite.crud;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.LinkedList;
import java.util.List;

import br.com.puc.sqlite.ciclista;

/**
 * Created by Felipe on 19/10/2015.
 *
 * CRUD operations (create "add", read "get", update, delete) ciclista + get all ciclista + delete all ciclista
 *
 */
public class TB_CICLISTA {
    // DB Name
    private SQLiteDatabase db;
    // TB_CICLISTA table name
    private static final String Table_CICLISTA = "TB_CICLISTA";

    // TB_CICLISTA Table Columns names
    private static final String KEY_NCICLI= "NCICLI";
    private static final String KEY_CNOME = "CNOME";
    private static final String KEY_CEMAIL = "CEMAIL";
    private static final String KEY_CCELULAR = "CCELULAR";
    private static final String KEY_CSTATUS = "CSTATUS";

    private static final String[] COLUMNS = {KEY_NCICLI,KEY_CNOME,KEY_CEMAIL,KEY_CCELULAR,KEY_CSTATUS};

    /*****************************************************************************************************
    * inicio da classe
     * **************************************************************************************************/

    public TB_CICLISTA() {
        // get reference to writable DB
        //this.db = db;
    }

    public void add_ciclista(ciclista ciclista,SQLiteDatabase db ){
        //for logging
        Log.d("add_ciclista", ciclista.toString());

        // 1. get reference to writable DB
        //Comentado pois agora vem por parametro
        //SQLiteDatabase db = this.getWritableDatabase();

        // 2. create ContentValues to add key "column"/value
        ContentValues values = new ContentValues();
        values.put(KEY_CNOME, ciclista.getCNOME());
        values.put(KEY_CEMAIL, ciclista.getCEMAIL());
        values.put(KEY_CCELULAR, ciclista.getCCELULAR());
        values.put(KEY_CSTATUS, ciclista.isCSTATUS());

        // 3. insert
        db.insert(Table_CICLISTA, // table
                null, //nullColumnHack
                values); // key/value -> keys = column names/ values = column values

        // 4. close
        db.close();
    }

    public ciclista get_ciclista(int id, SQLiteDatabase db){

        // 1. get reference to readable DB
        //Comentado pois agora vem por parametro
        //SQLiteDatabase db = this.getReadableDatabase();

        // 2. build query
        Cursor cursor =
                db.query(Table_CICLISTA, // a. table
                        COLUMNS, // b. column names
                        " id = ?", // c. selections
                        new String[] { String.valueOf(id) }, // d. selections args
                        null, // e. group by
                        null, // f. having
                        null, // g. order by
                        null); // h. limit

        // 3. if we got results get the first one
        if (cursor != null)
            cursor.moveToFirst();

        // 4. build ciclista object
        ciclista ciclista = new ciclista();
        ciclista.setNCICLI(Integer.parseInt(cursor.getString(0)));
        ciclista.setCNOME(cursor.getString(1));
        ciclista.setCEMAIL(cursor.getString(2));
        ciclista.setCCELULAR(Integer.parseInt(cursor.getString(3)));
        ciclista.setCSTATUS(Boolean.parseBoolean(cursor.getString(4)));

        //log
        Log.d("get_ciclista(" + id + ")", ciclista.toString());

        // 5. return ciclista
        return ciclista;
    }

    public List<ciclista> getAllciclistas(SQLiteDatabase db) {
        List<ciclista> ciclistas = new LinkedList<ciclista>();

        // 1. build the query
        String query = "SELECT  * FROM " + Table_CICLISTA;

        // 2. get reference to writable DB
        //SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        // 3. go over each row, build ciclista and add it to list
        ciclista ciclista = null;
        if (cursor.moveToFirst()) {
            do {
                ciclista = new ciclista();
                ciclista.setNCICLI(Integer.parseInt(cursor.getString(0)));
                ciclista.setCNOME(cursor.getString(1));
                ciclista.setCEMAIL(cursor.getString(2));
                ciclista.setCCELULAR(Integer.parseInt(cursor.getString(3)));
                ciclista.setCSTATUS(Boolean.parseBoolean(cursor.getString(4)));

                // Add ciclista to ciclistas
                ciclistas.add(ciclista);
            } while (cursor.moveToNext());
        }

        Log.d("getAllciclistas()", ciclistas.toString());

        // return ciclistas
        return ciclistas;
    }

    public int update_ciclista(ciclista ciclista, SQLiteDatabase db) {

        // 1. get reference to writable DB
        //SQLiteDatabase db = this.getWritableDatabase();

        // 2. create ContentValues to add key "column"/value
        ContentValues values = new ContentValues();
        values.put(KEY_CNOME, ciclista.getCNOME());
        values.put(KEY_CEMAIL, ciclista.getCEMAIL());
        values.put(KEY_CCELULAR, ciclista.getCCELULAR());
        values.put(KEY_CSTATUS, ciclista.isCSTATUS());

        // 3. updating row
        int i = db.update(Table_CICLISTA, //table
                values, // column/value
                KEY_NCICLI+" = ?", // selections
                new String[] { String.valueOf(ciclista.getNCICLI()) }); //selection args

        // 4. close
        db.close();

        return i;
    }

    public void delete_ciclista(ciclista ciclista, SQLiteDatabase db) {

        // 1. get reference to writable DB
        //SQLiteDatabase db = this.getWritableDatabase();

        // 2. delete
        db.delete(Table_CICLISTA, //table name
                KEY_NCICLI+" = ?",  // selections
                new String[] { String.valueOf(ciclista.getNCICLI())}); //selections args

        // 3. close
        db.close();

        //log
        Log.d("delete_ciclista", ciclista.toString());
    }

}
