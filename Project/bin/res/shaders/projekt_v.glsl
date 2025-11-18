#version 330

layout(location=0) in vec2 triangles_pos;
layout(location=1) in vec3 triangles_col;

out vec3 v_color;

void rotate(in float angle, inout vec4 pixelPos){
    mat2 rotation = mat2(cos(angle), sin(angle), -sin(angle), cos(angle));
    pixelPos = vec4(pixelPos.xy * rotation, 0.0, 1.0);
}

void main(){
    gl_Position = vec4(triangles_pos, 0.0, 1.0);
    rotate(10, gl_Position);
    v_color = triangles_col;
}