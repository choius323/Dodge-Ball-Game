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

		while (deltaX + deltaY < 4) { // 공 속도 조정
			deltaX = (int) (Math.random() * 4) + 2;
			deltaY = (int) (Math.random() * 2) + 1;
		}

		if (locX > maxX / 2) { // 방향을 중심쪽으로 조정
			deltaX = -deltaX;
		}
		if (locY > maxY / 2) {
			deltaY = -deltaY;
		}
	}

	public void run() {
		// 벽에 닿을 때 까지
		while (locX >= borderSize && locX <= (maxX - ballSize) && locY >= borderSize && locY <= (maxY - ballSize)) {
			gc.fillOval(locX, locY, ballSize, ballSize); // 그리기
			isMatch();
			try {
				Thread.sleep(20);
			} catch (InterruptedException e) {
//				if (finish = true) {
//				gc.clearRect(locX, locY, ballSize, ballSize);
//				}
				break;
			}
			gc.clearRect(locX, locY, ballSize, ballSize); // 지우기

			locX += deltaX; // 이동
			locY += deltaY;
		}
	}

	public static void setStatic(int x, int y) {
		finish = false;
		borderSize = Frame.borderSize;
		maxX = x;
		maxY = y;
	}

	private void setStartLocation(int rand) { // 시작 위치 지정
		switch (rand) {
		case 0: // 북쪽에서 시작
			locX = (int) (Math.random() * (maxX - ballSize));
			locY = borderSize;
			break;
		case 1: // 동쪽에서 시작
			locX = maxX - ballSize;
			locY = (int) (Math.random() * (maxY - ballSize));
			break;
		case 2: // 남쪽에서 시작
			locX = (int) (Math.random() * (maxX - ballSize));
			locY = maxY - ballSize;
			break;
		case 3: // 서쪽에서 시작
			locX = borderSize;
			locY = (int) (Math.random() * (maxY - ballSize));
			break;
		}
	}

	public void isMatch() { // 닿았는지 확인

		if (Math.abs(locX - UserThread.getX()) < ballSize && Math.abs(locY - UserThread.getY()) < ballSize) {
			finish = true;
			UserThread.match();
		}
	}
}
