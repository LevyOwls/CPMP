package clases;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.acl.LastOwnerException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

public class Field 
{
	private static ArrayList field=new ArrayList();
	private static int hMax=5;
	private static int movs=0;
	private static int columns;
	
	
	public void setField(ArrayList l)
	{
		field=l;
	}
	public int getHMax()
	{
		return hMax;
		
	}
	
	public void setHMax(int h)
	{
		hMax=h;
	}
	/**
	 * INGRESA LA LECTURA DE UN ARCHIVO A FIELD
	 * @param f file
	 * @throws FileNotFoundException
	 */
	public static void readFile(File f) throws FileNotFoundException
	{
		field.clear();
		int num;
		int columns;
		int containers;
		int q;
		
		 try  (Scanner entrada = new Scanner(f)) 
		    {

		    	//Primero estan las columnas y los contenedores totales.
		        columns = entrada.nextInt();
		        containers=entrada.nextInt();
		        
		       // System.out.println("Entrada: \nColumnas:"+ columnas+" Contenedores: "+contenedores);

		        while (entrada.hasNextInt()) 
		        {
		        	q = entrada.nextInt(); //La cantidad de contenedores de dicha columna
		        	int i;
		        	Column nuevo=new Column();
		        	for (i=0;i<q;i++)
		        	{
		        		num=entrada.nextInt();
		        		nuevo.add(num);
		        		//System.out.print(numero + " "); //se muestra la prioridad de los contenedores de la columna
		        	}
		        	field.add(nuevo);
		        	//System.out.print("\n");
		        	//System.out.println(nuevo);
		         }
		       
		    }
	}
	public static int getMovs() {
		return movs;
	}

	public static void setMovs(int movs) {
		Field.movs = movs;
	}
	/**
	 * IMPRIME EL ESTADO ACTUAL DE FIELD
	 */

	public static void showTime()
	{
		int i;
		Column temp;
		for (i=0;i<field.size();i++)
		{	
			temp=(Column)field.get(i);
			System.out.println(temp+"  "+temp.isLock());
		}
	}
	
	/**
	 * ACTUALIZA EL ESTADO ACTUAL DE LAS COLUMNAS 
	 */
	public static void review()
	{
		int i;
		Column temp;
		for (i=0;i<field.size();i++)
		{
			temp=(Column)field.get(i);
			temp.generaIdeal();
			temp.setDif();
			temp.setuB();
			temp.setLock();
			
		}
	}
	
	/**
	 * 					MUEVE UN CONTENEDOR DE COLUMNA, SE ASUME QUE LA OPERACION NO SERA REALIZADA CON UNA COLUMNA (c_d) QUE TENGA ALTURA MAXIMA
	 * 					O CUENTE CON UN NUMERO DESORDENADO
	 * @param c_o		COLUMNA ORIGEN
	 * @param c_d		COLUMNA DESTINO
	 * @return			TRUE EN CASO DE QUE LA OPERACION FUE REALIZADA CON EXITO, FALSE EN CASO CONTRARIO
	 */
	public static boolean Atom_move(int c_o,int c_d)
	{
		Column temp=(Column)field.get(c_o);
		//EN CASO DE QUE LA COLUMNA DE ORIGEN ESTE VACIA SE RETORNA FALSE
		if (temp.size()==0)
		{
			return false;
		}
		int numero=(int)temp.quitar();
		
		//SE INSERTA EL NUMERO EN LA COLUMNA DESTINO
		
		temp=(Column)field.get(c_d);
		
		temp.add(numero);
		return true;
	}
	
	public static void atomic_Move(int n,int c_d)
	{
		Column temp=(Column)field.get(c_d);
		
		temp.add(n);
		movs++;
	}
	
	/**
	 * 
	 * 	Genera los espacios disponibles por tier dentro de field. Se ve de arriba hacia abajo
	 * 
	 * 
	 */
	public static int[] generateTiers()
	{
		Column temp;
		int i,j;
		int altura=hMax;
		int helper;
		int tiers[]=new int[altura];
		
		//SE ASIGNA TAMA�O CERO A TODOS LOS TIERS
		for (i=0;i<tiers.length;i++)
		{
			tiers[i]=0;
		}
		
		for (i=0;i<field.size();i++)
		{
			temp=(Column)field.get(i);
			helper=hMax-temp.size();
			for (j=0;j<helper;j++)
			{
				tiers[j]++;
			}
		}
		
		for (i=0;i<tiers.length;i++)
		{
			System.out.println(tiers[i]+"   H: "+altura);
			altura--;
		}
		return tiers;
	}
	
