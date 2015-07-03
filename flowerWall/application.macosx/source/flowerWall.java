import processing.core.*; 
import processing.data.*; 
import processing.event.*; 
import processing.opengl.*; 

import java.util.Random; 
import de.voidplus.leapmotion.*; 
import twitter4j.conf.*; 
import twitter4j.internal.async.*; 
import twitter4j.internal.logging.*; 
import twitter4j.internal.util.*; 
import twitter4j.management.*; 
import twitter4j.auth.*; 
import twitter4j.api.*; 
import twitter4j.util.*; 
import twitter4j.internal.http.*; 
import twitter4j.*; 
import java.util.*; 
import ddf.minim.*; 
import ddf.minim.signals.*; 
import ddf.minim.analysis.*; 
import ddf.minim.effects.*; 

import twitter4j.conf.*; 
import twitter4j.util.function.*; 
import twitter4j.internal.async.*; 
import twitter4j.internal.org.json.*; 
import twitter4j.internal.logging.*; 
import twitter4j.json.*; 
import twitter4j.internal.util.*; 
import twitter4j.management.*; 
import twitter4j.auth.*; 
import twitter4j.api.*; 
import twitter4j.util.*; 
import twitter4j.internal.http.*; 
import twitter4j.*; 
import twitter4j.internal.json.*; 

import java.util.HashMap; 
import java.util.ArrayList; 
import java.io.File; 
import java.io.BufferedReader; 
import java.io.PrintWriter; 
import java.io.InputStream; 
import java.io.OutputStream; 
import java.io.IOException; 

public class flowerWall extends PApplet {



ArrayList<Flower> flowerpot = new ArrayList<Flower>();


boolean darkMode = false;
boolean isMouseMoving = false;
boolean isHandDetected = false;
int w,h,ratio, wInc,hInc;
PVector[] flowerPositions ;
int[] randomOrder;
boolean[] isTweetSent; 
boolean[] isTweetSent2; 
int incPopulate = 0;
PVector handPosition = new PVector(0,0,0); 
boolean initTwitter;
//leap motion
Cursor leap ;
PVector leapPosition;
int globalIndex = 0;

//TWITTER
ModelTwitter modelBird;
ConfigBird configBird;
String[] hashTags;
String[] users;
int twitterKey;
ArrayList<TweetObject> tweetList = new ArrayList<TweetObject>();
int cursorLocation = 0;
boolean debug = false;
PImage roof, wiw, leaf;


public void setup() {
  w = 1920; 
  ratio = 16/9;
  h = 1200;
  leap = new Cursor(this);

  size(w, h, OPENGL);
  background(255);
  smooth(8);
  initImages();
  makeGrid();
  if(debug){
    initTwitter = false;
  }else{
    initTwitter = true;//
  }
  if(initTwitter){
    setUpTwitter();  
  }
  
}

public void draw() {
  background(0);

  if(isHandDetected){
     background(0);
      displayWiwLogo();
      for (Flower flower : flowerpot) {
          if(flower.isOver(PApplet.parseInt(leapPosition.x),PApplet.parseInt(leapPosition.y))){
            flower.displayFullFlower();
            flower.displayTweet();
          }else{
            flower.displayPointFlower();
            //flower.displayTweet();
          }

        }
    
  }else{
    background(255);
     for (Flower flower : flowerpot) {
          flower.displayFullFlower();
        }
      }
  
  if (frame != null){
     frame.setTitle("FPS "+PApplet.parseInt(frameRate+" position : "+cursorLocation));
  }

  for (int i = tweetList.size()-1; i >= 0; i--) {
    TweetObject tweet = tweetList.get(i);
    if(!isTweetSent[i]){
        if(!isTweetSent2[i]){
       addFlower(tweet.getUsername(),tweet.getMessage());
       isTweetSent2[i] = true;

     }
      println("flowerpot.size(): "+flowerpot.size());
      println("i: "+i);
      println("isTweetSent[i]"+isTweetSent[i]);
      if(flowerpot.size() > i){
         String imageName = flowerpot.get(i).getImageName();

         if(imageName != "" && isTweetSent[i] == false ){
          isTweetSent[i] = true;
          String message = "Une fleur de plus dans le jardin BBDO gr\u00e2ce \u00e0 @"+tweet.getUsername();
          println("imageName: "+imageName);
          modelBird.sendTweetWithMedia(message, "/Users/fabax/Desktop/flowerWall/"+imageName);
         }
       
      }
      
      //println("imageName: "+imageName);
      
    }
   
  }

  showCursor();
  if(!isHandDetected){
    displayRoofGardenLogo();  
  }
}

public void displayRoofGardenLogo(){
  pushMatrix();
  pushStyle();
  imageMode(CENTER);
  translate(w/2, h/2);
  image(roof, 0, 0);
  popStyle();
  popMatrix();
}

public void displayWiwLogo(){
  pushMatrix();
  pushStyle();
  imageMode(CENTER);
  translate(w/2, h/2);
  image(wiw, 0, 0);
  popStyle();
  popMatrix();
}

public void makeGrid(){
  int wInc = 10;
  int hInc = 6;

  int stepW = w/wInc;
  int stepH = h/hInc;

  flowerPositions = new PVector[wInc*hInc];
  randomOrder = new int[wInc*hInc];
  isTweetSent = new boolean[wInc*hInc];
  isTweetSent2 = new boolean[wInc*hInc];

  int inclol = 0;
  for (int i = 0; i < wInc; ++i) {
    for (int j = 0; j < hInc; ++j) {
      int variationX = (int) random(-(stepW/4),stepW/4);
      int variationY = (int) random(-(stepH/4),stepH/5);

      int newPositionX = (i*stepW + (stepW/2))+variationX;
      int newPositionY = (j*stepH + (stepH/2))+variationY;

      flowerPositions[inclol] = new PVector(newPositionX,newPositionY);

  inclol++;
    }
  }

  shuffleArray(0,(hInc*wInc));
  //populate();

}

public void detectPointerLocation(){
  int wInc = 10;
  int hInc = 6;

  int stepW = w/wInc;
  int stepH = h/hInc;

  int inclol = 0;
  for (int i = 0; i < wInc; ++i) {
    for (int j = 0; j < hInc; ++j) {     
      if(mouseX > stepW*i && mouseX < (stepW*i)+stepW){
        if(mouseY > stepH*j && mouseY < (stepH*j)+stepH){
            cursorLocation = inclol;
         }
      }
      inclol ++;
    }
  }
}

public void showCursor(){
  leap.update();
 
  isHandDetected = leap.handStatus();
  //println("isHandDetected: "+isHandDetected);

  leapPosition = leap.getPosition();
  stroke(0);
  if(isHandDetected){
    fill(0);
  }else{
    noFill();
  }

  ellipse(leapPosition.x, leapPosition.y, 20,20);
  noStroke();
  fill(255);
  ellipse(leapPosition.x, leapPosition.y, 5,5);
}

public void shuffleArray(int a, int b)
{
Random rgen = new Random();  // Random number generator   
    int sizeArray = b-a+1;
   
 
    for(int i=0; i< sizeArray-1; i++){
      randomOrder[i] = a+i;
    }
 
    for (int i=0; i<randomOrder.length-1; i++) {
        int randomPosition = rgen.nextInt(randomOrder.length-1);
        int temp = randomOrder[i];
        randomOrder[i] = randomOrder[randomPosition];
        randomOrder[randomPosition] = temp;
    }

}

public void addFlower(String _author, String _tweet) {

  PVector newflowerPosition = flowerPositions[randomOrder[incPopulate]];
  int sizeFlower = (int) map(_tweet.length(), 0, 144, 100, 200);
  println("flowerpot.size(): "+flowerpot.size());


  flowerpot.add(new Marguerite(sizeFlower,PApplet.parseInt(newflowerPosition.x),PApplet.parseInt(newflowerPosition.y),w,h, _author, _tweet));
  
  if(incPopulate < 59){
    incPopulate++;  
  }else{
    incPopulate = 0;
    // for (int i = 0; i < flowerpot.size(); ++i) {
    //   flowerpot.remove(i);  
    // }
    
  }

  if(globalIndex > 59){
    int r = (int) random(0, 60);
    flowerpot.remove(r);  
  }

  globalIndex ++; 
  
}

public void initImages(){
  roof = loadImage("roofgarden.png");
  roof.resize(PApplet.parseInt(h*0.90f), PApplet.parseInt(h*0.90f));
  wiw = loadImage("logoWiW.png");
  leaf = loadImage("roofgarden.png");
}

public void setUpTwitter(){
  configBird = new ConfigBird("configBird.json");
  initConfig();
  modelBird = new ModelTwitter(twitterKey,flowerPositions,randomOrder);
  modelBird.listenToHashtag(hashTags);
}

public void initConfig() {
  hashTags = configBird.jsonArrayToStringArray("hashtags");
  users = configBird.jsonArrayToStringArray("users");
  twitterKey = configBird.getInt("twitterKey");
}

public void mouseClicked(){
  if(debug){
    addFlower("fabax","Follow the story of the @InfiniteBridge, recently premiered at the RCM to great acclaim: http://bit.ly/1Jcjf0l  #GreatExhibitionists #NPAPW");  
  }
  
}



class Coquelicot extends Flower {

