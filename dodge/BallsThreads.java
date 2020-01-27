package dodge;

import java.awt.Component;
import java.awt.Graphics;

public class BallsThreads implements Runnable {
	private Graphics gc;
	private int locX, locY, deltaX, deltaY;
	private static int maxX, maxY;
	private static final int ballSize = 12;
	private static int borderSize;
	static boolean finish;

	// methods
	public BallsThreads(Component graphic) {
		gc = graphic.getGraphics();
		setStartLocation((int) (Math.random() * 4));

		while (deltaX + deltaY < 4) { // �� �ӵ� ����
			deltaX = (int) (Math.random() * 4) + 2;
			deltaY = (int) (Math.random() * 2) + 1;
		}

		if (locX > maxX / 2) { // ������ �߽������� ����
			deltaX = -deltaX;
		}
		if (locY > maxY / 2) {
			deltaY = -deltaY;
		}
	}

	public void run() {
		// ���� ���� �� ����
		while (locX >= borderSize && locX <= (maxX - ballSize) && locY >= borderSize && locY <= (maxY - ballSize)) {
			gc.fillOval(locX, locY, ballSize, ballSize); // �׸���
			isMatch();
			try {
				Thread.sleep(20);
			} catch (InterruptedException e) {
//				if (finish = true) {
//				gc.clearRect(locX, locY, ballSize, ballSize);
//				}
				break;
			}
			gc.clearRect(locX, locY, ballSize, ballSize); // �����

			locX += deltaX; // �̵�
			locY += deltaY;
		}
	}

	public static void setStatic(int x, int y) {
		finish = false;
		borderSize = Frame.borderSize;
		maxX = x;
		maxY = y;
	}

	private void setStartLocation(int rand) { // ���� ��ġ ����
		switch (rand) {
		case 0: // ���ʿ��� ����
			locX = (int) (Math.random() * (maxX - ballSize));
			locY = borderSize;
			break;
		case 1: // ���ʿ��� ����
			locX = maxX - ballSize;
			locY = (int) (Math.random() * (maxY - ballSize));
			break;
		case 2: // ���ʿ��� ����
			locX = (int) (Math.random() * (maxX - ballSize));
			locY = maxY - ballSize;
			break;
		case 3: // ���ʿ��� ����
			locX = borderSize;
			locY = (int) (Math.random() * (maxY - ballSize));
			break;
		}
	}

	public void isMatch() { // ��Ҵ��� Ȯ��

		if (Math.abs(locX - UserThread.getX()) < ballSize && Math.abs(locY - UserThread.getY()) < ballSize) {
			finish = true;
			UserThread.match();
		}
	}
}
