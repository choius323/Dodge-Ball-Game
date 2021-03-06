package dodge;

import java.awt.Color;
import java.awt.Graphics;
import java.util.HashSet;

import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class UserThread implements Runnable {
	private static Graphics gc;
	private static JPanel mainPanel;
	private static int locX, locY, maxX, maxY; // 政煽 是帖, 置企 疎妊
	static ThreadGroup userGroup = new ThreadGroup("User Group");
	static boolean finish = false;
	final private static int userSize = 15;
	final private static int userSpeed = 5;
	private static final int borderSize = Frame.borderSize;
	static HashSet<Integer> hSet = new HashSet<Integer>();

	public UserThread() {
	}

	public UserThread(JPanel graphics) {
		mainPanel = graphics;
//		borderSize = Frame.borderSize;
//		mainPanel.setBorder(new LineBorder(Color.BLACK, borderSize)); // 砺砧軒 益軒奄
		UserThread.userGroup.interrupt(); // 戚耕 叔楳掻析 井酔 悪薦 曽戟 

		gc = mainPanel.getGraphics();
		maxX = mainPanel.getSize().width - borderSize; // 置企 x 疎妊
		maxY = mainPanel.getSize().height - borderSize; // 置企 y 疎妊

		BallsThreads.setStatic(maxX, maxY);

		locX = maxX / 2; // 段奄 政煽 x 疎妊
		locY = maxY / 2; // 段奄 政煽 y 疎妊

		
//		mainPanel.setCursor(mainPanel.getToolkit()
//				.createCustomCursor(new BufferedImage(3, 3, BufferedImage.TYPE_INT_ARGB), new Point(0, 0), "null")); // 朕辞
//																														// 需奄奄
	}

	public void run() {
		int score = 0;
		int a, num = 1;
		final int maxScore = 50;

		Thread ball = null; // 瑳 床傾球
		ThreadGroup balls = new ThreadGroup("balls Group");
//		balls.setDaemon(true); // 汽佼 床傾球亜 乞砧 曽戟鞠檎 雌是 床傾球亀 曽戟喫(?)
		finish = false;

		hSet.clear(); // 徹左球 脊径 段奄鉢
		mainPanel.addKeyListener(new KeyHandler()); // 徹 軒什格 蓄亜
		mainPanel.requestFocus(true); // 鳶確拭 匂朕什
		mainPanel.setFocusable(true);
		gc.clearRect(borderSize, borderSize, maxX - borderSize, maxY - borderSize);
//		mainPanel.setBorder(new LineBorder(Color.BLACK, borderSize));
		
		gc.setColor(Color.orange); // 紫遂切 事
		gc.fillOval(locX, locY, userSize, userSize); // 紫遂切 段奄 是帖 益顕

		while (true) {
			a = 0;
			while (a++ < (score + 2) / 3) { // 因 鯵呪 繕箭
				ball = new Thread(balls, new BallsThreads(mainPanel)); // 瑳 床傾球 持失
				ball.setName(Integer.toString(num++));
				ball.start(); // 瑳 床傾球 叔楳
			}

			score++;
			Frame.setScore(score); // 繊呪 傾戚鷺拭 妊獣
			if (score >= maxScore) {
				scorePopup("酔渋!!! すすすすすす", "数製!!! すすすすすす", score); // 五獣走 橡穣
				userGroup.interrupt();
			}
			System.out.println(Thread.activeCount());
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				if (finish == true) { // 採挙粕辞 魁貝闇走 溌昔
//					if (score < maxScore)
					scorePopup("惟績 神獄\n繊呪 : " + Integer.toString(score), "惟績 神獄", score);
				}
//				gc.clearRect(locX, locY, userSize, userSize); 
				KeyHandler.key.interrupt(); // 徹 軒什格 曽戟
				balls.interrupt(); // 瑳 床傾球 曽戟
				break;
			}
		}
	}

	static public int getX() {
		return locX;
	}

	static public int getY() {
		return locY;
	}

	static public void match() { // 採挙毘
		finish = true;
		userGroup.interrupt();
	}

	public static void scorePopup(String print, String title, int score) {
		String name = JOptionPane.showInputDialog(null, print + "\n\n戚硯聖 脊径馬室推.\n焼巷依亀 脊径馬走 省生檎 煽舌馬走 省柔艦陥.", title,
				JOptionPane.INFORMATION_MESSAGE); // 
		if (name != null && !name.trim().equals("")) { // 戚硯聖 脊径梅暗蟹 但聖 塊走 省紹暗蟹
			new ScoreUtil().saveScore(name, score); // 繊呪 煽舌
		}
	}

	static class move implements Runnable {
		private static final int LEFT = 1;
		private static final int UP = 2;
		private static final int RIGHT = 3;
		private static final int DOWN = 4;

		@Override
		public void run() {
			// TODO Auto-generated method stub
			while (true) {
				try {
					Thread.sleep(15); // 0.015ms
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					break;
				}
				if (hSet.contains(LEFT)) { // 図楕 脊径
					if (locX - userSpeed > borderSize) { // 戚疑梅聖 凶 混 角嬢亜澗走 溌昔
						gc.clearRect(locX, locY, userSize, userSize); //戚疑 穿 走酔奄
						locX = locX - userSpeed; // 戚疑 板 疎妊 脊径
						gc.fillOval(locX, locY, userSize, userSize); // 戚疑廃員 益軒奄
					}
				}
				if (hSet.contains(UP)) { 
					if (locY - userSpeed > borderSize) {
						gc.clearRect(locX, locY, userSize, userSize);
						locY = locY - userSpeed;
						gc.fillOval(locX, locY, userSize, userSize);
					}
				}
				if (hSet.contains(RIGHT)) {
					if (locX + userSpeed < maxX - userSize) {
						gc.clearRect(locX, locY, userSize, userSize);
						locX = locX + userSpeed;
						gc.fillOval(locX, locY, userSize, userSize);
					}
				}
				if (hSet.contains(DOWN)) {
					if (locY + userSpeed < maxY - userSize) {
						gc.clearRect(locX, locY, userSize, userSize);
						locY = locY + userSpeed;
						gc.fillOval(locX, locY, userSize, userSize);
					}
				}
			}
		}
	}
}
