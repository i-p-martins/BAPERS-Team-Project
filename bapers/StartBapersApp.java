package bapers;

import bapers.bapgui.BapersApplication;
import javafx.application.Application;

public class StartBapersApp  {

	/**
	 * 
	 * @param args
	 */
	public static void main(String[] args) throws Exception{
		try
		{
			Application.launch(BapersApplication.class);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

}