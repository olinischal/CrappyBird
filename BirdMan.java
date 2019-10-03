package birdmandemo;

import static birdmandemo.BirdMan.y;
import static birdmandemo.Main.frame;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import javax.imageio.ImageIO;
import javax.swing.Timer;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JPanel;
import java.awt.event.*;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.JFrame;

import java.util.*;
import java.lang.*;


public class BirdMan {

    static int DIAMETER = 25;
    static int X = ( Game.WIDTH / 2 ) - ( DIAMETER / 2 );
    static int y =  Game.HEIGHT / 2;
    static int acceleration = 1;
    static int speed = 2;

    static boolean dead = false;
    static BufferedImage img = null;{
        try {
            img = ImageIO.read(new URL("http://i.imgur.com/mw0ai3K.png"));

        } catch (IOException e) {
            System.out.println("WRONG BIRD");  //Prints "WRONG BIRD" if there is an error retrieving the image
        }}

    public BirdMan(){
    }


    public void jump(){
        speed = - 17;
    }

    public static void move(){

        if ( ( y > 0 ) && ( y < Game.HEIGHT )) {
            speed += acceleration;
            y += speed;
        }
        else {
            Game.playing= false;
            Game.reset();


        }

    }


    public static void paint(Graphics g){
        g.drawImage(img, X, y, null);
    }

    public static Rectangle getBounds(){
        return new Rectangle(X, y, DIAMETER, DIAMETER);
    }


}

@SuppressWarnings("serial")
class Game extends JPanel{

    static int HEIGHT = 800;
    static int WIDTH = 600;
    BirdMan birdy = new BirdMan();
    Wall wall = new Wall(WIDTH);
    Wall wall2 = new Wall(WIDTH + (WIDTH / 2));
    User[] user1 = new User[100];
    // user1[0] = new User();
    static int score = 0;
    int scrollX = 0;
    static boolean dead = false;
    static String deathMessage = "" ;
    static boolean gameOver = false;
    static boolean playing = true;
    String input ;
    int count = 0;
    int pass = 0;
    //grabs the background from Imgur
    BufferedImage img = null;{
        try {
            img = ImageIO.read(new URL("http://i.imgur.com/cXaR0vS.png"));
        } catch (IOException e) {
            System.out.println("WRONG BACKGROUND");
        }}


    public Game(String input){
        this.input = input;


        this.addMouseListener(new MouseAdapter(){

            public void mousePressed(MouseEvent arg0) {
                birdy.jump();
            }

        });

    }


    @SuppressWarnings("static-access")
    public void paint(Graphics g){
        super.paint(g);

        g.drawImage(img, scrollX, 0, null);
        g.drawImage(img, scrollX + 1800, 0, null);

        wall.paint(g);
        wall2.paint(g);
        birdy.paint(g);

        g.setFont(new Font("comicsans", Font.BOLD, 40));
        g.drawString("" + score, WIDTH / 2 - 20, 700);
        g.drawString(deathMessage, 200, 200);
    }



    public static void reset(){
        BirdMan.speed = 2;

        Game.deathMessage = "You died, try again";

        Timer deathTimer = new Timer(3000, new ActionListener(){
            public void actionPerformed(ActionEvent event){
                Game.deathMessage = "";
            };
        });

        deathTimer.start();
    }

    public void users(int pass) throws IOException {


        if(pass == 0){
            user1[count] = new User(input);

        }
        pass += 1;
        user1[count].setScore(score);
        System.out.println("User High score is " + user1[count].getScore());

        JOptionPane.showMessageDialog(null, user1[count].getName() + " score is " + score + "\n" + user1[count].getName() +
                " high score is " + user1[count].getScore());
        int a = JOptionPane.showConfirmDialog(null, "Do You Want To Restart The Game?", "Game Over",
                JOptionPane.YES_NO_CANCEL_OPTION);
        if (a == JOptionPane.YES_OPTION) {

            y = Game.HEIGHT / 2;


            wall.x = 600;
            wall2.x = 600 + (WIDTH / 2);
            playing = true;
        } else if (a == JOptionPane.NO_OPTION) {
            print();
            givenWritingStringToFile_whenUsingPrintWriter_thenCorrect();
            System.exit(0);
        } else if (a == JOptionPane.CANCEL_OPTION) {
            count += 1;
            newUsers(count);

        }


    }

    public void newUsers(int count){


        String input1 = JOptionPane.showInputDialog(null, "Enter player name:", "Welcome to Crappy Birds",
                JOptionPane.WARNING_MESSAGE);
        System.out.println(input1);
        System.out.println("the number of times user chose to create new user is " + count);

        user1[count] = new User(input1);



        y = Game.HEIGHT / 2;

        wall.x = 600;
        wall2.x = 600 + (WIDTH / 2);
        playing = true;


    }

    public void print(){
        for (int i = 0; i < count + 1 ; i++){
            System.out.println(user1[i].getName() + " : " + user1[i].getScore() + "\n");
        }
    }

    public static HashMap<String, Integer> sortByValue(HashMap<String, Integer> hm)
    {
        List<Map.Entry<String, Integer> > list =
                new LinkedList<Map.Entry<String, Integer> >(hm.entrySet());

        Collections.sort(list, new Comparator<Map.Entry<String, Integer> >() {
            public int compare(Map.Entry<String, Integer> o1,
                               Map.Entry<String, Integer> o2)
            {
                return (o2.getValue()).compareTo(o1.getValue());
            }
        });

        HashMap<String, Integer> temp = new LinkedHashMap<String, Integer>();
        for (Map.Entry<String, Integer> aa : list) {
            temp.put(aa.getKey(), aa.getValue());
        }
        return temp;
    }


