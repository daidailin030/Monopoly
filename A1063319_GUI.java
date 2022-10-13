import java.io.*;
import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.io.IOException;
import java.sql.ResultSet;
import java.util.Scanner;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;

public class A1063319_GUI extends JFrame implements ActionListener, Runnable{
    public JButton load, save, dice, exit; 
    public static JLabel dicenum, round, turn;
    public static int playerturn, roundnum, playernum = A1063319_Checkpoint6.CHARACTER_NUMBER.size(), movedPlayer;
    BufferedImage[] img = new BufferedImage[4];
    public static int[] userstartxLocation = {548, 582, 548, 582};
    public static int[] userstartyLocation = {586, 586, 620, 620};
    public static int[] userxLocation = {548, 582, 548, 582};
    public static int[] useryLocation = {586, 586, 620, 620};
    public static JLabel[] infor = new JLabel[5];
    int movex = 97;
    int movey = 96;
    boolean move = true;
    public static void main(String[] args) {
        ////TODO: GUI ////
        A1063319_Checkpoint6 user = new A1063319_Checkpoint6();
    }
    
    public A1063319_GUI() {
        super();
        LoadWait lw = new LoadWait();
        lw.start();

        setSize(700,700);
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel north = new JPanel(new FlowLayout());
        north.setPreferredSize(new Dimension(0,50));
        north.setBackground(Color.WHITE);

        //save
        save = new JButton("Save");
        save.setPreferredSize(new Dimension(75,30));
        north.add(save);
        save.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) 
            {
                try {
                    A1063319_Checkpoint6.Save("Character.txt", "Land.txt");
                }
                catch(IOException ex)
                {
                    System.out.println(ex.getMessage());
                }
            }
        });

        //load
        load = new JButton("Load");
        load.setPreferredSize(new Dimension(75,30));
        north.add(load);
        load.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) 
            {
                try {
                    setVisible(false);
                    roundnum=A1063319_Checkpoint6.Roundnum;
                    round.setText("Round "+roundnum);
                    playerturn = A1063319_Checkpoint6.Turn-1;
                    while(A1063319_Checkpoint6.status.get(playerturn)<=0){
                        playerturn++;
                        System.out.println("playerturn1"+playerturn);
                        if(playerturn==playernum){
                            playerturn=0;
                            roundnum++;
                            for(int i = 0; i < playernum ; i++){
                                A1063319_Checkpoint6.status.set(i,(A1063319_Checkpoint6.status.get(i)+1));
                            }
                        }else{
                            playerturn++;
                            if(playerturn==playernum){
                                playerturn=0;
                                roundnum++;
                                for(int i = 0; i < playernum ; i++){
                                    A1063319_Checkpoint6.status.set(i,(A1063319_Checkpoint6.status.get(i)+1));
                                }
                            }
                        }
                    }
                    A1063319_Checkpoint6.info.clear();
                    A1063319_Checkpoint6.location.clear();
                    A1063319_Checkpoint6.CHARACTER_NUMBER.clear();
                    A1063319_Checkpoint6.money.clear();
                    A1063319_Checkpoint6.status.clear();
                    A1063319_Checkpoint6.IMAGE_FILENAME.clear();
                    A1063319_Checkpoint6.LandOwner.clear();
                    A1063319_Checkpoint6.Land_TOLLS.clear();
                    A1063319_Checkpoint6.Land_PRICE.clear();
                    A1063319_Checkpoint6.Load("Character.txt", "Land.txt");
                    A1063319_GUI map = new A1063319_GUI();

                    Character[] player = new Character[playernum];
                    for(int i=0;i<playernum;i++) {
                        player[i]=new Character(A1063319_Checkpoint6.location.get(i), A1063319_Checkpoint6.CHARACTER_NUMBER.get(i), A1063319_Checkpoint6.money.get(i), A1063319_Checkpoint6.status.get(i), A1063319_Checkpoint6.IMAGE_FILENAME.get(i)) ; 
                    }
                    map.setVisible(true);
                }
                catch(IOException ex)
                {
                    System.out.println(ex.getMessage());
                }
            }
        });


        //character infor
        
        for(int x=0;x<playernum;x++) {
            infor[x]=new JLabel("<html>character "+A1063319_Checkpoint6.CHARACTER_NUMBER.get(x)+"<br>"+A1063319_Checkpoint6.money.get(x)+"</html>");
            infor[x].setPreferredSize(new Dimension(700/(playernum+2),45));
            infor[x].setVerticalAlignment(SwingConstants.CENTER);
            infor[x].setHorizontalAlignment(SwingConstants.CENTER);
            infor[x].setFont(new Font("Dialog",0, 16));	
            north.add(infor[x]);
        }
        if(playernum<4){
            JLabel[] none = new JLabel[5];
            for(int x = 0; x < ( 4-playernum ); x++){
                none[x] = new JLabel("");
                north.add(none[x]);
            }
        }
        add(north,BorderLayout.NORTH);

        //button exit
        JPanel button = new JPanel(new BorderLayout(0,0));
        button.setPreferredSize(new Dimension(0,18));
        exit = new JButton("EXIT");
        exit.setSize(30,18);
        button.setBackground(Color.WHITE);
        exit.addActionListener(this);
        button.add(exit,BorderLayout.EAST);
        add(button,BorderLayout.SOUTH);

        //left right
        JPanel borderleft = new JPanel();
        borderleft.setBackground(Color.WHITE);
        borderleft.setPreferredSize(new Dimension(28,0));
        add(borderleft,BorderLayout.WEST);
        JPanel borderright = new JPanel();
        borderright.setBackground(Color.WHITE);
        borderright.setPreferredSize(new Dimension(65,0));
        add(borderright,BorderLayout.EAST);

        //map
        JPanel middle = new JPanel(new BorderLayout(0,0));
        middle.setBackground(Color.WHITE);
        //north		
        JPanel line1 = new JPanel(new FlowLayout(FlowLayout.LEADING, 0, 0));
        line1.setBackground(Color.WHITE);
        ImageIcon[] pic1 = new ImageIcon[6];
        JLabel[] image1 = new JLabel[6];
        for(int x=0;x<6;x++) {
            pic1[x]=new ImageIcon((x+10)+".png");
            image1[x]=new JLabel(pic1[x]);
            line1.add(image1[x]);
        }
        middle.add(line1,BorderLayout.NORTH);

        //west
        JPanel line2 = new JPanel(new GridLayout(4,0,0,0));
        line2.setBackground(Color.WHITE);
        ImageIcon[] left = new ImageIcon[4];
        JLabel[] west = new JLabel[4];
        for(int x=3;x>=0;x--) {
            left[x]=new ImageIcon((x+6)+".png");
            west[x]=new JLabel(left[x]);
            line2.add(west[x]);
        }
        middle.add(line2,BorderLayout.WEST);

        //east
        JPanel line3 = new JPanel(new GridLayout(4,0,0,0));
        line3.setBackground(Color.WHITE);
        ImageIcon[] right = new ImageIcon[4];
        JLabel[] east = new JLabel[4];
        for(int x=0;x<4;x++) {
            right[x]=new ImageIcon((x+16)+".png");
            east[x]=new JLabel(right[x]);
            line3.add(east[x]);
        }
        middle.add(line3,BorderLayout.EAST);

        //south
        JPanel line6 = new JPanel(new FlowLayout(FlowLayout.LEADING, 0, 0));
        line6.setBackground(Color.WHITE);
        ImageIcon[] down = new ImageIcon[6];
        JLabel[] south = new JLabel[6];
        for(int x=5;x>=0;x--) {
            down[x]=new ImageIcon(x+".png");
            south[x]=new JLabel(down[x]);
            line6.add(south[x]);
        }
        middle.add(line6,BorderLayout.SOUTH);

        //center infor
        JPanel center = new JPanel(new BorderLayout(0,0));
        center.setBackground(Color.WHITE);
        ImageIcon title = new ImageIcon("title.png");
        JPanel titlepanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        titlepanel.setBackground(Color.WHITE);
        JLabel titleicon = new JLabel(title,SwingConstants.LEFT);
        titleicon.setVerticalAlignment(JLabel.CENTER);
        titleicon.setPreferredSize(new Dimension(title.getIconWidth(), title.getIconHeight()));
        titlepanel.add(titleicon);
        center.add(titlepanel,BorderLayout.NORTH);

        ImageIcon dicebg = new ImageIcon("display_dicenum.png") ;
        JLabel dice_bg = new JLabel(){
            public void paintComponent(Graphics g) {
                g.drawImage(dicebg.getImage(), 30, 6, dicebg.getIconWidth(), dicebg.getIconHeight(), dicebg.getImageObserver());
            }
        };
        dice_bg.setLayout(new FlowLayout(FlowLayout.CENTER));
        playerturn = A1063319_Checkpoint6.Turn-1;
        roundnum = A1063319_Checkpoint6.Roundnum;
        while(A1063319_Checkpoint6.status.get(playerturn)<=0){
            playerturn++;
            if(playerturn==playernum){
                playerturn=0;
                roundnum++;
                for(int i = 0; i < playernum ; i++){
                    A1063319_Checkpoint6.status.set(i,(A1063319_Checkpoint6.status.get(i)+1));
                }
            }else{
                playerturn++;
                if(playerturn==playernum){
                    playerturn=0;
                    roundnum++;
                    for(int i = 0; i < playernum ; i++){
                        A1063319_Checkpoint6.status.set(i,(A1063319_Checkpoint6.status.get(i)+1));
                    }
                }
            }
        }

        dicenum = new JLabel("0");
        dicenum.setHorizontalAlignment(dicenum.CENTER);
        dicenum.setVerticalAlignment(dicenum.CENTER);
        dicenum.setOpaque(false);
        dicenum.setFont(new Font("Dialog",0, 70));
        dice_bg.add(dicenum,BorderLayout.SOUTH);
        round = new JLabel("Round "+roundnum);
        round.setVerticalAlignment(SwingConstants.BOTTOM);
        round.setHorizontalAlignment(SwingConstants.LEFT);
        round.setFont(new Font("Dialog",0, 30));
        JPanel turnpanel = new JPanel();
        turnpanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
        turnpanel.setBackground(Color.WHITE);

        turn = new JLabel("Turn character "+A1063319_Checkpoint6.CHARACTER_NUMBER.get(playerturn));
        turn.setFont(new Font("Dialog",0, 40));
        turnpanel.add(turn);

        //dice
        ImageIcon diceicon = new ImageIcon("Dice.png");
        dice = new JButton(diceicon);
        dice.setBorder(null);
        dice.setBorderPainted(false);
        dice.setBackground(Color.WHITE);
        JPanel dicepanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 35));
        dicepanel.setBackground(Color.WHITE);
        dicepanel.setPreferredSize(new Dimension(180,200));
        dicepanel.add(dice);
        center.add(dicepanel,BorderLayout.WEST);
        dice.addActionListener(this);
        dice.setEnabled(true);

        JPanel diceinfor = new JPanel(new GridLayout(2,1,0,0));
        diceinfor.setBackground(Color.WHITE);
        diceinfor.setPreferredSize(new Dimension(170, 170));
        diceinfor.add(dice_bg,new FlowLayout(FlowLayout.CENTER));

        diceinfor.add(round,new FlowLayout(FlowLayout.CENTER));
        center.add(diceinfor,BorderLayout.EAST);		
        center.add(turnpanel,BorderLayout.SOUTH);

        middle.add(center,BorderLayout.CENTER);
        add(middle,BorderLayout.CENTER);

        try{
            for(int i=0;i<playernum;i++){
                img[i] = ImageIO.read(new File(A1063319_Checkpoint6.IMAGE_FILENAME.get(i)));
                int movesteps = A1063319_Checkpoint6.location.get(i)%20;
                if(movesteps<=5){
                    userxLocation[i]=userstartxLocation[i]-movex*movesteps;
                }
                //5 to 10
                else if(movesteps>5 && movesteps<=10){
                    userxLocation[i]=userstartxLocation[i]-movex*5;
                    useryLocation[i]=userstartyLocation[i]-movey*(movesteps-5);
                }
                //10 to 15
                else if(movesteps>10 && movesteps<=15){
                    useryLocation[i]=userstartyLocation[i]-movey*5;
                    userxLocation[i]=userstartxLocation[i]-movex*5+movex*(movesteps-10);
                }
                //15 to 20
                else if(movesteps>15 && movesteps<=20){
                    useryLocation[i]=userstartyLocation[i]-movey*5+movey*(movesteps-15);
                }
            }
        }catch (IOException ex){
        }
    }
    private class LoadWait extends Thread{
        public void run(){
            try{
                sleep(300);
            }catch(InterruptedException e){
                System.out.println(e.getMessage());
                System.exit(0);
            }
            repaint();
        }
    }
    public void paint(Graphics g) {
        super.paint(g); 
        int xLoaction = 560;
        int yLoaction = 650;
        int distancex = 97;
        int distancey = 96;
        int x = 0;
        while(x<5){
            if(A1063319_Checkpoint6.LandOwner.get(x)!=0){
                g.setFont(new Font("Dialog",0, 50));
                g.drawString(String.valueOf(A1063319_Checkpoint6.LandOwner.get(x)), xLoaction, yLoaction);
            }
            xLoaction -= distancex;
            x++;
        }

        xLoaction -= 13;
        yLoaction -= (distancey+5);
        int y = 6;
        while(y<10){
            if(A1063319_Checkpoint6.LandOwner.get(y)!=0){
                g.setFont(new Font("Dialog",0, 45));
                g.drawString(String.valueOf(A1063319_Checkpoint6.LandOwner.get(y)), xLoaction, yLoaction);
            }
            yLoaction -= distancey;
            y++;
        }
        yLoaction -= 23;
        xLoaction += (distancex+10);
        int x1 = 11;
        while(x1<15){
            if(A1063319_Checkpoint6.LandOwner.get(x1)!=0){
                g.setFont(new Font("Dialog",0, 45));
                g.drawString(String.valueOf(A1063319_Checkpoint6.LandOwner.get(x1)), xLoaction, yLoaction);
            }
            xLoaction += distancex;
            x1++;
        }
        xLoaction += 13;
        yLoaction += (distancey+19);
        int y1 = 16;
        while(y1<20){
            if(A1063319_Checkpoint6.LandOwner.get(y1)!=0){
                g.setFont(new Font("Dialog",0, 45));
                g.drawString(String.valueOf(A1063319_Checkpoint6.LandOwner.get(y1)), xLoaction, yLoaction);
            }
            yLoaction += distancey;
            y1++;
        }
        
        for(int i=0; i<playernum; i++){
            g.drawImage(img[i], userxLocation[i], useryLocation[i], null);
        }
    }

    public void startThread() {
        Thread t = new Thread(this);
        t.start();
    }

    public void run() {
        int moveplayer = movedPlayer;
        int steps = A1063319_Checkpoint6.dice;
        int curxlocate = userxLocation[moveplayer];
        int curylocate = useryLocation[moveplayer];
        int zeroxlocate = userstartxLocation[moveplayer];
        int zeroylocate = userstartyLocation[moveplayer];
        int direct = 0;
        int count = 0;
        int countx = 0;
        int county = 0;
        int distance = 0;
        if((curylocate == zeroylocate) && curxlocate <= zeroxlocate && curxlocate>= (zeroxlocate-movex*5)){
            direct = 1;
            int aleadywalk = ( (curxlocate - (zeroxlocate - movex*5 ) ) / movex );
            distance = (curxlocate-(zeroxlocate-movex*5)) + (steps-aleadywalk) * movey;
        }
        if((curxlocate == (zeroxlocate-movex*5)) && (curylocate > (zeroylocate-movey*5)||curylocate == (zeroylocate-movey*5)) && curylocate<= zeroxlocate){
            direct = 2;
            int aleadywalk = ( ( curylocate - ( zeroylocate - movey*5 ) ) / movey );
            distance = (curylocate-(zeroylocate-movey*5)) + (steps-aleadywalk) * movex;
        }
        if((curylocate == (zeroylocate-movey*5)) && curxlocate <= zeroxlocate && curxlocate>= (zeroxlocate-movex*5)){
            direct = 3;
            int aleadywalk = ( ( zeroxlocate - curxlocate ) / movex);
            distance = (zeroxlocate-curxlocate) + (steps-aleadywalk) * movey;
        }
        if((curxlocate == zeroxlocate) && curylocate <= zeroylocate && curylocate >= (zeroylocate-movey*5)){
            direct = 4;
            int aleadywalk = ( ( zeroylocate - curylocate ) / movey);
            distance = (zeroylocate-curylocate) + (steps-aleadywalk) * movex;
        }

        int SLEEP = 0;
        if(steps <=3){
            SLEEP = 2000 / distance;
        }else if(steps >3){
            SLEEP = 3000 / distance;
        }

        while(count < A1063319_Checkpoint6.dice){
            if(direct == 1){
                count = (curxlocate - (zeroxlocate-movex*5));
                if(userxLocation[moveplayer]> (zeroxlocate-movex*5)){
                    try {
                        Thread.sleep(SLEEP);
                    }catch(InterruptedException e) {
                        System.out.println("Unexcepted interrupt");
                        System.exit(0);
                    }	
                    userxLocation[moveplayer]--;
                    repaint();
                    countx ++;
                }else{
                    direct = 2;
                }
                count  = countx / movex + county / movey;
            }
            if(direct == 2){
                if(useryLocation[moveplayer] > (zeroylocate-movey*5)){
                    try {
                        Thread.sleep(SLEEP);
                    }catch(InterruptedException e) {
                        System.out.println("Unexcepted interrupt");
                        System.exit(0);
                    }	
                    useryLocation[moveplayer]--;
                    repaint();
                    county++;
                }else{
                    direct = 3;
                }
                count  = countx / movex + county / movey;
            }
            if(direct == 3){
                if(userxLocation[moveplayer] < zeroxlocate){
                    try {
                        Thread.sleep(SLEEP);
                    }catch(InterruptedException e) {
                        System.out.println("Unexcepted interrupt");
                        System.exit(0);
                    }	
                    userxLocation[moveplayer]++;
                    repaint();
                    countx++;
                }else{
                    direct = 4;
                }
                count  = countx / movex + county / 96;
            }
            if(direct == 4){
                if(useryLocation[moveplayer] < zeroylocate){
                    try {
                        Thread.sleep(SLEEP);
                    }catch(InterruptedException e) {
                        System.out.println("Unexcepted interrupt");
                        System.exit(0);
                    }	
                    useryLocation[moveplayer]++;
                    repaint();
                    county++;
                    if(useryLocation[moveplayer] == zeroylocate && A1063319_Checkpoint6.RoundFin){
                        A1063319_Checkpoint6.money.set(moveplayer, (A1063319_Checkpoint6.money.get(moveplayer)+2000));
			            infor[moveplayer].setText("<html>character "+A1063319_Checkpoint6.CHARACTER_NUMBER.get(moveplayer)+"<br>"+A1063319_Checkpoint6.money.get(moveplayer)+"</html>");
                        A1063319_Checkpoint6.RoundFin = false;
                    }
                }else{
                    if(A1063319_Checkpoint6.RoundFin){
                        A1063319_Checkpoint6.money.set(moveplayer, (A1063319_Checkpoint6.money.get(moveplayer)+2000));
			            infor[moveplayer].setText("<html>character "+A1063319_Checkpoint6.CHARACTER_NUMBER.get(moveplayer)+"<br>"+A1063319_Checkpoint6.money.get(moveplayer)+"</html>");
                        A1063319_Checkpoint6.RoundFin = false;
                    }
                    direct = 1;
                }
                count  = countx / movex + county / movey;
            }

        }
        int endLocation = A1063319_Checkpoint6.location.get(moveplayer);
        int havedOwner = A1063319_Checkpoint6.LandOwner.get(endLocation);
        int LandPrice = A1063319_Checkpoint6.Land_PRICE.get(endLocation);
        String Buy_Title = "Buy or Not!";
        if(havedOwner == 0 && ( endLocation%5!=0) && A1063319_Checkpoint6.money.get(moveplayer)>=LandPrice){
            String content = "<html><body><div width='200px' align='center'>Do you want to buy?\n";
            String Question = "<html><body><div width='200px' align='center'>$"+LandPrice;
            int confirm = JOptionPane.showOptionDialog(null, content+Question, ""+Buy_Title, JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE, null, null, null);
            if (confirm == JOptionPane.YES_OPTION) {
                System.out.println("BUY!");
                A1063319_Checkpoint6.LandOwner.set(A1063319_Checkpoint6.location.get(moveplayer),A1063319_Checkpoint6.CHARACTER_NUMBER.get(moveplayer));
                A1063319_Checkpoint6.money.set(moveplayer,(A1063319_Checkpoint6.money.get(moveplayer)-LandPrice));
                infor[moveplayer].setText("<html>character "+A1063319_Checkpoint6.CHARACTER_NUMBER.get(moveplayer)+"<br>"+A1063319_Checkpoint6.money.get(moveplayer)+"</html>");
                repaint();
            }
            if (confirm == JOptionPane.NO_OPTION){

            }

        }else if(havedOwner!=0 && havedOwner!= A1063319_Checkpoint6.CHARACTER_NUMBER.get(moveplayer)) {
            int PlaceOwner = A1063319_Checkpoint6.LandOwner.get(endLocation);
            int PlaceOwnerarray = PlaceOwner-1;
            int payTolls = A1063319_Checkpoint6.Land_TOLLS.get(endLocation);
            JOptionPane.showMessageDialog( null , "The place is owned by Chacter"+PlaceOwner+". Chacter"+A1063319_Checkpoint6.CHARACTER_NUMBER.get(moveplayer)+" needs to pay $"+payTolls+"." ,"Pay Tolls!" , JOptionPane.PLAIN_MESSAGE) ;
            System.out.println("Tolls:"+payTolls);
            int AfterPay = A1063319_Checkpoint6.money.get(moveplayer)-payTolls;
            int AfterReceive = A1063319_Checkpoint6.money.get(PlaceOwnerarray)+payTolls;
            A1063319_Checkpoint6.money.set(moveplayer, AfterPay);
            A1063319_Checkpoint6.money.set(PlaceOwnerarray, AfterReceive);
            infor[moveplayer].setText("<html>character "+A1063319_Checkpoint6.CHARACTER_NUMBER.get(moveplayer)+"<br>"+A1063319_Checkpoint6.money.get(moveplayer)+"</html>");
            infor[PlaceOwner-1].setText("<html>character "+A1063319_Checkpoint6.CHARACTER_NUMBER.get(PlaceOwnerarray)+"<br>"+A1063319_Checkpoint6.money.get(PlaceOwnerarray)+"</html>");
        }
        
        
        turn.setText("Turn character "+A1063319_Checkpoint6.CHARACTER_NUMBER.get(playerturn));
        round.setText("Round "+roundnum);
        dice.setEnabled(true);
    }
    public void actionPerformed(ActionEvent e){
        if(e.getSource()==dice) {	
            A1063319_Checkpoint6.Random();
            startThread();
            dice.setEnabled(false);
        }	
        if(e.getSource()==exit) {
            System.exit(0);
        }
    }
}
