import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Scanner;

import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.filechooser.FileNameExtensionFilter;

public class TextEditor extends JFrame implements ActionListener{
	
	JTextArea textArea;
	JScrollPane scrollPane;
	JLabel fontLabel;
	JSpinner fontSizeSpinner;
	JButton fontColourButton;
	JComboBox fontBox;
	
	JMenuBar menuBar;
	JMenu fileMenu;
	JMenuItem newItem;
	JMenuItem openItem;
	JMenuItem saveItem;
	JMenuItem closeItem;
	
	TextEditor(){
		
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //adding the X(exit) button on the top right corner
		this.setTitle("ani's text editor"); //title of the editor
		this.setSize(600, 600); //dimensions of editor
		this.setLayout(new FlowLayout()); //layout
		this.setLocationRelativeTo(null); //this line makes sure that the editor opens in the middle of the screen instead of top left
		
		textArea=new JTextArea(); //text area to type in
		textArea.setLineWrap(true); 
		textArea.setWrapStyleWord(true); //above two lines make sure that the line switches to text line once right edge is reached
		textArea.setFont(new Font("Arial",Font.PLAIN,22)); //setting default font
		
		scrollPane=new JScrollPane(textArea); //setting a scroll bar
		scrollPane.setPreferredSize(new Dimension(550,550)); //size of the entire editor (since text area is now part of scroll bar)
		scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED); //adding a vertical scroll bar, visibility is set to only when page is full
		
		fontLabel=new JLabel("Font Size:"); //adding a label to the font size spinner
		fontSizeSpinner=new JSpinner();
		fontSizeSpinner.setPreferredSize(new Dimension(50,25)); 
		fontSizeSpinner.setValue(22); //default value of font size
		fontSizeSpinner.addChangeListener(new ChangeListener() { //changelistener to change the text font whenever change is made in the font size spinner

			@Override
			public void stateChanged(ChangeEvent e) {
				textArea.setFont(new Font(textArea.getFont().getFamily(), Font.PLAIN,(int) fontSizeSpinner.getValue()));
				//new font is set in the text area
			}
			
		});
		
		fontColourButton=new JButton("Font Color"); //button to change the font colour
		fontColourButton.addActionListener(this);
		
		String[] fonts=GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames(); //string of the font names 
		
		fontBox=new JComboBox(fonts); //string of font names sent to combobox for list of Fonts
		fontBox.addActionListener(this);
		fontBox.setSelectedItem("Arial"); //default font option
		
		//------menu bar starts here---------
		
			menuBar = new JMenuBar(); //adding menu bar
			fileMenu = new JMenu("File"); 
			newItem=new JMenuItem("New"); 
			openItem=new JMenuItem("Open");
			saveItem=new JMenuItem("Save");
			closeItem=new JMenuItem("Exit");
			
			newItem.addActionListener(this);
			openItem.addActionListener(this);
			saveItem.addActionListener(this);
			closeItem.addActionListener(this);
			
			fileMenu.add(newItem);//adding items to the File menu
			fileMenu.add(openItem);
			fileMenu.add(saveItem);
			fileMenu.add(closeItem);
			menuBar.add(fileMenu);		//adding a 'File' menu
			
		//---------menu bar ends here--------
		
		//adding all the components in order
		this.setJMenuBar(menuBar);
		this.add(fontLabel);
		this.add(fontSizeSpinner);
		this.add(fontColourButton);
		this.add(fontBox);
		this.add(scrollPane);	
		this.setVisible(true);
		}
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		
		if(e.getSource()==fontColourButton) { //if font color button is pressed
			JColorChooser colourChooser=new JColorChooser(); //colour chooser gui
			Color colour=colourChooser.showDialog(null, "Choose font colour:", Color.black); //default color is set to black
			textArea.setForeground(colour); //changing the text color based on the user input in color chooser gui
		}
		
		if(e.getSource()==fontBox) { //font box to change the font
			
			textArea.setFont(new Font((String)fontBox.getSelectedItem(), Font.PLAIN, textArea.getFont().getSize()));
		}
		if(e.getSource()==newItem) { //if new option is clicked in file menu, setting everything to default value
			textArea.setText("");
			textArea.setFont(new Font("Arial",Font.PLAIN,22));
			textArea.setForeground(Color.black);
			fontBox.setSelectedItem("Arial");
			fontSizeSpinner.setValue(22);
		}
		if(e.getSource()==openItem) { //if open option is clicked in file menu
			
			JFileChooser fileChooser=new JFileChooser(); //file chooser gui
			fileChooser.setCurrentDirectory(new File(".")); //setting default location to the location of project
			FileNameExtensionFilter filter=new FileNameExtensionFilter("Text files","txt"); //creating a filter to only enable opening of text files
			fileChooser.setFileFilter(filter); //adding the filter
			
			int response=fileChooser.showOpenDialog(null); // open file dialog box
			if(response==JFileChooser.APPROVE_OPTION) { //if the response == 1 which means that correct file has been opened
				File file=new File(fileChooser.getSelectedFile().getAbsolutePath()); //opening the file into a variable
				Scanner fileIn=null; //scanner object to read the file into the editor
				
				try {
					fileIn=new Scanner(file);
					if(file.isFile()) { //reading the contents of the file into the editor
						while(fileIn.hasNextLine()) {
							String line=fileIn.nextLine()+"\n";
							textArea.append(line);
						}
					}
				} catch (FileNotFoundException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				finally {
					fileIn.close();
				}
			}
		}
		if(e.getSource()==saveItem) { //if save option is clicked in file menu
			
			JFileChooser fileChooser=new JFileChooser(); //file chooser gui
			fileChooser.setCurrentDirectory(new File("."));
			
			int response= fileChooser.showSaveDialog(null);
			if(response == JFileChooser.APPROVE_OPTION) { //if correct path has been chosen to save
				File file;
				PrintWriter fileOut=null; //PrintWriter object to create a new file in the destination path
				
				file=new File(fileChooser.getSelectedFile().getAbsolutePath());
				try {
					fileOut=new PrintWriter(file); 
					fileOut.println(textArea.getText()); //adding the contents of the editor to the newly created file in destination
				} catch (FileNotFoundException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				finally {
					fileOut.close();
				}
			}
		}
		if(e.getSource()==closeItem) {
			System.exit(0); //exiting the editor if exit is pressed
		}
		
	}
	
}