	int startingAngle;
	int angleInc;
	int petalesPerLayer	;
	int currentInc = 0;

	Coquelicot (int _size, int _x, int _y, int _sketchWidth, int _sketchHeight, String _author, String _tweet) {
		super (_size, _x, _y, _sketchWidth, _sketchHeight, _author,_tweet);
	}

	public void assemblePetales(){
		layer0();
		layer1();
	}

	public void layer0(){
		startingAngle = 0; 
		angleInc = 90; 
		petalesPerLayer = 360/angleInc;
		
		for (int i = 0; i < petalesPerLayer; ++i) {
			int newRotation = angleInc*i;
			int newColor = getPetaleColor(i);
			petales.add(new PetaleCoquelicot(new PVector(0,0), 7,couleur));
			petales.get(currentInc).setRotation(newRotation);
			petales.get(currentInc).setColor(newColor);

			currentInc ++;
		}
	}

	public void layer1(){
	startingAngle = 45; 
		angleInc = 45; 
		petalesPerLayer = 360/angleInc;

		for (int i = 0; i < petalesPerLayer; ++i) {
			int newRotation = angleInc*i;
			int newColor = getPetaleColor(i);
			petales.add(new PetaleCoquelicot(new PVector(0,0), 6,couleur));
			petales.get(currentInc).setRotation(newRotation);
			petales.get(currentInc).setColor(newColor);

			currentInc ++;
		}
	}

	public void initColors(){
	    couleur[0] = color(127, 0, 0);
	    couleur[1] = color(255, 76, 76);
	    couleur[2] = color(255, 0, 0);
	    couleur[3] = color(127, 38, 38);
	    couleur[4] = color(204, 0, 0);
	}

}
 

 class Cursor {

  PVector cursorPosition = new PVector(0,0,0);
  LeapMotion leap;
  Boolean detectHand = false;
  int inc = 0;

  Cursor (PApplet context) {
    leap = new LeapMotion(context);
  }

  public void update(){
    detectHand = false;

      for (Hand hand : leap.getHands()) {
      detectHand = true;    
      for (Finger finger : hand.getFingers ()) {
        PVector finger_position = finger.getPosition();

        int fingerType = finger.getType();

        if (fingerType == 1){ 
          setPosition(finger_position);
        }
      }
      
    }
  }

  public Boolean handStatus(){
    return detectHand;

  }

  public PVector getPosition(){
    return cursorPosition;
  }

  public void setPosition(PVector _cursorPosition){
    cursorPosition = _cursorPosition;
  }

}
class Flower  {
	PVector position = new PVector(0,0); 
	ArrayList<Petale> petales = new ArrayList<Petale>();
	int rotationInc = 3; 
	float rotation = 0;
	int rotationSpeed = 1;
	int[] colorSuite = new int[100];
	int[] couleur = new int[5];
	boolean isDark = false;
	int sizeFlower;
	int newHeight;
	boolean isOver = false;
	String author = "";
	String imageName = "";
	String tweet = "";
	PGraphics imageFlower;
	PGraphics imageFlowerPoint;
	int w,h;
	ArrayList<PGraphics> fullPetaleArray = new ArrayList<PGraphics>();
	ArrayList<PGraphics> pointPetaleArray = new ArrayList<PGraphics>();
	

	Flower (int _sizeFlower, int _x, int _y, int _sketchWidth, int _sketchHeight, String _author, String _tweet) {
		sizeFlower = _sizeFlower;
		position.x = _x; 
		position.y = _y;
		w = _sketchWidth;
		h = _sketchHeight;
		author = _author;
		tweet = _tweet;

		initColors();
		initColorSuite();
		assemblePetales();
	}

	public void assemblePetales(){
		
	}

	public void setImageName(String _name){
		imageName = _name;
	}

	public String getImageName(){
		return imageName;
	}

	public void rotateFlower(){
		rotate(radians(rotation));
		rotation += radians(1);
	}

	public void displayFullFlower(){
	}

	public void displayPointFlower(){

	}

	public void displayTweet(){
		pushMatrix();
		pushStyle();
		
		translate(position.x, position.y);
			fill(255,255,255,200);	
			rect(-10, -10, 210, 110);
			fill(0);
			text(author, 0, 0, 200,100);
			text(tweet, 0, 20, 200,100);
		popStyle();	
		popMatrix();
	}

	public void update(){
		
	}

