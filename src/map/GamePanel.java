package map;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Iterator;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import game.innovation.ConditionalDate;
import game.innovation.Innovation;
import game.territory.Realm;
import util.CDate;

public class GamePanel extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7961725523154149209L;

	JLabel picLabel;

	JButton menuEra;
	JButton menuMultiplayer;
	JButton menuStart;

	public float zoom = 1;
	public float zoom_dif = 1;
	public float cameraSpeed = 20;

	public static Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

	static Dimension gd = Toolkit.getDefaultToolkit().getScreenSize();
	static int width = (int) gd.getWidth();
	static int height = (int) gd.getHeight();
	static double width_scale = (width / height) / (screenSize.getWidth() / screenSize.getHeight());

	static BufferedImage myPicture;

	boolean test = false;

	public float cameraX = 0;
	public float cameraY = 0;

	public int mouseX;
	public int mouseY;

	public boolean KEY_W;
	public boolean KEY_A;
	public boolean KEY_S;
	public boolean KEY_D;

	public boolean KEY_E;

	public boolean display_state;
	public Realm display_state__realm;
	public boolean display_debug;

	public boolean display_innovation_tree;

	public static GameState gameState = GameState.MENU;

	World world;

	JPanel jpanel = new JPanel() {

		protected int x = 0;
		protected int y = 0;

		protected void center(Graphics g, String str) {
			g.drawString(str, x - g.getFontMetrics().stringWidth(str) / 2, y);
			// y - g.getFontMetrics().getHeight() / 2
			y += g.getFontMetrics().getHeight();
		}

		protected void left(Graphics g, String str) {
			g.drawString(str, x, y);
			y += g.getFontMetrics().getHeight();
		}

		protected void skip(Graphics g) {
			y += g.getFontMetrics().getHeight();
		}

		protected void set(int x, int y) {
			this.x = x;
			this.y = y;
		}

		protected void indent(int x) {
			this.x += x;
		}

		protected void dedent(int x) {
			this.x -= x;
		}

		protected void setX(int x) {
			this.x = x;
		}

		protected void focusCenterWidth() {
			x = width / 2;
		}

		protected void focusCenterHeight() {
			y = height / 2;
		}

		protected void focusFractionWidth(double frac) {
			x = (int) (width * frac);
		}

		protected void focusFractionHeight(double frac) {
			y = (int) (height * frac);
		}

		protected void paintComponent(Graphics g) {
			super.paintComponent(g);

			if (gameState == GameState.MENU) {
				focusCenterWidth();
				focusFractionHeight(0.2);
				center(g, "500BC");
				menuEra.setVisible(true);
				menuMultiplayer.setVisible(true);
				menuStart.setVisible(true);
			} else if (gameState == GameState.MAP) {
				menuEra.setVisible(false);
				menuMultiplayer.setVisible(false);
				menuStart.setVisible(false);

				int offsetX, offsetY;
				offsetX = -(int) (zoom * width) / 2 + width / 2;
				offsetY = -(int) (zoom * height) / 2 + height / 2;
				offsetX += cameraX * zoom; // d / width_scale;
				offsetY += cameraY * zoom;
				g.drawImage(myPicture, offsetX, offsetY, (int) (zoom * width / width_scale), (int) (zoom * height),
						null);

				int transX = (int) (((mouseX - width / 2) / zoom) - cameraX);
				int transY = (int) (((mouseY - height / 2) / zoom) - cameraY);
				String str = "Pos: (" + transX + "," + transY + ")";

				Font font = new Font("Arial", Font.PLAIN, 30);
				g.setFont(font);
				FontMetrics metrics = g.getFontMetrics(font);
				int h = metrics.getHeight();
				int adv = metrics.stringWidth(str);

				g.drawString(str, width - adv, height - h);
				str = "Date: " + World.date.getDay() + "." + World.date.getMonth() + "." + World.date.getYear();
				adv = metrics.stringWidth(str);
				g.drawString(str, width - adv, height - h * 2);

				Color gui_bg = new Color(0, 0, 0, 200);
				Color gui_text_title = new Color(255, 255, 255, 255);

				if (display_state) {
					g.setColor(gui_bg);
					g.fillRect(0, 0, width / 5, height);

					g.setColor(gui_text_title);
					set(width / 10, height / 10);
					center(g, display_state__realm.getName());

					setX(width / 80);
					left(g, display_state__realm.getGovernment().getTypeName());
					left(g, display_state__realm.getTerritories().size() + "/"
							+ display_state__realm.getTotalTerritoryCount() + " Territories");
					left(g, display_state__realm.getChildren().size() + " Substates");
					left(g, Math.round(display_state__realm.getTaxIncome() * 100) / 100 + "/month, "
							+ Math.round(display_state__realm.getTreasury() * 100) / 100 + " total");
				}

				if (display_debug) {
					g.setColor(gui_bg);
					g.fillRect((int) (width * 0.4), 0, (int) (width * 0.2), (int) (height * 0.15));
					g.setColor(gui_text_title);
					set((int) (width * 0.425), (int) (height * 0.025));
					left(g, world.territories.length + " Territories");
					left(g, world.realms.size() + " Realms");
					left(g, world.DEBUG_LIST.size() + " debug list size");
				}

				if (display_innovation_tree) {
					g.setColor(gui_bg);
					g.fillRect((int) (width * 0.1), (int) (height * 0.1), (int) (width * 0.8), (int) (height * 0.8));
					g.setColor(gui_text_title);
					set((int) (width * 0.15), (int) (height * 0.15));
					int resetX = x;
					int rowY;
					for (Innovation[] level : Innovation.getLevels()) {
						for (Innovation inno : level) {
							rowY = y;
							left(g, inno.name());

							ConditionalDate<Territory> cd = inno.getDefaultUnlock();
							CDate date = cd == null ? null : cd.check(World.territories[0]);

							left(g, String.valueOf(date == null ? "---" : date.getYear()));

							left(g, String.valueOf(inno.getProgressMult()));
							y = rowY;
							x += 300;
						}
						x = resetX;
						y += 150;
					}
				}
			}
		};
	};

	public GamePanel(World world) {
		this.world = world;

		System.out.println(width + "," + height);
		setExtendedState(JFrame.MAXIMIZED_BOTH);
		setUndecorated(true);

		add(jpanel);
		menuEra = new JButton("Select era");
		menuEra.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				gameState = GameState.ERA;
			}
		});
		menuEra.setVisible(false);
		jpanel.add(menuEra);
		World.beginDate = new CDate();
		World.beginDate.set(-250000, 1, 1);
		World.date = World.beginDate;

		menuMultiplayer = new JButton("Multiplayer");
		menuMultiplayer.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				gameState = GameState.MULTIPLAYER;
			}
		});
		menuMultiplayer.setVisible(false);
		jpanel.add(menuMultiplayer);

		menuStart = new JButton("Start");
		menuStart.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				gameState = GameState.MAP;
			}
		});
		menuStart.setVisible(false);
		jpanel.add(menuStart);

		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setVisible(true);

		addMouseWheelListener(new MouseWheelListener() {

			@Override
			public void mouseWheelMoved(MouseWheelEvent e) {
				if (e.getWheelRotation() < 0) {
					zoom_dif *= 1.1;
				} else if (e.getWheelRotation() > 0) {
					zoom_dif *= 1 / 1.1;
				}

			}
		});

		addMouseMotionListener(new MouseMotionListener() {

			@Override
			public void mouseMoved(MouseEvent e) {
				mouseX = e.getX();
				mouseY = e.getY();
			}

			@Override
			public void mouseDragged(MouseEvent e) {
				// TODO Auto-generated method stub

			}
		});

		addKeyListener(new KeyListener() {

			@Override
			public void keyTyped(KeyEvent e) {
				System.out.println("type");
			}

			@Override
			public void keyReleased(KeyEvent e) {
				System.out.println("release");
				if (e.getKeyCode() == KeyEvent.VK_W) {
					KEY_W = false;
				} else if (e.getKeyCode() == KeyEvent.VK_A) {
					KEY_A = false;
				} else if (e.getKeyCode() == KeyEvent.VK_S) {
					KEY_S = false;
				} else if (e.getKeyCode() == KeyEvent.VK_D) {
					KEY_D = false;
				}
			}

			@Override
			public void keyPressed(KeyEvent e) {
				if (gameState == GameState.MAP) {
					if (e.getKeyCode() == KeyEvent.VK_W) {
						KEY_W = true;
					} else if (e.getKeyCode() == KeyEvent.VK_A) {
						KEY_A = true;
					} else if (e.getKeyCode() == KeyEvent.VK_S) {
						KEY_S = true;
					} else if (e.getKeyCode() == KeyEvent.VK_D) {
						KEY_D = true;
					} else if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
						if (display_state) {
							display_state = false;
						} else if (display_innovation_tree) {
							display_innovation_tree = false;
						}
					} else if (e.getKeyCode() == 130) {
						display_debug = !display_debug;
					} else if (e.getKeyCode() == 48) {
						new Thread(() -> World.polishWorld(200)).start();
					} else if (e.getKeyCode() == KeyEvent.VK_9) {
						World.populateWorld();
					} else if (e.getKeyCode() == KeyEvent.VK_E) {
						World.DEBUG_LIST.add(World.getTerritoryAt(translatedX(), translatedY()));
					} else if (e.getKeyCode() == KeyEvent.VK_R) {
						World.dumpDebugList();
						if (World.DEBUG_LIST.size() > 0) {
							Iterator<Territory> it = World.DEBUG_LIST.iterator();
							Territory main = it.next();
							Territory tmp;
							while (it.hasNext()) {
								tmp = it.next();
								World.merge(tmp, tmp.getRealm(), main.getTopLevelRealm());
							}
						}
						World.DEBUG_LIST.clear();
					} else if (e.getKeyCode() == KeyEvent.VK_I) {
						display_innovation_tree = !display_innovation_tree;
					}

					else if (e.getKeyCode() == 520) {
						World.runDebugMethod();
					} else if (e.getKeyCode() == 67) {
						cameraX = 0;
						cameraY = 0;
					}
				} else if (gameState == GameState.ERA || gameState == GameState.MULTIPLAYER) {
					if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
						gameState = GameState.MENU;
					}
				}

				// System.out.println(e.getKeyCode());
			}
		});

		addMouseListener(new MouseListener() {

			@Override
			public void mouseReleased(MouseEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mousePressed(MouseEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mouseExited(MouseEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mouseEntered(MouseEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mouseClicked(MouseEvent e) {
				if (gameState == GameState.MAP) {
					// System.out.println(width_scale);
					double transX = (((mouseX - width / 2) / zoom) - cameraX) / width;
					double transY = (((mouseY - height / 2) / zoom) - cameraY) / height;
					// System.out.println(World.translateCoordsToID(transX, transY));
					// System.out.println(World.getTopRealm(transX, transY));
					display_state__realm = World.getTopRealm(transX, transY);
					display_state = true;
					// System.out.println(World.getTerritoryAt(transX, transY));
				}
			}
		});

		menuEra.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {

			}
		});

		setFocusable(true);
	}

	BufferedImage resizeImage(BufferedImage originalImage, int targetWidth, int targetHeight) throws IOException {
		BufferedImage resizedImage = new BufferedImage(targetWidth, targetHeight, BufferedImage.TYPE_INT_RGB);
		Graphics2D graphics2D = resizedImage.createGraphics();
		graphics2D.drawImage(originalImage, 0, 0, targetWidth, targetHeight, null);
		graphics2D.dispose();
		return resizedImage;
	}

	int i = 0;

	public void gameLoop() {
		if (KEY_W) {
			cameraY += 1 / zoom * cameraSpeed;
		}
		if (KEY_A) {
			cameraX += 1 / zoom * cameraSpeed;
		}
		if (KEY_S) {
			cameraY -= 1 / zoom * cameraSpeed;
		}
		if (KEY_D) {
			cameraX -= 1 / zoom * cameraSpeed;
		}

		// System.out.println("b: " + cameraX + "," + cameraY);
		int transX = (int) (((mouseX - width / 2) / zoom) - cameraX);
		int transY = (int) (((mouseY - height / 2) / zoom) - cameraY);
		// cameraX += transX*(1-1/zoom_dif); // pls fix
		// cameraY += transY*(1-1/zoom_dif); // pls fix
		zoom = zoom * zoom_dif;

		// System.out.println("a: " + cameraX + "," + cameraY);

		zoom_dif = 1;

	}

	@Override
	public void paint(Graphics g) {
		super.paint(g);

		// g.drawImage(myPicture, 0, 0, (int) (width * zoom), (int) (height *
		// zoom), null);
		// g.drawImage(myPicture, 0, 0, null);
		// System.out.println("call " + i);

	}

	private double translatedX() {
		return (((mouseX - width / 2) / zoom) - cameraX) / width;
	}

	private double translatedY() {
		return (((mouseY - height / 2) / zoom) - cameraY) / height;
	}
}
