package newhorizon.content;

import arc.graphics.Color;
import mindustry.graphics.Pal;

public class NHColor{
	public static Color 
	lightSky = Color.valueOf("#8DB0FF"),
	darkEnrColor = Pal.sapBullet,
	none = new Color(0, 0, 0, 0),
	thurmixRed = Color.valueOf("#FF9492"), 
	thurmixRedLight = Color.valueOf("#FFCED0"),
	darkEnr = darkEnrColor.cpy().lerp(Color.black, 0.85f);
}










