package clases;


import java.util.ArrayList;
import java.util.Collections;

public class Column  extends ArrayList
{
	private ArrayList ideal;
	private int uB;
	private int errors;
	private int dif;
	private boolean lock=false;

	
	
	/**
	 * 		
	 * @return COPIA DE LA COLUMNA 
	 */
	public ArrayList copy()
	{
		int i;
		ArrayList copy=new ArrayList();
		for (i=0;i<this.size();i++)
		{
			copy.add(this.get(i));
		}

		return copy;
	}
	
	public void generaIdeal()
	{
		ArrayList ideal=this.copy();
		Collections.sort(ideal);
		Collections.reverse(ideal);
		this.ideal=ideal;
	}
	
	/**
	 * QUITA ELEMENTO SUPERIOR DEL ARRAYLIST
	 */
	public Object quitar()
	{
		int altura=this.size();
		return this.remove(altura-1);
	}
	
	/**
	 * 
	 * @return EL NUMERO MAYOR DE LA COLUMNA, EN CASO DE QUE LA COLUMNA ESTE VACIA RETORNA 0
	 */
	public int biggerNumber()
	{
		if (ideal.size()==0)
		{
			return 0;
		}
		
		return (int)ideal.get(0);
	}
	
	/**
	 * 
	 * @param n NUMERO EL CUAL SE DESEA USAR COMO COTA SUPERIOR
	 * @return
	 */
	public int biggerNumber(int n)
	{
		if (ideal.size()==0)
		{
			return 0;
		}

		ArrayList temp=new ArrayList(ideal);
		
		while((int)temp.get(0)>n-1)
		{
			temp.remove(0);
		}
		return (int)temp.get(0);
	}
	//*****************************************************
	//*****************************************************
	/**************GET Y SETS*****************************/
	public int getuB() 
	{
		return uB;
	}
	public void setuB() 
	{
		int i;
		int contador=0;
		for (i=0;i<this.size();i++)
		{
			if (this.get(i)==ideal.get(i))
			{
				contador++;
			}
			else
			{
				break;
			}
		}
			
		uB=this.size()-contador;
			
		if (uB==0)
		{
			setLock(true);
		}
	}
	
	public boolean isLock() 
	{
		boolean asd=lock;
		return lock;
	}
	
	public void setLock(boolean lock) 
	{
		this.lock = lock;
	}
	
	public void setLock() 
	{
		int i;
		ArrayList normal=new ArrayList();
		for (i=0;i<this.size();i++)
		{
			normal.add(this.get(i));
		}
		
		if (normal.equals(ideal))
		{
			lock=true;
		}
		else
		{
			lock=false;
		}
			
	}
	
	public int getDif() 
	{
		return dif;
	}
	
	public void setDif() 
	{
		if (this.size()==0)
		{
			dif=0;
			return;
		}
		int bigNum=(int)ideal.get(0);
		int lilNum=(int)ideal.get((int)ideal.size()-1);
		dif=bigNum-lilNum;
	}
	
	public int getErrors() 
	{
		return errors;
	}
	
	public void setErrors() 
	{
		int i;
		int contador=0;
		for (i=0;i<this.size();i++)
		{
			if (this.get(i)!=ideal.get(i))
			{
				contador++;
			}
			else
			{
				break;
			}
		}
		
		errors=contador;
	}

	
	

}
