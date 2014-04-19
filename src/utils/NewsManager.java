package utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import com.example.cmp464.Item;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
/**
 * Database class that provides some CRUD methods
 * @author luizbraga
 *
 */
public class NewsManager extends SQLiteOpenHelper{

	private static final String TABLE_NAME = "news";

	private static final String ID = "id";
	private static final String TITLE = "title";
	private static final String DESCRIPTION = "description";
	private static final String LINK = "link";
	private static final String DATE = "date";

	// SQL statement to create the News table
	public static final String CREATE_TABLE = "CREATE TABLE "+
			TABLE_NAME+" ("+
			ID+" INTEGER PRIMARY KEY AUTOINCREMENT, "+
			TITLE+" TEXT, "+
			DESCRIPTION+" TEXT, "+
			LINK+" TEXT, "+
			DATE+" DATE)";


	public NewsManager(Context context) {
		super(context, "news", null, 1);
	}


	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(CREATE_TABLE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// Drop older table if existed
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
		// Create tables again
		onCreate(db);
	}
	
	public void dropTable(){
		SQLiteDatabase db = this.getWritableDatabase();
		// Drop older table if existed
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
		// Create tables again
		onCreate(db);
		db.close();
	}
	
	public void addAllNews(List<Item> news) {
		SQLiteDatabase db = this.getWritableDatabase();

		for(int i=0; i<news.size(); i++){
			ContentValues values = new ContentValues();

			values.put(TITLE, news.get(i).getTitle());
			values.put(DESCRIPTION, news.get(i).getDescription());
			values.put(LINK, news.get(i).getLink());

			values.put(DATE, news.get(i).getDate().toString());

			db.insert(TABLE_NAME, null, values);

		}
		
		db.close();
	}

	public List<Item> getAllItems(){
		List<Item> itemList = new ArrayList<Item>();

		// Select All Query
		String selectQuery = "SELECT  * FROM " + TABLE_NAME;

		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);

		// looping through all rows and adding to list
		if (cursor.moveToFirst()) {
			do {
				Item item = new Item(Integer.parseInt(cursor.getString(0)));
				item.setTitle(cursor.getString(1));
				item.setDescription(cursor.getString(2));
				item.setLink(cursor.getString(3));
				//item.setDate(cursor.getString(4));
				try {
					SimpleDateFormat formatter = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.US);
					Date date = formatter.parse(cursor.getString(4));
					item.setDate(date);
				} catch (ParseException e) {
					e.printStackTrace();
				}
				// Adding contact to list
				itemList.add(item);
			} while (cursor.moveToNext());
		}
		
		db.close();
		// return contact list
		return itemList;
	}

	public Date getNewestItemDate(){
		Date date = null;
		
		SQLiteDatabase db = this.getReadableDatabase();
		// The newest item is always the first element
		Cursor cursor = db.query(TABLE_NAME, new String[] { DATE }, ID+"=1", null, null, null, null);
		if (cursor != null)
			cursor.moveToFirst();
		
		try{
			SimpleDateFormat formatter = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.US);
			date = formatter.parse(cursor.getString(0));
		} catch(ParseException e){
			e.printStackTrace();
		}
		
		db.close();

		return date;
	}

}
