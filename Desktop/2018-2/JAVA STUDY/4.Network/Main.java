
public class Main {
	static String nickname;    
	
	public static void main(String[] args) {
		int player  = (int) (Math.random()*10000+1);
		nickname = Integer.toString(player);
 
		System.out.println(nickname);
		new Frame(nickname);
	}  

}