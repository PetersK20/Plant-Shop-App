import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.HeadlessException;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.event.WindowStateListener;
import java.io.File;
import java.io.IOException;
import java.sql.*;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

public class databaseTable extends JFrame{
	private static JPanel northernPanel;

	private JTable plantTable;
	private buttonListener buttonListeners;
	private Object[][] defaultTableData;
	private Object[] defaultColumns;
	private ResultSet databaseInfo;
	private TableColumn tc;

	private JLabel informUser1,informUser2,informUser3;
	private JButton logo;
	private JButton shutdown,returnToSearch,addPlant,deletePlant;
	private JScrollPane pane;
	private DefaultTableModel defaultModel;
	private JPanel southernPanel;
	private JButton resetPlant;
	private focusListen textListener;
		private static JPanel searchPanel;
private static boolean northCreated=false;
private Connection con;
private tableListener tableListeners;
	private JTextField plantId,plantName,miniHeight,maxiHeight,pictureFiled;
	private JComboBox plantColor;
	private JComboBox plantType;
	private plantSearch searchFrame;
	
public databaseTable(plantSearch searchFrame){
	this.searchFrame=searchFrame;
	this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
	this.setSize(this.getMaximumSize());
	this.setResizable(false);
	this.setLocationRelativeTo(null);
	
	textListener=new focusListen();
	buttonListeners=new buttonListener();
	tableListeners=new tableListener();
	
	establishConnection();
	createNorthenPanel();
	createCenterPanel();
	createSouthernPanel();
	createFrameLayout();
this.setVisible(true);
}//End of constructor
private void createFrameLayout() {
	northernPanel.setBorder(BorderFactory.createLineBorder(plantSearch.getBlueColor(),5));
	this.add(northernPanel,BorderLayout.NORTH);
	this.add(pane,BorderLayout.CENTER);
	this.add(southernPanel, BorderLayout.SOUTH);
	
	
}//End of createFrameLayout
private void establishConnection() {

	try{
		
		String forName="com.mysql.jdbc.Driver";
		Class.forName(forName);

		 con=searchFrame.getCon();
	

		Statement getData=con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
		
		String query="select * from `all`";
		databaseInfo=getData.executeQuery(query);

	}catch(ClassNotFoundException e){
		
		JOptionPane.showMessageDialog(null, "Java can't connect to the database, the class was not found","Error",JOptionPane.ERROR_MESSAGE);
	} catch (SQLException e) {
e.printStackTrace();
		JOptionPane.showMessageDialog(null, "Java can't connect to the database, the database, username, or password could have been changed","Error",JOptionPane.ERROR_MESSAGE);
	} 

}//End of establishConnection



private void createNorthenPanel() {
	northernPanel=new JPanel();
	northernPanel.setBackground(plantSearch.getGreyColor());
	
	logo=searchFrame.createButtons("C:/Users/Kyle Peters/Documents/picturesForPlantShopProject/logo.jpg",null,null,true);
	informUser1=searchFrame.createJLabel("Make Sure to add a space between the letter and number in column 1", plantSearch.getBlueColor(),plantSearch.getFontPlantButtons());
	informUser2=searchFrame.createJLabel("End the file name with .jpg and don't list the path", plantSearch.getBlueColor(),plantSearch.getFontPlantButtons());
	informUser3=searchFrame.createJLabel("For the height columns, use a period to seperate the feet and inches", plantSearch.getBlueColor(),plantSearch.getFontPlantButtons());
	shutdown=searchFrame.createButtons("Shut Down the Program", Color.BLUE, Color.WHITE, false);
	shutdown.addActionListener(buttonListeners);
	returnToSearch=searchFrame.createButtons("Return to Search", Color.BLUE, Color.WHITE, false);
	returnToSearch.addActionListener(buttonListeners);
	
	northernPanel.setLayout(new GridBagLayout());
	GridBagConstraints c=new GridBagConstraints();
	c.insets=new Insets(2,2,2,2);
	c.weightx=50;
	c.weighty=50;

	plantSearch.addWithGridBag(1,1,3,3,	GridBagConstraints.NONE,GridBagConstraints.CENTER,c,northernPanel,logo);
	
	plantSearch.addWithGridBag(4,1,1,1,	GridBagConstraints.BOTH,GridBagConstraints.CENTER,c,northernPanel,informUser1);
	plantSearch.addWithGridBag(4,2,1,1,	GridBagConstraints.BOTH,GridBagConstraints.CENTER,c,northernPanel,informUser2);
	plantSearch.addWithGridBag(4,3,1,1,	GridBagConstraints.BOTH,GridBagConstraints.CENTER,c,northernPanel,informUser3);
	
	plantSearch.addWithGridBag(7,1,1,1,	GridBagConstraints.BOTH,GridBagConstraints.CENTER,c,northernPanel,shutdown);
	plantSearch.addWithGridBag(7,2,1,1,	GridBagConstraints.BOTH,GridBagConstraints.CENTER,c,northernPanel,returnToSearch);

}//End of createNorthenPanel



private void createCenterPanel() {
	
	defaultColumns=new Object[]{"Plant Id","Name of Plant","Type of Plant","color","Minimum Height","Maximum Height","Picture File"};
	defaultModel=new DefaultTableModel(defaultTableData,defaultColumns);
	
	try {
		while(databaseInfo.next()){
				defaultColumns=new String[]{databaseInfo.getString(1),databaseInfo.getString(2),databaseInfo.getString(3),databaseInfo.getString(4),
						Double.toString(databaseInfo.getDouble(5)),Double.toString(databaseInfo.getDouble(6)),databaseInfo.getString(7)};

			defaultModel.addRow(defaultColumns);
		}
		} catch (SQLException e) {
			
			e.printStackTrace();
		}

plantTable=createJTable();
 
 plantTable.addMouseListener(tableListeners);
defaultColumns=new String[]{"Plant Id","Name of Plant","Type of Plant","color","Minimum Height","Maximum Height","Picture File"};
for(Object s:defaultColumns){	
modifyJTable(s);
}
 pane=new JScrollPane(plantTable);



}//End of createCenterPanel


private void modifyJTable(Object s) {
tc=plantTable.getColumn(s);
tc.setPreferredWidth(tc.getPreferredWidth()*3+2);


	
}//End of modifyJTable
private JTable createJTable(){

	JTable table=new JTable(defaultModel);
	table.setFont(plantSearch.getFontPlantButtons());
	table.setRowHeight(table.getRowHeight()+10);
	table.setAutoCreateRowSorter(true);
	table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

	return table;
}//End of createJTable
private void createSouthernPanel() {
	southernPanel=new JPanel();
	
	addPlant=(JButton) createSouthComponents("Add Plant", true, false,null);
	deletePlant=(JButton) createSouthComponents("Delete Plant", true, false,null);
	resetPlant=(JButton) createSouthComponents("Reset", true, false,null);
	
	plantId=(JTextField) createSouthComponents("ID", false, false,null);
	plantName=(JTextField) createSouthComponents("Name", false, false,null);

	plantType=(JComboBox) createSouthComponents(null, false, true,new String[]{"flower","bush","tree","crop"});
	plantColor=(JComboBox) createSouthComponents(null, false, true,new String[]{"blue","green","red","pink","yellow","orange","other"});
	
	miniHeight=(JTextField) createSouthComponents("Minimum Height", false, false,null);
	maxiHeight=(JTextField) createSouthComponents("Maximum Height", false, false,null);
	pictureFiled=(JTextField) createSouthComponents("File Name", false, false,null);
	
}//End of createSouthernPanel
private JComponent createSouthComponents(String name,boolean isButton,boolean isComboBox,String[]comboBox){

	if(isButton){
		JButton button=searchFrame.createButtons(name, Color.BLUE, Color.WHITE, false);
		button.addActionListener(buttonListeners);
		southernPanel.add(button);
		return button;
	}else if(isComboBox){
		JComboBox<String> box=searchFrame.createComboBox(comboBox);
		box.setForeground(Color.BLACK);
		southernPanel.add(box);
		return box;
	}else{
		JTextField field=searchFrame.createJTextField(name, 10);
		field.addFocusListener(textListener);
		southernPanel.add(field);
		return field;
	}
}//End of createSouthComponents
public void setFrameInvisible() {
this.setVisible(false);
	
}//End of setFrameInvisible
public boolean checkForErrors(){
	boolean errors=true;
	String[] tempId=plantId.getText().split(" ");
	String[] tempMini=(miniHeight.getText().split("\\."));
	String[] tempMaxi=(maxiHeight.getText().split("\\."));
	try{
		double i=Double.parseDouble(tempMini[0]);
		 i=Double.parseDouble(tempMini[1]);
		 i=Double.parseDouble(tempMaxi[0]);
		 i=Double.parseDouble(tempMaxi[1]);
		 
		 double mini=Double.parseDouble(tempMini[0])*10+Double.parseDouble(tempMini[1]);
		 double max=Double.parseDouble(tempMaxi[0])*10+Double.parseDouble(tempMaxi[1]);
File f=new File("C:/Users/Kyle Peters/Documents/picturesForPlantShopProject/"+pictureFiled.getText());

if(tempId.length!=2){
JOptionPane.showMessageDialog(null, "Plant ID does not include a space or includes more than one space","Error",JOptionPane.ERROR_MESSAGE);
}else if(tempMini.length!=2){
JOptionPane.showMessageDialog(null, "Minimum Height does not include a period or includes more than one period","Error",JOptionPane.ERROR_MESSAGE);
}else if(tempMaxi.length!=2){
JOptionPane.showMessageDialog(null, "Maximum Height does not include a period or includes more than one period","Error",JOptionPane.ERROR_MESSAGE);
}else if(mini>max){
JOptionPane.showMessageDialog(null, "the minimum height is greater than the maximum height","Error",JOptionPane.ERROR_MESSAGE);
}else if(!f.exists()){
JOptionPane.showMessageDialog(null, "the file name does not exist,the file is not in the right folder, or the file does not end in .jpg","Error",JOptionPane.ERROR_MESSAGE);
}else{
errors=false;

}
	}catch(NumberFormatException e){
		JOptionPane.showMessageDialog(null, "Values at Maximum Height or Minimum Height is not a number","Error",JOptionPane.ERROR_MESSAGE);
	} 	catch(IndexOutOfBoundsException e){
		JOptionPane.showMessageDialog(null, "Values at Maximum or Minimum Height does not include a period or includes more than one period","Error",JOptionPane.ERROR_MESSAGE);
	}
	return errors;
}//End of checkForErrors	
private class buttonListener implements ActionListener{

