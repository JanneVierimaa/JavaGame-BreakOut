//importoidaan mukaan tarvittavat luokkakirjastot 
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import static javafx.application.Application.STYLESHEET_CASPIAN;
import static javafx.application.Application.launch;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Bounds;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.stage.Stage;
import javafx.util.Duration;

//Luokka JavaPeli1
public class JavaPeli1 extends Application {

    public static Circle circle;
    public static Pane canvas;
    public static Rectangle[] rectangleAr;
    public static Text score;
    public static Text text;
    private boolean gameStarted;
    private int scoreCounter=0;
    private int hiScore=0;
    private int lives;
    private double moveBat=0.0;
     //nopeus muuttujat
    private double deltaX;
    private double deltaY;
    private double x; //kiihdytyskertoimet
    private double y;
    private int level=1;
    
    
    //alusta Palikat - metodi
    public void initBlocks(){
        double xStart =900;                                                                   //määritetään aloituskoordinaatit x ja y
        double yStart =30;
        
        for(int i=0; i<rectangleAr.length; i++){                                              //luodaan palikat taulukkomuodossa for-loopissa
            rectangleAr[i] = new Rectangle(xStart, yStart, 25, 80);
            rectangleAr[i].setFill(Color.color(Math.random(), Math.random(), Math.random())); //arvotaan värit
            rectangleAr[i].setStroke(Color.GREY);                                             //harmaa ääriviiva
            yStart=yStart+90;                                                                 //seuraavan palikan y-koordinaatin "väli" 10px (90-80)             
            if((yStart+90)>=canvas.getBoundsInLocal().getMaxY()){                             //tarkistetaan ettei palikat mene näyttöalueen yli
                xStart=xStart+50;                                                             //ja tehdään "sarakkeen vaihto" 
                yStart=30;
            }
            
        }
        for(int i=0; i<rectangleAr.length; i++){                                              //käydään läpi palikkataulukko ja lisätään jokainen palikkaolio canvasille
        canvas.getChildren().add(rectangleAr[i]);
        }
    }
    
    //getScore-metodi, hakee näytölle uudet pistemäärät, ja elämien määrän
    public void getScore(){
        score.setText("Score: "+scoreCounter + "  | HiScore: "+ hiScore+ "  | Lives: " + lives + "  | Level: " + level );
    }
    
    //initBall metodi - alustaa pallon aseman ja nopeuden sekä kiihtyvyyskertoimet
    public void initBall(){
        circle.relocate(400, 300);
        deltaX=3.0;
        deltaY=3.0;
        x=0.125*level;
        y=0.125*level;
    }
    
    //moveBall metodi - alkaa liikuttaa palloa nopeudella deltaX ja deltaY
    public void moveBall(){
        text.setText("");
        circle.setLayoutX(circle.getLayoutX() + deltaX);
        circle.setLayoutY(circle.getLayoutY() + deltaY);
    }
    //changeLevel metodi - kasvattaa leveliä ja alustaa tiilimuurin ja pallon uudelleen
    public void changeLevel(){
        initBlocks();
        initBall();
        gameStarted = false;
        //x=x+(0.125*level);
        //y=y+(0.125*level);
        level++;
        text.setText("Level " +level);
    }
    //gameOver - metodilla peli alkaa alusta, pisteet tulostetaan ja jos hiScore, niin myös se tulostetaan
    public void gameOver(){
        initBlocks();
        text.setText("Game Over! You scored: " +scoreCounter);
            if(scoreCounter>hiScore){
                hiScore=scoreCounter;
                text.setText("Game Over! You scored: "+scoreCounter + " New HiScore!!");
                }
        initStart();
    }
    
    public void initStart(){
        scoreCounter = 0;
        lives=5;
        level = 1;
    }
    
