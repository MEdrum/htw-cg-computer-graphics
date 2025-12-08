#version 330

layout(location=0) in vec4 vertex_pos;
layout(location=1) in vec3 color;
layout(location=2) in vec3 normal;

uniform vec4 light;
uniform mat4 projectionMatrix;
uniform mat4 transformationMatrix;
out vec3 v_color;

void main(){
    mat3 normalMatrix = transpose(inverse(mat3(transformationMatrix)));
    //mat3 normalMatrix = mat3(transformationMatrix);
    vec3 N = normalMatrix * normalize(normal);
    vec3 P = vec3(transformationMatrix * vertex_pos);

    vec3 L = normalize(vec3(light) - P);
    vec3 R = reflect(-L, normalize(N));
    vec3 V = normalize(-P);
    vec3 H = normalize(L+V);

    gl_Position = projectionMatrix * transformationMatrix * vertex_pos;

    v_color = 0.1 + light.w*(
    max(0,dot(L,normalize(N)))*color
    + pow(max(0,dot(R,V)), 30)*vec3(1, 1, 1)
    );

}