	//*************************************************************************
	//*************************************************************************
	//*************************************************************************
	//*************************************************************************
	
	
	/**
	 * GENERA UNA LISTA CON LA PRIORIDAD DE LOS CONTENEDORES DESORDENADOS, SIN REPETIR PRIORIDADES.
	 * @return lista con prioridades de contenedores desordenados.
	 */
	public static ArrayList disorderlyElements()
	{
		ArrayList elements=new ArrayList();
		
		Column temp;
		
		int i;
		int j;
		for (i=0;i<field.size();i++)
		{
			temp=(Column)field.get(i);
			//SI LA COLUMNA NO ESTA ORDENADA
			if (!temp.isLock())
			{
				for (j=0;j<temp.size();j++)
				{
					elements.add(temp.get(j));
				}
			}
		}
		Collections.sort(elements);
		
		ArrayList unrepeated=new ArrayList();
		int helper;
		for (i=0;i<elements.size();i++)
		{
			helper=(int)elements.get(i);
			if (!unrepeated.contains(helper))
			{
				unrepeated.add(helper);
			}
		}
		
		return unrepeated;
	}
	
	/**
	 * ESTO NO ASEGURA QUE SEA MAS FACIL SACAR ESTO 
	 * @param n 		CONTENEDOR AL CUAL SE LE BUSCA SOBREPONER OTRO CONTENEDOR
	 * @return			PRIORIDAD MAS CERCANA PARECIDA AL CONTENEDOR LA CUAL SIRVA PARA COLOCAR ENCIMA (CONTENEDOR>RETURN)
	 * 					-1 EN CASO DE NO ENCONTRAR NADA
	 */
	public static int selector(int n)
	{
		ArrayList disorderly=disorderlyElements();
		
		int num=n;
		while (num!=0)
		{
			if (disorderly.contains(num))
			{
				return num;
			}
			else
			{
				num--;
			}
		}
		
		return -1;
	}
	
	/**
	 * 
	 * @return ARRAYLIST CON LA PRIORIDAD DE LOS CONTENEDORES EN LA CIMA DE CADA COLUMNA
	 */
	public static ArrayList getTops()
	{
		ArrayList tops=new ArrayList();
		int i;
		int aux;
		Column temp;
		for (i=0;i<field.size();i++)
		{
			temp=(Column)field.get(i);
			if (!temp.isEmpty())
			{
				aux=(int)temp.get(temp.size()-1);
				tops.add(aux);
			}
			else
			{
				tops.add(0);
			}
		}
		return tops;
	}
	
	/**
	 * 
	 * @param n		NUMERO DE LA COLUMNA
	 * @return		RETORNA SI UNA COLUMNA ESTA LOCKEADA O NO.
	 */
	public static boolean isLock(int n)
	{
		Column temp=(Column)field.get(n);
		
		return temp.isLock();
	}
	
	/**
	 * VERIFICA SI UN CONTENEDOR ESTA EN LA CIMA Y DE SER ASI RETORNA SU COLUNMA (IGNORA LO LOCKEADO)
	 * @return	COLUMNA EN LA QUE SE ENCUENTRA UN CONTENEDOR O UN -1
	 */
	public static int containsInTop(int n)
	{
		int i;
		ArrayList tops=getTops();
		int aux;
		for (i=0;i<tops.size();i++)
		{
			aux=(int)tops.get(i);
			if (aux==n && isLock(i)==false)
			{
				return i;
			}
		}
		return -1;
		
	}
	/**
	 * 
	 * @return PRIMER ESPACIO VACIO QUE APAREZCA O -1 EN CASO DE NO EXISTIR
	 */
	public static int empty()
	{
		Column temp;
		int i;
		for (i=0;i<field.size();i++)
		{
			temp=(Column)field.get(i);
			if (temp.isEmpty())
			{
				return i;
			}
		}
		return -1;
	}
	
