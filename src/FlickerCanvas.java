import java.awt.Canvas;
import java.awt.Graphics;
import java.awt.Color;
import java.awt.Rectangle;

public class FlickerCanvas extends Canvas implements Runnable {

	// prob solving part
	final int MAX_Total_steps = 10000;
	final int MAX_Discs = 20;
	final int MAX_Pegs = 20;

	int Discs;
	int Pegs;

	int Total_steps, Curr_step;

	int[] from = new int[MAX_Total_steps];
	int[] to = new int[MAX_Total_steps];

	int[][] stack = new int[MAX_Pegs][MAX_Discs];
	int[] stack_top = new int[MAX_Pegs];

	// animation part
	Rectangle rect = null;// only used for window
	boolean running = false;
	Color foreground;
	Color background;
	Thread looper;

	int[] xCurrent = new int[MAX_Discs];
	int[] yCurrent = new int[MAX_Discs];
	int[] zCurrent = new int[MAX_Discs];
	int[][] DP = new int[MAX_Discs][MAX_Pegs];

	FlickerCanvas() {
		setSize(1000, 300);
	}

	int find_n(int n, int p) {
		int k = 0, i;
		for (i = 0; i < 20; i++) {
			if (DP[i][p + 1] >= n) {
				k = i;
				break;
			}
		}

		if (k <= 1)
			return 1;

		int S = n - DP[k - 1][p + 1];
		int l1 = DP[k - 2][p + 1];
		int r1 = l1 + Math.min(DP[k - 1][p], S);

		int l2 = n - (DP[k - 1][p] + Math.min(DP[k][p - 1], S));
		int r2 = n - DP[k - 1][p];

		int l = Math.max(l1, l2);
		int r = Math.min(r1, r2);

		return (l + r) / 2;

	}

	void multi_peg(int n, int p, int s, int d) {
		int pi = 0, i, j;
		if (n == 0)
			return;
		if (n == 1) {
			from[Total_steps] = s - 1;
			to[Total_steps] = d - 1;

			Total_steps++;

			stack_top[d]++;
			stack[d][stack_top[d]] = stack[s][stack_top[s]];
			stack[s][stack_top[s]] = 0;
			stack_top[s]--;

		} else {
			for (i = 1; i <= Pegs; i++) {
				if (i == s)
					continue;
				if (stack[i][stack_top[i]] <= stack[s][stack_top[s]]) {
					if (i == d)
						continue;
					pi = i;
					break;
				}
			}
			multi_peg(find_n(n, p), p, s, pi);
			multi_peg(n - find_n(n, p), p - 1, s, d);
			multi_peg(find_n(n, p), p, pi, d);
		}

	}

	void init(int n, int p) {
		background = Color.lightGray;
		foreground = Color.black;

		int i, j;
		Discs = n;
		Pegs = p;

		for (i = 0; i < MAX_Discs; i++)
			for (j = 3; j < MAX_Pegs; j++) {
				if (i == 0 || j == 3)
					DP[i][j] = 1;
				else
					DP[i][j] = DP[i - 1][j] + DP[i][j - 1];
			}

		for (i = 1; i <= Discs; i++)
			stack[1][i] = i;
		for (i = 0; i <= Pegs; i++)
			stack_top[i] = 0;
		stack_top[1] = Discs;
		multi_peg(Discs, Pegs, 1, Pegs);

		for (i = 0; i < Pegs; i++)
			stack_top[i] = 0;
		for (i = 0; i < Discs; i++)
			stack[0][stack_top[0]++] = i;// 0 mane sobche boro guti

		// System.out.println(Total_steps);

	}

	void start() {
		if (!running) {
			running = true;
			looper = new Thread(this);
			looper.start();
		}
	}

	void stop() {
		running = false;
	}

	void dumb(int xx, int yy, int zz) throws InterruptedException {

		int i, j, id;
		id = 0;
		for (i = 0; i < Pegs; i++)
			for (j = 0; j < stack_top[i]; j++) {

				// System.out.println(id+" "+stack[i][j]);

				xCurrent[id] = 100 * i;
				yCurrent[id] = 10 * (Discs - 1 - j);
				zCurrent[id] = stack[i][j];
				id++;

			}

		// System.out.println("asdf");

		xCurrent[Discs - 1] = xx;
		yCurrent[Discs - 1] = yy;
		zCurrent[Discs - 1] = zz;

		repaint();
		looper.sleep(3);
	}

	public void run() {
		try {

			int f, t, id, x, y, X, Y, xx, yy, zz;

			for (Curr_step = 0; Curr_step < Total_steps; Curr_step++) {

				f = from[Curr_step];
				t = to[Curr_step];

				stack_top[f]--;
				zz = stack[f][stack_top[f]];

				x = 100 * f;// fromx
				y = 10 * (Discs - 1 - stack_top[f]);// fromy

				X = 100 * t;// tox
				Y = 10 * (Discs - 1 - stack_top[t]);// toy

				// uppere jaitese
				xx = x;
				for (yy = y; yy >= 0; yy--)
					dumb(xx, yy, zz);
				y = 0;

				// slide kon dike korbe ta thik kortese
				if (X >= x)
					id = 1;
				else
					id = -1;

				// slide
				yy = 0;
				for (xx = x; xx != X; xx += id)
					dumb(xx, yy, zz);
				dumb(xx, yy, zz);
				x = X;

				// down e ese portese
				for (yy = 0; yy <= Y; yy++)
					dumb(xx, yy, zz);

				stack[t][stack_top[t]] = stack[f][stack_top[f]];
				stack_top[t]++;

			}

		} catch (InterruptedException e) {
			running = false;
		}
	}

	public void update(Graphics g) {
		int i;
		if (!getBounds().equals(rect)) {
			rect = getBounds();
		}
		if (running)
			paint(g);
	}

	public void paint(Graphics g) {
		if (rect == null)
			return;
		g.setColor(background);
		g.fillRect(0, 0, rect.width, rect.height);

		int i;

		for (i = 0; i < Discs; i++) {
			g.setColor(foreground);
			g.fillRect(xCurrent[i] + 3 * zCurrent[i], yCurrent[i], 100 - 3 * 2 * zCurrent[i], 10);
		}

	}
}
