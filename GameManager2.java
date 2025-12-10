package game;

import javax.media.opengl.GL;
import java.io.IOException;
import java.nio.file.*;
import java.util.*;

public class GameManager2 {

    public enum WeaponType { NORMAL, CLUSTER, BOUNCE }

    public static class Projectile {
        float x, y, vx, vy;
        WeaponType type;
        int bounces = 3;

        public Projectile(float x, float y, float vx, float vy, WeaponType type) {
            this.x = x; this.y = y; this.vx = vx; this.vy = vy; this.type = type;
        }
    }
    // ============================================================
    // Main GameManager Fields
    // ============================================================

    // Players

    private Player p1 = new Player(50, 40, true);
    private Player p2 = new Player(250, 40, false);

    // Score & Level
    private int p1Score = 0, p2Score = 0;
    private int p1Level = 1, p2Level = 1;
    private float speedMultiplier = 1.0f;

    // Username
    private String p1Name = "Player1";
    private String p2Name = "Player2";

    // High Score file
    private static final Path HIGH_SCORE_FILE = Path.of("highscores.txt");

    // Turn / Game State
    private boolean p1Turn = true;
    private boolean gameOver = false;
    private String gameOverMessage = "";
    private boolean aiming = true;
    private WeaponType activeWeapon = WeaponType.NORMAL;
    private float wind = 0f;
    private List<Projectile> projectiles = new ArrayList<>();

    // Texture Injection
    public void injectCharacterTextures(
            int[] idle1, int[] attack1, int[] hurt1, int[] death1,
            int[] idle2, int[] attack2, int[] hurt2, int[] death2
    ) {
        p1.idleTex = idle1; p1.attackTex = attack1; p1.hurtTex = hurt1; p1.deathTex = death1;
        p2.idleTex = idle2; p2.attackTex = attack2; p2.hurtTex = hurt2; p2.deathTex = death2;
    }

    // ============================================================
    // Constructors & Accessors
    // ============================================================
    private final int worldW, worldH;
    public GameManager2(int worldW, int worldH) { this.worldW = worldW; this.worldH = worldH; }

    public Player getPlayer1() { return p1; }
    public Player getPlayer2() { return p2; }
    public Player getActivePlayer() { return p1Turn ? p1 : p2; }
    public Player getInactivePlayer() { return p1Turn ? p2 : p1; }
    public boolean isP1Turn() { return p1Turn; }
    public boolean isGameOver() { return gameOver; }
    public String getGameOverMessage() { return gameOverMessage; }
    public String getTurnText() { return gameOver ? "" : (p1Turn ? "Player 1 turn" : "Player 2 turn"); }


    public int getScore(Player p) { return p == p1 ? p1Score : p2Score; }
    public int getLevel(Player p) { return p == p1 ? p1Level : p2Level; }
    public String getPlayerName(Player p) { return p == p1 ? p1Name : p2Name; }
    public void setPlayerName(Player p, String name) {
        if (p == p1) p1Name = name; else if (p == p2) p2Name = name;
    }

    // Aiming
    private float aimAngle = 45f;
    private float aimPower = 20f;
    public boolean isAiming() { return aiming; }
    public float getActiveAimX() { return getActivePlayer().x; }
    public float getActiveAimY() { return getActivePlayer().y + 20f; }
    public float getActiveAimAngle() { return aimAngle; }
    public float getActiveAimPower() { return aimPower; }
    public void adjustAimAngle(float v) { aimAngle = Math.max(5, Math.min(175, aimAngle + v)); }
    public void adjustAimPower(float v) { aimPower = Math.max(10, Math.min(100, aimPower + v)); }
    public WeaponType getActiveWeapon() { return activeWeapon; }
    public void setActiveWeapon(WeaponType t) { activeWeapon = t; }
    public void setWind(float w) { wind = w; }



    // ============================================================
    // Fire Attempt & Projectile Spawning
    // ============================================================
    public boolean tryFire() {
        if (!aiming) return false;
        Player ap = getActivePlayer();
        ap.anim = Player.AnimState.ATTACK;
        ap.currentFrame = 0;
        ap.animTimer = 0;
        aiming = false;
        return true;
    }
    public void spawnProjectiles(List<Projectile> list) { projectiles.addAll(list); }

    // ============================================================
    // Update Loop
    // ============================================================
    public void update() {
        if (gameOver) return;

        updateAnimations();
        updateProjectiles();

        // Level up logic
        checkLevelUp(p1);
        checkLevelUp(p2);
    }

