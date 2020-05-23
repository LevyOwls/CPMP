package clases;

import java.util.ArrayList;

public class Contenedor 
{
	private int prioridad;
	private Coordenadas coordenadas;
	private ArrayList historial=new ArrayList();
	private ArrayList cambios=new ArrayList();
	
	
	public Contenedor(int p)
	{
		prioridad=p;
	}
	public int getPrioridad() {
		return prioridad;
	}
	public void setPrioridad(int prioridad) {
		this.prioridad = prioridad;
	}
	public Coordenadas getCoordenadas() {
		return coordenadas;
	}
	public void setCoordenadas(int x, int y) 
	{
		if (historial.size()==0)
		{
			Coordenadas nuevo=new Coordenadas(x,y);
			this.coordenadas = nuevo;
			addHistorial();
			cambios.add(0);
		}
		else
		{
			//SI DIFIEREN ME DICE DONDE HAY CAMBIOS
			if (coordenadas.getX()!=x || coordenadas.getY()!=y)
			{
				Coordenadas nuevo=new Coordenadas(x,y);
				this.coordenadas = nuevo;
				addHistorial();
				cambios.add(historial.size()-1);
			}
			else
			{
				Coordenadas nuevo=new Coordenadas(x,y);
				this.coordenadas = nuevo;
				addHistorial();
			}
		}
		
	}
	
	public void addHistorial()
	{
		
		historial.add(coordenadas);
	}
	
}
