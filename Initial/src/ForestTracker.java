public class ForestTracker {
	public static void main(String [] args){
		char forest3[][]= {{}};
		char forest2[][] = {
				{'s' ,'1' ,'s' ,'1' ,'R'},
				{'S', '2', '4', '4','4'},
				{'1', '3', 'S', '3', '4'}
		};
		char forest1[][]= {
				{'R', 'R', 'S', '1', '4'},
				{'S' ,'2' ,'4' ,'4' ,'2'},
				{'1', '3', 'S', '3', '4'}
		};
		ForestTracker a = new ForestTracker();
		a.trackForestTerrain(forest3,5);
	}
	/*R - река
	S - скала
	цифри от 1 до 4, които означават гъстота на гората*/
	public char[][] trackForestTerrain(char[][] forest,int years){
		
		int iterations = years / 10;
		for (int i = 0; i < iterations; i++) {
			updateForest(forest);
		}
		for (int i = 0; i < forest.length; i++) {
			for (int j = 0; j < forest[0].length; j++) {
				System.out.print(forest[i][j]);
			}
			System.out.println();
		}
		return forest;
	}
	public static void updateForest(char[][] forest) {
		for(int i = 0 ; i<forest.length;i++){
			for (int j = 0; j < forest[i].length; j++) {
				if(forest[i][j]>='1'&&forest[i][j]<='3' )
				{
					forest[i][j]+=1;
				}
				else if (forest[i][j]=='4') {
					int neighbours=0;
					//top-left
					if(i-1>=0 && j-1>=0) {
						if(forest[i-1][j-1]=='4')
							neighbours++;
					}
					//top
					if(i-1>=0) {
						if(forest[i-1][j]=='4')
							neighbours++;
					}	
					//top-right
					if(i-1>=0 && j+1<forest[i].length) {
						if(forest[i-1][j+1]=='4')
							neighbours++;
					}	
					//right
					if(j+1<forest[i].length) {
						if(forest[i][j+1]=='4')
							neighbours++;
					}	
					//bottom-right
					if(i+1<forest.length && j+1<forest[0].length) {
						if(forest[i+1][j+1]=='4')
							neighbours++;
				
					}	
					//bottom
					if(i+1<forest.length) {
						if(forest[i+1][j]=='4')
							neighbours++;
				
					}	
					//bottom-left
					if(i+1<forest.length && j-1>=0) {
						if(forest[i+1][j-1]=='4')
							neighbours++;
				
					}	
					//left
					if(j-1>=0) {
						if(forest[i][j-1]=='4')
							neighbours++;
					}
					
					if(neighbours>=3) {
						forest[i][j]='3';
					}
				}
			}
		}
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
