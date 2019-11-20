package clases;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.acl.LastOwnerException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

import javax.sound.sampled.Line;

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
	public int getMovs() {
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
	 * 
	 * @param cotaSuperior					VARIABLE EXCLUYENNTE, CONSIDERANDO EL DOMINIO DE LOS POSIBLES NUMEROS RANDOM EN [0 ,(N-1)]
	 * @return
	 */
	public static int random(int cotaSuperior)
	{
		int h= (int) Math.floor(Math.random()*cotaSuperior);
		return h;
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
	 * REVISA LAS COLUMNAS PARA DEFINIR SI ESTA ORDENADO O NO
	 * @return				TRUE EN CASO DE QUE FIELD ESTE ORDDENADO, FALSE EN CASO CONTRARIO
	 */
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
	
	/**
	 * REVISA SI PUEDE COLOCARSE SOBRE ALGUNA COLUMNA
	 * @return 		NUMERO DE COLUMNA EN LA CUAL PUEDE POSICIONARSE O -1 EN CASO DE NO ENCONTRAR NINGUNA
	 */
	public static int posibleMove(int n,int c_o)
	{
		ArrayList tops=getTops();
		int i;
		int aux;
		int dif=n;
		int col=-1;
		Column temp;
		ArrayList desordenados=disorderlyElements();
		int bigDis=(int)desordenados.get(desordenados.size()-1);
		for (i=0;i<tops.size();i++)
		{
			aux=(int)tops.get(i);
			if (n<=aux)
			{
				if (dif>aux-n)
				{
					temp=(Column)field.get(i);
					//QUE NO SEA LA COLUMNA ORIGEN, QUE TENGA ESPACIO DISPONIBLE Y QUE ESTE ORDENADA
					if (temp.size()<hMax && i!=c_o && temp.isLock())
					{
						col=i;
						break;
					}
					
				}
			}
			//COLUMNA VACIA
			else
			{
				if (aux==0 && n==bigDis)
				{
					col=i;
					break;
				}
			}
			
			
		}
		return col;
	}
	
	/**
	 * MUEVE EL ELEMENTO SUPERIOR DE LA COLUMNA ORIGEN A LA COLUMNA DESTINO, NO VERIFICA ALTURA
	 * @param c_o COLUMNA ORIGEN
	 * @param c_d COLUMNA DESTINO
	 */
	public static void atomic_Move(int c_o,int c_d) 
	{
		Column temp=(Column)field.get(c_o);
		int aux=(int)temp.quitar();
		temp=(Column)field.get(c_d);
		temp.add(aux);
		movs++;
		review();
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
	
	/**
	 * 
	 * @return LA CANTIDAD DE COLUMNAS 
	 */
	public static int size()
	{
		return field.size();
		
	}
	
	/**
	 * FUNCION QUE PERMITE CONOCER SI EXISTEN ESPACIOS VACIOS DENTRO DE FIELD
	 * @return					RETORNA -1 EN CASO DE NO EXISTIR, EN CASO CONTRARIO RETORNA EL NUMERO DE LA COLUMNA VACIA.
	 */
	public static int firstEmpty()
	{
		int i;
		Column temp;
		
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
	//**********************************************************************************************************************
	
	/**
	 * 
	 * @param c_o		COLUMNA ORIGEN
	 * @return			MEJOR COLUMNA DE DONDE SACAR UN ELEMENTO PARA RELLENAR LA COLUMNA c_o
	 * 					EN CASO DE QUE NO SE PUEDA, RETORNAR -1
	 */
	public static int columnBestFill(int c_o)
	{
		ArrayList tops=getTops();
		
		int i;
		int aux;
		Column temp;
		int dif=1000;
		//SE OBTIENE EL TOP DE LA COLUMNA ORIGEN
		int columnTop=(int)tops.get(c_o);
		int bestCol=-1;
		//EN CASO DE QUE LA COLUMNA NO ESTE VACIA
		if (columnTop!=0)
		{
			for (i=0;i<tops.size();i++)
			{
				temp=(Column)field.get(i);
				//QUE NO ANALIZE LA COLUMNA DE ORIGEN
				if (i!=c_o)
				{
					aux=(int)tops.get(i);
					//EN CASO DE QUE AUX SEA MENOR A COLUMNTOP, ESTE DESORDENADO Y NO SEA 0 (RECORDAR QUE 0 ES VACIO)
					if (aux<=columnTop && !temp.isLock() && aux!=0)
					{
						//SI LA DIFERENCIA ANTERIOR ES MAYOR A LA DIFERENCIA ACTUAL
						if (dif>(columnTop-aux))
						{
							bestCol=i;
							dif=columnTop-aux;
						}
					}
				}
			}
		}
		else
		{
			ArrayList disorder=disorderlyElements();
			if (disorder.size()==0)
			{
				return -1;
			}
			int dis=(int)disorder.get(disorder.size()-1);
			
			for (i=0;i<tops.size();i++)
			{
				temp=(Column)field.get(i);
				//QUE NO ANALIZE LA COLUMNA DE ORIGEN
				if (i!=c_o)
				{
					aux=(int)tops.get(i);
					//EN CASO DE QUE EXISTA EL NUMERO MAYOR DENTRO DE LOS TOPS Y ESTE DESORDENADA LA COLUMNA DE DONDE SE OBTENGA
					if (dis==aux && !temp.isLock())
					{
						bestCol=i;
					}
				}
			}
			
		}

		
		return bestCol;
	}
	
	/**
	 * MEDIANTE BUUBLESORT ORDENA COLUMNAS POR TAMAÑO DE MENOR A MAYOR
	 * 
	 * @param size			ARRAYLIST CON TAMAÑO DE CADA COLUMNA
	 * @param columna		ARRAYLIST CON EL NUMERO DE COLUMNA
	 * @return				ARRAYLIST CON NUMERO DE COLUMNAS ORDENADA DE MENOR A MAYOR
	 */
	public static ArrayList bubbleSort(ArrayList size,ArrayList columna)
	{
		 boolean sorted=false;
		 int aux,i;
		 while (!sorted)
		 {
			 sorted = true;
			 for (i=0;i<size.size()-1;i++)
			 {
				 //SI I>I+1 CAMBIAN DE POSICION
				 if ((int)size.get(i)>(int)size.get(i+1))
				 {
					 //SE ORDENA POR TAMAÑO
					 aux=(int)size.get(i);
					 size.set(i,size.get(i + 1));
					 size.set(i + 1,aux);
					 //SE ORDENA POR COLUMNA
					 aux=(int)columna.get(i);
					 columna.set(i,columna.get(i + 1));
					 columna.set(i + 1,aux);
					 
					 sorted=false;
				 }
			 }
		 }
		 
		 return columna;
	}
	/**
	 * EL PREPROCESO, INTENTA DISMINUIR LA CANTIDAD DE PASOS QUE HACER A FUTURO.
	 */
	public static void AIOfill()
	{
		int i;
		Column temp;
		ArrayList size=new ArrayList();
		ArrayList columna=new ArrayList();
		//SE LEE EL TAMAÑO Y LA COLUMNA 
		for (i=0;i<field.size();i++)
		{
			temp=(Column)field.get(i);
			size.add(temp.size());
			columna.add(i);
		}
		
		//SE ORDENA DE MAYOR A MENOR, CON UN COLUMNSORT PARA REPETIR LOS PASOS EN AMBAS LISTAS
		
		columna=bubbleSort(size, columna);
		//LA IDEA ES TENER UNA LISTA CON LAS COLUMNAS ORGANIZADAS POR TAMAÑO, DE MAYOR A MENOR
		 //PARA ESTO ULTIMO, SE INVIERTE LA LISTA COLUMNAS
		int aux;
		 Collections.reverse(columna);
		 
		 //System.out.println(columna);
		 
		 //UNA VEZ OBTENIDAS LAS COLUMNAS POR ORDEN DE TAMAÑO (MENOR A MAYOR) SE RELLENAN
		 int c_d;

		 for (i=0;i<columna.size();i++)
		 {
			 //SE OBTIENE LA COLUMNA
			 c_d=(int)columna.get(i);
			 temp=(Column)field.get(c_d);
			 //SE VERIFICA EL TAMAÑO
			 
			 //SI LA ALTURA DE LA COLUMNA TEMP NO SUPERA LA ALTURA MAXIMA Y ESTA ORDENADO
			 if (temp.size()<hMax && temp.isLock())
			 {
				 aux=columnBestFill(c_d);
				 
				 //MIENTRAS SE PUEDA Y NO SUPERE LA ALTURA MAXIMA
				 while(aux!=-1 && temp.size()<hMax)
				 {
					 atomic_Move(aux,c_d);
					 aux=columnBestFill(c_d);
				 }
			 }
			 
		 }
	}
	
	public static void AIOEmptyFill(int c_o)
	{
		ArrayList tops=getTops();
		Column temp;
		int i;
		int col=-1;
		int best=0;
		for (i=0;i<field.size();i++)
		{
			//SE OBTIENE FIELD PARA VER SI LA COLUMNA ESTA BLOQUEADA O NO.
			temp=(Column)field.get(i);
			
			if (!temp.isLock() && i!=c_o)
			{
				if ((int)tops.get(i)>best)
				{
					col=i;
					best=(int)tops.get(i);
				}
			}
			
		}
		
		if (col!=-1)
		{
			atomic_Move(col,c_o);
		}
	}
	
	/**
	 * Se sacrifica una columna para la columna objetivo.
	 * @param c_o
	 */
	public static void AIOsacrifice(int c_o)
	{
		ArrayList size=new ArrayList();
		ArrayList columna=new ArrayList();
		int i;
		Column temp;
		//SE LEE EL TAMAÑO Y LA COLUMNA 
		for (i=0;i<field.size();i++)
		{
			temp=(Column)field.get(i);
			size.add(temp.size());
			columna.add(i);
		}
		
		//SE ORDENA DE MAYOR A MENOR, CON UN COLUMNSORT PARA REPETIR LOS PASOS EN AMBAS LISTAS
		
		boolean sorted = false;
		int aux;
		//BUBBLESORT
		 while (!sorted)
		 {
			 sorted = true;
			 for (i=0;i<size.size()-1;i++)
			 {
				 //SI I>I+1 CAMBIAN DE POSICION
				 if ((int)size.get(i)>(int)size.get(i+1))
				 {
					 //SE ORDENA POR TAMAÑO
					 aux=(int)size.get(i);
					 size.set(i,size.get(i + 1));
					 size.set(i + 1,aux);
					 //SE ORDENA POR COLUMNA
					 aux=(int)columna.get(i);
					 columna.set(i,columna.get(i + 1));
					 columna.set(i + 1,aux);
					 
					 sorted=false;
				 }
			 }
		 }
		 
		 //UNA VEZ OBTENIDAS LAS COLUMNAS ORDENADAS POR ALTURA, SE REVISA PARA SACRIFICIO LA MENOR
		 //SI ESTAN ORDENADAS, Y SE PUEDE ORDENAR EN OTRO LADO, MEJOR
		 int columnTop;
		 ArrayList tops=getTops();
		 int selector;
		 int topC_o=(int)tops.get(c_o);
		 for (i=0;i<field.size();i++)
		 {
			 aux=(int)columna.get(i);
			 columnTop=(int)tops.get(aux);
			 temp=(Column)field.get(aux);
			 //SACRIFICIO FUNCIONA CON COLUMNAS ORDENADAS
			 if (temp.isLock())
			 {
				 while (columnTop<topC_o || (int)tops.get(aux)==0)
				 {
					 selector=posibleMove(columnTop,aux);
					 if (selector!=-1)
					 {
						 atomic_Move(aux,selector);
					 }
					 else
					 {
						 break;
					 }
					 //SE ACTUALIZA EL TOP
					 tops=getTops();
					 columnTop=(int)tops.get(aux);
					 
					 //SI EL TOP ES 0, SE CIERRA EL BUCLE
					 if (columnTop==0)
					 {
						 break;
					 }
				 } 
				 //SI ESTO OCURRE ME GENERO UN ESPACIO, SI NO SE LOGRO SE SIGUE INTENTANDO
				 if (columnTop==0 || columnTop>(int)tops.get(aux) )
				 {
					 break;
				 }
			 }
			
			 
		 }
	}

	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
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
	
	