	public void setPosition(PVector _position){
		position = _position;
	}

	public void setRotation(int _rotation , int _rotationSpeed){
		rotationSpeed = _rotationSpeed;
		rotation = _rotation;
	}

	public void initColors(){
		int randomCOlor = (int) random(0, 6);

		if(randomCOlor == 0){
			couleur[0] = color(246, 161, 33);
			couleur[1] = color(229, 149,27);
			couleur[2] = color(235, 152, 26);
			couleur[3] = color(231, 153, 36);
			couleur[4] = color(248, 196, 117);

		}else if(randomCOlor == 1){
			couleur[0] = color(84, 195, 244);
			couleur[1] = color(70, 182, 231);
			couleur[2] = color(62, 173, 222);
			couleur[3] = color(40, 147, 194);
			couleur[4] = color(166, 224, 249);
		}else if(randomCOlor == 2){
			couleur[0] = color(37, 164, 157);
			couleur[1] = color(62, 190, 184);
			couleur[2] = color(75, 204, 198);
			couleur[3] = color(167, 233, 229);
			couleur[4] = color(44, 164, 158);
		}else if(randomCOlor == 3){
			couleur[0] = color(238, 75, 69);
			couleur[1] = color(247, 89, 84);
			couleur[2] = color(250, 111, 107);
			couleur[3] = color(223, 59, 54);
			couleur[4] = color(252, 177, 174);

		}else if(randomCOlor == 4){
			couleur[0] = color(196, 72, 132);
			couleur[1] = color(223, 101, 160);
			couleur[2] = color(236, 118, 175);
			couleur[3] = color(239, 194, 216);
			couleur[4] = color(200, 104, 150);
		}else if(randomCOlor == 5){
			couleur[0] = color(215, 183, 228);
			couleur[1] = color(195, 133, 218);
			couleur[2] = color(176, 117, 203);
			couleur[3] = color(158, 98, 184);
			couleur[4] = color(146, 87, 172);
		}else if(randomCOlor == 6){
			couleur[0] = color(215, 183, 228);
			couleur[1] = color(195, 133, 218);
			couleur[2] = color(176, 117, 203);
			couleur[3] = color(158, 98, 184);
			couleur[4] = color(146, 87, 172);
		}
	   
	}

	public void initColorSuite(){
		for (int i = 0; i < 100; i++) {
	    	int rand = PApplet.parseInt(random(0, 5));
	    	colorSuite[i] = rand;
	    }
	}

	public int getPetaleColor(int _i){
		return couleur[colorSuite[_i]];
	}

	public int[] getAllColors(){
		return couleur; 
	}

	public PVector getPosition(){
		return position;
	}

	public int getSizeFlower(){
		return sizeFlower;
	}

	public void setDark(boolean _isDark){
		isDark = _isDark;
	}

	public void setSizeFlower(int _sizeFlower){
		sizeFlower = _sizeFlower;
	}

	public boolean isOver(int _mouseX, int _mouseY){
		float dist = dist(_mouseX, _mouseY, position.x, position.y);
		if(dist < sizeFlower/3){
			isOver = true; 
		}else{
			isOver = false;
		}

		return isOver;
		//return false;
	}

	public void setAuthor(String _author){
		author = _author;
	}

	public void setTweet(String _tweet){
		tweet = _tweet;
	}

	public PGraphics getImage(){
		return imageFlower;
	}





}
class Marguerite extends Flower {

	int startingAngle;
	int angleInc;
	int petalesPerLayer	;
	int currentInc = 0;
//	PGraphics imageFlower;
	boolean isDrawing = false;
	int incRotate = 10; 
	int amountLayer0 = 1; 


	int startAngle = 0; 
	boolean isFlowerCreated = false;
	boolean isAnimationRunning = true;
	int incImage = 0; 

	//imae position 
	int imageX, imageH;
	
	Marguerite (int _sizeFlower, int _x, int _y, int _sketchWidth, int _sketchHeiht,String _author, String _tweet) {
		super(_sizeFlower, _x, _y, _sketchWidth,_sketchHeiht,_author,_tweet);
	}

	public void assemblePetales(){
		layer0();
		layer1();
		layer2();
	}

	public void displayFullFlower(){
		imageX = PApplet.parseInt(position.x - (sizeFlower));
		imageH = PApplet.parseInt(position.y - (sizeFlower));

		if(!isAnimationRunning){
			if(!isFlowerCreated){
				makeFlowerImage();
				makeFlowerImagePoint();
			}else{
				pushMatrix();
				image(imageFlower,imageX,imageH);
				popMatrix();	
			}	
		}else{
			pushMatrix();
			translate(position.x, position.y);
			ellipse(0, 0, 10,10 );
			boolean isAnimationdone = false;
			for ( Petale  p : petales) {
				p.openingAnimation();
				isAnimationdone = p.getAnimationState();
			}
			popMatrix();
			if(isAnimationdone){
				isAnimationRunning = false;
			}
		}
	}

	public void displayPointFlower(){
		imageX = PApplet.parseInt(position.x - (sizeFlower));
		imageH = PApplet.parseInt(position.y - (sizeFlower));

		if(!isAnimationRunning){
			if(!isFlowerCreated){
				makeFlowerImage();
				makeFlowerImagePoint();
			}else{
				pushMatrix();
				image(imageFlowerPoint,imageX,imageH);
				popMatrix();	
			}	
		}else{
			pushMatrix();
			translate(position.x, position.y);
			ellipse(0, 0, 10,10 );
			boolean isAnimationdone = false;
			for ( Petale  p : petales) {
				p.openingAnimation();
				isAnimationdone = p.getAnimationState();
			}
			popMatrix();
			if(isAnimationdone){
				isAnimationRunning = false;
			}
		}
	}

	public void makeFlowerImage(){
	//	println("sizeFlower: "+sizeFlower);
		imageFlower = createGraphics(sizeFlower*2, sizeFlower*2);
		//println("sizeFlower: "+sizeFlower);
		imageFlower.beginDraw();
		
		float angleStep = 45;
		imageFlower.pushMatrix();
		imageFlower.translate(sizeFlower,sizeFlower);
		int newRange  =  fullPetaleArray.size()/3;
		float angleDeviation = 0;

		int incDisplay = 0;
		for (int i = 0; i < fullPetaleArray.size(); ++i) {

			if(i<newRange){
				angleDeviation = 0; 
			}else if(i>newRange && i<(newRange*2)){
				angleDeviation = angleStep/2; 
			}else if(i>(newRange*2) && i<(newRange*3)){
				angleDeviation = angleStep; 
			}
			
			imageFlower.imageMode(CENTER);
			int petaleLenght = fullPetaleArray.get(i).width;

		 	float x = (petaleLenght/2) * cos(radians((angleStep*i)+angleDeviation));
  			float y = (petaleLenght/2) * sin(radians((angleStep*i)+angleDeviation));



			imageFlower.rectMode(CENTER);
			imageFlower.pushMatrix();
			imageFlower.translate(x, y);
			imageFlower.rotate(radians(angleDeviation+(angleStep*i)));
			imageFlower.image(fullPetaleArray.get(i), 0, 0);
			imageFlower.popMatrix();
			//println("i: "+i);


		}		
		int rStringName = (int) random(0, 10000);

		imageFlower.popMatrix();
		imageFlower.endDraw();

		String randomName = "flowerToSend/flower"+PApplet.parseInt(millis())+".png";
		setImageName(randomName);
		imageFlower.save(randomName);
		isFlowerCreated = true;
		incImage ++;
	}

