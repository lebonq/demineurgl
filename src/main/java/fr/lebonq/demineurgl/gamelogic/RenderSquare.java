package fr.lebonq.demineurgl.gamelogic;

import static org.lwjgl.opengl.GL11.GL_BLEND;
import static org.lwjgl.opengl.GL11.GL_ONE_MINUS_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.GL_QUADS;
import static org.lwjgl.opengl.GL11.GL_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;

import org.lwjgl.opengl.GL11;

import fr.lebonq.demineurgl.engine.graphics.Texture;

/**
 * Util classe qui permet de faire un carre
 */
public class RenderSquare {

    private RenderSquare(){

    }
    
    /**
     * Permet de faire le rendu custom d'un carre
     * @param pX Position X en haut a gauche du carre
     * @param pY Position Y en haut a gauche du carre
     * @param pR
     * @param pG
     * @param pB
     * @param pSize Taille du carre
     */
    public static void drawSquareNoTexture(int pX, int pY, float pR, float pG, float pB, int pSize){
        GL11.glBegin(GL_QUADS);
        GL11.glColor4d(pR,pG,pB,0.0f);//On defini la couleur
        GL11.glVertex2i(pX, pY);//On defini chaque point
        GL11.glVertex2i(pX+pSize, pY);
        GL11.glVertex2i(pX+pSize, pY+pSize);
        GL11.glVertex2i(pX, pY+pSize);
        GL11.glEnd();
    }

    public static void drawSquareTexture(int pX, int pY, int pWidth, int pHeight,Texture pTexture){
        GL11.glEnable(GL_TEXTURE_2D);//TO show 2d texture
        //Pour la transparence
        GL11.glEnable(GL_BLEND);
        GL11.glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);

        pTexture.bind(); // On bind la texture

        GL11.glBegin(GL_QUADS);
        GL11.glTexCoord2f(0, 0);
        GL11.glVertex2i(pX, pY);//On defini chaque point
        GL11.glTexCoord2f(1, 0);
        GL11.glVertex2i(pX+pWidth, pY);
        GL11.glTexCoord2f(1, 1);
        GL11.glVertex2i(pX+pWidth, pY+pHeight);
        GL11.glTexCoord2f(0, 1);
        GL11.glVertex2i(pX, pY+pHeight);
        GL11.glEnd();

        GL11.glDisable(GL_BLEND);
        GL11.glDisable(GL_TEXTURE_2D);
    }

    public static void drawSquareTextureWithSpecifiCoord(int pX, int pY, int pWidth, int pHeight,Texture pTexture,double[][] pCoord){
        GL11.glEnable(GL_TEXTURE_2D);//TO show 2d texture
        //Pour la transparence
        GL11.glEnable(GL_BLEND);
        GL11.glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);

        pTexture.bind(); // On bind la texture

        GL11.glBegin(GL_QUADS);
        GL11.glTexCoord2d(pCoord[0][0], pCoord[0][1]);
        GL11.glVertex2i(pX, pY);//On defini chaque point
        GL11.glTexCoord2d(pCoord[1][0], pCoord[1][1]);
        GL11.glVertex2i(pX+pWidth, pY);
        GL11.glTexCoord2d(pCoord[2][0], pCoord[2][1]);
        GL11.glVertex2i(pX+pWidth, pY+pHeight);
        GL11.glTexCoord2d(pCoord[3][0], pCoord[3][1]);
        GL11.glVertex2i(pX, pY+pHeight);
        GL11.glEnd();

        GL11.glDisable(GL_BLEND);
        GL11.glDisable(GL_TEXTURE_2D);
    }
}
