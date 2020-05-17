package clases;

import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream.GetField;
import java.util.ArrayList;
import clases.*;
import javax.swing.plaf.basic.BasicInternalFrameTitlePane.MoveAction;

public class Main 
{
	
	
	private static String fil;
	private static boolean analyze=false;
	public static void main (String Args[]) throws IOException
	{
		
		
		
		
		if (Args.length>=3)
		{
			System.out.println("Espere...");
			if (Args[0].equals("-ng"))
			{

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
				int hMax=Integer.parseInt(Args[1]);
				s.setMovs(0);
				s.setHMax(hMax);
				String instance=Args[2];
				System.out.println(instance);
				dir=new File(instance);
				s.readFile(dir);
				//lee el archivo
				s.review();
				//s.copyCat();
				//s.showTime();
				System.out.println();
				//s.showTime();
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
				String anlz=Args[3];
				if (anlz.equals("-anlz"))
				{
					FrameIn n=new FrameIn(s);
				}

				//s.showTime();
				/*System.out.println();
				s.showTime();
				System.out.println();*/
				//s.showTime();
				System.out.println(s.getMovs());
				//System.out.println(s.getVacates());
				//s.showVacatesSize();
				//System.out.println("°~°~°~°~°~°~°~°~°~°~°~°~°~°~°~°~°~°~°~°~°~°~°~°~°~°~°~°~°~°~°~°~°~°~°~°~°~");
				//System.out.println();
				//s.verificar();
				//s.showTime();
				
				//s.showTime();
				//System.out.println(s.totalContenedores());
				
			}
			else
			{
				System.out.println("No se encuentra el comando ingresado");
			}
			
			//ABRE EL ARCHIVO
		}
		if (Args.length==0)
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
				
				//s.copyCat();
				//s.showTime();
				//System.out.println();
				//s.showTime();
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
						//System.out.println();
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
				System.out.println(s.getMovs());
				//System.out.println(s.getVacates());
				//s.showVacatesSize();
				//System.out.println("°~°~°~°~°~°~°~°~°~°~°~°~°~°~°~°~°~°~°~°~°~°~°~°~°~°~°~°~°~°~°~°~°~°~°~°~°~");
				//System.out.println();
				//s.verificar();
				//s.showTime();
				
				//s.showTime();
				//System.out.println(s.totalContenedores());
				if (analyze==true)
				{
					FrameIn n=new FrameIn(s);
				}

			}	
			
			
			TFin = System.currentTimeMillis();
			tiempo = TFin - TInicio; 
			System.out.println();
			System.out.println(" Tiempo de ejecución en milisegundos: " + tiempo);
			
			//s.showTime();
		}
		
		
		
		
		
		
	}
	
}
