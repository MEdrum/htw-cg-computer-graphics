package projekt;

import static org.lwjgl.opengl.GL30.*;

import lenz.opengl.AbstractOpenGLBase;
import lenz.opengl.ShaderProgram;
import lenz.opengl.Texture;

public class Projekt extends AbstractOpenGLBase {

	private ShaderProgram gouraud;
    private ShaderProgram phong;
    private ShaderProgram texture_shader;

    private float[] light = new float[]{0.1f, 0.1f, 5.0f, 1f}; // x, y, z, I

    private Matrix4 projectionMatrix = new  Matrix4(0.1f, 1000);

    private Matrix4 rotationMatrix = new  Matrix4();
    private Matrix4 translationMatrix = new  Matrix4();
    private Matrix4 scaleMatrix = new  Matrix4();

    private Matrix4 objectMatrix = new  Matrix4();
    private Matrix4 phongObjectMatrix = new  Matrix4();
    private Matrix4 gouraudObjectMatrix = new  Matrix4();
    private Matrix4 textureObjectMatrix = new Matrix4();

    private Texture wood;
    private Texture pixelart;


	public static void main(String[] args) {
        new Projekt().start("CG Projekt", 2000, 2000);
	}

	@Override
	protected void init() {
		gouraud = new ShaderProgram("project_gouraud");
        phong = new ShaderProgram("project_phong");
        texture_shader = new ShaderProgram("project_texture_shader");

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

        float[] uv_coords = new float[]{
                0f, 0f, // ABC
                1f, 0f,
                0.5f, 0.8f,

                0.5f, 0f, // BDC
                0f, 0.8f,
                1f, 0.8f,

                0.5f, 0f, // ACD
                1f, 0.8f,
                0f, 0.8f,

                0f, 0f, // ADB
                0.5f, 0.8f,
                1f, 0f
        };


        int vaoId = glGenVertexArrays();
        glBindVertexArray(vaoId); // select first VAO

        connect_vbo(triangles, 0, 3);
        connect_vbo(colors, 1, 3);
        connect_vbo(normals, 2, 3);
        connect_vbo(uv_coords, 3, 2);

        this.translationMatrix.translate(0, 0, -5);
        this.scaleMatrix.scale(0.4f);

        init_shaders(new ShaderProgram[] {phong, gouraud, texture_shader}, projectionMatrix, light);

        // Textures
        wood = new Texture("wood_1k.jpg");
        pixelart = new Texture("pixelart_16p.png");


		glEnable(GL_DEPTH_TEST); // z-Buffer aktivieren
		glEnable(GL_CULL_FACE); // backface culling aktivieren
	}

    private void init_shaders(ShaderProgram[] shaders, Matrix4 projectionMatrix, float[] light){
        String projectionMatrixName = "projectionMatrix";
        String lightName = "light";

        for(int i=0; i<shaders.length; i++){
            int shaderID = shaders[i].getId();

            int projectionMatrixHandle = glGetUniformLocation(shaderID, projectionMatrixName);
            int lightHandle = glGetUniformLocation(shaderID, lightName);

            glUseProgram(shaderID);
            glUniformMatrix4fv(projectionMatrixHandle, false, projectionMatrix.getValuesAsArray());
            glUniform4fv(lightHandle, light);
        }
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
        this.objectMatrix = new Matrix4();
        this.phongObjectMatrix = new Matrix4();
        this.gouraudObjectMatrix = new Matrix4();
        this.textureObjectMatrix = new Matrix4();

        this.rotationMatrix.rotateY(0.01f).rotateX(0.01f);
        this.objectMatrix.multiply(this.rotationMatrix).multiply(this.translationMatrix).multiply(this.scaleMatrix);

        this.gouraudObjectMatrix.multiply(this.phongObjectMatrix).translate(0.5f, -0.5f, 0f);
        this.textureObjectMatrix.multiply(this.phongObjectMatrix).translate(0f, 0.5f, 0f);
        this.phongObjectMatrix.translate(-0.5f, -0.5f, 0f);

	}

	@Override
	protected void render() {
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

        drawShadedVAOwithOffset(gouraud, objectMatrix, -0.5f, -0.5f, 0);
        drawShadedVAOwithOffset(phong, objectMatrix, -0.5f, 0.5f, 0);
        glBindTexture(GL_TEXTURE_2D, wood.getId());
        drawShadedVAOwithOffset(texture_shader, objectMatrix, 0.5f, -0.5f, 0);
        glBindTexture(GL_TEXTURE_2D, pixelart.getId());
        drawShadedVAOwithOffset(texture_shader, objectMatrix, 0.5f, 0.5f, 0);
	}

    private void drawShadedVAOwithOffset(ShaderProgram shader, Matrix4 objectMatrix, float offset_x, float offset_y, float offset_z){
        glUseProgram(shader.getId());
        Matrix4 offsetMatrix = new Matrix4();
        offsetMatrix.translate(offset_x, offset_y, offset_z);
        int textureObjectMatrixHandle = glGetUniformLocation(shader.getId(), "transformationMatrix");
        glUniformMatrix4fv(textureObjectMatrixHandle, false, new Matrix4(objectMatrix).multiply(offsetMatrix).getValuesAsArray());
        glDrawArrays(GL_TRIANGLES, 0, 12);
    }
}