	/**
	 *  VERIFICA SI ESTA EL MAYOR EN LA CIMA, EN CASO DE NO ESTARLO RETORNA -1
	 * @return LA COLUMNA EN LA QUE SE ENCUENTRA EL CONTENEDOR CON NUMERO MAS GRANDE
	 */
	public static int bigInTop()
	{
		ArrayList disorder=disorderlyElements();
		Collections.reverse(disorder);
		//System.out.println(disorder);
		if (disorder.size()==0)
		{
			return -1;
		}
		int aux=(int)disorder.get(0);
		return containsInTop(aux);
		
	}
	
	public static int totalContenedores()
	{
		int i;
		int sum=0;
		Column temp;
		for (i=0;i<field.size();i++)
		{
			temp=(Column)field.get(i);
			sum=sum+temp.size();
		}
		return sum;
	}
	
	public static void fill()
	{
		//DESICIONES
		
		//RELLENAR ESPACIOS VACIOS CON LOS MAS GRANDES (DE PODERSE EN EL MOMENTO)
		int column;
		int n;
		int aux;
		int top;
		column=empty();
		n=bigInTop();
		Column temp;
		while (column!=-1 && n!=-1)
		{
			temp=(Column)field.get(n);
			aux=(int)temp.quitar();
			temp=(Column)field.get(column);
			temp.add(aux);
			setMovs(getMovs() + 1);
			review();
			column=empty();
			n=bigInTop();
		}
		
		//RELLENAR LAS DEMAS COLUMNAS
		int i;
		for (i=0;i<field.size();i++)
		{
			temp=(Column)field.get(i);
			//SI ESTA ORDENADO, NO ESTA VACIO Y SU ALTURA ES MENOR A ALTURA MAXIMA
			if (temp.isLock() && !temp.isEmpty() && temp.size()<hMax)
			{
				aux=(int)temp.get(temp.size()-1); //obtengo el top
				top=aux;
				aux=selector(aux);
				if (aux<=0)
				{
					break;
				}
				column=containsInTop(aux);
				if (column!=-1)
				{	
					temp=(Column)field.get(column);
					aux=(int)temp.quitar();
					temp=(Column)field.get(i);
					temp.add(aux);
					setMovs(getMovs() + 1);
					review();
					
					
				}
				else
				{	
					aux=selector(aux-1);
					if (aux<=0)
					{
						break;
					}
					column=containsInTop(aux);
					if (column!=-1)
					{
						temp=(Column)field.get(column);
						aux=(int)temp.quitar();
						temp=(Column)field.get(i);
						temp.add(aux);
						setMovs(getMovs() + 1);
						review();
						
					}
				}
			}
		}
		
	}
	


	
	/**
	 * REVISA SI PUEDE COLOCARSE SOBRE ALGUNA COLUMNA
	 * @return 		NUMERO DE COLUMNA EN LA CUAL PUEDE POSICIONARSE O -1 EN CASO DE NO ENCONTRAR NINGUNA
	 */
	public static int atomicMove(int n,int c_o)
	{
		ArrayList tops=getTops();
		int i;
		int aux;
		int dif=n;
		int col=-1;
		Column temp;
		for (i=0;i<tops.size();i++)
		{
			aux=(int)tops.get(i);
			if (n<aux)
			{
				if (dif>aux-n)
				{
					temp=(Column)field.get(i);
					//QUE NO SEA LA COLUMNA ORIGEN
					if (temp.size()<hMax && i!=c_o)
					{
						col=i;
					}
					
				}
			}
			
		}
		return col;
	}
	