	public void makeFlowerImagePoint(){
	//	println("sizeFlower: "+sizeFlower);
		imageFlowerPoint = createGraphics(sizeFlower*2, sizeFlower*2);
		//println("sizeFlower: "+sizeFlower);
		imageFlowerPoint.beginDraw();
		
		float angleStep = 45;
		imageFlowerPoint.pushMatrix();
		imageFlowerPoint.translate(sizeFlower,sizeFlower);
		int newRange  =  pointPetaleArray.size()/3;
		float angleDeviation = 0;

		int incDisplay = 0;
		for (int i = 0; i < pointPetaleArray.size(); ++i) {

			if(i<newRange){
				angleDeviation = 0; 
			}else if(i>newRange && i<(newRange*2)){
				angleDeviation = angleStep/2; 
			}else if(i>(newRange*2) && i<(newRange*3)){
				angleDeviation = angleStep; 
			}
			
			imageFlowerPoint.imageMode(CENTER);
			int petaleLenght = pointPetaleArray.get(i).width;

		 	float x = (petaleLenght/2) * cos(radians((angleStep*i)+angleDeviation));
  			float y = (petaleLenght/2) * sin(radians((angleStep*i)+angleDeviation));

			imageFlowerPoint.rectMode(CENTER);
			imageFlowerPoint.pushMatrix();
			imageFlowerPoint.translate(x, y);
			imageFlowerPoint.rotate(radians(angleDeviation+(angleStep*i)));
			imageFlowerPoint.image(pointPetaleArray.get(i), 0, 0);
			imageFlowerPoint.popMatrix();
			//println("i: "+i);


		}		
		int rStringName = (int) random(0, 10000);

		imageFlowerPoint.popMatrix();
		imageFlowerPoint.endDraw();
		//imageFlowerPoint.save("flowerPoint/flower"+rStringName+".png");
		isFlowerCreated = true;
		incImage ++;
	}

	public void layer0(){
		startingAngle = 0; 
		angleInc = 45; 
		petalesPerLayer = 360/angleInc;
		for (int i = 0; i < petalesPerLayer; ++i) {

			float r = random(0.8f, 0.9f);
			newHeight = PApplet.parseInt(sizeFlower*r);
			//println("newHeight: "+newHeight);

			int newRotation = angleInc*i;
			int newColor = getPetaleColor(i);
			petales.add(new PetaleMarguerite(new PVector(0,0), 7, couleur));
			petales.get(currentInc).setRotation(newRotation);
			petales.get(currentInc).setColor(newColor);
			petales.get(currentInc).setPetaleHeight(newHeight);
			petales.get(currentInc).setPointsPerEdge(20);
			petales.get(currentInc).setStripe(false);
			fullPetaleArray.add(petales.get(currentInc).makePetaleImage(currentInc));
			pointPetaleArray.add(petales.get(currentInc).makePetaleImagePoint(currentInc));
			currentInc ++;
		}
	}

	public void setImageName(String _imageName){
		imageName = _imageName; 
		println("imageName: "+imageName);
	}

	public String getImageName(){
		return imageName ;
	}

	public void layer1(){
		startingAngle = 45/2; 
		angleInc = 45; 
		petalesPerLayer = 360/angleInc;
		

		for (int i = 0; i < petalesPerLayer; ++i) {
			float r = random(0.7f, 0.8f);
			newHeight = PApplet.parseInt(sizeFlower*r);
			int newRotation = angleInc*i;
			newRotation +=startingAngle;
			int newColor = getPetaleColor(i);
			petales.add(new PetaleMarguerite(new PVector(0,0), 6, couleur));
			petales.get(currentInc).setRotation(newRotation);
			petales.get(currentInc).setColor(newColor);
			petales.get(currentInc).setPetaleHeight(newHeight);
			petales.get(currentInc).setPointsPerEdge(20);
			petales.get(currentInc).setStripe(false);
			fullPetaleArray.add(petales.get(currentInc).makePetaleImage(currentInc));
			pointPetaleArray.add(petales.get(currentInc).makePetaleImagePoint(currentInc));
			//petales.get(currentInc).setPgraphic(imageFlower);
			currentInc ++;
		}
	}

	public void layer2(){
		startingAngle = 45/2; 
		angleInc = 45; 
		petalesPerLayer = 360/angleInc;
		

		for (int i = 0; i < petalesPerLayer; ++i) {
			float r = random(0.5f, 0.7f);
			newHeight = PApplet.parseInt(sizeFlower*r);

			int newRotation = angleInc*i;
			newRotation +=startingAngle;
			int newColor = getPetaleColor(i);
			petales.add(new PetaleMarguerite(new PVector(0,0), 6, couleur));
			petales.get(currentInc).setRotation(newRotation);
			petales.get(currentInc).setColor(newColor);
			petales.get(currentInc).setPetaleHeight(newHeight);
			petales.get(currentInc).setPointsPerEdge(20);
			petales.get(currentInc).setStripe(false);
			fullPetaleArray.add(petales.get(currentInc).makePetaleImage(currentInc));
			pointPetaleArray.add(petales.get(currentInc).makePetaleImagePoint(currentInc));
			//petales.get(currentInc).setPgraphic(imageFlower);
			currentInc ++;
		}
		currentInc = 0;
	}

	// void initColors(){
	//     couleur[0] = color(23, 65, 127,255);
	//     couleur[1] = color(122, 175, 255,255);
	//     couleur[2] = color(45, 129, 255,255);
	//     couleur[3] = color(61, 88, 127,255);
	//     couleur[4] = color(36, 104, 204,255);
	// }

	public void setSizeFlower(int _sizeFlower){
		sizeFlower = _sizeFlower;
		isDrawing = true;

	}

