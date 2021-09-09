
public class Backup
{

}


//import java.util.*;
//
//public class minmaxag
//{
//	public static final int HORIZONTAL = 0;
//	public static final int VERTICAL = 1;
//	public static final int DIAGNAL = 2;
//	public static final int REVERSE_DIAGNAL = 3;
//	
//	public static final char MIN_CHAR = 'O';
//	public static final char MAX_CHAR = 'X';
//	public static final int BOARD_LENGTH = 7;
//	public static final int BOARD_HEIGHT = 6;
//	public static final int STATE_LENGTH = BOARD_LENGTH * BOARD_HEIGHT;
//	public static final int MAX_DEPTH = 5;
//
//	public static final int TIE = -1;
//	public static final int MIN_WINS = -10000;
//	public static final int MAX_WINS = 10000;
//	public static final int UNFINISHED = 10001;
//	
//	public static class SharedSpace
//	{
//
//		public SharedSpace(int x, int y, int scoreObtained, int shareId)
//		{
//			this.x = x;
//			this.y = y;
//			this.score = scoreObtained;
//			this.shareId = shareId;
//		}
//		
//		public boolean equals(SharedSpace other)
//		{
//			return this.x == other.x && this.y == other.y;
//		}
//		
//		int x;
//		int y;
//		int score;
//		int shareId;
//	}
//	
//	public static class MoveResult
//	{
//		public MoveResult(String state, int gameResult, int moveMade)
//		{
//			this.state = new String(state);
//			this.gameResult = gameResult;
//			this.moveMadeIdx = moveMade;
//		}
//		
//		public MoveResult()
//		{
//			this.state = "__________________________________________";
//			this.gameResult = UNFINISHED;
//		}
//		public String state;
//		public int gameResult;
//		public int moveMadeIdx;
//	}
//	
//	public static class NodeExpansionResult
//	{
//		public NodeExpansionResult(Integer nodeBest, Integer moveIdx)
//		{
//			this.nodeBest = nodeBest;
//			this.moveIdx = moveIdx;
//		}
//		Integer nodeBest;
//		Integer moveIdx;
//	}
//	
//	String lastState = "";
//	
//	int branchesCompleted = 0;
//	int branchesPruned = 0;
//
//	public minmaxag()
//	{
//	}
//
//	public int move(String S)
//	{
//		// positive x: ->
//		// positive y: ^
//
//		/*String test = "________O_X____XXO____XOX___XOXO______XXO_";
//		String p1 = test.substring(0, 41) + "X";
//		//only add the second part after moveChar if moveChar is not the last character
//		if (41 + 1 < STATE_LENGTH)
//			p1 += test.substring(41 + 1, STATE_LENGTH);
//		System.out.println(p1);*/
//		
//		int xCoord = 0;
//		int yCoord = 0;
//		String moveChar = "_";
//		int moveIdx = 0;
//		
//		//if lastState hasnt been initialized yet (opponent made the first move), figure out which move the opponent made by seeing which character is not '_'
//		if (lastState == "")
//		{
//			//figure out which move the opponent made
//			for (int x = 1; x <= BOARD_LENGTH; x++)
//			{
//				for (int y = 1; y <= BOARD_HEIGHT; y++)
//				{
//					if (charAtCoord(S, x, y) != '_')
//					{
//						xCoord = x;
//						yCoord = y;
//						//moveChar should really only be MIN_CHAR
//						moveChar = Character.toString(charAtCoord(S, x, y));
//						moveIdx = indexOfCoord(x, y);
//					}
//				}
//			}
//		}
//		//otherwise, figure out which move the opponent made by comparing the lastState with S
//		else
//		{
//			//figure out which move the opponent made
//			for (int x = 1; x <= BOARD_LENGTH; x++)
//			{
//				for (int y = 1; y <= BOARD_HEIGHT; y++)
//				{
//					if (charAtCoord(S, x, y) != charAtCoord(lastState, x, y))
//					{
//						xCoord = x;
//						yCoord = y;
//						//moveChar should really only be MIN_CHAR
//						moveChar = Character.toString(charAtCoord(S, x, y));
//						moveIdx = indexOfCoord(x, y);
//					}
//				}
//			}
//		}
//		//make a MoveResult from the move that the opponent made
//		int gameResult = evalMove(S, xCoord, yCoord, moveChar);
//		MoveResult startResult = new MoveResult(S, gameResult, moveIdx);
//		//calculate my move
//		NodeExpansionResult result = expandNode(startResult, 0, true, null);
//		//store my move as the lastState
//		//concatenate the first part of the state with moveChar
//		lastState = S.substring(0, result.moveIdx) + MAX_CHAR;
//		//only add the second part after moveChar if moveChar is not the last character
//		if (result.moveIdx + 1 < STATE_LENGTH)
//			lastState += S.substring(result.moveIdx + 1, STATE_LENGTH);
//
//		//make the move that I calculated
//		System.out.println("Result score:" + result.nodeBest);
//		return result.moveIdx;
//	}
//
//	// x is 1-based
//	// the max x is 7 (7 columns in the game)
//	private boolean canMove(String state, int x)
//	{
//		return charAtCoord(state, x, BOARD_HEIGHT) == '_';
//	}
//	
//	private int evalMove(String newState, int x, int y, String moveChar)
//	{
//		/*
//		 * FIRST: check if the game is over
//		 * Check the vertical ajdacents
//		 * Check the horizontal adjacents
//		 * Check diagnal adjacents
//		 */
//		
//		boolean winner = false;
//		boolean tie = true;
//		
//		//if the move was on the top row
//		if(y == BOARD_HEIGHT)
//		{
//			//check for the tie by seeing if the top row is not full
//			for (int i = 0; i < BOARD_LENGTH; i++)
//			{
//				//if we ran into an empty space, then tie is false
//				if (newState.charAt(i) == '_')
//				{
//					tie = false;
//				}
//			}
//		}
//		//otherwise, tie is automatically false because there is at least one move left
//		else
//		{
//			tie = false;
//		}
//		
//		int xScore = 0;
//		int yScore = 0;
//		int diagScore = 0;
//		int revDiagScore = 0;
//		char lastSeenX = '_';
//		char lastSeenY = '_';
//		char lastSeenDiag = '_';
//		char lastSeenRevDiag = '_';
//		
//		int offset = -3;
//		while (offset < 4 && !(winner || tie))
//		{
//			//if the horizontal coordinate is in bounds
//			if (coordInBounds(x + offset, y))
//			{
//				//if the moveChar == the current character and the lastSeenX character increment the xScore
//				char currentChar = charAtCoord(newState, x + offset, y);
//				if (moveChar.equals(Character.toString(currentChar)) && moveChar.equals(Character.toString(lastSeenX)))
//					xScore++;
//				lastSeenX = currentChar;
//				//if we found a winner, then stop
//				if (xScore >= 3)
//				{
//					winner = true;
//				}
//			}
//			
//			//if the horizontal coordinate is in bounds
//			if (coordInBounds(x, y + offset))
//			{
//				//if the moveChar == the current character and the lastSeenY character increment the YScore
//				char currentChar = charAtCoord(newState, x, y + offset);
//				if (moveChar.equals(Character.toString(currentChar)) && moveChar.equals(Character.toString(lastSeenY)))
//					yScore++;
//				lastSeenY = currentChar;
//				//if we found a winner, then stop
//				if (yScore >= 3)
//				{
//					winner = true;
//				}
//			}
//			
//			//if the diagnal coorinate is in bounds
//			if (coordInBounds(x + offset, y + offset))
//			{
//				//if the moveChar == the current character and the lastSeenDiag character increment the diagScore
//				char currentChar = charAtCoord(newState, x + offset, y + offset);
//				if (moveChar.equals(Character.toString(currentChar)) && moveChar.equals(Character.toString(lastSeenDiag)))
//					diagScore++;
//				lastSeenDiag = currentChar;
//				//if we found a winner, then stop
//				if (diagScore >= 3)
//				{
//					winner = true;
//				}
//			}
//			
//			//if the reverse diagnal coorinate is in bounds
//			if (coordInBounds(x + offset, y - offset))
//			{
//				//if the moveChar == the current character and the lastSeenRevDiag character increment the revDiagScore
//				char currentChar = charAtCoord(newState, x + offset, y - offset);
//				if (moveChar.equals(Character.toString(currentChar)) && moveChar.equals(Character.toString(lastSeenRevDiag)))
//					revDiagScore++;
//				lastSeenRevDiag = currentChar;
//				//if we found a winner, then stop
//				if (revDiagScore >= 3)
//				{
//					winner = true;
//				}
//			}
//			offset++;
//		}
//		
//		if (winner)
//		{
//			if (moveChar.equals(Character.toString(MAX_CHAR)))
//				return MAX_WINS;
//			else
//				return MIN_WINS;
//		}
//		else if (tie)
//		{
//			return TIE;
//		}
//		else
//		{
//			/*
//			*	Find a trap and rank the trap by it's height.
//			*	(Traps formed with lower height are more valuable than heigher height) (Height is relative to the distance to the nearest floor (token or bounds))
//			*
//			*	An opponent has to place a token when there is only one row and when you have a 3-in-a-row that your opponent has to block
//			*
//			*	Algorithm:
//			*  
//			*	Force trap:
//			*	First, find all of the places your opponent has to go
//			*		Find a potential 3-in-a-row (potential meaning that it's last token spot is open)
//			*		Store that spot as the place that the opponent has to go
//			*		Second, find any completable 4-in-a-row that uses the spot above the place where the opponent had to go
//			*		Rank the trap by it's height
//			*  
//			*	Fullproof trap:
//			*	First find a complete 3-in-a-row (complete meaning that it's last token spot has a height of 0)
//			*	Second check if there is another complete 3-in-a-row in the other direction
//			*  
//			*	If there are traps, return a win condition with the best trap rank (less than actual win condition)
//			*/
//			
//			//Max player's score evaluation
//			//for all tokens of Max player, check for potential traps
//			int maxScore = 0;
//			int minScore = 0;
//			
//			List<SharedSpace> maxUsedSpaces = new ArrayList<SharedSpace>();
//			List<SharedSpace> minUsedSpaces = new ArrayList<SharedSpace>();
//			
//			for (int yCoord = 1; yCoord <= BOARD_HEIGHT; yCoord++)
//			{
//				//go acrossed all of the tokens for this yCoord. keep track if there is any tokens on this level
//				for (int xCoord = 1; xCoord <= BOARD_LENGTH; xCoord++)
//				{
//					//if the next token across is MAX_CHAR or a '_', check for open 3-in-a-row possibilities for MAX
//					char prospectiveToken = charAtCoord(newState, xCoord, yCoord);
//					if (prospectiveToken == MAX_CHAR || prospectiveToken == '_')
//					{
//						//check if this token can be used as an open 3-in-a-row in HORIZONTAL, VERTICAL, DIAGNAL, REVERSE_DIAGNAL
//						
//						//HORIZONTAL
//						//only calculate if we have enough space
//						if (xCoord < 5)
//						{
//							List<SharedSpace> spacesToAdd = new ArrayList<SharedSpace>();
//							//try to start from xCoord, yCoord and store the empty space(s) that will be used to make a potential 4-in-a-row
//							int of = 0;
//							int directionScore = 0;
//							boolean neighborSeen = false;
//							int neighborId = 0;
//							boolean disrupted = false;
//							while (of < 4 && !disrupted)
//							{
//								//if the coord is in bounds, continue checking
//								int lookingX = xCoord + of;
//								int lookingY = yCoord;
//								if (coordInBounds(lookingX, lookingY))
//								{
//									//if the char is a MAX_CHAR then increment the score
//									char currentChar = charAtCoord(newState, lookingX, lookingY);
//									if (currentChar == MAX_CHAR)
//									{
//										directionScore++;
//									}
//									//if the char is a MIN_CHAR then we are disrupted
//									else if (currentChar == MIN_CHAR)
//									{
//										//the 3-in-a-row is disrupted
//										disrupted = true;
//									}
//									else
//									{
//										//if we haven't seen a neighbor yet, assign the neighborId
//										if (!neighborSeen)
//										{
//											neighborId = lookingX;
//										}
//										//say we've seen a neighbor
//										neighborSeen = true;
//										
//										//store it in spacesToAdd
//										SharedSpace newSpace = new SharedSpace(lookingX, lookingY, directionScore, neighborId);
//										spacesToAdd.add(newSpace);
//										
//									}
//								}
//								//otherwise, this will not work
//								else
//								{
//									disrupted = true;
//								}
//								of++;
//							}
//							
//							//if we can make a 4-in-a-row on these tokens, try to add the SharedSpaces to usedHorizontalShared spaces
//							if (!disrupted)
//							{
//								//check if spaces to add shares any spaces with another n-in-a-row. If it does, take the ones with the higher score
//								Iterator<SharedSpace> usedIterator = maxUsedSpaces.iterator();
//								boolean addSpaces = true;
//								while (usedIterator.hasNext())
//								{
//									SharedSpace usedSpace = usedIterator.next();
//									//if if the usedSpace shares an Id with spacesToAdd
//									if (neighborId == usedSpace.shareId)
//									{
//										//if the directionScore is greater than the usedSpace score, remove the usedSpace
//										if (usedSpace.score < directionScore)
//										{
//											usedIterator.remove();
//										}
//										//otherwise, just dont add the spacesToAdd
//										else
//										{
//											addSpaces = false;
//										}
//									}
//								}
//								
//								//if we can add these spacesToAdd, do it
//								if (addSpaces)
//								{
//									Iterator<SharedSpace> addIterator = spacesToAdd.iterator();
//									while (addIterator.hasNext())
//									{
//										//update the direction score
//										SharedSpace nextAddSpace = addIterator.next();
//										nextAddSpace.score = directionScore;
//										//add the space
//										maxUsedSpaces.add(nextAddSpace);
//									}
//								}
//							}
//							
//							//if direction score is >= three, then we have a 3-in-a-row (useful for trap logic)
//							if (directionScore >= 3)
//							{
//								
//							}
//						}
//						
//						//VERTICAL
//						//only calculate if we have enough space
//						if (yCoord < 4)
//						{
//							List<SharedSpace> spacesToAdd = new ArrayList<SharedSpace>();
//							//try to start from xCoord, yCoord and store the empty space(s) that will be used to make a potential 4-in-a-row
//							int of = 0;
//							int directionScore = 0;
//							boolean neighborSeen = false;
//							int neighborId = 0;
//							boolean disrupted = false;
//							while (of < 4 && !disrupted)
//							{
//								//if the coord is in bounds, continue checking
//								int lookingX = xCoord;
//								int lookingY = yCoord + of;
//								if (coordInBounds(lookingX, lookingY))
//								{
//									//if the char is a MAX_CHAR then increment the score
//									char currentChar = charAtCoord(newState, lookingX, lookingY);
//									if (currentChar == MAX_CHAR)
//									{
//										directionScore++;
//									}
//									//if the char is a MIN_CHAR then we are disrupted
//									else if (currentChar == MIN_CHAR)
//									{
//										//the 3-in-a-row is disrupted
//										disrupted = true;
//									}
//									else
//									{
//										//if we haven't seen a neighbor yet, assign the neighborId
//										if (!neighborSeen)
//										{
//											neighborId = lookingX;
//										}
//										//say we've seen a neighbor
//										neighborSeen = true;
//										
//										//store it in spacesToAdd
//										SharedSpace newSpace = new SharedSpace(lookingX, lookingY, directionScore, neighborId);
//										spacesToAdd.add(newSpace);
//										
//									}
//								}
//								//otherwise, this will not work
//								else
//								{
//									disrupted = true;
//								}
//								of++;
//							}
//							
//							//if we can make a 4-in-a-row on these tokens, try to add the SharedSpaces to usedHorizontalShared spaces
//							if (!disrupted)
//							{
//								//check if spaces to add shares an id with another n-in-a-row. If it does, take the ones with the higher score
//								Iterator<SharedSpace> usedIterator = maxUsedSpaces.iterator();
//								boolean addSpaces = true;
//								while (usedIterator.hasNext())
//								{
//									SharedSpace usedSpace = usedIterator.next();
//									//if if the usedSpace shares an Id with spacesToAdd
//									if (neighborId == usedSpace.shareId)
//									{
//										//if the directionScore is greater than the usedSpace score, remove the usedSpace
//										if (usedSpace.score < directionScore)
//										{
//											usedIterator.remove();
//										}
//										//otherwise, just dont add the spacesToAdd
//										else
//										{
//											addSpaces = false;
//										}
//									}
//								}
//								
//								//if we can add these spacesToAdd, do it
//								if (addSpaces)
//								{
//									Iterator<SharedSpace> addIterator = spacesToAdd.iterator();
//									while (addIterator.hasNext())
//									{
//										//update the direction score
//										SharedSpace nextAddSpace = addIterator.next();
//										nextAddSpace.score = directionScore;
//										//add the space
//										maxUsedSpaces.add(nextAddSpace);
//									}
//								}
//							}
//							
//							//if direction score is >= three, then we have a 3-in-a-row (useful for trap logic)
//							if (directionScore >= 3)
//							{
//								
//							}
//						}
//						
//						//DIAGNAL
//						//only calculate if we have enough space
//						if (xCoord < 5 && yCoord < 4)
//						{
//							List<SharedSpace> spacesToAdd = new ArrayList<SharedSpace>();
//							//try to start from xCoord, yCoord and store the empty space(s) that will be used to make a potential 4-in-a-row
//							int of = 0;
//							int directionScore = 0;
//							boolean neighborSeen = false;
//							int neighborId = 0;
//							boolean disrupted = false;
//							while (of < 4 && !disrupted)
//							{
//								//if the coord is in bounds, continue checking
//								int lookingX = xCoord + of;
//								int lookingY = yCoord + of;
//								if (coordInBounds(lookingX, lookingY))
//								{
//									//if the char is a MAX_CHAR then increment the score
//									char currentChar = charAtCoord(newState, lookingX, lookingY);
//									if (currentChar == MAX_CHAR)
//									{
//										directionScore++;
//									}
//									//if the char is a MIN_CHAR then we are disrupted
//									else if (currentChar == MIN_CHAR)
//									{
//										//the 3-in-a-row is disrupted
//										disrupted = true;
//									}
//									else
//									{
//										//if we haven't seen a neighbor yet, assign the neighborId
//										if (!neighborSeen)
//										{
//											neighborId = lookingX;
//										}
//										//say we've seen a neighbor
//										neighborSeen = true;
//										
//										//store it in spacesToAdd
//										SharedSpace newSpace = new SharedSpace(lookingX, lookingY, directionScore, neighborId);
//										spacesToAdd.add(newSpace);
//										
//									}
//								}
//								//otherwise, this will not work
//								else
//								{
//									disrupted = true;
//								}
//								of++;
//							}
//							
//							//if we can make a 4-in-a-row on these tokens, try to add the SharedSpaces to usedHorizontalShared spaces
//							if (!disrupted)
//							{
//								//check if spaces to add shares an id with another n-in-a-row. If it does, take the ones with the higher score
//								Iterator<SharedSpace> usedIterator = maxUsedSpaces.iterator();
//								boolean addSpaces = true;
//								while (usedIterator.hasNext())
//								{
//									SharedSpace usedSpace = usedIterator.next();
//									//if if the usedSpace shares an Id with spacesToAdd
//									if (neighborId == usedSpace.shareId)
//									{
//										//if the directionScore is greater than the usedSpace score, remove the usedSpace
//										if (usedSpace.score < directionScore)
//										{
//											usedIterator.remove();
//										}
//										//otherwise, just dont add the spacesToAdd
//										else
//										{
//											addSpaces = false;
//										}
//									}
//								}
//								
//								//if we can add these spacesToAdd, do it
//								if (addSpaces)
//								{
//									Iterator<SharedSpace> addIterator = spacesToAdd.iterator();
//									while (addIterator.hasNext())
//									{
//										//update the direction score
//										SharedSpace nextAddSpace = addIterator.next();
//										nextAddSpace.score = directionScore;
//										//add the space
//										maxUsedSpaces.add(nextAddSpace);
//									}
//								}
//							}
//							
//							//if direction score is >= three, then we have a 3-in-a-row (useful for trap logic)
//							if (directionScore >= 3)
//							{
//								
//							}
//						}
//						
//						//REVERSE_DIAGNAL
//						//only calculate if we have enough space
//						if (xCoord < 5 && yCoord > 3)
//						{
//							List<SharedSpace> spacesToAdd = new ArrayList<SharedSpace>();
//							//try to start from xCoord, yCoord and store the empty space(s) that will be used to make a potential 4-in-a-row
//							int of = 0;
//							int directionScore = 0;
//							boolean neighborSeen = false;
//							int neighborId = 0;
//							boolean disrupted = false;
//							while (of < 4 && !disrupted)
//							{
//								//if the coord is in bounds, continue checking
//								int lookingX = xCoord + of;
//								int lookingY = yCoord - of;
//								if (coordInBounds(lookingX, lookingY))
//								{
//									//if the char is a MAX_CHAR then increment the score
//									char currentChar = charAtCoord(newState, lookingX, lookingY);
//									if (currentChar == MAX_CHAR)
//									{
//										directionScore++;
//									}
//									//if the char is a MIN_CHAR then we are disrupted
//									else if (currentChar == MIN_CHAR)
//									{
//										//the 3-in-a-row is disrupted
//										disrupted = true;
//									}
//									else
//									{
//										//if we haven't seen a neighbor yet, assign the neighborId
//										if (!neighborSeen)
//										{
//											neighborId = lookingX;
//										}
//										//say we've seen a neighbor
//										neighborSeen = true;
//										
//										//store it in spacesToAdd
//										SharedSpace newSpace = new SharedSpace(lookingX, lookingY, directionScore, neighborId);
//										spacesToAdd.add(newSpace);
//										
//									}
//								}
//								//otherwise, this will not work
//								else
//								{
//									disrupted = true;
//								}
//								of++;
//							}
//							
//							//if we can make a 4-in-a-row on these tokens, try to add the SharedSpaces to usedHorizontalShared spaces
//							if (!disrupted)
//							{
//								//check if spaces to add shares an id with another n-in-a-row. If it does, take the ones with the higher score
//								Iterator<SharedSpace> usedIterator = maxUsedSpaces.iterator();
//								boolean addSpaces = true;
//								while (usedIterator.hasNext())
//								{
//									SharedSpace usedSpace = usedIterator.next();
//									//if if the usedSpace shares an Id with spacesToAdd
//									if (neighborId == usedSpace.shareId)
//									{
//										//if the directionScore is greater than the usedSpace score, remove the usedSpace
//										if (usedSpace.score < directionScore)
//										{
//											usedIterator.remove();
//										}
//										//otherwise, just dont add the spacesToAdd
//										else
//										{
//											addSpaces = false;
//										}
//									}
//								}
//								
//								//if we can add these spacesToAdd, do it
//								if (addSpaces)
//								{
//									Iterator<SharedSpace> addIterator = spacesToAdd.iterator();
//									while (addIterator.hasNext())
//									{
//										//update the direction score
//										SharedSpace nextAddSpace = addIterator.next();
//										nextAddSpace.score = directionScore;
//										//add the space
//										maxUsedSpaces.add(nextAddSpace);
//									}
//								}
//							}
//							
//							//if direction score is >= three, then we have a 3-in-a-row (useful for trap logic)
//							if (directionScore >= 3)
//							{
//								
//							}
//						}
//					}
//					
//					
//					
//					//if the next token across is MIN_CHAR or a '_', check for open 3-in-a-row possibilities for MIN
//					if (prospectiveToken == MIN_CHAR || prospectiveToken == '_')
//					{
//						//check if this token can be used as an open 3-in-a-row in HORIZONTAL, VERTICAL, DIAGNAL, REVERSE_DIAGNAL
//						
//						//HORIZONTAL
//						//only calculate if we have enough space
//						if (xCoord < 5)
//						{
//							List<SharedSpace> spacesToAdd = new ArrayList<SharedSpace>();
//							//try to start from xCoord, yCoord and store the empty space(s) that will be used to make a potential 4-in-a-row
//							int of = 0;
//							int directionScore = 0;
//							boolean neighborSeen = false;
//							int neighborId = 0;
//							boolean disrupted = false;
//							while (of < 4 && !disrupted)
//							{
//								//if the coord is in bounds, continue checking
//								int lookingX = xCoord + of;
//								int lookingY = yCoord;
//								if (coordInBounds(lookingX, lookingY))
//								{
//									//if the char is a MAX_CHAR then increment the score
//									char currentChar = charAtCoord(newState, lookingX, lookingY);
//									if (currentChar == MIN_CHAR)
//									{
//										directionScore++;
//									}
//									//if the char is a MIN_CHAR then we are disrupted
//									else if (currentChar == MAX_CHAR)
//									{
//										//the 3-in-a-row is disrupted
//										disrupted = true;
//									}
//									else
//									{
//										//if we haven't seen a neighbor yet, assign the neighborId
//										if (!neighborSeen)
//										{
//											neighborId = lookingX;
//										}
//										//say we've seen a neighbor
//										neighborSeen = true;
//										
//										//store it in spacesToAdd
//										SharedSpace newSpace = new SharedSpace(lookingX, lookingY, directionScore, neighborId);
//										spacesToAdd.add(newSpace);
//										
//									}
//								}
//								//otherwise, this will not work
//								else
//								{
//									disrupted = true;
//								}
//								of++;
//							}
//							
//							//if we can make a 4-in-a-row on these tokens, try to add the SharedSpaces to usedHorizontalShared spaces
//							if (!disrupted)
//							{
//								//check if spaces to add shares any spaces with another n-in-a-row. If it does, take the ones with the higher score
//								Iterator<SharedSpace> usedIterator = minUsedSpaces.iterator();
//								boolean addSpaces = true;
//								while (usedIterator.hasNext())
//								{
//									SharedSpace usedSpace = usedIterator.next();
//									//if if the usedSpace shares an Id with spacesToAdd
//									if (neighborId == usedSpace.shareId)
//									{
//										//if the directionScore is greater than the usedSpace score, remove the usedSpace
//										if (usedSpace.score < directionScore)
//										{
//											usedIterator.remove();
//										}
//										//otherwise, just dont add the spacesToAdd
//										else
//										{
//											addSpaces = false;
//										}
//									}
//								}
//								
//								//if we can add these spacesToAdd, do it
//								if (addSpaces)
//								{
//									Iterator<SharedSpace> addIterator = spacesToAdd.iterator();
//									while (addIterator.hasNext())
//									{
//										//update the direction score
//										SharedSpace nextAddSpace = addIterator.next();
//										nextAddSpace.score = directionScore;
//										//add the space
//										minUsedSpaces.add(nextAddSpace);
//									}
//								}
//							}
//							
//							//if direction score is >= three, then we have a 3-in-a-row (useful for trap logic)
//							if (directionScore >= 3)
//							{
//								
//							}
//						}
//						
//						//VERTICAL
//						//only calculate if we have enough space
//						if (yCoord < 4)
//						{
//							List<SharedSpace> spacesToAdd = new ArrayList<SharedSpace>();
//							//try to start from xCoord, yCoord and store the empty space(s) that will be used to make a potential 4-in-a-row
//							int of = 0;
//							int directionScore = 0;
//							boolean neighborSeen = false;
//							int neighborId = 0;
//							boolean disrupted = false;
//							while (of < 4 && !disrupted)
//							{
//								//if the coord is in bounds, continue checking
//								int lookingX = xCoord;
//								int lookingY = yCoord + of;
//								if (coordInBounds(lookingX, lookingY))
//								{
//									//if the char is a MAX_CHAR then increment the score
//									char currentChar = charAtCoord(newState, lookingX, lookingY);
//									if (currentChar == MIN_CHAR)
//									{
//										directionScore++;
//									}
//									//if the char is a MIN_CHAR then we are disrupted
//									else if (currentChar == MAX_CHAR)
//									{
//										//the 3-in-a-row is disrupted
//										disrupted = true;
//									}
//									else
//									{
//										//if we haven't seen a neighbor yet, assign the neighborId
//										if (!neighborSeen)
//										{
//											neighborId = lookingX;
//										}
//										//say we've seen a neighbor
//										neighborSeen = true;
//										
//										//store it in spacesToAdd
//										SharedSpace newSpace = new SharedSpace(lookingX, lookingY, directionScore, neighborId);
//										spacesToAdd.add(newSpace);
//										
//									}
//								}
//								//otherwise, this will not work
//								else
//								{
//									disrupted = true;
//								}
//								of++;
//							}
//							
//							//if we can make a 4-in-a-row on these tokens, try to add the SharedSpaces to usedHorizontalShared spaces
//							if (!disrupted)
//							{
//								//check if spaces to add shares an id with another n-in-a-row. If it does, take the ones with the higher score
//								Iterator<SharedSpace> usedIterator = minUsedSpaces.iterator();
//								boolean addSpaces = true;
//								while (usedIterator.hasNext())
//								{
//									SharedSpace usedSpace = usedIterator.next();
//									//if if the usedSpace shares an Id with spacesToAdd
//									if (neighborId == usedSpace.shareId)
//									{
//										//if the directionScore is greater than the usedSpace score, remove the usedSpace
//										if (usedSpace.score < directionScore)
//										{
//											usedIterator.remove();
//										}
//										//otherwise, just dont add the spacesToAdd
//										else
//										{
//											addSpaces = false;
//										}
//									}
//								}
//								
//								//if we can add these spacesToAdd, do it
//								if (addSpaces)
//								{
//									Iterator<SharedSpace> addIterator = spacesToAdd.iterator();
//									while (addIterator.hasNext())
//									{
//										//update the direction score
//										SharedSpace nextAddSpace = addIterator.next();
//										nextAddSpace.score = directionScore;
//										//add the space
//										minUsedSpaces.add(nextAddSpace);
//									}
//								}
//							}
//							
//							//if direction score is >= three, then we have a 3-in-a-row (useful for trap logic)
//							if (directionScore >= 3)
//							{
//								
//							}
//						}
//						
//						//DIAGNAL
//						//only calculate if we have enough space
//						if (xCoord < 5 && yCoord < 4)
//						{
//							List<SharedSpace> spacesToAdd = new ArrayList<SharedSpace>();
//							//try to start from xCoord, yCoord and store the empty space(s) that will be used to make a potential 4-in-a-row
//							int of = 0;
//							int directionScore = 0;
//							boolean neighborSeen = false;
//							int neighborId = 0;
//							boolean disrupted = false;
//							while (of < 4 && !disrupted)
//							{
//								//if the coord is in bounds, continue checking
//								int lookingX = xCoord + of;
//								int lookingY = yCoord + of;
//								if (coordInBounds(lookingX, lookingY))
//								{
//									//if the char is a MAX_CHAR then increment the score
//									char currentChar = charAtCoord(newState, lookingX, lookingY);
//									if (currentChar == MIN_CHAR)
//									{
//										directionScore++;
//									}
//									//if the char is a MIN_CHAR then we are disrupted
//									else if (currentChar == MAX_CHAR)
//									{
//										//the 3-in-a-row is disrupted
//										disrupted = true;
//									}
//									else
//									{
//										//if we haven't seen a neighbor yet, assign the neighborId
//										if (!neighborSeen)
//										{
//											neighborId = lookingX;
//										}
//										//say we've seen a neighbor
//										neighborSeen = true;
//										
//										//store it in spacesToAdd
//										SharedSpace newSpace = new SharedSpace(lookingX, lookingY, directionScore, neighborId);
//										spacesToAdd.add(newSpace);
//										
//									}
//								}
//								//otherwise, this will not work
//								else
//								{
//									disrupted = true;
//								}
//								of++;
//							}
//							
//							//if we can make a 4-in-a-row on these tokens, try to add the SharedSpaces to usedHorizontalShared spaces
//							if (!disrupted)
//							{
//								//check if spaces to add shares an id with another n-in-a-row. If it does, take the ones with the higher score
//								Iterator<SharedSpace> usedIterator = minUsedSpaces.iterator();
//								boolean addSpaces = true;
//								while (usedIterator.hasNext())
//								{
//									SharedSpace usedSpace = usedIterator.next();
//									//if if the usedSpace shares an Id with spacesToAdd
//									if (neighborId == usedSpace.shareId)
//									{
//										//if the directionScore is greater than the usedSpace score, remove the usedSpace
//										if (usedSpace.score < directionScore)
//										{
//											usedIterator.remove();
//										}
//										//otherwise, just dont add the spacesToAdd
//										else
//										{
//											addSpaces = false;
//										}
//									}
//								}
//								
//								//if we can add these spacesToAdd, do it
//								if (addSpaces)
//								{
//									Iterator<SharedSpace> addIterator = spacesToAdd.iterator();
//									while (addIterator.hasNext())
//									{
//										//update the direction score
//										SharedSpace nextAddSpace = addIterator.next();
//										nextAddSpace.score = directionScore;
//										//add the space
//										minUsedSpaces.add(nextAddSpace);
//									}
//								}
//							}
//							
//							//if direction score is >= three, then we have a 3-in-a-row (useful for trap logic)
//							if (directionScore >= 3)
//							{
//								
//							}
//						}
//						
//						//REVERSE_DIAGNAL
//						//only calculate if we have enough space
//						if (xCoord < 5 && yCoord > 3)
//						{
//							List<SharedSpace> spacesToAdd = new ArrayList<SharedSpace>();
//							//try to start from xCoord, yCoord and store the empty space(s) that will be used to make a potential 4-in-a-row
//							int of = 0;
//							int directionScore = 0;
//							boolean neighborSeen = false;
//							int neighborId = 0;
//							boolean disrupted = false;
//							while (of < 4 && !disrupted)
//							{
//								//if the coord is in bounds, continue checking
//								int lookingX = xCoord + of;
//								int lookingY = yCoord - of;
//								if (coordInBounds(lookingX, lookingY))
//								{
//									//if the char is a MAX_CHAR then increment the score
//									char currentChar = charAtCoord(newState, lookingX, lookingY);
//									if (currentChar == MIN_CHAR)
//									{
//										directionScore++;
//									}
//									//if the char is a MIN_CHAR then we are disrupted
//									else if (currentChar == MAX_CHAR)
//									{
//										//the 3-in-a-row is disrupted
//										disrupted = true;
//									}
//									else
//									{
//										//if we haven't seen a neighbor yet, assign the neighborId
//										if (!neighborSeen)
//										{
//											neighborId = lookingX;
//										}
//										//say we've seen a neighbor
//										neighborSeen = true;
//										
//										//store it in spacesToAdd
//										SharedSpace newSpace = new SharedSpace(lookingX, lookingY, directionScore, neighborId);
//										spacesToAdd.add(newSpace);
//										
//									}
//								}
//								//otherwise, this will not work
//								else
//								{
//									disrupted = true;
//								}
//								of++;
//							}
//							
//							//if we can make a 4-in-a-row on these tokens, try to add the SharedSpaces to usedHorizontalShared spaces
//							if (!disrupted)
//							{
//								//check if spaces to add shares an id with another n-in-a-row. If it does, take the ones with the higher score
//								Iterator<SharedSpace> usedIterator = minUsedSpaces.iterator();
//								boolean addSpaces = true;
//								while (usedIterator.hasNext())
//								{
//									SharedSpace usedSpace = usedIterator.next();
//									//if if the usedSpace shares an Id with spacesToAdd
//									if (neighborId == usedSpace.shareId)
//									{
//										//if the directionScore is greater than the usedSpace score, remove the usedSpace
//										if (usedSpace.score < directionScore)
//										{
//											usedIterator.remove();
//										}
//										//otherwise, just dont add the spacesToAdd
//										else
//										{
//											addSpaces = false;
//										}
//									}
//								}
//								
//								//if we can add these spacesToAdd, do it
//								if (addSpaces)
//								{
//									Iterator<SharedSpace> addIterator = spacesToAdd.iterator();
//									while (addIterator.hasNext())
//									{
//										//update the direction score
//										SharedSpace nextAddSpace = addIterator.next();
//										nextAddSpace.score = directionScore;
//										//add the space
//										minUsedSpaces.add(nextAddSpace);
//									}
//								}
//							}
//							
//							//if direction score is >= three, then we have a 3-in-a-row (useful for trap logic)
//							if (directionScore >= 3)
//							{
//								
//							}
//						}
//					}
//					
//				}
//			}
//			
//			//increment the score for each unique id in the maxUsedSpaces
//			List<Integer> idSeen = new ArrayList<Integer>();
//			Iterator<SharedSpace> iUsed = maxUsedSpaces.iterator();
//			while (iUsed.hasNext())
//			{
//				SharedSpace space = iUsed.next();
//				
//				Iterator<Integer> idIterator = idSeen.iterator();
//				boolean containsId = false;
//				while (!containsId && idIterator.hasNext())
//				{
//					if (idIterator.next().equals(new Integer(space.shareId)))
//					{
//						containsId = true;
//					}
//				}
//				if (!containsId)
//				{
//					idSeen.add(new Integer(space.shareId));
//					maxScore += space.shareId;
//				}
//			}
//			
//			//increment the score for each unique id in the minUsedSpaces
//			idSeen = new ArrayList<Integer>();
//			iUsed = minUsedSpaces.iterator();
//			while (iUsed.hasNext())
//			{
//				SharedSpace space = iUsed.next();
//				
//				Iterator<Integer> idIterator = idSeen.iterator();
//				boolean containsId = false;
//				while (!containsId && idIterator.hasNext())
//				{
//					if (idIterator.next().equals(new Integer(space.shareId)))
//					{
//						containsId = true;
//					}
//				}
//				if (!containsId)
//				{
//					idSeen.add(new Integer(space.shareId));
//					minScore += space.shareId;
//				}
//			}
//			
//			return maxScore - minScore;
//		}
//	}
//
//	// x is 1-based
//	// the max x is 7 (7 columns in the game)
//	private MoveResult makeMove(String state, int x, String moveChar)
//	{
//		// if we can make a move at this location, do it
//		if (canMove(state, x))
//		{
//			int y = BOARD_HEIGHT;
//			//if the char below the current y is open, move down by 1
//			while (y > 1 && charAtCoord(state, x, y - 1) == '_')
//			{
//				y--;
//			}
//
//			// insert the move at the selected x and y coord (will override any move that is already existing)
//			int charIdx = indexOfCoord(x, y);
//			//concatenate the first part of the state with moveChar
//			String stateResult = state.substring(0, charIdx) + moveChar;
//			//only add the second part after moveChar if moveChar is not the last character
//			if (charIdx + 1 < STATE_LENGTH)
//				stateResult += state.substring(charIdx + 1, STATE_LENGTH);
//			//evaluate the move and return the result
//			MoveResult moveResult = new MoveResult(stateResult, evalMove(stateResult, x, y, moveChar), charIdx);
//			return moveResult;
//		}
//		// otherwise, return the state that was given with an UNFINISHED result
//		{
//			System.out.println("Cannot make that move! \nState:" + state + "\nX: " + x);
//			return new MoveResult(state, UNFINISHED, 1);
//		}
//
//	}
//
//	// x and y are 1-based
//	// the max y is 6 (6 rows in the game)
//	// the max x is 7 (7 columns in the game)
//	private char charAtCoord(String state, int x, int y)
//	{
//		int index = STATE_LENGTH - (y * BOARD_LENGTH) + x - 1;
//		return state.charAt(index);
//	}
//
//	// x and y are 1-based
//	// the max y is 6 (6 rows in the game)
//	// the max x is 7 (7 columns in the game)
//	private int indexOfCoord(int x, int y)
//	{
//		return STATE_LENGTH - (y * BOARD_LENGTH) + x - 1;
//	}
//	
//	private boolean coordInBounds(int x, int y)
//	{
//		return x <= BOARD_LENGTH && x >= 1 && y <= BOARD_HEIGHT && y >= 1;
//	}
//
//	private NodeExpansionResult expandNode(MoveResult nodeState, int nodeDepth, boolean nodeIsMax, Integer parentBest)
//	{
//		System.out.println("NodeDepth: " + nodeDepth);
//		System.out.println("Branches completed: " + branchesCompleted);
//		// check if we have gone as far into the tree as we can go
//		if (nodeState.gameResult == MAX_WINS
//				|| nodeState.gameResult == MIN_WINS
//				|| nodeState.gameResult == TIE
//				|| nodeDepth == MAX_DEPTH)
//		{
//			if (nodeState.gameResult == MIN_WINS)
//			{
//				System.out.println("Went through MIN_WINS");
//			}
//			branchesCompleted++;
//			return new NodeExpansionResult(nodeState.gameResult, nodeState.moveMadeIdx);
//		}
//
//		// initialize the variables that will be used for expanding nodes
//		Integer nodeBestMoveIdx = null;
//		Integer nodeBest = null;
//		boolean stop = false;
//
//		// initialize the variables that will be used for calculating successors
//		String moveChar;
//		if (nodeIsMax)
//			moveChar = Character.toString(MAX_CHAR);
//		else
//			moveChar = Character.toString(MIN_CHAR);
//		List<MoveResult> successors = new ArrayList<MoveResult>();
//		// calculate the successors. Try to move at every x coordinate
//		for (int x = 1; x <= BOARD_LENGTH; x++)
//		{
//			//start at the middle
//			int xCoord = ((x + 2) % BOARD_LENGTH) + 1;
//			// if we can move at this x coordinate
//			if (canMove(nodeState.state, xCoord))
//			{
//				// make the move and add it to the successors
//				MoveResult successorState = makeMove(nodeState.state, xCoord, moveChar);
//				
//				successors.add(successorState);
//			}
//		}
//
//		// expand successors with AB pruning
//		// always expand the first successor
//		Iterator<MoveResult> successorsIterator = successors.iterator();
//		if (successorsIterator.hasNext())
//		{
//			MoveResult successorState = successorsIterator.next();
//			nodeBest = expandNode(successorState, nodeDepth + 1, !nodeIsMax, nodeBest).nodeBest;
//			nodeBestMoveIdx = successorState.moveMadeIdx;
//		}
//		else
//			// should never happen if the evalMove at the beginning of the function is working
//			// stop expanding if there are no successors
//			stop = true;
//
//		// while the condition of AB pruning is satisfied, and we still have successors
//		// left to expand
//		while (!stop)
//		{
//			// if we have more successors to expand, expand them
//			if (successorsIterator.hasNext())
//			{
//				// if this is a max node
//				if (nodeIsMax)
//				{
//					// check if it is good to keep expanding nodes from this node
//					if (parentBest != null && nodeBest >= parentBest)
//					{
//						branchesPruned++;
//						System.out.println("Pruned: " + branchesPruned);
//						stop = true;
//					}
//					else
//					{
//						// get the max of the new move and the current best move
//						MoveResult successorState = successorsIterator.next();
//						int prospectiveBest = expandNode(successorState, nodeDepth + 1, !nodeIsMax, nodeBest).nodeBest;
//						if (prospectiveBest > nodeBest)
//						{
//							nodeBest = prospectiveBest;
//							nodeBestMoveIdx = successorState.moveMadeIdx;
//						}
//					}
//				}
//				// otherwise, if this is a min node
//				else
//				{
//					// check if it is good to keep expanding nodes from this node
//					if (parentBest != null && nodeBest <= parentBest)
//						stop = true;
//					else
//					{
//						// get the min of the new move and the current best move
//						MoveResult successorState = successorsIterator.next();
//						int prospectiveBest = expandNode(successorState, nodeDepth + 1, !nodeIsMax, nodeBest).nodeBest;
//						if (prospectiveBest < nodeBest)
//						{
//							nodeBest = prospectiveBest;
//							nodeBestMoveIdx = successorState.moveMadeIdx;
//						}
//					}
//				}
//			}
//			// otherwise, stop expanding
//			else
//			{
//				stop = true;
//			}
//		}
//		return new NodeExpansionResult(nodeBest, nodeBestMoveIdx);
//	}
//}