    // ============================================================
    // Animation Handling
    // ============================================================
    private void updateAnimations() {
        Player p1ref = p1;
        Player p2ref = p2;

        // --- 1. P1 State Management ---
        if (p1ref.anim != Player.AnimState.HURT && p1ref.anim != Player.AnimState.DEATH) {
            if (p1Turn) {
                p1ref.anim = aiming ? Player.AnimState.IDLE : Player.AnimState.ATTACK;
            } else {
                p1ref.anim = Player.AnimState.IDLE;
            }
        }

        // --- 2. P2 State Management ---
        if (p2ref.anim != Player.AnimState.HURT && p2ref.anim != Player.AnimState.DEATH) {
            if (!p1Turn) {
                p2ref.anim = aiming ? Player.AnimState.IDLE : Player.AnimState.ATTACK;
            } else {
                p2ref.anim = Player.AnimState.IDLE;
            }
        }

        // Note: If a player is HURT or DEATH, their animation state is preserved and
        // the updatePlayerAnim() method is responsible for transitioning them back to IDLE
        // when the animation sequence completes.
        updatePlayerAnim(p1);
        updatePlayerAnim(p2);
    }
    private void updatePlayerAnim(Player p) {
        int[] set;
        switch (p.anim) {
            case ATTACK: set = p.attackTex; break;
            case HURT: set = p.hurtTex; break;
            case DEATH: set = p.deathTex; break;
            default: set = p.idleTex; break;
        }
        if (set == null || set.length == 0) return;

        if (p.anim == Player.AnimState.IDLE) {
            // PING-PONG (YOYO) LOGIC FOR SMOOTH IDLE
            final float IDLE_SPEED = 0.05f;
            int maxIndex = set.length - 1;

            if (p.animForward) p.animTimer += IDLE_SPEED;

            else p.animTimer -= IDLE_SPEED;

            // Reverse direction if hitting boundaries
            if (p.animTimer >= maxIndex) {
                p.animTimer = maxIndex;
                p.animForward = false;
            } else if (p.animTimer <= 0) {
                p.animTimer = 0;
                p.animForward = true;
            }

            p.currentFrame = (int)Math.round(p.animTimer);

        } else {
            // FORWARD-ONLY LOGIC for ATTACK/HURT/DEATH
            final float ACTION_SPEED = 0.25f;

            p.animTimer += ACTION_SPEED;
            int maxIndex = set.length - 1;

            // Bounds check and transition logic
            if (p.animTimer >= set.length) {
                // If the animation finishes, reset the state to IDLE
                // (Unless it's DEATH, which is handled below)
                if (p.anim == Player.AnimState.HURT || p.anim == Player.AnimState.ATTACK) {
                    p.anim = Player.AnimState.IDLE;
                    p.animTimer = 0;
                    p.currentFrame = 0;
                    return;
                }

                // For other non-looping animations, cap the timer at the last frame
                p.animTimer = set.length - 1;
            }

            p.currentFrame = (int)p.animTimer;

            // Death always stays on the last frame
            if (p.anim == Player.AnimState.DEATH) {
                p.currentFrame = maxIndex;
            }

            // Ensure frame never goes above the max index.
            p.currentFrame = Math.min(p.currentFrame, maxIndex);

            // Safety measure: frame must be >= 0
            p.currentFrame = Math.max(p.currentFrame, 0);
        }
    }


    // ============================================================
    // Projectiles & Damage
    // ============================================================
    private void updateProjectiles() {

        if (projectiles.isEmpty()) {
            if (!aiming) endTurn();
            return;
        }
        List<Projectile> dead = new ArrayList<>();
        Player target = getInactivePlayer();

        for (Projectile pr : projectiles) {
            // Movement logic (gravity/wind)
            pr.vx += wind * 0.01f;
            pr.vy -= 0.2f;
            pr.x += pr.vx;
            pr.y += pr.vy;

            // Collision checks
            if (target.anim != Player.AnimState.DEATH && hitPlayer(pr, target)) {
                hurtPlayer(target);
                dead.add(pr);
                continue;
            }

            // Hit ground/boundaries (Y=10)
            if (pr.y < 10) {
                if (pr.type == WeaponType.BOUNCE && pr.bounces > 0) { pr.vy *= -0.7f; pr.bounces--; }
                else if (pr.type == WeaponType.CLUSTER) { spawnCluster(pr); }
                else dead.add(pr);
            }
        }
        projectiles.removeAll(dead);
    }

    private void hurtPlayer(Player t) {
        t.hp -= 25;
        // Setting the state here. This state should remain HURT until updatePlayerAnim finishes it
        t.anim = Player.AnimState.HURT;
        t.animTimer = 0;
        t.currentFrame = 0;

        if (t.hp <= 0) {
            t.lives--;
            if (t.lives <= 0) {
                t.anim = Player.AnimState.DEATH;
                gameOver = true;
                gameOverMessage = (t == p1 ? p2Name + " Wins!" : p1Name + " Wins!");
                saveHighScore(p1); saveHighScore(p2); // حفظ النقاط عند النهاية
            } else t.hp = 100;
        } else {
            addScore(getActivePlayer(), 10); // زيادة النقاط عند إصابة الخصم
        }
    }

