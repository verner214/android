import com.google.gson.Gson;

//tagitrakt av från https://en.wikipedia.org/wiki/Gson

public class GsonTest {

	public static void main(String[] args) {
	
		Gson gson = new Gson();
		String json = "{\"name\":\"John\",\"surname\":\"Doe\",\"cars\":[{\"manufacturer\":\"Audi\",\"model\":\"A4\",\"capacity\":1.8,\"accident\":false},{\"manufacturer\":\"Škoda\",\"model\":\"Octavia\",\"capacity\":2.0,\"accident\":true}],\"phone\":245987453}";
		Person johnDoe = gson.fromJson(json, Person.class);
		System.out.println(johnDoe.toString());
	}
}
