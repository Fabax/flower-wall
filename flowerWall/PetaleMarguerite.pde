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
  color[] colorArray = new color[6];

  //animation
  int incAnimation1 = 0;
  int incAnimation2 = 0;
  boolean isAnimationDone = false;


  //image
  
  int inc =0;
  boolean isImageMade = false;



//float[] angleSuite = new float[pointsPerEdge];

PetaleMarguerite (PVector _position,  int _amount,color[] _colorArray) {
  super( _position, _amount, _colorArray);
  colorArray = _colorArray;
  amount = 10;
  isStripe = true;
  generateCurve();
  assemblePetale();

}

void assemblePetale(){  
  addPointsToArray();
}

void computeHeight(){
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

  petaleWidth = int(dist(distWidthA.x, distWidthA.y, distWidthB.x, distWidthB.y));
  petaleWidth +=10;

// dist(distWidthA.x, distWidthA.y, distWidthB.x, distWidthB.y)
//     petaleWidth = int();
}

PGraphics makePetaleImage(int _i){
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

PGraphics makePetaleImagePoint(int _i){
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

void drawPetale(){
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

void drawPetalePoint(){
 for (PVector p : newPointPosition) {
      fill(255);
      ellipse(p.x, p.y, 2, 2);
  }
}

void openingAnimation(){
  pushMatrix();
  pushStyle();
  translate(position.x, position.y);
      rotate(radians(rotation));
      beginShape();

  if(!isAnimationDone){
    if(incAnimation1 < pointsPerEdge){
      for (int i = 0; i < incAnimation1; ++i) {
        int x = int(newPointPosition.get(i).x);
        int y = int(newPointPosition.get(i).y);
        fill(0);
        ellipse(x, y, 2, 2);
       // println("incAnimation1: "+incAnimation1);
      }
    }

    if(incAnimation2 < pointsPerEdge){
      for (int i = 0; i < incAnimation2; ++i) {
        int x = int(newPointPosition.get(pointsPerEdge+i).x);
        int y = int(newPointPosition.get(pointsPerEdge+i).y);
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

void addPointsToArray(){
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

void generateCurve(){
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

void setPetaleHeight(int _petaleHeight){
  petaleHeight = _petaleHeight;
  newPointPosition.clear();
  generateCurve();
  addPointsToArray();
}

void setPointsPerEdge(int _pointsPerEdge){
  pointsPerEdge = _pointsPerEdge;
}

void setStripe(boolean _isStripe){
  isStripe = _isStripe;
}

void setDark(boolean _isDark){
  dark = _isDark;
}

boolean getAnimationState(){
  return  isAnimationDone;
}


}