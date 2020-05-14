package clases;


import java.awt.EventQueue;
import java.io.File;
import java.util.ArrayList;

import javax.swing.JFileChooser;
import javax.swing.JFrame;

public class SelectFile extends JFileChooser{

	private JFrame frame;
	
	/**
	 * Se abre una ventana la cual permite seleccionar que instancia(s) probar
	 * 
	 * @return retorna un arrayList con la direccion de los archivos a probar.
	 */
	public ArrayList SelectFile() 
	{
		JFileChooser chooser = new JFileChooser("C:\\Users\\Leviathan\\Desktop\\trabajo\\Referencias\\BF");
		chooser.setMultiSelectionEnabled(true);
		chooser.showOpenDialog(frame);
		File[] files = chooser.getSelectedFiles();
		ArrayList temp=new ArrayList();
		int i;
		
		for (i=0;i<files.length;i++)
		{
			temp.add(files[i]);
		}
		return temp;
	}

}
