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

	void assemblePetales(){
		layer0();
		layer1();
		layer2();
	}

	void displayFullFlower(){
		imageX = int(position.x - (sizeFlower));
		imageH = int(position.y - (sizeFlower));

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

	void displayPointFlower(){
		imageX = int(position.x - (sizeFlower));
		imageH = int(position.y - (sizeFlower));

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

	void makeFlowerImage(){
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

		String randomName = "flowerToSend/flower"+int(millis())+".png";
		setImageName(randomName);
		imageFlower.save(randomName);
		isFlowerCreated = true;
		incImage ++;
	}

	void makeFlowerImagePoint(){
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

	void layer0(){
		startingAngle = 0; 
		angleInc = 45; 
		petalesPerLayer = 360/angleInc;
		for (int i = 0; i < petalesPerLayer; ++i) {

			float r = random(0.8, 0.9);
			newHeight = int(sizeFlower*r);
			//println("newHeight: "+newHeight);

			int newRotation = angleInc*i;
			color newColor = getPetaleColor(i);
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

	void setImageName(String _imageName){
		imageName = _imageName; 
		println("imageName: "+imageName);
	}

	String getImageName(){
		return imageName ;
	}

	void layer1(){
		startingAngle = 45/2; 
		angleInc = 45; 
		petalesPerLayer = 360/angleInc;
		

		for (int i = 0; i < petalesPerLayer; ++i) {
			float r = random(0.7, 0.8);
			newHeight = int(sizeFlower*r);
			int newRotation = angleInc*i;
			newRotation +=startingAngle;
			color newColor = getPetaleColor(i);
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

	void layer2(){
		startingAngle = 45/2; 
		angleInc = 45; 
		petalesPerLayer = 360/angleInc;
		

		for (int i = 0; i < petalesPerLayer; ++i) {
			float r = random(0.5, 0.7);
			newHeight = int(sizeFlower*r);

			int newRotation = angleInc*i;
			newRotation +=startingAngle;
			color newColor = getPetaleColor(i);
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

	void setSizeFlower(int _sizeFlower){
		sizeFlower = _sizeFlower;
		isDrawing = true;

	}

	PGraphics getImage(){
		return imageFlower;
	}



}
