package fr.lebonq.demineurgl.engine.graphics;

import static org.lwjgl.opengl.GL11.GL_NEAREST;
import static org.lwjgl.opengl.GL11.GL_RGBA;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_MAG_FILTER;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_MIN_FILTER;
import static org.lwjgl.opengl.GL11.GL_UNPACK_ALIGNMENT;
import static org.lwjgl.opengl.GL11.GL_UNSIGNED_BYTE;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.opengl.GL11;
import org.lwjgl.stb.STBImage;
import org.lwjgl.system.MemoryStack;

public class Texture {
    private int aId;
    private int aWidth;
    private int aHeight;
    private static final Logger LOGGER = LogManager.getLogger();

    public Texture(String pFileName){
        
        ByteBuffer vImagBuffer;
        // Load Texture file
        try (MemoryStack stack = MemoryStack.stackPush()) {
            IntBuffer w = stack.mallocInt(1);
            IntBuffer h = stack.mallocInt(1);
            IntBuffer channels = stack.mallocInt(1);

            vImagBuffer = STBImage.stbi_load(pFileName, w, h, channels, 4);
            if (vImagBuffer == null) {
                LOGGER.error("Image file [{}] not loaded: {}",pFileName,STBImage.stbi_failure_reason());
            }
    
            this.aWidth = w.get();
            this.aHeight = h.get();
        }

        // Create a new OpenGL texture 
        this.aId = GL11.glGenTextures();
        // Bind the texture
        GL11.glBindTexture(GL_TEXTURE_2D, this.aId);

        GL11.glPixelStorei(GL_UNPACK_ALIGNMENT, 1);

        //Permet de scale la texture avec lobjet
        //GL11.glGenerateMipmap(GL_TEXTURE_2D);
        GL11.glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
        GL11.glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);

        //Upload our texture
        GL11.glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA,  this.aWidth,  this.aHeight, 0, GL_RGBA, GL_UNSIGNED_BYTE, vImagBuffer);

        //Free Menory
        STBImage.stbi_image_free(vImagBuffer);

    }

    public void bind(){
        GL11.glBindTexture(GL_TEXTURE_2D, this.aId);
    }
}