	public PGraphics getImage(){
		return imageFlower;
	}



}


//import twitter4j.internal.org.json.*;














public class ModelTwitter {
  //configuration de twitter
  Twitter twitter;
  User user;
  Configuration c;
  List<Status> tweets; // list of tweets for a search
  Status status;
  String[] hashTags;
  int timerListener ; // temps entre chaque envois de tweets au controller
  //Filtres
  String[] wordsOfTheTweet; // Array utiliser dans le filtrage pour stocker tous les mots d'un tweet
  boolean isFilterOn = false;
  String filterName;
  //cl\u00e9s twitter
  processing.data.JSONArray  twitterKeySet; 
  processing.data.JSONObject twitterKeys = new processing.data.JSONObject(); 
  processing.data.JSONArray autoTweetFile  = new processing.data.JSONArray();
  int incPopulate = 0; 
  PVector[] flowerPositionsT ;
  int[] randomOrderT;
  //--------------------------------------
  //  CONSTRUCTOR
  //--------------------------------------
  
  public ModelTwitter (int _twitterKey) {
    twitterConfiguration(_twitterKey);
    //go get the filter enable status
    isFilterOn = configBird.getBool("enableFilter");
    if(isFilterOn){
      //if filter is on we gp get the text;
      filterName = configBird.getString("filter");
    }
    //configuration of the time tweets get sent to the controller 
  }

  public ModelTwitter (int _twitterKey, PVector[] _flowerPostion, int[] _randomOrder) {
    twitterConfiguration(_twitterKey);
    //go get the filter enable status
    isFilterOn = configBird.getBool("enableFilter");
    if(isFilterOn){
      //if filter is on we gp get the text;
      filterName = configBird.getString("filter");
    }
    //configuration of the time tweets get sent to the controller 
  }

  //----- FIN DE GETTERS AND SETTERS
  // CONFIGURATION
  private void twitterConfiguration(int _twitterKey){
      ConfigurationBuilder cb = new ConfigurationBuilder();

      twitterKeySet = loadJSONArray("twitterKeys.json");
      twitterKeys = twitterKeySet.getJSONObject(_twitterKey);

      cb.setDebugEnabled(true);
      cb.setOAuthConsumerKey(twitterKeys.getString("setOAuthConsumerKey"));
      cb.setOAuthConsumerSecret(twitterKeys.getString("setOAuthConsumerSecret"));
      cb.setOAuthAccessToken(twitterKeys.getString("setOAuthAccessToken"));
      cb.setOAuthAccessTokenSecret(twitterKeys.getString("setOAuthAccessTokenSecret"));

      c = cb.build();
      TwitterFactory tf = new TwitterFactory(c);
      twitter = tf.getInstance();
  }
  //Methode that will listen to a specifique hashtag and will return the result in live
  public void listenToHashtag(String[] _keyWords){
        TwitterStream ts = new TwitterStreamFactory(c).getInstance();
        FilterQuery filterQuery = new FilterQuery(); 
        filterQuery.track(_keyWords);
        // On fait le lien entre le TwitterStream (qui r\u00e9cup\u00e8re les messages) et notre \u00e9couteur  
        ts.addListener(new TwitterListener(isFilterOn,filterName,timerListener));
         // On d\u00e9marre la recherche !
        ts.filter(filterQuery);  
  }  

  public void sendTweet(String _message){
    try {
        Status status = twitter.updateStatus(_message);
    }catch (TwitterException te){
        System.out.println("Error: "+ te.getMessage()); 
    }

  }
  public void sendTweetWithMedia(String _message, String _imageUrl){
     try {
        StatusUpdate status = new StatusUpdate(_message);
        status.setMedia(new File(_imageUrl));// BY SPECIFYING FILE PATH
        Status updateStatus = twitter.updateStatus(status);
      }catch (TwitterException te){
        System.out.println("Error: "+ te.getMessage()); 
      }
  }

  public void directMessage(String _reveiver ,String _directMessage){
    try {
        twitter.sendDirectMessage(_reveiver,_directMessage);
        println("Direct message sent");
    }catch (TwitterException te){
        System.out.println("Error: "+ te.getMessage()); 
    }
}

  // USER INFOS -----------------
  //get the user informations
  public void getUserInformations(String[] _users) {
    String[] userList = _users;
    for (int i = 0; i<userList.length; i++){
      try {
        user = twitter.showUser(userList[i]);
        displayUserInformations();
      } catch (TwitterException te) {
        println("Failed to get user informations " + te.getMessage());
        exit();
      }
    }
  }
  //display the user information (for debug)
  public void displayUserInformations() {
    println("getLocation(): "+user.getLocation());
    println("getFriendsCount(): "+user.getFriendsCount());
    println("getFollowersCount(): "+user.getFollowersCount());
    println("getDescription(): "+user.getDescription());
    println("getCreatedAt() : "+user.getCreatedAt() );
    println("getDescriptionURLEntities(): "+user.getDescriptionURLEntities());
    println("getFavouritesCount() : "+user.getFavouritesCount() );
  }// END OF USER INFOS -----------------
  //SEARCH TWEETS --------------
  public void searchTweets(String _search, int _numberOfResults){
    try {
        Query query = new Query(_search);
        query.count(_numberOfResults);
        QueryResult result = twitter.search(query);
        tweets = result.getTweets();
    } catch (TwitterException te) {
        println("Failed to search tweets: " + te.getMessage());
        exit();
    } 
  }
  public void displaySearchedTweets(int _numberOfResults){
    for (int i = 0; i<_numberOfResults; i++){
        status = tweets.get(i);
        println("Tweet numero "+i);
        println("Texte : "+status.getText());
        println("Date : "+status.getCreatedAt());
        println("Nombre de retweets : "+status.getRetweetCount());
        if(status.isRetweet()){
            println("Nom du retweeter : "+status.getRetweetedStatus().getUser().getScreenName());
        }
        println("envoyeur du tweet: "+status.getUser().getScreenName());
        if(status.getPlace() != null){
            println("pays : "+status.getPlace().getCountry());
            println("ville : "+status.getPlace().getName());
        }
        
    }
  }//END OF SEARCH TWEETS --------------
  // envoi un tweet \u00e0 un utilisateur
  public void mention(String _tweetMessage){
      try {
          Status status = twitter.updateStatus(_tweetMessage);
          println("Status updated to [" + status.getText() + "].");
      }catch (TwitterException te){
          System.out.println("Error: "+ te.getMessage()); 
      }
  }
  public String[] getAutoTweets(){
    String[] autoTweet = {"",""};
    autoTweetFile = loadJSONArray("autoTweets.json");
    int rand = PApplet.parseInt(random(1, autoTweetFile.size()));

    for (int i = 0; i < autoTweetFile.size(); i++) {
      if(i == rand){
        processing.data.JSONObject tweet = autoTweetFile.getJSONObject(i); 
        autoTweet[0] = tweet.getString("userName");
        autoTweet[1] = tweet.getString("message");
        println(autoTweet[0] + ", " + autoTweet[1]);
       }
    }
    return autoTweet;
  }
}

///--------------------------------------------------------------------------
///--------------------------------------------------------------------------
///--------------------------------------------------------------------------
public class TweetObject {

  public String userName; 
  public String message; 
  public String id; 
  public String imageUrl; 

