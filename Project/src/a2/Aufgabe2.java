package a2;

import static org.lwjgl.opengl.GL30.*;

import lenz.opengl.AbstractOpenGLBase;
import lenz.opengl.ShaderProgram;

public class Aufgabe2 extends AbstractOpenGLBase {

	public static void main(String[] args) {
		new Aufgabe2().start("CG Aufgabe 2", 2000, 2000);
	}

	@Override
	protected void init() {
		// folgende Zeile laed automatisch "aufgabe2_v.glsl" (vertex) und "aufgabe2_f.glsl" (fragment)
		ShaderProgram shaderProgram = new ShaderProgram("aufgabe2");
		glUseProgram(shaderProgram.getId());

		// Koordinaten, VAO, VBO, ... hier anlegen und im Grafikspeicher ablegen
        float [] triangles = new float[]{
                -0.5f, -0.5f, //Dreieck 0
                -0.5f, 0.45f,
                0.45f, -0.5f,

                0.5f, 0.5f, //Dreieck 1
                0.5f, -0.45f,
                -0.45f, 0.5f
            };

        float[] colors = new float[]{
            // Triangle 0
            1.0f, 0.0f, 0.0f, // vertex 0 color
            0.0f, 1.0f, 0.0f, // vertex 1 color
            0.0f, 0.0f, 1.0f, // vertex 2 color
            // Triangle 1
            1.0f, 1.0f, 0.0f, // vertex 3 color
            0.0f, 1.0f, 1.0f, // vertex 4 color
            1.0f, 0.0f, 1.0f  // vertex 5 color
        };

        int vaoId = glGenVertexArrays();
        glBindVertexArray(vaoId); // select first VAO

        // transfer coordinates to gpu
        int vboId = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, vboId);
        glBufferData(GL_ARRAY_BUFFER, triangles, GL_STATIC_DRAW);
        glVertexAttribPointer(0, 2, GL_FLOAT, false, 0, 0);
        glEnableVertexAttribArray(0);

        // transfer colors to gpu
        int colorVboId = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, colorVboId);
        glBufferData(GL_ARRAY_BUFFER, colors, GL_STATIC_DRAW);
        glVertexAttribPointer(1, 3, GL_FLOAT, false, 0, 0);
        glEnableVertexAttribArray(1);

	}

	@Override
	public void update() {
	}

	@Override
	protected void render() {
		glClear(GL_COLOR_BUFFER_BIT); // Zeichenflaeche leeren

		// hier vorher erzeugte VAOs zeichnen
        glDrawArrays(GL_TRIANGLES, 0, 6);
	}
}
