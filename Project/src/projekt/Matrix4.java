package projekt;

//All operations modify the matrix object itself, and return this object as well.
//Chaining of method-calls:
//Matrix4 m = new Matrix4().scale(5).translate(0,1,0).rotateX(0.5f);
public class Matrix4 {
    float[][] values;

	public Matrix4() {
		// init with identity
        values = new float[][] {
                {1f, 0f, 0f, 0f},
                {0f, 1f, 0f, 0f},
                {0f, 0f, 1f, 0f},
                {0f, 0f, 0f, 1f},
        };
	}

	public Matrix4(Matrix4 original) {
		// copy-constructor
        this.values = new float[4][4];
        for (int i = 0; i < 4; i++) {
            System.arraycopy(original.values[i], 0, this.values[i], 0, 4);
        }
	}

	public Matrix4(float near, float far) {
		// creates a projection matrix

        float frac1 = (-far - near) / (far - near);
        float frac2 = (-2*near*far) / (far - near);
        this.values = new float[][] {
                {1f, 0f, 0f,    0f},
                {0f, 1f, 0f,    0f},
                {0f, 0f, frac1, frac2},
                {0f, 0f, -1f,   0f}
        };
	}

    public Matrix4(float near, float far, float width, float height) {
        this();
        this.values[0][0] = (2*near)/width;
        this.values[1][1] = (2*near)/height;

        multiply(new Matrix4(near, far));
    }

	public Matrix4 multiply(Matrix4 other) {
		// matrix multiplication:
        // this = other * this
        float sum;
        float[][] result = new float[4][4];

        for (int x=0; x<4; x++) {
            for (int y=0; y<4; y++) {
                sum = 0.0f;
                for (int i=0; i<4; i++) {
                    sum += other.values[y][i] * this.values[i][x];
                }
                result[y][x] = sum;
            }
        }
        this.values = result;
		return this;
	}

	public Matrix4 translate(float x, float y, float z) {
        Matrix4 translation_matrix = new Matrix4();
        translation_matrix.values[0][3] = x;
        translation_matrix.values[1][3] = y;
        translation_matrix.values[2][3] = z;

        this.multiply(translation_matrix);
		return this;
	}

	public Matrix4 scale(float uniformFactor) {
        this.scale(uniformFactor, uniformFactor, uniformFactor);
		return this;
	}

	public Matrix4 scale(float sx, float sy, float sz) {
		Matrix4 scale_matrix = new Matrix4();
        scale_matrix.values[0][0] = sx;
        scale_matrix.values[1][1] = sy;
        scale_matrix.values[2][2] = sz;

        this.multiply(scale_matrix);
		return this;
	}

	public Matrix4 rotateX(float angle) {
		Matrix4 rotx_matrix = new Matrix4();
        float cos = (float)Math.cos(angle);
        float sin = (float)Math.sin(angle);
        rotx_matrix.values = new float[][] {
                {1f, 0f,  0f,   0f},
                {0f, cos, -sin, 0f},
                {0f, sin, cos,  0f},
                {0f, 0f,  0f,   1f}
        };
        this.multiply(rotx_matrix);
		return this;
	}

	public Matrix4 rotateY(float angle) {
        Matrix4 roty_matrix = new Matrix4();
        float cos = (float)Math.cos(angle);
        float sin = (float)Math.sin(angle);
        roty_matrix.values = new float[][] {
                {cos, 0f, -sin, 0f},
                {0f,  1f, 0f,   0f},
                {sin, 0f, cos,  0f},
                {0f,  0f, 0f,   1f}
        };
        this.multiply(roty_matrix);
		return this;
	}

	public Matrix4 rotateZ(float angle) {
        Matrix4 rotz_matrix = new Matrix4();
        float cos = (float)Math.cos(angle);
        float sin = (float)Math.sin(angle);
        rotz_matrix.values = new float[][] {
                {cos, -sin, 0f, 0f},
                {sin, cos,  0f, 0f},
                {0f,  0f,   1f, 0f},
                {0f,  0f,   0f, 1f}
        };
        this.multiply(rotz_matrix);
		return this;
	}

	public float[] getValuesAsArray() {
        // return values as a 1 dimensional array (column-wise)
        float[] result = new float[16];
        for (int i=0; i<16; i++) {
            int x = i/4;
            int y = i%4;
            result[i] = this.values[y][x];
        }

		return result;
	}
}
