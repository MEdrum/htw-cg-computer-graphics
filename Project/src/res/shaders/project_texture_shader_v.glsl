#version 330

layout(location=0) in vec4 vertex_pos;
// location=1 --> color
layout(location=2) in vec3 normal;
layout(location=3) in vec2 uv_coord;

uniform vec4 light;
uniform mat4 projectionMatrix;
uniform mat4 transformationMatrix;
out vec3 N;
out vec3 P;
out vec2 uv;

void main(){
    uv = uv_coord;

    mat3 normalMatrix = transpose(inverse(mat3(transformationMatrix)));
    //mat3 normalMatrix = mat3(transformationMatrix);
    N = normalMatrix * normalize(normal);
    P = vec3(transformationMatrix * vertex_pos);

    gl_Position = projectionMatrix * transformationMatrix * vertex_pos;
}