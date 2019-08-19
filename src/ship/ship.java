package ship;

import java.util.Scanner;
import java.util.concurrent.ThreadLocalRandom;

public class ship {

	public static Scanner scn = new Scanner(System.in);
	
	
	private static void Filler(char[][] field) {
		int num1 = 48;
		int num2 = 65;
		for (int i = 1; i<field.length; i++){
			field[i][0] = (char) num1++;
			field[0][i] = (char) num2++;
		}
	}
	
	
	
	private static void FinalFiller(char[][] field) {
		for (int i = 1; i<field.length; i++)
			for (int j = 1; j<field.length; j++)
				if (field[i][j] == 0)
					field[i][j] = '-';
	}



	private static void ShipMapping(char[][] field, int shipLength){
		int one;
		int two;
		int shipNum = 5-shipLength;
		int ship = 1;
		System.out.println("Time to place " + shipNum + " ship(s) with a length of " + shipLength + ".");
		while(shipNum>=ship){
			Show(field);
			if (shipLength == 1){
				System.out.println("Where goes ship number " + ship + "?");
				String place = InputScan();
				one = OnePlace(field, place.charAt(1));
				two = TwoPlace(field, place.charAt(0));
				if (field[one][two] != 0){
					System.out.println("Can't put it here!\n");
					continue;
				}
				ShipPlacing(field, one, two, 'o');
			}
			else{
				System.out.println("Enter the starting point of the ship number " + ship + ".");
				String place1 = InputScan();
				one = OnePlace(field, place1.charAt(1));
				two = TwoPlace(field, place1.charAt(0));
				if (field[one][two] != 0) {
					System.out.println("Can't put it here! Space occupied.\n");
					continue;
				}
				System.out.println("And where will it move?\nUp, left, down or right?");
				String input = scn.next();
				input = input.toLowerCase();
				while(!input.equals("up") && !input.equals("left") && !input.equals("down") && !input.equals("right")){
					System.out.println("Again, dummy! Up, left, down or right?");
					input = scn.next();
					input = input.toLowerCase();
				}
				int side = SideReceive(input);
				if (!SideCheck(field, one, two, side, shipLength-1)){
					System.out.println("Can't go there! No free space.");
					continue;
				}
				Move (field, one, two, shipLength-1, side, 'o');	
			}
			ship++;
		}
	}


	private static boolean SideCheck(char[][] field, int one, int two, int side, int shipLength) {
		if (side==1){
			if (one-shipLength < 1 || field[one-shipLength][two] != 0)
				return false;
		}
		else if (side==2){
			if (two-shipLength < 1 || field[one][two-shipLength] != 0)
				return false;
		}
		else if (side == 3){
			if (one + shipLength > 10 || field[one+shipLength][two] != 0)
				return false;
		}
		else{
			if (two+shipLength > 10 || field[one][two+shipLength] != 0)
				return false;
		}
		
		return true;
	}



	private static int SideReceive(String input) {
		if (input.equals("up"))
			return 1;
		if (input.equals("left"))
			return 2;
		if (input.equals("down"))
			return 3;
		else
			return 4;
	}



	private static void ShipRandom(char[][] field, int shipLength) {
		int one;
		int two;
		int shipNum = 5-shipLength;
		int ship = 1;
		while(shipNum>=ship){
			if (shipLength==1){
				one = ThreadLocalRandom.current().nextInt(1, 10 + 1);
				two = ThreadLocalRandom.current().nextInt(1, 10 + 1);
				if (field[one][two] != 0)
					continue;
				ShipPlacing(field, one, two, 'o');
			}
			else{
				one = ThreadLocalRandom.current().nextInt(1, 10 + 1);
				two = ThreadLocalRandom.current().nextInt(1, 10 + 1);
				if (field[one][two] != 0)
					continue;
				if(!RandomLong(field, one, two, shipLength-1))
					continue;
			}
			ship++;
		}
	}



