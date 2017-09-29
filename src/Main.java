import java.io.*;
import java.util.ArrayList;
import java.util.List;

import org.jdom2.*;
import org.jdom2.input.SAXBuilder;


public class Main
{
	private static final boolean DEBUG = true;


	/**
	 * main will attempt to convert a very specific inventory XML file to its CSV (tab delimited) equivalent.
	 * It is tab delimited so I don't have to deal with commas in the part descriptions.
	 * @param args the input file name and the output file name.
	 */
	public static void main( String[] args )
	{
		System.out.println( "XML to CSV" );
		if( args.length < 2 )
		{
			System.out.println( "Please enter an input XML file name and an output CSV file name." );
			return;
		}

		SAXBuilder saxBuilder = new SAXBuilder();
		File inputFile = new File( args[0] );
		try
		{
			Document document = saxBuilder.build( inputFile );
//			System.out.println( "Root element: " + document.getRootElement().getName() );
			Element classElement = document.getRootElement();
			List< Element > stockList = classElement.getChildren( "InventoryOnHandStocks" );
			// ToDo: move the XML reading code to a different method, so I can RAII this next line.
			List< InventoryObject > inventoryList = new ArrayList<>();

			for( Element inventoryItem : stockList )
			{
				InventoryObject tempInventoryObject = new InventoryObject();
				tempInventoryObject.setWarehouseCode( inventoryItem.getChild( "WarehouseCode" ).getText() );
				tempInventoryObject.setWarehouseName( inventoryItem.getChild( "WarehouseName" ).getText() );
				tempInventoryObject.setItemCode( inventoryItem.getChild( "ItemCode" ).getText() );
				tempInventoryObject.setDescription( inventoryItem.getChild( "Description" ).getText() );
				tempInventoryObject.setInventoryClass( inventoryItem.getChild( "InventoryClass" ).getText() );
				tempInventoryObject.setProductGroup( inventoryItem.getChild( "ProductGroup" ).getText() );
				tempInventoryObject.setPreferredSupplier( inventoryItem.getChild( "PreferredSupplier" ).getText() );
				tempInventoryObject.setMaxOnHand( inventoryItem.getChild( "MaxOnHand" ).getText() );
				tempInventoryObject.setMinOnHand( inventoryItem.getChild( "MinOnHand" ).getText() );
				tempInventoryObject.setLastPurchaseCostUnit( inventoryItem.getChild( "LastPurchaseCostUnit" ).getText() );
				tempInventoryObject.setLastPurchaseCostTotal( inventoryItem.getChild( "LastPurchaseCostTotal" ).getText() );
				tempInventoryObject.setAverageCostUnit( inventoryItem.getChild( "AverageCostUnit" ).getText() );
				tempInventoryObject.setAverageCostTotal( inventoryItem.getChild( "AverageCostTotal" ).getText() );
				tempInventoryObject.setStandardCostUnit( inventoryItem.getChild( "StandardCostUnit" ).getText() );
				tempInventoryObject.setStandardCostTotal( inventoryItem.getChild( "StandardCostTotal" ).getText() );
				tempInventoryObject.setCurrency( inventoryItem.getChild( "Currency" ).getText() );
				tempInventoryObject.setConsignmentItem( inventoryItem.getChild( "ConsignmentItem" ).getText() );
				tempInventoryObject.setActive( inventoryItem.getChild( "Active" ).getText() );
				inventoryList.add( tempInventoryObject );
			}
//			for( InventoryObject part : inventoryList )
//			{
//				System.out.println( part.getItemCode() + " " + part.getDescription() );
//			}
			System.out.println( "\n" + inventoryList.size() + " parts imported." );
			ListToCSV( inventoryList, args[1] );
		}
		catch( JDOMException | IOException e )
		{
			e.printStackTrace();
		}
	} // End of main() method.


	/**
	 * ListToCSV takes an ArrayList of InventoryObjects and attempts to write that data to a file in the current directory.
	 * @param inventoryList an ArrayList of InventoryObjects
	 * @param outFileName the file name to attempt to write.
	 */
	private static void ListToCSV( List< InventoryObject > inventoryList, String outFileName )
	{
		if( DEBUG )
		{
			System.out.println( "Attempting to write " + inventoryList.size() + " parts to " + outFileName + " in " + System.getProperty( "user.dir" ) );
			System.out.println( "The first part is Code: " + inventoryList.get( 0 ).getItemCode() );
			System.out.println( "The last part is Code: " + inventoryList.get( inventoryList.size() - 1 ).getItemCode() );
		}
		try
		{
			BufferedWriter outputBufferedWriter = new BufferedWriter( new FileWriter( outFileName ) );
			outputBufferedWriter.write( "WarehouseCode\tWarehouseName\tItemCode\tDescription\tInventoryClass\tProductGroup\tPreferredSupplier\tMaxOnHand\tMinOnHand\tLastPurchaseCostUnit\tLastPurchaseCostTotal\tAverageCostUnit\tAverageCostTotal\tStandardCostUnit\tStandardCostTotal\tCurrency\tConsignmentItem\tActive\n" );
			for( InventoryObject part : inventoryList )
			{
				outputBufferedWriter.write(
					part.getWarehouseCode() + "\t" +
						part.getWarehouseName() + "\t" +
						part.getItemCode() + "\t" +
						part.getDescription() + "\t" +
						part.getInventoryClass() + "\t" +
						part.getProductGroup() + "\t" +
						part.getPreferredSupplier() + "\t" +
						part.getMaxOnHand() + "\t" +
						part.getMinOnHand() + "\t" +
						part.getLastPurchaseCostUnit() + "\t" +
						part.getLastPurchaseCostTotal() + "\t" +
						part.getAverageCostUnit() + "\t" +
						part.getAverageCostTotal() + "\t" +
						part.getStandardCostUnit() + "\t" +
						part.getStandardCostTotal() + "\t" +
						part.getCurrency() + "\t" +
						part.getConsignmentItem() + "\t" +
						part.getActive() +
						'\n' );
			}
		}
		catch( IOException e )
		{
			e.printStackTrace();
		}
	}
}
