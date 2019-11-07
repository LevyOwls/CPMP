package clases;

import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream.GetField;
import java.util.ArrayList;

import javax.swing.plaf.basic.BasicInternalFrameTitlePane.MoveAction;

public class Main 
{
	
	
	public static void main (String Args[]) throws IOException
	{
		SelectFile sF=new SelectFile();
		ArrayList instances=sF.SelectFile();
		File dir;
	
		/*if (instances.size()!=0)
		{
			System.out.println(instances);
		}*/
		
		/********************************************/
		//Fin seleccion de instancias.
		/********************************************/
		long TInicio, TFin, tiempo; 
		TInicio = System.currentTimeMillis(); 
		
		Field s=new Field();
		int i;
		while (instances.size()!=0)
		{	
			dir=(File)instances.remove(0);
			s.readFile(dir);
			//lee el archivo
			s.review();
			s.showTime();
			System.out.println();
			s.generateTiers();

			//System.out.println(s.disorderlyElements());

		}	
		
		
		TFin = System.currentTimeMillis();
		tiempo = TFin - TInicio; 
		System.out.println("Tiempo de ejecución en milisegundos: " + tiempo);
	}
	
}
