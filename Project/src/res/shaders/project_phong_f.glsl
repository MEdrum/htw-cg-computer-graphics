#version 330

uniform vec4 light;

in vec3 N;
in vec3 P;
in vec3 v_color;
out vec3 color;


void main(){
    vec3 L = normalize(vec3(light) - P);
    vec3 R = reflect(-L, normalize(N));
    vec3 V = normalize(-P);

    // Phong
    color = 0.1 + light.w*(
        max(0,dot(L,normalize(N)))*v_color
        + pow(max(0,dot(R,V)), 30)*vec3(1, 1, 1)
    );
}