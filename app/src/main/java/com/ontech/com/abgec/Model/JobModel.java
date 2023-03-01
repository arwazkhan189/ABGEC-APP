package com.ontech.com.abgec.Model;

public class JobModel {

    private String uid;
    private String number;
    private String company;
    private String jobTitle;
    private String imageLink;
    private String jobId;
    private String joblocation;
    private String jobType;
    private String jobFunction;
    private String jobMode;
    private String salary;
    private String experience;

    private String pushkey;
    private String url;

    public JobModel() {
    }

    public JobModel(String uid, String number, String company, String jobTitle, String imageLink, String jobId, String joblocation, String jobType, String jobFunction, String jobMode, String salary, String experience, String pushkey, String url) {
        this.uid = uid;
        this.number = number;
        this.company = company;
        this.jobTitle = jobTitle;
        this.imageLink = imageLink;
        this.jobId = jobId;
        this.joblocation = joblocation;
        this.jobType = jobType;
        this.jobFunction = jobFunction;
        this.jobMode = jobMode;
        this.salary = salary;
        this.experience = experience;
        this.pushkey = pushkey;
        this.url = url;
    }

    public String getImageLink() {
        return imageLink;
    }

    public String getUid() {
        return uid;
    }

    public String getNumber() {
        return number;
    }

    public String getCompany() {
        return company;
    }

    public String getJobTitle() {
        return jobTitle;
    }

    public String getJobId() {
        return jobId;
    }

    public String getPushkey() {
        return pushkey;
    }

    public String getUrl() {
        return url;
    }

    public String getJoblocation() {
        return joblocation;
    }

    public String getJobType() {
        return jobType;
    }

    public String getJobFunction() {
        return jobFunction;
    }

    public String getJobMode() {
        return jobMode;
    }

    public String getSalary() {
        return salary;
    }

    public String getExperience() {
        return experience;
    }
}
