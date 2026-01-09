#version 150

uniform sampler2D SceneSampler;

uniform vec2 OutSize;
uniform float Time;
uniform float Strength;
uniform float FlipY;
uniform float EffectScale;

in vec2 vUv;
out vec4 fragColor;

float sat(float x) { return clamp(x, 0.0, 1.0); }

vec3 sampleScene(vec2 uv) {
    return texture(SceneSampler, clamp(uv, 0.0, 1.0)).rgb;
}

void main() {
    // -------------------------
    // 1) QUAD MASK (never scaled)
    // -------------------------
    vec2 pQuad = vUv - vec2(0.5);
    float rQuad = length(pQuad) * 2.0; // 0 center, 1 quad edge
    if (rQuad > 1.0) discard;

    // -------------------------
    // 2) EFFECT SPACE (scaled)
    // -------------------------
    vec2 pEff = pQuad * EffectScale;
    float rEff = length(pEff) * 2.0;

    // Direction should come from effect-space so warp directions match the scaled hole
    vec2 dir = normalize(pEff + vec2(1e-6));

    // Screen UV (what we warp)
    vec2 suv = gl_FragCoord.xy / OutSize;
    if (FlipY > 0.5) suv.y = 1.0 - suv.y;

    // Sample base (unwarped)
    vec3 base = sampleScene(suv);

    // -------------------------
    // 3) PARAMETERS (original look)
    // -------------------------
    float HOLE_R = 0.50;        // black disk radius in EFFECT space
    float influence = HOLE_R * 3.6; // bigger than 3.0 => nicer fade region

    // -------------------------
    // 4) OUTSIDE INFLUENCE: fully transparent, but do NOT discard
    //    (lets the quad exist so fading can be smooth)
    // -------------------------
    if (rEff > influence) {
        // keep a soft quad boundary just in case
        float edgeAlpha = 1.0 - smoothstep(0.98, 1.0, rQuad);
        fragColor = vec4(0.0, 0.0, 0.0, 0.0) * edgeAlpha;
        return;
    }

    // -------------------------
    // 5) EVENT HORIZON (black disk)
    // -------------------------
    if (rEff < HOLE_R) {
        // hard black, with tiny feather
        float feather = smoothstep(HOLE_R, HOLE_R - 0.02, rEff);
        vec3 col = mix(base, vec3(0.0), feather);
        float edgeAlpha = 1.0 - smoothstep(0.98, 1.0, rQuad);
        fragColor = vec4(col, edgeAlpha);
        return;
    }

    // -------------------------
    // 6) WARP (same curve as your original)
    // -------------------------
    float x = HOLE_R / max(rEff, 1e-6);
    float bend = Strength * (x * x);
    bend = clamp(bend, 0.0, 0.12);

    // convert to screen uv displacement
    float bendUV = bend * (HOLE_R * 0.85);
    vec2 warpedUV = suv - dir * bendUV;

    vec3 col = sampleScene(warpedUV);

    // -------------------------
    // 7) RING (original style)
    // -------------------------
    float ringW = HOLE_R * 0.25;
    float t = (rEff - HOLE_R) / max(ringW, 1e-6);
    float ring = sat(1.0 - t);
    ring *= ring;

    float flicker = 0.85 + 0.15 * sin(Time * 6.0 + rEff * 18.0);
    ring *= flicker;

    col += ring * vec3(0.9, 0.85, 0.8);

    // -------------------------
    // 8) FADE OUT (this is the important part)
    //    Make fade depend on rEff, not rQuad.
    // -------------------------
    float fade = 1.0 - smoothstep(HOLE_R, influence, rEff);
    // make it linger longer and feel natural
    fade = pow(fade, 2.2);

    // apply fade to ring too so it doesn't become a big outer halo
    col = mix(base, col, fade);

    // Quad edge alpha (only at the very outer quad boundary)
    float edgeAlpha = 1.0 - smoothstep(0.98, 1.0, rQuad);

    fragColor = vec4(col, sat(fade * edgeAlpha));
}
