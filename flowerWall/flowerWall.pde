import java.util.Random;

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
boolean debug = true;
PImage roof, wiw, leaf;


void setup() {
  w = 1300; 
  ratio = 16/9;
  h = 800;
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

void draw() {
  background(0);

  if(isHandDetected){
     background(0);
      displayWiwLogo();
      for (Flower flower : flowerpot) {
          if(flower.isOver(int(leapPosition.x),int(leapPosition.y))){
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
     frame.setTitle("FPS "+int(frameRate+" position : "+cursorLocation));
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
          String message = "Une fleur de plus dans le jardin BBDO grâce à @"+tweet.getUsername();
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

void displayRoofGardenLogo(){
  pushMatrix();
  pushStyle();
  imageMode(CENTER);
  translate(w/2, h/2);
  image(roof, 0, 0);
  popStyle();
  popMatrix();
}

void displayWiwLogo(){
  pushMatrix();
  pushStyle();
  imageMode(CENTER);
  translate(w/2, h/2);
  image(wiw, 0, 0);
  popStyle();
  popMatrix();
}

void makeGrid(){
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

void detectPointerLocation(){
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

void showCursor(){
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

void shuffleArray(int a, int b)
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

void addFlower(String _author, String _tweet) {

  PVector newflowerPosition = flowerPositions[randomOrder[incPopulate]];
  int sizeFlower = (int) map(_tweet.length(), 0, 144, 100, 200);
  println("flowerpot.size(): "+flowerpot.size());


  flowerpot.add(new Marguerite(sizeFlower,int(newflowerPosition.x),int(newflowerPosition.y),w,h, _author, _tweet));
  
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

void initImages(){
  roof = loadImage("roofgarden.png");
  roof.resize(int(h*0.90), int(h*0.90));
  wiw = loadImage("logoWiW.png");
  leaf = loadImage("roofgarden.png");
}

void setUpTwitter(){
  configBird = new ConfigBird("configBird.json");
  initConfig();
  modelBird = new ModelTwitter(twitterKey,flowerPositions,randomOrder);
  modelBird.listenToHashtag(hashTags);
}

void initConfig() {
  hashTags = configBird.jsonArrayToStringArray("hashtags");
  users = configBird.jsonArrayToStringArray("users");
  twitterKey = configBird.getInt("twitterKey");
}

void mouseClicked(){
  if(debug){
    addFlower("fabax","Follow the story of the @InfiniteBridge, recently premiered at the RCM to great acclaim: http://bit.ly/1Jcjf0l  #GreatExhibitionists #NPAPW");  
      
  }


  
}



