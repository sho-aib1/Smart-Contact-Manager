package com.smart.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "CONTACT")
public class Contact {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int cid;
	private String name;
	private String nickName;
	private String work;
	private String email;
	private String phno;
	private String img;
	@Column(length = 50000)
	private String description;

	@ManyToOne
	private User user;

	public Contact(int cid, String name, String nickName, String work, String email, String phno, String img,
			String description, User user) {
		this.cid = cid;
		this.name = name;
		this.nickName = nickName;
		this.work = work;
		this.email = email;
		this.phno = phno;
		this.img = img;
		this.description = description;
		this.user = user;
	}

	public Contact() {
		super();
	}

	public int getCid() {
		return cid;
	}

	public void setCid(int cid) {
		this.cid = cid;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	public String getWork() {
		return work;
	}

	public void setWork(String work) {
		this.work = work;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhno() {
		return phno;
	}

	public void setPhno(String phno) {
		this.phno = phno;
	}

	public String getImg() {
		return img;
	}

	public void setImg(String img) {
		this.img = img;
	}

	@Override
	public boolean equals(Object obj) {
		// TODO Auto-generated method stub
		return this.cid == ((Contact) obj).getCid();
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

//    @Override
//    public String toString() {
//        return "Contact [cid=" + cid + ", name=" + name + ", nickName=" + nickName + ", work=" + work + ", email="
//                + email + ", phno=" + phno + ", img=" + img + ", description=" + description + ", user=" + user + "]";
//    }

}
