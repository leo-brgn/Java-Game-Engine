package entities;

import java.util.LinkedList;

import org.lwjgl.util.vector.Vector3f;

import models.TexturedModel;

public class Sphere extends Entity{

	public Sphere(int resolution, float radius, Vector3f position, float rotX, float rotY, float rotZ, float scale) {
		super(null, position, rotX, rotY, rotZ, scale);
		setModel(createSphereModel(resolution, radius));
	}
	
	public TexturedModel createSphereModel(int resolution, float radius) {
		LinkedList<Vector3f> vertices = new LinkedList<Vector3f>();
		
		return null;
	}



	

}
