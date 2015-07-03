class PetaleCoquelicot extends Petale {



PetaleCoquelicot (PVector _position,  int _amount,color[] _colorArray) {
  super( _position, _amount, _colorArray);
}

void assemblePetale(){    
  lineRight();
  lineLeft();
  lineTop();
}

 void display(){
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

void drawPetale(){
   for (int i = 0; i < (amount*3); ++i) {
        if(isStripe){
          stroke(c);
          fill(c,200);
        }
        
         curveVertex(pointPosition[i].x, pointPosition[i].y);
      }
}

void lineRight(){
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

void lineLeft(){
  rayon = 10;
  angle = angleVariation;

  for (int i  = 0; i < amount; ++i) {
    rayon += rayonAugmentation;

    float x = rayon * cos(angle);
    float y = rayon * sin(angle);

    int index = (amount*3)-i;
    pointPosition[index-1] = new PVector(x,y);
    angle = angle + radians(deviation*1.5); 

    if(i == amount-1){
      angleEnd = angle;
    }
  }
}

void lineTop(){
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
