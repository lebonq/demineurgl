package fr.lebonq.demineurgl.engine;

import java.util.Date;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import fr.lebonq.demineurgl.gamelogic.GameLogic;

public class Engine {
    private static final Logger LOGGER = LogManager.getLogger();
    private Window aWindow;
    private GameLogic aGameLogic;
    private Input aInput;
    private double aSecPerUpdate;// En seconde
    private double aSecPerFrame; // En seconde
    public static int aFramerate; // Fps
    private int aUpdaterate; // Ups

    public Engine(GameLogic pLogic, int pFps, int pUps) {
        this.aFramerate = pFps;
        this.aUpdaterate = pUps;
        this.aSecPerFrame = 1.0d / this.aFramerate;
        this.aSecPerUpdate = 1.0d / this.aUpdaterate;
        this.aGameLogic = pLogic;
        this.aInput = new Input();
        this.aWindow = new Window(800, 600, "DemineurGL",this.aInput);
    }

    /**
     * Launch the game
     */
    public void run() {
        try {
            this.init();
        } catch (Exception e) {
            e.printStackTrace();
        }
        this.gameLoop();
    }

    /**
     * Initalise le moteurs
     */
    private void init() throws Exception{
        LOGGER.log(Level.INFO, "Engine initalization !");
        this.aWindow.init();
        this.aGameLogic.init(this.aWindow);
    }

    /**
     * Boucle de jeu
     */
    private void gameLoop(){
		double vPrevious = new Date().getTime();
 		double vSteps = 0d;
		while(!this.aWindow.shouldClose()){
			long vTimeBeforeRender = new Date().getTime();//On recupere la date avant le rendu
			double vElapsed = vTimeBeforeRender - vPrevious;
			vPrevious = vTimeBeforeRender;
			vSteps += vElapsed;
			
			while(vSteps >= this.aSecPerUpdate*1000){
				this.update();
				vSteps -= this.aSecPerUpdate*1000;
			}

			this.render();

			this.sync(vTimeBeforeRender);// Permet d'attendre le temps necessaire avant le rendu de l'autre image
		}
    }
    
    /**
	 * Permet de limiter la framerate
	 * @param pStartTime date de duree avant le rendu de la frame et l'updateS
	 */
	private void sync(long pStartTime){
		double vEndTime = pStartTime + this.aSecPerFrame*1000;
		while(vEndTime > new Date().getTime()){
			try{
				Thread.sleep(1);
			}
			catch(Exception pE){
				LOGGER.log(Level.ERROR, pE);
			}
		}
	}

    private void render() {
        this.aWindow.render();
        this.aGameLogic.render(this.aWindow);
    }

    private void update() {
        this.aWindow.update();
        this.aGameLogic.input(this.aInput);
        this.aGameLogic.update();
    }
}
