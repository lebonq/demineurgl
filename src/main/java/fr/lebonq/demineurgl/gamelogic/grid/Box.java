package fr.lebonq.demineurgl.gamelogic.grid;

import fr.lebonq.demineurgl.engine.Engine;
import fr.lebonq.demineurgl.gamelogic.RenderSquare;

public class Box {
    private boolean aDiscover;
    private boolean aBomb;
    private boolean aFlaged;
    private int aValue;
    private int aX;
    private int aY;

    //For render
    private int aSize;
    private int aFlagAnimState;
    private int aTickCounter;
    private int aFlagPlaceAnimState;
    private int aDirectionAnimPlaceFlag;
    private boolean aAnimationFlagPopFinish;

    public Box(boolean pBomb, int pX, int pY, int pSize){
        this.aBomb = pBomb;
        this.aDiscover = false;

        this.aX = pX;
        this.aY = pY;

        this.aSize = pSize;
        this.aDirectionAnimPlaceFlag = -1;
        this.aFlagPlaceAnimState = 0;
        this.aAnimationFlagPopFinish = true;
    }

    /**
     * Permet de faire le rendu de la box
     */
    public void render(){
        //Update animation and tick counter
        if(this.aTickCounter%(Engine.aFramerate*0.15) == 0){
            if(this.aFlagAnimState == Grid.FLAG_IDLE.getFrame()) this.aFlagAnimState = 0;
            else this.aFlagAnimState++;

            if(this.aFlagPlaceAnimState != 0 && this.aDirectionAnimPlaceFlag < 0){
                this.aFlagPlaceAnimState += this.aDirectionAnimPlaceFlag;
            }
            else if(this.aFlagPlaceAnimState != 8 && this.aDirectionAnimPlaceFlag > 0){
                this.aFlagPlaceAnimState += this.aDirectionAnimPlaceFlag;
            }
            else{
                this.aAnimationFlagPopFinish = true;
            }
        }

        this.aTickCounter = this.aTickCounter == Engine.aFramerate ? 0 : this.aTickCounter+1;

        //Rendering
        if(!(this.aDiscover)){
            RenderSquare.drawSquareTexture(this.aX, this.aY, this.aSize,this.aSize, Grid.BACKGROUND_HIDE_TEXTURE);
            if(this.aFlaged && this.aAnimationFlagPopFinish){
                double[][] vCoord = Grid.FLAG_IDLE.getCoordFrame(this.aFlagAnimState);
                RenderSquare.drawSquareTextureWithSpecifiCoord(this.aX+7, this.aY+2, 18,24, Grid.FLAG_IDLE, vCoord);
            }
            if(!(this.aAnimationFlagPopFinish)){
                double[][] vCoord = Grid.FLAG_POP.getCoordFrame(Math.abs(this.aFlagPlaceAnimState));
                RenderSquare.drawSquareTextureWithSpecifiCoord(this.aX+7, this.aY+2, 18,24, Grid.FLAG_POP, vCoord);
            }
            return;
        }
        else{
            RenderSquare.drawSquareTexture(this.aX, this.aY, this.aSize,this.aSize, Grid.BACKGROUND_TEXTURE);
        }

        if(!(this.aBomb) && this.aValue != 0){
            double[][] vCoord = new double[4][2];
            //All x coord for the texture
            vCoord[0][0] = (aValue-1)/8d;
            vCoord[1][0] = (aValue)/8d;
            vCoord[2][0] = (aValue)/8d;
            vCoord[3][0] = (aValue-1)/8d;
            //All y coord for the texture
            vCoord[0][1] = 0d;
            vCoord[1][1] = 0d;
            vCoord[2][1] = 1d;
            vCoord[3][1] = 1d;
            RenderSquare.drawSquareTextureWithSpecifiCoord(this.aX, this.aY, this.aSize,this.aSize, Grid.BACKGROUND_NUMBERS_TEXTURE, vCoord);            
        }
        else if(this.aBomb) RenderSquare.drawSquareTexture(this.aX, this.aY, this.aSize,this.aSize, Grid.BOMB_TEXTURE);
    }

    /**
     * Permet de savoir si les coordonees sont dans la box
     * @param pX
     * @param pY
     * @return
     */
    public boolean isContain(double pX, double pY){
        return pX >= this.aX && pX < this.aX+this.aSize 
            && pY >= this.aY && pY < this.aY+this.aSize;
    }

    public boolean isDiscover(){
        return this.aDiscover;
    }

    public void setDiscover(Grid pGrid){
        if(this.aDiscover) return;
        this.aDiscover = true;
        if(this.aFlaged)flag(pGrid);
    }

    public int getValue(){
        return this.aValue;
    }

    public void setValue(int pValue){
        this.aValue = pValue;
    }

    public void setBomb(boolean pBomb){
        this.aBomb = pBomb;
    }
    public boolean isBomb(){
        return  this.aBomb;
    }

    public void flag(Grid pGrid){
        this.aDirectionAnimPlaceFlag = this.aDirectionAnimPlaceFlag*-1;//On inverse la direction de l'animation

        if(this.aDirectionAnimPlaceFlag < 0 && this.aFlagPlaceAnimState == 0){//On verifie que lanimation soit termine et que elle doit monte
            this.aFlagPlaceAnimState = Grid.FLAG_POP.getFrame();
        }

        else if(this.aDirectionAnimPlaceFlag > 0 && this.aFlagPlaceAnimState == Grid.FLAG_POP.getFrame()){ //Pareil mais on descend
            this.aFlagPlaceAnimState = 0;
        }
        
        this.aAnimationFlagPopFinish = false;//On lance l'animation
        this.aFlaged = !(this.aFlaged);

        if(this.aFlaged) pGrid.removeFlag();
        else pGrid.addFlag();
    }

    public boolean isFlag(){
        return this.aFlaged;
    }

    /**
     * Permet d'avoir la position de la box
     * @return int[]{this.aX,this.aY};
     */
    public int[] getPosition(){
        return new int[]{this.aX,this.aY};
    }
}
