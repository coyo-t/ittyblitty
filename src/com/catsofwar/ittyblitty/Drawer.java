//
// AHHHHHH the ghost
// The ghost!
// Call the Ghostbuster
// The ghost is stealing my money!
// He punched me in the face!
// And now he run off with my money!
// Hey! Ghost, cum back, that's my money!
//

package com.catsofwar.ittyblitty;

import static java.lang.Math.*;

public class Drawer
{
	public int blend_mode = 0;
	public int bM = 0;
	
	PicMap surface;

	public Drawer (PicMap surface)
	{
		this.surface = surface;
	}

	public int getWide ()
	{
		return surface.getWide();
	}

	public int getTall ()
	{
		return surface.getTall();
	}

	public Drawer clear (int clearColour)
	{
		for (int i = surface.getCount(); (--i) >= 0;)
		{
			surface.setPixelARGB(i, clearColour);
		}
		return this;
	}

	public Drawer line (float x0, float y0, float x1, float y1, int colour)
	{
		final int steps;
		float xstep = x1 - x0;
		float ystep = y1 - y0;
		final var absx = abs(xstep);
		final var absy = abs(ystep);
		if (absx >= absy)
		{
			steps = (int)absx;
			if (xstep != 0)
			{
				ystep /= absx;
			}
			xstep = xstep < 0 ? -1 : 1;
		}
		else
		{
			steps = (int)absy;
			if (ystep != 0)
			{
				xstep /= absy;
			}
			ystep = ystep < 0 ? -1 : 1;
		}
		
		for (int i = 0; i <= steps; i++)
		{
			if (surface.pointfIn(x0, y0))
			{
				int addr = surface.xytoi((int)x0, (int)y0);
				surface.setPixelARGB(
					addr,
					colour
//					blend_mode == 0
//						? colour
//						: colour_blend(blend_mode, surface.getPixelARGB(addr), colour, colour_get_a(colour))
				);
			}
			x0 += xstep;
			y0 += ystep;
		}
		return this;
	}

	public Drawer hline (int x0, int x1, int y, int colour)
	{
		if (!pointInVertical(y))
			return this;
		
		if (x1 < x0)
		{
			final var temp = x0;
			x0 = x1;
			x1 = temp;
		}
		
		x0 = max(x0, 0);
		x1 = min(x1, getWide());
		
		final var lwide = x1 - x0;
		
		if (lwide <= 0)
			return this;
		
		var i = surface.xytoi(x0, y);
		final var end = surface.xytoi(x1, y);
		
		for (; i <= end; i++)
		{
			surface.setPixelARGB(i, colour);
		}
		
		return this;
	}

	public Drawer vline (int x, int y0, int y1, int colour)
	{
		if (!pointInHorizontal(x))
			return this;
		
		if (y1 < y0)
		{
			final var temp = y0;
			y0 = y1;
			y1 = temp;
		}
		
		y0 = max(y0, 0);
		y1 = min(y1, getTall());
		
		final var ltall = y1 - y0;
		
		if (ltall <= 0)
			return this;
		
		var i = surface.xytoi(x, y0);
		final var end = surface.xytoi(x, y1);
		final var stride = getWide();
		for (; i <= end; i += stride)
		{
			surface.setPixelARGB(i, colour);
		}
		
		return this;
	}

	public Drawer rectangle_wire (int x, int y, int wide, int tall, int colour)
	{
		final var x1 = x+wide-1;
		final var y1 = y+tall-1;
		
		hline(x, x1, y,  colour);
		hline(x, x1, y1, colour);
		vline(x, y+1, y1-1, colour);
		vline(x1, y+1, y1-1, colour);
		return this;
	}

	public Drawer rectangle_wire_centred (int x, int y, int wide, int tall, int colour)
	{
		return rectangle_wire(x - (wide>>1), y - (tall>>1), wide, tall, colour);
	}

	public Drawer rectangle (float x, float y, float wide, float tall, int colour)
	{
		int dx = max((int)x, 0);
		int dy = max((int)y, 0);
		int dwide = min((int)(x + wide), getWide());
		int dtall = min((int)(y + tall), getTall());
		int addr = surface.xytoi(dx, dy);
//		int alpha = colour_get_a(colour);
		int ystride = this.getWide() - (dwide - dx);
		for (; dy < dtall; dy++, addr += ystride)
		{
			for (int xx = dx; xx < dwide; xx++, addr++)
			{
				surface.setPixelARGB(
					addr,
					colour
//					alpha == 255
//						? colour
//						: colour_blend(blend_mode, surface.getPixelARGB(addr), colour, alpha)
				);
			}
		}
		return this;
	}

	public Drawer rectangle_centred (float x, float y, float wide, float tall, int colour)
	{
		return rectangle(x - wide / 2, y - tall / 2, wide, tall, colour);
	}

	public Drawer sprite_part (
		PicMap sprite,
		int x,
		int y,
		int wide,
		int tall,
		int src_left,
		int src_top,
		int src_wide,
		int src_tall)
	{
		var xo = (x >= 0) ? 0 : -x;
		var yo = (y >= 0) ? 0 : -y;
		final var x1 = (x + wide <= getWide()) ? wide : wide - (x + wide - getWide());
		final var y1 = (y + tall <= getTall()) ? tall : tall - (y + tall - getTall());
		for (; yo < y1; yo++)
		{
			int daddr = surface.xytoi(x+xo, y+yo);
			int saddr = (src_top + yo * src_tall / tall) * sprite.wide + src_left;
			
			for (int xx = xo; xx < x1; xx++, daddr++)
			{
				final var pix = sprite.getPixelARGB(saddr + xx * src_wide / wide);
				if (pix != 0)
				{
					surface.setPixelARGB(daddr, pix);
				}
			}
		}
		return this;
	}

