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
			s.setMovs(0);
			
			dir=(File)instances.remove(0);
			s.readFile(dir);
			//lee el archivo
			s.review();
			//s.showTime();
			System.out.println();
			s.showTime();
			//s.excelShowTime();
			while (!s.isOrdenado())
			{

				while (s.isFillPosible())
				{
					s.AIOfill();
					//System.out.println();
					//s.showTime();
					//System.out.println("Movimientos hasta ahora: "+s.getMovs());
				}
				if (s.isOrdenado())
				{
					System.out.println();
					//s.printMovs();
					break;
					
				}
				
				s.AIOVacateColumn();
			}
			
			//s.showTime();
			/*System.out.println();
			s.showTime();
			System.out.println();*/
			//s.showTime();
			System.out.print(dir.toString().substring(52) +" Movimientos: "+s.getMovs());
			System.out.println();
			s.verificar();
			s.showTime();
			
			//s.showTime();
			//System.out.println(s.totalContenedores());
			
		}	
		
		
		TFin = System.currentTimeMillis();
		tiempo = TFin - TInicio; 
		System.out.println();
		System.out.println(" Tiempo de ejecución en milisegundos: " + tiempo);
		
		//s.showTime();
	}
	
}
