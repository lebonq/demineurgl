package fr.lebonq.demineurgl.engine;

import org.lwjgl.glfw.GLFWCursorPosCallback;
import org.lwjgl.glfw.GLFWKeyCallback;
import org.lwjgl.glfw.GLFWMouseButtonCallback;
import  static org.lwjgl.glfw.GLFW.GLFW_RELEASE;

public class Input {

    private GLFWKeyCallback aKeyCallback;
    private GLFWCursorPosCallback aPosCallback;
    private GLFWMouseButtonCallback aMouseButtonCallback;

    /**
     * X at 0, Y at 1
     */
    private double[] aMousePos;

    /**
     * Key code at 0, Action at 1
     */
    private int[] aKeyData;

    private int[] aMouseBoutton;
    
    public Input(){
            this.aKeyData = new int[2];
            this.aMousePos = new double[2];
            this.aMouseBoutton = new int[2];
            this.aMouseBoutton[0] = -1;//valeur par defaut
            this.aKeyCallback = new GLFWKeyCallback(){
            
                @Override
                public void invoke(long pWindow, int pKey, int pScancode, int pAction, int pMods) {
                    aKeyData[0] = pKey;
                    aKeyData[1] = pAction;
                    if(pAction == GLFW_RELEASE){
                        aKeyData[0] = 0;
                    }
                }
            };

            this.aPosCallback = new GLFWCursorPosCallback(){
            
                @Override
                public void invoke(long pWindow, double pXpos, double pYpos) {
                    aMousePos[0] = pXpos;
                    aMousePos[1] = pYpos;
                }
            };

            this.aMouseButtonCallback = new GLFWMouseButtonCallback(){
            
                @Override
                public void invoke(long pWindow, int pButton, int pAction, int pMods) {
                    aMouseBoutton[0] = pButton;
                    aMouseBoutton[1] = pAction;
                    if(pAction == GLFW_RELEASE){
                        aMouseBoutton[0] = -1;
                    }
                }
            };
    }  
    
    //Differensa accesseur


    public GLFWKeyCallback getKeyCallback(){
        return this.aKeyCallback;
    }

    public GLFWCursorPosCallback getPosCallback(){
        return this.aPosCallback;
    }

    public GLFWMouseButtonCallback getMouseButtonCallback(){
        return this.aMouseButtonCallback;
    }

    public int[] getKeyData(){
        return this.aKeyData;
    }

    public double[] getMousePos(){
        return this.aMousePos;
    }

    public int[] getMouseButton(){
        return this.aMouseBoutton;
    }
}