	private static boolean RandomLong(char[][] field, int one, int two, int length) {
		boolean up = true;
		boolean left = true;
		boolean down = true;
		boolean right = true;
		
		if (one - length < 1 || field[one-length][two] != 0)
			up = false;
		if (one + length > 10 || field[one+length][two] != 0)
			down = false;
		if (two - length < 1 || field[one][two-length] != 0)
			left = false;
		if (two + length > 10 || field[one][two+length] != 0)
			right = false;
		
		if (!up && !left && !down && !right)
			return false;
		
		boolean go = false;
		while(!go){
			int side = ThreadLocalRandom.current().nextInt(1, 4+1);
			if (side==1 && up){
				Move(field, one, two, length, side, 'o');
				go = true;
			}
			else if (side==2 && left){
				Move(field, one, two, length, side, 'o');
				go = true;
			}
			else if (side==3 && down){
				Move(field, one, two, length, side, 'o');
				go = true;
			}
			else if (side==4 && right){
				Move(field, one, two, length, side, 'o');
				go = true;
			}
		}
		
		return true;
	}



	private static void Move(char[][] field, int one, int two, int length, int side, char ch) {
		ShipPlacing(field, one, two, ch);
		if (side==1){
			for(int i = 0; i<length; i++){
				one--;
				ShipPlacing(field, one, two, ch);
			}
		}
		else if (side==2){
			for (int i = 0; i<length; i++){
				two--;
				ShipPlacing(field, one, two, ch);
			}
		}
		else if (side==3)
			for(int i = 0; i<length; i++){
				one++;
				ShipPlacing(field, one, two, ch);
			}
		else{
			for (int i = 0; i<length; i++){
				two++;
				ShipPlacing(field, one, two, ch);
			}
		}	
	}



	private static void ShipPlacing(char[][] field, int one, int two, char ch) {
		
		field[one][two] = ch;
		
		if (field[one-1][two] == 0)
			field[one-1][two] = '-';
		if (field[one][two-1] == 0)
			field[one][two-1] = '-';
		if (one!=10 && field[one+1][two] == 0)
			field[one+1][two] = '-';
		if (two!=10 && field[one][two+1] == 0)
			field[one][two+1] = '-';
		if (one!=10 && two!=10 && field[one+1][two+1] == 0)
			field[one+1][two+1] = '-';
		if (one!=10 && field[one+1][two-1] == 0)
			field[one+1][two-1] = '-';
		if (two!=10 && field[one-1][two+1] == 0)
			field[one-1][two+1] = '-';
		if (one!=1 && two!=1 && field[one-1][two-1] == 0)
			field[one-1][two-1] = '-';
	}



	private static void Fleet (char[][] field, int gate){
		int ship = 1;
		while(ship<=4){
			if (gate==1)
				ShipRandom(field, ship);
			else
				ShipMapping(field, ship);
			ship++;
		}
	}



	private static void Show(char[][] field) {
		System.out.println("\n");
		for (int i = 0; i<field.length; i++){
			for (int j = 0; j<field.length; j++){
				System.out.print(field[i][j] + " ");
			}
			System.out.println();
		}
		System.out.println("\n");
	}
	
	
private static int OnePlace(char[][] field, char coordinates) {
	for (int i = 0; i<field.length; i++)
		if (field[i][0] == coordinates)
			return i;
	return 0;
	}



	private static int TwoPlace(char[][] field, char coordinates) {
		for (int i = 0; i<field.length; i++)
			if (field[0][i] == coordinates)
				return i;
		return 0;
	}

	
	
