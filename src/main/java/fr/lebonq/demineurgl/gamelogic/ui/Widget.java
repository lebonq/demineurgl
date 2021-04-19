package fr.lebonq.demineurgl.gamelogic.ui;

import fr.lebonq.demineurgl.engine.Window;
import static org.lwjgl.opengl.GL11.GL_BLEND;
import static org.lwjgl.opengl.GL11.GL_ONE_MINUS_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.GL_QUADS;
import static org.lwjgl.opengl.GL11.GL_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;

import org.lwjgl.opengl.GL11;
import fr.lebonq.demineurgl.engine.graphics.Texture;

public class Widget {
    protected Texture aBackGround;
    protected Texture aRenderedTexture;//Pour pourvoir modifier la texture de rendu
    protected int aX;
    protected int aY;
    protected int aWidthWindow;
    protected int aHeightWindow;
    protected int aWidth;
    protected int aHeight;

    protected double aXOffset;
    protected double aYOffset;
    protected double aWidthFactor;
    protected double aHeightFactor;

    public Widget(String pTexture, Window pWindow,double pXOffset, double pYOffset, double pHeightFactor, double pWidthFactor){
        this.aBackGround =  new Texture(pTexture);

        this.aXOffset = pXOffset;
        this.aYOffset = pYOffset;
        this.aWidthFactor = pWidthFactor;
        this.aHeightFactor = pHeightFactor;

        calculatePosition(pWindow);
        this.aRenderedTexture = this.aBackGround;
    }

    private void calculatePosition(Window pWindow){
        this.aHeightWindow = pWindow.getHeight();
        this.aWidthWindow = pWindow.getWidth();
        this.aY = (int)(this.aHeightWindow*this.aYOffset);
        this.aX = (int)(this.aWidthWindow*this.aXOffset);
        this.aWidth = (int)(this.aWidthWindow*this.aWidthFactor);
        this.aHeight = (int)(this.aHeightWindow*this.aHeightFactor);
    }

    public void render(){
        GL11.glEnable(GL_TEXTURE_2D);//TO show 2d texture
        //Pour la transparence
        GL11.glEnable(GL_BLEND);
        GL11.glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);

        this.aRenderedTexture.bind();

        GL11.glBegin(GL_QUADS);
        GL11.glTexCoord2f(0, 0);
        GL11.glVertex2i(this.aX, this.aY);//On defini chaque point
        GL11.glTexCoord2f(1, 0);
        GL11.glVertex2i(this.aX+this.aWidth, this.aY);
        GL11.glTexCoord2f(1, 1);
        GL11.glVertex2i(this.aX+this.aWidth, this.aY+this.aHeight);
        GL11.glTexCoord2f(0, 1);
        GL11.glVertex2i(this.aX, this.aY+this.aHeight);
        GL11.glEnd();

        GL11.glDisable(GL_BLEND);
        GL11.glDisable(GL_TEXTURE_2D);
    }

    public void update(Window pWindow){
        calculatePosition(pWindow);
    }
}