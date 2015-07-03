 import de.voidplus.leapmotion.*;

 class Cursor {

  PVector cursorPosition = new PVector(0,0,0);
  LeapMotion leap;
  Boolean detectHand = false;
  int inc = 0;

  Cursor (PApplet context) {
    leap = new LeapMotion(context);
  }

  void update(){
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

  Boolean handStatus(){
    return detectHand;

  }

  PVector getPosition(){
    return cursorPosition;
  }

  void setPosition(PVector _cursorPosition){
    cursorPosition = _cursorPosition;
  }

}
