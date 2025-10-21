#version 330
vec2 imgSize = vec2(700, 700);
vec2 imgCenter = imgSize/2;
vec3 backgroundColor = vec3(0.1, 0.0, 0.2);
vec3 foregroundColor = vec3(0.6, 0.0, 1.0);
vec3 lineColor = vec3(0.2, 0.0, 0.1);

vec2 upperLeftCorner = vec2(150, 150);
vec2 lowerRightCorner = vec2(300, 300);

vec2 circleCenter = imgCenter;
float circleRadius = 150;

vec2 lineStart = imgCenter;
vec2 lineEnd = vec2(350, 700);
float lineThickness = 5;

vec2 pixelPos = gl_FragCoord.xy;

void rotateView(in float angle, in vec2 center, out vec2 pixelPos){
    mat2 rotation = mat2(cos(angle), sin(angle), -sin(angle), cos(angle));
    pixelPos = (gl_FragCoord.xy - center) * rotation + center;
}

void drawRectangle(in vec2 pixelPos, in vec2 upperLeftCorner, in vec2 lowerRightCorner, inout vec3 pixelColor, in vec3 color){
    if(pixelPos.x > upperLeftCorner.x && pixelPos.y > upperLeftCorner.y){
        if(pixelPos.x < lowerRightCorner.x && pixelPos.y < lowerRightCorner.y){
            pixelColor = color;
        }
    }
}

void drawCircle(in vec2 pixelPos, in vec2 circleCenter, in float circleRadius, inout vec3 pixelColor) {
    if (distance(circleCenter, gl_FragCoord.xy) < circleRadius) {
        pixelColor += pixelColor;
    }
}

void drawLine(in vec2 pixelPos, in vec2 lineStart, in vec2 lineEnd, in float lineThickness, inout vec3 pixelColor, in vec3 color) {
    vec2 lineVec = lineEnd - lineStart;
    vec2 perp = lineVec.yx;
    perp.x *= -1;
    perp /= length(perp);
    perp *= lineThickness;
    vec2 point1 = lineStart - perp/2;
    vec2 point2 = lineEnd - perp/2;
    vec2 point3 = lineEnd + perp/2;
    vec2 point4 = lineStart + perp/2;
}

void drawLine2(in vec2 pixelPos, in vec2 lineStart, in vec2 lineEnd, in float lineThickness, inout vec3 pixelColor, in vec3 color) {
    vec2 lineVec = lineEnd - lineStart;
    vec2 unitLineVec = lineVec.xy/length(lineVec.xy);
    float angle = atan(unitLineVec.y, unitLineVec.x);
    rotateView(angle, lineStart+lineVec/2, pixelPos);
    rotateView(angle, lineStart+lineVec/2, lineStart);
    rotateView(angle, lineStart+lineVec/2, lineEnd);

    lineStart.y - lineThickness/2;
    lineEnd.y + lineThickness/2;

    drawRectangle(pixelPos, lineStart, lineEnd, pixelColor, color);
}

out vec3 pixelColor;



void main() {
    pixelColor = backgroundColor;

    // flower
    float angle = 6.28/10;
    for(int i = 0; i<10; i++){
        rotateView(angle*i, imgCenter, pixelPos);
        drawRectangle(pixelPos, upperLeftCorner, lowerRightCorner, pixelColor, foregroundColor);
    }

    drawCircle(pixelPos, circleCenter, circleRadius, pixelColor);

    drawLine2(pixelPos, lineStart, lineEnd, lineThickness, pixelColor, lineColor);
}

