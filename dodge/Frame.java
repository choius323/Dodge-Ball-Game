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
	static Font font = new Font("���� ���", Font.BOLD, 13);

	// methods
	public Frame() {
		this.setTitle("Dodge");
		this.setSize(600, 600);
		this.setLayout(new BorderLayout(3, 3));
		Dimension screen = getToolkit().getScreenSize();
		this.setLocation((screen.width - getSize().width) / 2, (screen.height - getSize().height) / 2); // ���� â ��ġ ����
		this.setBackground(new Color(80, 188, 233));

		scoreLabel = new JLabel("���� : 1"); // ���� ǥ�� ���̺�
		scoreLabel.setFont(font);
//		scoreLabel.setSize(400, 100);
		scoreLabel.setBorder(new EmptyBorder(5, 5, 5, 5)); // �ٱ� ����
		scoreLabel.setHorizontalAlignment(JLabel.CENTER); // ���� �߽� ����
		this.add(scoreLabel, BorderLayout.NORTH);

		mainPanel = new JPanel(); // ���� �г�
//		mainPanel.setSize(500, 500);
		mainPanel.setBorder(new LineBorder(Color.BLACK, borderSize)); // �׵θ� ����
		mainPanel.setBackground(new Color(80, 188, 233));
		this.add(mainPanel, BorderLayout.CENTER);

		JPanel btnMenu = new JPanel(); // ��ư �г�
		this.add(btnMenu, BorderLayout.SOUTH);
		btnMenu.setLayout(new GridLayout(1, 3, 5, 0));
		btnMenu.setBorder(new EmptyBorder(5, 5, 5, 5));
//		BtnMenu.setSize(400, 100);
		
		JButton btn;
		btn = new JButton("��    ��");
		btn.addActionListener(new ActionListener() { // ���� ����
			public void actionPerformed(ActionEvent e) {
				startGame();
			}
		});
		btn.setFont(font);
		btnMenu.add(btn);

		btn = new JButton("��     ��");
		btn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ScoreUtil.showScore();
			}
		});
		btn.setFont(font);
		btnMenu.add(btn);

		btn = new JButton("��     ��");
		btn.addActionListener(new ActionListener() { // ���� ��ư
			public void actionPerformed(ActionEvent e) {
				System.exit(0); // ���α׷� ����
			}
		});
		btn.setFont(font);
		btnMenu.add(btn);

		this.setResizable(false); // â ũ�� �缳�� �Ұ�
		this.setVisible(true); // â ���̱�
		this.setDefaultCloseOperation(Frame.EXIT_ON_CLOSE); // â �ݱ�
	}

	public void startGame() {
//		mainPanel.getGraphics().clearRect(0, 0, mainPanel.getWidth(), mainPanel.getHeight());
		Thread th = new Thread(UserThread.userGroup, new UserThread(mainPanel));
		th.setName("User Thread");
		th.start();
	}

	public static void setScore(int score) {
		scoreLabel.setText("���� : " + Integer.toString(score));
	}

	public static void main(String[] args) {
		new Frame();
	}
}