	/**
	 * GRASP
	 * @return NUMERO DE LA COLUMNA A ORDENAR SEGUN GRASP, EN CASO DE NO EXISTIR RETORNA -1
	 */
	public static int select_column()
	{
		//SE OBTIENEN LAS COLUMNAS DESORDENADAS.
		
		ArrayList columns=new ArrayList();
		ArrayList score=new ArrayList();
		Column temp;
		int i;
		ArrayList disorder=disorderlyElements();
		Collections.reverse(disorder);;
		if (disorder.size()==0)
		{
			return -1;
		}
		int mayor=(int)disorder.get(0);
		for (i=0;i<field.size();i++)
		{
			temp=(Column)field.get(i);
			if (!temp.isLock())
			{
				columns.add(temp);
			}
		}
		
		if (columns.size()==1)
		{
			for (i=0;i<field.size();i++)
			{
				if (columns.get(0).equals(field.get(i)))
				{
					return i;
				}
			}
		}
		
		
		//SE ASIGNA PUNTAJE POR COLUMNA, primero el uB 
		int uB,size,dif,errors,cont;
		float helper,maxScore;
		//System.out.println("\n\n");
		maxScore=0;
		for (i=0;i<columns.size();i++)
		{
			temp=(Column)columns.get(i);
			uB=(int)temp.getuB();
			dif=(int)temp.getDif();
			errors=(int)temp.getErrors();
			//mientras menor ub mejor pero mientras mayor diferencia mejor
			//entonces...se distribuye el puntaje de la siguiente forma
			helper=(float)(uB*0.6+dif*0.3+errors*0.1);
			//System.out.print(temp+"\tpuntaje: "+helper);
			int oracle=temp.indexOf(mayor);
			if(oracle!=-1)
			{
				helper=helper*(temp.getuB()-oracle)/5;
			}
			//EN CASO DE EXISTIR EL NUMERO MAYOR LOCAL, SE CONSIDERA EN EL PUNTAJE
			//System.out.println(temp+"\tpuntaje con oracle: "+helper);
			if (helper>maxScore)
			{
				maxScore=helper;
			}
			score.add(helper);
		}
		int minNum=-1;
		//reducir resultados para generar un random 
		if (score.size()!=1)
		{
			if (score.size()==0)
			{
				return -1;
			}
			minNum=score.size()/2;
		}
		
	
			ArrayList sortScore=new ArrayList(score);
			
			Collections.sort(sortScore);
			Collections.reverse(sortScore);
			i=0;int j;
			while (columns.size()>minNum || columns.size()!=1)
			{
				helper=(float)sortScore.get(i);
				for (j=0;j<score.size();j++)
				{
					if (helper==(float)score.get(j))
					{
						score.remove(j);
						columns.remove(j);
					}
				}
				i++;
				if(sortScore.size()<=i)
				{
					break;
				}
			}
			if (columns.size()==0)
			{
				temp=(Column)columns.get(0);
				for (i=0;i<field.size();i++)
				{
					if (temp.equals(field.get(i)))
					{
						//System.out.println("\n\n\nMejor Columna Para Ordenar"+i);
						return i;
					}
				}
			}
			int rnd=(int)(Math.random()*(columns.size()-1));
			//System.out.println("\n\n"+columns+"  selected:"+rnd);
			
			//AQUI HABIA UN ERROR PERO YA SE ARREGLO
			while(columns.size()<rnd)
			{
				rnd--;
			}
			
			temp=(Column)columns.get(rnd);
			
			for (i=0;i<field.size();i++)
			{
				if (temp.equals(field.get(i)))
				{
					//System.out.println("\n\n\nMejor Columna Para Ordenar"+i);
					return i;
				}
			}
			
			return -1;
		
	}
	/**
	 * @param n			NUMERO MAXIMO DE TAMA�O DE COLUMNA
	 * @param c_o		COLUMNA POR LA CUAL QUIERO REALIZAR EL SACRIFICIO
	 * @return			SI SE LOGRO O NO
	 */
	public static boolean sacrifice(int c_o,int n)
	{
		Column temp,col;
		ArrayList posibles=new ArrayList();
		
		int i;
		//COLUMNA ORIGEN
		col=(Column)field.get(c_o);
		
		for (i=0;i<field.size();i++)
		{
			temp=(Column)field.get(i);
			if (temp.size()==n)
			{
				posibles.add(i);
			}
		}
		//EN CASO QUE ESTE VACIO, SE INTENTA CON UNA COLUMA UN POCO MAS GRANDE
		if (posibles.isEmpty())
		{
			sacrifice(c_o,n+1);
		}
		else
		{
			int minum=1000000;
			//SE VERIFICA LA COLUMNA CON EL CONTENEDOR DE NUMERO MENOR
			for (i=0;i<posibles.size();i++)
			{
				temp=(Column)field.get((int) posibles.get(i));
				if (temp.biggerNumber()<=minum)
				{
					minum=temp.biggerNumber();
				}
			}
			int aux;
			//LUEGO SE BUSCA LA COLUMNA
			for (i=0;i<posibles.size();i++)
			{
				aux=(int) posibles.get(i);
				temp=(Column)field.get(aux);
				if (temp.biggerNumber()==minum)
				{
					break;
				}
			}
			
			//LA VARIABLE "i" TIENE AL COLUMNA A OCUPAR
			
			//ESTO ES PARA DEBUGEAR
			
			int helper=(int)posibles.get(i);
			temp=(Column)field.get(helper);
			ArrayList subCol=new ArrayList();
			while (temp.size()!=0)
			{
				subCol.add(temp.quitar());
			}
		
			int column;
			//AHORA SE INSERTAN EN OTRA COLUMNA
			while (subCol.size()!=0)
			{
				aux=(int)subCol.remove(0);
				column=atomicMove(aux,helper);
				if (column!=-1)
				{
					atomic_Move(aux, column);
				}
				else
				{
					temp=(Column)field.get(helper);
					//SE INSERTA EL ELEMENTO ACTUAL
					temp.add(aux);
					while (subCol.size()!=0)
					{
						//SE INSERTA EL RESTO DE ELEMENTOS
						aux=(int)subCol.remove(subCol.size()-1);
						temp.add(aux);
					}
					//NO SE LOGRO ORDENAR
					review();

					return false;
				}
			}
			
		}
		review();
		return true;
		
		
		
	}
	/*public static void columnSort()
	{
		int best=select_column();
		if (best==-1)
		{
			return ;
		}
		Column temp;
		
		int i;
		subColumnSort(best);
		
	}*/
	
