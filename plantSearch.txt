import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import java.sql.*;
import java.util.ArrayList;
import javax.swing.*;
public class plantSearch extends JFrame{
	private JButton all,flowers,bushes,trees,crops;
	private static JButton logo;
	private String password="plantShop1234";
	private JPanel northSearchPanel;
	private static ButtonListener buttonClickedListener;
	private static ButtonHoveredListener buttonMouseListener;
	private static Font fontPlantButtons;
	private static Color blueColor;
	private static Color greyColor;
	private Box boxPlantButtons,colorBox;
	private StringBuilder query;
	private static textKeyListener keyListener;
	private static textFocusListener focusListener;
	private String[] forCombBox;
	private JCheckBox blue,green,red,pink,yellow,orange,any;
	private JTextField searchByName;
	private JButton searchButton;
	private JLabel informHeight,andLabel;
	private JComboBox<String> minimum,maximum;
	private static Box height;
	private Box searchBox;
	private JButton resetCriteria;
	private JLabel informLabel;
	private boolean allSelected=true,flowerSelected=false,bushSelected=false,cropSelected=false,treeSelected=false;
	

	private static plantSearch initiateFrame;

	private Connection con;
	private ResultSet databaseInfo;
	private JPanel centerPanel;
	private boolean connected=false;
private JPanel holder;
	private ArrayList<JPanel> plantsPanel;
	private JScrollPane pane;
public static void main(String[] args){

	 initiateFrame=new plantSearch();
}//end of Main
public plantSearch(){
this.addComponentListener(new frameListener());
this.addWindowListener(new windowListener());
	this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
	this.setResizable(false);
	this.setSize(this.getMaximumSize());
this.setLocationRelativeTo(null);
	this.createNorthPanel();

	

	this.getConnection();

this.createplantPanels();

	pane=createFrameLayout();
	this.add(pane);

	this.setVisible(true);	
}//End of Constructor



private void createNorthPanel(){
	northSearchPanel=new JPanel();

	fontPlantButtons=new Font("Serif",Font.PLAIN,24);
	
	blueColor=new Color(51,153,255);
	northSearchPanel.setBorder(BorderFactory.createLineBorder(blueColor,5));
	
	greyColor=new Color(224,224,224);
	
	 buttonMouseListener=new ButtonHoveredListener();
	 buttonClickedListener=new ButtonListener();
	
	boxPlantButtons=Box.createHorizontalBox();
	
	logo=createButtons("C:/Users/Kyle Peters/Documents/picturesForPlantShopProject/logo.jpg",null,null,true);

	all=createButtons("All",greyColor,blueColor,false);
	boxPlantButtons.add(all);
	changeButton(all);
	flowers=createButtons("Flowers",greyColor,blueColor,false);
	boxPlantButtons.add(flowers);
	bushes=createButtons("Bushes",greyColor,blueColor,false);
	boxPlantButtons.add(bushes);
	trees=createButtons("Trees",greyColor,blueColor,false);
	boxPlantButtons.add(trees);
	crops=createButtons("Crops",greyColor,blueColor,false);
	boxPlantButtons.add(crops);
	
	 colorBox=Box.createHorizontalBox();

	 any=createCheckBoxes("Any");blue=createCheckBoxes("Blue");green=createCheckBoxes("Green"); red=createCheckBoxes("Red");
	 pink=createCheckBoxes("Pink");yellow=createCheckBoxes("Yellow");orange=createCheckBoxes("Orange"); any.setSelected(true);
	
	height=Box.createHorizontalBox();
	

	forCombBox=new String[]{"any","0 inches","3 inches","6 inches","9 inches","1 foot","1.5 feet","2 feet","3 feet","4 feet","5 feet","6 feet","7 feet"};
	informHeight=createJLabel("average height of the plant :  Between  ",blueColor,fontPlantButtons);
	height.add(informHeight);
	 minimum=createComboBox(forCombBox);
		andLabel=createJLabel("  and  ",blueColor,fontPlantButtons);
		height.add(andLabel);
		 maximum=createComboBox(forCombBox);
	northSearchPanel.setBackground(greyColor);
	
	keyListener=new textKeyListener();
	 focusListener=new textFocusListener();
	 
	searchByName=createJTextField("Search By Plant Name",15);


	
	searchBox=Box.createHorizontalBox();
	
	searchButton=createButtons("C:/Users/Kyle Peters/Documents/picturesForPlantShopProject/searchPicture.png",null,null,true);
	searchButton.addActionListener(buttonClickedListener);
	searchButton.setBackground(Color.BLUE);
	
	searchBox.add(searchByName);	
	searchBox.add(searchButton);
	

	
	resetCriteria=createButtons("Reset Criteria",Color.BLUE,Color.WHITE,false);

	informLabel=createJLabel("(You can leave any of these criteria unfilled)",blueColor,UIManager.getDefaults().getFont("JTextField.Font"));
	createLayout();
	
}//End of createNorthPanel









private void getConnection(){
try{
		if(!connected){
		String forName="com.mysql.jdbc.Driver";
		Class.forName(forName);
		String database="jdbc:mysql://localhost/plant_shop";
		String user="root";
		String password="Coke6008337";
		 con=DriverManager.getConnection(database,user,password);
		 connected=true;
		}
		
		PreparedStatement getData=null;

		query=new StringBuilder();
		query.ensureCapacity(120);
		if(searchByName.getText().equals("Search By Plant Name")){
			check();
		 getData=con.prepareStatement((query.toString()));
		 databaseInfo=getData.executeQuery();
		 		if(!databaseInfo.next()){
		 JOptionPane.showMessageDialog(null, "the plant you searched for was not found","Not Found",JOptionPane.INFORMATION_MESSAGE);
		 		}
		}else{
			check();
				if(query.toString().contains("where")){
					
				query.append(" and plant_name like '%"+searchByName.getText()+"%'");
				 getData=con.prepareStatement(query.toString());

				 databaseInfo=getData.executeQuery();
				if(!databaseInfo.next()){
					JOptionPane.showMessageDialog(null, "the plant you searched for was not found, if you didn't want to search by name then hit the reset button to reset the default textfield text","Not Found",JOptionPane.INFORMATION_MESSAGE);
				}
				
				}else{
				query.append(" where plant_name like '%"+searchByName.getText()+"%'");
				
				 getData=con.prepareStatement(query.toString());
		
				 databaseInfo=getData.executeQuery();
				if(!databaseInfo.next()){
					JOptionPane.showMessageDialog(null, "the plant you searched for was not found, if you didn't want to search by name then hit the reset button to reset the default textfield text","Not Found",JOptionPane.INFORMATION_MESSAGE);
				}
			}
				
		}
		
	
	}catch(ClassNotFoundException e){
		
		JOptionPane.showMessageDialog(null, "Java can't connect to the database, the class was not found","Error",JOptionPane.ERROR_MESSAGE);
	} catch (SQLException e) {
e.printStackTrace();
		JOptionPane.showMessageDialog(null, "Java can't connect to the database, the database, username, or password could have been changed","Error",JOptionPane.ERROR_MESSAGE);
	} 

}//End of getConnection



private void check(){
	
	if(allSelected==true){	
		
	finalCheck("select * from `all`");
	query.replace(0, query.capacity()-1,query.toString().replaceFirst("and", "where"));
	
	}else if(flowerSelected){
		
		finalCheck("select * from `all` where plant_type=\"flower\"");
		
	}else if(bushSelected){
		
		finalCheck("select * from `all` where plant_type=\"bush\"");
		
	}else if(cropSelected){
		
		finalCheck("select * from `all` where plant_type=\"crop\"");
		
	}else if(treeSelected){
		
		finalCheck("select * from `all` where plant_type=\"tree\"");
	
	}
}//End of Check



private void finalCheck(String queryStart){
	String color=findSelectedColors();	
	 query.append(queryStart);
	 
	 if(color.length()!=0){
		 query.append(" and "+color);	 
	 }
	 
	 String height =findSelectedHeight();
	 if(height.length()!=0){
		 query.append(" having "+height);
		}
	
}//End of finalCheck



