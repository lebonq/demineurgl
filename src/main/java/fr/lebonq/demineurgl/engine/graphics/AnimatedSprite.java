package fr.lebonq.demineurgl.engine.graphics;

/**
 * Contient un tableau 3D avec toute les coords de l'animation
 *  Ex aCoord[2][2][1] 3eme frame, 3eme point, en Y
 *  Ex aCoord[15][3][0] 16eme frame, 4eme point, en X
 */
public class AnimatedSprite extends Texture {
    private double aFrame;
    private double[][][] aCoord;

    public AnimatedSprite(String pFileName, double pFrame){
        super(pFileName);
        this.aFrame = pFrame;
        this.aCoord = new double[(int)this.aFrame][4][2];
        for(int i = 0; i < this.aFrame; i++){
            //All x coord for the texture
            this.aCoord[i][0][0] = (i)/this.aFrame;
            this.aCoord[i][1][0] = (i+1)/this.aFrame;
            this.aCoord[i][2][0] = (i+1)/this.aFrame;
            this.aCoord[i][3][0] = (i)/this.aFrame;
            //All y coord for the texture
            this.aCoord[i][0][1] = 0d;
            this.aCoord[i][1][1] = 0d;
            this.aCoord[i][2][1] = 1d;
            this.aCoord[i][3][1] = 1d;
        }

    }

    public double[][] getCoordFrame(int pFrame){
        return this.aCoord[pFrame];
    }

    /**
     * On return le nombre de frame -1
     * @return
     */
    public int getFrame(){
        return (int)this.aFrame - 1;
    }

}
