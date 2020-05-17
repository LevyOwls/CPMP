package clases;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.GridLayout;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

import com.sun.javafx.tk.Toolkit;
import javax.swing.JSplitPane;
import javax.swing.JInternalFrame;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.RowSpec;
import com.jgoodies.forms.layout.FormSpecs;
import java.awt.FlowLayout;
import javax.swing.BoxLayout;
import javax.swing.SpringLayout;
import java.awt.GridBagLayout;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class FrameIn extends JFrame {

	public static int columns;
	public static int maxSize;
	private JPanel contentPane;
	private int mov=0;
	private ArrayList frames;
	private Font font=new Font("Calibri", Font.BOLD, 20);
	private JLabel lblMovs;

	/**
	 * Create the frame.
	 */
	public FrameIn(Field f) {
		
		setBackground(Color.white);
		columns=f.getColumns();
		maxSize=f.getHMax();
		this.frames=f.frames;
		Dimension screenSize = getToolkit().getScreenSize();
		setSize( 1366, 768 );
		//CENTRAR JFRAME
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		getContentPane().setLayout(new BorderLayout(0, 0));
		
		JPanel panel = new JPanel(new GridLayout(5,8));
		//PANEL DE ANALISIS
		//JPanel panel = new JPanel(new GridLayout(maxSize,columns));
		getContentPane().add(panel, BorderLayout.CENTER);
		//PANEL DE LOS BOTONES
		JPanel panel_1 = new JPanel();
		getContentPane().add(panel_1, BorderLayout.SOUTH);
		
		JButton btnBack = new JButton("Atras");
		btnBack.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) 
			{
				mov--;
				
				if (mov==-1)
				{
					 JOptionPane.showMessageDialog(null, "Este es el estado inicial, no se puede retroceder mas pero buen intento");
					 mov++;
				}
				else
				{
					lblMovs.setText("              Movimientos:  "+mov);
					panel.removeAll();
					update(mov, panel);
					panel.repaint();
					panel_1.repaint();
					setVisible(true);
					
				}
			}
		});
		panel_1.add(btnBack);
		
		JButton btnNext = new JButton("Adelante");
		btnNext.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) 
			{
				mov++;
				if (mov==frames.size())
				{
					JOptionPane.showMessageDialog(null, "Esta es la solucion, no se puede avanzar mas pero buen intento");
				}
				else
				{
					lblMovs.setText("              Movimientos:  "+mov);
					panel.removeAll();
					update(mov,panel);
					panel.repaint();
					panel_1.repaint();
					setVisible(true);
				}
			}
		});
		panel_1.add(btnNext);
		
		lblMovs= new JLabel("              Movimientos:  "+mov);
		panel_1.add(lblMovs);
	
	
		update(mov,panel);
		/*
		int i;
		for (i=0;i<80;i++)
		{
			JLabel lbl=new JLabel(""+i+"",SwingConstants.CENTER);
			lbl.setBackground(Color.CYAN);
			lbl.setOpaque(true);
			lbl.setBorder(new LineBorder(Color.gray,8,true));
			lbl.setFont(font);
			panel.add(lbl);
		}
		    */
		
		
	
	
				
		setVisible(true);

	}
	
	public void init(JPanel panel)
	{
		//OBTIENE EL PRIMER FIELD
		Field field=(Field)frames.get(0);
		//ESTO FACILITA MUCHO LAS COSAS CON EL GRIDLAYER
		int matriz[][]=toMatrix(field);
		Column temp;
		int aux;
		int size=maxSize;
		int i,j;

		
		int n =matriz.length;
		int m = matriz[0].length;
		//SHOWTIME PERO VERSION ESTO
		for (i=0;i<n;i++)
		{
			for (j=0;j<m;j++)
			{
				aux=matriz[i][j];
				if (aux==0)
				{
					JLabel lbl=new JLabel("",SwingConstants.CENTER);
					//lbl.setBorder(new LineBorder(Color.white));
					panel.add(lbl);
				}
				else
				{
					JLabel lbl=new JLabel(""+aux+"",SwingConstants.CENTER);
					lbl.setBackground(Color.CYAN);
					lbl.setOpaque(true);
					lbl.setBorder(new LineBorder(Color.decode("#BAB9B9"),3,true));
					lbl.setFont(font);
					panel.add(lbl);
				}
			}
		}
		panel.setBackground(Color.white);
		repaint();
		
		
	}
	
	public void update(int num,JPanel panel)
	{
		Field field=(Field)frames.get(num);
		int matriz[][]=toMatrix(field);
		Column temp;
		int aux;
		int size=maxSize;
		int i,j;
		int n =matriz.length;
		int m = matriz[0].length;
		//SHOWTIME PERO VERSION ESTO
		for (i=0;i<n;i++)
		{
			for (j=0;j<m;j++)
			{
				aux=matriz[i][j];
				if (aux==0)
				{
					JLabel lbl=new JLabel("",SwingConstants.CENTER);
					//lbl.setBorder(new LineBorder(Color.white));
					panel.add(lbl);
				}
				else
				{
					JLabel lbl=new JLabel(""+aux+"",SwingConstants.CENTER);
					lbl.setBackground(Color.CYAN);
					lbl.setOpaque(true);
					lbl.setBorder(new LineBorder(Color.decode("#BAB9B9"),3,true));
					lbl.setFont(font);
					panel.add(lbl);
				}
			}
		}
		panel.setBackground(Color.white);
		repaint();

	}

	public int getMov() {
		return mov;
	}

	public void setMov(int mov) {
		this.mov = mov;
	}
	
	public int[][] toMatrix(Field field)
	{
		int matriz[][] =new int [columns][maxSize];
		
		int i,j;
		
		for (i=0;i<maxSize;i++)
		{
			for (j=0;j<columns;j++)
			{
				matriz[j][i]=0;
			}
		}
		
		Column temp;
		int aux;
		for (i=0;i<field.size();i++)
		{
			temp=field.getCol(i);
			for (j=0;j<temp.size();j++)
			{
				aux=(int)temp.get(j);
				matriz[i][j]=aux;
			}
		}
		
		//printMatrix(matriz);
		matriz=rotate(matriz);
		matriz=rotate(matriz);
		matriz=rotate(matriz);
		//System.out.println(" ");
		//printMatrix(matriz);

		
		return matriz;
		
	}
	
	public void printMatrix(int [][] matriz)
	{
	
		for (int x=0; x < matriz.length; x++) {
			  System.out.print("|");
			  for (int y=0; y < matriz[x].length; y++) {
			    System.out.print (matriz[x][y]);
			    if (y!=matriz[x].length-1) System.out.print("\t");
			  }
			  System.out.println("|");
			}
	}
	
	int [][] rotate(int [][] input){

		int n =input.length;
		int m = input[0].length;
		int [][] output = new int [m][n];

		for (int i=0; i<n; i++)
			for (int j=0;j<m; j++)
				output [j][n-1-i] = input[i][j];
		return output;
		}
	
	
}
