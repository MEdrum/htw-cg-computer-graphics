package projekt;

import static org.lwjgl.opengl.GL30.*;

import lenz.opengl.AbstractOpenGLBase;
import lenz.opengl.ShaderProgram;

public class Projekt extends AbstractOpenGLBase {

	private ShaderProgram shaderProgram;
    private Matrix4 transfromationMatrix = new  Matrix4();
    private Matrix4 projectionMatrix = new  Matrix4();

	public static void main(String[] args) {
        new Projekt().start("CG Projekt", 2000, 2000);
	}

	@Override
	protected void init() {
		shaderProgram = new ShaderProgram("projekt");
		glUseProgram(shaderProgram.getId());

		// Koordinaten, VAO, VBO, ... hier anlegen und im Grafikspeicher ablegen
        float [] triangles = new float[]{
                -0.5f, -0.5f, //Dreieck 0
                0.45f, -0.5f,
                -0.5f, 0.45f,


                0.5f, 0.5f, //Dreieck 1
                -0.45f, 0.5f,
                0.5f, -0.45f

        };

        float[] colors = new float[]{
                // Triangle 0
                1.0f, 0.0f, 0.0f, // vertex 0 color
                0.0f, 0.0f, 1.0f, // vertex 1 color
                0.0f, 1.0f, 0.0f, // vertex 2 color

                // Triangle 1
                1.0f, 1.0f, 0.0f, // vertex 3 color
                1.0f, 0.0f, 1.0f, // vertex 4 color
                0.0f, 1.0f, 1.0f  // vertex 5 color

        };

        int vaoId = glGenVertexArrays();
        glBindVertexArray(vaoId); // select first VAO

        connect_vbo(triangles, 0, 2);
        connect_vbo(colors, 1, 3);

        // TODO: transfer projection matrix as uniform to opengl

		glEnable(GL_DEPTH_TEST); // z-Buffer aktivieren
		glEnable(GL_CULL_FACE); // backface culling aktivieren
	}

    private void connect_vbo(float[] vbo, int buffer_index, int element_size){
        int vboId = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, vboId);
        glBufferData(GL_ARRAY_BUFFER, vbo, GL_STATIC_DRAW);
        glVertexAttribPointer(buffer_index, element_size, GL_FLOAT, false, 0, 0); // connect VBO
        glEnableVertexAttribArray(buffer_index); // activate VBO
    }

	@Override
	public void update() {
		// TODO: Transformation durchfuehren (Matrix anpassen)
        this.transfromationMatrix.rotateZ(0.001f);

	}

	@Override
	protected void render() {
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

        // TODO: Matrix an Shader uebertragen
        int transfromationMatrixHandle = glGetUniformLocation(shaderProgram.getId(), "transformationMatrix");
        glUniformMatrix4fv(transfromationMatrixHandle, false, this.transfromationMatrix.getValuesAsArray());



		// VAOs zeichnen
        glDrawArrays(GL_TRIANGLES, 0, 6);
	}
}
