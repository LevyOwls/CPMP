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
			s.showTime();
			System.out.println();
			
			for (i=0;i<s.totalContenedores();i++)
			{
				s.AIOfill();
			}
			int aux;
			aux=s.firstEmpty();
			System.out.println();
			s.showTime();
			System.out.println();
			while (s.firstEmpty()!=-1 && !s.isOrdenado())
			{
				s.AIOEmptyFill(aux);
				aux=s.firstEmpty();
			}
			for (i=0;i<s.totalContenedores();i++)
			{
				s.AIOfill();
			}
			
			System.out.println();
			s.showTime();
			System.out.println();
			
			int selector=s.select_column();		
			if (selector!=-1)
			{
				s.AIOsacrifice(selector);
			}
			
			System.out.println();
			s.showTime();
			System.out.println();
			
			for (i=0;i<s.totalContenedores();i++)
			{
				s.AIOfill();
			}
			
			System.out.println();
			s.showTime();
			System.out.println();
			
		}	
		
		
		TFin = System.currentTimeMillis();
		tiempo = TFin - TInicio; 
		System.out.println("Tiempo de ejecución en milisegundos: " + tiempo+ "movimientos realizados: "+s.getMovs());
		
		//s.showTime();
	}
	
}