	@Override
	public void actionPerformed(ActionEvent e) {
		 if(e.getSource()==shutdown){
			
			System.exit(0);
			
		}else if(e.getSource()==returnToSearch){			
			setFrameInvisible();
			searchFrame.getSearchByName().setText("Search By Plant Name");
			searchFrame.reCreateFrame();
		}else if(e.getSource()==addPlant){
			boolean errors=checkForErrors();
			if(!errors){addToDataBase();}
		}else if(e.getSource()==deletePlant){
			deletePlantData();
		}else if(e.getSource()==resetPlant){	
		plantId.setText("ID");
		plantName.setText("Name");
		miniHeight.setText("Minimum Height");
		maxiHeight.setText("Maximum Height");
		pictureFiled.setText("File Name");
		plantColor.setSelectedIndex(0);
		plantType.setSelectedIndex(0);
	}
		

			}
	
			private void deletePlantData() {
		
		
		try {
			databaseInfo.absolute(plantTable.getSelectedRow()+1);
			databaseInfo.deleteRow();
		} catch (SQLException e) {
			
			e.printStackTrace();
		}
		defaultModel.removeRow(plantTable.getSelectedRow());
		
	}//End of deletePlantData
			private void addToDataBase() {
				defaultModel.addRow(new Object[]{plantId.getText(),plantName.getText(),(String)plantType.getSelectedItem(),(String)plantColor.getSelectedItem(),miniHeight.getText(),maxiHeight.getText(),pictureFiled.getText()});
				try {
					databaseInfo.moveToInsertRow();
					databaseInfo.updateString(1, plantId.getText().toLowerCase());databaseInfo.updateString(2, plantName.getText().toLowerCase());databaseInfo.updateString(3, ((String) plantType.getSelectedItem()).toLowerCase());databaseInfo.updateString(4, ((String) plantColor.getSelectedItem()).toLowerCase());
					databaseInfo.updateString(5, miniHeight.getText().toLowerCase());databaseInfo.updateString(6, maxiHeight.getText().toLowerCase());databaseInfo.updateString(7, pictureFiled.getText().toLowerCase());
					databaseInfo.insertRow();
					databaseInfo.updateRow();
					
				} catch (SQLException e) {
					
		
				}
		
	}//End of addToDataBase
			
}//End of ButtonListener
private class tableListener implements MouseListener{

