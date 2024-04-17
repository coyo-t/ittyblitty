//
// Grandma's died!
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
// When Grandma has died
// You get really pissed 'cause she owe you money
// So you call Ghöüstbusteir™
// "Hey, please, Ghöüstbusteir™
// Hey, my grandma has died
// She owe me money, extract soul please."
// They tell you, "Who the fuck is this?"
// You tell them, "Get my grandma's money now, you dumbass!"
// Ah!
//
//
// The Ghöüstbusteirs™ still come
// Because you still pay them
// They take out their guns and... get Grandma
// You tell Grandma, "GIMME YOUR MONEY!"
// Ghöüstbusteir™ get guilty and they leave
// And you cry

package com.catsofwar.ittyblitty;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

class Util
{
	static void mvpx (
		PicMap src, int sx, int sy, int sw, int sh,
		PicMap dst, int dx, int dy)
	{
		var si = src.xytoi(sx, sy) << 2;
		var di = dst.xytoi(dx, dy) << 2;
		
		var end = dst.xytoi(dx+sw-1, dy+sh-1) << 2;
		
		final var copysz = sw << 2;
		final var sstride = src.wide << 2;
		final var dstride = dst.wide << 2;
		
		final var sdat = src.data;
		final var ddat = dst.data;
		
		while (di <= end)
		{
			ddat.put(di, sdat, si, copysz);
			si += sstride;
			di += dstride;
		}
	}
	
	static ByteBuffer createbb (int sz)
	{
		return ByteBuffer.allocateDirect(sz).order(ByteOrder.nativeOrder());
	}
}
