//
// Ghöüstbusteirs™
// They're back in town
// You need to pee?
// That is so crazy
// What the fuck?
// You need to pee?
// You call the Ghöüstbusteirs™
// They extract it
// There was ghosts
// In your bladder
// What the fuck?
// Yaaah!
//
//
// You need to take the pee
// Crazy ghost pee-pee
// That is insanity!
//
//
// Ghöüstbusteirs™
// Use their machine
// To hook it up
// To your dick
// What?
// It hurts
// When they activate it to remove ghosts
// What?
//

package com.catsofwar.ittyblitty;

import java.awt.image.BufferedImage;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class PicMap
{
	protected ByteBuffer data;
	protected int wide, tall;

	public PicMap (int wide, int tall)
	{
		if (wide <= 0 || tall <= 0)
			throw new RuntimeException("Pic dimension <= 0! oops!");
		
		this.wide = wide;
		this.tall = tall;
		this.data = Util.createbb((wide*tall)<<2);
	}

	public PicMap (BufferedImage from)
	{
		this(from.getWidth(), from.getHeight());
		
		final var pixels = from.getRGB(0, 0, wide, tall, null, 0, wide);
		final var ord = data.order();
		data.order(ByteOrder.BIG_ENDIAN);
		for (int pix: pixels)
		{
			data.putInt(((pix & 0xFFFFFF) << 8) | ((pix >>> 24) & 0xFF));
		}
		data.flip().order(ord);
	}

	public int getWide ()
	{
		return wide;
	}

	public int getTall ()
	{
		return tall;
	}

	public int getCount ()
	{
		return wide*tall;
	}

	public ByteBuffer getData ()
	{
		return data;
	}
	
	public PicMap setPixel (int i, int r, int g, int b, int a)
	{
		i = i << 2;
		data
		.put(i,   (byte)r)
		.put(i+1, (byte)g)
		.put(i+2, (byte)b)
		.put(i+3, (byte)a);
		return this;
	}
	
	public PicMap setPixel (int x, int y, int r, int g, int b, int a)
	{
		return setPixel(xytoi(x, y), r, g, b, a);
	}

	public PicMap setPixelARGB (int i, int c)
	{
		return setPixel(
			i,
			Col.get_r(c),
			Col.get_g(c),
			Col.get_b(c),
			Col.get_a(c)
		);
	}
	
	public PicMap setPixelARGB (int x, int y, int c)
	{
		return setPixelARGB(xytoi(x, y), c);
	}

	public int getPixelARGB (int i)
	{
		i = i << 2;
		return Col.make_rgba(
			data.get(i),
			data.get(i+1),
			data.get(i+2),
			data.get(i+3)
		);
	}

	public int getPixelARGB (int x, int y)
	{
		return getPixelARGB(xytoi(x, y));
	}

	int xytoi (int x, int y)
	{
		return y*wide+x;
	}

	public PicMap copyArea (int srcX, int srcY, int srcW, int srcH, int ofsX, int ofsY)
	{
		Util.mvpx(this, srcX, srcY, srcW, srcH, this, srcX+ofsX, srcY+ofsY);
		return this;
	}

	public PicMap paste (PicMap src, int atx, int aty)
	{
		Util.mvpx(src, 0, 0, src.wide, src.tall, this, atx, aty);
		return this;
	}
	
	public PicMap dupliArea (int subX, int subY, int subW, int subH)
	{
		final var outs = new PicMap(subW, subH);
		Util.mvpx(this, subX, subY, subW, subH, outs, 0, 0);
		return outs;
	}

	public boolean pointfIn (float x, float y)
	{
		return 0 <= x && x < wide && 0 <= y && y < tall;
	}

	public boolean pointIn (int x, int y)
	{
		return 0 <= x && x < wide && 0 <= y && y < tall;
	}

}
