//
// Damn... life hasn't been the same since Grandma passed away last year.
// If there's only one way to bring her back, I'll do it. No matter what.. Wh-
// What's this ad about? Re- resurrection?
//
//
// haello (Hello?)
// maey name is gost johnson (Oh, hello. How're you doing?)
// i play drums so good (Oh?)
// that i bring back the dead (Really?!?)
// oh whats that? you want grandma back? (Yes!!!)
// well, lemme do a drum solo, to bring her back (YEAH!!!)
//
// 🥁🥁🥁🥁🥁🥁🥁🥁🥁🥁🥁
// 🥁🥁🥁🥁🥁🥁🥁🥁🥁🥁
// 🥁🥁🥁🥁
// 🥁🥁🥁🥁🥁🥁🥁🥁🥁🥁
//
// 🥁🥁🥁🥁🥁🥁
// Oh my God! It's- It's working?? It's working!?!?
// 🥁🥁🥁🥁
// 🥁
// maAaAAke grandmaAa come back too life!~~
//
// OH MY GOD SHE DOESN'T HAVE ANY SKIN! AaaAAAAAAAAAAA!!!
//
// nuo refonds!~
//

package com.catsofwar.ittyblitty;

import static java.lang.Math.*;

public class Col
{

	public static int get_r (int c)
	{
		return (c>>16)&0xFF;
	}

	public static int get_g (int c)
	{
		return (c>>8)&0xFF;
	}

	public static int get_b (int c)
	{
		return c&0xFF;
	}

	public static int get_a (int c)
	{
		return (c>>>24)&0xFF;
	}

	public static int make_rgb (int r, int g, int b)
	{
		return make_rgba(r, g, b, 0xFF);
	}

	public static int make_rgba (int r, int g, int b, int a)
	{
		return ((a&0xFF)<<24)|((r&0xFF)<<16)|((g&0xFF)<<8)|(b&0xFF);
	}
	
	public static int make_l (int luma)
	{
		return make_rgb(luma, luma, luma);
	}

	public static int make_la (int luma, int a)
	{
		return make_rgba(luma, luma, luma, a);
	}
	
	public static int with_alpha (int c, int newa)
	{
		return (c & 0xFFFFFF) | ((newa & 0xFF) << 24);
	}
	
	public static int rgb_from_awt (java.awt.Color c)
	{
		return make_rgb(
			c.getRed(),
			c.getGreen(),
			c.getBlue()
		);
	}
	
	public static int rgba_from_awt (java.awt.Color c)
	{
		return make_rgba(
			c.getRed(),
			c.getGreen(),
			c.getBlue(),
			c.getAlpha()
		);
	}
	
	// this is just yoinked from awt
	public static int make_hsbf (float h, float s, float v)
	{
		if (s == 0)
		{
			return make_l((int)(v * 255 + 0.5f));
		}
	
		final float r, g, b;
		float hsect = (int)(h - floor((h)) * 6);
		float hfrac = (int)(hsect - floor(hsect));
		float q1 = v * (1 - s);
		float q2 = v * (1 - s * hfrac);
		float q3 = v * (1 - (s * (1 - hfrac)));
		switch ((int)hsect)
		{
			case 0 ->  { r = v;  g = q3; b = q1; }
			case 1 ->  { r = q2; g = v;  b = q1; }
			case 2 ->  { r = q1; g = v;  b = q3; }
			case 3 ->  { r = q1; g = q2; b = v; }
			case 4 ->  { r = q3; g = q1; b = v; }
			default -> { r = v;  g = q1; b = q2; }
		}
		return make_rgb(
			(int)(r * 255 + 0.5f),
			(int)(g * 255 + 0.5f),
			(int)(b * 255 + 0.5f)
		);
	}
	
