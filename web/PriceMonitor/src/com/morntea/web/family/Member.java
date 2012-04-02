package com.morntea.web.family;

import java.util.Date;
import java.util.List;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

@PersistenceCapable(identityType = IdentityType.APPLICATION, detachable="true")
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
    private String motherName;
        
    @Persistent
    private boolean gender;
    
    @Persistent
    private int generation;
    
    @Persistent
    private Date birthday;

    @Persistent
    private String phone;
    
    @Persistent
    private String address;
    
    @Persistent
    private Date lunarBirthday;
    
    @Persistent
    private Date deathday;
    
    @Persistent
    private String comment;
    
    private List<Long> descendants;

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

	public void setSpouseId(Long spouseId) {
		this.spouseId = spouseId;
	}

	public Long getSpouseId() {
		return spouseId;
	}

    public void setMotherName(String mother) {
        this.motherName = mother;
    }

    public String getMotherName() {
        return motherName;
    }

    public void setGeneration(int generation) {
        this.generation = generation;
    }

    public int getGeneration() {
        return generation;
    }

    public void setLunarBirthday(Date lunarBirthday) {
        this.lunarBirthday = lunarBirthday;
    }

    public Date getLunarBirthday() {
        return lunarBirthday;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAddress() {
        return address;
    }

    public void setDescendants(List<Long> descendants) {
        this.descendants = descendants;
    }

    public List<Long> getDescendants() {
        return descendants;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPhone() {
        return phone;
    }

}
