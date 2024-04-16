`BufferedImage` kinda suxzor so i made this

it doesnt have much rn

my liscence for using is uhhhhhh Gost Bost er.

i encourage you to just shove this in your project and modify it to hell and back. cuz i do that

fuck package managers

example (bad) (also not in java its kotlin but shut up):
```kt
private fun pasteSpr2 (dst: PicMap, src: AtlasSprite)
{
	val reg = src.srcCo
	dst.paste(src.firstPic, reg.x, reg.y)

	val margin = src.margin
	if (margin == 0 || src.invisibleMargin)
		return

	// This Is Stupid.
	val sw = src.wide
	val sh = src.tall
	val x0 = reg.x
	val y0 = reg.y
	val x1 = x0+sw-1
	val y1 = y0+sh-1

	for (i in 0..<margin)
	{
		dst.copyArea(x0, y0, 1, sh, -i-1, 0)
		dst.copyArea(x1, y0, 1, sh, +i+1, 0)
	}

	val xm = x0 - margin
	val xw = sw + (margin shl 2)
	for (i in 0..<margin)
	{
		dst.copyArea(xm, y0, xw, 1, 0, -i-1)
		dst.copyArea(xm, y1, xw, 1, 0, +i+1)
	}
}
```
