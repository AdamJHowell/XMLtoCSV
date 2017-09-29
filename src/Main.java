import java.io.*;
import java.util.ArrayList;
import java.util.List;

import org.jdom2.*;
import org.jdom2.input.SAXBuilder;


public class Main
{
	private static final boolean DEBUG = true;


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
			System.out.println( "Root element :" + document.getRootElement().getName() );
			Element classElement = document.getRootElement();
			List< Element > stockList = classElement.getChildren( "InventoryOnHandStocks" );
			List< InventoryObject > inventoryList = new ArrayList<>();
			System.out.println( "----------------------------" );
			for( Element inventoryItem : stockList )
			{
				InventoryObject tempInventoryObject = new InventoryObject();
				tempInventoryObject.setWarehouseCode( inventoryItem.getChild( "WarehouseCode" ).getText() );
				tempInventoryObject.setWarehouseName( inventoryItem.getChild( "WarehouseName" ).getText() );
				tempInventoryObject.setItemCode( inventoryItem.getChild( "ItemCode" ).getText() );
				tempInventoryObject.setDescription( inventoryItem.getChild( "Description" ).getText() );
				if( inventoryItem.getChild( "InventoryClass" ).getText().length() == 0 )
				{
//					tempInventoryObject.setInventoryClass( "\t" );
				}
				else
				{
					tempInventoryObject.setInventoryClass( inventoryItem.getChild( "InventoryClass" ).getText() );
				}
				if( inventoryItem.getChild( "ProductGroup" ).getText().length() == 0 )
				{
//					tempInventoryObject.setProductGroup( "\t" );
				}
				else
				{
					tempInventoryObject.setProductGroup( inventoryItem.getChild( "ProductGroup" ).getText() );
				}
				if( inventoryItem.getChild( "PreferredSupplier" ).getText().length() == 0 )
				{
//					tempInventoryObject.setPreferredSupplier( "\t" );
				}
				else
				{
					tempInventoryObject.setPreferredSupplier( inventoryItem.getChild( "PreferredSupplier" ).getText() );
				}
				if( inventoryItem.getChild( "MaxOnHand" ).getText().length() == 0 )
				{
//					tempInventoryObject.setMaxOnHand( "\t" );
				}
				else
				{
					tempInventoryObject.setMaxOnHand( inventoryItem.getChild( "MaxOnHand" ).getText() );
				}
				if( inventoryItem.getChild( "MinOnHand" ).getText().length() == 0 )
				{
//					tempInventoryObject.setMinOnHand( "\t" );
				}
				else
				{
					tempInventoryObject.setMinOnHand( inventoryItem.getChild( "MinOnHand" ).getText() );
				}


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
			for( InventoryObject part : inventoryList )
			{
				System.out.println( part.getItemCode() + " " + part.getDescription() );
			}
			System.out.println( "\n" + inventoryList.size() + " parts imported." );
			ListToCSV( inventoryList, args[1] );
		}
		catch( JDOMException | IOException e )
		{
			e.printStackTrace();
		}
	} // End of main() method.


	private static void ListToCSV( List< InventoryObject > inventoryList, String outFileName )
	{
		if( DEBUG )
		{
			System.out.println( "Attempting to write to " + outFileName + " in " + System.getProperty( "user.dir" ) );
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