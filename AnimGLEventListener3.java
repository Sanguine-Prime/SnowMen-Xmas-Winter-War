package Snowman;

// Standard JOGL imports
import javax.media.opengl.GL;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLEventListener;
import javax.media.opengl.glu.GLU;

// Utility imports
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.util.BitSet;
import com.sun.opengl.util.GLUT;

// Project specific imports
import Snowman.Texture.TextureReader;
import game.AimingSystem;
import game.GameManager;
import game.Player;

/**
 * FINAL CLEAN VERSION - JOGL 1 COMPATIBLE
 * - Fixes: Calls drawScene on the 'game' instance.
 * - Fixes: Correctly uses the GL texture ID directly inside drawTexture.
 * - NEW: Implements SPRITE_GLOBAL_SCALE to make snowman sprites larger.
 */
public class AnimGLEventListener3 extends AnimListener implements GLEventListener {

    // ============================================================
    // Core Systems & World Setup
    // ============================================================
    private GameManager game;
    private AimingSystem aiming;
    private GLUT glut = new GLUT();

    // World bounds (used by GameManager)
    private final int maxWidth = 300;
    private final int maxHeight = 200;

    // NEW: Global scaling factor for all sprites (1.5f makes them 50% larger)
    private final float SPRITE_GLOBAL_SCALE = 1.5f;

    // ============================================================
    // Texture Data & State
    // ============================================================

