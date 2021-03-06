import java.util.Scanner;
import java.io.*;
import java.util.Random;


 

public class Maze {
  public static String[] ch = new String[4]; // To hold the symbols (border,open space, start, exit)
  public static Scanner scan = new Scanner(System.in);
  public static int[] start = new int[2];//start is by (start[0], start[1]) or (x,y)
  
  public static void main(String args[]) throws IOException {  // IO exception for reading from file
    String[][] maze; // declares maze
    int hold; // used to make decisions in multiple parts of the program 
    while (true) {
      System.out.println("Enter 1 to read maze from file\nEnter 2 to create maze randomly");
      while (true) {
        scan = new Scanner(System.in); 
        try { // Try catch 
          hold = scan.nextInt();
          break;
        } catch (Exception e) { 
          System.out.println("Error please re-enter your selection as a number");
        }
      }
      if (hold != 1 && hold != 2) {
        System.out.println("Error please re-enter");
        continue;
      }
      if (hold == 1) {
        maze = fileScan();
        break;
      }
      if (hold == 2) {
        maze = randomGen();
        break;
      }
    } // End of maze creation
    
    hold = 0;//Resets hold to be reused. Hold is now used as part of the recursive method, it sotres the direction  last traveled
       
    print(maze);
    
    try { // try catch 
      if (!findPath(maze, start[0], start[1], hold)) {
        System.out.println("Error: maze is unsolvable");
      } else {
        maze[start[0]][start[1]] = ch[2]; // change the start position back to the Start
        System.out.println("A solution was found");
        print(maze);
      }
    } catch (StackOverflowError e) {
      System.out.println("Error: maze may be unsolvable  (StackOverflowError)");
    }
    
  }
  
  // Print Method
  public static void print(String[][] maze) {
    for (int i = 0; i < maze[0].length; i++) {
      for (int ii = 0; ii < maze.length; ii++) {
        System.out.print(maze[ii][i] + "\t");
      }
      System.out.println();
    }
    System.out.println();
  }
  
  
  // Scanning maze from a file
  public static String[][] fileScan() throws IOException {
    scan.nextLine();//allows file to be gotten from scan.nextLine
    
    Scanner scanFile;
    System.out.println("Enter the name of the file");
    while (true) {
      try { // try catch
        scanFile = new Scanner(new File(scan.nextLine()));
        break;
      }
      catch (FileNotFoundException e){
        System.out.println("File entered does not exist\nPlease re-enter");
      }
    }
    
    int sizeY = scanFile.nextInt();
    int sizeX = scanFile.nextInt();
    String[][] maze = new String[sizeX][sizeY];
    
    for (int i = 0; i < 4; i++) {
      ch[i] = scanFile.next(); // collects the symbols
    }
    
    for (int i = 0; i < maze[0].length; i++) {
      String[] hold = (scanFile.next()).split("");
      for (int ii = 0; ii < maze.length; ii++) {
        if (hold[ii].equals(ch[2])) {// finds the start position
          start[0] = ii;
          start[1] = i;
        }
        maze[ii][i] = hold[ii];
      }
    }
    
    
    scanFile.close();
    return maze;
  }
  
  // Random Maze Method
  public static String[][] randomGen() {
    int sizeY;
    int sizeX;
    Random random = new Random();
    
    while(true) {
      System.out.println("Enter how tall the maze should be (MUST BE greater than 2)");
      scan = new Scanner(System.in); 
      try {
        sizeY = scan.nextInt();
        if(sizeY < 4)   // Prevents maze dimensions to be less than 4
          continue;
        break;
      } catch(Exception e){
        System.out.println("Error please re-enter height");
      }
    }
    
    while(true) {
      System.out.println("Enter how wide the maze should be (MUST BE greater than 2)");
      scan = new Scanner(System.in);
      try {
        sizeX = scan.nextInt();
        if(sizeX < 4)  // Prevents maze dimensions to be less than 4
          continue; 
        break;
      } catch(Exception e){
        System.out.println("Error please re-enter width");
      }
    }
    String[][] maze = new String[sizeX][sizeY];
    
    // input for symbols
    System.out.println("Enter symbol for border");
    ch[0] = scan.next();
    System.out.println("Enter symbol for open path");
    ch[1] = scan.next();
    System.out.println("Enter symbol for start");
    ch[2] = scan.next();
    System.out.println("Enter symbol for exit");
    ch[3] = scan.next();
    
    //Filling the borders with blocks
    for (int i = 0; i < sizeX; i++) {
      maze[i][0] = ch[0];
      maze[i][sizeY - 1] = ch[0];
    }
    for (int i = 1; i < sizeY - 1; i++) {
      maze[0][i] = ch[0];
      maze[sizeX - 1][i] = ch[0];
    }
    
    // Randomly fills the maze with open spaces and blocks
    for (int i = 1; i < sizeY - 1; i++) {
      for (int ii = 1; ii < sizeX - 1; ii++) {
        if (random.nextInt(2) == 0) { // 50% chance of open space or border
          maze[ii][i] = ch[0];
        } else {
          maze[ii][i] = ch[1];
        }
      }
    }
    //[2][3]
    // Randomly picks an Exit anywhere on the border except the corners
    int temp1;
    int temp2;
    if (random.nextInt(2) == 0) { // if Exit should be on the first or last row  (50%)
      if (random.nextInt(2) == 0)
        temp1 = 0;
      else
        temp1 = sizeX - 1;
      temp2 = random.nextInt(sizeY - 2) + 1; // Makes sure the Exit is not on a corner
    } else {                  // if Exit should be on the first or last column  (50%)
      if (random.nextInt(2) == 0)
        temp2 = 0;
      else
        temp2 = sizeY - 1;
      temp1 = random.nextInt(sizeX - 2) + 1; // Makes sure the Exit is not on a corner
    }
    maze[temp1][temp2] = ch[3];
    
    // Randomly picks a Starting point anywhere but the Exit point or corners
    while (true) {
      start[0] = random.nextInt(sizeX-2) + 1;
      start[1] = random.nextInt(sizeY-2) + 1; 
    
      if (start[0] != temp1 && start[1] != temp2) { // This makes sure the start and end points are not the same
        maze[start[0]][start[1]] = ch[2];
        break;
      }
    }
    return maze;
  }
  
  // Recursive Backtracking method
  public static boolean findPath(String[][] maze, int x, int y, int p) { 
    

    if (maze[x][y].equals(ch[3])) return true; // If the method reaches the Exit
    if (maze[x][y].equals(ch[0])) return false; // returns false if the method hits a block

    if (p != 3 && findPath(maze, x, y-1, 1)) {//if previous call did not move down calls itself and moves one up
      maze[x][y] = "+";
      return true;
    }
    if (p != 2 && findPath(maze, x-1, y, 4)) {//if previous call did not move right calls it self and moves left
      maze[x][y] = "+";
      return true;
    }
    if (p != 1 && findPath(maze, x, y+1, 3)) {//if previous call did not move up calls itself and moves one down
      maze[x][y] = "+";
      return true;
    }
    if (p != 4 && findPath(maze, x+1, y, 2)) {//if previous call did not move left calls itself and moves right
      maze[x][y] = "+";
      return true;
    }
    return false;  // returns false if all directions are exhausted
  }
} 
