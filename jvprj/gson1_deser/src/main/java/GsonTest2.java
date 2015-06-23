import com.google.gson.Gson;

//lite enklare json och med privat klass.
//denna fil bevisar att
//1. extra jsonattribut som inte finns i javaklasserna struntas i dvs det gör inget.
//2. attribut som tvärtom saknas i jsonsträngen blir nullvärden

public class GsonTest2 {

	private class PClass {
	    private String name;
	    private String saknas;
	    private int phone;
	 
	    private PClass() {
	    }
	 
	    public PClass(String name, String saknas, int phone, int age, Car[] cars) {
	        this.name = name;
			this.saknas = saknas;
	        this.phone = phone;
	    }
	 
	    @Override
	    public String toString() {
	        StringBuilder sb = new StringBuilder();
	        sb.append("Name: " + name + " ");
	        sb.append("Phone: " + phone + "\n");	 
	        sb.append("Saknas: " + saknas + "\n");	 
	        return sb.toString();
	    }
	}

	public static void main(String[] args) {	
		Gson gson = new Gson();
		String json = "{\"name\":\"John\",\"phone\":245987453,\"unknown\":123}";
		PClass johnDoe = gson.fromJson(json, PClass.class);
		System.out.println(johnDoe.toString());
	}
}
