
public class DTO {
	String name;
	String id;
	String pw;
	String major;
	String gender;
	int score;
	
	/*원래는 Login프로젝트의 소스들을 참조하여 쓰고 싶었지만 
	 * Login에서 minesweeper 소스들을 참조하는 거는 되는데, Minesweeper 에서 Login소스들을 참조하는 건 
	 * 안되는 것 같아서 minesweeper프로젝트에 DAO, DTO, RankList 소스들을 어쩔 수 없이 복사해서 가져오게 되었다.
	 * Frame 클래스에서 instance화 하여 사용하였는데, minesweeper 안에서 DAO,DTO, RankList의 함수들이 call
	 * 되지 않고 Login프로젝트에 있는 클래스의 함수들이 call이 되어 의문인 점들을 메모해 두고자 했다.
	*/
	public String getId() {
		return id;
	}
	public void setId(String id) {
		System.out.println("setID:"+id);
		this.id = id;
	}
//	public String getPw() {
//		return pw;
//	}
//	public void setPw(String pw) {
//		this.pw = pw;
//	}
//	public String getName() {
//		return name;
//	}
//	public void setName(String name) {
//		this.name = name;
//	}
//	public String getMajor() {
//		return major;
//	}
//	public void setMajor(String major) {
//		this.major = major;
//	}
//	public String getGender() {
//		return gender;
//	}
//	public void setGender(String gender) {
//		this.gender = gender;
//	}
	public int getScore() {
		return score;
	}
	public void setScore(int score) {
		System.out.println("setScore:"+score);
		this.score = score;
	}
}
