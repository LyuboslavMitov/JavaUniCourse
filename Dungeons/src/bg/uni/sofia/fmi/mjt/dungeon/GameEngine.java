package bg.uni.sofia.fmi.mjt.dungeon;

import bg.uni.sofia.fmi.mjt.dungeon.actor.*;
import bg.uni.sofia.fmi.mjt.dungeon.treasure.*;

public class GameEngine {
	private char[][] map;
	private Hero hero;
	private Enemy[] enemies;
	private Treasure[] treasures;
	private int treasuresCounter=0;
	private int enemiesCounter=0;
	private Position heroPosition;
	
	public GameEngine(char[][] map, Hero hero, Enemy[] enemies, Treasure[] treasures, Position heroPosition) {
		this.map=map;
		this.hero= hero;
		this.enemies=enemies;
		this.treasures=treasures;
		this.heroPosition=heroPosition;
		//heroStartingPosition();
	}
	public GameEngine(char[][] map, Hero hero, Enemy[] enemies, Treasure[] treasures) {
		this.map=map;
		this.hero= hero;
		this.enemies=enemies;
		this.treasures=treasures;
		heroStartingPosition();
	}
	public char[][] getMap(){
		return map;
	}
	Position getHeroPosition() {
		return heroPosition;
	}
	public Hero getHero() {
		return hero;
	}
	public String makeMove(Direction direction) {
		int currentHeroX=heroPosition.getX();
		int currentHeroY=heroPosition.getY();
		switch (direction) {
		case LEFT:
			if(validateCoordinate(currentHeroX,currentHeroY-1)) {
				//update the map updateMap(currentHeroX-1, currentHeroY)
				String status=updateMap(currentHeroX,currentHeroY,currentHeroX, currentHeroY-1);
				return status;
			}
			break;
		case RIGHT:
			if(validateCoordinate(currentHeroX,currentHeroY+1)) {
				String status=updateMap(currentHeroX,currentHeroY,currentHeroX, currentHeroY+1);
				return status;
			}
			break;
		case UP:
			if(validateCoordinate(currentHeroX-1,currentHeroY)) {
				String status=updateMap(currentHeroX,currentHeroY,currentHeroX-1, currentHeroY);
				return status;
			}
			break;
		case DOWN:
			if(validateCoordinate(currentHeroX+1,currentHeroY)) {
				String status=updateMap(currentHeroX,currentHeroY,currentHeroX+1, currentHeroY);
				return status;
			}
			break;
		default:
			return "Unknown command entered.";	
		}
		return "";
	}
	private boolean validateCoordinate(int x, int y) {
		int rightBorder=map[0].length-1;
		int downBorder=map.length-1;
		if(y>rightBorder || y<0 || x>downBorder || x<0)
			return false;
		return true;
	}
	private String updateMap(int oldHeroPositionX,int oldHeroPositionY,int newHeroPositionX,int newHeroPositionY) {
		if(map[newHeroPositionX][newHeroPositionY]=='.') {
			heroPosition=new Position(newHeroPositionX, newHeroPositionY);
			map[newHeroPositionX][newHeroPositionY]='H';
			map[oldHeroPositionX][oldHeroPositionY]='.';
			return "You moved successfully to the next position.";
		}
		else if(map[newHeroPositionX][newHeroPositionY]=='#') {
			return "Wrong move. There is an obstacle and you cannot bypass it.";
		}
		else if(map[newHeroPositionX][newHeroPositionY]=='T') {
			heroPosition=new Position(newHeroPositionX, newHeroPositionY);
			map[newHeroPositionX][newHeroPositionY]='H';
			map[oldHeroPositionX][oldHeroPositionY]='.';
			return treasures[(treasuresCounter++)%treasures.length].collect(hero);
		}
		else if(map[newHeroPositionX][newHeroPositionY]=='E') {
			Enemy e = enemies[enemiesCounter++%enemies.length];
			while(true) {
				if(!e.isAlive())
				{
					map[newHeroPositionX][newHeroPositionY]='H';
					map[oldHeroPositionX][oldHeroPositionY]='.';
					return "Enemy died.";
				}
				if(!hero.isAlive())
				{
					return "Hero is dead! Game over!";
				}
				e.takeDamage(hero.attack());
				if(e.isAlive())
					hero.takeDamage(e.attack());
			}
		}
		else if(map[newHeroPositionX][newHeroPositionY]=='G') {
			return "You have successfully passed through the dungeon. Congrats!";
		}
		return "Invalid map?";
	}
	private void heroStartingPosition() {
		for (int i = 0; i < map.length; i++) {
			for (int j = 0; j < map[i].length; j++) {
				if(map[i][j]=='S') {
					heroPosition=new Position(i, j);
				}
			}
		}
	}
}