    // The list of all 71 filenames (BG + P1 + P2 duplicates)
    public static final String textureNames[] = {
            // Index 0: Background
            "Assets/BackGrounds/BG_03.png",

            // =======================================================
            // PLAYER 1 TEXTURES (Indices 1 to 35)
            // =======================================================
            // ATTACK 1–11
            "Assets/Snowman01/AttackSpriteSplit/Snowman1Attack001.png", "Assets/Snowman01/AttackSpriteSplit/Snowman1Attack002.png",
            "Assets/Snowman01/AttackSpriteSplit/Snowman1Attack003.png", "Assets/Snowman01/AttackSpriteSplit/Snowman1Attack004.png",
            "Assets/Snowman01/AttackSpriteSplit/Snowman1Attack005.png", "Assets/Snowman01/AttackSpriteSplit/Snowman1Attack006.png",
            "Assets/Snowman01/AttackSpriteSplit/Snowman1Attack007.png", "Assets/Snowman01/AttackSpriteSplit/Snowman1Attack008.png",
            "Assets/Snowman01/AttackSpriteSplit/Snowman1Attack009.png", "Assets/Snowman01/AttackSpriteSplit/Snowman1Attack010.png",
            "Assets/Snowman01/AttackSpriteSplit/Snowman1Attack011.png",

            // DEATH 12–20
            "Assets/Snowman01/DeathSpriteSplit/Snowman1Death001.png", "Assets/Snowman01/DeathSpriteSplit/Snowman1Death002.png",
            "Assets/Snowman01/DeathSpriteSplit/Snowman1Death003.png", "Assets/Snowman01/DeathSpriteSplit/Snowman1Death004.png",
            "Assets/Snowman01/DeathSpriteSplit/Snowman1Death005.png", "Assets/Snowman01/DeathSpriteSplit/Snowman1Death006.png",
            "Assets/Snowman01/DeathSpriteSplit/Snowman1Death007.png", "Assets/Snowman01/DeathSpriteSplit/Snowman1Death008.png",
            "Assets/Snowman01/DeathSpriteSplit/Snowman1Death009.png",

            // HURT 21–27
            "Assets/Snowman01/HurtSpriteSplit/Snowman1Hurt001.png", "Assets/Snowman01/HurtSpriteSplit/Snowman1Hurt002.png",
            "Assets/Snowman01/HurtSpriteSplit/Snowman1Hurt003.png", "Assets/Snowman01/HurtSpriteSplit/Snowman1Hurt004.png",
            "Assets/Snowman01/HurtSpriteSplit/Snowman1Hurt005.png", "Assets/Snowman01/HurtSpriteSplit/Snowman1Hurt006.png",
            "Assets/Snowman01/HurtSpriteSplit/Snowman1Hurt007.png",

            // IDLE 28–35
            "Assets/Snowman01/IdleSpriteSplit/Snowman1Idle001.png", "Assets/Snowman01/IdleSpriteSplit/Snowman1Idle002.png",
            "Assets/Snowman01/IdleSpriteSplit/Snowman1Idle003.png", "Assets/Snowman01/IdleSpriteSplit/Snowman1Idle004.png",
            "Assets/Snowman01/IdleSpriteSplit/Snowman1Idle005.png", "Assets/Snowman01/IdleSpriteSplit/Snowman1Idle006.png",
            "Assets/Snowman01/IdleSpriteSplit/Snowman1Idle007.png",

            // =======================================================
            // PLAYER 2 TEXTURES (Indices 36 to 70) - DUPLICATED P1 SPRITES
            // =======================================================

            // ATTACK 36-46
            "Assets/Snowman01/AttackSpriteSplit/Snowman1Attack001.png", "Assets/Snowman01/AttackSpriteSplit/Snowman1Attack002.png",
            "Assets/Snowman01/AttackSpriteSplit/Snowman1Attack003.png", "Assets/Snowman01/AttackSpriteSplit/Snowman1Attack004.png",
            "Assets/Snowman01/AttackSpriteSplit/Snowman1Attack005.png", "Assets/Snowman01/AttackSpriteSplit/Snowman1Attack006.png",
            "Assets/Snowman01/AttackSpriteSplit/Snowman1Attack007.png", "Assets/Snowman01/AttackSpriteSplit/Snowman1Attack008.png",
            "Assets/Snowman01/AttackSpriteSplit/Snowman1Attack009.png", "Assets/Snowman01/AttackSpriteSplit/Snowman1Attack010.png",
            "Assets/Snowman01/AttackSpriteSplit/Snowman1Attack011.png",

            // DEATH 47-55
            "Assets/Snowman01/DeathSpriteSplit/Snowman1Death001.png", "Assets/Snowman01/DeathSpriteSplit/Snowman1Death002.png",
            "Assets/Snowman01/DeathSpriteSplit/Snowman1Death003.png", "Assets/Snowman01/DeathSpriteSplit/Snowman1Death004.png",
            "Assets/Snowman01/DeathSpriteSplit/Snowman1Death005.png", "Assets/Snowman01/DeathSpriteSplit/Snowman1Death006.png",
            "Assets/Snowman01/DeathSpriteSplit/Snowman1Death007.png", "Assets/Snowman01/DeathSpriteSplit/Snowman1Death008.png",
            "Assets/Snowman01/DeathSpriteSplit/Snowman1Death009.png",

            // HURT 56-62
            "Assets/Snowman01/HurtSpriteSplit/Snowman1Hurt001.png", "Assets/Snowman01/HurtSpriteSplit/Snowman1Hurt002.png",
            "Assets/Snowman01/HurtSpriteSplit/Snowman1Hurt003.png", "Assets/Snowman01/HurtSpriteSplit/Snowman1Hurt004.png",
            "Assets/Snowman01/HurtSpriteSplit/Snowman1Hurt005.png", "Assets/Snowman01/HurtSpriteSplit/Snowman1Hurt006.png",
            "Assets/Snowman01/HurtSpriteSplit/Snowman1Hurt007.png",

            // IDLE 63-70
            "Assets/Snowman01/IdleSpriteSplit/Snowman1Idle001.png", "Assets/Snowman01/IdleSpriteSplit/Snowman1Idle002.png",
            "Assets/Snowman01/IdleSpriteSplit/Snowman1Idle003.png", "Assets/Snowman01/IdleSpriteSplit/Snowman1Idle004.png",
            "Assets/Snowman01/IdleSpriteSplit/Snowman1Idle005.png", "Assets/Snowman01/IdleSpriteSplit/Snowman1Idle006.png",
            "Assets/Snowman01/IdleSpriteSplit/Snowman1Idle007.png"
    };

    // Arrays to hold the actual GL Texture IDs (integers > 0)
    private int[] textureIDs = new int[textureNames.length];
    private TextureReader.Texture[] loadedTextures = new TextureReader.Texture[textureNames.length];

    private int[] p1Idle    = new int[]{};
    private int[] p1Attack  = new int[]{};
    private int[] p1Hurt    = new int[]{};
    private int[] p1Death   = new int[]{};

    private int[] p2Idle    = new int[]{};
    private int[] p2Attack  = new int[]{};
    private int[] p2Hurt    = new int[]{};
    private int[] p2Death   = new int[]{};