	@Override
	public void mouseClicked(MouseEvent e) {
		insertIntoTable();
		}//End of MouseClicked
	
	
	public void insertIntoTable(){
		String s=JOptionPane.showInputDialog(null,"insert a new value");
		
		boolean error = true;
		
		String original=(String) defaultModel.getValueAt(plantTable.getSelectedRow(), plantTable.getSelectedColumn());
			if(s!=null){
				defaultModel.setValueAt(s, plantTable.getSelectedRow(), plantTable.getSelectedColumn());
				error=checkForErrors();
			if(!error){
				try {
						s.toLowerCase();
					
				databaseInfo.absolute(plantTable.getSelectedRow()+1);
				databaseInfo.updateString(plantTable.getSelectedColumn()+1, s);
				databaseInfo.updateRow();
			} catch (SQLException exception) {
				
				exception.printStackTrace();
			}
			}else{
				defaultModel.setValueAt(original, plantTable.getSelectedRow(), plantTable.getSelectedColumn());
			}
		}
	}//End of insertIntoTable
	public boolean checkForErrors(){
		boolean errors=true;
		try {
			
			String[] tempId= ((String) defaultModel.getValueAt(plantTable.getSelectedRow(), 0)).split(" ");
			String[] tempMini=((String) defaultModel.getValueAt(plantTable.getSelectedRow(),4)).split("\\.");
			String[] tempMaxi=((String) defaultModel.getValueAt(plantTable.getSelectedRow(), 5)).split("\\.");

			double i=Double.parseDouble(tempMini[0]);
			 i=Double.parseDouble(tempMini[1]);
			 i=Double.parseDouble(tempMaxi[0]);
			 i=Double.parseDouble(tempMaxi[1]);
			 
			 double mini=Double.parseDouble(tempMini[0])*10+Double.parseDouble(tempMini[1]);
			 double max=Double.parseDouble(tempMaxi[0])*10+Double.parseDouble(tempMaxi[1]);
	File f=new File("C:/Users/Kyle Peters/Documents/picturesForPlantShopProject/"+((String) defaultModel.getValueAt(plantTable.getSelectedRow(), 6)));

	if(tempId.length!=2){
	JOptionPane.showMessageDialog(null, "Plant ID does not include a space or includes more than one space","Error",JOptionPane.ERROR_MESSAGE);
	}else if(mini>max){
	JOptionPane.showMessageDialog(null, "the minimum height is greater than the maximum height","Error",JOptionPane.ERROR_MESSAGE);
	}else if(!f.exists()){
	JOptionPane.showMessageDialog(null, "the file name does not exist,the file is not in the right folder, or the file does not end in .jpg","Error",JOptionPane.ERROR_MESSAGE);
	}else if(checkColor()){
		JOptionPane.showMessageDialog(null, "the color is not reconized, you can use blue, green, red, orange, pink, yellow, and other","Error",JOptionPane.ERROR_MESSAGE);
	}else if(checkType()){
		JOptionPane.showMessageDialog(null, "the Plant Type is not reconized, you can use bush, flower, crop, tree","Error",JOptionPane.ERROR_MESSAGE);
	}else{	
	errors=false;
	}

		}catch(NumberFormatException e){
			JOptionPane.showMessageDialog(null, "Maximum Height or Minimum Height is not a number","Error",JOptionPane.ERROR_MESSAGE);
		} catch(IndexOutOfBoundsException e){
			JOptionPane.showMessageDialog(null, "Maximum or Minimum Height does not include a period or includes more than one period","Error",JOptionPane.ERROR_MESSAGE);
		}
			
		return errors;
	}//End of checkForErrors
	
