// Name: Esra Korun
// ID: 2024400165

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

public class Main {
    public static final int MENU = 0;
    public static final int GAME = 1;
    public static final int GAME_OVER = 2;
    public static final int VICTORY = 3;

    public static int gameState = MENU;

    // aircraft
    public static double positionX = 300;
    public static double positionY = 160;
    public static double aircraftSpeed = 20;

    public static int fps = 30;

    public static boolean qPressed = false;
    public static boolean ePressed = false;
    public static boolean aPressed = false;
    public static boolean dPressed = false;
    public static boolean enterPressed = false;

    // bullet
    public static ArrayList<Double> bulletX = new ArrayList<>();
    public static ArrayList<Double> bulletY = new ArrayList<>();
    public static double bulletSpeed = 30;
    public static boolean spacePressed = false;

    // enemy
    public static ArrayList<Double> enemyX = new ArrayList<>();
    public static ArrayList<Double> enemyY = new ArrayList<>();

    public static double enemySpeed = 3;
    public static int enemyDirection = 1;  // 1 is right, -1 is left

    // enemy bullet
    public static ArrayList<Double> enemyBulletX = new ArrayList<>();
    public static ArrayList<Double> enemyBulletY = new ArrayList<>();
    public static double enemyBulletSpeed = 10;
    public static ArrayList<Double> enemyFireCooldown = new ArrayList<>();

    // explosion
    public static ArrayList<Double> explosionX = new ArrayList<>();
    public static ArrayList<Double> explosionY = new ArrayList<>();
    public static ArrayList<Integer> explosionFrame = new ArrayList<>();

    // pause
    public static boolean paused = false;
    public static boolean pPressed = false;

    // health
    public static int health = 3;
    public static final int maxHealth = 5;

    public static ArrayList<Double> healthX = new ArrayList<>();
    public static ArrayList<Double> healthY = new ArrayList<>();
    public static double healthSpeed = 5;

    // victory
    public static int victorySelection = 0; // 0:menu 1:end
    public static boolean upPressed = false;
    public static boolean downPressed = false;

    // end
    public static int score = 0;


    public static void main() {
        StdDraw.setCanvasSize(600, 800);
        StdDraw.setXscale(0, 600);
        StdDraw.setYscale(0, 800);
        StdDraw.setTitle("2042: Interceptor");
        StdDraw.enableDoubleBuffering();

        while (true){
            long start = System.currentTimeMillis();
            int frameTime = 1000 / fps;

            if (gameState == MENU){
                drawMenu();
                setFps();
                setAircraft();
                if(StdDraw.isKeyPressed(KeyEvent.VK_ENTER)){
                    if(!enterPressed){
                        createEnemies();
                        gameState = GAME;
                    }
                    enterPressed = true;
                }
                else enterPressed = false;

            } else if (gameState == GAME) {
                updateGame();
                drawGame();

            } else if (gameState == GAME_OVER) {
                drawEnd();
                gameEndSelection();
            } else if (gameState == VICTORY) {
                drawVictory();
                gameEndSelection();
            } else{
                break;
            }
            StdDraw.show();

            long end = System.currentTimeMillis();
            long elapsed = end - start;

            long sleepTime = frameTime - elapsed;
            if(sleepTime > 0){
                StdDraw.pause((int) sleepTime);
            }
        }
    }

