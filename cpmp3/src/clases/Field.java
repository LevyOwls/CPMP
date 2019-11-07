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
		
		//SE ASIGNA TAMAÑO CERO A TODOS LOS TIERS
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