  public TweetObject (String _userName, String _message,String _id, String _imageUrl) {
    userName = _userName;
    message = _message;
    id = _id;
    imageUrl = _imageUrl;
  }

  public void display(){
    println(this.userName);
    println(this.message);
    println(this.id);
    println(this.imageUrl);
  }

  // getters
  public String getUsername() {
    return userName;
  }

   public String getMessage() {
    return message;
  }

   public String getId() {
    return id;
  }

   public String getImageUrl() {
    return imageUrl;
  }
}
///--------------------------------------------------------------------------
///--------------------------------------------------------------------------
///--------------------------------------------------------------------------
public class ConfigBird {
  
  processing.data.JSONObject allConfigs;
  
  public ConfigBird (String _config) {
    allConfigs = loadJSONObject(_config);
  }

  public String[] jsonArrayToStringArray(String _jsonArray) {
    processing.data.JSONArray tempJsonArray = allConfigs.getJSONArray(_jsonArray);

    String [] citations = new String[tempJsonArray.size()];
    for (int i = 0; i < tempJsonArray.size(); i++){
      citations[i] = tempJsonArray.getString(i);
    }
    return citations;
  }

  public String getString(String _value) {
    return allConfigs.getString(_value);
  }

  public boolean getBool(String _value) {
    return allConfigs.getBoolean(_value);
  }

  public int getInt(String _value) {
    return allConfigs.getInt(_value);
  }

  public String metersToKilometers(float _value){
    String valueString;

    if(_value > 1000){
      valueString = _value/1000+" km";
    }else{
      valueString = _value+" m";
    }
    return valueString;
  }

  public String makeCustomMessage(float _value){
    String[] messages = jsonArrayToStringArray("citations");
    String stringToReturn = "";
    int counter = 500;
    int palier = getInt("palier");
    int taille = messages.length;

    for (int i = 0; i<taille; i++){

      if(i == taille -1){
        stringToReturn = messages[taille -1];
      }else if(_value < counter && _value >counter - palier){
        stringToReturn =  messages[i];
      }
      counter += palier;
    }
    return stringToReturn;
  }
}
///--------------------------------------------------------------------------
///--------------------------------------------------------------------------
///--------------------------------------------------------------------------
public class Filtre{ 
  String fichierMots;
  String lines[];
  int csvWidth;
  String [] arrayMots;
  String [] message;
  boolean filterOK;

  Filtre (String fichierMots_)
  {
    this.arrayMots = new String [0];
    this.fichierMots = fichierMots_;
    this.filterOK = true;
    this.arrayMots = loadStrings(fichierMots);
  }

  public void filterBadWords(String [] message_)
  {
    this.message = message_;
    this.filterOK = true;
     
    //Check si chaque mot n'est pas dans nos mots \u00e0 filtrer
    for (int i=0; i < message.length; i++)
    {
      for (int j=0; j < this.arrayMots.length; j++)
      {
        if (this.arrayMots[j].toLowerCase().equals(message[i].toLowerCase()) == true)
        {
          filterOK = false;
          //println("Message filtered : bad word found = "+arrayMots[j]);
        }
      }
    } 
  } 
}
///--------------------------------------------------------------------------
///--------------------------------------------------------------------------
///--------------------------------------------------------------------------
public class TwitterListener implements StatusListener{
  // onStatus : nouveau message qui vient d'arriver 
  
  String messageTweet, userName, userId, imageUrl; //informations qui seront filtr\u00e9s par la fonction modelTwitter
  processing.data.JSONArray tweets ;
  int counter;
  boolean isFilterOn ;
  String[]wordsOfTheTweet; // Array utiliser dans le filtrage pour stocker tous les mots d'un tweet
  Filtre filtre;

  public TwitterListener(boolean _isFilterOn,String _filterName ,int _time){
    filtre = new Filtre(_filterName);
    isFilterOn = _isFilterOn;
    tweets =  new processing.data.JSONArray();
    counter = 0;
  }

  public void onStatus(Status status) {
        //get the informations we want to return 
    userName = status.getUser().getScreenName();
    userId = Long.toString(status.getUser().getId());
    messageTweet = status.getText();
    imageUrl = status.getUser().getProfileImageURL();
  
    if(isFilterOn){
      filerTweet();
    }else{
      makeJson(userId,userName,messageTweet,imageUrl);
    }
  }  

  public void makeJson(String _id, String _user, String _message, String _imageUrl){
    processing.data.JSONObject tweetInfos =  new processing.data.JSONObject();
    processing.data.JSONObject tweetId =  new processing.data.JSONObject();

    tweetList.add(new TweetObject(_user,_message,_id,_imageUrl));
    addFlower(incPopulate);
    incPopulate++;
  }

  public void addFlower(int _inc){
    // PVector newflowerPosition = flowerPositionsT[randomOrderT[_inc]];
    // int r = (int) random(100,150);
    // flowerpot.add(new Marguerite(r,int(newflowerPosition.x),int(newflowerPosition.y),0,0));
    // if(flowerpot.size() < 60){
    //   incPopulate++;  
    // }else{
    //   incPopulate = 0;
    // }

  }

  public void filerTweet(){
    if(messageTweet!=null){
        wordsOfTheTweet = messageTweet.split(" ");
        filtre.filterBadWords(wordsOfTheTweet);
    if (filtre.filterOK){
         makeJson(userId,userName,messageTweet, imageUrl);
    }else{
        println("not added to the Json");
      }
    }
  }

  // onDeletionNotice
  public void onDeletionNotice(StatusDeletionNotice statusDeletionNotice) 
  {
  }

  // onTrackLimitationNotice
  public void onTrackLimitationNotice(int numberOfLimitedStatuses) {
  }  

  // onScrubGeo : r\u00e9cup\u00e9ration d'infos g\u00e9ographiques
  public void onScrubGeo(long userId, long upToStatusId) 
  {
    System.out.println("Got scrub_geo event userId:" + userId + " upToStatusId:" + upToStatusId);
  }

  public void onStallWarning(StallWarning warning){

  }
  // onException : une erreur est survenue (d\u00e9connexion d'internet, etc...)
  public void onException(Exception ex) 
  {
    ex.printStackTrace();
  }
}
///--------------------------------------------------------------------------
///--------------------------------------------------------------------------
///--------------------------------------------------------------------------

class Petale  {

  PVector position = new PVector(0,0,0);
  float rayon = 0; 
  float angle = 0; 
  int deviation = 5;
  int rayonAugmentation = 15;
  int amount = 10;
  PVector[] pointPosition ;

  float angleVariation = radians(70);
  float angleStart = 0;
  float angleEnd = 0;
  float rotation = 0;
  int c = color(0,0,0);
  int[] colorArray = new int[6];
  int petaleHeight ;
  int pointsPerEdge ;
  float xInc = 0;

