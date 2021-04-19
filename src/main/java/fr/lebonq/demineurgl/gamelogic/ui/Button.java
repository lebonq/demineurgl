package fr.lebonq.demineurgl.gamelogic.ui;

import fr.lebonq.demineurgl.engine.Window;
import fr.lebonq.demineurgl.engine.graphics.Texture;
import static org.lwjgl.glfw.GLFW.GLFW_PRESS;
import static org.lwjgl.glfw.GLFW.GLFW_MOUSE_BUTTON_LEFT;

public class Button extends Widget {

    private Texture aPush;
    private boolean aPushed;
    private Texture aHover;
    private boolean aHovered;

    private boolean aActive; //Button est actif et afficher à l'écran

    /**
     * 
     * @param pTextureRegular
     * @param pTextureHover
     * @param pTexturePush
     * @param pXOffset
     * @param pYOffset
     * @param pHeightFactor
     * @param pWidthFactor
     * @param pWindow
     */
    public Button(String pTextureRegular, String pTextureHover, String pTexturePush,double pXOffset, double pYOffset, double pHeightFactor, double pWidthFactor, Window pWindow) {
        super(pTextureRegular, pWindow,pXOffset, pYOffset,pHeightFactor,pWidthFactor);
        this.aHover =  new Texture(pTextureHover);
        this.aPush =  new Texture(pTexturePush);

    }

    public void update(Window pWindow, double pX, double pY, int pMouseData, int pMouseAction){
        super.update(pWindow);
        this.aHovered = isContain(pX, pY);
        
        this.aPushed = (pMouseData == GLFW_MOUSE_BUTTON_LEFT && pMouseAction == GLFW_PRESS &&  this.aHovered);

        if(this.aPushed) this.aRenderedTexture = this.aPush;
        else if(this.aHovered) this.aRenderedTexture = this.aHover;
        else this.aRenderedTexture = this.aBackGround;
    }
    
    public boolean isContain(double pX, double pY){
        return pX >= this.aX && pX < this.aX+this.aWidth 
            && pY >= this.aY && pY < this.aY+this.aHeight;
    }

    public boolean getButtonState(){
        return this.aActive;
    }

    public void setActive(boolean pActive){
        this.aActive = pActive;
    }
}