    public void givenWritingStringToFile_whenUsingPrintWriter_thenCorrect() throws IOException
    {

        HashMap<String, Integer> hm = new HashMap<String, Integer>();

        for (int i = 0; i < count + 1 ; i++){

            hm.put(user1[i].getName(), user1[i].getScore());
        }
        Map<String, Integer> hm1 = sortByValue(hm);

        FileWriter fileWriter = new FileWriter("Scoreboard.txt" , false);
        PrintWriter printWriter = new PrintWriter(fileWriter);

        for (Map.Entry<String, Integer> en : hm1.entrySet()){

            printWriter.println(en.getKey() + " : " + en.getValue());

        }



        printWriter.close();
    }



    @SuppressWarnings("static-access")
    public void move() {

        if (playing) {

            wall.move();
            wall2.move();
            birdy.move();

            scrollX += Wall.speed;

            if (scrollX == -1800)
            {
                scrollX = 0;
            }

            if ((wall.x == BirdMan.X) || (wall2.x == BirdMan.X)) {
                score();
            }

            this.addMouseListener(new MouseAdapter() {

                @SuppressWarnings("SuspiciousIndentAfterControlStatement")
                public void mousePressed(MouseEvent arg0) {
                    if (!playing) {

                        try {
                            users(pass);
                        } catch (IOException ex) {
                            Logger.getLogger(Game.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        pass = 1;
                        score = 0;


                    }
                }

            });
        }

    }



    public static void score(){
        score += 1;

        System.out.println("The updated score is " + score);
    }

    @SuppressWarnings("static-access")
    public void score1(){

        System.out.println("The value of user score is " + user1[count].getScore());
    }

    public void printUserDetails(){
        user1[count].setUserName(input);
        user1[count].print();
    }

}


class Main {

    static JFrame frame = new JFrame();
    public static void main (String [] args) throws InterruptedException{

        frame.setSize(Game.WIDTH, Game.HEIGHT);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);

        runnit();

    }

    public static void runnit() throws InterruptedException{
        String input = JOptionPane.showInputDialog(null, "Enter player name:", "Welcome to Crappy Birds",
                JOptionPane.WARNING_MESSAGE);
        System.out.println(input);

        final Menu menu = new Menu();
        final Game game = new Game(input);


        Timer animationTimer = new Timer(20, new ActionListener(){
            public void actionPerformed(ActionEvent event){
                game.repaint();

                game.move();
            };
        });

        frame.add(menu);
        menu.setVisible(true);
        frame.revalidate();
        frame.repaint();

        while (menu.startGame == false){
            Thread.sleep(10);
        }


        frame.remove(menu);
        frame.add(game);
        game.setVisible(true);
        frame.revalidate();

        animationTimer.start();


    }


}


class Menu extends JPanel{
    private static final long serialVersionUID = 1L;
    int highscore;

    static BufferedImage img = null;{
        try {
            img = ImageIO.read(new URL("http://i.imgur.com/U6KEwxe.png"));
        } catch (IOException e) {
            System.out.println("WRONG MENU");
        }}

    boolean startGame = false;


    public Menu(){
        setFocusable(true);
        addMouseListener(new MouseAdapter(){
            @Override
            public void mouseClicked(MouseEvent e) {
                startGame = true;
            }

        });
    }

    public void paint (Graphics g){
        super.paint(g);

        g.drawImage(img, 0, 0, null);


    }
}



class Wall {

    Random rnd = new Random();

    int x ;
    int y = rnd.nextInt(Game.HEIGHT - 400) + 200;
    static int speed = - 6;
    int WIDTH = 45;
    int height = Game.HEIGHT - y;
    int GAP = 200;

    //procures the Wall image from Imgur
    static BufferedImage img = null;{
        try {
            img = ImageIO.read(new URL("http://i.imgur.com/4SUsUuc.png"));

        } catch (IOException e) {
            System.out.println("WRONG WALL");
        }}

    public Wall(int i){
        this.x = i;
    }

    //draws the wall
    public void paint(Graphics g){
        g.drawImage(img, x, y, null);                       //top part
        g.drawImage(img, x, ( -Game.HEIGHT ) + ( y - GAP), null);  //bottom part
    }

    public void move(){

        x += speed;
        Rectangle wallBounds = new Rectangle(x, y, WIDTH, height);
        Rectangle wallBoundsTop = new Rectangle(x, 0, WIDTH, Game.HEIGHT - (height + GAP));

        if ( (wallBounds.intersects(BirdMan.getBounds()) ) || (wallBoundsTop.intersects(BirdMan.getBounds()))){
            Game.playing = false;
            //died();
            Game.reset();


        }

        if (x <= 0 - WIDTH){
            x = Game.WIDTH;
            y = rnd.nextInt(Game.HEIGHT - 400) + 200;
            height = Game.HEIGHT - y;
        }
    }


    public void died(){
        Game.playing = false;
        y = rnd.nextInt(Game.HEIGHT - 400) + 200;
        height = Game.HEIGHT - y;

    }
}









