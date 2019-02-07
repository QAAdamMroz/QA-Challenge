package Test2;

import javax.swing.JOptionPane;

public class Report {
	public static void main(String report)
	{
		if(report=="")
			report="everything is awesome";
		
		   JOptionPane.showMessageDialog(null, report, "Report ", JOptionPane.INFORMATION_MESSAGE);
		    
	}
}
