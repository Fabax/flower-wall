class Coquelicot extends Flower {

	int startingAngle;
	int angleInc;
	int petalesPerLayer	;
	int currentInc = 0;

	Coquelicot (int _size, int _x, int _y, int _sketchWidth, int _sketchHeight, String _author, String _tweet) {
		super (_size, _x, _y, _sketchWidth, _sketchHeight, _author,_tweet);
	}

	void assemblePetales(){
		layer0();
		layer1();
	}

	void layer0(){
		startingAngle = 0; 
		angleInc = 90; 
		petalesPerLayer = 360/angleInc;
		
		for (int i = 0; i < petalesPerLayer; ++i) {
			int newRotation = angleInc*i;
			color newColor = getPetaleColor(i);
			petales.add(new PetaleCoquelicot(new PVector(0,0), 7,couleur));
			petales.get(currentInc).setRotation(newRotation);
			petales.get(currentInc).setColor(newColor);

			currentInc ++;
		}
	}

	void layer1(){
	startingAngle = 45; 
		angleInc = 45; 
		petalesPerLayer = 360/angleInc;

		for (int i = 0; i < petalesPerLayer; ++i) {
			int newRotation = angleInc*i;
			color newColor = getPetaleColor(i);
			petales.add(new PetaleCoquelicot(new PVector(0,0), 6,couleur));
			petales.get(currentInc).setRotation(newRotation);
			petales.get(currentInc).setColor(newColor);

			currentInc ++;
		}
	}

	void initColors(){
	    couleur[0] = color(127, 0, 0);
	    couleur[1] = color(255, 76, 76);
	    couleur[2] = color(255, 0, 0);
	    couleur[3] = color(127, 38, 38);
	    couleur[4] = color(204, 0, 0);
	}

}
