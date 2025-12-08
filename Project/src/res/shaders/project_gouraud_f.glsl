#version 330

uniform vec4 light;

in vec3 v_color;
out vec3 color;


void main(){
    color = v_color;
/*
    // Phong
    color = 0.1 + light.w*(
    max(0,dot(L,normalize(N)))*v_color
    + pow(max(0,dot(R,V)), 30)*vec3(1, 1, 1)
    );
*/
    // Blinn phong
/*
    color = 0.1 + light.w*(
    max(0,dot(L,normalize(N)))*v_color
    + pow(max(0,dot(H,N)), 30)*vec3(0.3, 0.3, 0.3)
    );
*/
    // Gauraud


}