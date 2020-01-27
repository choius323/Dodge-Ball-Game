package dodge;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.*;

public class Frame extends JFrame {
	// fields
	private JPanel mainPanel;
	static JLabel scoreLabel;
	static final int borderSize = 5;
	static Font font = new Font("맑은 고딕", Font.BOLD, 13);

	// methods
	public Frame() {
		this.setTitle("Dodge");
		this.setSize(600, 600);
		this.setLayout(new BorderLayout(3, 3));
		Dimension screen = getToolkit().getScreenSize();
		this.setLocation((screen.width - getSize().width) / 2, (screen.height - getSize().height) / 2); // 시작 창 위치 조절
		this.setBackground(new Color(80, 188, 233));

		scoreLabel = new JLabel("점수 : 1"); // 점수 표시 레이블
		scoreLabel.setFont(font);
//		scoreLabel.setSize(400, 100);
		scoreLabel.setBorder(new EmptyBorder(5, 5, 5, 5)); // 바깥 여백
		scoreLabel.setHorizontalAlignment(JLabel.CENTER); // 글자 중심 정렬
		this.add(scoreLabel, BorderLayout.NORTH);

		mainPanel = new JPanel(); // 게임 패널
//		mainPanel.setSize(500, 500);
		mainPanel.setBorder(new LineBorder(Color.BLACK, borderSize)); // 테두리 설정
		mainPanel.setBackground(new Color(80, 188, 233));
		this.add(mainPanel, BorderLayout.CENTER);

		JPanel btnMenu = new JPanel(); // 버튼 패널
		this.add(btnMenu, BorderLayout.SOUTH);
		btnMenu.setLayout(new GridLayout(1, 3, 5, 0));
		btnMenu.setBorder(new EmptyBorder(5, 5, 5, 5));
//		BtnMenu.setSize(400, 100);
		
		JButton btn;
		btn = new JButton("시    작");
		btn.addActionListener(new ActionListener() { // 게임 시작
			public void actionPerformed(ActionEvent e) {
				startGame();
			}
		});
		btn.setFont(font);
		btnMenu.add(btn);

		btn = new JButton("점     수");
		btn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ScoreUtil.showScore();
			}
		});
		btn.setFont(font);
		btnMenu.add(btn);

		btn = new JButton("종     료");
		btn.addActionListener(new ActionListener() { // 종료 버튼
			public void actionPerformed(ActionEvent e) {
				System.exit(0); // 프로그램 종료
			}
		});
		btn.setFont(font);
		btnMenu.add(btn);

		this.setResizable(false); // 창 크기 재설정 불가
		this.setVisible(true); // 창 보이기
		this.setDefaultCloseOperation(Frame.EXIT_ON_CLOSE); // 창 닫기
	}

	public void startGame() {
//		mainPanel.getGraphics().clearRect(0, 0, mainPanel.getWidth(), mainPanel.getHeight());
		Thread th = new Thread(UserThread.userGroup, new UserThread(mainPanel));
		th.setName("User Thread");
		th.start();
	}

	public static void setScore(int score) {
		scoreLabel.setText("점수 : " + Integer.toString(score));
	}

	public static void main(String[] args) {
		new Frame();
	}
}