	/**
	 * 	IDENTIFICA EL TIPO DE SUBCOLUMNA A ORDENAR E INTENTA ORDENARLA
	 * @param c_o COLUMNA ORIGEN
	 * @return RETORNA TRUE EN CASO DE QUE SE PUEDA ORDENAR COMPLETAMENTE LA SUBCOLUMNA, FALSE EN CASO CONTRARIO 
	 */
	public static boolean subColumnSort(int c_o)
	{
		
		//OBTENGO LA COLUMNA
		Column temp=(Column)field.get(c_o);
		
		//SE CREA UNA LISTA QUE CONTENGA LOS ELEMENTOS
		ArrayList subCol=new ArrayList();
		
		/**SE ASUME QUE CUALQUIER COLUMNA QUE INGRESA A ESTA FUNCION TIENE AL MENOS 2 CONTENEDORES, YA QUE SI TIENE UNO SE ENCUENTRA ORDENADO*/

		//MIENTRAS LA COLUMNA NO ESTE ORDENADA
		while (!temp.isLock())
		{
			subCol.add(temp.quitar());
			review();
		}
		
		//UNA VEZ OBTENIDA LA SUBCOLUMNA SE COMIENZA A COLOCAR LOS ELEMENTOS DONDE SE PUEDA
		//Auxiliar
		int aux;
		int col;
		while (subCol.size()!=0)
		{
			aux=(int)subCol.remove(0);
			col=atomicMove(aux, c_o);
			//EN CASO DE QUE EXISTA UNA COLUMNA EN LA CUAL SE PUEDE ASIGNAR EL CONTENEDOR SE ASIGNA
			if (col!=-1)
			{
				atomic_Move(aux, col);
				//SE ACTUALIZA EL AUX ACTUAL
				/*if (subCol.size()!=0)
				{
					aux=(int)subCol.remove(0);
					
					//CUANDO LLEGUE AL FINAL
					if (subCol.size()==0)
					{
					
						if (atomicMove(aux,c_o)!=-1)
						{
							atomic_Move(aux, col);
							review();
							return true;
						}
						else
						{
							temp=(Column)field.get(c_o);
							temp.add(aux);
							review();
							return false;
						}
						
					}
					
					
				}*/
				//LA FUNCION CONTABILIZA EL MOVIMIENTO ASI QUE NO HAY NINGUN PROBLEMA CON ELLA.
			}
			//EN CASO DE QUE LA FUNCION SI DE -1, SE DEVUELVE TODO LO QUE QUEDO A LA COLUMNA ORIGINAL Y SE CIERRA EL CICLO
			else
			{
				temp=(Column)field.get(c_o);
				//SE INSERTA EL ELEMENTO ACTUAL
				temp.add(aux);
				review();
				System.out.println(temp.isLock());
				while (subCol.size()!=0)
				{
					//SE INSERTA EL RESTO DE ELEMENTOS
					aux=(int)subCol.remove(subCol.size()-1);
					temp.add(aux);
				}
				//NO SE LOGRO ORDENAR
				review();
				System.out.println(temp.isLock());
				return false;
				
			}
		}
		//SI SE LOGRO ORDENAR COMPLETAMENTE DICHA SUBCOLUMNA
		review();
		return true;
		
		
	}
	
	
	
	
	
	
	public static void columnSort()
	{
		boolean step;
		int best=select_column();
		if (best==-1)
		{
			return;
		}
		Column temp;
		
		int i;
		temp=(Column)field.get(best);
		
		for (i=0;i<temp.size();i++)
		{
			if (!subColumnSort(best))
			{
				step=temp.isLock();
				if (temp.isLock())
				{
					break;
				}
				else
				{
					fill();
					review();
				}
			}
		}
		review();
		//SI AUN NO ESTA ORDENADA SE CONSIDERA EL SACRIFICIO
		if (!temp.isLock())
		{
			sacrifice(best,1);
			subColumnSort(best);
			fill();
		}
		
	}
	
