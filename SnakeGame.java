package Game;

import javax.swing.*;

import java.awt.*;

import java.awt.event.*;

import java.util.ArrayList;

import java.util.Random;

public class SnakeGame extends JPanel implements ActionListener, KeyListener {

	private static final int WIDTH = 600;
    
	private static final int HEIGHT = 600;
    
	private static final int UNIT_SIZE = 20;
    
	private static final int DELAY = 100; // Game speed
    
	private static final int BONUS_INTERVAL = 4000; // Bonus appears every 4 seconds

	private final ArrayList<Point> snake;
    
	private Point food;

	private Point bonusFood;
    
	private int score;
    
	private char direction;
    
	private boolean running;
    
	private Timer timer;
    
	private long lastBonusTime;

    public SnakeGame() {
    
    	snake = new ArrayList<>();
        
    	score = 0;
        
    	direction = 'R';
        
    	running = true;
        
    	setPreferredSize(new Dimension(WIDTH, HEIGHT));
        
    	setBackground(Color.CYAN);
    	
        setFocusable(true);
        
        addKeyListener(this);
        
        startGame();
    }

    public void startGame() {
        
    	snake.clear();
        
    	snake.add(new Point(0, 0)); // Snake starts at top-left corner
        
    	spawnFood();
        
    	bonusFood = null; // No bonus food initially
        
    	lastBonusTime = System.currentTimeMillis();
        
    	timer = new Timer(DELAY, this);
        
    	timer.start();
    }

    public void spawnFood() {
      
    	Random random = new Random();
        
    	int x = random.nextInt(WIDTH / UNIT_SIZE) * UNIT_SIZE;
        
    	int y = random.nextInt(HEIGHT / UNIT_SIZE) * UNIT_SIZE;
        
    	food = new Point(x, y);
    }

    public void spawnBonusFood() {
        
    	Random random = new Random();
        
    	int x = random.nextInt(WIDTH / UNIT_SIZE) * UNIT_SIZE;
        
    	int y = random.nextInt(HEIGHT / UNIT_SIZE) * UNIT_SIZE;
        
    	bonusFood = new Point(x, y);
    }

    public void move() {
        
    	Point head = snake.get(0);
        
    	Point newHead = new Point(head);

        // Move snake based on direction
        
    	switch (direction) {
        
    	case 'U' -> newHead.y -= UNIT_SIZE;
        
    	case 'D' -> newHead.y += UNIT_SIZE;
        
    	case 'L' -> newHead.x -= UNIT_SIZE;
        
    	case 'R' -> newHead.x += UNIT_SIZE;
        
    	}

        // Check if snake eats food
        
    	if (newHead.equals(food)) {
        
    		score += 10;
            
    		spawnFood();
        } 
    	else if (bonusFood != null && newHead.equals(bonusFood)) {
        
    		score += 20; // Bonus score
            
    		bonusFood = null; // Remove bonus food
        } 
    	else {
        
    		snake.remove(snake.size() - 1); // Remove tail if no food eaten
        
    	}

        snake.add(0, newHead); // Add new head

        // Check for collision
        if (checkCollision()) {
        
        	running = false;
            
        	timer.stop();
        }
    }

    public boolean checkCollision() {
        
    	Point head = snake.get(0);

        // Check if head collides with body
        for (int i = 1; i < snake.size(); i++) {
        
        	if (head.equals(snake.get(i))) {
            
        		return true;
            }
        }

        // Check if head collides with walls
     
        return head.x < 0 || head.x >= WIDTH || head.y < 0 || head.y >= HEIGHT;
    
    }

    @Override
    public void paintComponent(Graphics g) {
    
    	super.paintComponent(g);
        
    	draw(g);
    }

    public void draw(Graphics g) {
        
    	if (running) {
            // Draw food
        
    		g.setColor(Color.RED);
            
    		g.fillRect(food.x, food.y, UNIT_SIZE, UNIT_SIZE);

            // Draw bonus food (if available)
            
    		if (bonusFood != null) {
            
    			g.setColor(Color.YELLOW);
                
    			g.fillRect(bonusFood.x, bonusFood.y, UNIT_SIZE, UNIT_SIZE);
            }

            // Draw snake
            
    		g.setColor(Color.GREEN);
            
    		for (Point p : snake) {
            
    			g.fillRect(p.x, p.y, UNIT_SIZE, UNIT_SIZE);
            }

            // Draw score
            
    		g.setColor(Color.WHITE);
            
    		g.setFont(new Font("Arial", Font.BOLD, 20));
            
    		g.drawString("Score: " + score, 10, 20);
        } 
    	else {
        
    		gameOver(g);
        }
    }

    public void gameOver(Graphics g) {
        
    	g.setColor(Color.RED);
        
    	g.setFont(new Font("Arial", Font.BOLD, 50));
        
    	g.drawString("Game Over", WIDTH / 2 - 150, HEIGHT / 2);
        
    	g.setFont(new Font("Arial", Font.BOLD, 20));
        
    	g.drawString("Final Score: " + score, WIDTH / 2 - 80, HEIGHT / 2 + 50);
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
    
    	if (running) {
        
    		move();

            // Check if bonus food should appear
            
    		long currentTime = System.currentTimeMillis();
            
    		if (currentTime - lastBonusTime >= BONUS_INTERVAL) {
            
    			spawnBonusFood();
                
    			lastBonusTime = currentTime;
            } 
    		else if (bonusFood != null && currentTime - lastBonusTime >= 2000) {
            
    			bonusFood = null; // Bonus food disappears after 2 seconds
            }
        }
        
    	repaint();
    }

    @Override
    
    public void keyPressed(KeyEvent e) {
    
    	switch (e.getKeyCode()) {
        
    	case KeyEvent.VK_UP -> {
        
    		if (direction != 'D') direction = 'U';
            
    	}
        
    	case KeyEvent.VK_DOWN -> {
        
    		if (direction != 'U') direction = 'D';
            
    	}
        
    	case KeyEvent.VK_LEFT -> {
        
    		if (direction != 'R') direction = 'L';
            
    	}
        
    	case KeyEvent.VK_RIGHT -> {
        
    		if (direction != 'L') direction = 'R';
            
    	}
        
    	}
    }

    @Override
    public void keyReleased(KeyEvent e) {}

    @Override
    public void keyTyped(KeyEvent e) {}

    public static void main(String[] args) {
        
    	JFrame frame = new JFrame("Snake Game");
        
    	SnakeGame game = new SnakeGame();
        
    	frame.add(game);
        
    	frame.pack();
        
    	frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
    	frame.setVisible(true);
    }
}