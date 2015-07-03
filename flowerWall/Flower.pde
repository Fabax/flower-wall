class Flower  {
	PVector position = new PVector(0,0); 
	ArrayList<Petale> petales = new ArrayList<Petale>();
	int rotationInc = 3; 
	float rotation = 0;
	int rotationSpeed = 1;
	int[] colorSuite = new int[100];
	color[] couleur = new color[5];
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

	void assemblePetales(){
		
	}

	void setImageName(String _name){
		imageName = _name;
	}

	String getImageName(){
		return imageName;
	}

	void rotateFlower(){
		rotate(radians(rotation));
		rotation += radians(1);
	}

	void displayFullFlower(){
	}

	void displayPointFlower(){

	}

	void displayTweet(){
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

	void update(){
		
	}

	void setPosition(PVector _position){
		position = _position;
	}

	void setRotation(int _rotation , int _rotationSpeed){
		rotationSpeed = _rotationSpeed;
		rotation = _rotation;
	}

	void initColors(){
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

	void initColorSuite(){
		for (int i = 0; i < 100; i++) {
	    	int rand = int(random(0, 5));
	    	colorSuite[i] = rand;
	    }
	}

	color getPetaleColor(int _i){
		return couleur[colorSuite[_i]];
	}

	color[] getAllColors(){
		return couleur; 
	}

	PVector getPosition(){
		return position;
	}

	int getSizeFlower(){
		return sizeFlower;
	}

	void setDark(boolean _isDark){
		isDark = _isDark;
	}

	void setSizeFlower(int _sizeFlower){
		sizeFlower = _sizeFlower;
	}

	boolean isOver(int _mouseX, int _mouseY){
		float dist = dist(_mouseX, _mouseY, position.x, position.y);
		if(dist < sizeFlower/3){
			isOver = true; 
		}else{
			isOver = false;
		}

		return isOver;
		//return false;
	}

	void setAuthor(String _author){
		author = _author;
	}

	void setTweet(String _tweet){
		tweet = _tweet;
	}

	PGraphics getImage(){
		return imageFlower;
	}





}