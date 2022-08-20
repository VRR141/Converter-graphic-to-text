package ru.netology.graphics.image;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.io.IOException;
import java.net.URL;

public class Converter implements TextGraphicsConverter {

    private int[] temp = new int[3];
    private int maxHeight = 200;
    private int maxWidth = 200;
    private double maxRatio = 2;
    private TextColorSchema schema = new Schema();

    @Override
    public String convert(String url) throws IOException, BadImageSizeException {
        BufferedImage img = ImageIO.read(new URL(url));

        checkRatio(img);

        BufferedImage bwImg = scaleToDark(img);

        WritableRaster bwRaster = bwImg.getRaster();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < bwImg.getHeight(); i++){
            for (int j = 0; j < bwImg.getWidth(); j++) {
                int color = bwRaster.getPixel(j, i, temp)[0];
                char c = schema.convert(color);
                sb.append(c).append(c).append(c);
            }
            sb.append("\n");
        }
        return sb.toString();
    }

    private BufferedImage scaleToDark(BufferedImage img){
        int realHeight = img.getHeight();
        int realWidth = img.getWidth();
        BufferedImage bwImg;

        if (realHeight > maxHeight || realWidth > maxWidth) {
            double temp = Math.max((double) realHeight/maxHeight, (double) realWidth/maxWidth);
            realHeight = (int) (realHeight / temp);
            realWidth = (int) (realHeight / temp);

            bwImg = new BufferedImage(realWidth, realHeight, BufferedImage.TYPE_BYTE_GRAY);
            Image scaled = img.getScaledInstance(realWidth, realHeight, BufferedImage.SCALE_SMOOTH);
            Graphics2D graphics = bwImg.createGraphics();
            graphics.drawImage(scaled, 0, 0, null);
            //System.out.println("SCALE:" + " h: " +realHeight + " w: " + realWidth);
        } else {
            bwImg = new BufferedImage(realWidth, realHeight, BufferedImage.TYPE_BYTE_GRAY);
            Graphics2D graphics = bwImg.createGraphics();
            graphics.drawImage(img, 0, 0, null);
            //System.out.println("NOT SCALE:" + " h: " +realHeight + " w: " + realWidth);
        }
        return bwImg;
    }

    @Override
    public void setMaxWidth(int width) {
        maxWidth = width;
    }

    @Override
    public void setMaxHeight(int height) {
        maxHeight = height;
    }

    @Override
    public void setMaxRatio(double maxRatio) {
        this.maxRatio = maxRatio;
    }

    @Override
    public void setTextColorSchema(TextColorSchema schema) {
        this.schema = schema;
    }

    private void checkRatio(BufferedImage img) throws BadImageSizeException {
        if (maxRatio != 0) {
            double tempRatio = ((double) img.getWidth() / img.getHeight());
            if ((tempRatio - maxRatio) > 0.01) {
                throw new BadImageSizeException(tempRatio, maxRatio);
            }
        }
    }
}