	private String findSelectedHeight(){

	String returnString="";
if(minimum.getSelectedIndex()==0&&maximum.getSelectedIndex()==0 ){
}else if(minimum.getSelectedIndex()==0&&maximum.getSelectedIndex()!=0){
	String[] array=((String)maximum.getSelectedItem()).split(" ");

	double maxi=Double.parseDouble(array[0]);
	if(array[1].equals("inches")){

maxi*=.1;
	}
	
		returnString="max_Height<=\""+maxi+"\"";
}
else if(minimum.getSelectedIndex()!=0&&maximum.getSelectedIndex()==0){
	String[] array=((String)minimum.getSelectedItem()).split(" ");
	double mini=Double.parseDouble(array[0]);
	if(array[1].equals("inches")){
mini*=.1;
	}
		returnString="max_Height>=\""+mini+"\"";
}
else{String[] array=((String)minimum.getSelectedItem()).split(" ");
String[] array2=((String)maximum.getSelectedItem()).split(" ");
double mini=Double.parseDouble(array[0]);
if(array[1].equals("inches")){
mini*=.1;

}
double maxi=Double.parseDouble(array2[0]);
if(array2[1].equals("inches")){
maxi*=.1;
}
	returnString="max_Height>=\""+mini+"\""+" and max_Height<=\""+maxi+"\"";
}
	return returnString;
}//End of findSelected Height
	
	
	
	
	
private String findSelectedColors(){
	String returnString="";
	if(blue.isSelected()){
		returnString+="color=\"blue\" ";
	}
	
	if(red.isSelected()){
		returnString+="color=\"red\" ";
	}
	if(orange.isSelected()){
		returnString+="color=\"orange\" ";
	}
	if(yellow.isSelected()){
		returnString+="color=\"yellow\" ";
	}
	if(green.isSelected()){
		returnString+="color=\"green\" ";
	}
	if(pink.isSelected()){
		returnString+="color=\"pink\" ";
	}
	if(any.isSelected()){
		returnString="";
	}
	String[]check = returnString.split(" ");
	returnString="";
	if(check.length!=0){
	for(int x=0;x<check.length;x++){
		if(x==0){
			returnString+=check[x];
		}else{
			returnString+=" or "+check[x];
		}
	}
	}
	return returnString;
}//End of findSelectedColors



private void createLayout(){
	northSearchPanel.setLayout(new GridBagLayout());
	GridBagConstraints c=new GridBagConstraints();
	c.insets=new Insets(2,2,2,2);
	c.weightx=50;
	c.weighty=50;

	
	
	addWithGridBag(1,1,3,3,	GridBagConstraints.NONE,GridBagConstraints.CENTER,c,northSearchPanel,logo);
	
	addWithGridBag(4,1,1,1,	GridBagConstraints.NONE,GridBagConstraints.CENTER,c,northSearchPanel,boxPlantButtons);

	addWithGridBag(4,2,1,1,	GridBagConstraints.NONE,GridBagConstraints.CENTER,c,northSearchPanel,colorBox);

	addWithGridBag(4,3,1,1,	GridBagConstraints.NONE,GridBagConstraints.CENTER,c,northSearchPanel,height);

	addWithGridBag(7,1,1,1,	GridBagConstraints.HORIZONTAL,GridBagConstraints.CENTER,c,northSearchPanel,searchBox);

	addWithGridBag(7,2,1,1,	GridBagConstraints.BOTH,GridBagConstraints.CENTER,c,northSearchPanel,resetCriteria);

	addWithGridBag(4,4,1,1,	GridBagConstraints.BOTH,GridBagConstraints.CENTER,c,northSearchPanel,informLabel);
}//End of CreateLayout



private ArrayList<JPanel> createplantPanels() {
plantsPanel=new ArrayList<JPanel>();

	try {
		 databaseInfo.absolute(0);
		while(databaseInfo.next()){

			
			JPanel panel=new JPanel();
			panel.setLayout(new GridBagLayout());
			GridBagConstraints c=new GridBagConstraints();
			JButton image= createButtons("C:/Users/Kyle Peters/Documents/picturesForPlantShopProject/"+databaseInfo.getString(7), null, null,true);

			JLabel l1=createJLabel("Plant Location: "+databaseInfo.getString(1).toUpperCase(),blueColor,fontPlantButtons);
			JLabel l2=createJLabel("Plant Name: "+databaseInfo.getString(2),blueColor,fontPlantButtons);
			JLabel l3=createJLabel("Plant Type: "+databaseInfo.getString(3),blueColor,fontPlantButtons);
			JLabel l4=createJLabel("Plant Color: "+databaseInfo.getString(4),blueColor,fontPlantButtons);
		
			String[] mini=(Double.toString(databaseInfo.getDouble(5)).split("\\."));

			String 	feet=(Double.parseDouble(mini[0])==1)? " foot, ":" feet, ";
	
			String inch=(Double.parseDouble(mini[1])==1)? " inch":" inches";
			JLabel l5=createJLabel("Plant Height Now: "+mini[0]+feet+mini[1]+inch,blueColor,fontPlantButtons);
			mini=(Double.toString(databaseInfo.getDouble(6)).split("\\."));
			feet=(Integer.parseInt(mini[0])==1)? " foot, ":" feet, ";
			inch=(Integer.parseInt(mini[1])==1)? " inch":" inches";
			JLabel l6=createJLabel("Maximum Plant Height: "+mini[0]+feet+mini[1]+inch,blueColor,fontPlantButtons);
		
			addWithGridBag(1,1,6,6,	GridBagConstraints.BOTH,GridBagConstraints.CENTER,c,panel,image);
			addWithGridBag(7,1,1,1,	GridBagConstraints.BOTH,GridBagConstraints.CENTER,c,panel,l1);
			addWithGridBag(7,2,1,1,	GridBagConstraints.BOTH,GridBagConstraints.CENTER,c,panel,l2);
			addWithGridBag(7,3,1,1,	GridBagConstraints.BOTH,GridBagConstraints.CENTER,c,panel,l3);
			addWithGridBag(7,4,1,1,	GridBagConstraints.BOTH,GridBagConstraints.CENTER,c,panel,l4);
			addWithGridBag(7,5,1,1,	GridBagConstraints.BOTH,GridBagConstraints.CENTER,c,panel,l5);
			addWithGridBag(7,6,1,1,	GridBagConstraints.BOTH,GridBagConstraints.CENTER,c,panel,l6);
panel.setBorder(BorderFactory.createLineBorder(greyColor,10));
image.setBackground(Color.WHITE);
panel.setBackground(Color.WHITE);
			plantsPanel.add(panel);
	
	
		}

	} catch (SQLException e) {

		e.printStackTrace();
	}
	
	return plantsPanel;
}//End of createPlantPanels



private JScrollPane createFrameLayout() {
	 holder=new JPanel();
	holder.setLayout(new GridBagLayout());
	GridBagConstraints c2=new GridBagConstraints();
	

centerPanel=new JPanel();
centerPanel.setBorder(BorderFactory.createLineBorder(blueColor,5));
holder.setBackground(blueColor);
centerPanel.setLayout(new GridBagLayout());
centerPanel.setBackground(blueColor);



int y=1;
int x=1;

for(JPanel pPanel:plantsPanel){


	addWithGridBag(x,y,1,1,	GridBagConstraints.NONE,GridBagConstraints.CENTER,c2,centerPanel,pPanel);
	y=(x==1)? y:++y;
	x=(x==1)? 2:1;

}
if(plantsPanel.size()==0){
	c2.insets=new Insets(0,0,220,0);
	
} 
	addWithGridBag(1,1,1,1,	GridBagConstraints.BOTH,GridBagConstraints.NORTH,c2,holder,northSearchPanel);
	if(plantsPanel.size()==1){
		c2.insets=new Insets(0,0,220,0);
	}
	addWithGridBag(1,2,1,1,	GridBagConstraints.BOTH,GridBagConstraints.NORTH,c2,holder,centerPanel);


JScrollPane pane=new JScrollPane(holder,JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
return pane;
}//End of createFrameLayout



public void reCreateFrame(){

this.getConnection();


	holder.remove(centerPanel);
	pane.remove(holder);
	this.remove(pane);
	this.createplantPanels();
	pane=this.createFrameLayout();
	this.add(pane);

this.setVisible(true);
	
}//End of reCreateFrame



public JTextField createJTextField(String text,int length){
	JTextField field=new JTextField(text,length);
	field.setFont(fontPlantButtons);
	field.addKeyListener(keyListener);
	field.addFocusListener(focusListener);
	return field;
}//End of createJTextField



public JLabel createJLabel(String lable,Color text,Font f){
	JLabel lables=new JLabel(lable);
	lables.setForeground(text);
	lables.setFont(f);
	return lables;
	
}//End of createJLabel



public JComboBox<String> createComboBox(String[] s){
	JComboBox<String> comboBox=new JComboBox<String>(s);
	comboBox.setForeground(blueColor);
	comboBox.setMaximumRowCount(5);
	comboBox.setFont(fontPlantButtons);

			height.add(comboBox);	
	return comboBox;
}//End of createJTextField



private JCheckBox createCheckBoxes(String name){
	JCheckBox toReturn=new JCheckBox(name);
	colorBox.add(toReturn);
	toReturn.setFont(fontPlantButtons);

	toReturn.setForeground(blueColor);
	toReturn.setBackground(greyColor);
	toReturn.addActionListener(buttonClickedListener);
	return toReturn;
}//End of createCheckBoxes



public JButton createButtons(String buttonText, Color background, Color foreground,boolean isLogo){
	
	JButton toReturnButton=null;
	
	if(!isLogo){
		
		 toReturnButton=new JButton(buttonText);
	toReturnButton.setBackground(background);
	toReturnButton.setFont(fontPlantButtons);
	toReturnButton.setForeground(foreground);
	toReturnButton.addActionListener(buttonClickedListener);
	toReturnButton.addMouseListener(buttonMouseListener);

	}else{
ImageIcon icon=new ImageIcon(buttonText);
Image image = icon.getImage();
if(buttonText.contains("logo.jpg")){
image=image.getScaledInstance(300, 200, java.awt.Image.SCALE_SMOOTH);
}else if(buttonText.contains("searchPicture.png")){
	image=image.getScaledInstance(50, 50, java.awt.Image.SCALE_SMOOTH);

}else{
	image=image.getScaledInstance(250, 180, java.awt.Image.SCALE_SMOOTH);
}
icon=new ImageIcon(image);
toReturnButton=new JButton(icon);
toReturnButton.setBackground(new Color(224,224,224));

}
	toReturnButton.setBorderPainted(false);
	toReturnButton.setFocusPainted(false);
	
	return toReturnButton;
}//End of createButton



public static void addWithGridBag(int x,int y,int width,int height, int fill, int anchor,GridBagConstraints constraints,JPanel p,JComponent c){
	

		constraints.gridx=x;
	constraints.gridy=y;


	constraints.gridwidth=width;

	constraints.gridheight=height;

	constraints.fill=fill;

	constraints.anchor=anchor;

p.add(c, constraints);
}//End of addWithGridBag



private void changeButton(JButton button){
	button.setBackground(blueColor);
	button.setForeground(greyColor);
}//End of changeButton



private void changeToDefault(JButton button){
	button.setBackground(greyColor);
	button.setForeground(blueColor);
}//End of changeToDefault



private class ButtonListener implements ActionListener{

	public void actionPerformed(ActionEvent e) {
if(e.getSource()==searchButton){
	if(searchByName.getText().equals(password)){
	initiateFrame.setVisible(false);
	 new databaseTable(initiateFrame);
	}else{
	reCreateFrame();
	}
}else if(e.getSource()==any){
	blue.setSelected(false);
	orange.setSelected(false);
	pink.setSelected(false);
	yellow.setSelected(false);
	green.setSelected(false);
	red.setSelected(false);
}
else if(e.getSource()==blue||e.getSource()==orange||e.getSource()==pink||e.getSource()==yellow||e.getSource()==red||e.getSource()==green){
	any.setSelected(false);

}else if(e.getSource()==resetCriteria){
	
	forResetCriteria();
	
}
else if(e.getSource()==all){
	
	forAll();
	
}else if(e.getSource()==flowers){
	
	forFlower();
	
}else if(e.getSource()==trees){
	
	forTree();
	
}else if(e.getSource()==bushes){
	
	forBush();
	
}else if(e.getSource()==crops){
	
	forCrop();

}
	
}//End of ActionPerformed
	public void forResetCriteria(){
		allSelected=true;
		flowerSelected=false;
		treeSelected=false;
		bushSelected=false;
		cropSelected=false;
		changeButton(all);
		changeToDefault(flowers);
		changeToDefault(trees);
		changeToDefault(bushes);
		changeToDefault(crops);

		any.setSelected(true);
		blue.setSelected(false);
		green.setSelected(false);
		red.setSelected(false);
		pink.setSelected(false);
		yellow.setSelected(false);
		orange.setSelected(false);
		
		minimum.setSelectedIndex(0);
		maximum.setSelectedIndex(0);
		
		searchByName.setText("Search By Plant Name");
	}public void forAll(){
		allSelected=true;
		flowerSelected=false;
		treeSelected=false;
		bushSelected=false;
		cropSelected=false;
		changeButton(all);
		changeToDefault(flowers);
		changeToDefault(trees);
		changeToDefault(bushes);
		changeToDefault(crops);
	}public void forFlower(){
		allSelected=false;
		
		flowerSelected=true;
		treeSelected=false;
		bushSelected=false;
		cropSelected=false;
		changeButton(flowers);
		changeToDefault(all);
		changeToDefault(trees);
		changeToDefault(bushes);
		changeToDefault(crops);
		
	}public void forBush(){
		
		allSelected=false;
		flowerSelected=false;
		treeSelected=false;
		bushSelected=true;
		cropSelected=false;
		changeButton(bushes);
		changeToDefault(flowers);
		changeToDefault(trees);
		changeToDefault(all);
		changeToDefault(crops);
		
	}public void forCrop(){
		
		allSelected=false;
		flowerSelected=false;
		treeSelected=false;
		bushSelected=false;
		cropSelected=true;
		changeButton(crops);
		changeToDefault(flowers);
		changeToDefault(trees);
		changeToDefault(bushes);
		changeToDefault(all);
		
	}public void forTree(){
		
		allSelected=false;
		flowerSelected=false;
		treeSelected=true;
		bushSelected=false;
		cropSelected=false;
		changeButton(trees);
		changeToDefault(flowers);
		changeToDefault(all);
		changeToDefault(bushes);
		changeToDefault(crops);
		
	}
}//End of ButtonListener

private class ButtonHoveredListener implements MouseListener{

	public void mouseClicked(MouseEvent e) {
	
	}

	@Override
	public void mouseEntered(MouseEvent e) {
if(e.getSource()==flowers){
	changeButton(flowers);
}else if(e.getSource()==crops){
	changeButton(crops);
}else if(e.getSource()==trees){
	changeButton(trees);
}else if(e.getSource()==bushes){
	changeButton(bushes);
}else if(e.getSource()==all){
	changeButton(all);
}
	}

	@Override
	public void mouseExited(MouseEvent e) {
		if(e.getSource()==flowers&&flowerSelected==false){
			changeToDefault(flowers);
		}else if(e.getSource()==crops&&cropSelected==false){
			changeToDefault(crops);
		}else if(e.getSource()==trees&&treeSelected==false){
			changeToDefault(trees);
		}else if(e.getSource()==bushes&&bushSelected==false){
			changeToDefault(bushes);
		}else if(e.getSource()==all&&allSelected==false){
			changeToDefault(all);
		}
	}

	@Override
	public void mousePressed(MouseEvent e) {

	}

	@Override
	public void mouseReleased(MouseEvent e) {

	}
	
}//End of ButtonHoveredListener
private class textFocusListener implements FocusListener{

	@Override
	public void focusGained(FocusEvent e) {
if(e.getSource()==searchByName && searchByName.getText().equals("Search By Plant Name")){
	searchByName.setText("");
}
		
	}
	public void focusLost(FocusEvent e) {
		if(e.getSource()==searchByName && searchByName.getText().equals("")){
			searchByName.setText("Search By Plant Name");
		}
		
	}
	
}//End of textFocusListener
private class textKeyListener implements KeyListener{


	public void keyPressed(KeyEvent e) {
		if(e.getKeyChar()=='\n'){
			reCreateFrame();
		}
		
	}


	public void keyReleased(KeyEvent arg0) {
		
		
	}


	public void keyTyped(KeyEvent arg0) {

		
	}
	
	
}//End of textKeyListener
class frameListener implements ComponentListener{

	@Override
	public void componentHidden(ComponentEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void componentMoved(ComponentEvent e) {
		if(initiateFrame!=null){
			initiateFrame.setLocationRelativeTo(null);
		}
		
	}

	@Override
	public void componentResized(ComponentEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void componentShown(ComponentEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	
}//End of frameListener
class windowListener implements WindowListener{

	@Override
	public void windowActivated(WindowEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowClosed(WindowEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowClosing(WindowEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowDeactivated(WindowEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowDeiconified(WindowEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowIconified(WindowEvent arg0) {
	initiateFrame.setState(JFrame.NORMAL);
		
	}

	@Override
	public void windowOpened(WindowEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	
}
public static Font getFontPlantButtons() {
	return fontPlantButtons;
}
public static void setFontPlantButtons(Font fontPlantButtons) {
	plantSearch.fontPlantButtons = fontPlantButtons;
}
public static Color getBlueColor() {
	return blueColor;
}
public static void setBlueColor(Color blueColor) {
	plantSearch.blueColor = blueColor;
}
public static Color getGreyColor() {
	return greyColor;
}
public void setGreyColor(Color greyColor) {
	plantSearch.greyColor = greyColor;
}
public static JButton getLogo() {
	return logo;
}
public void setLogo(JButton logo) {
	plantSearch.logo = logo;
}public Connection getCon() {
	return con;
}
public void setCon(Connection con) {
	this.con = con;
}
public JTextField getSearchByName() {
	return searchByName;
}
public void setSearchByName(JTextField searchByName) {
	this.searchByName = searchByName;
}
}//End of Class plantSearch
