package fr.lebonq.demineurgl.gamelogic.ui;

import fr.lebonq.demineurgl.engine.Window;


public class Ui {
    private Widget aWin;
    private Widget aLose;
    private Window aWindow;
    private Button aRestart;

    public Ui(Window pWindow){
        this.aWindow = pWindow;
        this.aWin = new Widget("win.png", this.aWindow, 0.10, 0.20, 0.15, 0.80);
        this.aLose = new Widget("lose.png", this.aWindow, 0.10, 0.20, 0.15, 0.80);
        this.aRestart =  new Button("button.png", "button_hover.png", "button_push.png",0.25,0.35,0.1,0.50, this.aWindow);
    }

    public void renderWin(){
        this.aWin.render();
        renderButton();
    }

    public void renderLose(){
        this.aLose.render();
        renderButton();
    }

    public void renderButton(){
        this.aRestart.render();
    }

    public void update(double pX, double pY,int pMouseData, int pMouseAction){
        this.aWin.update(this.aWindow);
        this.aLose.update(this.aWindow);
        this.aRestart.update(this.aWindow, pX, pY, pMouseData, pMouseAction);
    }

    public Button getButtonRestart(){
        return this.aRestart;
    }
}
