package com.cloudyang.info;


public class HistoryInfo {
	
	private String AllNumber;
	
	private String duplicate;
	
	private String invalid;
	
	private String valid;
	
	private String money;
	
	private String uploadTime;

	public String getAllNumber() {
		return AllNumber;
	}

	public void setAllNumber(String allNumber) {
		AllNumber = allNumber;
	}

	public String getDuplicate() {
		return duplicate;
	}

	public void setDuplicate(String duplicate) {
		this.duplicate = duplicate;
	}

	public String getInvalid() {
		return invalid;
	}

	public void setInvalid(String invalid) {
		this.invalid = invalid;
	}

	public String getValid() {
		return valid;
	}

	public void setValid(String valid) {
		this.valid = valid;
	}

	public String getMoney() {
		return money;
	}

	public void setMoney(String money) {
		this.money = money;
	}
	
	public String getUploadTime(){
		return uploadTime;
	}
	
	public void setUploadTime(String uploadtime){
		this.uploadTime = uploadtime;
	}
	
	/*public String getAllNumber(){
		return AllNumber;
	}
	
	public void setAllNumber(String allnumber){
		this.AllNumber = allnumber;
	}*/

}
