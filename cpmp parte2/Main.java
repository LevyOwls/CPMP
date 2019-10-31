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
			int h;
			s.review();
			s.showTime();
			s.review();
			System.out.println(s.totalContenedores());
			for (i=0;i<s.totalContenedores();i++)
			{
				s.fill();
				//System.out.println(s.totalContenedores());
				s.review();
			}
			int c;
			
			s.showTime();
			System.out.println("\n******FIN DE FILL");
			int hh=0;
			hh=hh-hh+hh;//SOLO ES UN CHECKPOINT PARA DEBUG
			

			for (i=0;i<s.totalContenedores();i++)
			{
				c=s.select_column();
				System.out.println(s.totalContenedores());
				System.out.println("\n");
				//s.showTime();
				s.review();
			}
			System.out.println("\n******FIN DE SUBCOLUMN");
			s.showTime();
			
			while(!s.isOrdenado())
			{
				s.columnSort();
				s.review();
				System.out.println(s.totalContenedores());
			}
			
			System.out.println("\n");
			s.showTime();
			
			System.out.println("\nmovimientos: "+s.getMovs()+"\nColumn sort: "+s.select_column());
			//System.out.println(s.disorderlyElements());

		}	
		
		
		TFin = System.currentTimeMillis();
		tiempo = TFin - TInicio; 
		System.out.println("Tiempo de ejecución en milisegundos: " + tiempo);
	}
	
}
