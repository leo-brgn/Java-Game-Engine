package engineTester;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector3f;

import entities.Camera;
import entities.Entity;
import entities.Light;
import entities.Player;
import models.RawModel;
import models.TexturedModel;
import objConverter.ModelData;
import objConverter.OBJFileLoader;
import renderEngine.DisplayManager;
import renderEngine.Loader;
import renderEngine.MasterRenderer;
import renderEngine.OBJLoader;
import terrains.Terrain;
import textures.ModelTexture;
import textures.TerrainTexture;
import textures.TerrainTexturePack;

public class MainGameLoop {
	
	public static void main(String[] args) {
		
		//***************************************  DISPLAY  ***************************************
		
		DisplayManager.createDisplay();
		Loader loader = new Loader();
		
		//***************************************  MODELS OBJ  ***************************************
		
		// Tree
		ModelData modelData = OBJFileLoader.loadOBJ("tree");
		RawModel model = loader.loadToVAO(modelData.getVertices(), modelData.getTextureCoords(), 
				modelData.getNormals(), modelData.getIndices());
		
		TexturedModel staticModel = new TexturedModel(model, new ModelTexture(loader.loadTexture("tree")));
		// Grass
		TexturedModel grass = new TexturedModel(OBJLoader.loadObjModel("lowPolyTree", loader),
				new ModelTexture(loader.loadTexture("lowPolyTree")));
		// Fern
		TexturedModel fern = new TexturedModel(OBJLoader.loadObjModel("fern", loader),
				new ModelTexture(loader.loadTexture("fern")));
		fern.getTexture().setHasTransparency(true);
		// Bunny 
		TexturedModel playerModel = new TexturedModel(OBJLoader.loadObjModel("person", loader),
				new ModelTexture(loader.loadTexture("playerTexture")));
		
		//***************************************  TEXTURE PACK  ***************************************
		
		TerrainTexture backgroundTexture = new TerrainTexture(loader.loadTexture("grassy"));
		TerrainTexture rTexture = new TerrainTexture(loader.loadTexture("dirt"));
		TerrainTexture gTexture = new TerrainTexture(loader.loadTexture("pinkFlowers"));
		TerrainTexture bTexture = new TerrainTexture(loader.loadTexture("path"));
		TerrainTexture blendMap = new TerrainTexture(loader.loadTexture("blendMap"));
		TerrainTexturePack texturePack = new TerrainTexturePack(backgroundTexture,rTexture,gTexture,bTexture);
		
		//***************************************  TERRAIN  ***************************************
		
		Terrain terrain = new Terrain(0, -1, loader, texturePack, blendMap, "heightMap");
		
		//*************************************** ENTITIES  ***************************************

		List<Entity> entities = new ArrayList<Entity>();
		Random random = new Random();
		for(int i =0; i<500; i++) {
			if(i % 20 == 0) {
				float x = random.nextFloat() * 800 - 400;
				float z = random.nextFloat() * - 600;
				float y = terrain.getHeightOfTerrain(x, z);
				entities.add(new Entity(fern, 
						new Vector3f(x, y, z),0, random.nextFloat()*360, 0, 0.9f));
			}
			if (i % 5 == 0) {
				float x = random.nextFloat() * 800 - 400;
				float z = random.nextFloat() * - 600;
				float y = terrain.getHeightOfTerrain(x, z);
				entities.add(new Entity(grass, 
						new Vector3f(x, y, z),0,random.nextFloat() * 360,0, random.nextFloat() * 0.1f + 0.6f));
				x = random.nextFloat() * 800 - 400;
				z = random.nextFloat() * - 600;
				y = terrain.getHeightOfTerrain(x, z);
				entities.add(new Entity(staticModel, 
						new Vector3f(x, y, z),0, random.nextFloat() * 360 ,0, random.nextFloat() * 1f + 4));
			}
		}
		
		/*List<Terrain> terrains = new ArrayList<Terrain>();
		for(int i=-1; i<2; i++) {
			terrains.add(new Terrain(i, -1, loader, texturePack, blendMap, "heightMap"));
		}*/
		
	
		Light light = new Light(new Vector3f(3000,2000,2000), new Vector3f(1,1,1));

		MasterRenderer renderer = new MasterRenderer();
		
		
		Player player = new Player(playerModel, new Vector3f(100,0,-50), 0,0,0,1);
		Camera camera = new Camera(player);
		
		
		
		while(!Display.isCloseRequested()) {

			renderer.processEntity(player);
			player.move(terrain);
			camera.move();
			
			renderer.processTerrain(terrain);

			for(Entity entity:entities) {
				renderer.processEntity(entity);
			}
			
			renderer.render(light, camera);
			DisplayManager.updateDisplay();
		}
		
		renderer.cleanUp();
		loader.cleanUp();
		DisplayManager.closeDisplay();
	}
}
