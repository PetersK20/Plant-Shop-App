
public class plantInfo {
private String plantId,miniHeight,maxiHeight,plantType,name,color,imageFile;

public plantInfo(String plantId, String miniHeight, String maxiHeight, String plantType, String name, String color,
		String imageFile) {
	this.plantId = plantId;
	this.miniHeight = miniHeight;
	this.maxiHeight = maxiHeight;
	this.plantType = plantType;
	this.name = name;
	this.color = color;
	this.imageFile = imageFile;
}

public String getPlantId() {
	return plantId;
}

public void setPlantId(String plantId) {
	this.plantId = plantId;
}

public String getMiniHeight() {
	return miniHeight;
}

public void setMiniHeight(String miniHeight) {
	this.miniHeight = miniHeight;
}

public String getMaxiHeight() {
	return maxiHeight;
}

public void setMaxiHeight(String maxiHeight) {
	this.maxiHeight = maxiHeight;
}

public String getPlantType() {
	return plantType;
}

public void setPlantType(String plantType) {
	this.plantType = plantType;
}

public String getName() {
	return name;
}

public void setName(String name) {
	this.name = name;
}

public String getColor() {
	return color;
}

public void setColor(String color) {
	this.color = color;
}

public String getImageFile() {
	return imageFile;
}

public void setImageFile(String imageFile) {
	this.imageFile = imageFile;
}
}
