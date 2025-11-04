#version 330

layout(location=0) in vec2 triangles_pos;
layout(location=1) in vec3 triangles_col;

out vec3 v_color;

void main(){
    gl_Position = vec4(triangles_pos, 0.0, 1.0);
    v_color = triangles_col;
}