	public Drawer sprite_part (
		PicMap sprite,
		int x,
		int y,
		int wide,
		int tall,
		int srcLeft,
		int srcTop,
		int srcWide,
		int srcTall,
		int tint)
	{
		var dx = (x >= 0) ? 0 : -x;
		var dy = (y >= 0) ? 0 : -y;
		final var dwide = (x + wide <= getWide()) ? wide : wide - (x + wide - getWide());
		final var dtall = (y + tall <= getTall()) ? tall : tall - (y + tall - getTall());
		final var atint = Col.get_a(tint);
		final var rtint = Col.get_r(tint);
		final var gtint = Col.get_g(tint);
		final var btint = Col.get_b(tint);
		switch (bM)
		{
			case 0 ->
			{
				for (; dy < dtall; dy = dy + 1)
				{
					int daddr = surface.xytoi(x+dx, y+dy);
					int saddr = (srcTop + dy * srcTall / tall) * sprite.wide + srcLeft;
					
					for (int xx = dx; xx < dwide; xx++, daddr++)
					{
						final var pix = sprite.getPixelARGB(saddr + xx * srcWide / wide);
						if (pix != 0)
						{
							int out;
							int rr = (rtint * Col.get_r(pix)) >> 8;
							int gg = (gtint * Col.get_g(pix)) >> 8;
							int bb = (btint * Col.get_b(pix)) >> 8;
							if (blend_mode == 0)
							{
								out = Col.make_rgb(rr, gg, bb);
							}
							else
							{
								out = Col.blend(blend_mode, surface.getPixelARGB(daddr), Col.make_rgb(rr, gg, bb), atint);
							}
							surface.setPixelARGB(daddr, out);
						}
					}
				}
			}
			case 1 ->
			{
				for (; dy < dtall; dy = dy + 1)
				{
					int daddr = surface.xytoi(x+dx, y+dy);
					int saddr = (srcTop + dy * srcTall / tall) * sprite.wide + srcLeft;
					
					for (int xx = dx; xx < dwide; xx++, daddr++)
					{
						final var ablend = Col.blend1_mul(atint, Col.get_a(sprite.getPixelARGB(saddr + xx * srcWide / wide)));
						if (ablend != 0)
						{
							surface.setPixelARGB(daddr, Col.blend(blend_mode, surface.getPixelARGB(daddr), tint, ablend));
						}
					}
				}
			}
			case 2 ->
			{
				for (; dy < dtall; dy = dy + 1)
				{
					int daddr = surface.xytoi(x+dx, y+dy);
					int saddr = (srcTop + dy * srcTall / tall) * sprite.wide + srcLeft;
					
					for (int xx = dx; xx < dwide; xx++, daddr++)
					{
						final var pix = sprite.getPixelARGB(saddr + xx * srcWide / wide);
						if (pix != 0xff000000)
						{
							int outp;
							final var rr = Col.get_r(pix);
							final var gg = Col.get_g(pix);
							final var bb = Col.get_b(pix);
							if (rr == gg && gg == bb)
							{
								outp = Col.make_rgb(
									Col.blend1_mul(rr, rtint),
									Col.blend1_mul(gg, gtint),
									Col.blend1_mul(bb, btint)
								);
							}
							else
							{
								outp = Col.with_alpha(pix, 0xFF);
							}
							surface.setPixelARGB(daddr, outp);
							
						}
					}
				}
			}
		}
		return this;
	}

	public Drawer fill_replace (int x, int y, int wide, int tall, int cReplace, int cWith)
	{
		var dx = max(x, 0);
		var dy = max(y, 0);
		var dwide = min(x + wide, getWide());
		var dtall = min(y + tall, getTall());
		var addr = surface.xytoi(dx, dy);
		final var ystride = getWide() - (dwide - dx);
		for (; dy < dtall; dy++)
		{
			for (int xx = dx; xx < dwide; xx++)
			{
				if (surface.getPixelARGB(addr) == cReplace)
				{
					surface.setPixelARGB(addr, cWith);
				}
				addr++;
			}
			addr += ystride;
		}
		return this;
	}
	
	public Drawer sprite_replace_colour (
		PicMap sprite,
		int x,
		int y,
		int wide,
		int tall,
		int srcLeft,
		int srcTop,
		int srcWide,
		int srcTall,
		int cReplace,
		int cWith)
	{
		
		var i10 = (x >= 0) ? 0 : -x;
		var yy = (y >= 0) ? 0 : -y;
		var i12 = (x + wide <= getWide())
			? wide
			: wide - (x + wide - getWide());
		
		var i13 = (y + tall <= getTall())
			? tall
			: tall - (y + tall - getTall());
		
		for (; yy < i13; yy = yy + 1)
		{
			var daddr = surface.xytoi(x+i10, y+yy);
			var saddr = (srcTop + yy * srcTall / tall) * sprite.wide + srcLeft;
			
			for (var xx = i10; xx < i12; xx++, daddr++)
			{
				if (sprite.getPixelARGB(saddr + xx * srcWide / wide) == cReplace)
				{
					surface.setPixelARGB(daddr, cWith);
				}
			}
		}
		return this;
	}

	public boolean pointInHorizontal (int x)
	{
		return 0 <= x && x < getWide();
	}
	
	public boolean pointInVertical (int y)
	{
		return 0 <= y && y < getTall();
	}

	public boolean pointIn (int x, int y)
	{
		return pointInHorizontal(x) && pointInVertical(y);
	}

}
