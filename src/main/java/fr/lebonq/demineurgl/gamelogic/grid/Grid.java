package fr.lebonq.demineurgl.gamelogic.grid;

import java.util.Date;
import java.util.Random;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import fr.lebonq.demineurgl.engine.Window;
import fr.lebonq.demineurgl.engine.graphics.AnimatedSprite;
import fr.lebonq.demineurgl.engine.graphics.Texture;

public class Grid {
    private static final Logger LOGGER = LogManager.getLogger();
    private Box[][] aBoxes;
    private Box[] aBombs;
    private int aNumberFlag;
    private boolean aInteractive;

    public static final Texture BOMB_TEXTURE = new Texture("bomb.png"); //On cree nos textures
    public static final Texture BACKGROUND_TEXTURE = new Texture("background.png"); 
    public static final Texture BACKGROUND_NUMBERS_TEXTURE = new Texture("background_numbers.png"); 
    public static final Texture BACKGROUND_HIDE_TEXTURE = new Texture("background_hide.png"); 
    public static final AnimatedSprite FLAG_IDLE = new AnimatedSprite("flag_idle.png",3); 
    public static final AnimatedSprite FLAG_POP = new AnimatedSprite("flag_pop.png",9); 

    public Grid(){
        long t1 = new Date().getTime();
        this.aBoxes = new Box[20][20];

        createGrid();

        long t2 = new Date().getTime();
        LOGGER.info("Il faut " + (t2-t1) + "ms pour creer la grille.");
    }

    public void render(Window pWindow){
        for (Box[] boxes : aBoxes) {
            for (Box box : boxes) {
                box.render();
            }
        }
        pWindow.changeSize(30*this.aBoxes.length, 30*this.aBoxes[0].length);
    }

    /**
     * Permet de creer la grille
     */
    public void createGrid(){
        this.aInteractive = true;

        int xBox = 0 ;
        int yBox = 0 ;
        for (int x = 0; x < aBoxes.length; x++) {
            for (int y = 0; y < aBoxes[0].length; y++) {
                this.aBoxes[x][y] = new Box(false,xBox,yBox,30);
                yBox += 30;
            }
            yBox = 0;
            xBox += 30;
        }

        this.aBombs = new Box[(int) ((this.aBoxes.length * this.aBoxes[0].length) * 0.16)];

        this.aNumberFlag = this.aBombs.length;

        for (int i = 0; i < this.aBombs.length; i++) {
            int x;
            int y;
            do {
                x =  new Random().nextInt(this.aBoxes.length);
                y =  new Random().nextInt(this.aBoxes[0].length);
            } while (this.aBoxes[x][y].isBomb());
            this.aBoxes[x][y].setBomb(true);
            this.aBombs[i] = this.aBoxes[x][y];
        }

        for (int x = 0; x < aBoxes.length; x++) {
            for (int y = 0; y < aBoxes[0].length; y++) {
                this.aBoxes[x][y].setValue(numberOfNeighbours(new int[]{x,y}));
            }
        }
    }

    /**
     * Retourne le nombre de voisin de la case
     * @param pPos
     * @return
     */
    private int numberOfNeighbours(int[] pPos){
        int vX = pPos[0];
        int vY = pPos[1];
        int vReturn = 0;

        if(this.aBoxes[vX][vY].isBomb()) return 0; // Si la case actuelle est une bombe on return 0

        if(vX + 1 < this.aBoxes.length ) vReturn += this.aBoxes[vX+1][vY].isBomb() ? 1 : 0; 
        if(vX + 1 < this.aBoxes.length && vY + 1 < this.aBoxes[0].length) vReturn += this.aBoxes[vX+1][vY+1].isBomb() ? 1 : 0; 
        if(vX + 1 < this.aBoxes.length && vY - 1 >= 0) vReturn += this.aBoxes[vX+1][vY-1].isBomb() ? 1 : 0; 
        if(vY + 1 < aBoxes[0].length) vReturn += this.aBoxes[vX][vY+1].isBomb() ? 1 : 0; 
        if(vY - 1 >= 0) vReturn += this.aBoxes[vX][vY-1].isBomb() ? 1 : 0; 
        if(vX - 1 >= 0) vReturn += this.aBoxes[vX-1][vY].isBomb() ? 1 : 0; 
        if(vX - 1 >= 0 && vY + 1 < aBoxes[0].length) vReturn += this.aBoxes[vX-1][vY+1].isBomb() ? 1 : 0; 
        if(vX - 1 >= 0 && vY - 1 >= 0) vReturn += this.aBoxes[vX-1][vY-1].isBomb() ? 1 : 0; 

        return vReturn;
    }

