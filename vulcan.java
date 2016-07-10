package lottery;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Random;
import java.util.Set;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.LineBorder;

public class Lottery {
	public static void main(String[] args) {
		EventQueue.invokeLater(new MyLottery());
	}
}

class MyLottery implements Runnable {
	
	public String nums = ""; //ºÅÂëÀÛ¼Ó
	public int index = 0; //ºÅÂë¸öÊýË÷Òý
	JTextArea now; //µ±Ç°ºÅÂë
	JTextArea selected; //ÒÑÑ¡ºÅÂë
	FileOutputStream fos; //ÎÄ¼þÊäÈëÁ÷
	
	@Override
	public void run() {
		
		//Ëæ»ú²úÉúÖÐ½±ºÅÂë²¢Ð´ÈëÎÄ¼þ
		Set<Integer> set = new HashSet<Integer>(); //ÓÃ¹þÏ£±í±£´æËæ»úÊý£¬±ÜÃâÖØ¸´
		Random rdm = new Random();
		while (set.size() != 7) {
			set.add(rdm.nextInt(35) + 1); //[0,35) ±äÎª [1,36) Âú×ã1-35Ëæ»úÊý	
		}
		
		//numbersÊý×é±£´æÖÐ½±ºÅÂë²¢ÅÅÐò
		final int[] numbers = new int[7]; 
		Iterator<Integer> iter = set.iterator();
		int ii = 0;
		while (iter.hasNext()) {
			numbers[ii ++] = (int)iter.next();
		}
		Arrays.sort(numbers); //ÅÅÐò
		
		String zhongjiang = "±¾ÆÚ¿ª½±ºÅÂë:";
		for (int i = 0; i < 7; i ++) {
			String temp = Integer.toString(numbers[i]);
			if (temp.length() == 1) temp = "0" + temp;
			if (i == 0)
				zhongjiang += temp;
			else
				zhongjiang += (","+temp);
		}
		zhongjiang += "\r\n";
		try {
			//ÎÄ¼þ±£´æÂ·¾¶
			fos = new FileOutputStream(new File("./src/lottery/lottery.txt"));
			fos.write(zhongjiang.getBytes());
		}catch (Exception e) {}
		
		//Ö÷¿ò¼Ü
		JFrame frame = new JFrame();
		frame.setTitle("Lottery");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLocationByPlatform(true);
		frame.setVisible(true);
		
		//ÉèÖÃ¿ò¼Ü´óÐ¡
		Toolkit kit = Toolkit.getDefaultToolkit();
		Dimension screen = kit.getScreenSize();
		int screenWidth = screen.width;
		int screenHeight = screen.height;
		frame.setBounds(screenWidth/2-150, screenHeight/2-175, 300, 350);
		
		//ÉèÖÃ×é¼þ·Ö²¼¸ñ¾Ö
		frame.setLayout(new GridLayout(0, 1, 0, 0));
		
		//Ìí¼Ó×é¼þ"35Ñ¡7"
		JPanel name = new JPanel();
		name.setBorder(new LineBorder(Color.lightGray, 1, true));
		
		JLabel jtArea = new JLabel();
		Font font = new Font("¿¬Ìå",Font.BOLD,30);
		jtArea.setFont(font);
		jtArea.setText("35Ñ¡7");
		jtArea.setOpaque(false);
			
		name.add(jtArea);
		
		frame.add(name);
		
		//Ìí¼Ó×é¼þ"ÊäÈëºÅÂë"
		JPanel inputJPanel = new JPanel();
		inputJPanel.setLayout(new GridLayout(0, 1, 0, 0));
		inputJPanel.setBorder(new LineBorder(Color.lightGray, 1, true));
		
		JPanel jp1 = new JPanel();
		JTextArea jt1 = new JTextArea("ÊäÈëÑ¡ºÅ:");
		jt1.setOpaque(false);
		jt1.setEditable(false);
		jp1.add(jt1);
		final JTextField num = new JTextField(); //ÊäÈëºÅÂëÇø
		num.setSize(60, 20);
		num.setColumns(4);
		
		//Ã¿ÊäÈëÒ»¸öºÅÂë°´»Ø³µ¼ÇÂ¼
		num.addKeyListener(new KeyListener() {
			Set<Integer> set2 = new HashSet<Integer>();
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyChar() == '\n') {
					
					String text = num.getText();
					if (text.matches("[0-9]+")) {

						int n = Integer.parseInt(text);
						if (n < 1 || n > 35) {
							Toolkit.getDefaultToolkit().beep();
							JOptionPane.showMessageDialog(null, "ÇëÊäÈë1-35ÄÚµÄÕûÊý£¡",
									"ÌáÊ¾", JOptionPane.WARNING_MESSAGE);
						}else {
							if (!set2.add(n)) {
								Toolkit.getDefaultToolkit().beep();
								JOptionPane.showMessageDialog(null, "ºÅÂë"+n+"ÒÑÑ¡Ôñ,ÇëÊäÈëÆäËûºÅÂë£¡",
										"ÌáÊ¾", JOptionPane.WARNING_MESSAGE);
							}
								
							else {
								if (text.length() == 1) 
									text = "0" + text;
								index ++;
								if (index % 7 == 0) {
									nums += text;
									index = 0;
								}
								else
									nums += text + ",";
								num.setText("");
								addNums(nums);
								if (index == 0) {
									nums = "";
									set2.clear();
									Toolkit.getDefaultToolkit().beep();
									JOptionPane.showMessageDialog(null, "ÒÑÑ¡Ôñ7¸öºÅÂë£¬ÇëÌí¼Ó£¡",
											"ÌáÊ¾", JOptionPane.WARNING_MESSAGE);
								}
							}
						}
					}
					else {
						Toolkit.getDefaultToolkit().beep();
						JOptionPane.showMessageDialog(null, "ÇëÊäÈëºÏ·¨Êý×Ö£¡",
								"¾¯¸æ", JOptionPane.WARNING_MESSAGE);
					}
				}
			}
			public void keyTyped(KeyEvent e) {}
			public void keyReleased(KeyEvent e) {}
			
		});
		jp1.add(num);
		
		inputJPanel.add(jp1);
		
		JPanel jp2 = new JPanel();
		
		JTextArea jt2 = new JTextArea("µ±Ç°Ñ¡ºÅ:");
		jt2.setOpaque(false);
		jt2.setEditable(false);
		jp2.add(jt2);
		
		//ºÅÂëÏÔÊ¾Çø
		now = new JTextArea(nums);
		now.setOpaque(false);
		now.setEditable(false);
		jp2.add(now);

		
		inputJPanel.add(jp2);
		
		//°´¼ü
		JPanel jp3 = new JPanel();
		JButton add = new JButton("Ìí¼Ó±¾×éºÅÂë");
		jp3.add(add);
		inputJPanel.add(jp3);
		add.addActionListener(new ActionListener() {	
			@Override
			public void actionPerformed(ActionEvent e) {
				if (now.getText().split(",").length == 7) {
					addData(sort(now.getText()));
					now.setText("");
				}
				else {
					Toolkit.getDefaultToolkit().beep();
					JOptionPane.showMessageDialog(null, "ÇëÊäÈë7¸öºÅÂë£¡",
							"ÌáÊ¾", JOptionPane.WARNING_MESSAGE);
				}
			}
		});
		
		frame.add(inputJPanel);
		
		JPanel result = new JPanel();
		result.setBorder(new LineBorder(Color.lightGray, 1, true));
		result.setLayout(new GridLayout(0, 1, 0, 0));
		
		JPanel checkJPanel = new JPanel();
		checkJPanel.setLayout(new FlowLayout(FlowLayout.CENTER));

		selected = new JTextArea();
		
		selected.setOpaque(false);
		selected.setEditable(false);
		checkJPanel.add(selected);

		result.add(checkJPanel);
		
		JPanel jp4 = new JPanel();
		jp4.setLayout(new FlowLayout(FlowLayout.CENTER));
	
		JButton check = new JButton("ÑéÖ¤²¢±£´æµ½ÎÄ¼þ");
		jp4.add(check);
		result.add(jp4);
		check.addActionListener(new ActionListener() {	
			@Override
			public void actionPerformed(ActionEvent e) {
				String string = selected.getText();
				
				if (!string.equals("")) {
					//½«ÊäÈëµÄºÅÂë°´ÐÐ´æÈëÊý×é£¬ÅÅÐò²¢Æ¥Åä²ÂÖÐµÄ¸öÊý
					String[] strings = string.split("\r\n");
					for (int i = 0; i < strings.length; i ++) {
						String line = strings[i];
						String[] lines = line.split(",");
						int[] ns = new int[7];
						//×ª»»ÎªÕûÊý£¬±£´æµ½Êý×éns
						for (int j = 0; j < 7; j ++)
							ns[j] = Integer.parseInt(lines[j]);
						Arrays.sort(ns); //ÅÅÐò
						int count = getDup(ns, numbers);
						
						line += (" - " + count + "\r\n");
				        try {
				        	fos.write(line.getBytes());
				        }catch (Exception exc) {}
				        
					}
					try {
						//¹Ø±ÕÎÄ¼þ
						fos.close();
					} catch (IOException e1) {}
					
					Toolkit.getDefaultToolkit().beep();
					JOptionPane.showMessageDialog(null, "±£´æ³É¹¦,ÏêÇéÇë¿´lottery.txtÎÄ¼þ", 
							"ÌáÊ¾", JOptionPane.PLAIN_MESSAGE);
				}
				else {
					Toolkit.getDefaultToolkit().beep();
					JOptionPane.showMessageDialog(null, "ÇëÏÈÌí¼ÓÖÁÉÙÒ»×éºÅÂë£¡",
							"ÌáÊ¾", JOptionPane.WARNING_MESSAGE);
				}
			}
		});
		
		frame.add(result);
	}
	
	private void addNums(String s) {
		now.setText(s);
	}
	
	private void addData(String s) {
		selected.append(s + "\r\n");
	}
	
	private int getDup(int[] ns, int[] numbers) {
		int count = 0;
		for (int i = 0; i < ns.length; i ++) {
			for (int j = 0; j < numbers.length; j ++) {
				if (ns[i] == numbers[j]) {
					count ++;
					break;
				}
			}
		}
		return count;
	}
	
	private String sort(String str) {
		String result = "";
		String[] strs = str.split(",");
		int[] nums = new int[strs.length];
		for (int i = 0; i < nums.length; i ++) {
			nums[i] = Integer.parseInt(strs[i]);
		}
		Arrays.sort(nums);
		for (int i = 0; i < nums.length; i ++) {
			String temp = Integer.toString(nums[i]);
			if (temp.length() == 1) temp = "0" + temp;
			if (i == 0)
				result += temp;
			else
				result += ("," + temp);
		}
		return result;
	}
}
