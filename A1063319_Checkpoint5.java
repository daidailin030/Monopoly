import java.util.Scanner;
import java.util.ArrayList;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.*;
import java.sql.*;
import java.awt.*;
import javax.swing.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.util.regex.Pattern;

public class A1063319_Checkpoint5 extends JFrame implements ActionListener{
	static ArrayList<Integer> location = new ArrayList<>();
	static ArrayList<Integer> CHARACTER_NUMBER = new ArrayList<>();
	static ArrayList<Integer> money = new ArrayList<>();
	static ArrayList<Integer> status = new ArrayList<>();
	static ArrayList<String> IMAGE_FILENAME = new ArrayList<>();
	static ArrayList<String> info = new ArrayList<>();

	static ArrayList<Integer> LandOwner= new ArrayList<>();
	static ArrayList<Integer> Land_TOLLS= new ArrayList<>();
	static ArrayList<Integer> Land_PRICE= new ArrayList<>();
	static String dicenumber;
	static int Roundnum=0, Turn=0, dice=0;
	static boolean RoundFin = false;
	public static void main(String[] args) throws IOException{
		A1063319_Checkpoint5 map= new A1063319_Checkpoint5();
		map.setVisible(true);
		
	}
	public A1063319_Checkpoint5(){
		super();
		setLocation(200, 150);
		setSize(200, 200);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setResizable(false);
		// setLayout(new GridLayout(3,1));
		JPanel up = new JPanel();
		up.setPreferredSize(new Dimension(0,20));
		add(up, BorderLayout.NORTH);
		JPanel down = new JPanel();
		down.setPreferredSize(new Dimension(0,20));
		add(down, BorderLayout.SOUTH);
		JPanel left = new JPanel();
		left.setPreferredSize(new Dimension(40,0));
		add(left, BorderLayout.WEST);
		JPanel right = new JPanel();
		right.setPreferredSize(new Dimension(40,0));
		add(right, BorderLayout.EAST);

		JPanel center = new JPanel();
		center.setPreferredSize(new Dimension(80,150));
		center.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));
		JButton StartButton = new JButton("Start");
		StartButton.setPreferredSize(new Dimension(80,30));
		StartButton.addActionListener(this);
		center.add(StartButton);
		JButton LoadButton = new JButton("Load");
		LoadButton.setPreferredSize(new Dimension(80,30));
		LoadButton.addActionListener(this);
		center.add(LoadButton);
		JButton ExitButton = new JButton("Exit");
		ExitButton.setPreferredSize(new Dimension(80,30));
		ExitButton.addActionListener(this);
		center.add(ExitButton);
		add(center, BorderLayout.CENTER);
	}
	public void actionPerformed(ActionEvent e){
		String s = e.getActionCommand();
		if(s.equals("Start")){
			System.out.println("Start");
			File character_txt = new File("Character.txt");
			try{
				if(character_txt.exists()){
					character_txt.delete();
				}	
				character_txt.createNewFile();
				PrintWriter characterwrite = new PrintWriter(new BufferedWriter(new FileWriter("Character.txt")));
				characterwrite.write("Round:1,Turn:1\n"+"0,1,2000,1,Character_1.png\n"+"0,2,2000,1,Character_2.png\n"+"0,3,2000,1,Character_3.png\n"+"0,4,2000,1,Character_4.png");
				characterwrite.close();
				
				File land_txt = new File("Land.txt");
				if(land_txt.exists()){
					land_txt.delete();
				}
				land_txt.createNewFile();
				PrintWriter landwrite = new PrintWriter(new BufferedWriter(new FileWriter("Land.txt")));
				landwrite.write("LOCATION_NUMBER, owner\n"+"1,0\n"+"2,0\n"+"3,0\n"+"4,0\n"+"6,0\n"+"7,0\n"+"8,0\n"+"9,0\n"+"11,0\n"+"12,0\n"+"13,0\n"+"14,0\n"+"16,0\n"+"17,0\n"+"18,0\n"+"19,0");
				landwrite.close();

				A1063319_Checkpoint5.Load("Character.txt", "Land.txt");
				A1063319_GUI GUI = new A1063319_GUI();
				setVisible(false);
				GUI.setVisible(true);
			}catch(IOException ex){
			}
		}
		if(s.equals("Load")){
			System.out.println("Load");
			File character_txt = new File("Character.txt");
			File land_txt = new File("Land.txt");
			if(!character_txt.exists() || !land_txt.exists()){
				new errorGUI();
			}else{
				try{
					A1063319_Checkpoint5.Load("Character.txt", "Land.txt");
					A1063319_GUI GUI = new A1063319_GUI();
					setVisible(false);
					dispose();
					GUI.setVisible(true);
				}catch(IOException ex){

				}
				
			}
		}
		if(s.equals("Exit")){
			System.out.println("Exit");
			System.exit(0);
		}
	}
	private class errorGUI extends JFrame{
		public errorGUI(){
			super();
			setSize(150, 100);
			setLocation(230, 220);
			setResizable(false);
			JLabel errormsg = new JLabel("Without record",JLabel.CENTER);

			add(errormsg, BorderLayout.CENTER);

			JButton Confirm = new JButton("Confirm");
			JPanel confirmbutton = new JPanel();
			Confirm.setPreferredSize(new Dimension(100,30));
			Confirm.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e){
					setVisible(false);
					dispose();
				}
			});
			confirmbutton.add(Confirm);
			add(confirmbutton, BorderLayout.SOUTH);
			
			setVisible(true);
		}
	}
	public static void Load(String filename, String filename2) throws IOException {
		//// TODO: You should load the variables from the file. ////
		Scanner scanner = new Scanner(new BufferedReader(new FileReader(filename)));
		scanner.useDelimiter("[,\n]");
		boolean firstLine = true;
		while(scanner.hasNext()){
			if(firstLine){
				String Round = scanner.nextLine();
				for(String str:Round.split(":|,")){
					info.add(str);
				}
				Roundnum = Integer.valueOf(info.get(1));
				Turn = Integer.valueOf(info.get(3));
				firstLine = false;
			}
			String LOCATION = scanner.next();
			location.add(Integer.valueOf(LOCATION));
			String character_number = scanner.next();
			CHARACTER_NUMBER.add(Integer.valueOf(character_number));
			String MONEY = scanner.next();
			money.add(Integer.valueOf(MONEY));
			String STATUS = scanner.next();
			status.add(Integer.valueOf(STATUS));
			IMAGE_FILENAME.add(scanner.next().replaceAll("\\s", ""));
		}
		scanner.close();

		Scanner LandScanner = new Scanner(new BufferedReader(new FileReader(filename2)));
		LandScanner.useDelimiter("[,\n]");
		
		final String driver = "com.mysql.cj.jdbc.Driver";
		final String protocol = "jdbc:mysql:";
		try{
			Class.forName(driver).newInstance();
			System.out.println("Loaded the embedded driver.");
		}catch (Exception err){
			System.err.println("Unable to load the embedded driver.");
			err.printStackTrace(System.err);
			System.exit(0);
        }
		String url = "//140.127.220.220/CHECKPOINT?serverTimezone=UTC";
		String username = "checkpoint";
		String password = "ckppwd";
		ResultSet rs = null;
		try{
			System.out.println("Connecting to and creating the database...");
			Connection conn = DriverManager.getConnection(protocol + url, username, password );
			System.out.println("Database connected.");
			Statement s = conn.createStatement();
			boolean FirstLine = true;
			int x = 0;
			Land[] LandObject = new Land[20];
			while(LandScanner.hasNext()){
				if(FirstLine){
					LandScanner.nextLine();
					FirstLine = false;
					LandOwner.add(0);
					Land_TOLLS.add(0);
					Land_PRICE.add(0);
				}
				String Land_str = LandScanner.next();
				int Land = Integer.valueOf(Land_str);
				String Owner_str = LandScanner.next().replaceAll("\\s", "");
				int Owner = Integer.valueOf(Owner_str);
				rs = s.executeQuery("SELECT PLACE_NUMBER, LAND_PRICE, TOLLS FROM LAND WHERE PLACE_NUMBER="+Land);
				while(rs.next()) {
					int LAND_PRICE = rs.getInt("LAND_PRICE");
					int TOLLS = rs.getInt("TOLLS");
					LandObject[x] = new Land(Land, Owner, LAND_PRICE, TOLLS);
					LandOwner.add(Owner);
					Land_TOLLS.add(TOLLS);
					Land_PRICE.add(LAND_PRICE);
					if(Land == 4 || Land == 9 || Land == 14){
						LandOwner.add(0);
						Land_TOLLS.add(0);
						Land_PRICE.add(0);
					}
				}
				x++;
			}
			System.out.println("LandPrice:\n"+Land_PRICE);
			System.out.println("LandTolls:\n"+Land_TOLLS);
			LandScanner.close();
			rs.close();
			conn.close();
			int size=CHARACTER_NUMBER.size();
			Character[] player = new Character[size];
			
			for(int i=0;i<size;i++) {
				player[i]=new Character(location.get(i), CHARACTER_NUMBER.get(i), money.get(i), status.get(i), IMAGE_FILENAME.get(i)) ; 
			}
		}catch (SQLException err){
			System.err.println("SQL error.");
			err.printStackTrace(System.err);
			System.exit(0);
		}
		
	}

	public static void Save(String filename, String filename2) throws IOException {
		//// TODO: You should save the changed variables into original data (filename). ////
		BufferedWriter write = new BufferedWriter(new FileWriter(filename));
		int size=CHARACTER_NUMBER.size();
		String Round_str = String.valueOf(A1063319_GUI.roundnum);
		String Turn_str = String.valueOf(CHARACTER_NUMBER.get(A1063319_GUI.playerturn));
		write.write(info.get(0)+":"+Round_str+","+info.get(2)+":"+Turn_str+"\n");
		for(int i=0;i<size;i++) {
		//int to String
		String CHARACTER_NUMBER_str=String.valueOf(CHARACTER_NUMBER.get(i));
		String location_str=String.valueOf(location.get(i));
		String status_str=String.valueOf(status.get(i));
		String money_str=String.valueOf(money.get(i));
		write.write(location_str+","+CHARACTER_NUMBER_str+","+money_str+","+status_str+","+IMAGE_FILENAME.get(i)+"\n");
		}
		write.close();

		BufferedWriter LandWriter = new BufferedWriter(new FileWriter(filename2));
		LandWriter.write("LOCATION_NUMBER, owner\n");
		for(int x = 1; x<20; x++){
			if(x!=5 || x!=10 || x!=15){
				LandWriter.write(x+","+LandOwner.get(x)+"\n");
			}
		}
		LandWriter.close();
	}
	
	public static void Random() {
		//// TODO: while calling the Random function, Character.location should plus the random value, and Character.status should minus one.
		//// TODO: While Character.status more than zero(not include zero), Character can move(plus the random value).
		int length=CHARACTER_NUMBER.size();
        
		if(A1063319_GUI.playerturn<length) {
			if(status.get(A1063319_GUI.playerturn)>0) {
                A1063319_GUI.movedPlayer = A1063319_GUI.playerturn;
                int l=location.get(A1063319_GUI.playerturn);
				dice=(int)(Math.random()*6)+1;
				l+=dice;
				if(l >= 20){
					l -= 20;
					RoundFin = true;
				}
				System.out.println("Character"+CHARACTER_NUMBER.get(A1063319_GUI.playerturn)+":"+dice);
				location.set(A1063319_GUI.playerturn,l);
				String dicenumber=String.valueOf(dice);
				A1063319_GUI.dicenum.setText(dicenumber);
				int s=status.get(A1063319_GUI.playerturn);
				s--;
				status.set(A1063319_GUI.playerturn,s);
			}
			if(A1063319_GUI.playerturn==length-1) {
				for(int s=0;s<length;s++) {
					status.set(s,(status.get(s)+1));
				}
				A1063319_GUI.playerturn=0;
				A1063319_GUI.roundnum++;
			}else {
				A1063319_GUI.playerturn++;
			}
		}
		while(status.get(A1063319_GUI.playerturn)<=0 && A1063319_GUI.playerturn<=length) {
			A1063319_GUI.playerturn++;
			if(A1063319_GUI.playerturn == length) {
				for(int s=0;s<length;s++) {
					status.set(s,(status.get(s)+1));
				}
				A1063319_GUI.playerturn=0;
				A1063319_GUI.roundnum++;
				break;
			}
			
		}
		
	
	}

}