	private static String InputScan() {
		String input = "borntobekilled";
		boolean pass = false;
		do{
			input = scn.next();
			if (input.length()!=2)
				System.out.println("Enter 2 inputs for the coordinates: the letter and the number.");
			else if ((input.charAt(0) < 65 || input.charAt(0) > 74) && (input.charAt(0) < 97 || input.charAt(0) > 106) || (input.charAt(1) < 48 || input.charAt(1) > 57))
				System.out.println("The first input must be the letter (A-J), and the second must be the number (0-9).");
			else
				pass = true;
		}while(!pass);
		if (input.charAt(0)>74){
			String inputf = (char)(input.charAt(0)-32) + input.substring(1);
			return inputf;
		}
		return input;
	}	
	  
	
	
	
	public static void main(String[] args) {
		final int size = 11;
		char field[][] = new char[size][size];
		char fieldshot[][] = new char[size][size];
		char fieldCPU[][] = new char[size][size];
		char fieldCPUshot[][] = new char[size][size];
		
		Filler(fieldCPU);
		Fleet(fieldCPU, 1);
		FinalFiller(fieldCPU);
		
		System.out.println("Enemies encoming, captain! Get your fleet ready!\n");
		
		Filler(field);
		Fleet(field, 2);
		FinalFiller(field);
		Show(field);
		
		Filler(fieldshot);
		Filler(fieldCPUshot);
		
		boolean turn = true;
		int unit = 20;
		int unitCPU = unit;
		while(unit!=0 && unitCPU!=0){
			if (turn)
				unitCPU = PlayerTurn(fieldCPU, fieldshot, unitCPU);
			else
				unit = CPUTurn(field, fieldCPUshot, unit);
			turn = !turn;
		}
		
		Wait();
		
		if (unit==0)
			System.out.println("Welp, cap, congratulations...\nYou suck!!!\nHope your next fleet will be more lucky then this one...");
		else
			System.out.println("Argh! We got em nicely, cap!\nCongratulations!!!\nO captain! My captain! Truly you rock!!!");
	}



	private static int PlayerTurn(char[][] fieldCPU, char[][] fieldshot, int unitCPU) {
		System.out.println("It's your turn, captain!");
		String input;
		boolean extra = true;
		while(extra && unitCPU != 0){
			extra = false;
			System.out.println("Do you wish to check the map, sir? Yes or no?");
			input = scn.next();
			input = input.toLowerCase();
			while(!input.equals("yes") && !input.equals("no") && !input.equals("evgenyrulez")){
				System.out.println("Eh? Again: yes or no?");
				input = scn.next();
				input = input.toLowerCase();
			}
			if (input.equals("yes"))
				Show(fieldshot);
			else if (input.equals("evgenyrulez")){
				Show(fieldCPU);
			}
			
			System.out.println("Where do we shoot?");
			input = InputScan();
			int one = OnePlace(fieldshot, input.charAt(1));
			int two = TwoPlace(fieldshot, input.charAt(0));
			
			Wait();
			
			if (fieldCPU[one][two] == 'o'){
				System.out.println("It's a hit, cap'n!");
				fieldCPU [one][two] = 'x';
				extra = true;
				unitCPU--;
				
				if (SingleCheck(fieldCPU, one, two)){
					System.out.println("You destroyed this little guy!");
					ShipPlacing(fieldshot, one, two, 'x');
				}
				
				
				else{
					System.out.println("You hit the big one!");
					
					boolean vertical = VerticalCheck(fieldCPU, one, two);
					
					if(DeadCheck(fieldCPU, fieldshot, one, two, vertical)){
						System.out.println("And you finish the bastard off!");
					}
					
					else{
						fieldshot[one][two] = 'x';
						System.out.println("But it's still alive.");
					}
					
				}
					
				
			}
			else if (fieldCPU[one][two] == 'x')
				System.out.println("It's dead, Jim.");
			
			else{
				System.out.println("D'oh! You missed, sir!");
					fieldshot[one][two] = '-';
			}
		}
		
		return unitCPU;
	}



	private static boolean VerticalCheck(char[][] fieldCPU, int one, int two) {
		
		if ((two==1 || fieldCPU[one][two-1] == '-') && (two==10 || fieldCPU[one][two+1] == '-'))
			return true;
		else
			return false;
	}