  boolean isStripe = false;
  boolean dark = false;
  boolean isAnimationDone = false;
  PGraphics imagePetale = createGraphics(100, 100);
  PGraphics imagePetalePoint = createGraphics(100, 100);



  public Petale (PVector _position,  int _amount, int[] _colorArray ) {
      position = _position;
      amount = _amount;
      int[] colorArray = _colorArray;
      pointPosition = new PVector[1000];
  }

  public void display(){

  }

  public void update(){
  
  }

  public void assemblePetale(){
   
  }

  public void drawPetale(){
    
  }

	//helpers
	public void incRotation(int _incRotation){
		rotation += radians(_incRotation);
	}

	public void incAmount(int _incAmount){
		amount += _incAmount;
	}

  public float[] noiseArrayGenerator(float _min, float _max, int _arraySize, boolean _isMoving){
    //Noise
    if(!_isMoving){
      xInc = 0;
    }
    float[] noiseArray = new float[_arraySize];
    
    float newRayon = 0.0f;

    for (int i = 0; i < _arraySize; i++) {
      float noise = noise(xInc);
      noiseArray[i] = map(noise, 0, 1, _min, _max);
      xInc += 1;
    }

    return noiseArray;

  }
  //setters
  public void setAmount( int _newAmount){
  	amount = _newAmount;
  }

  public void setRotation (int _newRotation){
    rotation = _newRotation;
  }

  public void setColor (int _c){
    c = _c;
  }

  public void setPetaleHeight(int _petaleHeight){
    petaleHeight = _petaleHeight;
  }

  public void setPointsPerEdge(int _pointsPerEdge){
    pointsPerEdge = _pointsPerEdge;
  }

  public void setStripe(boolean _isStripe){
    isStripe = _isStripe;
  }

  public void setDark(boolean _isDark){
    dark = _isDark;
  }

  public PGraphics makePetaleImage(int _i){
  PGraphics imagePetale = createGraphics(100,100);
  return imagePetale ;
  //exit();
}

public PGraphics makePetaleImagePoint(int _i){
  PGraphics imagePetale = createGraphics(100,100);
  return imagePetale ;
}

public void openingAnimation(){


}

public boolean getAnimationState(){
  return  isAnimationDone;
}





}
class PetaleCoquelicot extends Petale {



PetaleCoquelicot (PVector _position,  int _amount,int[] _colorArray) {
  super( _position, _amount, _colorArray);
}

public void assemblePetale(){    
  lineRight();
  lineLeft();
  lineTop();
}

 public void display(){
    pushMatrix();
    pushStyle();
    
    translate(position.x, position.y);
    rotate(radians(rotation));
      assemblePetale();
     if(!isStripe){
        stroke(c);
        fill(c,200);
      }
      beginShape();
        drawPetale();
      endShape(CLOSE);
    popStyle();  
    popMatrix();

    rayon = 0;
  }

public void drawPetale(){
   for (int i = 0; i < (amount*3); ++i) {
        if(isStripe){
          stroke(c);
          fill(c,200);
        }
        
         curveVertex(pointPosition[i].x, pointPosition[i].y);
      }
}

public void lineRight(){
  angle = 0;
  for (int i = 0; i <= amount; ++i) {
    float x = rayon * cos(angle);
    float y = rayon * sin(angle);
    
    int index = i;
    pointPosition[index] = new PVector(x,y);

    rayon +=rayonAugmentation;
      angle = angle + radians(deviation); 
  
    if(i == amount-1){
      angleStart = angle;
    }
  }
 }

public void lineLeft(){
  rayon = 10;
  angle = angleVariation;

  for (int i  = 0; i < amount; ++i) {
    rayon += rayonAugmentation;

    float x = rayon * cos(angle);
    float y = rayon * sin(angle);

    int index = (amount*3)-i;
    pointPosition[index-1] = new PVector(x,y);
    angle = angle + radians(deviation*1.5f); 

    if(i == amount-1){
      angleEnd = angle;
    }
  }
}

public void lineTop(){
    int rayonDiviation = 10;
    float rayonMin = rayon ; 
    float rayonMax = rayon + rayonDiviation;
    float[] noiseArray = noiseArrayGenerator(rayonMin,rayonMax,amount, false);
    
    float angleDifference = angleEnd - angleStart;
    float angleStep = angleDifference /amount;
    angle = angleStart;

    for (int i  = 0; i < amount; i++) {

      

    float x = noiseArray[i] * cos(angle);
    float y = noiseArray[i] * sin(angle);

    int index = amount+i;
    pointPosition[index] = new PVector(x,y);
    angle += angleStep;

    }
}



}
class PetaleMarguerite extends Petale {

  int currentInc = 0;
  int deviation = 10;
  float angleVariation = radians(10);
  PVector tipPetale = new PVector(0,0);
  PVector basePetale = new PVector(0,0);
  int petaleHeight = 180;
  int pointsPerEdge = 20;
  int petaleWidth = 66; 

  ArrayList<PVector> newPointPosition = new ArrayList<PVector>();
  ArrayList<PVector> noiseAngle = new ArrayList<PVector>();

  int angleMax = 45;
  ArrayList<Float> newAngleSuite = new ArrayList<Float>();
  int[] colorArray = new int[6];

  //animation
  int incAnimation1 = 0;
  int incAnimation2 = 0;
  boolean isAnimationDone = false;


  //image
  