    private int bgTextureID = -1; // Stores the actual GL texture ID

    // ============================================================
    // INIT (Texture Loading Here)
    // ============================================================
    @Override
    public void init(GLAutoDrawable gld) {
        GL gl = gld.getGL();
        GLU glu = new GLU();

        gl.glClearColor(1f, 1f, 1f, 1f);

        // Enable OpenGL features
        gl.glEnable(GL.GL_TEXTURE_2D);
        gl.glEnable(GL.GL_BLEND);
        gl.glBlendFunc(GL.GL_SRC_ALPHA, GL.GL_ONE_MINUS_SRC_ALPHA);

        // 1. Generate ALL texture IDs at once
        gl.glGenTextures(textureNames.length, textureIDs, 0);

        // 2. Load and Bind Textures
        for (int i = 0; i < textureNames.length; i++) {
            try {
                // Read texture data using the path array
                loadedTextures[i] = TextureReader.readTexture(textureNames[i], true);
                gl.glBindTexture(GL.GL_TEXTURE_2D, textureIDs[i]);

                // Use gluBuild2DMipmaps for robust JOGL 1 texture loading
                glu.gluBuild2DMipmaps(
                        GL.GL_TEXTURE_2D,
                        GL.GL_RGBA,
                        loadedTextures[i].getWidth(), loadedTextures[i].getHeight(),
                        GL.GL_RGBA,
                        GL.GL_UNSIGNED_BYTE,
                        loadedTextures[i].getPixels()
                );

                // Set filtering parameters
                gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_MIN_FILTER, GL.GL_LINEAR_MIPMAP_LINEAR);
                gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_MAG_FILTER, GL.GL_LINEAR);
                gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_WRAP_S, GL.GL_CLAMP);
                gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_WRAP_T, GL.GL_CLAMP);

            } catch (IOException e) {
                System.err.println("FATAL TEXTURE LOAD ERROR: Could not load " + textureNames[i] + ".");
                e.printStackTrace();
            }
        }

        // 3. Inject the loaded texture IDs into the local arrays and GameManager
        injectAllTextureIndices();

        // 4. Initialize Game Logic
        game = new GameManager(maxWidth, maxHeight);
        aiming = new AimingSystem();
        game.setWind(0f);

        // Inject IDs into GameManager
        game.injectCharacterTextures(
                p1Idle, p1Attack, p1Hurt, p1Death,
                p2Idle, p2Attack, p2Hurt, p2Death
        );
    }

    /**
     * Maps the sequential GL texture IDs (0-70) into the dedicated animation arrays.
     */
    private void injectAllTextureIndices() {
        // Constants define the length of each animation set
        final int P1_ATTACK_COUNT = 11;
        final int P1_DEATH_COUNT = 9;
        final int P1_HURT_COUNT = 7;
        final int P1_IDLE_COUNT = 7;

        this.bgTextureID = textureIDs[0];

        int index = 1; // Start of P1 animations (index 1)

        // --- P1 Indexing ---
        this.p1Attack = new int[P1_ATTACK_COUNT];
        for(int i = 0; i < P1_ATTACK_COUNT; i++) this.p1Attack[i] = textureIDs[index++];

        this.p1Death = new int[P1_DEATH_COUNT];
        for(int i = 0; i < P1_DEATH_COUNT; i++) this.p1Death[i] = textureIDs[index++];

        this.p1Hurt = new int[P1_HURT_COUNT];
        for(int i = 0; i < P1_HURT_COUNT; i++) this.p1Hurt[i] = textureIDs[index++];

        this.p1Idle = new int[P1_IDLE_COUNT];
        for(int i = 0; i < P1_IDLE_COUNT; i++) this.p1Idle[i] = textureIDs[index++];

        // --- P2 Indexing (uses the next 35 slots) ---
        this.p2Attack = new int[P1_ATTACK_COUNT];
        for(int i = 0; i < P1_ATTACK_COUNT; i++) this.p2Attack[i] = textureIDs[index++];

        this.p2Death = new int[P1_DEATH_COUNT];
        for(int i = 0; i < P1_DEATH_COUNT; i++) this.p2Death[i] = textureIDs[index++];

        this.p2Hurt = new int[P1_HURT_COUNT];
        for(int i = 0; i < P1_HURT_COUNT; i++) this.p2Hurt[i] = textureIDs[index++];

        this.p2Idle = new int[P1_IDLE_COUNT];
        for(int i = 0; i < P1_IDLE_COUNT; i++) this.p2Idle[i] = textureIDs[index++];

        System.out.println("Texture IDs injected. Total expected: 71. Total used: " + index);
    }


    // ============================================================
    // DISPLAY (Drawing Logic)
    // ============================================================
    @Override
    public void display(GLAutoDrawable glad) {
        GL gl = glad.getGL();
        gl.glClear(GL.GL_COLOR_BUFFER_BIT); // Clear buffers
        gl.glLoadIdentity(); // Reset the model-view matrix

        handleInput();
        game.update(); // Update game state

        drawBackground(gl);

        // FIX 1: Calling drawScene on the 'game' instance, not the class
        game.drawScene(gl,
                // ----------------------------------------------------------------------------------
                // TextureDrawer Lambda (implements sprite flipping)
                // ----------------------------------------------------------------------------------
                (glRef, texID, worldX, worldY, scale, facingRight) -> {

                    glRef.glPushMatrix();

                    // 1. Position the sprite
                    glRef.glTranslatef(toScreenX(worldX), toScreenY(worldY), 0);

                    // --- SPRITE SIZE FIX: Apply global scaling factor ---
                    float scaledX = scale * SPRITE_GLOBAL_SCALE;
                    float scaledY = scale * SPRITE_GLOBAL_SCALE;

                    // 2. Determine Scale, applying flip if facing left
                    float finalScaleX = scaledX;
                    if (!facingRight) {
                        finalScaleX = -scaledX; // Flip horizontally
                    }

                    // 3. Apply scale (including flip)
                    glRef.glScalef(finalScaleX, scaledY, 1); // Use the new scaled values

                    // 4. Draw the texture (calling the helper below)
                    drawTexture(glRef, texID, 1); // Draw a unit quad (base size)

                    glRef.glPopMatrix();
                },
                // ----------------------------------------------------------------------------------
                // CoordMapper Lambdas
                // ----------------------------------------------------------------------------------
                this::toScreenX,
                this::toScreenY
        );

        drawAimingLine(gl);
        drawUI(gl);
        gl.glFlush();
    }


    /**
     * Helper method to draw a texture centered at (0, 0) of size 'size'.
     * The 'textureID' parameter is the actual GL texture handle (integer > 0).
     */
    public void drawTexture(GL gl, int textureID, float size) {

        // FIX 2: Use the incoming textureID directly for binding,
        // not as an index into the local textureIDs array.
        gl.glBindTexture(GL.GL_TEXTURE_2D, textureID);

        gl.glBegin(GL.GL_QUADS);
        gl.glTexCoord2f(0f, 0f); gl.glVertex2f(-size/2, -size/2); // Bottom-left
        gl.glTexCoord2f(1f, 0f); gl.glVertex2f( size/2, -size/2); // Bottom-right
        gl.glTexCoord2f(1f, 1f); gl.glVertex2f( size/2,  size/2); // Top-right
        gl.glTexCoord2f(0f, 1f); gl.glVertex2f(-size/2,  size/2); // Top-left
        gl.glEnd();
    }

    // ============================================================
    // DRAWING METHODS
    // ============================================================
    private void drawBackground(GL gl) {
        if (bgTextureID > 0) {
            gl.glEnable(GL.GL_TEXTURE_2D);
            gl.glBindTexture(GL.GL_TEXTURE_2D, bgTextureID);
        } else {
            gl.glDisable(GL.GL_TEXTURE_2D);
        }

        gl.glColor3f(1f, 1f, 1f);

        gl.glBegin(GL.GL_QUADS);
        gl.glTexCoord2f(0f, 0f); gl.glVertex2f(-1f, -1f);
        gl.glTexCoord2f(1f, 0f); gl.glVertex2f(1f, -1f);
        gl.glTexCoord2f(1f, 1f); gl.glVertex2f(1f, 1f);
        gl.glTexCoord2f(0f, 1f); gl.glVertex2f(-1f, 1f);
        gl.glEnd();

        gl.glEnable(GL.GL_TEXTURE_2D);
    }

    private void drawAimingLine(GL gl) {
        if (!game.isAiming()) return;

        float ax = toScreenX(game.getActiveAimX());
        float ay = toScreenY(game.getActiveAimY());

        float length = 0.25f + (game.getActiveAimPower() * 0.015f);
        double angleRad = Math.toRadians(game.getActiveAimAngle());

        float bx = ax + (float)Math.cos(angleRad) * length;
        float by = ay + (float)Math.sin(angleRad) * length;

        gl.glDisable(GL.GL_TEXTURE_2D);
        gl.glColor3f(1f, 0f, 0f);
        gl.glLineWidth(4f);

        gl.glBegin(GL.GL_LINES);
        gl.glVertex2f(ax, ay);
        gl.glVertex2f(bx, by);
        gl.glEnd();

        gl.glEnable(GL.GL_TEXTURE_2D);
    }

    private void drawUI(GL gl) {
        gl.glDisable(GL.GL_TEXTURE_2D);
        gl.glColor3f(0f, 0f, 0f);

        renderText(gl, -0.95f, 0.92f, game.getTurnText());
        renderText(gl, -0.95f, 0.88f, "Weapon: " + game.getActiveWeapon().name());

        Player p1 = game.getPlayer1();
        Player p2 = game.getPlayer2();

        renderText(gl, 0.05f, 0.92f, "P1 HP:" + p1.getHp() + " L:" + p1.getLives());
        renderText(gl, 0.05f, 0.88f, "P2 HP:" + p2.getHp() + " L:" + p2.getLives());

        if (game.isGameOver()) {
            renderText(gl, -0.15f, 0f, game.getGameOverMessage());
            renderText(gl, -0.20f, -0.05f, "Press R to restart");
        }

        gl.glEnable(GL.GL_TEXTURE_2D);
    }

    private void renderText(GL gl, float x, float y, String s) {
        gl.glRasterPos2f(x, y);
        glut.glutBitmapString(GLUT.BITMAP_HELVETICA_18, s);
    }

    private void handleInput() {

        if (isKeyPressed(KeyEvent.VK_R)) {
            game.reset();
        }

        if (game.isGameOver()) {
            clearKeys();
            return;
        }

        if (game.isAiming()) {
            if (isKeyPressed(KeyEvent.VK_LEFT))  game.adjustAimAngle(1.5f);
            if (isKeyPressed(KeyEvent.VK_RIGHT)) game.adjustAimAngle(-1.5f);
            if (isKeyPressed(KeyEvent.VK_UP))    game.adjustAimPower(0.1f);
            if (isKeyPressed(KeyEvent.VK_DOWN))  game.adjustAimPower(-0.1f);

            // Weapons
            if (isKeyPressed(KeyEvent.VK_1)) game.setActiveWeapon(GameManager.WeaponType.NORMAL);
            if (isKeyPressed(KeyEvent.VK_2)) game.setActiveWeapon(GameManager.WeaponType.CLUSTER);
            if (isKeyPressed(KeyEvent.VK_3)) game.setActiveWeapon(GameManager.WeaponType.BOUNCE);

            if (isKeyPressed(KeyEvent.VK_SPACE)) {
                if (game.tryFire()) {
                    AimingSystem.ProjectileInit init = aiming.fireInit(
                            game.getActiveAimX(),
                            game.getActiveAimY(),
                            game.getActiveAimAngle(),
                            game.getActiveAimPower(),
                            game.getActiveWeapon()
                    );
                    game.spawnProjectiles(init.list);
                }
            }
        }

        clearKeys();
    }

    private BitSet keyBits = new BitSet(256);

    private void clearKeys() { keyBits.clear(); }

    @Override public void keyPressed(KeyEvent e) { keyBits.set(e.getKeyCode()); }
    @Override public void keyReleased(KeyEvent e) { keyBits.clear(e.getKeyCode()); }
    @Override public void keyTyped(KeyEvent e) {}

    public boolean isKeyPressed(int k) { return keyBits.get(k); }

    private float toScreenX(float x) { return (x / (maxWidth / 2f)) - 1f; }
    private float toScreenY(float y) { return (y / (maxHeight / 2f)) - 1f; }

    @Override public void reshape(GLAutoDrawable d, int x, int y, int w, int h) {}
    @Override public void displayChanged(GLAutoDrawable d, boolean m, boolean d2) {}
}