import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class Block extends JPanel {

    private Color mainColor;

    public Block(Color color) {
        this.mainColor = color;
        setPreferredSize(new Dimension(60, 60));
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        int width = getWidth();
        int height = getHeight();
        int borderThickness = 2;

        // 繪製外層（稍微淺一些的顏色）
        Color outerColor = mainColor.brighter();
        g2d.setColor(outerColor);
        g2d.fillRect(borderThickness, borderThickness, width - 2 * borderThickness, height - 2 * borderThickness);

        // 繪製外層邊框（可選）
        g2d.setColor(outerColor.darker());
        g2d.drawRect(borderThickness - 1, borderThickness - 1, width - 2 * borderThickness + 1, height - 2 * borderThickness + 1);

        // 計算內部方塊的邊長和起始位置
        int innerSide = Math.min(width, height) / 2;
        int innerX = width / 2 - innerSide / 2;
        int innerY = height / 2 - innerSide / 2;

        // 繪製中間（原始顏色）
        g2d.setColor(mainColor);
        g2d.fillRect(innerX, innerY, innerSide, innerSide);

        // 繪製中間部分的邊框（可選）
        g2d.setColor(mainColor.darker());
        g2d.drawRect(innerX - 1, innerY - 1, innerSide + 1, innerSide + 1);
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Block Example");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(null);

        // 使用一個更飽和的初始顏色
        Block block = new Block(new Color(255, 120, 0)); // 更飽和的橘色
        block.setBounds(10, 10, 80, 80);
        frame.add(block);

        frame.setSize(200, 200);
        frame.setVisible(true);
    }
}