	private boolean checkType() {
		boolean b=false;
		if(!((String) defaultModel.getValueAt(plantTable.getSelectedRow(), 2)).toLowerCase().equals("bush")&&!((String) defaultModel.getValueAt(plantTable.getSelectedRow(), 2)).toLowerCase().equals("tree")
				&&!((String) defaultModel.getValueAt(plantTable.getSelectedRow(), 2)).toLowerCase().equals("crop")&&!((String) defaultModel.getValueAt(plantTable.getSelectedRow(), 2)).toLowerCase().equals("flower")){
			b=true;
		}
		return b;
	}//End of checkType

	public boolean checkColor(){
		boolean b=false;
		try {
			if(!((String) defaultModel.getValueAt(plantTable.getSelectedRow(), 3)).toLowerCase().equals("blue")&&!((String) defaultModel.getValueAt(plantTable.getSelectedRow(), 3)).toLowerCase().equals("green")
					&&!((String) defaultModel.getValueAt(plantTable.getSelectedRow(), 3)).toLowerCase().equals("yellow")&&!((String) defaultModel.getValueAt(plantTable.getSelectedRow(), 3)).toLowerCase().equals("red")
					&&!((String) defaultModel.getValueAt(plantTable.getSelectedRow(), 3)).toLowerCase().equals("orange")&&!databaseInfo.getString(4).toLowerCase().equals("pink")
					&&!((String) defaultModel.getValueAt(plantTable.getSelectedRow(), 3)).toLowerCase().equals("other"))
			{
			b=true;
			}
		}catch (SQLException e) {
			e.printStackTrace();
		}
		return b;
	}//End of checkColor
	public void mouseEntered(MouseEvent arg0) {
		
	}
	public void mouseExited(MouseEvent arg0) {

	}
	public void mousePressed(MouseEvent arg0) {
		
	}
	public void mouseReleased(MouseEvent arg0) {
		
	}
	
}
class focusListen implements FocusListener{


