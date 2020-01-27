package dodge;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;

import javax.swing.JOptionPane;
import javax.swing.UIManager;

public class ScoreUtil implements Serializable {
	private static ObjectOutputStream oos = null; // 
	private static ObjectInputStream ois = null;
	static int bestScore;
	private static File f = new File("score.data");
	private static ArrayList<ScoreTable> scoreList;

	public ScoreUtil() {
		try {
			ois = new ObjectInputStream(new FileInputStream(f));
			scoreList = (ArrayList<ScoreTable>) ois.readObject();
			bestScore = scoreList.get(0).score; // 최고 점수
			
			ois.close();
		} catch (FileNotFoundException e) { // 파일 없을 경우
			// TODO Auto-generated catch block
			createFile();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
		}
	}

	private void createFile() {
		try {
			System.out.println("파일 생성");
			bestScore = 0;
			scoreList = new ArrayList<ScoreTable>();
			oos = new ObjectOutputStream(new FileOutputStream(f));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void saveScore(String name, int score) {
		try {
			if (bestScore == 0) { // 기록 없음
				System.out.println(scoreList);
				scoreList.add(0, new ScoreTable(name, score));
			} else { // 기록 있음
				boolean isSave = false;// 저장 했는지 확인
				oos = new ObjectOutputStream(new FileOutputStream(f));
				System.out.println("scoreList.size() : " + scoreList.size());
				
				for (int i = 0; i < scoreList.size(); i++) {
					System.out.println("scoreList.get(i).score : " + scoreList.get(i).score);
					if (scoreList.get(i).score < score && isSave == false) {
						scoreList.add(i, new ScoreTable(name, score)); // 유저 점수 기록
						isSave = true;
					}
				}
				if(isSave == false) { // 꼴찌
					scoreList.add(new ScoreTable(name, score));
				}
			}
			oos.writeObject(scoreList);

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				oos.flush();
				oos.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public static void showScore() { // 점수 버튼 클릭
		try {
			UserThread.userGroup.interrupt();
			ois = new ObjectInputStream(new FileInputStream(f));
			String temp = String.format("%-5s%25s", "점수", "이름") + "\n";
			scoreList = (ArrayList<ScoreTable>) ois.readObject();

			for (int i = 0; i < scoreList.size(); i++) {
				temp = temp.concat(scoreList.get(i).toString() + "\n");

			}
			UIManager.put("OptionPane.messageFont", Frame.font); // 메시지 폰트 변경
			UIManager.put("OptionPane.buttonFont", Frame.font);

			System.out.println(temp);
			JOptionPane.showMessageDialog(null, temp, "점수 기록", JOptionPane.INFORMATION_MESSAGE);

			ois.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			JOptionPane.showMessageDialog(null, "아직 기록이 없습니다.", "점수 기록", JOptionPane.INFORMATION_MESSAGE);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}

class ScoreTable implements Serializable { // 점수 저장용 클래스
	String name;
	int score;

	public ScoreTable(String name, int score) {
		this.name = name;
		this.score = score;
	}

	public String toString() {
		return String.format("%-5s%30s", Integer.toString(score), name);
	}
}