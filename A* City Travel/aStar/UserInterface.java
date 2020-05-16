import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.LinkedList;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.filechooser.FileNameExtensionFilter;

/*
 * Author: Sean Martens
 * This UI was built as an after thought, and not included in the the homework.
 * Many design decisions were made to not make any functional changes to the og source code. 
 * 
 */
public class UserInterface {
	
    // frame 
    static JFrame f; 
  
    // label 
    static JLabel l; 
  
    // combobox 
    static JComboBox c1; 
    
    // button
    static JButton b1;
    
    static boolean cityChosen = false;
	
	public UserInterface() {
		
	}
	
	public String setFileLocation() {
		JFileChooser chooser = new JFileChooser("Select city txt file");
		chooser.setApproveButtonText("Select cities.txt file");
        FileNameExtensionFilter filter = new FileNameExtensionFilter("txt", "txt");
        chooser.setFileFilter(filter);
        int returnVal = chooser.showOpenDialog(null);
        if(returnVal != JFileChooser.APPROVE_OPTION) {
        	System.err.println("User did not choose a file.");
            System.exit(-1);
        }
        return chooser.getSelectedFile().getAbsolutePath().toString();
	}
	
	public static String getCity(String message, City[] cities) {
		cityChosen = false;
		// create a new frame 
        JFrame f = new JFrame("Cities"); 
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
  
        // set layout of frame 
        f.setLayout(new FlowLayout()); 
  
        // array of string contating cities 
        LinkedList<String> cityStrings = new LinkedList<String>();
        for(City c : cities) {
        	cityStrings.add(c.get_name());
        }
        
        
        String s1[] = (String[]) cityStrings.toArray(new String[cityStrings.size()]);
  
        // create checkbox 
        c1 = new JComboBox(s1);  
  
        // create labels 
        l = new JLabel(message); 
        
        b1 = new JButton(message);
        b1.addActionListener(new ActionListener(){
        	@Override
        	public void actionPerformed(ActionEvent evt) {
        		buttonClicked();
        	}
        });
  
        // set color of text 
        l.setForeground(Color.red); 
  
        // create a new panel 
        JPanel p = new JPanel(); 
  
        p.add(l); 
  
        // add combobox to panel 
        p.add(c1); 
        
        p.add(b1);
  
        // add panel to frame 
        f.add(p); 
  
        // set the size of frame 
        f.setSize(600, 200); 
        
        f.setVisible(true);
        
        while(!cityChosen) {
        	//This class was added to help demo the app, so this implementation was not important.
        	System.out.print("");
        }
        
        f.setVisible(false);
        
        return c1.getSelectedItem().toString();
	}
	
	public static void buttonClicked() {
		cityChosen = true;
	}
}