    public static void drawMenu(){
        StdDraw.picture(300, 400, "assets/background.png", 600, 800);
        StdDraw.picture(300, 480, "assets/title.png", 400, 220);
        StdDraw.picture(positionX, positionY, "assets/interceptor.png", 60, 80);
        for(int i = 0; i < bulletX.size(); i++){
            StdDraw.picture(bulletX.get(i), bulletY.get(i), "assets/bullet.png", 8, 20);
        }

        StdDraw.setPenColor(StdDraw.WHITE);
        StdDraw.setFont(new Font("Arial", Font.BOLD, 20));
        StdDraw.text(300,320,"> START GAME <");
        StdDraw.setFont(new Font("Arial", Font.PLAIN, 18));
        StdDraw.text(300, 280, "FPS = " + fps + " | Speed = " + aircraftSpeed);

        StdDraw.setFont(new Font("Arial", Font.PLAIN, 12));
        StdDraw.text(300, 100, "Move: [←] [→] [↓] [↑]");
        StdDraw.text(300, 80, "Shoot: [SPACE]");
        StdDraw.text(300, 60, "Press [ENTER] to start");
        StdDraw.text(300, 40, "FPS: A/D   SPEED: Q/E");

    }
    public static void drawGame(){
        StdDraw.setPenColor(StdDraw.WHITE);

        StdDraw.picture(300, 400, "assets/background.png", 600, 800);
        StdDraw.textLeft(30, 770, "Score: " + score);
        //StdDraw.setPenColor(StdDraw.GREEN);
        //StdDraw.setPenRadius(0.005);
        StdDraw.picture(positionX, positionY, "assets/interceptor.png", 60, 80);
        //StdDraw.rectangle(positionX, positionY, 30, 40);

        for(int i = 0; i < bulletX.size(); i++){
            StdDraw.picture(bulletX.get(i), bulletY.get(i), "assets/bullet.png", 8, 20);
            //StdDraw.rectangle(bulletX.get(i), bulletY.get(i), 4, 10);
        }

        for(int i = 0; i < enemyX.size(); i++){
            StdDraw.picture(enemyX.get(i), enemyY.get(i), "assets/enemyFighter.png", 60, 80);
            //StdDraw.rectangle(enemyX.get(i), enemyY.get(i), 30, 40);
        }

        for(int i = 0; i < explosionX.size(); i++){
            if(explosionFrame.get(i) < 10){
                StdDraw.picture(explosionX.get(i), explosionY.get(i), "assets/explosionSmall.png", 60, 80);
            }
            else{
                StdDraw.picture(explosionX.get(i), explosionY.get(i), "assets/explosionBig.png", 60, 80);
            }
        }

        for(int i = 0; i < enemyBulletX.size(); i++){
            StdDraw.picture(enemyBulletX.get(i), enemyBulletY.get(i), "assets/enemyBullet.png", 8, 20);
            //StdDraw.rectangle(enemyBulletX.get(i), enemyBulletY.get(i), 4, 10);
        }

        for(int i = 0; i<healthX.size(); i++){
            StdDraw.picture(healthX.get(i), healthY.get(i), "assets/heart.png", 25,25);
        }

        for(int i = 0; i<health; i++){
            StdDraw.picture(560 - 30*i, 770, "assets/heart.png", 25,25);
        }

        if(paused){
            StdDraw.picture(300, 400, "assets/paused.png", 400, 100);
        }
    }
    public static void updateGame(){
        if(StdDraw.isKeyPressed(KeyEvent.VK_P)){
            if(!pPressed){
                paused = !paused;
            }
            pPressed = true;
        }
        else{
            pPressed = false;
        }

        if(paused) return;

        setAircraft();

        // enemy
        boolean changeDirection = false;
        for(int i = 0; i < enemyX.size(); i++){
            double newX = enemyX.get(i) + enemySpeed*enemyDirection;
            enemyX.set(i, newX);

            if(newX > 570 || newX < 30){
                changeDirection = true;
            }
        }

        if (changeDirection){
            enemyDirection *= -1;
        }

        // enemy bullet
        for (int i = 0; i < enemyX.size(); i++) {
            enemyFireCooldown.set(i, enemyFireCooldown.get(i) - (1.0 / fps));

            if (enemyFireCooldown.get(i) <= 0) {
                enemyBulletX.add(enemyX.get(i));
                enemyBulletY.add(enemyY.get(i));
                enemyFireCooldown.set(i, 2 + Math.random() * 2);
            }
        }

        for(int i = enemyBulletX.size()-1; i>= 0; i--){
            enemyBulletY.set(i, enemyBulletY.get(i)-enemyBulletSpeed);

            if(enemyBulletY.get(i)<0){
                enemyBulletX.remove(i);
                enemyBulletY.remove(i);
            }
        }

        // collision (enemyBullet and ship)
        for(int i = enemyBulletX.size()-1; i>= 0; i--){
            if(Math.abs(enemyBulletX.get(i) - positionX) < 35 && Math.abs(enemyBulletY.get(i) - positionY) < 35){
                enemyBulletX.remove(i);
                enemyBulletY.remove(i);
                health --;
                if(health == 0){
                    gameState = GAME_OVER;
                }
            }
        }
        for(int i = enemyX.size() - 1; i >= 0; i--){
            for(int j = bulletX.size()-1; j >=0; j--){
                if(Math.abs(bulletX.get(j) - enemyX.get(i)) < 35 &&
                        Math.abs(bulletY.get(j) - enemyY.get(i)) < 35){

                    score += 30;

                    if(Math.random() < 0.40){
                        healthX.add(enemyX.get(i));
                        healthY.add(enemyY.get(i));
                    }

                    bulletX.remove(j);
                    bulletY.remove(j);

                    explosionX.add(enemyX.get(i));
                    explosionY.add(enemyY.get(i));
                    explosionFrame.add(0);

                    enemyX.remove(i);
                    enemyY.remove(i);

                    enemyFireCooldown.remove(i);

                    break;
                }
            }
        }
        // (aircraft & enemyShip)
        for(int i = enemyX.size()-1; i >= 0; i--){
            if(Math.abs(enemyX.get(i) - positionX) < 55 && Math.abs(enemyY.get(i) - positionY) < 75){
                explosionX.add(enemyX.get(i));
                explosionY.add(enemyY.get(i));
                explosionFrame.add(0);

                enemyX.remove(i);
                enemyY.remove(i);
                enemyFireCooldown.remove(i);

                health --;
                if(health <= 0) gameState = GAME_OVER;
            }
        }

        // explosion
        for(int i = explosionX.size() - 1; i >= 0; i--){
            explosionFrame.set(i, explosionFrame.get(i) + 1);

            if(explosionFrame.get(i) > 20){
                explosionX.remove(i);
                explosionY.remove(i);
                explosionFrame.remove(i);
            }
        }

        for(int i = healthX.size()-1; i>=0; i--){
            healthY.set(i,healthY.get(i)-healthSpeed);

            if(Math.abs(healthX.get(i) - positionX) < 30 && Math.abs(healthY.get(i) - positionY) <30){
                if(health < maxHealth){
                    health++;
                }
                healthX.remove(i);
                healthY.remove(i);
            } else if (healthY.get(i) < 0) {
                healthX.remove(i);
                healthY.remove(i);
            }
        }
        if(enemyX.isEmpty()){
            gameState = VICTORY;
        }
    }
    public static void drawEnd(){
        StdDraw.picture(300, 400, "assets/background.png", 600, 800);
        StdDraw.picture(300, 480, "assets/gameOver.png", 400, 200);

        gameEndConf();

    }
    public static void resetGame(){
        positionX = 300;
        positionY = 160;

        paused = false;
        score = 0;
        health = 3;
        victorySelection = 0;

        bulletX.clear();
        bulletY.clear();

        enemyX.clear();
        enemyY.clear();

        enemyBulletX.clear();
        enemyBulletY.clear();
        enemyFireCooldown.clear();

        explosionX.clear();
        explosionY.clear();
        explosionFrame.clear();

        healthX.clear();
        healthY.clear();
    }
    public static void drawVictory(){
        StdDraw.picture(300, 400, "assets/background.png", 600, 800);
        StdDraw.picture(300, 480, "assets/victory.png", 450, 200);

        gameEndConf();
    }
    public static void createEnemies(){
        enemyX.clear();
        enemyY.clear();
        int rows = 2;
        int cols = 4;

        double startX = 75;
        double spacingX = 150;
        double startY = 750;
        double spacingY = 120;

        enemyFireCooldown.clear();

        for(int r = 0; r < rows; r++){
            for(int c = 0; c<cols; c++){
                enemyX.add(startX + c * spacingX);
                enemyY.add(startY - r * spacingY);

                enemyFireCooldown.add(0.5 + Math.random() * 2);
            }
        }

    }
    public static void setFps(){
        if(StdDraw.isKeyPressed(KeyEvent.VK_A)){
            if(!aPressed){
                fps += 1;
                if(fps > 120) fps = 120;
            }
            aPressed = true;
        }
        else{
            aPressed = false;
        }

        if(StdDraw.isKeyPressed(KeyEvent.VK_D)){
            if(!dPressed){
                fps -= 1;
                if(fps < 10) fps = 10;
            }
            dPressed = true;
        }
        else{
            dPressed = false;
        }
    }
    public static void setAircraft(){
        if (StdDraw.isKeyPressed(KeyEvent.VK_RIGHT)) positionX += aircraftSpeed;
        if (StdDraw.isKeyPressed(KeyEvent.VK_LEFT))  positionX -= aircraftSpeed;
        if (StdDraw.isKeyPressed(KeyEvent.VK_UP))    positionY += aircraftSpeed;
        if (StdDraw.isKeyPressed(KeyEvent.VK_DOWN))  positionY -= aircraftSpeed;

        if (positionX < 30) positionX = 30;
        if (positionY < 30) positionY = 30;
        if (positionX > 570) positionX = 570;
        if (positionY > 770) positionY = 770;


        // bullet
        if(StdDraw.isKeyPressed(KeyEvent.VK_SPACE)){
            if(!spacePressed){
                bulletX.add(positionX);
                bulletY.add(positionY);
                spacePressed = true;
            }
        }
        else{
            spacePressed = false;
        }

        for(int i = bulletX.size()-1; i >= 0; i--){
            bulletY.set(i, bulletY.get(i) + bulletSpeed);

            if(bulletY.get(i) > 800){
                bulletX.remove(i);
                bulletY.remove(i);
            }
        }

        if(gameState == MENU){
            if (StdDraw.isKeyPressed(KeyEvent.VK_Q)){
                if (!qPressed) aircraftSpeed += 1.0;
                if (aircraftSpeed > 40) aircraftSpeed = 40;
                qPressed = true;
            }
            else{
                qPressed = false;
            }

            if (StdDraw.isKeyPressed(KeyEvent.VK_E)){
                if (!ePressed) aircraftSpeed -= 1.0;
                if (aircraftSpeed < 10) aircraftSpeed = 10;
                ePressed = true;
            }
            else{
                ePressed = false;
            }
        }
    }
    public static void gameEndSelection(){
        if(StdDraw.isKeyPressed(KeyEvent.VK_UP)){
            if(!upPressed) victorySelection = 0;
            upPressed = true;
        }
        else upPressed = false;

        if(StdDraw.isKeyPressed(KeyEvent.VK_DOWN)){
            if(!downPressed) victorySelection = 1;
            downPressed = true;
        }
        else downPressed = false;

        if(StdDraw.isKeyPressed(KeyEvent.VK_ENTER)){
            if(!enterPressed){
                if(victorySelection == 0){
                    resetGame();
                    gameState = MENU;
                }
                else System.exit(0);
            }
            enterPressed = true;
        }
        else enterPressed = false;
    }
    public static void gameEndConf(){
        StdDraw.setPenColor(StdDraw.WHITE);
        StdDraw.setFont(new Font("Arial", Font.BOLD, 20));
        StdDraw.text(300, 350, "Score = " + score);

        if(victorySelection == 0){
            StdDraw.text(300, 300, "> Restart <");
            StdDraw.setFont(new Font("Arial", Font.PLAIN, 20));
            StdDraw.text(300, 270, " End Game ");
        }
        else{
            StdDraw.text(300, 270, "> End Game <");
            StdDraw.setFont(new Font("Arial", Font.PLAIN, 20));
            StdDraw.text(300, 300, " Restart ");
        }
        StdDraw.setFont(new Font("Arial", Font.PLAIN, 14));
        StdDraw.text(300, 120, "Navigate: [↑] [↓]   Select: [ENTER]");
    }
}