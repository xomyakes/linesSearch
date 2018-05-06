import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import java.util.Scanner;

public class Main {
	
	static final int maxColorsCount = 50;
	static int colorsCount = 0;
	static final int maxLinesCount=50;
	static int linesCount=0;
	
/* Программа расчитана на поиск линий толщиной 1 пиксель на белом фоне. 
 * Рассмотрен простейший случай, когда линии ориентированы строго вертикально,
 * либо горизонтально, линии могут быть разных цветов,
 * линии разных цветов могут соприкасаться*/
	
	public static void main(String args[]) throws IOException
	{
		//Считываем дирректорию файла
		Scanner in = new Scanner(System.in);
		System.out.println("Введите путь полное имя директории ");
		String directory = in.nextLine();
		
		// Записываем файл в изображение
		BufferedImage img = ImageIO.read(new File(directory, "image.png"));
		
		//Создаём массив проверенных пикселей
		boolean[] searched = new boolean[img.getHeight()*img.getWidth()];
		
		//Создаём массив найденных цветов
		 int[] colors = new int[maxColorsCount]; 
		 
		 //Создаём массив длин линий
		 int[] lines = new int[maxLinesCount];
		
		//Ищем линии
		SearchLines(img,searched,lines,colors);
		
		// Выводим длины линий
		System.out.println("Количество линий " + linesCount);
		int sum=0;
		for (int i:lines)
			sum+=i;
		System.out.println("Общая длинна линий равна " + sum);
				
		for (int i = 0; i< linesCount;i++)
			System.out.println("Длина " + i + " линии равна " + lines[i]);
				
	}
	// Поиск линии
	static void SearchLines(BufferedImage img, boolean[] searched,int[] lines, int[] colors)
	{
		for (int y = 0; y < img.getHeight();y++)
			for (int x = 0; x < img.getHeight();x++)
			{
					if (searched[y*img.getWidth()+x]==false)
					{
						int p;
						p = PixelColor(img,searched,colors,x,y);
						if (p!=-1)
						{
							lines[linesCount]=1;
							SearchNeighbours(img,searched,lines,colors,x,y);
							linesCount++;
						}
					}
			}
	}
	// Поиск остальных пикселей линии
	static void SearchNeighbours(BufferedImage img, boolean[] searched, int[] lines, int[] colors, int x, int y)
	{
		// Проверка пикселя справа
		if (x+1<img.getWidth())
		{
			if(PixelColor(img,searched,colors,x,y) == PixelColor(img,searched,colors,x+1,y))
			{
				lines[linesCount]++;
				SearchNeighbours(img,searched,lines,colors,++x,y);				
			}
			else if(PixelColor(img,searched,colors,x+1,y) != -1)
			{
				searched[y*img.getWidth()+x+1] = false;
			}
		}

		
		
		//Проверка пикселя слева
		if (y+1<img.getHeight())
		{
			if (PixelColor(img,searched,colors,x,y) == PixelColor(img,searched,colors,x,y+1))
			{
				lines[linesCount]++;
				SearchNeighbours(img,searched,lines,colors,x,++y);
			}
			else if (PixelColor(img,searched,colors,x,y+1)!=-1)
			{
				searched[(y+1)*img.getWidth()+x] = false;
			}
		}
	
	}
	static int PixelColor(BufferedImage img,boolean[] searched, int[] colors, int x, int y)
	{
		int argb = img.getRGB(x, y);
		//int alpha = (argb >> 24) & 0xff;
		int red = (argb >> 16) & 0xff;
		int green = (argb >>  8) & 0xff;
		int blue = (argb ) & 0xff;
		if (red != 255 || green !=255 || blue != 255)
		{
			boolean newColor = true;
			for (int i = 0; i< maxColorsCount;i++)
			{
				if (colors[i] == argb)
					newColor = false;			
			}
			if (newColor == true)
			{
				colors[colorsCount] = argb;
				colorsCount++;
			}	
		}
		searched[y*img.getWidth()+x] = true;
		return argb;
	}
}
