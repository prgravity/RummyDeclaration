package rummy;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.StringTokenizer;
/**
 *
 * @author vkumawat
 */

class Hand{
    int[][] game;
    int fourRuns;
    int threeRuns;
    int threeSets;
    
    public Hand(){
        game = new int[13][4];
        fourRuns = 0;
        threeRuns = 0;
        threeSets = 0;
    }
    
    public Hand(Hand hand){
        this.game = hand.game;
        this.fourRuns = 0;
        this.threeRuns = 0;
        this.threeSets = 0;
    }
    
    public Hand(int[] cards){
        game = new int[13][4];
        fourRuns = 0;
        threeRuns = 0;
        threeSets = 0;
        
        for (int i = 0; i < cards.length; i++) {
            int card = cards[i % 52];
            int rank = card % 13;
            int suit = card / 13;
            game[rank][suit] = 1;
        }
    }
    
    public void printHand(){
        for(int i = 0; i < 13; i++){
            for(int j = 0; j < 4; j++){
                System.out.print(game[i][j]+"\t");
            }
            System.out.println();
        }
    }
    
    public void initializeCardsRandomly(int n){
        Random rgen = new Random();
        int[] cards = new int[52];

        for (int i = 0; i < 52; i++) {
            cards[i] = i;
        }

        for (int i = 0; i < 52; i++) {
            int randomPosition = rgen.nextInt(cards.length);
            int temp = cards[i];
            cards[i] = cards[randomPosition];
            cards[randomPosition] = temp;
        }

        for (int i = 0; i < n; i++) {
            int card = cards[i % 52];
            int rank = card % 13;
            int suit = card / 13;
            game[rank][suit] = 1;
        }
    }
    
    public void getRunsOfFour(){
        for(int i = 0; i < 4; i++){
            for( int j = 0; j < 13; j++){
                if(j + 3 < 13 && fourRuns == 0 && game[j][i] == 1 && game[j+1][i] == 1 && game[j+2][i] == 1 && game[j+3][i] == 1){
                    game[j][i] = 0;
                    game[j + 1][i] = 0;
                    game[j + 2][i] = 0;
                    game[j + 3][i] = 0;
                    fourRuns++;
                }
            }
        }
    }
    
    public void getRunsOfThree(){
        for(int i = 0; i < 4; i++){
            for( int j = 0; j < 13; j++){
                if(threeRuns < 3 && j + 2 < 13 && game[j][i] == 1 && game[j + 1][i] == 1 && game[j + 2][i] == 1){
                    game[j][i] = 0;
                    game[j + 1][i] = 0;
                    game[j + 2][i] = 0;
                    threeRuns++;
                }
            }
        }
    }
    
    public void getSetsOfThree(){
        for(int i = 0; i < 13; i++){
            for( int j = 0; j < 4; j++){
                if(j + 2 < 4 && threeSets < 3 && game[i][j] == 1 && game[i][j+1] == 1 && game[i][j+2] == 1){
                    game[i][j] = 0;
                    game[i][j + 1] = 0;
                    game[i][j + 2] = 0;
                    threeSets++;
                }
            }
        }
    }
        
     public int getRemainingCardsForRunsOfFour(){
         int minCount = 100000;
         int minSuit = 0;
         int minRank = 0;
        for(int i = 0; i < 4; i++){
            for( int j = 0; j < 10; j++){
                int count = 4 - game[j][i] + game[j + 1][i] + game[j + 2][i] + game[j + 3][i];
                if(count < minCount){
                    minCount = count;
                    minSuit = i;
                    minRank = j;
                }
            }
        }
        game[minRank][minSuit] = 0;
        game[minRank + 1][minSuit] = 0;
        game[minRank + 2][minSuit] = 0;
        game[minRank + 3][minSuit] = 0;
        return minCount;
     }
     
     public int getRemainingCardsForRunsAndSetsOfThree(){
        int minCountRun = 100000;
        int minSuitRun = 0;
        int minRankRun = 0;
        for(int i = 0; i < 4; i++){
            for( int j = 0; j < 10; j++){
                int count = 3 - game[j][i] + game[j + 1][i] + game[j + 2][i];
                if(count < minCountRun){
                    minCountRun = count;
                    minSuitRun = i;
                    minRankRun = j;
                }
            }
        }
        
        int minCountSet = 100000;
        int minSuitSet = 0;
        int minRankSet = 0;
        for(int i = 0; i < 13; i++){
            for( int j = 0; j < 2; j++){
                int count = 3 - game[i][j] + game[i][j+1] + game[i][j+2];
                if(count < minCountSet){
                    minCountSet = count;
                    minSuitSet = j;
                    minRankSet = i;
                }
            }
        }

        if(minCountRun < minCountSet){
            game[minRankRun][minSuitRun] = 0;
            game[minRankRun + 1][minSuitRun] = 0;
            game[minRankRun + 2][minSuitRun] = 0;
            return minCountRun;
        }
        else{
            game[minRankSet][minSuitSet] = 0;
            game[minRankSet][minSuitSet + 1] = 0;
            game[minRankSet][minSuitSet + 2] = 0;
            return minCountSet;
        }
     }
        
    public int getRemainingCards(){
        int count = 0;
        if(fourRuns == 0){
            count += getRemainingCardsForRunsOfFour();
        }
        for(int i = 0; i < 3 - (threeSets + threeRuns); i++){
            count += getRemainingCardsForRunsAndSetsOfThree();
        }
        return count;
    }
    
    public Hand getOptimalSolution(Hand hand){
        Hand newHand = new Hand(hand);
        hand.getRunsOfFour();
        hand.getSetsOfThree();  //preference to sets of three
        hand.getRunsOfThree();
        
        newHand.getRunsOfFour();
        newHand.getRunsOfThree(); //preference to runs of three
        newHand.getSetsOfThree();
        
        if(newHand.getRemainingCards() > hand.getRemainingCards())
            return hand;
        return newHand;
    }
}

public class RummyGame {
    public static void main(String args[]){
        int[] cards = {0,1,2,3,13,14,15,16,26,27,28,29,39};
        Hand hand = new Hand(cards);
        hand.printHand();
        //hand.initializeCardsRandomly(13);
        hand = hand.getOptimalSolution(hand);
        
        System.out.println("Runs Of Four: "+hand.fourRuns+" Runs Of Three "+hand.threeRuns+" Sets Of Three: "+hand.threeSets);
        System.out.println("Number of cards needed: "+ hand.getRemainingCards());
    }
}
