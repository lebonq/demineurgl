package fr.lebonq.demineurgl;

import fr.lebonq.demineurgl.engine.Engine;
import fr.lebonq.demineurgl.gamelogic.GameLogic;

public class App {
    public static void main( String[] args )
    {
        GameLogic vLogic = new GameLogic();
        Engine vEngine = new Engine(vLogic,60,60);
        vEngine.run();
    }
}