	private static boolean DeadCheck(char[][] fieldCPU, char[][] fieldshot, int one, int two, boolean vertical) {
		int length = 0;
		int oneUp = one;
		int twoLeft = two;
		if (vertical){
			for (int i = 1; one-i >= 1 && fieldCPU[one-i][two]!='-' ; i++){
				if (fieldCPU[one-i][two] == 'o')
					return false;
				length++;
				oneUp = one-i;
			}
			for (int i = 1; one+i <= 10 && fieldCPU[one+i][two]!='-' ; i++){
				if (fieldCPU[one+i][two] == 'o')
					return false;
				length++;
			}
			
			Move(fieldshot, oneUp, two, length, 3, 'x');
			
		}
		else{
			for (int i = 1; two-i >= 1 && fieldCPU[one][two-i]!='-' ; i++){
				if (fieldCPU[one][two-i] == 'o')
					return false;
				length++;
				twoLeft = two-i; 
			}
			for (int i = 1; two+i <= 10 && fieldCPU[one][two+i]!='-' ; i++){
				if (fieldCPU[one][two+i] == 'o')
					return false;
				length++;
			}
			
			Move(fieldshot, one, twoLeft, length, 4, 'x');
		}
		
		return true;
	}



	private static boolean SingleCheck(char[][] fieldCPU, int one, int two) {
		
		if (fieldCPU[one-1][two] == 'o' || fieldCPU[one][two-1] == 'o' || fieldCPU[one-1][two] == 'x' || fieldCPU[one][two-1] == 'x')
			return false;
		
		if ( (one != 10 && (fieldCPU[one+1][two] == 'o' || fieldCPU[one+1][two] == 'x'))  ||   (two != 10 && (fieldCPU[one][two+1] == 'o' || fieldCPU[one][two+1] == 'x')) )
			return false;
		
		return true;
	}



	private static int CPUTurn(char[][] field, char[][] fieldCPUshot, int unit) {
		
		System.out.println("Brace yourselves!!! Fire incoming!!!");
		boolean alert = AlertCheck(fieldCPUshot);
		boolean extra = true;
		int one, two;
		
		while(extra && unit!=0){
			extra = false;
			System.out.println("Sir! Do you wish to check the status of our fleet?");
			String input = scn.next();
			input = input.toLowerCase();
			while(!input.equals("yes") && !input.equals("no") && !input.equals("evgenyrulez")){
				System.out.println("Eh? Again: yes or no?");
				input = scn.next();
				input = input.toLowerCase();
			}
			if (input.equals("yes"))
				Show(field);
			else if (input.equals("evgenyrulez"))
				Show(fieldCPUshot);
			
			Wait();
			
			if (alert){
				int oneX = 0;
				int twoX = 0;
				for (int i = 1; i<fieldCPUshot.length; i++){
					for (int j = 1; j<fieldCPUshot.length; j++){
						if (fieldCPUshot[i][j] == 'X'){
							oneX = i;
							twoX = j;
							break;
						}
					}
				}
				int side = CrippleCheck(fieldCPUshot, oneX, twoX);
				
				 if (side!=0){
					 if (AimShot(fieldCPUshot, field, oneX, twoX, side)){
						 extra = true;
						 unit--;
						 System.out.println("Mayday!!! We got hit!");
						 boolean vertical = VerticalCheck(field, oneX, twoX);
						 if (DeadCheck(field, fieldCPUshot, oneX, twoX, vertical)){
							 System.out.println("And he sank our whole ship. Asshole!!!");
							 fieldCPUshot[oneX][twoX] = 'x';
							 alert = false;
						 }
					 }
					 else
						 System.out.println("Hah! That loser missed!");
				 }
				 else{
					 if (BlindShoot(fieldCPUshot, field, oneX, twoX)){
						 extra = true;
						 unit--;
						 System.out.println("Mayday!!! We got hit!");
						 boolean vertical = VerticalCheck(field, oneX, twoX);
						 if (DeadCheck(field, fieldCPUshot, oneX, twoX, vertical)){
							 System.out.println("And he sank our whole ship. Asshole!!!");
							 fieldCPUshot[oneX][twoX] = 'x';
							 alert = false;
						 }
					 }
					 else
						 System.out.println("Hah! That loser missed!");
				 }
				
			}
			
			else{
				
				do{
					one = ThreadLocalRandom.current().nextInt(1, 10 + 1);
					two = ThreadLocalRandom.current().nextInt(1, 10 + 1);
				}while (fieldCPUshot[one][two] != 0);
				
				if (field[one][two] == '-'){
					System.out.println("Hah! That loser missed!");
					fieldCPUshot[one][two] = '-';
				}
				
				else{
					extra = true;
					unit--;
					field[one][two] = 'x';
					System.out.println("Mayday! We got hit!");
					
					if (SingleCheck(field, one, two)){
						System.out.println("He got our little one!");
						ShipPlacing(fieldCPUshot, one, two, 'x');
					}
					else{
						System.out.println("The enemy is red hot on us!");
						alert = true;
						fieldCPUshot[one][two] = 'X';
					}
				}
				
			}
		}
		
		return unit;
	}