    private boolean hitPlayer(Projectile p, Player t) {
        return Math.abs(p.x - t.x) < 25 && Math.abs(p.y - t.y) < 40;
    }


    private void spawnCluster(Projectile pr) {
        for (int i = 0; i < 5; i++) {
            float ang = (float)Math.toRadians(i*72);
            projectiles.add(new Projectile(pr.x, pr.y, (float)Math.cos(ang)*4, (float)Math.sin(ang)*4, WeaponType.NORMAL));
        }
    }

    // ============================================================
    // Score & Level
    // ============================================================
    private void addScore(Player p, int amount) {
        if (p == p1) p1Score += amount; else if (p == p2) p2Score += amount;
    }

    private void checkLevelUp(Player p) {
        int score = getScore(p);
        int currentLevel = getLevel(p);
        int threshold = currentLevel * 5;
        if (score >= threshold) {
            if (p == p1) { p1Level++; speedMultiplier *= 1.1f; }
            else { p2Level++; speedMultiplier *= 1.1f; }
        }
    }

    // ============================================================
    // End Turn / Reset
    // ============================================================
    private void endTurn() {
        if (getActivePlayer().anim != Player.AnimState.DEATH && getActivePlayer().anim != Player.AnimState.HURT)
            getActivePlayer().resetAnimToIdle();
        if (getInactivePlayer().anim != Player.AnimState.DEATH && getInactivePlayer().anim != Player.AnimState.HURT)
            getInactivePlayer().resetAnimToIdle();
        aiming = true;
        p1Turn = !p1Turn;
        wind = (float)(Math.random()*2-1);
    }

    public void reset() {
        // Preserve injected texture sets
        int[] p1i = p1.idleTex; int[] p1a = p1.attackTex; int[] p1h = p1.hurtTex; int[] p1d = p1.deathTex;
        int[] p2i = p2.idleTex; int[] p2a = p2.attackTex; int[] p2h = p2.hurtTex; int[] p2d = p2.deathTex;

        p1 = new Player(50, 40, true);
        p2 = new Player(250, 40, false);

        p1.idleTex = p1i; p1.attackTex = p1a; p1.hurtTex = p1h; p1.deathTex = p1d;
        p2.idleTex = p2i; p2.attackTex = p2a; p2.hurtTex = p2h; p2.deathTex = p2d;

        projectiles.clear();
        gameOver = false;
        aiming = true;
        p1Turn = true;
        wind = 0f;
        p1Score = 0; p2Score = 0;
        p1Level = 1; p2Level = 1;
        speedMultiplier = 1.0f;
    }

    // ============================================================
    // High Score Management
    // ============================================================
    private void saveHighScore(Player p) {
        try {
            List<String> lines = Files.exists(HIGH_SCORE_FILE) ? Files.readAllLines(HIGH_SCORE_FILE) : new ArrayList<>();
            lines.add(getPlayerName(p)+","+getScore(p)+","+System.currentTimeMillis());
            lines.sort((a,b)->Integer.compare(Integer.parseInt(b.split(",")[1]),Integer.parseInt(a.split(",")[1])));
            Files.write(HIGH_SCORE_FILE, lines, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
        } catch (IOException e){ e.printStackTrace(); }
    }

    public List<String> getTopScores(int n) {
        try {
            if (Files.exists(HIGH_SCORE_FILE)) {
                List<String> lines = Files.readAllLines(HIGH_SCORE_FILE);
                return lines.subList(0, Math.min(n, lines.size()));
            }
        } catch(IOException e){ e.printStackTrace(); }
        return new ArrayList<>();
    }

    // ============================================================
    // Scene Drawing
    // ============================================================
    public void drawScene(GL gl, TextureDrawer drawer, CoordMapper mapX, CoordMapper mapY) {
        drawPlayer(gl, p1, drawer);
        drawPlayer(gl, p2, drawer);

        // Draw projectiles
        gl.glDisable(GL.GL_TEXTURE_2D);
        gl.glColor3f(0f,0f,0f);
        gl.glPointSize(6f);
        gl.glBegin(GL.GL_POINTS);
        for(Projectile p : projectiles) gl.glVertex2f(mapX.map(p.x), mapY.map(p.y));
        gl.glEnd();
        gl.glEnable(GL.GL_TEXTURE_2D);
    }

    private void drawPlayer(GL gl, Player p, TextureDrawer drawer) {
        int[] set;
        switch(p.anim) {
            case ATTACK: set = p.attackTex; break;
            case HURT: set = p.hurtTex; break;
            case DEATH: set = p.deathTex; break;
            default: set = p.idleTex; break;
        }
        if(set==null || set.length==0) return;
        int tex = set[p.currentFrame];
        drawer.draw(gl, tex, p.x, p.y, p.scale, p.facingRight);
    }

    public interface TextureDrawer { void draw(GL gl, int textureIndex, float worldX, float worldY, float scale, boolean facingRight);}
    public interface CoordMapper { float map(float v);}
}
