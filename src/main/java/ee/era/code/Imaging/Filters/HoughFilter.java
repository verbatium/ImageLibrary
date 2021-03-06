package ee.era.code.Imaging.Filters;

import sun.awt.image.ByteInterleavedRaster;

import java.awt.*;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Stream;

import static java.lang.Math.cos;
import static java.lang.StrictMath.sin;

/**
 * Created by valeri on 28.02.16.
 */
public class HoughFilter implements BufferedImageOp {

	public static final double GRAD_TO_RAD = Math.PI / 180.0f;
	public static final int GRAD = 360;
	private double treshold;
	private double stepPerGrad;
	private double stepPerPixel;

	public HoughFilter() {
		stepPerGrad = 1.0f;
		stepPerPixel = 1.0f;
		treshold = 0.50f;
	}


	public HoughFilter(double treshold, double stepPerGrad, double stepPerPixel) {
		this.treshold = treshold;
		this.stepPerGrad = stepPerGrad;
		this.stepPerPixel = stepPerPixel;
	}

	public static <K, V extends Comparable<? super V>> Map<K, V>
			sortByValue(Map<K, V> map) {
		Map<K, V> result = new LinkedHashMap<>();
		Stream<Map.Entry<K, V>> st = map.entrySet().stream();

		st.sorted(Comparator.comparing(e -> e.getValue()))
				.forEachOrdered(e -> result.put(e.getKey(), e.getValue()));

		return result;
	}

	public double getStepPerPixel() {
		return stepPerPixel;
	}

	public void setStepPerPixel(int stepPerPixel) {
		this.stepPerPixel = stepPerPixel;
	}

	public double getTreshold() {
		return treshold;
	}

	public void setTreshold(double treshold) {
		this.treshold = treshold;
	}

	public double getStepPerGrad() {
		return stepPerGrad;
	}

	public void setStepPerGrad(int stepPerGrad) {
		this.stepPerGrad = stepPerGrad;
	}

	@Override
	public BufferedImage filter(BufferedImage src, BufferedImage dest) {

		int width = src.getWidth();
		int height = src.getHeight();

		// максимальное расстояние от начала координат - это длина диагонали
		int RMax = (int)Math.round(Math.sqrt(width * width + height * height));

		// картинка для хранения фазового пространства Хафа (r, f)
		// 0 < r < RMax
		// 0 < f < 2*PI
		BufferedImage phase = new BufferedImage((int) Math.round(RMax * stepPerPixel), (int) Math.round(GRAD * stepPerGrad), BufferedImage.TYPE_BYTE_GRAY);
		int x = 0, y = 0, r = 0, f = 0;
		double theta = 0;
		ByteInterleavedRaster bin = (ByteInterleavedRaster)src.getData();
		byte[] dataStorage = bin.getDataStorage();

		ByteInterleavedRaster phaseData = (ByteInterleavedRaster)phase.getData();
		byte[] dstData = phaseData.getDataStorage();

		int[] phaseMap = new int[dstData.length];
		int max = 0;

		// пробегаемся по пикселям изображения контуров
		for (y = 0; y < height; y++) {
			int ptr = (y * bin.getScanlineStride());
			for (x = 0; x < width; x++) {
				if ((dataStorage[ptr + x] & 0xFF) > 0) { // это пиксель контура?
					// рассмотрим все возможные прямые, которые могут
					// проходить через эту точку

					for (f = 0; f < GRAD * stepPerGrad; f++) { // перебираем все возможные углы наклона

						double grad = (double)f / stepPerGrad;
						theta = GRAD_TO_RAD * grad; // переводим градусы в радианы
						double distance = (sin(theta) * y + cos(theta) * x) * stepPerPixel;

						Integer d = (int)Math.round(distance);
                        Double v = f * RMax * stepPerPixel + d;
						phaseMap[v.intValue()]++; // увеличиваем счетчик для этой точки фазового пространства.c
						max = Math.max(max, phaseMap[v.intValue()]);
					}
				}
			}
		}
		double v = max * treshold;
		for (int i = 0; i < phaseMap.length; i++) {

			if (phaseMap[i] > v)
				dstData[i] = (byte) (phaseMap[i]);
		}

		WritableRaster wr = phaseData.createCompatibleWritableRaster(phase.getWidth(), phase.getHeight());
		wr.setDataElements(0, 0, phase.getWidth(), phase.getHeight(), dstData);
		phase.setData(wr);
		return phase;
	}

	@Override
	public Rectangle2D getBounds2D(BufferedImage dataStorage) {
		return null;
	}

	@Override
	public BufferedImage createCompatibleDestImage(BufferedImage src, ColorModel destCM) {
		return null;
	}

	@Override
	public Point2D getPoint2D(Point2D srcPt, Point2D dstPt) {
		return null;
	}

	@Override
	public RenderingHints getRenderingHints() {
		return null;
	}
}