    /**
     * Permet d'afficher la case ou la souris est
     * Coordonee de sur le contexte OpenGl
     * @param pX 
     * @param pY
     */
    public void discoverClicked(double pX, double pY){
        if(!(this.aInteractive)) return;
        long t1 = new Date().getTime();
        for (Box[] boxes : aBoxes) {
            for (Box box : boxes) {
                if(box.isContain(pX,pY)){
                    if(box.isDiscover()) return; //Si la case est decouverte on ne fait rien
                    int[] vPos = box.getPosition();
                    discoverNeighbours(vPos[0]/30, vPos[1]/30,0); //On divise par la taille pour avoir l'indice de la box dans la matrice
                    box.setDiscover(this);
                }
            }
        }
        long t2 = new Date().getTime();
        LOGGER.info("Il faut {}ms pour effectuer la decouverte de la grille.",(t2-t1));
    }

    /**
     * Action de clique gauche
     * @param pX
     * @param pY
     * @param i Profondeur de la recurssion
     */
    private void discoverNeighbours(int pX,int pY,int i){

        if(this.aBoxes[pX][pY].isBomb() || this.aBoxes[pX][pY].isDiscover()) return; // Si la case est une bombe ou est deja decouverte on arrete

        LOGGER.debug(i);

        this.aBoxes[pX][pY].setDiscover(this);//On decouvre la case actuel

        if(this.aBoxes[pX][pY].getValue() != 0) return;//Si la case est une valeur on arrete

        if(pX + 1 < this.aBoxes.length){
            discoverNeighbours(pX+1,pY,i++);
        } 
        if(pX + 1 < this.aBoxes.length && pY + 1 < this.aBoxes[0].length){
            discoverNeighbours(pX+1,pY+1,i++); 
        } 
        if(pX + 1 < this.aBoxes.length && pY - 1 >= 0){
            discoverNeighbours(pX+1,pY-1,i++);
        } 
        if(pY + 1 < aBoxes[0].length) {
            discoverNeighbours(pX,pY+1,i++);
        }
        if(pY - 1 >= 0){
            discoverNeighbours(pX,pY-1,i++);
        } 
        if(pX - 1 >= 0){
            discoverNeighbours(pX-1,pY,i++);
        } 
        if(pX - 1 >= 0 && pY + 1 < aBoxes[0].length){ 
            discoverNeighbours(pX-1,pY+1,i++);
        } 
        if(pX - 1 >= 0 && pY - 1 >= 0){
            discoverNeighbours(pX-1,pY-1,i++);
        }
    }

    /**
     * Permet de place un drapeau
     * @param pX
     * @param pY
     */
    public void placeFlag(double pX, double pY){

        for (Box[] boxes : aBoxes) {
            for (Box box : boxes) {
                if(box.isContain(pX,pY)){
                    if(box.isDiscover()) return;//Si decouverte on ne fait rien
                    if(this.aNumberFlag == 0 && !(box.isFlag())) return;//Si plus de drapeau on return et que on veut ne placer 1
                    box.flag(this);
                    LOGGER.debug(this.aNumberFlag);
                    return;
                }
            }
        }
    }

    public void addFlag(){
        this.aNumberFlag++;
    }

    public void removeFlag(){
        this.aNumberFlag--;
    }

    public boolean checkWin(){
        for (Box box : aBombs) {
            if(!box.isFlag()) return false;
        }
        this.aInteractive = false;
        LOGGER.info("You win");
        return true;
    }

    public boolean checklose(){
        for (Box box : aBombs) {
            if(box.isDiscover()){
                this.aInteractive = false;
                LOGGER.info("You lose");
                return true;
            } 
        }
        return false;
    }
}
