package dodge;

import java.awt.Color;
import java.awt.Graphics;
import java.util.HashSet;

import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class UserThread implements Runnable {
	private static Graphics gc;
	private static JPanel mainPanel;
	private static int locX, locY, maxX, maxY; // 유저 위치, 최대 좌표
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
//		mainPanel.setBorder(new LineBorder(Color.BLACK, borderSize)); // 테두리 그리기
		UserThread.userGroup.interrupt(); // 이미 실행중일 경우 강제 종료 

		gc = mainPanel.getGraphics();
		maxX = mainPanel.getSize().width - borderSize; // 최대 x 좌표
		maxY = mainPanel.getSize().height - borderSize; // 최대 y 좌표

		BallsThreads.setStatic(maxX, maxY);

		locX = maxX / 2; // 초기 유저 x 좌표
		locY = maxY / 2; // 초기 유저 y 좌표

		
//		mainPanel.setCursor(mainPanel.getToolkit()
//				.createCustomCursor(new BufferedImage(3, 3, BufferedImage.TYPE_INT_ARGB), new Point(0, 0), "null")); // 커서
//																														// 숨기기
	}

	public void run() {
		int score = 0;
		int a, num = 1;
		final int maxScore = 50;

		Thread ball = null; // 볼 쓰레드
		ThreadGroup balls = new ThreadGroup("balls Group");
//		balls.setDaemon(true); // 데몬 쓰레드가 모두 종료되면 상위 쓰레드도 종료됨(?)
		finish = false;

		hSet.clear(); // 키보드 입력 초기화
		mainPanel.addKeyListener(new KeyHandler()); // 키 리스너 추가
		mainPanel.requestFocus(true); // 패널에 포커스
		mainPanel.setFocusable(true);
		gc.clearRect(borderSize, borderSize, maxX - borderSize, maxY - borderSize);
//		mainPanel.setBorder(new LineBorder(Color.BLACK, borderSize));
		
		gc.setColor(Color.orange); // 사용자 색
		gc.fillOval(locX, locY, userSize, userSize); // 사용자 초기 위치 그림

		while (true) {
			a = 0;
			while (a++ < (score + 2) / 3) { // 공 개수 조절
				ball = new Thread(balls, new BallsThreads(mainPanel)); // 볼 쓰레드 생성
				ball.setName(Integer.toString(num++));
				ball.start(); // 볼 쓰레드 실행
			}

			score++;
			Frame.setScore(score); // 점수 레이블에 표시
			if (score >= maxScore) {
				scorePopup("우승!!! ㅉㅉㅉㅉㅉㅉ", "웃음!!! ㅉㅉㅉㅉㅉㅉ", score); // 메시지 팝업
				userGroup.interrupt();
			}
			System.out.println(Thread.activeCount());
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				if (finish == true) { // 부딛혀서 끝난건지 확인
//					if (score < maxScore)
					scorePopup("게임 오버\n점수 : " + Integer.toString(score), "게임 오버", score);
				}
//				gc.clearRect(locX, locY, userSize, userSize); 
				KeyHandler.key.interrupt(); // 키 리스너 종료
				balls.interrupt(); // 볼 쓰레드 종료
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

	static public void match() { // 부딛힘
		finish = true;
		userGroup.interrupt();
	}

	public static void scorePopup(String print, String title, int score) {
		String name = JOptionPane.showInputDialog(null, print + "\n\n이름을 입력하세요.\n아무것도 입력하지 않으면 저장하지 않습니다.", title,
				JOptionPane.INFORMATION_MESSAGE); // 
		if (name != null && !name.trim().equals("")) { // 이름을 입력했거나 창을 끄지 않았거나
			new ScoreUtil().saveScore(name, score); // 점수 저장
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
				if (hSet.contains(LEFT)) { // 왼쪽 입력
					if (locX - userSpeed > borderSize) { // 이동했을 때 벽 넘어가는지 확인
						gc.clearRect(locX, locY, userSize, userSize); //이동 전 지우기
						locX = locX - userSpeed; // 이동 후 좌표 입력
						gc.fillOval(locX, locY, userSize, userSize); // 이동한곳 그리기
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
