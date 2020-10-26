package trial;

import java.awt.EventQueue;
import java.awt.Window.Type;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.JTextPane;

import lectorAutomatasFD.LectorAutomataFD;
import reductorARD.reductorARD;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class Trial {

	
	JFileChooser seleccionar = new JFileChooser();
	File archivo;
	FileInputStream entrada;
	private JFrame frame;
	private JTextField textField;
	ArrayList<String> abc= new  ArrayList<String>();//abecedario
	ArrayList<String> autoReducido= new  ArrayList<String>();
	ArrayList<String> rect1= new  ArrayList<String>();
	ArrayList<String> documento;
	ArrayList<String> estadosDeAceptacion= new ArrayList<String>();
	/**
	 * Launch the application.
	 */
	//Método auxiliar robado de mi trabajao anterior para poder abrir archivos txt jeje
	private int[][] quintupla(ArrayList<String> automata)
	{
		int estados = Integer.parseInt(automata.get(0).trim());
		String[] alfabeto = automata.get(1).trim().split(",");
		int quintu = 0;
		int f =0;
		int[][] quintupla = new int[estados][alfabeto.length];
		for (var arreglo : automata)
		{
			if(quintu==1)
				for(int i = 0; i<alfabeto.length; i++)
					abc.add(alfabeto[i]);
			if(quintu<=2)
				quintu++;
			else
			{
				if(!arreglo.isEmpty())
				{					
						for(int j = 0; j< alfabeto.length; j++)
							quintupla[f][j] = Integer.parseInt(arreglo.split("(\\s)")[j]);
				}
				f++;
			}
		}
		return quintupla;
	}
	
	private boolean AnalizarAutomata(ArrayList<String> automata)
	{
		//1. Número de estados ejemplo: 4
		//2. Alfabeto ejemplo: 1,2... "a", "b" separado por comas
		//3. Se omite q0
		//3. Los estados de aceptación ejemplo: 1,3
		//4. Quintupla
		int quintu = 0;
		int f =0;
		int estados = Integer.parseInt(automata.get(0).trim());
		String[] alfabeto = automata.get(1).trim().split(",");
		String[] aceptacion = automata.get(2).trim().split(",");
	
		int[][] quintupla = new int[estados][alfabeto.length];
		
		for(int i=0;i<aceptacion.length;i++) 
			estadosDeAceptacion.add(aceptacion[i]);
			
		

		
		//Tiene que haber estados de aceptación
		if(aceptacion.length == 0)
			return false;
		
		//Los estados de aceptación no pueden ser mayores que los estados

		//Tiene que haber estados de aceptaciÃ³n
		if(aceptacion.length == 0)
			return false;
		
		//Los estados de aceptaciÃ³n no pueden ser mayores que los estados

		if(aceptacion.length > estados)
			return false;
		//La longitud de la quintupla no puede ser mayor que el alfabeto
		if(alfabeto.length != quintupla[0].length)
			return false;
		
		//Para recorrer cada elemento del arrraylist
		for (var arreglo : automata)
		{
			if(quintu<=2)
			quintu++;
			else
			{
				if(!arreglo.isEmpty())
				{					
						for(int j = 0; j< alfabeto.length; j++)
							quintupla[f][j] = Integer.parseInt(arreglo.split("(\\s)")[j]);
				}
				f++;
			}
		}
		//De izquierda a derecha la longitd del arreglo
		if(quintupla[0].length != alfabeto.length)
			return false;
		//De arriba abajo la longitud del arreglo
		if(quintupla.length != estados)
			return false;
		
		return true;
	}
	
	private ArrayList<String> AbrirArchivo(File archivo) {
		ArrayList<String> documento = new ArrayList<String>();
		try (BufferedReader br = new BufferedReader(new FileReader(archivo))) {
		    String line;
		    while ((line = br.readLine()) != null) {
		    	documento.add(line);
		    }
		}
		catch(Exception e)
		{
			System.out.print(e);
		}
		return documento;
	}
	
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Trial window = new Trial();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public Trial() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 450, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);

		JButton btnOpen = new JButton("Abrir Automata");
		btnOpen.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if(seleccionar.showDialog(null, "Abrir")==JFileChooser.APPROVE_OPTION)
				{
					archivo = seleccionar.getSelectedFile();
					if(archivo.canRead())
					{
						if(archivo.getName().endsWith("txt"))
						{

							 documento = AbrirArchivo(archivo);

							
							if(AnalizarAutomata(documento))
							{								
							int [][] quinta = quintupla(documento);
							reductorARD reducto = new reductorARD();

							autoReducido = reducto.ReductorAFD(quinta, documento.get(2).split(","), Integer.parseInt(documento.get(0)));
							for(var item: autoReducido)
								if(item.split(",")[1].equals("true"))
									System.out.println(item.split(",")[0] + " Este renglón representa el estado de aceptación");
								else
									System.out.println(item.split(",")[0]);
						
							}
							else
							{
								System.out.print("AUTOMATA NO VALIDO");
							}
							//textPane.setText(documento);
						}else {
							JOptionPane.showMessageDialog(null, "Archivo no compatible");
						}
					}
				}
			}
		});
		
		//
					//ab //3
		/*		___a____b______
		 * 		|  1    2
		 *    q2|  2    2
		 * 		|  2    3
		 * 		|
		 * 		|
		 */		
		 
		
		btnOpen.setBounds(12, 201, 196, 25);
		frame.getContentPane().add(btnOpen);
		
		JTextPane txtpnDsadadasd = new JTextPane();
		txtpnDsadadasd.setEditable(false);
		txtpnDsadadasd.setBounds(232, 12, 206, 132);
		frame.getContentPane().add(txtpnDsadadasd);
		
		textField = new JTextField();
		textField.addKeyListener(new KeyAdapter() {
			@Override
			public void keyTyped(KeyEvent e) {
				String a  = String.valueOf(e.getKeyChar());
				String b;
				boolean flag = false;
				for(var item : abc)
				{
					b = item;
						if(a.equals(b)) {
							flag = true;
						}
				}
				if(flag == false)
				{
					textField.setText(null);
					System.out.println("CARACTER INCORRECTO");
					return;
				}
				else
				{
					rect1.add(String.valueOf(e.getKeyChar()));
					
				}
			}
		});

		frame.getContentPane().add(textField);
		textField.setBounds(12, 12, 196, 132);
		textField.setColumns(10);
		
		JButton btnAnalizar = new JButton("Analizar ");
		btnAnalizar.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if(archivo == null)

					System.out.print("NO SE HA CARGADO NINGÚN AUTOMATA");


				String palabra = textField.getText();
				ArrayList<String> cadena = new ArrayList<String>();
				for(int i=0; i<palabra.length(); i++)
					cadena.add(String.valueOf(palabra.charAt(i)));
				
				reductorARD reducto = new reductorARD();
				boolean flag=false;
				String estadoFinal=reducto.analizarAutomataNoReducido(abc, quintupla(documento),cadena);
				System.out.println("la cadena finalizo en el estado: Q"+estadoFinal);
				 txtpnDsadadasd.setText("la cadena finalizo en el estado: Q"+estadoFinal+"\n");
			System.out.println("el cual es un estado de ");
			txtpnDsadadasd.setText(txtpnDsadadasd.getText()+"el cual es un estado de \n" );
			for(var item: estadosDeAceptacion) {
			
				if(item.equals(estadoFinal)) {
					flag=true;
				}
			}
			if(flag==true) {
				System.out.println("ACEPTACION");
				txtpnDsadadasd.setText(txtpnDsadadasd.getText()+"ACEPTACION");
			}
			else {
				System.out.println("NO ACEPTACION");
				txtpnDsadadasd.setText(txtpnDsadadasd.getText()+"NO ACEPTACION");
				
			}

			}
		});
		btnAnalizar.setBounds(232, 201, 206, 25);
		frame.getContentPane().add(btnAnalizar);
	}
}
