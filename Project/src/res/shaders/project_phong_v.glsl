#version 330

layout(location=0) in vec4 vertex_pos;
layout(location=1) in vec3 color;
layout(location=2) in vec3 normal;

uniform vec4 light;
uniform mat4 projectionMatrix;
uniform mat4 transformationMatrix;
out vec3 v_color;
out vec3 N;
out vec3 P;

void main(){
    mat3 normalMatrix = transpose(inverse(mat3(transformationMatrix)));
    //mat3 normalMatrix = mat3(transformationMatrix);
    N = normalMatrix * normalize(normal);
    P = vec3(transformationMatrix * vertex_pos);

    gl_Position = projectionMatrix * transformationMatrix * vertex_pos;


    v_color = color;
}