	public static boolean isOrdenado()
	{
		Column temp;
		int i;
		
		for (i=0;i<field.size();i++)
		{
			
			temp=(Column)field.get(i);
			if (!temp.isLock())
			{
				return false;
			}
		}
		
		return true;
	}
 
	
	
/*
 * 
 * 
	                                                                           `-:-.`                                                                                                                  
                                                                         .:++++++/-`                                                                                                               
                                                                       ./+++/:-:/+o+:`                                                                                                             
                                                                     `:+o+/:-----::+oo-`                                                                                                           
                                                                   `:+oo+:---------:/+o+-                                                                                                          
                                                                  -+oo+/-------::::--:/oo/.         -/+/:.                                                                                         
                                           `..`                 `:ooo+:::::--:/+++++/--:+oo:`     ./o+/+++:`                                                                                       
                                         -/+++o++:.`           ./ooo+:----::-/+++/.-+/---:+o+-`  .os/:::/+++-                                                                                      
                                        :+++///+++o+/:.`      .+ooo+::------:++++/``:/----:/+o/`.+o/::::::/++:`                                                                                    
                                       :+o+/::::::///+++:-`  ./+oo+:---------/+++++/+/------:+o++o/::::::::/++-                                                                                    
                                      .++++:::::::::--://+/:-/++++:----------:+++++++:-------:+oo+:--:::::::/++-                                                                                   
                                      :oo+/::::::::::---:::/++++/:------------:/+++/:---------:++/----:::::::+++.                                                                                  
                                     `+oo+/::::::::::-------://::---------------:::------------::-----:::::::/++/`                                                                                 
                                     -ooo+/:::::::::::---------------------------::////////::----------:::::::+o+.                                                                                 
                                     -ooo+/:::::::::::----------------------:/osyhhhhhhhhhhhhyys+/:----:::::::/oo/                                                                                 
                                     :oooo/::::::::::::::/++++o+++///:--:/+yhhhhyso/::----:/+osyyhyo/:--::/:::/oso`                                                                                
                                     :oooo/::::::::/+oyyhhhdddddddhhhysoshdhy+:.```           ``.:oyhs+ssyyyyysyhs-`                                                                               
                                     .osso/::::::+shhdhhso/::-----:/+syhdhs:``                    ``--////////+syhhyo/`                                                                            
                                     `osss+::::+yhdhy+-.``          ```-/:.                   ```..-----.````````.-+shyo-                                                                          
                                     `/ssss:::+hddy:``   `.--:::::-..````````````````````` ``.:oyyhhhhhhhyo+:.```````-oyho-                                                                        
                                      -syyysyyhddy-   `-+syhhhhhhhhhyso/:.`````````````````-+yhdhysoooosyhddhhs+-.`````.ohh:                                                                       
                                   ./syhhys+//+yy+.`-+yhhhhddddddddddhdhhyo/-.```````````.+ydhs+:--------:/oyhdhhs+-.````:yh+`               :.                                                    
                                 .+hhhyo:.`````....+yhddddhysooooosyyhhddddhyo:.`````````+hho/--------------:/oyhddyo:.```-yh/              `s-                                                    
                                /ydhs:.``````````:shddddhs+:---------/+syhddddhy/:::::::/oso+////:-------------:/oyddhs:.``:hh.              .`                                                    
                               +hdh/.` ````````./hddddhy+:--------------:/oyhdddhhhhhhhhhhhhhhhhhhhys/--------:::::+yhdhs:.-yd-                                                                    
                              `ydd+` `````````-ohddddhs:::-:----------------/oyyyyssooo++/////////+o+:-----::::::::::+yhdho:yh.                                                                    
                              `ohy:``...`````.sdddddhs::::::::-----------------------------------------:--:::::::::::::ohddhho                                                                     
                               .+sssyyyys+/.`-hdddddy/:::::::::://:------------------------------------:::::::::::+/::::/shdds`                                                                    
                              `+hddddddddhy/-:oyyyyo/::::::::::+hho:------------------------------------:::::::::shh+::::/ohdds-                                                                   
                              -hdddhysoooo+:::::///////::::::::sddy/:------------------------------------:-::::::yddo::::+syhhdh/               `:`                                                
                              :hdddho//////:::/+ossssssso::::::oddh/::-----------------/o/----------:oo:-::::::::+yy+::::+sssyhdh+.              `.                                                
                              -yddddy//////:::+ssssssssss/::::::+o+:::::::-------------/sy/---------:o+::-::::::::::::::://ossyyhhs-                                                               
                           `-/shddddy///////::/+ossooo+//:::::::::::::::::::------------::--------------::::::::::::::::::///+++sddy-                                                              
                          -ohdddddhyo///////::::::::::::::::::::::::::::::::::::-----------------------::::::::::::::::::////////shdy-                                                             
                         `shddddhs+//////////::::::::::::---::::::::::::::::-::----:::://+++oooossoo+//::::::::::::::::::////////+hdh+                                                             
                         .yhddddhs////////////::::::::::::::::::::::::::::::://+oossyyhhhhhhhyyssssyhhhyyso+/:::::::::::::://////+hdh:                                                             
                         `shdddddhs///////////::::::::::::-::::::::///+ossyyyyyyyysso+/::----...``..--:/osyyyyyso+//::::::://////shdy.                                                             
                          -yddddddho//////////::::::::::::::/++osyyhhhyyso++/:--...``````````````````````..--/+osyhhyo::::://///ohdd+                                                              
                           -yddddhs//////////:::::://++ossyyyyyso+/::-....`````````````````````````````````````...-:++-..-::://ohddy`                                                              
                            ./+o++/////::::::::::::+ssso+//:---......`````````````````````````````````````````````````````..-:ohddh-                                                               
                                 -oss+:------........................................```````````````````````````````````````.+hddy:                                                                
                                 /hddh/-..................................................`````````````````````````````````-shdhy-                     ``                                          
                                -ydddy-.........................................................`````````````````````````./yhdhs-                                                                  
                               `shddd+........................................................................`````````.:shddh/`                                                                   
                               `hddddyo/////::--................................................................`````.:shddhs-                                                                     
                              `-sddddddddddddhhyso+:--..........................................................``../shddhy/`                                                                      
                            .+shdddhhyysssyyyyhhhdddys/--........................................................-+yhdddy/.                                                                        
                          .ohddhs+:.```` `````.--/+syhdho//++oo+/:--:////::--........................----..:/ossyhhhhddh+-`                                                                        
                         -hddy/.`````````````      ``-/sys++/++osyyyyysssyyyys+/-.................-/oyyysoshhs+/----::+oyhy+-`     `...                                                            
                         oddh-```````````````````      `.`     ``./:.`````.-:+syy+-...---....--:::syo/-...-:-`        ``.:+yhs-  `:shhys/`                                                         
                         oddh:.`````````````````````                    `     `.+yy:/+ooo/:-/syyyys:`            ``````````-+yh/`+hdo/odds.                                                        
                         :ddds-.-://++oo+:.````````````````    ``````````````````/hhhhyyyssyhy/....````      `````````.::..../hhsddo---sdds`                                                       
                        ``/ysoshhddddddddhs/.```````````````````````````````````.shhso++/..+s-````````````````````````-shsyyyhdddho:---+hdh/                                                       
                      .oysosyddddhhhyyhhddddyo/-..``````````````.-:-..`````````./hdyo++++++++/.`````````````````````.:ydyyssoooo+/--:::/hddo`                                                      
                     :yddysshdddy/:::::/shhddddhysoo++////+/::/+shdhyso+/:::::/ohddho+++++++yds-.......-//-.`````..:ohho------:::::::::+hddo`                                                      
                    /hhhyssshdddh/::::::::/+oyhhhdddddddddddhhyo+/osyhhhhhhhhhhysyddhyssossydddhhyyyyyhhhdhs+///+syhyo:----::://////:/+hddh:                                                       
                   :hhhyssssydddds::::::---.`.-://+oossssoo+/-..```..-::////::-..-+yhhdhhhhyo/++ooooo++/:+yhhhhyys+:-.--:::///+++oosyhdddy:                                                        
                  :yhhhsssssshdddh+::::::--``````````````````````.`.........`````..-::///:-.``````````````..--...`````-/syyhhhhddddddddh+.                                                         
                 -yhhhyssssssyddddo::::::-.``````````.``````..------------------/syyyo/-.`````````````````````````````:hddddddddddddddy`                                                           
                 shhhyssssyyhddddy/::::::-.`````````````````.-::::::------------shhhddddy/.```````````````````````````./hddddhhhyyyyhhy.                                                           
               `/hhhysssyhdddddy+/::::::-.`````````````````.-::::::::::::::::-----::/+yddds-```````````````````````````.odddhdhhyssssyyo.                                                          
               -yhhhssshddddhs+//:::::::-``````...````````.+yhyo/::::::::::::::::::::-:+ddds````````````````````````````-yddyoyddhsssyyy/`                                                         
              `ohhhysshdddhs+///::::::::.`````........````.odddddhyso+++//////:::::::/+ydddy````````````````````````````./hdds:oddysssyyy-                                                         
              :yhhyssydddds////::::::::-.```............```.+hdddddddddddddhhhysssyyhdddddd/```````````````````````......-oddh+ydhsssssyyo`                                                        
              +hhhyssyddddy+///::::::::-.``..............````.:+oyhdddddddddddddddddddhyosdy.````````````````````.........:hddhdhssssssyyy:                                                        
              +hhyssssyddddy+//::::::::-``................````````.-:/yddyyyyyyssoo+/::--/hd+```````````````````......-----oddddsssssssyyyo`                                                       
              +hhysssssyddddds+::::::::..........-..........````.````.odho++///:::::://+osddo.``````````````````...--------:hddddyssssssyyy:                                                       
              +hhsssssssyhdddds:::::::-.........--................```.+dds+ossssosssssyyhhdy-``````````````````...----------yddhhhysssssyyy+`                                                      
              +hysssssssssydddy/::::::-.....--........................-sdddhhhy+/shdddddhy+-..........```.........-.--------sddyohdhsssssyyy.                                                      
              +hsssssssssydddds/::::::-.......-.........................:+syyddo:/syds+:--......................------------sddy/ohdyssssyyy/                                                      
              +yssssssssyddddy+/:::::::-.......-............................-hdy:--yd+..............................-------/yddyshdhssssssyyo`                                                     
              +ysssssssshddddo//:::::---.........-..........................-hdy:--yd+.............................---..--:ydddddhysssssssyys.                                                     
              +ssssssssyddddh+///:::::--.........-..........................:hdy:--ydo...............................----+yddddhhsssssssssyyy-                                                     
              /ssssssssyhddddyo++/:::::-..................................../ddh++ohds...............................-:+yhddhhyyssssssssssyyy/                                                     
              /sssssssssyddddddddhy+::::-........-............-....--......./ddho++sdy..--.....-...................-/ohddddhysssssssssssssyyy+      
 */
	
	

}
