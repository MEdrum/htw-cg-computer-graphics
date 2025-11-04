#version 330

layout(location=0) in vec2 triangles;

void main(){
    gl_Position = vec4(triangles, 0.0, 1.0);
}