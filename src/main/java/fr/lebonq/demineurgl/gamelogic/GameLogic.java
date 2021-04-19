package fr.lebonq.demineurgl.gamelogic;

import static org.lwjgl.glfw.GLFW.GLFW_KEY_R;
import static org.lwjgl.glfw.GLFW.GLFW_MOUSE_BUTTON_LEFT;
import static org.lwjgl.glfw.GLFW.GLFW_MOUSE_BUTTON_RIGHT;
import static org.lwjgl.glfw.GLFW.GLFW_PRESS;
import static org.lwjgl.glfw.GLFW.GLFW_RELEASE;
import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_DEPTH_BUFFER_BIT;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.opengl.GL11;

import fr.lebonq.demineurgl.engine.Input;
import fr.lebonq.demineurgl.engine.Window;
import fr.lebonq.demineurgl.gamelogic.grid.Grid;
import fr.lebonq.demineurgl.gamelogic.ui.Ui;

public class GameLogic {
    private static final Logger LOGGER = LogManager.getLogger();
    private Grid aGrid;
    private Ui aUi;
    private double aMousePosX;
    private double aMousePosY;
    private int aMouseButtonData;
    private int aKeyData;
    private int aKeyAction;
    private int aMouseButtonAction;
    private boolean aTriggerLeft;
    private boolean aTriggerRight;
    private boolean aTriggerKey;
    private boolean aWin;
    private boolean aLose;


    public void init(final Window pWindow){
        LOGGER.info("Gamelogic initialization !");
        this.aGrid = new Grid();
        this.aTriggerLeft = false;
        this.aTriggerRight = false;
        this.aTriggerKey = false;
        this.aKeyData = 0;
        this.aKeyAction = 0;
        this.aUi = new Ui(pWindow);
    }

    public void input(Input pInput){
        this.aMousePosX = pInput.getMousePos()[0];
        this.aMousePosY = pInput.getMousePos()[1];
        this.aMouseButtonData = pInput.getMouseButton()[0];
        this.aMouseButtonAction = pInput.getMouseButton()[1];
        this.aKeyData = pInput.getKeyData()[0];
        this.aKeyAction = pInput.getKeyData()[1];
    }
     
    public void update() {
        if(this.aWin || this.aLose){
            this.aUi.update(this.aMousePosX, this.aMousePosY, this.aMouseButtonData,this.aMouseButtonAction);
            this.aUi.getButtonRestart().setActive(true);
        }

        //Action au click gauche
        if(this.aMouseButtonData == GLFW_MOUSE_BUTTON_LEFT && this.aMouseButtonAction == GLFW_PRESS && !(this.aTriggerLeft) ){ 

            this.aGrid.discoverClicked( this.aMousePosX, this.aMousePosY); //Découverte d'une case

            if(this.aUi.getButtonRestart().getButtonState() && this.aUi.getButtonRestart().isContain(this.aMousePosX, this.aMousePosY)){ //action sur 
                this.aGrid.createGrid();
                //this.aUi.update(this.aMousePosX, this.aMousePosY, this.aMouseButtonData,this.aMouseButtonAction);
                this.aWin = false;
                this.aLose= false;
                this.aUi.getButtonRestart().setActive(false);
            }

            this.aTriggerLeft = true;
        }

        //Action au click droit
        if(this.aMouseButtonData == GLFW_MOUSE_BUTTON_RIGHT && this.aMouseButtonAction == GLFW_PRESS && !(this.aTriggerRight)){
            this.aGrid.placeFlag( this.aMousePosX, this.aMousePosY);
            this.aTriggerRight = true;
        }

        if(this.aKeyData == GLFW_KEY_R && !(this.aTriggerKey)){
            LOGGER.info("Restart game");
            this.aTriggerKey = true;
            this.aGrid.createGrid();
        }

        //Permet de faire click par click
        if(this.aMouseButtonAction == GLFW_RELEASE){
            this.aTriggerLeft = false;
            this.aTriggerRight = false;
        }

        if(this.aKeyAction == GLFW_RELEASE){
            this.aTriggerKey = false;
        }

        //Win or lose
        this.aWin = this.aGrid.checkWin();
        this.aLose= this.aGrid.checklose();
    }

    public void render(final Window pWindow) {
        GL11.glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
        this.aGrid.render(pWindow);
        if(this.aWin)this.aUi.renderWin();
        if(this.aLose)this.aUi.renderLose();
    }

}
