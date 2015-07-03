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
  color c = color(0,0,0);
  color[] colorArray = new color[6];
  int petaleHeight ;
  int pointsPerEdge ;
  float xInc = 0;

  boolean isStripe = false;
  boolean dark = false;
  boolean isAnimationDone = false;
  PGraphics imagePetale = createGraphics(100, 100);
  PGraphics imagePetalePoint = createGraphics(100, 100);



  public Petale (PVector _position,  int _amount, color[] _colorArray ) {
      position = _position;
      amount = _amount;
      color[] colorArray = _colorArray;
      pointPosition = new PVector[1000];
  }

  void display(){

  }

  void update(){
  
  }

  void assemblePetale(){
   
  }

  void drawPetale(){
    
  }

	//helpers
	void incRotation(int _incRotation){
		rotation += radians(_incRotation);
	}

	void incAmount(int _incAmount){
		amount += _incAmount;
	}

  float[] noiseArrayGenerator(float _min, float _max, int _arraySize, boolean _isMoving){
    //Noise
    if(!_isMoving){
      xInc = 0;
    }
    float[] noiseArray = new float[_arraySize];
    
    float newRayon = 0.0;

    for (int i = 0; i < _arraySize; i++) {
      float noise = noise(xInc);
      noiseArray[i] = map(noise, 0, 1, _min, _max);
      xInc += 1;
    }

    return noiseArray;

  }
  //setters
  void setAmount( int _newAmount){
  	amount = _newAmount;
  }

  void setRotation (int _newRotation){
    rotation = _newRotation;
  }

  void setColor (color _c){
    c = _c;
  }

  void setPetaleHeight(int _petaleHeight){
    petaleHeight = _petaleHeight;
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

  PGraphics makePetaleImage(int _i){
  PGraphics imagePetale = createGraphics(100,100);
  return imagePetale ;
  //exit();
}

PGraphics makePetaleImagePoint(int _i){
  PGraphics imagePetale = createGraphics(100,100);
  return imagePetale ;
}

void openingAnimation(){


}

boolean getAnimationState(){
  return  isAnimationDone;
}





}