  int inc =0;
  boolean isImageMade = false;



//float[] angleSuite = new float[pointsPerEdge];

PetaleMarguerite (PVector _position,  int _amount,int[] _colorArray) {
  super( _position, _amount, _colorArray);
  colorArray = _colorArray;
  amount = 10;
  isStripe = true;
  generateCurve();
  assemblePetale();

}

public void assemblePetale(){  
  addPointsToArray();
}

public void computeHeight(){
  PVector distWidthA = new PVector(0,0,0);
  PVector distWidthB = new PVector(0,0,0);
  int incComputeHeight = 0; 


   for (PVector p : newPointPosition) {  
      if(incComputeHeight == (pointsPerEdge/2) ){
        distWidthA = p;
        //println("distWidthA.x: "+distWidthA.x);
        //println("distWidthA.y: "+distWidthA.y);
      }

      if(incComputeHeight == (pointsPerEdge+(pointsPerEdge/2)) ){
        distWidthB = p;
        //println("distWidthB.x: "+distWidthB.x);
        //println("distWidthB.y: "+distWidthB.y);
      }
      //println("lol");
      incComputeHeight++;
    } 

  petaleWidth = PApplet.parseInt(dist(distWidthA.x, distWidthA.y, distWidthB.x, distWidthB.y));
  petaleWidth +=10;

// dist(distWidthA.x, distWidthA.y, distWidthB.x, distWidthB.y)
//     petaleWidth = int();
}

public PGraphics makePetaleImage(int _i){
  computeHeight();
  PGraphics imagePetale = createGraphics(petaleHeight, petaleWidth);
  imagePetale.beginDraw();

  imagePetale.pushMatrix();
  imagePetale.translate(0, petaleWidth/2);
    //imagePetale.background(0);
    imagePetale.stroke(c);
    imagePetale.fill(c,200);
     
    imagePetale.beginShape();
    
    int inc = 0; 
    for (PVector p : newPointPosition) {  
      imagePetale.vertex(p.x, p.y);
      inc ++;
    } 

    imagePetale.endShape(CLOSE);
  imagePetale.popMatrix();

  imagePetale.endDraw();
  return imagePetale ;
  //exit();
}

public PGraphics makePetaleImagePoint(int _i){
  computeHeight();
  PGraphics imagePetalePoint = createGraphics(petaleHeight, petaleWidth);
  imagePetalePoint.beginDraw();

  imagePetalePoint.pushMatrix();
  imagePetalePoint.translate(0, petaleWidth/2);
    //imagePetalePoint.background(0);
    imagePetalePoint.noStroke();
    imagePetalePoint.fill(255,255,255,100);
     
    //imagePetalePoint.beginShape();
    
    int inc = 0; 
    for (PVector p : newPointPosition) {  
      imagePetalePoint.ellipse(p.x, p.y,2,2);
      inc ++;
    } 

    //imagePetalePoint.endShape(CLOSE);
  imagePetalePoint.popMatrix();

  imagePetalePoint.endDraw();
  //imagePetalePoint.save("images/lol"+_i+".png");

  return imagePetalePoint ;
  //exit();
}

// void display(){
//  // makePetaleImage();  
//   if(isAnimationDone){
    
//     pushMatrix();
//     pushStyle();
//     translate(position.x, position.y);
//     rotate(radians(rotation));
//     if(!dark){
//       if(!isStripe){
//         stroke(c);
//         fill(c,200);
//       }
//     }else{
//       noFill();
//       noStroke();
//     }
   
//     beginShape();
//     if(!dark){
//       drawPetale();
//     }else{
//       drawPetalePoint();
//     }
//     endShape(CLOSE);
//     popStyle();  
//     popMatrix();

//   }else{
//     pushMatrix();
//     pushStyle();
//       translate(position.x, position.y);
//       rotate(radians(rotation));
//       beginShape();
//         openingAnimation();
//       endShape(CLOSE);
//     popStyle();  
//     popMatrix();
//   }

//   rayon = 0;
// }

public void drawPetale(){
  int i = 0; 
 for (PVector p : newPointPosition) {
      if(i == 2){
        i = 0;
      }
      if(isStripe){
        stroke(colorArray[i]);
        fill(colorArray[i],200);
      }
      curveVertex(p.x, p.y);
     i++;
  }
}

public void drawPetalePoint(){
 for (PVector p : newPointPosition) {
      fill(255);
      ellipse(p.x, p.y, 2, 2);
  }
}

public void openingAnimation(){
  pushMatrix();
  pushStyle();
  translate(position.x, position.y);
      rotate(radians(rotation));
      beginShape();

  if(!isAnimationDone){
    if(incAnimation1 < pointsPerEdge){
      for (int i = 0; i < incAnimation1; ++i) {
        int x = PApplet.parseInt(newPointPosition.get(i).x);
        int y = PApplet.parseInt(newPointPosition.get(i).y);
        fill(0);
        ellipse(x, y, 2, 2);
       // println("incAnimation1: "+incAnimation1);
      }
    }

    if(incAnimation2 < pointsPerEdge){
      for (int i = 0; i < incAnimation2; ++i) {
        int x = PApplet.parseInt(newPointPosition.get(pointsPerEdge+i).x);
        int y = PApplet.parseInt(newPointPosition.get(pointsPerEdge+i).y);
        fill(0);
        ellipse(x, y, 2, 2);
       // println("incAnimation1: "+incAnimation1);
      }
    }
  }else{
   // println("done motherfucker: ");
  }

  if((incAnimation1+incAnimation2) == newPointPosition.size()){
      isAnimationDone = true; 
      //println("isAnimationDone: "+isAnimationDone);
    }else{
        incAnimation1 ++;
        incAnimation2 ++;
    }

      endShape(CLOSE);
    popStyle();  
    popMatrix();
}

public void addPointsToArray(){
 // float[] noiseArray = noiseArrayGenerator(3,10,pointsPerEdge,false);
  basePetale.x = 0 * cos(radians(0));
  basePetale.y = 0 * sin(radians(0));

  int rayonStep = petaleHeight/pointsPerEdge;

  tipPetale.x = petaleHeight * cos(radians(0));
  tipPetale.y = petaleHeight * sin(radians(0));

  //adding points in the array 
  for (int i = 0; i < pointsPerEdge; ++i) {
    float x = (rayonStep*i) * cos(newAngleSuite.get(i));
    float y = (rayonStep*i) * sin(newAngleSuite.get(i));
    //ellipse(x, y, 2, 2);
    newPointPosition.add(new PVector(x,y));
  }

  newPointPosition.add(new PVector(tipPetale.x, tipPetale.y));
    

  for (int i = 0; i < pointsPerEdge; ++i) {
    float x = (rayonStep*i) * cos(-newAngleSuite.get(i));
    float y = (rayonStep*i) * sin(-newAngleSuite.get(i));
    //ellipse(x, y, 2, 2);
    newPointPosition.add(new PVector(x,y));
  }  

  newPointPosition.add(new PVector(tipPetale.x, tipPetale.y));
}

public void generateCurve(){
  int angleStep = angleMax/pointsPerEdge;


  for (int i = 0; i < pointsPerEdge/2; ++i) {
      newAngleSuite.add(radians(angleStep*i));  
      currentInc++;
  }

  for (int i = pointsPerEdge/2; i >= 0; --i) {
      newAngleSuite.add(radians(angleStep*i));  
      currentInc++;
    //println("angleSuite: "+angleSuite[i]);
  }

  currentInc = 0;
}

public void setPetaleHeight(int _petaleHeight){
  petaleHeight = _petaleHeight;
  newPointPosition.clear();
  generateCurve();
  addPointsToArray();
}

public void setPointsPerEdge(int _pointsPerEdge){
  pointsPerEdge = _pointsPerEdge;
}

public void setStripe(boolean _isStripe){
  isStripe = _isStripe;
}

public void setDark(boolean _isDark){
  dark = _isDark;
}

public boolean getAnimationState(){
  return  isAnimationDone;
}


}
  static public void main(String[] passedArgs) {
    String[] appletArgs = new String[] { "--full-screen", "--bgcolor=#666666", "--hide-stop", "flowerWall" };
    if (passedArgs != null) {
      PApplet.main(concat(appletArgs, passedArgs));
    } else {
      PApplet.main(appletArgs);
    }
  }
}
