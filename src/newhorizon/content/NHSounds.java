package newhorizon.content;

import arc.Core;
import arc.assets.AssetDescriptor;
import arc.assets.loaders.SoundLoader;
import arc.audio.Sound;
import mindustry.Vars;

public class NHSounds{
	public static Sound
		alarm = new Sound(),
		hyperspace = new Sound(),
		launch = new Sound(),
		rapidLaser = new Sound(),
		railGunCharge = new Sound(),
		railGunBlast = new Sound();
	
	public static void load(){
		hyperspace = loadSound("hyperspace");
		rapidLaser = loadSound("rapidLaser");
		launch = loadSound("launch");
		railGunBlast = loadSound("railGunBlast");
		railGunCharge = loadSound("railGunCharge");
		alarm = loadSound("alarm");
	}
	
	
	private static Sound loadSound(String soundName){
		if(!Vars.headless){
			String name = "sounds/" + soundName;
			String path = Vars.tree.get(name + ".ogg").exists() ? name + ".ogg" : name + ".mp3";
			
			Sound sound = new Sound();
			
			AssetDescriptor<?> desc = Core.assets.load(path, Sound.class, new SoundLoader.SoundParameter(sound));
			desc.errored = Throwable::printStackTrace;
			return sound;
		}else return new Sound();
	}
}
