package dodge;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

class KeyHandler implements KeyListener {
	private static UserThread u;
	private static Thread th;
	static ThreadGroup key;
	private static final int LEFT = 1;
	private static final int UP = 2;
	private static final int RIGHT = 3;
	private static final int DOWN = 4;

	public KeyHandler() {
		key = new ThreadGroup("KeyBoard");
		th = new Thread(key, new UserThread.move());

		th.start();
	}

	@Override
	public void keyTyped(KeyEvent e) { // 키 누를 때
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyPressed(KeyEvent e) { // 키 누르고 있을 때
		// TODO Auto-generated method stub
		if (e.getKeyCode() == KeyEvent.VK_LEFT) {
			UserThread.hSet.add(LEFT);
		} else if (e.getKeyCode() == KeyEvent.VK_UP) {
			UserThread.hSet.add(UP);
		} else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
			UserThread.hSet.add(RIGHT);
		} else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
			UserThread.hSet.add(DOWN);
		}
	}

	@Override
	public void keyReleased(KeyEvent e) { // 키 땔 때
		// TODO Auto-generated method stub
		if (e.getKeyCode() == KeyEvent.VK_LEFT) {
			UserThread.hSet.remove(LEFT);
		} else if (e.getKeyCode() == KeyEvent.VK_UP) {
			UserThread.hSet.remove(UP);
		} else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
			UserThread.hSet.remove(RIGHT);
		} else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
			UserThread.hSet.remove(DOWN);
		}
	}
}
