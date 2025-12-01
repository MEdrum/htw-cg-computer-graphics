#version 330

layout(location=0) in vec2 triangles_pos;
layout(location=1) in vec3 triangles_col;

uniform mat4 transformationMatrix;
out vec3 v_color;

void main(){
    gl_Position = vec4(triangles_pos, 0.0, 1.0);
    gl_Position = transformationMatrix * gl_Position; // if i comment this line, it works
    v_color = triangles_col;
}