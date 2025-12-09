#version 330

uniform vec4 light;
uniform sampler2D smplr;

in vec3 N;
in vec3 P;
in vec2 uv;
out vec3 color;


void main(){
    vec3 L = normalize(vec3(light) - P);
    vec3 R = reflect(-L, normalize(N));
    vec3 V = normalize(-P);
    vec3 H = normalize(L+V);

    vec4 texel = texture(smplr, uv);

    // Phong
    color = 0.1*vec3(texel)
        + light.w*(
            max(0,dot(L,normalize(N)))*vec3(texel)
            + pow(max(0,dot(R,V)), 30)*vec3(1, 1, 1)
        );

    // Blinn phong
    /*
    color = 0.1 + light.w*(
    max(0,dot(L,normalize(N)))*vec3(texel)
    + pow(max(0,dot(H,N)), 30)*vec3(0.1, 0.1, 0.1)
    );
    */

    //color = vec3(texel);
}