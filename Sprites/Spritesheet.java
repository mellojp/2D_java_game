package Sprites;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;

public class Spritesheet {
	public static BufferedImage spritesheet;
	public static BufferedImage[] player_Mfront, player_Ifront, player_Mback, player_Iback, enemy_back,enemy_front,bullet;
    private static Map<String, BufferedImage[]> animations = new HashMap<>();
	
	
	public Spritesheet(String path) {
		try {
			spritesheet = ImageIO.read(new File(path));
		} catch (IOException e) {
			e.printStackTrace();
		}
        

		player_Mfront= new BufferedImage[5];
        player_Ifront= new BufferedImage[2];
        player_Ifront = generateFrames(0, 0, 64, 64, 2);
        player_Mfront = generateFrames(64,0,64,64,5);       
        animations.put("player_Mfront", player_Mfront);
        animations.put("player_Ifront",player_Ifront);

        player_Mback= new BufferedImage[5];
        player_Iback= new BufferedImage[2];
        player_Mback = generateFrames(64, 64, 64, 64, 5);
        player_Iback = generateFrames(0,64,64,64,2);       
        animations.put("player_Mback", player_Mback);
        animations.put("player_Iback",player_Iback);
        
        enemy_back = new BufferedImage[4];
        enemy_front = new BufferedImage[4];
        enemy_back = generateFrames(128, 128, 64, 64, 4);
        enemy_front = generateFrames(128, 192, 64, 64, 4);
        animations.put("enemy_front", enemy_front);
        animations.put("enemy_back",enemy_back);

        bullet = new BufferedImage[4];
        bullet = generateFrames(465, 5, 5, 5, 4);
        animations.put("bullet",bullet);
        
        BufferedImage[] defaultAnim = new BufferedImage[1];
        defaultAnim[0] = getFrames("player_Ifront")[0]; 
        animations.put("default", defaultAnim);
       
    }
    public BufferedImage[] generateFrames(int x , int y, int width , int height, int number){
        BufferedImage[] frames = new BufferedImage[number];
        for(int i = 0; i < number; i++){
            frames[i] = Spritesheet.getSprite(x+(width*i),y,width,height);
        }
        return frames;
    }

    public static BufferedImage getSprite(int x , int y, int width , int height){
        return spritesheet.getSubimage(x, y, width, height);
    }
    public BufferedImage[] getFrames(String key){
        return animations.get(key);
    }
		
}