	private static boolean AimShot(char[][] fieldCPUshot, char[][] field, int oneX, int twoX, int side) {
		
		if (side==1){
			for (int i = 2; oneX-i >= 1 && fieldCPUshot[oneX-i][twoX]!='-'; i++){
				if (fieldCPUshot[oneX-i][twoX]==0){
					if (field[oneX-i][twoX]=='o'){
						field[oneX-i][twoX]='x';
						fieldCPUshot[oneX-i][twoX]='x';
						return true;
					}
					else{
						fieldCPUshot[oneX-i][twoX] = '-';
						return false;
					}
				}
			}
			for (int i = 1; field[oneX+i][twoX]!='-'; i++){
				if (fieldCPUshot[oneX+i][twoX]==0){
					if (field[oneX+i][twoX]=='o'){
						field[oneX+i][twoX]='x';
						fieldCPUshot[oneX+i][twoX]='x';
						return true;
					}
				}
			}
			
		}
		else if (side==2){
			for (int i = 2; twoX-i >= 1 && fieldCPUshot[oneX][twoX-i]!='-'; i++){
				if (fieldCPUshot[oneX][twoX-i]==0){
					if (field[oneX][twoX-i]=='o'){
						field[oneX][twoX-i]='x';
						fieldCPUshot[oneX][twoX-i]='x';
						return true;
					}
					else{
						fieldCPUshot[oneX][twoX-i] = '-';
						return false;
					}
				}
			}
			for (int i = 1; field[oneX][twoX+i]!='-'; i++){
				if (fieldCPUshot[oneX][twoX+i]==0){
					if (field[oneX][twoX+i]=='o'){
						field[oneX][twoX+i]='x';
						fieldCPUshot[oneX][twoX+i]='x';
						return true;
					}
				}
			}
		}
		
		else if (side==3){
			for (int i = 2; oneX+i <= 10 && fieldCPUshot[oneX+i][twoX]!='-'; i++){
				if (fieldCPUshot[oneX+i][twoX]==0){
					if (field[oneX+i][twoX]=='o'){
						field[oneX+i][twoX]='x';
						fieldCPUshot[oneX+i][twoX]='x';
						return true;
					}
					else{
						fieldCPUshot[oneX+i][twoX] = '-';
						return false;
					}
				}
			}
			for (int i = 1; field[oneX-i][twoX]!='-'; i++){
				if (fieldCPUshot[oneX-i][twoX]==0){
					if (field[oneX-i][twoX]=='o'){
						field[oneX-i][twoX]='x';
						fieldCPUshot[oneX-i][twoX]='x';
						return true;
					}
				}
			}
		}
		else{
			for (int i = 2; twoX+i <= 10 && fieldCPUshot[oneX][twoX+i]!='-'; i++){
				if (fieldCPUshot[oneX][twoX+i]==0){
					if (field[oneX][twoX+i]=='o'){
						field[oneX][twoX+i]='x';
						fieldCPUshot[oneX][twoX+i]='x';
						return true;
					}
					else{
						fieldCPUshot[oneX][twoX+i] = '-';
						return false;
					}
				}
			}
			for (int i = 1; field[oneX][twoX-i]!='-'; i++){
				if (fieldCPUshot[oneX][twoX-i]==0){
					if (field[oneX][twoX-i]=='o'){
						field[oneX][twoX-i]='x';
						fieldCPUshot[oneX][twoX-i]='x';
						return true;
					}
				}
			}
		}
		
		return false;
	}