    @Override
    public void start(final Stage primaryStage) {

        canvas = new Pane();
        final Scene scene = new Scene(canvas, 1100, 600);
        
        primaryStage.setTitle("JavaPeli");
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();

        circle = new Circle(15, Color.BLUE);                    //luodaan pallo-olio
        Rectangle rectangle = new Rectangle(50,300,15,200);     //luodaan lyöntimaila rectangle-olio
        rectangle.setFill(Color.BLACK);
        rectangle.setStroke(Color.GREY);
        rectangleAr = new Rectangle[24];                        //luodaan "tiilimuuri" rectangle-olio taulukkona                                            
        text = new Text(300,400,"Click Ball to Start Game");
        text.setFont(Font.font(STYLESHEET_CASPIAN, FontPosture.REGULAR, 30));
        score = new Text(300,40,"");
        score.setFont(Font.font(STYLESHEET_CASPIAN, FontPosture.REGULAR,20));
        
        initStart();                                                //haetaan aloitusarvot muuttujille lives, scorecounter ja level
        getScore();                                                 //haetaan pisteet ja elämät
        initBall();                                                 //alustetaan pallon asema ja nopeus
        
        
        canvas.getChildren().addAll(circle, rectangle,text,score);  //lisätään kaikki näytölle canvasille
        initBlocks();                                               //asetetaan tiilimuuri-palikat
              
        circle.setOnMouseClicked(e ->  gameStarted = true);         //kun hiirellä klikataan palloa, peli alkaa
        
        
        final Timeline loop;
        loop = new Timeline(new KeyFrame(Duration.millis(10), new EventHandler<ActionEvent>() {
            
           
            //double moveBat=0.0;  //apumuuttuja mailan hallintaan
            
            
            
            @Override
            
            public void handle(final ActionEvent t) {
                
                if(gameStarted){
                    moveBall();
                }
                
                //määritellään näyttöalueen "rajat" joista pallo pomppaa poispäin
                final Bounds bounds = canvas.getBoundsInLocal();
                final boolean atRightBorder = circle.getLayoutX() >= (bounds.getMaxX() - circle.getRadius());
                final boolean atLeftBorder = circle.getLayoutX() <= (bounds.getMinX() + circle.getRadius());
                final boolean atBottomBorder = circle.getLayoutY() >= (bounds.getMaxY() - circle.getRadius());
                final boolean atTopBorder = circle.getLayoutY() <= (bounds.getMinY() + circle.getRadius());
                
                
             //kun hiirtä liikutellaan näyttöalueella maila liikkuu mukana. 
             //Tarkistetaan kuitenkin että mailan alaosa ei mene näyttöalueen ulkopuolelle
            canvas.setOnMouseMoved(event -> moveBat = event.getY());
               if(moveBat < (bounds.getMaxY() - rectangle.getHeight()))
               {
                   rectangle.setY(moveBat);
                   
               }
               //korjataan mailan paikkaa jos hiiri vedetään "liian alas"
               else if(moveBat >=(bounds.getMaxX() - rectangle.getHeight()))
               {
                   rectangle.setY(bounds.getMaxX() - rectangle.getHeight());
               }
               
                //Jos pallo törmää oikeaan reunaan käännetään nopeus päinvastaiseksi.
                //Ja pallon väri vaihtuu randomilla, myös x suuntainen nopeus kiihtyy x(=0,125*level) verran
               if (atRightBorder) {
                    x=-x;
                    circle.setFill(Color.color(Math.random(), Math.random(), Math.random()));
                    deltaX = -deltaX;
                    deltaX=deltaX+x;                                                      
                }
                
               //Jos pallo osuu vasempaan reunaan, vähennetään yksi elämä ja laitetaan peli poikki.
               //pallo asetetaan takaisin keskelle initBall() - metodilla. 
               if (atLeftBorder){
                   lives--;
                   gameStarted = false;
                   text.setText("Try Again! Click the ball..Lives left: " +lives);
                   initBall();
                   
                        //jos elämät loppuvat pudotetaan level takaisin 1:een ja tulostetaan Game over ja pisteet. level asetetaan takaisin 1:een, Gameover - metodia käyttäen
                        if(lives==0){
                            gameOver();
                        }
                   getScore();  
                }
                //jos pallo osuu näyttöalueen ala tai yläreunaan käänetään y-akselin suuntainen nopeus päinvastaiseen suuntaan
                //myös nopeuskertoimen merkki vaihtuu, ja pallo vaihtaa väriä
                if (atBottomBorder || atTopBorder) {
                    y=-y;
                    circle.setFill(Color.color(Math.random(), Math.random(), Math.random()));
                    deltaY = -deltaY;
                    //circle.setRadius(circle.getRadius()-1.0);
                    //deltaY=deltaY+y;
                }
               
                //jos pallo osuu mailaan, käännetään x-suuntainen nopeus päinvastaiseksi ja lisätään nopeutta
                if (rectangle.contains(circle.getLayoutX(),circle.getLayoutY()))
               {
                   //x=x+2;
                   x=-x;
                   //y=-y;
                   deltaX=-deltaX;
                   //deltaY=-deltaY;
                   deltaX=deltaX+x;
                   //deltaY=deltaY+y;
                   
               }
               //tarkastetaan osuuko pallo tiilimuurin palikoihin ja käydään läpi taulukko, jos osuu käännetään x-suuntainen nopeus ja kiihdytetään
               for(int i=0; i<24; i++){
                   if(rectangleAr[i].contains(circle.getLayoutX(), circle.getLayoutY())){
                       x=-x;
                       //y=-y;
                       deltaX=-deltaX;
                       //deltaY=deltaY+y;
                       deltaX=deltaX+x;
                       rectangleAr[i].resize(1,1);
                       rectangleAr[i].relocate(0, 0);
                       rectangleAr[i].setVisible(false);
                       rectangleAr[i].setX(900);
                       rectangleAr[i].setY(900);
                       canvas.getChildren().remove(rectangleAr[i]);
                        
                       scoreCounter++;
                       
                       //mikäli kaikki palikat on saatu rikottua vaihdetaan seuraava level
                       if(scoreCounter == (rectangleAr.length * level)){
                           changeLevel();
                       }
                       getScore();
                       //canvas.getChildren().remove(rectangleAr[i]);
                       
                   }
                       
               }
            }
        }));

        loop.setCycleCount(Timeline.INDEFINITE);
        loop.play();
        
        
    }

    public static void main(final String[] args) {
        launch(args);
    }
}