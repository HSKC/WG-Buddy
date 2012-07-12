package de.htwg.lpn.wgbuddy.AllTests.tests;
import android.test.ActivityInstrumentationTestCase2;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import de.htwg.lpn.wgbuddy.Shopping_Create;


public class Shopping_CreateTest extends ActivityInstrumentationTestCase2<Shopping_Create>
{
	private Button addButton;
	private EditText nameEditText;
	private EditText descriptionEditText;
	private RatingBar ratingBar;
	private Shopping_Create activity;
	
	public Shopping_CreateTest()
	{
		super("de.htwg.lpn.wgbuddy.Shopping_Create", Shopping_Create.class);
	}

	@Override    
	protected void setUp() throws Exception 
	{        
		super.setUp();   
		activity = this.getActivity();
		addButton = (Button) activity.findViewById(de.htwg.lpn.wgbuddy.R.id.shoppingItemAddButton);
		nameEditText = (EditText) activity.findViewById(de.htwg.lpn.wgbuddy.R.id.ShoppingItemNameEditText);
		descriptionEditText = (EditText) activity.findViewById(de.htwg.lpn.wgbuddy.R.id.ShoppingDescriptionEditText);
		ratingBar = (RatingBar) activity.findViewById(de.htwg.lpn.wgbuddy.R.id.shoppingItemRatingBar);
	}
	
	public void testPreconditions()
	{
		assertNotNull(addButton);
		assertNotNull(nameEditText);
		assertNotNull(descriptionEditText);
		assertNotNull(ratingBar);
	}
}
