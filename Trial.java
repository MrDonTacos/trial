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
	
	public static boolean isNumeric(String cadena) {

        boolean resultado;
        try {
            Integer.parseInt(cadena);
            resultado = true;
        } catch (NumberFormatException excepcion) {
            resultado = false;
        }

        return resultado;
    }
 
	
	private int[][] transiciones(ArrayList<String> automata)
	{
		int estados = Integer.parseInt(automata.get(0).trim());
		String[] alfabeto = automata.get(1).trim().split(",");
		int iteradorr = 0;
		int f =0;
		int[][] transiciones = new int[estados][alfabeto.length];
		for (var arreglo : automata)
		{
			if(iteradorr==1)
				for(int i = 0; i<alfabeto.length; i++)
					abc.add(alfabeto[i]);
			if(iteradorr<=2)
				iteradorr++;
			else
			{
				if(!arreglo.isEmpty())
				{					
						for(int j = 0; j< alfabeto.length; j++)
							transiciones[f][j] = Integer.parseInt(arreglo.split("(\\s)")[j]);
				}
				f++;
			}
		}
		return transiciones;
	}
	
	private boolean AnalizarAutomata(ArrayList<String> automata)
	{
		//1. Número de estados ejemplo: 4
		//2. Alfabeto ejemplo: 1,2... "a", "b" separado por comas
		//3. Se omite q0
		//3. Los estados de aceptación ejemplo: 1,3
		//4. transiciones
		int iteradorr = 0;
		int f =0;
		
		if(!isNumeric(automata.get(0).trim()))
			return false;
		
		int estados = Integer.parseInt(automata.get(0).trim());
		String[] alfabeto = automata.get(1).trim().split(",");
		
		String[] aceptacion = automata.get(2).trim().split(",");
		int[][] transiciones = new int[estados][alfabeto.length];
		
		for(int i=0;i<aceptacion.length;i++) 
			estadosDeAceptacion.add(aceptacion[i]);
		
		for(var item: estadosDeAceptacion)
			if(Integer.parseInt(item) >= estados)
				return false;
		
		//Tiene que haber estados de aceptación
		if(aceptacion.length == 0)
			return false;
		
		
		//Los estados de aceptaciÃ³n no pueden ser mayores que los estados

		if(aceptacion.length > estados)
			return false;
		//La longitud de la transiciones no puede ser mayor que el alfabeto
		if(alfabeto.length != transiciones[0].length)
			return false;
		
		//Para recorrer cada elemento del arrraylist
		for (var arreglo : automata)
		{
			if(iteradorr<=2)
			iteradorr++;
			else
			{
				if(!arreglo.isEmpty())
				{					
						for(int j = 0; j< alfabeto.length; j++)
							transiciones[f][j] = Integer.parseInt(arreglo.split("(\\s)")[j]);
				}
				f++;
			}
		}
		//De izquierda a derecha la longitd del arreglo
		if(transiciones[0].length != alfabeto.length)
			return false;
		//De arriba abajo la longitud del arreglo
		if(transiciones.length != estados)
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
		frame.setBounds(100, 100, 563, 473);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		JTextPane minimizarAutomata = new JTextPane();
		JButton btnOpen = new JButton("Reducir Automata");
		btnOpen.setEnabled(false);
		btnOpen.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {			
							int [][] quinta = transiciones(documento);
							reductorARD reducto = new reductorARD();
							int estados = 0;
							ArrayList<Integer> EstadosAceptacion = new ArrayList<Integer>();
							ArrayList<String> Transiciones = new ArrayList<String>();
							autoReducido = reducto.ReductorAFD(quinta, documento.get(2).split(","), Integer.parseInt(documento.get(0)));
							for(var item: autoReducido)
							{								
								if(item.split(",")[1].equals("true"))	
									EstadosAceptacion.add(estados);
								
								Transiciones.add(item.split(",")[0].split("_")[0] + " " +item.split(",")[0].split("_")[1]);
								estados++;
							}
							//textPane.setText(documento);
							minimizarAutomata.setText(minimizarAutomata.getText() + estados + "\n");
							for(var item : abc)
								minimizarAutomata.setText(minimizarAutomata.getText() + item + ",");
							minimizarAutomata.setText(minimizarAutomata.getText() + "\n");
							for(var aceptacion: EstadosAceptacion)
								minimizarAutomata.setText(minimizarAutomata.getText() + aceptacion + ",");
							minimizarAutomata.setText(minimizarAutomata.getText() + "\n");
							for(var trans: Transiciones)
								minimizarAutomata.setText(minimizarAutomata.getText() + trans + "\n");
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
		 
		
		btnOpen.setBounds(232, 156, 206, 25);
		frame.getContentPane().add(btnOpen);
		JTextPane txtpnDsadadasd = new JTextPane();
		txtpnDsadadasd.setEditable(false);
		txtpnDsadadasd.setBounds(232, 195, 206, 132);
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
		btnAnalizar.setEnabled(false);
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
				String estadoFinal=reducto.analizarAutomataNoReducido(abc, transiciones(documento),cadena);
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
		btnAnalizar.setBounds(232, 359, 206, 25);
		frame.getContentPane().add(btnAnalizar);
		
		
		minimizarAutomata.setEditable(false);
		minimizarAutomata.setBounds(232, 12, 206, 132);
		frame.getContentPane().add(minimizarAutomata);
		
		JButton btnReducirAutomata = new JButton("Abrir Automata");
		btnReducirAutomata.addMouseListener(new MouseAdapter() {
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
								 btnOpen.setEnabled(true);
								 btnAnalizar.setEnabled(true);
								}
								else
								{
									System.out.print("AUTOMATA NO VALIDO");
									btnOpen.setEnabled(false);
									btnAnalizar.setEnabled(false);
								}
						}else {
							JOptionPane.showMessageDialog(null, "Archivo no compatible");
						}
					}
				}
							
			}
		});
		btnReducirAutomata.setBounds(12, 156, 196, 25);
		frame.getContentPane().add(btnReducirAutomata);
	}
}
