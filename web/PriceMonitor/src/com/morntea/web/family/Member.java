package com.morntea.web.family;

import java.util.Date;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

@PersistenceCapable(identityType = IdentityType.APPLICATION)
public class Member {
    @PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
    private Long id;

    @Persistent
    private String name;

    @Persistent
    private Long spouseId;
    
    @Persistent
    private Long fatherId;
    
    @Persistent
    private Long motherId;
        
    @Persistent
    private boolean gender;
    
    @Persistent
    private Date birthday;
    
    @Persistent
    private Date deathday;
    
    @Persistent
    private String comment;

    public Member() {
    }

    public Long getId() {
        return id;
    }

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean isGender() {
		return gender;
	}

	public void setGender(boolean gender) {
		this.gender = gender;
	}

	public Date getBirthday() {
		return birthday;
	}

	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}

	public Date getDeathday() {
		return deathday;
	}

	public void setDeathday(Date deathday) {
		this.deathday = deathday;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public void setFatherId(Long fatherId) {
		this.fatherId = fatherId;
	}

	public Long getFatherId() {
		return fatherId;
	}

	public void setMotherId(Long motherId) {
		this.motherId = motherId;
	}

	public Long getMotherId() {
		return motherId;
	}

	public void setSpouseId(Long spouseId) {
		this.spouseId = spouseId;
	}

	public Long getSpouseId() {
		return spouseId;
	}

}
