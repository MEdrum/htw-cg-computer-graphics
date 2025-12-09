package projekt;

import lenz.opengl.AbstractOpenGLBase;
import lenz.opengl.ShaderProgram;

import static org.lwjgl.opengl.GL30.*;

public class Projekt2 extends AbstractOpenGLBase {

	private ShaderProgram gouraud;
    private ShaderProgram phong;
    private float[] light = new float[]{0.1f, 0.1f, 5.0f, 1f}; // x, y, z, I
    private Matrix4 rotationMatrix = new  Matrix4();
    private Matrix4 translationMatrix = new  Matrix4();
    private Matrix4 scaleMatrix = new  Matrix4();
    private Matrix4 phongObjectMatrix = new  Matrix4();
    private Matrix4 gouraudObjectMatrix = new  Matrix4();
    private Matrix4 projectionMatrix = new  Matrix4(0.1f, 1000);

	public static void main(String[] args) {
        new Projekt2().start("CG Projekt", 2000, 2000);
	}

	@Override
	protected void init() {
		gouraud = new ShaderProgram("project_gouraud");
        phong = new ShaderProgram("project_phong");
		glUseProgram(phong.getId());

        /*
        Tetrahedrons edges:
        A = -1f, -1f, 1f,
        B = 1f, -1f, -1f,
        C = 1f, 1f, 1f,
        D = -1f, 1f, -1f,

        Triangles:
        ABC
        BDC
        ACD
        ADB
        */

		// Koordinaten, VAO, VBO, ... hier anlegen und im Grafikspeicher ablegen
        float [] triangles = new float[]{
                -1f, -1f, 1f, // ABC
                1f, -1f, -1f,
                1f, 1f, 1f,

                1f, -1f, -1f, // BDC
                -1f, 1f, -1f,
                1f, 1f, 1f,

                -1f, -1f, 1f, // ACD
                1f, 1f, 1f,
                -1f, 1f, -1f,

                -1f, -1f, 1f, // ADB
                -1f, 1f, -1f,
                1f, -1f, -1f
        };

        float[] colors = new float[]{
                1f, 1f, 0f, // ABC
                0f, 1f, 1f,
                1f, 1f, 1f,

                0f, 1f, 1f, // BDC
                1f, 0f, 1f,
                1f, 1f, 1f,

                1f, 1f, 0f, // ACD
                1f, 1f, 1f,
                1f, 0f, 1f,

                1f, 1f, 0f, // ADB
                1f, 0f, 1f,
                0f, 1f, 1f

        };

        float[] normals = new float[]{
                1f, -1f, 1f, // ABC
                1f, -1f, 1f,
                1f, -1f, 1f,

                1f, 1f, -1f, // BDC
                1f, 1f, -1f,
                1f, 1f, -1f,

                -1f, 1f, 1f, // ACD
                -1f, 1f, 1f,
                -1f, 1f, 1f,

                -1f, -1f, -1f, // ADB
                -1f, -1f, -1f,
                -1f, -1f, -1f
        };


        int vaoId = glGenVertexArrays();
        glBindVertexArray(vaoId); // select first VAO

        connect_vbo(triangles, 0, 3);
        connect_vbo(colors, 1, 3);
        connect_vbo(normals, 2, 3);

        this.translationMatrix.translate(0, 0, -5);
        this.scaleMatrix.scale(0.4f);

        // transfer projection matrix as uniform to opengl
        int projectionMatrixHandle = glGetUniformLocation(phong.getId(), "projectionMatrix");
        glUniformMatrix4fv(projectionMatrixHandle, false, this.projectionMatrix.getValuesAsArray());
        projectionMatrixHandle = glGetUniformLocation(gouraud.getId(), "projectionMatrix");
        glUniformMatrix4fv(projectionMatrixHandle, false, this.projectionMatrix.getValuesAsArray());

        int lightHandle = glGetUniformLocation(phong.getId(), "light");
        glUniform4fv(lightHandle, this.light);
        lightHandle = glGetUniformLocation(gouraud.getId(), "light");
        glUniform4fv(lightHandle, this.light);

		glEnable(GL_DEPTH_TEST); // z-Buffer aktivieren
		//glEnable(GL_CULL_FACE); // backface culling aktivieren
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
        this.phongObjectMatrix = new Matrix4();
        this.gouraudObjectMatrix = new Matrix4();
        this.rotationMatrix.rotateY(0.01f).rotateX(0.01f);
        this.phongObjectMatrix.multiply(this.rotationMatrix).multiply(this.translationMatrix).multiply(this.scaleMatrix);

	}

	@Override
	protected void render() {
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

        // Pass matrix to vertex shader
        int phongObjectMatrixHandle = glGetUniformLocation(phong.getId(), "transformationMatrix");
        glUniformMatrix4fv(phongObjectMatrixHandle, false, this.phongObjectMatrix.getValuesAsArray());
        int gouraudObjectMatrixHandle = glGetUniformLocation(phong.getId(), "transformationMatrix");
        glUniformMatrix4fv(gouraudObjectMatrixHandle, false, this.gouraudObjectMatrix.getValuesAsArray());

		// VAOs zeichnen
        glDrawArrays(GL_TRIANGLES, 0, 12);
	}
}