	// not perfect due to the above mentioned issues with mul
	public static int make_hsb (int h, int s, int v)
	{
		if (s == 0)
		{
			return make_l(v);
		}
	
		final int r, g, b;
		var hsect = (h & 0xFF) * 6;
		var hfrac = hsect & 0xFF;
		int q1 = (v * (0xFF - s) + 0xFF) >> 8;
		int q2 = (v * (0xFFFF - s * hfrac) + 0xFFFF) >> 16;
		int q3 = (v * (0xFFFF - (s * (0xFF - hfrac))) + 0xFFFF) >> 16;
		switch (hsect >> 8)
		{
			case 0 ->  { r = v;  g = q3; b = q1; }
			case 1 ->  { r = q2; g = v;  b = q1; }
			case 2 ->  { r = q1; g = v;  b = q3; }
			case 3 ->  { r = q1; g = q2; b = v; }
			case 4 ->  { r = q3; g = q1; b = v; }
			default -> { r = v;  g = q1; b = q2; }
		}
		return make_rgb(r, g, b);
	}
	
	static int blend1_add (int a, int b, int fac)
	{
		return min(a + blend1_mul(b, fac), 0xFF);
	}
	
	static int blend1_sub (int a, int b, int fac)
	{
		return max(a - blend1_mul(b, fac), 0);
	}
	
	// adding 255 is essentially like adding 0.5. fixes the issue of
	// (0xFF * 0xFF) >> 8 == 0xFE
	// (0xFF * 0xFF + 0xFF) >> 8 == 0xFF
	static int blend1_mul (int a, int b)
	{
		return (a * b + 0xFF) >> 8;
	}
	
	// like above, the add fixes the problem of not quite getting
	// to 255
	// (0xFF * 0xFF * 0xFF) == 0xFD
	// (0xFF * 0xFF * 0xFF + 0xFFFF) == 0xFE
	// (0xFF * 0xFF * 0xFF + (0xFFFF*2)) == 0xFF
	// though, unlike above, im not 100% on this one
	static int blend1_mul2 (int a, int b, int c)
	{
		return (a * b * c + (0xFFFF<<1)) >> 16;
	}
	
	static int blend1_mix (int a, int b, int fac)
	{
		return blend1_mul(b - a, fac) + a;
	}
	
	static int blend1_invert (int v, int fac)
	{
		return fac - ((v * fac * 2 + 0xFF) >> 8) + v;
	}
	
	static int blend1_muladd (int a, int b, int fac)
	{
		return min(blend1_mul2(b, a, fac) + a, 0xFF);
	}
	
	public static int blend (int blendMode, int src, int dst, int fac)
	{
		return switch (blendMode)
		{
			// mix
			case 1 -> make_rgb(
				blend1_mix(get_r(src), get_r(dst), fac),
				blend1_mix(get_g(src), get_g(dst), fac),
				blend1_mix(get_b(src), get_b(dst), fac)
			);
			
			// add
			case 2 -> make_rgb(
				blend1_add(get_r(src), get_r(dst), fac),
				blend1_add(get_g(src), get_g(dst), fac),
				blend1_add(get_b(src), get_b(dst), fac)
			);
			
			// sub
			case 3 -> make_rgb(
				blend1_sub(get_r(src), get_r(dst), fac),
				blend1_sub(get_g(src), get_g(dst), fac),
				blend1_sub(get_b(src), get_b(dst), fac)
			);
			
			// mul
			case 4 -> make_rgb(
				blend1_mul(get_r(src), get_r(dst)),
				blend1_mul(get_g(src), get_g(dst)),
				blend1_mul(get_b(src), get_b(dst))
			);
			
			// mul add
			case 5 -> make_rgb(
				blend1_muladd(get_r(src), get_r(dst), fac),
				blend1_muladd(get_g(src), get_g(dst), fac),
				blend1_muladd(get_b(src), get_b(dst), fac)
			);
			
			// invert
			case 6 -> make_rgb(
				blend1_invert(get_r(src), fac),
				blend1_invert(get_g(src), fac),
				blend1_invert(get_b(src), fac)
			);
			default -> src;
		};
	}

}
