package fr.lebonq.demineurgl.engine;

import static org.lwjgl.opengl.GL11.GL_PROJECTION;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.glfw.GLFWWindowSizeCallback;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;

public class Window {
    private int aWidth;
    private int aHeight;
    private String aName;
    private long aWindowInstance;
    private GLFWVidMode aPrimaryScreen;
    private boolean isResized;
    private Input aInput;

    public Window(int pWidth, int pHeight, String pName, Input pInput) {
        this.aWidth = pWidth;
        this.aHeight = pHeight;
        this.aName = pName;
        this.aInput = pInput;
    }

    public void init(){
        if (!GLFW.glfwInit()) { // On regarde si GLFW a pu sinitialiser
            throw new IllegalStateException("Unable to initialize GLFW"); // message d'erreur
        }
        this.aWindowInstance = GLFW.glfwCreateWindow(this.aWidth, this.aHeight, this.aName, 0, 0);//On cree l'instance de la fenetre

        //Icon

        this.aPrimaryScreen = GLFW.glfwGetVideoMode(GLFW.glfwGetPrimaryMonitor()); //On get le moniteur ou s'affiche la fenetre
        GLFW.glfwSetWindowPos(this.aWindowInstance, (this.aPrimaryScreen.width() - this.aWidth) / 2,(this.aPrimaryScreen.height() - this.aHeight) / 2); // On met la fenetre au centre
        GLFW.glfwSetMouseButtonCallback(this.aWindowInstance, this.aInput.getMouseButtonCallback());
        GLFW.glfwSetCursorPosCallback(this.aWindowInstance, this.aInput.getPosCallback());
        GLFW.glfwSetKeyCallback(this.aWindowInstance, this.aInput.getKeyCallback());
        GLFW.glfwSetWindowSizeCallback(this.aWindowInstance, new GLFWWindowSizeCallback() {//Pourrezise la fenetre

            @Override
            public void invoke(final long pWindow, final int pWidth, final int pHeight) {
                aWidth = pWidth;
                aHeight = pHeight;
                GL11.glViewport(0, 0, pWidth, pHeight);
                isResized = true;
            }
        });

        GLFW.glfwMakeContextCurrent(this.aWindowInstance);//On fait de openGL le contexte de la fenetre

        GL.createCapabilities();// Cree les capacitees openGL
        GL11.glMatrixMode(GL_PROJECTION);
        GL11.glLoadIdentity();
        GL11.glOrtho(0, this.aWidth, this.aHeight, 0, 1, -1);
        
        GL11.glClearColor(1.0f, 1.0f, 1.0f, 0.0f);// Met la couleur de fond en blanc
        GLFW.glfwShowWindow(this.aWindowInstance);//On affiche la fentre

    }

    /**
     * Rendu des elements dans le contexte OGL de la fenetre
     */
    public void render(){
        GLFW.glfwSwapBuffers(this.aWindowInstance);
    }

    /**
     * Mettre a jour letat de la fenetre
     */
    public void update(){
        GLFW.glfwPollEvents(); // recupere les events user de opengl
        if (isResized) {
            GL11.glMatrixMode(GL_PROJECTION);
            GL11.glLoadIdentity();
            GL11.glOrtho(0, this.aWidth, this.aHeight, 0, 1, -1);
            isResized = false;
        }
    }

    /**
     * Permet de savoir si on doit fermer la fenetre
     * @return
     */
	public boolean shouldClose() {
		return GLFW.glfwWindowShouldClose(this.aWindowInstance);
    }
    
    public void changeSize(int pWidth, int pHeight){
        this.aHeight = pHeight;
        this.aWidth = pWidth;
        GLFW.glfwSetWindowSize(this.aWindowInstance,pWidth,pHeight);
    }

    public int getWidth(){
        return this.aWidth;
    }

    public int getHeight(){
        return this.aHeight;
    }
}