	public void focusGained(FocusEvent e) {
	if(e.getSource()==plantId && plantId.getText().equals("ID")){
			plantId.setText("");
}else if(e.getSource()==plantName && plantName.getText().equals("Name")){
		plantName.setText("");
}else if(e.getSource()==miniHeight && miniHeight.getText().equals("Minimum Height")){
	miniHeight.setText("");
}else if(e.getSource()==maxiHeight && maxiHeight.getText().equals("Maximum Height")){
	maxiHeight.setText("");
}else if(e.getSource()==pictureFiled && pictureFiled.getText().equals("File Name")){
	pictureFiled.setText("");
}

	}
	public void focusLost(FocusEvent e) {
		if(e.getSource()==plantId && plantId.getText().equals("")){
			plantId.setText("ID");
}else if(e.getSource()==plantName && plantName.getText().equals("")){
		plantName.setText("Name");
}else if(e.getSource()==miniHeight && miniHeight.getText().equals("")){
	miniHeight.setText("Minimum Height");
}else if(e.getSource()==maxiHeight && maxiHeight.getText().equals("")){
	maxiHeight.setText("Maximum Height");
}else if(e.getSource()==pictureFiled && pictureFiled.getText().equals("")){
	pictureFiled.setText("File Name");
}
		
	}
	
}//End of focusListener

}//End of Class databaseTable


