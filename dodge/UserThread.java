package dodge;

import java.awt.Color;
import java.awt.Graphics;
import java.util.HashSet;

import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class UserThread implements Runnable {
	private static Graphics gc;
	private static JPanel mainPanel;
	private static int locX, locY, maxX, maxY; // ���� ��ġ, �ִ� ��ǥ
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
//		mainPanel.setBorder(new LineBorder(Color.BLACK, borderSize)); // �׵θ� �׸���
		UserThread.userGroup.interrupt(); // �̹� �������� ��� ���� ���� 

		gc = mainPanel.getGraphics();
		maxX = mainPanel.getSize().width - borderSize; // �ִ� x ��ǥ
		maxY = mainPanel.getSize().height - borderSize; // �ִ� y ��ǥ

		BallsThreads.setStatic(maxX, maxY);

		locX = maxX / 2; // �ʱ� ���� x ��ǥ
		locY = maxY / 2; // �ʱ� ���� y ��ǥ

		
//		mainPanel.setCursor(mainPanel.getToolkit()
//				.createCustomCursor(new BufferedImage(3, 3, BufferedImage.TYPE_INT_ARGB), new Point(0, 0), "null")); // Ŀ��
//																														// �����
	}

	public void run() {
		int score = 0;
		int a, num = 1;
		final int maxScore = 50;

		Thread ball = null; // �� ������
		ThreadGroup balls = new ThreadGroup("balls Group");
//		balls.setDaemon(true); // ���� �����尡 ��� ����Ǹ� ���� �����嵵 �����(?)
		finish = false;

		hSet.clear(); // Ű���� �Է� �ʱ�ȭ
		mainPanel.addKeyListener(new KeyHandler()); // Ű ������ �߰�
		mainPanel.requestFocus(true); // �гο� ��Ŀ��
		mainPanel.setFocusable(true);
		gc.clearRect(borderSize, borderSize, maxX - borderSize, maxY - borderSize);
//		mainPanel.setBorder(new LineBorder(Color.BLACK, borderSize));
		
		gc.setColor(Color.orange); // ����� ��
		gc.fillOval(locX, locY, userSize, userSize); // ����� �ʱ� ��ġ �׸�

		while (true) {
			a = 0;
			while (a++ < (score + 2) / 3) { // �� ���� ����
				ball = new Thread(balls, new BallsThreads(mainPanel)); // �� ������ ����
				ball.setName(Integer.toString(num++));
				ball.start(); // �� ������ ����
			}

			score++;
			Frame.setScore(score); // ���� ���̺� ǥ��
			if (score >= maxScore) {
				scorePopup("���!!! ������������", "����!!! ������������", score); // �޽��� �˾�
				userGroup.interrupt();
			}
			System.out.println(Thread.activeCount());
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				if (finish == true) { // �ε����� �������� Ȯ��
//					if (score < maxScore)
					scorePopup("���� ����\n���� : " + Integer.toString(score), "���� ����", score);
				}
//				gc.clearRect(locX, locY, userSize, userSize); 
				KeyHandler.key.interrupt(); // Ű ������ ����
				balls.interrupt(); // �� ������ ����
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

	static public void match() { // �ε���
		finish = true;
		userGroup.interrupt();
	}

	public static void scorePopup(String print, String title, int score) {
		String name = JOptionPane.showInputDialog(null, print + "\n\n�̸��� �Է��ϼ���.\n�ƹ��͵� �Է����� ������ �������� �ʽ��ϴ�.", title,
				JOptionPane.INFORMATION_MESSAGE); // 
		if (name != null && !name.trim().equals("")) { // �̸��� �Է��߰ų� â�� ���� �ʾҰų�
			new ScoreUtil().saveScore(name, score); // ���� ����
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
				if (hSet.contains(LEFT)) { // ���� �Է�
					if (locX - userSpeed > borderSize) { // �̵����� �� �� �Ѿ���� Ȯ��
						gc.clearRect(locX, locY, userSize, userSize); //�̵� �� �����
						locX = locX - userSpeed; // �̵� �� ��ǥ �Է�
						gc.fillOval(locX, locY, userSize, userSize); // �̵��Ѱ� �׸���
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