	private static boolean BlindShoot(char[][] fieldCPUshot, char[][] field, int oneX, int twoX) {
		boolean go = false;
		do{
			int side = ThreadLocalRandom.current().nextInt(1, 4+1);
			if (side==1 && oneX!=1 && fieldCPUshot[oneX-1][twoX] != '-'){
				if (field[oneX-1][twoX] == 'o'){
					field[oneX-1][twoX] = 'x';
					fieldCPUshot[oneX-1][twoX] = 'x';
					return true;
				}
				else{
					fieldCPUshot[oneX-1][twoX] = '-';
					return false;
				}
				
			}
			else if (side==2 && twoX!=1 && fieldCPUshot[oneX][twoX-1] != '-'){
				if (field[oneX][twoX-1] == 'o'){
					field[oneX][twoX-1] = 'x';
					fieldCPUshot[oneX][twoX-1] = 'x';
					return true;
				}
				else{
					fieldCPUshot[oneX][twoX-1]='-';
					return false;
				}
			}
			else if (side==3 && oneX!=10 && fieldCPUshot[oneX+1][twoX] != '-'){
				if (field[oneX+1][twoX] == 'o'){
					field[oneX+1][twoX] = 'x';
					fieldCPUshot[oneX+1][twoX] = 'x';
					return true;
				}
				else{
					fieldCPUshot[oneX+1][twoX]='-';
					return false;
				}
				
			}
			else if (side==4 && twoX!=10 && fieldCPUshot[oneX][twoX+1] != '-'){
				if (field[oneX][twoX+1] == 'o'){
					field[oneX][twoX+1] = 'x';
					fieldCPUshot[oneX][twoX+1] = 'x';
					return true;
				}
				else{
					fieldCPUshot[oneX][twoX+1] = '-';
					return false;
				}
			}
			
		}while(!go);
		
		return false;
	}



	private static int CrippleCheck(char[][] fieldCPUshot, int oneX, int twoX) {
		
		if (oneX!=1 && fieldCPUshot[oneX-1][twoX] == 'x')
			return 1;
		else if (oneX!=10 && fieldCPUshot[oneX+1][twoX] == 'x')
			return 3;
		else if (twoX!=1 && fieldCPUshot[oneX][twoX-1] == 'x')
			return 2;
		else if (twoX!=10 && fieldCPUshot[oneX][twoX+1] == 'x')
			return 4;
		
		return 0;
	}



	private static boolean AlertCheck(char[][] fieldCPUshot) {
		for (int i = 1; i<fieldCPUshot.length; i++){
			for (int j = 1; j<fieldCPUshot.length; j++){
				if (fieldCPUshot[i][j] == 'X')
					return true;
			}
		}
		return false;
	}



	private static void Wait() {
		System.out.println("\n'Press Enter to continue...'\n");
		scn.nextLine();
		scn.nextLine();
	}
}