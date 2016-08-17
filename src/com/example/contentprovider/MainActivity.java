package com.example.contentprovider;

import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.UserDictionary;
import android.support.v7.app.ActionBarActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

public class MainActivity extends ActionBarActivity {
	private EditText mSearchWord = null;
	private Button myButtonQuery = null;
	private Button myButtonInsert = null;
	private Button myButtonUpdate = null;
	private Button myButtonDelete = null;
	private ListView myListView = null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		mSearchWord = (EditText) findViewById(R.id.myEditText);
		myButtonQuery = (Button) findViewById(R.id.myButtonQuery);
		myButtonInsert = (Button) findViewById(R.id.myButtonInsert);
		myButtonUpdate = (Button) findViewById(R.id.myButtonUpdate);
		myButtonDelete = (Button) findViewById(R.id.myButtonDelete);
		myListView = (ListView) findViewById(R.id.myListView);
		mSearchWord.setText("請輸入");
		myButtonQuery.setText("查詢");
		myButtonInsert.setText("插入");
		myButtonUpdate.setText("更新");
		myButtonDelete.setText("刪除");
		myButtonDelete.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				delete();
			}
		});
		mSearchWord.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				mSearchWord.setText("");
				return false;
			}
		});
		// A "projection" defines the columns that will be returned for each row
		myButtonQuery.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				//myTextView.setText("");
				String[] mWordListColumns =
					{
					    UserDictionary.Words.WORD,   // Contract class constant containing the word column name
					    UserDictionary.Words.LOCALE  // Contract class constant containing the locale column name
					};
				
				Cursor cursor = query();
				SimpleCursorAdapter adapter = new SimpleCursorAdapter
						(MainActivity.this, R.layout.list_view_item, cursor, mWordListColumns,new int[]{R.id.key,R.id.value});
				myListView.setAdapter(adapter);
			}
		});
		myButtonInsert.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				insert();
			}
		});
		myButtonUpdate.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				update();
			}
		});
		
	}
	private void delete(){
		// Defines selection criteria for the rows you want to delete
		String mSelectionClause = UserDictionary.Words.WORD +" like ?";
		String[] mSelectionArgs = {"insert"};

		// Defines a variable to contain the number of rows deleted
		int mRowsDeleted = 0;


		// Deletes the words that match the selection criteria
		mRowsDeleted = getContentResolver().delete(
		    UserDictionary.Words.CONTENT_URI,   // the user dictionary content URI
		    mSelectionClause,                    // the column to select on
		    mSelectionArgs                    // the value to compare to
		);
	}
	private void update(){
		// Defines an object to contain the updated values
		ContentValues mUpdateValues = new ContentValues();

		// Defines selection criteria for the rows you want to update
		String mSelectionClause = UserDictionary.Words.LOCALE +  " LIKE ?";
		String[] mSelectionArgs = {"en_%"};

		// Defines a variable to contain the number of updated rows
		int mRowsUpdated = 0;

		/*
		 * Sets the updated value and updates the selected words.
		 */
		mUpdateValues.put(UserDictionary.Words.LOCALE,"cn");

		mRowsUpdated = getContentResolver().update(
		    UserDictionary.Words.CONTENT_URI,   // the user dictionary content URI
		    mUpdateValues,                       // the columns to update
		    mSelectionClause,                    // the column to select on
		    mSelectionArgs                      // the value to compare to
		);
	}
	private void insert(){
		// Defines a new Uri object that receives the result of the insertion
		Uri mNewUri;

	

		// Defines an object to contain the new values to insert
		ContentValues mNewValues = new ContentValues();

		/*
		 * Sets the values of each column and inserts the word. The arguments to the "put"
		 * method are "column name" and "value"
		 */
		mNewValues.put(UserDictionary.Words.APP_ID, "example.user");
		mNewValues.put(UserDictionary.Words.LOCALE, "en_US");
		mNewValues.put(UserDictionary.Words.WORD, "insert");
		mNewValues.put(UserDictionary.Words.FREQUENCY, "100");

		mNewUri = getContentResolver().insert(
		    UserDictionary.Words.CONTENT_URI,   // the user dictionary content URI
		    mNewValues                          // the values to insert
		);
	}
	private Cursor query(){
		String[] mProjection =
			{
			    UserDictionary.Words._ID,    // Contract class constant for the _ID column name
			    UserDictionary.Words.WORD,   // Contract class constant for the word column name
			    UserDictionary.Words.LOCALE  // Contract class constant for the locale column name
			};

			// Defines a string to contain the selection clause
			String mSelectionClause = null;

			// Initializes an array to contain selection arguments
			String[] mSelectionArgs = {""};
			
			/*
			 * This defines a one-element String array to contain the selection argument.
			 */

			// Gets a word from the UI
			String mSearchString = mSearchWord.getText().toString();

			// Remember to insert code here to check for invalid or malicious input.

			// If the word is the empty string, gets everything
			if (TextUtils.isEmpty(mSearchString)) {
			    // Setting the selection clause to null will return all words
			    mSelectionClause = null;
			    mSelectionArgs = null;

			} else {
			    // Constructs a selection clause that matches the word that the user entered.
			    mSelectionClause = UserDictionary.Words.WORD + " = ?";

			    // Moves the user's input string to the selection arguments.
			    mSelectionArgs[0] = mSearchString;

			}

			// Does a query against the table and returns a Cursor object
			Cursor mCursor = getContentResolver().query(
			    UserDictionary.Words.CONTENT_URI,  // The content URI of the words table
			    mProjection,                       // The columns to return for each row
			    mSelectionClause,                   // Either null, or the word the user entered
			    mSelectionArgs,                    // Either empty, or the string the user entered
			    null);                       // The sort order for the returned rows

			// Some providers return null if an error occurs, others throw an exception
			if (null == mCursor) {
			    /*
			     * Insert code here to handle the error. Be sure not to use the cursor! You may want to
			     * call android.util.Log.e() to log this error.
			     *
			     */
				Log.i("xiaoxu", "mCursor == null");
			// If the Cursor is empty, the provider found no matches
			} else if (mCursor.getCount() < 1) {
				
			    /*
			     * Insert code here to notify the user that the search was unsuccessful. This isn't necessarily
			     * an error. You may want to offer the user the option to insert a new row, or re-type the
			     * search term.
			     */
				
				Log.i("xiaoxu", "the search was unsuccessful");

			} else {
			    // Insert code here to do something with the results
//				int cols = mCursor.getColumnCount();
//				StringBuffer sb = new StringBuffer();
//				while(mCursor.moveToNext()){
//					for(int i = 0; i < cols; i++){
//						String colName = mCursor.getColumnName(i);
//						String colVal = mCursor.getString(i);
//						sb.append(colName+" : "+colVal);
//						sb.append("\n");
//					}
//					sb.append("\n");
//				}
//				myTextView.setText(sb.toString());
			}
			return mCursor;
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
